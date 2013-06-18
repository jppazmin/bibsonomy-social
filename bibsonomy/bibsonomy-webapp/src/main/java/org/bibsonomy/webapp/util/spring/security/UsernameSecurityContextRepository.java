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

package org.bibsonomy.webapp.util.spring.security;

import static org.bibsonomy.util.ValidationUtils.present;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bibsonomy.util.spring.security.UserAdapter;
import org.bibsonomy.webapp.util.spring.security.authentication.SessionAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * implements {@link SecurityContextRepository}
 * - saves the <code>username</code> of the logged in user in the session attribute {@value #ATTRIBUTE_LOGIN_USER_NAME}
 * - loads the security context by retrieving the username from the session and loading the userdetails from the provided
 * UserdetailsSerivce {@link #service}
 * 
 * @author dzo
 * @version $Id: UsernameSecurityContextRepository.java,v 1.12 2011-03-04 16:42:47 folke Exp $
 */
public class UsernameSecurityContextRepository implements SecurityContextRepository {	
	private static final String ATTRIBUTE_LOGIN_USER_NAME = "LOGIN_USER_NAME";

	@Deprecated
	private static final String REQ_ATTRIB_USER = "user";
	
	/**
	 * Delivers details for each given user.
	 */
	private UserDetailsService service;
	private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

	/**
	 * Checks for a user name in the session. If one is found, the corresponding
	 * user details are extracted and the user is stored as request attribute.
	 * 
	 * @see org.springframework.security.web.context.SecurityContextRepository#loadContext(org.springframework.security.web.context.HttpRequestResponseHolder)
	 */
	@Override
	public SecurityContext loadContext(final HttpRequestResponseHolder requestResponseHolder) {
		final HttpServletRequest request = requestResponseHolder.getRequest();
		final SecurityContextImpl securityContext = new SecurityContextImpl();
		
		final String username = getLoginUser(request);
		if (present(username)) {
			/*
			 * user name found in session -> get the corresponding user
			 */
			final UserDetails user = this.service.loadUserByUsername(username);
			final Authentication authentication = new SessionAuthenticationToken(user, user.getAuthorities());
			securityContext.setAuthentication(authentication);

			/*
			 * For backwards compatibility, we add the user
			 * as request attribute (used by old servlets and JSPs).
			 * TODO: remove when all old jsp sites are ported to the new spring system
			 */
			request.setAttribute(REQ_ATTRIB_USER, ((UserAdapter)user).getUser());
		}
		
		return securityContext;
	}
	
	
	/**
	 * Stores the user name in the session.
	 * 
	 * @see org.springframework.security.web.context.SecurityContextRepository#saveContext(org.springframework.security.core.context.SecurityContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void saveContext(final SecurityContext context, final HttpServletRequest request, final HttpServletResponse response) {
		this.setLoginUser(request, context.getAuthentication());
	}

	/**
	 * The name of the logged in user is stored in the session. This method 
	 * extracts the name from the session.
	 * 
	 * @param request
	 * @return
	 */
	private static String getLoginUser(final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		if (session == null) {
			return null;
		}
		
		return (String) session.getAttribute(ATTRIBUTE_LOGIN_USER_NAME);
	}
	
	private void setLoginUser(final HttpServletRequest request, final Authentication authentication) {
		if (this.authenticationTrustResolver.isAnonymous(authentication)) {
            return;
        }
		
		/*
		 * If an authentication is present, we store the user name in the 
		 * session. Note that we /always/ store it - also when it already 
		 * contained in the session (i.e., we don't check for 
		 * !this.containsContext(request)). Thus, the session should time out
		 * after XX minutes of /inactivity/.
		 * 
		 */
		if (present(authentication)) {
			final UserDetails user = (UserDetails) authentication.getPrincipal();
			final String loginUsername = user.getUsername();
			final HttpSession session = request.getSession(true);
			session.setAttribute(ATTRIBUTE_LOGIN_USER_NAME, loginUsername);
		}
	}
	
	@Override
	public boolean containsContext(HttpServletRequest request) {
		return getLoginUser(request) != null;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(UserDetailsService service) {
		this.service = service;
	}

}
