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

package org.bibsonomy.scraper.generic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.WebUtils;
import org.bibsonomy.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Scrapes pages providing BibTeX via the <a href="http://unapi.info/">UN-API</a>.
 * 
 * @author rja
 * @version $Id: UnAPIScraper.java,v 1.13 2011-04-29 07:24:27 bibsonomy Exp $
 */
public class UnAPIScraper implements Scraper {

	private static final String SITE_NAME = "UnAPIScraper";
	private static final String SITE_URL = "http://unapi.info/";
	private static final String INFO = "Scrapes pages providing BibTeX (format=bibtex) via <a href=\"http://unapi.info/\">UN-API</a>.";
	private static final Log log = LogFactory.getLog(UnAPIScraper.class);

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singleton(this);
	}

	public boolean scrape(ScrapingContext scrapingContext) throws ScrapingException {
		if (scrapingContext.getUrl() == null)
			return false;
		
		final String pageContents = scrapingContext.getPageContent();
		/*
		 * search for 
		 * 
		 * <link rel="unapi-server" type="application/xml" title="unAPI" href="http://canarydatabase.org/unapi" /> 
		 * 
		 * and
		 * 
		 * <abbr class='unapi-id' title='http://canarydatabase.org/record/488'> </abbr> 
		 */
		if (pageContents != null && pageContents.contains("unapi-server") && pageContents.contains("unapi-id")) {
			/*
			 * do the expensive JTidy stuff to extract the server and id
			 */
			final Document document = XmlUtils.getDOM(pageContents, true);
			/*
			 * get the server id
			 */
			final String href = getApiHref(document);
			if (href != null) {
				log.debug("found server id " + href);
				/*
				 * get record identifier
				 */
				final String id = getRecordIdentifier(document);
				if (id != null) {
					log.debug("found record id " + id);
					/*
					 * query for bibtex
					 */
					try {
						/*
						 * build URL to get record in bibtex format
						 */
						final URL url = new URL(href + "?format=bibtex&id=" + URLEncoder.encode(id, "UTF-8"));
						log.debug("querying service at " + url);
						/*
						 * get the data
						 */
						final String bibtex = WebUtils.getContentAsString(url);
						if (bibtex != null) {
							/*
							 * success! 
							 */
							log.debug("got bibtex (" + bibtex.length() + " characters)");
							scrapingContext.setScraper(this);
							scrapingContext.setBibtexResult(bibtex);
							return true;
						}
					} catch (IOException ex) {
						throw new InternalFailureException(ex);
					}
				}
			}
		}
		return false;
	}

	/** Extracts the "href" attribute from "link" tags whose "rel" attribute equals "unapi-server".
	 * 
	 * @param document
	 * @return The href attribute of the proper link-tag or <code>null</code> if it could not be found.
	 */
	private String getApiHref(final Document document) {
		final NodeList elementsByTagName = document.getElementsByTagName("link");
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			final Node node = elementsByTagName.item(i);
			final NamedNodeMap attributes = node.getAttributes();
			final Node relAttribute = attributes.getNamedItem("rel");
			if (relAttribute != null && "unapi-server".equals(relAttribute.getNodeValue())) {
				/*
				 * link to server found -> extract href
				 */
				final Node href = attributes.getNamedItem("href");
				if (href != null) {
					return href.getNodeValue();
				}
			}
		}
		return null;
	}

	
	/** Extracts the "title" attribute from the first (!) "abbr" tag whose "class" attribute equals "unapi-id".
	 * 
	 * @param document
	 * @return The "title" attribute of the proper abbr-tag or <code>null</code> if it could not be found.
	 * 
	 */
	private String getRecordIdentifier(final Document document) {
		/*
		 * debug
		 */
		final NodeList abbrTags = document.getElementsByTagName("abbr");
		log.debug("found " + abbrTags.getLength() + " abbr nodes.");
		for (int i = 0; i < abbrTags.getLength(); i++) {
			final Node node = abbrTags.item(i);
			final NamedNodeMap attributes = node.getAttributes();
			final Node classAttribute = attributes.getNamedItem("class");
			if (classAttribute != null && "unapi-id".equals(classAttribute.getNodeValue())) {
				/*
				 * record found -> extract id
				 */
				final Node title = attributes.getNamedItem("title");
				if (title != null) {
					return title.getNodeValue();
				}
			}
		}
		return null;

	}

	public boolean supportsScrapingContext(ScrapingContext scrapingContext) {
		if(scrapingContext.getUrl() != null){
			try {
				String pageContents = scrapingContext.getPageContent();
				if (pageContents != null && pageContents.contains("unapi-server") && pageContents.contains("unapi-id"))
					return true;
			} catch (ScrapingException ex) {
				return false;
			}
		}
		return false;
	}
	
	public static ScrapingContext getTestContext(){
		ScrapingContext context = null;
		try {
			context = new ScrapingContext(new URL("http://canarydatabase.org/record/488"));
		} catch (MalformedURLException ex) {
			log.debug(ex);
		}
		return context;
	}
	
	public String getInfo() {
		return INFO;
	}
	
	/**
	 * @return site name
	 */
	public String getSupportedSiteName(){
		return SITE_NAME;
	}
	
	
	/**
	 * @return site url
	 */
	public String getSupportedSiteURL(){
		return SITE_URL;
	}


}