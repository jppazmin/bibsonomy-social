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

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/**
 * A post connects a given resource with a user and a certain date.
 * 
 * @version $Id: Post.java,v 1.36 2011-05-17 09:09:34 econ11 Exp $
 * @param <T>
 *            resource type
 */
public class Post<T extends Resource> implements Serializable {

	/**
	 * For persistency (Serializable)
	 */
	private static final long serialVersionUID = -4890029197498534435L;

	/**
	 * This is the {@link Resource} that this post is encapsulating.
	 */
	private T resource;

	/**
	 * We need this here if we want to use groupBy in iBatis
	 * TODO: document me
	 * TODO: Is this field really part of the model?
	 */
	private Integer contentId;

	/**
	 * This post belongs to this {@link User}.
	 */
	private User user;

	/**
	 * This post belongs to these {@link Group}s.
	 */
	private Set<Group> groups;

	/**
	 * This post is tagged with these {@link Tag}s.
	 */
	private Set<Tag> tags;
	
	/**
	 * This post is tagged with these {@link SystemTag}s
	 * they are hidden but can be called when needed
	 */
	private Set<Tag> hiddenSystemTags;
	/**
	 * This post is tagged with these {@link Tag}s
	 * they are not hidden
	 */
	private Set<Tag> visibleTags;

	/**
	 * This is the {@link Date} when this post was lastly modified.
	 */
	private Date changeDate;
	
	/**
	 * This is the {@link Date} when this post was created.
	 */
	private Date date;
	
	/**
	 * This is a text describing the post. <br/>
	 * 
	 * The description should be part of the post because it's a description
	 * individually made by one user for his post - another user may describe
	 * the post with another text.
	 */
	private String description;
	
	
	/**
	 * a ranking (used to sort a list of posts)
	 */
	private double ranking = 0.0;
	
	/**
	 * identifier if this post is picked or not
	 */
	private boolean isPicked = false;
	
	/**
	 * identifier if post is in the inbox
	 * use only to create the inbox page of a user
	 */
	private boolean isInboxPost = false;
	
	
	/**
	 * List of repositories where this post has been send to (PUMA specific)
	 */
	private List<Repository> repositorys;

	/**
	 * @return the repositorys
	 */
	public List<Repository> getRepositorys() {
		return this.repositorys;
	}

	/**
	 * @param repositorys the repositorys to set
	 */
	public void setRepositorys(List<Repository> repositorys) {
		this.repositorys = repositorys;
	}

	/**
	 * @return contentId
	 */
	public Integer getContentId() {
		return this.contentId;
	}

	/**
	 * @param contentId
	 */
	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return groups
	 */
	public Set<Group> getGroups() {
		if (this.groups == null) {
			this.groups = new HashSet<Group>();
		}
		return this.groups;
	}

	/**
	 * @param groups
	 */
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return changeDate;
	}

	/**
	 * @return postingDate
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param postingDate
	 */
	public void setDate(Date postingDate) {
		this.date = postingDate;
	}

	/**
	 * @return resource
	 */
	public T getResource() {
		return this.resource;
	}

	/**
	 * @param resource
	 */
	public void setResource(T resource) {
		this.resource = resource;
	}

	/**
	 * @return tags
	 */
	public Set<Tag> getTags() {
		if (this.tags == null) {
			/*
			 * a linked hash set gives predictable iteration order
			 * (insertion order)
			 */
			this.tags = new LinkedHashSet<Tag>();
		}
		return this.tags;
	}

	/**
	 * @param tags
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Convenience method to add a tag.
	 * 
	 * @param tagName
	 */
	public void addTag(final String tagName) {
		// call getTags to initialize this.tags
		this.getTags();
		// add the tag
		this.tags.add(new Tag(tagName));
	}

	/**
	 * Convenience method to add a group.
	 * 
	 * @param groupName
	 */
	public void addGroup(final String groupName) {
		// call getGroups to initialize this.groups
		this.getGroups();
		// add the group
		this.groups.add(new Group(groupName));
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set a ranking value
	 * 
	 * @param ranking
	 * 			the ranking to set
	 */
	public void setRanking(double ranking) {
		this.ranking = ranking;
	}

	/**
	 * Retrieve the ranking of this post
	 * 
	 * @return a double representing the ranking of this post
	 */
	public double getRanking() {
		return ranking;
	}

	/**
	 * Returns if this post is an inbox post or not
	 * 
	 * @return boolean
	 */
	public boolean isInboxPost() {
		return this.isInboxPost;
	}

	/**
	 * Set if this post is an inbox post or not
	 * 
	 * @param isInboxPost
	 */
	public void setInboxPost(boolean isInboxPost) {
		this.isInboxPost = isInboxPost;
	}
	/**
	 * Returns if this post is picked or not
	 * 
	 * @return boolean
	 */
	public boolean isPicked() {
		return this.isPicked;
	}

	/**
	 * Set if this post is picked or not
	 * 
	 * @param isPicked
	 */
	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}
	
	@Override
	public String toString() {
		return "\n" + (user == null ? "" : user.getName()) + "\n\ttagged\n\t\t" + resource + "\n\twith\n" + tags;
	}

	/**
	 * @param hiddenSystemTags
	 */
	public void setHiddenSystemTags(Set<Tag> hiddenSystemTags) {
		this.hiddenSystemTags = hiddenSystemTags;
	}

	/**
	 * @return the hidden system Tags of this post
	 */
	public Set<Tag> getHiddenSystemTags() {
		return hiddenSystemTags;
	}
	
	/**
	 * Add a SystemTag (Tag) to the HiddenSystemTag list
	 * @param tag
	 */
	public void addHiddenSystemTag(Tag tag) {
		if (!present(this.hiddenSystemTags)) {
			this.hiddenSystemTags = new HashSet<Tag>();
		}
		this.hiddenSystemTags.add(tag);
	}

	/**
	 * @param visibleTags
	 */
	public void setVisibleTags(Set<Tag> visibleTags) {
		this.visibleTags = visibleTags;
	}

	/**
	 * @return the visible tags
	 */
	public Set<Tag> getVisibleTags() {
		return visibleTags;
	}
	
	/**
	 * @param tag
	 */
	public void addVisibleTag(Tag tag) {
		if (!present(this.visibleTags)) {
			this.visibleTags = new HashSet<Tag>();
		}
		this.visibleTags.add(tag);
	}
}