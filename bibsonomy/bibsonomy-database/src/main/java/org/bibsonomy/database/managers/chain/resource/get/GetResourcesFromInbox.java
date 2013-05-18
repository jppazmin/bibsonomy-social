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
import org.bibsonomy.database.managers.PostDatabaseManager;
import org.bibsonomy.database.managers.chain.resource.ResourceChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * Returns the posts in the users inbox
 * @param <R> 
 * @param <P>
 * 
 * @author sdo
 * @version $Id: GetResourcesFromInbox.java,v 1.2 2010-06-02 11:31:59 nosebrain Exp $
 * 
 */
public class GetResourcesFromInbox<R extends Resource, P extends ResourceParam<R>> extends ResourceChainElement<R, P> {
	
	@Override
	protected List<Post<R>> handle(final P param, final DBSession session) {
		final PostDatabaseManager<R, P> db = this.getDatabaseManagerForType(param.getClass());
		
		if (present(param.getHash())) {
			/*
			 * If an intraHash is given, we retrieve only the posts with this hash from the users inbox 
			 */
			return db.getPostsFromInboxByHash(param.getUserName(), param.getHash(), session);
		}
	
		return db.getPostsFromInbox(param.getUserName(), param.getLimit(), param.getOffset(), session);
	}

	@Override
	protected boolean canHandle(final P param) {
		return param.getGrouping() == GroupingEntity.INBOX;
	}
}