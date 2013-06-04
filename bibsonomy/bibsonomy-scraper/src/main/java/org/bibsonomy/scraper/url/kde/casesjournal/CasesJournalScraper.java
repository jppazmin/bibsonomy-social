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

package org.bibsonomy.scraper.url.kde.casesjournal;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * @author claus
 * @version $Id: CasesJournalScraper.java,v 1.13 2011-04-29 07:24:47 bibsonomy Exp $
 */
public class CasesJournalScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME				= "Cases Journal";
	private static final String CASES_JOURNAL_URL_BASE	= "http://casesjournal.com";	
	private static final String SITE_URL				= CASES_JOURNAL_URL_BASE+"/";
	private static final String INFO					= "For selected BibTeX snippets and articles from " + href(SITE_URL , SITE_NAME)+".";

	/*
	 * needed URLs and components
	 */
	private static final String CASES_JOURNAL_HOST			= "casesjournal.com";
	private static final String CASES_JOURNAL_PATH			= "/content";
	private static final String CASES_JOURNAL_POST_STRING	= "action=submit&format=bibtex&include=cit";
	private static final String CASES_JOURNAL_CITATION_URL	= "/citation";
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + CASES_JOURNAL_HOST), Pattern.compile(CASES_JOURNAL_PATH + ".*")));

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext scrapingContext)
			throws ScrapingException {
		
		final String url = scrapingContext.getUrl().toString();
		String bibtex = null;
		String _url = url + CASES_JOURNAL_CITATION_URL;
		
		scrapingContext.setScraper(this);
		
		try {
			bibtex = WebUtils.getContentAsString(_url, null, CASES_JOURNAL_POST_STRING, null);
		} catch (Exception e) {
			throw new ScrapingException(e);
		}
		
		if(bibtex != null) {
			scrapingContext.setBibtexResult(bibtex);
			return true;
		}else
			throw new ScrapingFailureException("getting bibtex failed");
		
	}

	public String getInfo() {
		return INFO;
	}

	public String getSupportedSiteName() {
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}
	
}
