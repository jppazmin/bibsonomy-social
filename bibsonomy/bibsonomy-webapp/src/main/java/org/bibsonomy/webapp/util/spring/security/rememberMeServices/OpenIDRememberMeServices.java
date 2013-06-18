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

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.util.spring.security.UserAdapter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.openid.OpenIDConsumer;
import org.springframework.security.openid.OpenIDConsumerException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

/**
 * - saves on login success the username, openid and a signature (username, openid,  
 *   key) in a cookie
 * - it auto logins the user by redirecting the user to his open id provider
 * 
 * @author dzo
 * @version $Id: OpenIDRememberMeServices.java,v 1.8 2011-06-15 14:24:42 nosebrain Exp $
 */
public class OpenIDRememberMeServices extends AbstractRememberMeServices {
	private static final Log log = LogFactory.getLog(OpenIDRememberMeServices.class);
	
	/**
	 * must be the same that is used by the login filter
	 */
	private OpenIDConsumer consumer;
	private Map<String,String> realmMapping = Collections.emptyMap();
    private Set<String> returnToUrlParameters = Collections.emptySet();
	private String projectRoot;
	
	/**
	 * the url of the open id login filter
	 */
	private String filterUrl;
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Override
	protected void onLoginSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication successfulAuthentication) {
		if (successfulAuthentication instanceof OpenIDAuthenticationToken) {
			final OpenIDAuthenticationToken token = (OpenIDAuthenticationToken) successfulAuthentication;
			
			final Object principal = token.getPrincipal();
			
			if (principal instanceof UserDetails) {
				final UserDetails userDetails = (UserDetails) principal;
				final String username = userDetails.getUsername();
				final String openID = token.getIdentityUrl();
				
				final int tokenLifetime = this.getTokenValiditySeconds();
				final long expiryTime = this.calculateExpiryTime(tokenLifetime);
				
				final String signatureValue = this.makeTokenSignature(new String[] { Long.toString(expiryTime), username, openID});
				
				this.setCookie(new String[] {openID, username, Long.toString(expiryTime), signatureValue}, tokenLifetime, request, response);

		        if (log.isDebugEnabled()) {
		            log.debug("Added remember-me cookie for user '" + username + "', expiry: '"  + new Date(expiryTime) + "'");
		        }
			}
		}
	}

	@Override
	protected UserDetails processAutoLoginCookie(final String[] cookieTokens, final HttpServletRequest request, final HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
		if (cookieTokens.length != 4) {
            throw new InvalidCookieException("Cookie token did not contain 4 tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final long tokenExpiryTime = this.getExpiryTime(cookieTokens[2]);
       
    	/*  
    	 * if user is not logged in, redirect user to his open id provider
    	 * extract open id and user name
    	 */
    	final String username = cookieTokens[1];
    	final String claimedIdentity = cookieTokens[0];
    	
    	/*
    	 * note: AbstractRememberMeServices#autoLogin checks if the user was deleted
    	 */
    	final UserDetails userDetails = this.getUserDetailsService().loadUserByUsername(username);

    	/*
    	 * extract open ID from the database user
    	 */
    	if (!present(userDetails) || !(userDetails instanceof UserAdapter)) {
			throw new AuthenticationServiceException("User or ID could not be found in database.");
		}

		final String databaseIdentity = ((UserAdapter) userDetails).getUser().getOpenID();

    	/*
    	 * check token signature
    	 */
    	final String expectedTokenSignature = this.makeTokenSignature(new String[] { Long.toString(tokenExpiryTime), username, databaseIdentity});
    	final String signature = cookieTokens[3];
		if (!expectedTokenSignature.equals(signature)) {
            throw new InvalidCookieException("Cookie token[3] contained signature '" + signature  + "' but expected '" + expectedTokenSignature + "'");
        }
    	
		/*
		 * build the url for the open id
		 */
		final String returnToUrl = this.buildReturnToUrl(request);
        final String realm = this.lookupRealm(returnToUrl);
        String openIdUrl = null;
        try {
        	openIdUrl = this.consumer.beginConsumption(request, claimedIdentity, returnToUrl, realm);
        	if (log.isDebugEnabled()) {
        		log.debug("return_to is '" + returnToUrl + "', realm is '" + realm + "'");
        		log.debug("Redirecting to " + openIdUrl);
        	}
        	
        	/*
        	 * save request in cache
        	 */
        	this.requestCache.saveRequest(request, response);
        	
        	response.sendRedirect(openIdUrl);
        } catch (final IOException ex) {
			log.warn("could not set redirect url " + openIdUrl, ex);
		} catch (final OpenIDConsumerException e) {
        	log.debug("Failed to consume claimedIdentity: " + claimedIdentity, e);
        	throw new AuthenticationServiceException("Unable to process claimed identity '" + claimedIdentity + "'");
		}
        
        // throw an exception to redirect the user
        throw new RememberMeAuthenticationException("redirect was sent");
	}
	
	protected String buildReturnToUrl(final HttpServletRequest request) {
		final StringBuilder sb = new StringBuilder(this.projectRoot);
		sb.append(this.filterUrl.replaceFirst("\\/", "")); // TODO: document or remove?!
		final Iterator<String> iterator = returnToUrlParameters.iterator();
        boolean isFirst = true;

        while (iterator.hasNext()) {
            final String name = iterator.next();
            // Assume for simplicity that there is only one value
            final String value = request.getParameter(name);

            if (value == null) {
                continue;
            }

            if (isFirst) {
                sb.append("?");
                isFirst = false;
            }
            sb.append(name).append("=").append(value);

            if (iterator.hasNext()) {
                sb.append("&");
            }
        }

        return sb.toString();
	}

	protected String lookupRealm(final String returnToUrl) {
        String mapping = realmMapping.get(returnToUrl);

        if (mapping == null) {
            try {
                final URL url = new URL(returnToUrl);
                final int port = url.getPort();

                final StringBuilder realmBuffer = new StringBuilder(returnToUrl.length())
                        .append(url.getProtocol())
                        .append("://")
                        .append(url.getHost());
                if (port > 0) {
                    realmBuffer.append(":").append(port);
                }
                realmBuffer.append("/");
                mapping = realmBuffer.toString();
            } catch (final MalformedURLException e) {
                log.warn("returnToUrl was not a valid URL: [" + returnToUrl + "]", e);
            }
        }

        return mapping;
    }
	
	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(final OpenIDConsumer consumer) {
		this.consumer = consumer;
	}
	
	/**
	 * @param realmMapping the realmMapping to set
	 */
	public void setRealmMapping(final Map<String, String> realmMapping) {
		this.realmMapping = realmMapping;
	}

	/**
	 * @param returnToUrlParameters the returnToUrlParameters to set
	 */
	public void setReturnToUrlParameters(final Set<String> returnToUrlParameters) {
		this.returnToUrlParameters = returnToUrlParameters;
	}

	/**
	 * @param projectRoot the projectRoot to set
	 */
	public void setProjectRoot(final String projectRoot) {
		this.projectRoot = projectRoot;
	}

	/**
	 * @param filterUrl the filterUrl to set
	 */
	public void setFilterUrl(final String filterUrl) {
		this.filterUrl = filterUrl;
	}

	/**
	 * @param requestCache the requestCache to set
	 */
	public void setRequestCache(final RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
}
