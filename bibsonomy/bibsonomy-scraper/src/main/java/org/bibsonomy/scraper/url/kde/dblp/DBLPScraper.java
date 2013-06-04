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

package org.bibsonomy.scraper.url.kde.dblp;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.bibtex.parser.SimpleBibTeXParser;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

import bibtex.parser.ParseException;

/**
 * @author wbi
 * @version $Id: DBLPScraper.java,v 1.16 2011-04-29 07:24:37 bibsonomy Exp $
 */
public class DBLPScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "University of Trier Digital Bibliography & Library Project";
	private static final String DBLP_HOST_NAME1  = "http://dblp.uni-trier.de";
	private static final String SITE_URL  = DBLP_HOST_NAME1+"/";
	private static final String info = "This scraper parses a publication page from the " + href(SITE_URL, SITE_NAME)+".";

	private static final String DBLP_HOST1  = "dblp.uni-trier.de";
	private static final String DBLP_HOST_NAME2  = "http://search.mpi-inf.mpg.de/dblp/";
	private static final String DBLP_HOST2  = "search.mpi-inf.mpg.de";
	private static final String DBLP_PATH2  = "/dblp/";

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + DBLP_HOST1), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + DBLP_HOST2), Pattern.compile(DBLP_PATH2 + ".*")));
	}
	
	/*
	 * These are no mirrors, they just link to above hosts
	 */
	/*
	private static final String DBLP_HOST_NAME3  = "http://www.sigmod.org/dblp/";
	private static final String DBLP_HOST_NAME4  = "http://www.vldb.org/dblp/";
	private static final String DBLP_HOST_NAME5  = "http://sunsite.informatik.rwth-aachen.de/dblp/";
	 */
	private static final Pattern DBLP_PATTERN = Pattern.compile("(<pre>\\s*(@[A-Za-z]+\\s*\\{.+?\\})\\s*</pre>)+", Pattern.MULTILINE | Pattern.DOTALL);


	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);
		
		String bibtexContent = "";

		final Matcher m = DBLP_PATTERN.matcher(sc.getPageContent());
		while(m.find()) {
			// get the bibtex entry out of the <pre> blocks
			String bibtexResult = m.group(2);
			
			// append url
			BibTexUtils.addFieldIfNotContained(bibtexResult, "url", sc.getUrl().toString());
			
			// add them to the final bibtex list
			bibtexContent += bibtexResult.replaceFirst("<a href=\".*?\">(.*)</a>", "$1");
		}

		// if there are any bibtex entries
		if (!"".equals(bibtexContent)){
			try {
				SimpleBibTeXParser parser = new SimpleBibTeXParser();
				// parse and get them as string
				BibTex bibtex = parser.parseBibTeX(bibtexContent);
				
				// if there are no exception and bibtex is not null
				if (bibtex != null && parser.getCaughtExceptions() == null){
					// ... set the result and return true
					sc.setBibtexResult(BibTexUtils.toBibtexString(bibtex));
					return true;
				} else {
					return false;
				}
			} catch (ParseException ex) {
				throw new InternalFailureException(ex);
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			} 
		} else {
			throw new ScrapingFailureException("failure during download");
		}
	}

	public String getInfo() {
		return info;
	}

	public Collection<Scraper> getScraper() {
		return Collections.singletonList((Scraper) this);
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


