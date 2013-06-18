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

package org.bibsonomy.webapp.util.spring.security.handler;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.TeerGrube;
import org.bibsonomy.webapp.util.spring.security.exceptionmapper.UsernameNotFoundExceptionMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * @author dzo
 * @version $Id: FailureHandler.java,v 1.8 2011-06-15 14:24:40 nosebrain Exp $
 */
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	/**
	 * The user that shall be registered is stored in a session attribute with
	 * that name. 
	 */
	public static final String USER_TO_BE_REGISTERED = "register_this_user"; 
	
	private TeerGrube grube;
	private Set<UsernameNotFoundExceptionMapper> usernameNotFoundExceptionMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		// TODO: remove instanceof chain
		if (exception instanceof BadCredentialsException) {
			/*
			 * log failure
			 * Brute Force Attacks!! 
			 */
			final BadCredentialsException badCredentialsException = (BadCredentialsException) exception;
			final Authentication authentication = badCredentialsException.getAuthentication();
			final String username = (String) authentication.getPrincipal();
			if (present(username)) {
				final RequestLogic requestLogic = new RequestLogic(request);
				
				this.grube.add(username);
				this.grube.add(requestLogic.getInetAddress());
			}
		}
		/*
		 * redirect to registration (LDAP and OpenID)
		 */
		if (exception instanceof UsernameNotFoundException) {
			final UsernameNotFoundException unne = (UsernameNotFoundException) exception;
			/*
			 * Find the correct mapper which handles the specific 
			 * exception (LDAP/OpenID) and "converts" the user data to our 
			 * user object.
			 */
			for (final UsernameNotFoundExceptionMapper mapper : usernameNotFoundExceptionMapper) {
				if (mapper.supports(unne)) {
					/*
					 * store user data and authentication in session
					 */
					final HttpSession session = request.getSession(true);
					session.setAttribute(USER_TO_BE_REGISTERED, mapper.mapToUser(unne));
					/*
					 * redirect to the correct registration page
					 */
					getRedirectStrategy().sendRedirect(request, response, mapper.getRedirectUrl());
					return;
				}
			}
		}
		super.onAuthenticationFailure(request, response, exception);
	}

	/**
	 * @param grube the grube to set
	 */
	public void setGrube(TeerGrube grube) {
		this.grube = grube;
	}

	/**
	 * @param usernameNotFoundExceptionMapper
	 */
	public void setUsernameNotFoundExceptionMapper(Set<UsernameNotFoundExceptionMapper> usernameNotFoundExceptionMapper) {
		this.usernameNotFoundExceptionMapper = usernameNotFoundExceptionMapper;
	}
}
