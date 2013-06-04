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

package org.bibsonomy.scraper.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;

/**
 * Converter for oai to bibtex
 * @author tst
 * @version $Id: OAIConverter.java,v 1.6 2011-04-29 07:24:34 bibsonomy Exp $
 */
public class OAIConverter {
	
	private static final String PATTERN_TITLE = "<dc:title>([^<]*)<";
	private static final String PATTERN_CREATOR = "<dc:creator>([^<]*)<";
	private static final String PATTERN_DESCRIPTION = "<dc:description>([^<]*)<";
	private static final String PATTERN_DATE = "<dc:date>([^<]*)<";
	private static final String PATTERN_IDENTIFIER = "<dc:identifier>([^<]*)<";
	
	private static final String PATTERN_YEAR = ".*(\\d{4}).*";
	
	/**
	 * convert a oai2 refernce into bibtex
	 * @param reference 
	 * @return The resultign BibTeX string.
	 * @throws ScrapingException
	 */
	public static String convert(String reference) throws ScrapingException{
		StringBuffer bibtexResult = new StringBuffer();
		
		String key = "";
		//parse reference
		
		// get title
		String title = null;
		Pattern patternTitle = Pattern.compile(PATTERN_TITLE);
		Matcher matcherTitle = patternTitle.matcher(reference);
		if(matcherTitle.find())
			title = matcherTitle.group(1);
		
		//get authors
		String creator = "";
		Pattern patternCreator = Pattern.compile(PATTERN_CREATOR);
		Matcher matcherCreator = patternCreator.matcher(reference);
		while(matcherCreator.find()){
			if(creator.equals("")){
				creator = matcherCreator.group(1);
				// add lastname from the first author to bibtex key
				key = creator.substring(0, creator.indexOf(","));
			}else
				creator = creator + " and " + matcherCreator.group(1);
		}
		
		String description = "";
		String note = "";
		Pattern patternDescription = Pattern.compile(PATTERN_DESCRIPTION, Pattern.MULTILINE);
		Matcher matcherDescription = patternDescription.matcher(reference);
		while(matcherDescription.find()){
			if(matcherDescription.group(1).startsWith("Comment:"))
				note = matcherDescription.group(1);
			else if(description.equals(""))
				description = matcherDescription.group(1);
			else
				description = description + " " + matcherDescription.group(1);
		}
		
		String year = null;
		Pattern patternDate = Pattern.compile(PATTERN_DATE);
		Matcher matcherDate = patternDate.matcher(reference);
		if(matcherDate.find()){
			String date = matcherDate.group(1);
			Pattern patternYear = Pattern.compile(PATTERN_YEAR);
			Matcher matcherYear = patternYear.matcher(date);
			if(matcherYear.find()){
				year = matcherYear.group(1);
				key = key + year;
			}
		}
		
		String identifier = null;
		Pattern patternIdentifier = Pattern.compile(PATTERN_IDENTIFIER);
		Matcher matcherIdentifier = patternIdentifier.matcher(reference);
		if(matcherIdentifier.find())
			identifier = matcherIdentifier.group(1);
		
		// build bibtex
		
		// start and bibtex key
		bibtexResult.append("@MISC{");
		bibtexResult.append(key);
		bibtexResult.append(",\n");
		
		// title
		if(title != null){
			bibtexResult.append("title = {");
			bibtexResult.append(title);
			bibtexResult.append("}");
			bibtexResult.append(",\n");
		}else
			throw new ScrapingFailureException("no title found");
		
		// author
		if(!creator.equals("")){
			bibtexResult.append("author = {");
			bibtexResult.append(creator);
			bibtexResult.append("}");
			bibtexResult.append(",\n");
		}else
			throw new ScrapingFailureException("no authors found");

		// year
		if(year != null){
			bibtexResult.append("year = {");
			bibtexResult.append(year);
			bibtexResult.append("}");
			bibtexResult.append(",\n");
		}else
			throw new ScrapingFailureException("no year found");

		// abstract
		if(!description.equals("")){
			bibtexResult.append("abstract = {");
			bibtexResult.append(description);
			bibtexResult.append("}");
			bibtexResult.append(",\n");
		}
		
		// url
		if(identifier != null){
			bibtexResult.append("url = {");
			bibtexResult.append(identifier);
			bibtexResult.append("}");
			bibtexResult.append(",\n");
		}

		// note
		if(note != null){
			bibtexResult.append("note = {");
			bibtexResult.append(note);
			bibtexResult.append("}");
			bibtexResult.append(",\n");
		}

		// remove last ","
		bibtexResult = new StringBuffer(bibtexResult.subSequence(0, bibtexResult.lastIndexOf(",")));
		
		// finish bibtex
		bibtexResult.append("\n}\n");

		return bibtexResult.toString();
	}

}
