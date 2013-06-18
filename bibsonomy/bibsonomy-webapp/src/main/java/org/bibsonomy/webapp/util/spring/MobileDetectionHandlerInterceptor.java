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

package org.bibsonomy.webapp.util.spring;

import static org.bibsonomy.util.ValidationUtils.present;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.wurfl.core.DeviceNotDefinedException;
import net.sourceforge.wurfl.core.WURFLManager;

import org.bibsonomy.webapp.util.MobileViewNameResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Waldemar Biller <biller@cs.uni-kassel.de>
 * @version $Id: MobileDetectionHandlerInterceptor.java,v 1.10 2011-06-09 14:01:59 rja Exp $
 */
public class MobileDetectionHandlerInterceptor implements HandlerInterceptor {

	private static final String PARAM_IS_MOBILE = "isMobile";
	private static final String MANUAL = "manual";
	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String PARAM_MOBILE = "mobile";
	
	private int cookieAge = 3600 * 24 * 365;
	
	private WURFLManager wurflManager;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

		if (	present(modelAndView) &&
				present(modelAndView.getViewName()) &&
				!isMobileDisabled(request, response) && 
				isMobileDevice(request)
		) {

			if(request.getParameterMap().containsKey(MANUAL)) {
				modelAndView.getModel().put(MANUAL, true);
			}

			this.addMobileCookie(response, TRUE);
			modelAndView.getModel().put(PARAM_IS_MOBILE, true);
			modelAndView.setViewName(MobileViewNameResolver.resolveView(modelAndView.getViewName()));
		}
	}


	private boolean isMobileDevice(HttpServletRequest request) {
		try {
			return present(wurflManager.getDeviceForRequest(request).getCapability("mobile_browser"));
		} catch (final DeviceNotDefinedException ex) {
			return false;
		}
	}


	/**
	 * checks whether mobile parameter is present or not and if the value of this is false
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean isMobileDisabled(HttpServletRequest request, HttpServletResponse response) {

		if (request.getParameterMap().containsKey(PARAM_MOBILE) ) { 
			if (FALSE.equals(request.getParameter(PARAM_MOBILE))) {
				//disable mobile site for the session
				this.addMobileCookie(response, FALSE);
				return true;				
			}
			if (TRUE.equals(request.getParameter(PARAM_MOBILE))) {
				this.addMobileCookie(response, TRUE);
				return false;
			}
		}
		// if no request param is given, we check whether the mobile view
		// is disabled via cookie
		return this.isMobileDisabledByCookie(request);
	}

	/**
	 * checks the cookies for the mobile cookie and if it's value is false
	 * @param request
	 * @return
	 */
	private boolean isMobileDisabledByCookie(HttpServletRequest request) {
		if (present(request.getCookies())) {
			for (final Cookie cookie : request.getCookies()) {
				if (PARAM_MOBILE.equals(cookie.getName()) && FALSE.equals(cookie.getValue()))
					return true;
			}
		}
		return false;
	}

	private void addMobileCookie(HttpServletResponse response, String mobile) {
		Cookie cookie = new Cookie(PARAM_MOBILE, mobile);
		cookie.setMaxAge(cookieAge);
		response.addCookie(cookie);
	}
	
	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		return true;
	}

	/**
	 * set the WURFL manager
	 * @param wurflManager
	 */
	public void setWurflManager(final WURFLManager wurflManager) {
		this.wurflManager = wurflManager;
	}

	/**
	 * get the WURFL manager
	 * @return the WURFL manager
	 */
	public WURFLManager getWurflManager() {
		return wurflManager;
	}

	/** The age (in seconds) a cookie will stay at most in the browser. Default: One year.
	 * @param cookieAge
	 */
	public void setCookieAge(int cookieAge) {
		this.cookieAge = cookieAge;
	}

}
