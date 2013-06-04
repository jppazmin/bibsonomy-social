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

package org.bibsonomy.scraper.url.kde.biomed;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * @author wbi
 * @version $Id: BioMedCentralScraper.java,v 1.14 2011-04-29 07:24:43 bibsonomy Exp $
 */
public class BioMedCentralScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "BioMed Central";
	private static final String BIOMEDCENTRAL_HOST_NAME  = "http://www.biomedcentral.com";
	private static final String SITE_URL = BIOMEDCENTRAL_HOST_NAME+"/";

	private static final String info = "This Scraper parse a publication from " + href(SITE_URL, SITE_NAME)+".";

	private static final String BIOMEDCENTRAL_HOST  = "biomedcentral.com";
	private static final String BIOMEDCENTRAL_BIBTEX_PATH = "citation";
	private static final String BIOMEDCENTRAL_BIBTEX_PARAMS = "?format=bibtex&include=cit&direct=0&action=submit";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + BIOMEDCENTRAL_HOST), AbstractUrlScraper.EMPTY_PATTERN));

	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		String url = sc.getUrl().toString();

		if(!(url.endsWith("/" + BIOMEDCENTRAL_BIBTEX_PATH + "/") || 
				url.endsWith("/" + BIOMEDCENTRAL_BIBTEX_PATH) ||
				url.endsWith(BIOMEDCENTRAL_BIBTEX_PATH))) {

			if(!url.endsWith("/")) {
				url += "/" + BIOMEDCENTRAL_BIBTEX_PATH;
			} else {
				url += BIOMEDCENTRAL_BIBTEX_PATH;
			}
		}

		if(!url.endsWith("/")) {
			url += "/" + BIOMEDCENTRAL_BIBTEX_PARAMS;
		} else {
			url += BIOMEDCENTRAL_BIBTEX_PARAMS;
		}			

		try {
			sc.setUrl(new URL(url));
		} catch (MalformedURLException ex) {
			throw new InternalFailureException(ex);
		}
		String bibResult = sc.getPageContent();

		if(bibResult != null) {
			sc.setBibtexResult(bibResult);
			return true;
		}
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
