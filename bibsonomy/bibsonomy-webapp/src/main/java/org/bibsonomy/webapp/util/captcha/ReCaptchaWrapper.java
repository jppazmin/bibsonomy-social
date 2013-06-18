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
import java.util.Properties;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaImpl;

/** Wrapper around {@link ReCaptcha}.
 * 
 * @author rja
 * @version $Id: ReCaptchaWrapper.java,v 1.2 2010-07-14 13:20:31 nosebrain Exp $
 */
public class ReCaptchaWrapper implements Captcha {

	private final ReCaptchaImpl reCaptcha;
	
	/**
	 * Create a new instance of the CaptchaImplementation, which is a wrapper
	 * around {@link ReCaptcha}.
	 */
	public ReCaptchaWrapper() {
		this.reCaptcha = new ReCaptchaImpl();
	}
	
	@Override
	public CaptchaResponse checkAnswer(String challenge, String response, String remoteAddr) {
		return new ReCaptchaResponseWrapper(reCaptcha.checkAnswer(remoteAddr, challenge, response));
	}

	@Override
	public String createCaptchaHtml(final Locale locale) {
		final Properties props = new Properties();
		/*
		 * set language
		 */
		props.setProperty("lang", locale.getLanguage());
		return reCaptcha.createRecaptchaHtml(null, props);
	}

	/** Sets the private key used to authenticate to the reCaptcha server
	 * to check the challenge. 
	 * 
	 * @param privateKey
	 */
	public void setPrivateKey(String privateKey) {
		reCaptcha.setPrivateKey(privateKey);
	}

	/** Sets the public key for communication with the reCaptcha server.
	 * @param publicKey
	 */
	public void setPublicKey(String publicKey) {
		reCaptcha.setPublicKey(publicKey);
	}

	/** Sets the inet address of the reCaptcha Server.
	 * 
	 * @param recaptchaServer
	 */
	public void setRecaptchaServer(String recaptchaServer) {
		reCaptcha.setRecaptchaServer(recaptchaServer);
	}

	/** Set this to <code>true</code>, if you want reCaptcha to include
	 * HTML &lt;noscript&gt; tags.
	 * @param includeNoscript
	 */
	public void setIncludeNoscript(boolean includeNoscript) {
		reCaptcha.setIncludeNoscript(includeNoscript);
	}

}
