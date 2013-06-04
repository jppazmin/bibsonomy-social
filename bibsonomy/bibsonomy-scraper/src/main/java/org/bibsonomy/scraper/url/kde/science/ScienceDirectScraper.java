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

package org.bibsonomy.scraper.url.kde.science;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;
import org.bibsonomy.util.id.DOIUtils;

/** Scraper for ScienceDirect.
 * 
 * @author rja
 *
 */
public class ScienceDirectScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "ScienceDirect";

	private static final String SITE_URL = "http://www.sciencedirect.com/";

	private static final String info = "This scraper parses a publication page from " + href(SITE_URL, SITE_NAME)+".";

	private static final String SCIENCE_CITATION_HOST     = "sciencedirect.com";
	private static final String SCIENCE_CITATION_PATH     = "/science";
	private static final String SCIENCE_CITATION_URL     = "http://www.sciencedirect.com/science";

	private static final String PATTERN_DOWNLOAD_PAGE_LINK = "<a href=\"(/science\\?_ob=DownloadURL[^\"]*)\"";

	private static final String PATTERN_ACCT               = "<input type=hidden name=_acct value=([^>]*)>";
	private static final String PATTERN_ARTICLE_LIST_ID    = "<input type=hidden name=_ArticleListID value=(.+?)>";
	private static final String PATTERN_USER_ID            = "<input type=hidden name=_userid value=(.+?)>"; // "&_userid=([^&]*)";
	private static final String PATTERN_UIOKEY             = "<input type=hidden name=_uoikey value=(.+?)>"; //"&_uoikey=([^&]*)";
	private static final String PATTERN_MD5                = "<input type=hidden name=md5 value=(.+?)>";
	private static final String PATTERN_KEYWORDS		   = "keywords = \"(.+)\"";
	private static final String PATTERN_QUOTE_START		   = "\\s*=\\s*\"";
	private static final String PATTERN_QUOTE_END		   = "\\\"\\s*,\\s*$|\\\"\\s*$";

	private static final Pattern patternDownload	= Pattern.compile(PATTERN_DOWNLOAD_PAGE_LINK);
	private static final Pattern patternAcct		= Pattern.compile(PATTERN_ACCT);
	private static final Pattern patternArList		= Pattern.compile(PATTERN_ARTICLE_LIST_ID);
	private static final Pattern patternUserId		= Pattern.compile(PATTERN_USER_ID);
	private static final Pattern patternUiokey		= Pattern.compile(PATTERN_UIOKEY);
	private static final Pattern patternMD5			= Pattern.compile(PATTERN_MD5);
	private static final Pattern patternKeywords	= Pattern.compile(PATTERN_KEYWORDS);
	private static final Pattern patternQuoteStart	= Pattern.compile(PATTERN_QUOTE_START, Pattern.MULTILINE);
	private static final Pattern patternQuoteEnd	= Pattern.compile(PATTERN_QUOTE_END, Pattern.MULTILINE);
	
	private static final Pattern patternBrokenPages = Pattern.compile("(.*pages = \"[0-9]+) - ([0-9]+\".*)", Pattern.DOTALL); 
	
	private static final String KEYWORD_DELIMITER = ", ";

	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SCIENCE_CITATION_HOST), Pattern.compile(SCIENCE_CITATION_PATH + ".*")));

	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		// This Scraper might handle the specified url
		try {
			String downloadQuery = null;

			// article page
			final URL url = sc.getUrl();
			if (url.getPath().startsWith("/science")) {
				if(url.getQuery() == null || url.getQuery().contains("_ob=ArticleURL")) {
					final String pageContent = sc.getPageContent();

					// search link to download page (there is only one download link on page)
					final Matcher matcherDownload = patternDownload.matcher(pageContent);
					if(matcherDownload.find())
						downloadQuery = matcherDownload.group(1);

					// download page
				} else if(url.getQuery().contains("_ob=DownloadURL")) 
					downloadQuery = url.toString();
			} else
				throw new  PageNotSupportedException("This page is currently not supported.");

			final String downloadURL = "http://www.sciencedirect.com" + downloadQuery;
			final String downloadPageContent = WebUtils.getContentAsString(new URL(downloadURL));


			String acct = null;
			final Matcher matcherAcct = patternAcct.matcher(downloadPageContent);
			if(matcherAcct.find())
				acct = matcherAcct.group(1);

			String arList = "";
			final Matcher matcherArList = patternArList.matcher(downloadPageContent);
			if(matcherArList.find())
				arList= matcherArList.group(1);

			String userId = null;
			final Matcher matcherUserId = patternUserId.matcher(downloadPageContent);
			if(matcherUserId.find())
				userId = matcherUserId.group(1);

			String uiokey = null;
			final Matcher matcherUiokey = patternUiokey.matcher(downloadPageContent);
			if(matcherUiokey.find())
				uiokey = matcherUiokey.group(1);

			String md5 = null;
			final Matcher matcherMD5 = patternMD5.matcher(downloadPageContent);
			if(matcherMD5.find())
				md5 = matcherMD5.group(1);

			if (acct != null && userId != null && uiokey != null && md5 != null) {
				final String postContent = "_ob=DownloadURL&_method=finish&_acct=" + acct + "&_userid=" + userId + "&_docType=FLA&_ArticleListID=" + arList + "&_uoikey=" + uiokey + "&count=1&md5=" + md5 + "&JAVASCRIPT_ON=Y&format=cite-abs&citation-type=BIBTEX&Export=Export&RETURN_URL=http%3A%2F%2Fwww.sciencedirect.com%2Fscience%2Fhome";
				
				// merge multiple keyword fields
				String _bibtex = WebUtils.getPostContentAsString(new URL(SCIENCE_CITATION_URL), postContent, "latin1");
				final StringBuilder _keywords = new StringBuilder();
				
				Matcher _m = patternKeywords.matcher(_bibtex);
				
				while (_m.find()) {
					_keywords.append(_m.group(1));
					_keywords.append(KEYWORD_DELIMITER);
					if (_bibtex.contains(_m.group() + ",")) {
						_bibtex = _bibtex.replace(_m.group() + ",", "");
					} else {
						_bibtex = _bibtex.replace(_m.group(), "");
					}
				}

				/*
				 * remove last comma
				 */
				final int indexOfDelim = _keywords.lastIndexOf(KEYWORD_DELIMITER);
				if (indexOfDelim > 0)
					_keywords.delete(indexOfDelim, indexOfDelim + 1);
				
				// change quotes to curly brackets. it could be possible, that the abstract contains double quotes
				_m = patternQuoteStart.matcher(_bibtex);
				_bibtex = _m.replaceAll(" = {");
				_m = patternQuoteEnd.matcher(_bibtex);
				_bibtex = _m.replaceAll("},");
				
				// add keyword field
				_bibtex = BibTexUtils.addFieldIfNotContained(_bibtex, "keywords", _keywords.toString());
				
				final String bibtex = cleanBibTeX(_bibtex);

				/*
				 * Job done
				 */
				if (bibtex != null && ! bibtex.trim().equals("")) {
					sc.setBibtexResult(bibtex);
					return true;
				} else
					throw new ScrapingFailureException("getting bibtex failed");
			}else
				throw new ScrapingFailureException("Needed ID is missing.");

		} catch (MalformedURLException me) {
			throw new InternalFailureException(me);
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}
	}


	/**
	 * Fixes some errors in BibTeX source code of ScienceDirect:
	 * - pages field should be XX--YY, not "XX - YY"
	 * - removing \r
	 *  
	 * @param bibtex
	 * @return
	 */
	protected String cleanBibTeX(final String bibtex) {
		if (bibtex != null) {
			/*
			 * replace \r
			 */
			String result = bibtex.replace("\r","");
			/*
			 * fix "pages" field
			 */
			Matcher matcher = patternBrokenPages.matcher(result);
			if (matcher.matches()) {
				result = matcher.replaceFirst("$1--$2");
			}
			
			result = DOIUtils.cleanDOI(result);
			
			return result;
		}
		return null;
	}


	public String getInfo() {
		return info;
	}


	@Override
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
