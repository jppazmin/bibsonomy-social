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
import org.bibsonomy.util.WebUtils;

/** Scraper for AIFB.
 * 
 * @author ccl
 *
 */
public class AIFBScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME		= "Institut AIFB Universität Karlsruhe";
	private static final String AIFB_SITE_NAME	= "AIFB";
	private static final String AIFB_HOST_NAME	= "http://www.aifb.kit.edu";
	private static final String AIFB_HOST		= "aifb.kit.edu";
	private static final String AIFB_WEB		= "/web/";
	private static final String info			= "This scraper parses institute, research group and " +
												  "people-specific pages from the " +
												  href("http://www.aifb.uni-karlsruhe.de/", SITE_NAME);
	
    private static final String DOWNLOAD_HREF_STRING	= "<a href=\"(.+?format%3Dkiteva[^\"]+)\"";
    private static final Pattern DOWNLOAD_HREF_PATTERN	= Pattern.compile(DOWNLOAD_HREF_STRING);
    
    private static final String URL		 = "url";
    
    private static final String WC				= ".*";
    private static final String ARTICLE			= "Article\\d+";
    private static final String INPROCEEDINGS	= "Inproceedings\\d+";
    private static final String BOOK			= "Book\\d+";
    private static final String INCOLLECTION	= "Incollection\\d+";
    private static final String PROCEEDINGS		= "Proceedings\\d+";
    private static final String PHDTHESIS		= "Phdthesis\\d+";
    private static final String TECHREPORT		= "Techreport\\d+";
    private static final String DELIVERABLE		= "Deliverable\\d+";
    private static final String UNPUBLISHED		= "Unpublished\\d+";
    
	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

    static {
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + ARTICLE)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + INPROCEEDINGS)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + BOOK)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + INCOLLECTION)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + PROCEEDINGS)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + PHDTHESIS)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + TECHREPORT)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + DELIVERABLE)));
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + AIFB_HOST), Pattern.compile(AIFB_WEB + UNPUBLISHED)));
    }

	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);
		String bibtex = null;		
		Matcher _m = DOWNLOAD_HREF_PATTERN.matcher(sc.getPageContent());
		
		if (_m.find()) {
			try {
				bibtex = WebUtils.getContentAsString(AIFB_HOST_NAME + _m.group(1));
			} catch (Exception e) {
				throw new InternalFailureException(e);
			}
		}
				
		if (bibtex != null) {
			bibtex = BibTexUtils.addFieldIfNotContained(bibtex, URL, sc.getUrl().toString());
			sc.setBibtexResult(bibtex.toString());
			return true;
		}		
		return false;
	}
	
	public String getInfo() {
		return info;
	}

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		return AIFB_SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return AIFB_HOST_NAME;
	}
}
