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

package org.bibsonomy.scraper.url.kde.acs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
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
 * @version $Id: ACSScraper.java,v 1.16 2011-04-29 07:24:44 bibsonomy Exp $
 */
public class ACSScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "ACS";
	private static final String SITE_URL = "http://www.acs.org/";
	private static final String info = "This Scraper parses a publication from " + href(SITE_URL, SITE_NAME)+".";

	private static final String ACS_HOST_NAME  = "http://pubs.acs.org";
	private static final String ACS_PATH = "/doi/abs/";
	private static final String ACS_BIBTEX_PATH = "/action/downloadCitation";
	private static final String ACS_BIBTEX_PARAMS = "?include=abs&format=bibtex&doi=";

	private static Pattern PATTERN_GETTING_DOI_PATH = Pattern.compile("doi/abs/([^\\?]*)");
	private static Pattern PATTERN_GETTING_DOI_QUERY = Pattern.compile("doi=([^\\&]*)");
	
	private static final Pattern pathPatternAbstract = Pattern.compile(ACS_PATH + ".*");
	private static final Pattern pathPatternBibtex = Pattern.compile(ACS_BIBTEX_PATH + ".*");
	
	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		final Pattern hostPattern = Pattern.compile(".*" + "pubs.acs.org");
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, pathPatternBibtex));
		patterns.add(new Tuple<Pattern, Pattern>(hostPattern, pathPatternAbstract));
	}

	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		final String path = sc.getUrl().getPath();
		sc.setScraper(this);
		URL citationURL = sc.getUrl();

		/*if (path.startsWith(ACS_ABSTRACT_PATH)) {
			final String id = path.substring(path.indexOf("/abs/") + 5, path.indexOf(".html"));
			try {
				citationURL = new URL(ACS_HOST_NAME + ACS_BIBTEX_PATH + "?jid=" + id);
			} catch (MalformedURLException ex) {
				throw new InternalFailureException(ex);
			}
		} */

		String bibResult = null;

		/*
		 * http://pubs.acs.org/action/downloadCitation?doi=10.1021%2Fci049894n&include=abs&format=bibtex
		 * 
		 * Cookie: JSESSIONID=yyCNJ10bJFpTNTysSn2nNzxq1HdTRYky5ZK1gqJn19vhMvy3FkQv!-1004683069; SERVER=172.25.11.116:16092; pubs=OVWPXNS172.25.1.54CKKLW; appsrv=OVWPXNS172.23.10.162CKMLK; I2KBRCK=1; I2KBRCK=1; REQUESTIP=172.25.0.60
		 */
		try {
			// get doi from url
			String doi = null;
			Matcher matcherPath = PATTERN_GETTING_DOI_PATH.matcher(citationURL.toString());
			if(matcherPath.find())
				doi = matcherPath.group(1);
			else{
				Matcher matcherQuery = PATTERN_GETTING_DOI_QUERY.matcher(citationURL.toString());
				if(matcherQuery.find())
					doi = matcherQuery.group(1);
			}
			
			if(doi != null){
				String cookie = getCookie(citationURL);
				bibResult = getACSContent(new URL(ACS_HOST_NAME + ACS_BIBTEX_PATH + ACS_BIBTEX_PARAMS + doi), cookie);
			}
			
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

		String cookie = "I2KBRCK=1;";
		//TODO
		if(cookieContent != null){
			for (String crumb : cookieContent) {
				if(cookie.equals(""))
					cookie = crumb;
				else
					cookie = cookie + ";" + crumb;
			}
		}
		urlConn.disconnect();

		return cookie;
	}

	/** FIXME: refactor
	 * @param queryURL
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	private String getACSContent(URL queryURL, String cookie) throws IOException{

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
		return "ACS Publication";
	}

	public String getSupportedSiteURL() {
		return ACS_HOST_NAME;
	}


}
