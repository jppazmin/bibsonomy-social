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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.enums.HttpMethod;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Provides convenient access to the HTTP request. The request should 
 * never be accessed directly, instead, all methods should be put into
 * this class. 
 * 
 * Nevertheless, try to keep this class as small as possible and try 
 * to re-use or refactor existing methods!
 * 
 * @author rja
 * @version $Id: RequestLogic.java,v 1.21 2011-04-06 09:31:28 folke Exp $
 */
public class RequestLogic {
	private static final Log log = LogFactory.getLog(RequestLogic.class);
	
	/*
	 * HTTP header definitions
	 */
	private static final String HEADER_REFERER = "Referer";
	private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
	private static final String HEADER_ACCEPT = "accept";
	

	/**
	 * The HTTP request this object is handling.
	 */
	private HttpServletRequest request;

	/**
	 * Default constructor.
	 */
	public RequestLogic() {
		// noop
	}

	/**
	 * Constructor to give the request.
	 * @param request
	 */
	public RequestLogic(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the method of the request
	 */
	public String getMethod() {
		return request.getMethod();
	}
	
	/**
	 * @return The inet address of the requesting host. Since the system 
	 * typically runs behind a proxy, this is NOT the host address, but 
	 * rather the contents of the x-forwarded-for header. This also contains
	 * all involved proxies, separated by comma.
	 */
	public String getInetAddress() {		
		return request.getHeader(HEADER_X_FORWARDED_FOR);
	}

	/** 
	 * Extracts from the list of hosts in the x-forwarded-for header the
	 * first host.
	 * 
	 * FIXME: check for correct implementation!
	 *  
	 * Since we're typically behind a proxy, we have to strip the proxies address.
	 * TODO: Does stripping the proxy work?
	 * 
	 * 
	 * @see #getInetAddress()
	 * @return The extracted address of the host.
	 */
	public String getHostInetAddress () {
		final String inetAddress = getInetAddress();
		if (inetAddress != null) {
			final int proxyStartPos = inetAddress.indexOf(",");
			if (log.isDebugEnabled()) log.debug("inetAddress = " + inetAddress + ", proxyStartPos = " + proxyStartPos);
			if (proxyStartPos > 0) { 
				return inetAddress.substring(0, proxyStartPos);
			}
			if (log.isDebugEnabled()) log.debug("inetAddress = " + inetAddress + " (cutted)");
			return inetAddress;
		}
		return "";
	}

	/**
	 * @return The referer header of the request.
	 */
	public String getReferer() {
		return request.getHeader(HEADER_REFERER);
	}

	/**
	 * @return The accept header of the request.
	 */
	public String getAccept() {
		return request.getHeader(HEADER_ACCEPT);
	}

	/**
	 * @return The locale associated with the current request.
	 */
	public Locale getLocale() {
		return new RequestContext(request).getLocale();
	}

	/**
	 * @return The cookies contained in the request.
	 */
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	/**
	 * Invalidates the current session (i.e., closes it).
	 */
	public void invalidateSession() {
		request.getSession().invalidate();
	}

	/** Sets the request attribute <code>key</code> to <code>value</code>. 
	 * 
	 * @param key
	 * @param value
	 */
	public void setSessionAttribute(final String key, final Object value) {
		request.getSession(true).setAttribute(key, value); 
	}

	/** Gets a session attribute
	 * 
	 * @param key
	 * @return Object
	 */
	public Object getSessionAttribute(final String key) {
		return request.getSession(true).getAttribute(key);
	}

	/**
	 * Gets the HTTP session
	 * @return HTTPSession
	 */
	public HttpSession getSession() {
		return request.getSession();
	}

	/**
	 * Gets a paramter from the HTTPRequest
	 * @param parameter name of the parameter
	 * @return value of parameter
	 */
	public String getParameter(final String parameter) {
		return request.getParameter(parameter);
	}

	/**
	 * @return Parameter map
	 */
	@SuppressWarnings("rawtypes")
	public Map getParameterMap() {
		return request.getParameterMap();
	}

	/**
	 * @return request URL
	 */
	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	/**
	 * @return query string
	 */
	public String getQueryString() {
		return request.getQueryString();
	}

	/**
	 * Strips the context path from the request URL and appends the query string. 
	 * 
	 * @return The URL of the request as it has been issued by the user, i.e.,
	 * without any context path and containing the complete query string.
	 */
	public String getCompleteRequestURL() {
		try {
			final String queryString = request.getQueryString();
			final String contextPath = request.getContextPath();
			final URL url = new URL(request.getRequestURL().toString());
			final String path = url.getPath();
			final URL result;
			if (path.startsWith(contextPath)) {
				result = new URL(url.getProtocol(), url.getHost(), url.getPort(), path.replaceFirst(contextPath, "") + "?" + queryString);
			} else {
				result = new URL(url.toString() + "?" + queryString);
			}
			return result.toString();
		} catch (final MalformedURLException e) {
			// ignore silently - should never happen
		}
		return "";
	}

	/**
	 * @return The User object associated with the logged in user.
	 */
	public User getLoginUser() {
		// TODO: instead of using the RequestWrapperContext we could use the authentication provided in the request
		// but then we must set the user of the context in the minimalistic controller spring wrapper
		// FIXME: IoC break: use user object instead of accessing request
		// FIXME: use bibsonomy2 user object and check password again
		return ((RequestWrapperContext) this.request.getAttribute(RequestWrapperContext.class.getName())).getLoginUser();
	}


	/**
	 * The HTTP request the logic is working on.
	 * 
	 * @param request
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * @return the http method of the request
	 */
	public HttpMethod getHttpMethod() {
		return HttpMethod.getHttpMethod(this.request.getMethod());
	}

	/**
	 * Builds query string from parameter map
	 * TODO unused; remove?
	 * @return querystring
	 */
	public String getParametersAsQueryString() {
		final StringBuilder buf = new StringBuilder("?");
		try {
			final Enumeration<?> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				final String param = (String) paramNames.nextElement();
				buf.append(param + "=");
				final String paramValues[] = request.getParameterValues(param);
				if (paramValues.length == 1) {
					String paramValue = paramValues[0];
					if (paramValue.length() == 0) {
						buf.append("");
					} else {
						buf.append(paramValue);
					}
				} else {
					for (int i = 0; i < paramValues.length; i++ ) {
						buf.append(paramValues[i] + "%20" );
					}
				}
				buf.append("&");
			}
		} catch (final Exception ex) {
			log.warn("Could not build query string.", ex);
		}
		return buf.toString();
	}

	/**
	 * XXX: don't change the visibility of this method
	 * if you need the request add a method in this logic delegating the action to the
	 * {@link #request} attribute
	 * 
	 * @return the request
	 */
	HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 *  Extract the parts of the given request that are relevant to OAuth.
	 *  
	 * @return
	 */
	public OAuthMessage getOAuthMessage(String URL) {
		return OAuthServlet.getMessage(getRequest(), URL);
	}

}
