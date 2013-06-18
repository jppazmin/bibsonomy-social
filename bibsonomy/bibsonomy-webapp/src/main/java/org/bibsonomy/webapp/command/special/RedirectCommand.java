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

package org.bibsonomy.webapp.command.special;

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * 
 * Command to do redirects for
 * <ul>
 * <li>/my* pages like /myBibSonomy, /myRelations, etc.,</li>
 * <li>/redirect pages for search forms,</li>
 * <li>/uri/ content negotiation.</li>
 * </ul>
 *  
 * @author rja
 * @version $Id: RedirectCommand.java,v 1.1 2009-01-14 12:21:21 rja Exp $
 */
public class RedirectCommand extends BaseCommand {

	/**
	 * Name of the /my* page, e.g., "myRelations".
	 */
	private String myPage;

	
	/**
	 * The search terms for the general search form.
	 */
	private String search;
	/**
	 * The scope of the performed search (e.g., "user", "group", ...)
	 */
	private String scope;
	/**
	 * The user to restrict the author search to.  
	 */
	private String requUser;


	/**
	 * The URL to be used for content negotation.
	 */
	private String url;
	
	
	/** Requested URL for content negotiation.
	 * 
	 * @return The URL for content negotiation.
	 */
	public String getUrl() {
		return this.url;
	}

	/** Sets the requested URL for content negotiation.
	 * @param url 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/** Return the requested user - only relevant for /author pages, i.e., when scope=author.
	 * 
	 * @return The requested user name.
	 */
	public String getRequUser() {
		return this.requUser;
	}

	/** Set the requested user name - only relevant for /author pages, i.e., when scope=author. 
	 * @param requUser
	 */
	public void setRequUser(String requUser) {
		this.requUser = requUser;
	}

	/**
	 * @return The name of the /my* page, e.g., "myRelations".
	 */
	public String getMyPage() {
		return this.myPage;
	}

	/** Set the name of the /my* page, e.g., "myRelations".
	 * @param myPage
	 */
	public void setMyPage(String myPage) {
		this.myPage = myPage;
	}

	/** 
	 * 
	 * @return The search string.
	 */
	public String getSearch() {
		return this.search;
	}

	/** Set the search string.
	 * 
	 * @param search
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * @return The scope of a search.
	 */
	public String getScope() {
		return this.scope;
	}

	/** Sets the scope of a search.
	 * @param scope
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	
}
