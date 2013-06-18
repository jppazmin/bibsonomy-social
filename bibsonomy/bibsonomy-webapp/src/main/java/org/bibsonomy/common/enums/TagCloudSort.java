/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.common.enums;

/**
 * Sorting modes for tag clouds
 * 
 * @author Dominik Benz
 * @version $Id: TagCloudSort.java,v 1.2 2007-12-21 17:25:40 cschenk Exp $
 */
public enum TagCloudSort {
	/** alphanumerical sorting */
	ALPHA(0),
	/** sorting by tag frequency */
	FREQ(1);

	private final int id;

	private TagCloudSort(final int id) {
		this.id = id;
	}

	/**
	 * @return ID of this tag cloud sort mode
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id
	 * @return a TagCloudSort object for the corresponding type
	 */
	public static TagCloudSort getSort(final int id) {
		switch (id) {
		case 0:
			return ALPHA;
		case 1:
			return FREQ;
		default:
			throw new RuntimeException("Sort " + id + " doesn't exist.");
		}
	}
}
