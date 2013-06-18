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

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.HomepageCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for Homepage
 *
 * @author Dominik Benz
 * @version $Id: HomepageController.java,v 1.32 2010-11-17 10:55:35 nosebrain Exp $
 */
public class HomepageController extends SingleResourceListController implements MinimalisticController<HomepageCommand> {
	private static final Log log = LogFactory.getLog(HomepageController.class);

	/*
	 * on the homepage, only 50 tags are shown in the tag cloud
	 */
	private static final int MAX_TAGS = 50;

	@Override
	public View workOn(final HomepageCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);
		
		// handle the case when only tags are requested
		this.handleTagsOnly(command, GroupingEntity.ALL, null, null, null, null, MAX_TAGS, null);
		
		// retrieve and set the requested resource lists
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			// disable manual setting of start value for homepage
			command.getListCommand(resourceType).setStart(0);
			setList(command, resourceType, GroupingEntity.ALL, null, null, null, null, null, null, 20);
			postProcessAndSortList(command, resourceType);
		}
												
		// html format - retrieve tags and return HTML view
		if ("html".equals(format)) {
			command.setPageTitle("home"); // TODO: i18n
			setTags(command, Resource.class, GroupingEntity.ALL, null, null, null, null, MAX_TAGS, null);
			
			/*
			 * add news posts (= latest blog posts) FIXME: make configurable
			 */
			command.setNews(this.logic.getPosts(Bookmark.class, GroupingEntity.GROUP, "kde", Arrays.asList("bibsonomynews"), null, null, null, 0, 3, null));
			this.endTiming();
			
			return Views.HOMEPAGE; // TODO: make configurable 
		}
		
		this.endTiming();
		// export - return the appropriate view
		return Views.getViewByFormat(format);	
	}

	/**
	 * Enforce maximal 50 tags on 
	 * @see org.bibsonomy.webapp.controller.ResourceListController#getFixedTagMax(int)
	 */
	@Override
	protected int getFixedTagMax(int tagMax) {
		return MAX_TAGS;
	}
	
	@Override
	public HomepageCommand instantiateCommand() {
		return new HomepageCommand();
	}
}
