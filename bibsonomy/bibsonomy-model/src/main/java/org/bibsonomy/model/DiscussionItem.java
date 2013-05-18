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

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author dzo
 * @version $Id: DiscussionItem.java,v 1.4 2011-07-26 08:30:33 bibsonomy Exp $
 */
public class DiscussionItem {

	/**
	 * the internal id; only use in database module!
	 */
	private Integer id;
	
	/**
	 * the hash representing this item
	 */
	private String hash;
	
	/**
	 * the groups of this item
	 */
	private Set<Group> groups;
	
	/**
	 * the user who posted this item
	 */
	private User user;

	/**
	 * the creation date
	 */
	private Date date;
	
	/**
	 * the <em>last</em> change date
	 */
	private Date changeDate;
	
	/**
	 * the comments of this item
	 */
	private List<DiscussionItem> subDiscussionItems;

	/**
	 * the hash of the parent discussion item
	 */
	private String parentHash;

	/**
	 * the user can decide if his username is published with the review
	 */
	private boolean anonymous;

	/**
	 * @return the comments
	 */
	public List<DiscussionItem> getSubDiscussionItems() {
		return this.subDiscussionItems;
	}

	/**
	 * @param discussionItem the discussion item to add
	 */
	public void addToDiscussionItems(final DiscussionItem discussionItem) {
		if (this.subDiscussionItems == null) {
			this.subDiscussionItems = new LinkedList<DiscussionItem>();
		}
		
		if (!this.subDiscussionItems.contains(discussionItem)) {
			this.subDiscussionItems.add(discussionItem);
		}
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return this.changeDate;
	}

	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(final Date changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(final Set<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the groups
	 */
	public Set<Group> getGroups() {
		if (this.groups == null) {
			this.groups = new HashSet<Group>();
		}
		return this.groups;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(final String hash) {
		this.hash = hash;
	}

	/**
	 * @return the parentHash
	 */
	public String getParentHash() {
		return this.parentHash;
	}

	/**
	 * @param parentHash the parentHash to set
	 */
	public void setParentHash(final String parentHash) {
		this.parentHash = parentHash;
	}

	/**
	 * @return the anonymous
	 */
	public boolean isAnonymous() {
		return this.anonymous;
	}

	/**
	 * @param anonymous the anonymous to set
	 */
	public void setAnonymous(final boolean anonymous) {
		this.anonymous = anonymous;
	}
}
