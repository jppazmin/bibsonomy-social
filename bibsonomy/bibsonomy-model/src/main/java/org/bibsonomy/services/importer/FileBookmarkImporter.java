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

package org.bibsonomy.services.importer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.User;

/**
 * Allows to import lists of bookmarks from a file. 
 * 
 * @author rja
 * @version $Id: FileBookmarkImporter.java,v 1.7 2011-04-29 06:45:04 bibsonomy Exp $
 */
public interface FileBookmarkImporter {

	/**
	 * initializes the file which contains the bookmarks, the current user and his group.
	 * 
	 * @param file 
	 * @param user 
	 * @param groupName 
	 * @throws IOException - if the file could not be opened/read. 
	 * 
	 */
	public void initialize(File file, User user, String groupName) throws IOException;
	
	/**
	 * Returns the bookmarks extracted from the given file (see {@link #initialize(File, User, String)}). 
	 * 
	 * @return A list of bookmark posts, extracted from the given file.
	 */
	public List<Post<Bookmark>> getPosts();
	
}
