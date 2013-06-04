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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

/**
 * Scraper for Pages with a span element which matches to the COinS specification.
 *  
 * @author tst
 * @version $Id: CoinsScraper.java,v 1.9 2011-04-29 07:24:27 bibsonomy Exp $
 */
public class CoinsScraper implements Scraper {

	private static final String SITE_NAME = "CoinsScraper";
	private static final String SITE_URL = "http://ocoins.info/";
	private static final String INFO = "<a href=\"http://ocoins.info/\">COinS</a> Scraper: Scraper for Metadata in COinS format.";

	private static final Pattern patternCoins = Pattern.compile("<span class=\"Z3988\" title=\"([^\\\"]*)\"");
	private static final Pattern patternKeyValue = Pattern.compile("([^=]*)=([^&]*)&amp;?");
	private static final Pattern datePattern = Pattern.compile("(\\d{4})");


	public String getInfo() {
		return INFO;
	}

	public boolean scrape(ScrapingContext sc)throws ScrapingException {
		if (sc == null || sc.getUrl() == null) return false;
		
		final String page = sc.getPageContent();
		StringBuffer bibtex = null;

		final Matcher matcherCoins = patternCoins.matcher(page);
		if(matcherCoins.find()){
			// span found, this scraper is responsible
			sc.setScraper(this);

			String titleValue = matcherCoins.group(1);

			// store all key/value tuples
			HashMap<String, String> tuples = new HashMap<String, String>();

			final Matcher matcherKeyValue = patternKeyValue.matcher(titleValue);

			// search tuples and store it in map
			while(matcherKeyValue.find()){
				String key;
				String value;
				try {
					key = URLDecoder.decode(matcherKeyValue.group(1), "UTF-8");
					value = URLDecoder.decode(matcherKeyValue.group(2), "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					throw new InternalFailureException(ex);
				}

				// store only values which are not null and not empty
				if(key != null && value != null && !key.equals("") && !value.equals(""))
					tuples.put(key, value);
			}

			/*
			 * first get values which are needed for books and journals
			 */

			// get author
			String author = null;
			if(tuples.containsKey("rft.au"))
				author = tuples.get("rft.au");

			// get title
			String atitle = null;
			if(tuples.containsKey("rft.atitle"))
				atitle = tuples.get("rft.atitle");

			// get year
			String year = null;
			if(tuples.containsKey("rft.date")){
				String date = tuples.get("rft.date");
				// get year from date
				final Matcher dateMatcher = datePattern.matcher(date);
				if(dateMatcher.find())
					year = dateMatcher.group(1);
			}

			// get pages
			String pages = null;
			if(tuples.containsKey("rft.pages"))
				pages = tuples.get("rft.pages");
			else if(tuples.containsKey("rft.spage") && tuples.containsKey("rft.epage")){
				// build pages with spage and epage
				String spage = tuples.get("rft.spage");
				String epage = tuples.get("rft.epage");
				pages = spage + "-" + epage;
			}

			// get issn
			String issn = null;
			if(tuples.containsKey("rft.issn"))
				issn = tuples.get("rft.issn");

			// get isbn
			String isbn = null;
			if(tuples.containsKey("rft.isbn"))
				isbn = tuples.get("rft.isbn");


			/*
			 * book and journal specific behaviour
			 */

			// check for journal
			if(tuples.containsKey("rft_val_fmt") && tuples.get("rft_val_fmt").contains(":journal")){

				// get title
				String title = null;
				if(tuples.containsKey("rft.title"))
					title = tuples.get("rft.title");

				// get volume
				String volume = null;
				if(tuples.containsKey("rft.volume"))
					volume = tuples.get("rft.volume");

				// get issue 
				String issue = null;
				if(tuples.containsKey("rft.issue"))
					issue = tuples.get("rft.issue");

				// get eissn
				String eissn = null;
				if(tuples.containsKey("rft.eissn"))
					eissn = tuples.get("rft.eissn");

				// get coden
				String coden = null;
				if(tuples.containsKey("rft.coden"))
					coden = tuples.get("rft.coden");

				// get sici
				String sici = null;
				if(tuples.containsKey("rft.sici"))
					sici = tuples.get("rft.sici");

				/*
				 * build bibtex
				 */

				// beginning part
				bibtex = new StringBuffer();
				bibtex.append("@inproceedings{");

				// get and set bibkey
				if(tuples.containsKey("rft.artnum"))
					bibtex.append(tuples.get("rft.artnum"));
				else if(tuples.containsKey("rft.aufirst ") && year != null)
					bibtex.append(tuples.get("rft.artnum"));
				else
					bibtex.append("default");
				bibtex.append(",\n");

				// build title
				if(atitle != null){
					bibtex.append("title = {");
					bibtex.append(atitle);
					bibtex.append("},\n");
				}

				// build author
				if(author != null){
					bibtex.append("author = {");
					bibtex.append(author);
					bibtex.append("},\n");
				}

				// build journal
				if(title != null){
					bibtex.append("journal = {");
					bibtex.append(title);
					bibtex.append("},\n");
				}

				// build year
				if(year != null){
					bibtex.append("year = {");
					bibtex.append(year);
					bibtex.append("},\n");
				}

				// build volume
				if(volume != null){
					bibtex.append("volume = {");
					bibtex.append(volume);
					bibtex.append("},\n");
				}

				// build issue
				if(issue != null){
					bibtex.append("issue = {");
					bibtex.append(issue);
					bibtex.append("},\n");
				}

				// build pages
				if(pages != null){
					bibtex.append("pages = {");
					bibtex.append(pages);
					bibtex.append("},\n");
				}

				// build issn
				if(issn != null){
					bibtex.append("issn = {");
					bibtex.append(issn);
					bibtex.append("},\n");
				}

				// build eissn
				if(eissn != null){
					bibtex.append("eissn = {");
					bibtex.append(eissn);
					bibtex.append("},\n");
				}

				// build isbn
				if(isbn != null){
					bibtex.append("isbn = {");
					bibtex.append(isbn);
					bibtex.append("},\n");
				}

				// build coden
				if(coden != null){
					bibtex.append("coden = {");
					bibtex.append(coden);
					bibtex.append("},\n");
				}

				// build sici
				if(sici != null){
					bibtex.append("sici = {");
					bibtex.append(sici);
					bibtex.append("},\n");
				}

				// remove last ","
				bibtex = new StringBuffer(bibtex.subSequence(0, bibtex.lastIndexOf(",")));

				// finish bibtex
				bibtex.append("\n}\n");

				// check for book
			} else if(tuples.containsKey("rft_val_fmt") && tuples.get("rft_val_fmt").contains(":book")){

				// get btitle (booktitle)
				String btitle = null;
				if(tuples.containsKey("rft.btitle"))
					btitle = tuples.get("rft.btitle");

				// get address
				String address = null;
				if(tuples.containsKey("rft.place"))
					address = tuples.get("rft.place");

				// get publisher
				String publisher = null;
				if(tuples.containsKey("rft.pub"))
					publisher = tuples.get("rft.pub");

				// get edition
				String edition = null;
				if(tuples.containsKey("rft.edition"))
					edition = tuples.get("rft.edition");

				// get series
				String series = null;
				if(tuples.containsKey("rft.series"))
					series = tuples.get("rft.series");

				// get 
				String bici = null;
				if(tuples.containsKey("bici"))
					bici = tuples.get("bici");

				/*
				 * build bibtex
				 */

				// beginning part
				bibtex = new StringBuffer();
				bibtex.append("@book{");

				// get and set bibkey
				if(tuples.containsKey("rft.artnum"))
					bibtex.append(tuples.get("rft.artnum"));
				else if(tuples.containsKey("rft.aufirst ") && year != null)
					bibtex.append(tuples.get("rft.artnum"));
				else
					bibtex.append("default");
				bibtex.append(",\n");

				// build title
				if(atitle != null){
					bibtex.append("title = {");
					bibtex.append(atitle);
					bibtex.append("},\n");
				}

				// build booktitle (if atitle empty then use btitle as title)
				if(btitle != null){
					if(atitle == null){
						bibtex.append("title = {");
						bibtex.append(btitle);
						bibtex.append("},\n");
					}else{
						bibtex.append("booktitle = {");
						bibtex.append(btitle);
						bibtex.append("},\n");
					}
				}

				// build author
				if(author != null){
					bibtex.append("author = {");
					bibtex.append(author);
					bibtex.append("},\n");
				}

				// build isbn
				if(isbn != null){
					bibtex.append("isbn = {");
					bibtex.append(isbn);
					bibtex.append("},\n");
				}

				// build address
				if(address != null){
					bibtex.append("address = {");
					bibtex.append(address);
					bibtex.append("},\n");
				}

				// build publisher
				if(publisher != null){
					bibtex.append("publisher = {");
					bibtex.append(publisher);
					bibtex.append("},\n");
				}

				// build year 
				if(year != null){
					bibtex.append("year = {");
					bibtex.append(year);
					bibtex.append("},\n");
				}

				// build edition
				if(edition != null){
					bibtex.append("edition = {");
					bibtex.append(edition);
					bibtex.append("},\n");
				}

				// build series
				if(series != null){
					bibtex.append("series = {");
					bibtex.append(series);
					bibtex.append("},\n");
				}

				// build pages
				if(pages != null){
					bibtex.append("pages = {");
					bibtex.append(pages);
					bibtex.append("},\n");
				}

				// build issn
				if(issn != null){
					bibtex.append("issn = {");
					bibtex.append(issn);
					bibtex.append("},\n");
				}

				// build bici
				if(bici != null){
					bibtex.append("bici = {");
					bibtex.append(bici);
					bibtex.append("},\n");
				}

				// remove last ","
				bibtex = new StringBuffer(bibtex.subSequence(0, bibtex.lastIndexOf(",")));

				// finish bibtex
				bibtex.append("\n}\n");
			}

			// return bibtex
			if(bibtex != null){
				// append url
				BibTexUtils.addFieldIfNotContained(bibtex, "url", sc.getUrl().toString());
				
				// add downloaded bibtex to result 
				sc.setBibtexResult(bibtex.toString());
				
				return true;
			}else
				throw new ScrapingFailureException("span not contains a book or journal");
		}
		return false;
	}

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singleton(this);
	}

	public boolean supportsScrapingContext(ScrapingContext sc) {
		if(sc.getUrl() != null){
			Matcher matcherCoins;
			try {
				matcherCoins = patternCoins.matcher(sc.getPageContent());
				if(matcherCoins.find())
					return true;
			} catch (ScrapingException ex) {
				return false;
			}
		}
		return false;
	}

	public static ScrapingContext getTestContext(){
		ScrapingContext context = new ScrapingContext(null);
		try {
			context.setUrl(new URL("http://www.westmidlandbirdclub.com/bibliography/NBotWM.htm"));
		} catch (MalformedURLException ex) {
		}
		return context;
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
