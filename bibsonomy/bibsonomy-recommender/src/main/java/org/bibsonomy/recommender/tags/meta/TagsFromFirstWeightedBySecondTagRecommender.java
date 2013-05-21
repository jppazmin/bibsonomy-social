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
import java.util.Iterator;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.AbstractTagRecommender;
import org.bibsonomy.services.recommender.TagRecommender;

/**
 * Takes the tags from {@link #firstTagRecommender} and orders them by their 
 * scores from {@link #secondTagRecommender}. If they're not recommended by 
 * {@link #secondTagRecommender}, they get a lower score. If 
 * {@link #firstTagRecommender} can't deliver enough tags, they're filled up 
 * with the top tags from {@link #secondTagRecommender}.
 * 
 * @author rja
 * @version $Id: TagsFromFirstWeightedBySecondTagRecommender.java,v 1.12 2010-07-14 12:04:33 nosebrain Exp $
 */
public class TagsFromFirstWeightedBySecondTagRecommender extends AbstractTagRecommender {
	private static final Log log = LogFactory.getLog(TagsFromFirstWeightedBySecondTagRecommender.class);

	protected TagRecommender firstTagRecommender;
	protected TagRecommender secondTagRecommender;

	/**
	 * Initializes the recommender with the given recommenders.
	 * 
	 * @param firstTagRecommender
	 * @param secondTagRecommender
	 */
	public TagsFromFirstWeightedBySecondTagRecommender(TagRecommender firstTagRecommender, TagRecommender secondTagRecommender) {
		super();
		this.firstTagRecommender = firstTagRecommender;
		this.secondTagRecommender = secondTagRecommender;
	}
	
	/**
	 * Don't initializes any recommenders - you have to call the setters!
	 */
	public TagsFromFirstWeightedBySecondTagRecommender() {
		super();
	}
	
	@Override
	protected void addRecommendedTagsInternal(final Collection<RecommendedTag> recommendedTags, final Post<? extends Resource> post) {

		if (firstTagRecommender == null || secondTagRecommender == null) {
			throw new IllegalArgumentException("No tag recommenders available.");
		}

		/*
		 * Get recommendation from first recommender.
		 */
		final SortedSet<RecommendedTag> firstRecommendedTags = firstTagRecommender.getRecommendedTags(post);
		log.debug("got " + firstRecommendedTags.size() + " recommendations from " + firstTagRecommender);
		if (log.isDebugEnabled()) {
			log.debug(firstRecommendedTags);
		}

		/*
		 * Get recommendation from second tag recommender.
		 * 
		 * Since we need to get the scores from this recommender for the tags from the first
		 * recommender, we use the TopTagsMapBackedSet, such that we can easily get tags by their name.
		 * Additionally, we might need to fill up the tags with the top tags (according to 
		 * the RecommendedTagComparator) from the second recommender. We get those from the 
		 * TopTagsMapBackedSet, too. 
		 */
		final TopTagsMapBackedSet secondRecommendedTags = new TopTagsMapBackedSet(numberOfTagsToRecommend);
		secondTagRecommender.addRecommendedTags(secondRecommendedTags, post);
		log.debug("got " + secondRecommendedTags.size() + " recommendations from " + secondTagRecommender);

		/*
		 * The scores from the tags in the next 'fill up round' should be lower 
		 * as the scores from this 'round'. Thus, we find the smallest value 
		 */
		final double minScore = doFirstRound(recommendedTags, firstRecommendedTags, secondRecommendedTags); 
		log.debug("used " + recommendedTags.size() + " tags from the first recommender which occured in second recommender");


		final int ctr = doSecondRound(recommendedTags, firstRecommendedTags, secondRecommendedTags, minScore); 
		log.debug("used another " + ctr + " tags from the first recommender ");		

		
		/*
		 * If we have not enough tags, yet, add tags from second until set is complete.
		 */
		if (recommendedTags.size() < numberOfTagsToRecommend) {
			/*
			 * we want to get the top tags, not ordered alphabetically!
			 */
			final SortedSet<RecommendedTag> topRecommendedTags = secondRecommendedTags.getTopTags();
			doThirdRound(recommendedTags, topRecommendedTags, minScore, recommendedTags.size());
		}
		if (log.isDebugEnabled()) {
			log.debug("final recommendation: " + recommendedTags);
		}

	}


	protected double doFirstRound(final Collection<RecommendedTag> recommendedTags, final SortedSet<RecommendedTag> firstRecommendedTags, final MapBackedSet<String, RecommendedTag> secondRecommendedTags) {
		/* 
		 * First round:
		 * Iterate over tags from first recommender and check them against second recommender.
		 * Add only those tags, which are contained in the second recommender
		 */
		final Iterator<RecommendedTag> iterator1 = firstRecommendedTags.iterator();
		/*
		 * We need to find the minimum to add the remaining tags with lower scores
		 */
		double minScore = Double.MAX_VALUE;
		while (recommendedTags.size() < numberOfTagsToRecommend && iterator1.hasNext()) {
			final RecommendedTag recommendedTag = iterator1.next();
			if (secondRecommendedTags.contains(recommendedTag)) {
				/*
				 * this tag is also recommended by the second recommender: give it his score
				 */

				final RecommendedTag secondRecommendedTag = secondRecommendedTags.get(recommendedTag);
				recommendedTags.add(secondRecommendedTag);
				/*
				 * remember minimal score
				 */
				final double score = secondRecommendedTag.getScore();
				if (score < minScore) minScore = score;
				/*
				 * remove tag, such that don't use it again in the second round
				 */
				iterator1.remove();
			}
		}
		/*
		 * We would like to have values not larger than 1.0 ... but basically
		 * this prevents to have minScore = Double.MAX_VALUE, when no tags from
		 * the first recommender were recommended by the second recommender.
		 */
		if (minScore > 1.0) minScore = 1.0; 
		return minScore;
	}

	protected int doSecondRound(final Collection<RecommendedTag> recommendedTags, final SortedSet<RecommendedTag> firstRecommendedTags, final MapBackedSet<String, RecommendedTag> secondRecommendedTags, final double minScore) {
		/*
		 * Second round:
		 * add remaining tags from first recommender, scored lower than the tags before
		 */
		final Iterator<RecommendedTag> iterator2 = firstRecommendedTags.iterator();
		int ctr = 0;
		while (recommendedTags.size() < numberOfTagsToRecommend && iterator2.hasNext()) {
			final RecommendedTag recommendedTag = iterator2.next();
			ctr++;
			recommendedTag.setScore(getLowerScore(minScore, ctr));
			recommendedTags.add(recommendedTag);
		}
		return ctr;
	}

	protected int doThirdRound(final Collection<RecommendedTag> recommendedTags, final SortedSet<RecommendedTag> thirdRecommendedTags, final double minScore, final int ctr) {
		/*
		 * Third round:
		 * If we have not enough tags, yet, add tags from third recommender until set is complete.
		 */
		int myCtr = ctr;
		final Iterator<RecommendedTag> iterator3 = thirdRecommendedTags.iterator();
		while (recommendedTags.size() < numberOfTagsToRecommend && iterator3.hasNext()) {
			final RecommendedTag recommendedTag = iterator3.next();
			if (!recommendedTags.contains(recommendedTag)) {
				/*
				 * tag has not already been added -> set its score lower than min
				 */
				myCtr++;
				recommendedTag.setScore(getLowerScore(minScore, myCtr));
				recommendedTags.add(recommendedTag);
			}
		}
		return myCtr;

	}
	

	/**
	 * Goal of this method: "append" not so good tags on already recommended ("good") tags 
	 * by ensuring that their score is lower than the "good" tags.
	 * 
	 * Depending on the sign of the min score of the already recommended tags, we apply
	 * a strategy to use the ctr as score. 
	 * 
	 * @param minScore
	 * @param ctr
	 * @return
	 */
	private double getLowerScore(double minScore, int ctr) {
		final double newScore;
		if (minScore > 0) {
			/*
			 * go closer to zero (and don't do 'min/1 = min', thus '/ctr + 1')
			 */
			newScore = minScore / (ctr + 1);
		} else {
			/*
			 * go closer to -infinity
			 */
			newScore = minScore - ctr;
		}
		return newScore;
	}
	
	@Override
	public String getInfo() {
		return "Using the tags from the second recommender to weight the recommended tags from the first recommender.";
	}

	/**
	 * @return The first tag recommender.
	 */
	public TagRecommender getFirstTagRecommender() {
		return this.firstTagRecommender;
	}


	/** This tag recommender's tags are ordered by their respective score
	 * from the second tag recommender. 
	 * 
	 * @param firstTagRecommender
	 */
	public void setFirstTagRecommender(TagRecommender firstTagRecommender) {
		this.firstTagRecommender = firstTagRecommender;
	}


	/**
	 * @return The second tag recommender.
	 */
	public TagRecommender getSecondTagRecommender() {
		return this.secondTagRecommender;
	}


	/**
	 * The scores of this recommender are used to weight the tags from the first
	 * tag recommender.
	 *  
	 * @param secondTagRecommender
	 */
	public void setSecondTagRecommender(TagRecommender secondTagRecommender) {
		this.secondTagRecommender = secondTagRecommender;
	}

	@Override
	protected void setFeedbackInternal(Post<? extends Resource> post) {
		this.firstTagRecommender.setFeedback(post);
		this.secondTagRecommender.setFeedback(post);
	}
}
