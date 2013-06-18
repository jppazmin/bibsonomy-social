/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.controller.actions;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.File;

import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.util.file.FileUtil;
import org.bibsonomy.webapp.command.actions.DownloadFileCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;

/**
 * controller for viewing uploaded documents
 *   - /documents/INTRAHASH/USERNAME/FILENAME
 * 
 * @author cvo
 * @version $Id: DownloadFileController.java,v 1.7 2011-07-14 13:31:12 nosebrain Exp $
 */
public class DownloadFileController implements MinimalisticController<DownloadFileCommand>, ErrorAware {

	private final static String ACTION_DELETE = "delete";

	/**
	 * logical interface to BibSonomy's core functionality
	 */
	private LogicInterface logic = null;

	/**
	 * document path
	 */
	private String docpath = null;

	/**
	 * hold current errors
	 */
	private Errors errors = null;

	@Override
	public DownloadFileCommand instantiateCommand() {
		return new DownloadFileCommand();
	}

	@Override
	public View workOn(final DownloadFileCommand command) {
		if (!command.getContext().isUserLoggedIn()) {
			throw new AccessDeniedException("please log in");
		}

		final String intrahash = command.getIntrahash();
		final String requestedUser = command.getRequestedUser();

		final Document document = logic.getDocument(requestedUser, intrahash, command.getFilename());

		if (!present(document)) {
			this.errors.reject("error.document_not_found");
			return Views.ERROR;
		}

		// TODO: is this controller responsible for this action anymore?
		if (ACTION_DELETE.equals(command.getAction())) {
			/*
			 * handle document deletion
			 */
			if (!command.getContext().isValidCkey()) {
				this.errors.reject("error.field.valid.ckey");
				return Views.ERROR;
			}
			/*
			 * delete entry in database
			 */
			this.logic.deleteDocument(document, intrahash);
			/*
			 * delete file on disk
			 */
			new File(FileUtil.getFilePath(this.docpath, document.getFileHash())).delete();
			/*
			 * return to bibtex details page
			 * TODO: use url generator!
			 */
			return new ExtendedRedirectView(("/bibtex/" + HashID.INTRA_HASH.getId() + intrahash + "/" + UrlUtils.safeURIEncode(requestedUser)));
		} 

		/*
		 * default: handle document download
		 */
		command.setPathToFile(FileUtil.getFilePath(this.docpath, document.getFileHash()));
		command.setContentType(FileUtil.getContentType(document.getFileName()));
		/*
		 * stream document to user
		 */
		return Views.DOWNLOAD_FILE;
	}

	/**
	 * 
	 * @param errors
	 */
	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	/**
	 * @param logic
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @param docpath
	 */
	public void setDocpath(final String docpath) {
		this.docpath = docpath;
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

}
