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

package org.bibsonomy.scraper.url.kde.pubmedcentral;

import java.io.IOException;
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

/** Scrapder for PubMed (http://www.pubmedcentral.nih.gov).
 * 
 * @author rja
 *
 */
public class PubMedCentralScraper extends AbstractUrlScraper {
	private static final String SITE_URL = "http://www.pubmedcentral.nih.gov/";
	private static final String SITE_NAME = "PubMedCentral";
	private static final String info = "This scraper parses a publication page of citations from " + href(SITE_URL, SITE_NAME)+".";
	
	private static final String HOST = "pubmedcentral.nih.gov";
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
	
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
			sc.setScraper(this);
			
			try {
				String bibtexresult = null;

				Pattern p = null;
				Matcher m = null;
				
				//save the original URL 
				String _origUrl = sc.getUrl().toString();
				
				//find the string in the content that contains the list_uid for hubmed.org
				p = Pattern.compile("meta.+content=\"(\\d+)\"");
				m = p.matcher(sc.getPageContent());

				
				//if the uid will be found, the bibtex string would be extracted from hubmed
				if (m.find()){
					String newUrl = "http://www.hubmed.org/export/bibtex.cgi?uids=" + m.group(1);
					bibtexresult = WebUtils.getContentAsString(new URL(newUrl));
				} 			
				
				//replace the humbed url through the original URL
				p = Pattern.compile("url = \".*\"");
				m = p.matcher(bibtexresult);

				if (m.find()){
					bibtexresult = m.replaceFirst("url = \"" + _origUrl + "\"" );
				}
			
				//-- bibtex string may not be empty
				if (bibtexresult != null && !"".equals(bibtexresult)) {
					sc.setBibtexResult(bibtexresult);
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