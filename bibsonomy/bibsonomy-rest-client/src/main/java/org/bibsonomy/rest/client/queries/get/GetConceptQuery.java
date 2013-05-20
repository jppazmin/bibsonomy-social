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

import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.util.ResourceUtils;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * Use this Class to get concepts 
 * 1) from all users
 * 2) from a specified group or
 * 3) from a specified user
 * 
 * @author Stefan Stützer
 * @version $Id: GetConceptQuery.java,v 1.10 2011-06-09 12:00:42 rja Exp $
 */
public class GetConceptQuery extends AbstractQuery<List<Tag>> {
	protected Class<? extends Resource> resourceType;
	private String groupingName;
	private ConceptStatus status = ConceptStatus.ALL;
	private String regex;	
	private GroupingEntity grouping = GroupingEntity.ALL;	
	private List<String> tags;	
	
	public GetConceptQuery() {
		this.downloadedDocument = null;		
	}
	
	@Override
	protected List<Tag> doExecute() throws ErrorPerformingRequestException {
		String url;
		
		switch (this.grouping) {
		case USER:
			url = URL_USERS + "/" + this.groupingName + "/" + URL_CONCEPTS;			
			break;
		case GROUP:
			throw new UnsupportedOperationException("Grouping " + grouping + " is not implemented yet");
			//url = URL_GROUPS + "/" + this.groupingName + "/" + URL_CONCEPTS;
			//break;
		case ALL:
			url = URL_CONCEPTS;
			break;
		default:
			throw new UnsupportedOperationException("Grouping " + grouping + " is not available for concept query");
		}
				
		if (this.status != null) {
			url += "?status=" + this.status.toString().toLowerCase();
		}

		if (this.resourceType != null) {
			url += "&resourcetype=" + ResourceUtils.toString(this.resourceType).toLowerCase();
		}
		
		if (this.regex != null) {
			url += "?filter=" + this.regex;
		}		
		
		if (this.tags != null && this.tags.size() > 0) {
			boolean first = true;
			for (final String tag : tags) {
				if (first) {
					url += "&tags=" + tag;
					first = false;
				} else {
					url += "+" + tag;
				}
			}
		}			
		
		this.downloadedDocument = performGetRequest(url);
		return null;
	}

	@Override
	public List<Tag> getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		return getRendererFactory().getRenderer(getRenderingFormat()).parseTagList(this.downloadedDocument);
	}

	public void setResourceType(Class<? extends Resource> resourceType) {
		this.resourceType = resourceType;
	}

	public void setUserName(final String userName) {
		this.groupingName = userName;
		this.grouping = GroupingEntity.USER;
	}

	public void setGroupName(final String groupName) {
		this.groupingName = groupName;
		this.grouping = GroupingEntity.GROUP;
	}
	
	public void setStatus(ConceptStatus status) {
		this.status = status;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}	

	public void setTags(List<String> tags) {
		this.tags = tags;
	}	
}