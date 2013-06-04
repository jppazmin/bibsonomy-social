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

package org.bibsonomy.scraper.url.kde.journalogy;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for Journalogy (Microsoft Academic Search)
 * http://www.journalogy.org
 * 
 * @author clemens
 * @version $Id: JournalogyScraper.java,v 1.3 2011-04-29 07:24:44 bibsonomy Exp $
 */
public class JournalogyScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "Journalogy (Microsoft Academic Search)";
	private static final String SITE_URL = "http://www.journalogy.org/";
	private static final String info = "This scraper parses a publication page of citations from "
			+ href(SITE_URL, SITE_NAME)+".";
	
	private static final String HOST = "journalogy.org";
	private static final String HOST2 = "academic.research.microsoft.com";

	private static final List<Tuple<Pattern, Pattern>> patterns = new LinkedList<Tuple<Pattern, Pattern>>();
	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST2), AbstractUrlScraper.EMPTY_PATTERN));
	}

	private static final Pattern pattern_download = Pattern.compile("/BibTeX.bib?type=2&id=");
	private static final Pattern pattern_id = Pattern.compile("/(Paper|Publication)/([0-9]+)");
	
	public String getSupportedSiteName() {
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}

	public String getInfo() {
		return info;
	}

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);
		try {
			// extract id
			final Matcher idMatcher = pattern_id.matcher(sc.getUrl().toString());
			
			if(idMatcher.find()) {
				String downloadLink = "http://" + HOST2 + pattern_download + idMatcher.group(2);
				String bibtex = WebUtils.getContentAsString(downloadLink);
				if (bibtex != null) {
					// add the missing ","  
					bibtex = bibtex.replaceFirst("\\{", "\\{,");					
					sc.setBibtexResult(bibtex);
					return true;
				}
			} else {
				throw new ScrapingFailureException("No bibtex available.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
}
