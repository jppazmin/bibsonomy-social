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

import org.apache.commons.httpclient.methods.DeleteMethod;
import org.bibsonomy.rest.client.worker.HttpWorker;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: DeleteWorker.java,v 1.14 2011-05-24 09:47:34 bibsonomy Exp $
 */
public final class DeleteWorker extends HttpWorker<DeleteMethod> {

	/**
	 * @param username the user name
	 * @param apiKey the api key of the user
	 */
	public DeleteWorker(final String username, final String apiKey) {
		super(username, apiKey);
	}
	
	@Override
	protected DeleteMethod getMethod(final String url, String requestBody) {
		final DeleteMethod delete = new DeleteMethod(url);
		delete.setFollowRedirects(true);
		return delete;
	}
}