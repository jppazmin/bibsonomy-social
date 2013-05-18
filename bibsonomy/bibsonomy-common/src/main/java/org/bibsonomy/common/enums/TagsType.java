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
 * Enum which summarizes which kinds of tags we have in the system
 * (and which can be queried)
 * 
 * @author Dominik Benz
 * @version $Id: TagsType.java,v 1.4 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum TagsType {
	/** the standard kind of tag */
	DEFAULT,
	/** related tags, i.e., tags which co-occur with a given tag */
	RELATED,
	/** similar tags, i.e., tags which are semantically similar to a given tag */
	SIMILAR;

	/**
	 * Returns the name for this kind of tags, i.e.:
	 * 
	 * <pre>
	 *  DEFAULT  - default
	 *  REALTED  - related
	 *  SIMILAR  - similar
	 * </pre>
	 * 
	 * @return an all lowercase string for this kind of tag
	 */
	public String getName() {
		return this.name().toLowerCase();
	}
}