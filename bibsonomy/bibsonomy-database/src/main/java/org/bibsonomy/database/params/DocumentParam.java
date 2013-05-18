/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.params;

import org.bibsonomy.common.enums.HashID;

/**
 * @author Christian Kramer
 * @version $Id: DocumentParam.java,v 1.4 2010-04-09 09:04:39 nosebrain Exp $
 */
public class DocumentParam extends GenericParam {

	/**
	 * holds the hash of/to the file
	 */
	private String fileHash;

	/**
	 * the name of the file
	 */
	private String fileName;

	/**
	 * the hash of the resource
	 */
	private String resourceHash;

	/**
	 * the contentId of the bibtex entry
	 */
	private int contentId;
	
	/**
	 * the md5hash of the document
	 */
	private String md5hash;
	
	/**
	 * Constructor
	 */
	public DocumentParam() {
		this.requestedSimHash = HashID.INTRA_HASH;
	}

	/**
	 * returns the md5hash which has been builded over the content
	 * 
	 * @return md5hash
	 */
	public String getMd5hash() {
		return this.md5hash;
	}

	/**
	 * set the md5hash
	 * 
	 * @param md5hash
	 */
	public void setMd5hash(String md5hash) {
		this.md5hash = md5hash;
	}

	/**
	 * defines the needed simhash which should be unique for each bibtex entry
	 */
	private HashID requestedSimHash;

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
	 * @return resourceHash
	 */
	public String getResourceHash() {
		return this.resourceHash;
	}

	/**
	 * @param resourceHash
	 */
	public void setResourceHash(String resourceHash) {
		this.resourceHash = resourceHash;
	}

	/**
	 * @return contentId
	 */
	public int getContentId() {
		return this.contentId;
	}

	/**
	 * @param contentId
	 */
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return requestedSimHash
	 */
	public int getRequestedSimHash() {
		return this.requestedSimHash.getId();
	}

	/**
	 * @param requestedSimHash
	 */
	public void setRequestedSimHash(HashID requestedSimHash) {
		this.requestedSimHash = requestedSimHash;
	}
}