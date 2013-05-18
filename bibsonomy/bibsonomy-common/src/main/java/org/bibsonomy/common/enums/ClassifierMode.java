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
 * The working mode of an automatic classify algorithm
 * 
 * @author Stefan Stützer
 * @version $Id: ClassifierMode.java,v 1.9 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum ClassifierMode {
	/** day mode */
	DAY("D"),

	/** night mode */
	NIGHT("N");

	private String abbreviation;

	private ClassifierMode(final String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * @param mode
	 * @return the corresponding enum
	 */
	public static ClassifierMode getMode(final String mode) {
		if ("D".equals(mode)) return DAY;
		return NIGHT;
	}

	/**
	 * @return abbreviation of the specified mode
	 */
	public String getAbbreviation() {
		return abbreviation;
	}
}