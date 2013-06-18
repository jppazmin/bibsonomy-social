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

import java.net.URL;
import java.util.regex.Pattern;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author rja
 * @version $Id: UserValidator.java,v 1.10 2010-12-17 13:29:32 rja Exp $
 */
public class UserValidator implements Validator<User> {

	private static final int USERNAME_MAX_LENGTH = 15;
	/**
	 * We allow only a..z A..Z 0..9 - . _ 
	 * (this covers more than 99% of all usernames before introducing this
	 * restriction)
	 */
	public static final Pattern USERNAME_DISALLOWED_CHARACTERS_PATTERN = Pattern.compile("[^a-zA-Z0-9\\.\\-_]");

	/** 
	 * Validates {@link User} and also subclasses of {@link User}. 
	 *  
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		if (clazz != null) {
			return User.class.isAssignableFrom(clazz);
		}
		return false;
	}

	/** Validates the given user object. The object must not be null.
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object userObj, final Errors errors) {

		final User user = (User) userObj;

		/*
		 * Let's check, that the given user is not null.
		 */
		Assert.notNull(user);

		/*
		 * Before we make a detailed check on correctness, we look,
		 * if required attributes are set. 
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.field.required");

		/*
		 * detailed checks
		 */
		if (!errors.hasFieldErrors("name")) validateName(user.getName(), errors); 
		if (!errors.hasFieldErrors("email")) validateEmail(user.getEmail(), errors);
		validateHomepage(user.getHomepage(), errors);
	}


	/** Validates the correctness of the email-address. This is done by 
	 * some simple tests, e.g., if the address contains whitespace, an '@'
	 * or a '.'.
	 * 
	 * @param email
	 * @param errors
	 */
	private void validateEmail (final String email, final Errors errors) {
		if (email == null ||
				"".equals(email.trim()) || 
				email.indexOf(' ') != -1 ||
				email.indexOf('@') == -1 || 
				email.length() > 255 ||
				email.lastIndexOf(".") < email.lastIndexOf("@") ||
				email.lastIndexOf("@") != email.indexOf("@") ||
				email.length() - email.lastIndexOf(".") < 2	) {
			errors.rejectValue("email","error.field.valid.user.email");
		}
	}

	/** Checks the validity of the homepage. The homepage might either be NULL 
	 * or a http (or https) address.
	 * <br/>
	 * FIXME: if the homepage is already a URL in the bean (command, model),
	 * and the user enters a non-valid URL, an exception is thrown. How can
	 * we catch this exception and instead here care for a correct URL?
	 * <br/>
	 * Not a real solution, but a workaround: 
	 *   set <code>typeMismatch.java.net.URL</code> in messages.properties
	 * <br/>  
	 * Note, that changing values inside <code>errors</code> is no possible, 
	 * hence, we can not just remove the error there.
	 * 
	 * @param homepage
	 * @param errors
	 */
	private void validateHomepage(final URL homepage, final Errors errors) {
		if (homepage != null) {
			if (!("http".equals(homepage.getProtocol()) || "https".equals(homepage.getProtocol()))) {
				errors.rejectValue("homepage", "error.field.valid.user.homepage");
			}
		}
	}

	/** Checks the validity of the user name. There are some user names which are 
	 * reserved (public, private, friends, null) and some characters which are not
	 * allowed (whitespace, -, +, /, &, ?, ", \, >, <, %).
	 * 
	 * @param name
	 * @param errors
	 */
	private void validateName (final String name, final Errors errors) {
		if (name == null ||
				"".equals(name) ||
				"public".equals(name) ||
				"private".equals(name) ||
				"friends".equals(name) ||
				"null".equals(name) ||
				name.length() > USERNAME_MAX_LENGTH ||
				USERNAME_DISALLOWED_CHARACTERS_PATTERN.matcher(name).find())
		{
			errors.rejectValue("name","error.field.valid.user.name");
		}
	}

}
