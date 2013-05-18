/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.util.tex;

import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Framework to encode TeX Macros to unicode.
 * 
 * @author Christian Claus, Dominik Benz
 * @version $Id: TexDecode.java,v 1.8 2011-04-29 06:36:52 bibsonomy Exp $
 */
public class TexDecode {

	/** logger */
	private static final Log LOGGER = LogFactory.getLog(TexDecode.class);
	/**
	 * file holding the mapping between latex macros and unicode codes
	 */
	private static final String LATEXMACRO_UNICODECHAR_MAP_FILENAME = "latex_macro_unicode_char_map.tsv";
	private static final String LATEXMACRO_UNICODECHAR_MAP_DELIM = "\t";
	/**
	 * the mapping between latex macros and unicode codes. It needs to be sorted
	 * because when we build the regex from it, the "longest" macros need to be
	 * present at the beginning of the regex.
	 */
	private static TreeMap<String, String> texMap = new TreeMap<String, String>(new StringLengthComp());
	/** regex patterns */
	private static Pattern texRegexpPattern;
	private static final String CURLY_BRACKETS = "[{}]*";
	private static final String BRACKETS = "[()]*[\\[\\]]*";

	/**
	 * helper comparator to sort strings by their length
	 */
	private static class StringLengthComp implements Comparator<String> {
		@Override
		public int compare(String s1, String s2) {
			if (s1.length() > s2.length()) return -1;
			if (s1.length() < s2.length()) return 1;
			return s1.compareTo(s2);
		}
	}

	/**
	 * initializes the HashMap 'texMap' with TeX macros as key and a referenced
	 * Unicode value as value. Also builds the regex for matching the tex
	 * macros.
	 */
	static {
		loadMapFile();
		final StringBuffer texRegexp = new StringBuffer();
		texRegexp.append("(");
		for (String macro : texMap.keySet()) {
			// build regex
			texRegexp.append(Pattern.quote(macro));
			texRegexp.append("|");
		}
		// delete last "|", add closing bracket
		texRegexp.deleteCharAt(texRegexp.length() - 1);
		texRegexp.append(")");
		// compile pattern
		texRegexpPattern = Pattern.compile(texRegexp.toString());
	}

	/**
	 * Decodes a String which contains TeX macros into it's Unicode
	 * representation.
	 * 
	 * @param s
	 * @return Unicode representation of the String
	 */
	public static String decode(String s) {
		if (s != null) {
			final Matcher texRegexpMatcher = texRegexpPattern.matcher(s.trim().replaceAll(CURLY_BRACKETS, ""));
			final StringBuffer sb = new StringBuffer();
			int i = 0;
			while (texRegexpMatcher.find()) {
				i++;
				texRegexpMatcher.appendReplacement(sb, texMap.get(texRegexpMatcher.group()));
			}
			texRegexpMatcher.appendTail(sb);
			return sb.toString().trim().replaceAll(BRACKETS, "");
		}
		return "";
	}

	/**
	 * Getter for the texMap
	 * 
	 * @return HashMap of TeX->Unicode representation
	 */
	protected static TreeMap<String, String> getTexMap() {
		return texMap;
	}

	/**
	 * parse the file containing the mappings of unicode characters to latex
	 * macros and store it in texMap.
	 */
	private static final void loadMapFile() {
		Scanner scanner = new Scanner(TexDecode.class.getClassLoader().getResourceAsStream(LATEXMACRO_UNICODECHAR_MAP_FILENAME), "UTF-8");
		String line;
		String[] parts;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			parts = line.split(LATEXMACRO_UNICODECHAR_MAP_DELIM);
			// convert hex representation into unicode string
			texMap.put(parts[1].trim(), String.valueOf(Character.toChars(Integer.parseInt(parts[0].trim(), 16))));
			LOGGER.debug("added new mapping " + parts[1].trim() + " -> " + texMap.get(parts[1].trim()));
		}

	}

}