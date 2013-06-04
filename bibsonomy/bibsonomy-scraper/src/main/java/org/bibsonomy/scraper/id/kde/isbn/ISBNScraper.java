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

package org.bibsonomy.scraper.id.kde.isbn;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.scraper.url.kde.amazon.AmazonScraper;
import org.bibsonomy.scraper.url.kde.worldcat.WorldCatScraper;
import org.bibsonomy.util.id.ISBNUtils;

/**
 * Scraper for ISBN support. Searchs for ISBN in snippet and uses WorldcatScraper
 * or AmazonScraper for download.
 *  
 * @author tst
 * @version $Id: ISBNScraper.java,v 1.18 2011-04-29 07:24:45 bibsonomy Exp $
 */
public class ISBNScraper implements Scraper {
	
	private static final String INFO = "ISBN/ISSN support in scraped snippet";
	
	// need to add these parameter to receiver the correct journal
	private static final String ADV_PARAM = "&dblist=638&fq=dt%3Aser&qt=facet_dt%3A";

	public String getInfo() {
		return INFO;
	}
	
	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}
	
	public boolean scrape(final ScrapingContext sc) throws ScrapingException {
		if (sc != null && sc.getSelectedText() != null) {
			final String isbn = ISBNUtils.extractISBN(sc.getSelectedText());
			final String issn = ISBNUtils.extractISSN(sc.getSelectedText());
			
			if (present(isbn)) {
				try {
					String bibtex = WorldCatScraper.getBibtexByISBN(isbn);
					
					if (!present(bibtex)) {
						bibtex = AmazonScraper.getBibtexByISBN(isbn);
					}
					
					if (present(bibtex)) {
						sc.setBibtexResult(bibtex);
						sc.setScraper(this);
						return true;
					}
					
					throw new ScrapingFailureException("bibtex download from worldcat / amazon failed");
				} catch (final IOException ex) {
					throw new InternalFailureException(ex);
				}

			} else if (present(issn)){
				try {
					String bibtex = WorldCatScraper.getBibtexByISSN(issn+ADV_PARAM);
					
					if (present(bibtex)){
						sc.setBibtexResult(bibtex);
						sc.setScraper(this);
						return true;
					} else {
						bibtex = WorldCatScraper.getBibtexByISSN(issn);
						if (present(bibtex)){
							sc.setBibtexResult(bibtex);
							sc.setScraper(this);
							return true;
						} else {
							throw new ScrapingFailureException("bibtex download from worldcat");
						}
					}
				} catch (final IOException ex) {
					throw new InternalFailureException(ex);
				}
			}
		}
		
		return false;
	}
	
	public boolean supportsScrapingContext(ScrapingContext sc) {
		if(sc.getSelectedText() != null){
			final String isbn = ISBNUtils.extractISBN(sc.getSelectedText());
			final String issn = ISBNUtils.extractISSN(sc.getSelectedText());
			if (isbn != null || issn != null)
				return true;
		}
		return false;
	}
	
	public static ScrapingContext getTestContext(){
		final ScrapingContext context = new ScrapingContext(null);
		context.setSelectedText("9783608935448");
		return context;
	}
	
	/**
	 * @return site name
	 */
	public String getSupportedSiteName(){
		return null;
	}
	
	
	/**
	 * @return site url
	 */
	public String getSupportedSiteURL(){
		return null;
	}

}
