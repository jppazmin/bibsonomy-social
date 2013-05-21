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

package org.bibsonomy.recommender.tags.multiplexer.modifiers;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collection;

import org.bibsonomy.model.RecommendedTag;

/**
 * Replaces invalid scoring values:
 *    *  NaN : Integer.MIN_VALUE
 *    * -inf : Integer.MIN_VALUE
 *    * +inf : Integer.MAX_VALUE
 * @author fei
 * @version $Id: InvalidScoringFilter.java,v 1.2 2009-11-19 13:28:12 nosebrain Exp $
 */
public class InvalidScoringFilter implements RecommendedTagModifier {

	@Override
	public void alterTags(Collection<RecommendedTag> tags) {
		if (present(tags)) {
			for (final RecommendedTag tag : tags) {
				double score      = tag.getScore();
				double confidence = tag.getConfidence();
				
				// filter score
				if (Double.isNaN(score)) {
					tag.setScore(Integer.MIN_VALUE);
				} else if( score==Double.NEGATIVE_INFINITY ) {
					tag.setScore(Integer.MIN_VALUE);
				} else if( score==Double.POSITIVE_INFINITY ) {
					tag.setScore(Integer.MAX_VALUE);
				}

				// filter confidence
				if( Double.isNaN(confidence) ) {
					tag.setConfidence(Integer.MIN_VALUE);
				} else if( confidence==Double.NEGATIVE_INFINITY ) {
					tag.setConfidence(Integer.MIN_VALUE);
				} else if( confidence==Double.POSITIVE_INFINITY ) {
					tag.setConfidence(Integer.MAX_VALUE);
				}
			}
		}
	}
}
