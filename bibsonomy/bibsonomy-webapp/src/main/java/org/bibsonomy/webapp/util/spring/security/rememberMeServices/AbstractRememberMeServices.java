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

package org.bibsonomy.webapp.util.spring.security.rememberMeServices;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.security.core.codec.Hex;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;


/**
 * @author dzo
 * @version $Id: AbstractRememberMeServices.java,v 1.5 2010-12-09 10:33:00 rja Exp $
 */
public abstract class AbstractRememberMeServices extends org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices implements CookieBasedRememberMeServices {
	private static final String TOKEN_SIGNATURE_SEPERATOR = ":";
	
	protected String makeTokenSignature(final String[] values) {
		final StringBuilder sb = new StringBuilder();
		for (final String string : values) {
			sb.append(string);
			sb.append(TOKEN_SIGNATURE_SEPERATOR);
		}
		sb.append(this.getKey());
		
		final String data = sb.toString();
	    MessageDigest digest;
	    try {
	        digest = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	        throw new IllegalStateException("No MD5 algorithm available!");
	    }

	    return new String(Hex.encode(digest.digest(data.getBytes())));
	}

	protected long getExpiryTime(final String cookieString) {
		long tokenExpiryTime;
		try {
	        tokenExpiryTime = new Long(cookieString).longValue();
	    } catch (NumberFormatException nfe) {
	        throw new InvalidCookieException("Cookie token did not contain a valid number (contained '" + cookieString + "')");
	    }
	
	    if (isTokenExpired(tokenExpiryTime)) {
	        throw new InvalidCookieException("Cookie token has expired (expired on '"  + new Date(tokenExpiryTime) + "'; current time is '" + new Date() + "')");
	    }
		return tokenExpiryTime;
	}

	protected boolean isTokenExpired(long tokenExpiryTime) {
		return tokenExpiryTime < System.currentTimeMillis();
	}

	protected long calculateExpiryTime(final int tokenLifetime) {
		long expiryTime = System.currentTimeMillis();
	    
		// SEC-949
	    expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);
	    return expiryTime;
	}
	
	@Override
	public String getCookieName() {
		return super.getCookieName();
	}
}
