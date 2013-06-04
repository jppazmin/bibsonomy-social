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

package org.bibsonomy.scraper.url.kde.agu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for publications from http://www.agu.org/pubs/ using the RIS export
 * @author tst
 * @version $Id: AGUScraper.java,v 1.8 2011-04-29 07:24:32 bibsonomy Exp $
 */
public class AGUScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "American Geophysical Union (AGU)";
	private static final String SITE_URL = "http://www.agu.org/pubs/";
	private static final String INFO = "For Publications from the " + href(SITE_URL, SITE_NAME)+".";
	
	private static final String HOST = "agu.org";
	
	private static final String PATH = "/pubs";
	
	private Pattern patternDownloadUrl = Pattern.compile("href=\"([^\\\"]*)\">Export RIS Citation");
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), Pattern.compile(PATH + ".*")));

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext scrapingContext)
			throws ScrapingException {
		scrapingContext.setScraper(this);
		
		String pageContent = null;
		pageContent = scrapingContext.getPageContent();
		
		if(pageContent != null){
			// get download url
			String downloadUrl = null;
			Matcher matcherDownloadUrl = patternDownloadUrl.matcher(pageContent);
			if(matcherDownloadUrl.find())
				downloadUrl = "http://www.agu.org" + matcherDownloadUrl.group(1);
			else
				throw new PageNotSupportedException("This AGU page is not supported.");
			
			if(downloadUrl!=null){
				
				// get RIS citation
				String ris = null;
				try {
					/*
					 * little bug fix:
					 * decode &amp; to & because it seems that AGU not decodes incoming URLs by
					 * itself (without this replacement the result is a error and not a RIS citation)
					 */
					ris = WebUtils.getContentAsString(new URL(downloadUrl.replace("&amp;", "&")));
				} catch (MalformedURLException ex) {
					throw new InternalFailureException(ex);
				} catch (IOException ex) {
					throw new InternalFailureException(ex);
				}
				
				if(ris != null){
					// convert ris to bibtex
					String bibtex = null;
					RisToBibtexConverter converter = new RisToBibtexConverter();
					bibtex = converter.RisToBibtex(ris);
					
					if(bibtex != null){
						// finish
						scrapingContext.setBibtexResult(bibtex);
						return true;
					}else
						throw new ScrapingFailureException("Converting RIS to bibtex failed");
					
				}else
					throw new ScrapingFailureException("Cannot get RIS citation.");
				
			}else
				return false;
			
		}else
			throw new ScrapingFailureException("Cannot download content from " + scrapingContext.getUrl().toString());
		
	}

	public String getInfo() {
		return INFO;
	}

	public String getSupportedSiteName() {
		return "AGU";
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}

}
