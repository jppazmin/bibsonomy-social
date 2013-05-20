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

import org.bibsonomy.common.enums.Status;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to add an user to an already existing group.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: AddUserToGroupQuery.java,v 1.14 2011-06-09 12:00:46 rja Exp $
 */
public final class AddUserToGroupQuery extends AbstractQuery<String> {
	private final User user;
	private final String groupName;

	/**
	 * Adds an user to an already existing group. <p/>note that the user and the
	 * group must exist before this query can be performed
	 * 
	 * @param groupname
	 *            name of the group the user is to be added to. the group must
	 *            exist, else a {@link IllegalArgumentException} is thrown
	 * @param user
	 *            the user to be added
	 * @throws IllegalArgumentException
	 *             if the groupname is null or empty, or if the user is null or
	 *             has no name defined
	 */
	public AddUserToGroupQuery(final String groupName, final User user) throws IllegalArgumentException {
		if (!present(groupName)) throw new IllegalArgumentException("no groupName given");
		if (!present(user)) throw new IllegalArgumentException("no user specified");
		if (!present(user.getName())) throw new IllegalArgumentException("no username specified");
	
		this.groupName = groupName;
		this.user = user;
	}
	
	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializeUser(sw, user, null);
		this.downloadedDocument = performRequest(HttpMethod.POST, URL_GROUPS + "/" + this.groupName + "/" + URL_USERS, sw.toString());
		return null;
	}
	
	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {				
		if (this.isSuccess())
			return Status.OK.getMessage();
		return this.getError();
	}
}