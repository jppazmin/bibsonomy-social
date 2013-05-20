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

package org.bibsonomy.rest.client.worker.impl;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.client.worker.HttpWorker;
import org.bibsonomy.rest.utils.HeaderUtils;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: PostWorker.java,v 1.15 2011-05-24 09:47:34 bibsonomy Exp $
 */
public final class PostWorker extends HttpWorker<PostMethod> {

	public PostWorker(final String username, final String apiKey) {
		super(username, apiKey);
	}
	
	public Reader perform(final String url, final File file) throws ErrorPerformingRequestException {
		LOGGER.debug("POST Multipart: URL: " + url);

		if (this.proxyHost != null) {
			this.getHttpClient().getHostConfiguration().setProxy(this.proxyHost, this.proxyPort);
		}

		// TODO: remove deprecated method
		final MultipartPostMethod post = new MultipartPostMethod(url);

		post.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		post.addRequestHeader(HeaderUtils.HEADER_AUTHORIZATION, HeaderUtils.encodeForAuthorization(this.username, this.apiKey));
		post.addRequestHeader("Content-Type", "multipart/form-data");

		try {
			post.addParameter("file", file);

			this.getHttpClient().getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			this.httpResult = this.getHttpClient().executeMethod(post);
			LOGGER.debug("HTTP result: " + this.httpResult);
			LOGGER.debug("response:\n" + post.getResponseBodyAsString());
			LOGGER.debug("===================================================");
			return new StringReader(post.getResponseBodyAsString());
		} catch (final IOException e) {
			LOGGER.debug(e.getMessage(), e);
			throw new ErrorPerformingRequestException(e);
		} finally {
			post.releaseConnection();
		}
	}
	
	@Override
	protected PostMethod getMethod(String url, String requestBody) {
		final PostMethod post = new PostMethod(url);
		post.setFollowRedirects(false);

		post.setRequestEntity(new StringRequestEntity(requestBody));
		return post;
	}

	@Override
	protected Reader readResponse(PostMethod method) throws IOException, ErrorPerformingRequestException {
		return new StringReader(method.getResponseBodyAsString());
	}
}