/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.command.ajax;

import java.util.List;

import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.webapp.command.GroupingCommand;

/**
 * @author dzo
 * @version $Id: DiscussionItemAjaxCommand.java,v 1.1 2011-06-16 14:06:07 nosebrain Exp $
 * @param <D> 
 */
public class DiscussionItemAjaxCommand<D extends DiscussionItem> extends AjaxCommand implements GroupingCommand {
	
	/**
	 * the discussionItem
	 */
	private D discussionItem;
	
	/**
	 * the hash of the resource
	 */
	private String hash;
	
	/**
	 * The abstract (or general) group of the post:
	 * public, private, or other 
	 */
	private String abstractGrouping;
	
	/**
	 * the groups
	 */
	private List<String> groups;
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.webapp.command.ajax.GroupingCommand#getAbstractGrouping()
	 */
	@Override
	public String getAbstractGrouping() {
		return this.abstractGrouping;
	}

	/* (non-Javadoc)
	 * @see org.bibsonomy.webapp.command.ajax.GroupingCommand#setAbstractGrouping(java.lang.String)
	 */
	@Override
	public void setAbstractGrouping(String abstractGrouping) {
		this.abstractGrouping = abstractGrouping;
	}

	/* (non-Javadoc)
	 * @see org.bibsonomy.webapp.command.ajax.GroupingCommand#getGroups()
	 */
	@Override
	public List<String> getGroups() {
		return this.groups;
	}

	/* (non-Javadoc)
	 * @see org.bibsonomy.webapp.command.ajax.GroupingCommand#setGroups(java.util.List)
	 */
	@Override
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	/**
	 * @return the comment
	 */
	public D getDiscussionItem() {
		return this.discussionItem;
	}
	
	/**
	 * @param discussionItem the comment to set
	 */
	public void setDiscussionItem(final D discussionItem) {
		this.discussionItem = discussionItem;
	}
	
	/**
	 * @return the hash
	 */
	public String getHash() {
		return this.hash;
	}
	
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}	
}
