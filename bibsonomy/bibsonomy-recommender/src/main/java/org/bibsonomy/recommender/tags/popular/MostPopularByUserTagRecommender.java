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
 * Returns the most popular (i.e., most often used) tags of the user as 
 * recommendation for the post.  
 * 
 * @author fei
 * @version $Id: MostPopularByUserTagRecommender.java,v 1.16 2010-07-14 11:44:27 nosebrain Exp $
 */
public class MostPopularByUserTagRecommender extends AbstractTagRecommender {
	private static final Log log = LogFactory.getLog(MostPopularByUserTagRecommender.class);
	
	private DBLogic dbLogic;
	
	@Override
	protected void addRecommendedTagsInternal(final Collection<RecommendedTag> recommendedTags, final Post<? extends Resource> post) {
		final String username = post.getUser().getName();
		if (username != null) {
			try {
				/*
				 * we get the count to normalize the score
				 */
				final int count = dbLogic.getNumberOfTasForUser(username);
				
				final List<Pair<String, Integer>> tagsWithCount = dbLogic.getMostPopularTagsForUser(username, numberOfTagsToRecommend);
				for (final Pair<String, Integer> tagWithCount : tagsWithCount) {
					final String tag = getCleanedTag(tagWithCount.getFirst());
					if (tag != null) {
						recommendedTags.add(new RecommendedTag(tag, ((1.0 * tagWithCount.getSecond()) / count), 0.5));
					}
				}
			} catch (SQLException ex) {
				log.error("Error getting recommendations for user " + username, ex);
			}
		}
	}

	@Override
	public String getInfo() {
		return "Most Popular Tags By User Recommender";
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
