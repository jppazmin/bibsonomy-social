/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.common.exceptions;

/**
 * NOTE: if you want to redirect the user to the login page (webapp module)
 * you must use the AccessDeniedException from the Spring Security package
 * 
 * @author dzo
 * @version $Id: AccessDeniedException.java,v 1.4 2011-06-15 14:39:51 nosebrain Exp $
 */
public class AccessDeniedException extends RuntimeException {
	private static final long serialVersionUID = -2496286544331707252L;
	
	private static final String DEFAULT_MESSAGE = "You are not authorized to perform the requested operation.";

	/**
	 * creates an AccessDeniedException with {@link #DEFAULT_MESSAGE}
	 */
	public AccessDeniedException() {
		super(DEFAULT_MESSAGE);
	}
	
	/**
	 * @see RuntimeException#RuntimeException(String)
	 * @param message
	 */
	public AccessDeniedException(String message) {
		super(message);
	}
}
