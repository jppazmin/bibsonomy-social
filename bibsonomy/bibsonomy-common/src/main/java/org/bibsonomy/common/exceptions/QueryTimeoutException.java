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
 * @author Dominik Benz <benz@cs.uni-kassel.de>
 * @version $Id: QueryTimeoutException.java,v 1.8 2011-04-29 06:36:47 bibsonomy Exp $
 */
public class QueryTimeoutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new query timeout exception, which is basically an SQL
	 * exception with the name of the timed out query
	 * 
	 * @param ex
	 * 
	 * @param query
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public QueryTimeoutException(Exception ex, String query) {
		super(query, ex);
	}
}