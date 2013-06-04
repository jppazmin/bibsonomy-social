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

package org.bibsonomy.scraper.url.kde.prola;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
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
import org.bibsonomy.util.WebUtils;


/**
 * Scraper for prola.aps.org. It scrapes selected bibtex snippets and selected articles.
 * @author tst
 */
public class ProlaScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "PROLA";
	private static final String PROLA_APS_URL_BASE = "http://prola.aps.org";
	private static final String SITE_URL = PROLA_APS_URL_BASE+"/";
	private static final String INFO = "For selected BibTeX snippets and articles from " + href(SITE_URL , SITE_NAME)+".";

	/*
	 * needed URLs and components
	 */
	private static final String PROLA_APS_HOST = ".aps.org";
	private static final String PROLA_APS_BIBTEX_PARAM = "type=bibtex";

	/*
	 * needed regular expressions to extract download link
	 */
	private static final Pattern linkPattern = Pattern.compile("<a\\b[^<]*</a>");
	private static final Pattern linkValuePattern = Pattern.compile(">(.*)<");
	private static final Pattern hrefPattern = Pattern.compile("href=\"([^\"]*)\"");
	private static final Pattern bibPattern = Pattern.compile("@\\b[^\\{@]*\\{.*", Pattern.DOTALL);


	/*
	 * value of download link
	 */
	private static final String DOWNLOAD_LINK_VALUE = "BibTeX";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + PROLA_APS_HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	public String getInfo() {
		return INFO;
	}

	/**
	 * Extract atricles from *.aps.org. It works with the article page, the bibtex page
	 */
	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		final String prolaPageContent = sc.getPageContent();

		/*
		 * check if selected page is a bibtex page
		 */
		if(sc.getUrl().getQuery() != null && sc.getUrl().getQuery().contains(PROLA_APS_BIBTEX_PARAM)){
			//remove comments bevor reference
			final StringBuffer bibtex = new StringBuffer(cleanBibtexEntry(sc.getPageContent()));
			
			// append url
			BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());

			// add downloaded bibtex to result 
			sc.setBibtexResult(bibtex.toString().trim());
			return true;						
		}

		// no snippet, download bibtex
		String downloadLink = null;

		// extract all links from downloaded page
		final Matcher linkMatcher = linkPattern.matcher(prolaPageContent);

		while (linkMatcher.find()) {
			String linkMatch = linkMatcher.group();

			// extract the value between the a tags

			final Matcher linkValueMatcher = linkValuePattern.matcher(linkMatch);

			if (linkValueMatcher.find()) {
				/*
				 * check if the link is the download bibtex link
				 */
				if (DOWNLOAD_LINK_VALUE.equals(linkValueMatcher.group(1))) {

					// extracted link is the bibtex download link, search href attribute 
					final Matcher hrefMatcher = hrefPattern.matcher(linkMatch);

					if (hrefMatcher.find()) {
						/*
						 * build url to bibtex of this article
						 */
						downloadLink = PROLA_APS_URL_BASE + hrefMatcher.group(1);
						break;
					}
				}
			}
		}

		try {
			// check if download link is found
			if (present(downloadLink)) {

				// download article as bibtex
				final String downloadedBibtex = WebUtils.getContentAsString(downloadLink);

				if (present(downloadedBibtex)) {

					//remove comments bevor reference
					final StringBuffer bibtex = new StringBuffer(cleanBibtexEntry(downloadedBibtex));
					
					// append url
					BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
					
					// add downloaded bibtex to result 
					sc.setBibtexResult(bibtex.toString().trim());
					
					return true;						
				} else
					throw new ScrapingException("ProlaScraper: can't get bibtex from this article");
			} else
				throw new PageNotSupportedException("ProlaScraper: This prola side has no bibtex download link.");
		} catch (IOException e) {
			throw new InternalFailureException(e);
		}
	}

	/**
	 * This method cuts of eveerything bevor bibtex entry.
	 * @param bibtexSnippet bibtex entry as String
	 * @return cleaned bibtex String
	 */
	private String cleanBibtexEntry(String bibtexSnippet){
		// search begin of bibtex entry
		final Matcher bibMatcher = bibPattern.matcher(bibtexSnippet);

		if (bibMatcher.find())
			// cut of everything bevor bibtex entry
			return bibMatcher.group();

		return null;
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
