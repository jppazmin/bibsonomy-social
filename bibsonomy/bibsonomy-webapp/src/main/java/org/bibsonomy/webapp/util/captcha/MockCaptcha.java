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
 * @version $Id: MockCaptcha.java,v 1.2 2010-07-14 13:20:31 nosebrain Exp $
 */
public class MockCaptcha implements Captcha {

	@Override
	public CaptchaResponse checkAnswer(String challenge, String response, String hostInetAddress) {
		return new MockCaptchaResponse();
	}

	@Override
	public String createCaptchaHtml(Locale locale) {
		return "<input type='hidden' name='recaptcha_response_field' value='foo'/>" +
			   "<strong>Captcha will be always true due to use of " + MockCaptcha.class.getName() + ".</strong>";
		
	}

}
