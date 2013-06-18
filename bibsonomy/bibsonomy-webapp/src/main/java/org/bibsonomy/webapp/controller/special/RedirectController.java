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

package org.bibsonomy.webapp.controller.special;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.util.ValidationUtils;
import org.bibsonomy.webapp.command.special.RedirectCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.HeaderUtils;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.springframework.validation.Errors;

/**
 * Controller for handling various redirects, in particular /my* pages, 
 * the main page search form, and /uri/ content negotiation.
 * 
 * <p>Currently, the following /my* pages are available:
 * <ul>
 * <li>/myBibSonomy</li>
 * <li>/myBibTeX</li>
 * <li>/myRelations</li>
 * <li>/myPDF</li>
 * <li>/myDuplicates</li>
 * </ul>
 * </p>
 * 
 * @author rja
 * @version $Id: RedirectController.java,v 1.6 2010-07-14 14:21:45 nosebrain Exp $
 */
public class RedirectController implements MinimalisticController<RedirectCommand>, RequestAware, ErrorAware {
	private RequestLogic requestLogic;
	private static final Log log = LogFactory.getLog(RedirectController.class);
	private Errors errors;

	@Override
	public View workOn(final RedirectCommand command) {
		log.debug("handling /redirect URLs");
		String redirectUrl = "/login";

		final User user = command.getContext().getLoginUser();
		final String myPage = command.getMyPage();
		final String search = command.getSearch();
		final String url    = command.getUrl();
		log.debug("input: myPage=" + myPage + ", search=" + search + ", scope=" + command.getScope() + ", url=" + url);

		if (command.getContext().isUserLoggedIn() && ValidationUtils.present(myPage)) {
			/*
			 * handle /my* pages
			 */
			redirectUrl = getMyPageRedirect(myPage, user.getName());
		} else if (ValidationUtils.present(search)) {
			/*
			 * handle main page search form
			 */
			try {
				redirectUrl = getSearchPageRedirect(search, command.getScope(), command.getRequUser());
			} catch (UnsupportedEncodingException e) {
				log.error("Could not search form redirect URL.", e);
			}

		} else if (ValidationUtils.present(url)) { 
			/* 
			 * Handle /uri/ content negotiating using the Accept: header.
			 */
			log.debug("doing content negotiation for URL " + url);
			redirectUrl = getContentNegotiationRedirect(url, requestLogic.getAccept());
		}
		log.debug("finally redirecting to " + redirectUrl);
		return new ExtendedRedirectView(redirectUrl);
	}

	/** 
	 * CONTENT NEGOTIATION
	 * Creates a redirect to the requested output format dependent on the HTTP "accept" header.
	 * 
	 * @param url - the requested URL 
	 * @param acceptHeader - the accepted formats
	 * @return - the redirect URL.
	 */
	private String getContentNegotiationRedirect(final String url, final String acceptHeader) {
		log.debug("accepted formats: " + acceptHeader);
		/*
		 * determine relevant resource type
		 */
		int resourceType = 2;
		if (url.startsWith("url")) resourceType = 1;

		/*
		 * build redirectUrl
		 */
		final String responseFormat = HeaderUtils.getResponseFormat(acceptHeader, resourceType);
		/*
		 * check, if specific format returned
		 */
		if (ValidationUtils.present(responseFormat)) {
			return "/" + responseFormat + "/" + url;
		} 
		/*
		 * redirect to default format
		 */
		return "/" + url;
	}

	/** Handles redirects for main page search form. 
	 * 
	 * @param search
	 * @param scope
	 * @return
	 * @throws UnsupportedEncodingException - if it could not encode the parameters for the redirect.
	 */
	private String getSearchPageRedirect(final String search, final String scope, final String requUser) throws UnsupportedEncodingException {
		log.debug("handling redirect for main page search form");
		/*
		 * redirect either to /user/*, to /author/*, to /tag/* or to /concept/tag/* page 
		 */
		if ("author".equals(scope) && ValidationUtils.present(requUser)) {
			/*
			 * special handling, when requUser is given - this is for /author pages only
			 */
			log.debug("requUser given - handling /author");
			return "/author/" + URLEncoder.encode(search,"UTF-8") + "?requUser=" + URLEncoder.encode(requUser, "UTF-8");
		}
		if (scope.startsWith("user:")) {
			/*
			 * special handling, when scope is "user:USERNAME", this is search restricted to the given user name
			 */
			log.debug("scope is user:");
			return "/search/" + URLEncoder.encode(search + " " + scope, "UTF-8");
		}
		if (scope.startsWith("group:")) {
			/*
			 * special handling, when scope is "group:GROUPNAME", this is search restricted to the given group name
			 */
			log.debug("scope is group:");
			return "/search/" + URLEncoder.encode(search + " " + scope, "UTF-8");
		}
		/*
		 * all other pages simply go to /scope/search
		 */
		log.debug("generic handling of /scope/search");
		return "/" + scope + "/" + URLEncoder.encode(search, "UTF-8");


	}

	/** Handles pages starting with /my*, in particular
	 * <ul>
	 * <li>/myBibSonomy</li>
	 * <li>/myBibTeX</li>
	 * <li>/myRelations</li>
	 * <li>/myPDF</li>
	 * <li>/myDuplicates</li>
	 * </ul>
	 * <p>NOTE: this method only works for logged in users. If the user name is empty,
	 * or the myPage unknown, <code>null</code> is returned.</p> 
	 * 
	 * @param myPage - name of the page (i.e., <code>myRelations</code>).
	 * @param loginUserName - name of the logged in user
	 * @return The redirect to the appropriate page
	 */
	private String getMyPageRedirect(final String myPage, final String loginUserName) {
		/*
		 * we need a valid user name
		 */
		if (!ValidationUtils.present(loginUserName)) return null;
		/*
		 * redirects for /my* pages
		 */
		try {
			/*
			 * FIXME: use projectName here ?
			 */
			if ("myBibSonomy".equals(myPage)) {
				return "/user/" + URLEncoder.encode(loginUserName, "UTF-8");
			} else if ("myBibTeX".equals(myPage)) {
				return "/bib/user/" + URLEncoder.encode(loginUserName, "UTF-8") + "?items=1000";
			} else if ("myRelations".equals(myPage)) {
				return "/relations/" + URLEncoder.encode(loginUserName, "UTF-8");
			} else if ("myPDF".equals(myPage)) {
				return "/user/" + URLEncoder.encode(loginUserName, "UTF-8") + "?filter=myPDF";
			} else if ("myDuplicates".equals(myPage)) {
				return "/user/" + URLEncoder.encode(loginUserName, "UTF-8") + "?filter=myDuplicates";
			} else {
				log.error("Unknown /my* page called: " + myPage);
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Could not create /my* URL.", e);
		}
		/*
		 * we could not create an appropriate URL -> return null
		 */
		return null;
	}
	
	@Override
	public RedirectCommand instantiateCommand() {
		return new RedirectCommand();
	}
	
	/** 
	 * Supplies the requestLogic to this controller. The controller needs it to 
	 * get the HTTP "accept" header.
	 * 
	 * @see org.bibsonomy.webapp.util.ResponseAware#setResponseLogic(org.bibsonomy.webapp.util.ResponseLogic)
	 */
	@Override
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}
	
	@Override
	public Errors getErrors() {
		return errors;
	}
	
	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}
}