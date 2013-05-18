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

import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.bibsonomy.util.upload.FileUploadInterface;

/**
 * @author cvo
 * @version $Id: FileUploadFactory.java,v 1.1 2010-10-07 12:44:03 nosebrain Exp $
 */
public class FileUploadFactory {

	private String docpath = null;

    private boolean tempPath = false;
	
    /**
     * TODO: improve documentation
     * 
     * @param items
     * @param allowedExt
     * @return TODO
     */
	public FileUploadInterface getFileUploadHandler(final List<FileItem> items, final String[] allowedExt) {
		return new HandleFileUpload(items, allowedExt, this.docpath, this.tempPath);
	}
	
	/**
	 * @return the docpath
	 */
	public String getDocpath() {
		return this.docpath;
	}

	/**
	 * Sets the path where the documents from fileupload should be stored.
	 *  
	 * @param docpath - path where documents shall be stored.
	 */
	public void setDocpath(String docpath) {
		this.docpath = docpath;
	}
	
	/**
	 * @return the tempPath
	 */
	public boolean getTempPath() {
		return this.tempPath;
	}

	/**
	 * 
	 * 
	 * @param tempPath - if <code>true</code>, the files will be stored 
	 * temporarily - thus another directory naming scheme is used
	 */
	public void setTempPath(boolean tempPath) {
		this.tempPath = tempPath;
	}
	
}
