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

import java.util.Collections;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.systemstags.markup.MyOwnSystemTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.webapp.command.UserResourceViewCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for the cv page:
 * 	- /cv/user/USERNAME
 * 
 * @author Philipp Beau
 * @version $Id: CvPageController.java,v 1.9 2010-06-21 19:02:49 doerfel Exp $
 */
public class CvPageController extends ResourceListController implements MinimalisticController<UserResourceViewCommand> {

	/**
	 * implementation of {@link MinimalisticController} interface
	 */
	@Override
	public View workOn(final UserResourceViewCommand command) {
		final String requUser = command.getRequestedUser();

		if (!present(requUser)) {
			throw new MalformedURLSchemeException("error.cvpage_without_username");
		}

		command.setUser(this.logic.getUserDetails(requUser));

		final GroupingEntity groupingEntity = GroupingEntity.USER;

		this.setTags(command, Resource.class, groupingEntity, requUser, null, command.getRequestedTagsList(), null, 1000, null);

		/* 
		 * TODO: remove (lists are loaded by wiki tags) retrieve and set the
		 * requested publication(s) / bookmark(s) with the "myown" tag
		 */
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(command.getFormat(), command.getResourcetype())) {
			final int entriesPerPage = command.getListCommand(resourceType).getEntriesPerPage();		
			this.setList(command, resourceType, groupingEntity, requUser, Collections.singletonList(MyOwnSystemTag.NAME), null, Order.ADDED, null, null, entriesPerPage);
		}
		
		return Views.CVPAGE;
	}

	/**
	 * implementation of {@link MinimalisticController} interface
	 */
	@Override
	public UserResourceViewCommand instantiateCommand() {
		return new UserResourceViewCommand();
	}	
}
