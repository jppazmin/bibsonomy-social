/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.command;

import org.bibsonomy.model.Group;


/**
 * Bean for Group-Sites
 *
 * @author  Stefan Stuetzer
 * @version $Id: GroupResourceViewCommand.java,v 1.7 2008-11-21 10:21:55 rja Exp $
 */
public class GroupResourceViewCommand extends TagResourceViewCommand {

	/** the group whose resources are requested*/
	private String requestedGroup = "";
	
	/** bean for group members */
	private Group group;
	
	/**
	 * @return requestedGroup name of the group whose resources are requested
	 */
	public String getRequestedGroup() {
		return this.requestedGroup;
	}

	/**
	 *  @param requestedGroup name of the group whose resources are requested
	 */
	public void setRequestedGroup(String requestedGroup) {
		this.requestedGroup = requestedGroup;
	}

	/** Get the group associated with this command.
	 * 
	 * @return The group associated with this command.
	 */
	public Group getGroup() {
		return this.group;
	}

	/** Set the group associated with this command
	 * @param group
	 */
	public void setGroup(Group group) {
		this.group = group;
	}		
}