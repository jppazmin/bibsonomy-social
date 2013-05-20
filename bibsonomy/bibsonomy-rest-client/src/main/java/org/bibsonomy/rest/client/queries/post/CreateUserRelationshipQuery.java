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

package org.bibsonomy.rest.client.queries.post;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.StringWriter;

import org.bibsonomy.model.User;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.util.StringUtils;

/**
 * Creatte a relationship among users.
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: CreateUserRelationshipQuery.java,v 1.4 2011-06-09 12:00:46 rja Exp $
 */
public class CreateUserRelationshipQuery extends AbstractQuery<String> {

	/** parameter value for friend relationship */
	public final static String FRIEND_RELATIONSHIP = "friend";
	/** paramaeter value for follower relationship */
	public final static String FOLLOWER_RELATIONSHIP = "follower";

	/** source user */
	private String username;
	/** target user */
	private String targetUserName;
	/** type of relationship (friend/follower) */
	private String relationType;
	/** tag for tagged relationships */
	private String tag;

	/**
	 * Create new query.
	 * 
	 * @param username
	 *            - the (currently logged in) source user
	 * @param targetUserName
	 *            - the name of the user to establish a relationship with
	 * @param relationType
	 *            - the type of relationship (i.e. "friend" or "follower"
	 * @param tag
	 *            - a tag (for taggged relationships)
	 */
	public CreateUserRelationshipQuery(final String username, final String targetUserName, final String relationType, final String tag) {
		/*
		 * check input
		 */
		if (!present(username)) throw new IllegalArgumentException("No source user given!");
		if (!present(targetUserName)) throw new IllegalArgumentException("No target user given");
		if (!(FRIEND_RELATIONSHIP.equals(relationType) || FOLLOWER_RELATIONSHIP.equals(relationType))) {
			throw new IllegalArgumentException("Relation type must be either '" + FRIEND_RELATIONSHIP + "' or '" + FOLLOWER_RELATIONSHIP + "'");
		}
		/*
		 * set params
		 */
		this.username = username;
		this.targetUserName = targetUserName;
		this.relationType = relationType;
		this.tag = tag;
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		/*
		 * create body of request
		 */
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializeUser(sw, new User(this.targetUserName), null);
		/*
		 * friend/follower, tag
		 */
		final String friendOrFollower = ( FRIEND_RELATIONSHIP.equals(relationType) ? URL_FRIENDS : URL_FOLLOWERS );
		final String queryTag = ( present(tag) ? "/"+tag : "" );
		/*
		 * perform request
		 */
		this.downloadedDocument = performRequest(HttpMethod.POST, URL_USERS + "/" + this.username + "/" + friendOrFollower + queryTag + "?format=" + getRenderingFormat().toString().toLowerCase(), StringUtils.toDefaultCharset(StringUtils.toDefaultCharset(sw.toString())));
		return null;

	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getTargetUserName() {
		return this.targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}	

}
