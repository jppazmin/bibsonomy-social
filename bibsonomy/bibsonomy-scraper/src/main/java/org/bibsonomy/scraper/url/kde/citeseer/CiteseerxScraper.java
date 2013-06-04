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

package org.bibsonomy.scraper.url.kde.citeseer;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.WebUtils;

/**
 * @author tst
 * @version $Id: CiteseerxScraper.java,v 1.23 2011-04-29 07:24:26 bibsonomy Exp $
 */
public class CiteseerxScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "CiteSeerX";
	private static final String SITE_URL  = "http://citeseerx.ist.psu.edu/";

	private static final String INFO = "This scraper parses a publication page from the " +
									   "Scientific Literature Digital Library and Search Engine " + href(SITE_URL, SITE_NAME);
	
	private static final String HOST = "citeseerx.ist.psu.edu";
	private static final Pattern bibtexPattern = Pattern.compile("<h2>BibTeX.*?</h2>\\s*<div class=\"content\">\\s*(@.*?)\\s*</div>", Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern abstractPattern = Pattern.compile("Abstract:.*?<p class=\"para4\">(.*?)</p>", Pattern.MULTILINE | Pattern.DOTALL);
	
	private static final Pattern brokenUrlFixPattern = Pattern.compile(".*summary\\d+.*");

	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
			sc.setScraper(this);
			
			try {
				// test if url is valid
				WebUtils.getContentAsString(sc.getUrl().toString());
			} catch (IOException e) {
				// if not, try to solve the known citeseerx problem
				String url = sc.getUrl().toString();
				final Matcher m = brokenUrlFixPattern.matcher(url);
				
				if (m.matches()) {
					url = url.replace("summary", "summary?doi=");
					try {
						sc.setUrl(new URL(url));
					} catch (MalformedURLException ex) {
						throw new ScrapingException("Couldn't build new URL");
					}
				}
			}

			// check for selected bibtex snippet
			if(present(sc.getSelectedText())){
				sc.setBibtexResult(sc.getSelectedText());
				sc.setScraper(this);
				return true;
			}
			
			// no snippet selected
			String page = sc.getPageContent();
			
			// search BibTeXsnippet in html
			final Matcher matcher = bibtexPattern.matcher(page);
			
			if (matcher.find()) {
				String bibtex = matcher.group(1).replace("<br/>", "\n").replace("&nbsp;", " ");
				
				/*
				 * search abstract
				 */
				final Matcher abstractMatcher = abstractPattern.matcher(page);
				if (abstractMatcher.find()) {
					bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "abstract", abstractMatcher.group(1));
				} 
				
				// append url
				bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
				
				
				
				sc.setBibtexResult(bibtex);
				return true;

			}else
				throw new PageNotSupportedException("no bibtex snippet available");
	}
	
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
