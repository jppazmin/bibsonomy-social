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

package org.bibsonomy.webapp.exceptions;

/**
 * Exception to handle malformed URL schemes
 * 
 * @author Dominik Benz
 * @version $Id: MalformedURLSchemeException.java,v 1.1 2008-01-10 09:15:19 dbe Exp $
 */
public class MalformedURLSchemeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new malformed URL scheme exception with the specified
	 * detail message. .
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     */
	public MalformedURLSchemeException(final String message) {
		super(message);
	}
}
