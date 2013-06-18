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

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;

/**
 * Bean for User-Sites
 *
 * @author  Dominik Benz
 * @version $Id: UserResourceViewCommand.java,v 1.9 2010-07-29 14:32:52 rja Exp $
 */
public class UserResourceViewCommand extends TagResourceViewCommand {

	/** the group whode resources are requested*/
	private ConceptsCommand concepts = new ConceptsCommand();
	/**
     * used to show infos about the user in the sidebar (only for admins, currently)
     */
	private User user;
	private int bookmarkCount = 0;
	private int bibtexCount = 0;
	private boolean isFollowerOfUser = false;
	
	/**
	 * Has the requested user added the logged in user to her friend list? 
	 */
	private boolean friendOfUser = false;
	/**
	 * Has the logged in user added the requested user to his friend list?
	 */
	private boolean ofFriendUser = false;
	
	/**
	 * defines the similarity measure by which the related users are computed  
	 * (default is folkrank)
	 */
	private String userSimilarity = UserRelation.FOLKRANK.name();
	
	/**
	 * @return the concepts
	 */
	public ConceptsCommand getConcepts() {
		return this.concepts;
	}

	/**
	 * @param concepts the concepts to set
	 */
	public void setConcepts(ConceptsCommand concepts) {
		this.concepts = concepts;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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
	 * Set user similarity 
	 * @param userSimilarity - a string describing the user similarity
	 */
	public void setUserSimilarity(String userSimilarity) {
		this.userSimilarity = userSimilarity;
	}

	/**
	 * Get user similarity 
	 * @return - the user similarity
	 */
	public String getUserSimilarity() {
		return userSimilarity;
	}

	/**
	 * Get boolean if user is following this user or if not
	 * @return true if user already follows this user and false if not
	 */
	public boolean isFollowerOfUser() {
		return this.isFollowerOfUser;
	}

	/**
	 * Set if user is following this use or if not
	 * @param isFollowerOfUser
	 */
	public void setFollowerOfUser(boolean isFollowerOfUser) {
		this.isFollowerOfUser = isFollowerOfUser;
	}

	/**
	 * @return <code>true</code> if the logged in user is in the friend list of the requested user.
	 */
	public boolean getFriendOfUser() {
		return this.friendOfUser;
	}

	/**
	 * @return <code>true</code> if the requested user is in the friend list of the logged in user.
	 */
	public boolean getOfFriendUser() {
		return this.ofFriendUser;
	}

	/**
	 * @param friendOfUser
	 */
	public void setFriendOfUser(boolean friendOfUser) {
		this.friendOfUser = friendOfUser;
	}

	/**
	 * @param ofFriendUser
	 */
	public void setOfFriendUser(boolean ofFriendUser) {
		this.ofFriendUser = ofFriendUser;
	}

}