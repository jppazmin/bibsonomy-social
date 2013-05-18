/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.lucene.database.params.ResourcesParam;
import org.bibsonomy.lucene.param.LucenePost;
import org.bibsonomy.model.Resource;

/**
 * class for accessing the bibsonomy database 
 * 
 * @author fei
 * @version $Id: LuceneDBLogic.java,v 1.14 2010-10-13 11:31:53 nosebrain Exp $
 * @param <R> the resource the logic handles
 */
public abstract class LuceneDBLogic<R extends Resource> extends LuceneDBGenerateLogic<R> {
	private static final Log log = LogFactory.getLog(LuceneDBLogic.class);

	/**
	 * constructor disabled for enforcing singleton pattern 
	 */
	protected LuceneDBLogic() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getPostsForUser(java.lang.String, java.lang.String, org.bibsonomy.common.enums.HashID, int, java.util.List, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LucenePost<R>> getPostsForUser(final String userName, final String requestedUserName, final HashID simHash, final int groupId, final List<Integer> visibleGroupIDs, final int limit, final int offset) {
		final ResourcesParam<R> param = getResourcesParam();
		param.setRequestedUserName(requestedUserName);
		param.setSimHash(simHash);
		param.setGroupId(groupId);
		param.setGroups(visibleGroupIDs);
		param.setLimit(limit);
		param.setOffset(offset);
		
		List<LucenePost<R>> retVal = null;
		try {
			retVal = this.sqlMap.queryForList("get" + this.getResourceName() + "ForUser", param);
		} catch (SQLException e) {
			log.error("Error fetching " +" for user " + param.getUserName(), e);
		}
		
		return retVal != null ? retVal : new LinkedList<LucenePost<R>>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getNewestRecordDateFromTas()
	 */
	@Override
	public Date getNewestRecordDateFromTas() {
		Date retVal = null;
		try {
			retVal = (Date)this.sqlMap.queryForObject("getNewestRecordDateFromTas");
		} catch (SQLException e) {
			log.error("Error determining last tas entry", e);
		}
		
		return retVal;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getContentIdsToDelete(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getContentIdsToDelete(Date lastLogDate) {
		List<Integer> retVal;
		
		try {
			retVal = this.sqlMap.queryForList("get" + this.getResourceName() + "ContentIdsToDelete", lastLogDate);
		} catch (SQLException e) {
			log.error("Error getting content ids to delete", e);
			retVal = new LinkedList<Integer>();
		}
		
		log.debug("getContentIdsToDelete - count: " + retVal.size());

		return retVal;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getFriendsForUser(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getFriendsForUser(String userName) {
		List<String> retVal = null;
		
		try {
			retVal = this.sqlMap.queryForList("getFriendsForUser", userName);
		} catch (SQLException e) {
			log.error("Error getting friends for user "+userName, e);
		}
		if( retVal==null ) {
			retVal = new LinkedList<String>();
		}
		
		return retVal;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getGroupMembersByGroupName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getGroupMembersByGroupName(String groupName) {
		List<String> retVal = null;
		
		try {
			retVal = this.sqlMap.queryForList("getGroupMembersByGroupName", groupName);
		} catch (SQLException e) {
			log.error("Error getting group members", e);
		}
		if( retVal==null ) {
			retVal = new LinkedList<String>();
		}
		
		return retVal;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getLastLogDate()
	 */
	@Override
	public Date getLastLogDate() {
		final DBSession session = this.openSession();
		try {
			return this.queryForObject("getLastLog" + this.getResourceName(), Date.class, session);
		} finally {
			session.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getNumberOfPosts()
	 */
	@Override
	public int getNumberOfPosts() {
		Integer retVal = 0;
		try {
			retVal = (Integer)sqlMap.queryForObject("get" + this.getResourceName() + "Count");
		} catch (SQLException e) {
			log.error("Error determining " + this.getResourceName() + " size.", e);
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getPostEntries(java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LucenePost<R>> getPostEntries(Integer skip, Integer max) {
		final ResourcesParam<R> param = this.getResourcesParam();
		param.setOffset(skip);
		param.setLimit(max);
		
		try {
			return sqlMap.queryForList("get" + this.getResourceName() + "ForIndex", param);
		} catch (SQLException e) {
			log.error("Error getting " + this.getResourceName() + " entries.", e);
		}
		
		return new LinkedList<LucenePost<R>>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.lucene.database.LuceneDBInterface#getNewPosts(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LucenePost<R>> getNewPosts(Integer lastTasId) {
		final ResourcesParam<R> param = this.getResourcesParam();
		param.setLastTasId(lastTasId);
		param.setLimit(Integer.MAX_VALUE);
		
		try {
			return sqlMap.queryForList("get" + this.getResourceName() + "PostsForTimeRange", param);
		} catch (SQLException e) {
			log.error("Error getting " + this.getResourceName() + " entries.", e);
		}
		
		return new LinkedList<LucenePost<R>>();
	}
	
	protected abstract String getResourceName();
	
	protected abstract ResourcesParam<R> getResourcesParam();
}
