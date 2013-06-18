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

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.Resource;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.UrlCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for Urls 
 * 
 * <pre>
 * matches for urls like:
 * <ul>
 * 		<li>^/+url/*$ 
 *          --> error, need at least the url hash
 * 		<li>^/+url\?requestUrl=(.*)$
 *          --> send redirect
 * 		<li>^/+url/+([0-9a-fA-F]+)(\?(.*))?$
 *          --> should work as expected, maps to requestUrlHash
 * </ul>
 *
 * @author Flori
 * @version $Id: UrlPageController.java,v 1.23 2011-06-29 13:51:27 nosebrain Exp $
 */
public class UrlPageController extends SingleResourceListController implements MinimalisticController<UrlCommand> {
	private static final Log log = LogFactory.getLog(UrlPageController.class);

	@Override
	public View workOn(final UrlCommand command) {
		log.debug(this.getClass().getSimpleName());
		this.startTiming(this.getClass(), command.getFormat());

		final GroupingEntity groupingEntity;
		final String groupingName;

		if (present(command.getRequestedUser())) {
			/*
			 * handle /url/HASH/USER
			 */
			groupingEntity = GroupingEntity.USER;
			groupingName = command.getRequestedUser();
		} else {
			/*
			 * handle /url/HASH
			 */
			groupingEntity = GroupingEntity.ALL;
			groupingName = null;
		}

		// no URL hash given -> error
		final String requHash = command.getRequUrlHash();
		if (!present(command.getRequUrl()) && !present(requHash)) {
			throw new MalformedURLSchemeException("error.url_no_hash");
		}		

		// handle the case when only tags are requested
		command.setResourcetype(Collections.<Class<? extends Resource>>singleton(Bookmark.class));
		this.handleTagsOnly(command, groupingEntity, groupingName, null, null, requHash, 1000, null);
		
		// send redirect to /url/HASH
		if (present(command.getRequUrl())) {
			// TODO: add format in front of /url/? (probably not, this URL should only be called by input form)
			return new ExtendedRedirectView("/url/" + StringUtils.getMD5Hash(command.getRequUrl()));
		}

		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(command.getFormat(), command.getResourcetype())) {
			final ListCommand<?> listCommand = command.getListCommand(resourceType);
			final int entriesPerPage = listCommand.getEntriesPerPage();

			this.setList(command, resourceType, groupingEntity, groupingName, null, requHash, null, null, null, entriesPerPage);
			this.postProcessAndSortList(command, resourceType);

			this.setTotalCount(command, resourceType, groupingEntity, groupingName, null, requHash, null, null, null, entriesPerPage, null);
		}
		
		if (!present(command.getBookmark().getList())) {	
			/*
			 * We throw a ResourceNotFoundException such that we don't get empty
			 * resource pages.
			 */
			throw new ResourceNotFoundException(requHash);
		}

		/*
		 * build page title
		 */
		command.setPageTitle("url :: " + command.getBookmark().getList().get(0).getResource().getUrl());
		this.endTiming();

		// html format - retrieve tags and return HTML view
		if ("html".equals(command.getFormat())) {
			final List<DiscussionItem> discussionItems = this.logic.getDiscussionSpace(requHash);
			command.setDiscussionItems(discussionItems);
			
			// FIXME: here we assume, bookmarks are handled, further above we use listsToInitialize ...
			setTags(command, Bookmark.class, groupingEntity, groupingName, null, null, requHash, 1000, null);
			return Views.URLPAGE;	
		}

		// export - return the appropriate view
		return Views.getViewByFormat(command.getFormat());				
	}

	@Override
	public UrlCommand instantiateCommand() {
		return new UrlCommand();
	}
}
