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

package org.bibsonomy.database.params;

import org.bibsonomy.model.User;

/**
 * @param <T> the class of the id
 * 
 * @author dzo
 * @version $Id: LoggingParam.java,v 1.3 2010-07-01 13:30:19 bibsonomy Exp $ 
 */
public class LoggingParam<T> {
	private T oldId;
	private T newId;
	private User user;
	
	/**
	 * @return the oldId
	 */
	public T getOldId() {
		return this.oldId;
	}
	
	/**
	 * @param oldId the oldId to set
	 */
	public void setOldId(T oldId) {
		this.oldId = oldId;
	}

	/**
	 * @param newId the newId to set
	 */
	public void setNewId(T newId) {
		this.newId = newId;
	}

	/**
	 * @return the newId
	 */
	public T getNewId() {
		return newId;
	}
	
	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
