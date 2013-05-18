/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.model.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.SerializeBibtexMode;
import org.bibsonomy.common.enums.SortKey;
import org.bibsonomy.common.enums.SortOrder;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.PersonName;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.comparators.BibTexPostComparator;
import org.bibsonomy.model.comparators.BibTexPostInterhashComparator;
import org.bibsonomy.services.URLGenerator;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.util.tex.TexDecode;

/**
 * Some BibTex utility functions.
 * 
 * @author Dominik Benz
 * @version $Id: BibTexUtils.java,v 1.83 2011-07-15 12:31:28 rja Exp $
 */
public class BibTexUtils {
	private static final Log log = LogFactory.getLog(BibTexUtils.class);

	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource) 
	 */
	public static final String ADDITIONAL_MISC_FIELD_BIBURL = "biburl";

	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource) 
	 */
	public static final String ADDITIONAL_MISC_FIELD_DESCRIPTION = "description";

	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource). It is needed by the DBLP update to allow
	 * setting of the post date.
	 */
	public static final String ADDITIONAL_MISC_FIELD_DATE = "date";

	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource). It represents the "date" field of the post.
	 */
	public static final String ADDITIONAL_MISC_FIELD_ADDED_AT = "added-at";
	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource). It represents the "changeDate" of the post. 
	 */
	public static final String ADDITIONAL_MISC_FIELD_TIMESTAMP = "timestamp";

	/**
	 * ISO date + time for "added-at" and "timestamp" field  
	 */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource)
	 */
	public static final String ADDITIONAL_MISC_FIELD_KEYWORDS = "keywords";

	/**
	 * This field from the post is added to the BibTeX string (in addition to 
	 * all fields from the resource)
	 */
	public static final String ADDITIONAL_MISC_FIELD_PRIVNOTE = "privnote";

	/**
	 * This fields from the post are added to the BibTeX string (in addition to 
	 * all fields from the resource)
	 */
	public static final String[] ADDITIONAL_MISC_FIELDS = new String[] {
		ADDITIONAL_MISC_FIELD_DESCRIPTION,
		ADDITIONAL_MISC_FIELD_KEYWORDS,
		ADDITIONAL_MISC_FIELD_BIBURL,
		ADDITIONAL_MISC_FIELD_PRIVNOTE,
		ADDITIONAL_MISC_FIELD_ADDED_AT,
		ADDITIONAL_MISC_FIELD_TIMESTAMP
	};

	/**
	 * the supported entrytypes of a bibtex
	 * be careful when changing order some code uses the order to map entrytypes to (swrc|ris) entrytypes
	 * 
	 * e.g., in org.bibsonomy.model.util.BibTexUtils.ENTRYTYPES 
	 * 
	 * FIXME: this is bad. Please fix this behaviour. 
	 */
	public static final String[] ENTRYTYPES = {"article", "book", "booklet", "conference", "electronic", "inbook", "incollection", "inproceedings",
		"manual", "mastersthesis", "misc", "patent", "periodical", "phdthesis", "preamble", "presentation", "proceedings", "standard", "techreport", "unpublished"
	};

	/*
	 * patterns used for matching
	 */
	private static final Pattern YEAR_PATTERN = Pattern.compile("\\d{4}");
	private static final Pattern DOI_PATTERN = Pattern.compile("http://.+/(.+?/.+?$)");
	private static final Pattern LAST_COMMA_PATTERN = Pattern.compile(".+}?\\s*,\\s*}\\s*$", Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

	/*
	 * fields to be excluded when creating bibtex strings.
	 */
	private static final Set<String> EXCLUDE_FIELDS = new HashSet<String>(Arrays.asList(new String[] { 
			"abstract",        // added separately
			"bibtexAbstract",  // added separately
			"bibtexKey",       // added at beginning of entry
			"entrytype",       // added at beginning of entry
			"misc",            // contains several fields; handled separately 
			"month",           // handled separately
			"openURL", 
			"simHash0", // not added
			"simHash1", // not added
			"simHash2", // not added
			"simHash3"  // not added
	}));

	/**
	 * Some BibTeX styles translate month abbreviations into (language specific) 
	 * month names. If we find such a month abbreviation, we should not put 
	 * braces around the string.
	 */
	private static final Map<String, Integer> BIBTEX_MONTHS = new HashMap<String, Integer>();
	static {
		final String[] months = new String[] {
				"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"
		};
		for (int i = 0; i < months.length; i++) {
			BIBTEX_MONTHS.put(months[i], i + 1);
		}
	}


	/** default opening bracket */
	public static final char DEFAULT_OPENING_BRACKET = '{';
	/** default closing bracket */
	public static final char DEFAULT_CLOSING_BRACKET = '}';
	/** value separator used to separate key/value pairs; i.e. key=val SEP key2=val2*/
	public static final char KEYVALUE_SEPARATOR = ',';
	/** assignment operator to assign keys to values; i.e. key OP val, ...*/
	public static final char ASSIGNMENT_OPERATOR = '=';
	/** indentation used for key/value pairs when converted to a bibtex string */
	public static final String KEYVALUE_INDENT = "  ";

	/**
	 * Builds a string from a given bibtex object which can be used to build an OpenURL
	 * see http://www.exlibrisgroup.com/sfx_openurl.htm
	 *
	 * @param bib the bibtex object
	 * @return the DESCRIPTION part of the OpenURL of this BibTeX object
	 */
	public static String getOpenurl(final BibTex bib) {
		// stores the completed URL (just the DESCRIPTION part)
		final StringBuilder openurl = new StringBuilder();

		/*
		 * extract first authors parts of the name
		 */
		// get first author (if author not present, use editor)
		String author = bib.getAuthor();
		if (!present(author)) {
			author = bib.getEditor();
		}
		// TODO: this is only necessary because of broken (DBLP) entries which have neither author nor editor!
		if (!present(author)) {
			author = "";
		}
		final PersonName personName = new PersonName(author.replaceFirst(PersonNameUtils.PERSON_NAME_DELIMITER + ".*", "").trim());
		// check, if first name is just an initial
		final String auinit1;
		final String firstName = personName.getFirstName();
		if (present(firstName) && firstName.length() == 1) {
			auinit1 = firstName;
			personName.setFirstName(null);
		} else {
			auinit1 = null;
		}

		// parse misc fields
		bib.parseMiscField();
		// extract DOI
		String doi = bib.getMiscField("doi");
		if (doi != null) {
			// TODO: urls rausfiltern testen
			final Matcher m = DOI_PATTERN.matcher(doi);
			if (m.find()) {
				doi = m.group(1);
			}
		}

		try {
			// append year (due to inconsistent database not always given!)
			if (present(bib.getYear())) {
				openurl.append("date=" + bib.getYear().trim());
			}
			// append doi
			if (present(doi)) {
				appendOpenURL(openurl,"id", "doi:" + doi.trim());
			}
			// append isbn + issn
			appendOpenURL(openurl,"isbn", bib.getMiscField("isbn"));
			appendOpenURL(openurl,"issn", bib.getMiscField("issn"));
			// append name information for first author
			appendOpenURL(openurl, "aulast", personName.getLastName());
			appendOpenURL(openurl, "aufirst", firstName);
			appendOpenURL(openurl, "auinit1", auinit1);
			// genres == entrytypes
			if (bib.getEntrytype().toLowerCase().equals("journal")) {
				appendOpenURL(openurl, "genre", "journal");
				appendOpenURL(openurl, "title", bib.getTitle());
			} else if (bib.getEntrytype().toLowerCase().equals("book")) {
				appendOpenURL(openurl, "genre", "book");
				appendOpenURL(openurl, "title", bib.getTitle());
			} else if (bib.getEntrytype().toLowerCase().equals("article")) {
				appendOpenURL(openurl, "genre", "article");
				appendOpenURL(openurl, "title", bib.getJournal());
				appendOpenURL(openurl, "atitle", bib.getTitle());
			} else if (bib.getEntrytype().toLowerCase().equals("inbook")) {
				appendOpenURL(openurl, "genre", "bookitem");
				appendOpenURL(openurl, "title", bib.getBooktitle());
				appendOpenURL(openurl, "atitle", bib.getTitle());
			} else if (bib.getEntrytype().toLowerCase().equals("proceedings")) {
				appendOpenURL(openurl, "genre", "proceeding");
				appendOpenURL(openurl, "title", bib.getBooktitle());
				appendOpenURL(openurl, "atitle", bib.getTitle());
			} else {
				appendOpenURL(openurl, "title", bib.getBooktitle());
				appendOpenURL(openurl, "atitle", bib.getTitle());
			}
			appendOpenURL(openurl, "volume", bib.getVolume());
			appendOpenURL(openurl, "issue", bib.getNumber());
		} catch (final UnsupportedEncodingException ex) {
			log.error("error while generating openURL", ex);
		}

		return openurl.toString();
	}

	private static void appendOpenURL(final StringBuilder buffer, final String name, final String value) throws UnsupportedEncodingException {
		if (value != null && !value.trim().equals("")) {
			buffer.append("&" + name + "=" + URLEncoder.encode(value.trim(), "UTF-8"));
		}
	}


	/**
	 * return a bibtex string representation of the given bibtex object. by default, 
	 * the contained misc fields are parsed before the bibtex string is generated.
	 * 
	 * @param bib - the bibtex object
	 * @return - a string representation of the given bibtex object
	 */
	public static String toBibtexString(final BibTex bib) {
		return toBibtexString(bib, SerializeBibtexMode.PARSED_MISCFIELDS);
	}


	/**
	 * return a bibtex string representation of the given bibtex object
	 * 
	 * @param bib - a bibtex object
	 * @param mode - the serializing mode (parse misc fields or include misc fields as they are)
	 * @return String bibtexString
	 * 
	 * TODO use BibTex.DEFAULT_OPENBRACKET etc.
	 * 
	 */
	public static String toBibtexString(final BibTex bib, SerializeBibtexMode mode) {
		try {
			final BeanInfo bi = Introspector.getBeanInfo(bib.getClass());

			/*
			 * start with entrytype and key
			 */
			final StringBuilder buffer = new StringBuilder("@" + bib.getEntrytype() + "{" + bib.getBibtexKey() + ",\n");

			/*
			 * append all other fields
			 */
			for (final PropertyDescriptor d : bi.getPropertyDescriptors()) {
				final Method getter = d.getReadMethod();
				// loop over all String attributes
				final Object o = getter.invoke(bib, (Object[]) null);
				if (String.class.equals(d.getPropertyType()) 
						&& o != null 
						&& ! EXCLUDE_FIELDS.contains(d.getName()) ) {

					/*
					 * Strings containing whitespace give empty fields ... we ignore them 
					 */
					String value = ((String) o);
					if (present(value)) {
						if (! NUMERIC_PATTERN.matcher(value).matches()) {
							value = DEFAULT_OPENING_BRACKET + value + DEFAULT_CLOSING_BRACKET;
						}
						buffer.append("  " + d.getName().toLowerCase() + " = " + value + ",\n");
					}
				}
			}
			/*
			 * process miscFields map, if present
			 */
			if (present(bib.getMiscFields())) {
				if ( mode.equals(SerializeBibtexMode.PARSED_MISCFIELDS) && !bib.isMiscFieldParsed()) {
					// parse misc field, if not yet done
					bib.parseMiscField();
				}
				buffer.append(serializeMiscFields(bib.getMiscFields(), true));
			}

			/*
			 * include plain misc fields if desired
			 */
			if (mode.equals(SerializeBibtexMode.PLAIN_MISCFIELDS) && present(bib.getMisc())) {
				buffer.append("  " + bib.getMisc() + ",\n");
			}
			/*
			 * add month
			 */
			final String month = bib.getMonth();
			if (present(month)) {
				// we don't add {}, this is done by getMonth(), if necessary
				buffer.append("  month = " + getMonth(month) + ",\n");
			}
			/*
			 * add abstract
			 */
			final String bibAbstract = bib.getAbstract();
			if (present(bibAbstract)) {
				buffer.append("  abstract = {" + bibAbstract + "},\n");
			}
			/*
			 * remove last comma
			 */
			buffer.delete(buffer.lastIndexOf(","), buffer.length());
			buffer.append("\n}");	

			return buffer.toString();

		} catch (IntrospectionException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}		
		return null;
	}



	/**
	 * Some BibTeX styles translate month abbreviations into (language specific) 
	 * month names. If we find such a month abbreviation, we should not put 
	 * braces around the string. This method returns the correct string - with
	 * braces, if it's not an abbreviation, without otherwise.
	 * 
	 * @param month
	 * @return The correctly 'quoted' month.
	 */
	public static String getMonth(final String month) {
		if (month != null && BIBTEX_MONTHS.containsKey(month.toLowerCase().trim())) return month;
		return "{" + month + "}";
	}


	/**
	 * Tries to extract the month number from the given string. The following 
	 * input formats are supported:
	 * <ul>
	 * <li>long English month name: January, february, MARCH, ...</li>
	 * <li>abbreviated English month name: Jan, feb, MAR, ...</li>
	 * <li>month as number: 01, 2, 3, ...</li>
	 * </ul> 
	 * <strong>Note:</strong> if an unreadable month is given, the untouched
	 * string is returned. 
	 * 
	 * 
	 * @param month
	 * @return The month represented as number in the range 1, ..., 12
	 */
	public static String getMonthAsNumber(final String month) {
		if (present(month)) {
			final String trimmed = month.replace('#', ' ').trim();
			if (trimmed.length() >= 3) {
				final String abbrev = trimmed.toLowerCase().substring(0, 3);
				if (BIBTEX_MONTHS.containsKey(abbrev)) {
					return BIBTEX_MONTHS.get(abbrev).toString();
				}
			}
			return trimmed;
		}
		return month;
	}

	/**
	 * Creates a bibtex string with some bibsonomy-specific information using 
	 * {@link #toBibtexString(Post)}.
	 * 
	 * <ul>
	 * 		<li>tags in <code>keywords</code> field</li>
	 * 		<li>URL to bibtex details page in <code>biburl</code> field</li>
	 * 		<li>description in the <code>description</code> field</li>
	 * </ul>
	 * 
	 * @see #toBibtexString(Post)
	 * 
	 * @param post 
	 * 			- a bibtex post
	 * @param urlGenerator - to generate a proper URL pointing to the post. 
	 * 
	 * @return A string representation of the posts in BibTeX format.
	 */
	public static String toBibtexString(final Post<BibTex> post, final URLGenerator urlGenerator) {
		post.getResource().addMiscField(ADDITIONAL_MISC_FIELD_BIBURL, urlGenerator.getPublicationUrl(post.getResource(), post.getUser()).toString());
		return toBibtexString(post);
	}


	/**
	 * Return a bibtex representation of the given post. Defaults to 
	 * serialize mode PARSED_MISCFIELDS.
	 * 
	 * @param post - a post
	 * @return - a bibtex string representation of this post.
	 */
	public static String toBibtexString(final Post<BibTex> post) {
		return toBibtexString(post, SerializeBibtexMode.PARSED_MISCFIELDS);
	}

	/**
	 * Creates a BibTeX string containing more than only the fields in the 
	 * BibTeX object:
	 * 
	 * <ul>
	 * 		<li>tags in the <code>keywords</code> field</li>
	 *      <li>description in the <code>description</code> field</li>
	 * </ul>
	 * 
	 * @param post - a BibTeX post.
	 * @param mode - the serialize mode
	 * 
	 * @return A string representation of the post in BibTeX format.
	 */
	public static String toBibtexString(final Post<BibTex> post, SerializeBibtexMode mode) {
		final BibTex bib = post.getResource();	
		/*
		 * add additional fields.
		 *  
		 * ATTENTION: if you add fields here, you have to add them also 
		 * (in SimpleBibTeXParser.updateWithParsedBibTeX!)
		 * in ADDITIONAL_MISC_FIELDS. Thus when someone enters a bibtex field with the 
		 * name of your added field, it will not be stored in the misc section.
		 */
		bib.addMiscField(ADDITIONAL_MISC_FIELD_KEYWORDS, TagUtils.toTagString(post.getTags(), " "));
		if (present(post.getDescription())) {
			bib.addMiscField(ADDITIONAL_MISC_FIELD_DESCRIPTION, post.getDescription());
		}
		if (present(post.getDate())) {
			bib.addMiscField(ADDITIONAL_MISC_FIELD_ADDED_AT, DATE_FORMAT.format(post.getDate()));
		}
		if (present(post.getChangeDate())) {
			bib.addMiscField(ADDITIONAL_MISC_FIELD_TIMESTAMP, DATE_FORMAT.format(post.getDate()));
		}
		return toBibtexString(bib, mode);
	}

	/**
	 * @see #generateBibtexKey(String, String, String, String)
	 * @param bib
	 * 
	 * @return The generated BibTeX key.
	 */
	public static String generateBibtexKey(final BibTex bib) {
		if (bib == null) return "";
		return generateBibtexKey(bib.getAuthor(), bib.getEditor(), bib.getYear(), bib.getTitle());
	}

	/**
	 * Generates a bibtex key of the form "first persons lastname from authors
	 * or editors" or "noauthororeditor" concatenated with year.
	 * 
	 * @param authors
	 *            some string representation of the list of authors with their
	 *            first- and lastnames
	 * @param editors
	 *            some string representation of the list of editors with their
	 *            first- and lastnames
	 * @param year
	 * @param title
	 * @return a bibtex key for a bibtex with the fieldvalues given by arguments
	 */
	public static String generateBibtexKey(final String authors, final String editors, final String year, final String title) {
		/*
		 * TODO: pick either author or editor. DON'T use getAuthorlist (it sorts alphabetically!). CHECK for null values.
		 * What to do with Chinese authors and other broken names?
		 * How to extract the first RELEVANT word of the title?
		 * remove Sonderzeichen, LaTeX markup!
		 */
		final StringBuilder buffer = new StringBuilder();

		/* get author */
		String first = PersonNameUtils.getFirstPersonsLastName(authors);
		if (first == null) {
			first = PersonNameUtils.getFirstPersonsLastName(editors);
			if (first == null) {
				first = "noauthororeditor";
			}
		}
		buffer.append(first);

		/* the year */ 
		if (year != null) {
			buffer.append(year.trim());
		}

		/* first relevant word of the title */
		if (title != null) {
			/* best guess: pick first word with more than 4 characters, longest first word */
			// FIXME: what do we want to do inside this if statement?
			buffer.append(getFirstRelevantWord(title).toLowerCase());
		}

		return buffer.toString().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
	}


	/**
	 * Relevant = longer than four characters (= 0-9a-z)
	 * 
	 * @param title
	 * @return
	 */
	private static String getFirstRelevantWord(final String title) {
		final String[] split = title.split("\\s");
		for (final String s : split) {
			final String ss = s.replaceAll("[^a-zA-Z0-9]", "");
			if (ss.length() > 4) {
				return ss;
			}
		}
		return "";
	}



	/**
	 * Cleans up a string containing LaTeX markup and converts special chars to HTML special chars.
	 * 
	 * @param bibtex a bibtex string
	 * @return the cleaned bibtex string
	 */
	public static String cleanBibTex(String bibtex) {

		if (!present(bibtex)) return "";			


		// replace markup
		bibtex = bibtex.replaceAll("\\\\[a-z]+\\{([^\\}]+)\\}", "$1");  // \\markup{marked_up_text}		

		// decode Latex macros into unicode characters
		bibtex = TexDecode.decode(bibtex).trim();

		// convert non-ASCII into HTML entities
		final StringBuilder buffer = new StringBuilder(bibtex.length());
		char c;		
		for (int i = 0; i < bibtex.length(); i++) {
			c = bibtex.charAt(i);

			// HTML Special Chars
			if (c == '"')
				buffer.append("&quot;");
			else if (c == '&')
				buffer.append("&amp;");
			else if (c == '<')
				buffer.append("&lt;");
			else if (c == '>')
				buffer.append("&gt;");
			else {
				int ci = 0xffff & c;
				if (ci < 160 )
					// nothing special only 7 Bit
					buffer.append(c);
				else {
					// Not 7 Bit use the unicode system
					buffer.append("&#");
					buffer.append(new Integer(ci).toString());
					buffer.append(';');
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Tries to find a year (four connected digits) in a string and returns them as int.
	 * If it fails, returns Integer.MAX_VALUE.
	 * 
	 * @param year
	 * @return an integer representation of the year, or Integer.MAX_VALUE if it fails
	 */
	public static int getYear(final String year) {
		try {
			return Integer.parseInt(year);
		} catch (final NumberFormatException ignore) {
			/*
			 * try to get four digits ...
			 */
			final Matcher m = YEAR_PATTERN.matcher(year);
			if (m.find()) {
				return Integer.parseInt(m.group());
			}
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Sort a list of bibtex posts (and eventually remove duplicates).
	 * 
	 * @param bibtexList
	 * @param sortKeys
	 * @param sortOrders
	 */
	public static void sortBibTexList(final List<Post<BibTex>> bibtexList, final List<SortKey> sortKeys, final List<SortOrder> sortOrders) {
		if (present(bibtexList) && bibtexList.size() > 1) {
			Collections.sort(bibtexList, new BibTexPostComparator(sortKeys, sortOrders));
		}
	}

	/**
	 * Sorts a list of bibtex posts and removes duplicates.
	 * 
	 * @param bibtexList
	 */
	public static void removeDuplicates(final List<Post<BibTex>> bibtexList) {
		final Set<Post<BibTex>> temp = new TreeSet<Post<BibTex>>(new BibTexPostInterhashComparator());
		temp.addAll(bibtexList);
		// FIXME: a bit cumbersome at this point - but we need to work on the bibtexList
		bibtexList.clear();
		bibtexList.addAll(temp);
	}


	/** Adds the field <code>fieldName</code> to the BibTeX entry, if the entry 
	 * does not already contain it.
	 * 
	 * @param bibtex - the BibTeX entry
	 * @param fieldName - the name of the field
	 * @param fieldValue - the value of the field
	 * 
	 * @return The new BibTeX entry.
	 */
	public static String addFieldIfNotContained(final String bibtex, final String fieldName, final String fieldValue) {
		if (bibtex == null) return bibtex;

		final StringBuffer buf = new StringBuffer(bibtex);
		addFieldIfNotContained(buf, fieldName, fieldValue);
		return buf.toString();
	}

	/** Adds the field <code>fieldName</code> to the BibTeX entry, if the entry 
	 * does not already contain it.
	 * 
	 * @param bibtex - the BibTeX entry
	 * @param fieldName - the name of the field
	 * @param fieldValue - the value of the field
	 * 
	 */
	public static void addFieldIfNotContained(final StringBuffer bibtex, final String fieldName, final String fieldValue) {
		if (bibtex == null) return;
		/*
		 * it seems, we can do regex stuff only on strings ... so we have 
		 * to convert the buffer into a string :-(
		 */
		final String bibtexString = bibtex.toString();
		/*
		 * The only way safe to find out if the entry already contains
		 * the field is to parse it. This is expensive, thus we only 
		 * do simple heuristics, which is of course, error prone! 
		 */
		if (!bibtexString.matches("(?s).*" + fieldName + "\\s*=\\s*.*")) {
			/*
			 * add the field at the end before the last brace
			 */
			addField(bibtex, fieldName, fieldValue);
		}
	}

	/** Adds the given field at the end of the given BibTeX entry by placing
	 * it before the last brace. 
	 * 
	 * @param bibtex - the BibTeX entry
	 * @param fieldName - the name of the field
	 * @param fieldValue - the value of the field
	 */
	public static void addField(final StringBuffer bibtex, final String fieldName, final String fieldValue) {
		/*
		 * ignore empty bibtex and empty field values
		 */
		if (bibtex == null || fieldValue == null || fieldValue.trim().equals("")) return;

		/*
		 * remove last comma if there is one (before closing last curly bracket)
		 */
		final String bib = bibtex.toString().trim();
		final Matcher m = LAST_COMMA_PATTERN.matcher(bib);

		if (m.matches()) {
			final int _lastIndex = bib.lastIndexOf(",");
			bibtex.replace(_lastIndex, _lastIndex + 1, "");
		}

		final int lastIndexOf = bibtex.lastIndexOf("}");
		if (lastIndexOf > 0) {
			bibtex.replace(lastIndexOf, bibtex.length(), "," + fieldName + " = {" + fieldValue + "}\n}");
		}
	}

	/**
	 * replaces all " and "'s in author and editor with a new line
	 * @param bibtex
	 */
	public static void prepareEditorAndAuthorFieldForView(final BibTex bibtex) {
		final String author = prepareNameRepresentationForView(bibtex.getAuthor());
		final String editor = prepareNameRepresentationForView(bibtex.getEditor());

		bibtex.setAuthor(author);
		bibtex.setEditor(editor);
	}

	private static String prepareNameRepresentationForView(final String string) {
		return present(string) ? string.replaceAll(PersonNameUtils.PERSON_NAME_DELIMITER, "\n") : "";
	}

	/**
	 * reverses {@link #prepareEditorAndAuthorFieldForView(BibTex)}
	 * (replaces new line with an " and ")
	 * 
	 * @param bibtex
	 */
	public static void prepareEditorAndAuthorFieldForDatabase(final BibTex bibtex) {
		final String author = prepareNameRepresentationForDatabase(bibtex.getAuthor());
		final String editor = prepareNameRepresentationForDatabase(bibtex.getEditor());

		bibtex.setAuthor(author);
		bibtex.setEditor(editor);
	}

	private static String prepareNameRepresentationForDatabase(final String string) {
		return present(string) ? string.replaceAll("\n", PersonNameUtils.PERSON_NAME_DELIMITER) : "";
	}


	/**
	 * Converts the key = value pairs contained in the 
	 * miscFields map of a bibtex object into a serialized representation in the 
	 * misc-Field. It appends 
	 * 
	 *  key1 = {value1}, key2 = {value2}, ...
	 *  
	 * for all defined miscFields to the return string.
	 * 
	 * @param miscFields - a map containing key/value pairs
	 * @param appendTrailingSeparator - whether to append a trailing separator at the end of the string
	 * @return - a string representation of the given object.
	 */
	public static String serializeMiscFields(Map<String,String> miscFields, boolean appendTrailingSeparator) {
		final StringBuilder miscFieldsSerialized = new StringBuilder();
		// loop over misc fields, if any
		if (present(miscFields)) {
			final Iterator<String> it = miscFields.keySet().iterator();
			while (it.hasNext()) {				
				final String currKey = it.next();
				miscFieldsSerialized.append(KEYVALUE_INDENT + currKey.toLowerCase() + " " + ASSIGNMENT_OPERATOR + " " + DEFAULT_OPENING_BRACKET + miscFields.get(currKey) + DEFAULT_CLOSING_BRACKET);
				if (it.hasNext() || appendTrailingSeparator) {	miscFieldsSerialized.append(KEYVALUE_SEPARATOR + "\n");	}
			}

		}
		// write serialized misc fields into misc field
		return miscFieldsSerialized.toString();				
	}


	/**
	 * Parse a given misc field string into a hashmap containing key/value pairs.
	 * 
	 * @param miscFieldString - the misc field string
	 * @return a hashmap containg the parsed key/value pairs.
	 */
	public static Map<String,String> parseMiscFieldString(String miscFieldString) {
		return StringUtils.parseBracketedKeyValuePairs(miscFieldString, ASSIGNMENT_OPERATOR, KEYVALUE_SEPARATOR, DEFAULT_OPENING_BRACKET, DEFAULT_CLOSING_BRACKET);		
	}

}