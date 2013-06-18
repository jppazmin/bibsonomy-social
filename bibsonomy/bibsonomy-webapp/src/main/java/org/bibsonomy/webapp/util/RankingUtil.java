/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;

/**
 * Util class to compute ranking
 * 
 * @author dbenz
 * @version $Id: RankingUtil.java,v 1.5 2010-04-26 10:10:35 nosebrain Exp $
 */
public class RankingUtil {
	private static final Log LOGGER = LogFactory.getLog(RankingUtil.class);
	
	/**
	 * the (rough) maximal global count of a tag (used to compute tf/idf-weighting)
	 */
	private static int MAX_TAG_GLOBALCOUNT = 200000;
	
	/**
	 * TODO: improve doc
	 * 
	 * @author dbenz
	 */
	public enum RankingMethod {
		/**
		 * TODO: improve doc
		 */
		TAG_OVERLAP,
		
		/**
		 * TODO: improve doc
		 */
		TFIDF;
	}
	
	/**
	 * Compute the ranking of a list of posts
	 * 
	 * @param <T>
	 * @param sourceUserTags
	 * @param targetUserTags
	 * @param posts
	 * @param rtype 
	 * @param normalize 
	 */
	public static <T extends Resource> void computeRanking(List<Tag> sourceUserTags, List<Tag> targetUserTags, List<Post<T>> posts, RankingMethod rtype, boolean normalize) {
		// first, build map of target user's tags
		Map<String, Integer> tagGlobalCounts = new HashMap<String,Integer>();
		Map<String, Integer> tagUserCounts = new HashMap<String,Integer>();
		int maxUserFreq = 0;
		for (Tag t : sourceUserTags) {
			if (t.getGlobalcount() > 0) {
				tagGlobalCounts.put(t.getName(), t.getGlobalcount());
			}
			tagUserCounts.put(t.getName(), t.getUsercount());
			if (t.getUsercount() > maxUserFreq) {
				maxUserFreq = t.getUsercount();
			}
		}		
		// compute a ranking value for each post
		if (RankingMethod.TFIDF.equals(rtype)) {
			for (Post<T> post : posts) {
				for (Tag tag : post.getTags()) {
					if (tagGlobalCounts.get(tag.getName()) != null  && targetUserTags.contains(tag) ) {					
						post.setRanking( post.getRanking() + ( (tagUserCounts.get(tag.getName()).doubleValue() / maxUserFreq ) * Math.log(MAX_TAG_GLOBALCOUNT / tagGlobalCounts.get(tag.getName()) ) ) );
					}
				}
				// normalize
				if (normalize) {
					post.setRanking(post.getRanking() / post.getTags().size());
				}
			}			
		}
		if (RankingMethod.TAG_OVERLAP.equals(rtype)) {
			for (Post<T> post : posts) {
				for (Tag tag : post.getTags()) {
					if (tagGlobalCounts.get(tag.getName()) != null  && targetUserTags.contains(tag) ) {
						post.setRanking( post.getRanking() + 1);
					}
				}
				// normalize
				if (normalize) {
					post.setRanking(post.getRanking() / post.getTags().size());
				}
			}			
		}						
	}
	
	/**
	 * TODO: impove doc
	 * 
	 * @param <T>
	 * @param sourceUserTags
	 * @param targetUserTags
	 */
	public static <T extends Resource> void computeRanking(List<Tag> sourceUserTags, List<Tag> targetUserTags) {
		// first, build map of target user's tags
		Map<String, Integer> tagGlobalCounts = new HashMap<String,Integer>();
		Map<String, Integer> tagUserCounts = new HashMap<String,Integer>();
		int maxUserFreq = 0;
		for (Tag t : sourceUserTags) {
			if (t.getGlobalcount() > 0) {
				tagGlobalCounts.put(t.getName(), t.getGlobalcount());
			}
			if (t.getUsercount() > 0) {
				tagUserCounts.put(t.getName(), t.getUsercount());
			}
			if (t.getUsercount() > maxUserFreq) {
				maxUserFreq = t.getUsercount();
			}
		}
		// compute the intersection of tags
		targetUserTags.retainAll(sourceUserTags);
		
		// compute the ranking for the intersection
		for (Tag tag : targetUserTags) {
			// double weight = ( ( ( (double) tagUserCounts.get(tag.getName()) ) / maxUserFreq ) * Math.log(MAX_TAG_GLOBALCOUNT / tagGlobalCounts.get(tag.getName()) ) ) * 100 ;
			tag.setGlobalcount(tag.getUsercount());
			LOGGER.debug("working on tag " + tag.getName() + ", having user freq " + tagUserCounts.get(tag.getName()) + " and global count " + tagGlobalCounts.get(tag.getName()));
			if (tagUserCounts.get(tag.getName()) != null && tagGlobalCounts.get(tag.getName()) != null) {
				double weight = ( ( ( (double) tagUserCounts.get(tag.getName()) ) / maxUserFreq ) * Math.log(MAX_TAG_GLOBALCOUNT / tagGlobalCounts.get(tag.getName()) ) ) * 10 ;
				// tag.setGlobalcount((int) weight);				
				tag.setUsercount((int) weight);
			}
			else {
				tag.setUsercount(0);
			}
		}
				
	}
}

