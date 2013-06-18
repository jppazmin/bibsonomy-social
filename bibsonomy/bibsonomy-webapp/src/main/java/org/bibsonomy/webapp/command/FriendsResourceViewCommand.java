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

package org.bibsonomy.webapp.command;

import java.util.List;

import org.bibsonomy.model.User;

/**
 * @author Steffen Kress
 * @version $Id: FriendsResourceViewCommand.java,v 1.5 2010-09-06 08:53:40 rja Exp $
 */
public class FriendsResourceViewCommand extends TagResourceViewCommand {
	private List<User> userFriends;
	private List<User> friendsOfUser;
	/** for queries for specific kinds of users (e.g., friends) */
	private String userRelation;

	/**
	 * @param userFriends
	 */
	public void setUserFriends(List<User> userFriends) {
		this.userFriends = userFriends;
	}

	/**
	 * @param friendsOfUser
	 */
	public void setFriendsOfUser(List<User> friendsOfUser) {
		this.friendsOfUser = friendsOfUser;
		
	}

	/**
	 * @return friends of the user
	 */
	public List<User> getFriendsOfUser() {
		return friendsOfUser;
	}

	/**
	 * @return the users friends
	 */
	public List<User> getUserFriends() {
		return userFriends;
	}

	/**
	 * @return The relation the users to return have with the requested user.
	 */
	public String getUserRelation() {
		return this.userRelation;
	}

	/**
	 * @param userRelation
	 */
	public void setUserRelation(String userRelation) {
		this.userRelation = userRelation;
	}

}
