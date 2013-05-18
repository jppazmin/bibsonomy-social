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

package org.bibsonomy.database;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.model.User;

/**
 * This class produces DBLogic instances with API authentication
 * 
 * @author Jens Illig
 * @version $Id: DBLogicApiInterfaceFactory.java,v 1.8 2010-09-08 11:01:01 nosebrain Exp $
 */
public class DBLogicApiInterfaceFactory extends DBLogicUserInterfaceFactory {

	@Override
	protected User getLoggedInUserAccess(final String loginName, final String password, final DBSession session) {
		return this.userDBManager.validateUserAccessByAPIKey(loginName, password, session);
	}
}