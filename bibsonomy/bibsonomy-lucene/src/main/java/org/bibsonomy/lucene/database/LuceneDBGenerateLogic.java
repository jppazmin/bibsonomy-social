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

import java.io.Reader;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.lucene.database.util.LuceneDatabaseSessionFactory;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * TODO: an AbstractDatabaseManager class e.g in bibsonomy-database-common
 * 
 * implements methods needed for index creation
 * 
 * @author fei
 * @version $Id: LuceneDBGenerateLogic.java,v 1.12 2011-05-09 13:39:40 folke Exp $
 * 
 * @param <R> 
 */
public abstract class LuceneDBGenerateLogic<R extends Resource> extends AbstractDatabaseManager implements LuceneDBInterface<R> {
	private static final Log log = LogFactory.getLog(LuceneDBGenerateLogic.class);

	/** path to the ibatis database configuration file */
	private static final String SQL_MAP_CONFIG = "SqlMapConfig_lucene.xml";
	
	/** access to database */
	@Deprecated // use openSession instead
	protected final SqlMapClient sqlMap;

	private DBSessionFactory sessionFactory;

	/**
	 * Constructor
	 */
	protected LuceneDBGenerateLogic() {
		this.sessionFactory = new LuceneDatabaseSessionFactory();
		try {
			// initialize database client for lucene queries
			final Reader reader = Resources.getResourceAsReader(SQL_MAP_CONFIG);
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			log.info("Database connection initialized.");
		} catch (Exception e) {
			throw new RuntimeException("Error initializing LuceneDBGenerateLogic class.", e);
		}
	}
	
	protected DBSession openSession() {
		return this.sessionFactory.getDatabaseSession();
	}
	
	@Override
	public Integer getLastTasId() {
		try {
			return (Integer)sqlMap.queryForObject("getLastTasId");
		} catch (SQLException e) {
			log.error("Error determining last tas entry.", e);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override 
	public List<User> getPredictionForTimeRange(Date fromDate) {	
		try {
			return sqlMap.queryForList("getPredictionForTimeRange", fromDate);
		} catch (SQLException e) {
			log.error("Error getting flagged users", e);
		}
		
		return new LinkedList<User>();
	}

}
