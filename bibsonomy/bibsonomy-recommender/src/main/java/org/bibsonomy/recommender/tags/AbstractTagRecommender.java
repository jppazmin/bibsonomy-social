/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.comparators.RecommendedTagComparator;
import org.bibsonomy.services.recommender.TagRecommender;
import org.bibsonomy.util.TagStringUtils;

/**
 * The basic skeleton to implement a tag recommender.
 * 
 * @author rja
 * @version $Id: AbstractTagRecommender.java,v 1.5 2010-07-14 11:42:29 nosebrain Exp $
 */
public abstract class AbstractTagRecommender implements TagRecommender {
	private static final Log log = LogFactory.getLog(AbstractTagRecommender.class);
	
	private static final int DEFAULT_NUMBER_OF_TAGS_TO_RECOMMEND = 5;
	
	
	/**
	 * The maximal number of tags the recommender shall return on a call to
	 * {@link #getRecommendedTags(Post)}.
	 */
	protected int numberOfTagsToRecommend = DEFAULT_NUMBER_OF_TAGS_TO_RECOMMEND;
	
	/**
	 * Should the recommender return only tags cleaned according to 
	 * {@link TagStringUtils#cleanTag(String)} and removed according to
	 * {@link TagStringUtils#isIgnoreTag(String)}?
	 */
	protected boolean cleanTags = false;

	/**
	 * Returns user's five overall most popular tags
	 * 
	 * @see org.bibsonomy.services.recommender.TagRecommender#getRecommendedTags(org.bibsonomy.model.Post)
	 */
	@Override
	public SortedSet<RecommendedTag> getRecommendedTags(final Post<? extends Resource> post) {
		final SortedSet<RecommendedTag> recommendedTags = new TreeSet<RecommendedTag>(new RecommendedTagComparator());
		this.addRecommendedTags(recommendedTags, post);
		
		return recommendedTags;
	}

	/**
	 * @return The (maximal) number of tags this recommender shall return.
	 */
	public int getNumberOfTagsToRecommend() {
		return this.numberOfTagsToRecommend;
	}

	/** Set the (maximal) number of tags this recommender shall return. The default is {@value #DEFAULT_NUMBER_OF_TAGS_TO_RECOMMEND}.
	 * 
	 * @param numberOfTagsToRecommend
	 */
	public void setNumberOfTagsToRecommend(int numberOfTagsToRecommend) {
		this.numberOfTagsToRecommend = numberOfTagsToRecommend;
	}

	@Override
	public void addRecommendedTags(final Collection<RecommendedTag> recommendedTags, final Post<? extends Resource> post) {
		log.debug("Getting tag recommendations for " + post);
		this.addRecommendedTagsInternal(recommendedTags, post);
		if (log.isDebugEnabled()) log.debug("Recommending tags " + recommendedTags);
	}
	
	protected abstract void addRecommendedTagsInternal(Collection<RecommendedTag> recommendedTags, Post<? extends Resource> post);

	@Override
	public void setFeedback(Post<? extends Resource> post) {
		log.debug("got post with id " + post.getContentId() + " as feedback.");
		this.setFeedbackInternal(post);
	}

	protected abstract void setFeedbackInternal(Post<? extends Resource> post);

	
	/**
	 * @return The current value of cleanTags. Defaults to <code>false</code>.
	 */
	public boolean isCleanTags() {
		return this.cleanTags;
	}

	/**
	 * Should the recommender return only tags cleaned according to 
	 * {@link TagStringUtils#cleanTag(String)} and removed according to
	 * {@link TagStringUtils#isIgnoreTag(String)}?
	 * The default is <code>false</code>
	 * 
	 * @param cleanTags
	 */
	public void setCleanTags(boolean cleanTags) {
		this.cleanTags = cleanTags;
	}
	
	/**
	 * Cleans the tag depending on the setting of {@link #cleanTags}. 
	 * If it is <code>false</code> (default), the tag is returned as is.
	 * If it is <code>true</code>, the tag is cleaned according to {@link TagStringUtils#cleanTag(String)}
	 * and checked agains {@link TagStringUtils#isIgnoreTag(String)}. 
	 * If it should be ignored, <code>null</code> is returned, else the
	 * cleaned tag.
	 * 
	 * This method should be used by all recommenders extending this class before
	 * adding tags to the result set.
	 * 
	 * @param tag 
	 * @return The tag - either cleaned or not, or <code>null</code> if it is
	 * an ignore tag.
	 */
	protected String getCleanedTag(final String tag) {
		if (cleanTags) {
			final String cleanedTag = TagStringUtils.cleanTag(tag);
			if (TagStringUtils.isIgnoreTag(cleanedTag)) {
				return null;
			}
			
			return cleanedTag;
		}
		
		return tag;
	}

}