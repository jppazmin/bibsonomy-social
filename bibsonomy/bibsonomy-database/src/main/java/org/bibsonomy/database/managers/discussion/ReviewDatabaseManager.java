/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.managers.discussion;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.discussion.DiscussionItemParam;
import org.bibsonomy.database.params.discussion.ReviewParam;
import org.bibsonomy.model.Review;
import org.bibsonomy.model.User;


/**
 * database manager for crud reviews
 * 
 * @author dzo
 * @version $Id: ReviewDatabaseManager.java,v 1.3 2011-07-14 12:41:33 nosebrain Exp $
 */
public class ReviewDatabaseManager extends DiscussionItemDatabaseManager<Review> {
	
	private static final ReviewDatabaseManager INSTANCE = new ReviewDatabaseManager();

	/**
	 * @return the @{link:ReviewDatabaseManager} instance
	 */
	public static ReviewDatabaseManager getInstance() {
		return INSTANCE;
	}

	private ReviewDatabaseManager() {
		// only call super
	}
	
	/** 
	 * check rating and only length of all properties
	 * 
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.discussion.DiscussionItemDatabaseManager#checkDiscussionItem(org.bibsonomy.model.DiscussionItem, DBSession)
	 */
	@Override
	protected void checkDiscussionItem(final Review review, final DBSession session) {
		final double rating = review.getRating();
		
		if (Double.compare(rating, Review.MIN_REVIEW_RATING) < 0 || Double.compare(rating, Review.MAX_REVIEW_RATING) > 0) {
			throw new ValidationException("Review rating not in range"); // TODO: error message?!
		}
		
		final double decimal = Math.abs(rating - Math.rint(rating));
		
		if (Double.compare(decimal, 0) != 0 && Double.compare(decimal - 0.5, 0) != 0) {
			throw new ValidationException("Only ?.0 and ?.5 ratings are supported"); // TODO: error message?
		}
		
		if (present(review.getText()) && review.getText().length() > Review.MAX_TEXT_LENGTH) {
			throw new ValidationException("review text too long");
		}
	}
	
	@Override
	protected void handleDiscussionItemDelete(final String interHash, final User user, final Review oldComment, final DBSession session) {
		final ReviewParam reviewParam = this.createReviewParam(interHash, user.getName());
		reviewParam.setDiscussionItem(oldComment);
		
		/*
		 * update rating cache
		 */
		if (!user.isSpammer()) {
			this.update("updateReviewRatingsCacheDelete", reviewParam, session);
		}
	}	
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.database.managers.discussion.DiscussionItemDatabaseManager#preCheckDiscussionItem(java.lang.String, org.bibsonomy.model.DiscussionItem, org.bibsonomy.database.common.DBSession)
	 */
	@Override
	protected void checkDiscussionItemOnCreate(final String interHash, final Review review, final DBSession session) {
		this.checkDiscussionItem(review, session);
		
		if (present(review.getParentHash())) {
			throw new ValidationException("you can't reply to a discussion item with a review");
		}
		
		/*
		 * check if the user already reviewed the resource
		 */
		final String userName = review.getUser().getName();
		final Review oldReview = this.getReviewForPostAndUser(interHash, userName, session);
		if (present(oldReview)) {
			throw new ValidationException("user already reviewed resource '" + interHash + "'");
		}
	}

	@Override
	protected void discussionItemCreated(final String interHash, final Review review, final DBSession session) {
		/* 
		 * update ratings cache only if the user isn't a spammer
		 */
		final ReviewParam param = this.createReviewParam(interHash, review.getUser().getName());
		param.setDiscussionItem(review);
		if (!review.getUser().isSpammer()) {
			insert("updateReviewRatingsCacheInsert", param, session);
		}
	}
	
	@Override
	protected void discussionItemUpdated(final String interHash, final Review review, final Review oldReview, final DBSession session) {
		final String username = review.getUser().getName();		
		final ReviewParam param = this.createReviewParam(interHash, username);
		param.setDiscussionItem(review);
		/*
		 * only update cache if user wasn't a spammer
		 */
		if (!review.getUser().isSpammer()) {
			this.insert("updateReviewRatingsCacheInsert", param, session);
			
			param.setDiscussionItem(oldReview);
			this.update("updateReviewRatingsCacheDelete", param, session);
		}
	}
	
	protected Review getReviewForPostAndUser(final String interHash, final String username, final DBSession session) {
		final ReviewParam param = new ReviewParam();
		this.fillDiscussionItemParam(param, interHash, username);
		return this.queryForObject("getReviewForHashAndUser", param, Review.class, session);
	}
	
	private ReviewParam createReviewParam(final String interHash, final String username) {
		final ReviewParam param = new ReviewParam();
		this.fillDiscussionItemParam(param, interHash, username);
		return param;
	}

	@Override
	protected DiscussionItemParam<Review> createDiscussionItemParam() {
		return new ReviewParam();
	}
}
