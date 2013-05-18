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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Everything, which can be tagged, is derived from this class.
 * 
 * What may be accurate for representing the type of a Resource?
 * -> naturally its class! (which is lighter, more intuitive and flexible
 *    (eg reflective instantiation) and most notably more precise in
 *    type-safe generic methods than an enum).
 * 
 * @version $Id: Resource.java,v 1.37 2011-06-27 15:52:01 nosebrain Exp $
 */
public abstract class Resource implements Serializable, Rateable {
	/**
	 * For persistence (Serializable) 
	 */
	private static final long serialVersionUID = -9153320764851332223L;
	
	/**
	 * How many posts with this resource exist.
	 */
	private int count;

	/**
	 * The inter user hash is less specific than the {@link #intraHash}.
	 */
	private String interHash;

	/**
	 * The intra user hash is relatively strict and takes many fields of this
	 * resource into account.
	 */
	private String intraHash;

	/**
	 * These are the {@link Post}s this resource belongs to.
	 */
	private List<Post<? extends Resource>> posts;

	/**
	 * Each resource has a title. 
	 * 
	 * TODO: It is given by the user and thus might better fit into the post.
	 */
	private String title;
	
	/**
	 * all comments for this resource
	 */
	private List<DiscussionItem> discussionItems;
	
	/**
	 * the rating (avg, …) of the resource
	 */
	private Double rating;
	
	/**
	 * number of all ratings for the resource
	 */
	private Integer numberOfRatings;
	
	/**
	 * FIXME: This method does not belong to the model!!!! It would be fine to
	 * make it a static method of this class and use the resource (to
	 * recalculate hashes for) as parameter.
	 */
	public abstract void recalculateHashes();

	/**
	 * @return interHash
	 */
	public String getInterHash() {
		return this.interHash;
	}

	/**
	 * @param interHash
	 */
	public void setInterHash(final String interHash) {
		this.interHash = interHash;
	}

	/**
	 * @return intraHash
	 */
	public String getIntraHash() {
		return this.intraHash;
	}

	/**
	 * @param intraHash
	 */
	public void setIntraHash(final String intraHash) {
		this.intraHash = intraHash;
	}

	/**
	 * @return posts
	 */
	public List<Post<? extends Resource>> getPosts() {
		if (this.posts == null) {
			this.posts = new LinkedList<Post<? extends Resource>>();
		}
		return this.posts;
	}

	/**
	 * @param posts
	 */
	public void setPosts(final List<Post<? extends Resource>> posts) {
		this.posts = posts;
	}

	/**
	 * @return count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * @param count
	 */
	public void setCount(final int count) {
		this.count = count;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
	
	/**
	 * @return the rating
	 */
	@Override
	public Double getRating() {
		return this.rating;
	}

	/**
	 * @param rating the rating to set
	 */
	@Override
	public void setRating(final double rating) {
		this.rating = rating;
	}
	
	/**
	 * @return the numberOfRatings
	 */
	@Override
	public Integer getNumberOfRatings() {
		return this.numberOfRatings;
	}

	/**
	 * @param numberOfRatings the numberOfRatings to set
	 */
	@Override
	public void setNumberOfRatings(final int numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}

	/**
	 * @return the discussionItems
	 */
	public List<DiscussionItem> getDiscussionItems() {
		return this.discussionItems;
	}

	/**
	 * @param discussionItems the discussionItems to set
	 */
	public void setDiscussionItems(final List<DiscussionItem> discussionItems) {
		this.discussionItems = discussionItems;
	}

	@Override
	public String toString() {
		return "<" + intraHash + "/" + interHash + ">";
	}
}