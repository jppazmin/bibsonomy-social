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

package org.bibsonomy.webapp.util;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.util.spring.security.rememberMeServices.CookieBasedRememberMeServices;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Some methods to help handling cookies.
 * 
 * @author rja
 * @version $Id: CookieLogic.java,v 1.13 2010-12-20 07:27:46 rja Exp $
 */
public class CookieLogic implements RequestAware, ResponseAware {
	private static final Log log = LogFactory.getLog(CookieLogic.class);
		
	/**
	 * Used to generate random cookies.
	 */
	private static Random GENERATOR = new Random();
	

	private RequestLogic requestLogic;
	private ResponseLogic responseLogic;
	
	/**
	 * The name of the cookie which holds the spammer cookie.
	 */
	private String cookieSpammer = "_lPost";
	
	/**
	 * The character, which the cookie only contains, if the user is a spammer.
	 */
	private char spammerCookieContains = '3';

	/**
	 * The age (in seconds) a cookie will stay at most in the browser. Default: One year.  
	 */
	private int cookieAge = 3600 * 24 * 365;
	
	/**
	 * The path on the server the cookie is valid for. Default: root path. 
	 */
	private String cookiePath = "/";
	
	/**
	 * Default Constructor 
	 */
	public CookieLogic() {
		// request and/or response logic must be set through the setters
	}
	
	/**
	 * Constructor to set request and response logic.
	 * @param requestLogic
	 * @param responseLogic
	 */
	public CookieLogic(final RequestLogic requestLogic, final ResponseLogic responseLogic) {
		this.requestLogic = requestLogic;
		this.responseLogic = responseLogic;
	}
	
	/**
	 * Checks, if a request contains a spammer cookie. 
	 * A spammer cookie always contains a "3", other cookies not.
	 * 
	 * @return <code>true</code> if cookie contained in request
	 */
	public boolean hasSpammerCookie() {
		final String cookie = getCookie(requestLogic.getCookies(), cookieSpammer); 
		return cookie != null && cookie.contains(String.valueOf(spammerCookieContains));
	}
	
	
	/** Checks, if the request contains any cookies.
	 * 
	 * @return <code>true</code>, if the request contains a cookie.
	 */
	public boolean containsCookies() {
		final Cookie[] cookies = requestLogic.getCookies();
		return cookies != null && cookies.length > 0;
	}
	
	/** 
	 * Returns the cookie with the specified name.
	 * 
	 * @param cookies 
	 * @param name
	 * @return The value of the named cookie or <code>null</code>, if the cookie could not be found.
	 */
	private static String getCookie(final Cookie[] cookies, final String name) {
		if (cookies != null) {			
			for (final Cookie cookie:cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	
	/** Adds a cookie which indicates, if a user is a spamemr. 
	 * 
	 * @param spammer - a boolean indicating wether the user is a spammer or not.
	 */
	public void addSpammerCookie(final boolean spammer) {
		/*
		 * build cookie value as first 10 characters of hashed date
		 */
		String value = StringUtils.getMD5Hash(new Date().toString()).substring(0, 10);
		/* 
		 * spammers cookies contain always a "3", others never contain a "3" (i.e. HTTP_COOKIE_SPAMMER_CONTAINS) 
		 */
		if (spammer) {
			/* A SPAMMER: make sure, that spammer value is contained */
			int pos = GENERATOR.nextInt(value.length());
			value = value.substring(0, pos) + this.spammerCookieContains + value.substring(pos + 1, value.length());
		} else {
			/* NOT A SPAMMER: replace spammer value */
			value = value.replace(this.spammerCookieContains, '0');
		}
		/*
		 * create cookie
		 */
		addCookie(this.cookieSpammer, value);
	}
	
	/**
	 * @param services the services to use
	 * @param authentication
	 */
	public void createRememberMeCookie(final CookieBasedRememberMeServices services, final Authentication authentication) {
		services.loginSuccess(this.requestLogic.getRequest(), this.responseLogic.getResponse(), authentication);
	}
	
	/**
	 * After the user has been successfully authenticated (or registered), this
	 * method should be called.
	 * 
	 * @param handler
	 * @param authentication
	 */
	public void onAuthenticationSuccess(final AuthenticationSuccessHandler handler, final Authentication authentication) {
		try {
			
			handler.onAuthenticationSuccess(this.requestLogic.getRequest(), this.responseLogic.getResponse(), authentication);
		} catch (IOException e) {
			throw new AuthenticationServiceException("Could not authenticate user " + authentication.getName(), e);
		} catch (ServletException e) {
			throw new AuthenticationServiceException("Could not authenticate user " + authentication.getName(), e);			
		}
	}
	
	/**
	 * @param services
	 * @param authentication
	 */
	public void updateRememberMeCookie(final CookieBasedRememberMeServices services, final Authentication authentication) {
		/*
		 * first check if remember me was checked by the user when he logged in
		 */
		if (this.containsCookies(services.getCookieName())) {
			this.createRememberMeCookie(services, authentication);
		}
	}	

	/** Adds a cookie to the response. Sets default values for path and maxAge. 
	 * 
	 * @param key - The key identifying this cookie.
	 * @param value - The value of the cookie.
	 */
	private void addCookie(final String key, final String value) {
		log.debug("Adding cookie " + key + ": " + value);
		final Cookie cookie = new Cookie(key, value);
		cookie.setPath(cookiePath);
		cookie.setMaxAge(cookieAge);
		responseLogic.addCookie(cookie);
	}
	
	private boolean containsCookies(final String name) {
		return getCookie(this.requestLogic.getCookies(), name) != null;
	}
		
	/**
	 * The logic to access the HTTP request. Neccessary for getting cookies.
	 *
	 * @param requestLogic
	 */
	@Override
	public void setRequestLogic(RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}
	
	/**
	 * The logic to access the HTTP response. Neccessary for setting cookies.
	 * @param responseLogic
	 */
	@Override
	public void setResponseLogic(ResponseLogic responseLogic) {
		this.responseLogic = responseLogic;
	}

	/** The name of the cookie which holds the spammer cookie.
	 * @param cookieSpammer
	 */
	public void setCookieSpammer(final String cookieSpammer) {
		this.cookieSpammer = cookieSpammer;
	}

	/** The age (in seconds) a cookie will stay at most in the browser. Default: One year.
	 * @param cookieAge
	 */
	public void setCookieAge(final int cookieAge) {
		this.cookieAge = cookieAge;
	}

	/** The path on the server the cookie is valid for. Default: root path ("/").
	 * @param cookiePath
	 */
	public void setCookiePath(final String cookiePath) {
		this.cookiePath = cookiePath;
	}
	
	/**
	 * @param spammerCookieContains the spammerCookieContains to set
	 */
	public void setSpammerCookieContains(char spammerCookieContains) {
		this.spammerCookieContains = spammerCookieContains;
	}
}
