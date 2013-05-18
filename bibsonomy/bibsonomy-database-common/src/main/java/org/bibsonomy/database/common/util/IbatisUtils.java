/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.util;

import java.io.Reader;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * @author dzo
 * @version $Id: IbatisUtils.java,v 1.3 2011-05-12 14:25:28 rja Exp $
 */
public final class IbatisUtils {

    private static final Properties props = new Properties();
    static {
	props.setProperty("JNDIDataSource", "java:comp/env/jdbc/bibsonomy");
    }
    
    /**
     * loads the specified iBatis config
     * 
     * @param filename
     * @return the ibatis sql map
     */
    public static SqlMapClient loadSqlMap(final String filename) {
	return loadSqlMap(filename, props);
    }

    /**
     * loads the specified iBatis config
     * 
     * @param filename
     * @param props - the properties to specify the JNDI datasource using the key "JNDIDataSource"
     * @return the ibatis sql map
     */
    public static SqlMapClient loadSqlMap(final String filename, final Properties props) {
	try {
	    // initialize database client
	    final Reader reader = Resources.getResourceAsReader(filename);
	    return SqlMapClientBuilder.buildSqlMapClient(reader, props);
	} catch (final Exception e) {
	    throw new RuntimeException("Error loading " + filename + " sqlmap.", e);
	}
    }
}
