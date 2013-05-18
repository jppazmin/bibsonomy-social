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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.params.beans.TagIndex;
import org.bibsonomy.database.managers.chain.statistic.post.PostStatisticChain;
import org.bibsonomy.database.managers.chain.statistic.tag.TagStatisticChain;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.database.params.StatisticsParam;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;

/**
 * @author Dominik Benz
 * @author Stefan Stützer
 * @version $Id: StatisticsDatabaseManager.java,v 1.26 2011-05-09 12:08:43 nosebrain Exp $
 */
public class StatisticsDatabaseManager extends AbstractDatabaseManager {

	private static final StatisticsDatabaseManager singleton = new StatisticsDatabaseManager();

	private static final PostStatisticChain postchain = new PostStatisticChain();
	private static final TagStatisticChain tagChain = new TagStatisticChain();

	/**
	 * @return StatisticsDatabaseManager
	 */
	public static StatisticsDatabaseManager getInstance() {
		return singleton;
	}

	private final BibTexDatabaseManager bibtexDBManager;
	private final BookmarkDatabaseManager bookmarkDBManager;
	private final Map<Class<? extends Resource>, PostDatabaseManager<? extends Resource, ? extends ResourceParam<? extends Resource>>> postDatabaseManager;

	private StatisticsDatabaseManager() {
		this.bibtexDBManager = BibTexDatabaseManager.getInstance();
		this.bookmarkDBManager = BookmarkDatabaseManager.getInstance();

		// TODO: refactor @see DBLogic
		this.postDatabaseManager = new HashMap<Class<? extends Resource>, PostDatabaseManager<? extends Resource, ? extends ResourceParam<? extends Resource>>>();
		this.postDatabaseManager.put(Bookmark.class, this.bookmarkDBManager);
		this.postDatabaseManager.put(BibTex.class, this.bibtexDBManager);
	}

	/**
	 * @param param
	 * @param session
	 * @return The number of posts matching the given params.
	 * 
	 */
	public int getPostStatistics(final StatisticsParam param, final DBSession session) {
		final Integer count = postchain.getFirstElement().perform(param, session);  
		// to not get NPEs later
		return count == null ? 0 : count;
	}

	/**
	 * @param param
	 * @param session
	 * @return The number of tags matching the given params
	 */
	public int getTagStatistics(final StatisticsParam param, final DBSession session) {
		final Integer count = tagChain.getFirstElement().perform(param, session);
		// to not get NPEs later
		return count == null ? 0 : count;
	}

	/**
	 * @param param
	 * @param session
	 * @return number of relations from a user
	 */
	public int getNumberOfRelationsForUser(final StatisticsParam param, final DBSession session) {
		final Integer count = this.queryForObject("getNumberOfRelationsForUser", param.getRequestedUserName(), Integer.class, session);
		return count == null ? 0 : count;
	}

	/**
	 * @param resourceType
	 * @param requestedUserName
	 * @param userName 
	 * @param groupId 
	 * @param visibleGroupIDs 
	 * @param session
	 * @return a statistical number (int)
	 */
	public int getNumberOfResourcesForUser(final Class<? extends Resource> resourceType, final String requestedUserName, final String userName, final int groupId, final List<Integer> visibleGroupIDs, final DBSession session) {
		return this.getDatabaseManagerForResourceType(resourceType).getPostsForUserCount(requestedUserName, userName, groupId, visibleGroupIDs, session);
	}

	/**
	 * @param resourceType
	 * @param requHash 
	 * @param simHash 
	 * @param session
	 * @return a statistical number (int)
	 */
	public int getNumberOfResourcesForHash(final Class<? extends Resource> resourceType, final String requHash, final HashID simHash, final DBSession session) {
		return this.getDatabaseManagerForResourceType(resourceType).getPostsByHashCount(requHash, simHash, session);	
	}

	/**
	 * @param resourceType
	 * @param requHash 
	 * @param simHash 
	 * @param userName
	 * @param session
	 * @return a statistical number (int)
	 */
	public int getNumberOfResourcesForHashAndUser(final Class<? extends Resource> resourceType, final String requHash, final HashID simHash, final String userName, final DBSession session) {
		return this.getDatabaseManagerForResourceType(resourceType).getPostsByHashAndUserCount(requHash, simHash, userName, session);
	}

	/**
	 * Returns the number of resources of the given group
	 * 
	 * @param resourceType
	 * @param requestedUserName 
	 * @param userName 
	 * @param groupId
	 * @param visibleGroupIDs
	 * @param session
	 * @return number of resources for given group
	 */
	public int getNumberOfResourcesForGroup(final Class<? extends Resource> resourceType, final String requestedUserName, final String userName, final int groupId, final List<Integer> visibleGroupIDs, final DBSession session) {
		return this.getDatabaseManagerForResourceType(resourceType).getPostsForGroupCount(requestedUserName, userName, groupId, visibleGroupIDs, session);
	}

	/**
	 * Returns the number of resources for a list of tags
	 * 
	 * @param resourceType
	 * @param tagIndex 
	 * @param groupId 
	 * @param session
	 * @return number of resources for a list of tags
	 */
	public int getNumberOfResourcesForTags(final Class<? extends Resource> resourceType, final List<TagIndex> tagIndex, final int groupId, final DBSession session) {
		return this.getDatabaseManagerForResourceType(resourceType).getPostsByTagNamesCount(tagIndex, groupId, session);
	}

	/**
	 * Returns the number of resources for a given user and a list of tags
	 * 
	 * @param resourceType
	 * @param tagIndex
	 * @param requestedUserName
	 * @param loginUserName
	 * @param visibleGroupIDs
	 * @param session
	 * @return number of resources for a given user and a list of tags
	 */
	public int getNumberOfResourcesForUserAndTags(final Class<? extends Resource> resourceType, final List<TagIndex> tagIndex, final String requestedUserName, final String loginUserName, final List<Integer> visibleGroupIDs, final DBSession session) {
		return this.getDatabaseManagerForResourceType(resourceType).getPostsByTagNamesForUserCount(requestedUserName, loginUserName, tagIndex, visibleGroupIDs, session);
	}

	/**
	 * Returns the number of resources for a given user that occur at least twice
	 * 
	 * @param resourceType
	 * @param requestedUserName 
	 * @param session
	 * @return number of resources  that occur at least twice
	 */
	public int getNumberOfDuplicates(final Class<? extends Resource> resourceType, final String requestedUserName, final DBSession session) {
		if (resourceType == BibTex.class) {
			return this.bibtexDBManager.getPostsDuplicateCount(requestedUserName, session);
		}

		throw new UnsupportedResourceTypeException("Resource type " + resourceType + " not supported for this query.");
	}

	/**
	 * TODO: document me...
	 * 
	 * @param tagName
	 * @return tag global count
	 */
	public int getTagGlobalCount(final String tagName) {
		// FIXME: implement me...
		return 0;
	}	

	/**
	 * 
	 * @param resourceType
	 * @param requestedUserName
	 * @param loginUserName
	 * @param visibleGroupIDs
	 * @param session
	 * @return number of resources that are available for some groups
	 */
	public int getNumberOfResourcesForUserAndGroup(final Class<? extends Resource> resourceType, final String requestedUserName, final String loginUserName, final List<Integer> visibleGroupIDs, final DBSession session){
		return this.getDatabaseManagerForResourceType(resourceType).getGroupPostsCount(requestedUserName, loginUserName, visibleGroupIDs, session);
	}

	/**
	 * @param resourceType
	 * @param requestedUserName
	 * @param tagIndex
	 * @param loginUserName
	 * @param visibleGroupIDs
	 * @param session
	 * @return number of resources that are available for some groups and tagged by a tag of the tagIndex
	 */
	public int getNumberOfResourcesForUserAndGroupByTag(final Class<? extends Resource> resourceType, final String requestedUserName, final List<TagIndex> tagIndex, final String loginUserName, final List<Integer> visibleGroupIDs, final DBSession session){
		return this.getDatabaseManagerForResourceType(resourceType).getGroupPostsCountByTag(requestedUserName, loginUserName, tagIndex, visibleGroupIDs, session);
	}

	/**
	 * @param resourceType
	 * @param days
	 * @param session
	 * @return the number of days when a resource was popular
	 */
	public int getPopularDays(final Class<? extends Resource> resourceType, final int days, final DBSession session){
		return this.getDatabaseManagerForResourceType(resourceType).getPostPopularDays(days, session);
	}

	private PostDatabaseManager<? extends Resource, ? extends ResourceParam<? extends Resource>> getDatabaseManagerForResourceType(final Class<? extends Resource> resourceType) {
		if (this.postDatabaseManager.containsKey(resourceType)) {
			return this.postDatabaseManager.get(resourceType);
		}

		throw new UnsupportedResourceTypeException("Resource type " + resourceType.getSimpleName() + " not supported for this query.");
	}
}