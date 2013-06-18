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

/**
 * Command class for the download or deleted File operation
 * @author cvo
 * @version $Id: DownloadFileCommand.java,v 1.4 2010-08-27 12:00:19 wla Exp $
 */
public class DownloadFileCommand extends BaseCommand implements Serializable, DownloadCommand {
	private static final long serialVersionUID = 5650155398969930691L;

	/**
	 * the filename of the document which should be downloaded
	 */
	private String filename = null;
	
	/**
	 * intrahash of the file
	 */
	private String intrahash = null;
	
	/**
	 * user who wants to download the file
	 */
	private String requestedUser = null;
	
	/**
	 * path to file 
	 */
	private String pathToFile = null;
	
	/**
	 * content type of the file 
	 */
	private String contentType = null;
	
	/**
	 * download / delete
	 */
	private String action = null;
	
	/**
	 * 
	 * @return action
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * 
	 * @param action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 
	 * @return content type
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}

	
	/**
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return path to the file
	 */
	@Override
	public String getPathToFile() {
		return this.pathToFile;
	}

	/**
	 * 
	 * @param pathToFile
	 */
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	/**
	 * 
	 * @return user who request the file
	 */
	public String getRequestedUser() {
		return this.requestedUser;
	}

	/**
	 * 
	 * @param requestedUser
	 */
	public void setRequestedUser(String requestedUser) {
		this.requestedUser = requestedUser;
	}

	/**
	 * 
	 * @return filename of the requested file
	 */
	@Override
	public String getFilename() {
		return this.filename;
	}

	/**
	 * 
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * 
	 * @return intrahash of the file
	 */
	public String getIntrahash() {
		return this.intrahash;
	}

	/**
	 * 
	 * @param intrahash
	 */
	public void setIntrahash(String intrahash) {
		this.intrahash = intrahash;
	}	
}
