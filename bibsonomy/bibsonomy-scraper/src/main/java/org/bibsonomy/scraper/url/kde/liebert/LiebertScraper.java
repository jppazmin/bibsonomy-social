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

package org.bibsonomy.scraper.url.kde.liebert;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

/**
 * @author wbi
 * @version $Id: LiebertScraper.java,v 1.14 2011-04-29 07:24:39 bibsonomy Exp $
 */
public class LiebertScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Liebert Online";
	private static final String LIEBERT_HOST_NAME  = "http://www.liebertonline.com";	
	private static final String SITE_URL  = LIEBERT_HOST_NAME+"/";
	private static final String info = "This Scraper parses a publication from " + href(SITE_URL, SITE_NAME)+".";

	private static final String LIEBERT_HOST  = "liebertonline.com";
	private static final String LIEBERT_ABSTRACT_PATH = "/doi/abs/";
	private static final String LIEBERT_BIBTEX_PATH = "/action/showCitFormats";
	private static final String LIEBERT_BIBTEX_PATH_AND_QUERY = "/action/showCitFormats?doi=";
	private static final String LIEBERT_BIBTEX_DOWNLOAD_PATH = "/action/downloadCitation";
	private static final String LIEBERT_BIBTEX_PARAMS = "?downloadFileName=bibsonomy&include=cit&format=bibtex&direct=on&doi=";

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		final Pattern hostPattern = Pattern.compile(".*" + LIEBERT_HOST);
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, Pattern.compile(LIEBERT_ABSTRACT_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, Pattern.compile(LIEBERT_BIBTEX_PATH + ".*")));
	}
	
	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		String url = sc.getUrl().toString();

		String id = null;
		URL userURL = null;

		if(url.startsWith(LIEBERT_HOST_NAME + LIEBERT_ABSTRACT_PATH)) {
			userURL = sc.getUrl();
			int idStart = url.indexOf(LIEBERT_ABSTRACT_PATH) + LIEBERT_ABSTRACT_PATH.length();
			int idEnd = 0;

			if(url.contains("?prevSearch=")) {
				idEnd = url.indexOf("?prevSeach=");
			} else {
				idEnd = url.length();
			}

			id = url.substring(idStart, idEnd);

		}

		if(url.startsWith(LIEBERT_HOST_NAME + LIEBERT_BIBTEX_PATH_AND_QUERY)) {

			int idStart = url.indexOf("?doi=") + 5;
			int idEnd = url.length();

			id = url.substring(idStart, idEnd);

			try {
				userURL = new URL(LIEBERT_HOST_NAME + LIEBERT_ABSTRACT_PATH + id);
			} catch (MalformedURLException ex) {
				throw new InternalFailureException(ex);
			}
		}

		id = id.replaceAll("/", "%2F");

		String bibResult = null;

		try {
			URL citURL = new URL(LIEBERT_HOST_NAME + LIEBERT_BIBTEX_DOWNLOAD_PATH + LIEBERT_BIBTEX_PARAMS + id);
			bibResult = getContent(citURL, getCookies(userURL));

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
	 * @param queryURL
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	private String getContent(URL queryURL, String cookie) throws IOException {
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

	/** FIXME: refactor
	 * @param queryURL
	 * @return
	 * @throws IOException
	 */
	private String getCookies(URL queryURL) throws IOException {
		HttpURLConnection urlConn = null;

		urlConn = (HttpURLConnection) queryURL.openConnection();

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
		List<String> cookies = urlConn.getHeaderFields().get("Set-Cookie");

		StringBuffer cookieString = new StringBuffer();

		for(String cookie : cookies) {
			cookieString.append(cookie.substring(0, cookie.indexOf(";") + 1) + " ");
		}

		//This is neccessary, otherwise we don't get the Bibtex file.
		cookieString.append("I2KBRCK=1");

		urlConn.disconnect();

		return cookieString.toString();
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
