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
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.logic.LogicInterfaceFactory;

/**
 * This is a temporary logic interface factory to enable logic interface access
 * from within BibSonomy 1 without having to re-do authentication
 * 
 * XXX: Please remove this class once this is not necessary anymore.
 * 
 * dbe, 20071203
 * 
 * @author Dominik Benz
 */
public class DBLogicNoAuthInterfaceFactory implements LogicInterfaceFactory {

	private DBSessionFactory dbSessionFactory;

	@Override
	public LogicInterface getLogicAccess(final String loginName, final String password) {
		if (loginName != null) {
			/*
			 * In this case we don't fill the user object completely, but set
			 * it's name such that the user is seen as logged in (users which
			 * are not logged in cause a user object with empty name).
			 */
			return new DBLogic(new User(loginName), this.dbSessionFactory);
		}
		// guest access
		return new DBLogic(new User(), this.dbSessionFactory);
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
		return dbSessionFactory.getDatabaseSession();
	}
}