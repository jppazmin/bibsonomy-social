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

package org.bibsonomy.util.id;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rja
 * @version $Id: ISBNUtils.java,v 1.7 2011-04-29 06:36:59 bibsonomy Exp $
 */
public class ISBNUtils {
	/*
	 * pattern to match ISBN 10 and 13
	 */
	private static final Pattern isbnPattern = Pattern.compile("([^0-9]|^)(978\\d{9}[\\dx]|979\\d{9}[\\dx]|\\d{9}[\\dx])([^0-9x]|$)", Pattern.CASE_INSENSITIVE);

	/*
	 * pattern to match ISSN 8 and 13
	 */
	private static final Pattern issnPattern = Pattern.compile("([^0-9]|^)(977\\d{9}[\\dx]|\\d{7}[\\dx])([^0-9x]|$)", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Search substring with pattern format and returns it.
	 * @param snippet 
	 * @return ISBN, null if no ISBN is available
	 */
	public static String extractISBN(final String snippet){
		if (snippet != null) {
			final Matcher isbnMatcher = isbnPattern.matcher(cleanISBN(snippet));
			if (isbnMatcher.find())
				return isbnMatcher.group(2);
		}
		return null;
	}
	
	/**
	 * Search substring with pattern format and returns it.
	 * @param snippet 
	 * @return ISSN, null if no ISSN is available
	 */
	public static String extractISSN(final String snippet) {
		if (snippet != null) {
			final Matcher issnMatcher = issnPattern.matcher(cleanISBN(snippet));
			if (issnMatcher.find())
				return issnMatcher.group(2);
		}
		return null;
	}


	/**
	 * remove seperation signs between numbers
	 * @param snippet from ScrapingContext
	 * @return snippet without " " and "-"
	 */
	public static String cleanISBN(final String snippet){
		return snippet.replaceAll("\\s", "").replace("-", "");
	}
	
	/**
	 * replaces seperations through "-"
	 * @param snippet
	 * @return snippet with "-" seperated
	 */
	public static String cleanISSN(final String snippet){
		return snippet.replaceAll(" ", "-");
	}
}
