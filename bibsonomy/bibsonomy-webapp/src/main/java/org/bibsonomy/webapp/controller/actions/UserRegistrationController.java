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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.InetAddressStatus;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.MailUtils;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.actions.UserRegistrationCommand;
import org.bibsonomy.webapp.util.CookieAware;
import org.bibsonomy.webapp.util.CookieLogic;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.captcha.Captcha;
import org.bibsonomy.webapp.util.captcha.CaptchaResponse;
import org.bibsonomy.webapp.validation.UserRegistrationValidator;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/** This controller handles the registration of users.
 * 
 * @author rja
 * @version $Id: UserRegistrationController.java,v 1.23 2011-02-28 16:25:54 doerfel Exp $
 */
public class UserRegistrationController implements ErrorAware, ValidationAwareController<UserRegistrationCommand>, RequestAware, CookieAware {

	/**
	 * After successful registration, the user is redirected to this page. 
	 */
	private String successRedirect = "/register_success";

	private static final Log log = LogFactory.getLog(UserRegistrationController.class);

	protected LogicInterface logic;
	protected LogicInterface adminLogic;
	private Errors errors = null;
	private Captcha captcha;
	private RequestLogic requestLogic;
	private CookieLogic cookieLogic;
	private MailUtils mailUtils;

	/**
	 * @param logic - an instance of the logic interface.
	 */
	public void setLogic(LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @param adminLogic - an instance of the logic interface with admin access.
	 */
	@Required
	public void setAdminLogic(LogicInterface adminLogic) {
		Assert.notNull(adminLogic, "The provided logic interface must not be null.");
		this.adminLogic = adminLogic;
		/*
		 * Check, if logic has admin access.
		 */
		Assert.isTrue(Role.ADMIN.equals(this.adminLogic.getAuthenticatedUser().getRole()), "The provided logic interface must have admin access.");
	}

	/** 
	 * Returns an instance of the command the controller handles.
	 * 
	 * @see org.bibsonomy.webapp.util.MinimalisticController#instantiateCommand()
	 */
	@Override
	public UserRegistrationCommand instantiateCommand() {
		final UserRegistrationCommand userRegistrationCommand = new UserRegistrationCommand();
		/*
		 * add user to command
		 */
		userRegistrationCommand.setRegisterUser(new User());
		return userRegistrationCommand;
	}


	/**
	 * Main method which does the registration.
	 * @see org.bibsonomy.webapp.util.MinimalisticController#workOn(org.bibsonomy.webapp.command.ContextCommand)
	 */
	@Override
	public View workOn(UserRegistrationCommand command) {
		log.debug("workOn() called");

		command.setPageTitle("registration");

		/*
		 * variables used throughout the method
		 */
		final Locale locale = requestLogic.getLocale();
		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		/* Check user role
		 * 
		 * If user is logged in and not an admin: show error message
		 */
		if (context.isUserLoggedIn() && !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("error.logged.in.user.activate");
		}
		
		
		/* Check cookies
		 * 
		 * Check, if user has cookies enabled (there should be at least a "JSESSIONID" cookie)
		 */
		if (!cookieLogic.containsCookies()) {
			errors.reject("error.cookies_required");
		}

		/*
		 * User which wants to register (form data)
		 */
		final User registerUser = command.getRegisterUser();


		/*
		 * Get the hosts IP address.
		 */
		final String inetAddress = requestLogic.getInetAddress();
		final String hostInetAddress = requestLogic.getHostInetAddress();


		/* Check spamIP
		 * 
		 * TODO: This is a candidate of functionality for a super-class "CaptchaController".
		 * 
		 * check, if IP is blocked from registration or
		 *        if user has spammer cookie set
		 * if one of the conditions is true, the user is silently blocked and has to re-enter
		 * the captcha again and again.
		 */
		if (InetAddressStatus.WRITEBLOCKED.equals(getInetAddressStatus(hostInetAddress)) || cookieLogic.hasSpammerCookie()) {
			/*
			 * Spammer found!
			 * 
			 * Must enter captcha again (and again, and again ...)
			 */
			log.warn("Host " + hostInetAddress + " with SPAMMER cookie/blocked IP tried to register as user " + registerUser.getName());
			errors.rejectValue("recaptcha_response_field", "error.field.valid.captcha");
		} else {
			/*
			 * Valid user
			 * 
			 * check captcha
			 */
			checkCaptcha(command.getRecaptcha_challenge_field(), command.getRecaptcha_response_field(), hostInetAddress);
		}

		/*
		 * If user is an admin, he must provide a valid ckey!
		 */
		final boolean adminAccess = context.isUserLoggedIn() && Role.ADMIN.equals(loginUser.getRole());
		if (adminAccess && !context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
		}

		/*
		 * check, if user name already exists
		 */
		
		if (registerUser.getName() != null) {
			final List<User> pendingUserList = logic.getUsers(null, GroupingEntity.PENDING, registerUser.getName(), null, null, null, null, null, 0, Integer.MAX_VALUE);
			if (logic.getUserDetails(registerUser.getName()).getName() != null || pendingUserList.size() > 0) {
				/*
				 * yes -> user must choose another name
				 */
				errors.rejectValue("registerUser.name", "error.field.duplicate.user.name");
			}
		}

		/*
		 * return to form until validation passes
		 */
		if (errors.hasErrors()) {
			/*
			 * Generate HTML to show captcha.
			 */
			command.setCaptchaHTML(captcha.createCaptchaHtml(locale));
			return Views.REGISTER_USER;
		}


		log.debug("validation passed with " + errors.getErrorCount() + " errors, proceeding to access database");

		/*
		 * if the user is not logged in, we need an instance of the logic interface
		 * with admin access 
		 */
		if (!context.isUserLoggedIn()) {
			this.logic = this.adminLogic;
		}


		/*
		 * at this point:
		 * - the form is filled with correct values
		 * - the captcha is correct
		 * - the user has cookies enabled
		 * - the user seems to be not a spammer
		 * - the user is an admin (with valid ckey) or not logged in
		 * - the user name does not exist in the DB
		 * - we have an instance of the LogicInterface with admin access
		 */


		/*
		 * set the full inet address of the user
		 */
		registerUser.setIPAddress(inetAddress);

		/*
		 * hash password of user before storing it into database
		 */
		registerUser.setPassword(StringUtils.getMD5Hash(registerUser.getPassword()));

		/*
		 * create user in DB - he still needs to be activated
		 */
		logic.createUser(registerUser);

		/*
		 * send registration confirmation mail
		 */
		try {
			mailUtils.sendRegistrationMail(registerUser.getName(), registerUser.getEmail(), registerUser.getActivationCode(), inetAddress, locale);
		} catch (final Exception e) {
			log.error("Could not send registration confirmation mail for user " + registerUser.getName(), e);
		}

		/*
		 * proceed to the view
		 */
		if (adminAccess) {
			/*
			 * admin created a new user -> give feedback about successful creation of user
			 */
			command.setRegisterUser(logic.getUserDetails(registerUser.getName()));
			return Views.REGISTER_USER_SUCCESS_ADMIN; 
		}

		/*
		 * ... and present him a success view
		 */
		return new ExtendedRedirectView(successRedirect);
	}

	/**
	 * Checks the captcha. If the response from the user does not match the captcha,
	 * an error is added. 
	 * 
	 * FIXME: duplictaed in {@link EditPostController} 
	 * 
	 * @param command - the command associated with this request.
	 * @param hostInetAddress - the address of the client
	 * @throws InternServerException - if checking the captcha was not possible due to 
	 * an exception. This could be caused by a non-rechable captcha-server. 
	 */
	private void checkCaptcha(final String challenge, final String response, final String hostInetAddress) throws InternServerException {
		if (org.bibsonomy.util.ValidationUtils.present(challenge) && org.bibsonomy.util.ValidationUtils.present(response)) {
			/*
			 * check captcha response
			 */
			try {
				final CaptchaResponse res = captcha.checkAnswer(challenge, response, hostInetAddress);

				if (!res.isValid()) {
					/*
					 * invalid response from user
					 */
					errors.rejectValue("recaptcha_response_field", "error.field.valid.captcha");
				} else if (res.getErrorMessage() != null) {
					/*
					 * valid response, but still an error
					 */
					log.warn("Could not validate captcha response: " + res.getErrorMessage());
				}
			} catch (final Exception e) {
				log.fatal("Could not validate captcha response.", e);
				throw new InternServerException("error.captcha");
			}
		}
	}

	@Override
	public Errors getErrors() {
		return errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		/*
		 * here: check for binding errors
		 */
		this.errors = errors;
	}

	/** Returns, if validation is required for the given command. On default,
	 * for all incoming data validation is required.
	 * 
	 * @see org.bibsonomy.webapp.util.ValidationAwareController#isValidationRequired(org.bibsonomy.webapp.command.ContextCommand)
	 */
	@Override
	public boolean isValidationRequired(final UserRegistrationCommand command) {
		return true; // TODO: When is validation really required?
	}

	/** Checks the status of the given inetAddress in the DB
	 * @param inetAddress
	 * @return
	 */
	private InetAddressStatus getInetAddressStatus(final String inetAddress) {
		// query the DB for the status of address 
		try {
			return logic.getInetAddressStatus(InetAddress.getByName(inetAddress));
		} catch (final UnknownHostException e) {
			log.info("Could not check inetAddress " + inetAddress, e);
		}
		// fallback: unknown
		return InetAddressStatus.UNKNOWN;
	}

	@Override
	public Validator<UserRegistrationCommand> getValidator() {
		return new UserRegistrationValidator();
	}

	/** Give this controller an instance of {@link Captcha}.
	 * 
	 * @param captcha
	 */
	@Required
	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}

	/** The logic needed to access the request
	 * @param requestLogic 
	 */
	@Required
	@Override
	public void setRequestLogic(RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	/** The logic needed to access the cookies.
	 * 
	 * @param cookieLogic
	 */
	@Required
	@Override
	public void setCookieLogic(CookieLogic cookieLogic) {
		this.cookieLogic = cookieLogic;
	}

	/** After successful registration, the user is redirected to this page.
	 * @param successRedirect
	 */
	public void setSuccessRedirect(String successRedirect) {
		this.successRedirect = successRedirect;
	}

	/** Injects an instance of the MailUtils to send registration success mails.
	 * @param mailUtils
	 */
	@Required
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}

}
