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

package org.bibsonomy.scraper.url.kde.elsevierhealth;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.scraper.id.kde.isbn.ISBNScraper;
import org.bibsonomy.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** 
 * Scrapes the Elsevier Health publication page
 * @author ccl
 */
public class ElsevierhealthScraper extends AbstractUrlScraper {
	
	private static final String SITE_NAME	= "Elsevier: Medical publishers, online journals";
	private static final String SITE_URL	= "http://www.us.elsevierhealth.com";
	private static final String HOST		= "us.elsevierhealth.com";
	private static final String WC			= ".*";
	private static final String EXPORT_PAGE	= "product.jsp";
	private static final String INFO		= "This scraper parses a publication page from the " + href(SITE_URL, SITE_NAME);
	
	private static final String ISBN_PATTERN	  = "isbn=(\\d+)";
	private static final String YEAR_PATTERN	  = "Copyright (\\d+)";
	private static final String PUBLISHER_PATTERN = "<span class='small'>(.+)\\s+Title";
	
	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	
    static {
    	patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(WC + HOST), Pattern.compile(EXPORT_PAGE + WC)));
    }
    

    protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);
		
		BibTex bibtex = null;
		Pattern _p	  = null;
		Matcher _m	  = null;
		String isbn	  = null;
		
		/*
		 * get the isbn
		 */
		_p = Pattern.compile(ISBN_PATTERN);
		_m = _p.matcher(sc.getUrl().toString());
		if (_m.find()) {
			isbn = _m.group(1);
		}
		
		/*
		 * Normally, we'll get more informations via our isbn scraper.
		 * So we'll try this one first
		 */
		ISBNScraper scraper = new ISBNScraper();
		sc.setSelectedText(isbn);
		scraper.scrape(sc);
		
		if (sc.getBibtexResult() != null) {
			return true;
		}

		try {
			// Parse the page and obtain a DOM
			final String content = sc.getPageContent();
			final Document document = XmlUtils.getDOM(content);

			NodeList _nl = null;
			Node _n		 = null;
			String _tmp	 = "";
			
			/*
			 * extracts the title
			 */
			_nl = document.getElementsByTagName("h1");
			
			for (int i = 0; i < _nl.getLength(); i++) {
				_n = _nl.item(i);
				if (_n.getAttributes().getNamedItem("class") != null
						&& _n.getAttributes().getNamedItem("class").getNodeValue().equals("H1Title")) {
					bibtex = new BibTex();
					bibtex.setTitle(XmlUtils.getText(_n));
					break;
				}
			}
			
			/*
			 * stop scraping if publication has no title
			 */
			if (bibtex == null) {
				throw new ScrapingFailureException("getting bibtex failed");
			}
			
			_p = Pattern.compile(YEAR_PATTERN);
			_m = _p.matcher(XmlUtils.getText(_n.getParentNode().getParentNode().getNextSibling()));

			if (_m.find()) {
				bibtex.setYear(_m.group(1));
			}
			
			/*
			 * Normally, all entries are books. However, the entry type isn't
			 * specified on elvevierhealth sites.
			 */
			bibtex.setEntrytype("book");
			
			/*
			 * add isbn to bibtex
			 */
			if (isbn != null) {
				bibtex.addMiscField("isbn", isbn);
			}
			
			/*
			 * extracts abstract and author fields
			 */
			_nl = document.getElementsByTagName("a");
			
			for (int i = 0; i < _nl.getLength(); i++) {
				_n = _nl.item(i);
				if (_n.getAttributes().getNamedItem("name") != null
						&& _n.getAttributes().getNamedItem("name").getNodeValue().equals("authorinfo")) {
					_n = _n.getNextSibling().getNextSibling();
					
					for (int j = 0; j < _n.getChildNodes().getLength(); j++) {
						if (_n.getChildNodes().item(j).hasChildNodes()) {
							if (_tmp.equals("")) {
								_tmp = _n.getChildNodes().item(j).getFirstChild().getNodeValue();
							} else {
								_tmp += " and " + _n.getChildNodes().item(j).getFirstChild().getNodeValue();
							}
						}
					}
					
					bibtex.setAuthor(_tmp);
				}
				if (_n.getAttributes().getNamedItem("name") != null
						&& _n.getAttributes().getNamedItem("name").getNodeValue().equals("description")) {
					_n = _n.getNextSibling().getNextSibling();
					bibtex.setAbstract(XmlUtils.getText(_n));
				}
			}
			
			/*
			 * generates the bibtex key
			 */
			bibtex.setBibtexKey(BibTexUtils.generateBibtexKey(bibtex));
			
			/*
			 * add url
			 */
			bibtex.addMiscField("url", sc.getUrl().toString());
			
			/*
			 * add publisher
			 */
			_p = Pattern.compile(PUBLISHER_PATTERN);
			_m = _p.matcher(content);
			if (_m.find()) {
				bibtex.setPublisher(_m.group(1));
			}
			
			sc.setBibtexResult(BibTexUtils.toBibtexString(bibtex));
			
			return true;
		} catch (Exception e) {
			throw new InternalFailureException(e);
		}
	}

	
	public String getInfo() {
		return INFO;
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
