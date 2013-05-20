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

import org.bibsonomy.model.Group;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to change details of an existing group in bibsonomy.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ChangeGroupQuery.java,v 1.14 2011-06-09 12:00:49 rja Exp $
 */
public final class ChangeGroupQuery extends AbstractQuery<String> {
	private final Group group;
	private final String groupName;

	/**
	 * Changes details of an existing group in bibsonomy.
	 * 
	 * @param groupName
	 *            name of the group to be changed
	 * @param group
	 *            new values
	 * @throws IllegalArgumentException
	 *             if groupname is null or empty, or if the group has no name
	 *             specified
	 */
	public ChangeGroupQuery(final String groupName, final Group group) throws IllegalArgumentException {
		if (!present(groupName)) throw new IllegalArgumentException("no groupName given");
		if (!present(group)) throw new IllegalArgumentException("no group specified");
		if (!present(group.getName())) throw new IllegalArgumentException("no groupname specified");

		this.groupName = groupName;
		this.group = group;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializeGroup(sw, group, null);
		this.downloadedDocument = performRequest(HttpMethod.PUT, URL_GROUPS + "/" + this.groupName, sw.toString());
		return null;
	}
	
	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess())
			return getRendererFactory().getRenderer(getRenderingFormat()).parseGroupId(this.downloadedDocument); 
		return this.getError();
	}		
}