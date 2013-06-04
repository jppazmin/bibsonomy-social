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

package org.bibsonomy.scraper.url.kde.cambridge;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

/**
 * @author wbi
 * @version $Id: CambridgeScraper.java,v 1.14 2011-04-29 07:24:42 bibsonomy Exp $
 */
public class CambridgeScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "Cambridge Journals";
	private static final String CAMBRIDGE_HOST_NAME  = "http://journals.cambridge.org";
	private static final String SITE_URL = CAMBRIDGE_HOST_NAME+"/";
	private static final String info = "This Scraper parses a journal from " + href(SITE_URL, SITE_NAME)+".";

	private static final String CAMBRIDGE_HOST  = "journals.cambridge.org";
	private static final String CAMBRIDGE_ABSTRACT_PATH = "/action/displayAbstract";
	private static final String CAMBRIDGE_BIBTEX_DOWNLOAD_PATH = "/action/exportCitation?org.apache.struts.taglib.html.TOKEN=51cf342977f2aaa784c6ddfa66c3572c&emailid=&Download=Download&displayAbstract=No&format=BibTex&componentIds=";

	private static final Pattern idPattern = Pattern.compile("aid=([^&]*)");

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + CAMBRIDGE_HOST), Pattern.compile(CAMBRIDGE_ABSTRACT_PATH + ".*")));
	
	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		String url = sc.getUrl().toString();

		String id = null;
		URL citUrl = null;
		if(url.startsWith(CAMBRIDGE_HOST_NAME + CAMBRIDGE_ABSTRACT_PATH)) {
			final Matcher idMatcher = idPattern.matcher(url);
			if(idMatcher.find())
				id = idMatcher.group(1);
			else
				throw new ScrapingFailureException("No aid found.");

			try {
				citUrl = new URL(CAMBRIDGE_HOST_NAME + CAMBRIDGE_BIBTEX_DOWNLOAD_PATH + id);
			} catch (MalformedURLException ex) {
				throw new InternalFailureException(ex);
			}
		}

		String bibResult = null;
		try {
			bibResult = getContent(citUrl, getCookie(sc.getUrl()));
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}

		if(bibResult != null) {
			sc.setBibtexResult(bibResult);
			return true;
		}else
			throw new ScrapingFailureException("getting bibtex failed");
	}

	/** FIXME: refactor
	 * @param abstractUrl
	 * @return
	 * @throws IOException
	 */
	private String getCookie(URL abstractUrl) throws IOException{
		/*
		 * receive cookie from springer
		 */
		HttpURLConnection urlConn = null;

		urlConn = (HttpURLConnection) abstractUrl.openConnection();

		urlConn.setAllowUserInteraction(false);
		urlConn.setDoInput(true);
		urlConn.setDoOutput(false);
		urlConn.setUseCaches(false);

		/*
		 * set user agent (see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) since some 
		 * pages require it to download content.
		 */
		urlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");

		urlConn.connect();
		/*
		 * extract cookie from connection
		 */
		List<String> cookieContent = urlConn.getHeaderFields().get("Set-Cookie");
		//extract sessionID and store in cookie

		StringBuffer cookieString = new StringBuffer();

		for(String cookie : cookieContent) {
			cookieString.append(cookie.substring(0, cookie.indexOf(";") + 1) + " ");
		}

		urlConn.disconnect();

		return cookieString.toString();
	}

	/** FIXME: refactor
	 * @param queryURL
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	private String getContent(URL queryURL, String cookie) throws IOException{

		/*
		 * get BibTex-File from ACS
		 */
		HttpURLConnection urlConn = (HttpURLConnection) queryURL.openConnection();
		urlConn.setAllowUserInteraction(false);
		urlConn.setDoInput(true);
		urlConn.setDoOutput(false);
		urlConn.setUseCaches(false);
		/*
		 * set user agent (see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) since some 
		 * pages require it to download content.
		 */
		urlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
		//insert cookie
		urlConn.setRequestProperty("Cookie", cookie);
		urlConn.connect();
		StringWriter out = new StringWriter();
		InputStream in = new BufferedInputStream(urlConn.getInputStream());
		int b;
		while ((b = in.read()) >= 0) {
			out.write(b);
		}
		urlConn.disconnect();
		return out.toString();
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
