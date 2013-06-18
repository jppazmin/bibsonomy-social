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

import org.bibsonomy.model.Post;

/**
 * bean for listviews across multiple browsable pages 
 * 
 * @param <T> type of the entities in the list 
 *  
 * @author Jens Illig
 * @version $Id: ListCommand.java,v 1.12 2010-04-28 13:53:52 nosebrain Exp $
 */
public class ListCommand<T> {
	private int numPreviousPages = 2;
	private int numNextPages = 2;
	private int entriesPerPage = -1;
	/** we store the parent command here in order to be able to access the default settings for entriesPerPage therein */
	private final ContextCommand parentCommand;

	private final PageCommand curPage = new PageCommand();
	private List<PageCommand> previousPages;
	private List<PageCommand> nextPages;
	private int totalCount = 0;
	private List<T> list;
	
	/**
	 * Constructor
	 * 
	 * @param parentCommand - the command which contains this list
	 */
	public ListCommand(final ContextCommand parentCommand) {
		this.parentCommand = parentCommand;
	}

	/**
	 * Constructor
	 * 
	 * @param parentCommand - the command which contains this list
	 * @param list - the list this command contains 
	 */
	public ListCommand(final ContextCommand parentCommand, final List<T> list) {
		this.parentCommand = parentCommand;
		this.list = list;
	}
	
	/**
	 * @return the sublistlist on the current page
	 */
	public List<T> getList() {
		return this.list;
	}
	/**
	 * @param list the sublistlist on the current page
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
	
	/**
	 * @return inclusive start index of the current page
	 */
	public int getStart() {
		return this.curPage.getStart();
	}
	/**
	 * @param start inclusive start index of the current page
	 */
	public void setStart(int start) {
		this.curPage.setStart(start);
		this.curPage.setNumber(null);
		this.previousPages = null;
		this.nextPages = null;
	}
	
	/**
	 * @param totalCount size of the list without window limits or offsets
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.previousPages = null;
		this.nextPages = null;
	}
	/**
	 * @return size of the list without window limits or offsets
	 */
	public int getTotalCount() {
		// if no entries are displayed, our navigation doesn't make sense 
		if (this.entriesPerPage == 0) {
			return 0;
		}
		return this.totalCount;
	}
	
	/**
	 * @return the last starting index
	 */
	public int getLast() {
		if (this.entriesPerPage == 0) {
			return 1;
		}
		if (this.totalCount % this.entriesPerPage == 0) {
			return this.totalCount - this.entriesPerPage;
		}		
		return this.totalCount - (this.totalCount % this.entriesPerPage);

	}
	
	/**
	 * @return list of available pages before the current page. An upper
	 *         limit on the previous pages can be specified by
	 *         {@link #setNumPreviousPages(int)} 
	 */
	public List<PageCommand> getPreviousPages() {
		if (this.previousPages == null) {
			this.previousPages = new ArrayList<PageCommand>();
			for (int i = (this.numPreviousPages >= this.getCurPage().getNumber()) ? 1 : this.getCurPage().getNumber() - this.numPreviousPages; i < this.getCurPage().getNumber(); ++i) {
				final int start = (i - 1) * this.entriesPerPage;
				this.previousPages.add(new PageCommand(i, start));
			}
		}
		return this.previousPages;
	}
	
	/**
	 * @return list of available pages following the current page. An upper
	 *         limit on the next pages can be specified by
	 *         {@link #setNumNextPages(int)} 
	 */
	public List<PageCommand> getNextPages() {
		if (this.nextPages == null) {
			this.nextPages = new ArrayList<PageCommand>();
			for (int i = 1; i <= this.numNextPages; ++i) {
				final int start = this.curPage.getStart() + i * this.entriesPerPage;
				if (start < this.totalCount || this.totalCount == 0) {
					this.nextPages.add(new PageCommand(this.getCurPage().getNumber() + i, start));
				}
			}
		}
		return this.nextPages;
	}
	
	/**
	 * @return the page before the current page. null if the current page is the first one. 
	 */
	public PageCommand getPreviousPage() {
		final List<PageCommand> prev = this.getPreviousPages();
		if (prev.size() > 0) {
			return prev.get(prev.size() - 1);
		}
		return null;
	}
	
	/**
	 * @return the page following the current page. null if the current page
	 *         is the last one.
	 */
	public PageCommand getNextPage() {
		final List<PageCommand> next = this.getNextPages();
		if (next.size() > 0) {
			return next.get(0);
		}
		return null;
	}
	
	/**
	 * @param numNextPages an upper limit for the size of the list returned
	 *        by {@link #getNextPages()}
	 */
	public void setNumNextPages(int numNextPages) {
		this.numNextPages = numNextPages;
	}
	/**
	 * @param numPreviousPages an upper limit for the size of the list returned
	 *        by {@link #getPreviousPages()}
	 */
	public void setNumPreviousPages(int numPreviousPages) {
		this.numPreviousPages = numPreviousPages;
	}
	
	/**
	 * @param entriesPerPage number of entities to be diplayed on one page
	 */
	public void setEntriesPerPage(int entriesPerPage) {
		this.entriesPerPage = entriesPerPage;
		this.curPage.setNumber(null);
		this.previousPages = null;
		this.nextPages = null;
	}
	
	/**
	 * @return the current page
	 */
	public PageCommand getCurPage() {
		if (this.curPage.getNumber() == null) {
			if (this.entriesPerPage == 0) {
				this.curPage.setNumber(1);
			}
			else {
				this.curPage.setNumber( (this.curPage.getStart() + this.entriesPerPage - 1) / this.entriesPerPage + 1);
			}
		}
		return this.curPage;
	}
	
	/**
	 * @return entries per page
	 */
	public int getEntriesPerPage() {
		if (this.entriesPerPage == -1) {
			// fallback to user settings, if not set explicitly before via url parameter
			this.entriesPerPage = this.parentCommand.getContext().getLoginUser().getSettings().getListItemcount(); 
		}
		return this.entriesPerPage;  
	}
	
	/**
	 * @return the resource type of the posts in the list
	 */
	public String getResourcetype() {
		try {
			if (list.get(0) != null) {
				T item = list.get(0);
				if (item instanceof Post<?>) {
					Post<?> postItem = (Post<?>) item;
					return postItem.getResource().getClass().getSimpleName().toLowerCase();
				}
			}
		} catch (Exception ex) {
			// ignore it
		}
		return null;
	}
	
	/**
	 * @return number of current items
	 */
	public int getNumCurrentItems() {
		if (this.list != null) {
			return this.list.size();
		}
		return 0;
	}
}
