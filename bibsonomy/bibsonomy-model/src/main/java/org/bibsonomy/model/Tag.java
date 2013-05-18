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

import org.bibsonomy.model.util.TagUtils;

/**
 * This class represents a tag.
 * 
 * @see TagUtils for the empty tag  
 * 
 * @version $Id: Tag.java,v 1.27 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class Tag implements Comparable<Tag>, Serializable {
	/**
	 * For persistency (Serializable)
	 */
	private static final long serialVersionUID = 1634496749338156864L;

	/**
	 * TODO: U.U. waere es nur schoener, wenn man das von ausserhalb "konfigurieren" koennte.
	 * Ist aber bei einem Tag eher kompliziert. Oder steht es im Parser drin? Wenn wir
	 * den wie eine Bean in bibsonomy-servlet.xml erstellen (z.B. wie die Logic) und
	 * dann ueberall reinreichen, wo wir ihn brauchen, dann koennte man ihn mittels
	 * dieser XML-Datei konfigurieren (aehnlich dem Captcha). 
	 */
	public static final int MAX_TAGS_ALLOWED = 100; // more tags are not allowed (they get lost)

	/**
	 * The id of this tag.
	 */
	private int id;

	/**
	 * The name of this tag.
	 */
	private String name;

	/**
	 * The stemmed version of the tag's name.
	 */
	private String stem;

	/**
	 * Indicating how often this tag is used in the complete system.
	 */
	private int globalcount;

	/**
	 * Indicating how often this tag is used by the user.
	 */
	private int usercount;

	/**
	 * These are the supertags of this tag:
	 * 
	 * <pre>
	 *   football--&gt; =&gt; football, sports 
	 * </pre>
	 */
	private List<Tag> superTags;

	/**
	 * These are the subtags of this tag.
	 * 
	 * <pre>
	 *   --&gt;football =&gt; football, european-football, american-football 
	 * </pre>
	 */
	private List<Tag> subTags;

	/**
	 * These are the {@link Post}s that are tagged with this tag.
	 */
	private List<Post<? extends Resource>> posts;

	/**
	 * Zero argument constructor.
	 */
	public Tag() {
		this.setName(null);
	}

	/**
	 * Constructs an instance and sets the name for the tag.
	 * 
	 * @param name
	 */
	public Tag(final String name) {
		this.setName(name);
	}

	/**
	 * Copy-constructor which copies the given tag (including it's sub-/supertags).
	 * <br/>The list of posts is NOT copied!
	 * 
	 * @param tag
	 */
	public Tag(final Tag tag) {
		this.setName(tag.getName());
		this.setGlobalcount(tag.getGlobalcount());
		this.setUsercount(tag.getUsercount());
		this.setStem(tag.getStem());
		this.setId(tag.getId());
		/*
		 * copy sub tags
		 */
		final List<Tag> thisSubTags = this.getSubTags();
		for (final Tag t: tag.getSubTags()) {
			thisSubTags.add(new Tag(t));
		}
		/*
		 * copy super tags
		 */
		final List<Tag> thisSuperTags = this.getSuperTags();
		for (final Tag t: tag.getSuperTags()) {
			thisSuperTags.add(new Tag(t));
		}
		
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
	 * @return subTags
	 */
	public List<Tag> getSubTags() {
		if (this.subTags == null) {
			this.subTags = new LinkedList<Tag>();
		}
		return this.subTags;
	}

	/**
	 * adds a subTag to the current tag
	 * @param subTag
	 */
	public void addSubTag(Tag subTag) {
		if (this.subTags == null) {
			this.subTags = new LinkedList<Tag>();
		}
		
		this.subTags.add(subTag);
	}	
	
	/**
	 * @param subTags
	 */
	public void setSubTags(List<Tag> subTags) {
		this.subTags = subTags;
	}

	/**
	 * adds a superTag to the current tag
	 * @param superTag
	 */
	public void addSuperTag(Tag superTag) {
		if (this.superTags == null) {
			this.superTags = new LinkedList<Tag>();
		}
		
		this.superTags.add(superTag);
	}	
	
	/**
	 * @return superTags
	 */
	public List<Tag> getSuperTags() {
		if (this.superTags == null) {
			this.superTags = new LinkedList<Tag>();
		}
		return this.superTags;
	}

	/**
	 * @param superTags
	 */
	public void setSuperTags(List<Tag> superTags) {
		this.superTags = superTags;
	}

	/**
	 * @return usercount
	 */
	public int getUsercount() {
		return this.usercount;
	}

	/**
	 * @param usercount
	 */
	public void setUsercount(int usercount) {
		this.usercount = usercount;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return count
	 */
	public int getGlobalcount() {
		return this.globalcount;
	}

	/**
	 * @param count
	 */
	public void setGlobalcount(int count) {
		this.globalcount = count;
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
	 * @return stem
	 */
	public String getStem() {
		return this.stem;
	}

	/**
	 * @param stem
	 */
	public void setStem(String stem) {
		this.stem = stem;
	}

	@Override
	public String toString() {
		return this.id + " '" + this.name + "' '" + this.stem + "' " + this.globalcount;
	}

	
	@Override
	public boolean equals(Object tag) {
		// FIXME: check for null pointers
		// FIXME: check if tag is instance of Tag
		// FIXME: what about upper vs. lower case?
		return this.getName().equals(((Tag) tag).getName());
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public int compareTo(Tag tag) {
		// FIXME: what about upper vs. lower case?
		return this.getName().compareTo(tag.getName());
	}
	
}