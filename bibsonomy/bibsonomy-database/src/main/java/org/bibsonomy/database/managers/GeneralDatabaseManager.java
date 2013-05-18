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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.model.User;

/**
 * Used to retrieve all different kind of stuff from the database.
 * 
 * @author Christian Schenk
 * @author Anton Wilhelm
 * @version $Id: GeneralDatabaseManager.java,v 1.52 2011-06-16 13:54:59 nosebrain Exp $
 */
public class GeneralDatabaseManager extends AbstractDatabaseManager {

	private static final GeneralDatabaseManager singleton = new GeneralDatabaseManager();

	/**
	 * @return the singleton instance
	 */
	public static GeneralDatabaseManager getInstance() {
		return singleton;
	}
	
	private GeneralDatabaseManager() {
		// noop
	}

	/**
	 * Checks whether <code>userA</code> is friend of <code>userB</code>, i.e., <strong>if 
	 * <code>userA</code> is in <code>userB</code>'s list of friends</strong>.
	 * <br/>
	 * <ul>
	 * <li>If one of the user names is empty the result will be <code>false</code>.</li> 
	 * <li>In case the user names are equal <code>true</code> will be returned, i.e. every user is his own
	 * friend.</li>
	 * </ul>
	 * 
	 * @param userA
	 *            the user who might be a friend of <code>userB</code>.
	 * @param userB
	 *            the user whose friendship to <code>userA</code> should be checked.
	 * @param session
	 *            a db session
	 *            
	 * @return <code>true</code> if <code>userA</code> is in <code>userB</code>'s list of friends, 
	 *         <code>false</code> otherwise
	 */
	public boolean isFriendOf(final String userA, final String userB, final DBSession session) {
		/*
		 * user names missing -> no friends
		 */
		if (!present(userA) || !present(userB)) {
			return false;
		}
		/*
		 * everybody is his/her own friend
		 */
		if (userA.equals(userB)) {
			return true;
		}
		/*
		 * we're looking at userB's friend list, hence, we create userB ...
		 */
		final User user = new User(userB);
		/*
		 * ... and then add userA to the list of his friends.
		 */
		user.addFriend(new User(userA));
		/*
		 * now we can query the DB, if userA is really userB's friend
		 */
		final UserParam param = new UserParam();
		param.setUser(user);
		return this.queryForObject("isFriendOf", param, Boolean.class, session);
	}

	/**
	 * Checks whether a user, given by userName, is a spammer. If userName is
	 * set to null the default behaviour is to return false, i.e. no spammer.
	 * 
	 * @param userName check the user with this name
	 * @param session a db session
	 * @return true if the user is a spammer, false otherwise
	 */
	public Boolean isSpammer(final String userName, final DBSession session) {
		if (!present(userName)) return false;
		return this.queryForObject("isSpammer", userName, Boolean.class, session);
	}

	/**
	 * Gets the next database-ID for inserting an entity with the type specified
	 * by the idsType argument. Updates the ID generator.
	 * 
	 * @param idsType type of the id to be created
	 * @param session a db session
	 * @return the next database-ID
	 */
	public Integer getNewId(final ConstantID idsType, final DBSession session) {
		this.updateIds(idsType, session);
		return this.queryForObject("getNewId", idsType.getId(), Integer.class, session);
	}

	protected void updateIds(final ConstantID idsType, final DBSession session) {
		this.insert("updateIds", idsType.getId(), session);
	}
}