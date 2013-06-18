/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.controller;

import static org.bibsonomy.util.ValidationUtils.present;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.FriendsResourceViewCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * 
 * Shows the /friends overview page, i.d. all posts, set to "viewable for friends" 
 * by users which have loginUser as friend. 
 * 
 * @author Steffen Kress
 * @version $Id: FriendsPageController.java,v 1.15 2011-03-21 10:31:45 dbe Exp $
 */
public class FriendsPageController extends SingleResourceListController implements MinimalisticController<FriendsResourceViewCommand> {
	private static final Log log = LogFactory.getLog(FriendsPageController.class);

	@Override
	public View workOn(final FriendsResourceViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		// we need to be logged in
		if (!command.getContext().isUserLoggedIn()) {
			throw new MalformedURLSchemeException("error.friends_page_not_logged_in"); // TODO: redirect to login?!
		}
		
		// set grouping entity
		final GroupingEntity groupingEntity = GroupingEntity.FRIEND;
		
		// handle case when users are requested
		this.handleUsers(command);
		
		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			this.setList(command, resourceType, groupingEntity, null, null, null, null, null, null, command.getListCommand(resourceType).getEntriesPerPage());
			this.postProcessAndSortList(command, resourceType);
		}

		// set page title
		command.setPageTitle("friends"); // TODO: i18n
		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			this.endTiming();
			return Views.FRIENDSPAGE;
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);
	}

	/**
	 * Initialize user list, depending on chosen users type
	 * 
	 * @param command the command object
	 */
	protected void handleUsers(final FriendsResourceViewCommand command) {
		final String userRelation = command.getUserRelation();
		
		final String loginUserName = command.getContext().getLoginUser().getName();
		if ( (!present(userRelation)) && ("html".equals(command.getFormat())) ) {
			// this is the default case for the FriendsPageController
			command.setUserFriends(logic.getUserRelationship(loginUserName, UserRelation.FRIEND_OF, null));
			command.setFriendsOfUser(logic.getUserRelationship(loginUserName, UserRelation.OF_FRIEND, null));
		} else if (present(userRelation)) {
			if( UserRelation.OF_FRIEND.name().equalsIgnoreCase(userRelation) ) {
				command.setFriendsOfUser(logic.getUserRelationship(loginUserName, UserRelation.OF_FRIEND, null));
			} else if( UserRelation.FRIEND_OF.name().equalsIgnoreCase(userRelation) ) {
				command.setUserFriends(logic.getUserRelationship(loginUserName, UserRelation.FRIEND_OF, null));
			}
		
			// when users only are requested, we don't need resources
			this.setInitializeNoResources(true);		
		}
	}

	@Override
	public FriendsResourceViewCommand instantiateCommand() {
		return new FriendsResourceViewCommand();
	}
}
