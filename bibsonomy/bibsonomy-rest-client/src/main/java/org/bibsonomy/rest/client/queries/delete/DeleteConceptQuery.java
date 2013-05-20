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

package org.bibsonomy.rest.client.queries.delete;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.Status;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to delete a concept or a single relation.
 * 
 * @author Stefan Stützer
 * @version $Id: DeleteConceptQuery.java,v 1.6 2011-05-24 09:38:20 bibsonomy Exp $
 */
public class DeleteConceptQuery extends AbstractQuery<String> {

	private final String conceptName;
	private final GroupingEntity grouping;
	private final String groupingName;
	
	/** if is set only the relation <em>conceptName <- subTag </em> will be deleted */
	private String subTag;
	
	public DeleteConceptQuery(final String conceptName, final GroupingEntity grouping, final String groupingName) {
		this.conceptName = conceptName;
		this.grouping = grouping;
		this.groupingName = groupingName;
		this.downloadedDocument = null;
	}
	
	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		String url;
		
		switch (grouping) {
		case USER:	
			url = URL_USERS; 
			break;
		case GROUP:
			url = URL_GROUPS;
			break;
		default:
			throw new UnsupportedOperationException("Grouping " + grouping + " is not available for concept delete query");
		}

		url += "/" + this.groupingName + "/" + URL_CONCEPTS + "/" + this.conceptName;
		
		if (subTag != null) {
			url += "?subtag=" + this.subTag;
		}
		
		this.downloadedDocument = performRequest(HttpMethod.DELETE, url, null);
		return null;	
	}

	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess())
			return Status.OK.getMessage();
		return this.getError();
	}

	public void setSubTag(String subTag) {
		this.subTag = subTag;
	}	
}