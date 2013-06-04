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

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * @version $Id: ScrapingService.java,v 1.11 2011-04-29 07:24:27 bibsonomy Exp $
 */
@Deprecated
public class ScrapingService extends AbstractUrlScraper {

	private static final Log log = LogFactory.getLog(ScrapingService.class);
	private static String baseurl = "";
	private final static String info = "This scraper handles several URLs by forwarding them to an external service.";
	
	static {
		try {
			baseurl = ((String) ((Context) new InitialContext().lookup("java:/comp/env")).lookup("scrapingServiceURL"));
		} catch (NamingException e) {
			log.fatal(e);
		}
	}
	
	public String getInfo() {
		return info;
	}
	
	@Override
	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		if (sc.getUrl() != null) {
			try {
				log.debug("calling external service with url " + sc.getUrl());
				URL url = new URL (baseurl + "?url=" + URLEncoder.encode(sc.getUrl().toString(), "UTF-8"));
				log.debug("calling external service " + url);
				final String content = WebUtils.getContentAsString(url);
				if (content != null && content.startsWith("% ConnoteaScraper")) {
					log.debug("got content");
					sc.setBibtexResult(content);
					/*
					 * returns itself to know, which scraper scraped this
					 */
					sc.setScraper(this);

					return true;
				}else
					throw new ScrapingFailureException("getting bibtex failed");

			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}
		}
		return false;
	}

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		List<Tuple<Pattern,Pattern>> list = new LinkedList<Tuple<Pattern,Pattern>>();
		list.add(new Tuple<Pattern, Pattern>(AbstractUrlScraper.EMPTY_PATTERN, AbstractUrlScraper.EMPTY_PATTERN));
		return list;
	}

	@Override
	public boolean supportsUrl(URL url) {
		// match every url
		return true;
	}

	@Override
	public boolean supportsScrapingContext(ScrapingContext scrapingContext) {
		// return false, this scraper is deprecated
		return false;
	}

	/**
	 * @return The name of the site, which gets scraped with this. ATTENTION: UNDEFINED (returns null) FOR SCRAPER SERVICES! 
	 * Only defined for concrete URLScraper! 
	 */
	public String getSupportedSiteName() {
		return null;
	}

	/**
	 * @return The URL of the site, which gets scraped with this. ATTENTION: UNDEFINED (returns null) FOR SCRAPER SERVICES! 
	 * Only defined for concrete URLScraper! 
	 */
	public String getSupportedSiteURL() {
		return null;
	}
	
}
