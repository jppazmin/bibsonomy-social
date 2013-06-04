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

package org.bibsonomy.scraper.url.kde.nasaads;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.WebUtils;

/**
 * Scarper for NASA ADS.
 * It collects bibtex snippets and single referenzes (html page or bibtex page).  
 * @author tst
 */
public class NasaAdsScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "The SAO/NASA Astrophysics Data System";

	private static final String SITE_URL = "http://adsabs.harvard.edu/";

	private static final String INFO = "Extracts publications from " + href(SITE_URL, SITE_NAME) + 
	". Publications can be entered as a marked bibtex snippet (one or more publications) or by the page of a single reference.";

	/*
	 * host of nasa ads
	 */
	private static final String URL_NASA_ADS_HOST = "adsabs.harvard.edu";

	/*
	 * supported content types
	 */
	private static final String NASA_ADS_CONTENT_TYPE_PLAIN = "text/plain";

	private static final String NASA_ADS_CONTENT_TYPE_HTML = "text/html";

	/*
	 * description text from a bibtex link
	 */
	private static final String BIBTEX_LINK_VALUE = "Bibtex entry for this abstract";

	/*
	 * pattern for link and its href
	 */
	private static final Pattern linkPattern = Pattern.compile("<a(.*)</a>");

	private static final Pattern hrefPattern = Pattern.compile("href=\"[^\"]*\"");

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + URL_NASA_ADS_HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	public String getInfo() {
		return INFO;
	}

	/**
	 * This scraper collects bibtex snippets and single referenzes (html page or bibtex page).
	 */
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		/*
		 * check of snippet
		 */
		if(present(sc.getSelectedText())){
			sc.setBibtexResult(sc.getSelectedText());
			return true;

			/*
			 * no snippet, check content from url
			 */
		}else{

			/*
			 * changed code from ScrapingContext.getContentAsString
			 * the desicion what do depends on the type of the content 
			 */
			
			try {
				/*
				 * FIXME: refactor this ... it occurrs in several scrapers!
				 */
				final HttpURLConnection urlConn = (HttpURLConnection) sc.getUrl().openConnection();
				urlConn.setAllowUserInteraction(false);
				urlConn.setDoInput(true);
				urlConn.setDoOutput(false);
				urlConn.setUseCaches(false);
				/*
				 * set user agent (see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) since some 
				 * pages require it to download content.
				 */
				urlConn.setRequestProperty(
						"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
				urlConn.connect();
				final StringWriter out = new StringWriter();
				final InputStream in = new BufferedInputStream(urlConn.getInputStream());
				int b;
				while ((b = in.read()) >= 0) {
					out.write(b);
				}
				urlConn.disconnect();
				in.close();
				out.flush();
				out.close();
				String nasaAdsContent = out.toString();

				/*
				 * if bibtex content, then use this content as snippet
				 */
				if(urlConn.getContentType().startsWith(NASA_ADS_CONTENT_TYPE_PLAIN)){

					sc.setBibtexResult(nasaAdsContent);
					return true;

					/*
					 * if html content, search link to bibtex content
					 */
				}else if(urlConn.getContentType().startsWith(NASA_ADS_CONTENT_TYPE_HTML)){

					final Matcher linkMatcher = linkPattern.matcher(nasaAdsContent);

					/*
					 * check all links
					 */
					while(linkMatcher.find()){
						String link = linkMatcher.group();
						// check bibtex link
						if(link.contains(BIBTEX_LINK_VALUE)){
							// extract href from link
							final Matcher hrefMatcher = hrefPattern.matcher(link);
							if(hrefMatcher.find()){
								String href = hrefMatcher.group();
								// get URL
								String bibtexURL = href.substring(6, href.length()-1);
								// get snippet
								String bibtexSnippet = WebUtils.getContentAsString(new URL(bibtexURL));
								sc.setBibtexResult(bibtexSnippet);
								return true;
							}
						}
					}
				}
			} catch (ConnectException cex) {
				throw new InternalFailureException(cex);
			} catch (IOException ioe) {
				throw new InternalFailureException(ioe);
			}
		}
		throw new PageNotSupportedException("NasaADSScraper: Not supported nasa ads page. no bibtex link in html.");
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
