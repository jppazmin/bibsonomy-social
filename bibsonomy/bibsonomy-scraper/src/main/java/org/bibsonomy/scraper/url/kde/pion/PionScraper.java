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

package org.bibsonomy.scraper.url.kde.pion;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * Scraping Logger for access on http://pion.co.uk/
 * @author tst
 * @version $Id: PionScraper.java,v 1.11 2011-04-29 07:24:40 bibsonomy Exp $
 */
public class PionScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Pion";

	private static final String SITE_URL = "http://pion.co.uk/";

	private static final Log log = LogFactory.getLog(PionScraper.class);

	private static final String INFO = "Scraper for publications from " + href(SITE_URL, SITE_NAME)+".";

	private static final String HOST = "pion.co.uk";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		// log message
		log.debug("Observed Scraper called: PionScraper is called with " + sc.getUrl().toString());

		// TODO: throw exception or not?
		// throw new PageNotSupportedException("This Page is currently not supported");
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
