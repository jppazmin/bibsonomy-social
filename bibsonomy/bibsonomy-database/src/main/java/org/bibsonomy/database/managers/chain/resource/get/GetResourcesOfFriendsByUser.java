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

import static org.bibsonomy.util.ValidationUtils.nullOrEqual;
import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.resource.ResourceChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.database.systemstags.search.NetworkRelationSystemTag;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;

/**
 * Returns a list of resources for a given friend of a user (this friends also
 * posted this resources to group friends (made resources viewable for
 * friends)).
 * 
 * @author Miranda Grahl
 * @version $Id: GetResourcesOfFriendsByUser.java,v 1.3 2011-05-28 01:30:07 folke Exp $
 * @param <R> 
 * @param <P> 
 */
public class GetResourcesOfFriendsByUser<R extends Resource, P extends ResourceParam<R>> extends ResourceChainElement<R, P> {

	@Override
	protected boolean canHandle(final P param) {
		return (present(param.getUserName()) &&
				param.getGrouping() == GroupingEntity.FRIEND &&
				// discriminate from the tagged user relation queries
				( !present(param.getRelationTags()) || 
					param.getRelationTags().size()==1 && (NetworkRelationSystemTag.BibSonomyFriendSystemTag.equals(param.getRelationTags().get(0)))
				) &&
				present(param.getRequestedUserName()) &&
				!present(param.getTagIndex()) &&
				!present(param.getHash()) &&
				nullOrEqual(param.getOrder(), Order.ADDED) &&
				!present(param.getSearch()) && 
				!present(param.getAuthor()) &&
				!present(param.getTitle()));
	}

	@Override
	protected List<Post<R>> handle(final P param, final DBSession session) {
		/*
		 * if the requested user has the current user in his/her friend list, he may 
		 * see the posts
		 */
		if (this.generalDb.isFriendOf(param.getUserName(), param.getRequestedUserName(), session)) {
			final int groupId = GroupID.FRIENDS.getId();
			return this.getDatabaseManagerForType(param.getClass()).getPostsForUser(param.getUserName(), param.getRequestedUserName(), HashID.getSimHash(param.getSimHash()), groupId, param.getGroups(), param.getFilter(), param.getLimit(), param.getOffset(), param.getSystemTags().values(), session);
		}

		return new ArrayList<Post<R>>();
	}

}
