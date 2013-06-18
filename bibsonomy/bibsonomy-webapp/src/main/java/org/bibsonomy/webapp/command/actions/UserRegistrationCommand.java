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

package org.bibsonomy.webapp.command.actions;

import java.io.Serializable;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.BaseCommand;

/** 
 * This command encapsulates the user and other details for the registration page. 
 * 
 * @author rja
 * @version $Id: UserRegistrationCommand.java,v 1.7 2011-07-14 15:01:26 nosebrain Exp $
 */
public class UserRegistrationCommand extends BaseCommand implements Serializable, CaptchaCommand {
	private static final long serialVersionUID = 1371638749968299277L;
	
	
	/**
	 * Holds the details of the user which wants to register (like name, email, password)
	 */
	private User registerUser;
	/**
	 * The user has to re-type the password, to ensure, that he did not make any typos.
	 */
	private String passwordCheck;
	/**
	 * Contains the HTML-Code to view the reCaptcha. Is filled ONLY by the controller!
	 * Any validator must check, that the user did not fill this field.
	 */
	private String captchaHTML;
	/**
	 * The (encoded) challenge the user has to solve. Is given as a request parameter by 
	 * the reCaptcha form.
	 */
	private String recaptcha_challenge_field;
	/**
	 * The response to the captcha, the user entered.
	 */
	private String recaptcha_response_field;
	/**
	 * User accepts our privacy statement and AGB.
	 */
	private boolean acceptPrivacy;

	/** 
	 * @return The user which tries to register.
	 */
	public User getRegisterUser() {
		return this.registerUser;
	}

	/**
	 * @param registerUser - the user which tries to register.
	 */
	public void setRegisterUser(final User registerUser) {
		this.registerUser = registerUser;
	}
	
	/**
	 * @return the captchaHTML
	 */
	@Override
	public String getCaptchaHTML() {
		return this.captchaHTML;
	}

	/**
	 * @param captchaHTML the captchaHTML to set
	 */
	@Override
	public void setCaptchaHTML(final String captchaHTML) {
		this.captchaHTML = captchaHTML;
	}

	/**
	 * @return the recaptcha_challenge_field
	 */
	@Override
	public String getRecaptcha_challenge_field() {
		return this.recaptcha_challenge_field;
	}

	/**
	 * @param recaptchaChallengeField the recaptcha_challenge_field to set
	 */
	@Override
	public void setRecaptcha_challenge_field(final String recaptchaChallengeField) {
		this.recaptcha_challenge_field = recaptchaChallengeField;
	}

	/**
	 * @return the recaptcha_response_field
	 */
	@Override
	public String getRecaptcha_response_field() {
		return this.recaptcha_response_field;
	}

	/**
	 * @param recaptchaResponseField the recaptcha_response_field to set
	 */
	@Override
	public void setRecaptcha_response_field(final String recaptchaResponseField) {
		this.recaptcha_response_field = recaptchaResponseField;
	}

	/**
	 * @return the passwordCheck
	 */
	public String getPasswordCheck() {
		return this.passwordCheck;
	}

	/**
	 * @param passwordCheck the passwordCheck to set
	 */
	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}
	
	/**
	 * @return the acceptPrivacy
	 */
	public boolean isAcceptPrivacy() {
		return this.acceptPrivacy;
	}

	/**
	 * @param acceptPrivacy the acceptPrivacy to set
	 */
	public void setAcceptPrivacy(final boolean acceptPrivacy) {
		this.acceptPrivacy = acceptPrivacy;
	}

}
