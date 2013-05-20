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

import java.util.List;

import org.bibsonomy.model.User;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to receive an ordered list of all users belonging to a given
 * group.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetUserListOfGroupQuery.java,v 1.16 2011-06-09 12:00:43 rja Exp $
 */
public final class GetUserListOfGroupQuery extends AbstractQuery<List<User>> {

	private final String groupname;
	private final int start;
	private final int end;

	/**
	 * Gets an user list of a group
	 */
	public GetUserListOfGroupQuery(final String groupname) {
		this(groupname, 0, 19);
	}

	/**
	 * Gets an user list of a group.
	 * 
	 * @param start
	 *            start of the list
	 * @param end
	 *            end of the list
	 * @throws IllegalArgumentException
	 *             if the groupname is null or empty
	 */
	public GetUserListOfGroupQuery(final String groupname, int start, int end) throws IllegalArgumentException {
		if (!present(groupname)) throw new IllegalArgumentException("no groupname given");
		if (start < 0) start = 0;
		if (end < start) end = start;

		this.groupname = groupname;
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
		this.downloadedDocument = performGetRequest(URL_GROUPS + "/" + this.groupname + "/" + URL_USERS + "?start=" + this.start + "&end=" + this.end);
		return null;
	}
}