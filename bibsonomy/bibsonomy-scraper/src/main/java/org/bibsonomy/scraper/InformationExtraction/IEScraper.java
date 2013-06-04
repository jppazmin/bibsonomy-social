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

package org.bibsonomy.scraper.InformationExtraction;


import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.ie.BibExtraction;


/**
 * Extracts data from selected text using the information extraction tool MALLET.
 * 
 * @author rja
 *
 */
public class IEScraper implements Scraper {

	private static final Pattern yearPattern = Pattern.compile("\\d{4}");


	/**
	 * Extract a valid Bibtex entry from a given publication snippet by using information extraction.
	 */
	public boolean scrape(ScrapingContext sc) throws ScrapingException {
		//FIXME: ScrapingContext.getSelectedText returns the selected text within the browser in ISO and not UTF-8 format
		//we need to convert this, because the mallet function removes erroneous signs, that get created
		//when formatting a UTF-8 String in ISO format.
		//A proper fix would be to make the getSelectedText function return UTF-8 only.
		String selectedText = convertISO2UTF8(sc.getSelectedText());
		
		/*
		 * don't scrape, if there is nothing selected
		 */
		if (selectedText == null || selectedText.trim().equals("")) return false;

		try {
			final HashMap<String, String> map = new BibExtraction().extraction(selectedText);

			if (map != null) {

				/*
				 * build Bibtex String from map
				 */
				final StringBuffer bibtex = getBibtex(map);
				/*
				 * add url to bibtex entry
				 */
				if (sc.getUrl() != null) {
					BibTexUtils.addField(bibtex, "url", sc.getUrl().toString());
				}
				/*
				 * set result
				 */
				sc.setBibtexResult(bibtex.toString());

				/*
				 * save the text the user selected (and the scraper used) into map 
				 */
				map.put("ie_selectedText", selectedText);

				/*
				 * save map data as XML in scraping context 
				 */
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				XMLEncoder encoder = new XMLEncoder(bout);
				encoder.writeObject(map);
				encoder.close();
				sc.setMetaResult(bout.toString("UTF-8"));

				/*
				 * returns itself to know, which scraper scraped this
				 */
				sc.setScraper(this);

				return true;
			}

		} catch (IOException e) {
			throw new ScrapingException(e);
		} catch (ClassNotFoundException e) {
			throw new ScrapingException(e);
		} catch (NamingException e) {
			throw new ScrapingException(e);
		}
		return false;
	}

	/** Builds a bibtex string from a given hashmap
	 * @param map
	 * @return
	 */
	private StringBuffer getBibtex(final HashMap<String, String> map) {
		/*
		 * extract year (needed already here for bibtex key)
		 */
		map.put("year", getYearFromDate(map.get("date")));
		/*
		 * generate bibtex key
		 */
		final String bibtexKey = BibTexUtils.generateBibtexKey(map.get("author"), map.get("editor"), map.get("year"), map.get("title"));
		/*
		 * start with a stringbuffer which contains start of bibtex entry
		 */
		final StringBuffer bib = new StringBuffer("@misc{" + bibtexKey + ",\n");
		/*
		 * iterate over fields of hashmap
		 */
		for (final String key:map.keySet()) {
			/*
			 * extract value
			 */
			String value = map.get(key);
			if (value != null) {
				/*
				 *  replace curly brackets
				 */
				value = value.replace('{','(').replace('}',')');
				/*
				 * clean person lists
				 */
				if ("author".equals(key) || "editor".equals(key)) {
					value = cleanPerson(value);
				}
				bib.append(key + " = {" + value + "},\n");
			}
		}

		/*
		 * replace last "," with a closing curly bracket "}"
		 */
		final int pos = bib.lastIndexOf(",");
		bib.replace(pos, pos + 1, "\n}");

		return bib;
	}

	/**
	 * Extracts the year from the date string.
	 * 
	 * @param date
	 * @return
	 */
	private String getYearFromDate(final String date) {
		if (date != null) {
			/*
			 * look for YYYY, extract and append it
			 */
			final Matcher m = yearPattern.matcher(date);
			if (m.find()) {
				return m.group();
			}
		}
		return null;
	}

	/** Returns a self description of this scraper.
	 * 
	 */
	public String getInfo() {
		return "IEScraper: Extraction of bibliographic references by information extraction. Author: Thomas Steuber";
	}

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}

	/** Cleans a String containing person names.
	 * @param person
	 * @return
	 */
	private String cleanPerson(String person) {
		// not modify references with " and " 
		if (person.contains(" and "))
			return person;
		// in references with ";" and no " and " replace ";" with " and "
		if (person.contains(";"))
			return person.replace(";", " and ");
		// in references with "," and no " and " or ";" replace "," with " and "
		if (person.contains(","))
			return person.replace(",", " and ");

		return person;
	}

	public boolean supportsScrapingContext(ScrapingContext sc) {
		if(sc.getSelectedText()!=null)
			return true; // supports every snippet
		return false;
	}

	public static ScrapingContext getTestContext(){
		ScrapingContext context = new ScrapingContext(null);
		context.setSelectedText("Michael May and Bettina Berendt and Antoine Cornuejols and Joao Gama and Fosca Giannotti and Andreas Hotho and Donato Malerba and Ernestina Menesalvas and Katharina Morik and Rasmus Pedersen and Lorenza Saitta and Yucel Saygin and Assaf Schuster and Koen Vanhoof. Research Challenges in Ubiquitous Knowledge Discovery. Next Generation of Data Mining (Chapman & Hall/Crc Data Mining and Knowledge Discovery Series), Chapman & Hall/CRC,2008.");
		return context;
	}
	
	/**
	 * @return site name
	 */
	public String getSupportedSiteName(){
		return null;
	}
	
	/**
	 * @return site url
	 */
	public String getSupportedSiteURL(){
		return null;
	}
	
	/** 
	 * Converts a string from ISO-8859-1 to UTF-8 format.
	 * @param toConvert A String in ISO format.
	 * @return the argument in UTF-8 format
	 */
	private String convertISO2UTF8(String toConvert) {
		String result = null;
		if( toConvert==null ) return null;
		try {
			byte[] utf8 = toConvert.getBytes("ISO-8859-1"); 
			result = new String(utf8, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			/*nothing clever, i could do here*/
		} 
		
		return result;
	}

}
