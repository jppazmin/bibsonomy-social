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

package org.bibsonomy.scraper.snippet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;

import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.ScrapingException;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;

/**
 * scrapes BibTex from the selected text
 * @version $Id: SnippetScraper.java,v 1.10 2011-04-29 07:24:28 bibsonomy Exp $
 */
public class SnippetScraper implements Scraper {
    private static final String info = "SnippetScraper: This scraper checks passed snippets for " +
    								   "valid BibTeX entries. Author: KDE";
    
	public boolean scrape(ScrapingContext sc) throws ScrapingException{
		String selectedText = sc.getSelectedText();
		/*
		 * don't scrape, if there is nothing selected
		 */
		if (selectedText == null || selectedText.trim().equals("")) return false;

		try{
			/* **************************************************
			 * snippet parsing starts here
			 * **************************************************/

			BibtexParser parser = new BibtexParser(true);
			BibtexFile bibtexFile = new BibtexFile();
			BufferedReader sr = new BufferedReader(new StringReader(selectedText));
			// parse file, exceptions are catched below
			parser.parse(bibtexFile, sr);

			for (Object potentialEntry:bibtexFile.getEntries()) {
				if ((potentialEntry instanceof BibtexEntry)) {
					sc.setBibtexResult(selectedText);
					sc.setScraper(this);
					return true; 
				}
			}

		} catch (ParseException pe) {
			throw new ScrapingException(pe);
		} catch (IOException ioe) {
			throw new ScrapingException(ioe);			
		}
		return false;
	}

	public String getInfo() {
		return info;
	}

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper>singletonList(this);
	}

	public boolean supportsScrapingContext(ScrapingContext sc) {
		String selectedText = sc.getSelectedText();
		/*
		 * don't scrape, if there is nothing selected
		 */
		if (selectedText == null || selectedText.trim().equals("")) return false;

		try{
			/* **************************************************
			 * snippet parsing starts here
			 * **************************************************/

			BibtexParser parser = new BibtexParser(true);
			BibtexFile bibtexFile = new BibtexFile();
			BufferedReader sr = new BufferedReader(new StringReader(selectedText));
			// parse file, exceptions are catched below
			parser.parse(bibtexFile, sr);

			for (Object potentialEntry:bibtexFile.getEntries()) {
				if ((potentialEntry instanceof BibtexEntry)) {
					return true; 
				}
			}

		} catch(ParseException pe) {
			return false;
		} catch (IOException ioe) {
			return false;
		}
		return false;
	}
	
	public static ScrapingContext getTestContext(){
		ScrapingContext context = new ScrapingContext(null);
		context.setSelectedText(" @techreport{triple/Store/Report,\n" +
				"title = {Scalability report on triple store applications},\n" +
				"author = {Ryan Lee},\n" +
				"institution = {Massachusetts Institute of Technology},\n" +
				"url = {http://simile.mit.edu/reports/stores/index.html},\n" +
				"year = {2004},\n" +
				"abstract = {This report examines a set of open source triple store systems suitable for The SIMILE Project's browser-like applications. Measurements on performance within a common hardware, software, and dataset environment grant insight on which systems hold the most promise for acting as large, remote backing stores for SIMILE's future requirements. The SIMILE Project (Semantic Interoperability of Metadata In like and Unlike Environments) is a joint research project between the World Wide Web Consortium (W3C), Hewlett-Packard Labs (HP), the Massachusetts Institute of Technology / Computer Science and Artificial Intelligence Laboratory (MIT / CSAIL), and MIT Libraries. Funding is provided by HP.},\n" +
				"keywords = {MIT applications kde performance performance-project rdf report ss07 store triple uni }\n" +
				"}");
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

}
