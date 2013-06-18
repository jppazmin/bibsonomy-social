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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.TagCloudSort;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.search.UserRelationSystemTag;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.SphereResourceViewCommand;
import org.bibsonomy.webapp.command.TagCloudCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * controller responsible for the following pages:
 *      - /spheres
 * 		- /spheres/RELATION
 * 		- /spheres/RELATION/TAG
 * 
 * @author Nils Raabe, Folke Mitzlaff
 * @version $Id: SpheresPageController.java,v 1.1 2011-07-25 14:31:01 folke Exp $
 */
public class SpheresPageController extends SingleResourceListControllerWithTags implements MinimalisticController<SphereResourceViewCommand> {
	private static final Log log = LogFactory.getLog(SpheresPageController.class);

	private static final TagCloudSort TAG_CLOUD_SORT = TagCloudSort.ALPHA;
	private static final int TAG_CLOUD_MINFREQ = 3;
	private static final int TAG_CLOUD_SIZE = 25;
	
	@Override
	public View workOn(final SphereResourceViewCommand command) {
		final RequestWrapperContext context = command.getContext();
		if (!context.isUserLoggedIn()){
			throw new AccessDeniedException("please log in");
		}
		
		if (present(command.getRequestedUserRelation())) {
			// handle 
			//  - /spheres/RELATION
			//  - /spheres/RELATION/TAG
			log.debug("Displaying details for sphere '"+command.getRequestedUserRelation()+"'");
			return handleDetailsView(command, context.getLoginUser());
		}
		// handle 
		//  - /spheres
		log.debug("Displaying list of all spheres");
		return handleListView(command, context.getLoginUser());
		
	}


	/**
	 * display details for given sphere, filtering posts according to given tag
	 * 
	 * @param command the parameter object
	 * @param loginUser login user
	 * @return
	 */
	private View handleDetailsView(final SphereResourceViewCommand command, final User loginUser) {
		final String requestedUserRelation 		= command.getRequestedUserRelation();
		final String requestedTags				= command.getRequestedTags();
		final String format 					= command.getFormat();
		final List<String> requestedUserTags 	= command.getRequestedTagsList();
		final GroupingEntity groupingEntity 	= GroupingEntity.USER;
		final List<Post<Bookmark>> bookmarksPosts = new ArrayList<Post<Bookmark>>();
		final List<Post<BibTex>> bibTexPosts = new ArrayList<Post<BibTex>>();


		// if no Userrelation given -> error
		if (!present(requestedUserRelation)) {
			throw new MalformedURLSchemeException("error.group_page_without_groupname");
		}

		// get tagged friends
		final List<User> relatedUsers = this.logic.getUserRelationship(loginUser.getName(), UserRelation.OF_FRIEND, SystemTagsUtil.buildSystemTagString(UserRelationSystemTag.NAME, requestedUserRelation));

		// if no friends are in this relation -> error
		if (!present(relatedUsers)) {
			throw new MalformedURLSchemeException("error.no_friends_in_this_friendrelation");
		}

		// get all bookmarks and publication posts for the requested tag - if no tag given -> relationTags is an empty List
		final List<String> relationTags = new ArrayList<String>();
		if (present(requestedTags)) {
			relationTags.add(requestedTags);
		}

		// retrieve and set the requested resource lists and tags for every user
		for (final User user : relatedUsers) {
			final String userName = user.getName();
			bookmarksPosts.addAll(this.logic.getPosts(Bookmark.class, groupingEntity, userName, relationTags, null, Order.ADDED, null, 0, 20, null));
			bibTexPosts.addAll(this.logic.getPosts(BibTex.class, groupingEntity, userName, relationTags, null, Order.ADDED, null, 0, 20, null));
			/*
			 * FIXME: you are overriding each resource list and every tag list
			 * in every iteration of this loop! this means that you only get
			 * the posts and tags of the last user
			 * hint: chain element GetResourcesByTaggedUserRelation must be called!
			 */
			for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {			
				final ListCommand<?> listCommand = command.getListCommand(resourceType);
				final int entriesPerPage = listCommand.getEntriesPerPage();
				this.setList(command, resourceType, groupingEntity, userName, relationTags, null, null, null, null, entriesPerPage);
				this.postProcessAndSortList(command, resourceType);
			}

			this.setTags(command, Resource.class, groupingEntity, userName, null, requestedUserTags, null, 20, null);

			if (present(requestedUserTags)) {
				this.setRelatedTags(command, Resource.class, groupingEntity, userName, null, requestedUserTags, Order.ADDED, 0, 20, null);
			}
		}

		// Set all parameters in the command.	 
		// set page title TODO: i18n
		command.setPageTitle("taggedfriend :: " + requestedUserRelation);

		// set the related users
		command.setRelatedUsers(relatedUsers);

		// set the related bookmarks
		command.setBmPosts(bookmarksPosts);

		// set the related publications
		command.setBibPosts(bibTexPosts);

		return Views.SPHEREDETAILS;
	}


	@Override
	public SphereResourceViewCommand instantiateCommand() {
		return new SphereResourceViewCommand();
	}
	
	/**
	 * display list of all spheres for the given login user
	 * 
	 * TODO: limit number of spheres per page
	 * 
	 * @param command the parameter object
	 * @param loginUser login user
	 * @return
	 */
	private View handleListView(SphereResourceViewCommand command, User loginUser) {
		List<User> relatedUsers = this.logic.getUserRelationship(loginUser.getName(), UserRelation.OF_FRIEND, null);
		
		// XXX: we collect all information by hand - this should be done already
		//      in an appropriate database query and result mapping
		Map<String, Set<User>> spheres = new HashMap<String, Set<User>>();
		
		// loop over each related user and add to each sphere he/she 
		// belongs to (as given by the relation system tags)
		for (User relatedUser : relatedUsers) {
			for (Tag tag : relatedUser.getTags() ) {
				String relationName = null;
				if (SystemTagsUtil.isSystemTag(tag.getName(), UserRelationSystemTag.NAME)) {
					relationName = SystemTagsUtil.extractArgument(tag.getName());
				}
				if (present(relationName)) {
					if (!spheres.containsKey(relationName)) {
						spheres.put(relationName, new HashSet<User>());
					}
					// add user to the sphere given by the relation name
					Set<User> sphereUsers = spheres.get(relationName);
					sphereUsers.add(relatedUser);
				}
			}
		}
		command.setSpheres(spheres);
		
		// XXX: we collect all information by hand - this should be done already
		//      in an appropriate database query and result mapping
		Map<String, ListCommand<Post<Bookmark>>> spheresBMPosts = new HashMap<String, ListCommand<Post<Bookmark>>>();
		Map<String, ListCommand<Post<BibTex>>> spheresPBPosts = new HashMap<String, ListCommand<Post<BibTex>>>();
		Map<String, TagCloudCommand> spheresTagClouds = new HashMap<String, TagCloudCommand>();
		
		for (Entry<String,Set<User>> sphere : spheres.entrySet() ) {
			// get tag cloud for current sphere
			List<String> sphereTags = new ArrayList<String>();
			sphereTags.add(SystemTagsUtil.buildSystemTagString(UserRelationSystemTag.NAME, sphere.getKey()));
			
			// get bookmarks and publications for current sphere 
			List<Post<Bookmark>> bmPosts = logic.getPosts(Bookmark.class, GroupingEntity.FRIEND, loginUser.getName(), sphereTags, null, Order.ADDED, null, 0, 5, null);
			List<Post<BibTex>> pbPosts = logic.getPosts(BibTex.class, GroupingEntity.FRIEND, loginUser.getName(), sphereTags, null, Order.ADDED, null, 0, 5, null);
			
			// pack resource lists into resource list commands (for according jsps)
			ListCommand<Post<Bookmark>> bmListCommand = new ListCommand<Post<Bookmark>>(command);
			ListCommand<Post<BibTex>> pbListCommand = new ListCommand<Post<BibTex>>(command);
			
			bmListCommand.setList(bmPosts);
			pbListCommand.setList(pbPosts);
			
			// store posts into result map
			spheresBMPosts.put(sphere.getKey(), bmListCommand);
			spheresPBPosts.put(sphere.getKey(), pbListCommand);

			// set tag cloud
			List<Tag> aspectTagCloud= logic.getTags(Resource.class, GroupingEntity.FRIEND, loginUser.getName(), null, sphereTags, null, Order.FREQUENCY, 0, 25, null, null);
			final TagCloudCommand tagCloudCommand = new TagCloudCommand();
			tagCloudCommand.setMaxCount(TAG_CLOUD_SIZE);
			tagCloudCommand.setMinFreq(TAG_CLOUD_MINFREQ);
			tagCloudCommand.setSort(TAG_CLOUD_SORT);
			tagCloudCommand.setTags(aspectTagCloud);
			spheresTagClouds.put(sphere.getKey(), tagCloudCommand);
			
		}
		
		// fill command object
		command.setSpheresBMPosts(spheresBMPosts);
		command.setSpheresPBPosts(spheresPBPosts);
		command.setSpheresTagClouds(spheresTagClouds);
		
		// all done
		return Views.SPHERELIST;
	}	

}
