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

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.enums.SearchEntity;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.common.params.beans.TagIndex;
import org.bibsonomy.database.systemstags.SystemTag;
import org.bibsonomy.database.systemstags.search.NetworkRelationSystemTag;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.enums.Order;

/**
 * This is the most generic param. All fields which are not specific to
 * bookmarks or publications are collected here. The parameter-objects are used by
 * iBATIS in the SQL-statements to fill in values; they are put at the position
 * of ?-marks.
 * 
 * @author Dominik Benz
 * @author Miranda Grahl
 * @author Christian Kramer
 * @author Christian Schenk
 * @version $Id: GenericParam.java,v 1.67 2011-07-18 14:12:00 nosebrain Exp $
 */
public abstract class GenericParam {
	/**
	 * A set of tags
	 */
	private Set<Tag> tags;
	
	/**
	 * A single tag
	 */
	private Tag tag;
	
	/**
	 * A tag name
	 */
	private String tagName;
	
	/**
	 * A title for searching resources by a given title
	 */
	private String title;
	
	/**
	 * A author name for searching resources by a given author
	 */
	private String author;
	
	/**
	 * List of (tagname, index)-pairs, where tagname can be both a name of a tag
	 * or concept.
	 */
	private List<TagIndex> tagIndex;
	
	/**
	 * corresponds to -->[tagName]
	 */
	private int numTransitiveConcepts;
	
	/**
	 * corresponds to ->[tagName] 
	 */
	private int numSimpleConcepts;
	
	/**
	 * corresponds to [tagName]
	 */
	private int numSimpleTags;
	
	/**
	 * corresponds to [tagName]-> 
	 */
	private int numSimpleConceptsWithParent;
	
	/**
	 * corresponds to [tagName]-->
	 */
	private int numSimpleConceptsWithAncestors;
	
	/**
	 * corresponds to <->[tagName]
	 */
	private int numCorrelatedConcepts;
	
	/** 
	 * List of the groups the user belongs to 
	 * 
	 * we store this as a set, because a user can of course 
	 * be only just once a member of each group; but as IBATIS
	 * expects a List to loop over, getGroups returns a List
	 * 
	 * */
	private Set<Integer> groups;
	
	
	/**
	 * List of groupnames the user belongs to. 
	 */
	private Set<String> groupNames;
	
	/**
	 * List of tags (with index) which are assigned to relations 
	 */
	private final List<TagIndex> relationTagIndex;

	/**
	 * List of tags which are assigned to relations 
	 */
	private final List<String> relationTags;
	
	/**
	 * Should tagnames (names of tags and concepts) be case sensitive; by
	 * default this is false, i.e. tagnames aren't case sensitive.
	 */
	private boolean caseSensitiveTagNames;
	/** creation-date */
	private Date date;
	/** change-date*/
	private Date changeDate;
	/** If a contentId is updated or deleted we need this as reference */
	private int requestedContentId;
	/**
	 * The hash of a post, e.g. a bookmark or a BibTex TODO: really of the post
	 * and not of the resource? and for what kind of hash is this used? isn't it
	 * resource-specific and shouldn't it be set in the resource-field?
	 */
	private String hash;

	/**
	 * This is used to restrict simHashes, i.e. to limit the overall resultset.
	 * The default simHash is defined in {@link HashID}.
	 */
	private HashID simHash;	

	/* modified search parameter */
	private String search;

	/* not modified search parameter */
	private String rawSearch;
	
	/** This is the current user. */
	private String userName;
	private String description;
	private String extension;
	/**
	 * The current user, who would be identified by userName, can look at other
	 * people's content. This requested user is identified by this string.
	 */
	private String requestedUserName;
	/** 
	 * The ID of a group which by default is invalid.
	 */
	private int groupId;
	/** If we're searching for a group this is used for the name of the group */
	private String requestedGroupName;
	/** The SQL-Limit which is by default 10 */
	private int limit;
	/** The SQL-Offset which is by default 0 */
	private int offset;
	/** The type of a ID is by default DS_CONTENT_ID * */
	private ConstantID idsType;
	private int newContentId;
	private String url;
	private ConstantID contentType;
	
	private Order order;
	private GroupingEntity grouping;
	private FilterEntity filter;
	private SearchEntity searchEntity;
	/*
     * the days of a popular resource
     * TODO: please document use better. This are not really
     * the "days", but more or less the position in the list
     * of available days?!
	 */
	private int days;
	
	/*
	 * retrieve resources via their bibtexkey 
	 */
	private String bibtexKey;
	
	private final Map<String, SystemTag> systemTags;

	/**
	 * sets default values
	 */
	public GenericParam() {
		this.tagIndex = new ArrayList<TagIndex>();
		this.numSimpleTags = 0;
		this.numSimpleConcepts = 0;
		this.numTransitiveConcepts = 0;
		this.numSimpleConceptsWithParent = 0;
		this.numSimpleConceptsWithAncestors = 0;
		this.numCorrelatedConcepts = 0;
		this.caseSensitiveTagNames = false;
		/*
		 * set groupId to -1
		 */
		this.setGroupId(GroupID.INVALID);
		
		this.idsType = ConstantID.IDS_UNDEFINED_CONTENT_ID;
		this.limit = 10;
		this.offset = 0;
		this.simHash = HashID.SIM_HASH; // the default hash type
		
		this.grouping = GroupingEntity.ALL;
		
		this.groups =  new HashSet<Integer>();
		this.groupNames =  new HashSet<String>();
		//when using this field the value of days must be greater 0 
		this.days = -1;
		
		this.systemTags = new HashMap<String, SystemTag>();
		
		this.relationTags = new ArrayList<String>();
		this.relationTagIndex = new ArrayList<TagIndex>();
	}
	
	/**
	 * @return the caseSensitiveTagNames
	 */
	public boolean isCaseSensitiveTagNames() {
		return this.caseSensitiveTagNames;
	}

	/**
	 * @param caseSensitiveTagNames the caseSensitiveTagNames to set
	 */
	public void setCaseSensitiveTagNames(final boolean caseSensitiveTagNames) {
		this.caseSensitiveTagNames = caseSensitiveTagNames;
	}

	private void addToTagIndex(final String tagName) {
		this.tagIndex.add(new TagIndex(tagName, this.tagIndex.size() + 1));
	}

	/**
	 * adds a tag
	 * @param tagName the name of the tag to add
	 */
	public void addTagName(final String tagName) {
		this.addToTagIndex(tagName);
		this.numSimpleTags++;
	}
	
	/**
	 * TODO: improve docu
	 * @param tagName the name of the tag to add
	 */
	public void addSimpleConceptName(final String tagName) {
		this.addToTagIndex(tagName);
		this.numSimpleConcepts++;
	}
	
	/**
	 * TODO: improve docu
	 * @param tagName the name of the tag to add
	 */
	public void addTransitiveConceptName(final String tagName) {
		this.addToTagIndex(tagName);
		this.numTransitiveConcepts++;
	}
	
	/**
	 * TODO: improve docu
	 * @param tagName
	 */
	public void addSimpleConceptWithParentName(final String tagName) {
		this.addToTagIndex(tagName);
		this.numSimpleConceptsWithParent++;
	}
	
	/**
	 * TODO: improve docu
	 * @param tagName
	 */
	public void addSimpleConceptwithAncestorsName(final String tagName) {
		this.addToTagIndex(tagName);
		this.numSimpleConceptsWithAncestors++;
	}
	
	/**
	 * TODO: improve docu
	 * @param tagName
	 */
	public void addCorrelatedConceptName(final String tagName) {
		this.addToTagIndex(tagName);
		this.numCorrelatedConcepts++;
	}
	
	/**
	 * @return the tagIndex
	 */
	public List<TagIndex> getTagIndex() {
		return this.tagIndex;
	}

	/**
	 * @param tagIndex the tagIndex to set
	 */
	public void setTagIndex(final List<TagIndex> tagIndex) {
		this.tagIndex = tagIndex;
	}

	/**
	 * This is used to determine the max. amount of join-indices for the
	 * iteration of the join-index; e.g. if we're searching for tag names. If we
	 * have only one tag, we don't need a join index, if we got two then we need
	 * one, if we got three then we need two, and so on.<br/> We had to
	 * introduce this because iBATIS can only call methods that are true getter
	 * or setter. A call to tagIndex.size() is not possible. An attempt fails
	 * with "There is no READABLE property named 'size' in class
	 * 'java.util.ArrayList'".
	 * @return TODO
	 */
	public int getMaxTagIndex() {
		// TODO: if this methods name was intuitive, size-1 should be returned
		// because tagIndex[size] is out of bounds
		return this.tagIndex.size();
	}

	/**
	 * TODO comment
	 * @param tagName 
	 */
	public void addRelationTag(final String tagName) {
		this.relationTags.add(tagName);
		this.relationTagIndex.add(new TagIndex(tagName, this.relationTagIndex.size() + 1));
	}

	/**
	 * TODO comment
	 * @param relationTags the relation tags to add
	 */
	public void addRelationTags(final List<String> relationTags) {
		for (final String tagName : relationTags) {
			this.addRelationTag(tagName);
		}
	}
	
	/**
	 * TODO comment
	 * @return the relation tags
	 */
	public List<String> getRelationTags() {
		return relationTags;
	}

	/**
	 * TODO comment
	 * @return the relation tag index
	 */
	public List<TagIndex> getRelationTagIndex() {
		return relationTagIndex;
	}
	
	/**
	 * @return the search
	 */
	public String getSearch() {
		return this.search;
	}

	/**
	 * @return the rawSearch
	 */
	public String getRawSearch() {
		return this.rawSearch;
	}

	/**
	 * sets the rawsearch to search and prepares the search param for the database query
	 * 
	 * @param search the search to set
	 */
	public void setSearch(final String search) {
		if (search != null) {
			this.rawSearch = search;
			this.search = search.replaceAll("([\\s]|^)([\\S&&[^-]])", " +$2");
		}
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
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(final Date changeDate) {
	    this.changeDate = changeDate;
	}

	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
	    return changeDate;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return this.limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(final int limit) {
		this.limit = limit;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return this.offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(final int offset) {
		this.offset = offset;
	}

	/**
	 * returns the list of groups a user is member of
	 * 
	 * ATTENTION: this is not just a plain getter - we transform 
	 * the set of groups into a list of groups for IBATIS compatibility
	 * 
	 * @return a list of groups
	 */
	public List<Integer> getGroups() {
		return new ArrayList<Integer>(this.groups);
	}

	/**
	 * set the groups
	 *
	 * wrapper method for setting the groups set by a list
	 * 
	 * @param groups a LIST of group ids
	 */
	public void setGroups(final Collection<Integer> groups) {
		this.groups = new HashSet<Integer>(groups);
	}
	
	/**
	 * @return the groupId
	 */
	public int getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(final int groupId) {
		this.groupId = groupId;
	}

	/**
	 * This setter sets the group id using the GroupID enum.
	 * 
	 * @param groupID
	 */
	public void setGroupId(final GroupID groupID) {
		this.groupId = groupID.getId();
	}

	/**
	 * If you need the ID of the friends group in a statement, 
	 * use this method.  
	 * 
	 * @return The ID of the friends group.
	 */
	public int getGroupTypeFriends() {
		return GroupID.FRIENDS.getId();
	}
	
	/**
	 * If you need the ID of the public group in a statement, 
	 * use this method.  
	 * 
	 * @return The ID of the public group.
	 */
	public int getGroupTypePublic() {
		return GroupID.PUBLIC.getId();
	}
	
	/**
	 * If you need the system tag which identifies BibSonomy's trust
	 * relation
	 * 
	 * @return BibSonomy's trust network system tag
	 */
	public String getBibSonomyFriendsTag() {
		return NetworkRelationSystemTag.BibSonomyFriendSystemTag;
	}
	
	/**
	 * If you need the system tag which identifies BibSonomy's follower
	 * relation
	 * 
	 * @return BibSonomy's follower network system tag
	 */
	public String getBibSonomyFollowerTag() {
		return NetworkRelationSystemTag.BibSonomyFollowerSystemTag;
	}
	
	// TODO: what hash?, what for?, why in genericparam and not in
	// resource-field?
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
	 * @return the simHash
	 */
	public int getSimHash() {
		return this.simHash.getId();
	}

	/**
	 * @param simHash the simHash to set
	 */
	public void setSimHash(final HashID simHash) {
		this.simHash = simHash;
	}
	
	/**
	 * @return the requestedContentId
	 */
	public int getRequestedContentId() {
		return this.requestedContentId;
	}

	/**
	 * @param requestedContentId the requestedContentId to set
	 */
	public void setRequestedContentId(final int requestedContentId) {
		this.requestedContentId = requestedContentId;
	}

	// TODO: why in genericparam and not in resource-field?
	/**
	 * @return the requestedUserName
	 */
	public String getRequestedUserName() {
		return this.requestedUserName;
	}

	/**
	 * @param requestedUserName the requestedUserName to set
	 */
	public void setRequestedUserName(final String requestedUserName) {
		this.requestedUserName = requestedUserName;
	}

	/**
	 * @return the requestedGroupName
	 */
	public String getRequestedGroupName() {
		return this.requestedGroupName;
	}

	/**
	 * @param requestedGroupName the requestedGroupName to set
	 */
	public void setRequestedGroupName(final String requestedGroupName) {
		this.requestedGroupName = requestedGroupName;
	}

	/**
	 * @return the id of the idsType
	 */
	public int getIdsType() {
		return this.idsType.getId();
	}
	
	/**
	 * @param idsType the idsType to set
	 */
	public void setIdsType(final ConstantID idsType) {
		this.idsType = idsType;
	}

	/**
	 * @return the tags
	 */
	public Set<Tag> getTags() {
		return this.tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(final Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @return the newContentId
	 */
	public int getNewContentId() {
		return this.newContentId;
	}

	/**
	 * @param newContentId the newContentId to set
	 */
	public void setNewContentId(final int newContentId) {
		this.newContentId = newContentId;
	}

	/**
	 * @return the tag
	 */
	public Tag getTag() {
		return this.tag;
	}

	/**
	 * sets also the tagName field to the name of the tag
	 * 
	 * @param tag the tag to set
	 */
	public void setTag(final Tag tag) {
		this.tag = tag;
		this.tagName = tag.getName();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return this.extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(final String extension) {
		this.extension = extension;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * TODO: change method name to contentTypeId
	 * 
	 * @return the id of the content type
	 */
	public int getContentType() {
		return this.contentType.getId();
	}

	/**
	 * TODO: change method name to getContentType
	 * 
	 * @return the content type
	 */
	public ConstantID getContentTypeConstant() {
		return this.contentType;
	}
	
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(final ConstantID contentType) {
		this.contentType = contentType;
	}

	/**
	 * sets the content type by the nativeContentType param
	 * 
	 * @param resourceType
	 */
	public void setContentTypeByClass(final Class<? extends Resource> resourceType) {
		setContentType(ConstantID.getContentTypeByClass(resourceType));
	}

	/**
	 * @return if the tag is not null the name of the tag else the tagName
	 */
	public String getTagName() {
		if (tag != null) {
			return tag.getName();
		}
		return this.tagName;
	}

	/**
	 * sets the tag name to the param and sets the tag to null
	 * 
	 * @param tagName the tagName to set
	 */
	public void setTagName(final String tagName) {
		this.tag = null;
		this.tagName = tagName;
	}

	/**
	 * FIXME: only used by test method
	 * @return {@link #getTagName()} lower case
	 */
	public String getTagNameLower() {
		return this.getTagName().toLowerCase();
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
	 * @return the author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(final String author) {
		this.author = author;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return this.order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final Order order) {
		this.order = order;
	}

	/**
	 * @return the grouping
	 */
	public GroupingEntity getGrouping() {
		return this.grouping;
	}

	/**
	 * @param grouping the grouping to set
	 */
	public void setGrouping(final GroupingEntity grouping) {
		this.grouping = grouping;
	}

	/**
	 * @return the filter
	 */
	public FilterEntity getFilter() {
		return this.filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(final FilterEntity filter) {
		this.filter = filter;
	}

	/**
	 * @return the numSimpleConcepts
	 */
	public Integer getNumSimpleConcepts() {
		return this.numSimpleConcepts;
	}

	/**
	 * @return the numSimpleTags
	 */
	public Integer getNumSimpleTags() {
		return this.numSimpleTags;
	}
	
	/**
	 * @param numSimpleTags the numSimpleTags to set
	 */
	public void setNumSimpleTags(final int numSimpleTags) {
		this.numSimpleTags = numSimpleTags;
	}

	/**
	 * @return numTransitiveConcepts
	 */
	public Integer getNumTransitiveConcepts() {
		return this.numTransitiveConcepts;
	}

	/**
	 * @return numSimpleConceptsWithParent
	 */
	public int getNumSimpleConceptsWithParent() {
		return this.numSimpleConceptsWithParent;
	}

	/**
	 * @return the numCorrelatedConcepts
	 */
	public int getNumCorrelatedConcepts() {
		return this.numCorrelatedConcepts;
	}

	/**
	 * @return the numSimpleConceptsWithAncestors
	 */
	public int getNumSimpleConceptsWithAncestors() {
		return this.numSimpleConceptsWithAncestors;
	}
	
	/**
	 * @param numTransitiveConcepts the numTransitiveConcepts to set
	 */
	public void setNumTransitiveConcepts(final int numTransitiveConcepts) {
		this.numTransitiveConcepts = numTransitiveConcepts;
	}

	/**
	 * @param numSimpleConcepts the numSimpleConcepts to set
	 */
	public void setNumSimpleConcepts(final int numSimpleConcepts) {
		this.numSimpleConcepts = numSimpleConcepts;
	}
	
	/**
	 * adds a group to the group list
	 * @param groupId the id of the group to add
	 */
	public void addGroup(final Integer groupId) {
		this.groups.add(groupId);
	}
	
	/**
	 * adds all groups to the group list 
	 * @param groups the id's of the groups to add
	 */
	public void addGroups(final Collection<Integer> groups) {
		this.groups.addAll(groups);
	}
	
	/**
	 * @return the searchEntity
	 */
	public SearchEntity getSearchEntity() {
		return this.searchEntity;
	}

	/**
	 * @param searchEntity the searchEntity to set
	 */
	public void setSearchEntity(final SearchEntity searchEntity) {
		this.searchEntity = searchEntity;
	}

	/**
	 * @return the days
	 */
	public int getDays() {
		return this.days;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(final int days) {
		this.days = days;
	}

	/**
	 * @return the bibtexKey
	 */
	public String getBibtexKey() {
		return this.bibtexKey;
	}

	/**
	 * @param bibtexKey the bibtexKey to set
	 */
	public void setBibtexKey(final String bibtexKey) {
		this.bibtexKey = bibtexKey;
	}

	/**
	 * add group ids and groupnames of groups this user may see
	 * 
	 * @param groups - a list of groups
	 */
	public void addGroupsAndGroupnames(final Collection<? extends Group> groups) {
		// add groupids + groupnames
		String groupName = "";
		for (final Group g : groups) {
			this.groups.add(g.getGroupId());
			groupName = g.getName() == null ? "group_" + g.getGroupId() : g.getName().toLowerCase();
			// TODO warum kann der Gruppenname (im Test) null sein? 
			this.groupNames.add(groupName);
			// this.groupNames.add(g.getName().toLowerCase());
		}
	}

	/**
	 * @return the groupNames
	 */
	public Set<String> getGroupNames() {
		return this.groupNames;
	}

	/**
	 * @param groupNames the groupNames to set
	 */
	public void setGroupNames(final Set<String> groupNames) {
		this.groupNames = groupNames;
	}

	/**
	 * adds a system tag to the map
	 * uses the system tag name as key
	 * @param tag
	 */
	public void addToSystemTags(final SystemTag tag) {
		if (tag != null) {
			this.systemTags.put(tag.getName(), tag);
		}
	}
	
	/**
	 * adds a collection of system tags to system tags
	 * 
	 * @param systemTags	the collection to add to system tags
	 */
	public void addAllToSystemTags(final Collection<SystemTag> systemTags) {
		if (present(systemTags)) {
			for (final SystemTag tag : systemTags) {
				this.addToSystemTags(tag);
			}
		}
	}

	/**
	 * @return a map of system tags [tag name => systemTag, …]
	 */
	public Map<String, SystemTag> getSystemTags() {
		return Collections.unmodifiableMap(this.systemTags);
	}
	
	/**
	 * Introspect the current param object and return a string representation of the form attribute = value
	 * for all attributes of this object.
	 * 
	 * @return - a string representation of the given object by introspection.
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}