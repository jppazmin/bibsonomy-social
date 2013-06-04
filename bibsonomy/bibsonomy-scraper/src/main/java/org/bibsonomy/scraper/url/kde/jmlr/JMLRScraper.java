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

package org.bibsonomy.scraper.url.kde.jmlr;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * Scraper for papers from http://jmlr.csail.mit.edu/
 * @author tst
 * @version $Id: JMLRScraper.java,v 1.13 2011-04-29 07:24:47 bibsonomy Exp $
 */
public class JMLRScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Journal of Machine Learning Research";
	private static final String SITE_URL = "http://jmlr.csail.mit.edu/";
	private static final String INFO = "Scraper for papers from " + href(SITE_URL, SITE_NAME)+".";

	private static final String HOST = "jmlr.csail.mit.edu";

	private static final String PATH = "/papers/";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));

	private static final Pattern titlePattern = Pattern.compile("<h2>([^<]*)</h2>");
	private static final Pattern authorPattern = Pattern.compile("<i>([^<]*)</i></b>");
	private static final Pattern pageYearPattern = Pattern.compile("</i></b>([^<]*)</p>");
	private static final Pattern volumePattern = Pattern.compile("/papers/([^/]*)/");
	private static final Pattern yearPattern = Pattern.compile("(\\d{4})");
	private static final Pattern pagePattern = Pattern.compile(":([^,]*),");
	private static final Pattern lastnamePattern = Pattern.compile(" ([\\S]*) and");
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);

		if(sc.getUrl().getPath().startsWith(PATH) && sc.getUrl().getPath().endsWith(".html")){
			String pageContent = sc.getPageContent();

			// get title (directly)
			String title = null;
			final Matcher titleMatcher = titlePattern.matcher(pageContent);
			if(titleMatcher.find())
				title = titleMatcher.group(1);

			// get author (directly)
			String author = null;
			final Matcher authorMatcher = authorPattern.matcher(pageContent);
			if(authorMatcher.find())
				author = authorMatcher.group(1);
			// clean up author string
			if(author != null)
				author = author.replace(",", " and");

			// get volume (from url)
			String volume = null;
			final Matcher volumeMatcher = volumePattern.matcher(sc.getUrl().getPath());
			if(volumeMatcher.find())
				volume = volumeMatcher.group(1);

			// get pageYear (directly)
			String pageYear = null;
			final Matcher pageYearMatcher = pageYearPattern.matcher(pageContent);
			if(pageYearMatcher.find())
				pageYear = pageYearMatcher.group(1);

			// extract year from pageYear string
			String year = null;
			final Matcher yearMatcher = yearPattern.matcher(pageYear);
			if(yearMatcher.find())
				year = yearMatcher.group(1);

			// extarct page from pageYear string
			String page = null;
			final Matcher pageMatcher = pagePattern.matcher(pageYear);
			if(pageMatcher.find())
				page = pageMatcher.group(1);

			/*
			 * build bibtex
			 */
			final StringBuffer bibtex = new StringBuffer("@proceedings{");

			// build bibtex key
			if(year != null && author != null){
				final Matcher lastnameMatcher = lastnamePattern.matcher(author);
				if(lastnameMatcher.find()) // combination lastname and year
					bibtex.append(lastnameMatcher.group(1)).append(year);
				else // only year
					bibtex.append(year);
			}else // default value
				bibtex.append("jmlrKey");
			bibtex.append(",\n");

			// add title
			appendField(bibtex, "title", title);
			// add author
			appendField(bibtex, "author", author);
			// add year
			appendField(bibtex, "year", year);
			// add page
			appendField(bibtex, "page", page);
			// add volume
			appendField(bibtex, "volume", volume);
			// add url
			appendField(bibtex, "url", sc.getUrl().toString());

			// remove last ","
			bibtex.deleteCharAt(bibtex.lastIndexOf(","));

			// last part
			bibtex.append("}");

			// finish
			sc.setBibtexResult(bibtex.toString());
			return true;

		}else
			throw new PageNotSupportedException("Select a page with the abtract view from a JMLR paper.");
	}
	
	private static void appendField(final StringBuffer bibtex, final String fieldName, final String fieldValue) {
		if (fieldValue != null) bibtex.append(fieldName + " = {" + fieldValue + "},\n");
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
