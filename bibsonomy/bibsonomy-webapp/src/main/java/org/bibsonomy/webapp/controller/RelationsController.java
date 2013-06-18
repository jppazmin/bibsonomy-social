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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.webapp.command.RelationsCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Christian Kramer
 * @version $Id: RelationsController.java,v 1.6 2010-11-17 10:55:35 nosebrain Exp $
 */
public class RelationsController extends SingleResourceListControllerWithTags implements MinimalisticController<RelationsCommand>{
	private static final Log LOGGER = LogFactory.getLog(RelationsController.class);

	/*
	 * the following concepts are unwanted on the relations page
	 * XXX: inject them using Spring?
	 */ 
	private static final String[] tagsToRemove = new String[]{
		"bookmarks_toolbar",
		"bookmarks_toolbar_folder",
		"forex",
		"from_internet_explorer",
		"how",
		"lesezeichen-symbolleiste",
		"personal_toolbar_folder",
		"the",
		"what"
	};
	
	@Override
	public RelationsCommand instantiateCommand() {	
		return new RelationsCommand();
	}

	@Override
	public View workOn(RelationsCommand command) {
		LOGGER.debug(this.getClass().getSimpleName());
		this.startTiming(this.getClass(), command.getFormat());

		// html format - retrieve tags and return HTML view
		if ("html".equals(command.getFormat())) {
			command.setPageTitle("relations"); // TODO: i18n
			/*
			 * request the concepts
			 */
			final List<Tag> tags = logic.getConcepts(Resource.class, GroupingEntity.ALL, null, null, null, ConceptStatus.ALL, 0, 50);

			for (final String string : tagsToRemove) {
				tags.remove(new Tag(string));
			}

			command.setTagRelations(tags);
		}

		this.endTiming();
		return Views.RELATIONS;
	}

}
