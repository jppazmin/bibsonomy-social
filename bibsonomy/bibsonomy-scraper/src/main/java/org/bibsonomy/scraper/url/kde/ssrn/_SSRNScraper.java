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

package org.bibsonomy.scraper.url.kde.ssrn;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

/**
 * Edit tst: early version from wbi. Problem is that the download is not available.
 * May be some addditional informations are missing. The Cookie is build like the
 * cookie you can see in web browsers. May be that some Javscript actions change 
 * something important. But this download try to rebuild the request header as you
 * can see with the LiveHttpHeader Addon from Firefox.
 * 
 * @author wbi
 * @version $Id: _SSRNScraper.java,v 1.8 2011-04-29 07:24:37 bibsonomy Exp $
 */
public class _SSRNScraper implements Scraper {
	private static final String SITE_NAME = "SSRN";
	private static final String SSRN_HOST_NAME  = "http://papers.ssrn.com";
	private static final String SITE_URL  = SSRN_HOST_NAME+"/";
	private static final String info = "This Scraper parses a publication from " + href(SITE_URL, SITE_NAME) +
	"and extracts the adequate BibTeX entry.";

	
	private static final String SSRN_ABSTRACT_PATH = "/sol3/papers.cfm?abstract_id=";
	private static final String SSRN_BIBTEX_PATH = "/sol3/RefExport.cfm";
	private static final String SSRN_BIBTEX_PARAMS = "?function=download&format=2&abstract_id=";
	
	public String getInfo() {
		return info;
	}

	public Collection<Scraper> getScraper() {
		return Collections.singletonList((Scraper) this);
	}

	public boolean scrape(ScrapingContext sc)
			throws ScrapingException {
		
		if(sc.getUrl() != null) {
			
			String url = sc.getUrl().toString();
			if(url.startsWith(SSRN_HOST_NAME)) {
				String id = null;
				if(url.startsWith(SSRN_HOST_NAME + SSRN_ABSTRACT_PATH)) {
					id = url.substring(url.indexOf(SSRN_ABSTRACT_PATH) + SSRN_ABSTRACT_PATH.length());
				}
				
				if(url.startsWith(SSRN_HOST_NAME + SSRN_BIBTEX_PATH)) {
					id = url.substring(url.indexOf(SSRN_BIBTEX_PATH + "?abstract_id=") + (SSRN_BIBTEX_PATH + "?abstract_id=").length(), url.indexOf("&function"));
				}
				
				if(id != null) {
					String downloadLink = SSRN_HOST_NAME + SSRN_BIBTEX_PATH + SSRN_BIBTEX_PARAMS + id;
					String cookies = null;
					
					try {
						cookies = getCookies(sc.getUrl());
					} catch (IOException ex) {
						throw new InternalFailureException("Could not store cookies from " + sc.getUrl());
					}
					
					String bibtex = null;
					try {
						bibtex = getContent(new URL(downloadLink), cookies);
					} catch (MalformedURLException ex) {
						throw new InternalFailureException("The url "+ downloadLink + " is not valid");
					} catch (IOException ex) {
						throw new ScrapingFailureException("BibTex download failed. Result is null!");
					}
					
					if(bibtex != null) {
						
						bibtex = bibtex.replace("\r\n", "\n");
						sc.setBibtexResult(bibtex);
						
						sc.setScraper(this);
						return true;
					}
				} else {
					throw new ScrapingFailureException("ID for donwload link is missing.");
				}
			}
		}
		
		return false;
	}

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
		urlConn.setRequestProperty("Set-Cookie", cookie);
		
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
		
		cookieString.append("SSRN_LOGIN=wbi%40cs%2Euni%2Dkassel%2Ede; ");
		cookieString.append("SSRN_PW=Walde209; ");
		
		urlConn.disconnect();
		
		return cookieString.toString();
	}

	public boolean supportsScrapingContext(ScrapingContext scrapingContext) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//added this, because this does not extend AbtractUrlScraper... is this class deprecated? ... see SSRNScraper
	/** Builds a href to the URL with the given anchor text.
	 *  
	 * @param url
	 * @param text
	 * @return
	 */
	public static String href(final String url, final String text) {
		return "<a href=\"" + url + "\">" + text + "</a>";
	}
}
