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

package org.bibsonomy.webapp.util.spring.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.bibsonomy.model.User;
import org.bibsonomy.util.spring.security.UserAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author dzo
 * @version $Id: SecurityContextAwareSessionLocaleResolver.java,v 1.3 2011-03-16 13:54:48 nosebrain Exp $
 */
public class SecurityContextAwareSessionLocaleResolver extends SessionLocaleResolver {
	
	@Override
	protected Locale determineDefaultLocale(final HttpServletRequest request) {
		/*
		 * check if an user is logged in to use the user's default language
		 */
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			final Object principal = authentication.getPrincipal();
			if (principal != null && principal instanceof UserAdapter) {
				final User user = ((UserAdapter) principal).getUser();
				final String lang = user.getSettings().getDefaultLanguage();
				final Locale locale = new Locale(lang);
				/*
				 * save it in the session
				 * NOTE: it's ok to call set locale with response = null
				 * setLocale doesn't use the response parameter
				 */
				this.setLocale(request, null, locale);
				return locale;
			}
		}
		
		/*
		 * else use the default application locale
		 */
		return super.determineDefaultLocale(request);
	}

}
