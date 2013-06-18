/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
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

package org.bibsonomy.rest.strategy.users;

import java.io.Writer;
import java.util.List;

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.strategy.AbstractGetListStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * 
 * Gets related users for a given user (e.g. friends, followers).
 * 
 * @author ema, dbe
 * @version $Id: GetFriendsForUserStrategy.java,v 1.5 2010-09-06 08:30:22 rja
 *          Exp $
 */
public class GetRelatedusersForUserStrategy extends AbstractGetListStrategy<List<User>> {

	/**
	 * Request Attribute ?relation="incoming/outgoing"
	 */
	public static final String ATTRIBUTE_KEY_RELATION = "relation";
	/** value for "incoming" */
	public static final String INCOMING_ATTRIBUTE_VALUE_RELATION = "incoming";
	/** value for "outgoing" */
	public static final String OUTGOING_ATTRIBUTE_VALUE_RELATION = "outgoing";
	/** default value */
	public static final String DEFAULT_ATTRIBUTE_VALUE_RELATION = INCOMING_ATTRIBUTE_VALUE_RELATION;

	String userName = null;
	String relation = null;
	UserRelation relationship = null;
	String tag = null;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - the context of the request
	 * @param userName
	 *            - the user name for whic related users should be queried
	 * @param relationship
	 *            - the kind of relationship to be queried
	 * @param tag TODO
	 */
	public GetRelatedusersForUserStrategy(Context context, String userName, String relationship, String tag) {
		super(context);
		this.userName = userName;
		this.tag = tag;
		this.relation = chooseRelation(context);
		this.relationship = chooseRelationship(relationship, this.relation);
	}

	@Override
	protected void render(Writer writer, List<User> resultList) {
		this.getRenderer().serializeUsers(writer, resultList, getView());

	}

	@Override
	protected List<User> getList() {
		return this.getLogic().getUserRelationship(userName, relationship, tag);
	}

	@Override
	protected StringBuilder getLinkPrefix() {
		return new StringBuilder(this.getUrlRenderer().getApiUrl() + RestProperties.getInstance().getFriendsUrl());
	}

	@Override
	protected void appendLinkPostFix(StringBuilder sb) {
	}

	@Override
	protected String getContentType() {
		return "users";
	}

	/**
	 * Choose the right UserRelation enum, based on kind of relationship and the
	 * direction
	 * 
	 * @param relationship
	 *            - the kind of relationship
	 * @param relation
	 *            - the direction
	 * @return the appropriate UserRelatkion enum
	 */
	public static UserRelation chooseRelationship(final String relationship, final String relation) {
		if (RestProperties.getInstance().getFriendsUrl().equals(relationship)) {
			if (OUTGOING_ATTRIBUTE_VALUE_RELATION.equals(relation)) {
				return UserRelation.FRIEND_OF;
			}
			return UserRelation.OF_FRIEND;
		} else if (RestProperties.getInstance().getFollowersUrl().equals(relationship)) {
			if (OUTGOING_ATTRIBUTE_VALUE_RELATION.equals(relation)) {
				return UserRelation.FOLLOWER_OF;
			}
			return UserRelation.OF_FOLLOWER;
		}
		throw new NoSuchResourceException("No resources for relationship type " + relationship + " available - please check your URL syntax.");
	}

	/**
	 * Choose the approprate relation, based on the URL parameter.
	 * 
	 * @param context
	 *            - the context of the request
	 * @return - the appropriate relation.
	 */
	public static String chooseRelation(Context context) {
		String rel = context.getStringAttribute(ATTRIBUTE_KEY_RELATION, DEFAULT_ATTRIBUTE_VALUE_RELATION);
		if (rel == null || !(rel.equals(INCOMING_ATTRIBUTE_VALUE_RELATION) || rel.equals(OUTGOING_ATTRIBUTE_VALUE_RELATION))) {
			return DEFAULT_ATTRIBUTE_VALUE_RELATION;
		}
		return rel;
	}

}
