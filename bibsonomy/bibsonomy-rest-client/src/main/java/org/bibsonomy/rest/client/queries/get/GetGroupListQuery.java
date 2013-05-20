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

import org.bibsonomy.model.Group;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to receive an ordered list of all groups bibsonomy has.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetGroupListQuery.java,v 1.15 2011-06-09 12:00:43 rja Exp $
 */
public final class GetGroupListQuery extends AbstractQuery<List<Group>> {

	private final int start;
	private final int end;

	/**
	 * Gets bibsonomy's group list
	 */
	public GetGroupListQuery() {
		this(0, 19);
	}

	/**
	 * Gets bibsonomy's group list.
	 * 
	 * @param start
	 *            start of the list
	 * @param end
	 *            end of the list
	 */
	public GetGroupListQuery(int start, int end) {
		if (start < 0) start = 0;
		if (end < start) end = start;

		this.start = start;
		this.end = end;
	}

	@Override
	public List<Group> getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		return getRendererFactory().getRenderer(getRenderingFormat()).parseGroupList(this.downloadedDocument);
	}

	@Override
	protected List<Group> doExecute() throws ErrorPerformingRequestException {
		this.downloadedDocument = performGetRequest(URL_GROUPS + "?start=" + this.start + "&end=" + this.end);
		return null;
	}
}