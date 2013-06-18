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

package org.bibsonomy.webapp.controller.admin;

import static org.bibsonomy.util.ValidationUtils.present;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupUpdateOperation;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.admin.AdminGroupViewCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * Controller for group admin page
 * 
 * @author bsc
 * @version $Id: AdminGroupController.java,v 1.6 2011-07-14 15:19:56 nosebrain Exp $
 */
public class AdminGroupController implements MinimalisticController<AdminGroupViewCommand> {
	private static final Log log = LogFactory.getLog(AdminGroupController.class);
	private LogicInterface logic;

	/* Possible actions */
	private static final String FETCH_GROUP_SETTINGS  = "fetchGroupSettings"; 
	private static final String UPDATE_GROUP 		  = "updateGroup";  
	private static final String CREATE_GROUP          = "createGroup";


	@Override
	public View workOn(final AdminGroupViewCommand command) {
		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		/* Check user role
		 * If user is not logged in or not an admin: show error message */
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in as admin");
		}

		/* Check for and perform the specified action */
		final String action = command.getAction();
		if(!present(action)) {
			log.debug("No action specified.");
		} else if (FETCH_GROUP_SETTINGS.equals(action)) {
			final String groupName = command.getGroup().getName();
			final Group fetchedGroup = logic.getGroupDetails(groupName);

			if (present(fetchedGroup)) {
				command.setGroup(fetchedGroup);
			} else {
				command.setAdminResponse("The group \"" + groupName + "\" does not exist.");
			}
		} else if (UPDATE_GROUP.equals(action)) {
			command.setAdminResponse(updateGroup(command.getGroup()));
		} else if (CREATE_GROUP.equals(action)) {
			command.setAdminResponse(createGroup(command.getGroup()));
		}

		return Views.ADMIN_GROUP;
	}



	/**
	 * Create a new group
	 * 
	 * @param command
	 */
	private String createGroup(final Group group) {
		/*
		 * Check if group-name is empty
		 */
		final String groupName = group.getName();
		if (!present(groupName)) {
			return "Group-creation failed: Group-name is empty!";
		}
		/*
		 * check database for existing group
		 */
		if (present(logic.getGroupDetails(groupName))) {
			return "Group already exists!";
		} 
		
		/*
		 * check corresponding group user
		 */
		final User user = logic.getUserDetails(groupName);

		// Check if user exists
		if (!present(user) || !present(user.getName())) {
			return "Group-creation failed: Cannot create a group for nonexistent username \"" + groupName + "\"." ;
		}
		// Check if the user is a spammer
		else if (user.isSpammer()) {
			return "Group-creation failed: No groups allowed for users tagged as \"spammer\".";
		} 
		// Create the group, otherwise.
		else {
			logic.createGroup(group);
			return "Successfully created new group!";
		}
	}

	/** Update the settings of a group. */
	private String updateGroup(final Group commandGroup) {
		final Group dbGroup = logic.getGroupDetails(commandGroup.getName());

		if (!present(dbGroup)) {
			return "The group \"" + commandGroup + "\" does not exist.";
		}
		dbGroup.setPrivlevel(commandGroup.getPrivlevel());
		dbGroup.setSharedDocuments(commandGroup.isSharedDocuments());

		logic.updateGroup(dbGroup, GroupUpdateOperation.UPDATE_SETTINGS);
		return "Group updated successfully!";
	}

	@Override
	public AdminGroupViewCommand instantiateCommand() {
		return new AdminGroupViewCommand();
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}
}