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

import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * Interface for Screen Scrapers.
 */
public interface Scraper {
	
	/** Try to retrieve BibTeX entries from the passed context data.
	 * 
	 * @param scrapingContext - data neccessary for scraping, results of scraping
	 *                          e.g. URL, content of the page, scraping results.
	 * @return True, if scraping was successful and result was stored in context.
	 * @throws ScrapingException if an exception occurs.
	 */
    public boolean scrape(final ScrapingContext scrapingContext) throws ScrapingException;
    
    /** Describe the scraper by a string. 
     * 
     * @return A string to describe the scraper. 
     */
    public String getInfo();
    
    /** A single (leaf) scraper should return Collections.singletonList(this), 
     * a {@link CompositeScraper} a union of its leaf scrapers.
     * 
     * This is used to access scrapers in linear order, for example to print information about them.
     * 
     * @return The current scraper or a list of its subscrapers.
     */
    public Collection<Scraper> getScraper ();
    
    
    
    /** Checks if this scraper can scrape the given context.
     * <br/>
     * Note that some scrapers might need to download the contents to check
     * if they can scrape it. This should not hold for {@link AbstractUrlScraper}s.
     * 
     * @param scrapingContext
     * @return <code>true</code> if the given {@link ScrapingContext} is 
     * scrapable by this scraper.
     */
    public boolean supportsScrapingContext(final ScrapingContext scrapingContext);
    

}
