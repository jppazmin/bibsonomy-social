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

package org.bibsonomy.scraper.url.kde.wormbase;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.EndnoteToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for http://www.wormbase.org
 * @author tst
 * @version $Id: WormbaseScraper.java,v 1.13 2011-04-29 07:24:45 bibsonomy Exp $
 */
public class WormbaseScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Wormbase";
	private static final String SITE_URL = "http://www.wormbase.org/";
	private static final String INFO = "Scraper for papers from " + href(SITE_URL, SITE_NAME)+".";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + "wormbase.org"), AbstractUrlScraper.EMPTY_PATTERN));
	
	private static final Pattern pattern = Pattern.compile("name=([^;]*);");

	private static final String DOWNLOAD_URL = "http://www.textpresso.org/cgi-bin/wb/exportendnote?mode=singleentry&lit=C.%20elegans&id=";

	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);


		// get id
		final Matcher matcherName = pattern.matcher(sc.getUrl().toString());
		if(matcherName.find()) {
			final String name = matcherName.group(1);

			// get endnote
			try {
				final String endnote = WebUtils.getContentAsString(new URL(DOWNLOAD_URL + name));

				// convert bibtex
				final EndnoteToBibtexConverter converter = new EndnoteToBibtexConverter();
				String bibtex = converter.processEntry(endnote);

				if(bibtex != null){
					// append url
					bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
					
					// add downloaded bibtex to result 
					sc.setBibtexResult(bibtex);
					return true;
				}else
					throw new ScrapingFailureException("generating bibtex failed");

			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}

		}else
			throw new PageNotSupportedException("no paper ID available");
	}

	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		// TODO Auto-generated method stub
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		// TODO Auto-generated method stub
		return SITE_URL;
	}

}
