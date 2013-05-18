/**
 *
 *  BibSonomy-Logging - Logs clicks from users of the BibSonomy webapp.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.logging;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * 
 * @author sst
 * @version $Id: QueryDB.java,v 1.5 2010-07-14 12:21:59 nosebrain Exp $
 */
public class QueryDB {
	private static QueryDB instance = null;
	
	/**
	 * @return the {@link QueryDB} instance
	 * @throws IOException
	 */
	public static QueryDB getInstance() throws IOException {
		if (instance == null) {
			instance = new QueryDB();
		}
		return instance;
	}
	
	private final SqlMapClient sqlMapClient;
	
	/**
	 * Private constructor = singleton
	 * @throws IOException 
	 */
	private QueryDB() throws IOException {
		final Reader reader = new InputStreamReader(QueryDB.class.getClassLoader().getResourceAsStream("SqlMapConfigLogger.xml"));
		sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
	}
	
	/**
	 * inserts the log data into the db
	 * @param logdata
	 * @throws SQLException
	 */
	public void insertLogdata(Log logdata) throws SQLException {
		sqlMapClient.insert("BibLog.insertLogdata", logdata);
	}
}