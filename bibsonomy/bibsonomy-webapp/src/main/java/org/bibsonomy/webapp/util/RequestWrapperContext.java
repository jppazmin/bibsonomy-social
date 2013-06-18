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

package org.bibsonomy.webapp.util;

import javax.servlet.http.HttpServletRequest;

import org.bibsonomy.model.User;
import org.bibsonomy.util.spring.security.AuthenticationUtils;

import filters.ActionValidationFilter;

/**
 * <p>
 * Provides access to loginUser, format, ckey, validCkey, etc. by extracting
 * this attributes from the request.
 * <br/>
 * Basically, a wrapper/proxy for the request. 
 * </p>
 * 
 * <p>Using this class is only a <em>workaround</em> to transfer request attributes
 * from the filters {@link ActionValidationFilter} to the command and later to the Views.
 * </p>
 * 
 * @author rja
 * @version $Id: RequestWrapperContext.java,v 1.13 2011-06-16 10:11:39 nosebrain Exp $
 */
public class RequestWrapperContext {

	private HttpServletRequest request;
	
	/** The request this wrapper provides access to.
	 * 
	 * @param request
	 */
	public void setRequest(final HttpServletRequest request) {
		this.request = request;
	}
	
	/** Returns the logged in user.
	 *  
	 * @return An instance of the logged in user.
	 */
	public User getLoginUser() {
		return AuthenticationUtils.getUser();
	}
	
	/**
	 * helper function to ease checking if a logged in user exists
	 * @return <code>true</code> iff a user is logged in, false otherwise
	 */
	public boolean isUserLoggedIn() {
		return this.getLoginUser().getName() != null;
	}
	
	/**
	 * wrapper for function userLoggedIn to enable access from JSPs
	 * @see #isUserLoggedIn()
	 * @return true if a user is logged in, false otherwise 
	 */
	public boolean getUserLoggedIn() {
		return this.isUserLoggedIn();
	}
	
	/**
	 * Delivers a new ckey to be used in forms.
	 * 
	 * @return the ckey. 
	 */
	public String getCkey() {
		return getRequestAttribute(ActionValidationFilter.REQUEST_ATTRIB_CREDENTIAL, String.class);
	}
	
	/** Returns <code>true</code> only, when the user request contained a valid ckey.
	 * 
	 * @return <code>true</code>, when the request contained a valid ckey.
	 */
	public boolean isValidCkey() {
		final Boolean isValidCkey = getRequestAttribute(ActionValidationFilter.REQUEST_ATTRIB_VALID_CREDENTIAL, Boolean.class);
		return isValidCkey != null && isValidCkey;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getRequestAttribute(final String name, @SuppressWarnings("unused") final Class<T> clazz) {
		return (T) request.getAttribute(name);
	}
	
	/**
	 * @return The query string of the request.
	 */
	public String getQueryString() {
		return request.getQueryString();
	}
	
	
}
