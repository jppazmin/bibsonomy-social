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

package org.bibsonomy.webapp.command.opensocial;

import java.util.List;

import org.bibsonomy.opensocial.oauth.database.beans.OAuthConsumerInfo;


/**
 * @author fei
 * @version $Id: OAuthAdminCommand.java,v 1.2 2011-06-11 13:08:47 bsc Exp $
 */
public class OAuthAdminCommand extends OAuthCommand {
	public enum AdminAction { List, Register };
	
	private String adminAction;
	
	private List<OAuthConsumerInfo> consumers;
	
	//------------------------------------------------------------------------
	// getter/setter
	//------------------------------------------------------------------------
	
	public void setAdminAction(String authorizeAction) {
		this.adminAction = authorizeAction;
	}

	public String getAdminAction() {
		return adminAction;
	}
	
	/**
	 * tmp getter until spring's enum binding works again
	 * @return
	 */
	public AdminAction getAdminAction_() {
		return this.adminAction == null ? null : AdminAction.valueOf(this.adminAction);
	}

	public void setConsumers(List<OAuthConsumerInfo> consumers) {
		this.consumers = consumers;
	}

	public List<OAuthConsumerInfo> getConsumers() {
		return consumers;
	}

}
