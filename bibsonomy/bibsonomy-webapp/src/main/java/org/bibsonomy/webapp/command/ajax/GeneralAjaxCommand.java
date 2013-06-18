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

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;

/**
 * General command for ajax requests
 * 
 * @author fba
 * @version $Id: GeneralAjaxCommand.java,v 1.1 2010-04-28 15:30:31 nosebrain Exp $
 */
public class GeneralAjaxCommand extends AjaxCommand {
	/**
	 * page title
	 */
	private String pageTitle; 	
	/**
	 * page URL
	 */
	private String pageURL;	
	/**
	 * page description
	 */
	private String pageDescription;	
	/**
	 * page keywords
	 */
	private String pageKeywords;
	/**
	 * generic query parameter
	 */
	private String q;
	
	/**
	 * generic user name parameter
	 */
	private String requestedUser;
	
	/**
	 * a list of bibtexs 
	 */
	private List<Post<BibTex>> bibtexPosts;

	/**
	 * @return the pageTitle
	 */
	@Override
	public String getPageTitle() {
		return this.pageTitle;
	}

	/**
	 * @param pageTitle the pageTitle to set
	 */
	@Override
	public void setPageTitle(final String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the pageURL
	 */
	public String getPageURL() {
		return this.pageURL;
	}

	/**
	 * @param pageURL the pageURL to set
	 */
	public void setPageURL(final String pageURL) {
		this.pageURL = pageURL;
	}

	/**
	 * @return the pageDescription
	 */
	public String getPageDescription() {
		return this.pageDescription;
	}

	/**
	 * @param pageDescription the pageDescription to set
	 */
	public void setPageDescription(final String pageDescription) {
		this.pageDescription = pageDescription;
	}

	/**
	 * @return the pageKeywords
	 */
	public String getPageKeywords() {
		return this.pageKeywords;
	}

	/**
	 * @param pageKeywords the pageKeywords to set
	 */
	public void setPageKeywords(final String pageKeywords) {
		this.pageKeywords = pageKeywords;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return this.q;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(final String q) {
		this.q = q;
	}

	/**
	 * @return the requestedUser
	 */
	public String getRequestedUser() {
		return this.requestedUser;
	}

	/**
	 * @param requestedUser the requestedUser to set
	 */
	public void setRequestedUser(final String requestedUser) {
		this.requestedUser = requestedUser;
	}

	/**
	 * @return the bibtexPosts
	 */
	public List<Post<BibTex>> getBibtexPosts() {
		return this.bibtexPosts;
	}

	/**
	 * @param bibtexPosts the bibtexPosts to set
	 */
	public void setBibtexPosts(final List<Post<BibTex>> bibtexPosts) {
		this.bibtexPosts = bibtexPosts;
	}

}
