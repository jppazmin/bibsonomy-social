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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.SettingsViewCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.validation.DeleteUserValidator;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;

/**
 * @author daill
 * @version $Id: DeleteUserController.java,v 1.14 2011-07-14 13:43:37 nosebrain Exp $
 */
public class DeleteUserController implements ValidationAwareController<SettingsViewCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(DeleteUserController.class);
	
	private LogicInterface logic;
	private Errors errors;

	@Override
	public SettingsViewCommand instantiateCommand() {
		return new SettingsViewCommand();
	}
	
	@Override
	public View workOn(final SettingsViewCommand command) {
		final RequestWrapperContext context = command.getContext();
		
		/*
		 * user has to be logged in to delete himself
		 */
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedException("please log in");
		} 
		
		command.setUser(context.getLoginUser());
		
		/*
		 * go back to the settings page and display errors from command field
		 * validation
		 */
		if (errors.hasErrors()) {
			return Views.SETTINGSPAGE;
		}
		
		/*
		 * check the ckey
		 */
		if (context.isValidCkey()){
			log.debug("User is logged in, ckey is valid ... check the security answer");
			
			/*
			 * check the security input …
			 */
			if ("yes".equalsIgnoreCase(command.getDelete())) {
				/*
				 * all fine -> delete the user
				 */
				final String loginUserName = context.getLoginUser().getName();
				log.debug("answer is correct - deleting user: " + loginUserName);
				try {
					logic.deleteUser(loginUserName);
				} catch (final UnsupportedOperationException ex) {
					// this happens when a user is a group
					errors.reject("error.user_is_group_cannot_be_deleted");
				}
			} else {
				/*
				 * … else add an error
				 */
				errors.reject("error.secure.answer");
			}
		} else {
			errors.reject("error.field.valid.ckey");
		}
		

		if (errors.hasErrors()){
			return Views.SETTINGSPAGE;
		}
		
		return new ExtendedRedirectView("/logout");
	}
	
	/**
	 * @param logic
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	@Override
	public Validator<SettingsViewCommand> getValidator() {
		return new DeleteUserValidator();
	}

	@Override
	public boolean isValidationRequired(final SettingsViewCommand command) {
		return true;
	}	

}
