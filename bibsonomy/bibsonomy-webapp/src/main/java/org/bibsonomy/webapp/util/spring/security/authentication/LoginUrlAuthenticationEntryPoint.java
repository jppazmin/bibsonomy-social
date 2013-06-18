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

package org.bibsonomy.webapp.util.spring.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.springframework.security.core.AuthenticationException;

/**
 * @author dzo
 * @version $Id: LoginUrlAuthenticationEntryPoint.java,v 1.1 2011-06-15 14:24:43 nosebrain Exp $
 */
public class LoginUrlAuthenticationEntryPoint extends org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint {

	private static final String NOTICE_PARAM_NAME = "notice";

	@Override
	protected String determineUrlToUseForThisRequest(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) {
		final String urlToUse = super.determineUrlToUseForThisRequest(request, response, exception);
		
		/*
		 * if the cause of the exception is an AccessDeniedNoticeException
		 * append the notice param of the exception to the url
		 */
		if (exception.getCause() instanceof AccessDeniedNoticeException) {
			final AccessDeniedNoticeException noticeException = (AccessDeniedNoticeException) exception.getCause();
			return UrlUtils.setParam(urlToUse, NOTICE_PARAM_NAME, noticeException.getNotice());
		}
		
		return urlToUse;
	}
}
