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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.User;
import org.bibsonomy.opensocial.oauth.database.IOAuthLogic;
import org.bibsonomy.opensocial.oauth.database.beans.OAuthConsumerInfo;
import org.bibsonomy.webapp.command.BaseCommand;
import org.bibsonomy.webapp.command.opensocial.OAuthAdminCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.validation.opensocial.BibSonomyOAuthValidator;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;

/**
 * Controller for managing OAuth consumer keys
 * 
 * @author Folke Mitzlaff
 * @version $Id: AdminOAuthController.java,v 1.4 2011-07-14 15:19:56 nosebrain Exp $
 **/
public class AdminOAuthController implements ValidationAwareController<OAuthAdminCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(AdminOAuthController.class);
	
	/** database access to the OAuth consumer store */
	private IOAuthLogic oauthLogic;
	
	/**
	 * hold current errors
	 */
	private Errors errors = null;
	
	//------------------------------------------------------------------------
	// MinimalisticController interface
	//------------------------------------------------------------------------
	@Override
	public OAuthAdminCommand instantiateCommand() {
		final OAuthAdminCommand command = new OAuthAdminCommand();
		command.setConsumerInfo(new OAuthConsumerInfo());
		return command;
	}
	
	@Override
	public View workOn(final OAuthAdminCommand command) {
		ensureAdminAcess(command);

		// show errors if command validation failed
		if (errors.hasErrors()) {
			command.setAdminAction(OAuthAdminCommand.AdminAction.List.name());
		}
		
		if (!present(command.getAdminAction())) {
			command.setAdminAction(OAuthAdminCommand.AdminAction.List.name());
		}
		
		switch (command.getAdminAction_()) {
		case Register:
			this.oauthLogic.createConsumer(command.getConsumerInfo());
			//$FALL-THROUGH$ to get the list of consumerInfos
		case List:
			final List<OAuthConsumerInfo> consumerInfo = this.oauthLogic.listConsumers();
			command.setConsumers(consumerInfo);
			break;
		default:
			log.error("Invalid action given for administrating OAuth.");
		}
		
		return Views.ADMIN_OAUTH;
	}

	//------------------------------------------------------------------------
	// ValidationAwareController interface
	//------------------------------------------------------------------------
	@Override
	public Validator<OAuthAdminCommand> getValidator() {
		return new BibSonomyOAuthValidator();
	}

	@Override
	public boolean isValidationRequired(final OAuthAdminCommand command) {
		return true;
	}

	//------------------------------------------------------------------------
	// private helpers
	//------------------------------------------------------------------------
	/**
	 * ensure that the requesting user is logged in and an administrator
	 * @param command
	 */
	private void ensureAdminAcess(final BaseCommand command) {
		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in as admin");
		}
	}

	/**
	 * @param oauthLogic the oauth logic to set
	 */
	public void setOauthLogic(final IOAuthLogic oauthLogic) {
		this.oauthLogic = oauthLogic;
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

}