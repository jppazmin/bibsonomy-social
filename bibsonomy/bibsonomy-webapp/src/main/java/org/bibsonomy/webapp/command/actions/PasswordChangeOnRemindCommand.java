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
 * @author daill
 * @version $Id: PasswordChangeOnRemindCommand.java,v 1.4 2010-12-10 12:54:02 dbe Exp $
 */
public class PasswordChangeOnRemindCommand extends BaseCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8544593363734488269L;
	/**
	 * the username with the ative temporary password
	 */
	private String userName;

	/**
	 * the new password entered by the user
	 */
	private String newPassword;
	/**
	 * the copy of the new password entered by the user
	 */
	private String passwordCheck;
	
	/**
	 * the reminder hash sent to the user (containing his encryped password)
	 */
	private String reminderHash;
	
	
	
	// **********************************************************
	// getter / setter
	// **********************************************************	
	
	/**
	 * @return String
	 */
	public String getUserName() {
		return this.userName;
	}
	/**
	 * @param userName
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}
	/**
	 * @return String
	 */
	public String getNewPassword() {
		return this.newPassword;
	}
	/**
	 * @param newPassword
	 */
	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return String
	 */
	public String getPasswordCheck() {
		return this.passwordCheck;
	}
	/**
	 * @param passwordCheck
	 */
	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}
	public void setReminderHash(String reminderHash) {
		this.reminderHash = reminderHash;
	}
	public String getReminderHash() {
		return reminderHash;
	}	

	
}
