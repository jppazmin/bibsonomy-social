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

package org.bibsonomy.util.upload;

import org.bibsonomy.model.Document;
import org.bibsonomy.model.User;

/**
 * @author  Christian Kramer
 * @version $Id: FileUploadInterface.java,v 1.3 2011-06-10 14:00:17 doerfel Exp $
 */
public interface FileUploadInterface {

	/**
	 * firefox extion
	 */
	public static final String[] firefoxImportExt = { "html", "htm" };
	
	/**
	 * pdf, ps, djv, djvu, txt extensions
	 */
	public static final String[] fileUploadExt = { "pdf", "ps", "djv", "djvu", "txt", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "ods", "odt", "odp" };
	
	/**
	 * png, jpg extensions
	 */
	public static final String[] pictureExt = { "png", "jpg", "jpeg" };
	
	/**
	 * layout defintion extension
	 */
	public static final String[] fileLayoutExt = { "layout" };
	
	/**
	 * bibtex, endnote extension
	 */
	public static final String[] bibtexEndnoteExt = {"bib", "endnote"};
	
	/**
	 * Writes the uploaded file to the disk and returns the file together
	 * with meta information in the document
	 *
	 * @return The document describing the file (including the file!).
	 * @throws Exception
	 */
	public Document writeUploadedFile() throws Exception;

	/**
	 * Stores the created file on the hard drive and returns a document object
	 * The parameter string is needed for the creation for the hashedName of the document object
	 * 
	 * @param hashedName 
	 * @param loginUser 
	 * @return the document object representation
	 * @throws Exception
	 */
	public Document writeUploadedFile(String hashedName, User loginUser) throws Exception;

}