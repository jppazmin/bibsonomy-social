/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
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

package org.bibsonomy.rest.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Parses the request items
 * 
 * @author Christian Kramer
 * @version $Id: MultiPartRequestParser.java,v 1.2 2010-10-07 12:45:07 nosebrain Exp $
 */
public class MultiPartRequestParser {
	// initialize the max size ~50MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 51;
	
	private List<FileItem> items;

	/**
	 * @param request
	 * @throws FileUploadException
	 */
	public MultiPartRequestParser(final HttpServletRequest request) throws FileUploadException {
		// the factory to hold the file
		final FileItemFactory factory = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(factory);

		/*
		 * need to check if the request content-type isn't null because the
		 * apache.commons.fileupload doesn't to that so the junit tests will
		 * fail with a nullpointer exception
		 */
		boolean isMultipart = false;
		if (request.getContentType() != null) {
			isMultipart = ServletFileUpload.isMultipartContent(request);
		}

		// online parse the items if the content-type is multipart
		if (isMultipart) {
			upload.setSizeMax(MAX_REQUEST_SIZE);

			// parse the items
			@SuppressWarnings("unchecked") // ServletFileUpload.parseRequest specified to return a list of FileItems
			final List<FileItem> parseRequest = upload.parseRequest(request);
			this.items = parseRequest;
		}
	}

	/**
	 * @return a list fo FileItem's
	 */
	public List<FileItem> getList() {
		return this.items;
	}
}