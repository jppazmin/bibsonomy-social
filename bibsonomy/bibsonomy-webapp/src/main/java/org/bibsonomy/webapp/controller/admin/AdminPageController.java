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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.admin.AdminCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * Controller for admin page
 * 
 * @author Beate Krause
 * @version $Id: AdminPageController.java,v 1.5 2011-07-14 15:19:56 nosebrain Exp $
 */
public class AdminPageController implements	MinimalisticController<AdminCommand> {
	private static final Log log = LogFactory.getLog(AdminPageController.class);

	
	private LogicInterface logic;

	@Override
	public View workOn(final AdminCommand command) {
		log.debug(this.getClass().getSimpleName());

		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		/* Check user role
		 * If user is not logged in or not an admin: show error message */
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in");
		}

		/*
		 * get information about a specific user
		 */
		if (command.getAclUserInfo() != null) {
			log.debug("Get information for: " + command.getAclUserInfo());
			command.setUser(logic.getUserDetails(command.getAclUserInfo()));
		}
		
		/*
		 * add a group to the system
		 */
		log.debug("Group name " + command.getRequestedGroupName());
		if (command.getRequestedGroupName() != null){
			// create the new group
			final Group newGroup = new Group(command.getRequestedGroupName());
			newGroup.setPrivlevel(command.getSelPrivlevel());
			// update group
			logic.createGroup(newGroup);	
			// inform user about success
			command.setAdminResponse("Successfully created a group");
		}
		
		return Views.ADMIN;
	}

	@Override
	public AdminCommand instantiateCommand() {
		return new AdminCommand();
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}
}