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

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * Scrapers of this type can decide using only the URL, if they
 * support the given {@link ScrapingContext} or not. 
 * 
 * @author rja
 * @version $Id: AbstractUrlScraper.java,v 1.7 2011-04-29 07:24:31 bibsonomy Exp $
 */
public abstract class AbstractUrlScraper implements UrlScraper {

	/**
	 * If a scraper does not need to check the path (or host) of a 
	 * the URL, it should return this value as pattern. 
	 */
	protected static final Pattern EMPTY_PATTERN = null;

	
	/* (non-Javadoc)
	 * @see org.bibsonomy.scraper.UrlScraper#getUrlPatterns()
	 */
	public abstract List<Tuple<Pattern,Pattern>> getUrlPatterns();

	/* (non-Javadoc)
	 * @see org.bibsonomy.scraper.UrlScraper#supportsUrl(java.net.URL)
	 */
	public boolean supportsUrl(final URL url) {
		if (url != null) {
			final List<Tuple<Pattern, Pattern>> urlPatterns = getUrlPatterns();

			/*
			 * possible matching combinations:
			 * first = true && second = true
			 * first = true && second = null
			 * first = null && second = true
			 */
			for (final Tuple<Pattern, Pattern> tuple: urlPatterns){
				final boolean match1 = tuple.getFirst() == EMPTY_PATTERN ||
				tuple.getFirst().matcher(url.getHost()).find();

				final boolean match2 = tuple.getSecond() == EMPTY_PATTERN || 
				tuple.getSecond().matcher(url.getPath()).find();

				if (match1 && match2) return true;
			}
		}
		return false;
	}

	/**
	 * Builds a href to the URL with the given anchor text.
	 *  
	 * @param url
	 * @param text
	 * @return a href html element
	 */
	public static String href(final String url, final String text) {
		return "<a href=\"" + url + "\">" + text + "</a>";
	}

	/**
	 * Scrapes the given context.
	 * 
	 * @see org.bibsonomy.scraper.Scraper#scrape(org.bibsonomy.scraper.ScrapingContext)
	 */
	public boolean scrape(final ScrapingContext sc) throws ScrapingException {
		if (sc != null && supportsUrl(sc.getUrl())) {
			return scrapeInternal(sc);
		}
		return false;
	}
	
	
	/** This method is called by {@link #scrape(ScrapingContext)}, when the URL is supported
	 * by the scraper.
	 *  
	 * @param scrapingContext
	 * @return <code>true</code> if the scraping was successful.
	 * @throws ScrapingException
	 */
	protected abstract boolean scrapeInternal(final ScrapingContext scrapingContext) throws ScrapingException;

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}
	
	public boolean supportsScrapingContext(ScrapingContext scrapingContext) {
		return supportsUrl(scrapingContext.getUrl());
	}

}
