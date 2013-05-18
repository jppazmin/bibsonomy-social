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
 * Get related users for a given user.
 * 
 * @author Dominik Benz
 * @version $Id: GetRelatedUsersByUser.java,v 1.3 2010-06-02 11:31:54 nosebrain Exp $
 */
public class GetRelatedUsersByUser extends UserChainElement {

	@Override
	protected List<User> handle(final UserParam param, final DBSession session) {
		/*
		 * handle folkrank separately
		 */
		if (UserRelation.FOLKRANK.equals(param.getUserRelation())) {
			return this.userDB.getRelatedUsersByFolkrankAndUser(param.getRequestedGroupName(),
																param.getUserName(),
																param.getLimit(), 
																param.getOffset(), 
																session);				
		}
		return this.userDB.getRelatedUsersBySimilarity(param.getRequestedUserName(), 
													   param.getUserName(),
													   param.getUserRelation(), 
													   param.getLimit(), 
													   param.getOffset(), 
													   session);
	}

	@Override
	protected boolean canHandle(final UserParam param) {
		return (GroupingEntity.USER.equals(param.getGrouping()) &&
				present(param.getRequestedUserName()) &&
				present(param.getUserRelation()));
	}
}