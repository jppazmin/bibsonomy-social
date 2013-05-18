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

package org.bibsonomy.database.managers.chain.discussion;

import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.managers.discussion.DiscussionDatabaseManager;
import org.bibsonomy.database.params.discussion.DiscussionItemParam;
import org.bibsonomy.model.DiscussionItem;

/**
 * @author dzo
 * @version $Id: DiscussionChainElement.java,v 1.1 2011-06-16 13:55:00 nosebrain Exp $
 */
public abstract class DiscussionChainElement extends ListChainElement<DiscussionItem, DiscussionItemParam<?>> {
	
	protected final DiscussionDatabaseManager discussionDatabaseManager;
	
	/**
	 * constructs a new discussion chain element
	 */
	public DiscussionChainElement() {
		this.discussionDatabaseManager = DiscussionDatabaseManager.getInstance();
	}

}
