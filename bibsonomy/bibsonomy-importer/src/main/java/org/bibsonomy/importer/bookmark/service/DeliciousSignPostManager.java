/**
 *
 *  BibSonomy-Importer - Various importers for bookmarks and publications.
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

package org.bibsonomy.importer.bookmark.service;

public class DeliciousSignPostManager {
    
    private String oAuthKey;
    
    private String consumerKey;
    private String consumerSecret;
    
    private String callbackBaseUrl;
    
    private String requestTokenEndpointUrl;
    private String accessTokenEndpointUrl;
    private String authorizationWebsiteUrl;
    
    private String bundlesUrl;
    private String bookmarksUrl;

    public DeliciousSignPost createDeliciousSignPost() {
	return new DeliciousSignPost(
		consumerKey,
		consumerSecret,
		requestTokenEndpointUrl,
		accessTokenEndpointUrl,
		authorizationWebsiteUrl);
    }

	public void setoAuthKey(String oAuthKey) {
	    this.oAuthKey = oAuthKey;
	}

	public String getoAuthKey() {
	    return oAuthKey;
	}

	public void setConsumerKey(String consumerKey) {
	    this.consumerKey = consumerKey;
	}

	public String getConsumerKey() {
	    return consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) {
	    this.consumerSecret = consumerSecret;
	}

	public String getConsumerSecret() {
	    return consumerSecret;
	}

	public void setCallbackBaseUrl(String callbackBaseUrl) {
	    this.callbackBaseUrl = callbackBaseUrl;
	}

	public String getCallbackBaseUrl() {
	    return callbackBaseUrl;
	}

	public void setRequestTokenEndpointUrl(String requestTokenEndpointUrl) {
	    this.requestTokenEndpointUrl = requestTokenEndpointUrl;
	}

	public String getRequestTokenEndpointUrl() {
	    return requestTokenEndpointUrl;
	}

	public void setAccessTokenEndpointUrl(String accessTokenEndpointUrl) {
	    this.accessTokenEndpointUrl = accessTokenEndpointUrl;
	}

	public String getAccessTokenEndpointUrl() {
	    return accessTokenEndpointUrl;
	}

	public void setAuthorizationWebsiteUrl(String authorizationWebsiteUrl) {
	    this.authorizationWebsiteUrl = authorizationWebsiteUrl;
	}

	public String getAuthorizationWebsiteUrl() {
	    return authorizationWebsiteUrl;
	}

	public void setBundlesUrl(String bundlesUrl) {
	    this.bundlesUrl = bundlesUrl;
	}

	public String getBundlesUrl() {
	    return bundlesUrl;
	}

	public void setBookmarksUrl(String postsUrl) {
	    this.bookmarksUrl = postsUrl;
	}

	public String getBookmarksUrl() {
	    return bookmarksUrl;
	}
}
