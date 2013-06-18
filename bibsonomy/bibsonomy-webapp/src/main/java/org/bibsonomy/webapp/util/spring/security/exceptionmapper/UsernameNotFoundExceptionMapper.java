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

package org.bibsonomy.webapp.util.spring.security.exceptionmapper;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.util.spring.security.exceptions.LdapUsernameNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Maps the data from {@link UsernameNotFoundException} subclass onto our 
 * user object.
 * 
 * @author rja
 * @version $Id: UsernameNotFoundExceptionMapper.java,v 1.3 2010-12-08 10:12:00 rja Exp $
 */
public abstract class UsernameNotFoundExceptionMapper {

	private String redirectUrl;
	
	/**
	 * Checks if this mapper can handle the given exception.
	 * 
	 * @param e
	 * @return <code>true</code> if the given exception is a subclass of {@link LdapUsernameNotFoundException}.
	 */
	public abstract boolean supports(final UsernameNotFoundException e);

	/**
	 * Maps the user data from the LDAP/OpenID/whatever server to our user object.
	 * 
	 * @param e
	 * @return A user containing the information from the LDAP server.
	 */
	public abstract User mapToUser(final UsernameNotFoundException e);

	/**
	 * @return The URL to which the user shall be redirected if she/he needs to
	 * register first.
	 */
	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	/**
	 * The URL to which the user shall be redirected if she/he needs to
	 * register first.
	 * 
	 * @param redirectUrl
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	
}