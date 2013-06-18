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

package org.bibsonomy.rest.strategy;

import java.util.StringTokenizer;

import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.exceptions.UnsupportedHttpMethodException;
import org.bibsonomy.rest.strategy.groups.AddGroupStrategy;
import org.bibsonomy.rest.strategy.groups.AddUserToGroupStrategy;
import org.bibsonomy.rest.strategy.groups.DeleteGroupStrategy;
import org.bibsonomy.rest.strategy.groups.GetGroupStrategy;
import org.bibsonomy.rest.strategy.groups.GetListOfGroupsStrategy;
import org.bibsonomy.rest.strategy.groups.GetUserListOfGroupStrategy;
import org.bibsonomy.rest.strategy.groups.RemoveUserFromGroupStrategy;
import org.bibsonomy.rest.strategy.groups.UpdateGroupDetailsStrategy;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GroupsHandler.java,v 1.4 2010-07-14 08:31:43 nosebrain Exp $
 */
public class GroupsHandler implements ContextHandler {

	@Override
	public Strategy createStrategy(final Context context, final StringTokenizer urlTokens, final HttpMethod httpMethod) {
		final int numTokensLeft = urlTokens.countTokens();
		final String groupName;

		switch (numTokensLeft) {
		case 0:
			// /groups
			return createGroupListStrategy(context, httpMethod);
		case 1:
			// /groups/[groupname]
			return createGroupStrategy(context, httpMethod, urlTokens.nextToken());
		case 2:
			groupName = urlTokens.nextToken();
			if (RestProperties.getInstance().getUsersUrl().equalsIgnoreCase(urlTokens.nextToken())) {
				// /groups/[groupname]/users
				return createUserPostsStrategy(context, httpMethod, groupName);
			}
			break;
		case 3:
			// /groups/[groupname]/users/[username]
			groupName = urlTokens.nextToken();
			if (RestProperties.getInstance().getUsersUrl().equalsIgnoreCase(urlTokens.nextToken())) {
				if (HttpMethod.DELETE == httpMethod) {
					return new RemoveUserFromGroupStrategy(context, groupName, urlTokens.nextToken());
				}
			}
			break;
		}
		throw new NoSuchResourceException("cannot process url (no strategy available) - please check url syntax ");
	}

	private Strategy createGroupListStrategy(final Context context, final HttpMethod httpMethod) {
		switch (httpMethod) {
		case GET:
			return new GetListOfGroupsStrategy(context);
		case POST:
			return new AddGroupStrategy(context);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "GroupList");
		}
	}

	private Strategy createGroupStrategy(final Context context, final HttpMethod httpMethod, final String groupName) {
		switch (httpMethod) {
		case GET:
			return new GetGroupStrategy(context, groupName);
		case PUT:
			return new UpdateGroupDetailsStrategy(context, groupName);
		case DELETE:
			return new DeleteGroupStrategy(context, groupName);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "Group");
		}
	}

	private Strategy createUserPostsStrategy(final Context context, final HttpMethod httpMethod, final String groupName) {
		switch (httpMethod) {
		case GET:
			return new GetUserListOfGroupStrategy(context, groupName);
		case POST:
			return new AddUserToGroupStrategy(context, groupName);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "Group");
		}
	}
}