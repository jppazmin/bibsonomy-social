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
import java.util.SortedSet;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.AbstractTagRecommender;
import org.bibsonomy.recommender.tags.popular.MostPopularByResourceTagRecommender;
import org.bibsonomy.recommender.tags.popular.MostPopularByUserTagRecommender;
import org.bibsonomy.services.recommender.TagRecommender;

/**
 * Merges and weights the recommendations of the given recommenders. 
 * 
 *   <p>
 *   The {@link #tagRecommenders} array shall include all the tagrecommenders which should 
 *   be queried. Each recommender's tag scores and confidences are weighted by the corresponding 
 *   value in {@link #weights}.
 *   </p>
 *   
 *   <p>The weights should sum up to 1. If no weights are given, the score (confidence) of each tag 
 *   is multiplied by 1 and added.
 *   
 * 
 * @author rja
 * @version $Id: WeightedMergingTagRecommender.java,v 1.6 2010-07-14 12:04:33 nosebrain Exp $
 */
public class WeightedMergingTagRecommender extends AbstractTagRecommender {

	private TagRecommender[] tagRecommenders;
	private double[] weights;

	/**
	 * Initializes the tag recommenders with a {@link MostPopularByUserTagRecommender} 
	 * and {@link MostPopularByResourceTagRecommender} recommender, giving the first one
	 * a weight of 0.4 and the second one a weight of 0.6.
	 */
	public WeightedMergingTagRecommender() {
		this.tagRecommenders = new TagRecommender[] {
				new MostPopularByUserTagRecommender(),
				new MostPopularByResourceTagRecommender()
		};
		this.weights = new double[] {
				0.4,
				0.6
		};
	}

	@Override
	protected void addRecommendedTagsInternal(final Collection<RecommendedTag> recommendedTags, final Post<? extends Resource> post) {

		if (tagRecommenders == null) {
			throw new IllegalArgumentException("No tag recommenders available.");
		}

		final TopTagsMapBackedSet result = new TopTagsMapBackedSet(numberOfTagsToRecommend);

		/*
		 * iterate over all given recommenders
		 */
		for (int i = 0; i < tagRecommenders.length; i++) {
			final TagRecommender recommender = tagRecommenders[i];
			final double scoreWeight = (weights != null || weights.length == tagRecommenders.length) ? weights[i] : 1;

			final SortedSet<RecommendedTag> tempRecommendedTags = recommender.getRecommendedTags(post);
			/*
			 * iterate over all tags and add them to result
			 */
			for (final RecommendedTag recommendedTag: tempRecommendedTags) {
				addTag(result, recommendedTag, scoreWeight);
			}
		}

		/*
		 * copy result map into sorted set
		 */
		recommendedTags.addAll(result.getTopTags());
	}

	/**
	 * Adds a tag to the result.
	 * 
	 * If the tag is not already contained: multiplies the score and confidence 
	 * of the tag with the weigth and adds the tag to the result.
	 * 
	 * Otherwise, multiplies the score and confidence with the weight and adds 
	 * the result to the score/confidence of the existing tag.
	 * 
	 * @param result - the map into which the recommendedTag should be put 
	 * @param recommendedTag
	 * @param weight the weight the score/confidence of the tag should be weighted with.
	 */
	private void addTag(final TopTagsMapBackedSet result, final RecommendedTag recommendedTag, final double weight) {
		final double score = recommendedTag.getScore() * weight;
		final double confidence = recommendedTag.getConfidence() * weight;
		final String tagName = recommendedTag.getName();

		if (result.contains(recommendedTag)) {
			/*
			 * add score and confidence
			 */
			final RecommendedTag recommendedTag2 = result.get(recommendedTag);
			recommendedTag2.setScore(recommendedTag2.getScore() + score);
			recommendedTag2.setConfidence(recommendedTag2.getConfidence() + confidence);
		} else {
			/*
			 * create new tag with weighted score and confidence
			 */
			result.add(new RecommendedTag(tagName, score, confidence));
		}
	}
	
	@Override
	public String getInfo() {
		return "Merges and weights the recommendations of the given recommenders.";
	}

	/**
	 * @return The weights used to weight the score/confidence of each recommended tag of each recommender.
	 */
	public double[] getWeights() {
		return this.weights;
	}

	/** The score/confidence of each tag from the tag recommenders in {@link #tagRecommenders} is
	 * weighted by the corresponding weight in {@link #weights}. 
	 *  
	 * @param weights
	 */
	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	/**
	 * @return The tag recommenders used by this tag recommender.
	 */
	public TagRecommender[] getTagRecommenders() {
		return this.tagRecommenders;
	}

	/**
	 * Give this recommender an array of tag recommenders it will query.
	 * 
	 * @param tagRecommenders
	 */
	public void setTagRecommenders(TagRecommender[] tagRecommenders) {
		this.tagRecommenders = tagRecommenders;
	}

	@Override
	protected void setFeedbackInternal(Post<? extends Resource> post) {
		for (final TagRecommender reco: tagRecommenders) {
			reco.setFeedback(post);
		}
		
	}
}
