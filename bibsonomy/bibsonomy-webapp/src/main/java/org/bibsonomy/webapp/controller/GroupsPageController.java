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

/**
 * 
 */
package org.bibsonomy.webapp.controller;

import org.bibsonomy.webapp.command.GroupsListCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for group overview:
 * - groups
 * 
 * @author Folke Eisterlehner
 * @version $Id: GroupsPageController.java,v 1.4 2010-11-17 10:55:34 nosebrain Exp $
 */
public class GroupsPageController extends SingleResourceListController implements MinimalisticController<GroupsListCommand> {

	/**
	 * implementation of {@link MinimalisticController} interface
	 */
	@Override
	public View workOn(final GroupsListCommand command) {
		// fill out title
		command.setPageTitle("groups"); // TODO: i18n
		
		/*
		 * get all groups from db; Integer#MAX_VALUE should be enough
		 */
		command.setList(logic.getGroups(0, Integer.MAX_VALUE));
		
		return Views.GROUPSPAGE;
	}

	/**
	 * implementation of {@link MinimalisticController} interface
	 */
	@Override
	public GroupsListCommand instantiateCommand() {
		return new GroupsListCommand();
	}	
}
