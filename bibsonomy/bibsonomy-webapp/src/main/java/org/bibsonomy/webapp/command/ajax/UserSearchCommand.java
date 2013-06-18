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

package org.bibsonomy.webapp.command.ajax;

import java.util.List;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.SimpleResourceViewCommand;


/**
 * @author bsc
 * @version $Id: UserSearchCommand.java,v 1.3 2011-05-23 16:20:36 bsc Exp $
 */
public class UserSearchCommand extends SimpleResourceViewCommand {
	private String search;
	private int limit;
	private List<User> users;
	private boolean showSpammers;
	
	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return this.users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
	/**
	 * @return the search
	 */
	public String getSearch() {
		return this.search;
	}
	/**
	 * @param search the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return this.limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = (limit > 0 ? limit : 10);
	}
	/**
	 * @param showSpammers the showSpammers to set
	 */
	public void setShowSpammers(boolean showSpammers) {
		this.showSpammers = showSpammers;
	}
	/**
	 * @return the showSpammers
	 */
	public boolean showSpammers() {
		return showSpammers;
	}
}
