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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.scraper.exceptions.UsageFailureException;

/**
 * This scraper contains other scrapers and the scrape method calls them
 * until a scraper is successful.
 * 
 * @param <S> Type of scraper this scraper contains.
 * 
 */
public class CompositeScraper<S extends Scraper> implements Scraper {

	private List<S> _scrapers = new LinkedList<S>();
	private static final Log log = LogFactory.getLog(CompositeScraper.class);

	/**
	 * Call scrapers until one is successful.
	 * 
	 * @see org.bibsonomy.scraper.Scraper#scrape(org.bibsonomy.scraper.ScrapingContext)
	 */
	public boolean scrape(final ScrapingContext scrapingContext) throws ScrapingException {
		try {
			for (final S scraper : _scrapers) {
				if (scraper.scrape(scrapingContext)) {
					return true;
				}
			}
			
		} catch (final InternalFailureException e) {
			log.fatal("Exception during scraping following url: " + scrapingContext.getUrl());
			// internal failure 
			log.fatal(e,e);			
			throw (e);
		} catch (final UsageFailureException e) {
			log.info("Exception during scraping following url: " + scrapingContext.getUrl());
			// a user has used a scraper in a wrong way
			log.info(e);
			throw (e);
		} catch (final PageNotSupportedException e) {
			log.error("Exception during scraping following url: " + scrapingContext.getUrl());
			// a scraper can't scrape a page but the host is supported
			log.error(e,e);
			throw (e);
		} catch (final ScrapingFailureException e) {
			log.fatal("Exception during scraping following url: " + scrapingContext.getUrl());
			// getting bibtex failed (conversion failed)
			log.fatal(e,e);
			throw (e);
		} catch (final ScrapingException e) {
			log.error("Exception during scraping following url: " + scrapingContext.getUrl());
			// something else
			log.error(e,e);
			throw (e);
		} catch (final Exception e) {
			log.fatal("Exception during scraping following url: " + scrapingContext.getUrl());
			// unexpected internal failure 
			log.fatal(e,e);			
			throw (new InternalFailureException(e));
		}
		return false;
	}

	/**
	 * Add a scraper to list.
	 * 
	 * @param scraper
	 */
	public void addScraper(final S scraper) {
		_scrapers.add(scraper);
	}

	public String getInfo () {
		return "Generic Composite Scraper";
	}

	/** 
	 * Returns the collection of all the scrapers contained in the Composite Scraper
	 * 
	 */
	public Collection<Scraper> getScraper () {
		final LinkedList<Scraper> scrapers = new LinkedList<Scraper>();
		for (final S scraper : _scrapers) {
			scrapers.addAll(scraper.getScraper());
		}
		return scrapers;
	}

	public boolean supportsScrapingContext(final ScrapingContext scrapingContext){
		for (final S scraper : _scrapers){
			if (scraper.supportsScrapingContext(scrapingContext)){
				return true;
			}
		}
		return false;
	}

}