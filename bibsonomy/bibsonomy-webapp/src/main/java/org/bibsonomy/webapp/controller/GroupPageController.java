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
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.markup.RelevantForSystemTag;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.webapp.command.GroupResourceViewCommand;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for Grouppages
 * 
 * group/GROUP and group/GROUP/TAGS
 * 
 * @author Stefan Stuetzer
 * @version $Id: GroupPageController.java,v 1.44 2011-01-26 10:10:03 dbe Exp $
 */
public class GroupPageController extends SingleResourceListControllerWithTags implements MinimalisticController<GroupResourceViewCommand> {
	private static final Log log = LogFactory.getLog(GroupPageController.class);

	@Override
	public View workOn(GroupResourceViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		final String groupingName = command.getRequestedGroup();
		
		// if no group given -> error
		if (!present(groupingName)) {
			throw new MalformedURLSchemeException("error.group_page_without_groupname");
		}				

		// set grouping entity and grouping name
		final GroupingEntity groupingEntity = GroupingEntity.GROUP;
		final List<String> requTags = command.getRequestedTagsList();

		//check if system-tag "sys:relevantFor:" exists in taglist
		final boolean isRelevantFor = SystemTagsUtil.containsSystemTag(requTags, RelevantForSystemTag.NAME);
		
		// handle case when only tags are requested
		this.handleTagsOnly(command, groupingEntity, groupingName, null, requTags , null, Integer.MAX_VALUE, null);

		// special group given - return empty page
		if (GroupID.isSpecialGroup(groupingName)) return Views.GROUPPAGE;
		
		FilterEntity filter = null;

		// display only posts, which have a document attached
		if ("myGroupPDF".equals(command.getFilter())) {
			filter = FilterEntity.JUST_PDF;
			this.supportedResources.remove(Bookmark.class);
		}
		
		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {			
			final ListCommand<?> listCommand = command.getListCommand(resourceType);
			final int entriesPerPage = listCommand.getEntriesPerPage();
			this.setList(command, resourceType, groupingEntity, groupingName, requTags, null, null, filter, null, entriesPerPage);
			this.postProcessAndSortList(command, resourceType);

			// retrieve resource counts, if no tags are given
			if (requTags.size() == 0 && filter != FilterEntity.JUST_PDF) { 
				this.setTotalCount(command, resourceType, groupingEntity, groupingName, requTags, null, null, null, null, entriesPerPage, null);
			}
		}

		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			if (isRelevantFor && filter != FilterEntity.JUST_PDF) {
				/*
				 * handle the "relevant for group" pages
				 */
				command.setPageTitle("relevant for :: " + groupingName); // TODO: i18n
				this.setRelatedTags(command, Resource.class, groupingEntity, groupingName, null, requTags, Order.ADDED, 0, 20, null);
				this.endTiming();
				return Views.RELEVANTFORPAGE;
			} 

			// set title
			command.setPageTitle("group :: " + groupingName); // TODO: i18n

			// always retrieve all tags of this group
			// only fetch tags if they were not already fetched by handleTagsOnly
			if (command.getTagstype() == null) {
				this.setTags(command, Resource.class, groupingEntity, groupingName, null, null, null, Integer.MAX_VALUE, null);
			}
			this.setGroupDetails(command, groupingName);

			if (requTags.size() > 0) {
				this.setRelatedTags(command, Resource.class, groupingEntity, groupingName, null, requTags, Order.ADDED, 0, 20, null);
			}
			
			this.endTiming();

			// forward to bibtex page if PDF filter is set
			if (filter == FilterEntity.JUST_PDF) {
				return Views.GROUPDOCUMENTPAGE;
			} else if (requTags.size() > 0) {
				/*
				 * get the information on tags and concepts for the sidebar
				 */
				command.setConceptsOfGroup(this.getConceptsForSidebar(command, GroupingEntity.GROUP, groupingName, requTags));
				command.setConceptsOfAll(this.getConceptsForSidebar(command, GroupingEntity.ALL, null, requTags));
				command.setPostCountForTagsForAll(this.getPostCountForSidebar(GroupingEntity.ALL, "", requTags));
				
				return Views.GROUPTAGPAGE;	
			} 

			return Views.GROUPPAGE;		
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);		
	}

	@Override
	public GroupResourceViewCommand instantiateCommand() {
		return new GroupResourceViewCommand();
	}

	/**
	 * Retrieve all members of the given group in dependence of the group privacy level
	 * FIXME: duplicated in ViewablePageController!
	 * @param cmd the command
	 * @param groupName the name of the group
	 */
	private void setGroupDetails(final GroupResourceViewCommand cmd, final String groupName) {
		final Group group = this.logic.getGroupDetails(groupName);
		if (present(group)) {
			group.setUsers(this.logic.getUsers(null, GroupingEntity.GROUP, groupName, null, null, null, null, null, 0, 1000));
		}
		cmd.setGroup(group);
	}

}