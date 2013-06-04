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

package org.bibsonomy.scraper.url.kde.ams;

import java.io.IOException;
import java.util.Collections;
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
 * Scraper for ams.allenpress.com
 * @author tst
 * @version $Id: AmsScraper.java,v 1.17 2011-04-29 07:24:40 bibsonomy Exp $
 */
public class AmsScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "American Meteorological Society";
	private static final String SITE_URL = "http://ams.allenpress.com/";
	private static final String INFO = "For references from the "+href(SITE_URL, SITE_NAME)+".";
	
	//private static final String FORMAT_ENDNOTE = "&format=endnote";
	private static final String FORMAT_BIBTEX = "&format=bibtex";


	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*ams.allenpress.com"), AbstractUrlScraper.EMPTY_PATTERN));
	
	private static final Pattern pattern = Pattern.compile("doi=([^&]*)[&]?");
	
	public String getInfo() {
		return INFO;
	}

	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
			sc.setScraper(this);
			
			final Matcher matcher = pattern.matcher(sc.getUrl().toString());
			if (matcher.find()) {
				final String doi = matcher.group(1).replace("%2F", "/");
				
				//final String downloadUrl = "http://ams.allenpress.com/perlserv/?request=download-citation&t=endnote&f=1520-0485_38_1669&doi=" + doi + "&site=amsonline";
				final String downloadUrl = "http://journals.ametsoc.org/action/downloadCitation?doi=" + doi + "&include=cit";
				
				
				try {
					final String bibtexContent = WebUtils.getContentAsString(downloadUrl + FORMAT_BIBTEX);
					
					if(bibtexContent != null){
						sc.setBibtexResult(bibtexContent);
						return true;
					}else
						throw new ScrapingFailureException("failure during download");
					
					/*final String endnoteContent = WebUtils.getContentAsString(downloadUrl + FORMAT_ENDNOTE);
					
					if(endnoteContent != null){
						
						final EndnoteToBibtexConverter converter = new EndnoteToBibtexConverter();
						final String bibtex = converter.processEntry(endnoteContent);
						
						if(bibtex != null){
							sc.setBibtexResult(bibtex);
							return true;
						}else
							throw new ScrapingFailureException("failure during converting to bibtex");
					}else
						throw new ScrapingFailureException("failure during download");*/
					
				} catch (IOException e) {
					throw new InternalFailureException(e);
				}

				
			}else
				throw new PageNotSupportedException("not found DOI in URL");
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
