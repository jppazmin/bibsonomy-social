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

package org.bibsonomy.scraper.url.kde.openrepository;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for openrepository pages
 * @author tst
 * @version $Id: OpenrepositoryScraper.java,v 1.13 2011-04-29 07:24:39 bibsonomy Exp $
 */
public class OpenrepositoryScraper extends AbstractUrlScraper {

	private static final String SITE_URL = "http://openrepository.com/";
	private static final String SITE_NAME = "Open Repository";
	private static final String SUPPORTED_HOST_OPENREPOSITORY = "openrepository.com";
	private static final String SUPPORTED_HOST_E_SPACE = "e-space.mmu.ac.uk";
	private static final String SUPPORTED_HOST_E_SPACE_PATH = "/e-space";
	private static final String SUPPORTED_HOST_HIRSLA = "hirsla.lsh.is";
	private static final String SUPPORTED_HOST_HIRSLA_PATH = "/lsh";
	private static final String SUPPORTED_HOST_GTCNI = "arrts.gtcni.org.uk";
	private static final String SUPPORTED_HOST_GTCNI_PATH = "/gtcni";
	private static final String SUPPORTED_HOST_EXETER = "eric.exeter.ac.uk";
	private static final String SUPPORTED_HOST_EXETER_PATH = "/exeter";

	private static final String PATTERN_HANDLE = "handle/(.*)";

	private static final String INFO = "Supports the following repository: " + href(SITE_URL, SITE_NAME) + ".";

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>(); 
	
	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SUPPORTED_HOST_OPENREPOSITORY), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SUPPORTED_HOST_E_SPACE), Pattern.compile(SUPPORTED_HOST_E_SPACE_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SUPPORTED_HOST_EXETER), Pattern.compile(SUPPORTED_HOST_EXETER_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SUPPORTED_HOST_GTCNI), Pattern.compile(SUPPORTED_HOST_GTCNI_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SUPPORTED_HOST_HIRSLA), Pattern.compile(SUPPORTED_HOST_HIRSLA_PATH + ".*")));
	}
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		String downloadURL = null;

		final String url = sc.getUrl().toString();
		if(url.contains(SUPPORTED_HOST_OPENREPOSITORY)){
			downloadURL = "http://www." + SUPPORTED_HOST_OPENREPOSITORY + "/references?format=refman&handle=" + getHandle(url);
		}else if(url.contains(SUPPORTED_HOST_E_SPACE + SUPPORTED_HOST_E_SPACE_PATH)){
			downloadURL = "http://www." + SUPPORTED_HOST_E_SPACE + SUPPORTED_HOST_E_SPACE_PATH + "/references?format=refman&handle=" + getHandle(url);
		}else if(url.contains(SUPPORTED_HOST_EXETER + SUPPORTED_HOST_EXETER_PATH)){
			downloadURL = "http://www." + SUPPORTED_HOST_EXETER + SUPPORTED_HOST_EXETER_PATH + "/references?format=refman&handle=" + getHandle(url);
		}else if(url.contains(SUPPORTED_HOST_HIRSLA + SUPPORTED_HOST_HIRSLA_PATH)){
			downloadURL = "http://www." + SUPPORTED_HOST_HIRSLA + SUPPORTED_HOST_HIRSLA_PATH + "/references?format=refman&handle=" + getHandle(url);
		}else if(url.contains(SUPPORTED_HOST_GTCNI + SUPPORTED_HOST_GTCNI_PATH)){
			downloadURL = "http://" + SUPPORTED_HOST_GTCNI + SUPPORTED_HOST_GTCNI_PATH + "/references?format=refman&handle=" + getHandle(url);
		}

		if(downloadURL != null){
			sc.setScraper(this);

			try {
				String ris = WebUtils.getContentAsString(new URL(downloadURL));

				RisToBibtexConverter converter = new RisToBibtexConverter();
				String bibtex = converter.RisToBibtex(ris);

				if(bibtex != null){
					sc.setBibtexResult(bibtex);
					return true;
				}else
					throw new ScrapingFailureException("getting bibtex failed");

			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}
		}// else page is not supported. may be other scraper hits
		return false;
	}

	/**
	 * get handle id from url
	 * @param url
	 * @return id, null if matching failed
	 */
	private String getHandle(String url){
		String handle = null;

		Pattern handlePattern = Pattern.compile(PATTERN_HANDLE);
		Matcher handleMatcher = handlePattern.matcher(url);
		if(handleMatcher.find())
			handle = handleMatcher.group(1);

		return handle;
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
