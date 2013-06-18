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
import org.bibsonomy.webapp.command.BibtexResourceViewCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * Controller for the basket page
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: BasketPageController.java,v 1.8 2010-11-17 10:55:35 nosebrain Exp $
 */
public class BasketPageController extends SingleResourceListController implements MinimalisticController<BibtexResourceViewCommand>, ErrorAware {

	private Errors errors;

	@Override
	public View workOn(BibtexResourceViewCommand command) {
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		// if user is not logged in, redirect him to login page
		if (command.getContext().isUserLoggedIn() == false) {
			errors.reject("error.general.login");
			return Views.ERROR;
		}				

		// set login user name + grouping entity = BASKET
		final String loginUserName = command.getContext().getLoginUser().getName();
		final GroupingEntity groupingEntity = GroupingEntity.BASKET;

		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {			
			final int entriesPerPage = command.getListCommand(resourceType).getEntriesPerPage();

			this.setList(command, resourceType, groupingEntity, loginUserName, null, null, null, null, null, entriesPerPage);
			this.postProcessAndSortList(command, resourceType);

			/*
			 * set all posts from basket page to "picked" such that their "pick"
			 * link changes to "unpick"
			 */
			for (final Post<? extends Resource> post : command.getListCommand(resourceType).getList()){
				post.setPicked(true);
			}

			// the number of items in this user's basket has already been fetched
			command.getListCommand(resourceType).setTotalCount(command.getContext().getLoginUser().getBasket().getNumPosts());
		}	

		this.endTiming();			
		if ("html".equals(format)) {
			command.setPageTitle(" :: basket" );
			return Views.BASKETPAGE;	
		}

		// export - return the appropriate view
		return Views.getViewByFormat(format);

	}

	@Override
	public BibtexResourceViewCommand instantiateCommand() {
		return new BibtexResourceViewCommand();
	}

	@Override
	public Errors getErrors() {
		return errors;
	}

	@Override
	public void setErrors(Errors errors) {
		this.errors = errors;
	}

}
