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

package org.bibsonomy.util;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Static methods for dealing with string representations of tags
 * 
 * @author 
 * @version $Id: TagStringUtils.java,v 1.11 2011-04-29 06:36:50 bibsonomy Exp $
 */
public class TagStringUtils {
	
	/**
	 * Tags which are ignored for recommendation. Note that those tags 
	 * are already cleaned using {@link #cleanTag(String)}.
	 */
	private static final Set<String> ignoreTags = new HashSet<String>();
	static {
		ignoreTags.add(cleanTag("imported"));
		ignoreTags.add(cleanTag("public"));
		ignoreTags.add(cleanTag("system:imported"));
		ignoreTags.add(cleanTag("nn"));
		ignoreTags.add(cleanTag("system:unfiled"));
	}
	
	/**
	 * Allows in a string of tags to change the delimiter to space. Additionally, tags consisting of
	 * more than one word (separated by whitespace) can be joined with whitespaceSub.
	 * 
	 *  Example:
	 *  
	 *  with the delimiter = "," and whitespace = "_"
	 *  the string 
	 *    computer algebra, maple, math
	 *  would be changed to
	 *    computer_algebra maple math
	 *  
	 * @param tagstring - a string of tags.
	 * @param substitute - <code>true</code>, if the tag string should be modified.
	 * @param delimiter - the character which separates the tags in the tag string.
	 * @param whitespaceSub - the character with which whitespace should be separated. 
	 * This allows to merge tags with more than one word into a tag with one word.
	 *   
	 * @return The cleaned string of tags.
	 */
	public static String cleanTags(final String tagstring, final boolean substitute, final String delimiter, final String whitespaceSub) {
		if (tagstring != null) {
			if (substitute && delimiter != null && delimiter.length() == 1 && !delimiter.trim().equals("")) {
				String tmpTags = tagstring.trim();

				// remove whitespace around delimiter
				tmpTags = tmpTags.replaceAll("\\s*" + Pattern.quote(delimiter) + "\\s*", delimiter);

				// substitute whitespace inside tags
				if (whitespaceSub != null && whitespaceSub.length() <= 1) {
					tmpTags = tmpTags.replaceAll("\\s+", whitespaceSub);
				}

				// user wants to have another delimiter than whitespace
				return tmpTags.replaceAll(Pattern.quote(delimiter), " ");
			}
			return tagstring;
		} 
		return "";
	}
	
	
	
	/** Checks if the given tag is a tag which should be ignored according 
	 * to the tag recommendation challenge rules.
	 * <br/>Note that the tag must have been cleaned using {@link #cleanTag(String)}!
	 * 
	 * @param tag
	 * @return <code>true</code> if the tag should be ignored.
	 */
	public static boolean isIgnoreTag(final String tag) {
		return tag == null || tag.isEmpty() || ignoreTags.contains(tag);
	}
	
	
	/**
	 * Cleans the given tag according to the recommendation challenge rules.
	 * Only numbers and letters (in lowercase!) remain, normalized to Unicode
	 * normal form KC. 
	 * 
	 * @see Normalizer
	 * @param tag
	 * @return The cleaned tag.
	 */
	public static String cleanTag(final String tag) {
		return Normalizer.normalize(tag.toLowerCase().replaceAll("[^0-9\\p{L}]+", ""), Normalizer.Form.NFKC);
	}
}