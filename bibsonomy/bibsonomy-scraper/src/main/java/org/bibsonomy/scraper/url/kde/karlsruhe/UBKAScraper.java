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

/**
 * 
 */
package org.bibsonomy.scraper.url.kde.karlsruhe;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.PageNotSupportedException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;


/**
 * @author sre
 *
 */
public class UBKAScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "University Library (UB) Karlsruhe";
	private static final String UBKA_HOST_NAME = "http://www.ubka.uni-karlsruhe.de";
	private static final String SITE_URL = UBKA_HOST_NAME+"/";
	private static final String info = "This scraper parses a publication page from the " + href(SITE_URL, SITE_NAME)+".";

	private static final String UBKA_HOST = "ubka.uni-karlsruhe.de";
	private static final String UBKA_SEARCH_NAME = "http://www.ubka.uni-karlsruhe.de/hylib-bin/suche.cgi";
	private static final String UBKA_SEARCH_PATH = "/hylib-bin/suche.cgi";


	//bibtex id (fix value)
	private static final String UBKA_PARAM_BIBTEX   = "bibtex=1";
	//opac id (fix value)
	private static final String UBKA_PARAM_OPACDB   = "opacdb=UBKA_OPAC";
	//output id (free value, must be set)
	private static final String UBKA_PARAM_PRINTMAB = "printMAB=1";
	//query id (user dependent value)
	private static final String UBKA_PARAM_ND    	= "nd";

	private static final String  UBKA_BIB_PATTERN   = ".*<td valign=\"top\"\\s*>\\s*(@[A-Za-z]+&nbsp;\\s*\\{.+}\\s).*";
	private static final String  UBKA_COMMA_PATTERN = "(.*keywords\\s*=\\s*\\{)(.*?)(\\},?<br>.*)";	
	private static final String  UBKA_SPACE_PATTERN = "&nbsp;";
	private static final String  UBKA_BREAK_PATTERN = "<br>";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + UBKA_HOST), AbstractUrlScraper.EMPTY_PATTERN));

	
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		if(UBKA_SEARCH_PATH.equals(sc.getUrl().getPath())){
			/* URL looks some like this:
			 * http://www.ubka.uni-karlsruhe.de/hylib-bin/suche.cgi?opacdb=UBKA_OPAC&nd=256943346
			 * &session=1147556008&use_cookie_session=1&returnTo=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fhylib%2Fka_opac.html
			 */	
			String result = null;

			if(sc.getUrl().getQuery().contains(UBKA_PARAM_BIBTEX)){
				//current publication must be published as bibtex
				result = extractBibtexFromUBKA(sc.getPageContent());
			}else{
				//publication is not published as bibtex
				try {
					URL expURL = new URL(UBKA_SEARCH_NAME + "?" +  
							UBKA_PARAM_OPACDB + "&" +
							UBKA_PARAM_ND + "=" + extractQueryParamValue(sc.getUrl().getQuery(),UBKA_PARAM_ND) + "&" +
							UBKA_PARAM_PRINTMAB + "&" + 
							UBKA_PARAM_BIBTEX);
					//download page and extract bibtex
					result = extractBibtexFromUBKA(WebUtils.getContentAsString(expURL));
				} catch (IOException me) {
					throw new InternalFailureException(me);
				}
			}
			if(result != null){
				// append url
				result = BibTexUtils.addFieldIfNotContained(result, "url", sc.getUrl().toString());
				
				// add downloaded bibtex to result 
				sc.setBibtexResult(result);
				
				/*
				 * returns itself to know, which scraper scraped this
				 */
				sc.setScraper(this);

				return true;
			}else
				throw new ScrapingFailureException("getting bibtex failed");

		}else
			throw new PageNotSupportedException("This UBKA URL is not supported!");

	}

	/**
	 * This method extracts bibtex entries from 
	 * @param pageContent The page content in a string.
	 * @return Extracted bibtex entry as a string.
	 * @throws ScrapingException
	 */
	private String extractBibtexFromUBKA(String pageContent) throws ScrapingException{
		try{
			//replace <br>
			Pattern p = Pattern.compile(UBKA_BREAK_PATTERN);
			Matcher m = p.matcher(pageContent);
			pageContent = m.replaceAll("");

			p = Pattern.compile(UBKA_BIB_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
			m = p.matcher(pageContent);	
			if (m.matches()) {//we got the entry
				String bib = m.group(1);

				//replace spaces &nbsp;
				p = Pattern.compile(UBKA_SPACE_PATTERN);
				m = p.matcher(bib);
				bib = m.replaceAll(" ");

				//replace comma in keywords={bla, bla, bla bla}
				p = Pattern.compile(UBKA_COMMA_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
				m = p.matcher(bib);
				if (m.matches()){
					bib = m.group(1) + m.group(2).replaceAll(",", " ") + m.group(3);	        
				}

				// old fix, ubka fixed the missing "," after key in bibtex
				//bib = bib.replaceFirst("\n", ",\n");


				return bib;			
			}
		}catch(PatternSyntaxException pse){
			throw new InternalFailureException(pse);
		}
		return null;		
	}

	/**
	 * This method extracts the value of a specific parameter from a query string.
	 * @param query String representing the query part of an url. E.g. opacdb=UBKA_OPAC&nd=256943346
	 * &session=1147556008&use_cookie_session=1&returnTo=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fhylib%2Fka_opac.html
	 * @param name Name of param to extract the value from.
	 * @return extracted value
	 */
	private String extractQueryParamValue(String query, String name) throws ScrapingException{

		StringTokenizer st = new StringTokenizer(query,"&=",true);
		Properties params = new Properties();
		String previous = null;
		while (st.hasMoreTokens())
		{
			String currToken = st.nextToken();
			if ("?".equals(currToken) || "&".equals(currToken))
			{
				//ignore
			}else if ("=".equals(currToken))
			{
				try {
					params.setProperty(URLDecoder.decode(previous, "UTF-8"),URLDecoder.decode(st.nextToken(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new InternalFailureException(e);
				}
			}else{
				previous = currToken;
			}
		}

		return (String) params.get(name);
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
