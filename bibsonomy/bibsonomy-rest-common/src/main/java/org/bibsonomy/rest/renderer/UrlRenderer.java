/**
 *
 *  BibSonomy-Rest-Common - Common things for the REST-client and server.
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

package org.bibsonomy.rest.renderer;

import static org.bibsonomy.rest.RestProperties.Property.URL_DATE_FORMAT;
import static org.bibsonomy.rest.RestProperties.Property.URL_DOCUMENTS;
import static org.bibsonomy.rest.RestProperties.Property.URL_GROUPS;
import static org.bibsonomy.rest.RestProperties.Property.URL_POSTS;
import static org.bibsonomy.rest.RestProperties.Property.URL_TAGS;
import static org.bibsonomy.rest.RestProperties.Property.URL_USERS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bibsonomy.rest.RestProperties;

/** 
 * This renderer creates URLs according to BibSonomys REST URL scheme.
 * 
 * @author rja
 * @version $Id: UrlRenderer.java,v 1.9 2011-06-09 11:58:45 rja Exp $
 */
public class UrlRenderer {
	private static final String PARTS_DELIMITER = "/";
	
	private final String userUrlPrefix;
	private final String groupUrlPrefix;
	private final String tagUrlPrefix;
	
	private final String postsUrlDelimiter;
	private final String documentsUrlDelimiter;
	
	private final DateFormat dateFormat;
	
	private final String apiUrl;
	
	public UrlRenderer(final String apiUrl) {
		this.apiUrl = apiUrl;
		final RestProperties properties = RestProperties.getInstance();
		this.userUrlPrefix = apiUrl + properties.get(URL_USERS) + PARTS_DELIMITER;
		this.groupUrlPrefix = apiUrl + properties.get(URL_GROUPS) + PARTS_DELIMITER;
		this.tagUrlPrefix = apiUrl + properties.get(URL_TAGS) + PARTS_DELIMITER;
		this.postsUrlDelimiter = PARTS_DELIMITER + properties.get(URL_POSTS) + PARTS_DELIMITER;
		this.documentsUrlDelimiter = PARTS_DELIMITER + properties.get(URL_DOCUMENTS) + PARTS_DELIMITER;
		this.dateFormat = new SimpleDateFormat(properties.get(URL_DATE_FORMAT));
	}	

	/** Creates a URL which points to the given user. 
	 * 
	 * @param name - the name of the user.
	 * @return A URL which points to the given user.
	 */
	public String createHrefForUser(final String name) {
		return this.userUrlPrefix + name;
	}
	
	/** Creates a URL which points to the given tag.
	 * 
	 * @param tag - the name of the tag.
	 * @return A URL which points to the given tag.
	 */
	public String createHrefForTag(final String tag) {
		return this.tagUrlPrefix + tag;
	}	

	/** Creates a URL which points to the given group.
	 * 
	 * @param name - the name of the group.
	 * @return A URL which points to the given group. 
	 */
	public String createHrefForGroup(final String name) {
		return this.groupUrlPrefix + name;
	}

	/** Creates a URL which points to the given resource.
	 * 
	 * @param userName - the name of the user which owns the resource.
	 * @param intraHash - the intra hash of the resource.
	 * @return A URL which points to the given resource.
	 */
	public String createHrefForResource(final String userName, final String intraHash) {
		return this.userUrlPrefix + userName + this.postsUrlDelimiter + intraHash;
	}
	
	/**
	 * TODO: remove?
	 * Creates a URL which points to the given resource.
	 * 
	 * @param userName - the name of the user which owns the resource.
	 * @param intraHash - the intra hash of the resource.
	 * @param date - the date at which the resource has been posted.
	 * @return A URL which points to the given resource.
	 */
	public String createHrefForResource(final String userName, final String intraHash, final Date date) {
		return this.userUrlPrefix + userName + this.postsUrlDelimiter + intraHash + PARTS_DELIMITER + dateFormat.format(date);
	}
	
	/** Creates a URL which points to the given document attached to the given resource.
	 * 
	 * @param userName - the name of the user which owns the resource (and document).
	 * @param intraHash - the intra has of the resource.
	 * @param documentFileName - the name of the document.
	 * @return A URL which points to the given document.
	 */
	public String createHrefForResourceDocument(final String userName, final String intraHash, final String documentFileName) {
		return this.createHrefForResource(userName, intraHash) + this.documentsUrlDelimiter + documentFileName;
	}

	/**
	 * 
	 * @return The API URL currently used to render URLs.
	 */
	public String getApiUrl() {
		return this.apiUrl;
	}
}
