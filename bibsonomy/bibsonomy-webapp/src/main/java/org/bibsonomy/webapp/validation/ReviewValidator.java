/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.validation;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.model.Review;
import org.springframework.validation.Errors;


/**
 * validator for reviews
 * validates: 
 * 		- text length
 * 		- rating
 * 
 * @author dzo
 * @version $Id: ReviewValidator.java,v 1.3 2011-06-27 16:00:23 nosebrain Exp $
 */
public class ReviewValidator extends DiscussionItemValidator<Review> {
	
	@Override
	protected void validateDiscussionItem(final Review review, final Errors errors) {
		final double rating = review.getRating();
		if (Double.compare(rating, Review.MIN_REVIEW_RATING) < 0 || Double.compare(rating, Review.MAX_REVIEW_RATING) > 0) {
			errors.rejectValue(DISCUSSION_ITEM_PATH + "rating", "error.field.valid.review.rating.range", new Object[] { Review.MIN_REVIEW_RATING, Review.MAX_REVIEW_RATING }, "Only ratings between " + Review.MIN_REVIEW_RATING  + " and " + Review.MAX_REVIEW_RATING + " are allowed.");
		} else {
			final double decimal = Math.abs(rating - Math.rint(rating));
		
			/*
			 * only x.0 and x.5 ratings are allowed
			 */
			if (Double.compare(decimal, 0) != 0 && Double.compare(decimal - 0.5, 0) != 0) {
				errors.rejectValue(DISCUSSION_ITEM_PATH + "rating", "error.field.valid.review.rating.decimal");
			}
		}
		final String text = review.getText();
		if (present(text) && text.length() > Review.MAX_TEXT_LENGTH) {
			errors.rejectValue(DISCUSSION_ITEM_PATH + "text", "error.field.valid.comment.text.length", new Object[] { Review.MAX_TEXT_LENGTH }, "The text is too long. Only " + Review.MAX_TEXT_LENGTH + " characters allowed.");
		}
	}
}
