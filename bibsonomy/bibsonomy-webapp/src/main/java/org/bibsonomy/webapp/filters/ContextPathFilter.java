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

package org.bibsonomy.webapp.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Removes the context path from the request. 
 * 
 * @author rja
 * @version $Id: ContextPathFilter.java,v 1.5 2011-04-11 13:06:05 rja Exp $
 */
public class ContextPathFilter implements Filter {
	
	protected final Log log = LogFactory.getLog(ContextPathFilter.class);
	
    /**
     * Instances of this class ignore the context path of the application. I.e., 
     * calls to getRequestURL() & Co. return the URL without the context path.
     * 
     * This is necessary for our setting where we have the Tomcat running behind
     * an Apache Proxy where the application does not have a context path. 
     * 
     * @author rja
     *
     */
    protected static final class ContextPathFreeRequest extends HttpServletRequestWrapper {

		public ContextPathFreeRequest(HttpServletRequest request) {
			super(request);
		}
		
		/**
		 * Modified to return the request URL without the context path. 
		 * 
		 * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURL()
		 */
		@Override
		public StringBuffer getRequestURL() {
			return stripContextPath(super.getRequestURL(), super.getContextPath());
		}
		
		@Override
		public String getRequestURI() {
			return stripContextPath(super.getRequestURI(), super.getContextPath());
		}

		/**
		 * Always returns "".
		 * 
		 * @see javax.servlet.http.HttpServletRequestWrapper#getContextPath()
		 */
		@Override
		public String getContextPath() {
			return "";
		}
		
		/**
		 * 
		 * @param url
		 * @param contextPath
		 * @return
		 */
		protected StringBuffer stripContextPath(final StringBuffer url, final String contextPath) {
			final int indexOf = url.indexOf(contextPath);
			return url.replace(indexOf, indexOf + contextPath.length(), "");
		}
		
		protected String stripContextPath(final String url, final String contextPath) {
			final int indexOf = url.indexOf(contextPath);
			return url.substring(0, indexOf) + url.substring(indexOf + contextPath.length());
		}
    }
	
    /**
     * Can be used to log calls to the response.
     * 
     * @author rja
     *
     */
    protected static final class LoggingResponse extends HttpServletResponseWrapper {
    	protected final Log log = LogFactory.getLog(LoggingResponse.class);
    	
		public LoggingResponse(HttpServletResponse response) {
			super(response);
		}
		
		@Override
		public void addCookie(Cookie cookie) {
			log.debug("adding cookie " + cookie.getName() + ": " + cookie.getValue() + " with path " + cookie.getPath());
			super.addCookie(cookie);
		}
    }
    
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/*
		 * If we have an HTTP servlet request we wrap it into our own class to 
		 * modify certain calls whose results could contain the context path.
		 */
		if (request instanceof HttpServletRequest) {
			chain.doFilter(new ContextPathFreeRequest((HttpServletRequest) request), response);	
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
