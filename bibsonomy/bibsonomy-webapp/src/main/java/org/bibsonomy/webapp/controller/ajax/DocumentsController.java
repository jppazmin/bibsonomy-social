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

package org.bibsonomy.webapp.controller.ajax;

import java.io.File;
import java.util.Locale;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Document;
import org.bibsonomy.util.HashUtils;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.util.file.FileUtil;
import org.bibsonomy.util.upload.FileUploadInterface;
import org.bibsonomy.webapp.command.ajax.AjaxDocumentCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.context.MessageSource;

/**
 * Handles document upload for publication posts.
 * 
 * FIXME: proper XML escaping missing
 * 
 * @author wla
 * @version $Id: DocumentsController.java,v 1.8 2011-06-16 14:06:06 nosebrain Exp $
 */
public class DocumentsController extends AjaxController implements MinimalisticController<AjaxDocumentCommand> {
	private static final Log log = LogFactory.getLog(DocumentsController.class);
	
	private static final String ALLOWED_EXTENSIONS = StringUtils.implodeStringArray(FileUploadInterface.fileUploadExt, ", ");
	
	/**
	 * Path to the documents folder
	 */
	private String docPath;
	private String tempPath;
	private MessageSource messageSource;
	
	/**
	 * max file size, currently 50mb
	 */
	private final int maxFileSizeMB = 50;
	private final long maxFileSize = maxFileSizeMB * 1024 * 1024;
	
	@Override
	public AjaxDocumentCommand instantiateCommand() {
		return new AjaxDocumentCommand();
	}

	@Override
	public View workOn(AjaxDocumentCommand command) {
		log.debug("workOn started");
		final RequestWrapperContext context = command.getContext();
		final Locale locale = requestLogic.getLocale();
		/*
		 * Check whether user is logged in 
		 */
		if (!context.isUserLoggedIn()) {
			command.setResponseString(getXmlError("error.general.login", null, command.getFileID(), null, locale));
			return Views.AJAX_XML;
		}
		
		/*
		 * check ckey
		 */
		if (!command.getContext().isValidCkey()) {
			command.setResponseString(getXmlError("error.field.valid.ckey", null, command.getFileID(), null, locale));
			return Views.AJAX_XML;
		}
		
		/*
		 * check request method, GET is delete file request, POST is upload File request
		 */
		final String method = requestLogic.getMethod();
		final String response;
		if ("GET".equals(method)) {
			/*
			 * delete file
			 */
			if (command.isTemp()) {
				response = deleteTempDocument(command);
			} else {
				response = deleteDocument(command, locale);	
			}
		} else if ("POST".equals(method)) {
			/*
			 * upload file
			 */
			response = uploadFile(command, locale);
		} else {
			return Views.ERROR;
		}
		command.setResponseString(response);
		return Views.AJAX_XML;
	}
	
	/**
	 * This method removes temporary file
	 * @param command
	 * @return status=ok
	 */
	private String deleteTempDocument(AjaxDocumentCommand command) {
		/*
		 * delete temporary saved file
		 */
		new File(tempPath + command.getFileHash().substring(32)).delete();
		return "<root><status>ok</status><fileid>" + command.getFileID() + "</fileid></root>";
	}
	
	/**
	 * This method deletes an existing  file from filesystem and database 
	 * @param command
	 * @return
	 */
	private String deleteDocument(final AjaxDocumentCommand command, final Locale locale) {
		log.debug("start deleting file");
		final String userName  = command.getContext().getLoginUser().getName();
		final String intraHash = command.getIntraHash();
		final String fileName  = command.getFileName();
		final Document document = logic.getDocument(userName, intraHash, fileName);
		
		/*
		 * check whether logged-in user is the document owner
		 */
		final String documentOwner = document.getUserName();
		if (!documentOwner.equals(userName)) {
			return getXmlError("post.bibtex.wrongUser", null, command.getFileID(), null, locale); 
		}
		
		/*
		 * delete entry in database
		 */
		logic.deleteDocument(document, intraHash);
		
		/*
		 * delete file on disk
		 */
		new File(FileUtil.getFilePath(docPath, document.getFileHash())).delete();
		final String response = messageSource.getMessage("bibtex.actions.filedeleted", new Object[] {fileName}, locale); 
		return "<root><status>deleted</status><response>" + response + "</response></root>";
	}
	
	/**
	 * This method handles file upload to a temporary directory 
	 * @param command
	 * @return
	 */
	private String uploadFile(AjaxDocumentCommand command, Locale locale) {
		log.debug("Start uploading file");

		/*
		 * the uploaded file
		 */
		final FileItem fileItem = command.getFile().getFileItem();
		final int fileID = command.getFileID();
		/*
		 * unsupported file extensions
		 */
		if (!StringUtils.matchExtension(fileItem.getName(), FileUploadInterface.fileUploadExt)) {
			return getXmlError("error.upload.failed.filetype", new Object[] {ALLOWED_EXTENSIONS}, fileID, fileItem.getName(), locale);	
		}
		
		/*
		 * wrong file size
		 */
		final long size = fileItem.getSize();
		if (size >= maxFileSize) {
			return getXmlError("error.upload.failed.size", new Object[] {maxFileSizeMB}, fileID, fileItem.getName(), locale);
		} else if (size == 0) {
			return getXmlError("error.upload.failed.size0", null, fileID, fileItem.getName(), locale);
		}
		
		final File uploadFile;
		String fileHash = FileUtil.getRandomFileHash(fileItem.getName());
		if (command.isTemp()) { // /editPublication
			uploadFile = new File(tempPath + fileHash);
		} else { // /bibtex/....
			uploadFile = new File(FileUtil.getFilePath(docPath, fileHash));
		}
		final String md5Hash = HashUtils.getMD5Hash(fileItem.get());
		try {
			fileItem.write(uploadFile);
		} catch (final Exception ex) {
			log.error("Could not write uploaded file.", ex);
			return getXmlError("error.500", null, fileID, fileItem.getName(), locale);
		}
		
		if (!command.isTemp()) {
			final Document document = new Document();
			document.setFileName(fileItem.getName());
			document.setFileHash(fileHash);
			document.setMd5hash(md5Hash);
			document.setUserName(command.getContext().getLoginUser().getName());
			
			/*
			 * add document to the data base
			 */
			logic.createDocument(document, command.getIntraHash());
			
			/*
			 * clear fileHash (randomFileName), so only md5Hash over the file content will be sent back
			 */
			fileHash ="";
		}
		
		return "<root><status>ok</status><fileid>" + fileID + "</fileid><filehash>" + md5Hash + fileHash + "</filehash><filename>" + fileItem.getName() + "</filename></root>";
	}

	/**
	 * generates AJAX_XML response string with status = error and given reason
	 * @param reason error reason
	 * @return
	 */
	private String getXmlError (final String messageCode, final Object[] arguments, int fileID, final String fileName, Locale locale) {
		final String reason = messageSource.getMessage(messageCode, arguments, locale);
		final String errorMsg = messageSource.getMessage("error.upload.failed", new Object[] {reason}, locale);
		return "<root><status>error</status><reason>" + errorMsg + "</reason><fileid>"+ fileID + "</fileid><filename>" + fileName + "</filename></root>";
	}
	
	/**
	 * @param docPath
	 *            path to the documents folder to set
	 */
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	/**
	 * @param tempPath
	 *            the tempPath to set
	 */
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
