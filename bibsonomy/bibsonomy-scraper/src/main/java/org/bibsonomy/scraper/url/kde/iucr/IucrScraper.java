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

package org.bibsonomy.scraper.url.kde.iucr;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.scraper.exceptions.UsageFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * scraper for jornals from iucr.org. Because of the frame structure of 
 * journals.iucr.org pages only issues can be scraped which are sperated in a
 * another tab then the journal itself. The url of the issue in the new tab 
 * points dirctly to the issue and not to jounal page (if you open the issue in 
 * the same tab, then the url in the navgationbar will still point to the journal 
 * page and scraping is not possible.
 * 
 * example:
 * we want the second issue from this journal ->
 * http://journals.iucr.org/b/issues/2008/03/00/issconts.html
 * 
 * if we open the doi-link in the same tab, we get this url ->
 * http://journals.iucr.org/b/issues/2008/03/00/issconts.html
 * 
 * the issue will only be loaded in the central frame of the page and has no 
 * effect on the url. So we cannot recognize which issue was selected by the user.
 * If the user open the doi-link in a new tab, only the content of the central 
 * frame will be loaded and we can get the URL to the page of the issue. Like this
 * one ->
 * http://scripts.iucr.org/cgi-bin/paper?S0108768108005119
 * 
 * The rest is simple: extract the cnor from the form and build a download link,
 * like this -> http://scripts.iucr.org/cgi-bin/biblio?Action=download&cnor=ck5030&saveas=BIBTeX
 * 
 * @author tst
 * @version $Id: IucrScraper.java,v 1.12 2011-04-29 07:24:41 bibsonomy Exp $
 */
public class IucrScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "International Union of Crystallography";
	private static final String SITE_URL = "http://www.iucr.org/";
	private static final String INFO = "Scraper for journals from the " + href(SITE_URL, SITE_NAME) +".";
	
	/*
	 * messages
	 */
	private static final String USEAGE_FAILURE_MESSAGE = "Please open the publication in a new browser tab and post it again.";

	/*
	 * hosts
	 */

	private static final String HOST = "iucr.org";

	private static final String HOST_JOURNAL_PREFIX = "journal";

	private static final String HOST_SCRIPTS_PREFIX = "scripts";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	private static final Pattern cnorPattern = Pattern.compile("<input name=\"cnor\" value=\"([^\"]*)\" type=\"hidden\">");

	/*
	 * Download link
	 */
	private static final String DOWNLOAD_LINK_PART = "http://scripts.iucr.org/cgi-bin/biblio?Action=download&saveas=BIBTeX&cnor=";


	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);

		if(sc.getUrl().getHost().startsWith(HOST_JOURNAL_PREFIX)){
			throw new UsageFailureException(USEAGE_FAILURE_MESSAGE);

		}else if(sc.getUrl().getHost().startsWith(HOST_SCRIPTS_PREFIX)){

			try {
				final String pageContent = sc.getPageContent();

				// extract cnor number from HTML
				final Matcher cnorMatcher = cnorPattern.matcher(pageContent);
				if(cnorMatcher.find()) {
					final String cnor = cnorMatcher.group(1);

					// download bibtex
					final String bibtex = WebUtils.getContentAsString(new URL((DOWNLOAD_LINK_PART + cnor)));

					if(bibtex != null){

						// successful
						sc.setBibtexResult(bibtex);
						return true;

					}else{
						// bibtex == null, may be wrong download url
						throw new ScrapingFailureException("Bibtex download failed. Bibtex result is null.");
					}

					// can't extract cnor
				}else{
					// missing id
					throw new ScrapingFailureException("ID for donwload link is missing.");
				}

			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}

		}else{
			// no journal or scripts page
			throw new PageNotSupportedException(PageNotSupportedException.DEFAULT_ERROR_MESSAGE + this.getClass().getName());
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
