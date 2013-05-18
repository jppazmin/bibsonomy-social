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

import java.io.IOException;
import java.util.List;

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;

/**
 * Allows to import lists of bookmarks from remote services. 
 * 
 * @author rja
 * @version $Id: RemoteServiceBookmarkImporter.java,v 1.6 2011-04-29 06:45:04 bibsonomy Exp $
 */
public interface RemoteServiceBookmarkImporter {

	/**
	 * Sets the credentials used to authenticate the user against the remote
	 * service.
	 * 
	 * @param userName 
	 * @param password - could be also an API key or the like.
	 */
	public void setCredentials(final String userName, final String password);
	
	/**
	 * Returns the bookmarks retrieved from the remote service with the given 
	 * credentials (see {@link #setCredentials(String, String)}).
	 * 
	 * @return A list of bookmark posts, queried from the service.
	 * @throws IOException - if the remote service could not be called.
	 */
	public List<Post<Bookmark>> getPosts() throws IOException;
		
}
