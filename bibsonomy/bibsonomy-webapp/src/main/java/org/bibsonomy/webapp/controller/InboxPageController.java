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

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.UserResourceViewCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * Controller for the InboxPage (shows all posts in your inbox)
 * 
 * @author sdo
 * @version $Id: InboxPageController.java,v 1.10 2010-11-17 10:55:35 nosebrain Exp $
 */
public class InboxPageController extends SingleResourceListController implements MinimalisticController<UserResourceViewCommand>, ErrorAware {
	private Errors errors;

	@Override
	public View workOn(final UserResourceViewCommand command) {
		/*
		 * FIXME: implement filter=no parameter
		 */

		// user has to be logged in
		if (!command.getContext().isUserLoggedIn()){
			errors.reject("error.general.login");
			return Views.ERROR; // TODO: redirect to login page
		}
		
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);
				
		final String loginUserName = command.getContext().getLoginUser().getName();
		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			final int entriesPerPage = command.getListCommand(resourceType).getEntriesPerPage();
			this.setList(command, resourceType, GroupingEntity.INBOX, loginUserName, null, null, null, null, null, entriesPerPage);
			postProcessAndSortList(command, resourceType);
			/*
			 * mark all posts to be inbox posts (such that the "remove" link appears 
			 */
			for (final Post<? extends Resource> post: command.getListCommand(resourceType).getList()){
				post.setInboxPost(true);
			}
			// the number of items in this user's inbox has already been fetched
			this.setTotalCount(command, resourceType, GroupingEntity.INBOX, loginUserName, null, null, null, null, null, entriesPerPage, null);
		}
		this.endTiming();

		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			command.setPageTitle("inbox"); // TODO: i18n
			return Views.INBOX;		
		}

		// export - return the appropriate view
		return Views.getViewByFormat(format);	
	}

	@Override
	public UserResourceViewCommand instantiateCommand() {
		return new UserResourceViewCommand();
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(Errors errors) {
		this.errors = errors;
	}

}
