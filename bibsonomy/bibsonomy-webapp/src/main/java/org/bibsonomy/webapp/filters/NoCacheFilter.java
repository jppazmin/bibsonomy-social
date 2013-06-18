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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import filters.ActionValidationFilter;

/**
 * Filter sets everything in the response what could make clients
 * not cache it.
 * 
 * @author Jens Illig
 * @author rja
 * @version $Id: NoCacheFilter.java,v 1.6 2011-04-11 13:08:46 rja Exp $
 */
public class NoCacheFilter implements Filter {
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		/*
		 * ignore resource files (CSS, JPEG/PNG, JavaScript) ... 
		 */
		if (httpRequest.getServletPath().startsWith(ActionValidationFilter.STATIC_RESOURCES)) {
			filterChain.doFilter(request, response);
			return;
		} 
		
		/*
		 * FIXME: workaround for IE6 bug 
		 * http://www.somacon.com/p106.php
		 * http://www.brookes.ac.uk/mediaworkshop/brookesvirtual/faqs.html#cache
		 */
		if (request.isSecure()) {
			if (httpRequest.getRequestURI().startsWith("/documents/")) {
				/*
				 * don't modify cache header for PDF documents when SSL is enabled
				 */
				filterChain.doFilter(request, response);
				return;
			}
		}
			
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Pragma","no-cache");
		httpResponse.setHeader("Cache-Control","no-cache");
		httpResponse.setDateHeader("Expires",-1);
		httpResponse.setDateHeader("Last-Modified",0);
	    filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
