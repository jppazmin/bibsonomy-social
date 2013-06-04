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

package org.bibsonomy.scraper.url.kde.muse;

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
import org.bibsonomy.util.WebUtils;

/**
 * Scraper for muse.jhu.edu
 * @author tst
 * @version $Id: ProjectmuseScraper.java,v 1.12 2011-04-29 07:24:33 bibsonomy Exp $
 */
public class ProjectmuseScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Project MUSE";
	private static final String SITE_URL = "http://muse.jhu.edu/";
	private static final String INFO = "Scraper for citations from " + href(SITE_URL, SITE_NAME)+".";

	private static final String HOST = "muse.jhu.edu";

	private static final String PREFIX_DOWNLOAD_URL = "http://muse.jhu.edu/metadata/sgml/journals/";

	private static final String PATTERN_JOURNAL_ID = "/journals/(.*)";
	/*
	 * regex pattern (sgml)
	 */
	private static final String PATTERN_URL = "<url>(.*)</url>";
	private static final String PATTERN_JOURNAL = "<journal>(.*)</journal>";
	private static final String PATTERN_ISSN = "<issn>(.*)</issn>";
	private static final String PATTERN_VOLUME = "<volume>(.*)</volume>";
	private static final String PATTERN_ISSUE = "<issue>(.*)</issue>";
	private static final String PATTERN_YEAR = "<year>(.*)</year>";
	private static final String PATTERN_FPAGES = "<fpage>(.*)</fpage>";
	private static final String PATTERN_LPAGES = "<lpage>(.*)</lpage>";
	private static final String PATTERN_COPYRIGHT = "<copyright>(.*)</copyright>";
	private static final String PATTERN_TITLE = "<doctitle>(.*)</doctitle>";
	private static final String PATTERN_AUTHOR = "<docauthor>(.*)</docauthor>";
	private static final String PATTERN_SURNAME = "<surname>(.*)</surname>";
	private static final String PATTERN_FNAME = "<fname>(.*)</fname>";
	private static final String PATTERN_ABSTRACT = "<abstract>\\s*<p>([^<]*)</p>\\s*</abstract>";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);

		// build sgml download url
		final String journalID = getRegexResult(PATTERN_JOURNAL_ID, sc.getUrl().toString());

		try {
			URL downloadUrl = new URL(PREFIX_DOWNLOAD_URL + journalID);

			String sgml = WebUtils.getContentAsString(downloadUrl);

			String bibKey = null;
			String authors = "";

			// author may be occur more then one time, thats why special behaviour
			Pattern pattern = Pattern.compile(PATTERN_AUTHOR);
			Matcher matcher = pattern.matcher(sgml);
			while(matcher.find()){
				String author = matcher.group(1);
				String surname = getRegexResult(PATTERN_SURNAME, author); 
				String fname = getRegexResult(PATTERN_FNAME, author);

				// first surname is the first part of the bibtex key
				if(bibKey == null)
					bibKey = surname;

				// first author
				if(authors.equals(""))
					authors = fname + " " + surname;
				// additional authors
				else
					authors = authors + " and " + fname + " " + surname;
			}

			// get year
			String year = getRegexResult(PATTERN_YEAR, sgml);
			// add year to bibtex key
			if(year != null){
				if(bibKey == null)
					bibKey = year;
				else
					bibKey = bibKey + year;
			}

			// get title
			String title = getRegexResult(PATTERN_TITLE, sgml);

			// get url
			String url = getRegexResult(PATTERN_URL, sgml);

			// get journal
			String journal = getRegexResult(PATTERN_JOURNAL, sgml);

			// get issn
			String issn = getRegexResult(PATTERN_ISSN, sgml);

			// get volume
			String volume = getRegexResult(PATTERN_VOLUME, sgml);

			// get issue
			String issue = getRegexResult(PATTERN_ISSUE, sgml);

			// get pages
			String fpages = getRegexResult(PATTERN_FPAGES, sgml);
			String lpages = getRegexResult(PATTERN_LPAGES, sgml);
			String pages = null;
			if(fpages != null && lpages == null)
				pages = fpages;
			else if(fpages == null && lpages != null)
				pages = lpages;
			else if(fpages != null && lpages != null)
				pages = fpages + "-" + lpages;

			// get abstract
			String abstractCitation = getRegexResult(PATTERN_ABSTRACT, sgml);

			StringBuffer bibtex = new StringBuffer();
			bibtex.append("@inproceedings{");

			// add bibtex key
			if(bibKey != null)
				bibtex.append(bibKey).append(",\n");
			else
				bibtex.append("noKey,\n");

			// add author
			if(authors != null)
				bibtex.append("author = {").append(authors).append("},\n");

			// add year
			if(year != null)
				bibtex.append("year = {").append(year).append("},\n");

			// add title
			if(title != null)
				bibtex.append("title = {").append(title).append("},\n");

			// add url
			if(url != null)
				bibtex.append("url = {").append(url).append("},\n");

			// add journal
			if(journal != null)
				bibtex.append("journal = {").append(journal).append("},\n");

			// add issn
			if(issn != null)
				bibtex.append("issn = {").append(issn).append("},\n");

			// add volume
			if(volume != null)
				bibtex.append("volume = {").append(volume).append("},\n");

			// add issue
			if(issue != null)
				bibtex.append("issue = {").append(issue).append("},\n");

			// add pages
			if(pages != null)
				bibtex.append("pages = {").append(pages).append("},\n");

			// add abstratc
			if(abstractCitation != null)
				bibtex.append("abstract = {").append(abstractCitation).append("},\n");

			// remove last ","
			bibtex.deleteCharAt(bibtex.length()-2);

			// add last parts
			bibtex.append("}\n");

			sc.setBibtexResult(bibtex.toString());
			return true;

		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}

	}

	/**
	 * execute regex and return matching result
	 * @param regex Regular Expression
	 * @param content target of regex
	 * @return matching result, null if no matching
	 */
	private String getRegexResult(String regex, String content){
		final Matcher matcher = Pattern.compile(regex).matcher(content);
		if(matcher.find())
			return matcher.group(1);
		return null;
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
