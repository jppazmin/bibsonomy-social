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

package org.bibsonomy.rest.client.queries.put;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.StringWriter;

import org.bibsonomy.model.User;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to change details of an existing user account.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ChangeUserQuery.java,v 1.14 2011-06-09 12:00:49 rja Exp $
 */
public final class ChangeUserQuery extends AbstractQuery<String> {
	private final User user;
	private final String userName;

	/**
	 * Changes details of an existing user account.
	 * 
	 * @param userName
	 *            the user to change
	 * @param user
	 *            new values
	 * @throws IllegalArgumentException
	 *             if the username is null or empty, or if the user hat no name
	 *             specified.
	 */
	public ChangeUserQuery(final String userName, final User user) throws IllegalArgumentException {
		if (!present(userName)) throw new IllegalArgumentException("no username given");
		if (!present(user)) throw new IllegalArgumentException("no user specified");
		if (!present(user.getName())) throw new IllegalArgumentException("no username specified");

		this.userName = userName;
		this.user = user;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializeUser(sw, this.user, null);
		this.downloadedDocument = performRequest(HttpMethod.PUT, URL_USERS + "/" + this.userName, sw.toString());
		return null;
	}
	
	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess())
			return getRendererFactory().getRenderer(getRenderingFormat()).parseUserId(this.downloadedDocument); 
		return this.getError();
	}	
}