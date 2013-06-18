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

package org.bibsonomy.webapp.command.actions;

import java.io.Serializable;

import org.bibsonomy.webapp.command.BaseCommand;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author ice
 * @version $Id: PictureCommand.java,v 1.1 2010-08-27 12:00:19 wla Exp $
 */
public class PictureCommand extends BaseCommand implements Serializable, DownloadCommand {

	private static final long serialVersionUID = -3444057502420374593L;
	
	private String requestedUser;
	
	private String filename;
	
	private String pathToFile;
	
	private String contentType;
	
	private CommonsMultipartFile file;
	
	private boolean delete;

	/**
	 * @param RequestedUser the getRequestedUser to set
	 */
	public void setRequestedUser(String RequestedUser) {
		this.requestedUser = RequestedUser;
	}

	/**
	 * @return the getRequestedUser
	 */
	public String getRequestedUser() {
		return requestedUser;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the filename
	 */
	@Override
	public String getFilename() {
		return filename;
	}


	/**
	 * @param pathToFile the pathToFile to set
	 */
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	/**
	 * @return the pathToFile
	 */
	@Override
	public String getPathToFile() {
		return pathToFile;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the contentType
	 */
	@Override
	public String getContentType() {
		return contentType;
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
	 * @param delete the delete to set
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete() {
		return delete;
	}


	
}
