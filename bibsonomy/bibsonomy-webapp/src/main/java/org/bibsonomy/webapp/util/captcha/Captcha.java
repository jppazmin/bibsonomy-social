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

package org.bibsonomy.webapp.util.captcha;

import java.util.Locale;

/**
 * @author rja
 * @version $Id: Captcha.java,v 1.2 2008-06-19 15:08:17 rja Exp $
 */
public interface Captcha {

	/** Creates the HTML string which displays the captcha.
	 * 
	 * @param locale - to determine the language for the captcha description.
	 * @return A piece of HTML code rendering the Captcha. 
	 */
	public String createCaptchaHtml(final Locale locale);
	
	/** Checks the response corresponding to the challenge.
	 * 
	 * @param challenge
	 * @param response
	 * @param remoteHostInetAddress
	 * @return A response containing errors and information about the validity.
	 */
	public CaptchaResponse checkAnswer(final String challenge, final String response, final String remoteHostInetAddress);
	
}
