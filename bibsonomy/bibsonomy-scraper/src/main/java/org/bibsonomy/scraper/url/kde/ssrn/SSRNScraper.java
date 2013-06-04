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

package org.bibsonomy.scraper.url.kde.ssrn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;
import org.bibsonomy.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Scraping Logger for access on http://www.ssrn.com/
 * @author tst
 * @version $Id: SSRNScraper.java,v 1.12 2011-04-29 07:24:37 bibsonomy Exp $
 */
public class SSRNScraper extends AbstractUrlScraper {

	private static final String SITE_NAME	   = "SSRN";
	private static final String SSRN_HOST_NAME = "http://papers.ssrn.com";
	private static final String SITE_URL	   = SSRN_HOST_NAME+"/";
	private static final String INFO		   = "This Scraper parses a publication from " + href(SITE_URL, SITE_NAME) +
	"and extracts the adequate BibTeX entry.";

	private static final String SSRN_ABSTRACT_PATH = "/sol3/papers.cfm?abstract_id=";
	private static final String SSRN_BIBTEX_PATH   = "/sol3/RefExport.cfm";
	private static final String SSRN_BIBTEX_PARAMS = "?function=download&format=2&abstract_id=";
	
	private static final String AUTHOR_PATTERN	= "author\\s*=\\s*[{]+(.+)[}]+";
	private static final String EDITOR_PATTERN	= "editor\\s*=\\s*[{]+(.+)[}]+";
	private static final String TITLE_PATTERN	= "title\\s*=\\s*[{]+(.+)[}]+";
	private static final String YEAR_PATTERN	= "year\\s*=\\s*[{]+(.+)[}]+";
	
	private static final String HOST = "ssrn.com";
	
	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	}
	
	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		String url = sc.getUrl().toString();
		if(url.startsWith(SSRN_HOST_NAME)) {
			String id = null;
			if(url.startsWith(SSRN_HOST_NAME + SSRN_ABSTRACT_PATH)) {
				id = url.substring(url.indexOf(SSRN_ABSTRACT_PATH) + SSRN_ABSTRACT_PATH.length());
			}
			
			if(url.startsWith(SSRN_HOST_NAME + SSRN_BIBTEX_PATH)) {
				id = url.substring(url.indexOf(SSRN_BIBTEX_PATH + "?abstract_id=") + (SSRN_BIBTEX_PATH + "?abstract_id=").length(), url.indexOf("&function"));
			}
			
			if(id != null) {
				String downloadLink = SSRN_HOST_NAME + SSRN_BIBTEX_PATH + SSRN_BIBTEX_PARAMS + id;
				String cookies = null;
				
				try {
					cookies = getCookies(sc.getUrl());
				} catch (IOException ex) {
					throw new InternalFailureException("Could not store cookies from " + sc.getUrl());
				}
				
				String bibtex  = null;
				String content = null;
				try {
					content = WebUtils.getContentAsString(new URL(downloadLink), cookies);
					final Document doc = XmlUtils.getDOM(content);
					NodeList list = doc.getElementsByTagName("input");
					
					for (int i = 0; i < list.getLength(); i++) {
						NamedNodeMap attributes = list.item(i).getAttributes();
						
						if (attributes.getNamedItem("value") != null) {
							bibtex = attributes.getNamedItem("value").getNodeValue().replaceAll("},", "},\n");
							String bibtexKey = generateBibtexKey(bibtex);
							bibtex = bibtex.replaceFirst(" ", bibtexKey + ",\n ");
						}
					}
					
				} catch (MalformedURLException ex) {
					throw new InternalFailureException("The url "+ downloadLink + " is not valid");
				} catch (IOException ex) {
					throw new ScrapingFailureException("BibTex download failed. Result is null!");
				}
				
				if(bibtex != null) {
					sc.setBibtexResult(bibtex);
					sc.setScraper(this);

					return true;
				}
			} else {
				throw new ScrapingFailureException("ID for donwload link is missing.");
			}
		}

		return false;
	}
		
	private String generateBibtexKey(String bibtex) {
		String authors	 = null;
		String editors	 = null;
		String year		 = null;
		String title	 = null;
		
		Pattern p = Pattern.compile(AUTHOR_PATTERN);
		Matcher m = p.matcher(bibtex);
		if (m.find()) {
			authors = m.group(1);
		}
		
		p = Pattern.compile(EDITOR_PATTERN);
		m = p.matcher(bibtex);
		if (m.find()) {
			editors = m.group(1);
		}
		
		p = Pattern.compile(TITLE_PATTERN);
		m = p.matcher(bibtex);
		if (m.find()) {
			title = m.group(1);
		}
		
		p = Pattern.compile(YEAR_PATTERN);
		m = p.matcher(bibtex);
		if (m.find()) {
			year = m.group(1);
		}
		
		return BibTexUtils.generateBibtexKey(authors, editors, year, title);
	}

	private String getCookies(URL queryURL) throws IOException {
		StringBuffer cookieString = new StringBuffer(WebUtils.getCookies(queryURL));
		
		cookieString.append(" ; CFCLIENT_SSRN=loginexpire%3D%7Bts%20%272009%2D12%2D12%2012%3A35%3A00%27%7D%23blnlogedin%3D1401777%23;domain=hq.ssrn.com;path=/; ");
		cookieString.append("SSRN_LOGIN=wbi%40cs%2Euni%2Dkassel%2Ede;domain=.ssrn.com;path=/; ");
		cookieString.append("SSRN_PW=Walde209;domain=.ssrn.com;path=/; ");
		
		return cookieString.toString();
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