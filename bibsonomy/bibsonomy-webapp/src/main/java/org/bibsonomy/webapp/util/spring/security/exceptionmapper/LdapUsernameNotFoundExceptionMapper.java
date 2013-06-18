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

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.util.spring.security.exceptions.LdapUsernameNotFoundException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author rja
 * @version $Id: LdapUsernameNotFoundExceptionMapper.java,v 1.5 2011-03-28 09:06:27 rja Exp $
 */
public class LdapUsernameNotFoundExceptionMapper extends UsernameNotFoundExceptionMapper {

	@Override
	public boolean supports(final UsernameNotFoundException e) {
		return present(e) && LdapUsernameNotFoundException.class.isAssignableFrom(e.getClass());
	}

	@Override
	public User mapToUser(final UsernameNotFoundException e) {
		final User user = new User();
		if (e instanceof LdapUsernameNotFoundException) {
			final DirContextOperations ctx = ((LdapUsernameNotFoundException) e).getDirContextOperations();

			/*
			 * copy user attributes
			 */
			user.setRealname(ctx.getStringAttribute("givenname") + " " + ctx.getStringAttribute("sn"));
			user.setEmail(ctx.getStringAttribute("mail"));
			user.getSettings().setDefaultLanguage(ctx.getStringAttribute("preferredlanguage"));
			user.setPlace(ctx.getStringAttribute("l")); // location
			user.setLdapId(ctx.getStringAttribute("uid"));

			/*
			 * After successful registration the user is logged in using the
			 * plain text password. Thus, we store it here in the user. 
			 */
			final Object credentials = e.getAuthentication().getCredentials();
			if (credentials instanceof String) {
				user.setPassword((String) credentials);
			}
		}

		return user;
	}

}
