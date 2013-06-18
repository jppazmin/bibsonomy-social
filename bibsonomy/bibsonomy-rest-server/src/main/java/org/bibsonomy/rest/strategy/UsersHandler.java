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
import org.bibsonomy.rest.strategy.users.DeleteDocumentStrategy;
import org.bibsonomy.rest.strategy.users.DeletePostStrategy;
import org.bibsonomy.rest.strategy.users.DeleteUserConceptStrategy;
import org.bibsonomy.rest.strategy.users.DeleteUserStrategy;
import org.bibsonomy.rest.strategy.users.GetPostDetailsStrategy;
import org.bibsonomy.rest.strategy.users.GetPostDocumentStrategy;
import org.bibsonomy.rest.strategy.users.GetRelatedusersForUserStrategy;
import org.bibsonomy.rest.strategy.users.GetUserConceptStrategy;
import org.bibsonomy.rest.strategy.users.GetUserConceptsStrategy;
import org.bibsonomy.rest.strategy.users.GetUserListStrategy;
import org.bibsonomy.rest.strategy.users.GetUserPostsStrategy;
import org.bibsonomy.rest.strategy.users.GetUserStrategy;
import org.bibsonomy.rest.strategy.users.PostPostDocumentStrategy;
import org.bibsonomy.rest.strategy.users.PostPostStrategy;
import org.bibsonomy.rest.strategy.users.PostRelatedusersForUserStrategy;
import org.bibsonomy.rest.strategy.users.PostUserConceptStrategy;
import org.bibsonomy.rest.strategy.users.PostUserStrategy;
import org.bibsonomy.rest.strategy.users.PutPostStrategy;
import org.bibsonomy.rest.strategy.users.PutUserConceptStrategy;
import org.bibsonomy.rest.strategy.users.PutUserStrategy;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @author Christian Kramer
 * @version $Id: UsersHandler.java,v 1.11 2011-04-04 08:23:11 rja Exp $
 */
public class UsersHandler implements ContextHandler {

	@Override
	public Strategy createStrategy(final Context context, final StringTokenizer urlTokens, final HttpMethod httpMethod) {
		final int numTokensLeft = urlTokens.countTokens();
		final String userName;
		final String req;
		final RestProperties restProperties = RestProperties.getInstance();
		
		switch (numTokensLeft) {
		case 0:
			// /users
			return createUserListStrategy(context, httpMethod);
		case 1:
			// /users/[username]
			return createUserStrategy(context, httpMethod, urlTokens.nextToken());
		case 2:
			userName = urlTokens.nextToken();
			req = urlTokens.nextToken();

			// /users/[username]/posts
			if (restProperties.getPostsUrl().equalsIgnoreCase(req)) {
				return createUserPostsStrategy(context, httpMethod, userName);
			}

			// /users/[username]/concepts
			if (restProperties.getConceptUrl().equalsIgnoreCase(req)) {
				return createUserConceptsStrategy(context, httpMethod, userName);
			}

			// /users/[username]/friends , /users/[username]/followers 
			if (restProperties.getFriendsUrl().equalsIgnoreCase(req) ||
				restProperties.getFollowersUrl().equalsIgnoreCase(req) ) {
				return createRelatedusersForUserStrategy(context, httpMethod, userName, req, null);
			}
			break;
		case 3:
			userName = urlTokens.nextToken();
			req = urlTokens.nextToken();

			// /users/[username]/posts/[resourceHash]
			if (restProperties.getPostsUrl().equalsIgnoreCase(req)) {
				final String resourceHash = urlTokens.nextToken();
				return createUserPostStrategy(context, httpMethod, userName, resourceHash);
			}

			// /users/[username]/concepts/[conceptName]
			if (restProperties.getConceptUrl().equalsIgnoreCase(req)) {
				final String conceptName = urlTokens.nextToken();
				return createUserConceptsStrategy(context, httpMethod, userName, conceptName);
			}
			// /users/[username]/friends/[tag]
			if (restProperties.getFriendsUrl().equalsIgnoreCase(req)) {
				final String tag = urlTokens.nextToken();
				return createRelatedusersForUserStrategy(context, httpMethod, userName, req, tag);
			}
			break;
		case 4:
			// /users/[username]/posts/[resourcehash]/documents
			userName = urlTokens.nextToken();
			if (restProperties.getPostsUrl().equalsIgnoreCase(urlTokens.nextToken())) {
				final String resourceHash = urlTokens.nextToken();

				if (restProperties.getDocumentsUrl().equalsIgnoreCase(urlTokens.nextToken())) {
					return createDocumentPostStrategy(context, httpMethod, userName, resourceHash);
				}
			}
			break;
		case 5:
			// /users/[username]/posts/[resourcehash]/documents/[filename]
			userName = urlTokens.nextToken();
			if (restProperties.getPostsUrl().equalsIgnoreCase(urlTokens.nextToken())) {
				final String resourceHash = urlTokens.nextToken();

				if (restProperties.getDocumentsUrl().equalsIgnoreCase(urlTokens.nextToken())) {
					final String filename = urlTokens.nextToken();
					return createDocumentPostStrategy(context, httpMethod, userName, resourceHash, filename);
				}
			}
			break;
		}
		throw new NoSuchResourceException("cannot process url (no strategy available) - please check url syntax ");
	}

	private Strategy createUserListStrategy(final Context context, final HttpMethod httpMethod) {
		switch (httpMethod) {
		case GET:
			return new GetUserListStrategy(context);
		case POST:
			return new PostUserStrategy(context);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "UserList");
		}
	}

	private Strategy createUserStrategy(final Context context, final HttpMethod httpMethod, final String userName) {
		switch (httpMethod) {
		case GET:
			return new GetUserStrategy(context, userName);
		case PUT:
			return new PutUserStrategy(context, userName);
		case DELETE:
			return new DeleteUserStrategy(context, userName);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "User");
		}
	}

	private Strategy createUserPostsStrategy(final Context context, final HttpMethod httpMethod, final String userName) {
		switch (httpMethod) {
		case GET:
			return new GetUserPostsStrategy(context, userName);
		case POST:
			return new PostPostStrategy(context, userName);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "User-Post");
		}
	}

	private Strategy createUserPostStrategy(final Context context, final HttpMethod httpMethod, final String userName, final String resourceHash) {
		switch (httpMethod) {
		case GET:
			return new GetPostDetailsStrategy(context, userName, resourceHash);
		case PUT:
			return new PutPostStrategy(context, userName, resourceHash);
		case DELETE:
			return new DeletePostStrategy(context, userName, resourceHash);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "User");
		}
	}

	private Strategy createDocumentPostStrategy(final Context context, final HttpMethod httpMethod, final String userName, final String resourceHash) {
		switch (httpMethod) {
		case POST:
			return new PostPostDocumentStrategy(context, userName, resourceHash);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "User-Post-Document");
		}
	}

	private Strategy createDocumentPostStrategy(final Context context, final HttpMethod httpMethod, final String userName, final String resourceHash, final String filename) {
		switch (httpMethod) {
		case GET:
			return new GetPostDocumentStrategy(context, userName, resourceHash, filename);
		case DELETE:
			return new DeleteDocumentStrategy(context, userName, resourceHash, filename);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "Document-Get-Delete-Document");
		}
	}

	private Strategy createUserConceptsStrategy(final Context context, final HttpMethod httpMethod, final String userName) {
		switch (httpMethod) {
		case GET:
			return new GetUserConceptsStrategy(context, userName);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "Concepts");

		}
	}

	private Strategy createRelatedusersForUserStrategy(final Context context, final HttpMethod httpMethod, final String userName, final String relationship, String tag) {
		switch (httpMethod) {
		case GET:
			return new GetRelatedusersForUserStrategy(context, userName, relationship, tag);
		case POST:
			return new PostRelatedusersForUserStrategy(context, userName, relationship, tag);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "Friends");

		}
	}

	private Strategy createUserConceptsStrategy(final Context context, final HttpMethod httpMethod, final String userName, final String conceptName) {
		switch (httpMethod) {
		case GET:
			return new GetUserConceptStrategy(context, conceptName, userName);
		case PUT:
			return new PutUserConceptStrategy(context, userName);
		case POST:
			return new PostUserConceptStrategy(context, userName);
		case DELETE:
			return new DeleteUserConceptStrategy(context, conceptName, userName);
		default:
			throw new UnsupportedHttpMethodException(httpMethod, "Concept-Conceptname");
		}
	}
}