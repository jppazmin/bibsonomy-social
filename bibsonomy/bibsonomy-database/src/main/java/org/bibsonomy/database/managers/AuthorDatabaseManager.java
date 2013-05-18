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

package org.bibsonomy.database.managers;

import java.util.List;

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.model.Author;

/**
 * @author Christian Claus
 * @version $Id: AuthorDatabaseManager.java,v 1.7 2011-05-09 12:08:44 nosebrain Exp $
 */
public class AuthorDatabaseManager extends AbstractDatabaseManager {
	private final static AuthorDatabaseManager singleton = new AuthorDatabaseManager();

	/**
	 * @return AuthorDatabaseManager
	 */
	public static AuthorDatabaseManager getInstance() {
		return singleton;
	}
	
	private AuthorDatabaseManager() {
		// noop
	}
	
	/**
	 * TODO: improve documentation
	 * 
	 * @param session
	 * @return list of authors
	 */
	@SuppressWarnings("unchecked")
	public List<Author> getAuthors(final DBSession session) {
		return queryForList("getAuthors", null, session);
	}
}
