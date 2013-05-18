/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.params.beans.TagIndex;
import org.bibsonomy.database.managers.GeneralDatabaseManager;
import org.bibsonomy.database.params.GenericParam;

/**
 * Methods concerning the database.
 * 
 * @author Jens Illig
 * @author Christian Schenk
 * @version $Id: DatabaseUtils.java,v 1.38 2010-09-23 12:05:08 nosebrain Exp $
 */
public final class DatabaseUtils {

	/**
	 * Checks if the logged-in user may see private / friends posts
	 * 
	 * PLEASE NOTE: as public stuff (tags, posts, ...) can be seen by everyone,
	 * the PUBLIC group is present by default in the param object (see constructor
 	 * of GenericParam
 	 * 
 	 * Furthermore, the groups the logged-in user is explitely member of (e.g. KDE)
 	 * are retrieved when getting access to the LogicInterface and are
 	 * set in the param object by the LogicInterfaceHelper.buildParam(... method
	 * 
	 * @param db
	 * @param param
	 * @param session
	 */
	public static void checkPrivateFriendsGroup(final GeneralDatabaseManager db, final GenericParam param, final DBSession session) {		 				
		if (present(param.getUserName()) && present(param.getRequestedUserName())) {
			final List<Integer> groupIds = new ArrayList<Integer>();
			// If userName and requestedUserName are the same -> add private and friends
			// otherwise: if they're friends -> only add friends
			if (param.getUserName().equals(param.getRequestedUserName())) {
				groupIds.add(GroupID.PRIVATE.getId());
				groupIds.add(GroupID.FRIENDS.getId());
			} else {
				if (db.isFriendOf(param.getUserName(), param.getRequestedUserName(), session)) {
					groupIds.add(GroupID.FRIENDS.getId());
				}
			}
			// add the groups
			param.addGroups(groupIds);			
		}
	}

	/**
	 * This needs to be done for all get*ForGroup* queries.
	 * @param db 
	 * @param param 
	 * @param session 
	 */
	public static void prepareGetPostForGroup(final GeneralDatabaseManager db, final GenericParam param, final DBSession session) {
		/*
		 * FIXME: Why is DatabaseUtils.checkPrivateFriendsGroup(db, param, session) called here?
		 * 
		 * It tests if a user name is given AND if the user is logged in. 
		 * At least the first test ALWAYS fails on /group/ pages, since we 
		 * have a group given, not a user name.  
		 * 
		 * 
		 */
		DatabaseUtils.checkPrivateFriendsGroup(db, param, session);
	}

	/**
	 * This needs to be done for all get*ForUser* queries.
	 * @param db 
	 * @param param 
	 * @param session 
	 */
	public static void prepareGetPostForUser(final GeneralDatabaseManager db, final GenericParam param, final DBSession session) {
		// if the groupId is invalid we have to check for groups manually
		if (param.getGroupId() == GroupID.INVALID.getId()) {
			DatabaseUtils.checkPrivateFriendsGroup(db, param, session);
		}
	}

	/**
	 * extracts list of tag names from given list of TagIndex instances
	 * 
	 * TODO: could we fill and use Generic.tags instead? 
	 * 
	 * @param tagIndex
	 * @return a list of tag names
	 */
	public static List<String> extractTagNames(final List<TagIndex> tagIndex) {
		final List<String> retVal = new LinkedList<String>();
		
		for (final TagIndex tagIdx : tagIndex) {
			retVal.add(tagIdx.getTagName());
		}
		
		return retVal;
	}
}