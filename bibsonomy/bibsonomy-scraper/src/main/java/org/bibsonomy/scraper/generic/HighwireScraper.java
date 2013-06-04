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
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * 
 * @version $Id: HighwireScraper.java,v 1.10 2011-04-29 07:24:26 bibsonomy Exp $
 */
public class HighwireScraper implements Scraper {
	private static final String SITE_NAME = "Highwire Scraper Collection";
	private static final String SITE_URL = "http://highwire.stanford.edu/lists/allsites.dtl";
	private static final String INFO 	= "This scraper parses a publication page from one of these <a href=\"http://highwire.stanford.edu/lists/allsites.dtl\">journals hosted by Highwire Press</a>  " +
											"and extracts the adequate BibTeX entry.";

	//Pattern p = Pattern.compile("/cgi/citmgr\\?(gca=\\w+;\\d+/\\d+/[\\w+]*\\d+[&]*)+");
	private static final Pattern urlPattern = Pattern.compile("/cgi/citmgr\\?gca=[\\w+;/&=.-]+");
	
	public boolean scrape(ScrapingContext sc) throws ScrapingException {
		if (sc.getUrl() != null) { //-- url shouldn't be null

			/*
			 * test if the export link is available: /cgi/citmgr?gca=abcd;999/99/99
			 * 
			 * If not, this scraper can't do anything and hence returns false. It does NOT 
			 * throw an exception because the IEScraper might do its job.
			 * 
			 */
			String pageContent;
			try {
				pageContent = sc.getPageContent();
			} catch (ScrapingException e) {
				return false;
			}
			
			Matcher m = urlPattern.matcher(pageContent);
			
			try {
				//-- if its available extract the needed parts and form the final bibtex export url
				if (m.find()){
					sc.setScraper(this);
					
					//-- to export the bibtex we need to replace ? through ?type=bibtex
					String exportUrl = m.group(0).replaceFirst("\\?","?type=bibtex&");

					//-- form the host url and put them together 
					String newUrl = "http://" + sc.getUrl().getHost() + exportUrl;

					//-- get the bibtex export and throw new ScrapingException if the url is broken
					String bibtexresult = WebUtils.getContentAsString(new URL(newUrl));

					/*
					 * Need to fix the bibtexkey. Its necessary to replace
					 * ALL whitespace through underscores otherwise the import 
					 * will crash.
					 */
					//-- create the pattern to finde the bibtexkey
					Pattern pa1 = Pattern.compile("@\\w+\\{.+,");
					Matcher ma1 = pa1.matcher(bibtexresult);

					//-- for every match ...
					while(ma1.find()){
						String bibtexpart = ma1.group(0);
						Pattern pat = Pattern.compile("\\s");
						Matcher mat = pat.matcher(bibtexpart);
						// ... check if whitespaces are existing and replace 
						// them through underscore
						if (mat.find()){
							String preparedbibtexkey = mat.replaceAll("_");
							bibtexresult = bibtexresult.replaceFirst(Pattern.quote(bibtexpart), preparedbibtexkey);
						}
					}


					//-- bibtex string may not be empty
					if (bibtexresult != null && !"".equals(bibtexresult)) {
						sc.setBibtexResult(bibtexresult);
						return true;
					}
					
					throw new ScrapingFailureException("getting bibtex failed");
				}

			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}
		}
		//-- This Scraper can`t handle the specified url
		return false;
	}

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}

	public boolean supportsScrapingContext(ScrapingContext sc) {
		if (sc.getUrl() != null) {
			String pageContent;
			try {
				pageContent = sc.getPageContent();
			} catch (ScrapingException e) {
				return false;
			}
			
			Matcher m = urlPattern.matcher(pageContent);
			
			if (m.find())
				return true;
		}
		return false;
	}
	
	public static ScrapingContext getTestContext(){
		ScrapingContext context = new ScrapingContext(null);
		try {
			context.setUrl(new URL("http://mend.endojournals.org/cgi/gca?sendit=Get+All+Checked+Abstract(s)&gca=17%2F1%2F1"));
		} catch (MalformedURLException ex) {
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
