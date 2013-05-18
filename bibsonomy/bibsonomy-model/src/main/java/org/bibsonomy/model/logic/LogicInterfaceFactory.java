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

package org.bibsonomy.model.logic;

/**
 * Common interface of factories for objects of a LogicInterface implementation.
 * 
 * @author Jens Illig
 * @version $Id: LogicInterfaceFactory.java,v 1.9 2011-04-29 06:45:02 bibsonomy Exp $
 */
public interface LogicInterfaceFactory {
	/**
	 * @param loginName name of the user, who wants to access the system
	 * @param apiKey some sort of password
	 * @return a logicinterface implementation, that takes care of the users rights
	 */
	public LogicInterface getLogicAccess(String loginName, String apiKey);
}