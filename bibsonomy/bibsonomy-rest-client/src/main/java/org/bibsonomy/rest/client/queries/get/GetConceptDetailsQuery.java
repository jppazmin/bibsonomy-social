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

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Tag;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to get information about the specified concept
 * 
 * @author Stefan Stützer
 * @version $Id: GetConceptDetailsQuery.java,v 1.9 2011-06-09 12:00:42 rja Exp $
 */
public class GetConceptDetailsQuery extends AbstractQuery<Tag> {

	private final String conceptname;
	private String groupingName;
	private GroupingEntity grouping = GroupingEntity.ALL;
	
	public GetConceptDetailsQuery(final String conceptName) {
		this.conceptname = conceptName;
		this.downloadedDocument = null;
	}
	
	@Override
	protected Tag doExecute() throws ErrorPerformingRequestException {
		String url = null;
		
		switch (this.grouping) {
		case USER:
			url = URL_USERS + "/" + this.groupingName + "/" + URL_CONCEPTS + "/" + this.conceptname;
			break;
		case GROUP:
			throw new UnsupportedOperationException("Grouping " + grouping + " is not implemented yet");
			//url = URL_GROUPS + "/" + this.groupingName + "/" + URL_CONCEPTS + "/" + this.conceptname;
			//break;
		case ALL:
			url = URL_CONCEPTS + "/" + this.conceptname;  
			break;			
		default:
			throw new UnsupportedOperationException("Grouping " + grouping + " is not available for concept details query");
		}
		
		this.downloadedDocument = performGetRequest(url);
		return null;
	}

	@Override
	public Tag getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		return getRendererFactory().getRenderer(getRenderingFormat()).parseTag(this.downloadedDocument);
	}
	
	public void setUserName(final String userName) {
		this.groupingName = userName;
		this.grouping = GroupingEntity.USER;
	}
	
	public void setGroupName(final String groupName) {
		this.groupingName = groupName;
		this.grouping = GroupingEntity.GROUP;
	}	
}