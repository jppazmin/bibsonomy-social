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

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * @author mwa
 * @version $Id: MultiResourceViewCommand.java,v 1.4 2010-07-14 15:38:38 nosebrain Exp $
 * 
 * With this command we are able to receive multiple lists of posts for a resource. 
 * 
 */
public class MultiResourceViewCommand extends ResourceViewCommand {

	/** a list of bibtex lists **/
	private final List<ListCommand<Post<BibTex>>> listsBibTeX = new ArrayList<ListCommand<Post<BibTex>>>();

	/** a list of bookmark lists**/
	private final List<ListCommand<Post<Bookmark>>> listsBookmark = new ArrayList<ListCommand<Post<Bookmark>>>();

	/** description for a bibtex list **/
	private List<String> listsBibTeXDescription = new ArrayList<String>();

	/** description for a bookmark list **/
	private List<String> listsBookmarkDescription = new ArrayList<String>();

	
	/**
	 * @param <T> type of the entities in the list
	 * @param resourceType type of the entities in the list
	 * @return the list with entities of type resourceType
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Resource> List<ListCommand<Post<T>>> getListCommand(final Class<T> resourceType) {
		if (resourceType == BibTex.class) {
			return (List) getListsBibTeX();
		} else if (resourceType == Bookmark.class) {
			return (List) getListsBookmark();
		}
		throw new UnsupportedResourceTypeException(resourceType.getName());
	}
	
	/**
	 * @param <T> type of the entities in the list
	 * @param resourceType type of the entities in the list
	 * @return the list with entities of type resourceType
	 */
	public <T extends Resource> List<String> getListsDescription(final Class<T> resourceType) {
		if (resourceType == BibTex.class) {
			return this.getListsBibTeXDescription();
		} else if (resourceType == Bookmark.class) {
			return this.getListsBookmarkDescription();
		}
		throw new UnsupportedResourceTypeException(resourceType.getName());
	}

	/**
	 * @return the listsBibTeXDescription
	 */
	public List<String> getListsBibTeXDescription() {
		return this.listsBibTeXDescription;
	}

	/**
	 * @param listsBibTeXDescription the listsBibTeXDescription to set
	 */
	public void setListsBibTeXDescription(List<String> listsBibTeXDescription) {
		this.listsBibTeXDescription = listsBibTeXDescription;
	}

	/**
	 * @return the listsBookmarkDescription
	 */
	public List<String> getListsBookmarkDescription() {
		return this.listsBookmarkDescription;
	}

	/**
	 * @param listsBookmarkDescription the listsBookmarkDescription to set
	 */
	public void setListsBookmarkDescription(List<String> listsBookmarkDescription) {
		this.listsBookmarkDescription = listsBookmarkDescription;
	}

	/**
	 * @return the listsBibTeX
	 */
	public List<ListCommand<Post<BibTex>>> getListsBibTeX() {
		return this.listsBibTeX;
	}

	/**
	 * @return the listsBookmark
	 */
	public List<ListCommand<Post<Bookmark>>> getListsBookmark() {
		return this.listsBookmark;
	}	
}
