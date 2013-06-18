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
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.util.EnumUtils;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.UserResourceViewCommand;
import org.bibsonomy.webapp.config.Parameters;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RankingUtil;
import org.bibsonomy.webapp.util.RankingUtil.RankingMethod;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for user pages /user/USERNAME
 * 
 * @author Dominik Benz
 * @version $Id: UserUserPageController.java,v 1.9 2010-11-17 10:55:34 nosebrain Exp $
 */
public class UserUserPageController extends SingleResourceListControllerWithTags implements MinimalisticController<UserResourceViewCommand> {
	private static final Log LOGGER = LogFactory.getLog(UserUserPageController.class);

	@Override
	public View workOn(final UserResourceViewCommand command) {
		LOGGER.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		final String groupingName = command.getRequestedUser();
		
		// no user given -> error
		if (!present(groupingName)) {
			throw new MalformedURLSchemeException("error.user_page_without_username");
		}
		
		// set grouping entity, grouping name, tags, user similarity
		final GroupingEntity groupingEntity = GroupingEntity.USER;
		final List<String> requTags = command.getRequestedTagsList();		
		final UserRelation userRelation = EnumUtils.searchEnumByName(UserRelation.values(), command.getUserSimilarity()); 

		// handle case when only tags are requested
		this.handleTagsOnly(command, groupingEntity, groupingName, null, requTags, null, Integer.MAX_VALUE, null);
		
		// if we're in the personalized view, we have to retrieve all posts first & then 
		// re-rank them 
		final int entriesPerPage = Parameters.NUM_RESOURCES_FOR_PERSONALIZED_RANKING;
		command.setSortPage("ranking");
		command.setSortPageOrder("desc");

		// fetch all tags of logged-in user
		final List<Tag> loginUserTags = this.logic.getTags(Resource.class, groupingEntity, command.getContext().getLoginUser().getName(), null, null, null, null, 0, Integer.MAX_VALUE, null, null);
		
		// fetch all tags of requested user
		final List<Tag> targetUserTags = this.logic.getTags(Resource.class, groupingEntity, groupingName, null, null, null, null, 0, Integer.MAX_VALUE, null, null);		
		
		// retrieve and set the requested resource lists, along with total
		// counts
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			final ListCommand<?> listCommand = command.getListCommand(resourceType);
						
			final int origEntriesPerPage = listCommand.getEntriesPerPage();
			this.setList(command, resourceType, groupingEntity, groupingName, requTags, null, null, null, null, entriesPerPage);
			listCommand.setEntriesPerPage(origEntriesPerPage);
										
			// compute the ranking for each post in the list
			RankingUtil.computeRanking(loginUserTags, targetUserTags, command.getListCommand(resourceType).getList(), RankingMethod.TFIDF, false);

			// post-process & sort
			this.postProcessAndSortList(command, resourceType);

			// show only the top ranked resources for each resource type
			if (command.getListCommand(resourceType).getList().size() > origEntriesPerPage) {
				this.restrictResourceList(command, resourceType, listCommand.getStart(), listCommand.getStart() + origEntriesPerPage);				
			}
			
			// set total nr. 
			this.setTotalCount(command, resourceType, groupingEntity, groupingName, requTags, null, null, null, null, origEntriesPerPage, null);
		}


		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			// set page title
			command.setPageTitle("user :: " + groupingName + " (personalized)"); // TODO: i18n
			
			// re-compute tag weights
			RankingUtil.computeRanking(loginUserTags, targetUserTags);
			
			// insert tags into tagcloud
			command.getTagcloud().setTags(targetUserTags);
			
			// this.setTags(command, Resource.class, groupingEntity, groupingName, null, null, null, null, 0, Integer.MAX_VALUE, null);
			
			// retrieve similar users
			List<User> similarUsers = this.logic.getUsers(null, GroupingEntity.USER, groupingName, null, null, null, userRelation, null, 0, 10);
			command.getRelatedUserCommand().setRelatedUsers(similarUsers);
			
			this.endTiming();
			// return personalized view
			return Views.USERUSERPAGE;
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);
	}

	@Override
	public UserResourceViewCommand instantiateCommand() {
		return new UserResourceViewCommand();
	}
}
