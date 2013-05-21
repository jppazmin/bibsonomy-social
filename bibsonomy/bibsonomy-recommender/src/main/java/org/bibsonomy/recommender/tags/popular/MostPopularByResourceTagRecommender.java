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

package org.bibsonomy.recommender.tags.popular;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.AbstractTagRecommender;
import org.bibsonomy.recommender.tags.database.DBLogic;
import org.bibsonomy.recommender.tags.database.params.Pair;

/**
 * Returns the most popular (i.e., most often attached) tags of the resource as 
 * recommendation for the post.  
 * 
 * @author fei
 * @version $Id: MostPopularByResourceTagRecommender.java,v 1.15 2010-07-14 11:44:27 nosebrain Exp $
 */
public class MostPopularByResourceTagRecommender extends AbstractTagRecommender {
	private static final Log log = LogFactory.getLog(MostPopularByResourceTagRecommender.class);

	
	private DBLogic dbLogic;
	
	@Override
	protected void addRecommendedTagsInternal(final Collection<RecommendedTag> recommendedTags, final Post<? extends Resource> post) {

		final Resource resource = post.getResource();
		/*
		 * we have to call recalculateHashes() first, otherwise the intraHash is not available
		 */
		resource.recalculateHashes();

		final String intraHash = resource.getIntraHash();

		if (intraHash != null) {
			try {
				/*
				 * we get the count to normalize the score
				 */
				final int count = dbLogic.getNumberOfTasForResource(resource.getClass(), intraHash);
				log.debug("Resource has " + count + " TAS.");

				final List<Pair<String,Integer>> tagsWithCount = dbLogic.getMostPopularTagsForResource(resource.getClass(), intraHash, numberOfTagsToRecommend);
				if (tagsWithCount != null && !tagsWithCount.isEmpty()) {
					for (final Pair<String,Integer> tagWithCount : tagsWithCount) {
						final String tag = getCleanedTag(tagWithCount.getFirst());
						if (tag != null) {
							recommendedTags.add(new RecommendedTag(tag, ((1.0 * tagWithCount.getSecond()) / count), 0.5));
						}
					}
					log.debug("Returning the tags " + recommendedTags);
				} else {
					log.debug("Resource not found or no tags available.");
				}
			} catch (SQLException ex) {
				log.error("Error getting recommendations for resource " + resource, ex);
			}
		} else {
			log.debug("Could not get recommendations, because no intraHash was given.");
		}
	}
	
	@Override
	public String getInfo() {
		return "Most Popular Tags By Resource Recommender";
	}

	/**
	 * @return the dbLogic
	 */
	public DBLogic getDbLogic() {
		return this.dbLogic;
	}

	/**
	 * @param dbLogic the dbLogic to set
	 */
	public void setDbLogic(DBLogic dbLogic) {
		this.dbLogic = dbLogic;
	}

	@Override
	protected void setFeedbackInternal(Post<? extends Resource> post) {
		/*
		 * this recommender ignores feedback
		 */
	}
}
