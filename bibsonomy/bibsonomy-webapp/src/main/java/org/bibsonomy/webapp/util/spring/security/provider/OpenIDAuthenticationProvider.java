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

package org.bibsonomy.webapp.util.spring.security.provider;

import org.bibsonomy.webapp.util.spring.security.exceptions.OpenIdUsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * When a user is successfully authenticated using OpenID but we can't find him
 * in our database, we want to send him to a registration form where the fields
 * are filled with his OpenID data.
 * 
 * The only place to add this information is here, because where we find out
 * that the user is not registered, yet, we don't have the OpenID authentication
 * to put it into the exception.
 * 
 * @author rja
 * @version $Id: OpenIDAuthenticationProvider.java,v 1.3 2010-12-08 10:11:56 rja Exp $
 */
public class OpenIDAuthenticationProvider extends org.springframework.security.openid.OpenIDAuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			return super.authenticate(authentication);
		} catch (final OpenIdUsernameNotFoundException e) {
			/*
			 * Add user data to exception and re-throw it. The data is later 
			 * stored in the session and used for filling the registration form. 
			 */
			e.setAuthentication(authentication);
			throw e;
		}
	}
}
