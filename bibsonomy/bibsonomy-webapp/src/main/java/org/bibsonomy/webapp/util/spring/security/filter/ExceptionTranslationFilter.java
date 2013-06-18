/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.util.spring.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.webapp.util.spring.security.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

/**
 * FIXME: code copy but we can't override the handleException method in a proper
 * way :( maybe we should report a bug item to the spring security issue tracker 
 * 
 * @see LoginUrlAuthenticationEntryPoint
 * 
 * @author dzo
 * @version $Id: ExceptionTranslationFilter.java,v 1.1 2011-06-15 14:24:42 nosebrain Exp $
 */
@Deprecated
public class ExceptionTranslationFilter extends GenericFilterBean {

	private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
	private AuthenticationEntryPoint authenticationEntryPoint;
	private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint must be specified");
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		try {
			chain.doFilter(request, response);

			if (logger.isDebugEnabled()) {
				logger.debug("Chain processed normally");
			}
		} catch (final IOException ex) {
			throw ex;
		} catch (final Exception ex) {
			// Try to extract a SpringSecurityException from the stacktrace
			final Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
			RuntimeException ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);

			if (ase == null) {
				ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
			}

			if (ase != null) {
				handleException(request, response, chain, ase);
			} else {
				// Rethrow ServletExceptions and RuntimeExceptions as-is
				if (ex instanceof ServletException) {
					throw (ServletException) ex;
				} else if (ex instanceof RuntimeException) {
					throw (RuntimeException) ex;
				}

				// Wrap other Exceptions. These are not expected to happen
				throw new RuntimeException(ex);
			}
		}
	}
	
	/**
	 * @return the authenticationEntryPoint
	 */
	public AuthenticationEntryPoint getAuthenticationEntryPoint() {
		return authenticationEntryPoint;
	}

	protected AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return authenticationTrustResolver;
	}

	private void handleException(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final RuntimeException exception) throws IOException, ServletException {
		if (exception instanceof AuthenticationException) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication exception occurred; redirecting to authentication entry point", exception);
			}

			sendStartAuthentication(request, response, chain, (AuthenticationException) exception);
		} else if (exception instanceof AccessDeniedException) {
			if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Access is denied (user is anonymous); redirecting to authentication entry point", exception);
				}

				// XXX: here we only added the cause of the
				// InsufficientAuthenticationException
				sendStartAuthentication(request, response, chain, new InsufficientAuthenticationException("Full authentication is required to access this resource", exception));
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Access is denied (user is not anonymous); delegating to AccessDeniedHandler", exception);
				}

				accessDeniedHandler.handle(request, response, (AccessDeniedException) exception);
			}
		}
	}

	protected void sendStartAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final AuthenticationException reason) throws ServletException, IOException {
		// SEC-112: Clear the SecurityContextHolder's Authentication, as the
		// existing Authentication is no longer considered valid
		SecurityContextHolder.getContext().setAuthentication(null);
		requestCache.saveRequest(request, response);
		logger.debug("Calling Authentication entry point.");
		authenticationEntryPoint.commence(request, response, reason);
	}

	public void setAccessDeniedHandler(final AccessDeniedHandler accessDeniedHandler) {
		Assert.notNull(accessDeniedHandler, "AccessDeniedHandler required");
		this.accessDeniedHandler = accessDeniedHandler;
	}

	public void setAuthenticationEntryPoint(final AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	/**
	 * 
	 * @param authenticationTrustResolver
	 */
	public void setAuthenticationTrustResolver(final AuthenticationTrustResolver authenticationTrustResolver) {
		Assert.notNull(authenticationTrustResolver, "authenticationTrustResolver must not be null");
		this.authenticationTrustResolver = authenticationTrustResolver;
	}

	/**
	 * @param throwableAnalyzer
	 *            the throwableAnalyzer to set
	 */
	public void setThrowableAnalyzer(final ThrowableAnalyzer throwableAnalyzer) {
		Assert.notNull(throwableAnalyzer, "throwableAnalyzer must not be null");
		this.throwableAnalyzer = throwableAnalyzer;
	}

	/**
	 * The RequestCache implementation used to store the current request before
	 * starting authentication. Defaults to an {@link HttpSessionRequestCache}.
	 * 
	 * @param requestCache
	 */
	public void setRequestCache(final RequestCache requestCache) {
		Assert.notNull(requestCache, "requestCache cannot be null");
		this.requestCache = requestCache;
	}

	/**
	 * Default implementation of <code>ThrowableAnalyzer</code> which is capable
	 * of also unwrapping <code>ServletException</code>s.
	 */
	private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {
		/**
		 * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
		 */
		@Override
		protected void initExtractorMap() {
			super.initExtractorMap();

			registerExtractor(ServletException.class, new ThrowableCauseExtractor() {
				
				@Override
				public Throwable extractCause(final Throwable throwable) {
					ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
					return ((ServletException) throwable).getRootCause();
				}
			});
		}

	}

}