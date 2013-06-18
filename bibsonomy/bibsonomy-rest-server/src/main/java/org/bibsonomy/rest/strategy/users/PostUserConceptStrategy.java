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

import java.io.Writer;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Tag;
import org.bibsonomy.rest.strategy.AbstractCreateStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * Handle a concept creation request
 * 
 * @author Stefan Stützer
 * @version $Id: PostUserConceptStrategy.java,v 1.1 2008-02-14 16:21:19 ss05sstuetzer Exp $
 */
public class PostUserConceptStrategy extends AbstractCreateStrategy {

	private final String userName;
	
	/**
	 * @param context - the context
	 * @param userName -  the owner of the new concept
	 */
	public PostUserConceptStrategy(Context context, String userName) {
		super(context);
		this.userName = userName;
	}

	@Override
	protected String create() {
		final Tag concept = this.getRenderer().parseTag(this.doc);
		return this.getLogic().createConcept(concept, GroupingEntity.USER, userName);				
	}

	@Override
	protected void render(Writer writer, String resourceHash) {
		this.getRenderer().serializeResourceHash(writer, resourceHash);	
	}

	@Override
	protected String getContentType() {
		return null;
	}
}