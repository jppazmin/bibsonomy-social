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

package org.bibsonomy.scraper.url.kde.jstor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
 * @version $Id: JStorScraper.java,v 1.13 2011-04-29 07:24:36 bibsonomy Exp $
 */
public class JStorScraper extends AbstractUrlScraper {

	private static final String info = "This Scraper parses a publication from " + href("http://www.jstor.org/", "JSTOR");

	private static final String JSTOR_HOST  = "jstor.org";
	private static final String JSTOR_HOST_NAME  = "http://www.jstor.org";
	private static final String JSTOR_ABSTRACT_PATH = "/pss/";
	private static final String JSTOR_EXPORT_PATH = "/action/exportSingleCitation";
	private static final String JSTOR_EXPORT_PATH_AND_QUERY = "/action/exportSingleCitation?singleCitation=true&suffix=";
	private static final String JSTOR_DOWNLOAD_PATH = "/action/downloadSingleCitation?format=bibtex&include=abs&singleCitation=true&noDoi=yesDoi&suffix={id}&downloadFileName={id}";

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		final Pattern hostPattern = Pattern.compile(".*" + JSTOR_HOST);
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, Pattern.compile(JSTOR_ABSTRACT_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, Pattern.compile(JSTOR_EXPORT_PATH + ".*")));
	}
	
	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {

		sc.setScraper(this);

		String url = sc.getUrl().toString();

		String id = null;
		if(url.startsWith(JSTOR_HOST_NAME + JSTOR_ABSTRACT_PATH)) {
			if(url.contains("?cookieSet=")) {
				id = url.substring(url.indexOf(JSTOR_ABSTRACT_PATH) + JSTOR_ABSTRACT_PATH.length(), url.indexOf("?cookieSet="));
			} else {
				id = url.substring(url.indexOf(JSTOR_ABSTRACT_PATH) + JSTOR_ABSTRACT_PATH.length());
			}

		}

		if(url.startsWith(JSTOR_HOST_NAME + JSTOR_EXPORT_PATH_AND_QUERY)) {
			id = url.substring(url.indexOf("&suffix=") + "&suffix=".length());
		}

		if(id != null) {
			String downloadLink = new String(JSTOR_HOST_NAME + JSTOR_DOWNLOAD_PATH.replace("{id}", id));

			// get cookies from the server
			String cookies = null;
			try {
				cookies = getCookies(sc.getUrl());
			} catch (IOException ex) {
				throw new InternalFailureException("Failed to download Cookies for " + downloadLink);
			}

			//download the bibtex file from the server
			String bibtex = null;
			try {
				bibtex = getContent(new URL(downloadLink), cookies);
			} catch (IOException ex) {
				throw new InternalFailureException("Failed to download the bibtex file.");
			}

			if(bibtex != null) {
				//delete the information in the bibtex file
				bibtex = bibtex.replace("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\nJSTOR CITATION LIST\r\n\r\n\r\n", "");

				//replace CR+LF with LF
				bibtex = bibtex.replace("\r\n", "\n");

				//Convert to UTF-8. Because Server sends a ISO8859-1 encoded string
				try {
					bibtex = new String(bibtex.getBytes("ISO8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					throw new InternalFailureException("Could not convert to UTF-8!");
				}

				sc.setBibtexResult(bibtex);
				return true;
			} else
				throw new ScrapingFailureException("getting bibtex failed");

		} else {
			//missing id
			throw new ScrapingFailureException("ID is missing!");
		}
	}

	/** FIXME: refactor
	 * @param queryURL
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	private String getContent(URL queryURL, String cookie) throws IOException {

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

		//seems that the application needs this cookie even before getting the other cookies, strange :-\
		urlConn.setRequestProperty("Cookie", "I2KBRCK=1");

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
		return "JSTOR";
	}

	public String getSupportedSiteURL() {
		return JSTOR_HOST_NAME;
	}
}
