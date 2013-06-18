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

package org.bibsonomy.webapp.command.ajax;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author wla
 * @version $Id: AjaxDocumentCommand.java,v 1.1 2011-01-17 17:46:35 wla Exp $
 */
public class AjaxDocumentCommand extends AjaxCommand {

	private String intraHash;
	
	private String fileName;
	
	private String fileHash;
	
	private int fileID;
	
	private boolean temp;
	
	private CommonsMultipartFile file;

	/**
	 * @param intraHash the intraHash to set
	 */
	public void setIntraHash(String intraHash) {
		this.intraHash = intraHash;
	}

	/**
	 * @return the intraHash
	 */
	public String getIntraHash() {
		return intraHash;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileHash the fileHash to set
	 */
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	/**
	 * @return the fileHash
	 */
	public String getFileHash() {
		return fileHash;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}

	/**
	 * @return the file
	 */
	public CommonsMultipartFile getFile() {
		return file;
	}

	/**
	 * @param fileID the fileID to set
	 */
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}

	/**
	 * @return the fileID
	 */
	public int getFileID() {
		return fileID;
	}

	/**
	 * @param temp the temp to set
	 */
	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	/**
	 * @return the temp
	 */
	public boolean isTemp() {
		return temp;
	}
	
	
	
		
}
