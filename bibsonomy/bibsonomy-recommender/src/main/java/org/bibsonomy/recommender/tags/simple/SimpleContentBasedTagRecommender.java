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
import java.util.Iterator;
import java.util.Scanner;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.AbstractTagRecommender;
import org.bibsonomy.recommender.tags.simple.termprocessing.TermProcessingIterator;

/**
 * Extracts tags from the title of the post. Cleans the words using a stopword list
 * and the cleanTag method according to the Discovery Challenge. Stops, when it has
 * found enough proper tags.
 * 
 * @see TermProcessingIterator
 * @author rja
 * @version $Id: SimpleContentBasedTagRecommender.java,v 1.14 2010-07-14 11:38:29 nosebrain Exp $
 */
public class SimpleContentBasedTagRecommender extends AbstractTagRecommender {
	
	/** Simply adds tags from the post's title to the given collection. The score of each tag
	 * is its inverse position in the title, such that tags coming earlier will have a higher
	 * score. 
	 * 
	 * @see org.bibsonomy.services.recommender.TagRecommender#addRecommendedTags(java.util.Collection, org.bibsonomy.model.Post)
	 */
	@Override
	protected void addRecommendedTagsInternal(Collection<RecommendedTag> recommendedTags, Post<? extends Resource> post) {
		final String title = post.getResource().getTitle();
		if (title != null) {
			/*
			 * extract tags from title using Jens' Termprocessor.
			 */

			final Iterator<String> extractor = buildTagExtractionIterator(title);
			/*
			 * add extracted tags (not more than numberOfTagsToRecommend)
			 */
			int ctr = 0;
			while(extractor.hasNext() == true && ctr < numberOfTagsToRecommend) {
				final String tag = getCleanedTag(extractor.next());
				if (tag != null) {
					ctr++;
					/*
					 * add one to not get 1.0 as score 
					 */
					recommendedTags.add(new RecommendedTag(tag, 1.0 / (ctr + 1.0), 0.0));
				}
			}
		}
	}
	
	@Override
	public String getInfo() {
		return "Simple content based recommender which extracts tags from title, description, URL.";
	}

	private Iterator<String> buildTagExtractionIterator(final String title) {
		final Scanner s = new Scanner(title);
		s.useDelimiter("([\\|/\\\\ \t;!,\\-:\\)\\(\\]\\[\\}\\{]+)|(\\.[\\t ]+)");
		return new TermProcessingIterator(s);
	}

	@Override
	protected void setFeedbackInternal(Post<? extends Resource> post) {
		/*
		 * ignored
		 */
	}

}
