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



import org.bibsonomy.webapp.command.actions.PasswordReminderCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author daill
 * @version $Id: PasswordReminderValidator.java,v 1.4 2010-08-03 07:17:00 rja Exp $
 */
public class PasswordReminderValidator implements Validator<PasswordReminderCommand>{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class arg0) {
		return PasswordReminderCommand.class.equals(arg0);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Assert.notNull(obj);
		
		/*
		 * user name and email must be given
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "error.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userEmail", "error.field.required");
		
		/*
		 * check, that challenge response is given
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "recaptcha_response_field", "error.field.required");
	}
}
