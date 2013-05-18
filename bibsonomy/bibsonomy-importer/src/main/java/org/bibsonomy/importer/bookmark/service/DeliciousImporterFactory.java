/**
 *
 *  BibSonomy-Importer - Various importers for bookmarks and publications.
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

package org.bibsonomy.importer.bookmark.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.bibsonomy.services.importer.RelationImporter;
import org.bibsonomy.services.importer.RemoteServiceBookmarkImporter;

/**
 * Creates a new instance of the {@link DeliciousImporter}.
 * 
 * @author:  rja
 * @version: $Id: DeliciousImporterFactory.java,v 1.5 2009-07-27 10:06:21 rja Exp $
 * $Author: rja $
 * 
 */
public class DeliciousImporterFactory {
	
	private String bundlesPath = "/v1/tags/bundles/all";
	private String postsPath   = "/v1/posts/all";
	
	/*
	 * TODO: there was a reason we use "-1" as port ... please document it 
	 * here
	 */
	private int port = -1;
	private String protocol = "https";
	private String host = "api.del.icio.us";
	
	private String userAgent = "Wget/1.9.1";
	

	public RelationImporter getRelationImporter() throws MalformedURLException {
		return new DeliciousImporter(buildURL(bundlesPath), userAgent);
	}

	public RemoteServiceBookmarkImporter getBookmarkImporter() throws MalformedURLException {
		return new DeliciousImporter(buildURL(postsPath), userAgent);
	}

	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * The user agent string the importer shall use to identify itself against
	 * the Delicious API in the corresponding HTTP header field.
	 *  
	 * @param userAgent
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	private URL buildURL(final String path) throws MalformedURLException {
		return new URL (protocol, host, port, path);
	}

	public String getBundlesPath() {
		return bundlesPath;
	}

	public void setBundlesPath(String bundlesPath) {
		this.bundlesPath = bundlesPath;
	}

	public String getPostsPath() {
		return postsPath;
	}

	public void setPostsPath(String postsPath) {
		this.postsPath = postsPath;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}

