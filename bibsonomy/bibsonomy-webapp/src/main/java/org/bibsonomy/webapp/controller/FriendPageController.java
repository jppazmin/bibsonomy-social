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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.UserResourceViewCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Steffen Kress
 * @version $Id: FriendPageController.java,v 1.15 2010-11-17 10:55:35 nosebrain Exp $
 */
public class FriendPageController extends SingleResourceListControllerWithTags implements MinimalisticController<UserResourceViewCommand> {
	private static final Log log = LogFactory.getLog(FriendPageController.class);
	
	private static final int THRESHOLD = 20000;

	@Override
	public View workOn(final UserResourceViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		
		if (!command.getContext().isUserLoggedIn()){
			log.info("Trying to access a friendpage without being logged in");
			return new ExtendedRedirectView("/login");
		}
		
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);
		
		final String groupingName = command.getRequestedUser();

		// no user given -> error
		if (!present(groupingName)) {
			throw new MalformedURLSchemeException("error.friend_page_without_friendname");
		}

		// set grouping entity, grouping name, tags
		final GroupingEntity groupingEntity = GroupingEntity.FRIEND;
		final List<String> requTags = command.getRequestedTagsList();

		// handle the case when tags only are requested
		this.handleTagsOnly(command, groupingEntity, groupingName, null, requTags, null, Integer.MAX_VALUE, null);	

		// retrieve and set the requested resource lists, along with total
		// counts
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			this.setList(command, resourceType, groupingEntity, groupingName, requTags, null, null, null, null, command.getListCommand(resourceType).getEntriesPerPage());
			this.postProcessAndSortList(command, resourceType);
		}
		// set page title
		command.setPageTitle("friend :: " + groupingName);  // TODO: i18n

		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			this.setTags(command, Resource.class, groupingEntity, groupingName, null, requTags, null, 20000, null);

			// log if a user has reached threshold
			if (command.getTagcloud().getTags().size() >= THRESHOLD) {
				log.error("User " + groupingName + " has reached threshold of " + THRESHOLD + " tags on friend page");
			}
			
			/*
			 * add user details to command, if loginUser is admin
			 */
			if (Role.ADMIN.equals(logic.getAuthenticatedUser().getRole())) {
				command.setUser(logic.getUserDetails(groupingName));
			}

			this.endTiming();
			return Views.FRIENDPAGE; 
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);
	}

	@Override
	public UserResourceViewCommand instantiateCommand() {
		return new UserResourceViewCommand();
	}
}
