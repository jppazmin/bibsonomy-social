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
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.database.systemstags.search.NetworkRelationSystemTag;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.util.EnumUtils;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.UserResourceViewCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for user pages /user/USERNAME
 * 
 * @author Dominik Benz
 * @version $Id: UserPageController.java,v 1.49 2011-06-23 09:15:26 folke Exp $
 */
public class UserPageController extends SingleResourceListControllerWithTags implements MinimalisticController<UserResourceViewCommand> {
	private static final int THRESHOLD = 20000; // TODO: move constant to a more general class (other controller also need this value)
	private static final Log LOGGER = LogFactory.getLog(UserPageController.class);
	
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
		
		// set grouping entity, grouping name, tags, userSimilarity
		final GroupingEntity groupingEntity = GroupingEntity.USER;
		
		final List<String> requTags = command.getRequestedTagsList();
		final UserRelation userRelation = EnumUtils.searchEnumByName(UserRelation.values(), command.getUserSimilarity());
		
		// wrong user similarity requested -> error
		if (!present(userRelation)) {
			throw new MalformedURLSchemeException("error.user_page_with_wrong_user_similarity");			
		}
		
		/*
		 * handle case when only tags are requested
		 */
		this.handleTagsOnly(command, groupingEntity, groupingName, null, requTags, null, Integer.MAX_VALUE, null);
		
		/*
		 * if user is logged in, check if the logged in user follows the requested user
		 */
		final RequestWrapperContext context = command.getContext();
		if (context.isUserLoggedIn()) {
			final List<User> followersOfUser = this.logic.getUsers(null, GroupingEntity.FOLLOWER, null, null, null, null, UserRelation.FOLLOWER_OF, null, 0, 0);
			for (final User u : followersOfUser){
				if (u.getName().equals(groupingName)) {
					command.setFollowerOfUser(true);
					break;
				}
			}
		}
		
		/*
		 * extract filter
		 */
		final FilterEntity filter = getFilter(command.getFilter());
		if (FilterEntity.JUST_PDF.equals(filter) || FilterEntity.DUPLICATES.equals(filter)) {
			this.supportedResources.remove(Bookmark.class);
		}
		
		// "redirect" to user-user-page controller if requested
		// TODO: better to this via Spring URL mapping
		if (context.isUserLoggedIn() && command.isPersonalized()) {
			final UserUserPageController uupc = new UserUserPageController();
			uupc.supportedResources = this.supportedResources;
			uupc.logic = this.logic;
			uupc.userSettings = this.userSettings;
			return uupc.workOn(command);
		}		

		int totalNumPosts = 1;

		// retrieve and set the requested resource lists, along with total
		// counts
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			final ListCommand<?> listCommand = command.getListCommand(resourceType);
			final int entriesPerPage = listCommand.getEntriesPerPage();
			
			this.setList(command, resourceType, groupingEntity, groupingName, requTags, null, null, filter, null, entriesPerPage);
			this.postProcessAndSortList(command, resourceType);

			/*
			 * set the post counts
			 */
			if (filter != FilterEntity.JUST_PDF && filter != FilterEntity.DUPLICATES) {
				this.setTotalCount(command, resourceType, groupingEntity, groupingName, requTags, null, null, filter, null, entriesPerPage, null);
				totalNumPosts += listCommand.getTotalCount();
			}
		}

		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			// set page title
			command.setPageTitle("user :: " + groupingName); // TODO: i18n
			if (present(requTags)) {
				// add the tags to the title
				command.setPageTitle(command.getPageTitle() + " :: " + StringUtils.implodeStringCollection(requTags, "+"));
			}
			
			// only fetch tags if they were not already fetched by handleTagsOnly
			if (command.getTagstype() == null) {
				this.setTags(command, Resource.class, groupingEntity, groupingName, null, null, null, Integer.MAX_VALUE, null);
			}

			// retrieve concepts
			final List<Tag> concepts = this.logic.getConcepts(null, groupingEntity, groupingName, null, null, ConceptStatus.PICKED, 0, Integer.MAX_VALUE);
			command.getConcepts().setConceptList(concepts);
			command.getConcepts().setNumConcepts(concepts.size());

			// log if a user has reached threshold
			if (command.getTagcloud().getTags().size() >= THRESHOLD) {
				LOGGER.debug("User " + groupingName + " has reached threshold of " + THRESHOLD + " tags on user page");
			}
			
			// retrieve similar users, by the given user similarity measure
			final List<User> similarUsers = this.logic.getUsers(null, GroupingEntity.USER, groupingName, null, null, null, userRelation, null, 0, 10);	
			command.getRelatedUserCommand().setRelatedUsers(similarUsers);
			
			if (present(requTags)) {
				this.setRelatedTags(command, Resource.class, groupingEntity, groupingName, null, requTags, Order.ADDED, 0, 20, null);
				command.getRelatedTagCommand().setTagGlobalCount(totalNumPosts);
				this.endTiming();

				// forward to publication page if filter is set
				if (FilterEntity.JUST_PDF.equals(filter) || FilterEntity.DUPLICATES.equals(filter)) {
					return Views.USERDOCUMENTPAGE;
				}
				
				// get the information needed for the sidebar
				command.setConceptsOfRequestedUser(this.getConceptsForSidebar(command, GroupingEntity.USER, groupingName, requTags));
				command.setConceptsOfAll(this.getConceptsForSidebar(command, GroupingEntity.ALL, null, requTags));
				command.setPostCountForTagsForAll(this.getPostCountForSidebar(GroupingEntity.ALL, "", requTags));
				
				return Views.USERTAGPAGE;
			}

			/*
			 * For logged users we check, if she is in a friends or group relation
			 * with the requested user. 
			 */
			final String loginUserName = context.getLoginUser().getName();
			if (context.isUserLoggedIn()) {
				/*
				 * Put the user into command to be able to show some details.
				 * 
				 * The DBLogic checks, if the login user may see the user's 
				 * details. 
				 */
				final User requestedUser = logic.getUserDetails(groupingName);
				command.setUser(requestedUser);
				/*
				 * Has loginUser this user set as friend?
				 */
				command.setOfFriendUser(logic.getUserRelationship(loginUserName, UserRelation.OF_FRIEND, NetworkRelationSystemTag.BibSonomyFriendSystemTag).contains(requestedUser));
				command.setFriendOfUser(logic.getUserRelationship(loginUserName, UserRelation.FRIEND_OF, NetworkRelationSystemTag.BibSonomyFriendSystemTag).contains(requestedUser));
				/*
				 * TODO: we need an adminLogic to access the requested user's groups ...
				 */
			}
			
			this.endTiming();

			// forward to bibtex page if filter is set
			if (filter == FilterEntity.JUST_PDF || filter == FilterEntity.DUPLICATES) {
				return Views.USERDOCUMENTPAGE;
			} 
			
			return Views.USERPAGE;
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);
	}

	/**
	 * Maps a filter string to the corresponding filter enum.
	 * 
	 * @param filterString
	 * @return
	 */
	private FilterEntity getFilter(final String filterString) {
		if ("myPDF".equals(filterString)) {
			/*
			 * display only posts which have a document attached
			 */
			return FilterEntity.JUST_PDF;
 		} else if ("myDuplicates".equals(filterString)) {
			/*
			 * display duplicate entries
			 */
			return FilterEntity.DUPLICATES;
		}
		return null;
	}

	@Override
	public UserResourceViewCommand instantiateCommand() {
		return new UserResourceViewCommand();
	}
}
