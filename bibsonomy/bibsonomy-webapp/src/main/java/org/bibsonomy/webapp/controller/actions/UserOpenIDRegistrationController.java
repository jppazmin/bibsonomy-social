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

import java.util.Random;

import org.bibsonomy.model.User;
import org.bibsonomy.util.HashUtils;
import org.bibsonomy.util.spring.security.UserAdapter;
import org.bibsonomy.webapp.command.actions.UserIDRegistrationCommand;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.validation.UserOpenIDRegistrationValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.openid.OpenIDAuthenticationToken;

/**
 * This controller handles the registration of users via OpenID
 * (see http://openid.net/)
 * 
 * @author Stefan Stützer
 * @author rja
 * @version $Id: UserOpenIDRegistrationController.java,v 1.13 2011-03-04 16:42:42 folke Exp $
 */
public class UserOpenIDRegistrationController extends AbstractUserIDRegistrationController {

	@Override
	protected String getLoginNotice() {
		return "register.openid.step1";
	}
	
	@Override
	protected void setAuthentication(User registerUser, User user) {
		registerUser.setOpenID(user.getOpenID());
		/*
		 * We don't have a password for OpenID users, thus we set a random one
		 * for security reasons.
		 */
		registerUser.setPassword(generateRandomPassword());
	}

	@Override
	protected Authentication getAuthentication(User user) {
		return new OpenIDAuthenticationToken(new UserAdapter(user), new UserAdapter(user).getAuthorities(), user.getOpenID(), null);
	}

	private String generateRandomPassword() {
		final byte[] bytes = new byte[16];
		new Random().nextBytes(bytes);
		final String randomPassword = HashUtils.getMD5Hash(bytes);
		return randomPassword;
	}

	@Override
	public Validator<UserIDRegistrationCommand> getValidator() {
		return new UserOpenIDRegistrationValidator();
	}
}