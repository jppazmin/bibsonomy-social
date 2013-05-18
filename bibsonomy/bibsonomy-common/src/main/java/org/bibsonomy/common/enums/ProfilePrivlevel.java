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
 * Privacy levels for the profile of an user
 * 
 * @author dzo
 * @version $Id: ProfilePrivlevel.java,v 1.4 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum ProfilePrivlevel {
	/**
	 * everyone can see the profile of the user
	 */
	PUBLIC(0),
	
	/**
	 * only the user can see the profile
	 */
	PRIVATE(1),
	
	/**
	 * only friends of the user can see the profile
	 */
	FRIENDS(2);
	
	private int id;
	
	private ProfilePrivlevel(final int id) {
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public int getProfilePrivlevel() {
		return this.id;
	}
	
	/**
	 * @param name
	 * @return the corresponding ProfilePrivLevel-enum for the given string. If the string does not match 
	 * any level, {@value #PRIVATE} is returned.
	 */
	public static ProfilePrivlevel getProfilePrivlevel(final String name) {		
		try {
			return valueOf(name.toUpperCase());
		} catch (final Exception ex) {
			return PRIVATE;
		}
	}
	
	/**
	 * @param name
	 * @return true, iff the string has a corresponding ProfilePrivLevel-enum
	 */
	public static boolean isProfilePrivlevel(final String name) {
		for (final ProfilePrivlevel level : values()) {
			if (level.name().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}

	/** 
	 * @param profilePrivlevel the id of the profile privlevel
	 * @return  the corresponding ProfilePrivlevel-enum for the given int.
	 */
	public static ProfilePrivlevel getProfilePrivlevel(final int profilePrivlevel) {
		for (final ProfilePrivlevel level : values()) {
			if (level.getProfilePrivlevel() == profilePrivlevel) {
				return level;
			}
		}

		throw new RuntimeException("ProfilePrivlevel is out of bounds (" + profilePrivlevel + ")");
	}
}
