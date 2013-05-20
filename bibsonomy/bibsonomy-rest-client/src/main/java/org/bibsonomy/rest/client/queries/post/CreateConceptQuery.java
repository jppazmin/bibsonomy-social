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

import java.io.StringWriter;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Tag;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.util.StringUtils;

/**
 * Use this Class to create a new concept
 * 
 * @author Stefan Stützer
 * @version $Id: CreateConceptQuery.java,v 1.8 2011-06-09 12:00:46 rja Exp $
 */
public class CreateConceptQuery extends AbstractQuery<String> {
	private final Tag concept;
	private final String conceptName;	
	private final GroupingEntity grouping;
	private final String groupingName;
	
	
	public CreateConceptQuery(final Tag concept,final String conceptName, final GroupingEntity grouping, final String groupingName) {
		this.concept = concept;
		this.conceptName = conceptName;
		this.grouping = grouping;
		this.groupingName = groupingName;		
	}
	
	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		String url;
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializeTag(sw, concept, null);
		
		switch (grouping) {
		case USER:
			url = URL_USERS;			
			break;
		case GROUP:
			url = URL_GROUPS;
			break;
		default:
			throw new UnsupportedOperationException("Grouping " + grouping + " is not available for concept change query");
		}		
		
		url += "/" + this.groupingName + "/" + URL_CONCEPTS + "/" + this.conceptName;
		this.downloadedDocument = performRequest(HttpMethod.POST, url, StringUtils.toDefaultCharset(sw.toString()));
		return null;
	}

	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess())
			return getRendererFactory().getRenderer(getRenderingFormat()).parseResourceHash(this.downloadedDocument); 
		return this.getError();
	}
}