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

package org.bibsonomy.webapp.command.actions;

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.model.Group;
import org.bibsonomy.model.Tag;
import org.bibsonomy.webapp.command.PostCommand;

/**
 * @author mwa
 * @version $Id: GroupAdminCommand.java,v 1.2 2010-04-26 14:41:43 nosebrain Exp $
 */
public class GroupAdminCommand extends PostCommand {

	private boolean userLoggedIn;
	
	private Group group;
	
	private String requestedGroup;
	
	private String setName;
	
	private List<Tag> tags;
	
	/**
	 * inits values
	 */
	public GroupAdminCommand(){
		group = new Group();
		tags = new ArrayList<Tag>();
	}
	
	/**
	 * @return the userLoggedIn
	 */
	public boolean isUserLoggedIn() {
		return this.userLoggedIn;
	}

	/**
	 * @param userLoggedIn the userLoggedIn to set
	 */
	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
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
	 * @return the requestedGroup
	 */
	public String getRequestedGroup() {
		return this.requestedGroup;
	}

	/**
	 * @param requestedGroup the requestedGroup to set
	 */
	public void setRequestedGroup(String requestedGroup) {
		this.requestedGroup = requestedGroup;
	}

	/**
	 * @return the setName
	 */
	public String getSetName() {
		return this.setName;
	}

	/**
	 * @param setName the setName to set
	 */
	public void setSetName(String setName) {
		this.setName = setName;
	}

	/**
	 * @return the tags
	 */
	public List<Tag> getTags() {
		return this.tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

}
