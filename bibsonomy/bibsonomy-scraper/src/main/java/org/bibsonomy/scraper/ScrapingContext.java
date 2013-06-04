/**
 *
 *  BibSonomy-Scraper - Web page scrapers returning BibTeX for BibSonomy.
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

package org.bibsonomy.scraper;

import java.io.IOException;
import java.net.URL;

import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.WebUtils;

/**
 * This class is used to pass all relevant data from scraper to scraper and 
 * back. Note that a scraper should also be able to cope with 
 * <code>null</code> URLs.
 * 
 */
public class ScrapingContext {

	/**
	 * The URL of the web page where the entries to scrape are.
	 */
	private URL url = null;
	/**
	 * The text the user selected on the web page. Extracted by JavaScript 
	 * method window.getSelection().
	 */
	private String selectedText = null;
	/**
	 * The content of the web page behind {@link #url}. Will be retrieved 
	 * automatically by @link #getPageContent() if not available.
	 */
	private String pageContent = null;
	/**
	 * The result of the scraping process. A string of one ore more BibTeX 
	 * entries. If a scraper is successful {@link #bibtexResult} must be not null and
	 * must contain one or more valid BibTeX entries.
	 */
	private String bibtexResult = null;
	/**
	 * An additional result of the scraper which is stored in the database to 
	 * enhance later scraping results. The format of {@link #metaResult} is up to the 
	 * scraper and will be stored as is. May be <code>null</code>.
	 */
	private String metaResult = null;
	/**
	 * The scraper which was successful in scraping.
	 */
	private Scraper scraper = null;

	/**
	 * Initially, only the URL is given (and therefore somehow mandatory). 
	 * 
	 * @param url URL of the web page to scrape.
	 */
	public ScrapingContext(final URL url) {
		this.url = url;
	}

	/**
	 * If a selection is given, you can use this constructor.
	 * @param url
	 * @param selectedText
	 */
	public ScrapingContext(final URL url, final String selectedText) {
		this.url = url;
		this.selectedText = selectedText;
	}

	
	/**
	 * Gets the content of the current URL as String.
	 * If the URL is not accessible, we set the content to "". 
	 * When scrapers see empty content although they need it, they should 
	 * return <code>false</code> on a scrape() call anyway. 
	 * 
	 * @return the page content of the current url in a string
	 * @throws ScrapingException 
	 */
	public String getPageContent() throws ScrapingException  {
		if (pageContent == null) {
			try {
				pageContent = WebUtils.getContentAsString(url);
			} catch (IOException ex) {
				throw new ScrapingException(ex);
			}
		}
		return pageContent;
	}

	/** 
	 * @return The scraped BibTeX result.
	 */
	public String getBibtexResult() {
		return bibtexResult;
	}

	/** Sets the BibTeX result.
	 * 
	 * @param bibtexResult - a valid BibTeX string. 
	 */
	public void setBibtexResult(final String bibtexResult) {
		this.bibtexResult = bibtexResult;
	}

	/**
	 * @return The text the user has selected on a web page.
	 */
	public String getSelectedText() {
		return selectedText;
	}

	/**
	 * @param selectedText - the text the user has selected.
	 */
	public void setSelectedText(final String selectedText) {
		if (!"".equals(selectedText)) {
			this.selectedText = selectedText;
		}
	}

	/**
	 * @return The current URL.
	 */
	public URL getUrl() {
		return url;
	}

	/** Set the URL
	 * @param url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return The additional meta data the scraper has stored.
	 */
	public String getMetaResult() {
		return metaResult;
	}

	/** Sets the additional meta data the scraper wants to store in the database.
	 * @param metaResult
	 */
	public void setMetaResult(final String metaResult) {
		this.metaResult = metaResult;
	}

	/**
	 * @return The scraper which has extracted the entry.
	 */
	public Scraper getScraper() {
		return scraper;
	}

	/** Set the scraper which was successful in scraping.
	 * @param scraper
	 */
	public void setScraper(final Scraper scraper) {
		this.scraper = scraper;
	}

}
