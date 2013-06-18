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

import java.io.File;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.layout.jabref.JabrefLayoutRenderer;
import org.bibsonomy.layout.jabref.JabrefLayoutUtils;
import org.bibsonomy.layout.jabref.LayoutPart;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.file.FileUtil;
import org.bibsonomy.util.upload.FileUploadInterface;
import org.bibsonomy.util.upload.impl.FileUploadFactory;
import org.bibsonomy.webapp.command.actions.JabRefImportCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * TODO: add a validator for the command (should check file extension, …)
 * 
 * @author cvo
 * @version $Id: JabRefImportController.java,v 1.14 2011-06-08 11:54:58 nosebrain Exp $
 */
public class JabRefImportController implements MinimalisticController<JabRefImportCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(ImportBookmarksController.class);
	
	private static final String DELETE = "delete";

	private static final String CREATE = "create";

	/**
	 * hold current errors
	 */
	private Errors errors;

	/**
	 * logic database interface
	 */
	private LogicInterface logic;


	/**
	 * the factory used to get an instance of a FileUploadHandler.
	 */
	private FileUploadFactory uploadFactory;

	/**
	 * An instance of the (new!) layout renderer. We need it here to unload
	 * custom user layouts.
	 */
	private JabrefLayoutRenderer jabrefLayoutRenderer;

	@Override
	public View workOn(final JabRefImportCommand command) {
		final RequestWrapperContext context = command.getContext();

		/*
		 * only users which are logged in might post -> send them to
		 * login page
		 */
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedException("please log in");
		}

		final User loginUser = context.getLoginUser();

		/*
		 * check credentials to fight CSRF attacks 
		 */
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
			/*
			 * FIXME: use new settings page when complete
			 */
			return Views.ERROR;
		}

		/*
		 * delete a layout
		 */
		if (DELETE.equals(command.getAction())) {
			final String hash = command.getHash();
			final String userName = loginUser.getName();
			
			log.debug("attempting to delete layout " + hash + " for user " + userName);
			
			final Document document = this.logic.getDocument(userName, hash);

			if (document != null) {
				log.debug("deleting layout " + document.getFileName() + " for user " + userName);
				
				this.logic.deleteDocument(document, null);

				new File(FileUtil.getFilePath(this.uploadFactory.getDocpath(), hash)).delete();
				/*
				 * delete layout object from exporter
				 */
				jabrefLayoutRenderer.unloadUserLayout(userName);
			} else {
				errors.reject("error.document_not_found");
			}

		} else if (CREATE.equals(command.getAction())) {
			log.debug("creating layouts for user " + loginUser.getName());
			/*
			 * .beginLAYOUT
			 */
			writeLayoutPart(loginUser, command.getFileBegin(), LayoutPart.BEGIN);
			/*
			 * .item LAYOUT
			 */
			writeLayoutPart(loginUser, command.getFileItem(), LayoutPart.ITEM);
			/*
			 * .end LAYOUT
			 */
			writeLayoutPart(loginUser, command.getFileEnd(), LayoutPart.END);
		}

		
		if (errors.hasErrors()) {
			/*
			 * FIXME: use new settings page when complete
			 */
			return Views.ERROR;
		}
		
		/*
		 * success
		 */
		return new ExtendedRedirectView("/settings?selTab=2");
	}

	/**
	 * Writes the file of the specified layout part to disk and into the 
	 * database.
	 * 
	 * @param loginUser
	 * @param fileItem
	 * @param layoutPart
	 */
	private void writeLayoutPart(final User loginUser, final CommonsMultipartFile fileItem, final LayoutPart layoutPart) {
		if (fileItem != null && fileItem.getSize() > 0) {
			log.debug("writing layout part " + layoutPart + " with file " + fileItem.getOriginalFilename());
			try {
				final String hashedName = JabrefLayoutUtils.userLayoutHash(loginUser.getName(), layoutPart);				
				
				final FileUploadInterface uploadFileHandler = this.uploadFactory.getFileUploadHandler(Collections.singletonList(fileItem.getFileItem()), FileUploadInterface.fileLayoutExt);
				/*
				 * write file to disk
				 */
				final Document uploadedFile = uploadFileHandler.writeUploadedFile(hashedName, loginUser);
				/*
				 * store row in database
				 */
				this.logic.createDocument(uploadedFile, null);
			} catch (Exception ex) {
				log.error("Could not add custom " + layoutPart + " layout." + ex.getMessage());
				throw new RuntimeException("Could not add custom " + layoutPart + " layout: " + ex.getMessage());
			}
		}
	}

	@Override
	public JabRefImportCommand instantiateCommand() {
		return new JabRefImportCommand();
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(Errors errors) {
		this.errors = errors;
	}

	/**
	 * 
	 * @param logic
	 */
	@Required
	public void setLogic(LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @param uploadFactory
	 */
	@Required
	public void setUploadFactory(FileUploadFactory uploadFactory) {
		this.uploadFactory = uploadFactory;
	}
	
	/**
	 * @param jabrefLayoutRenderer the jabrefLayoutRenderer to set
	 */
	@Required
	public void setJabrefLayoutRenderer(JabrefLayoutRenderer jabrefLayoutRenderer) {
		this.jabrefLayoutRenderer = jabrefLayoutRenderer;
	}
}
