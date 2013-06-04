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

package org.bibsonomy.scraper.url.kde.librarything;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.WebUtils;


/**
 * Scraper for www.librarything.com
 * Its supports the following URL-prefixes:
 * http://www.librarything.com/work-info/
 * http://www.librarything.com/work/
 * @author tst
 */
public class LibrarythingScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "librarything";
	private static final String URL_LIBRARYTHING_PAGE = "http://www.librarything.com";
	private static final String SITE_URL = URL_LIBRARYTHING_PAGE+"/";
	private static final String INFO = "Extracts publication from " + href("http://www.librarything.com/work-info", SITE_NAME) + 
	". If a http://www.librarything.com/work page is selected, then the scraper trys to download the according work-info page.";


	

	private static final String URL_LIBRARYTHING_PAGE_HOST = "librarything.com";

	/*
	 * FIXME: make all patterns static Patterns!
	 */
	private static String LIBRARYTHING_PATTERN_BIBLIOGRAPHIC_INFOS = "<td class=\"bookeditfield\" id=\"bookedit_publication\">([^<]*)</td>";

	private static String LIBRARYTHING_PATTERN_OTHER_AUTHORS = "<td class=\"bookeditfield\" id=\"bookedit_otherauthors\">([^<]*)</td>";

	private static String LIBRARYTHING_PATTERN_TITLE = "<td class=\"bookeditfield\" id=\"bookedit_title\"><b>([^<]*)</b></td>";

	private static String LIBRARYTHING_PATTERN_WORK_TITLE = "<span class=\"bookeditfield\" id=\"bookedit_title\"><b>([^<]*)</b></span>";

	private static String LIBRARYTHING_PATTERN_AUTHOR_LINK = "<td class=\"bookeditfield\" id=\"bookedit_authorunflip\">(.*)</td>";

	private static String LIBRARYTHING_PATTERN_AUTHOR = "<h2>by <a href=\"/author/[^>]*>([^<]*)</a></h2>";

	private static String LIBRARYTHING_PATTERN_DATE = "<td class=\"bookeditfield\" id=\"bookedit_date\">([^<]*)</td>";

	private static String LIBRARYTHING_PATTERN_ISBN = "<td class=\"bookeditfield\" id=\"bookedit_ISBN\">([^<]*)</td>";

	private static String LIBRARYTHING_PATTERN_WORK_AUTHOR_LINK = "<td class=\"left\">Author</td><td class=\"bookNonEditField\">(.*)</td>";

	private static String LIBRARYTHING_PATTERN_WORK_ISBN_10 = "<td class=\"left\">ISBN-10</td><td class=\"bookNonEditField\">([^<]*)</td>";

	private static String LIBRARYTHING_PATTERN_WORK_ISBN_13 = "<td class=\"left\">ISBN-13</td><td class=\"bookNonEditField\">([^<]*)</td>";

	final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*librarything\\..*"), AbstractUrlScraper.EMPTY_PATTERN));

	private static final String PATTERN_LINK = "<a\\b[^>]*>([^<]*)</a>";

	private String author = null;
	private String title = null;
	private String year = null;
	private String misc = null;
	private String key = SITE_NAME;

	/**
	 * This Scraper works only with the following URL-prefixes and no selected text.
	 * http://www.librarything.com/work-info/
	 * http://www.librarything.com/work/
	 */
	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		URL url = null;

		sc.setScraper(this);

		// build .com url			
		if(!sc.getUrl().getHost().contains("librarything.com")){
			String urlString = sc.getUrl().toString();

			// extract part bevor tld
			int indexLibrarything = urlString.indexOf("librarything.");
			String bevorTLD = urlString.substring(0, indexLibrarything + 13);

			//extract part after tld
			urlString = urlString.substring(indexLibrarything+12);
			int indexFirstSlash = urlString.indexOf("/");
			String afterTLD = urlString.substring(indexFirstSlash);

			// build new .com url
			try {
				url = new URL(bevorTLD + "com" + afterTLD);
			} catch (MalformedURLException e) {
				throw new InternalFailureException(e);
			}

			// is already a .com url
		}else{
			url = sc.getUrl();
		}

		String content;
		try {
			content = WebUtils.getContentAsString(url);
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}

		// extract data
		Pattern authorPattern = Pattern.compile(LIBRARYTHING_PATTERN_AUTHOR);
		Matcher authorMatcher = authorPattern.matcher(content);
		if(authorMatcher.find()){
			author = authorMatcher.group(1);
		}else{
			authorPattern = Pattern.compile(LIBRARYTHING_PATTERN_WORK_AUTHOR_LINK);
			authorMatcher = authorPattern.matcher(content);
			if(authorMatcher.find()){
				author = authorMatcher.group();
				Pattern linkPattern = Pattern.compile(PATTERN_LINK);
				Matcher linkMatcher = linkPattern.matcher(author);
				if(linkMatcher.find())
					author = linkMatcher.group(1);
			}else{
				authorPattern = Pattern.compile(LIBRARYTHING_PATTERN_AUTHOR_LINK);
				authorMatcher = authorPattern.matcher(content);
				if(authorMatcher.find()){
					author = authorMatcher.group();
					Pattern linkPattern = Pattern.compile(PATTERN_LINK);
					Matcher linkMatcher = linkPattern.matcher(author);
					if(linkMatcher.find())
						author = linkMatcher.group(1);
				}					
			}
		}

		authorPattern = Pattern.compile(LIBRARYTHING_PATTERN_OTHER_AUTHORS);
		authorMatcher = authorPattern.matcher(content);
		if(authorMatcher.find()){
			String otherAuthors = authorMatcher.group(1);
			if(author == null && !otherAuthors.equals(""))
				author = otherAuthors;
			else if(!otherAuthors.equals(""))
				author = author + " and " + authorMatcher.group(1);
		}


		Pattern titlePattern = Pattern.compile(LIBRARYTHING_PATTERN_TITLE);
		Matcher titleMatcher = titlePattern.matcher(content);
		if(titleMatcher.find()){
			title = titleMatcher.group(1);
		}else{
			titlePattern = Pattern.compile(LIBRARYTHING_PATTERN_WORK_TITLE);
			titleMatcher = titlePattern.matcher(content);
			if(titleMatcher.find()){
				title = titleMatcher.group(1);
			}
		}

		Pattern yearPattern = Pattern.compile(LIBRARYTHING_PATTERN_DATE);
		Matcher yearMatcher = yearPattern.matcher(content);
		if(yearMatcher.find()){
			year = yearMatcher.group(1);
			key = key + year;
		}

		Pattern isbnPattern = Pattern.compile(LIBRARYTHING_PATTERN_ISBN);
		Matcher isbnMatcher = isbnPattern.matcher(content);
		if(isbnMatcher.find()){
			misc = "isbn={" + isbnMatcher.group(1) + "}";
		}else{
			isbnPattern = Pattern.compile(LIBRARYTHING_PATTERN_WORK_ISBN_10);
			isbnMatcher = isbnPattern.matcher(content);
			if(isbnMatcher.find()){
				if(misc == null)
					misc = "isbn={" + isbnMatcher.group(1) + "}";
				else
					misc = misc + ", " + "isbn={" + isbnMatcher.group(1) + "}";
			}

			isbnPattern = Pattern.compile(LIBRARYTHING_PATTERN_WORK_ISBN_13);
			isbnMatcher = isbnPattern.matcher(content);
			if(isbnMatcher.find()){
				if(misc == null)
					misc = "isbn={" + isbnMatcher.group(1) + "}";
				else
					misc = misc + ", " + "isbn={" + isbnMatcher.group(1) + "}";
			}
		}

		StringBuffer resultBibtex = new StringBuffer();
		resultBibtex.append("@book{" + key + ",\n");			

		if(author != null)
			resultBibtex.append("\tauthor = {" + author + "},\n");
		if(title != null)
			resultBibtex.append("\ttitle = {" + title + "},\n");
		if(year != null)
			resultBibtex.append("\tyear = {" + year + "},\n");
		if(misc != null)
			resultBibtex.append("\t" + misc + ",\n");
		if(url != null)
			resultBibtex.append("\turl = {" + url + "},\n");

		String bibResult = resultBibtex.toString();
		
		// need to unscaped the html entities since they use them on their page
		bibResult = StringEscapeUtils.unescapeHtml(bibResult);
		
		bibResult = bibResult.substring(0, bibResult.length()-2) + "\n}\n";

		sc.setBibtexResult(bibResult);
		return true;
	}
	
	public String getInfo() {
		return INFO;
	}

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		return "Librarything";
	}
	
	public String getSupportedSiteURL() {
		return URL_LIBRARYTHING_PAGE;
	}

}
