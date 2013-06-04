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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
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

/** Scraper for journals from IEEE Explore.
 * @author rja
 *
 */
public class IEEEXploreJournalProceedingsScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "IEEEXplore Journals";
	private static final String SITE_URL = "http://ieeexplore.ieee.org/";
	private static final String info = "This scraper creates a BibTeX entry for the journals and proceedings at " +
	href(SITE_URL, SITE_NAME)+".";
	
	private static final Log log = LogFactory.getLog(IEEEXploreJournalProceedingsScraper.class);
	private static final String IEEE_HOST		   = "ieeexplore.ieee.org";
	private static final String IEEE_HOST_NAME     = SITE_URL;
	private static final String IEEE_PATH 	  	   = "xpl";
	private static final String IEEE_JOURNAL	   = "@article";
	private static final String IEEE_PROCEEDINGS   = "@proceedings";
	private static final String IEEE_INPROCEEDINGS = "@inproceedings";

	private static final String CONST_DATE       = "Publication Date: ";
	private static final String CONST_VOLUME     = "Volume: ";
	private static final String CONST_PAGES      = "On page(s): ";
	private static final String CONST_BOOKTITLE	 = "This paper appears in: ";

	private static final Pattern pattern = Pattern.compile("arnumber=([^&]*)");
	private static final Pattern pattern2 = Pattern.compile("chklist=([^%]*)");

	
	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + IEEE_HOST), Pattern.compile("/" + IEEE_PATH + ".*")));
	
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		//FIXME: this should into the path pattern!
		if (sc.getUrl().toString().indexOf("punumber") == -1 ) {
			sc.setScraper(this);

			String id = null;
			Matcher matcher = pattern.matcher(sc.getUrl().toString());
			if(matcher.find())
				id = matcher.group(1);

			matcher = pattern2.matcher(sc.getUrl().toString());
			if(id == null && matcher.find())
				id = matcher.group(1);

			if(id != null){
				String downUrl = "http://ieeexplore.ieee.org/xpl/downloadCitations?citations-format=citation-abstract&download-format=download-bibtex&fromPageName=abstract&recordIds=" + id;
				String bibtex = null;
				try {
					bibtex = WebUtils.getContentAsString(new URL(downUrl));
				} catch (MalformedURLException ex) {
					throw new InternalFailureException(ex);
				} catch (IOException ex) {
					throw new InternalFailureException(ex);
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
					log.debug("IEEEXploreJournalProceedingsScraper: direct bibtex download failed. Use JTidy to get bibliographic data.");
					sc.setBibtexResult(ieeeJournalProceedingsScrape(sc));
					return true;

				}
			}else{
				log.debug("IEEEXploreJournalProceedingsScraper use JTidy to get Bibtex from " + sc.getUrl().toString());
				sc.setBibtexResult(ieeeJournalProceedingsScrape(sc));
				return true;
			}
		}
		return false;
	}

	public String getInfo() {
		return info;
	}
	public String ieeeJournalProceedingsScrape (ScrapingContext sc) throws ScrapingException {

		try{
			//-- init all NodeLists and Node
			NodeList pres 		= null; 
			Node currNode 		= null;
			NodeList temp 		= null;

			//-- init Strings for bibtex entries
			// month uncased because of multiple date types
			String type 		= "";
			String url 			= sc.getUrl().toString();
			String author 		= "";
			String year 		= "";
			String abstr		= "";
			String title		= "";
			String booktitle	= "";
			String volume = null;
			String pages  = null;
			String issn   = null;
			String isbn   = null;
			String doi    = null;

			String authors[] 	= null; 
			String tempAuthors 	= null;


			//-- get the html doc and parse the DOM
			final Document document = XmlUtils.getDOM(sc.getPageContent());

			//get the abstract block
			String ident1 = "<span class=\"sectionHeaders\">Abstract</span><br>";
			String ident2 = "<td class=\"bodyCopyGrey\"><p class=\"bodyCopyGreySpaced\"><strong>Index";
			if (sc.getPageContent().indexOf(ident1) != -1 && sc.getPageContent().indexOf(ident2) != -1 ){
				abstr = sc.getPageContent().substring(sc.getPageContent().indexOf(ident1)+ident1.length(),sc.getPageContent().indexOf(ident2)).replaceAll("\\s\\s+", "").replaceAll("(<.+?>)", "").trim();			
			}

			/*-- Get the title of the journal --
			 * Iterate through all spans
			 */
			pres = null;
			pres = document.getElementsByTagName("span"); //get all <span>-Tags
			for (int i = 0; i < pres.getLength(); i++) {
				Node curr = pres.item(i);
				Element g = (Element)curr;
				Attr own = g.getAttributeNode("class");

				if ("headNavBlueXLarge2".equals(own.getValue())) {
					title = curr.getFirstChild().getNodeValue();
					temp = pres.item(i+1).getChildNodes();

					if (!"".equals(temp.item(0).getNodeValue())) {
						tempAuthors = temp.item(0).getNodeValue();

						if ("\u00A0\u00A0".equals(tempAuthors))	{
							authors = new String[] {"N/A"};
						} else {
							authors = tempAuthors.split("\u00A0\u00A0");
						}
					}
					break;
				}
			}

			/*-- Get the global infomation like publication date, number of pages ... --
			 * iterate through all p's stop at "This paper appears in:" because its
			 * available in all journals.
			 * Save Nodelist and break the loops.
			 * */
			pres = null;
			NodeList match = null;
			pres = document.getElementsByTagName("p"); //get all <p>-Tags
			for (int i=0; i<pres.getLength(); i++){
				currNode = pres.item(i);
				temp = currNode.getChildNodes();
				//iterate through childs to find "Publication Date:"
				for (int j=0; j<temp.getLength(); j++){
					if (temp.item(j).getNodeValue().indexOf(CONST_BOOKTITLE) != -1){
						if (!"".equals(temp.item(1).getFirstChild().getFirstChild().getNodeValue())){
							booktitle = temp.item(1).getFirstChild().getFirstChild().getNodeValue();
						}
						match=temp;
						break;
					}
				}
			}
			//get the different childs of the founded p-tag
			for (int i=0; i<match.getLength(); i++){
				if (!"".equals(match.item(i).getNodeValue())){
					String infoMatches = null;
					if (match.item(i).getNodeValue().indexOf(CONST_DATE) != -1){
						//extract the year
						infoMatches = match.item(i).getNodeValue().substring(CONST_DATE.length());
						StringTokenizer tokenizer = new StringTokenizer(infoMatches);
						String yearPattern = "\\d{4}";
						Pattern yearP = Pattern.compile(yearPattern);

						while ( tokenizer.hasMoreTokens() ){
							String token = tokenizer.nextToken();
							Matcher matcherYear = yearP.matcher(token);
							if (matcherYear.matches()){
								year = token;
							}
						}
					}
					if (volume == null && match.item(i).getNodeValue().indexOf(CONST_VOLUME) != -1){
						infoMatches = match.item(i).getNodeValue();
						volume = infoMatches.substring(infoMatches.indexOf(CONST_VOLUME) + CONST_VOLUME.length(),infoMatches.indexOf(",")).trim();
					}
					if (pages == null && match.item(i).getNodeValue().indexOf(CONST_PAGES) != -1){
						infoMatches = match.item(i).getNodeValue();
						pages = infoMatches.substring(infoMatches.indexOf(CONST_PAGES) + CONST_PAGES.length()).trim();
					}
					if (issn == null) issn = getField(match, i, "ISSN: ");
					if (isbn == null) isbn = getField(match, i, "ISBN: "); 
					if (doi  == null) doi  = getField(match, i, "Digital Object Identifier: ");
				}
			}

			//-- set bibtex type @article for journals & @proceeding for proceedings
			if ((isbn == null || isbn.trim().equals("")) && issn != null && !issn.trim().equals("")) {
				type = IEEE_JOURNAL;
			} else {
				if (title.equals(booktitle)){
					type = IEEE_PROCEEDINGS;
				} else {
					type = IEEE_INPROCEEDINGS;
				}
			}


			//-- get all authors out of the arraylist and prepare them to bibtex entry "author"
			for (int i=0; i<authors.length; i++){
				if (i==authors.length-1){
					author += authors[i].trim();
				} else {
					author += authors[i].trim() + " and ";
				}
			}


			//-- kill spaces and add the year to bibtexkey
			//- replace all special chars to avaoid crashes through bibtexkey
			StringBuffer b = new StringBuffer (type + "{" + getName(authors[0]) + ":" + year + ",");
			appendBibtexField(b, "author", author);
			appendBibtexField(b, "abstract", abstr);

			appendBibtexField(b, "title", title);
			appendBibtexField(b, "booktitle", booktitle);
			appendBibtexField(b, "url", url);
			appendBibtexField(b, "year", year);
			appendBibtexField(b, "isbn", isbn);
			appendBibtexField(b, "issn", issn);
			appendBibtexField(b, "doi", doi);
			appendBibtexField(b, "volume", volume);
			appendBibtexField(b, "pages", pages);
			b.append("}");

			return b.toString();

		}catch(Exception e){
			throw new InternalFailureException(e);
		}
	}

	private String getName(String author) {
		if (author != null) {
			final int indexOfComma = author.indexOf(",");
			if (indexOfComma != -1) {
				return author.substring(0, indexOfComma).replaceAll("[^a-zA-Z]", "");
			} else {
				return author.replaceAll("[^a-zA-Z]", "");
			}
		}
		return null;
	}

	private String getField(NodeList match, int i, final String field) {
		final String nodeValue = match.item(i).getNodeValue();
		if (nodeValue.indexOf(field) != -1){
			return nodeValue.substring(field.length()).trim();
		}
		return null;
	}

	private void appendBibtexField (StringBuffer b, String field, String value) {
		if (value != null) {
			b.append(field + " = {" + value + "},");
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