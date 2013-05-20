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

package org.bibsonomy.rest.client.queries.get;

import java.util.List;

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Returns a list of users which either the requested user has in his friend list,
 * or all users, which have the requested user in his friend list.
 * 
 * TODO: Should be replaced by a generic strategy which allows to fetch users
 *  based on the different {@link UserRelation}s.
 * 
 * @version $Id: GetFriendsQuery.java,v 1.8 2011-06-09 12:00:42 rja Exp $
 */
@Deprecated
public final class GetFriendsQuery extends AbstractQuery<List<User>> {

	/**
	 * Request Attribute ?relation="incoming/outgoing"
	 */
	public static final String ATTRIBUTE_KEY_RELATION = "relation";
	public static final String INCOMING_ATTRIBUTE_VALUE_RELATION = "incoming";
	public static final String OUTGOING_ATTRIBUTE_VALUE_RELATION = "outgoing";
	public static final String DEFAULT_ATTRIBUTE_VALUE_RELATION = INCOMING_ATTRIBUTE_VALUE_RELATION;

	private final int start;
	private final int end;
	private final String relation;
	private final String username;

	/**
	 * Gets bibsonomy's user list.
	 * 
	 * @param start
	 *            start of the list
	 * @param end
	 *            end of the list
	 */
	public GetFriendsQuery(int start, int end, String username, String relation) {
		if (start < 0) start = 0;
		if (end < start) end = start;
		
		this.username = username;

		if (relation == null)
			this.relation = DEFAULT_ATTRIBUTE_VALUE_RELATION;
		else
			this.relation = relation;

		this.start = start;
		this.end = end;
	}

	@Override
	public List<User> getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		return getRendererFactory().getRenderer(getRenderingFormat()).parseUserList(this.downloadedDocument);
	}

	@Override
	protected List<User> doExecute() throws ErrorPerformingRequestException {
		this.downloadedDocument = performGetRequest(URL_USERS + "/" + this.username + "/" + URL_FRIENDS + "?" + ATTRIBUTE_KEY_RELATION + "=" + relation + "&start=" + this.start + "&end=" + this.end);
		return null;
	}
}