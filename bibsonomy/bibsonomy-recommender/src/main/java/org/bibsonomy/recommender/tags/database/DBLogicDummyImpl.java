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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.database.params.Pair;
import org.bibsonomy.recommender.tags.database.params.RecAdminOverview;
import org.bibsonomy.recommender.tags.database.params.RecQueryParam;
import org.bibsonomy.recommender.tags.database.params.RecSettingParam;
import org.bibsonomy.recommender.tags.database.params.SelectorSettingParam;
import org.bibsonomy.recommender.tags.database.params.TasEntry;

/**
 * @author rja
 * @version $Id: DBLogicDummyImpl.java,v 1.9 2010-07-14 11:59:46 nosebrain Exp $
 */
public class DBLogicDummyImpl implements DBLogic {

	private final Map<Pair<Long, Long>, Collection<RecommendedTag>> recoMap = new HashMap<Pair<Long,Long>, Collection<RecommendedTag>>(); 
	
	private final Map<String, Long> recos = new HashMap<String, Long>();
	
	
	@Override
	public Long addQuery(final String userName, final Date date, final Post<? extends Resource> post, final int postID, final int queryTimeout) throws SQLException {
		return 0l;
	}

	@Override
	public int addRecommendation(final Long queryId, final Long settingsId, final SortedSet<RecommendedTag> tags, final long latency) throws SQLException {
		return recoMap.put(new Pair<Long, Long>(queryId, settingsId), tags).size();
	}

	@Override
	public Long addRecommender(final Long queryId, final String recId, final String recDescr, final byte[] recMeta) throws SQLException {
		if (!recos.containsKey(recId)) {
			recos.put(recId, Long.valueOf(recos.size()));
		} 
		return recos.get(recId);
	}

	@Override
	public Long addResultSelector(final Long qid, final String selectorInfo, final byte[] selectorMeta) throws SQLException {
		return 0l;
	}

	@Override
	public void addSelectedRecommender(final Long qid, final Long sid) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectWithPost(final Post<? extends Resource> post, final int postID) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Long> getActiveRecommenderIDs(final Long qid) throws SQLException {
		return new LinkedList<Long>(recos.values());
	}

	@Override
	public List<Long> getAllNotSelectedRecommenderIDs(final Long qid) throws SQLException {
		return new LinkedList<Long>();
	}

	@Override
	public List<Long> getAllRecommenderIDs(final Long qid) throws SQLException {
		return new LinkedList<Long>(recos.values());
	}

	@Override
	public Integer getContentIDForQuery(final Long queryID) throws SQLException {
		return 0; // TODO
	}

	@Override
	public Integer getContentIDForQuery(final String userName, final Date date, final Integer postID) {
		return 0; // TODO
	}

	@Override
	public <T extends Resource> List<Pair<String, Integer>> getMostPopularTagsForResource(final Class<T> resourceType, final String intraHash, final int range) throws SQLException {
		final List<Pair<String, Integer>> mostPopularTagsForResource = new LinkedList<Pair<String,Integer>>();

		mostPopularTagsForResource.add(new Pair<String, Integer>("mpResource1", 10));
		mostPopularTagsForResource.add(new Pair<String, Integer>("mpResource2", 8));
		mostPopularTagsForResource.add(new Pair<String, Integer>("mpResource3", 6));
		mostPopularTagsForResource.add(new Pair<String, Integer>("mpResource4", 4));
		mostPopularTagsForResource.add(new Pair<String, Integer>("mpResource5", 2));


		return mostPopularTagsForResource;
	}

	@Override
	public List<Pair<String, Integer>> getMostPopularTagsForUser(final String username, final int range) throws SQLException {
		final List<Pair<String, Integer>> mostPopularTagsForUser = new LinkedList<Pair<String,Integer>>();

		mostPopularTagsForUser.add(new Pair<String, Integer>("mpUser1", 10));
		mostPopularTagsForUser.add(new Pair<String, Integer>("mpUser2", 8));
		mostPopularTagsForUser.add(new Pair<String, Integer>("mpUser3", 6));
		mostPopularTagsForUser.add(new Pair<String, Integer>("mpUser4", 4));
		mostPopularTagsForUser.add(new Pair<String, Integer>("mpUser5", 2));


		return mostPopularTagsForUser;
	}

	@Override
	public List<TasEntry> getNewestEntries(final Integer offset, final Integer range) throws SQLException {
		return new LinkedList<TasEntry>(); // TODO
	}

	@Override
	public <T extends Resource> Integer getNumberOfTagsForResource(final Class<T> resourceType, final String intraHash) throws SQLException {
		return 5;
	}

	@Override
	public Integer getNumberOfTagsForUser(final String username) throws SQLException {
		return 5;
	}

	@Override
	public <T extends Resource> Integer getNumberOfTasForResource(final Class<T> resourceType, final String intraHash) throws SQLException {
		return 5;
	}

	@Override
	public Integer getNumberOfTasForUser(final String username) throws SQLException {
		return 5;
	}

	@Override
	public List<RecQueryParam> getQueriesForRecommender(final Long sid) throws SQLException {
		return new LinkedList<RecQueryParam>();	
	}

	@Override
	public RecQueryParam getQuery(final Long qid) throws SQLException {
		return new RecQueryParam();
	}

	@Override
	public Long getQueryForPost(final String user_name, final Date date, final Integer postID) throws SQLException {
		return 0l;
	}

	@Override
	public SortedSet<RecommendedTag> getRecommendations(final Long qid, final Long sid) throws SQLException {
		return new TreeSet<RecommendedTag>(recoMap.get(new Pair<Long, Long>(qid, sid)));
	}

	@Override
	public void getRecommendations(final Long qid, final Long sid, final Collection<RecommendedTag> recommendedTags) throws SQLException {
		recommendedTags.addAll(recoMap.get(new Pair<Long, Long>(qid, sid)));
	}

	@Override
	public SortedSet<RecommendedTag> getRecommendations(final Long qid) throws SQLException {
		return new TreeSet<RecommendedTag>();
	}

	@Override
	public void getRecommendations(final Long qid, final Collection<RecommendedTag> recommendedTags) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public RecSettingParam getRecommender(final Long sid) throws SQLException {
		return new RecSettingParam();
	}

	@Override
	public List<Pair<Long, Long>> getRecommenderSelectionCount(final Long qid) throws SQLException {
		return new LinkedList<Pair<Long,Long>>();
	}

	@Override
	public List<Long> getSelectedRecommenderIDs(final Long qid) throws SQLException {
		return new LinkedList<Long>();
	}

	@Override
	public List<RecommendedTag> getSelectedTags(final Long qid) throws SQLException {
		return new LinkedList<RecommendedTag>();
	}

	@Override
	public SelectorSettingParam getSelector(final Long sid) throws SQLException {
		return new SelectorSettingParam();
	}

	@Override
	public List<String> getTagNamesForPost(final Integer cid) throws SQLException {
		return new LinkedList<String>();
	}

	@Override
	public List<String> getTagNamesForRecQuery(final Long sid, final Long qid) throws SQLException {
		return new LinkedList<String>();
	}

	@Override
	public Integer guessPostFromQuery(final Long query_id) throws SQLException {
		return 0;
	}

	@Override
	public Long guessQueryFromPost(final Integer content_id) throws SQLException {
		return 0l;
	}

	@Override
	public boolean logRecommendation(final Long qid, final Long sid, final long latency, final SortedSet<RecommendedTag> tags, final SortedSet<RecommendedTag> preset) throws SQLException {
		return false;
	}

	@Override
	public int storeRecommendation(final Long qid, final Long rid, final Collection<RecommendedTag> result) throws SQLException {
		return recoMap.put(new Pair<Long, Long>(qid, rid), result).size();
	}

	@Override
	public Integer getUserIDByName(final String userName) {
		return 0;
	}

	@Override
	public String getUserNameByID(final int userID) {
		return "nouser";
	}

	@Override
	public void addRecommenderToQuery(final Long qid, final Long sid) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public Long insertRecommenderSetting(final String recId, final String recDescr, final byte[] recMeta) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long insertSelectorSetting(final String selectorInfo, final byte[] selectorMeta) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResultSelectorToQuery(final Long qid, final Long rid) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Long> getActiveRecommenderSettingIds() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getAverageLatencyForRecommender(final Long sid, final Long numberOfQueries) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> getDisabledRecommenderSettingIds() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecAdminOverview getRecommenderAdminOverview(final String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, String> getRecommenderIdsForSettingIds(final List<Long> sids) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRecommenderUrl(final long sid, final URL url) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateRecommenderstatus(final List<Long> activeRecs, final List<Long> disabledRecs) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeRecommender(final String url) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Long> getLocalRecommenderSettingIds() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> getDistantRecommenderSettingIds() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
