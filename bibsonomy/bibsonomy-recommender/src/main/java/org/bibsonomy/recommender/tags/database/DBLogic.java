/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags.database;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.database.params.Pair;
import org.bibsonomy.recommender.tags.database.params.RecQueryParam;
import org.bibsonomy.recommender.tags.database.params.RecSettingParam;
import org.bibsonomy.recommender.tags.database.params.RecAdminOverview;
import org.bibsonomy.recommender.tags.database.params.SelectorSettingParam;
import org.bibsonomy.recommender.tags.database.params.TasEntry;

/**
 * @author rja
 * @version $Id: DBLogic.java,v 1.10 2010-07-05 15:28:15 bsc Exp $
 */
public interface DBLogic {

	//------------------------------------------------------------------------
	// database access interface
	//------------------------------------------------------------------------
	/**
	 * Add new query to database.
	 * 
	 * @param userName user who submitted post
	 * @param date querie's timestamp
	 * @param post user's post
	 * @param postID TODO
	 * @param timeout querie's timeout value
	 * @return unique query id
	 * 
	 * @throws SQLException 
	 */
	public Long addQuery(String userName, Date date, Post<? extends Resource> post, int postID, int timeout) throws SQLException;

	/**
	 * Add recommender to given query.
	 * 
	 * @param queryId
	 * @param recId
	 * @param recDescr 
	 * @param recMeta
	 * @return unique identifier for given recommender settings
	 * @throws SQLException 
	 */
	public Long addRecommender(Long queryId, String recId, String recDescr, byte[] recMeta) throws SQLException;

	/**
	 * adds given recommender (identified by it's id) to given query
	 * 
	 * @param qid query's id
	 * @param sid recommender's id
	 * @throws SQLException
	 */
	public void addRecommenderToQuery(Long qid, Long sid ) throws SQLException;
	
	/**
	 * insert recommender setting into table - if given setting already exists,
	 * return its id. This should always be embedded into a transaction.
	 * @param recId 
	 * @param recDescr 
	 * @param recMeta 
	 * 
	 * @return unique identifier for given settings
	 * @throws SQLException
	 */
	public Long insertRecommenderSetting(String recId, String recDescr, byte[] recMeta) throws SQLException;
	
	/**
	 * Add result selector to given query.
	 * @param qid query id
	 * @param selectorInfo 
	 * @param selectorMeta 
	 * 
	 * @return TODO
	 * @throws SQLException 
	 */
	public Long addResultSelector(Long qid, String selectorInfo, byte[] selectorMeta) throws SQLException;

	/**
	 * set selection strategy which was applied in given query
	 * 
	 * @param qid query's id
	 * @param rid result selector's id
	 * @throws SQLException
	 */
	public void setResultSelectorToQuery(Long qid, Long rid ) throws SQLException;

   /**
	 * insert selector setting into table - if given setting already exists,
	 * return its id. This should always be embedded into a transaction.
	 * @param selectorInfo 
	 * @param selectorMeta 
	 * 
	 * @return unique identifier for given settings
	 * @throws SQLException 
	 */
	public Long insertSelectorSetting(String selectorInfo, byte[] selectorMeta) throws SQLException;

	/**
	 * Add id of recommender selected for given query.
	 * 
	 * @param qid query_id 
	 * @param sid recommender's setting id
	 * @throws SQLException 
	 */
	public void addSelectedRecommender(Long qid, Long sid) throws SQLException;

	/**
	 * Add recommender's recommended tags.
	 * 
	 * @param queryId unique id identifying query
	 * @param settingsId unique id identifying recommender
	 * @param tags recommended tags
	 * @param latency 
	 * @return number of recommendations added
	 * @throws SQLException
	 */
	public int addRecommendation(Long queryId, Long settingsId, SortedSet<RecommendedTag> tags, long latency) throws SQLException;

	/**
	 * Connect postID with recommendation.
	 *  For each post process an unique id is generated. This is used for mapping 
	 *  posts to recommendations and vice verca. 
	 * @param post post as stored in bibsonomy
	 * @param postID post's random id as generated in PostBookmarkController
	 * @throws SQLException 
	 */
	public void connectWithPost(Post<? extends Resource> post, int postID) throws SQLException;

	/**
	 * Get sorted list of tags recommended in a given query by a given recommender. 
	 * 
	 * @param qid
	 * @param sid
	 * @return tags recommended in query identified by qid and recommender identified by sid
	 * @throws SQLException
	 */
	public SortedSet<RecommendedTag> getRecommendations(Long qid, Long sid) throws SQLException;

	/**
	 * Append tags which were recommended in a given query by a given recommender to a given collection. 
	 * 
	 * @param qid
	 * @param sid
	 * @param recommendedTags 
	 * 
	 * @throws SQLException
	 */
	public void getRecommendations(Long qid, Long sid, Collection<RecommendedTag> recommendedTags) throws SQLException;

	/**
	 * Get sorted list of tags recommended in a given query. 
	 * 
	 * @param qid
	 * @return tags recommended in query identified by qid and all recommenders 
	 * @throws SQLException
	 */
	public SortedSet<RecommendedTag> getRecommendations(Long qid) throws SQLException;

	/**
	 * Append tags which are recommended in a given query to given collection 
	 * 
	 * @param qid query id
	 * @param recommendedTags collection where recommended tags should be appended
	 * @throws SQLException
	 */
	public void getRecommendations(Long qid, Collection<RecommendedTag> recommendedTags) throws SQLException;

	/**
	 * Get (unsorted) list of selected tags for a given query. 
	 * 
	 * @param qid
	 * @return tags recommended in query identified by qid and all recommenders 
	 * @throws SQLException
	 */
	public List<RecommendedTag> getSelectedTags(Long qid) throws SQLException;

	/**
	 * Get list of recommender settings which where selected for given query.
	 * 
	 * @param qid query_id
	 * @return list of recommender settings 
	 * @throws SQLException 
	 */
	public List<Long> getSelectedRecommenderIDs(Long qid) throws SQLException;

	/**
	 * Get list of newest tas entries
	 * @param offset
	 * @param range
	 * @return list of range number of new entries, starting by offset 
	 * @throws SQLException 
	 */
	public List<TasEntry> getNewestEntries(Integer offset, Integer range) throws SQLException;

	/**
	 * Get user's most popular tag names with corresponding tag frequencies 
	 * 
	 * @param username
	 * @param range - the number of tags to get 
	 * 
	 * @return list of pairs [tagname,frequency]
	 * @throws SQLException 
	 */
	public List<Pair<String, Integer>> getMostPopularTagsForUser(final String username, final int range) throws SQLException;

	/**
	 * Gets the most popular tags of the given resource.
	 * 
	 * @param <T> The type of the resource.
	 * @param resourceType
	 * @param intraHash
	 * @param range
	 * @return The most popular tags of the given resource.
	 * @throws SQLException
	 */
	public <T extends Resource> List<Pair<String, Integer>> getMostPopularTagsForResource(final Class<T> resourceType, final String intraHash, final int range) throws SQLException;

	/**
	 * Get number of tags used by given user. 
	 * @param username
	 * @return number of tags used by given user
	 * @throws SQLException 
	 */
	public Integer getNumberOfTagsForUser(String username) throws SQLException;

	/**
	 * Get number of TAS of the given user. 
	 * @param username
	 * @return number of TAS of given user
	 * @throws SQLException 
	 */
	public Integer getNumberOfTasForUser(String username) throws SQLException;

	/**
	 * Get number of tags attached to a given resource.. 
	 * @param <T> 
	 * @param resourceType - type of the resource 
	 * @param intraHash - hash of the resource
	 * 
	 * @return The number of tags attached to the resource.
	 * @throws SQLException 
	 */
	public <T extends Resource> Integer getNumberOfTagsForResource(final Class<T> resourceType, final String intraHash) throws SQLException;

	/**
	 * Get number of TAS for a given resource.. 
	 * @param <T> 
	 * @param resourceType - type of the resource 
	 * @param intraHash - hash of the resource
	 * 
	 * @return The number of TAS of the resource.
	 * @throws SQLException 
	 */
	public <T extends Resource> Integer getNumberOfTasForResource(final Class<T> resourceType, final String intraHash) throws SQLException;
	
	/**
	 * Maps BibSonomy's user name to corresponding user id
	 * 
	 * @param userName user's name
	 * @return user's id, null if user name doesn't exist
	 */
	public Integer getUserIDByName(String userName);	

	/**
	 * Maps BibSonomy's user id to corresponding user name
	 * 
	 * @param userID user's id
	 * @return user's name, null if user id doesn't exist
	 */
	public String getUserNameByID(int userID);

	/**
	 * Get list of all tags from given recommender and query
	 * @param sid recommender's setting id
	 * @param qid query id
	 * @return list of all tags from given recommender and query
	 * @throws SQLException 
	 */
	public List<String> getTagNamesForRecQuery(Long sid, Long qid) throws SQLException;

	/**
	 * Get list of all tags chosen by user for given post
	 * @param cid post's content_id
	 * @return list of all tags chosen by user for given post
	 * @throws SQLException 
	 */
	public List<String> getTagNamesForPost(Integer cid) throws SQLException;

	/**
	 * Returns details for given recommender.
	 * @param sid Recommender's setting id
	 * @return Details for given recommender if found -- null otherwise
	 * @throws SQLException 
	 */
	public RecSettingParam getRecommender(Long sid) throws SQLException;

	/**
	 * Get list of all recommenders (id) which delivered tags in given query.
	 * @param qid query id
	 * @return list of ids
	 * @throws SQLException 
	 */
	public List<Long> getActiveRecommenderIDs(Long qid) throws SQLException;

	/**
	 * Get list of all recommenders (id) which where queried.
	 * @param qid query id
	 * @return list of ids
	 * @throws SQLException 
	 */
	public List<Long> getAllRecommenderIDs(Long qid) throws SQLException;

	/**
	 * Get list of all recommenders (id) which where queried.
	 * @param qid query id
	 * @return list of ids
	 * @throws SQLException 
	 */
	public List<Pair<Long, Long>> getRecommenderSelectionCount(Long qid) throws SQLException;

	/**
	 * Get list of all recommenders (id) which where queried and not selected previously 
	 * during given post process.
	 * 
	 * @param qid query id
	 * @return list of ids
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public List<Long> getAllNotSelectedRecommenderIDs(Long qid) throws SQLException;

	/**
	 * Returns details for given selector.
	 * @param sid Result selector's setting id
	 * @return Details for given recommender if found -- null otherwise
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public SelectorSettingParam getSelector(Long sid) throws SQLException;

	/**
	 * Return query information for given query id
	 * @param qid querie's id
	 * @return RecQueryParam on success, null otherwise
	 * @throws SQLException 
	 */
	public RecQueryParam getQuery(Long qid) throws SQLException;

	/**
	 * Return list of all queries for given recommender 
	 * @param sid recommender's query
	 * @return list of all queries for given recommender
	 * @throws SQLException 
	 */
	public List<RecQueryParam> getQueriesForRecommender(Long sid) throws SQLException;

	/**
	 * Get recommender-info for admin statuspage
	 * @return recommender-info
	 * @param id recommenderID
	 * @throws SQLException 
	 */
	public RecAdminOverview getRecommenderAdminOverview(String id) throws SQLException;
		
	/**
	 * Get the average latency of a given recommender-setting
	 * 
	 * @param sid
	 * @param numberOfQueries number of newest latency-values which will be fetched to calculate the average latency
	 * @return average latency of the recommender
	 * @throws SQLException
	 */
	public Long getAverageLatencyForRecommender(Long sid, Long numberOfQueries) throws SQLException;

	/**
	 * Get all setting-ids which are flagged as a distant recommender
	 * @return setting-ids of all distant recommenders
	 * @throws SQLException
	 */
	public List<Long> getDistantRecommenderSettingIds() throws SQLException;
	
	/**
	 * Get all setting-ids which are flagged as a local recommender
	 * @return setting-ids of all local recommenders
	 * @throws SQLException
	 */
	public List<Long> getLocalRecommenderSettingIds() throws SQLException;
	
	/**
	 * Get all settingids which are set to status 'active'
	 * @return identifiers of currently activated settings 
	 * @throws SQLException
	 */
	public List<Long> getActiveRecommenderSettingIds() throws SQLException;
	
	/**
	 * Get all settingids which are set to status 'disabled'
	 * @return identifiers of currently disabled settings 
	 * @throws SQLException
	 */
	public List<Long> getDisabledRecommenderSettingIds() throws SQLException;
	
    /**
     * Get related recommender-ids for a list of setting-ids
     * @param sids setting-ids
     * @return map settingid->recommenderid
     * @throws SQLException
     */
	public Map<Long, String> getRecommenderIdsForSettingIds(List<Long> sids) throws SQLException;
	
	/**
	 * Activate and disable several recommenders at once.
	 * @param activeRecs identifiers of the settings to be activated
	 * @param disabledRecs identifiers of the settings to be disabled
	 * @throws SQLException
	 */
	public void updateRecommenderstatus(List<Long> activeRecs, List<Long> disabledRecs ) throws SQLException;
		
	/**
	 * Set a recommender to status 'removed'.
	 * @param url recommender-id
	 * @throws SQLException
	 */
	public void removeRecommender(String url) throws SQLException;
	
	/**
	 * Change the url of a recommender which is already contained in the database.
	 * @param sid setting-id
	 * @param url new url
	 * @throws SQLException
	 */
	public void updateRecommenderUrl(long sid, URL url) throws SQLException;
	
	/**
	 * Tries to guess query_id from given content id.
	 * 
	 * @param content_id
	 * @return nearest query_id, if guess is possible -- otherwise null
	 * @throws SQLException
	 */
	public Long guessQueryFromPost(Integer content_id) throws SQLException;

	/**
	 * Guess content_id for given query_id.
	 * 
	 * @param query_id
	 * @return nearest content_id if found -- otherwise null
	 * @throws SQLException 
	 */
	public Integer guessPostFromQuery(Long query_id) throws SQLException;

	/**
	 * Get queryID for given postID, user_name and date
	 * @param user_name 
	 * @param date 
	 * @param postID
	 * @return TODO
	 * @throws SQLException 
	 */
	public Long getQueryForPost(String user_name, Date date, Integer postID) throws SQLException;

	/**
	 * Get contentID for given queryID
	 * @param queryID 
	 * 
	 * @return TODO
	 * @throws SQLException 
	 */
	public Integer getContentIDForQuery(Long queryID) throws SQLException;

	/**
	 * Get contentID for given query data
	 * @param userName 
	 * @param date 
	 * @param postID 
	 * 
	 * @return TODO
	 */
	public Integer getContentIDForQuery(String userName, Date date, Integer postID);

	/**
	 * Store selected recommended tags.
	 * 
	 * @param qid query id
	 * @param rid result selector id
	 * @param result set of recommended tags
	 * @return TODO
	 * @throws SQLException 
	 */
	public int storeRecommendation(Long qid, Long rid, Collection<RecommendedTag> result) throws SQLException;

	//------------------------------------------------------------------------
	// logging interface implementation
	//------------------------------------------------------------------------
	/**
	 * Log recommender event.
	 * @param qid unique query id for identifying interrelated recommender responses
	 * @param sid unique id identifying recommender's settings
	 * @param latency 
	 * @param tags tags calculated by recommender
	 * @param preset predetermined tags, null if none given
	 * @return true on success, false otherwise
	 * @throws SQLException 
	 */
	public boolean logRecommendation(Long qid, Long sid, long latency, SortedSet<RecommendedTag> tags, SortedSet<RecommendedTag> preset) throws SQLException;

}