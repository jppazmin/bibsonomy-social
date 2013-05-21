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

import java.util.SortedSet;
import java.util.TreeSet;

import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.comparators.RecommendedTagComparator;

/**
 * Works like {@link MapBackedSet} but additionally keeps a set of the top 
 * number of tags (according to {@link RecommendedTagComparator}). 
 * Thus, doesn't support the {@link #remove(Object)} method, since we could
 * not 'refill' the top tags after removal of one. Uses the tags name to compare them.
 * 
 * 
 * @author rja
 * @version $Id: TopTagsMapBackedSet.java,v 1.2 2009-04-08 14:14:54 rja Exp $
 */
public class TopTagsMapBackedSet extends MapBackedSet<String, RecommendedTag> {

	private final SortedSet<RecommendedTag> sortedTags;
	private final int numberOfTags;
	private final RecommendedTagComparator comp;

	/**
	 * @param numberOfTags - maximal number of top tags to keep.
	 */
	public TopTagsMapBackedSet(final int numberOfTags) {
		super(new DefaultKeyExtractor());
		this.sortedTags = new TreeSet<RecommendedTag>(new RecommendedTagComparator());
		this.numberOfTags = numberOfTags;
		this.comp = new RecommendedTagComparator();
	}

	@Override
	public boolean add(RecommendedTag e) {
		addToSortedSet(e);
		return super.add(e);
	}

	/**
	 * Adds the tag to the sorted set, if the sorted set is smaller than 
	 * {@link #numberOfTags} or if the tag is larger (according to
	 * {@link RecommendedTagComparator} than the last tag (which is then
	 * removed).
	 * 
	 * @param e
	 */
	private void addToSortedSet(final RecommendedTag e) {
		if (sortedTags.size() < numberOfTags) {
			sortedTags.add(e);
		} else if (this.comp.compare(e, sortedTags.last()) < 0) {
			/*
			 * new tag is better than last -> replace it!
			 */
			sortedTags.remove(sortedTags.last());
			sortedTags.add(e);
		}
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("remove() is not supported by this set.");
	}

	/**
	 * @return The top tags sorted by {@link RecommendedTagComparator}. 
	 */
	public SortedSet<RecommendedTag> getTopTags() {
		return this.sortedTags;
	}


	/**
	 * Uses the name of a recommended tag as key for the map backed set.
	 * @author rja
	 *
	 */
	static class DefaultKeyExtractor implements MapBackedSet.KeyExtractor<String, RecommendedTag> {
		@Override
		public String getKey(RecommendedTag value) {
			return value.getName();
		}
	}

}
