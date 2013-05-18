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
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: UnsupportedGroupingException.java,v 1.3 2007-10-30 17:37:35
 *          jillig Exp $
 */
public class UnsupportedGroupingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new unsupported grouping exception with the specified
	 * grouping.
	 * 
	 * @param grouping
	 *            the grouping which is not supported. This is written into a
	 *            detail message which is saved for later retrieval by the
	 *            {@link #getMessage()} method.
	 */
	public UnsupportedGroupingException(final String grouping) {
		super("Grouping ('" + grouping + "') is not supported");
	}
}