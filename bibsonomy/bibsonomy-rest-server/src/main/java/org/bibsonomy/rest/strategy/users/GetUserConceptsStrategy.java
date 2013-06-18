/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest.strategy.users;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.util.ResourceUtils;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.rest.strategy.Strategy;

/**
 * Handle a user concepts request
 * 
 * @author Stefan Stützer
 * @version $Id: GetUserConceptsStrategy.java,v 1.7 2011-04-06 12:10:04 nosebrain Exp $
 */
public class GetUserConceptsStrategy extends Strategy {
	protected final Class<? extends Resource> resourceType;
	private final String userName;
	private final ConceptStatus status;
	private final String regex;	
	private final List<String> tags;
	
	/**
	 * @param context
	 * @param userName
	 */
	public GetUserConceptsStrategy(final Context context, final String userName) {
		super(context);
		this.userName = userName;
		this.resourceType = ResourceUtils.getResource(context.getStringAttribute("resourcetype", "all"));				
		this.status = ConceptStatus.getConceptStatus(context.getStringAttribute("status", "all"));
		this.regex = context.getStringAttribute("filter", null);
		this.tags = context.getTags("tags");
	}

	@Override
	public void perform(final ByteArrayOutputStream outStream) throws InternServerException, NoSuchResourceException {
		final List<Tag> concepts = this.getLogic().getConcepts(resourceType, GroupingEntity.USER, userName, regex, tags, status, 0, Integer.MAX_VALUE);
		this.getRenderer().serializeTags(writer, concepts, new ViewModel());			
	}
	
	@Override
	protected String getContentType() {
		return "tags";
	}
}