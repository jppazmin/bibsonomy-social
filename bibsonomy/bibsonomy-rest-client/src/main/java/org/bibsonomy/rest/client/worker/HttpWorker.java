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

package org.bibsonomy.rest.client.worker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.client.util.RestClientUtils;
import org.bibsonomy.rest.renderer.RenderingFormat;
import org.bibsonomy.rest.utils.HeaderUtils;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: HttpWorker.java,v 1.18 2011-05-24 09:47:36 bibsonomy Exp $
 * @param <M> the http method used by the http worker
 */
public abstract class HttpWorker<M extends HttpMethod> {

	protected static final Log LOGGER = LogFactory.getLog(HttpWorker.class.getName());

	private static final String USER_AGENT_VALUE = RestProperties.getInstance().getApiUserAgent();
	
	
	private final HttpClient httpClient;
	protected int httpResult;

	protected final String username;
	protected final String apiKey;
	
	protected String proxyHost;
	protected int proxyPort;
	
	private RenderingFormat renderingFormat;

	/**
	 * @param username the username
	 * @param apiKey the apikey
	 */
	public HttpWorker(final String username, final String apiKey) {
		this.username = username;
		this.apiKey = apiKey;
		
		this.httpClient = new HttpClient();
		final HttpClientParams httpClientParams = new HttpClientParams();
		final DefaultHttpMethodRetryHandler defaultHttpMethodRetryHandler = new DefaultHttpMethodRetryHandler(0, false);
		httpClientParams.setParameter(HeaderUtils.HEADER_USER_AGENT, USER_AGENT_VALUE + "_" + RestClientUtils.getRestClientVersion());
		httpClientParams.setParameter(HttpClientParams.RETRY_HANDLER, defaultHttpMethodRetryHandler);
		httpClientParams.setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, RestClientUtils.CONTENT_CHARSET);
		httpClientParams.setAuthenticationPreemptive(true);
		
		this.httpClient.setParams(httpClientParams);
	}

	/**
	 * @see #perform(String, String)
	 * 
	 * @param url the url to call
	 * @return a reader that holds the response from the server
	 * @throws ErrorPerformingRequestException
	 */
	public Reader perform(final String url) throws ErrorPerformingRequestException {
		return this.perform(url, null);
	}
	
	/**
	 * 
	 *  
	 * @param url  the url to call
	 * @param requestBody	the body to send
	 * @return a reader that holds the response from the server
	 * @throws ErrorPerformingRequestException
	 */
	public Reader perform(final String url, final String requestBody) throws ErrorPerformingRequestException {
		
		// dirty but working
		if (this.proxyHost != null){
			getHttpClient().getHostConfiguration().setProxy(this.proxyHost, this.proxyPort);
		}
		
		final M method = this.getMethod(url, requestBody);
		
		// add auth header
		method.addRequestHeader(HeaderUtils.HEADER_AUTHORIZATION, HeaderUtils.encodeForAuthorization(this.username, this.apiKey));
		method.setDoAuthentication(true);
		
		// TODO: add accept and content type header
		method.addRequestHeader("Accept", this.renderingFormat.getMimeType());
		method.addRequestHeader("Content-Type", this.renderingFormat.getMimeType());
		
		try {
			this.httpResult = getHttpClient().executeMethod(method);
			LOGGER.debug("HTTP result: " + this.httpResult);
			LOGGER.debug("response:\n" + method.getResponseBodyAsString());
			LOGGER.debug("===================================================");
			return this.readResponse(method);
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ErrorPerformingRequestException(e);
		} finally {
			method.releaseConnection();
		}
	}
	
	protected abstract M getMethod(final String url, final String requestBody);
	
	/**
	 * 
	 * @param method
	 * @return a reader that holds the response from the server
	 * @throws IOException
	 * @throws ErrorPerformingRequestException
	 */
	protected Reader readResponse(final M method) throws IOException, ErrorPerformingRequestException {
		return new StringReader(method.getResponseBodyAsString());
	}

	/**
	 * @return Returns the httpClient.
	 */
	protected HttpClient getHttpClient() {
		return this.httpClient;
	}

	/**
	 * @return Returns the httpResult.
	 */
	public int getHttpResult() {
		return this.httpResult;
	}
	
	/**
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @param proxyPort
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	/**
	 * @param renderingFormat the renderingFormat to set
	 */
	public void setRenderingFormat(RenderingFormat renderingFormat) {
		this.renderingFormat = renderingFormat;
	}
}