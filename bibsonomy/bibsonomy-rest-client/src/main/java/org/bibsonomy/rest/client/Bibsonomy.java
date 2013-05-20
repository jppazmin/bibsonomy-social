/**
 *
 *  BibSonomy-Rest-Client - The REST-client.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest.client;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.client.queries.get.GetPostsQuery;
import org.bibsonomy.rest.renderer.RendererFactory;
import org.bibsonomy.rest.renderer.RenderingFormat;
import org.bibsonomy.rest.renderer.UrlRenderer;

/*
 * FIXME: naming this class "Bibsonomy" is bad (large "S" is missing anyway ;-) - 
 * BibSonomy should never be found in the source code (package names are no problem,
 * of course).
 */

/**
 * This is a class for accessing the <a
 * href="http://www.bibsonomy.org/api/">Bibsonomy REST API</a>.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: Bibsonomy.java,v 1.15 2011-06-09 12:00:51 rja Exp $
 */
public final class Bibsonomy {

	private String username;
	private String apiKey;
	private String proxyHost;
	private int proxyPort = 80;
	// FIXME: the apiURL is here twice: also in the rendererFactory.urlRenderer
	private String apiURL = RestProperties.getInstance().getDefaultApiUrl();
	private final UrlRenderer urlRenderer = new UrlRenderer(RestProperties.getInstance().getDefaultApiUrl());
	private RendererFactory rendererFactory = new RendererFactory(urlRenderer);
	private RenderingFormat renderingFormat = RenderingFormat.XML;

	/**
	 * Creates an object to interact with Bibsonomy. Remember to set
	 * {@link Bibsonomy#username} and {@link Bibsonomy#apiKey} via their
	 * accessor methods.
	 */
	public Bibsonomy() {
	}

	/**
	 * Creates an object to interact with Bibsonomy.
	 * 
	 * @param username
	 *            name of the user
	 * @param apiKey
	 *            apiKey of the user
	 * @throws IllegalArgumentException
	 *             if username or password is null or empty
	 */
	public Bibsonomy(final String username, final String apiKey) throws IllegalArgumentException {
		this.setUsername(username);
		this.setApiKey(apiKey);

		this.setProxyHost(System.getProperty("http.proxyHost"));
		
		final String poxyPort = System.getProperty("http.proxyPort");
		if (present(poxyPort)) {
			this.setProxyPort(Integer.parseInt(poxyPort));
		}
	}

	/**
	 * Executes the given query.
	 * 
	 * @param query
	 *            the query to execute
	 * @throws ErrorPerformingRequestException
	 *             if something fails, eg an ioexception occurs (see the cause)
	 * @throws IllegalStateException
	 *             if the username or the password has not yet been set
	 */
	public void executeQuery(final AbstractQuery<?> query) throws ErrorPerformingRequestException, IllegalStateException {
		if (!present(this.username)) throw new IllegalStateException("The username has not yet been set.");
		if (!present(this.apiKey)) throw new IllegalStateException("The password has not yet been set.");
		query.setRenderingFormat(this.renderingFormat);
		query.setApiURL(this.apiURL);
		query.setRendererFactory(this.rendererFactory);
		query.setProxyHost(this.proxyHost);
		query.setProxyPort(this.proxyPort);
		query.execute(this.username, this.apiKey);
	}

	/**
	 * Executes the given query and notifies the callback on progress. Note that
	 * the callback only gets informed if the Query is kind of a Get-Query, a
	 * {@link GetPostsQuery}, for example.
	 * 
	 * @param query
	 *            the query to execute
	 * @param callback
	 *            the callback object to inform
	 * @throws ErrorPerformingRequestException
	 *             if something fails, eg an ioexception occurs (see the cause)
	 * @throws IllegalStateException
	 *             if the username or the password has not yet been set
	 */
	public void executeQuery(final AbstractQuery<?> query, final ProgressCallback callback) throws ErrorPerformingRequestException, IllegalStateException {
		query.setProgressCallback(callback);
		executeQuery(query);
	}

	/**
	 * @param username
	 *            The username to set.
	 * @throws IllegalArgumentException
	 *             if the given username is null or empty
	 */
	public void setUsername(final String username) throws IllegalArgumentException {
		if (!present(username)) throw new IllegalArgumentException("The given username is not valid.");
		this.username = username;
	}

	/**
	 * @param apiKey
	 *            The password to set.
	 * @throws IllegalArgumentException
	 *             if the given password is null or empty
	 */
	public void setApiKey(final String apiKey) throws IllegalArgumentException {
		if (!present(apiKey)) throw new IllegalArgumentException("The given apiKey is not valid.");
		this.apiKey = apiKey;
	}

	/**
	 * This is the accessor method for the apiurl: the url pointing to the REST
	 * webservice. It defaults to <i>http://www.bibsonomy.org/api/</i>. If no
	 * trailing slash is given it is appended automatically.
	 * 
	 * @param apiURL
	 *            The apiURL to set.
	 * @throws IllegalArgumentException
	 *             if the given url is null or empty
	 */
	public void setApiURL(String apiURL) throws IllegalArgumentException {
		if (!present(apiURL)) throw new IllegalArgumentException("The given apiURL is not valid.");
		if (apiURL.equals("/")) throw new IllegalArgumentException("The given apiURL is not valid.");
		if (!apiURL.endsWith("/")) apiURL += "/";
		this.apiURL = apiURL;
		this.rendererFactory = new RendererFactory(new UrlRenderer(apiURL));
	}

	/**
	 * Sets the {@link RenderingFormat} to use. Note that currently only the
	 * {@link RenderingFormat#XML} is supported (which is set by default), so
	 * this method is only intended for future releases.<br/>
	 * 
	 * Setting the RenderingFormat to some other value than
	 * {@link RenderingFormat#XML} will cause an
	 * {@link UnsupportedOperationException} to be thrown, at the moment.
	 * 
	 * @param renderingFormat
	 *            The {@link RenderingFormat} to use.
	 * @throws UnsupportedOperationException
	 *             If renderingFormat isn't set to XML.
	 */
	public void setRenderingFormat(final RenderingFormat renderingFormat) {
		if (!renderingFormat.equals(RenderingFormat.XML)) {
			throw new UnsupportedOperationException("Currently only the xml rendering format is supported.");
		}
		this.renderingFormat = renderingFormat;
	}

	/**
	 * 
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * 
	 * @param proxyPort
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}