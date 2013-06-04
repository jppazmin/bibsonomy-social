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

package org.bibsonomy.scraper.url.kde.anthrosource;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * @author wbi
 * @version $Id: AnthroSourceScraper.java,v 1.15 2011-04-29 07:24:44 bibsonomy Exp $
 */
public class AnthroSourceScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "AnthroSource";

	private static final String SITE_URL = "http://www.anthrosource.net/";

	private Log log = LogFactory.getLog(AnthroSourceScraper.class);

	private static final String info = "This Scraper parses a publication from " + href(SITE_URL, SITE_NAME)+".";

	private static final String AS_HOST  = "anthrosource.net";
	private static final String AS_ABSTRACT_PATH = "/doi/abs/";
	private static final String AS_BIBTEX_PATH = "/action/showCitFormats";

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		final Pattern hostPattern = Pattern.compile(".*" + AS_HOST);
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, Pattern.compile(AS_ABSTRACT_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, Pattern.compile(AS_BIBTEX_PATH + ".*")));
	}
	
	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		log.debug("Observed Scraper called: AnthroSourceScraper is called with " + sc.getUrl().toString());
		
		return false;
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
