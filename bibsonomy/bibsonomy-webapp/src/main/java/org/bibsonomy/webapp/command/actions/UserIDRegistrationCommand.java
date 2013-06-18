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

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.BaseCommand;

/**
 * @author Stefan Stützer
 * @version $Id: UserIDRegistrationCommand.java,v 1.1 2010-12-14 14:21:23 rja Exp $
 */
public class UserIDRegistrationCommand extends BaseCommand implements Serializable {
	
	/**
	 * serial uid
	 */
	private static final long serialVersionUID = 1371638749968299277L;
	
	/**
	 * Holds the details of the user which wants to register (like name, email, password)
	 */
	private User registerUser;

	/**
	 * Registration step
	 */
	private int step = 1;
	
	private boolean rememberMe;
	
	/**
	 * @return register user
	 */
	public User getRegisterUser() {
		return this.registerUser;
	}
	
	/**
	 * Sets register user 
	 * @param registerUser
	 */
	public void setRegisterUser(User registerUser) {
		this.registerUser = registerUser;
	}
	
	/**
	 * @return registration step
	 */
	public int getStep() {
		return this.step;
	}
	
	/**
	 * Sets registration step
	 * @param step
	 */
	public void setStep(int step) {
		this.step = step;
	}

	/**
	 * @return If the user wants to stay logged in. 
	 */
	public boolean getRememberMe() {
		return this.rememberMe;
	}

	/**
	 * @param rememberMe
	 */
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}	
}