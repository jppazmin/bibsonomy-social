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

package org.bibsonomy.common.errors;

/**
 * Use this message if something went wrong and doesn't fit one of the other ErrorMessages.
 * Add the Exception, that occurred.
 * 
 * @author sdo
 * @version $Id: UnspecifiedErrorMessage.java,v 1.10 2011-04-29 06:36:57 bibsonomy Exp $
 */
public class UnspecifiedErrorMessage extends ErrorMessage {
	private final Exception ex;
	
	/**
	 * @param ex The exception that was caught.
	 */
	public UnspecifiedErrorMessage(Exception ex) {
		super(ex.getClass().getName() + " : " + ex.getMessage(), "database.exception.unspecified");
		
		this.ex = ex;
	}
	
	/**
	 * @return the exception, that caused the error
	 */
	public Exception getException() {
		return ex;
	}
}
