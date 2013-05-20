/**
 *
 *  BibSonomy-Rest-Client - The REST-client.
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

package org.bibsonomy.rest.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dzo
 * @version $Id: RestClientUtils.java,v 1.3 2011-07-26 13:47:19 bibsonomy Exp $
 */
public class RestClientUtils {
	private static final Log log = LogFactory.getLog(RestClientUtils.class);
	
	/**
	 * the content charset used by the rest client
	 */
	public static final String CONTENT_CHARSET = "UTF-8";
	
	private static final String PROPERTIES_FILE_NAME = "bibsonomy-rest-client.properties";
	private static final String PROPERTIES_VERSION_KEY = "version";
	
	private static String REST_CLIENT_VERSION = "unknown";
	
	/**
	 * @return the version of the client
	 */
	public static String getRestClientVersion() {
		return REST_CLIENT_VERSION;
	}
	
	static {		
		try {
			final Properties properties = new Properties();
			
			final InputStream stream = RestClientUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
			properties.load(stream);
			stream.close();
			
			REST_CLIENT_VERSION = properties.getProperty(PROPERTIES_VERSION_KEY);
		} catch (final IOException ex) {
			log.error("could not load version", ex);
		}
	}
	
	
}
