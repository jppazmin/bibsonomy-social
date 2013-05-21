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

package org.bibsonomy.recommender.tags.simple;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.comparators.RecommendedTagComparator;
import org.bibsonomy.services.recommender.TagRecommender;

/**
 * Always recommends the tags given in the constructor.
 * 
 * @author rja
 * @version $Id: FixedTagsTagRecommender.java,v 1.4 2009-06-22 12:52:28 rja Exp $
 */
public class FixedTagsTagRecommender implements TagRecommender {


	private SortedSet<RecommendedTag> tags;

	/**
	 * Adds the given tags to the fixed set of tags, ordered by their 
	 * occurrence in the arrays.
	 * 
	 * @param tags
	 */
	public FixedTagsTagRecommender(final String[] tags) {
		this.tags = new TreeSet<RecommendedTag>(new RecommendedTagComparator());
		for (int i = 0; i < tags.length; i++) {
			this.tags.add(new RecommendedTag(tags[i], 1.0 / (i + 1.0), 0.0));
		}
	}
	
	/**
	 * 
	 * @param tags - the tags this recommender will recommend.
	 */
	public FixedTagsTagRecommender(final SortedSet<RecommendedTag> tags) {
		super();
		this.tags = tags;
	}

	@Override
	public void addRecommendedTags(Collection<RecommendedTag> recommendedTags, Post<? extends Resource> post) {
		recommendedTags.addAll(tags);
	}

	@Override
	public String getInfo() {
		return "A simple recommender with a fixed set of tags.";
	}

	@Override
	public SortedSet<RecommendedTag> getRecommendedTags(Post<? extends Resource> post) {
		return tags;
	}

	@Override
	public void setFeedback(Post<? extends Resource> post) {
		/*
		 * ignored
		 */
	}
}
