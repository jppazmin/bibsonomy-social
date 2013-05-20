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

package org.bibsonomy.rest.client.queries.post;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.File;

import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;

/**
 * @author wbi
 * @version $Id: CreatePostDocumentQuery.java,v 1.6 2011-05-24 09:38:19 bibsonomy Exp $
 */
public class CreatePostDocumentQuery extends AbstractQuery<String> {

	private final File file;
	private final String username;
	private final String resourceHash;

	public CreatePostDocumentQuery(final String username, final String resourceHash, final File file) {
		if (!present(username)) throw new IllegalArgumentException("no username given");
		if (!present(resourceHash)) throw new IllegalArgumentException("no resourceHash given");

		this.username = username;
		this.resourceHash = resourceHash;

		this.file = file;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		this.downloadedDocument = this.performMultipartPostRequest(URL_USERS + "/" + this.username + "/posts/" + this.resourceHash + "/documents", this.file);
		return null;
	}

}
