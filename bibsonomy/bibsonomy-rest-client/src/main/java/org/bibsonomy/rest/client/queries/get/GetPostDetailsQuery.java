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

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to receive details about a post of an user.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetPostDetailsQuery.java,v 1.17 2011-06-09 12:00:43 rja Exp $
 */
public final class GetPostDetailsQuery extends AbstractQuery<Post<? extends Resource>> {

	private final String username;
	private final String resourceHash;

	/**
	 * Gets details of a post of an user.
	 * 
	 * @param username
	 *            name of the user
	 * @param resourceHash
	 *            hash of the resource
	 * @throws IllegalArgumentException
	 *             if userName or resourceHash are null or empty
	 */
	public GetPostDetailsQuery(final String username, final String resourceHash) throws IllegalArgumentException {
		if (!present(username)) throw new IllegalArgumentException("no username given");
		if (!present(resourceHash)) throw new IllegalArgumentException("no resourceHash given");

		this.username = username;
		this.resourceHash = resourceHash;
	}

	@Override
	public Post<? extends Resource> getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		return getRendererFactory().getRenderer(getRenderingFormat()).parsePost(this.downloadedDocument);
	}

	@Override
	protected Post<? extends Resource> doExecute() throws ErrorPerformingRequestException {
		this.downloadedDocument = performGetRequest(URL_USERS + "/" + this.username + "/" + URL_POSTS + "/" + this.resourceHash);
		return null;
	}
}