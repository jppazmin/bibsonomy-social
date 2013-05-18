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

import java.util.List;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.resource.ResourceChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;

/**
 * Returns a list of bookmarks for given tag/tags.
 * 
 * @author Miranda Grahl
 * @version $Id: GetResourcesByTagNames.java,v 1.3 2010-07-02 11:47:40 nosebrain Exp $
 * @param <R> 
 * @param <P> 
 */
public class GetResourcesByTagNames<R extends Resource, P extends ResourceParam<R>> extends ResourceChainElement<R, P> {

	@Override
	protected boolean canHandle(final P param) {
		return (param.getGrouping() == GroupingEntity.ALL &&
				present(param.getTagIndex()) &&
				param.getNumSimpleConcepts() == 0 &&
				param.getNumSimpleTags() > 0 &&
				param.getNumTransitiveConcepts() == 0 &&
				!present(param.getHash()) &&
				nullOrEqual(param.getOrder(), Order.ADDED, Order.FOLKRANK) &&
				!present(param.getSearch()) &&
				!present(param.getAuthor()) &&
				!present(param.getTitle()) );
	}

	@Override
	protected List<Post<R>> handle(final P param, final DBSession session) {
		return this.getDatabaseManagerForType(param.getClass()).getPostsByTagNames(GroupID.PUBLIC.getId(), param.getTagIndex(), param.getOrder(), param.getLimit(), param.getOffset(), session);
	}

}
