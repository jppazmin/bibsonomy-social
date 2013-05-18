/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.util;

import java.util.Set;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.Privlevel;
import org.bibsonomy.model.Group;

/**
 * @author Christian Schenk
 * @version $Id: GroupUtils.java,v 1.12 2011-06-16 13:17:28 nosebrain Exp $
 */
public class GroupUtils {


	private static final Group INVALID_GROUP = getGroup("invalid", "invalid group", GroupID.INVALID, Privlevel.HIDDEN);

	private static final Group FRIENDS_GROUP = getGroup("friends", "friends group", GroupID.FRIENDS, Privlevel.HIDDEN);
	private static final Group PRIVATE_GROUP = getGroup("private", "private group", GroupID.PRIVATE, Privlevel.HIDDEN);
	private static final Group PUBLIC_GROUP  = getGroup("public",  "public group",  GroupID.PUBLIC,  Privlevel.PUBLIC);

	private static final Group FRIENDS_SPAM_GROUP = getGroup("friends", "friends group", GroupID.FRIENDS_SPAM, Privlevel.HIDDEN);
	private static final Group PRIVATE_SPAM_GROUP = getGroup("private", "private group", GroupID.PRIVATE_SPAM, Privlevel.HIDDEN);
	private static final Group PUBLIC_SPAM_GROUP  = getGroup("public",  "public group",  GroupID.PUBLIC_SPAM,  Privlevel.PUBLIC);


	/**
	 * Public group
	 * 
	 * @return public group
	 */
	public static Group getPublicGroup() {
		return PUBLIC_GROUP;
	}

	/**
	 * Private group
	 * 
	 * @return private group
	 */
	public static Group getPrivateGroup() {
		return PRIVATE_GROUP;
	}

	/**
	 * Friends group
	 * 
	 * @return friends group
	 */
	public static Group getFriendsGroup() {
		return FRIENDS_GROUP;
	}


	/**
	 * Public spam group
	 * 
	 * @return public group
	 */
	public static Group getPublicSpamGroup() {
		return PUBLIC_SPAM_GROUP;
	}

	/**
	 * Private spam group
	 * 
	 * @return private group
	 */
	public static Group getPrivateSpamGroup() {
		return PRIVATE_SPAM_GROUP;
	}

	/**
	 * Friends spam group
	 * 
	 * @return friends group
	 */
	public static Group getFriendsSpamGroup() {
		return FRIENDS_SPAM_GROUP;
	}

	/**
	 * Invalid group
	 * 
	 * @return invalid group
	 */
	public static Group getInvalidGroup() {
		return INVALID_GROUP;
	}

	/**
	 * Checks if the given group is an "exclusive" group, i.e., a group which can't 
	 * be chosen as "viewable for" together with another group - basically the 
	 * groups "private" and "public". Use this method because it also checks spam
	 * groups! 
	 *  
	 * @param group
	 * @return <code>true</code> if the group is exclusively be "viewable for". 
	 */
	public static boolean isExclusiveGroup(final Group group) {
		return (
				getPrivateGroup().equals(group) || 
				getPublicGroup().equals(group)
		);
	}

	/**
	 * Checks if the given group ID is an "exclusive" group ID, i.e., a group which can't 
	 * be chosen as "viewable for" together with another group - basically the 
	 * groups "private" and "public". Use this method because it also checks spam
	 * groups! 
	 *  
	 * @param groupId
	 * @return <code>true</code> if the group is exclusively "viewable for" 
	 */
	public static boolean isExclusiveGroup(final int groupId) {
		return (
				GroupID.equalsIgnoreSpam(getPrivateGroup().getGroupId(), groupId) || 
				GroupID.equalsIgnoreSpam(getPublicGroup().getGroupId(), groupId)
		);
	}
	
	/**
	 * 
	 * @param groups
	 * @param isSpammer
	 */
	public static void prepareGroups(final Set<Group> groups, final boolean isSpammer) {
		for (final Group group : groups) {
			/*
			 * update the group id of the post
			 */
			group.setGroupId(GroupID.getGroupId(group.getGroupId(), isSpammer));
		}
	}

	/**
	 * Checks if the given groups contain an "exclusive" group, i.e., a group which 
	 * can't be chosen as "viewable for" together with another group - basically the 
	 * groups "private" and "public". Use this method because it also checks spam
	 * groups! 
	 *  
	 * @param groups
	 * @return <code>true</code> if one of the groups is exclusively be "viewable for". 
	 */
	public static boolean containsExclusiveGroup(final Set<Group> groups) {
		/*
		 * at least one of the groups is public or private
		 */
		return groups.contains(getPublicGroup()) || groups.contains(getPrivateGroup());
	}
	
	/**
	 * Checks if the set of groups contains only the public group.
	 * @param groups
	 * @return <code>true</code> if the set of groups contains only the public group.
	 */
	public static boolean isPublicGroup(final Set<Group> groups) {
		return groups.size() == 1 && groups.contains(getPublicGroup());
	}

	/**
	 * Helper method that returns a new {@link Group} object.
	 */
	private static Group getGroup(final String name, final String description, final GroupID groupId, final Privlevel privlevel) {
		final Group group = new Group();
		group.setName(name);
		group.setDescription(description);
		group.setGroupId(groupId.getId());
		group.setPrivlevel(privlevel);
		return group;
	}
}