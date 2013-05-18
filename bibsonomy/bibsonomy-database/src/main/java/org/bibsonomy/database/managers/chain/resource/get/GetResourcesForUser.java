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
import static org.bibsonomy.util.ValidationUtils.presentValidGroupId;

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.resource.ResourceChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;

/**
 * Returns a list of resources for a given user.
 * 
 * @author Miranda Grahl
 * @version $Id: GetResourcesForUser.java,v 1.2 2010-07-02 11:47:40 nosebrain Exp $
 * @param <R> 
 * @param <P> 
 */
public class GetResourcesForUser<R extends Resource, P extends ResourceParam<R>> extends ResourceChainElement<R, P> {

	@Override
	protected boolean canHandle(final P param) {
		return (param.getGrouping() == GroupingEntity.USER &&
				present(param.getRequestedUserName()) &&
				!presentValidGroupId(param.getGroupId()) &&
				!present(param.getTagIndex()) &&
				!present(param.getHash()) &&
				nullOrEqual(param.getOrder(), Order.ADDED) &&
				!present(param.getSearch())) && 
				!present(param.getAuthor()) && 
				!present(param.getTitle());
	}

	@Override
	protected List<Post<R>> handle(final P param, final DBSession session) {
		return this.getDatabaseManagerForType(param.getClass()).getPostsForUser(param.getUserName(), param.getRequestedUserName(), HashID.getSimHash(param.getSimHash()), param.getGroupId(), param.getGroups(), param.getFilter(), param.getLimit(), param.getOffset(), param.getSystemTags().values(), session);
	}

}
