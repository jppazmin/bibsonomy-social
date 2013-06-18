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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.actions.PasswordChangeOnRemindCommand;
import org.bibsonomy.webapp.config.AuthConfig;
import org.bibsonomy.webapp.config.AuthMethod;
import org.bibsonomy.webapp.exceptions.InvalidPasswordReminderException;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.validation.PasswordChangeOnRemindValidator;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.validation.Errors;

/**
 * This controller is responsible for setting a new password after a password reminder
 * email has been sent. 
 * 
 * @author daill
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: PasswordChangeOnRemindController.java,v 1.14 2011-07-14 14:02:05 nosebrain Exp $
 */
public class PasswordChangeOnRemindController implements ErrorAware, ValidationAwareController<PasswordChangeOnRemindCommand>, RequestAware {
	private static final Log log = LogFactory.getLog(PasswordChangeOnRemindController.class);

	private LogicInterface adminLogic;
	private RequestLogic requestLogic;

	private String cryptKey;
	private Errors errors;
		
	private AuthConfig authConfig;
	
	private int maxMinutesPasswordReminderValid = 60;

	@Override
	public View workOn(final PasswordChangeOnRemindCommand command) {
		log.debug("starting work");
		
		/*
		 * check if internal authentication is supported
		 */
		if (!authConfig.containsAuthMethod(AuthMethod.INTERNAL) )  {
			errors.reject("error.method_not_allowed");
			log.warn("authmethod " + AuthMethod.INTERNAL + " missing in config");
			return Views.ERROR;			
		}
		
		/*
		 * no reminder hash given -> return input form
		 */
		final String reminderHash = command.getReminderHash();
		if (!present(reminderHash)) {
			return Views.PASSWORD_CHANGE_ON_REMIND;
		}

		/*
		 * extract the reminder credentials form the reminder hash, fetch 
		 * corresponding details from DB 
		 */
		final ReminderCredentials cred;
		try {
			cred = this.decryptReminderHash(reminderHash);
		} catch (final InvalidPasswordReminderException ex) {
			errors.reject("error.method_not_allowed");
			log.warn("could not decrypt reminder hash " + reminderHash);
			return Views.ERROR;
		}
		
		if (!present(cred.username) || !present(cred.reminderPassword)) {
			errors.reject("error.method_not_allowed");
			log.warn("either username " + cred.username + ") or reminderPassword (" + cred.reminderPassword + ") not present");
			return Views.ERROR;
		}
		
		final User user = adminLogic.getUserDetails(cred.username);
				
		/*
		 * check if the reminderPassword is correct
		 */		
		if (!present(user.getReminderPassword()) || !user.getReminderPassword().equals(cred.reminderPassword) ) {
			errors.reject("error.reminder_password_not_correct");
			return Views.ERROR;
		}
		
		/*
		 * check if the reminderPassword has expired
		 */
		if (this.hasExpired(user.getReminderPasswordRequestDate())) {
			errors.reject("error.reminder_password_expired");
			return Views.ERROR;			
		}


		/*
		 * set user name into command to show it in form field
		 */
		command.setUserName(cred.username);
		
		/*
		 * if there are any errors show them
		 */
		if (errors.hasErrors()) {
			return Views.PASSWORD_CHANGE_ON_REMIND;
		}

		/*
		 * set new password, reset old one 
		 */
		final String hashedPassword = StringUtils.getMD5Hash(command.getNewPassword());
		user.setPassword(hashedPassword);
		user.setReminderPassword("");

		log.debug("writing the new password to the database");
		/*
		 * update user in database
		 */
		adminLogic.updateUser(user, UserUpdateOperation.UPDATE_PASSWORD);

		// destroy session
		requestLogic.invalidateSession();

		log.debug("redirect to login page");
		// redirect to home
		return new ExtendedRedirectView("/login?notice=login.notice.password_changed");
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
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	@Override
	public PasswordChangeOnRemindCommand instantiateCommand() {
		return new PasswordChangeOnRemindCommand();
	}

	@Override
	public Validator<PasswordChangeOnRemindCommand> getValidator() {
		return new PasswordChangeOnRemindValidator();
	}

	@Override
	public boolean isValidationRequired(final PasswordChangeOnRemindCommand command) {
		return true;
	}

	/**
	 * @param adminLogic
	 */
	public void setAdminLogic(final LogicInterface adminLogic){
		this.adminLogic = adminLogic;
	}
		
	/**
	 * decode the reminder hash.
	 * @param reminderHash - the reminder hash
	 * @return the reminder credentials
	 */
	private ReminderCredentials decryptReminderHash(final String reminderHash) {
		try {
			final BasicTextEncryptor crypt = new BasicTextEncryptor();
			crypt.setPassword(this.getCryptKey());
			/*
			 * If the hash contained a "+" it is decoded as " " because of the URL parameter
			 * decoding. We fix this here.
			 */ 
			final String reminderHashDecrypted = crypt.decrypt(reminderHash.replaceAll(" ", "+"));
			final String[] parts = reminderHashDecrypted.split(":");
			return new ReminderCredentials(parts[0], parts[1]);
		}
		catch (final IndexOutOfBoundsException ex) {
			throw new InvalidPasswordReminderException();
		}
		catch (final EncryptionOperationNotPossibleException ex) {
			throw new InvalidPasswordReminderException();
		}
	}
	
	/**
	 * check if the password reminder date is still valid
	 * @param reminderPasswordDate - the reminder date
	 * @return true if still valid, false otherwise
	 */
	private boolean hasExpired(final Date reminderPasswordDate) {
		return ( System.currentTimeMillis() - reminderPasswordDate.getTime() > (this.maxMinutesPasswordReminderValid * 60 * 1000) );
	}
	
	/**
	 * Sets the key to decrypt the password reminder
	 * 
	 * @param cryptKey - the crypt key
	 */
	public void setCryptKey(final String cryptKey) {
		this.cryptKey = cryptKey;
	}
	
	/**
	 * get the crypt key
	 * 
	 * @return - The key to decrypt the password reminder
	 */
	public String getCryptKey() {
		return cryptKey;
	}
	
	/** The maximal number of minutes, a password reminder is valid.
	 * 
	 * @param maxMinutesPasswordReminderValid
	 */
	public void setMaxMinutesPasswordReminderValid(final int maxMinutesPasswordReminderValid) {
		this.maxMinutesPasswordReminderValid = maxMinutesPasswordReminderValid;
	}

	/**
	 * small helper class to hold username and temp-password
	 */
	private class ReminderCredentials {
		public String username;
		public String reminderPassword;
		
		public ReminderCredentials(final String username, final String reminderPassword) {
			this.username = username;
			this.reminderPassword = reminderPassword;
		}
	}


	/**
	 * @param authConfig the authConfig to set
	 */
	public void setAuthConfig(final AuthConfig authConfig) {
		this.authConfig = authConfig;
	}
	
}

