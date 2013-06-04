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

package org.bibsonomy.scraper.url.kde.karlsruhe;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * Scraper for liinwww.ira.uka.de/bibliography
 * @author tst
 * @version $Id: BibliographyScraper.java,v 1.11 2011-04-29 07:24:28 bibsonomy Exp $
 */
public class BibliographyScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "LIIN";
	private static final String SITE_URL = "http://liinwww.ira.uka.de/";
	private static final String INFO = "Scrapes BibTeX refrences from " + href(SITE_URL, SITE_NAME);
	
	private static final String HOST = "liinwww.ira.uka.de";
	private static final String PATH = "/cgi-bin/bibshow";
	
	private static final String BIBTEX_START_BLOCK = "<pre class=\"bibtex\">";
	private static final String BIBTEX_END_BLOCK = "</pre>";
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), Pattern.compile(PATH + ".*")));

	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
			sc.setScraper(this);
			
			String page = sc.getPageContent();
			
			if(page.indexOf(BIBTEX_START_BLOCK) > -1){
				// cut off first part
				page = page.substring(page.indexOf(BIBTEX_START_BLOCK)+20);
				
				// cut off end
				page = page.substring(0, page.indexOf(BIBTEX_END_BLOCK));
				
				// clean up - links and span
				page = page.replaceAll("<[^>]*>", "");
				
				/*
				 * TODO: uncomment theese lines to add the url to our bibtex entry.
				 * but here is the problem: the example url contains a '}'. means:
				 * we have to change something inside the PseudoLexer. (Line 270)
				 * 
				// append url
				page = BibTexUtils.addFieldIfNotContained(page, "url", sc.getUrl().toString());
				
				// remove multiple commas
				Pattern p = Pattern.compile(",(\\s*),", Pattern.MULTILINE);
				Matcher m = p.matcher(page);
				page = m.replaceAll(",$1");*/
				
				sc.setBibtexResult(page);
				return true;
			}else
				throw new ScrapingException("Can't find bibtex in scraped page.");			

	}

	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		// TODO Auto-generated method stub
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		// TODO Auto-generated method stub
		return SITE_URL;
	}
}
