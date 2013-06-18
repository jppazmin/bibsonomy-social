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
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.RelatedUserCommand;
import org.bibsonomy.webapp.command.TagResourceViewCommand;
import org.bibsonomy.webapp.config.Parameters;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;


/**
 * Controller for tag pages
 * /tag/TAGNAME
 * 
 * @author Michael Wagner
 * @version $Id: TagPageController.java,v 1.35 2010-11-17 10:55:35 nosebrain Exp $
 */
public class TagPageController extends SingleResourceListControllerWithTags implements MinimalisticController<TagResourceViewCommand>{
	private static final Log log = LogFactory.getLog(TagPageController.class);
	
	@Override
	public View workOn(final TagResourceViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);
		
		// if no tags given return
		if (!present(command.getRequestedTags())) {
			throw new MalformedURLSchemeException("error.tag_page_without_tag");
		}
		
		final List<String> requTags = command.getRequestedTagsList();
		
		// count number of non system tags
		final int tagCount = SystemTagsUtil.countNonSystemTags(requTags); 
		
		// handle case when only tags are requested
		// FIXME we can only retrieve 1000 tags here
		this.handleTagsOnly(command, GroupingEntity.ALL, null, null, requTags, null, 1000, null);
		
		// requested order
		Order order = Order.ADDED;
		try {
			order = Order.getOrderByName(command.getOrder());
		} catch (final IllegalArgumentException ex) {
			// TODO: why rethrowing the exception and not just don't catch it?
			throw new MalformedURLSchemeException(ex.getMessage());
		}

		// get the information on tags and concepts needed for the sidebar
		command.setConceptsOfAll(this.getConceptsForSidebar(command, GroupingEntity.ALL, null, requTags));
		final String loginUser = command.getContext().getLoginUser().getName();
		if (present(loginUser)) {
			command.setConceptsOfLoginUser(this.getConceptsForSidebar(command, GroupingEntity.USER, loginUser, requTags));
			command.setPostCountForTagsForLoginUser(this.getPostCountForSidebar(GroupingEntity.USER, loginUser, requTags));
		}
		
		int totalNumPosts = 1; 
		
		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {			
			final ListCommand<?> listCommand = command.getListCommand(resourceType);
			final int entriesPerPage = listCommand.getEntriesPerPage();

			this.setList(command, resourceType, GroupingEntity.ALL, null, requTags, null, order, null, null, entriesPerPage);
			this.postProcessAndSortList(command, resourceType);
			
			this.setTotalCount(command, resourceType, GroupingEntity.ALL, null, requTags, null, null, null, null, entriesPerPage, null);
			totalNumPosts += listCommand.getTotalCount();
		}	
		
		/*
		 *  if order = folkrank - retrieve related users
		 *  
		 *  TODO: in practice, this is (currently) only neccessary for HTML and SWRC. The related
		 *  users will be ignored by all other views.
		 *   
		 *  (burst, publrss, swrc) related pages
		 */
		if (order.equals(Order.FOLKRANK)) {
			this.setRelatedUsers(command, GroupingEntity.ALL, requTags, order, UserRelation.FOLKRANK, 0, Parameters.NUM_RELATED_USERS);
		}
		
		// html format - retrieve related tags and return HTML view
		if ("html".equals(format)) {
			command.setPageTitle("tag :: " + StringUtils.implodeStringCollection(requTags, " "));		
			if (tagCount > 0) {
				this.setRelatedTags(command, Resource.class, GroupingEntity.ALL, null, null, requTags, order, 0, Parameters.NUM_RELATED_TAGS, null);
			}
			// similar tags only make sense for a single requested tag
			if (tagCount == 1) {
				this.setSimilarTags(command, Resource.class, GroupingEntity.ALL, null, null, requTags, order, 0, Parameters.NUM_RELATED_TAGS, null);
			}
			// set total nr. of posts 
			command.getRelatedTagCommand().setTagGlobalCount(totalNumPosts);
			this.endTiming();
			return Views.TAGPAGE;
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);
		
	}
	
	@Override
	public TagResourceViewCommand instantiateCommand() {
		return new TagResourceViewCommand();
	}
		
	/**
	 * retrieve related user by tag
	 * 
	 * @param cmd
	 * @param tags
	 * @param order
	 * @param start
	 * @param end
	 */
	protected void setRelatedUsers(final TagResourceViewCommand cmd, final GroupingEntity grouping, final List<String> tags, final Order order, final UserRelation relation, final int start, final int end) {
		final RelatedUserCommand relatedUserCommand = cmd.getRelatedUserCommand();
		relatedUserCommand.setRelatedUsers(this.logic.getUsers(null, grouping, null, tags, null, order, relation, null, start, end));
	}
	
}
