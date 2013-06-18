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

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.model.User;

/**
 * @author daill
 * @version $Id: RelatedUserCommand.java,v 1.4 2010-04-28 15:36:14 nosebrain Exp $
 */
public class RelatedUserCommand extends BaseCommand {
	
	/**
	 *  list of user to show
	 */
	private List<User> relatedUsers = new ArrayList<User>();

	/**
	 * @return list of user
	 */
	public List<User> getRelatedUsers() {
		return this.relatedUsers;
	}

	/**
	 * @param relatedUsers the relatedUsers to set
	 */
	public void setRelatedUsers(final List<User> relatedUsers) {
		this.relatedUsers = relatedUsers;
	}

}
