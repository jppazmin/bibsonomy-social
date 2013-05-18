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

package org.bibsonomy.database.managers.chain.resource.get;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.resource.ResourceChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * Returns all resources of users you are following.
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: GetResourcesByFollowedUsers.java,v 1.2 2010-06-02 11:31:59 nosebrain Exp $
 * @param <R> 
 * @param <P> 
 */
public class GetResourcesByFollowedUsers<R extends Resource, P extends ResourceParam<R>> extends ResourceChainElement<R, P> {

	@Override
	protected boolean canHandle(P param) {
		return (present(param.getUserName()) &&
				present(param.getGroups()) &&
				param.getGrouping() == GroupingEntity.FOLLOWER);
	}

	@Override
	protected List<Post<R>> handle(P param, DBSession session) {
		return this.getDatabaseManagerForType(param.getClass()).getPostsByFollowedUsers(param.getUserName(), param.getGroups(), param.getLimit(), param.getOffset(), session);
	}

}
