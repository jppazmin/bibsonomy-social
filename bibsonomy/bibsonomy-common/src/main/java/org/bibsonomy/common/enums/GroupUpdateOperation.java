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
 * @author cvo
 * @version $Id: GroupUpdateOperation.java,v 1.6 2011-07-14 13:01:48 nosebrain Exp $
 */
public enum GroupUpdateOperation {
	
	/**
	 * Update the settings of a group.
	 */
	UPDATE_SETTINGS,
	
	/**
	 * Adds new user to a group.
	 * TODO: why do we need this operation? LogicInterface defines an
	 * addUserToGroup method!
	 */
	ADD_NEW_USER,
	
	/**
	 * Update the whole group
	 */
	UPDATE_ALL;
}
