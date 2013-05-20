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

import org.bibsonomy.model.Group;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.util.StringUtils;

/**
 * Use this Class to create a new group in bibsonomy.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: CreateGroupQuery.java,v 1.16 2011-06-09 12:00:46 rja Exp $
 */
public final class CreateGroupQuery extends AbstractQuery<String> {
	private final Group group;

	/**
	 * Creates a new group account in bibsonomy.
	 * 
	 * @param group
	 *            the group to be created
	 * @throws IllegalArgumentException
	 *             if the group has no name is defined
	 */
	public CreateGroupQuery(final Group group) throws IllegalArgumentException {
		if (!present(group)) throw new IllegalArgumentException("no group specified");
		if (!present(group.getName())) throw new IllegalArgumentException("no groupname specified");

		this.group = group;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializeGroup(sw, this.group, null);
		this.downloadedDocument = performRequest(HttpMethod.POST, URL_GROUPS, StringUtils.toDefaultCharset(sw.toString()));
		return null;
	}
	
	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess())
			return getRendererFactory().getRenderer(getRenderingFormat()).parseGroupId(this.downloadedDocument); 
		return this.getError();
	}		
}