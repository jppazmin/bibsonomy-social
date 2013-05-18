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
 * Privacy levels for groups:
 * <ul>
 * <li>public: the member list is public</li>
 * <li>hidden: the member list is hidden</li>
 * <li>members: only members can see members</li>
 * </ul>
 * 
 * @author Christian Schenk
 * @version $Id: Privlevel.java,v 1.13 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum Privlevel {
	/** the member list is public */
	PUBLIC(0),
	/** the member list is hidden */
	HIDDEN(1),
	/** only members can see members */
	MEMBERS(2);

	private final int privlevel;

	private Privlevel(final int privlevel) {
		this.privlevel = privlevel;
	}

	/**
	 * @return constant value behind the symbol
	 */
	public int getPrivlevel() {
		return this.privlevel;
	}

	/**
	 * @param privlevel
	 *            constant value behind the Privlevel symbol to retrieve
	 * @return the corresponding Privlevel-enum for the given int.
	 */
	public static Privlevel getPrivlevel(final int privlevel) {
		switch (privlevel) {
		case 0:
			return PUBLIC;
		case 1:
			return HIDDEN;
		case 2:
			return MEMBERS;
		default:
			throw new RuntimeException("Privlevel is out of bounds (" + privlevel + ")");
		}
	}
	
	/** Returns the corresponding privlevel for the given String.
	 * 
	 * @param privlevel
	 * @return A privlevel according to the String. If the string does not match 
	 * any privlevel, {@value #MEMBERS} is returned.
	 */
	public static Privlevel getPrivlevel(final String privlevel) {
		if ("members".equals(privlevel)) {
			return MEMBERS;
		} 
		if ("hidden".equals(privlevel)) {
			return HIDDEN;
		}
		return PUBLIC;
	}
	
}