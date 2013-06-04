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

package org.bibsonomy.scraper.url.kde.iop;

import java.io.IOException;
import java.net.MalformedURLException;
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
 * SCraper for http://www.iop.org
 * @author tst
 * @version $Id: IOPScraper.java,v 1.19 2011-04-29 07:24:43 bibsonomy Exp $
 */
public class IOPScraper extends AbstractUrlScraper {

	/*
	 * URL parts
	 */

	private static final String IOP_URL_PATH_START = "/EJ";
	private static final String IOP_EJ_URL_BASE    = "http://www.iop.org";
	private static final String SITE_NAME = "IOP";
	private static final String SITE_URL = IOP_EJ_URL_BASE + IOP_URL_PATH_START;

	private static final String INFO = "Scraper for electronic journals from " + href(SITE_URL, SITE_NAME);

	private static final String IOP_HOST           = "iop.org";
	private static final String NEW_IOP_HOST       = "iopscience.iop.org";
	

	/*
	 * needed regular expressions to extract download citation link
	 */
	private static final Pattern linkPattern = Pattern.compile("<a\\b[^<]*</a>");
	private static final Pattern linkValuePattern = Pattern.compile(">(.*)<");
	private static final Pattern hrefPattern = Pattern.compile("href=\"[^\"]*\"");
	private static final Pattern formPublicationIdPattern = Pattern.compile("<input.*type=\"hidden\".*name=\"articleId\".*value=\"(.*)\".*/>");
	
	
	
	/*
	 * value of citation download link
	 */
	private static final String DOWNLOAD_LINK_VALUE = "Download citation";

	private static final List<Tuple<Pattern, Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();
	static{
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + IOP_HOST), Pattern.compile(IOP_URL_PATH_START + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + NEW_IOP_HOST), AbstractUrlScraper.EMPTY_PATTERN));
	}
		
		
	
	public String getInfo(){
		return INFO;
	}
	/**
	 * This scraper extract the citation download link and builds the direct link to the bibtex reference.
	 * It supports only http://www.iop.org sides which starts in the path with "/EJ". EJ stands for electrionic journals.
	 */
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		// download article page
		String articlePageContent = sc.getPageContent();

		// the link to the citation
		String citationLink = null;

		final Matcher publicationIdMatcher = formPublicationIdPattern.matcher(articlePageContent);
		String pubId = "";
		while(publicationIdMatcher.find())
			pubId = publicationIdMatcher.group(1);
		
		String postArgs = "articleId="+pubId +
						  "&exportType=abs" +
						  "&exportFormat=iopexport_bib";
		
		String bibtex = "";
		try {
			bibtex = WebUtils.getPostContentAsString(new URL("http://iopscience.iop.org/export"), postArgs, "utf8");
		} catch (MalformedURLException ex) {
			throw new ScrapingFailureException("URL to scrape does not exist. It maybe malformed.");
		} catch (IOException ex) {
			throw new ScrapingFailureException("An unexpected IO error has occurred. Maybe IOP is down.");
		}
		if(bibtex != null){
			sc.setBibtexResult(bibtex.trim());
			return true;
		}
		return false;
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
