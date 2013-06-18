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

package org.bibsonomy.webapp.command.admin;

import java.util.HashMap;
import java.util.Map;

import org.bibsonomy.common.enums.Privlevel;
import org.bibsonomy.model.Group;
import org.bibsonomy.webapp.command.BaseCommand;


/**
 * Command bean for admin page 
 * 
 * @author bsc
 * @version $Id: AdminGroupViewCommand.java,v 1.3 2011-06-09 12:58:09 rja Exp $
 */
public class AdminGroupViewCommand extends BaseCommand {	
	
	/** specific action for admin page */
	private String action;
	/** Privacy options for the group */
	private final Map<String, Privlevel> privlevel;
	
	private String adminResponse = "";
	private Group group = new Group();
	
	
	public AdminGroupViewCommand() {
		/*
		 * FIXME: use proper localized messages
		 */
		privlevel = new HashMap<String, Privlevel>();
		privlevel.put("Member list hidden", Privlevel.HIDDEN);
		privlevel.put("Member list public", Privlevel.PUBLIC);
		privlevel.put("Members can list members", Privlevel.MEMBERS);
	}


	/**
	 * @return the privlevels
	 */
	public Map<String, Privlevel> getPrivlevel() {
		return this.privlevel;
	}
	/**
	 * @return the group
	 */
	public Group getGroup() {
		return this.group;
	}

	/**
	 * @param group the group to set 
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @param adminResponse
	 */
	public void setAdminResponse(String adminResponse) {
		this.adminResponse = adminResponse;
	}

	/**
	 * @return the admin response
	 */
	public String getAdminResponse() {
		return adminResponse;
	}
}