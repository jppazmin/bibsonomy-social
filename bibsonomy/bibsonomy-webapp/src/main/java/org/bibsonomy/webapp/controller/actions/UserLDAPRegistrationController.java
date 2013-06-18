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

package org.bibsonomy.webapp.controller.actions;

import org.bibsonomy.model.User;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.actions.UserIDRegistrationCommand;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.validation.UserLDAPRegistrationValidator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * This controller handles the registration of users via LDAP
 * 
 * @author Sven Stefani
 * @author rja
 * @version $Id: UserLDAPRegistrationController.java,v 1.18 2011-03-28 06:37:21 rja Exp $
 */
public class UserLDAPRegistrationController extends AbstractUserIDRegistrationController {
	
	/**
	 * Shall the LDAP ID be suggested as user name?
	 */
	private boolean ldapIdIsUsername = false;
	
	@Override
	protected String getLoginNotice() {
		return "register.ldap.step1";
	}
	
	@Override
	protected void setAuthentication(User registerUser, User user) {
		registerUser.setLdapId(user.getLdapId());
		/*
		 * For LDAP users we store their (hashed) LDAP password. Thus - if 
		 * "internal" authentication is enabled, they can login with their LDAP
		 * password using the internal authentication method.
		 */
		registerUser.setPassword(StringUtils.getMD5Hash(user.getPassword()));
	}

	@Override
	protected Authentication getAuthentication(User user) {
		return new UsernamePasswordAuthenticationToken(user.getLdapId(), user.getPassword());
	}

	@Override
	public Validator<UserIDRegistrationCommand> getValidator() {
		return new UserLDAPRegistrationValidator();
	}
	
	@Override
	protected String generateUserName(User user) {
		if (ldapIdIsUsername) {
			return user.getLdapId();
		}
		return super.generateUserName(user);
	}

	/**
	 * @return <code>true</code>, if the LDAP ID shall be suggested as user name 
	 * during registration.
	 */
	public boolean isLdapIdIsUsername() {
		return this.ldapIdIsUsername;
	}

	/**
	 * If the LDAP ID shall be suggested as user name during registration, set 
	 * this to <code>true</code>. Otherwise, a user name is generated using the 
	 * real name. 
	 *  
	 * @param ldapIdIsUsername
	 */
	public void setLdapIdIsUsername(boolean ldapIdIsUsername) {
		this.ldapIdIsUsername = ldapIdIsUsername;
	}
}