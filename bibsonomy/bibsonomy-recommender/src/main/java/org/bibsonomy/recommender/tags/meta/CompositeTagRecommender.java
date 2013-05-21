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

package org.bibsonomy.recommender.tags.meta;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.services.recommender.TagRecommender;

/**
 * @author rja
 * @version $Id: CompositeTagRecommender.java,v 1.3 2010-07-14 12:04:33 nosebrain Exp $
 */
public class CompositeTagRecommender implements TagRecommender {

	private final List<TagRecommender> recommender = new LinkedList<TagRecommender>();
	private final Comparator<RecommendedTag> comparator;
	
	/** Create a new instance of this class. The comparator is necessary to fill the
	 * SortedSet in {@link #getRecommendedTags(Post)}. 
	 * 
	 * @param comparator
	 */
	public CompositeTagRecommender(final Comparator<RecommendedTag> comparator) {
		this.comparator = comparator;
	}
	
	@Override
	public SortedSet<RecommendedTag> getRecommendedTags(Post<? extends Resource> post) {
		final SortedSet<RecommendedTag> recommendedTags = new TreeSet<RecommendedTag>(comparator);
		addRecommendedTags(recommendedTags, post);
		return recommendedTags;
	}

	@Override
	public String getInfo() {
		return "Generic composite scraper.";
	}

	/** Adds a tag recommender to the list of recommenders.
	 *  
	 * @param tagRecommender
	 */
	public void addTagRecommender(final TagRecommender tagRecommender) {
		this.recommender.add(tagRecommender);
	}

	@Override
	public void addRecommendedTags(Collection<RecommendedTag> recommendedTags, Post<? extends Resource> post) {
		for (final TagRecommender t: recommender) {
			t.addRecommendedTags(recommendedTags, post);
		}
	}

	@Override
	public void setFeedback(Post<? extends Resource> post) {
		for (final TagRecommender t: recommender) {
			t.setFeedback(post);
		}
		
	}

}
