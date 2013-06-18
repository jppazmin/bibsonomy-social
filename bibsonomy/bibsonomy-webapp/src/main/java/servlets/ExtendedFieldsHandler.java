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

package servlets;

import helpers.database.DBPrivnoteManager;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.model.User;
import org.bibsonomy.util.spring.security.AuthenticationUtils;

import filters.ActionValidationFilter;

@Deprecated
public class ExtendedFieldsHandler extends HttpServlet{

	private static final long serialVersionUID = 4051324539558769200L;

	@Override
	public void init(ServletConfig config) throws ServletException{	
		super.init(config); 
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final User user = AuthenticationUtils.getUser();
		final String currUser = user.getName(); 
		if (currUser == null) {
			response.sendRedirect("/login?referer=/basket");
			return;
		}

		/*
		 * action update private note
		 */
		final String action = request.getParameter("action");
		final boolean validCkey = ActionValidationFilter.isValidCkey(request);
		if (action != null && validCkey) {
			if ("updatePrivateNote".equals(action)) {		
				final String hash = request.getParameter("hash");
				
				/*
				 * FIXME: missing check, if currUser owns this publication!
				 */
				final String privnote    = request.getParameter("privnote");
				final String oldprivnote = request.getParameter("oldprivnote");
				if (((privnote == null && oldprivnote != null) || (privnote != null && (oldprivnote == null || !privnote.equals(oldprivnote))))) {
					/*
					 * something has changed --> write it to DB
					 */
					DBPrivnoteManager.setPrivnoteForUser(privnote, currUser, hash);
				}
				
				response.sendRedirect("/bibtex/" + HashID.INTRA_HASH.getId() + URLEncoder.encode(hash, "UTF-8") + "/" + URLEncoder.encode(currUser, "UTF-8"));
				return;
			}
		}
	}

}
