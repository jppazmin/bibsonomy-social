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

package org.bibsonomy.database.managers.chain.discussion.get;

import java.util.List;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.discussion.DiscussionChainElement;
import org.bibsonomy.database.params.discussion.DiscussionItemParam;
import org.bibsonomy.model.DiscussionItem;

/**
 * @author dzo
 * @version $Id: GetDiscussionSpaceByHash.java,v 1.2 2011-06-21 13:42:21 nosebrain Exp $
 */
public class GetDiscussionSpaceByHash extends DiscussionChainElement {

	@Override
	protected List<DiscussionItem> handle(final DiscussionItemParam<?> param, final DBSession session) {
		return this.discussionDatabaseManager.getDiscussionSpaceForResource(param.getInterHash(), param.getUserName(), param.getGroups(), session);
	}

	@Override
	protected boolean canHandle(final DiscussionItemParam<?> param) {
		return true; // currently only one chain element
	}

}
