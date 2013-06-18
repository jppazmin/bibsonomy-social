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

package org.bibsonomy.webapp.util.spring.security.exceptions;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Signals that a user that was successfully authenticated using LDAP was not
 * found in the database. 
 * 
 * @author rja
 * @version $Id: LdapUsernameNotFoundException.java,v 1.3 2010-12-08 10:12:02 rja Exp $
 */
public class LdapUsernameNotFoundException extends UsernameNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1083350264240144138L;
	private final DirContextOperations dirContextOperations;
	
	/**
	 * @param msg
	 * @param dirContextOperations
	 */
	public LdapUsernameNotFoundException(String msg, DirContextOperations dirContextOperations) {
		super(msg);
		this.dirContextOperations = dirContextOperations;
	}

	/**
	 * @return The context from LDAP.
	 */
	public DirContextOperations getDirContextOperations() {
		return this.dirContextOperations;
	}

}
