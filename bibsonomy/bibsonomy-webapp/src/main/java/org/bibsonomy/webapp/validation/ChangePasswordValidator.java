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

import org.bibsonomy.webapp.command.SettingsViewCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author cvo
 * @version $Id: ChangePasswordValidator.java,v 1.3 2010-07-13 16:03:37 nosebrain Exp $
 */
public class ChangePasswordValidator implements Validator<SettingsViewCommand> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return SettingsViewCommand.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final SettingsViewCommand command = (SettingsViewCommand) target;

		Assert.notNull(command);

		// if one of the password fields is empty or contains only whitespace
		// fail
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "error.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "error.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPasswordRetype", "error.field.required");

		// if there is no field error on newPassword or passwordCheck but they
		// don't equal fail
		if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("newPasswordRetype")) {
			if (!command.getNewPassword().equals(command.getNewPasswordRetype())) {
				errors.rejectValue("newPasswordRetype", "error.settings.password.match");
			}
		}
	}
}
