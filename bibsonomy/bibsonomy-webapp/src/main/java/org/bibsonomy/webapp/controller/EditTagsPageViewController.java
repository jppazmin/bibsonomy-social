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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.ConceptUpdateOperation;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.webapp.command.EditTagsPageViewCommand;
import org.bibsonomy.webapp.command.actions.EditTagsCommand;
import org.bibsonomy.webapp.command.actions.RelationsEditCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.bibsonomy.webapp.view.Views;


/**
 * Controller for the edit_tags page 
 * 
 * @author Henrik Bartholmai
 * @version $Id: EditTagsPageViewController.java,v 1.7 2011-07-18 13:54:09 nosebrain Exp $
 */
public class EditTagsPageViewController extends SingleResourceListControllerWithTags implements MinimalisticController<EditTagsPageViewCommand> {
	
	@Override
	public View workOn(final EditTagsPageViewCommand command) {
		/*
		 * no user given -> error
		 */
		if (!command.getContext().isUserLoggedIn()) {
			throw new AccessDeniedNoticeException("please log in", "error.general.login");
		}
		
		int changedResources = 0;

		switch (command.getForcedAction()) {

		case 1:
			changedResources = workOnEditTagsHandler(command);
			break;
			
		case 2:
			workOnRelationsHandler(command);
			break;
		}
		
		/*
		 * clear the input fields
		 */
		command.getEditTags().setAddTags("");
		command.getEditTags().setDelTags("");
		command.getRelationsEdit().setLower("");
		command.getRelationsEdit().setUpper("");
		command.setUpdatedTagsCount(changedResources);
		
		/*
		 * set grouping entity, grouping name, tags
		 */
		final GroupingEntity groupingEntity = GroupingEntity.USER;
		final User user = command.getContext().getLoginUser();
		final String groupingName = user.getName();

		/*
		 * set the tags of the user to get his tag cloud
		 */
		this.setTags(command, Resource.class, groupingEntity, groupingName, null, null, null, 20000, null);

		/*
		 * get all concepts of the user 
		 */
		final List<Tag> concepts = this.logic.getConcepts(null, groupingEntity, groupingName, null, null, ConceptStatus.ALL, 0, Integer.MAX_VALUE);
		command.getConcepts().setConceptList(concepts);
		command.getConcepts().setNumConcepts(concepts.size());
		
		return Views.EDIT_TAGS;
	}
	
	private int workOnEditTagsHandler(final EditTagsPageViewCommand cmd) {
		final User user = cmd.getContext().getLoginUser();
		final EditTagsCommand command = cmd.getEditTags();
		int updatedTags = 0;
		
		try {
			final Set<Tag> tagsToReplace = TagUtils.parse(command.getDelTags());

			if (tagsToReplace.size() <= 0) {
				return 0;
			}
			
			final Set<Tag> replacementTags = TagUtils.parse(command.getAddTags());

			//remove possible relations!
			Iterator<Tag> it = tagsToReplace.iterator();
			while(it.hasNext()) {
				final Tag t = it.next();
				
				if(t.getSuperTags().size() != 0) {
					it.remove();
					continue;
				}
				
				if(t.getSubTags().size() != 0)
					t.getSubTags().clear();
			}
			
			it = replacementTags.iterator();
			while(it.hasNext()) {
				final Tag t = it.next();
				
				if(t.getSuperTags().size() != 0) {
					it.remove();
					continue;
				}
				
				if(t.getSubTags().size() != 0)
					t.getSubTags().clear();
			}
			
			if(!command.isUpdateRelations()) {
				
				updatedTags = logic.updateTags(user, new LinkedList<Tag>(tagsToReplace), new LinkedList<Tag>(replacementTags), false);
			} else {
				if(tagsToReplace.size() != 1 || replacementTags.size() != 1) 
					throw new MalformedURLSchemeException("edittags.main.note");
				
				updatedTags = logic.updateTags(user, new LinkedList<Tag>(tagsToReplace), new LinkedList<Tag>(replacementTags), true);
			}
			
		} catch (final RecognitionException ex) {
			// TODO How can i handle this
		}

		return updatedTags;
	}
	
	private void workOnRelationsHandler(final EditTagsPageViewCommand cmd) {
		final User user = cmd.getContext().getLoginUser();
		final RelationsEditCommand command = cmd.getRelationsEdit();
		
		switch (command.getForcedAction()) {
		case 0:
			try {
	
			final Set<Tag> upperList = TagUtils.parse(command.getUpper());
			final Set<Tag> lowerList = TagUtils.parse(command.getLower());
			
			if(upperList.size() != 1 || lowerList.size() != 1)
				break;
			
			final Tag upper = upperList.iterator().next();
			final Tag lower = lowerList.iterator().next();
			
			if (upper.getSubTags().size() != 0 || upper.getSuperTags().size() != 0 ||
					lower.getSubTags().size() != 0 || lower.getSuperTags().size() != 0)
				break;
			
			upper.setSubTags(new LinkedList<Tag>(lowerList));
			
			logic.updateConcept(upper, GroupingEntity.USER, user.getName(),ConceptUpdateOperation.UPDATE);
			break;
			

			} catch (final RecognitionException ex) {
				// TODO how should i handle this
				break;
			}

		case 1:
			logic.deleteRelation(command.getUpper(), command.getLower(), GroupingEntity.USER, user.getName());
			break;
			
		}
	}

	@Override
	public EditTagsPageViewCommand instantiateCommand() {
		return new EditTagsPageViewCommand();
	}
}