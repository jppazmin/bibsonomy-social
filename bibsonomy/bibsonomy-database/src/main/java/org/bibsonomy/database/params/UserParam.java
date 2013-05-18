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

package org.bibsonomy.database.params;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;

/**
 * Parameters that are specific to users.
 * 
 * @author Miranda Grahl
 * @author Christian Schenk
 * @version $Id: UserParam.java,v 1.12 2011-06-01 22:00:14 folke Exp $
 */
public class UserParam extends GenericParam {

	/**
	 * a user
	 */
	private User user;
	

	/**
	 * a user relation
	 */
	private UserRelation userRelation;

	
	/**
	 * Returns the first friend of this user.<br/>
	 * 
	 * XXX: iBatis should support this: "friends[0].name", which should return
	 * the name of the first friend - but this doesn't seem to work so we need
	 * this extra method.
	 * 
	 * 2011/06/01, fei: moved this code fragment from the user model - this
	 *                  is used only in the query 'isFriendOf'
	 * 
	 * @return friend
	 */
	public User getFriend() {
		if (!present(this.user) || this.user.getFriends().size() < 1) return null;
		return this.user.getFriends().get(0);
	}
	
	/**
	 * @return user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * set user relation
	 * 
	 * @param userRelation
	 */
	public void setUserRelation(UserRelation userRelation) {
		this.userRelation = userRelation;
	}

	/**
	 * get user relation
	 * 
	 * @return the user relation
	 */
	public UserRelation getUserRelation() {
		return userRelation;
	}
}