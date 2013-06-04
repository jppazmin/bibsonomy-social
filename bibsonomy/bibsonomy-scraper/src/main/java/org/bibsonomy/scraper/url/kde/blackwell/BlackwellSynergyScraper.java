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

package org.bibsonomy.scraper.url.kde.blackwell;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.ScrapingException;


/**
 * Scraper for blackwell-synergy.com
 * 
 * This scraper is offline 
 * blackwell is offline and all journals are now available under intersience.wiley.com
 * 
 * @author tst
 */
public class BlackwellSynergyScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "Blackwell Synergy";
	private static final String SITE_URL = "http://blackwell-synergy.com";
	private static final String INFO = "This Scraper parse a publication from " + href(SITE_URL, SITE_NAME)+".";
	
	
	private static final Log log = LogFactory.getLog(BlackwellSynergyScraper.class);

		
	/**
	 * pattern for form inputs
	 */
	private static final String PATTERN_INPUT = "<input\\b[^>]*>";
	
	/**
	 * pattern for value attribute
	 */
	private static final String PATTERN_VALUE = "value=\"[^\"]*\"";

	/**
	 * balckwell host
	 */
	private static final String HOST = "blackwell-synergy.com";
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	/**
	 * get Info
	 */
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
			// log every try to call this Scraper
			log.info("offline Scraper called: BlackwellSynergyScraper with " + sc.getUrl().toString());
			
			/*
			 * 
			try {
				String bibtex = null;
				String cookie = getCookie();
				
				// scrape selected snippet
				if(sc.getSelectedText() != null && !sc.getSelectedText().equals("")){
					bibtex = sc.getSelectedText();
				}
				
				// scrape bibtex page
				if(sc.getUrl().toString().contains("action/downloadCitation")){
					bibtex = getPageContent((HttpURLConnection) sc.getUrl().openConnection(), cookie);
				}else{
					// extract link to download page
					
					String currentPage = getPageContent((HttpURLConnection) sc.getUrl().openConnection(), cookie);
					
					// search input fields with doi
					Pattern inputPattern = Pattern.compile(PATTERN_INPUT);
					Matcher inputMatcher = inputPattern.matcher(currentPage);
					
					LinkedList<String> dois = new LinkedList<String>();
					
					while(inputMatcher.find()){
						String input = inputMatcher.group();
						if(input.contains("name=\"doi\"")){
							Pattern valuePattern = Pattern.compile(PATTERN_VALUE);
							Matcher valueMatcher = valuePattern.matcher(input);
							
							// extract doi
							if(valueMatcher.find()){
								String value = valueMatcher.group();
								value = value.substring(7,value.length()-1);
								// store doi
								dois.add(value);
							}
						}
					}
					
					// build download URL
					if(dois.size()>0){
						StringBuffer url = new StringBuffer();
						url.append("http://www.blackwell-synergy.com/action/downloadCitation?");
						url.append("include=abs");
						url.append("&format=bibtex");
						
						// add dois to URL
						for(String doi: dois){
							url.append("&doi=");
							url.append(doi);
						}
						
						// download publications(in bibtex) page
						URL publURL = new URL(url.toString());
						bibtex = getPageContent((HttpURLConnection) publURL.openConnection(), cookie);
					}
				}
				
				// return scraped bibtex
				if(bibtex != null){
					sc.setBibtexResult(bibtex);
					sc.setScraper(this);
					return true;
				}
			} catch (MalformedURLException ex) {
				throw new InternalFailureException(ex);
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}*/
		return false;
	}

	/** FIXME: refactor
	 * Gets the cookie which is needed to extract the content of aip pages.
	 * (changed code from ScrapingContext.getContentAsString) 
	 * @param urlConn Connection to api page (from url.openConnection())
	 * @return The value of the cookie.
	 * @throws IOException
	 */
	private String getCookie() throws IOException{
		HttpURLConnection urlConn = (HttpURLConnection) new URL("http://www.blackwell-synergy.com/help").openConnection();
		String cookie = null;
		
		urlConn.setAllowUserInteraction(true);
		urlConn.setDoInput(true);
		urlConn.setDoOutput(false);
		urlConn.setUseCaches(false);
		urlConn.setFollowRedirects(true);
		urlConn.setInstanceFollowRedirects(false);
		
		urlConn.setRequestProperty(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
		urlConn.connect();
		
		// extract cookie from header
		Map map = urlConn.getHeaderFields();
		cookie = urlConn.getHeaderField("Set-Cookie");
		if(cookie != null && cookie.indexOf(";") >= 0)
			cookie = cookie.substring(0, cookie.indexOf(";"));
		
		urlConn.disconnect();		
		return cookie;
	}

	/** FIXME: refactor
	 * Extract the content of a scitation.aip.org page.
	 * (changed code from ScrapingContext.getContentAsString)
	 * @param urlConn Connection to api page (from url.openConnection())
	 * @param cookie Cookie for auth.
	 * @return Content of aip page.
	 * @throws IOException
	 */
	private String getPageContent(HttpURLConnection urlConn, String cookie) throws IOException{

		urlConn.setAllowUserInteraction(true);
		urlConn.setDoInput(true);
		urlConn.setDoOutput(false);
		urlConn.setUseCaches(false);
		urlConn.setFollowRedirects(true);
		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestProperty("Cookie", cookie);

		urlConn.setRequestProperty(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
		urlConn.connect();
							  
		// build content
		StringWriter out = new StringWriter();
		InputStream in = new BufferedInputStream(urlConn.getInputStream());
		int b;
		while ((b = in.read()) >= 0) {
			out.write(b);
		}
		
		urlConn.disconnect();
		in.close();
		out.flush();
		out.close();
		
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
