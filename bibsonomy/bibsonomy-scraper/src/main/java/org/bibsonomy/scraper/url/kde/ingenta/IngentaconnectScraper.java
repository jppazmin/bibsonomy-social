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

package org.bibsonomy.scraper.url.kde.ingenta;

import java.io.IOException;
import java.net.MalformedURLException;
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
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;
import org.bibsonomy.util.id.DOIUtils;

/** Scraper for ingentaconnect.
 * @author rja
 *
 */
public class IngentaconnectScraper extends AbstractUrlScraper{

	private static final String SITE_NAME = "Ingentaconnect";
	private static final String SITE_URL = "http://www.ingentaconnect.com/";
	private static final String info = "This scraper parses a publication page from " + href(SITE_URL, SITE_NAME)+".";

	private static final String INGENTA_HOST = "ingentaconnect.com";


	private static final Pattern exportPattern = Pattern.compile("BibText Export\" href=\"(.*)\"");

	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + INGENTA_HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {

		sc.setScraper(this);

		// This Scraper might handle the specified url
		try {

			/* 
			 * create query URL
			 */
			String URLString = SITE_URL.substring(0, SITE_URL.length() - 1);
			String page = sc.getPageContent();
			
			final Matcher m = exportPattern.matcher(page);
			
			if (m.find()) {
				URLString = URLString + m.group(1);
			} else {
				return false;
			}

			URL queryURL = new URL(URLString);

			String cookies      = null;
			String bibtexResult = null;
			
			try {
				cookies = WebUtils.getCookies(queryURL);
				bibtexResult = WebUtils.getContentAsString(queryURL, cookies);
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}

			/*
			 * fix bibtex
			 */
			StringBuffer buffer = new StringBuffer();
			
			if (bibtexResult != null) {
				String[] lines = bibtexResult.split("\n");
				lines[0] = lines[0].replaceAll(" ", "");
				StringBuffer authorBuffer = new StringBuffer("author = \"");
				
				boolean firstAuthor = true;
				
				for (int i = 0; i < lines.length - 1; i++) {
					//transform author-lines to ONE author-line
					if (lines[i].contains("author")){
						if (firstAuthor){
							authorBuffer.append(lines[i].substring(lines[i].indexOf("\"") + 1, lines[i].lastIndexOf("\"")));
							firstAuthor = false;
						}
						else{
							authorBuffer.append(" and " + lines[i].substring(lines[i].indexOf("\"") + 1, lines[i].lastIndexOf("\"")));
						}
					}
					else{
						lines[i] = removeHTML(lines[i]);
						//append missing ","
						if (!lines[i].endsWith(",")){
							buffer.append(lines[i] + ",\n");
						}
						else{
							buffer.append(lines[i] + "\n");
						}
					}
				}

				//add author-line
				authorBuffer.append("\"}");
				buffer.append(authorBuffer);
				bibtexResult = DOIUtils.cleanDOI(buffer.toString());
			}
			
			if (bibtexResult != null) {
				sc.setBibtexResult(bibtexResult);
				return true;
			} else {
				throw new ScrapingFailureException("getting bibtex failed");
			}
			
		} catch (MalformedURLException e) {
			throw new InternalFailureException(e);
		}
	}

	private String removeHTML(String line) {		
		line = line.replaceAll("<.?p>|<.?P>", "");
		line = line.replaceAll("<.?b>|<.?B>", "");
		line = line.replaceAll("<.?i>|<.?I>", "");
		line = line.replaceAll("<.?u>|<.?U>", "");
		line = line.replaceAll("<.?hr>|<.?HR>", "");
		line = line.replaceAll("<.?br>|<.?BR>", "");
		line = line.replaceAll("<.?sup>|<.?SUP>", "");
		line = line.replaceAll("<.?sub>|<.?SUB>", "");
		line = line.replaceAll("&#[0-9]*;", "");

		//replace images with alt
		line = line.replaceAll("<[iI][mM][gG] .* [aA][lL][tT]=\"", "");
		line = line.replaceAll("\">", "");
		//remove images without alt
		line = line.replaceAll("<[iI][mM][gG].*>", "");

		//remove everything
		String newline = line.replaceAll("<.*>", "");
//		if (newline.length() < line.length()){
//			System.out.println("DEBUG: irgendwas weggeworfen!");
//			System.out.println("ALT: " + line);
//			System.out.println("NEU: " + newline);
//		}

		return newline;
	}

	public String getInfo() {
		return info;
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
