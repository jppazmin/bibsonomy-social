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

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.strategy.AbstractCreateStrategy;
import org.bibsonomy.rest.strategy.Context;

import static org.bibsonomy.rest.strategy.users.GetRelatedusersForUserStrategy.chooseRelationship;
import static org.bibsonomy.rest.strategy.users.GetRelatedusersForUserStrategy.chooseRelation;

/**
 * @author dbe
 * @version $Id: PostRelatedusersForUserStrategy.java,v 1.2 2011-03-21 10:49:57 dbe Exp $
 */
public class PostRelatedusersForUserStrategy extends AbstractCreateStrategy {
	
	private String userName;
	private String relation;
	private String tag;
	private UserRelation relationship;
	
	/**
	 * @param context
	 * @param userName
	 * @param relationship
	 * @param tag
	 */
	public PostRelatedusersForUserStrategy(final Context context, final String userName, final String relationship, final String tag) {
		super(context);
		this.userName = userName;
		this.tag = tag;
		this.relation = chooseRelation(context);
		this.relationship = chooseRelationship(relationship, this.relation);
	}

	@Override
	protected void render(Writer writer, String resourceID) {
		this.getRenderer().serializeUserId(writer, resourceID);
	}

	@Override
	protected String create() {
		User targetUser = parseUser();
		/*
		 * perform DB query. Exceptions are handled within the RestServlet.
		 */
		this.getLogic().createUserRelationship(this.userName, targetUser.getName(), relationship, tag);
		/*
		 * we return the userName as "resource id" to be sent back in the
		 * response in case of success 
		 */
		return this.userName;
	}
	
	private User parseUser() {
		return this.getRenderer().parseUser(this.doc);		
	}

}
