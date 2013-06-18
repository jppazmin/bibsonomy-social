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

package org.bibsonomy.webapp.command;

import java.util.List;

import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.GoldStandardPublication;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * command with fields for the resource lists (one list for each resource).
 * 
 * is mainly a container for two list commands (bookmarks & publications), the requested username
 * and a list of tags associated with the bookmarks / publications
 * 
 * @author Jens Illig
 * @author Dominik Benz
 * @version $Id: SimpleResourceViewCommand.java,v 1.11 2011-06-16 14:27:45 nosebrain Exp $
 */
public class SimpleResourceViewCommand extends ResourceViewCommand {
	private ListCommand<Post<Bookmark>> bookmark = new ListCommand<Post<Bookmark>>(this);
	private ListCommand<Post<BibTex>> bibtex = new ListCommand<Post<BibTex>>(this);
	private ListCommand<Post<GoldStandardPublication>> goldStandardPublications = new ListCommand<Post<GoldStandardPublication>>(this);
	// TODO: move to listcommand or use the listCommand
	private Post<GoldStandardPublication> goldStandardPublication;
	
	private List<DiscussionItem> discussionItems;
	
	/**
	 * @param <T> type of the entities in the list
	 * @param resourceType type of the entities in the list
	 * @return the list with entities of type resourceType
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Resource> ListCommand<Post<T>> getListCommand(final Class<T> resourceType) {
		if (resourceType == BibTex.class) {
			return (ListCommand) getBibtex();
		}
		
		if (resourceType == Bookmark.class) {
			return (ListCommand) getBookmark();
		}
		
		if (resourceType == GoldStandardPublication.class) {
			return (ListCommand) getGoldStandardPublications();
		}
		
		throw new UnsupportedResourceTypeException(resourceType.getName());
	}
	
	/**
	 * @return the bibtex ListView
	 */
	public ListCommand<Post<BibTex>> getBibtex() {
		return this.bibtex;
	}
	
	/**
	 * @param bibtex the bibtex ListView
	 */
	public void setBibtex(final ListCommand<Post<BibTex>> bibtex) {
		this.bibtex = bibtex;
	}
	
	/**
	 * @return the bookmark ListView
	 */
	public ListCommand<Post<Bookmark>> getBookmark() {
		return this.bookmark;
	}
	
	/**
	 * @param bookmark the bookmark ListView
	 */
	public void setBookmark(final ListCommand<Post<Bookmark>> bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * @param goldStandardPublication the goldStandardPublication to set
	 */
	public void setGoldStandardPublication(Post<GoldStandardPublication> goldStandardPublication) {
		this.goldStandardPublication = goldStandardPublication;
	}

	/**
	 * @return the goldStandardPublication
	 */
	public Post<GoldStandardPublication> getGoldStandardPublication() {
		return goldStandardPublication;
	}

	/**
	 * @param goldStandardPublications the goldStandardPublications to set
	 */
	public void setGoldStandardPublications(ListCommand<Post<GoldStandardPublication>> goldStandardPublications) {
		this.goldStandardPublications = goldStandardPublications;
	}

	/**
	 * @return the goldStandardPublications
	 */
	public ListCommand<Post<GoldStandardPublication>> getGoldStandardPublications() {
		return goldStandardPublications;
	}

	/**
	 * @return the discussionItems
	 */
	public List<DiscussionItem> getDiscussionItems() {
		return this.discussionItems;
	}

	/**
	 * @param discussionItems the discussionItems to set
	 */
	public void setDiscussionItems(List<DiscussionItem> discussionItems) {
		this.discussionItems = discussionItems;
	}
	
}