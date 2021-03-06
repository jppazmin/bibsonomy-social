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

import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.bibsonomy.rest.client.worker.HttpWorker;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: PutWorker.java,v 1.14 2011-05-24 09:47:34 bibsonomy Exp $
 */
public final class PutWorker extends HttpWorker<PutMethod> {

	/**
	 * 
	 * @param username
	 * @param apiKey
	 */
	public PutWorker(final String username, final String apiKey) {
		super(username, apiKey);
	}

	@Override
	protected PutMethod getMethod(String url, String requestBody) {
		final PutMethod put = new PutMethod(url);
		put.setFollowRedirects(false);

		// TODO: remove deprecated method
		put.setRequestEntity(new StringRequestEntity(requestBody));
		return put;
	}
}