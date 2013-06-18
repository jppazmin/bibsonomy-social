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

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.User;

/**
 * Command for ajax requests from admin page
 * 
 * @author Stefan Stützer
 * @version $Id: AdminAjaxCommand.java,v 1.2 2010-04-28 15:30:31 nosebrain Exp $
 */
public class AdminAjaxCommand extends AjaxCommand {
	
	/** list of bookmarks of an user */
	private List<Post<Bookmark>> bookmarks;
	
	/** prediction history of a user  */
	private List<User> predictionHistory;
	
	/** user for which we want to add a group or mark as spammer */
	private String userName; 
	
	/** key for updating classifier settings */
	private String key;
	
	/** value for updating classifier settings */
	private String value;
	
	/** show spam posts; enabled by default*/
	private String showSpamPosts = "true";
	
	/** total number of bookmarks*/
	private int bookmarkCount;
	
	/** total number of bibtex*/
	private int bibtexCount;
	
	/** evaluator name */
	private String evaluator;

	/**
	 * @return the bookmarks
	 */
	public List<Post<Bookmark>> getBookmarks() {
		return this.bookmarks;
	}

	/**
	 * @param bookmarks the bookmarks to set
	 */
	public void setBookmarks(final List<Post<Bookmark>> bookmarks) {
		this.bookmarks = bookmarks;
	}

	/**
	 * @return the predictionHistory
	 */
	public List<User> getPredictionHistory() {
		return this.predictionHistory;
	}

	/**
	 * @param predictionHistory the predictionHistory to set
	 */
	public void setPredictionHistory(final List<User> predictionHistory) {
		this.predictionHistory = predictionHistory;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(final String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the showSpamPosts
	 */
	public String getShowSpamPosts() {
		return this.showSpamPosts;
	}

	/**
	 * @param showSpamPosts the showSpamPosts to set
	 */
	public void setShowSpamPosts(final String showSpamPosts) {
		this.showSpamPosts = showSpamPosts;
	}

	/**
	 * @return the bookmarkCount
	 */
	public int getBookmarkCount() {
		return this.bookmarkCount;
	}

	/**
	 * @param bookmarkCount the bookmarkCount to set
	 */
	public void setBookmarkCount(final int bookmarkCount) {
		this.bookmarkCount = bookmarkCount;
	}

	/**
	 * @return the bibtexCount
	 */
	public int getBibtexCount() {
		return this.bibtexCount;
	}

	/**
	 * @param bibtexCount the bibtexCount to set
	 */
	public void setBibtexCount(final int bibtexCount) {
		this.bibtexCount = bibtexCount;
	}

	/**
	 * @return the evaluator
	 */
	public String getEvaluator() {
		return this.evaluator;
	}

	/**
	 * @param evaluator the evaluator to set
	 */
	public void setEvaluator(final String evaluator) {
		this.evaluator = evaluator;
	}
}