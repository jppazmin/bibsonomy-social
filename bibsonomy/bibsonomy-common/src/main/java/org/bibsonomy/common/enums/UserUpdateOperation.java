/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.common.enums;

/**
 * Depicts which party of a user should be updated when calling 
 * the <code>update(...)</code> method in the LogicInterface.
 * 
 * @author cvo
 * @version $Id: UserUpdateOperation.java,v 1.9 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum UserUpdateOperation {

	/**
	 * Update all parts of the entity.
	 */
	UPDATE_ALL(0),
	/**
	 * Update only the password of a user.
	 */
	UPDATE_PASSWORD(1),
	/**
	 * Update only the settings of a user.
	 */
	UPDATE_SETTINGS(2),
	/**
	 * Update only the core settings of a user (personal data, like homepage etc.)
	 */
	UPDATE_CORE(3),
	/**
	 * Update only the API key of a user
	 */
	UPDATE_API(4),
	/**
	 * Activates the user
	 */
	ACTIVATE(5);
	
	private int id;
	
	private UserUpdateOperation(final int userUpdateOperation) {
		this.id = userUpdateOperation;
	}
}
