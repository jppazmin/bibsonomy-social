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

package org.bibsonomy.scraper.url.kde.eric;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
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
 * SCraper for papers from http://www.eric.ed.gov/
 * @author tst
 * @version $Id: EricScraper.java,v 1.15 2011-04-29 07:24:34 bibsonomy Exp $
 */
public class EricScraper extends AbstractUrlScraper {
	
	private static final String SITE_URL = "http://www.eric.ed.gov/";
	private static final String SITE_NAME = "Education Resources Information Center";
	private static final String INFO = "Scraper for publications from the " + href(SITE_URL, SITE_NAME)+".";
	
	private static final String ERIC_HOST = "eric.ed.gov";
	
	private static final String EXPORT_BASE_URL = "http://www.eric.ed.gov/ERICWebPortal/MyERIC/clipboard/performExport.jsp?texttype=endnote&accno=";
	
	private static final String PATTERN_ACCNO = "accno=([^&]*)";

	private static final Pattern accnoPattern = Pattern.compile("accno=([^&]*)");

	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + ERIC_HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal (ScrapingContext sc)throws ScrapingException {
		/*
		 * example:
		 * http://www.eric.ed.gov/ERICWebPortal/Home.portal?_nfpb=true&ERICExtSearch_SearchValue_0=star&searchtype=keyword&ERICExtSearch_SearchType_0=kw&_pageLabel=RecordDetails&objectId=0900019b802f2e44&accno=EJ786532&_nfls=false
		 * accno=EJ786532
		 * 
		 * texttype=endnote
		 * 
		 */
		
			sc.setScraper(this);
			
			//extract accno from url query
			String accno = null;
			
			final Matcher accnoMatcher = accnoPattern.matcher(sc.getUrl().getQuery());
			if(accnoMatcher.find())
				accno = accnoMatcher.group(1);
			
			// build download URL
			String downloadUrl = null;
			if(accno != null)
				downloadUrl = EXPORT_BASE_URL + accno;
			
			// download ris
			try {
				
				if(downloadUrl != null){
					String ris = WebUtils.getContentAsString(new URL(downloadUrl));
					
					// convert to bibtex
					String bibtex = null;
					RisToBibtexConverter converter = new RisToBibtexConverter();
					
					bibtex = converter.RisToBibtex(ris);
				
					if(bibtex != null){
						// append url
						bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
						
						// add downloaded bibtex to result 
						sc.setBibtexResult(bibtex);
						return true;
					}else
						throw new ScrapingFailureException("getting bibtex failed");
					
				}else
					throw new PageNotSupportedException("Value for accno is missing.");
				
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
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
