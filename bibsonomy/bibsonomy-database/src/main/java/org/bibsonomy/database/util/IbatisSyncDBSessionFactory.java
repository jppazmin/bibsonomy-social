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

import java.util.Properties;

import org.bibsonomy.database.common.impl.AbstractDBSessionFactory;
import org.bibsonomy.database.common.util.IbatisUtils;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;

/**
 * Factory for real database sessions via iBatis.
 * 
 * @author Jens Illig
 * @version $Id: IbatisSyncDBSessionFactory.java,v 1.5 2011-07-22 13:09:54 rja Exp $
 */
public class IbatisSyncDBSessionFactory extends AbstractDBSessionFactory {

	private static final String JNDI_DATASOURCE = "java:comp/env/jdbc/sync_puma";
	private final static Properties props = new Properties();

	static {
		props.setProperty("JNDIDataSource", JNDI_DATASOURCE);
	}

	private static final SqlMapClient client = IbatisUtils.loadSqlMap("SqlMapConfig.xml", props);

	protected static final SqlMapClient getSqlMapClient() {
		return client;
	}

	@Override
	protected SqlMapSession getSqlMap() {
		return client.openSession();
	}

	/**
	 * Set JNDIDataSource like  props.setProperty("JNDIDataSource", "java:comp/env/jdbc/[serviceName]");
	 * service name must be specified in context.xml
	 * @return properties to set JNDIDataSource
	 */
	public Properties getProps(){
		return props;
	}
	
	@Override
	public String toString() {
		return JNDI_DATASOURCE;
	}
}
