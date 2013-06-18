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
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.actions.UserIDRegistrationCommand;
import org.bibsonomy.webapp.util.CookieAware;
import org.bibsonomy.webapp.util.CookieLogic;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.bibsonomy.webapp.util.spring.security.handler.FailureHandler;
import org.bibsonomy.webapp.util.spring.security.rememberMeServices.CookieBasedRememberMeServices;
import org.bibsonomy.webapp.validation.UserValidator;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * This controller handles the registration of users via generic ID providers,
 * e.g., OpenID or LDAP. 
 * 
 * 
 * @author rja
 * @version $Id: AbstractUserIDRegistrationController.java,v 1.6 2011-07-14 13:39:10 nosebrain Exp $
 */
public abstract class AbstractUserIDRegistrationController implements ErrorAware, ValidationAwareController<UserIDRegistrationCommand>, RequestAware, CookieAware {
	private static final Log log = LogFactory.getLog(AbstractUserIDRegistrationController.class);
	
	protected LogicInterface adminLogic;
	private Errors errors = null;
	private RequestLogic requestLogic;
	private CookieLogic cookieLogic;
	private CookieBasedRememberMeServices rememberMeServices;
	private AuthenticationManager authenticationManager;
	
	/**
	 * The page with the form that will be shown to the user for registration.
	 */
	private Views registrationFormView;
	
	/**
	 * After successful registration, the user is redirected to this page.
	 */
	private String successRedirect = "";

	/**
	 * Only users which were successfully authenticated using the ID provider 
	 * and whose ID does not exist in our database are allowed to use this
	 * controller.
	 * 
	 * @see org.bibsonomy.webapp.util.MinimalisticController#workOn(org.bibsonomy.webapp.command.ContextCommand)
	 */
	@Override
	public View workOn(final UserIDRegistrationCommand command) {
		log.debug("workOn() called");

		/*
		 * If the user is already logged in: show error message
		 */
		if (command.getContext().isUserLoggedIn()) {
			throw new AccessDeniedException("error.method_not_allowed");
		}

		/*
		 * If the user has been successfully authenticated by the ID provider 
		 * and he is not yet registered, his data is contained in the session.  
		 */
		final Object o = this.requestLogic.getSessionAttribute(FailureHandler.USER_TO_BE_REGISTERED);
		if (!present(o) || !(o instanceof User)) {
			/*
			 * user must first login.
			 */
			throw new AccessDeniedNoticeException("please log in", this.getLoginNotice());
		}

		/*
		 * user found in session - proceed with the registration 
		 */
		log.debug("got user from session");
		final User user = (User) o;

		/*
		 * 2 = user has not been on form, yet -> fill it with user data from ID provider
		 * 3 = user has seen the form and possibly changed data
		 */
		if (command.getStep() == 2) {
			log.debug("step 2: start registration");
			/*
			 * fill command with user data from ID provider
			 */
			command.setRegisterUser(user);
			/*
			 * generate a new user name if necessary
			 */
			if (!present(user.getName())) user.setName(generateUserName(user));
			/*
			 * ensure that we proceed to the next step
			 */
			command.setStep(3);
			/*
			 * return to form
			 */
			return registrationFormView;
		}
		
		log.debug("step 3: complete registration");
		/* 
		 * if there are any errors in the form, we return back to fix them.
		 */
		if (errors.hasErrors()) {
			log.info("an error occoured: " + errors.toString());
			return registrationFormView;
		}
		/*
		 * no errors: try to store user
		 */
		/*
		 * user that wants to register (form data)
		 */
		final User registerUser = command.getRegisterUser();
		/*
		 * check, if user name already exists
		 */
		if (present(registerUser.getName()) && present(adminLogic.getUserDetails(registerUser.getName()).getName())) {
			/*
			 * yes -> user must choose another name
			 */
			errors.rejectValue("registerUser.name", "error.field.duplicate.user.name");
		}
		/*
		 * return to form until validation passes
		 */
		if (errors.hasErrors()) {
			/*
			 * We add the ID since it is shown to the user in the form.
			 */
			setAuthentication(registerUser, user);
			return registrationFormView;
		}
		log.info("validation passed with " + errors.getErrorCount() + " errors, proceeding to access database");
		/*
		 * set the user's inet address
		 */
		registerUser.setIPAddress(requestLogic.getInetAddress());
		/*
		 * before we store the user, we must ensure that he contains the 
		 * credentials used to authenticate him
		 */
		setAuthentication(registerUser, user);
		/*
		 * create user in DB
		 */
		adminLogic.createUser(registerUser);
		/*
		 * delete user from session.
		 */
		requestLogic.setSessionAttribute(FailureHandler.USER_TO_BE_REGISTERED, null);

		/*
		 * log user into system
		 */
		final Authentication authentication = getAuthentication(user);

		final Authentication authenticated = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authenticated);
		cookieLogic.createRememberMeCookie(rememberMeServices, authenticated);

		/*
		 * present the success view
		 */
		return new ExtendedRedirectView(successRedirect);
	}
	
	/**
	 * Before using this controller, the user must have been authenticated 
	 * using the ID provider. If the user is not authenticated, she is sent 
	 * to the login page where this notice is shown. 
	 * 
	 * @return The notice shown on the login page when the user must 
	 * authenticate before registration.
	 */
	protected abstract String getLoginNotice();
	
	/**
	 * After successful registration the user must be "logged in" using Spring
	 * Security. Therefore, we need an authentication that the corresponding 
	 * authentication manager and remememberMeService can use. This depends 
	 * very much on the type of ID used for authentication. 
	 * Typically, a new {@link UsernamePasswordAuthenticationToken} is returned. 
	 * 
	 * @param user
	 * @return
	 */
	protected abstract Authentication getAuthentication(final User user);
	
	/**
	 * Before we store the user <code>registerUser</code> in the database, his
	 * authentication credentials (e.g., ID and password) must be set. You must
	 * provide a method which does this using the data from <code>user</code> - 
	 * the user returned from the ID provider. 
	 * 
	 * @param registerUser - the user that will be stored in the database
	 * @param user - the user we got from the ID provider
	 */
	protected abstract void setAuthentication(final User registerUser, final User user);

	/**
	 * Generates a user name using the user's real name and some other heuristics.
	 * Ensures that the user name doesn't exist, yet. 
	 * 
	 * @param user - the user for that we shall generate a new user name
	 * @return A user name that does not exist, yet.
	 */
	protected String generateUserName(final User user) {
		/*
		 * Find user name which does not exist yet in the database.
		 * 
		 * check if username is already used and try another
		 */
		String newName = cleanUserName(user.getRealname());
		int tryCount = 0;
		log.debug("try existence of username: " + newName);
		while ((newName.equalsIgnoreCase(adminLogic.getUserDetails(newName).getName())) && (tryCount < 101)) {
			try {
				if (tryCount == 0) {
					// try first character of forename concatenated with surname
					// bugs bunny => bbunny
					newName = cleanUserName(user.getRealname()).substring(0, 1).concat(newName);
				} else if (tryCount == 100) {
					// now use first character of fore- and first two characters of surename concatenated with user id 
					// bugs bunny => bbu01234567
					newName = cleanUserName(newName.substring(0, 3).concat(user.getLdapId() == null ? user.getOpenID() : user.getLdapId()));
				} else {
					// try first character of forename concatenated with surename concatenated with current number
					// bugs bunny => bbunnyX where X is between 1 and 9
					if (tryCount==1) {
						// add trycount to newName
						newName = cleanUserName(newName.concat(Integer.toString(tryCount)));
					} else { 
						// replace last two characters of string with trycount
						newName = cleanUserName(newName.substring(0, newName.length() - Integer.toString(tryCount-1).length()).concat(Integer.toString(tryCount)));
					}
				}
				log.debug("try existence of username: " + newName + " (" + tryCount + ")");
				tryCount++;
			} catch (final IndexOutOfBoundsException ex) {
				/*
				 * if some substring values are out of range, catch exception and use surename
				 */
				newName = cleanUserName(user.getRealname());
				tryCount = 99;
			}
		}
		return newName;
	}
	
	private static String cleanUserName(final String name) {
		if (!present(name)) return "";
		return UserValidator.USERNAME_DISALLOWED_CHARACTERS_PATTERN.matcher(name).replaceAll("").toLowerCase();
	}

	@Override
	public UserIDRegistrationCommand instantiateCommand() {
		final UserIDRegistrationCommand command = new UserIDRegistrationCommand();
		/*
		 * add user to command
		 */
		command.setRegisterUser(new User());
		return command;
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
	public boolean isValidationRequired(final UserIDRegistrationCommand command) {
		return true;
	}

	@Override
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	@Override
	public void setCookieLogic(final CookieLogic cookieLogic) {
		this.cookieLogic = cookieLogic;
	}

	/**
	 * @param adminLogic
	 *            - an instance of the logic interface with admin access.
	 */
	@Required
	public void setAdminLogic(final LogicInterface adminLogic) {
		Assert.notNull(adminLogic, "The provided logic interface must not be null.");
		this.adminLogic = adminLogic;
		/*
		 * Check, if logic has admin access.
		 */
		Assert.isTrue(Role.ADMIN.equals(this.adminLogic.getAuthenticatedUser().getRole()), "The provided logic interface must have admin access.");
	}


	/**
	 * After successful registration, the user is redirected to this page.
	 * 
	 * @param successRedirect
	 */
	public void setSuccessRedirect(final String successRedirect) {
		this.successRedirect = successRedirect;
	}

	/**
	 * @return The remember me service.
	 */
	public CookieBasedRememberMeServices getRememberMeServices() {
		return this.rememberMeServices;
	}

	/**
	 * @param rememberMeServices
	 */
	public void setRememberMeServices(final CookieBasedRememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}
	
	/**
	 * Sets the authentication manager used to authenticate the user after 
	 * successful registration.
	 * 
	 * @param authenticationManager
	 */
	public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Sets the view used to present the user the registration form.
	 * 
	 * @param registrationFormView
	 */
	public void setRegistrationFormView(final Views registrationFormView) {
		this.registrationFormView = registrationFormView;
	}
}