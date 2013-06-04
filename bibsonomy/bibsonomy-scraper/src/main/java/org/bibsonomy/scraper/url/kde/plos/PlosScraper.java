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

package org.bibsonomy.scraper.url.kde.plos;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.EndnoteToBibtexConverter;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for X.plosjournals.org
 * @author tst
 */
public class PlosScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "PLoS";
	private static final String SITE_URL = "http://www.plos.org/journals/index.php";
	private static final String INFO = "Scraper for journals from " + href(SITE_URL, SITE_NAME)+".";

	/*
	 * ending of plos journal URLs
	 */
	private static final String PLOS_BIOLOGY_HOST_ENDING = "plosbiology.org";
	private static final String PLOS_MEDICINE_HOST_ENDING = "plosmedicine.org";
	
	private static final String HTTP = "http://";
	
	/*
	 * download url prefix
	 */
	private static final String PLOS_DOWNLOAD_URL_PREFIX = "/article/getBibTexCitation.action?articleURI=";
	
	private static final String PLOS_INFO_PATTERN_STRING = "(info:doi/.*/\\w+.\\w+.\\d+)";
	private static final Pattern PLOS_INFO_PATTERN = Pattern.compile(PLOS_INFO_PATTERN_STRING);

	/**
	 * get INFO
	 */
	public String getInfo() {
		return INFO;
	}

	private static final List<Tuple<Pattern, Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();
	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + PLOS_BIOLOGY_HOST_ENDING), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + PLOS_MEDICINE_HOST_ENDING), AbstractUrlScraper.EMPTY_PATTERN));
	}
	
	/**
	 * Scrapes journals from plos.org 
	 */
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		try {
			final Matcher _m = PLOS_INFO_PATTERN.matcher(sc.getUrl().toString());
			String info = null;
			String url  = null;
			
			if (_m.find()) {
				info = _m.group(1);
			}
			
			if (info != null && sc.getUrl().toString().contains(PLOS_BIOLOGY_HOST_ENDING)) {
				url = HTTP + PLOS_BIOLOGY_HOST_ENDING + PLOS_DOWNLOAD_URL_PREFIX + info;
			} else if (info != null && sc.getUrl().toString().contains(PLOS_MEDICINE_HOST_ENDING)) {
				url = HTTP + PLOS_MEDICINE_HOST_ENDING + PLOS_DOWNLOAD_URL_PREFIX + info;
			}
			
			String bibtexResult = WebUtils.getContentAsString(url);
			
			if(bibtexResult != null){
				
				sc.setBibtexResult(bibtexResult);
				return true;

			} else {
				throw new ScrapingFailureException("endnote is not available");
			}

		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}
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
