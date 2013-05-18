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

package org.bibsonomy.common.enums;

/**
 * @author Robert Jäschke
 * @version $Id: InetAddressStatus.java,v 1.9 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum InetAddressStatus {
	/**
	 * The IP is blocked - write access is not allowed at all (neither
	 * registration nor posting).
	 */
	WRITEBLOCKED(1),
	/** The IP can not be found in the status table - its status is unknown. */
	UNKNOWN(0);

	private static final InetAddressStatus[] map = new InetAddressStatus[] { UNKNOWN, WRITEBLOCKED };
	private final int status;

	private InetAddressStatus(final int status) {
		this.status = status;
	}

	/**
	 * Returns the numerical representation of this object.
	 * 
	 * @return The numerical representation of the object.
	 */
	public int getInetAddressStatus() {
		return this.status;
	}

	/**
	 * Creates an instance of this class by its String representation.
	 * 
	 * @param inetAddressStatus -
	 *            a String representing the object. Must be an integer number.
	 * @return The corresponding object.
	 */
	public static InetAddressStatus getInetAddressStatus(final String inetAddressStatus) {
		if (inetAddressStatus == null) return UNKNOWN;
		return getInetAddressStatus(Integer.parseInt(inetAddressStatus));
	}

	/**
	 * Creates an instance of this class by its Integer representation.
	 * 
	 * @param inetAddressStatus -
	 *            an Integer representing the object.
	 * @return The corresponding object.
	 */
	public static InetAddressStatus getInetAddressStatus(final int inetAddressStatus) {
		return map[inetAddressStatus];
	}
}