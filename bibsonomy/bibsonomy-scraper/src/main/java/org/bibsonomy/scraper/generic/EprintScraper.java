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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.id.DOIUtils;

/**
 * Scraper for repositories which use eprint
 * 
 * @author tst
 * @version $Id: EprintScraper.java,v 1.10 2011-04-29 07:24:27 bibsonomy Exp $
 */
public class EprintScraper implements Scraper {

	private static final String INFO = "Scraper for repositories which use " + AbstractUrlScraper.href("http://www.eprints.org/", "eprints");

	private static final Pattern patternMeta = Pattern.compile("<meta content=\"([^\\\"]*)\" name=\"([^\\\"]*)\" />");
	private static final Pattern patternMetaReverseOrder = Pattern.compile("<meta name=\"([^\\\"]*)\" content=\"([^\\\"]*)\" />");

	private final static Map<String, String> eprintToBibtexFields = new HashMap<String, String>();
	private final static Map<String, String> eprintTypesToBibtexTypes = new HashMap<String, String>();

	static {
		eprintToBibtexFields.put("eprints.creators_name", "author");
		eprintToBibtexFields.put("eprints.editors_name", "editor");
		eprintToBibtexFields.put("eprints.type", "bibtextype");
		eprintToBibtexFields.put("eprints.title", "title");
		eprintToBibtexFields.put("eprints.note", "note");
		eprintToBibtexFields.put("eprints.abstract", "abstract");
		eprintToBibtexFields.put("eprints.date", "year");
		eprintToBibtexFields.put("eprints.volume", "volume");
		eprintToBibtexFields.put("eprints.publisher", "publisher");
		eprintToBibtexFields.put("eprints.place_of_pub", "adddress");
		eprintToBibtexFields.put("eprints.pagerange", "pages");
		eprintToBibtexFields.put("eprints.pages", "pages");
		eprintToBibtexFields.put("eprints.isbn", "isbn");
		eprintToBibtexFields.put("eprints.book_title", "booktitle");
		eprintToBibtexFields.put("eprints.official_url", "url");
		eprintToBibtexFields.put("eprints.publication", "journal");
		eprintToBibtexFields.put("eprints.number", "number");
		eprintToBibtexFields.put("eprints.issn", "issn");
		eprintToBibtexFields.put("eprints.id_number", "doi");

		eprintTypesToBibtexTypes.put("book", "book");
		eprintTypesToBibtexTypes.put("book_section", "inbook");
		eprintTypesToBibtexTypes.put("article", "article");
		eprintTypesToBibtexTypes.put("conference", "inproceeding");
		eprintTypesToBibtexTypes.put("monograph", "techreport");
		eprintTypesToBibtexTypes.put("thesis", "phdthesis");
	}

	public String getInfo() {
		return INFO;
	}

	public Collection<Scraper> getScraper() {
		return Collections.<Scraper> singleton(this);
	}

	public boolean scrape(ScrapingContext scrapingContext) throws ScrapingException {
		scrapingContext.setScraper(this);

		/*
		 * read eprint meta fields and resolve bibtex fields
		 */
		final Map<String, LinkedList<String>> bibtexFields = new HashMap<String, LinkedList<String>>();
		final Matcher metaMatcher = patternMeta.matcher(scrapingContext.getPageContent());
		final Matcher metaMatcherReverseOrder = patternMetaReverseOrder.matcher(scrapingContext.getPageContent());

		while (metaMatcher.find()) {
			final String value = metaMatcher.group(1);
			final String key = metaMatcher.group(2);
			// get bib field
			final String bibtexField = eprintToBibtexFields.get(key);

			// store in map
			if (!bibtexFields.containsKey(bibtexField)) {
				bibtexFields.put(bibtexField, new LinkedList<String>());
			}
			bibtexFields.get(bibtexField).add(value);
		}
		while (metaMatcherReverseOrder.find()) {
			final String value = metaMatcherReverseOrder.group(2);
			final String key = metaMatcherReverseOrder.group(1);
			// get bib field
			final String bibtexField = eprintToBibtexFields.get(key);

			// store in map
			if (!bibtexFields.containsKey(bibtexField)) {
				bibtexFields.put(bibtexField, new LinkedList<String>());
			}
			bibtexFields.get(bibtexField).add(value);

		}
		/*
		 * build bibtex
		 */

		// bibtex type
		final String bibtextype;
		if (bibtexFields.containsKey("bibtextype")) {
			final String type = bibtexFields.get("bibtextype").getFirst();
			if (eprintTypesToBibtexTypes.containsKey(type)) {
				bibtextype = eprintTypesToBibtexTypes.get(type);
			} else {
				bibtextype = "misc";
			}
		} else {
			bibtextype = "misc";
		}

		bibtexFields.remove("bibtextype"); // not needed anymore

		// bibtex key (author/editor lastname + year)
		String bibtexkey = "";
		String year = null;
		String firstLastname = null;
		// get components lastname and year
		if (bibtexFields.containsKey("author")) {
			String author = bibtexFields.get("author").getFirst();
			Matcher matcherLastname = Pattern.compile("([^,]*)").matcher(author);
			if (matcherLastname.find()) {
				firstLastname = matcherLastname.group(1);
			}
		} else if (bibtexFields.containsKey("editor")) {
			String editor = bibtexFields.get("editor").getFirst();
			Matcher matcherLastname = Pattern.compile("([^,]*)").matcher(editor);
			if (matcherLastname.find()) {
				firstLastname = matcherLastname.group(1);
			}
		}

		if (bibtexFields.containsKey("year")) {
			String yearField = bibtexFields.get("year").getFirst();
			Matcher matcherYear = Pattern.compile("(\\d{4})").matcher(yearField);
			if (matcherYear.find()) {
				year = matcherYear.group(1);
			}
		}
		// build bibtex key
		if (firstLastname != null && year != null) {
			bibtexkey = firstLastname + year;
		} else {
			bibtexkey = "defaultKey";
		}

		// build bibtex
		final StringBuffer bibtexBuffer = new StringBuffer("@" + bibtextype + "{" + bibtexkey + ",\n");

		// iterate over every bibtex field and append it to buffer
		// TODO: inefficient key iterator
		for (final String bibtexField : bibtexFields.keySet()) {
			if (bibtexField != null) {
				String value = null;
				// special handling for author
				if (bibtexField.equals("author")) {
					String authorString = "";
					// TODO: concatenates string using +
					for (String author : bibtexFields.get("author"))
						authorString = authorString + author + " and ";
					// remove last " and "
					authorString = authorString.substring(0, authorString.length() - 5);

					value = authorString;
				}
				// special handling for editor
				else if (bibtexField.equals("editor")) {
					String editorString = "";
					// TODO: concatenates string using +
					for (final String editor : bibtexFields.get("editor")) {
						editorString = editorString + editor + " and ";
					}
					// remove last " and "
					editorString = editorString.substring(0, editorString.length() - 5);
				}
				// special handling for doi
				else if (bibtexField.equals("doi")) {
					String doi = bibtexFields.get(bibtexField).getFirst();
					if (DOIUtils.isDOI(doi)) {
						value = doi;
					}
				}
				// special handling for year
				else if (bibtexField.equals("year")) {
					if (year != null) {
						value = year;
					} else {
						value = bibtexFields.get(bibtexField).getFirst();
					}
				}
				// rest, simply add
				else {
					String bibtexFieldValue = "";
					// TODO: concatenates string using +
					for (String singelValue : bibtexFields.get(bibtexField)) {
						bibtexFieldValue = bibtexFieldValue + " " + singelValue;
					}
					bibtexFieldValue = bibtexFieldValue.trim();

					value = bibtexFieldValue;
				}

				// append
				if (value != null) {
					bibtexBuffer.append(bibtexField + " = {" + value + "},\n");
				}
			}
		}

		bibtexBuffer.replace(bibtexBuffer.length() - 2, bibtexBuffer.length(), "\n");
		// finish
		bibtexBuffer.append("}");

		// append url
		BibTexUtils.addFieldIfNotContained(bibtexBuffer, "url", scrapingContext.getUrl().toString());

		scrapingContext.setBibtexResult(bibtexBuffer.toString());

		return true;
	}

	public boolean supportsScrapingContext(ScrapingContext scrapingContext) {
		try {
			final String page = scrapingContext.getPageContent();
			return page.contains("name=\"eprints.date\"") && page.contains("name=\"eprints.title\"");
		} catch (ScrapingException ex) {
			return false;
		}
	}

}
