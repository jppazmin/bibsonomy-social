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

import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.MailUtils;
import org.bibsonomy.util.spring.security.UserAdapter;
import org.bibsonomy.webapp.command.actions.UserActivationCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;

/**
 * controller for
 * 		- activate/ACTIVATIONCODE
 * 
 * @author Clemens Baier
 * @version $Id: UserActivationController.java,v 1.9 2011-03-04 16:42:42 folke Exp $
 */
public class UserActivationController implements MinimalisticController<UserActivationCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(UserActivationController.class);

	private RequestLogic requestLogic;
	private LogicInterface logic;
	private Errors errors;
	private MailUtils mailUtils;
	private AuthenticationManager authenticationManager;
	
	private String successRedirect = "/activate_success";

	@Override
	public UserActivationCommand instantiateCommand() {
		return new UserActivationCommand();
	}

	@Override
	public View workOn(UserActivationCommand command) {
		final RequestWrapperContext context = command.getContext();
		command.setPageTitle("activation");
		
		/*
		 * user must not be logged in
		 */
		if (context.isUserLoggedIn()) {
			throw new AccessDeniedException("error.logged.in.user.activate");
		}
		
		final String activationCode = command.getActivationCode();
		
		final List<User> list = logic.getUsers(null, GroupingEntity.PENDING, null, null, null, null, null, activationCode, 0, 1);
		User pendingUser = null;
		if (list.size() == 0 || !present(activationCode)) {
			errors.reject("error.illegal_activation_code");
		} else {
			log.debug("trying to activate user with code '" + activationCode + "'");

			pendingUser = list.get(0);
			
			/* 
			 * FIXME: this check should be done by the userdatabasemanager in
			 * the activate user method
			 * 
			 * check, if activation code is invalid.
			 * 
			 * now < registration_date + 24h
			 */
			final Calendar now = Calendar.getInstance();
			final Calendar activationCodeExpirationDate = Calendar.getInstance();
			activationCodeExpirationDate.setTime(pendingUser.getRegistrationDate());
			activationCodeExpirationDate.add(Calendar.HOUR, 24);
			if (!now.before(activationCodeExpirationDate)) {
				errors.reject("error.activation_code_expired");
			}
		}

		/*
		 * if there are errors, show them
		 */
		if (errors.hasErrors()) {
			return Views.ERROR;
		}

		/*
		 * activate user
		 */
		logic.updateUser(pendingUser, UserUpdateOperation.ACTIVATE);

		/*
		 * send activation confirmation mail
		 */
		try {
			mailUtils.sendActivationMail(pendingUser.getName(), pendingUser.getEmail(), requestLogic.getInetAddress(), requestLogic.getLocale());
		} catch (final Exception e) {
			log.error("Could not send activation confirmation mail for user " + pendingUser.getName(), e);
		}
		
		/*
		 * log the user into the system, e.g., authenticate the user
		 * (luckily, getUsers() includes the password of the user - if not, this
		 * would not work and we would have to call getUserDetails() first). See
		 * also the next comment.
		 */
		final UserDetails userDetails = new UserAdapter(pendingUser);
		final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, pendingUser.getPassword());

		/*
		 * In principle we could directly call SecurityContextHolder.getContext().setAuthentication()
		 * with the constructed authentication. However, the pendingUser 
		 * returned by getUsers() does not include the user's settings - which 
		 * are retrieved from the database by calling authenticate() on the 
		 * authenticationManager.
		 */
		final Authentication authenticated = authenticationManager.authenticate(authentication);
		
		SecurityContextHolder.getContext().setAuthentication(authenticated);

		/*
		 * TODO: ask user if "remember me" cookie shall be added
		 */
		
		return new ExtendedRedirectView(successRedirect);
	}

	/**
	 * @param logic
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @return errors
	 */
	@Override
	public Errors getErrors() {
		return this.errors;
	}

	/**
	 * @param errors
	 */
	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	/**
	 * @param requestLogic
	 */
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}
	
	/** After successful activation, the user is redirected to this page.
	 * @param successRedirect
	 */
	public void setSuccessRedirect(String successRedirect) {
		this.successRedirect = successRedirect;
	}

	/** Injects an instance of the MailUtils to send activation success mails.
	 * @param mailUtils
	 */
	@Required
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}

	/**
	 * Sets the authentication manager used to authenticate the user after 
	 * successful activation.
	 * 
	 * @param authenticationManager
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}