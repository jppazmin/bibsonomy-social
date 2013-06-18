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

/**
 * @author dzo
 * @version $Id: CaptchaCommand.java,v 1.1 2011-07-14 14:55:56 nosebrain Exp $
 */
public interface CaptchaCommand {

	/**
	 * @return the recaptcha_challenge_field
	 */
	public String getRecaptcha_challenge_field();

	/**
	 * @param recaptchaChallengeField the recaptcha_challenge_field to set
	 */
	public void setRecaptcha_challenge_field(String recaptchaChallengeField);

	/**
	 * @return the recaptcha_response_field
	 */
	public String getRecaptcha_response_field();

	/**
	 * @param recaptchaResponseField the recaptcha_response_field to set
	 */
	public void setRecaptcha_response_field(String recaptchaResponseField);

	/**
	 * @param captchaHTML
	 */
	public void setCaptchaHTML(String captchaHTML);

	/**
	 * @return captcha html
	 */
	public String getCaptchaHTML();

}