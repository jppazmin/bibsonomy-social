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

package org.bibsonomy.scraper.url.kde.iwap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * Scraper for papers from http://www.iwaponline.com
 * @author tst
 * @version $Id: IWAPonlineScraper.java,v 1.12 2011-04-29 07:24:38 bibsonomy Exp $
 */
public class IWAPonlineScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "IWA Publishing";

	private static final String SITE_URL = "http://www.iwaponline.com";

	private static final String INFO = "This Scraper supports papers from " + href(SITE_URL, SITE_NAME) +".";

	/*
	 * host
	 */

	private static final String HOST = "iwaponline.com";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	/*
	 * Pattern
	 * FIXME: refactor to static Patterns
	 */

	private static final String PATTERN_META = "<META[^>]*>";

	private static final String PATTERN_NAME = "NAME=\"([^\"]*)\"";

	private static final String PATTERN_CONTENT = "CONTENT=\"([^\"]*)\"";

	private static final String PATTERN_DATE = "(\\d{4})";

	/*
	 * Meta data elements
	 */

	private static final String META_ELEMENT_PUBLISHER = "DC.Publisher";
	private static final String META_ELEMENT_DATE = "DC.Date";
	private static final String META_ELEMENT_IDENTIFIER = "DC.Identifier";
	private static final String META_ELEMENT_LANGUAGE = "DC.Language";
	private static final String META_ELEMENT_RIGHTS = "DC.Rights";// TODO: not used       
	private static final String META_ELEMENT_TYPE = "arttype";// not used
	private static final String META_ELEMENT_VOLUME = "PPL.Volume";
	private static final String META_ELEMENT_ISSUE = "PPL.Issue";
	private static final String META_ELEMENT_FIRST_PAGE = "PPL.FirstPage";
	private static final String META_ELEMENT_LAST_PAGE = "PPL.LastPage";
	private static final String META_ELEMENT_DOC_TYPE = "PPL.DocType";// TODO: not used (general value is "Research article")
	private static final String META_ELEMENT_DOI = "PPL.DOI";
	private static final String META_ELEMENT_TITLE = "DC.Title";
	private static final String META_ELEMENT_CREATOR = "DC.Creator";
	private static final String META_ELEMENT_KEYWORD = "DC.Keyword";

	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);

		// get page
		String page = sc.getPageContent();

		// map for meta data
		HashMap<String, String> metaMap = new HashMap<String, String>();

		// get meta data
		Pattern metaPattern = Pattern.compile(PATTERN_META);
		Matcher metaMatcher = metaPattern.matcher(page);
		while(metaMatcher.find()){
			String meta = metaMatcher.group();

			// get name
			String name = null;
			Pattern namePattern = Pattern.compile(PATTERN_NAME);
			Matcher nameMatcher = namePattern.matcher(meta);
			if(nameMatcher.find())
				name = nameMatcher.group(1);

			// get content
			String content = null;
			Pattern contentPattern = Pattern.compile(PATTERN_CONTENT);
			Matcher contentMatcher = contentPattern.matcher(meta);
			if(contentMatcher.find())
				content = contentMatcher.group(1);

			// store in map
			if(name != null && content != null){
				if(metaMap.containsKey(name) && name.equals(META_ELEMENT_CREATOR))
					metaMap.put(name, metaMap.get(name) + " and " + content);
				else if(metaMap.containsKey(name) && name.equals(META_ELEMENT_KEYWORD))
					metaMap.put(name, metaMap.get(name) + " " + content);
				else
					metaMap.put(name, content);
			}
		}


		/*
		 * build bibtex
		 */

		StringBuffer bibtex = new StringBuffer();

		// start building with key and date
		if(metaMap.containsKey(META_ELEMENT_DATE)){
			Pattern datePattern = Pattern.compile(PATTERN_DATE);
			Matcher dateMatcher = datePattern.matcher(metaMap.get(META_ELEMENT_DATE));
			if(dateMatcher.find()){
				String year = dateMatcher.group(1);
				bibtex.append("@article{iwap" + year + ",\n");
				bibtex.append("year = {" + year + "},\n");
			}else{
				bibtex.append("@article{iwap,\n");
			}
		}else{
			bibtex.append("@article{iwap\n");
		}

		// publisher
		if(metaMap.containsKey(META_ELEMENT_PUBLISHER))
			bibtex.append("publisher = {" + metaMap.get(META_ELEMENT_PUBLISHER) + "},\n");

		// url
		if(metaMap.containsKey(META_ELEMENT_IDENTIFIER))
			bibtex.append("url = {" + metaMap.get(META_ELEMENT_IDENTIFIER) + "},\n");

		// volume
		if(metaMap.containsKey(META_ELEMENT_VOLUME))
			bibtex.append("volume = {" + metaMap.get(META_ELEMENT_VOLUME) + "},\n");

		// number
		if(metaMap.containsKey(META_ELEMENT_ISSUE))
			bibtex.append("number = {" + metaMap.get(META_ELEMENT_ISSUE) + "},\n");

		// pages
		if(metaMap.containsKey(META_ELEMENT_FIRST_PAGE) && metaMap.containsKey(META_ELEMENT_LAST_PAGE))
			bibtex.append("pages = {" + metaMap.get(META_ELEMENT_FIRST_PAGE)+ " - "+ metaMap.get(META_ELEMENT_LAST_PAGE) + "},\n");

		// misc
		if(metaMap.containsKey(META_ELEMENT_DOI) || metaMap.containsKey(META_ELEMENT_LANGUAGE)){
			// get doi
			String doi = null;
			if(metaMap.containsKey(META_ELEMENT_DOI))
				doi = metaMap.get(META_ELEMENT_DOI);

			// get language
			String language = null;
			if(metaMap.containsKey(META_ELEMENT_LANGUAGE))
				language = metaMap.get(META_ELEMENT_LANGUAGE);

			// start misc
			bibtex.append("misc = {");
			// append doi
			if(doi != null)
				bibtex.append("doi = {" + metaMap.get(META_ELEMENT_DOI)+ "}");
			// check if "," is needed
			if(doi != null && language != null)
				bibtex.append(",");
			// append language
			if(language != null)
				bibtex.append("language = {" + metaMap.get(META_ELEMENT_LANGUAGE)+ "}");
			// finish misc
			bibtex.append("},\n");
		}

		// title
		if(metaMap.containsKey(META_ELEMENT_TITLE))
			bibtex.append("title = {" + metaMap.get(META_ELEMENT_TITLE) + "},\n");

		// author
		if(metaMap.containsKey(META_ELEMENT_CREATOR))
			bibtex.append("author = {" + metaMap.get(META_ELEMENT_CREATOR) + "},\n");

		// keywords
		if(metaMap.containsKey(META_ELEMENT_KEYWORD))
			bibtex.append("keywords = {" + metaMap.get(META_ELEMENT_KEYWORD) + "},\n");

		// remove last ","
		bibtex = bibtex.deleteCharAt(bibtex.length()-2);

		// finish building
		bibtex.append("}");

		sc.setBibtexResult(bibtex.toString());
		return true;
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
