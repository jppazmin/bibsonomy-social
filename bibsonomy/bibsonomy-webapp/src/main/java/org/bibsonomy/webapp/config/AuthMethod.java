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

package org.bibsonomy.webapp.config;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.util.StringUtils;

/**
 * Identifier for all supported authentication methods.
 * 
 * @author folke
 * @version $Id: AuthMethod.java,v 1.6 2011-03-16 13:25:02 nosebrain Exp $
 */
public enum AuthMethod {
	
	/**
	 * TODO
	 */
	INTERNAL,
	
	/**
	 * TODO
	 */
	LDAP,
	
	/**
	 * TODO
	 */
	OPENID;
	
	/**
	 * TODO implement x509
	 */
//	X509;
	
    /**
     * Retrieve Method by name
     * 
     * @param name
     *            the requested authentication method (e.g. "OpenId")
     * @return the corresponding Order enum
     * @throws IllegalArgumentException 
     */
	public static AuthMethod getAuthMethodByName(String name) throws IllegalArgumentException {
		if (!present(name)) {
			throw new IllegalArgumentException("No authentication method!");
		}
		try {
			return AuthMethod.valueOf(name.toUpperCase());
		} catch (final IllegalArgumentException ia) {
			throw new IllegalArgumentException("Requested order not supported. Possible values are " + StringUtils.implodeStringArray(AuthMethod.values(), ", "));
		}
	}

}
