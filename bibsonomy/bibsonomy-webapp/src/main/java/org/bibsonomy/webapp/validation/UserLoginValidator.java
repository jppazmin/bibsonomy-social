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

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.webapp.command.actions.UserLoginCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author rja
 * @version $Id: UserLoginValidator.java,v 1.4 2010-07-13 16:03:37 nosebrain Exp $
 */
public class UserLoginValidator implements Validator<UserLoginCommand> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return UserLoginCommand.class.equals(clazz);
	}

	/**
	 * Validates the given loginObj.
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object loginObj, final Errors errors) {
		/*
		 * To ensure that the received command is not null, we throw an
		 * exception, if this assertion fails.
		 */
		Assert.notNull(loginObj);
		
		final UserLoginCommand cmd = (UserLoginCommand) loginObj;

		if (!present(cmd.getOpenID()) && !present(cmd.getUsername()) && !present(cmd.getPassword())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "openID", "error.field.required");
		}		
	}
}