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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.spring.controller.MinimalisticControllerSpringWrapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Intercepts the request and creates a {@link RequestWrapperContext}. 
 * The context acts as a proxy to the request and is put into the request.
 * {@link MinimalisticControllerSpringWrapper} then can extract the 
 * context from the request and put it into the command.
 * </p>
 * <p>
 * TODO: it would be nice, if this wrapping wouldn't be neccessary.
 * </p>
 * 
 * @author rja
 * @version $Id: RequestWrapperContextHandlerInterceptor.java,v 1.4 2010-12-08 10:12:06 rja Exp $
 */
public class RequestWrapperContextHandlerInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// nothing to do
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// nothing to do
	}

	/** Puts a {@link RequestWrapperContext} as attribute into the request. 
	 * The context acts as a proxy for the request. The name of the Attribute
	 * is {@link org.bibsonomy.webapp.util.RequestWrapperContext}.
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/*
		 * create context and populate it with the request
		 */
		final RequestWrapperContext context = new RequestWrapperContext();
		context.setRequest(request);
		/*
		 * put context into request
		 */
		request.setAttribute(RequestWrapperContext.class.getName(), context);
		/*
		 * always return true - otherwise request handling would be aborted.
		 */
		return true;
	}

}
