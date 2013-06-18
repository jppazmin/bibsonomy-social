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
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.BaseCommand;


/**
 * Command bean for admin page 
 * 
 * @author Beate Krause
 * @version $Id: AdminCommand.java,v 1.7 2011-07-07 09:55:38 wla Exp $
 */
public class AdminCommand extends BaseCommand{

	/** Automatic actions (linked) for this page */
	private final Map<String,String> actionTitles;
	
	/** Privacy options for the group */
	private final Map<String, Privlevel> privlevel;
	
	/** Selected privacy level */
	private Privlevel selPrivlevel;

	/** information about a specific user */
	private String aclUserInfo; 
	
	/** specific action for admin page */
	private String action; 

	/** specific user to show */
	private User user;
	
	/** group name of group to be added to the system */
	private String requestedGroupName;
	
	/** specific user information */
	private String adminResponse;

	/**
	 * inits titles
	 */
	public AdminCommand(){
		// set actions 
		actionTitles = new HashMap<String, String>();
		actionTitles.put("spam", "Flag / unflag spammers");
		actionTitles.put("group", "Manage groups");
		actionTitles.put("lucene", "Manage lucene");
		actionTitles.put("recommender", "Manage recommenders");
		actionTitles.put("oauth", "Manage OAuth Consumers");
		actionTitles.put("sync", "Manage synchronization settings");
		
		// set privacy options
		privlevel = new HashMap<String, Privlevel>();
		privlevel.put("Member list hidden", Privlevel.HIDDEN);
		privlevel.put("Member list public", Privlevel.PUBLIC);
		privlevel.put("Members can list members", Privlevel.MEMBERS);
	}

	/**
	 * @return the actionTitles
	 */
	public Map<String, String> getActionTitles() {
		return this.actionTitles;
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
	 * @return the aclUserInfo
	 */
	public String getAclUserInfo() {
		return this.aclUserInfo;
	}

	/**
	 * @param aclUserInfo the aclUserInfo to set
	 */
	public void setAclUserInfo(String aclUserInfo) {
		this.aclUserInfo = aclUserInfo;
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
	
	/**
	 * @return the requestedGroupName
	 */
	public String getRequestedGroupName() {
		return this.requestedGroupName;
	}

	/**
	 * @param requestedGroupName the requestedGroupName to set
	 */
	public void setRequestedGroupName(String requestedGroupName) {
		this.requestedGroupName = requestedGroupName;
	}

	/**
	 * @return the privlevel
	 */
	public Map<String, Privlevel> getPrivlevel() {
		return this.privlevel;
	}
	
	/**
	 * @return the selPrivlevel
	 */
	public Privlevel getSelPrivlevel() {
		return this.selPrivlevel;
	}

	/**
	 * @param selPrivlevel the selPrivlevel to set
	 */
	public void setSelPrivlevel(Privlevel selPrivlevel) {
		this.selPrivlevel = selPrivlevel;
	}

	/**
	 * @return the adminResponse
	 */
	public String getAdminResponse() {
		return this.adminResponse;
	}

	/**
	 * @param adminResponse the adminResponse to set
	 */
	public void setAdminResponse(String adminResponse) {
		this.adminResponse = adminResponse;
	}
}