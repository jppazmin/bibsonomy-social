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

package org.bibsonomy.scraper.url.kde.opac;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.converter.PicaToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

/**
 * @author C. Kramer
 * @version $Id: OpacScraper.java,v 1.23 2011-04-29 07:24:37 bibsonomy Exp $
 */
public class OpacScraper extends AbstractUrlScraper {
	private static final String SITE_URL = "http://opac.bibliothek.uni-kassel.de/";
	private static final String HOST_NAME = "opac.bibliothek.uni-kassel.de";
	private static final String SITE_NAME = "Bibliothek Kassel";
	private static final String info = "This scraper parses a publication page from " + href(SITE_URL , SITE_NAME);

	/**
	 * TODO: This Scraper match only on URL's with es specific query value in path and queries. The current patterns don't work.
	 */
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(HOST_NAME + ".*"), Pattern.compile(".*/PPN.*")));
	
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		//log.fatal("Opac");
		//log.fatal(sc.getUrl().toString());
		//Pattern.matches("^http.*?+/CHARSET=UTF-8/PRS=PP/PPN\\?PPN=[0-9X]+$", sc.getUrl().toString())
		//sc.getUrl().toString().startsWith(OPAC_URL)
		sc.setScraper(this);

		try {
			// create a converter and start converting :)
			final PicaToBibtexConverter converter = new PicaToBibtexConverter(sc.getPageContent(), "xml", sc.getUrl().toString());

			final String bibResult = converter.getBibResult();

			if(bibResult != null){
				sc.setBibtexResult(bibResult);
				return true;
			}else
				throw new ScrapingFailureException("getting bibtex failed");

		} catch (Exception e){
			throw new InternalFailureException(e);
		}
	}

	public String getInfo() {
		return info;
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
