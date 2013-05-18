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
 * @author dzo
 * @version $Id: RatingAverage.java,v 1.2 2011-07-26 13:42:40 bibsonomy Exp $
 */
public enum RatingAverage {
	/**
	 * 1/n * ∑{i = 1}{n}(a_i)
	 */
	ARITHMETIC_MEAN;

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 * 
	 * for iBatis statement mapping
	 */
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
