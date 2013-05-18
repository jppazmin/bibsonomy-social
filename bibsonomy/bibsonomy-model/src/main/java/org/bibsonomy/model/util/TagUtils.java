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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.comparators.TagCountComparator;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.util.tagparser.TagString3Lexer;
import org.bibsonomy.model.util.tagparser.TagString3Parser;
import org.bibsonomy.util.ValidationUtils;

/**
 * @author Dominik Benz
 * @version $Id: TagUtils.java,v 1.22 2011-04-29 06:45:01 bibsonomy Exp $
 */
public class TagUtils {
	private static final Log log = LogFactory.getLog(TagUtils.class); 
	
	private static final Tag emptyTag = new Tag("system:unfiled");
	private static final Tag importedTag = new Tag("imported");
	/** string which is used for separating different tags in a concatenated string */
	private static final String defaultListDelimiter = " ";
	
	/**
	 * Get the maximum user count of all tags contained in a list
	 * 
	 * @param tags a list of tags
	 * @return the maximum user count
	 */
	public static int getMaxUserCount(List<Tag> tags) {
		int maxUserCount = 0;
		for (final Tag tag : tags) {
			if (tag.getUsercount() > maxUserCount) {
				maxUserCount = tag.getUsercount();
			}
		}
		return maxUserCount;
	}

	/**
	 * Get the maximum global count of all tags contained in a list
	 * 
	 * @param tags a list of tags
	 * @return the maximum global count
	 */
	public static int getMaxGlobalcountCount(List<Tag> tags) {
		int maxGlobalCount = 0;
		for (final Tag tag : tags) {
			if (tag.getGlobalcount() > maxGlobalCount) {
				maxGlobalCount = tag.getGlobalcount();
			}
		}
		return maxGlobalCount;
	}
	
	/**
	 * If a post has no tag attached, the empty tag should be added in the TAS table. 
	 * 
	 * @return The empty tag ({@value #emptyTag}).
	 */
	public static Tag getEmptyTag() {
		return emptyTag;
	}
	
	/**
	 * If the corresponding post has been imported
	 * @return The imported tag ({@value #importedTag})
	 */
	public static Tag getImportedTag() {
		return importedTag;
	}
	
	/**
	 * returns the default delimiter for a concatenated list of tags
	 * 
	 * @return default delimiter for a concatenated list of tags
	 */
	public static String getDefaultListDelimiter() {
		return defaultListDelimiter;
	}	
	
	/**
	 * Converts a collection of tags into a string of tags using the given delimiter. 
	 * 
	 * @param tags 
	 * 			- a list of tags
	 * @param delim
	 * 			- a delimiter String by which the tags are to be separated 
	 * @return a delimiter-separated string of tags
	 * 			
	 */
	public static String toTagString (final Collection<Tag> tags, final String delim) {
		// check for special cases
		if (tags == null || tags.size() < 1) {
			return "";
		}
		if (delim == null) {
			throw new RuntimeException("Using NULL as delimiter is not allowed when building tag string.");
		}
		// concat tag names
		final StringBuilder sb = new StringBuilder();
		for (final Tag tag : tags) {
			sb.append(tag.getName());
			sb.append(delim);
		}
		// return string
		if (delim.length() == 0) {
			return sb.toString();
		}
		return sb.delete(sb.length() - delim.length(), sb.length()).toString();
	}
	
	/**
	 * Parses the incoming tag string and returns a set of tags
	 * 
	 * @param tagString
	 * @return a set of tags
	 * @throws RecognitionException
	 */
	public static Set<Tag> parse(final String tagString) throws RecognitionException {
		final Set<Tag> tags = new TreeSet<Tag>();

		if (tagString != null) {
			/*
			 * prepare parser
			 */
			final CommonTokenStream tokens = new CommonTokenStream();
			tokens.setTokenSource(new TagString3Lexer(new ANTLRStringStream(tagString)));
			final TagString3Parser parser = new TagString3Parser(tokens, tags);
			/*
			 * parse
			 */
			parser.tagstring();
		}
		return tags;
	}	
	
	/**
	 * Merges two given lists of tags into one, adding tag counts where necessary, 
	 * limiting resulting list according to given parameters
	 * 
	 * FIXME: sub-tags, etc. are ignored!!!
	 * FIXME: tags are not sorted 
	 * 
	 * @param src1 a list of tags to merge
	 * @param src2 an other list of tags to merge
	 * @param tagOrder NOT IMPLEMENTED
	 * @param limitType POPULAR or FREQUENCY  
	 * @param limit number of popular tags to return or minimum frequency
	 * 
	 * @return either the top n tags of the merged list (if limitType==POPULAR)
	 *         or all tags with globalTagCount>=limit (if limitType==FREQUENCY)
	 */
	public static List<Tag> mergeTagLists(final List<Tag> src1, final List<Tag> src2, Order tagOrder, Order limitType, int limit) {
		if( Order.POPULAR.equals(limitType)) {
			return mergePopularityFilteredTagLists(src1, src2, tagOrder, limit);
		}
		
		return mergeFrequencyFilteredTagLists(src1, src2, tagOrder, limit);
	}
	
	/**
	 * Merges two given lists of tags into one, adding tag counts where necessary 
	 * and returns only those tags with globalcount >= given limit
	 * 
	 * FIXME: sub-tags, etc. are ignored!!!
	 * FIXME: tags are not sorted 
	 * 
	 * @param src1 a list of tags to merge
	 * @param src2 an other list of tags to merge
	 * @param tagOrder NOT IMPLEMENTED
	 * @param limit minimum frequency
	 * 
	 * @return all tags with globalTagCount>=limit
	 */
	private static List<Tag> mergeFrequencyFilteredTagLists(final List<Tag> src1, final List<Tag> src2, Order tagOrder, int limit ) {
		List<Tag> mergedList = new LinkedList<Tag>();
		
		log.debug("Merging tag lists ("+src1.size()+"/"+src2.size()+")and filter by minFreq");

		// collect tags from first tag list
		Map<String,Tag> tagCollector = new HashMap<String, Tag>();
		for( Tag t : src1 ) {
			tagCollector.put(t.getName(), t);
		}
		// add tags from second list, adding corresponding counts on collisions
		for( Tag t : src2 ) {
			Tag oldTag = tagCollector.remove(t.getName());
			if( ValidationUtils.present(oldTag) ) {
				t.setGlobalcount(t.getGlobalcount() + oldTag.getGlobalcount());
				t.setUsercount(t.getUsercount() + oldTag.getUsercount());
			}
			if( t.getGlobalcount()>=limit ) {
				mergedList.add(t);
			} 
		}
		
		// add all tags from src1\src2
		for( Map.Entry<String, Tag> entry : tagCollector.entrySet() ) {
			if( entry.getValue().getGlobalcount()>=limit ) {
				mergedList.add(entry.getValue());
			} 
		}
		
		log.debug("Done merging lists.");
		// all done
		return mergedList;
	}

	/**
	 * Merges two given lists of tags into one, adding tag counts where necessary,
	 * returning only the top n popular tags 
	 * 
	 * FIXME: sub-tags, etc. are ignored!!!
	 * FIXME: tags are not sorted 
	 * 
	 * @param src1 a list of tags to merge
	 * @param src2 an other list of tags to merge
	 * @param tagOrder NOT IMPLEMENTED
	 * @param limit number of popular tags to return
	 * 
	 * @return the top n tags of the merged list 
	 */
	private static List<Tag> mergePopularityFilteredTagLists(final List<Tag> src1, final List<Tag> src2, Order tagOrder, int limit ) {
		List<Tag> mergedList = new LinkedList<Tag>();
		
		log.debug("Merging tag lists ("+src1.size()+"/"+src2.size()+")and filter by popularity");
		
		// collect tags from first tag list
		Map<String,Tag> tagCollector = new HashMap<String, Tag>();
		for( Tag t : src1 ) {
			tagCollector.put(t.getName(), t);
		}
		// add tags from second list, adding corresponding counts on collisions
		for( Tag t : src2 ) {
			Tag oldTag = tagCollector.remove(t.getName());
			if( ValidationUtils.present(oldTag) ) {
				t.setGlobalcount(t.getGlobalcount() + oldTag.getGlobalcount());
				t.setUsercount(t.getUsercount() + oldTag.getUsercount());
			}
			mergedList.add(t);
		}
		
		// add all tags from src1\src2
		for( Map.Entry<String, Tag> entry : tagCollector.entrySet() ) {
			mergedList.add(entry.getValue());
		}

		
		// sort tags according to tag counts
		log.debug("Sorting tags...");
		Collections.sort(mergedList, new TagCountComparator());
		log.debug("Done sorting tags.");
		
		// all done
		return mergedList.subList(0, Math.min(mergedList.size(), limit));
	}
}