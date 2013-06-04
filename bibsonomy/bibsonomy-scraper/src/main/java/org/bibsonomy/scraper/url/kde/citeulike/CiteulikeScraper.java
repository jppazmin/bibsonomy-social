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

package org.bibsonomy.scraper.url.kde.citeulike;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for citeulike.org
 * @author tst
 * @version $Id: CiteulikeScraper.java,v 1.12 2011-04-29 07:24:38 bibsonomy Exp $
 */
public class CiteulikeScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "CiteUlike";

	private static final String SITE_URL = "http://www.citeulike.org/";

	private static final String INFO = "Scrapes publications from " + href(SITE_URL, SITE_NAME)+".";

	private static final String HOST = "citeulike.org";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));

	private static final String ARTICLE_POSTS = "article-posts";
	
	private static final String ARTICLE = "article";

	private static final String BIBTEX = "/bibtex";

	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);

		String downloadUrl = sc.getUrl().toString();
		
		// build bibtex download URL
		if (downloadUrl.contains(ARTICLE_POSTS)) {
			downloadUrl = downloadUrl.replace(ARTICLE_POSTS, ARTICLE);
			try {
				downloadUrl = WebUtils.getRedirectUrl(new URL(downloadUrl)).toString();
			} catch (MalformedURLException ex) {
				throw new InternalFailureException(ex);
			}
		}

		downloadUrl = downloadUrl.replace(HOST, HOST + BIBTEX);
		
		// download
		String bibtex = null;
		try {
			bibtex = WebUtils.getContentAsString(new URL(downloadUrl));
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}

		// set result
		if(bibtex != null){
			sc.setBibtexResult(bibtex);
			return true;
		}else
			throw new ScrapingFailureException("getting bibtex failed");

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
