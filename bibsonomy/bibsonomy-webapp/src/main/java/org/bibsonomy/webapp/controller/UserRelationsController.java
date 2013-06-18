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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.webapp.command.UserRelationCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Christian Voigtmann
 * @version $Id: UserRelationsController.java,v 1.11 2010-07-14 14:21:46 nosebrain Exp $
 */
public class UserRelationsController extends SingleResourceListControllerWithTags implements MinimalisticController<UserRelationCommand> {
	private static final Log LOGGER = LogFactory.getLog(AuthorPageController.class);

	@Override
	public View workOn(final UserRelationCommand command) {
		LOGGER.debug(this.getClass().getSimpleName());

		this.startTiming(this.getClass(), command.getFormat());
		
		// no user given -> error
		if (!present(command.getRequestedUser())) {
			/*
			 * FIXME: wrong error message, should be /relations/ without user
			 */
			throw new MalformedURLSchemeException("error.user_page_without_username");
		}

		// set grouping entity, grouping name, tags
		final GroupingEntity groupingEntity = GroupingEntity.USER;
		final String groupingName = command.getRequestedUser();

		//query for the number of relations of a user
		final int numberOfRelations = this.logic.getTagStatistics(null, groupingEntity, groupingName, null, null, ConceptStatus.ALL, 0, Integer.MAX_VALUE);

		final int limit = command.getConcepts().getEntriesPerPage();
		final int offset = command.getConcepts().getStart();
		
		// retrieving concepts
		final List<Tag> concepts = this.logic.getConcepts(null, groupingEntity, groupingName, null, null, ConceptStatus.ALL, offset, limit + offset);

		command.getConcepts().setConceptList(concepts);


		command.getConcepts().setNumConcepts(concepts.size());
		command.getConcepts().setTotalCount(numberOfRelations);

		// set page title
		// TODO: internationalize
		command.setPageTitle("relations :: " + groupingName);
		
		if ("html".equals(command.getFormat())) {
			this.setTags(command, Resource.class, groupingEntity, groupingName, null, null, null, 20000, null);

			// log if a user has reached threshold
			if (command.getTagcloud().getTags().size() > 19999) {
				LOGGER.error("User " + groupingName + " has reached threshold of 20000 tags on user page");
			}

			/*
			 * add user details to command, if loginUser is admin
			 */
			if (Role.ADMIN.equals(logic.getAuthenticatedUser().getRole())) {
				command.setUser(logic.getUserDetails(command.getRequestedUser()));
			}
		}

		this.endTiming();
		// export - return the appropriate view
		return Views.USERRELATED;
	}

	@Override
	public UserRelationCommand instantiateCommand() {
		return new UserRelationCommand();
	}
}