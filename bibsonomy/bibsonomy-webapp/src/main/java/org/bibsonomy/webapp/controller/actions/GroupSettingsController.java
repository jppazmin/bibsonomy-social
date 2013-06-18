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

package org.bibsonomy.webapp.controller.actions;

import static org.bibsonomy.util.ValidationUtils.present;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupUpdateOperation;
import org.bibsonomy.common.enums.Privlevel;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.UserUtils;
import org.bibsonomy.webapp.command.SettingsViewCommand;
import org.bibsonomy.webapp.controller.SearchPageController;
import org.bibsonomy.webapp.controller.SettingsPageController;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;

/**
 * FIXME: refactor as subclass of {@link SettingsPageController}.
 * 
 * @author ema
 * @version $Id: GroupSettingsController.java,v 1.7 2011-07-26 14:51:34 rja Exp $
 */
public class GroupSettingsController implements MinimalisticController<SettingsViewCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(SearchPageController.class);

	/**
	 * hold current errors
	 */
	private Errors errors;
	private LogicInterface logic;

	@Override
	public SettingsViewCommand instantiateCommand() {
		return new SettingsViewCommand();
	}

	@Override
	public View workOn(final SettingsViewCommand command) {
		final RequestWrapperContext context = command.getContext();
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedException("please log in");
		}
		
		final User loginUser = context.getLoginUser();
		command.setUser(loginUser);
		
		// used to set the user specific value of maxCount/minFreq 
		command.setChangeTo((loginUser.getSettings().getIsMaxCount() ? loginUser.getSettings().getTagboxMaxCount() : loginUser.getSettings().getTagboxMinfreq()));
		
		// check whether the user is a group		
		if (UserUtils.userIsGroup(loginUser))  {
			command.setHasOwnGroup(true);
			command.showGroupTab(true);
		} else {
			// if he is not, he will be forwarded to the first settings tab
			command.showGroupTab(false);
			command.setSelTab(SettingsViewCommand.MY_PROFILE_IDX);
			this.errors.reject("settings.group.error.groupDoesNotExist");
			return Views.SETTINGSPAGE;
		}
			
		/*
		 * check the ckey
		 */
		if (!context.isValidCkey()) {
			this.errors.reject("error.field.valid.ckey");
			return Views.ERROR;
		}
		
		log.debug("User is logged in, ckey is valid");
		// the group properties to update
		final Privlevel priv = Privlevel.getPrivlevel(command.getPrivlevel());
		final boolean sharedDocs = command.getSharedDocuments() == 1;
		
		// the group to update
		final Group groupToUpdate = this.logic.getGroupDetails(loginUser.getName());
		
		if (!present(groupToUpdate)) {
			// TODO: are these statements unreachable? @see if (UserUtils.userIsGroup())
			command.showGroupTab(false);
			command.setSelTab(SettingsViewCommand.MY_PROFILE_IDX);
			this.errors.reject("settings.group.error.groupDoesNotExist");
			return Views.SETTINGSPAGE;
		}
		
		// update the bean
		groupToUpdate.setPrivlevel(priv);
		groupToUpdate.setSharedDocuments(sharedDocs);
		
		try {
			this.logic.updateGroup(groupToUpdate, GroupUpdateOperation.UPDATE_SETTINGS);
		} catch (final Exception ex) {
			// TODO: what exceptions can be thrown?!
		}
		return Views.SETTINGSPAGE;
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

}
