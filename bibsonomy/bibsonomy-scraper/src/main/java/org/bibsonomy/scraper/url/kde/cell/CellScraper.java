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

package org.bibsonomy.scraper.url.kde.cell;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * @author tst
 * @version $Id: CellScraper.java,v 1.9 2011-04-29 07:24:34 bibsonomy Exp $
 */
public class CellScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME = "Cell";
	private static final String SITE_URL = "http://www.cell.com/";
	private static String INFO = "Scraper for Journals from " + href(SITE_URL, SITE_NAME)+".";
	
	private static String HOST_CELL = "cell.com";
	private static String PATH_BIOPHYSICAL = "/biophysj/";
	
	private static Pattern patternId = Pattern.compile("\\/abstract\\/([^\\/]*)");

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST_CELL), Pattern.compile(PATH_BIOPHYSICAL + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST_CELL), AbstractUrlScraper.EMPTY_PATTERN));
	}
	
	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	@Override
	protected boolean scrapeInternal(ScrapingContext sc)throws ScrapingException {
		sc.setScraper(this);
		
		String id = null;
		Matcher idMatcher = patternId.matcher(sc.getUrl().toString());
		if(idMatcher.find())
			id = idMatcher.group(1);
		
		if(id != null){
			String downloadUrl = null;
			
			String path = sc.getUrl().getPath();
			if(path.startsWith(PATH_BIOPHYSICAL))
				downloadUrl = "http://www.cell.com/citationexport?format=cite-abs&citation-type=RIS&pii=" + id + "&action=download&Submit=Export";
			else
				downloadUrl = "http://www.cell.com/biophysj/citationexport?format=cite-abs&citation-type=RIS&pii=" + id + "&action=download&Submit=Export";
			
			if(downloadUrl != null){
				try {
					String ris = null;
					
					ris = WebUtils.getContentAsString(downloadUrl);
					if(ris != null){
						String bibtex = null;
						
						RisToBibtexConverter converter = new RisToBibtexConverter();
						bibtex = converter.RisToBibtex(ris);
						
						if(bibtex != null){
							sc.setBibtexResult(bibtex);
							return true;
						}else
							throw new ScrapingFailureException("convert from ris to bibtex failed");
					}else
						throw new ScrapingFailureException("cannot download ris");
					
				} catch (MalformedURLException ex) {
					throw new InternalFailureException(ex);
				} catch (IOException ex) {
					throw new InternalFailureException(ex);
				}
			}else
				throw new ScrapingFailureException("building download url failed");
		}else
			throw new ScrapingFailureException("missing id in url");
	}

	public String getInfo() {
		return INFO;
	}

	public String getSupportedSiteName() {
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}

}
