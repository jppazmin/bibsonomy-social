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

package org.bibsonomy.rest.strategy.concepts;

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
 * Handles a global concepts request
 * 
 * @author Stefan Stützer
 * @version $Id: GetConceptsStrategy.java,v 1.6 2011-04-06 12:10:05 nosebrain Exp $
 */
public class GetConceptsStrategy extends Strategy {
	protected final Class<? extends Resource> resourceType;
	private final String regex;	
	private final List<String> tags;
	
	/**
	 * @param context
	 */
	public GetConceptsStrategy(Context context) {
		super(context);
		this.resourceType = ResourceUtils.getResource(context.getStringAttribute("resourcetype", "all"));				
		this.regex = context.getStringAttribute("filter", null);
		this.tags = context.getTags("tags");
	}
	
	@Override
	public void perform(final ByteArrayOutputStream outStream) throws InternServerException, NoSuchResourceException {
		final List<Tag> concepts = this.getLogic().getConcepts(resourceType, GroupingEntity.ALL, null, regex, tags, ConceptStatus.ALL, 0, Integer.MAX_VALUE);
		this.getRenderer().serializeTags(writer, concepts, new ViewModel());
	}

	@Override
	protected String getContentType() {
		return "tags";
	}
}