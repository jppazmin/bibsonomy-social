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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.ResultList;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.SearchViewCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for search page
 * 
 * @author Beate Krause
 * @version $Id: SearchPageController.java,v 1.25 2010-11-17 10:55:34 nosebrain Exp $
 */
public class SearchPageController extends SingleResourceListController implements MinimalisticController<SearchViewCommand> {
	private static final Log log = LogFactory.getLog(SearchPageController.class);
	
	/**
	 * We can restrict search to a group's or a user's posts ... here we list all
	 * supported grouping entities.
	 */
	private static final List<GroupingEntity> SUPPORTED_GROUPING_ENTITIES = Arrays.asList(GroupingEntity.USER, GroupingEntity.GROUP);
	
	@Override
	public View workOn(final SearchViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		
		this.startTiming(this.getClass(), format);
		
		// no search given -> error 
		if (!present(command.getRequestedSearch())){
			throw new MalformedURLSchemeException("error.search_page_without_search");	
		}
		
		String search = command.getRequestedSearch();
		GroupingEntity groupingEntity = GroupingEntity.ALL;
		String groupingName = null;
		
		//maximum number of displayed tags
		int maximumTags = Integer.MAX_VALUE;

		/*
		 * search only in a user's, group's, etc. posts ...
		 */
		for (final GroupingEntity groupingEnt : SUPPORTED_GROUPING_ENTITIES) {
			final String groupingEntString = groupingEnt.name().toLowerCase();
			
			if (search.matches(".*" + groupingEntString  + ":.*")) {
				groupingEntity = groupingEnt;
				
				// extract name of grouping entity
				int start = search.indexOf(groupingEntString + ":");
				int end  = search.indexOf(" ", start);
				if (end == -1) {
					end = search.length();
				} // if group:* is last word
				
				groupingName = search.substring(start + (groupingEntString + ":").length(), end);
				
				// warning if more than one group in search string
				if (search.replaceFirst(groupingEntString + ":[^\\s]*", "").matches(".*" + groupingEntString + ":.*")){
					throw new MalformedURLSchemeException("error.search_more_than_one_" + groupingEntString);
				}
				
				// replace all occurences of "group:*"
				search = search.replaceAll(groupingEntString + ":[^\\s]*", "").trim();
				
				// don't search for other grouping entities
				break;
			}
		}
		
		// if grouping entity set to GroupingEntity.ALL, database only allows 1000 tags maximum
		if (groupingEntity.equals(GroupingEntity.ALL)) {
			maximumTags = 1000;
		}
		
		final List<String> requestedTags = command.getRequestedTagsList();
		
		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			this.setList(command, resourceType, groupingEntity, groupingName, requestedTags, null, null, null, search, command.getListCommand(resourceType).getEntriesPerPage());

			final ListCommand<?> listCommand = command.getListCommand(resourceType);
			final List<?> list = listCommand.getList();

			if (list instanceof ResultList<?>) {
				@SuppressWarnings("unchecked")
				final ResultList<Post<?>> resultList = (ResultList<Post<?>>) list;
				listCommand.setTotalCount(resultList.getTotalCount()); 
				log.debug("resultList.getTotalCount() = " + resultList.getTotalCount());
			}			
			
			this.postProcessAndSortList(command, resourceType);
		}
		
		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			command.setPageTitle("search"); // TODO: i18n
			// fill the tag cloud with all tag assignments of the relevant documents
			this.setTags(command, Resource.class, groupingEntity, groupingName, null, null, null, maximumTags, search);
			this.endTiming();
			return Views.SEARCHPAGE;			
		}
		
		this.endTiming();
		
		// export - return the appropriate view
		return Views.getViewByFormat(format);		
	}

	@Override
	public SearchViewCommand instantiateCommand() {
		return new SearchViewCommand();
	}

}
