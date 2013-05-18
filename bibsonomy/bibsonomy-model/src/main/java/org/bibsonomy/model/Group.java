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
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.Privlevel;

/**
 * A group groups users.
 * 
 * @version $Id: Group.java,v 1.34 2011-04-29 06:45:06 bibsonomy Exp $
 */
public class Group implements Serializable{

	/**
	 * For persistency (Serializable)
	 */
	private static final long serialVersionUID = -4364391580208670647L;

	/**
	 * The internal id of this group.
	 */
	private int groupId = GroupID.INVALID.getId();

	/**
	 * This group's name.
	 */
	private String name;

	/**
	 * The real (long) name of the group.
	 */
	private String realname;
	
	/**
	 * The homepage of the group.
	 */
	private URL homepage;
	
	/**
	 * A short text describing this group.
	 */
	private String description;

	/**
	 * These are the {@link Post}s of this group.
	 */
	private List<Post<? extends Resource>> posts;

	/**
	 * These {@link User}s belong to this group.
	 */
	private List<User> users;

	/**
	 * The privacy level of this group.
	 */
	private Privlevel privlevel;

	/**
	 * If <code>true</code>, other group members can access documents
	 * attached to BibTeX posts, if the post is viewable for the group or
	 * public.
	 */
	private boolean sharedDocuments;

	/**
	 * If you add a tagset to a group and a user marks this group as 
	 * 'relevent for' a post, then the user has to tag one entry of this set
	 * to his post. A tagset has the following form: Map<SetName,Tags>.
	 */
	private List<TagSet> tagSets;
	
	/**
	 * constructor
	 */
	public Group() {
	}

	/**
	 * constructor
	 * 
	 * @param name
	 */
	public Group(final String name) {
		this();
		this.setName(name);
	}

	/**
	 * constructor
	 * 
	 * @param groupId
	 */
	public Group(final GroupID groupId) {
		this(groupId.getId());
		this.setName(groupId.name().toLowerCase());
	}

	/**
	 * constructor
	 * 
	 * @param groupid
	 */
	public Group(final int groupid) {
		this.groupId = groupid;
		this.privlevel = Privlevel.MEMBERS;
		this.sharedDocuments = false;
	}

	/**
	 * @return groupId
	 */
	public int getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	public void setPosts(List<Post<? extends Resource>> posts) {
		this.posts = posts;
	}

	/**
	 * @return users
	 */
	public List<User> getUsers() {
		if (this.users == null) {
			this.users = new LinkedList<User>();
		}
		return this.users;
	}

	/**
	 * @param users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Returns the first member of this group.
	 * FIXME: maybe we should put this inside a param object
	 * @see User#getFriend()
	 * 
	 * @return first user
	 */
	@Deprecated 
	public User getUser() {
		if (this.getUsers().size() < 1) return null;
		return this.users.get(0);
	}

	/**
	 * @return privlevel
	 */
	public Privlevel getPrivlevel() {
		return this.privlevel;
	}

	/**
	 * @param privlevel
	 */
	public void setPrivlevel(Privlevel privlevel) {
		this.privlevel = privlevel;
	}

	/**
	 * If <code>true</code>, other group members can access documents
	 * attached to BibTeX posts, if the post is viewable for the group or
	 * public.
	 * 
	 * @return The truth value regarding shared documents for this group.
	 */
	public boolean isSharedDocuments() {
		return this.sharedDocuments;
	}

	/**
	 * @param sharedDocuments
	 */
	public void setSharedDocuments(boolean sharedDocuments) {
		this.sharedDocuments = sharedDocuments;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Group)) {
			return false;
		}
		return equals((Group) obj);
	}
	
	/**
	 * @return The tag sets associated with this group. See {@link #setTagSets(List)}.
	 */
	public List<TagSet> getTagSets() {
		return this.tagSets;
	}

	/** Sets the tag sets for this group.
	 * Tag sets allow group admins to specify pre-defined tags which
	 * the users can/should use when marking a post as "relevant for" this group. 
	 * 
	 * @param tagSets
	 */
	public void setTagSets(List<TagSet> tagSets) {
		this.tagSets = tagSets;
	}

	/** Get the real (long) name of this group.
	 * 
	 * @return The real (long) name of this group.
	 */
	public String getRealname() {
		return this.realname;
	}

	/** Set the real (long) name of this group.
	 * @param realname
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * @return The homepage of this group
	 */
	public URL getHomepage() {
		return this.homepage;
	}

	/** Set the homepage of this group.
	 * @param homepage
	 */
	public void setHomepage(URL homepage) {
		this.homepage = homepage;
	}

	/**
	 * Compares two groups. Two groups are equal, if their groupId is equal.
	 * 
	 * @param other
	 * @return <code>true</code> if the two groups are equal.
	 */
	public boolean equals(final Group other) {
		if (this.groupId != GroupID.INVALID.getId() && other.groupId != GroupID.INVALID.getId()) {
			/*
			 * both groups have IDs set --> compare them by id
			 */
			final boolean groupIdEquals = GroupID.equalsIgnoreSpam(this.groupId, other.groupId);
			if (this.name != null && other.name != null) {
				final boolean nameEquals = this.name.equalsIgnoreCase(other.name);
				/*
				 * since both have also names set ... we should include the names in the comparison!
				 */
				if (( groupIdEquals && !nameEquals) ||
				    (!groupIdEquals &&  nameEquals)) {
					/*
					 * IDs do not match with names --> exception! 
					 */
					throw new RuntimeException("The names and the IDs of the given groups " + this + " and " + other + " do not match.");
				}
				/*
				 * here we know: 
				 * either both name and id are equal, or neither name and id are equal
				 * -> we need to return only the comparison value of the ids 
				 */
			}
			/*
			 * if one of the groups has a name given and the other not, they're incomparable
			 * (otherwise constructing a consistent hashcode() is impossible!
			 */
			if ((this.name != null && other.name == null) || 
			    (this.name == null && other.name != null)) {
				throw new RuntimeException("The given groups " + this + " and " + other + " are incomparable, because one of them has both name and ID set, the other not.");
			}
			return groupIdEquals;
		}
		/*
		 * at least one of the groups has no ID set --> check their name
		 */
		if (this.name != null && other.name != null) {
			return this.name.equalsIgnoreCase(other.name);
		}
		throw new RuntimeException("The given groups " + this + " and " + other + " are incomparable.");
	}

	/** 
	 * Returns a string representation of a group in the form <code>name(groupId)</code>. 
	 *  
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name + "(" + this.groupId + ")";
	}

	@Override
	public int hashCode() {
		if (this.name != null && this.groupId != GroupID.INVALID.getId()) return this.name.toLowerCase().hashCode();
		if (this.name != null) return this.name.toLowerCase().hashCode();
		return groupId;
	}
}