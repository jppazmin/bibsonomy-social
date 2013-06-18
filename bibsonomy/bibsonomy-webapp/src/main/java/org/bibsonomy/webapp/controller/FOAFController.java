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

 package org.bibsonomy.webapp.controller;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.FOAFCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * controller for the FOAF-rdf output:
 * 		- /foaf/user/USER
 * For more information please visit the <a href="http://www.foaf-project.org/">FOAF project page</a>
 * 
 * XXX: instead of returning hand-crafted RDF via JSPs, we should 
 * probably use an RDF writer from the Jena project.
 * 
 * @author dzo
 * @version $Id: FOAFController.java,v 1.8 2011-07-14 13:26:22 nosebrain Exp $
 */
public class FOAFController implements MinimalisticController<FOAFCommand> {
	private LogicInterface logic;
	
	@Override
	public FOAFCommand instantiateCommand() {
		return new FOAFCommand();
	}

	@Override
	public View workOn(final FOAFCommand command) {		
		if (!command.getContext().isUserLoggedIn()) {
			throw new org.springframework.security.access.AccessDeniedException("please log in");
		}
		
		final String requestedUser = command.getRequestedUser();
		
		if (!present(requestedUser)) {
			throw new MalformedURLSchemeException("error.foaf_output_without_username");
		}
		
		/*
		 * get informations from logic
		 */
		final User user = this.logic.getUserDetails(requestedUser);
		
		/*
		 * add friends
		 */
		try {
			user.addFriends(this.logic.getUserRelationship(requestedUser, UserRelation.OF_FRIEND, null));
		} catch (final AccessDeniedException ex) {
			// ignore it
		}
		
		/*
		 *  prepare mail address / encode it using sha-1
		 */
		final String mail = user.getEmail();
		if (present(mail)) {
			final String toEncode = "mailto:" + mail;
			user.setEmail(StringUtils.getSHA1Hash(toEncode));
		}		
		
		command.setUser(user);
		return Views.FOAF;
	}

	/**
	 * @param logic the adminLogic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}
}
