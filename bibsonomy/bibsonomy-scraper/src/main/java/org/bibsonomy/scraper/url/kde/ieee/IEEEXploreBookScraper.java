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
import java.util.LinkedList;
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
public class IEEEXploreBookScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "IEEEXplore Books";
	private static final String SITE_URL = "http://ieeexplore.ieee.org/books/bkbrowse.jsp";
	private static final Log log = LogFactory.getLog(IEEEXploreBookScraper.class);
	private static final String info = "This scraper creates a BibTeX entry for the books at " +
	href(SITE_URL, SITE_NAME);

	private static final String IEEE_HOST        = "ieeexplore.ieee.org";
	private static final String IEEE_BOOK_PATH   = "xpl";
	private static final String IEEE_SEARCH_PATH = "search";
	private static final String IEEE_BOOK	     = "@book";

	private static final String CONST_ISBN     = "ISBN: ";
	private static final String CONST_PAGES    = "Page(s): ";
	private static final String CONST_ON_PAGES = "On page(s): ";
	private static final String CONST_EDITION  = "Edition: ";
	private static final String CONST_VOLUME   = "Volume: ";
	private static final String CONST_DATE	   = "Publication Date: ";
	
	private static final String EXPORT_ARNUM_URL = "http://ieeexplore.ieee.org/xpl/downloadCitations";

	private static final Pattern URL_PATTERN_BKN      = Pattern.compile("bkn=([^&]*)");
	private static final Pattern URL_PATTERN_ARNUMBER = Pattern.compile("arnumber=([^&]*)");

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + IEEE_HOST), Pattern.compile(IEEE_BOOK_PATH + ".*")));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + IEEE_HOST), Pattern.compile(IEEE_SEARCH_PATH + ".*")));
	}
	
	public boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);
		
		String bibtex = null;
		String postContent = null;
		
		Matcher matcher = URL_PATTERN_BKN.matcher(sc.getUrl().toString());
		
		
		if(matcher.find()){
			postContent = "citations-format=citation-abstract&download-format=download-bibtex&fromPageName=bookAbstract&recordIds=" + matcher.group(1);
		}
		
		matcher = URL_PATTERN_ARNUMBER.matcher(sc.getUrl().toString());
		
		if(matcher.find()){
			postContent = "citations-format=citation-abstract&download-format=download-bibtex&fromPageName=abstract&recordIds=" + matcher.group(1);
		}
		
		if (postContent != null) {
			try {
				bibtex = WebUtils.getPostContentAsString(new URL(EXPORT_ARNUM_URL), postContent);
			} catch (IOException ex) {
				throw new InternalFailureException(ex);
			}
		}
		
		if(bibtex != null){
			// clean up
			bibtex = bibtex.replace("<br>", "");

			// append url
			bibtex = BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
			
			// add downloaded bibtex to result 
			sc.setBibtexResult(bibtex);
			return true;

		}else{
			log.debug("IEEEXploreBookScraper use JTidy to get Bibtex from " + sc.getUrl().toString());
			sc.setBibtexResult(ieeeBookScrape(sc));
			return true;

		}
	}

	/**
	 * @param sc
	 * @return bibtex
	 * @throws ScrapingException
	 */
	public String ieeeBookScrape (ScrapingContext sc) throws ScrapingException {
		try{
			//-- init all NodeLists and Node
			NodeList pres 		= null; 
			Node currNode 		= null;
			NodeList temp 		= null;

			//-- init String map for bibtex entries
			String type 		= IEEE_BOOK;
			String url 			= sc.getUrl().toString();
			String authors 		= "";
			String numpages 	= "";
			String title 		= "";
			String isbn 		= "";
			String publisher 	= "";
			String month 		= "";
			String year 		= "";
			String edition 		= "";
			String abstr 		= "";

			String bibtexkey	= null;
			String _tempabs		= null;
			String ident1		= null;
			String ident2		= null;

			//-- get the html doc and parse the DOM
			final Document doc = XmlUtils.getDOM(sc.getPageContent());

			/*
			 * -- Search title and extract --
			 * The title has always the css-class "headNavBlueXLarge".
			 *
			 * FIXME: this part could be deprecated. don't knot it at all...
			 *
			pres = null;
			pres = doc.getElementsByTagName("span"); //get all <span>-Tags
			for (int i = 0; i < pres.getLength(); i++) {
				Node curr = pres.item(i);
				Element g = (Element)curr;
				Attr own = g.getAttributeNode("class");			

				//-- Extract the title
				if ("headNavBlueXLarge".equals(own.getValue())){
					title = curr.getFirstChild().getNodeValue();
				}
			} */
			
			if (title == null || title.equals("")) {
				ident1 = "<title>";
				ident2 = "</title>";
				if (sc.getPageContent().contains(ident1) && sc.getPageContent().contains(ident2)) {
					int _startIndex = sc.getPageContent().indexOf(ident1) + ident1.length();
					int _endIndex   = sc.getPageContent().indexOf(ident2);
					title = sc.getPageContent().substring(_startIndex, _endIndex);
					title = title.replaceAll("IEEEXplore#\\s", "");
				}
			}
			
			/* 
			 * get the abstract block
			 * 
			 * FIXME: this part could be deprecated. don't knot it at all...
			 * 
			ident1 = "<strong>Abstract</strong>";
			ident2 = "<strong>Table of Contents </strong>";
			if (sc.getPageContent().indexOf(ident1) != -1 && sc.getPageContent().indexOf(ident2) != -1 ){
				_tempabs = sc.getPageContent().substring(sc.getPageContent().indexOf(ident1)+ident1.length(),sc.getPageContent().indexOf(ident2)).replaceAll("\\s\\s+", "").replaceAll("(<.+?>)", "").trim();
				abstr = _tempabs;			
			} */
			
			ident1 = "<span class=\"sectionHeaders\">Abstract</span>";
			ident2 = "<td class=\"bodyCopyGrey\"><p class=\"bodyCopyGreySpaced\"><strong>";
			if (sc.getPageContent().contains(ident1) && sc.getPageContent().contains(ident2)) {
				int _startIndex = sc.getPageContent().indexOf(ident1) + ident1.length();
				int _endIndex   = sc.getPageContent().indexOf(ident2);
				_tempabs = sc.getPageContent().substring(_startIndex, _endIndex);
				abstr = _tempabs.replaceAll("\\s\\s+", "").replaceAll("(<.+?>)", "").trim();
			}
			
			/* 
			 * get the book formats like hardcover
			 * 
			 * FIXME: this part could be deprecated. don't knot it at all...
			 * 
			 *
			ident1 = "<td class=\"bodyCopyBlackLarge\" nowrap>Hardcover</td>";
			ident2 = "<td class=\"bodyCopyBlackLarge\" nowrap><span class=\"sectionHeaders\">&raquo;</span>";
			if (sc.getPageContent().indexOf(ident1) != -1){
				_format = sc.getPageContent().substring(sc.getPageContent().indexOf(ident1),sc.getPageContent().indexOf(ident2)).replaceAll("\\s\\s+", "").replaceAll("(<.+?>)", "");

				_format = _format.substring(_format.indexOf(CONST_ISBN) + CONST_ISBN.length());
				isbn = _format.substring(0,_format.indexOf("&nbsp;"));
			}*/

			/*-- get all <p>-Tags to extract the standard informations
			 *  In every standard page the css-class "bodyCopyBlackLargeSpaced"
			 *  indicates the collection of all informations.
			 * */
			pres = null;
			pres = doc.getElementsByTagName("p"); //get all <p>-Tags
			for (int i=0; i<pres.getLength(); i++){
				currNode = pres.item(i);

				if (currNode.hasAttributes()) {
					Element g = (Element)currNode;
					Attr own = g.getAttributeNode("class");
					if ("bodyCopyBlackLargeSpaced".equals(own.getValue()) && currNode.hasChildNodes()){
						temp = currNode.getChildNodes();

						for(int j =0; j<temp.getLength(); j++){
							if (temp.item(j).getNodeValue().indexOf(CONST_DATE) != -1){
								String date = temp.item(j).getNodeValue().substring(18);
								year = date.substring(date.length()-5).trim();
								month = date.substring(0,date.length()-4).trim();
								// not correct in all cases
								// publisher = temp.item(j+2).getNodeValue().trim();
							}
							if (temp.item(j).getNodeValue().indexOf(CONST_PAGES) != -1){
								numpages = temp.item(j).getNodeValue().substring(CONST_PAGES.length()).trim();
							} else if (temp.item(j).getNodeValue().indexOf(CONST_ON_PAGES) != -1) {
								numpages = temp.item(j).getNodeValue().substring(CONST_ON_PAGES.length()).trim();
							}
							if (temp.item(j).getNodeValue().indexOf(CONST_EDITION) != -1){
								edition = temp.item(j).getNodeValue().substring(CONST_EDITION.length()).trim();
							} else if (temp.item(j).getNodeValue().indexOf(CONST_VOLUME) != -1) {
								edition = temp.item(j).getNodeValue().substring(CONST_VOLUME.length()).trim();
							}
							if (isbn == "" && temp.item(j).getNodeValue().indexOf(CONST_ISBN) != -1){
								isbn= temp.item(j).getNodeValue().substring(CONST_ISBN.length()).trim();
							}
						}
						break;
					}
				}
			}

			/*-- Search authors and save them --
			 * 
			 * FIXME: this part could be deprecated. don't knot it at all...
			 * 
			pres = null;
			pres = doc.getElementsByTagName("a"); //get all <a>-Tags

			//init vars to count authors to form a bibtex String
			int numaut = 0;
			
			 *
			 * iterate through the a tags and search the attribute value "<in>aud)" 
			 * to identify the authors in the source of the ieeexplore page
			 * 
			for (int i = 39; i < pres.getLength(); i++) {
				Node curr = pres.item(i);
				Element g = (Element)curr;
				Attr own = g.getAttributeNode("href");

				if (own.getValue().indexOf("<in>au)") != -1){
					//Form Bibtex String by counting authors
					if (numaut > 0 ){
						authors += " and " + curr.getFirstChild().getNodeValue(); 
					}
					if (numaut == 0) {
						numaut=i;
						authors += curr.getFirstChild().getNodeValue();

						if (curr.getFirstChild().getNodeValue().indexOf(",") != -1 && bibtexkey == null){
							bibtexkey = curr.getFirstChild().getNodeValue().substring(0,curr.getFirstChild().getNodeValue().trim().indexOf(","));
						} else if (curr.getFirstChild().getNodeValue().trim().indexOf(" ") != -1 && bibtexkey == null){
							bibtexkey = curr.getFirstChild().getNodeValue().trim().substring(0,curr.getFirstChild().getNodeValue().trim().indexOf(" "));
						} else if (bibtexkey == null){
							bibtexkey = curr.getFirstChild().getNodeValue().trim();
						}
					}
				}
			} */
			
			/*
			 * get authors
			 */
			if (authors == null || authors.equals("")) {
				ident1 = "<font color=990000><b>";
				ident2 = "<br>";
				int _startIndex = sc.getPageContent().indexOf(ident1) + ident1.length();
				if (sc.getPageContent().contains(ident1) && sc.getPageContent().indexOf(ident2, _startIndex) != -1) {
					int _endIndex = sc.getPageContent().indexOf(ident2, _startIndex);
					authors = sc.getPageContent().substring(_startIndex, _endIndex);
					authors = authors.replaceAll("\\s\\s+", "").replaceAll("(<.+?>)", "").trim();
					authors = authors.replaceAll("&nbsp;&nbsp;", " and ");
					
					if (authors.endsWith(" and ")) {
						authors = authors.substring(0, authors.length() - 5);
					}
				}
			}
			
			//-- kill special chars and add the year to bibtexkey
			if ((isbn == null || !isbn.equals(""))
					&& (year == null || !year.equals(""))) {
				bibtexkey = isbn.replaceAll("-", "");
				bibtexkey = bibtexkey.replaceAll("[^0-9A-Za-z]", "") + ":" + year;
			}
				
			//create the book-bibtex
			return type + " { " + bibtexkey + ", \n" 
			+ "author = {" + authors + "}, \n" 
			+ "title = {" + title + "}, \n" 
			+ "year = {" + year + "}, \n" 
			+ "url = {" + url + "}, \n"
			+ "pages = {" + numpages + "}, \n"
			+ "edition = {" + edition + "}, \n" 
			+ "publisher = {" + publisher + "}, \n"
			+ "isbn = {" + isbn + "}, \n" 
			+ "abstract = {" + abstr + "}, \n"
			+ "month = {" + month + "}\n}";

		}catch(Exception e){
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