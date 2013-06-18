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
import org.bibsonomy.webapp.command.actions.UserIDRegistrationCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Validator for UserOpenIDRegistrationController
 * 
 * @author Stefan Stützer
 * @version $Id: UserOpenIDRegistrationValidator.java,v 1.7 2011-02-16 12:11:47 rja Exp $
 */
public class UserOpenIDRegistrationValidator implements Validator<UserIDRegistrationCommand>{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		return UserIDRegistrationCommand.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserIDRegistrationCommand userObj = (UserIDRegistrationCommand) target;
		
		/*
		 * OpenID has to be entered in the second step
		 */
		if (userObj.getStep() == 2) {			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "registerUser.openID", "error.field.required");
		} else {
			/*
			 * username and email are required for successful registration
			 */
			/*
			 * Check the user data. 
			 */
			final User user = userObj.getRegisterUser();
			Assert.notNull(user);

			/*
			 * validate user
			 */
			errors.pushNestedPath("registerUser");
			ValidationUtils.invokeValidator(new UserValidator(), user, errors);
			errors.popNestedPath();
		}
	}	
}