/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.enums;

/**
 * @author dzo
 * @version $Id: DiscussionItemType.java,v 1.1 2011-06-16 13:18:38 nosebrain Exp $
 */
public enum DiscussionItemType {
	
	/**
	 * represents a deleted or invisible discussion item
	 */
	DISCUSSION_ITEM(0),
	
	/**
	 * review (optional text with rating)
	 */
	REVIEW(1),
	
	/**
	 * comment (only text)
	 */
	COMMENT(2);
	
	private int id;
	
	private DiscussionItemType(final int id) {
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
}
