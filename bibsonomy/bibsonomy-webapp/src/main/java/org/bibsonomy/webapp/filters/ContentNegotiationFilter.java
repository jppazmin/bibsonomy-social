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

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.webapp.util.HeaderUtils;
import org.bibsonomy.webapp.view.Views;

/**
 * Implements content negotiation for integration of BibSonomy into the 
 * Open Linked Data Cloud. See http://www4.wiwiss.fu-berlin.de/bizer/pub/LinkedDataTutorial/
 * <br/>
 * 
 * Configure the URLs the filter should handle using the <code>excludePattern</code>
 * init parameter.
 * <br/>
 * 
 * This filter checks the "accept" header of the request to redirect to the
 * corresponding format. Only the format with highest priority is used. If the 
 * first part of the path is one of our export formats, no action is done.   
 *
 * The filter works only on the HTTP methods GET and HEAD.
 * 
 * The filter excluded some URLs based on the {@link #excludePattern}.
 *
 * TODO: should we add /uri/ to the excludePatterns list?
 * 
 * @author rja
 * @version $Id: ContentNegotiationFilter.java,v 1.4 2011-05-25 15:47:39 rja Exp $
 */
public class ContentNegotiationFilter implements Filter {

	private static final Log log = LogFactory.getLog(ContentNegotiationFilter.class);
	
	/**
	 * overwritten by init parameter "excludePatterns"
	 */
	private Pattern excludePattern = Pattern.compile("^/(api|resources|ajax)/.*");
	
	/**
	 * Mapping of MIME types to the supported export formats. 
	 * Used for (strict) content negotiation using the "Accept" header on regular pages.
	 */
	private static Map<String,String> FORMAT_MAPPING = new HashMap<String, String>();
	static {
		FORMAT_MAPPING.put("application/rdf+xml", Views.FORMAT_STRING_BURST);
		FORMAT_MAPPING.put("application/json", Views.FORMAT_STRING_JSON);
		FORMAT_MAPPING.put("text/csv", Views.FORMAT_STRING_CSV);
		FORMAT_MAPPING.put("text/x-bibtex", Views.FORMAT_STRING_BIB);
	}
	
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final String requestURI = httpRequest.getRequestURI();
		/*
		 * proceed to next filter, if method is not GET or HEAD
		 * 
		 * Note: we use strings since the filter is called often and we don't 
		 * want the overhead of org.bibsonomy.rest.enums.HttpMethod. Furthermore,
		 * we would need to catch the UnsupportedHttpMethodException.
		 * 
		 */
		final String httpMethod = httpRequest.getMethod().toUpperCase();
		if (!("GET".equals(httpMethod) || "HEAD".equals(httpMethod))) {
			log.debug("skipping " + ContentNegotiationFilter.class.getName() + " for " + requestURI + " (cause: unsupported HTTP method " + httpMethod + ")");
			chain.doFilter(request, response);
			return;
		}
		/*
		 * proceed to next filter in chain for certain patterns
		 */
		if (excludePattern.matcher(requestURI).matches()) {
			log.debug("skipping " + ContentNegotiationFilter.class.getName() + " for " + requestURI + " (cause: match in exclude pattern)");
			chain.doFilter(request, response);
			return;
		}
		final String firstPathElement = UrlUtils.getFirstPathElement(requestURI);
		/*
		 * We skip working on the request when a specific format is already 
		 * selected using the URL path or the URL's "format" parameter.
		 *
		 * This is also crucial to avoid redirect loops!
		 * 
		 * Note: we must ignore "bibtex" here (which is accepted by 
		 * Views.getViewByFormat), since as path part it is not a format.
		 * 
		 */
		if (!Views.FORMAT_STRING_BIBTEX.equals(firstPathElement)) {
			try {
				Views.getViewByFormat(firstPathElement);
				chain.doFilter(request, response);
				log.debug("skipping " + ContentNegotiationFilter.class.getName() + " for " + requestURI  + " (cause: format selected by URL path)");
				return;
			} catch (final BadRequestOrResponseException e) {
				// no known format selected - continue
			}
		}
		try {
			Views.getViewByFormat(httpRequest.getParameter("format"));
			chain.doFilter(request, response);
			log.debug("skipping " + ContentNegotiationFilter.class.getName() + " for " + requestURI  + " (cause: format selected by URL parameter)");
			return;
		} catch (final BadRequestOrResponseException e) {
			// no known format selected - continue
		}
		
		final String accept = httpRequest.getHeader("Accept");
		log.debug("working on accept header '" + accept + "'");
		log.debug("request URI: " + requestURI);
		final String responseFormat = getResponseFormat(accept);
		log.debug("format would be: " + responseFormat);
		
		if (present(responseFormat)) {
			/*
			 * send redirect
			 */
			final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setStatus(HttpServletResponse.SC_SEE_OTHER);
			httpServletResponse.sendRedirect("/" + responseFormat + HeaderUtils.getPathAndQuery(httpRequest));
			/*
			 * stop processing
			 */
			return;
		}
		chain.doFilter(httpRequest, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        final String initParameterExcludePatterns = filterConfig.getInitParameter("excludePattern");
        if (present(initParameterExcludePatterns)) {
        	this.excludePattern = Pattern.compile(initParameterExcludePatterns);
        }
	}
	
	/**
	 * Supports content negotiation using the HTTP accept header.
	 *  
	 * Extracts the preferred response format from the accept header and returns
	 * the corresponding URL path for the webapp. Only the format with the 
	 * highest priority is regarded. Thus, this method is much stricter than
	 * {@link HeaderUtils#getResponseFormat(String, int)}. 
	 * <br/> 
	 * If no matching URL path could be found, <code>null</code> is returned.
	 *   
	 * 
	 * @param acceptHeader - the "Accept" header of the HTTP request.
	 * @return The 
	 */
	private String getResponseFormat(final String acceptHeader) {
		if (!present(acceptHeader)) return null;
		/*
		 * extract ordered list of preferred types
		 */
		final SortedMap<Double, Vector<String>> preferredTypes = org.bibsonomy.rest.utils.HeaderUtils.getPreferredTypes(acceptHeader);
		/*
		 * extract highest ranked formats
		 */
		final Vector<String> firstFormats = preferredTypes.get(preferredTypes.firstKey());
		/*
		 * return best match
		 */
		return FORMAT_MAPPING.get(firstFormats.firstElement());
	}


}
