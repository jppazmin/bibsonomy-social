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

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * Command for sending password reminder emails.
 * 
 * @author daill
 * @version $Id: PasswordReminderCommand.java,v 1.5 2011-07-14 15:01:26 nosebrain Exp $
 */
public class PasswordReminderCommand extends BaseCommand implements Serializable, CaptchaCommand {

	private static final long serialVersionUID = 6971611795826344738L;

	/**
	 * The user's name.
	 */
	private String userName;
	
	/**
	 * The user's email address.
	 */
	private String userEmail;
	
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
	 * true when password reminder has been sent successfully
	 */
	private boolean success = false;
	
	//**********************************************************************
	// getter / setter
	//**********************************************************************	
	
	/**
	 * @return returns a captcha
	 */
	@Override
	public String getCaptchaHTML() {
		return this.captchaHTML;
	}
	
	/**
	 * @param captchaHTML
	 */
	@Override
	public void setCaptchaHTML(final String captchaHTML) {
		this.captchaHTML = captchaHTML;
	}
	
	/**
	 * @return captcha
	 */
	@Override
	public String getRecaptcha_challenge_field() {
		return this.recaptcha_challenge_field;
	}
	
	/**
	 * @param recaptcha_challenge_field
	 */
	@Override
	public void setRecaptcha_challenge_field(final String recaptcha_challenge_field) {
		this.recaptcha_challenge_field = recaptcha_challenge_field;
	}
	
	/**
	 * @return captcha entry
	 */
	@Override
	public String getRecaptcha_response_field() {
		return this.recaptcha_response_field;
	}
	
	/**
	 * @param recaptcha_response_field
	 */
	@Override
	public void setRecaptcha_response_field(final String recaptcha_response_field) {
		this.recaptcha_response_field = recaptcha_response_field;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return this.userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(final String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(final boolean success) {
		this.success = success;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	
}
