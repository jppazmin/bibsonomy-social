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

package org.bibsonomy.scraper.url.kde.spires;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Scraper for the SLAC National Accelerator Laboratory
 * @author rja
 *
 */
public class SpiresScraper extends AbstractUrlScraper{
	private static final String SITE_NAME = "SLAC National Accelerator Laboratory";
	private static final String SITE_URL = "http://slac.stanford.edu/";
	private static final String FORMAT_WWWBRIEFBIBTEX = "FORMAT=WWWBRIEFBIBTEX";

	private static final String info = "Gets publications from " + href(SITE_URL, SITE_NAME)+".";

	private static final List<Tuple<Pattern, Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();
	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + "slac.stanford.edu"), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + "www-library.desy.de"), AbstractUrlScraper.EMPTY_PATTERN));
	}
	
	
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
			sc.setScraper(this);
			
			try {
				final URL url = sc.getUrl();
				
				URL bibtexUrl = url;
				if (!url.getQuery().contains(FORMAT_WWWBRIEFBIBTEX)) { 
					bibtexUrl = new URL(url.toString() + "&" + FORMAT_WWWBRIEFBIBTEX);
				}
				
				final Document temp = XmlUtils.getDOM(bibtexUrl);
				
				//extract the bibtex snippet which is embedded in pre tags
				String bibtex = null;
				final NodeList nl = temp.getElementsByTagName("pre"); //get the pre tags (normally one)
				for (int i = 0; i < nl.getLength(); i++) {
					Node currNode = nl.item(i);
					if (currNode.hasChildNodes()){
						bibtex = currNode.getChildNodes().item(0).getNodeValue();	
					}
				}	
				
				/*
				 * add URL
				 */
				bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", url.toString());
				
				//-- bibtex string may not be empty
				if (bibtex != null && ! "".equals(bibtex)) {
					sc.setBibtexResult(bibtex);
					return true;
				}else
					throw new ScrapingFailureException("getting bibtex failed");

				
			} catch (IOException e) {
				throw new InternalFailureException(e);
			}
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