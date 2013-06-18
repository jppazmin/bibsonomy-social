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

package org.bibsonomy.webapp.command.actions;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.scraper.ScrapingContext;

/**
 * FIXME: check the methods here
 * 
 * @author rja
 * @author dzo
 * @version $Id: EditPublicationCommand.java,v 1.5 2011-02-02 10:31:59 econ11 Exp $
 */
public class EditPublicationCommand extends EditPostCommand<BibTex> {
	
	/**
	 * selected text provided by bookmarklet
	 */
	private String selection;
	
	/**
	 * url provided by bookmarklet
	 */
	private String url;
	
	/**
	 * The metadata from scraping
	 */
	private ScrapingContext scrapingContext;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Sets the title of a post.
	 * Needed for the (old) postBookmark button and "copy" links.
	 * 
	 * @param description
	 */
	public void setDescription(final String description){
		this.getPost().setDescription(description); // TODO
	}
	
	/**
	 * Sets the description of a post.
	 * Needed for the (old) postBookmark button and "copy" links.
	 * 
	 * @param description
	 */
	public void setExtended(final String description){
		this.getPost().setDescription(description); // TODO
	}

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(String selection) {
		this.selection = selection;
	}

	/**
	 * @return the selection
	 */
	public String getSelection() {
		return this.selection;
	}

	/**
	 * @return The scraping context which describes where this bookmark is 
	 * coming from.
	 */
	public ScrapingContext getScrapingContext() {
		return this.scrapingContext;
	}

	/**
	 * The scraping context allows us to show the user meta information about
	 * the scraping process.
	 * 
	 * @param scrapingContext
	 */
	public void setScrapingContext(ScrapingContext scrapingContext) {
		this.scrapingContext = scrapingContext;
	}
}

