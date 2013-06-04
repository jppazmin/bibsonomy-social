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

package org.bibsonomy.scraper.url.kde.wileyintersience;

import java.io.IOException;
import java.net.MalformedURLException;
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
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.util.WebUtils;


/**
 * Scraper for www3.interscience.wiley.com
 * @author rja
 * @version $Id: WileyIntersienceScraper.java,v 1.13 2011-04-29 07:24:44 bibsonomy Exp $
 */
public class WileyIntersienceScraper extends AbstractUrlScraper {

	private static final String SITE_HOST = "onlinelibrary.wiley.com";
	private static final String SITE_URL  = "http://" + SITE_HOST + "/";
	private static final String SITE_NAME = "InterScience";
	private static final String INFO = "Extracts publications from the abstract page of " + href(SITE_URL,SITE_NAME) + ".";
	private static final Pattern DOI_PATTERN = Pattern.compile("/doi/(.+)/.*");


	private static final List<Tuple<Pattern,Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SITE_HOST), DOI_PATTERN)); 

		
	/**
	 * Scraper for onlinelibrary.wiley.com
	 * 
	 * supported page:
	 * - abtsract page
	 * - download citation page
	 */
	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);
		/*
		 * extract DOI
		 */
		final String path = sc.getUrl().getPath();
		final Matcher doiMatcher = DOI_PATTERN.matcher(path);
		if (doiMatcher.matches()) {
			final String doi = doiMatcher.group(1);
			/*
			 * build something like 
			 * doi=10.1002%252F1521-4095%28200011%2912%253A22%253C1655%253A%253AAID-ADMA1655%253E3.0.CO%253B2-2&fileFormat=BIBTEX&hasAbstract=CITATION_AND_ABSTRACT
			 */
			final String postContent = "doi=" + UrlUtils.safeURIEncode(doi) + "&fileFormat=BIBTEX&hasAbstract=CITATION_AND_ABSTRACT";
			final String url = SITE_URL + "documentcitationdownloadformsubmit";

			try {
				final String cookies = WebUtils.getCookies(sc.getUrl());
				final String bibtex = WebUtils.getPostContentAsString(cookies, new URL(url), postContent);
				sc.setBibtexResult(bibtex);
				return true;
			}catch (MalformedURLException e) {
				throw new InternalFailureException(e);
			}catch (IOException ioe){
				throw new InternalFailureException(ioe);
			}
		}
		return false;
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
		return SITE_HOST;
	}

	private boolean containsMandatoryEndnoteInformation(String endnote)
	{
		return 	endnote.contains("AU:") &&
		endnote.contains("TI:") &&
		endnote.contains("YR:");
	}
}
