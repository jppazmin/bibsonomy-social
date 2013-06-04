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

package org.bibsonomy.scraper.url.kde.aip;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
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
import org.bibsonomy.util.TagStringUtils;
import org.bibsonomy.util.WebUtils;


/**
 * Scraper for scitation.aip.org
 * It supports following urls:
 * - http://scitation.aip.org/vsearch/servlet/VerityServlet?
 * - http://scitation.aip.org/getabs/servlet/GetCitation?
 * - http://jcp.aip.orgs
 * @author tst
 * @version $Id: AipScitationScraper.java,v 1.18 2011-04-29 07:24:28 bibsonomy Exp $
 *
 */
public class AipScitationScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "AIP Scitation";
	private static final String SITE_URL = "http://scitation.aip.org/";
	private static final String INFO = "Extracts publications from " + href(SITE_URL, SITE_NAME) + 
	". Publications can be entered as a selected BibTeX snippet or by posting the page of the reference.";

	private static final Pattern hostPattern = Pattern.compile(".*" + "aip.org");
	private static final Pattern pathPattern = AbstractUrlScraper.EMPTY_PATTERN;

	private static final String URL_AIP_CITATION_BIBTEX_PAGE_PATH = "/getabs/servlet/GetCitation";
	private static final String URL_AIP_CITATION_BIBTEX_PAGE      = SITE_URL + "getabs/servlet/GetCitation?";
	private static final String URL_SPIE_AIP_CITATION_BIBTEX_PAGE = "http://spiedl.aip.org/getabs/servlet/GetCitation?";
	private static final String URL_DOI = "http://dx.doi.org/";

	/*
	 * supported mime types
	 */
	private static final String AIP_CONTENT_TYPE_HTML = "<!DOCTYPE html";
	private static final Pattern AIP_CONTENT_TYPE_HTML_PATTERN = Pattern.compile(AIP_CONTENT_TYPE_HTML);

	private static final Pattern inputPattern = Pattern.compile("<input(.*)>");
	private static final Pattern valuePattern = Pattern.compile("value=\"([^\"]*)\"");
	private static final Pattern namePattern = Pattern.compile("name=\"([^\"]*)\"");
	private static final Pattern keywordsPattern = Pattern.compile("keywords = \\{[^\\}]*\\}");

	/*
	 * html fields with static values
	 */
	private static final String HTML_INPUT_NAME_FN_AND_VALUE = "fn=view_bibtex2";
	private static final String HTML_INPUT_NAME_DOWNLOADCITATION_AND_VALUE = "downloadcitation=+Go+";

	/*
	 * html fields with dynamic values
	 */
	private static final String HTML_INPUT_NAME_SOURCE = "source";
	private static final String HTML_INPUT_NAME_PREFTYPE = "PrefType";
	private static final String HTML_INPUT_NAME_PREFACTION = "PrefAction";
	private static final String HTML_INPUT_NAME_SELECTCHECK = "SelectCheck";

	/*
	 * link before doi 
	 */
	private static final String LINK_BEFORE_DOI = "<a href=\"http://scitation.aip.org/jhtml/doi.jsp\">doi:</a>";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(hostPattern, pathPattern));
	
	/**
	 * Extract snippets from a bibtex page and single references from overview pages 
	 */
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		/*
		 * check of snippet
		 * snippet must be from a bibtex page
		 */			
		if(present(sc.getSelectedText()) && sc.getUrl().getPath().startsWith(URL_AIP_CITATION_BIBTEX_PAGE_PATH) && sc.getUrl().toString().contains(HTML_INPUT_NAME_FN_AND_VALUE)){
			sc.setBibtexResult(cleanKeywords(sc.getSelectedText()));
			
			return true;

			/*
			 * no snippet, check content from url
			 */
		} else{
			try {
				// get page content
				String aipContent = WebUtils.getContentAsString(sc.getUrl().toString());
				String selectcheck = null;
				
				// this often selects the ident string of related documents, not the requested
				Pattern selectCheckPattern = Pattern.compile("name=\"SelectCheck\"\\s+value=\"(\\w+)");
				Matcher selectCheckMatcher = selectCheckPattern.matcher(aipContent);
				
				// need an alternate pattern to get the correct ident string
				Pattern alternateSelectCheckPattern = Pattern.compile("[(]'Download',\\W{0,}'(\\w+)'[)]");
				Matcher alternateSelectCheckMather = alternateSelectCheckPattern.matcher(aipContent);
				
				
				if(alternateSelectCheckMather.find()) {
					selectcheck = alternateSelectCheckMather.group(1);
				} else if(selectCheckMatcher.find()) {
					selectcheck = selectCheckMatcher.group(1);
				}

				
				final Matcher m = AIP_CONTENT_TYPE_HTML_PATTERN.matcher(aipContent);
				
				if (m.find()) {
					/*
					 * if html content, build new link to bibtex content
					 */
					String aipContent2 = WebUtils.getContentAsString(SITE_URL + "journals/help_system/getabs/actions/download_citation_form.jsp");
					StringBuffer downloadURL = getBibTeXDownloadURL(aipContent2, URL_AIP_CITATION_BIBTEX_PAGE, selectcheck);
					
					// may be a spie link
					if(downloadURL == null){
						//extract doi
						int indexOfDOILink = aipContent.indexOf(LINK_BEFORE_DOI) + LINK_BEFORE_DOI.length();

						String startDOI = aipContent.substring(indexOfDOILink);
						String doi = startDOI.substring(0, startDOI.indexOf("\n"));
						String spieContent = WebUtils.getContentAsString(URL_DOI + doi);
						
						downloadURL = getBibTeXDownloadURL(spieContent, URL_SPIE_AIP_CITATION_BIBTEX_PAGE, selectcheck);
					}

					/*
					 * download and scrape bibtex
					 */
					if (downloadURL != null){
						String bibtexResult = WebUtils.getContentAsString(downloadURL.toString());
						sc.setBibtexResult(cleanKeywords(bibtexResult));
						return true;
					} else
						throw new ScrapingFailureException("getting bibtex failed");

				}else {
					/*
					 * if bibtex content, then use this content as snippet
					 */
					
					sc.setBibtexResult(cleanKeywords(aipContent));
					return true;
				}
			} catch (ConnectException cex) {
				throw new InternalFailureException(cex);
			} catch (IOException ioe) {
				throw new InternalFailureException(ioe);
			}
		}
	}

	/**
	 * Extract the value of the "value" attribute.
	 * @param input The input element as string.
	 * @param inputName The value of the name attribute.
	 * @return The value of the "value" attribute.
	 * @throws UnsupportedEncodingException
	 */
	private String getInputValue(String input, String inputName) throws UnsupportedEncodingException{
		String result = null;

		// search value attribute
		final Matcher valueMatcher = valuePattern.matcher(input);
		if(valueMatcher.find()){

			String value = valueMatcher.group(1);

			// value must be encoded to be used in url
			value = URLEncoder.encode(value, "UTF-8");

			// build parameter for url
			result = inputName + "=" + value;
		}

		return result;
	}

	
	/**
	 * The keywords field in bibtex references from scitation.aip.org are using ";" as delimiter. But it must be space seperated.
	 * @param bibtex Extracted bibtex content.
	 * @return Extracted bibtex content with valid keywords
	 */
	private String cleanKeywords(String bibtex){
		String result = bibtex;

		// storage for the parts of the bibtex reference
		String firstPart = null;
		String keywords = null;
		String secondPart = null;

		// search keywords field
		final Matcher keywordsMatcher = keywordsPattern.matcher(bibtex);

		if(keywordsMatcher.find()){
			// cut reference in 3 pieces
			keywords = keywordsMatcher.group();
			firstPart = bibtex.substring(0, bibtex.indexOf(keywords));
			secondPart = bibtex.substring(bibtex.indexOf(keywords) + keywords.length());

			// get the value of the keywords field
			keywords = keywords.substring(12, keywords.length()-1);

			// clean tag string
			keywords = TagStringUtils.cleanTags(keywords, true, ";", "_");

			// join the parts back to a complete bibtex reference
			result = firstPart + "keywords = {" + keywords + "}" + secondPart;
		}
		
		return result;
	}

	private StringBuffer getBibTeXDownloadURL(String aipContent, String aipPath, String selectcheckScript) throws UnsupportedEncodingException{
		// sarch input fields
		final Matcher inputMatcher = inputPattern.matcher(aipContent);

		String prefaction = null;
		String preftype = null;
		String selectcheck = null;
		String source = null;

		//check all input fields
		while(inputMatcher.find()){

			String input = inputMatcher.group(1);

			// check name values
			final Matcher nameMatcher = namePattern.matcher(input);

			if(nameMatcher.find()){

				String name = nameMatcher.group(1);

				// if name is supported, then extract its value
				if(name.contains(HTML_INPUT_NAME_PREFACTION)){
					prefaction = getInputValue(input, HTML_INPUT_NAME_PREFACTION);
				}else if(name.contains(HTML_INPUT_NAME_PREFTYPE)){
					preftype = getInputValue(input, HTML_INPUT_NAME_PREFTYPE);
				}else if(name.contains(HTML_INPUT_NAME_SELECTCHECK)){
					selectcheck = getInputValue(input, HTML_INPUT_NAME_SELECTCHECK);
				}else if(name.contains(HTML_INPUT_NAME_SOURCE)){
					source = getInputValue(input, HTML_INPUT_NAME_SOURCE);
				}
			}
		}
		
		// if selectcheck not found, then try with selectcheck from script block 
		if(selectcheck == null || selectcheck.equals("SelectCheck=null"))
			selectcheck = "SelectCheck=" + selectcheckScript;

		/*
		 * build bibtex link
		 */
		StringBuffer bibtexLink = null;
		if(source != null && preftype != null && prefaction != null && selectcheck != null){
			bibtexLink = new StringBuffer(aipPath);
			bibtexLink.append(HTML_INPUT_NAME_FN_AND_VALUE);
			bibtexLink.append("&");
			bibtexLink.append(prefaction);
			bibtexLink.append("&");
			bibtexLink.append(preftype);
			bibtexLink.append("&");
			bibtexLink.append(selectcheck);
			bibtexLink.append("&");
			bibtexLink.append(source);
			bibtexLink.append("&");
			bibtexLink.append(HTML_INPUT_NAME_DOWNLOADCITATION_AND_VALUE);
		}

		return bibtexLink;
	}

	public String getInfo() {
		return INFO;
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
