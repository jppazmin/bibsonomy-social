/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model;

/**
 * @author dzo
 * @version $Id: Review.java,v 1.11 2011-07-26 08:30:33 bibsonomy Exp $
 */
public class Review extends DiscussionItem {
	
	/**
	 * the max value of a review rating
	 */
	public static double MAX_REVIEW_RATING = 5;
	
	/**
	 * the min value of a review rating
	 */
	public static double MIN_REVIEW_RATING = 0;
	
	/**
	 * the max text length of a review
	 */
	public static int MAX_TEXT_LENGTH = 15000;
	
	/**
	 * rating from MIN to MAX_REVIEW_RATING (x.0 and x.5)
	 */
	private double rating;
	
	private String text;
	
	/**
	 * @return the rating
	 */
	public double getRating() {
		return this.rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(final double rating) {
		this.rating = rating;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
