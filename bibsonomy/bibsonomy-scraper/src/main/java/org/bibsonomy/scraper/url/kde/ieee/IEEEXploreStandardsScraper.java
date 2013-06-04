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

package org.bibsonomy.scraper.url.kde.ieee;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.WebUtils;
import org.bibsonomy.util.XmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Scraper for IEEE Explore
 * @author rja
 *
 */
public class IEEEXploreStandardsScraper extends AbstractUrlScraper {
	private static final String SITE_NAME 	= "IEEEXplore Standards";
	private static final String SITE_URL  	= "http://ieeexplore.ieee.org/";
	private static final String info 		= "This scraper creates a BibTeX entry for the standards at " + href(SITE_URL, SITE_NAME)+".";

	
	private static final Log log = LogFactory.getLog(IEEEXploreStandardsScraper.class);
	
	private static final String IEEE_HOST        	 	  = "ieeexplore.ieee.org";
	private static final String IEEE_STANDARDS_PATH   	  = "xpl";
	private static final String IEEE_STANDARDS		 	  = "@misc";
	private static final String IEEE_STANDARDS_IDENTIFIER = "punumber";
	
	private static final String CONST_EISBN               = "E-ISBN: ";
	private static final String CONST_PAGE                = "Page(s): ";
	private static final String CONST_DATE                = "Publication Date: ";

	private static final Pattern pattern = Pattern.compile("arnumber=([^&]*)");
	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + IEEE_HOST), Pattern.compile("/" + IEEE_STANDARDS_PATH + ".*")));

	
	protected boolean scrapeInternal (ScrapingContext sc) throws ScrapingException {
		if (sc.getUrl().toString().indexOf(IEEE_STANDARDS_IDENTIFIER) != -1 ) {
			sc.setScraper(this);
			
			Matcher matcher = pattern.matcher(sc.getUrl().toString());
			if(matcher.find()){
				String downUrl = "http://ieeexplore.ieee.org/xpl/downloadCitations?citations-format=citation-abstract&download-format=download-bibtex&fromPageName=abstract&recordIds=" + matcher.group(1);
				String bibtex = null;
				try {
					bibtex = WebUtils.getContentAsString(new URL(downUrl));
				} catch (IOException ex) {
					throw new InternalFailureException(ex);
				}
				
				if(bibtex != null){
					// clean up
					bibtex = bibtex.replace("<br>", "");
					
					// append url
					bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
					
					// add downloaded bibtex to result 
					sc.setBibtexResult(bibtex.toString().trim());
					return true;
					
				}else{
					log.debug("IEEEXploreStandardsScraper: direct bibtex download failed. Use JTidy to get bibliographic data.");
					sc.setBibtexResult(ieeeStandardsScrape(sc));
					return true;
					
				}
			}else{
				log.debug("IEEEXploreStandardsScraper use JTidy to get Bibtex from " + sc.getUrl().toString());
				sc.setBibtexResult(ieeeStandardsScrape(sc));
				return true;
			}
		}
		return false;
	}

	public String getInfo() {
		return info;
	}

	public String ieeeStandardsScrape (ScrapingContext sc) throws ScrapingException {
		try{
			//-- init all NodeLists and Node
			NodeList pres 		= null; 
			Node currNode 		= null;
			NodeList temp 		= null;
	
			//-- init String map for bibtex entries
			String type 		= IEEE_STANDARDS;
			String url 			= sc.getUrl().toString();
			String numpages 	= "";
			String title 		= "";
			String isbn 		= "";
			String abstr	 	= "";
			String year 		= "";
	
			//-- get the html doc and parse the DOM
			final Document document = XmlUtils.getDOM(sc.getPageContent());
	
			/* -- get the spans to extract the title and abstract
			 */
			pres = null;
			pres = document.getElementsByTagName("span"); //get all <span>-Tags
			for (int i=0; i<pres.getLength(); i++){
				currNode = pres.item(i);
				if (currNode.hasAttributes()) {
					Element g = (Element)currNode;
					Attr own = g.getAttributeNode("class");
					//-- Extract the title
					if ("headNavBlueXLarge2".equals(own.getValue())){
						temp = currNode.getChildNodes();
						title = temp.item(temp.getLength()-1).getNodeValue().trim();
					}
					//-- Extract the abstract
					if ("sectionHeaders".equals(own.getValue()) && "Abstract".equals(currNode.getFirstChild().getNodeValue())){
						abstr = currNode.getParentNode().getLastChild().getNodeValue().trim();
					}
				}
			}
	
	
			/*-- get all <p>-Tags to extract the standard informations
			 *  In every standard page the css-class "bodyCopyBlackLargeSpaced"
			 *  indicates the collection of all informations.
			 * */
			pres = null;
			pres = document.getElementsByTagName("p"); //get all <p>-Tags
			for (int i=0; i<pres.getLength(); i++){
				currNode = pres.item(i);
				if (currNode.hasAttributes()) {
					Element g = (Element)currNode;
					Attr own = g.getAttributeNode("class");
					if ("bodyCopyBlackLargeSpaced".equals(own.getValue())){
						temp = currNode.getChildNodes();
	
						for(int j =0; j<temp.getLength(); j++){
							if (temp.item(j).getNodeValue().indexOf(CONST_DATE) != -1){
								String date = temp.item(j).getNodeValue().substring(CONST_DATE.length()).trim();
								year = date.substring(date.length()-4).trim();
							}
							if (temp.item(j).getNodeValue().indexOf(CONST_PAGE) != -1){
								numpages = temp.item(j).getNodeValue().substring(CONST_PAGE.length()).trim();
							}
							if (temp.item(j).getNodeValue().indexOf(CONST_EISBN) !=  -1){
								isbn = temp.item(j).getNodeValue().substring(CONST_EISBN.length()).trim();
							}
						}
					}
				}
			}
	
			//create valid bibtex snippet
			return type + " {," 
						+ "title = {" + title + "}, " 
						+ "year = {" + year + "}, " 
						+ "url = {" + url + "}, "
						+ "pages = {" + numpages + "}, " 
						+ "abstract = {" + abstr + "}, "
						+ "isbn = {" + isbn + "}}";
		
		}catch(Exception e){
			throw new InternalFailureException(e);
		}
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