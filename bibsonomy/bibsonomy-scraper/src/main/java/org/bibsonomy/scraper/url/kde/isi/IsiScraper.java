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

package org.bibsonomy.scraper.url.kde.isi;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * @author tst
 * @version $Id: IsiScraper.java,v 1.13 2011-04-29 07:24:47 bibsonomy Exp $
 * 
 * 
 * This scraper is disabled (but still in chain for logging. The session information 
 * (and also the last visited publication) is related with the clients IP-Address. Every
 * try ends with a redirect to a error page (with the message: get a new session).
 * 
 */
public class IsiScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "ISI Web of Knowledge";
	private static final String SITE_URL = "http://apps.isiknowledge.com/";
	private static final String INFO = "Scrapes publications from " + href(SITE_URL, SITE_NAME)+".";
	
	private static final Log log = LogFactory.getLog(IsiScraper.class);
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(
			Pattern.compile(".*" + "apps.isiknowledge.com"), 
			Pattern.compile("/full_record.do" + ".*")
	));

	private static final Pattern sidPattern = Pattern.compile("SID=([^\\&]*)");
	private static final Pattern selectedIdsPattern = Pattern.compile("name=\\\"selectedIds\\\" id=\\\"selectedIds\\\" value=\\\"([^\\\"]*)");
	private static final Pattern downloadLinkPattern = Pattern.compile("href=\\\"([^\\\"]*bibtex&)\\\"><img");

	private static final String BASE_URL_1 = "http://apps.isiknowledge.com/OutboundService.do";
	private static final String BASE_URL_2 = "http://pcs.isiknowledge.com/uml/";

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);
		
		try {

			final URL pageUrl = sc.getUrl();
			// get cookie
			final String cookie = WebUtils.getCookies(new URL("http://isiknowledge.com/?DestApp=UA"));

			// get sid from url
			final Matcher sidMatcher = sidPattern.matcher(pageUrl.getQuery());
			final String sid;
			if(sidMatcher.find())
				sid = sidMatcher.group(1);
			else
				throw new ScrapingFailureException("article ID not found in URL");

			// get selectedIds from given page 
			final Matcher selectedIdsMatcher = selectedIdsPattern.matcher(WebUtils.getContentAsString(pageUrl, cookie + ";SID=" + sid + ";CUSTOMER=FAK Consortium"));
			final String selectedIds;
			if(selectedIdsMatcher.find())
				selectedIds = selectedIdsMatcher.group(1);
			else
				throw new ScrapingFailureException("selected publications not found (selectedIds is missing)");

			// build post request for getting bibtex download page

			// post content
			final String postData = 
				"action=go&" +
				"mode=quickOutput&" +
				"product=UA&" +
				"SID=" + sid + "&" +
				"format=save&" +
				"fields=FullNoCitRef&" +
				"mark_id=WOS&" +
				"count_new_items_marked=0&" +
				"selectedIds=" + selectedIds + "&" +
				"qo_fields=fullrecord&" +
				"save_options=bibtex&" +
				"save.x=27&" +
				"save.y=12&" +
				"next_mode=&" +
				"redirect_url= ";

			// call post request
			final String content = WebUtils.getPostContentAsString(cookie,  new URL(BASE_URL_1), postData);

			// extract direct bibtex download link from post result
			final Matcher downloadLinkMatcher = downloadLinkPattern.matcher(content);
			final URL downloadUrl;
			if(downloadLinkMatcher.find())
				downloadUrl = new URL(BASE_URL_2 + downloadLinkMatcher.group(1));
			else
				throw new ScrapingFailureException("cannot find BibTeX download link");

			// get bibtex
			final String bibtex = WebUtils.getContentAsString(downloadUrl, cookie);

			if(bibtex != null){
				sc.setBibtexResult(bibtex);
				return true;
			}
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}
	
		return false;		
	}

	public String getInfo() {
		return INFO;
	}

	public String getSupportedSiteName() {
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}

}
