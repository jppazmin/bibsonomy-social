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

import static org.bibsonomy.util.ValidationUtils.present;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.webapp.command.actions.UserLoginCommand;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.ServiceUnavailableException;
import org.bibsonomy.webapp.validation.UserLoginValidator;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;

/**
 * this controller is only responsible for the view
 * 	- /login
 * 
 * the login is handled by Spring Security
 * @author dzo
 * @version $Id: UserLoginController.java,v 1.35 2011-07-14 13:15:47 nosebrain Exp $
 */
public class UserLoginController implements ValidationAwareController<UserLoginCommand> {
	private static final Log log = LogFactory.getLog(UserLoginController.class);
	
	private RequestLogic requestLogic;

	@Override
	public UserLoginCommand instantiateCommand() {
		return new UserLoginCommand();
	}

	@Override
	public View workOn(final UserLoginCommand command) {
		/*
		 * get last exception from spring security and clean the attribute
		 */
		final Exception lastException = (Exception) this.requestLogic.getSessionAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		this.requestLogic.setSessionAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, null);
		
		/*
		 * get last exception from spring security
		 */
		if (present(lastException)) {
			/*
			 * This means the user tried too often to login (triggered TeerGrube)
			 */
			if (lastException instanceof ServiceUnavailableException) {
				throw (ServiceUnavailableException) lastException;
			}
			/*
			 * We want to log all remaining exceptions, except the trivial ones.
			 */
			if (!(lastException instanceof BadCredentialsException)) {
				log.warn("User " + command.getUsername() + " tried to login and raised exception ", lastException);
			}
			final String messageKey = lastException.getClass().getSimpleName().toLowerCase();
			command.setMessage(messageKey);
		}
		
		return Views.LOGIN;
	}
	
	/**
	 * @param requestLogic the requestLogic to set
	 */
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	@Override
	public boolean isValidationRequired(final UserLoginCommand command) {
		return true;
	}

	@Override
	public Validator<UserLoginCommand> getValidator() {
		return new UserLoginValidator();
	}
}
