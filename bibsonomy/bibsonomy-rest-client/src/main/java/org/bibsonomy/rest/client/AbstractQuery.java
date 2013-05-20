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

import java.io.File;
import java.io.Reader;

import org.apache.commons.httpclient.HttpStatus;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.client.worker.HttpWorker;
import org.bibsonomy.rest.client.worker.impl.DeleteWorker;
import org.bibsonomy.rest.client.worker.impl.GetWorker;
import org.bibsonomy.rest.client.worker.impl.HeadWorker;
import org.bibsonomy.rest.client.worker.impl.PostWorker;
import org.bibsonomy.rest.client.worker.impl.PutWorker;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.renderer.RendererFactory;
import org.bibsonomy.rest.renderer.RenderingFormat;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: AbstractQuery.java,v 1.27 2011-06-09 12:00:51 rja Exp $
 * @param <T> 
 */
public abstract class AbstractQuery<T> {
	protected static final String URL_TAGS = RestProperties.getInstance().getTagsUrl();
	protected static final String URL_USERS = RestProperties.getInstance().getUsersUrl();
	protected static final String URL_FRIENDS = RestProperties.getInstance().getFriendsUrl();
	protected static final String URL_FOLLOWERS = RestProperties.getInstance().getFollowersUrl();
	protected static final String URL_GROUPS = RestProperties.getInstance().getGroupsUrl();
	protected static final String URL_POSTS = RestProperties.getInstance().getPostsUrl();
	protected static final String URL_POSTS_ADDED = RestProperties.getInstance().getAddedPostsUrl();
	protected static final String URL_POSTS_POPULAR = RestProperties.getInstance().getPopularPostsUrl();
	protected static final String URL_CONCEPTS = RestProperties.getInstance().getConceptUrl();

	private String apiKey;
	private String username;
	private String apiURL;
	private String proxyHost;
	private int proxyPort;
	private int statusCode = -1;

	private RenderingFormat renderingFormat = RenderingFormat.XML;
	private RendererFactory rendererFactory;
	private ProgressCallback callback;

	protected Reader downloadedDocument;

	private T result;
	private boolean executed = false;

	/**
	 * @return <true> iff the query was executed
	 */
	public boolean isExecuted() {
		return this.executed;
	}

	/**
	 * @param executed the executed to set
	 */
	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	private void configHttpWorker(HttpWorker<?> worker) {
		worker.setProxyHost(this.proxyHost);
		worker.setRenderingFormat(this.renderingFormat);
		worker.setProxyPort(this.proxyPort);
	}

	protected final Reader performGetRequest(final String url) throws ErrorPerformingRequestException {
		final GetWorker worker = new GetWorker(this.username, this.apiKey, this.callback);
		this.configHttpWorker(worker);
		
		final Reader downloadedDocument = worker.perform(this.apiURL + url, null);
		this.statusCode = worker.getHttpResult();
		return downloadedDocument;
	}

	protected final Reader performMultipartPostRequest(final String url, final File file) throws ErrorPerformingRequestException {
		final PostWorker worker;
		final Reader result;
		final String absoluteUrl;
		absoluteUrl = this.apiURL + url;

		worker = new PostWorker(this.username, this.apiKey);
		this.configHttpWorker(worker);
		result = worker.perform(absoluteUrl, file);
		this.statusCode = worker.getHttpResult();

		return result;
	}

	/**
	 * Run GET worker to download a file
	 * @param url
	 * @param file
	 * @throws ErrorPerformingRequestException
	 * @author Waldemar Biller
	 */
	protected final void performFileDownload(final String url, final File file) throws ErrorPerformingRequestException {
		final GetWorker worker = new GetWorker(this.username, this.apiKey, this.callback);
		this.configHttpWorker(worker);
		
		final String absoluteUrl = this.apiURL + url;
		worker.performFileDownload(absoluteUrl, file);
		this.statusCode = worker.getHttpResult();
	}

	protected final Reader performRequest(final HttpMethod method, final String url, final String requestBody) throws ErrorPerformingRequestException {
		final HttpWorker<?> worker;
		final Reader result;
		final String absoluteUrl = this.apiURL + url;

		switch (method) {
		case POST:
			worker = new PostWorker(this.username, this.apiKey);
			this.configHttpWorker(worker);
			result = ((PostWorker) worker).perform(absoluteUrl, requestBody);
			break;
		case DELETE:
			worker = new DeleteWorker(this.username, this.apiKey);
			this.configHttpWorker(worker);
			result = ((DeleteWorker) worker).perform(absoluteUrl, null);
			break;
		case PUT:
			worker = new PutWorker(this.username, this.apiKey);
			this.configHttpWorker(worker);
			result = ((PutWorker) worker).perform(absoluteUrl, requestBody);
			break;
		case HEAD:
			worker = new HeadWorker(this.username, this.apiKey);
			this.configHttpWorker(worker);
			result = ((HeadWorker) worker).perform(absoluteUrl, null);
			break;
		case GET:
			throw new UnsupportedOperationException("use AbstractQuery::performGetRequest( String url)");
		default:
			throw new UnsupportedOperationException("unsupported operation: " + method.toString());
		}

		this.statusCode = worker.getHttpResult();
		return result;
	}

	/**
	 * Execute this query. The query blocks until a result from the server is
	 * received.
	 *
	 * @param username
	 *            username at bibsonomy.org
	 * @param apiKey
	 *            the user's password
	 * @throws ErrorPerformingRequestException
	 *             if something fails, eg an ioexception occurs (see the cause)
	 */
	final void execute(final String username, final String apiKey) throws ErrorPerformingRequestException {
		this.username = username;
		this.apiKey = apiKey;
		this.executed = true;
		this.result = this.doExecute();
	}

	/**
	 * @return result of the query
	 * @throws ErrorPerformingRequestException if something fails, eg an ioexception occurs (see the cause).
	 */
	protected abstract T doExecute() throws ErrorPerformingRequestException;

	/**
	 * @return the HTTP status code this query had (only available after
	 *         execution).
	 * @throws IllegalStateException
	 *             if query has not yet been executed.
	 */
	public final int getHttpStatusCode() throws IllegalStateException {
		if (this.statusCode == -1) throw new IllegalStateException("Execute the query first.");
		return this.statusCode;
	}

	/**
	 * @return the result of this query, if there is one.
	 * @throws BadRequestOrResponseException
	 *             if the received data is not valid.
	 * @throws IllegalStateException
	 *             if @see #getResult() gets called before 
	 *             @see Bibsonomy#executeQuery(AbstractQuery)
	 */
	public T getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (!this.executed) throw new IllegalStateException("Execute the query first.");
		return this.result;
	}

	/**
	 * @param apiURL
	 *            The apiURL to set.
	 */
	void setApiURL(final String apiURL) {
		this.apiURL = apiURL;
	}

	/**
	 * @return the {@link RenderingFormat} to use.
	 */
	protected RenderingFormat getRenderingFormat() {
		return this.renderingFormat;
	}
	
    /**
	 * @param renderingFormat
	 *            the {@link RenderingFormat} to use.
	 */
	void setRenderingFormat(final RenderingFormat renderingFormat) {
		this.renderingFormat = renderingFormat;
	}

	/**
	 * @param callback
	 *            the {@link ProgressCallback} to inform
	 */
	void setProgressCallback(final ProgressCallback callback) {
		this.callback = callback;
	}

	/**
	 * @return <code>true</code> iff the request was successful
	 */
	public boolean isSuccess() {
		return this.getHttpStatusCode() == HttpStatus.SC_OK || this.getHttpStatusCode() == HttpStatus.SC_CREATED;
	}
	
	/**
	 * @return error code iff the request was not successful
	 */
	public String getError() {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		return rendererFactory.getRenderer(this.getRenderingFormat()).parseError(this.downloadedDocument);
	}

	/**
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public RendererFactory getRendererFactory() {
		return this.rendererFactory;
	}

	public void setRendererFactory(RendererFactory rendererFactory) {
		this.rendererFactory = rendererFactory;
	}
}