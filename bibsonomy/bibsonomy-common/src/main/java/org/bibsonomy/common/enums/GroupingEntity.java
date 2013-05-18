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

import org.bibsonomy.common.exceptions.UnsupportedGroupingException;
import org.bibsonomy.util.EnumUtils;

/* TODO: what is "grouped" by these entities? isn't it filtering? */
/**
 * defines possible entities for which query constraints can be set
 * (someone called this "grouping")
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @author dbenz
 * @version $Id: GroupingEntity.java,v 1.15 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum GroupingEntity {
	/**
	 * for constrainting the user which is somehow related to the entities
	 * in a list which is to be filtered.
	 */
	USER,
	
	/**
	 * for constraints on the filtered entity's owning user's group membership
	 * regardless of whether the actual group of the entity is public or
	 * friend or the constraintgroup, as long as some unspecified kind of
	 * authorization for seeing the entity is present.
	 */
	GROUP,
	
	/* TODO: describe why this is called "viewable" */
	/**
	 * for constraints on the <b>explicit</b> group-association of
	 * the entities in a list which is to be filtered. This is
	 * <b>NOT</b> viewability through <b>any</b> group membership
	 * but "ownership of the entity by the group" in the current
	 * bibsonomy-schema-speaking.
	 */
	VIEWABLE,
	
	/**
	 * for no constraints on the entities in a list which is to be filtered
	 */
	ALL,
	
	/**
	 * for constrainting the associated groups of the entities in the list, 
	 * which is to be filtered, to contain the group 'friends' and the requesting 
	 * user is in that "friends-group".  
	 */
	FRIEND,
	
	/**
	 * for constraining the associated groups of the entities in the list, 
	 * which is to be filtered, to contain the group 'followers' and the requesting 
	 * user is in that "followers-group".  
	 */
	FOLLOWER,
	
	/**
	 * for constraining the entities in the list to the ones contained in the
	 * basket collection of a given user
	 */
	BASKET, 
	
	/**
	 * for constraining the entities in the list to the ones contained in the 
	 * inbox of the given user
	 */
	INBOX,
	
	PENDING;
	
	/**
	 * @param groupingEntity name of the GroupingEntity Instance to retrieve
	 * @return the corresponding GroupingEntity-enum for the given string argument.
	 */
	public static GroupingEntity getGroupingEntity(final String groupingEntity) {
		final GroupingEntity ge = EnumUtils.searchEnumByName(GroupingEntity.values(), groupingEntity);
		if (ge == null) throw new UnsupportedGroupingException(groupingEntity);
		return ge;
	}
}