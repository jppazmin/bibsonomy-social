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

package org.bibsonomy.scraper.url.kde.springer;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.util.WebUtils;



/** Scraper für SpringerLink.
 * 
 * @author rja
 *
 */
public class SpringerLinkScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "SpringerLink";
	private static final String SITE_URL = "http://springerlink.com/";

	private static final Pattern CONTENT_PATTERN = Pattern.compile("content/(.+?)(/|$)");
	private static final Pattern ID_PATTERN = Pattern.compile("id=([^\\&]*)");

	private static final Pattern VIEW_STATE_PATTERN = Pattern.compile("id=\"__VIEWSTATE\" value=\"(.+?)\"");
	private static final Pattern EVENT_VALIDATION_PATTERN = Pattern.compile("id=\"__EVENTVALIDATION\" value=\"(.+?)\"");

	
	private static final String SPRINGER_CITATION_HOST_COM = "springerlink.com";
	private static final String SPRINGER_CITATION_HOST_DE = "springerlink.de";
	private static final String SPRINGER_LINK_METAPRESS = "springerlink.metapress.com";

	private static final String INFO = "This scraper parses a publication page from " + href(SITE_URL, SITE_NAME)+".";


	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static{
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SPRINGER_CITATION_HOST_COM), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SPRINGER_CITATION_HOST_DE), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + SPRINGER_LINK_METAPRESS), AbstractUrlScraper.EMPTY_PATTERN));

	}

	@Override
	protected boolean scrapeInternal(final ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		try {
			final String url = sc.getUrl().toString();
			/*
			 *  extract document ID
			 */
			final String docid;
			final Matcher mContent = CONTENT_PATTERN.matcher(url);
			final Matcher mId = ID_PATTERN.matcher(url);
			if (mContent.find()) {
				docid = mContent.group(1);
			} else if (mId.find()) {
				docid = mId.group(1);
			} else {
				/*
				 * could not find ID
				 */
				return false;
			}

			/*
			 * We need to get this page to extract the hidden form fields 
			 * "__VIEWSTATE" and "__EVENTVALIDATION".
			 *  
			 * Without those form fields we don't get access to the BibTeX
			 * entry.
			 */

			final String formPage = WebUtils.getContentAsString(SITE_URL + "content/" + docid + "/export-citation/");

			final Matcher viewStateMatcher = VIEW_STATE_PATTERN.matcher(formPage);
			final Matcher eventValidationMatcher = EVENT_VALIDATION_PATTERN.matcher(formPage);
			if (viewStateMatcher.find() && eventValidationMatcher.find()) {
				/*
				 * both form fields found! :-)
				 */
				final String postContent = 
					"__VIEWSTATE=" + UrlUtils.safeURIEncode(viewStateMatcher.group(1)) + 
					"&ctl00%24ctl18%24cultureList=en-us" +
					"&ctl00%24ctl18%24SearchControl%24BasicSearchForTextBox=" +
					"&ctl00%24ctl18%24SearchControl%24BasicAuthorOrEditorTextBox=" +
					"&ctl00%24ctl18%24SearchControl%24BasicPublicationTextBox=" +
					"&ctl00%24ctl18%24SearchControl%24BasicVolumeTextBox=" +
					"&ctl00%24ctl18%24SearchControl%24BasicIssueTextBox=" +
					"&ctl00%24ctl18%24SearchControl%24BasicPageTextBox=" +
					"&ctl00%24ContentPrimary%24ctl00%24ctl00%24Export=AbstractRadioButton" +
					"&ctl00%24ContentPrimary%24ctl00%24ctl00%24Format=TextRadioButton" +
					"&ctl00%24ContentPrimary%24ctl00%24ctl00%24CitationManagerDropDownList=BibTex" +
					"&ctl00%24ContentPrimary%24ctl00%24ctl00%24ExportCitationButton=Export+Citation" +
					"&__EVENTVALIDATION=" + UrlUtils.safeURIEncode(eventValidationMatcher.group(1));
				final String bibtexEntry = WebUtils.getPostContentAsString(new URL(SITE_URL + "content/" + docid + "/export-citation/"), postContent);

				/*
				 * Job done
				 */
				if (present(bibtexEntry)) {
					sc.setBibtexResult(cleanEntry(bibtexEntry));
					return true;
				} 
			}
			throw new ScrapingFailureException("getting bibtex failed");
		} catch (MalformedURLException e) {
			throw new InternalFailureException(e);
		} catch (IOException e) {
			throw new InternalFailureException(e);
		}
	}

	/**
	 * Cleans up some things in the BibTeX entry.
	 * 
	 * @param s
	 * @return
	 */
	private static String cleanEntry(final String s) {
		/*
		 * The DOI is hidden in the "note" field -> rename it to "doi".
		 */
		final String s1 = s.replace("note = {", "doi = {");
		/*
		 * The publisher field not only contains the publisher name (i.e.,
		 * "Springer") but also the address (i.e., "Berlin / Heidelberg"). We
		 * split the field into two fields "publisher" and "address".
		 */
		final String s2 = s1.replace("Springer Berlin", "Springer},\n   address = {Berlin");
		/*
		 * There is a space between the entry type and the first "{" - that is
		 * not valid BibTeX. Therefore, we remove that space.
		 */
		final String s3 = s2.replaceFirst(" \\{", "{");
		
		return s3;
	}

	public String getInfo() {
		return INFO;
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
