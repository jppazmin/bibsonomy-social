/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model;

import java.io.File;
import java.util.Date;

/**
 * This Class defines a Document
 * 
 * @author Christian Kramer
 * @version $Id: Document.java,v 1.15 2011-04-29 06:45:06 bibsonomy Exp $
 */
public class Document {
	/**
	 * the filename
	 */
	private String fileName;

	/**
	 * the username of the document
	 */
	private String userName;

	/**
	 * the hash of the file
	 */
	private String fileHash;
	
	/**
	 * md5hash over content of the file 
	 */
	private String md5hash;
	
	/**
	 * The date at which the document has been saved.
	 */
	private Date date;
	
	/**
	 * The actual file ... sometimes it's contained in the document!
	 */
	private File file;

	/**
	 * @return md5hash
	 */
	public String getMd5hash() {
		return this.md5hash;
	}

	/**
	 * @param md5hash
	 */
	public void setMd5hash(String md5hash) {
		this.md5hash = md5hash;
	}

	/**
	 * @return fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		if (userName != null) {
			this.userName = userName.toLowerCase(); 
		} else {
			this.userName = userName;
		}
	}

	/**
	 * @return fileHash
	 */
	public String getFileHash() {
		return this.fileHash;
	}

	/**
	 * @param fileHash
	 */
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return fileName;
	}
}