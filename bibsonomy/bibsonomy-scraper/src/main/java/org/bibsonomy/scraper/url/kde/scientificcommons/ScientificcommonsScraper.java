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

package org.bibsonomy.scraper.url.kde.scientificcommons;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for scientificcommons.org
 * @author tst
 * @version $Id: ScientificcommonsScraper.java,v 1.9 2011-04-29 07:24:30 bibsonomy Exp $
 */
public class ScientificcommonsScraper extends AbstractUrlScraper{

	
	private static final String SITE_NAME = "ScientificCommons.org";

	private static final String SITE_URL = "http://www.scientificcommons.org/";

	private static final String INFO = "Scraper for journals from the " + href(SITE_URL, SITE_NAME)+".";
	
	private static final String HOST = "scientificcommons.org";
	
	private static final String DOWNLOAD_URL = "http://en.scientificcommons.org/export/bibtex/";
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	private static final Pattern PATTERN_ID = Pattern.compile(HOST + "/(.*)");
	
	private static final Pattern PATTERN_YEAR = Pattern.compile("year = \\{([^\\}]*)\\}");
	private static final Pattern PATTERN_AUTHORNAME_1 = Pattern.compile("author = \\{([^\\,]*)\\,");
	private static final Pattern PATTERN_AUTHORNAME_2 = Pattern.compile("author = \\{(.*) and");
	
	
	
	
	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		// get resource id from url
		String id = null;
		Matcher matcherId = PATTERN_ID.matcher(sc.getUrl().toString());
		if(matcherId.find())
			id = matcherId.group(1);
		
		if(id != null){
			String bibtex = null;
			
			// get bibtex
			try {
				bibtex = WebUtils.getContentAsString(new URL(DOWNLOAD_URL + id));
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}
			
			if(bibtex != null){
				// bibtex key is missing 
				
				// get year
				String year = null;
				Matcher matcherYear = PATTERN_YEAR.matcher(bibtex);
				if(matcherYear.find())
					year = matcherYear.group(1);
				
				// get first author name
				String author = null;
				Matcher matcherAuthor = PATTERN_AUTHORNAME_1.matcher(bibtex);
				if(matcherAuthor.find()){
					String authorTemp = matcherAuthor.group(1);
					
					if(authorTemp.contains(" ")){
						// author is not sperated by ","
						matcherAuthor = PATTERN_AUTHORNAME_2.matcher(bibtex);
						if(matcherAuthor.find()){
							author = matcherAuthor.group(1).replaceAll(" ", "");// remove " " from string
							
						}else
							// author is not seperated by "and"
							author = "";

					}else
						author = authorTemp;
						
				}
					
				
				if(year != null && author != null){
					
					if(author.contains(" "))
						// author name 
						bibtex = bibtex.replaceFirst("\\{", "{" + year + ",");
					else
						// add bibtex key (author, year) to entry
						bibtex = bibtex.replaceFirst("\\{", "{" + author + year + ",");
					
					// finished
					sc.setBibtexResult(bibtex);
					return true;
				}else
					throw new ScrapingFailureException("Invalid bibtex, author/year is missing");
				
			}else
				throw new ScrapingFailureException("Bibtex download failed");
			
		}else
			throw new ScrapingFailureException("Cannot find identifier in given URL");
		
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
