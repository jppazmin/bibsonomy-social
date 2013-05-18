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

import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.database.managers.GroupDatabaseManager;
import org.bibsonomy.database.managers.UserDatabaseManager;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.logic.LogicInterfaceFactory;
import org.bibsonomy.model.util.UserUtils;

/**
 * This class produces DBLogic instances with user authentication
 * 
 * @author Jens Illig
 * @version $Id: DBLogicUserInterfaceFactory.java,v 1.21 2010-09-08 11:01:01 nosebrain Exp $
 */
public class DBLogicUserInterfaceFactory implements LogicInterfaceFactory {

	protected final UserDatabaseManager userDBManager = UserDatabaseManager.getInstance();
	protected final GroupDatabaseManager groupDb = GroupDatabaseManager.getInstance();

	protected DBSessionFactory dbSessionFactory;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bibsonomy.model.logic.LogicInterfaceFactory#getLogicAccess(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public LogicInterface getLogicAccess(final String loginName, final String password) {
		if (loginName != null) {
			final User loggedInUser = getLoggedInUser(loginName, password);
			if (loggedInUser.getName() != null) {
				return new DBLogic(loggedInUser, this.dbSessionFactory);
			}
			throw new AccessDeniedException("Wrong Authentication ('" + loginName + "'/'" + password + "')");
		}
		// guest access
		return new DBLogic(new User(), this.dbSessionFactory);
	}
	
	/**
	 * Returns a user object containing the details of the user, if he is logged
	 * in correctly. If not, the returned user object is empty and it's user
	 * name NULL.
	 * 
	 * @param loginName
	 * @param password
	 * @return user object with details of the logged in user
	 */
	protected User getLoggedInUser(final String loginName, final String password) {
		final DBSession session = openSession();
		try {
			final User loggedInUser = getLoggedInUserAccess(loginName, password, session);
			if (loggedInUser.getName() != null) {
				UserUtils.setGroupsByGroupIDs(loggedInUser, this.groupDb.getGroupIdsForUser(loggedInUser.getName(), session));
			}
			return loggedInUser;
		} finally {
			session.close();
		}
	}
	
	/**
	 * Calls the correct validation method on the {@link UserDatabaseManager}.
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	protected User getLoggedInUserAccess(final String loginName, final String password, final DBSession session) {
		return this.userDBManager.validateUserAccessByPassword(loginName, password, session);
	}
	
	/**
	 * @param dbSessionFactory
	 *            the {@link DBSessionFactory} to use
	 */
	public void setDbSessionFactory(final DBSessionFactory dbSessionFactory) {
		this.dbSessionFactory = dbSessionFactory;
	}

	/**
	 * Returns a new database session.
	 */
	protected DBSession openSession() {
		return this.dbSessionFactory.getDatabaseSession();
	}
	
}