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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.errors.DuplicatePostErrorMessage;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.errors.IdenticalHashErrorMessage;
import org.bibsonomy.common.errors.MissingFieldErrorMessage;
import org.bibsonomy.common.errors.UpdatePostErrorMessage;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.common.params.beans.TagIndex;
import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.database.plugin.DatabasePluginRegistry;
import org.bibsonomy.database.systemstags.SystemTag;
import org.bibsonomy.database.systemstags.SystemTagsExtractor;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.executable.ExecutableSystemTag;
import org.bibsonomy.database.systemstags.search.NetworkRelationSystemTag;
import org.bibsonomy.database.util.DatabaseUtils;
import org.bibsonomy.database.validation.DatabaseModelValidator;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.SimHash;
import org.bibsonomy.services.searcher.ResourceSearch;
import org.bibsonomy.util.ReflectionUtils;

/**
 * Used to create, read, update and delete posts from the database.
 * 
 * the following statements should exist for the resource:
 * 	- getSync<RESOURCE>
 *	- get<RESOURCE>FromInbox
 *  - get<RESOURCE>FromInboxByHash
 *  - get<RESOURCE>ByConceptByTag
 *  - get<RESOURCE>ByConceptForGroup
 *  - get<RESOURCE>ByConceptForUser
 *  - get<RESOURCE>ByTagNames
 *  - get<RESOURCE>ByTagNamesAndFolkrank
 *  - get<RESOURCE>ByTagNamesCount
 *  - get<RESOURCE>ByUserFriends
 *  - get<RESOURCE>ByTagNamesForUserCount
 *  - get<RESOURCE>Popular
 *  - get<RESOURCE>PopularDays
 *  - get<RESOURCE>ForHomepage
 *  - get<RESOURCE>ByHash
 *  - get<RESOURCE>ByHashCount
 *  - get<RESOURCE>ByHashAndUserCount
 *  - get<RESOURCE>ByHashForUser
 *  - get<RESOURCE>Viewable
 *  - get<RESOURCE>ViewableByTag
 *  - get<RESOURCE>ForGroup
 *  - get<RESOURCE>ForGroupCount
 *  - get<RESOURCE>ForMyGroupPosts
 *  - get<RESOURCE>ForMyGroupPostsByTag
 *  - get<RESOURCE>ForGroupByTag
 *  - get<RESOURCE>ForUser
 *  - get<RESOURCE>ForUserCount
 *  - get<RESOURCE>ByFollowedUsers
 *  - getContentIdFor<RESOURCE>
 *  - getGroup<RESOURCE>CountByTag
 *  - getGroup<RESOURCE>Count
 *  - get<RESOURCE>FromBasketForUser
 *  - insert<RESOURCE>
 *  - insert<RESOURCE>update
 *  - update<RESOURCE>Hash
 *  - delete<RESOURCE>
 *  
 * @author dzo
 * 
 * @version $Id: PostDatabaseManager.java,v 1.73 2011-07-14 15:24:20 rja Exp $
 * @param <R> the resource
 * @param <P> the param
 */
public abstract class PostDatabaseManager<R extends Resource, P extends ResourceParam<R>> extends AbstractDatabaseManager implements CrudableContent<R, P> {
	private static final Log log = LogFactory.getLog(PostDatabaseManager.class);

	/** database managers */
	protected final GeneralDatabaseManager generalDb;
	protected final TagDatabaseManager tagDb;
	protected final DatabasePluginRegistry plugins;
	protected final PermissionDatabaseManager permissionDb;
	protected final GroupDatabaseManager groupDb;


	/** simple class name of the resource managed by the class */
	protected final String resourceClassName;

	/** instance of the lucene searcher */
	private ResourceSearch<R> resourceSearch;

	/** the validator for the posts*/
	protected final DatabaseModelValidator<R> validator;


	/**
	 * inits the database managers and resource class name
	 */
	protected PostDatabaseManager() {
		this.generalDb = GeneralDatabaseManager.getInstance();
		this.tagDb = TagDatabaseManager.getInstance();
		this.plugins = DatabasePluginRegistry.getInstance();
		this.permissionDb = PermissionDatabaseManager.getInstance();
		this.groupDb = GroupDatabaseManager.getInstance();

		this.resourceClassName = this.getResourceClassName();

		this.validator = new DatabaseModelValidator<R>();
	}

	/**
	 * @return the lucene search instance to use
	 */
	protected ResourceSearch<R> getResourceSearch() {
		return resourceSearch;
	}

	/**
	 * @param resourceSearch
	 */
	public void setResourceSearch(final ResourceSearch<R> resourceSearch) {
		this.resourceSearch = resourceSearch;
	}

	/**
	 * 
	 * @param userName
	 * @param session
	 * @return Map with all synchronization posts of type R
	 */
	public Map<String, SynchronizationPost> getSyncPostsMapForUser(final String userName, final DBSession session) {
	    return this.queryForMap("getSync" + this.resourceClassName, this.createParam(userName, userName), "intraHash", session);
	}
	
	/**
	 * 
	 * @param userName
	 * @param session
	 * @return a list of posts to synchronize
	 */
	@SuppressWarnings("unchecked")
	public List<SynchronizationPost> getSyncPostsListForUser(final String userName, final DBSession session) {
	    return this.queryForList("getSync" + this.resourceClassName, this.createParam(userName, userName), session);
	}
	
	@SuppressWarnings("unchecked")
	protected List<Post<R>> postList(final String query, final P param, final DBSession session) {
		return this.queryForList(query, param, session);
	}

	/**
	 * @param receiver 
	 * @param limit 
	 * @param offset 
	 * @param session
	 * 
	 * @return a lists of posts of type R with the inbox content
	 */
	public List<Post<R>> getPostsFromInbox(final String receiver, final int limit, final int offset, final DBSession session) {
		final P param = this.createParam(limit, offset);
		param.setRequestedUserName(receiver);
		return this.postList("get" + this.resourceClassName + "FromInbox", param, session);
	}

	/**
	 * @param receiver 
	 * @param intraHash 
	 * @param session
	 * @return a lists of Posts of type R with the inbox content
	 */
	public List<Post<R>> getPostsFromInboxByHash(final String receiver, final String intraHash, final DBSession session) {
		final P param = this.getNewParam();
		param.setRequestedUserName(receiver);
		param.setHash(intraHash);
		return this.postList("get" + this.resourceClassName + "FromInboxByHash", param, session);
	}


	/**
	 * <em>/concept/tag/TAGNAME</em>
	 * 
	 * @param tagIndex
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return a list of posts
	 */
	public List<Post<R>> getPostsByConceptByTag(final List<TagIndex> tagIndex, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(limit, offset);
		param.setTagIndex(tagIndex);
		param.addAllToSystemTags(systemTags);

		return this.postList("get" + this.resourceClassName + "ByConceptByTag", param, session);
	}

	/**
	 * <em>/concept/group/GruppenName/EinTag</em><br/><br/>
	 * 
	 * This method retrieves all posts of all group members of the given
	 * group which are tagged at least with one of the concept tags or its
	 * subtags
	 * 
	 * @param loginUser 
	 * @param visibleGroupIDs 
	 * @param requestedGroupName
	 * @param tagIndex
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return a list of posts
	 */
	public List<Post<R>> getPostsByConceptForGroup(final String loginUser, final List<Integer> visibleGroupIDs, final String requestedGroupName, final List<TagIndex> tagIndex, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUser, null, limit, offset);
		param.setGroups(visibleGroupIDs);
		param.setRequestedGroupName(requestedGroupName);
		param.setTagIndex(tagIndex);
		param.addAllToSystemTags(systemTags);

		DatabaseUtils.prepareGetPostForGroup(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ByConceptForGroup", param, session);
	}


	/**
	 * <em>/concept/user/MaxMustermann/EinTag</em><br/><br/>
	 * 
	 * This method prepares queries which retrieve all posts for a given
	 * user name (requestedUser) and given tags. The tags are interpreted as
	 * supertags and the queries are built in a way that they results reflect
	 * the semantics of
	 * http://www.bibsonomy.org/bibtex/1d28c9f535d0f24eadb9d342168836199 p. 91,
	 * formular (4).<br/>
	 * 
	 * Additionally the group to be shown can be restricted. The queries are
	 * built in a way, that not only public posts are retrieved, but also
	 * friends or private or other groups, depending upon if userName us allowed
	 * to see them.
	 * 
	 * @param loginUser
	 * @param requestedUserName
	 * @param visibleGroupIDs 
	 * @param tagIndex
	 * @param caseSensitive
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsByConceptForUser(final String loginUser, final String requestedUserName, final List<Integer> visibleGroupIDs, final List<TagIndex> tagIndex, final boolean caseSensitive, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUser, requestedUserName, limit, offset);
		param.setGroups(visibleGroupIDs);
		param.setTagIndex(tagIndex);
		param.setCaseSensitiveTagNames(caseSensitive);
		param.addAllToSystemTags(systemTags);

		DatabaseUtils.checkPrivateFriendsGroup(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ByConceptForUser", param, session);
	}

	/** 
	 * <em>/tag/EinTag</em>, <em>/viewable/EineGruppe/EinTag</em><br/><br/>
	 * 
	 * On the <em>/tag</em> page only public entries are shown (groupType must
	 * be set to public) which have all of the given tags attached. On the
	 * <em>/viewable/</em> page only posts are shown which are set viewable to
	 * the given group and which have all of the given tags attached.
	 * @param groupId
	 * @param tagIndex
	 * @param order
	 * @param limit
	 * @param offset
	 * @param session
	 * @return a list of posts
	 * 
	 */
	public List<Post<R>> getPostsByTagNames(final int groupId, final List<TagIndex> tagIndex, final Order order, final int limit, final int offset, final DBSession session) {
		final P param = this.createParam(limit, offset);
		param.setGroupId(groupId);
		param.setTagIndex(tagIndex);
		param.setOrder(order);

		if (present(order)) {
			if (Order.FOLKRANK.equals(param.getOrder())){
				param.setGroupId(GroupID.PUBLIC);
				return this.postList("get" + this.resourceClassName + "ByTagNamesAndFolkrank", param, session);
			}
		}

		return this.postList("get" + this.resourceClassName + "ByTagNames", param, session);
	}	

	/**
	 * <em>/user/MaxMustermann/EinTag</em><br/><br/>
	 * 
	 * This method prepares queries which retrieve all resources for a given
	 * user name (requestedUser) and given tags.<br/>
	 * 
	 * Additionally the group to be shown can be restricted. The queries are
	 * built in a way, that not only public posts are retrieved, but also
	 * friends or private or other groups, depending upon if userName us allowed
	 * to see them.
	 * @param loginUserName 
	 * @param requestedUserName 
	 * @param tagIndex 
	 * @param groupId 
	 * @param visibleGroupIDs 
	 * @param limit 
	 * @param offset 
	 * @param filter
	 * @param systemTags
	 * @param session 
	 * @return list of resource posts
	 */
	public List<Post<R>> getPostsByTagNamesForUser(final String loginUserName, final String requestedUserName, final List<TagIndex> tagIndex, final int groupId, final List<Integer> visibleGroupIDs, final int limit, final int offset, final FilterEntity filter, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUserName, requestedUserName, limit, offset);
		param.setTagIndex(tagIndex);
		param.setGroupId(groupId);
		param.setGroups(visibleGroupIDs);
		param.setFilter(filter);
		param.addAllToSystemTags(systemTags);

		HashID.getSimHash(param.getSimHash()); 
		return this.getPostsByTagNamesForUser(param, session);
	}

	protected List<Post<R>> getPostsByTagNamesForUser(final P param, final DBSession session) {
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ByTagNamesForUser", param, session);
	}

	/**
	 * Counts the number of visible posts for a given list of tags
	 * 
	 * @param tagIndex a list of tags
	 * @param session DB session
	 * @param groupId is the group id
	 * @return the number of visible posts
	 */
	public Integer getPostsByTagNamesCount(final List<TagIndex> tagIndex, final int groupId, final DBSession session) {
		final P param = this.getNewParam();
		param.setGroupId(groupId);
		param.setTagIndex(tagIndex);

		final Integer result = this.queryForObject("get" + this.resourceClassName + "ByTagNamesCount", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * Retrieves the number of resource items tagged by the tags present in tagIndex by user requestedUserName
	 * being visible to the logged in user
	 * 
	 * @param requestedUserName
	 * 			owner of the resource items
	 * @param loginUserName
	 * 			logged in user
	 * @param tagIndex
	 * 			a list of tags
	 * @param visibleGroupIDs
	 * 			a list of groupIDs the logged in user is member of
	 * @param session
	 * 			DB session
	 * @return the corresponding number of visible resource items
	 */
	public int getPostsByTagNamesForUserCount(final String requestedUserName, final String loginUserName, final List<TagIndex> tagIndex, final List<Integer> visibleGroupIDs, final DBSession session) {
		final P param = this.getNewParam();
		param.addGroups(visibleGroupIDs);
		param.setRequestedUserName(requestedUserName);
		param.setUserName(loginUserName);
		param.setTagIndex(tagIndex);

		final Integer result = this.queryForObject("get" + this.resourceClassName + "ByTagNamesForUserCount", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * <em>/friends</em><br/><br/>
	 * 
	 * Prepares queries which show all posts of users which have currUser as
	 * their friend.
	 * 
	 * @param user
	 * @param simHash
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsByUserFriends(final String user, final HashID simHash, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(user, null, limit, offset);
		param.setSimHash(simHash);
		param.addAllToSystemTags(systemTags);

		param.setGroupId(GroupID.FRIENDS);
		return this.postList("get" + this.resourceClassName + "ByUserFriends", param, session);
	}
	
	/**
	 * Get list of all (public) posts from users for which the requesting user
	 * has added an accordingly tagged user relationship. BibSonomy's 
	 * friendship relation (=trust network) corresponds to the system tag 'sys:network:bibsonomy-friends' 
	 * 
	 * @param user
	 * @param tags
	 * @param userRelationTags 
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return a list of posts
	 */
	public List<Post<R>> getPostsByTaggedUserRelation(final String user, final Set<Tag> tags, final List<String> userRelationTags, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(user, null, limit, offset);
		param.addAllToSystemTags(systemTags);
		param.setTags(tags);
		param.addRelationTags(userRelationTags);
		
		if (SystemTagsUtil.containsSystemTag(userRelationTags, NetworkRelationSystemTag.BibSonomyFriendSystemTag)) {
			// the BibSonomy-Friend-Graph is the trust network
			param.setGroupId(GroupID.FRIENDS);
		} else {
			param.setGroupId(GroupID.PUBLIC);
		}
		
		return this.postList("get" + this.resourceClassName + "ByTaggedUserRelation", param, session);
	}

	/**
	 * TODO: improve docs
	 * 
	 * @param days
	 * @param limit
	 * @param offset
	 * @param hashId
	 * @param session 
	 * 
	 * @return list of posts
	 */
	public List<Post<R>> getPostsPopular(final int days, final int limit, final int offset, final HashID hashId, final DBSession session) {
		final P param = this.createParam(limit, offset);
		param.setDays(days);
		param.setSimHash(hashId);

		return this.postList("get" + this.resourceClassName + "Popular", param, session);
	}

	/** 
	 * @param days
	 * @param session
	 * @return the number of days when a post was popular
	 */
	public int getPostPopularDays(final int days, final DBSession session){
		final P param = this.getNewParam();
		param.setDays(days);

		final Integer result = this.queryForObject("get" + this.resourceClassName + "PopularDays", param, Integer.class, session);

		return present(result) ? result : 0;
	}

	/**
	 * This method prepares queries which retrieve all resources for the home
	 * page of BibSonomy. These are typically the X last posted entries. Only
	 * public posts are shown.
	 * 
	 * @param filter
	 * @param limit
	 * @param offset 
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsForHomepage(final FilterEntity filter, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(limit, offset);
		param.setGroupId(GroupID.PUBLIC);
		param.setSimHash(HashID.INTER_HASH);
		param.setFilter(filter);
		param.addAllToSystemTags(systemTags);

		return this.getPostsForHomepage(param, session);
	}

	/**
	 * @param param
	 * @param session
	 * @return list of posts for the homepage
	 */
	protected List<Post<R>> getPostsForHomepage(final P param, final DBSession session) {
		return this.postList("get" + this.resourceClassName + "ForHomepage", param, session);
	}

	/**
	 * Prepares a query which retrieves all posts which are represented by
	 * the given hash.
	 * 
	 * @param requResource
	 * @param simHash
	 * @param groupId
	 * @param limit
	 * @param offset
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsByHash(final String requResource, final HashID simHash, final int groupId, final int limit, final int offset, final DBSession session) {
		final P param = this.createParam(limit, offset);
		param.setHash(requResource);
		param.setSimHash(simHash);
		param.setGroupId(groupId);

		return this.postList("get" + this.resourceClassName + "ByHash", param, session);
	}

	/**
	 * Retrieves the number of posts represented by the given hash.
	 * 
	 * @param requHash 
	 * @param simHash 
	 * @param session
	 * @return number of posts for the given hash
	 */
	public int getPostsByHashCount(final String requHash, final HashID simHash, final DBSession session) {
		final P param = this.getNewParam();
		param.setHash(requHash);
		param.setSimHash(simHash);

		final Integer result = this.queryForObject("get" + this.resourceClassName + "ByHashCount", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * @param requHash 
	 * @param simHash 
	 * @param userName
	 * @param session
	 * @return number of resources for the given hash and a user
	 */
	public int getPostsByHashAndUserCount(final String requHash, final HashID simHash, final String userName, final DBSession session) {
		final P param = this.getNewParam();
		param.setUserName(userName);
		param.setHash(requHash);
		param.setSimHash(simHash);

		final Integer result = this.queryForObject("get" + this.resourceClassName + "ByHashAndUserCount", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * Prepares a query which retrieves the resources (which is represented by
	 * the given hash) for a given user. Since user name is given, full group
	 * checking is done, i.e. everybody who may see the resource will see it.
	 * 
	 * @param loginUserName
	 * @param requHash
	 * @param requestedUserName
	 * @param visibleGroupIDs
	 * @param hashType
	 * @param session
	 * @return list of resource posts
	 */
	public List<Post<R>> getPostsByHashForUser(final String loginUserName, final String requHash, final String requestedUserName, final List<Integer> visibleGroupIDs, final HashID hashType, final DBSession session) {
		final P param = this.createParam(loginUserName, requestedUserName);
		param.addGroups(visibleGroupIDs);
		param.setHash(requHash);
		param.setSimHash(hashType);

		DatabaseUtils.checkPrivateFriendsGroup(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ByHashForUser", param, session);
	}

	/**
	 * get list of posts from resource searcher
	 * 
	 * @param userName
	 * @param requestedUserName
	 * @param requestedGroupName
	 * @param allowedGroups
	 * @param searchTerms
	 * @param titleSearchTerms
	 * @param authorSearchTerms
	 * @param tagIndex
	 * @param year
	 * @param firstYear
	 * @param lastYear
	 * @param limit
	 * @param offset
	 * @return a list of posts
	 */
	public List<Post<R>> getPostsByResourceSearch(final String userName, final String requestedUserName, final String requestedGroupName, final Collection<String> allowedGroups,final String searchTerms, final String titleSearchTerms, final String authorSearchTerms, final Collection<String> tagIndex, final String year, final String firstYear, final String lastYear, final int limit, final int offset) {
		if (present(this.resourceSearch)) {
			return this.resourceSearch.getPosts(userName, requestedUserName, requestedGroupName, allowedGroups, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex, year, firstYear, lastYear, limit, offset);
		}

		log.error("no resource searcher is set");	
		return new LinkedList<Post<R>>();
	}
	
	/**  
	 * <em>/viewable/EineGruppe</em><br/><br/>
	 * 
	 * Prepares queries to retrieve posts which are set viewable to group.
	 * 
	 * @param requestedGroupName
	 * @param loginUserName 
	 * @param groupId
	 * @param simHash
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsViewable(final String requestedGroupName, final String loginUserName, final int groupId, final HashID simHash, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		if (GroupID.isSpecialGroupId(groupId)) {
			// show users own posts, which are private, public or for friends
			return this.getPostsForUser(loginUserName, loginUserName, HashID.INTER_HASH, groupId, new LinkedList<Integer>(), null, limit, offset, null, session);
		}

		final P param = this.createParam(loginUserName, null, limit, offset);
		param.setRequestedGroupName(requestedGroupName);
		param.setGroupId(groupId);
		param.setSimHash(simHash);
		param.addAllToSystemTags(systemTags);

		return this.postList("get" + this.resourceClassName + "Viewable", param, session);
	}

	/**
	 * TODO: check method
	 * 
	 * Returns viewable BibTexs for a given tag.
	 * @param loginUserName 
	 * @param requestedUserName
	 * @param tagIndex
	 * @param groupId
	 * @param filter
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsViewableByTag(final String loginUserName, final String requestedUserName, final List<TagIndex> tagIndex, final int groupId, final FilterEntity filter, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		if (GroupID.isSpecialGroupId(groupId)) {
			return this.getPostsByTagNamesForUser(loginUserName, requestedUserName, tagIndex, groupId, Collections.<Integer>emptyList(), limit, offset, filter, systemTags, session);
		}

		final P param = this.createParam(loginUserName, requestedUserName, limit, offset);
		param.setTagIndex(tagIndex);
		param.setGroupId(groupId);
		param.addAllToSystemTags(systemTags);

		return this.postList("get" + this.resourceClassName + "ViewableByTag", param, session);
	}

	/** 
	 * <em>/group/EineGruppe</em><br/><br/>
	 * 
	 * Prepares queries which show all posts of all users belonging to the
	 * group. This is an aggregated view of all posts of the group members.<br/>
	 * Full viewable-for checking is done, i.e. everybody sees everything he is
	 * allowed to see.<br/>
	 * 
	 * See also
	 * http://www.bibsonomy.org/bibtex/1d28c9f535d0f24eadb9d342168836199 page
	 * 92, formula (9) for formal semantics of this query.
	 * 
	 * @param groupId
	 * @param visibleGroupIDs 
	 * @param loginUserName
	 * @param simHash
	 * @param filter
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsForGroup(final int groupId, final List<Integer> visibleGroupIDs, final String loginUserName, final HashID simHash, final FilterEntity filter, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUserName, null, limit, offset);
		param.setGroupId(groupId);
		param.setGroups(visibleGroupIDs);
		param.setSimHash(simHash);
		param.setFilter(filter);
		param.addAllToSystemTags(systemTags);

		return this.getPostsForGroup(param, session);
	}

	protected List<Post<R>> getPostsForGroup(final P param, final DBSession session) {
		DatabaseUtils.prepareGetPostForGroup(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ForGroup", param, session);
	}

	/**
	 * Returns the number of posts belonging to the group.<br/><br/>
	 * 
	 * TODO: these are just approximations - users own private/friends posts
	 * and friends posts are not included (same for publications)
	 * 
	 * visibleGroupIDs && userName && (userName != requestedUserName) optional
	 * 
	 * @param requestedUserName 
	 * @param loginUserName 
	 * @param groupId
	 * @param visibleGroupIDs 
	 * @param session
	 * @return the (approximated) number of posts for the given group, see method above
	 */
	public int getPostsForGroupCount(final String requestedUserName, final String loginUserName, final int groupId, final List<Integer> visibleGroupIDs, final DBSession session) {
		final P param = this.createParam(loginUserName, requestedUserName);
		param.setGroups(visibleGroupIDs);
		param.setGroupId(groupId);

		DatabaseUtils.checkPrivateFriendsGroup(this.generalDb, param, session);
		final Integer result = this.queryForObject("get" + this.resourceClassName + "ForGroupCount", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * TODO: improve docs
	 * 
	 * @param requestedUserName
	 * @param loginUserName
	 * @param limit 
	 * @param offset 
	 * @param visibleGroupIDs
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsForMyGroupPosts(final String requestedUserName, final String loginUserName, final int limit, final int offset, final List<Integer> visibleGroupIDs, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUserName, requestedUserName, limit, offset);
		param.setGroups(visibleGroupIDs);
		param.addAllToSystemTags(systemTags);

		return this.postList("get" + this.resourceClassName + "ForMyGroupPosts", param, session);
	}

	/**
	 * TODO: improve docs
	 * 
	 * @param requestedUserName
	 * @param loginUserName
	 * @param tagIndex
	 * @param limit
	 * @param offset
	 * @param visibleGroupIDs
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsForMyGroupPostsByTag(final String requestedUserName, final String loginUserName, final List<TagIndex> tagIndex, final int limit, final int offset, final List<Integer> visibleGroupIDs, final Collection<SystemTag> systemTags, final DBSession session){
		final P param = this.createParam(loginUserName, requestedUserName, limit, offset);
		param.setTagIndex(tagIndex);
		param.setGroups(visibleGroupIDs);
		param.addAllToSystemTags(systemTags);

		return this.postList("get" + this.resourceClassName + "ForMyGroupPostsByTag", param, session);
	}

	/**  
	 * <em>/group/EineGruppe/EinTag+NochEinTag</em><br/><br/>
	 * 
	 * Does basically the same as getPostsForGroup with the additionally
	 * possibility to restrict the tags the posts have to have.
	 * 
	 * @param groupId
	 * @param visibleGroupIDs 
	 * @param loginUserName
	 * @param tagIndex
	 * @param filter
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsForGroupByTag(final int groupId, final List<Integer> visibleGroupIDs, final String loginUserName, final List<TagIndex> tagIndex, final FilterEntity filter, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUserName, null, limit, offset);
		param.setGroupId(groupId); 
		param.setGroups(visibleGroupIDs);
		param.setTagIndex(tagIndex);
		param.setFilter(filter);
		param.addAllToSystemTags(systemTags);

		return getPostsForGroupByTag(param, session);
	}

	protected List<Post<R>> getPostsForGroupByTag(final P param, final DBSession session) {
		DatabaseUtils.prepareGetPostForGroup(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ForGroupByTag", param, session);
	}

	protected List<Post<R>> getPostsForUser(final P param, final DBSession session) {
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.postList("get" + this.resourceClassName + "ForUser", param, session);
	}

	/** 
	 * <em>/user/MaxMustermann</em><br/><br/>
	 * 
	 * This method prepares queries which retrieve all posts for a given
	 * user name (requestedUserName). Additionally the group to be shown can be
	 * restricted. The queries are built in a way, that not only public posts
	 * are retrieved, but also friends or private or other groups, depending
	 * upon if userName is allowed to see them.
	 * 
	 * ATTENTION! in case of a given groupId it is NOT checked if the user
	 * actually belongs to this group.
	 * 
	 * @param loginUserName
	 * @param requestedUserName
	 * @param simHash
	 * @param groupId
	 * @param visibleGroupIDs 
	 * @param filter
	 * @param limit
	 * @param offset
	 * @param systemTags
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsForUser(final String loginUserName, final String requestedUserName, final HashID simHash, final int groupId, final List<Integer> visibleGroupIDs, final FilterEntity filter, final int limit, final int offset, final Collection<SystemTag> systemTags, final DBSession session) {
		final P param = this.createParam(loginUserName, requestedUserName, limit, offset);
		param.setGroupId(groupId);
		param.setGroups(visibleGroupIDs);
		param.setSimHash(simHash);
		param.setFilter(filter);
		param.addAllToSystemTags(systemTags);

		return this.getPostsForUser(param, session);
	}

	/**
	 * Returns the number of posts for a given user.
	 * 
	 * @param requestedUserName 
	 * @param loginUserName 
	 * @param groupId 
	 * @param visibleGroupIDs 
	 * @param session
	 * @return the number of posts of the requested User which the logged in user is allowed to see
	 * 
	 * groupId or
	 * visibleGroupIDs && userName && (userName != requestedUserName)
	 */
	public int getPostsForUserCount(final String requestedUserName, final String loginUserName, final int groupId, final List<Integer> visibleGroupIDs, final DBSession session) {
		final P param = this.createParam(loginUserName, requestedUserName);
		param.setGroupId(groupId);
		param.setGroups(visibleGroupIDs);

		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session); // set groups
		final Integer result = this.queryForObject("get" + this.resourceClassName + "ForUserCount", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * Get posts of users which the logged-in users is following.
	 * 
	 * @param loginUserName - 
	 * @param visibleGroupIDs
	 * @param limit
	 * @param offset
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsByFollowedUsers(final String loginUserName, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		final P param = this.createParam(loginUserName, null, limit, offset);
		param.setGroups(visibleGroupIDs);

		return this.postList("get" + this.resourceClassName + "ByFollowedUsers", param, session);
	}

	/**
	 * Returns a contentId for a given post.
	 * 
	 * @param hash
	 * @param requestedUserName
	 * @param session
	 * @return contentId for a given post
	 */
	protected Integer getContentIdForPost(final String hash, final String requestedUserName, final DBSession session) {
		if (!present(hash) || !present(requestedUserName)) {
			throw new RuntimeException("Hash and user name must be set");
		}

		final P param = this.getNewParam();
		param.setHash(hash);
		param.setRequestedUserName(requestedUserName);
		param.setSimHash(HashID.INTRA_HASH);

		return this.queryForObject("getContentIdFor" + this.resourceClassName, param, Integer.class, session);
	}

	/**
	 * TODO: documentation
	 * 
	 * @param requestedUserName
	 * @param loginUserName
	 * @param tagIndex
	 * @param visibleGroupIDs
	 * @param session
	 * @return number of posts that are available for some groups and tagged by a tag of the tagIndex
	 */
	public int getGroupPostsCountByTag(final String requestedUserName, final String loginUserName, final List<TagIndex> tagIndex, final List<Integer> visibleGroupIDs, final DBSession session){			
		final P param = this.createParam(loginUserName, requestedUserName);
		param.setTagIndex(tagIndex);
		param.setGroups(visibleGroupIDs);

		final Integer result = this.queryForObject("getGroup" + this.resourceClassName + "CountByTag", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * TODO: documentation
	 * 
	 * @param requestedUserName
	 * @param loginUserName
	 * @param visibleGroupIDs
	 * @param session
	 * @return number of posts that are available for some groups
	 */
	public int getGroupPostsCount(final String requestedUserName, final String loginUserName, final List<Integer> visibleGroupIDs, final DBSession session){
		final P param = this.createParam(loginUserName, requestedUserName);
		param.setGroups(visibleGroupIDs);

		final Integer result = this.queryForObject("getGroup" + this.resourceClassName + "Count", param, Integer.class, session);
		return present(result) ? result : 0;
	}

	/**
	 * This method prepares a query which retrieves all posts the user
	 * has in his basket list. The result is shown on the page
	 * <em>/basket</em>. Since every user can only see his <em>own</em>
	 * basket page, we use userName as restriction for the user name and not
	 * requestedUserName.
	 * 
	 * @param loginUser
	 * @param limit
	 * @param offset
	 * @param session
	 * @return list of posts
	 */
	public List<Post<R>> getPostsFromBasketForUser(final String loginUser, final int limit, final int offset, final DBSession session) {
		final P param = this.createParam(loginUser, null, limit, offset);
		param.setSimHash(HashID.INTER_HASH);

		return this.postList("get" + this.resourceClassName + "FromBasketForUser", param, session);
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.CrudableContent#getPosts(org.bibsonomy.database.params.P, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	public List<Post<R>> getPosts(final P param, final DBSession session) {
		return this.getChain().getFirstElement().perform(param, session);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.CrudableContent#getPostsDetails(java.lang.String, java.lang.String, java.lang.String, java.util.List, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	public Post<R> getPostDetails(final String loginUserName, final String resourceHash, final String requestedUserName, final List<Integer> visibleGroupIDs, final DBSession session) throws ResourceMovedException, ResourceNotFoundException {
		final List<Post<R>> list = this.getPostsByHashForUser(loginUserName, resourceHash, requestedUserName, visibleGroupIDs, HashID.INTRA_HASH, session);

		if (list.isEmpty()) {
			log.debug(this.resourceClassName + "-posts from user '" + requestedUserName + "' with hash '" + resourceHash + "' for user '" + loginUserName + "' not found");
			return null;
		}

		if (list.size() > 1) {
			log.warn("multiple " + this.resourceClassName + "-posts from user '" + requestedUserName + "' with hash '" + resourceHash + "' for user '" + loginUserName + "' found ->returning first");
		}
		final Post<R> post = list.get(0);
		/*
		 * If one of the post's groups is neither public nor private
		 * (i.e., it is friends or a "regular" group) we must get
		 * the (remaining) groups from the grouptas table.
		 */
		if (!GroupUtils.isExclusiveGroup(post.getGroups().iterator().next())) {
			/*
			 * neither public nor private ... ... get the groups
			 * from the grouptas table
			 */
			post.setGroups(new HashSet<Group>(this.groupDb.getGroupsForContentId(post.getContentId(), session)));
		}
		
		return post;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.CrudableContent#createPost(org.bibsonomy.model.Post, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	public boolean createPost(final Post<R> post, final DBSession session) {
		session.beginTransaction();
		try {
			this.checkPost(post, session);

			/*
			 * systemtags perform before create
			 */
			final List<ExecutableSystemTag> systemTags = SystemTagsExtractor.extractExecutableSystemTags(post.getTags(), new HashSet<Tag>());
			for (final ExecutableSystemTag systemTag: systemTags) {
				systemTag.performBeforeCreate(post, session);
			}
			final String userName = post.getUser().getName();
			/*
			 * the current intra hash of the resource
			 */
			final String intraHash = post.getResource().getIntraHash();
			/*
			 * get posts with the intrahash of the given post to check for possible duplicates 
			 */
			Post<R> postInDB = null;
			try {
				postInDB = this.getPostDetails(userName, intraHash, userName, new ArrayList<Integer>(), session);
			} catch(final ResourceMovedException ex) {
				/*
				 * getPostDetails() throws a ResourceMovedException for hashes for which
				 * no actual post exists, but an old post has existed with that hash.
				 * 
				 * Since we are not interested in former posts with that hash we ignore
				 * this exception silently. 
				 */
			}
			/*
			 * check if user is trying to create a resource that already exists
			 */
			if (present(postInDB)) {
				final ErrorMessage errorMessage = new DuplicatePostErrorMessage(this.resourceClassName, post.getResource().getIntraHash());
				session.addError(post.getResource().getIntraHash(), errorMessage);
				log.warn("Added DuplicatePostErrorMessage for post " + post.getResource().getIntraHash());
				session.commitTransaction();
				return false;
			}
			/*
			 * ALWAYS get a new contentId
			 */
			post.setContentId(this.generalDb.getNewId(ConstantID.IDS_CONTENT_ID, session));
			/*
			 * on update, do a delete first ...
			 */
			this.insertPost(post, session);
			// add the tags
			this.tagDb.insertTags(post, session);
			/*
			 * systemTags perform after create
			 */
			for (final ExecutableSystemTag systemTag: systemTags) {
				systemTag.performAfterCreate(post, session);
			}
			session.commitTransaction();
		} finally {
			session.endTransaction();
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.CrudableContent#updatePost(org.bibsonomy.model.Post, java.lang.String, org.bibsonomy.common.enums.PostUpdateOperation, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	public boolean updatePost(final Post<R> post, final String oldHash, final PostUpdateOperation operation, final DBSession session, final User loginUser) {
		final String userName = post.getUser().getName();
		session.beginTransaction();
		try {
			/*
			 * the resource with the "old" intrahash, that was sent
			 * within the update resource request
			 */
			Post<R> oldPost = null;
			if (present(oldHash)) {
				// if yes, check if a post exists with the old intrahash
				try {
					oldPost = this.getPostDetails(loginUser.getName(), oldHash, userName, new ArrayList<Integer>(), session);
				} catch(final ResourceMovedException ex) {
					/*
					 * getPostDetails() throws a ResourceMovedException for hashes for which
					 * no actual post exists, but an old post has existed with that hash.
					 * 
					 * Since we are not interested in former posts with that hash we ignore
					 * this exception silently. 
					 */
				}
				/*
				 * check if post to update is in db
				 */
				if (!present(oldPost)) {
					/*
					 * not found -> throw exception
					 */
					final ErrorMessage errorMessage = new UpdatePostErrorMessage(this.resourceClassName, post.getResource().getIntraHash());
					session.addError(post.getResource().getIntraHash(), errorMessage);
					// we have to commit to adjust counters in session otherwise we will not get the DatabaseException from the session
					session.commitTransaction();
					log.warn("Added UpdatePostErrorMessage (" + this.resourceClassName + " with hash " + oldHash + " does not exist for user " + userName + ")");
					return false;
				}

			} else {
				// we do not add this to the databaseException since this an error not caused by a user
				throw new IllegalArgumentException("Could not update post: no intrahash specified.");
			}
			
			/*
			 * don't change groups in case of synchronization 
			 */
			if (Role.SYNC.equals(loginUser.getRole())) {
				post.setGroups(oldPost.getGroups());
			}
			
			/*
			 * perform system tags before update
			 */
			final List<ExecutableSystemTag> executableSystemTags = SystemTagsExtractor.extractExecutableSystemTags(post.getTags(), oldPost.getTags());
			for (final ExecutableSystemTag systemTag : executableSystemTags) {
				systemTag.performBeforeUpdate(post, oldPost, operation, session);
			}

			/*
			 * Only when we update the complete post, we must recalculate the hash, because the
			 * hash might have changed and otherwise we could not check if a post with the new
			 * hash already exists. 
			 */
			if (PostUpdateOperation.UPDATE_ALL.equals(operation)) {
				post.getResource().recalculateHashes();
			}

			/*
			 * the current intra hash of the resource
			 */
			final String intraHash = post.getResource().getIntraHash();


			/*
			 * get posts with the intrahash of the given post to check for possible duplicates 
			 */
			Post<R> newPostInDB = null; 
			try {
				newPostInDB = this.getPostDetails(loginUser.getName(), intraHash, userName, new ArrayList<Integer>(), session);
			} catch (final ResourceMovedException ex) {
				/*
				 * getPostDetails() throws a ResourceMovedException for hashes for which
				 * no actual post exists, but an old post has existed with that hash.
				 * 
				 * Since we are not interested in former posts with that hash we ignore
				 * this exception silently. 
				 */
			}
			/*
			 * check if user is trying to create a resource that already exists
			 */
			if (present(newPostInDB)) {
				/*
				 * new resource exists ... 
				 */
				if (!intraHash.equals(oldHash)) {
					/* 
					 * Although we're doing an update, the old intra hash is different from the new one
					 * in principle, this is OK, but not when the new hash already exists. Because that
					 * way we would delete the post with the old hash and post the new one - resulting
					 * in two posts with the same (new hash)
					 */
					final ErrorMessage errorMessage = new IdenticalHashErrorMessage(this.resourceClassName, post.getResource().getIntraHash());
					session.addError(post.getResource().getIntraHash(), errorMessage);
					// we have to commit to adjust counters in session otherwise we will not get the DatabaseException from the session
					session.commitTransaction();
					return false;

				}
			}

			/*
			 * set the old creation date
			 */
			post.setDate(oldPost.getDate());
			
			/*
			 * now execute the postupdate operation
			 */
			if (present(operation)) {
				this.workOnOperation(post, oldPost, operation, session);
			} else {
				this.performUpdateAll(post, oldPost, session);
			}		
			/*
			 * systemTags perform after Update
			 */
			for (final ExecutableSystemTag systemTag: executableSystemTags) {
				systemTag.performAfterUpdate(post, oldPost, operation, session);
			}
			session.commitTransaction();
		} finally {
			session.endTransaction();
		}
		return true;
	}

	protected void workOnOperation(final Post<R> post, final Post<R> oldPost, final PostUpdateOperation operation, final DBSession session) {
		switch (operation) {
		case UPDATE_TAGS:
			this.performUpdateOnlyTags(post, oldPost, session);
			break;
		default:
			/*
			 * as default update all parts of a post
			 */
			this.performUpdateAll(post, oldPost, session);
		}
	}

	protected void checkPost(final Post<R> post, final DBSession session) {
		final R resource = post.getResource();
		this.validator.validateFieldLength(resource, resource.getIntraHash(), session);			
	}

	private void performUpdateAll(final Post<R> post, final Post<R> oldPost, final DBSession session) {
		session.beginTransaction();
		try {
			/*
			 * first check the post
			 */
			this.checkPost(post, session);

			/*
			 * ALWAYS get a new contentId
			 */
			post.setContentId(this.generalDb.getNewId(ConstantID.IDS_CONTENT_ID, session));

			/*
			 * inform the listeners
			 */
			this.onPostUpdate(oldPost.getContentId(), post.getContentId(), session);

			/*
			 * delete old post
			 */
			this.deletePost(oldPost, true, session);

			/*
			 * insert new post
			 */
			this.insertPost(post, session);

			/* 
			 * add the tags
			 */
			this.tagDb.insertTags(post, session);

			session.commitTransaction();
		} finally {
			session.endTransaction();
		}
	}

	/**
	 * updates only the tags of the given post
	 * 
	 * @param post	the post to update
	 * @param oldPost	the old post in database
	 * @param session
	 */
	private void performUpdateOnlyTags(final Post<R> post, final Post<R> oldPost, final DBSession session) {		
		session.beginTransaction();
		try {
			/*
			 * delete old tags
			 */
			this.tagDb.deleteTags(oldPost, session);
			/*
			 * fill the new posts with data from the old post that
			 * should not change (e.g., date, user name, groups)
			 */
			post.setUser(oldPost.getUser());
			post.setGroups(oldPost.getGroups());
			post.setContentId(oldPost.getContentId());
			post.setDate(oldPost.getDate());
			post.setResource(oldPost.getResource());
			/*
			 * insert new tags
			 */
			this.tagDb.insertTags(post, session);

			session.commitTransaction();			
		} finally {
			session.endTransaction();
		}
	}

	/**
	 * called when a post was updated
	 * 
	 * @param oldContentId	the old content id of the post
	 * @param newContentId	the new content id of the post
	 * @param session
	 */
	protected abstract void onPostUpdate(Integer oldContentId, Integer newContentId, DBSession session);

	/**
	 * inserts a post into the database
	 *
	 * @param post		the post to insert
	 * @param session
	 */
	protected void insertPost(final Post<R> post, final DBSession session) {
		boolean errors = false;
		if (!present(post.getResource())) {
			final ErrorMessage errorMessage = new MissingFieldErrorMessage("Resource");
			session.addError(post.getResource().getIntraHash(), errorMessage);
			errors = true;
		}
		if (!present(post.getGroups())) {
			final ErrorMessage errorMessage = new MissingFieldErrorMessage("Groups");
			session.addError(post.getResource().getIntraHash(), errorMessage);
			errors = true;
		}
		if (errors) {
			// one or more errors occurred in this method 
			// => we don't want to go deeper into the process with these kinds of errors
			log.error("Added MissingFieldErrorMessage for post " + post.getResource().getIntraHash());
			return;
		}

		final P param = this.getInsertParam(post, session);
		// insert
		this.insertPost(param, session);
	}

	/**
	 * inserts a new post in db
	 * 
	 * @param param
	 * @param session
	 */
	protected void insertPost(final P param, final DBSession session) {
		session.beginTransaction();
		try {
			// Insert resource
			this.insert("insert" + this.resourceClassName, param, session);
			// Insert/Update SimHashes
			this.insertOrUpdatePostHash(param, session, false);

			session.commitTransaction();
		} finally {
			session.endTransaction();
		}
	}

	/**
	 * TODO: check this method
	 * inserts or updates the post hashes for the given resource (in param)
	 * 
	 * @param param
	 * @param session
	 * @param delete
	 */
	protected void insertOrUpdatePostHash(final P param, final DBSession session, final boolean delete) {
		for (final HashID hashId : this.getHashRange()) {
			final String hash = SimHash.getSimHash(param.getResource(), hashId);
			// no action on an empty hash
			if (present(hash)) {
				param.setSimHash(hashId);
				param.setHash(hash);

				if (delete) {
					// decrement counter
					this.update("update" + this.resourceClassName + "Hash", param, session);
				} else {
					// insert new hash or increment its counter, if it already exists
					this.insert("insert" + this.resourceClassName + "Hash", param, session);
				}
			} 				
		}
	}

	/**
	 * @return the hash range of the resource
	 */
	protected abstract HashID[] getHashRange();

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.CrudableContent#deletePost(java.lang.String, java.lang.String, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	public boolean deletePost(final String userName, final String resourceHash, final DBSession session) {
		Post<R> post = null;
		try {
			post = this.getPostDetails(userName, resourceHash, userName, new ArrayList<Integer>(), session);
		} catch (final ResourceMovedException ex) {
			// ignore
		} catch (final ResourceNotFoundException ex) {
			// ignore
		}

		if (!present(post)) {
			log.debug("post with hash \"" + resourceHash + "\" not found");
			return false;
		}

		return this.deletePost(post, false, session);
	}

	/**
	 * deletes a post from the database
	 * 
	 * @param post			the post to delete
	 * @param update		<code>true</code> if its called by {@link PostDatabaseManager#update(String, Object, DBSession)}
	 * @param session
	 * @return <code>true</code> iff the post was deleted successfully
	 */
	protected boolean deletePost(final Post<? extends R> post, final boolean update, final DBSession session) {
		session.beginTransaction();
		try {
			final String userName = post.getUser().getName();

			final R resource = post.getResource();
			final String resourceHash = resource.getIntraHash();

			// Used for userName, hash and contentId
			final P param = this.createParam(userName, userName);
			param.setHash(resourceHash);
			param.setRequestedContentId(post.getContentId());
			param.setResource(resource);

			if (!update) {
				this.onPostDelete(post.getContentId(), session);
			}

			this.tagDb.deleteTags(post, session);

			this.insertOrUpdatePostHash(param, session, true);

			this.delete("delete" + this.resourceClassName, param, session);

			session.commitTransaction();

		} finally {
			session.endTransaction();
		}

		return true;
	}

	/**
	 * called when a post was deleted successfully
	 * 
	 * @param contentId	the content id of the post which was deleted
	 * @param session
	 */
	protected abstract void onPostDelete(Integer contentId, DBSession session);

	/**
	 * @return the chain
	 */
	protected abstract FirstListChainElement<Post<R>, P> getChain();

	/**
	 * @return the simple class name of the first generic param (<R>, Resource)
	 */
	protected String getResourceClassName() {
		return ReflectionUtils.getActualClassArguments(this.getClass()).get(0).getSimpleName();
	}

	/** 
	 * @return a new <P> param
	 */
	protected abstract P getNewParam();

	protected P createParam(final String loginUserName, final String requestedUserName) {
		final P param = this.getNewParam();

		param.setUserName(loginUserName);
		param.setRequestedUserName(requestedUserName);

		return param;
	}

	protected P createParam(final int limit, final int offset) {
		final P param = this.getNewParam();

		param.setLimit(limit);
		param.setOffset(offset);

		return param;
	}

	protected P createParam(final String loginUserName, final String requestedUserName, final int limit, final int offset) {
		final P param = this.createParam(limit, offset);
		param.setUserName(loginUserName);
		param.setRequestedUserName(requestedUserName);

		return param;
	}

	/**
	 * @param post
	 * @param session
	 * @return new param for insert a resource
	 */
	protected abstract P getInsertParam(final Post<? extends R> post, final DBSession session);

}
