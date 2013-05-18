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

package org.bibsonomy.database.managers.chain.user;

import org.bibsonomy.database.managers.UserDatabaseManager;
import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.model.User;

/**
 * Chain element for user chain
 * 
 * @author Dominik Benz
 * @version $Id: UserChainElement.java,v 1.3 2010-09-28 11:19:49 nosebrain Exp $
 */
public abstract class UserChainElement extends ListChainElement<User, UserParam> {

	protected final UserDatabaseManager userDB;

	/**
	 * Constructs a chain element
	 */
	public UserChainElement() {
		this.userDB = UserDatabaseManager.getInstance();
	}
}