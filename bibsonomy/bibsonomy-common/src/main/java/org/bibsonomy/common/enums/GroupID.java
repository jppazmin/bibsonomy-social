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

import static org.bibsonomy.util.ValidationUtils.present;

/**
 * Constant group ids.
 * @version $Id: GroupID.java,v 1.21 2011-07-14 13:09:39 nosebrain Exp $
 */
public enum GroupID {
	
	/** the public group */
	PUBLIC(0),
	
	/** the owning user's private group */
	PRIVATE(1),
	
	/** the owning user's friends group */
	FRIENDS(2),
	
	/** an invalid value */
	INVALID(-1),
	
	/**
	 * public group for spam posts
	 */
	PUBLIC_SPAM(-2147483648),
	
	/**
	 * private group for spam posts
	 */
	PRIVATE_SPAM(-2147483647),
	
	/**
	 * public group for spam posts 
	 */
	FRIENDS_SPAM(-2147483646); 

	/*
	 * use logical OR (|) to set first bit
	 */ 
	private static final int CONST_SET_1ST_BIT = 0x80000000;
	
	/*
	 * use logical AND (&) to clear first bit
	 */
	private static final int CONST_CLEAR_1ST_BIT = 0x7FFFFFFF;
	
	private final int id;

	private GroupID(final int id) {
		this.id = id;
	}

	/**
	 * @return the constant value behind the symbol
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param groupName
	 *            the groupname to look up
	 * @return GroupID representation of a special group which name correspond
	 *         to the argument
	 */
	public static GroupID getSpecialGroup(final String groupName) {
		if (!present(groupName)) return null;
		final GroupID group = valueOf(groupName.toUpperCase());
		if (isSpecialGroupId(group.getId())) return group;
		return null;
	}	

	/**
	 * categorizes groupIds between special and nonspecial groups. special
	 * groups are groups, that are not created by users.
	 * 
	 * @param groupId
	 *            the groupId to check
	 * @return true if the groupId argument is a special group
	 */
	public static boolean isSpecialGroupId(final int groupId) {
		return ((groupId < 3) && (groupId >= 0));
	}

	/**
	 * categorizes groupIds between special and nonspecial groups. special
	 * groups are groups, that are not created by users.
	 * 
	 * @param groupId
	 *            the groupId to check
	 * @return true if the groupId argument is a special group
	 */
	public static boolean isSpecialGroupId(final GroupID groupId) {
		return isSpecialGroupId(groupId.getId());
	}

	/**
	 * wrapper function to check if a given groupname represents a special group
	 * 
	 * @param groupName
	 * @return true if the given group is a special group, false otherwise
	 */
	public static boolean isSpecialGroup(final String groupName) {
		try {
			if (getSpecialGroup(groupName) != null) return true;
		} catch (IllegalArgumentException ignore) {
		}
		return false;
	}

	/**
	 * Merges spaminformation into the groupId (MSB set iff isSpammer is <code>true</code>).
	 * 
	 * FIXME: can't handle {@link GroupID#INVALID}.
	 * 
	 * @param groupId
	 *            the original groupId
	 * @param isSpammer
	 *            true if the user is a spammer, otherwise false
	 * @return groupId with potentially modified MSB
	 */
	public static int getGroupId(final int groupId, final boolean isSpammer) {
		if (isSpammer) return getSpam(groupId);
		/* 
		 * Note: "return groupid" is not enough, since we want to use that to
		 * unflag spammers posts, as well
		 */ 
		return getNonSpam(groupId);
	}

	private static int getNonSpam(final int groupId) {
		return groupId & CONST_CLEAR_1ST_BIT;
	}

	/**
	 * @param groupId
	 * @return
	 */
	private static int getSpam(final int groupId) {
		return groupId | CONST_SET_1ST_BIT;
	}
	
	/**
	 * Compares two group IDs ignoring if they're spam flagged or not. 
	 * 
	 * @param groupIdA
	 * @param groupIdB
	 * @return <code>true</code> if both IDs are equal, independent of their spam flag
	 */
	public static boolean equalsIgnoreSpam(final int groupIdA, final int groupIdB) {
		final int a = getNonSpam(groupIdA);
		final int b = getNonSpam(groupIdB);
		return (a) == (b);
	}
	
	/**
	 * Compares two group IDs ignoring if they're spam flagged or not. 
	 * 
	 * @param groupIdA
	 * @param groupIdB
	 * @return <code>true</code> if both IDs are equal, independent of their spam flag
	 */
	public static boolean equalsIgnoreSpam(final GroupID groupIdA, final GroupID groupIdB) {
		return equalsIgnoreSpam(groupIdA.getId(), groupIdB.getId());
	}
}