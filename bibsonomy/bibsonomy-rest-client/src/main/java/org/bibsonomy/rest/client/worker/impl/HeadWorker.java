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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.methods.HeadMethod;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.client.worker.HttpWorker;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: HeadWorker.java,v 1.13 2011-05-24 09:47:34 bibsonomy Exp $
 */
public class HeadWorker extends HttpWorker<HeadMethod> {

	/**
	 * 
	 * @param username
	 * @param apiKey
	 */
	public HeadWorker(final String username, final String apiKey) {
		super(username, apiKey);
	}

	@Override
	protected HeadMethod getMethod(String url, String requestBody) {
		final HeadMethod head = new HeadMethod(url);
		head.setFollowRedirects(true);
		return head;
	}

	@Override
	protected Reader readResponse(HeadMethod method) throws IOException, ErrorPerformingRequestException {
		return new StringReader(method.getStatusText());
	}
}