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
import org.bibsonomy.webapp.command.FollowersViewCommand;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.config.Parameters;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RankingUtil;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Christian Kramer
 * @version $Id: FollowersPageController.java,v 1.6 2010-11-17 10:55:35 nosebrain Exp $
 */
public class FollowersPageController extends SingleResourceListController implements MinimalisticController<FollowersViewCommand>{
	private static final Log log = LogFactory.getLog(FollowersPageController.class);

	@Override
	public View workOn(final FollowersViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		// you have to be logged in
		if (!command.getContext().isUserLoggedIn()) {
			throw new MalformedURLSchemeException("error.general.login"); // TODO: redirect to login page?!
		}
		
		// set params
		final UserRelation userRelation = EnumUtils.searchEnumByName(UserRelation.values(), command.getUserSimilarity());
		GroupingEntity groupingEntity = GroupingEntity.FOLLOWER;
		String groupingName = null;
		if (present(command.getRequestedUser())) {
			groupingEntity = GroupingEntity.USER;
			groupingName = command.getRequestedUser();
		}
		
		// ranking settings
		final Integer start = command.getRanking().getPeriod() * Parameters.NUM_RESOURCES_FOR_PERSONALIZED_RANKING;
		command.getRanking().setPeriodStart(start + 1);
		command.getRanking().setPeriodEnd(start + Parameters.NUM_RESOURCES_FOR_PERSONALIZED_RANKING);		
		
		
		// handle case when only tags are requested
		this.handleTagsOnly(command, groupingEntity, groupingName, null, null, null, Integer.MAX_VALUE, null);
		
		// personalization settings
		final int entriesPerPage = Parameters.NUM_RESOURCES_FOR_PERSONALIZED_RANKING;
		command.setSortPage("ranking");
		command.setSortPageOrder("desc");
		command.setPersonalized(true);
		command.setDuplicates("no");
		
		// fetch all tags of logged-in user
		final String username = command.getContext().getLoginUser().getName();
		final List<Tag> loginUserTags = this.logic.getTags(Resource.class, GroupingEntity.USER, username, null, null, null, null, 0, Integer.MAX_VALUE, null, null);
		
		// fetch all tags of followed users TODO implement...
		final List<Tag> targetUserTags = this.logic.getTags(Resource.class, GroupingEntity.USER, username, null, null, null, null, 0, Integer.MAX_VALUE, null, null);		
		
		// retrieve and set the requested resource lists, along with total
		// counts
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			final ListCommand<?> listCommand = command.getListCommand(resourceType);
						
			final int origEntriesPerPage = listCommand.getEntriesPerPage();
			final int origStart = listCommand.getStart();
			listCommand.setStart(start);
			this.setList(command, resourceType, groupingEntity, groupingName, null, null, null, null, null, entriesPerPage);
			listCommand.setEntriesPerPage(origEntriesPerPage);
			listCommand.setStart(origStart);
										
			// compute the ranking for each post in the list
			RankingUtil.computeRanking(loginUserTags, targetUserTags, command.getListCommand(resourceType).getList(), command.getRanking().getMethodObj(), command.getRanking().getNormalize());

			// post-process & sort
			this.postProcessAndSortList(command, resourceType);

			// show only the top ranked resources for each resource type
			if (command.getListCommand(resourceType).getList().size() > origEntriesPerPage) {
				this.restrictResourceList(command, resourceType, listCommand.getStart(), listCommand.getStart() + origEntriesPerPage);				
			}
			
			// set total count
			//this.setTotalCount(command, resourceType, groupingEntity, null, null, null, null, null, null, origEntriesPerPage, null);
		}		
		

		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			command.setFollowersOfUser(logic.getUsers(null, GroupingEntity.FOLLOWER, null, null, null, null, UserRelation.FOLLOWER_OF, null, 0, 0));
			command.setUserIsFollowing(logic.getUsers(null, GroupingEntity.FOLLOWER, null, null, null, null, UserRelation.OF_FOLLOWER, null, 0, 0));

			// retrieve similar users, by the given user similarity measure
			final List<User> similarUsers = this.logic.getUsers(null, GroupingEntity.USER, username, null, null, null, userRelation, null, 0, 10);	
			command.getRelatedUserCommand().setRelatedUsers(similarUsers);			
			
			this.endTiming();
			return Views.FOLLOWERS;
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);
	}

	@Override
	public FollowersViewCommand instantiateCommand() {
		return new FollowersViewCommand();
	}
}
