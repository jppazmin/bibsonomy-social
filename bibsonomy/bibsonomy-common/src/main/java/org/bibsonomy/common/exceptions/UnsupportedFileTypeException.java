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
 * @author rja
 * @version $Id: UnsupportedFileTypeException.java,v 1.5 2011-04-29 06:36:47 bibsonomy Exp $
 */
public class UnsupportedFileTypeException extends RuntimeException {
	private static final long serialVersionUID = 6493856479182895955L;
	
	private final String[] allowedExt;

	/**
	 * Constructs a new unsupported file type exception with the specified
	 * allowed extensions. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause(Throwable)}.
	 * 
	 * @param allowedExt
	 * 				the supported file type extensions.
	 *            
	 */
	public UnsupportedFileTypeException(final String[] allowedExt) {
		super("Please check your file. Only " + getExceptionExtensions(allowedExt) + " files are accepted.");
		this.allowedExt = allowedExt;
	}
	
	/**
	 * Converts the given files extensions to upper cases and connects them with "," and "or", e.g.:
	 * input:
	 *   "pdf", "ps", "djv", "djvu", "txt"
	 * output:
	 *   "PDF, PS, TXT or DJVU"
	 * @param allowedExt
	 * @return
	 */
	private static String getExceptionExtensions(final String[] allowedExt) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < allowedExt.length - 1; i++) {
			buf.append(allowedExt[i].toUpperCase() + ", ");
		}
		
		if (allowedExt.length > 1) {
			buf.append("or ");
		}
		buf.append(allowedExt[allowedExt.length - 1].toUpperCase());
		
		return buf.toString();
	}

	/**
	 * @return the allowed Extensions
	 */
	public String[] getAllowedExt() {
		return allowedExt;
	}
}