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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.exceptions.ConversionException;
import org.bibsonomy.util.StringUtils;


/** Converts EndNote (RIS) into BibTeX.
 * 
 * @author rja
 * @version $Id: EndnoteToBibtexConverter.java,v 1.16 2011-04-29 07:24:34 bibsonomy Exp $
 */
public class EndnoteToBibtexConverter {

	private static final Log log = LogFactory.getLog(EndnoteToBibtexConverter.class);

	/**
	 * Maps every endnote entrytype to the corresponding BibTeX entrytype
	 */
	private static Map<String,String> endnoteToBibtexEntryTypeMap = new HashMap<String,String>();
	/**
	 * Maps every endnote field name to the corresponding BibTeX field name
	 */
	private static Map<String,String> endnoteToBibtexFieldMap     = new HashMap<String,String>();

	/*
	 * TODO explain pattern: We should clearly state which kind of Endnote is handled by this Converter
	 */
	private static final Pattern LINE_PATTERN       = Pattern.compile("(?s)((%\\S)\\s+(([^%]|%\\s)*))");



	static {
		// fill both maps to provide the data
		buildEndnoteToBibtexFieldMap();
		buildEndnoteToBibtexEntryTypeMap();
	}

	/**
	 * example for a StackOverFlowException (those occure only on 32bit Java versions)
	 * @param args
	 */
	public static void main (String[] args){
		String test1 = "%A Knox, John A.\n" +
		"%T Recent and Future Trends in U.S. Undergraduate Meteorology Enrollments, Degree Recipients, and Employment Opportunities\n"
		+ "%0 Journal Article\n"
		+ "%D 2008\n"
		+ "%J Bulletin of the American Meteorological Society\n"
		+ "%P 873-883\n"
		+ "\n"
		+ "%V 89\n"
		+ "%N 6\n"
		+ "%U http://dx.doi.org/10.1175%2F2008BAMS2375.1\n"
		+ "%8 June 01, 2008\n"
		+ "%X Using data derived from the American Meteorological Society&#8211;University Corporation for Atmospheric Research Curricula and U.S. Department of Education statistics, it is found that the number of meteorology bachelor&#039;s degree recipients in the United States has reached a level unprecedented in at least the past 40 years: from 600 to possibly 1,000 graduates per year. Furthermore, this number is increasing at a rate of approximately 8&#037;&#8211;11&#037; per year. The number of meteorology majors has also increased up to 10&#037; per year since the late 1990s. The number of meteorology bachelor&#039;s degree recipients is projected to increase at a rate of approximately 5&#037;&#8211;12&#037; per year through 2011. This simultaneous combination of record numbers and rapid recent increases is not mirrored in other related fields or in the American college population as a whole, suggesting a meteorology-specific cause for the increase in undergraduates. These graduation and enrollment trends are compared to data on the employment of meteorology bachelor&#039;s degree holders. The number of entry-level meteorology positions in the United States available each year appears to be no more than about half the number of new degreed meteorologists. According to data from the U.S. Bureau of Labor Statistics, growth in meteorology employment has averaged 1.2&#037; per year from 1994&#8211;2004 and is expected to be no more than 1.6&#037; per year through 2014. These numbers and trends portend an increasing oversupply of meteorology graduates versus meteorology employment opportunities if current enrollment and employment trends continue. Possible responses of the meteorology community are explored.";

		String test2 = "%D 2003\n" +
		"%0 ARTICLE\n"
		+ "%T Genomic gene clustering analysis of pathways in eukaryotes .\n"
		+ "%J Genome Research\n"
		+ "%V 13\n"
		+ "%P 875 882\n"
		+ "%A Lee JM\n"
		+ "%A Sonnhammer ELL\n"
		+ "%M WBPaper00005873\n"
		+ "%X Genomic clustering of genes in a pathway is commonly found in prokaryotes due to transcriptional operons , but these are not present in most eukaryotes . Yet , there might be clustering to a lesser extent of pathway members in eukaryotic genomes , that assist coregulation of a set of functionally cooperating genes . We analyzed five sequenced eukaryotic genomes for clustering of genes assigned to the same pathway in the KEGG database . Between 98% and 30% of the analyzed pathways in a genome were found to exhibit significantly higher clustering levels than expected by chance . in descending order by the level of clustering , the genomes studied were Saccharomyces cerevisiae , Homo sapiens , Caenorhabditis elegans , Arabidopsis thaliana , and Drosophila melanogaster . Surprisingly , there is not much agreement between genomes in terms of which pathways are most clustered . Only seven of 69 pathways found in all species were significantly clustered in all five of them . This species-specific pattern of pathway clustering may reflect adaptations or evolutionary events unique to a particular lineage . We note that although operons are common in C elegans , only 58% of the pathways showed significant clustering , which is less than in human . Virtually all pathways in S cerevisiae showed significant";

		EndnoteToBibtexConverter converter = new EndnoteToBibtexConverter();
		System.out.println(converter.processEntry(test1));
		System.out.println(converter.processEntry(test2));

	}

	/**
	 * Converts a reader providing EndNote entries into a reader providing
	 * BibTeX entries.
	 * 
	 * @param in
	 * @return A reader returning the BibTeX.
	 * @throws ConversionException 
	 */
	public Reader endnoteToBibtexReader(final BufferedReader in) throws ConversionException {
		return new BufferedReader(new StringReader(endnoteToBibtexString(in)));
	}

	/**
	 * Converts a reader providing EndNote entries into a string of BibTeX
	 * entries
	 * 
	 * @param in
	 * @return A string of BibTeX entries.
	 * @throws ConversionException
	 */
	public String endnoteToBibtexString(final BufferedReader in) throws ConversionException {
		try {
			return endnoteToBibtex(StringUtils.getStringFromReader(in));
		} catch (final IOException e) {
			throw new ConversionException("Could not convert from EndNote to BibTeX.");
		}
	}

	/**
	 * Converts a string of EndNote entries into a string of BibTeX entries.
	 * 
	 * @param endnote
	 * @return A string of BibTeX entries.
	 */ 
	public String endnoteToBibtex(final String endnote) {
		final StringBuffer result = new StringBuffer();

		// split the endnote entry by 2 blank lines
		final String[] _endNoteParts = endnote.split("(?m)^\\n{1}(?=%\\w{1}\\s{1})");

		// process each endnote entry
		for (final String part : _endNoteParts) {
			result.append("\n\n" + processEntry(part));
		}

		final String resultString = result.toString();
		return resultString;
	}

	/**
	 * Convert String of one single endnote entry into a String of one BibTeX entry
	 * @param entry
	 * @return The processed string.
	 */
	public String processEntry(String entry){

		final Map<String,String> map = new HashMap<String,String>();

		/*
		 * Handle abstract first
		 * = bug fix with large abstract (abstract might consist of more than one line)
		 */
		String abstractEntry = null;
		int startAbstract = entry.indexOf("%X");
		if(startAbstract != -1){

			String entryToAbstract = entry.substring(0, startAbstract);
			int endAbstract = entry.indexOf("\n", startAbstract)+1;
			abstractEntry = entry.substring(startAbstract+3, endAbstract-1);
			String entryAfterAbstract = entry.substring(endAbstract);

			// build new entry without abstract
			entry = entryToAbstract + entryAfterAbstract;
		}
		if(abstractEntry != null)
			map.put("abstract", abstractEntry);

		/*
		 * Go through all other fields
		 */
		try {
			// need to get every line (i.e. %T Foo %K bar)
			final Matcher eachLineMatcher = LINE_PATTERN.matcher(entry);

			//process each line
			while (eachLineMatcher.find()){

				// the content of this line's field
				String tempData = eachLineMatcher.group(3).trim();

				if (tempData == null){
					continue;
				}
				final String tempLine = eachLineMatcher.group(0).trim();
				// the type of this lines field
				final String endnoteType = eachLineMatcher.group(2).trim();

				/*
				 * map the reference type
				 */
				if (endnoteToBibtexEntryTypeMap.containsKey(tempLine)) {
					map.put("type", endnoteToBibtexEntryTypeMap.get(tempLine));
				} else {
					/*
					 * handle standard fields
					 */
					if (endnoteToBibtexFieldMap.containsKey(endnoteType)) {
						final String bibtexFieldName = endnoteToBibtexFieldMap.get(endnoteType);
						tempData = this.preprocess(bibtexFieldName, tempData);
						if (map.containsKey(bibtexFieldName)) {
							/*
							 * field already contained: special handling!
							 * handle author and editor, ignore others
							 */
							if ("author".equals(bibtexFieldName) || "editor".equals(bibtexFieldName)) {
								final String newName = map.get(bibtexFieldName) + " and " + tempData;
								map.put(bibtexFieldName, newName);
							}
						} else {
							map.put(bibtexFieldName, tempData);
						}
					}
				}
			}

			/*
			 * special postprocessing for ISBN/ISSN
			 */
			if (map.containsKey("isbn")) {
				final String isbn = map.get("isbn");
				if (isbn.length() == 8 || isbn.length() == 9) {
					map.remove("isbn");
					map.put("issn", isbn);
				}
			}

			/*
			 * build the bibtex key
			 */
			String year = map.get("year");
			if (year != null && year.length() != 4) {
				// if year is not valid (sometimes it contains day, month and/or time), ignore it
				year = null;
			}
			map.put("key", BibTexUtils.generateBibtexKey(map.get("author"), map.get("editor"), year, map.get("title")));


		} catch (RuntimeException e){
			log.fatal("Could not process the data: " + e);
		} catch (Exception e){
			log.fatal("Could not process the data: " + e);
		}

		/*
		 * in the end its necessary to build the complete bibtex part 
		 * i.e. 
		 * 
		 * @article{foo{
		 * author={bar and foo},
		 * title={foobars big revenge},
		 * abstract={this is a shot example}}
		 * 
		 */
		return buildBibtex(map);
	}

	/**
	 * Preprocessing to clean up some of the entries.
	 * currently only author and editor fields are processed:
	 * a field of the form "surname1, firstname1; surname2, firstname2 & surname3, firstname3"
	 * is transformed into "firstname1 surname1 and firstname2 surname2 and firstname3 surname3"   
	 * @param bibtexFieldName
	 * @param fieldEntry
	 * @return
	 */
	private String preprocess(String bibtexFieldName, String fieldEntry) {
		// preprocess author and editor
		if ("author".equals(bibtexFieldName) || "editor".equals(bibtexFieldName)) {
			if (fieldEntry.contains(";") && fieldEntry.contains("&")) {
				/*
				 *  we assume to have the form
				 *  "author1; author2; author3 & author 4"
				 */
				StringBuffer result = new StringBuffer();
				String[] authors = fieldEntry.split("[;&]");
				for (int i = 0; i<authors.length; i++) {
					/*
					 * we distinguish the forms
					 * "surname, firstname" and
					 * "firstname surname"
					 */
					String[] parts = authors[i].split(",");
					if (parts.length>1) {
						// we assume "surname, firstname" => add firstname first
						result.append(parts[1].trim());
						result.append(" ");
					}
					result.append(parts[0].trim());
					if (i<authors.length-1) {
						result.append(" and ");
					}
				}
				return result.toString();
			}
		}
		return fieldEntry;

	}


	//method to build a String with the complete Bibtex part
	private String buildBibtex(Map<String,String> data){

		//the StringBuffer to store the complete String
		StringBuffer result = new StringBuffer();

		try {
			/*
			 * if the data map is empty return null because it cant be an
			 * endnote file.
			 */
			if (data.isEmpty()){
				return null;
			}

			/*
			 * if no type is available the we use the following rules to specify the reference type
			 * 
			 * 1. %J and %V 					-> article (Journal Article)
			 * 2. %B 							-> incollection (Book Section)
			 * 3. %R but not %T 				-> misc (Report)
			 * 4. %I without %B, %J, or %R 		-> book (Book)
			 * 5. Neither %B, %J, %R, nor %I 	-> article (Journal Article)
			 */
			if (!data.containsKey("type")){
				if (data.containsKey("journal") && data.containsKey("volume")) {
					data.put("type", "article");
				} else if (data.containsKey("booktitle")) {
					data.put("type", "incollection");
				} else if (data.containsKey("address") && !(data.containsKey("booktitle") || data.containsKey("journal"))) {
					data.put("type", "book"); 
				} else if (!data.containsKey("booktitle") && !data.containsKey("journal") && !data.containsKey("%address")) {
					data.put("type", "article");
				}
			}

			// test if some necessary items were available, if not need to fix that with dummys
			if (!data.containsKey("type")) {
				data.put("type", "misc");
			}

			//now the bibtex part will be build completely starting with the type
			//clean up key (in InformaworldScraper a "," occurs in key, which results in a broken bibtex entry
			result.append("@" + data.get("type") + "{" + data.get("key").replace(",", "") + ",\n");
			data.remove("type");
			data.remove("key");

			//add every item of the data map to the stringbuffer
			for (String key : data.keySet()){
				result.append(key + " = {" + data.get(key) + "},\n");
			}

			// remove "," before last "\n"
			result.deleteCharAt(result.length() - 2 );

			// and at the end close the bibtexpart
			result.append("}");

		} catch (Exception e){
			log.fatal("Could not build the bibtex part :" + e);
		}

		//return the final String
		return result.toString();
	}


	/**
	 * returns true if the snippet contains only endnote entries, false otherwise
	 * WARNING: this is a heuristic!
	 * @param snippet
	 * @return true if snippet is endnote
	 */
	public static boolean canHandle(final String snippet) {
		final Matcher _eachLineMatcher = LINE_PATTERN.matcher(snippet.trim());
		return _eachLineMatcher.find();	// true if the first line looks like endnote with "%"
	}


	private static void buildEndnoteToBibtexFieldMap () {
		endnoteToBibtexFieldMap.put("%A",                     "author"); // Author
		endnoteToBibtexFieldMap.put("%B",                  "booktitle"); // Secondary Title
		endnoteToBibtexFieldMap.put("%C",               "howpublished"); // Place Published
		endnoteToBibtexFieldMap.put("%D",                       "year"); // Year
		endnoteToBibtexFieldMap.put("%E",                     "editor"); // Editor
		// endnoteToBibtexFieldMap.put("%F", "label"); // Label
		// endnoteToBibtexFieldMap.put("%G", "language"); // Language
		// endnoteToBibtexFieldMap.put("%H", "translated_author"); // Translated
		// Author
		endnoteToBibtexFieldMap.put("%I",                    "address"); // Publisher
		endnoteToBibtexFieldMap.put("%J",                    "journal"); // Journal Name
		endnoteToBibtexFieldMap.put("%K",                   "keywords"); // Keywords
		// endnoteToBibtexFieldMap.put("%L", "call_number"); // Call Number
		// endnoteToBibtexFieldMap.put("%M", "accession_number"); // Accession
		// Number
		endnoteToBibtexFieldMap.put("%N",                     "series"); // Number
		endnoteToBibtexFieldMap.put("%P",                      "pages"); // Pages
		// endnoteToBibtexFieldMap.put("%Q", "translated_title"); // Translated
		// Title
		// endnoteToBibtexFieldMap.put("%R", "electronic_resource_number"); //
		// Electronic Resource Number
		// endnoteToBibtexFieldMap.put("%S", "tertiary title"); // Tertiary
		// Title
		endnoteToBibtexFieldMap.put("%T",                      "title"); // Title
		endnoteToBibtexFieldMap.put("%U",                        "url"); // URL
		endnoteToBibtexFieldMap.put("%V",                     "volume"); // Volume
		// %W Database Provider
		endnoteToBibtexFieldMap.put("%X",                   "abstract"); // Abstract
		// %Y Tertiary Author
		endnoteToBibtexFieldMap.put("%Z",                     "annote"); // Notes
		// TODO: fehlende Felder (u.U. auskommentiert) einsetzen
		endnoteToBibtexFieldMap.put("%7",                    "edition"); // Edition
		endnoteToBibtexFieldMap.put("%&",                    "chapter"); // Section
		endnoteToBibtexFieldMap.put("%@",                       "isbn"); // ISBN/ISSN

	}

	private static void buildEndnoteToBibtexEntryTypeMap () {
		/*
		 * -> book
		 */
		endnoteToBibtexEntryTypeMap.put("%0 Book",            "book");
		endnoteToBibtexEntryTypeMap.put("%0 Edited Book",     "book");
		endnoteToBibtexEntryTypeMap.put("%0 Electronic Book", "book");

		/*
		 * -> article 
		 */
		endnoteToBibtexEntryTypeMap.put("%0 Journal Article",    "article"); 
		endnoteToBibtexEntryTypeMap.put("%0 Magazine Article",   "article");
		endnoteToBibtexEntryTypeMap.put("%0 Newspaper Article",  "article");
		endnoteToBibtexEntryTypeMap.put("%0 Electronic Article", "article");

		endnoteToBibtexEntryTypeMap.put("%0 Thesis",                 "mastersthesis");
		endnoteToBibtexEntryTypeMap.put("%0 Unpublished Work",       "unpublished");
		endnoteToBibtexEntryTypeMap.put("%0 Conference Paper",       "inproceedings");
		endnoteToBibtexEntryTypeMap.put("%0 Conference Proceedings", "proceedings");
		endnoteToBibtexEntryTypeMap.put("%0 Book Section",           "incollection");
		endnoteToBibtexEntryTypeMap.put("%0 Unpublished Work",       "unpublished");

		/*
		 * -> misc
		 */
		endnoteToBibtexEntryTypeMap.put("%0 Generic",               "misc"); 
		endnoteToBibtexEntryTypeMap.put("%0 Artwork",               "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Audiovisual Material",  "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Bill",                  "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Case",                  "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Chart or Table",        "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Classical Work",        "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Electronic Source",     "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Equation",              "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Figure",                "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Film or Broadcast",     "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Government Document",   "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Hearing",               "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Legal Rule/Regulation", "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Manuscript",            "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Map",                   "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Online Database",       "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Online Multimedia",     "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Patent",                "patent");
		endnoteToBibtexEntryTypeMap.put("%0 Personal Communication","misc");
		endnoteToBibtexEntryTypeMap.put("%0 Report",                "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Statute",               "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Unused 1",              "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Unused 2",              "misc");
		endnoteToBibtexEntryTypeMap.put("%0 Unused 2",              "misc");


	}


}