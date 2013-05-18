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

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

public class DeliciousSignPost implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -6998612190700927048L;
    
	private OAuthConsumer consumer;
	private OAuthProvider provider;
	
	public DeliciousSignPost(
		String consumerKey,
		String consumerSecret,
		String requestTokenEndpointUrl,
	    	String accessTokenEndpointUrl,
	    	String authorizationWebsiteUrl) {
		consumer = new DefaultOAuthConsumer(
			consumerKey,
			consumerSecret);
		provider = new DefaultOAuthProvider(
			requestTokenEndpointUrl,
			accessTokenEndpointUrl,
			authorizationWebsiteUrl);
	}

	public String getRequestToken(String callbackUrl) throws Exception {
		String authUrl;
		authUrl = provider.retrieveRequestToken(consumer, callbackUrl);
		return authUrl;
	}

	public void getAccessToken(String pin) throws Exception {
		provider.retrieveAccessToken(consumer, pin);
	}
//	
//	public void setTokenWithSecret(String arg0, String arg1) {
//	    consumer.setTokenWithSecret(arg0, arg1);
//	}
	
	public HttpURLConnection sign(URL url) throws Exception {
		HttpURLConnection request;
		request = (HttpURLConnection) url.openConnection();
		return sign(request);
	}
	
	public HttpURLConnection sign(HttpURLConnection request) throws Exception {
		consumer.sign(request);
		return request;
	}
}
