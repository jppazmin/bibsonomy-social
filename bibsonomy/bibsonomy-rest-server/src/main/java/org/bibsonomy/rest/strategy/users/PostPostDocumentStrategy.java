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

package org.bibsonomy.rest.strategy.users;

import java.io.Writer;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.model.Document;
import org.bibsonomy.rest.RestServlet;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.strategy.AbstractCreateStrategy;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.util.upload.FileUploadInterface;
import org.bibsonomy.util.upload.impl.FileUploadFactory;


/**
 * Handle the request to post a document
 * 
 * @author Christian Kramer
 * @version $Id: PostPostDocumentStrategy.java,v 1.23 2011-06-16 10:55:26 nosebrain Exp $
 */
public class PostPostDocumentStrategy extends AbstractCreateStrategy {
	private final String userName;
	private final String resourceHash;
	private final List<FileItem> items;
	private final String projectHome;
	private String uri;
	
	private final FileUploadFactory fileUploadFactory;
	
	/**
	 * @param context
	 * @param userName
	 * @param resourceHash
	 */
	public PostPostDocumentStrategy(final Context context, final String userName, final String resourceHash) {
		super(context);
		this.userName = userName;
		this.resourceHash = resourceHash;
		this.items = context.getItemList();
		this.projectHome = context.getAdditionalInfos().get(RestServlet.PROJECT_HOME_KEY);
		
		this.fileUploadFactory = new FileUploadFactory();
		this.fileUploadFactory.setDocpath(context.getAdditionalInfos().get(RestServlet.DOCUMENTS_PATH_KEY));
		this.fileUploadFactory.setTempPath(false);
	}
	
	@Override
	public void canAccess() {
		if (!this.userName.equals(this.getLogic().getAuthenticatedUser().getName())) throw new AccessDeniedException();
	}

	@Override
	protected String create() {
		final FileUploadInterface up = fileUploadFactory.getFileUploadHandler(this.items, FileUploadInterface.fileUploadExt);
		
		try {
			final Document document = up.writeUploadedFile();
			
			/*
			 * add user name to document (needed by createDocument) 
			 */
			document.setUserName(this.userName);
			
			this.getLogic().createDocument(document, this.resourceHash);
			
			uri = this.projectHome + "api/users/" + this.userName + "/posts/" + this.resourceHash + "/documents/" + document.getFileName();
			
			return uri;
			
		} catch (final Exception ex) {
			throw new BadRequestOrResponseException(ex.getMessage());
		}
	}

	@Override
	protected void render(final Writer writer, final String uri) {
		this.getRenderer().serializeURI(writer, uri);
	}
	
}