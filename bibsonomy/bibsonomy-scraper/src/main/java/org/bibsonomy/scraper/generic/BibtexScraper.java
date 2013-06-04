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

package org.bibsonomy.scraper.generic;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringEscapeUtils;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.parser.BibtexParser;

/**
 * Search in sourcecode from the given page for bibtex and scrape it.
 * @author tst
 * @version $Id: BibtexScraper.java,v 1.11 2011-04-29 07:24:27 bibsonomy Exp $
 */
public class BibtexScraper implements Scraper {

	private static final String INFO = "Scraper for BibTeX, independent from URL.";

	public String getInfo() {
		return INFO;
	}

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}

	/**
	 * 
	 * @see org.bibsonomy.scraper.Scraper#scrape(org.bibsonomy.scraper.ScrapingContext)
	 */
	public boolean scrape(ScrapingContext sc) throws ScrapingException {
		if (sc != null && sc.getUrl() != null) {
			
			final String result = parseBibTeX(sc.getPageContent());

			if (result != null) {
				sc.setScraper(this);
				sc.setBibtexResult(result);
				return true;
			}
			
		}
		return false;
	}

	private String parseBibTeX(final String pageContent) {
		if (pageContent == null) return null;
		
		// html clean up
		final String source = StringEscapeUtils.unescapeHtml(pageContent).replace("<br/>", "\n");
		// TODO: may be some other format elements like <i>, <p> etc. are still in code
		
		try {
			
			/* 
			 * copied from SnippetScraper
			 */
			final BibtexParser parser = new BibtexParser(true);
			final BibtexFile bibtexFile = new BibtexFile();
			final BufferedReader sr = new BufferedReader(new StringReader(source));
			// parse source
			parser.parse(bibtexFile, sr);

			for (Object potentialEntry : bibtexFile.getEntries()) {
				if ((potentialEntry instanceof BibtexEntry)) {
					return potentialEntry.toString();
				}
			}
		} catch (final Exception ex) {
			/*
			 * be silent
			 * This scraper shall not throw any exceptions, since it shall just
			 * check, if the given page contains bibtex or not. If scraping is 
			 * not possible, fail silently.
			 */
		}
		return null;
	}
	
	public boolean supportsScrapingContext(final ScrapingContext sc) {
		if (sc != null && sc.getUrl() != null) {
			try {
				return parseBibTeX(sc.getPageContent()) != null;
			} catch (InternalFailureException ex) {
				return false;
			} catch (ScrapingException ex) {
				return false;
			}
		}
		return false;
	}
	
	public static ScrapingContext getTestContext() {
		try {
			return new ScrapingContext(new URL("http://de.wikipedia.org/wiki/BibTeX"));
		} catch (final MalformedURLException ex) {
			return new ScrapingContext(null);
		}
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
