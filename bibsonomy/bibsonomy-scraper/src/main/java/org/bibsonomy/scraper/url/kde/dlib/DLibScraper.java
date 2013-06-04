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

package org.bibsonomy.scraper.url.kde.dlib;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;


/**
 * Scraper for www.dlib.org
 * @author tst
 */
public class DLibScraper extends AbstractUrlScraper {
	
	private static final String SITE_URL = "http://www.dlib.org/";
	private static final String SITE_NAME = "D-Lib";
	private static final String INFO = "Scraper for metadata from " + href(SITE_URL, SITE_NAME)+".";

	/**
	 * D-Lib host
	 */
	private static final String DLIB_HOST = "dlib.org";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + DLIB_HOST + "$"), AbstractUrlScraper.EMPTY_PATTERN));

	/*
	 * FIXME: refactor all patterns into static Patterns
	 */

	/**
	 * URL ending from a normal publication page (HTML)  
	 */
	private static final String HTML_PAGE = "html";

	/**
	 * URL ending from a meta publication page (XML)  
	 */
	private static final String META_DATA_PAGE = "meta.xml";
	
	/**
	 * dlib title -> bibtex title 
	 */
	private static final String PATTERN_TITLE = "<title>(.*)</title>";

	/**
	 * dlib creator -> bibtex author
	 */
	private static final String PATTERN_CREATOR = "<creator>(.*)</creator>";

	/**
	 * dlib date -> bibtex year & month
	 */
	private static final String PATTERN_DATE = "<date date-type = \"publication\">(.*)</date>";

	/**
	 * Pattern for year (used with dlib date)
	 */
	private static final String PATTERN_YEAR = ".*([0-9]{4}).*";
	
	/**
	 * dlib type -> bibtex type -> not used, article is default content type
	 */
	private static final String PATTERN_TYPE = "<type resource-type = \"work\">(.*)</type>";

	/**
	 * dlib identifier -> bibtex doi & url
	 */
	private static final String PATTERN_IDENTIFIER_DOI = "<identifier uri-type = \"DOI\">(.*)</identifier>";
	
	/**
	 * dlib identifier -> bibtex doi & url
	 */
	private static final String PATTERN_IDENTIFIER_URL = "<identifier uri-type = \"URL\">(.*)</identifier>";
	
	/**
	 * dlib serial-name -> bibtex journal
	 */
	private static final String PATTERN_JOURNAL = "<serial-name>(.*)</serial-name>";
	
	/**
	 * dlib issn -> bibtex issn
	 */
	private static final String PATTERN_ISSN = "<issn>(.*)</issn>";
	
	/**
	 * dlib volume -> bibtex volume
	 */
	private static final String PATTERN_VOLUME = "<volume>(.*)</volume>";
	
	/**
	 * dlib issue -> bibtex issue
	 */
	private static final String PATTERN_ISSUE = "<issue>(.*)</issue>";
	
	/**
	 * pattern for bibtexkey (used with url from scraping context)
	 */
	private static final String PATTERN_BIBTEX_KEY = "dlib/(.*)/(.*)/";
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		if(sc.getUrl().getHost().endsWith(DLIB_HOST)){
			try {
				sc.setScraper(this);
				
				String metaData = null;
				
				// get metadata
				if(sc.getUrl().toString().endsWith(META_DATA_PAGE)){
					metaData = sc.getPageContent();
				}else if(sc.getUrl().toString().endsWith(HTML_PAGE)){
					String metaDataUrl = sc.getUrl().toString().substring(0, sc.getUrl().toString().length()-4) + META_DATA_PAGE;
					metaData = WebUtils.getContentAsString(new URL(metaDataUrl));
				}
				
				// build xml to bibtex
				if(metaData != null){
					String bibtex = null;
					
					// extract & build bibtex
					bibtex = buildBibtex(metaData, sc.getUrl().toString());
					
					if(bibtex != null){
						// success 
						sc.setBibtexResult(bibtex);
						return true;
					}else
						throw new ScrapingFailureException("getting bibtex failed");

				}else
					throw new PageNotSupportedException("This dlib page is not supported.");
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}
		}
		return false;
	}

	private String buildBibtex(String metaData, String publUrl){
		StringBuffer buffer = new StringBuffer();
		
		// article is default content type
		buffer.append("@article{");
		
		// extract key from url 
		for(String keyPart: extractElement(PATTERN_BIBTEX_KEY, publUrl))
			buffer.append(keyPart);
		
		// add title
		List<String> title = extractElement(PATTERN_TITLE, metaData);
		if(title.size() > 0){
			buffer.append(",\ntitle = {");
			buffer.append(title.get(0));
			buffer.append("}");
		}

		// add author
		List<String> authors = extractElement(PATTERN_CREATOR, metaData);
		if(authors.size() > 0){
			buffer.append(",\nauthor = {");
			for(String author: authors){
				buffer.append(author);
				buffer.append(" and ");
			}
			buffer = buffer.delete(buffer.length()-5, buffer.length());
			buffer.append("}");
		}

		List<String> date = extractElement(PATTERN_DATE, metaData);
		if(date.size() > 0){
			// add year
			String year = extractElement(PATTERN_YEAR, date.get(0)).get(0);
			buffer.append(",\nyear = {");
			buffer.append(year);
			buffer.append("}");
			
			// add month
			buffer.append(",\nmonth = {");
			buffer.append(date.get(0).replace(year, ""));
			buffer.append("}");
		}

		// add doi
		List<String> doi = extractElement(PATTERN_IDENTIFIER_DOI, metaData);
		if(doi.size() > 0){
			buffer.append(",\ndoi = {");
			buffer.append(doi.get(0));
			buffer.append("}");
		}

		// add url
		List<String> url = extractElement(PATTERN_IDENTIFIER_URL, metaData);
		if(url.size() > 0){
			buffer.append(",\nurl = {");
			buffer.append(url.get(0));
			buffer.append("}");
		}

		// add journal
		List<String> journal = extractElement(PATTERN_JOURNAL, metaData);
		if(journal.size() > 0){
			buffer.append(",\njournal = {");
			buffer.append(journal.get(0));
			buffer.append("}");
		}

		// add issn
		List<String> issn = extractElement(PATTERN_ISSN, metaData);
		if(issn.size() > 0){
			buffer.append(",\nissn = {");
			buffer.append(issn.get(0));
			buffer.append("}");
		}

		// add volume
		List<String> volume = extractElement(PATTERN_VOLUME, metaData);
		if(volume.size() > 0){
			buffer.append(",\nvolume = {");
			buffer.append(volume.get(0));
			buffer.append("}");
		}

		// add issue
		List<String> issue = extractElement(PATTERN_ISSUE, metaData);
		if(issue.size() > 0){
			buffer.append(",\nnumber = {");
			buffer.append(issue.get(0));
			buffer.append("}");
		}
		
		buffer.append("\n}");

		return buffer.toString();
	}
	
	/**
	 * Extract elements by regex
	 * @param patternString regex
	 * @param metaData publication
	 * @return List with extracted elements
	 */
	private List<String> extractElement(String patternString, String metaData){
		List<String> elements = new LinkedList<String>();
		
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(metaData);
		
		while(matcher.find()){
			int groups = matcher.groupCount();
			for(int i=1; i<=groups; i++)
				elements.add(matcher.group(i));
		}
		
		return elements;
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
