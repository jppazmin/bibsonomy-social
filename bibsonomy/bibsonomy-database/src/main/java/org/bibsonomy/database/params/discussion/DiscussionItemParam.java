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

package org.bibsonomy.database.params.discussion;

import org.bibsonomy.database.common.enums.DiscussionItemType;
import org.bibsonomy.database.params.GenericParam;
import org.bibsonomy.model.DiscussionItem;

/**
 * @param <D> 
 * @author dzo
 * @version $Id: DiscussionItemParam.java,v 1.2 2011-06-21 13:42:22 nosebrain Exp $
 */
public class DiscussionItemParam<D extends DiscussionItem> extends GenericParam {
	
	private String interHash;
	private D discussionItem;

	/**
	 * @return the interHash
	 */
	public String getInterHash() {
		return this.interHash;
	}

	/**
	 * @param interHash the interHash to set
	 */
	public void setInterHash(final String interHash) {
		this.interHash = interHash;
	}
	
	/**
	 * @return the discussion item
	 */
	public D getDiscussionItem() {
		return this.discussionItem;
	}

	/**
	 * @param discussionItem the discussion item to set
	 */
	public void setDiscussionItem(final D discussionItem) {
		this.discussionItem = discussionItem;
	}
	
	/**
	 * @return the type of the discussion item
	 */
	public DiscussionItemType getDiscussionItemType() {
		return DiscussionItemType.DISCUSSION_ITEM;
	}
}
