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

package org.bibsonomy.database.managers.chain.user.get;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.user.UserChainElement;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.model.User;

/**
 * Get the list of users which follow the given user.
 * 
 * @author Christian Kramer
 * @version $Id: GetUserFollowers.java,v 1.4 2011-05-26 00:16:44 folke Exp $
 */
public class GetUserFollowers extends UserChainElement{
	
	@Override
	protected List<User> handle(UserParam param, DBSession session) {
		return this.userDB.getUserRelation(param.getUserName(), UserRelation.OF_FOLLOWER, null, session);
	}

	@Override
	protected boolean canHandle(UserParam param) {
		return (GroupingEntity.FOLLOWER.equals(param.getGrouping()) &&
				present(param.getUserName()) &&
				UserRelation.OF_FOLLOWER.equals(param.getUserRelation()));
	}

}
