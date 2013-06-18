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

import java.io.Serializable;

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * This Commands information: which Message is to be removed from the inbox
 * 
 * @author sdo
 * @version $Id: RemoveMessageCommand.java,v 1.5 2010-05-11 12:24:53 nosebrain Exp $
 */
public class RemoveMessageCommand extends BaseCommand implements Serializable {
	private static final long serialVersionUID = -6623936347565283765L;
	
	
	private String hash;
	private String user;
	private boolean clear;
	
	/**
	 * @return true if user wishes to delete all from his inbox messages
	 */
	public boolean isClear() {
		return this.clear;
	}

	/**
	 * @param clear
	 */
	public void setClear(boolean clear) {
		this.clear = clear;
	}

	/**
	 * @return String
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 */
	public void setUser(final String user) {
		this.user = user;
	}

	/**
	 * @return String
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * @param hash
	 */
	public void setHash(final String hash) {
		this.hash = hash;
	}	
	
}
