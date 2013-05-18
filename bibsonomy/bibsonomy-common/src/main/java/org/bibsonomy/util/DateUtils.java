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

package org.bibsonomy.util;

import java.util.Date;

/**
 * Helper functions for dates
 * 
 * @author Dominik Benz
 * @version $Id: DateUtils.java,v 1.6 2011-04-29 06:36:50 bibsonomy Exp $
 */
public class DateUtils {
	/** 
	 * Compares two dates like compareTo but with additional checks, if one of the dates is NULL.
	 * 
	 * @param d1
	 * @param d2
	 * @return 0 if d1 == null and d2 == null, -1 if d1 == null, 1 if d2 == null
	 */
	public static int secureCompareTo(final Date d1, final Date d2) {
		// null = d1 = d2 = null
		if (d1 == null && d2 == null) return 0;
		// null = d1 < d2 != null
		if (d1 == null) return -1;
		// null != d1 > d2 = null
		if (d2 == null) return 1;
		// null != d1 ? d2 != null
		return d1.compareTo(d2);
	}
}
