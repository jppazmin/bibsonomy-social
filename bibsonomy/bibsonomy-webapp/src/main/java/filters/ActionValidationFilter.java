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

package filters;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.util.spring.security.AuthenticationUtils;

/**
 * This filter checks the credential of the user and sets a corresponding request 
 * attribute with the result of the check.
 * 
 * Resources in /resources are ignored.
 *
 * @version $Id: ActionValidationFilter.java,v 1.15 2011-03-04 16:42:45 folke Exp $
 */
public class ActionValidationFilter implements Filter {
	private final static Log log = LogFactory.getLog(ActionValidationFilter.class);

	/**
	 * TODO: improve documentation
	 */
	public static final String REQUEST_ATTRIB_VALID_CREDENTIAL = "validckey"; // true or false
	
	/**
	 * TODO: improve documentation
	 */
	public static final String REQUEST_ATTRIB_CREDENTIAL = "ckey"; // current ckey
	
	private static final String REQUEST_PARAM_CREDENTIAL = "ckey"; // ckey from request
	

	protected FilterConfig filterConfig = null;

	/**
	 * the resources (css, js) path
	 */
	public static final String STATIC_RESOURCES = "/resources";

	/**
	 * the api path
	 */
	public static final String API = "/api";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String requPath = httpServletRequest.getServletPath();
		/*
		 * ignore resource files (CSS, JPEG/PNG, JavaScript) ... 
		 */
		if (requPath.startsWith(STATIC_RESOURCES) || requPath.startsWith(API)) {
			chain.doFilter(request, response);
			return;
		} 

		/*
		 * This filter makes only sense, if user is logged in.
		 */
		User user = AuthenticationUtils.getUser();
		if (user != null && user.getName() != null) {
			/*
			 * get sessions credential storage variable
			 */
			String storedCredential = (String) request.getAttribute(REQUEST_ATTRIB_CREDENTIAL);
			/*
			 * if null, create new one
			 */
			if (storedCredential == null) {
				storedCredential = getNewCredential(user, httpServletRequest.getSession());
				request.setAttribute(REQUEST_ATTRIB_CREDENTIAL, storedCredential);
			}
			log.debug("credential for " + user.getName() + " = " + storedCredential);
			
			/*
			 * get credential from request parameter
			 * 
			 * FIXME: This does not work with multipart-requests! Thus, on such
			 * requests we must otherwise send the ckey.
			 */
			String requestCredential = request.getParameter(REQUEST_PARAM_CREDENTIAL);
			/*
			 * check and propagate correctness 
			 */
			request.setAttribute(REQUEST_ATTRIB_VALID_CREDENTIAL, storedCredential.equals(requestCredential));
			
		}

		// Pass control on to the next filter
		chain.doFilter(request, response);

	}
	/** Static method to check validity of the sent credential.
	 * @param request
	 * @return <true> iff ckey is valid
	 */
	public static boolean isValidCkey (final ServletRequest request) {
		final Boolean validCredential = (Boolean) request.getAttribute(REQUEST_ATTRIB_VALID_CREDENTIAL);
		return validCredential != null && validCredential;
	}
	
	/** Creates the user-dependent credential.
	 * 
	 * Currently we use the hashed email-address + session-id of user. Another (better) 
	 * option would be the password + session-id. 
	 * Even better would be separate credentials for each specific action (delete, change 
	 * email, etc.).
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	private String getNewCredential(User user, HttpSession session) {
		return (StringUtils.getMD5Hash(user.getEmail() + session.getId())); 
	}


	/**
	 * Place this filter into service.
	 *
	 * @param filterConfig The filter configuration object
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	/**
	 * Take this filter out of service.
	 */
	@Override
	public void destroy() {
		this.filterConfig = null;
	}
}
