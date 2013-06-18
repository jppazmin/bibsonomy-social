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

import org.bibsonomy.webapp.command.actions.PasswordChangeOnRemindCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author daill
 * @version $Id: PasswordChangeOnRemindValidator.java,v 1.4 2010-07-13 16:03:37 nosebrain Exp $
 */
public class PasswordChangeOnRemindValidator implements Validator<PasswordChangeOnRemindCommand>{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class arg0) {
		return PasswordChangeOnRemindCommand.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors errors) {
		// if command is null fail
		Assert.notNull(arg0);
		
		// get the command
		final PasswordChangeOnRemindCommand command = (PasswordChangeOnRemindCommand)arg0;
		
		// if one of the password fields is empty or contains only whitespaces fail
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "error.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordCheck", "error.field.required");

		// if there is no field error on newPassword or passwordCheck but they don't equal fail
		if(!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("passwordCheck") && !command.getNewPassword().equals(command.getPasswordCheck())){
			errors.reject("error.field.valid.passwordCheck", "passwords don't match");
		}		
	}

}
