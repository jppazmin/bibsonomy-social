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
 * @author sdo
 * @version $Id: UnsupportedRelationException.java,v 1.4 2011-04-29 06:36:47 bibsonomy Exp $
 */
public class UnsupportedRelationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new unsupported relation exception with a default
	 * detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause(Throwable)}.
	 */
	public UnsupportedRelationException() {
		super("The relation can not be processed by this method");
	}

	/**
	 * Constructs a new unsupported relation exception with the specified
	 * detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause(Throwable)}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public UnsupportedRelationException(String message) {
		super(message);
	}

}
