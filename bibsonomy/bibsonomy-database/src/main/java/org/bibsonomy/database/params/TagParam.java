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

package org.bibsonomy.database.params;

import java.util.List;

import org.bibsonomy.common.enums.TagSimilarity;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * Parameters that are specific to tags.
 * 
 * @author Dominik Benz
 * @author Miranda Grahl
 * @author Christian Kramer
 * @version $Id: TagParam.java,v 1.21 2010-04-09 09:04:39 nosebrain Exp $
 */
public class TagParam extends GenericParam {

	// FIXME Probably a duplicate: previously newContentId from GenericParam was used
	private int id;
	// FIXME: don't know if it is the third variable with the same meaning, but
	// at least it is the first one, with an intuitive name
	private Integer tasId;
	private String name;
	private String stem;
	private int count;
	private int usercount;
	private TagSimilarity tagRelationType;

	/**
	 * Decides whether to retrieve the subtags of the current tag
	 */
	private boolean retrieveSubTags;

	/**
	 * Decides whether to retrieve the superTags of the current tag
	 */
	private boolean retrieveSuperTags;

	/**
	 * Decides whether to retrieve the sub-/supertags in a transitive manner,
	 * i.e. the complete subtree under the current tag (subtags) or all tags
	 * from the current tag up to the tree root (supertags)
	 */
	private boolean retrieveSubSuperTagsTransitive;

	/**
	 * TODO document
	 */
	private List<Post<? extends Resource>> posts;

	/**
	 * Regular expression
	 */
	private String regex;

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the tasId
	 */
	public Integer getTasId() {
		return this.tasId;
	}

	/**
	 * @param tasId the tasId to set
	 */
	public void setTasId(Integer tasId) {
		this.tasId = tasId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the stem
	 */
	public String getStem() {
		return this.stem;
	}

	/**
	 * @param stem the stem to set
	 */
	public void setStem(String stem) {
		this.stem = stem;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the usercount
	 */
	public int getUsercount() {
		return this.usercount;
	}

	/**
	 * @param usercount the usercount to set
	 */
	public void setUsercount(int usercount) {
		this.usercount = usercount;
	}

	/**
	 * @return the tagRelationType
	 */
	public TagSimilarity getTagRelationType() {
		return this.tagRelationType;
	}

	/**
	 * @param tagRelationType the tagRelationType to set
	 */
	public void setTagRelationType(TagSimilarity tagRelationType) {
		this.tagRelationType = tagRelationType;
	}

	/**
	 * @return the retrieveSubTags
	 */
	public boolean isRetrieveSubTags() {
		return this.retrieveSubTags;
	}

	/**
	 * @param retrieveSubTags the retrieveSubTags to set
	 */
	public void setRetrieveSubTags(boolean retrieveSubTags) {
		this.retrieveSubTags = retrieveSubTags;
	}

	/**
	 * @return the retrieveSuperTags
	 */
	public boolean isRetrieveSuperTags() {
		return this.retrieveSuperTags;
	}

	/**
	 * @param retrieveSuperTags the retrieveSuperTags to set
	 */
	public void setRetrieveSuperTags(boolean retrieveSuperTags) {
		this.retrieveSuperTags = retrieveSuperTags;
	}

	/**
	 * @return the retrieveSubSuperTagsTransitive
	 */
	public boolean isRetrieveSubSuperTagsTransitive() {
		return this.retrieveSubSuperTagsTransitive;
	}

	/**
	 * @param retrieveSubSuperTagsTransitive the retrieveSubSuperTagsTransitive to set
	 */
	public void setRetrieveSubSuperTagsTransitive(boolean retrieveSubSuperTagsTransitive) {
		this.retrieveSubSuperTagsTransitive = retrieveSubSuperTagsTransitive;
	}

	/**
	 * @return the posts
	 */
	public List<Post<? extends Resource>> getPosts() {
		return this.posts;
	}

	/**
	 * @param posts the posts to set
	 */
	public void setPosts(List<Post<? extends Resource>> posts) {
		this.posts = posts;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return this.regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

}