/**
 *
 *  BibSonomy-Web-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.util.upload.impl;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.UnsupportedFileTypeException;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.User;
import org.bibsonomy.util.HashUtils;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.util.file.FileUtil;
import org.bibsonomy.util.upload.FileUploadInterface;

/**
 * Handles the file upload
 * 
 * @author Christian Kramer
 * @version $Id: HandleFileUpload.java,v 1.1 2010-10-07 12:44:03 nosebrain Exp $
 */
public class HandleFileUpload implements FileUploadInterface {
	private static final Log log = LogFactory.getLog(HandleFileUpload.class);
	
	/**
	 * Used to compute the file hash.
	 */
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		df.setTimeZone(TimeZone.getDefault());
	}

	private final Document document = new Document();
	private FileItem upFile;

	private final String docPath;
	private final boolean isTempPath;

	/**
	 * default constructor
	 */
	protected HandleFileUpload(final List<FileItem> items, final String[] allowedExt, final String docPath, final boolean isTempPath) {
		this.docPath = docPath;
		this.isTempPath = isTempPath;
		
		if (items.size() == 1) {
			this.upFile = items.get(0);
		} else {

			// copy items into global field map
			for (final FileItem temp : items) {
				if ("file".equals(temp.getFieldName())) {
					this.upFile = temp;
				}
			}
		}

		final String filename = this.upFile.getName();
		if (present(filename)) {
			this.document.setFileName(FilenameUtils.getName(filename));
		}
		
		// check file extensions which we accept
		if (!present(document.getFileName()) || !StringUtils.matchExtension(document.getFileName(), allowedExt)) {
			throw new UnsupportedFileTypeException(allowedExt);
		}

		// create hash over file content
		this.document.setMd5hash(HashUtils.getMD5Hash(this.upFile.get()));

		// compute random file hash
		this.document.setFileHash(StringUtils.getMD5Hash(this.upFile.getName() + Math.random() + df.format(new Date())));
	}

	/**
	 * writes an uploaded file to the disk and returns the object
	 * 
	 * @return file
	 * @throws Exception
	 */
	@Override
	public Document writeUploadedFile() throws Exception {
		final String documentPath;
		if (isTempPath) {
			documentPath = docPath + "/" + document.getFileHash();
		} else {
			documentPath = FileUtil.getFilePath(docPath, document.getFileHash());
		}
		
		document.setFile(new File(documentPath));

		try {
			this.upFile.write(document.getFile());
		} catch (final Exception ex) {
			log.error("Could not write uploaded file.", ex);
			throw ex;
		}

		return document;
	}

	@Override
	public Document writeUploadedFile(final String fileHash, final User loginUser) throws Exception {
		document.setFileHash(fileHash);
		document.setUserName(loginUser.getName());
		return this.writeUploadedFile();
	}

}