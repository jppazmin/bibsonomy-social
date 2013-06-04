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

package org.bibsonomy.scraper.url.kde.googlescholar;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * This scraper supports download links from the GoogleSonomy Firefox plugin.
 * 
 * @author tst
 * @version $Id: GoogleScholarScraper.java,v 1.6 2011-04-29 07:24:30 bibsonomy Exp $
 */
public class GoogleScholarScraper extends AbstractUrlScraper {
	
	private static final String SITE_URL  = "http://scholar.google.com/";
	private static final String SITE_NAME = "Google Scholar";
	private static final String INFO      = "Scrapes BibTeX from " + href(SITE_URL, SITE_NAME) + ".";

	private static final String HOST = "scholar.google.";
	private static final String PATH = "/scholar.bib";
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST + ".*"), Pattern.compile(PATH + ".*")));
	
	@Override
	protected boolean scrapeInternal(final ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);
		
		try {
			// get cookie
			String cookie = WebUtils.getCookies(sc.getUrl());
			
			if (cookie != null) {
				// add :CF=4 to cookie value GSP=ID=
				final int index = cookie.indexOf(";", cookie.indexOf("GSP=ID="));
				cookie = cookie.substring(0, index) + ":CF=4" + cookie.substring(index);
				
				// download bibtex
				String bibtex = WebUtils.getContentAsString(sc.getUrl().toString(), cookie);
				
				if(bibtex != null){
					// append url
					bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
					
					// add downloaded bibtex to result 
					sc.setBibtexResult(bibtex);
					return true;
				}
				
				throw new ScrapingFailureException("bibtex download failed");
				
			}
			
			throw new ScrapingFailureException("Cannot get cookie");
			
		} catch (final IOException ex) {
			throw new InternalFailureException(ex);
		}
		
	}

	public String getInfo() {
		return INFO;
	}

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}
	
}
