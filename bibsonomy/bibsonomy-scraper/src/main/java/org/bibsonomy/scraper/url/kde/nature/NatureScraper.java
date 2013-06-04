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

package org.bibsonomy.scraper.url.kde.nature;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for publication from nature.com
 * @author tst
 */
public class NatureScraper extends AbstractUrlScraper {

	private static final String SITE_URL = "http://www.nature.com/";

	private static final String SITE_NAME = "Nature";

	/**
	 * Host
	 */
	private static final String HOST = "nature.com";

	/**
	 * INFO
	 */
	private static final String INFO = "Scraper for publications from " + href(SITE_URL, SITE_NAME)+".";

	/**
	 * pattern for links
	 */
	private static final Pattern linkPattern = Pattern.compile("<a\\b[^<]*</a>");

	/**
	 * pattern for href field
	 */
	private static final Pattern hrefPattern = Pattern.compile("href=\"[^\"]*\"");

	/**
	 * name from download link
	 */
	private static final String CITATION_DOWNLOAD_LINK_NAME = ">Export citation<";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	/**
	 * get INFO
	 */
	public String getInfo() {
		return INFO;
	}

	/**
	 * Scrapes publications from nature.com
	 */
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		try {
			// get publication page
			final String publicationPage = sc.getPageContent();

			// extract download citation link
			final Matcher linkMatcher = linkPattern.matcher(publicationPage);
			while(linkMatcher.find()){
				String link = linkMatcher.group();

				// check if link is download link
				if(link.contains(CITATION_DOWNLOAD_LINK_NAME)){

					// get href attribute
					final Matcher hrefMatcher = hrefPattern.matcher(link);
					if(hrefMatcher.find()){
						String href = hrefMatcher.group();
						href = href.substring(6, href.length()-1);

						// download citation (as ris)
						final String ris = WebUtils.getContentAsString(new URL("http://" + sc.getUrl().getHost() + "/" + href));

						// convert ris to bibtex
						final RisToBibtexConverter converter = new RisToBibtexConverter();
						final String bibtex = converter.RisToBibtex(ris);

						// return bibtex
						if(bibtex != null){
							sc.setBibtexResult(bibtex);
							return true;
						}else
							throw new ScrapingFailureException("getting bibtex failed");

					}
				}
			}
			throw new PageNotSupportedException("Page not supported. Download URL is missing.");
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
