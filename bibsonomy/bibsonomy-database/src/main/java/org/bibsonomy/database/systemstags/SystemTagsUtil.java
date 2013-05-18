/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.systemstags;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.database.systemstags.executable.ExecutableSystemTag;
import org.bibsonomy.database.systemstags.executable.ForFriendTag;
import org.bibsonomy.database.systemstags.executable.ForGroupTag;
import org.bibsonomy.database.systemstags.markup.MarkUpSystemTag;
import org.bibsonomy.database.systemstags.search.SearchSystemTag;
import org.bibsonomy.model.Tag;

/**
 * Helper class to encapsulate methods to create and work with systemTags
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: SystemTagsUtil.java,v 1.25 2011-06-16 14:29:59 doerfel Exp $
 */
public class SystemTagsUtil {
	/**
	 *  the pattern matches a string and devides it into tree parts
	 *  the first part is the optional prefix "sys" or "system"
	 *  the second part is called the type
	 *  the last part is called the argument
	 * <ul>
	 * <li> someStringWithoutColon:someString 
	 * <li> sys:someStringWithoutColon:someString
	 * <li> system:someStringWithoutColon:someString
	 * </ul>
	 *  WARNING: this pattern identifies any String of the above form
	 *  Thus a string that matches the pattern is not neccessarily a systemTag - it just looks like one
	 */
	private final static Pattern SYS_TAG_PATTERN = Pattern.compile("^(sys:|system:)?([^:]+)(:(.+))?");
	private final static String PREFIX ="sys";
	public final static String DELIM = ":";


	/*
	 * Methods to tell systemTags from "regular" tags 
	 */

	/**
	 * Determines whether a tag (given by name) is an executable systemTag
	 * 
	 * @param tagName = the tags name
	 * @return true if the tag is an executable systemTag, false otherwise
	 */
	public static boolean isExecutableSystemTag(final String tagName) {
		return getSystagfactory().isExecutableSystemTag(tagName);
	}

	/**
	 * Determines whether a tag (given by name) is a searchSystemTag
	 * 
	 * @param tagName = the tags name
	 * @return true if the tag is a searchSystemTag, false otherwise
	 */
	public static boolean isSearchSystemTag(final String tagName) {
		return getSystagfactory().isSearchSystemTag(tagName);
	}

	/**
	 * Determines whether a tag (given by name) is a searchSystemTag
	 * 
	 * @param tagName = the tags name
	 * @return true if the tag is a searchSystemTag, false otherwise
	 */
	public static boolean isMarkUpSystemTag(final String tagName) {
		return getSystagfactory().isMarkUpSystemTag(tagName);
	}

	/**
	 * Determines whether a tag (given by name) is a systemTag
	 * (i. e. if it is registered as a systemTag in BibSonomy)
	 * @param tagName = the tags name
	 * @return true if the tag is a systemTag, false otherwise
	 */
	public static boolean isSystemTag(final String tagName) {
		return getSystagfactory().isExecutableSystemTag(tagName) || 
		getSystagfactory().isSearchSystemTag(tagName) ||
		getSystagfactory().isMarkUpSystemTag(tagName);
	}

	/**
	 * Determines whether a tag (given by name) is a systemTag of a given kind
	 * (i. e. if the extracted tagName matches a given String
	 * @param tagName
	 * @param tagType
	 * @return <code>true</code> iff it is a systemtag of the given kind
	 */
	public static boolean isSystemTag(final String tagName, final String tagType) {
		final String extractedTagType = extractType(tagName);
		return present(extractedTagType) && extractedTagType.equalsIgnoreCase(tagType) && isSystemTag(tagName);
	}


	/**
	 * Checks, if a list of tagNames contains a member, that starts with a given string, ignoring case
	 * @param tagNames = a list of tagNames
	 * @param tagType = the type we are looking for
	 * @return true if tagNames contains a tagName that matches the given tagType as a systemTag
	 */
	public static boolean containsSystemTag(final List<String> tagNames, final String tagType) {
		for (final String tagName : tagNames) {
			if (isSystemTag(tagName, tagType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks, if a list of tags contains a member, that starts with a given string, ignoring case
	 * @param tags = a collection of tags
	 * @param tagType = the type we are looking for
	 * @return true if tags contains a tag that matches the given tagType as a systemTag
	 */
	public static boolean containsSystemTag(final Collection<Tag> tags, final String tagType) {
		for (final Tag tag: tags) {
			if (present(tag) && isSystemTag(tag.getName(), tagType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Counts the number of "regular" (i.e., non-system) tags
	 * within a list of tags
	 * 
	 * @param tagNames = a list of tagNames
	 * @return the number of non-systemTags
	 */
	public static int countNonSystemTags(final List<String> tagNames) {
		int numNonSysTags = 0;
		for (final String tagName : tagNames) {
			if (!isSystemTag(tagName)) {
				numNonSysTags++;
			}			
		}
		return numNonSysTags;
	}

	/*
	 * Methods to create systemTags
	 */

	/**
	 * Create a new instance of a markUp systemTag
	 * 
	 * @param tagName the original tag from that a systemTag is to be created
	 * @return a new instance of the matching systemTag 
	 * 		   or null, if the given tag does not describe a systemTag
	 */
	public static SystemTag createSystemTag(final Tag tag) {
		SystemTag sysTag = createExecutableTag(tag);
		if (present(sysTag)) {
			return sysTag;
		}
		sysTag = createMarkUpSystemTag(tag.getName());
		if (present(sysTag)) {
			return sysTag;
		}
		sysTag = createSearchSystemTag(tag.getName());
		return sysTag;
	}

	/**
	 * Create a new instance of an executable systemTag
	 * 
	 * @param tag = the original Tag from that a systemTag is to be created
	 * @return a new instance of the matching systemTag 
	 * 		   or null, if the given tag does not describe a systemTag
	 */
	public static ExecutableSystemTag createExecutableTag(final Tag tag) {
		final ExecutableSystemTag sysTag = getSystagfactory().getExecutableSystemTag(tag.getName());
		/*
		 *  FIXME: this should be done inside the systemtagFactory!
		 */
		if (present(sysTag)) {
			sysTag.setArgument(extractArgument(tag.getName()));
			setIndividualFields(sysTag, tag);
		}
		return sysTag;
	}

	/**
	 * Sets some fields, that are required by some ExecutableSystemTags but not all of them
	 * @param sysTag
	 */
	private static void setIndividualFields(final ExecutableSystemTag sysTag, final Tag tag) {
		if (ForGroupTag.class.isAssignableFrom(sysTag.getClass())) {
			// The forGroupTag needs a DBSessionFactory to create a post for the group
			((ForGroupTag)sysTag).setDBSessionFactory(getSystagfactory().getDbSessionFactory());
		} else if (ForFriendTag.class.isAssignableFrom(sysTag.getClass())) {
			// The forFriendTag needs access to the regular Tag of its post
			((ForFriendTag)sysTag).setTag(tag);
		}
	}

	/**
	 * Create a new instance of a search systemTag
	 * 
	 * @param tagName the original tag from that a systemTag is to be created
	 * @return a new instance of the matching systemTag 
	 * 		   or null, if the given tag does not describe a systemTag
	 */
	public static SearchSystemTag createSearchSystemTag(final String tagName) {
		final SearchSystemTag sysTag = getSystagfactory().getSearchSystemTag(tagName);
		if (present(sysTag)) {
			sysTag.setArgument(extractArgument(tagName));
		}
		return sysTag;
	}

	/**
	 * Create a new instance of a markUp systemTag
	 * 
	 * @param tagName the original tag from that a systemTag is to be created
	 * @return a new instance of the matching systemTag 
	 * 		   or null, if the given tag does not describe a systemTag
	 */
	public static MarkUpSystemTag createMarkUpSystemTag(final String tagName) {
		final MarkUpSystemTag sysTag = getSystagfactory().getMarkUpSystemTag(tagName);
		if (present(sysTag)) {
			sysTag.setArgument(extractArgument(tagName));
		}
		return sysTag;
	}


	/**
	 * Builds a system tag string for a given kind of system tag and an argument
	 * @param sysTagName = which kind of system tag
	 * @param sysTagArgument = argument of the system tag
	 * @return the system tag string built
	 */
	public static String buildSystemTagString(final String sysTagName, final String sysTagArgument) {
		return PREFIX + DELIM + sysTagName + DELIM + sysTagArgument;
	}

	/**
	 * Wrapper method for {SystemTagsUtil.buildSystemTagString(SystemTags sysTagName, String sysTagArgument)}
	 * @param sysTagName
	 * @param sysTagArgument 
	 * @return @see {SystemTagsUtil.buildSystemTagString(SystemTags sysTagPrefix, String sysTagValue)}
	 */
	public static String buildSystemTagString(final String sysTagName, final Integer sysTagArgument) {
		return buildSystemTagString(sysTagName, String.valueOf(sysTagArgument));
	}

	/*
	 * Methods to extract or analyze tags for their systemTag properties
	 */

	/**
	 * Extract systemTag's argument i. e. it maps
	 * <ul>
	 * <li> someStringWithoutColon:someString &rarr someString 
	 * <li> sys:someStringWithoutColon:someString &rarr someString
	 * <li> system:someStringWithoutColon:someString &rarr someString
	 * <li> someStringWithoutColon &rarr null
	 * <li> null &rarr null
	 * </ul>
	 * @param tagName the system tag string
	 * @return tag's name
	 */
	public static String extractArgument(final String tagName) {
		if (!present(tagName)) {
			return null;
		}
		final Matcher sysTagMatcher = SYS_TAG_PATTERN.matcher(tagName);
		if (sysTagMatcher.lookingAt() && present(sysTagMatcher.group(4))) {
			return sysTagMatcher.group(4).trim();
		}
		return null;
	}

	/**
	 * Extract system tag's name i. e. it returns someStringWithoutColon.toLowerCase() for
	 * <ul>
	 * <li> someStringWithoutColon:someString 
	 * <li> sys:someStringWithoutColon:someString
	 * <li> system:someStringWithoutColon:someString
	 * <li> someStringWithoutColon
	 * <li> and null if the param tagName is null
	 * </ul>
	 * @param tagName the system tag string
	 * @return tag's name
	 */
	public static String extractType(final String tagName) {
		if (!present(tagName)) {
			return null;
		}
		final Matcher sysTagMatcher = SYS_TAG_PATTERN.matcher(tagName);
		if (sysTagMatcher.lookingAt()) {
			return sysTagMatcher.group(2).toLowerCase();
		}

		return tagName.toLowerCase();
	}

	/**
	 * Returns true if the given tagName looks like a systemTag with Prefix
	 * (i. e. is of the form prefix:type:argument and none of the three parts is empty)
	 * @param tagName
	 * @return
	 */
	public static boolean hasPrefixTypeAndArgument(final String tagName) {
		if (!present(tagName)) {
			return false;
		}
		final Matcher sysTagMatcher = SYS_TAG_PATTERN.matcher(tagName);
		if (sysTagMatcher.lookingAt()) {
			return present(sysTagMatcher.group(1)) &&   // prefix
			present(sysTagMatcher.group(2)) &&	// type
			present(sysTagMatcher.group(4));	// argument
		}
		return false;
	}
	/**
	 * Returns true if the given tagName looks like a systemTag with Prefix
	 * (i. e. is of the form prefix:type(:argument) where (:argument) ist optional
	 * @param tagName
	 * @return
	 */
	public static boolean hasPrefixAndType(final String tagName) {
		if (!present(tagName)) {
			return false;
		}
		final Matcher sysTagMatcher = SYS_TAG_PATTERN.matcher(tagName);
		if (sysTagMatcher.lookingAt()) {
			return present(sysTagMatcher.group(1)) &&   // prefix
			present(sysTagMatcher.group(2));	// type
		}
		return false;
	}

	public static boolean hasTypeAndArgument(final String tagName) {
		if (!present(tagName)) {
			return false;
		}
		final Matcher sysTagMatcher = SYS_TAG_PATTERN.matcher(tagName);
		if (sysTagMatcher.lookingAt()) {
			return present(sysTagMatcher.group(2)) &&   // type
			present(sysTagMatcher.group(4));	// argument
		}
		return false;
	}

	/** 
	 * The systemTagFactory that manages our registered systemTags
	 */
	public static SystemTagFactory getSystagfactory() {
		return SystemTagFactory.getInstance();
	}
}
