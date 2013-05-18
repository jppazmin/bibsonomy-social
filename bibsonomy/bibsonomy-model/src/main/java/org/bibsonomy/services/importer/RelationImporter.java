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

import org.bibsonomy.model.Tag;

/**
 * Imports relations from a remote service or file. Additional interface, classes
 * can implement together with {@link RemoteServiceBookmarkImporter} or {@link FileBookmarkImporter}.
 * 
 * @author rja
 * @version $Id: RelationImporter.java,v 1.6 2011-04-29 06:45:04 bibsonomy Exp $
 */
public interface RelationImporter {

	/**
	 * @return The imported relations. 
	 * @throws IOException - if an error opening the file/remote service occured.
	 */
	public List<Tag> getRelations() throws IOException;
	
	/**
	 * Sets the credentials used to authenticate the user against the remote
	 * service.
	 * 
	 * @param userName 
	 * @param password - could be also an API key or the like.
	 */
	public void setCredentials(final String userName, final String password);
	
}
