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

package org.bibsonomy.webapp.validation;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.actions.UserRegistrationCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author rja
 * @version $Id: UserRegistrationValidator.java,v 1.9 2010-08-03 07:17:00 rja Exp $
 */
public class UserRegistrationValidator implements Validator<UserRegistrationCommand> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return UserRegistrationCommand.class.equals(clazz);
	}

	/**
	 * Validates the given userObj.
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object userObj, final Errors errors) {
		/*
		 * To ensure that the received command is not null, we throw an
		 * exception, if this assertion fails.
		 */
		Assert.notNull(userObj);
		final UserRegistrationCommand command = (UserRegistrationCommand) userObj;

		/*
		 * Check the user data. 
		 */
		final User user = command.getRegisterUser();
		Assert.notNull(user);

		/*
		 * validate user
		 */
		errors.pushNestedPath("registerUser");
		ValidationUtils.invokeValidator(new UserValidator(), user, errors);
		errors.popNestedPath();

		/*
		 * Check the validity of the supplied passwords.
		 * Both passwords must be non-empty and must match each other. 
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "registerUser.password", "error.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordCheck", "error.field.required");
		if (!errors.hasFieldErrors("registerUser.password") && 
			!errors.hasFieldErrors("passwordCheck") && 
			!command.getPasswordCheck().equals(user.getPassword())) {
			/*
			 * passwords are not empty and don't match
			 */
			errors.reject("error.field.valid.passwordCheck", "passwords don't match");
		}

		/*
		 * check that the user accepts our privacy statement
		 */
		if (!command.isAcceptPrivacy()) {
			errors.rejectValue("acceptPrivacy", "error.field.valid.acceptPrivacy");
		}
		/*
		 * check, that challenge response is given
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "recaptcha_response_field", "error.field.required");
	}

}
