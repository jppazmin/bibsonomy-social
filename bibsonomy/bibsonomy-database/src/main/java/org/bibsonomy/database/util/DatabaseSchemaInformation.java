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

package org.bibsonomy.database.util;


import org.bibsonomy.database.common.util.AbstractDatabaseSchemaInformation;

import com.ibatis.sqlmap.client.SqlMapSession;

/**
 * @author dzo
 * @version $Id: DatabaseSchemaInformation.java,v 1.7 2011-06-21 13:42:20 nosebrain Exp $
 */
public class DatabaseSchemaInformation extends AbstractDatabaseSchemaInformation {
	
	private static final String PUBLICATION_COMMON_ID = "BibTexCommon.bibtex_common";
	private static final String USER_COMMON_ID = "UserCommon.user";
	
	/**
	 * the name of the publication database table
	 */
	public static final String PUBLICATION_TABLE = "bibtex";
	
	/**
	 * the name of the bookmark database table
	 */
	public static final String BOOKMARK_TABLE = "bookmark";
	
	/**
	 * the name of the tas database table
	 */
	public static final String TAG_TABLE = "tas";
	
	/**
	 * the name of the grouptas database table
	 */
	public static final String GROUP_TAG_TABLE = "grouptas";
	
	private static final String USER_TABLE = "user";
	
	/**
	 * the name of the discussion database table
	 */
	public static final String DISCUSSION_TABLE = "discussion";
	
	
	private static final DatabaseSchemaInformation INSTANCE = new DatabaseSchemaInformation();

	/**
	 * @return the @{link:DatabaseSchemaInformationImpl} instance
	 */
	public static DatabaseSchemaInformation getInstance() {
		return INSTANCE;
	}
	
	
	private DatabaseSchemaInformation() {
		final SqlMapSession sqlMap = IbatisDBSessionFactory.getSqlMapClient().openSession();
		/*
		 * we provide the database table name to make the getMaxFieldLengths call faster!
		 */
		try {
			this.insertMaxFieldLengths(PUBLICATION_COMMON_ID, PUBLICATION_TABLE, sqlMap);
			this.insertMaxFieldLengths(USER_COMMON_ID, USER_TABLE, sqlMap);
		} finally {
			sqlMap.close();
		}
	}
}
