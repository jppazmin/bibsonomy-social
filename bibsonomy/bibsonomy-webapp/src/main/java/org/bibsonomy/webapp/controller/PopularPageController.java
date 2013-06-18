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

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.search.DaysSystemTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.webapp.command.MultiResourceViewCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for the popular page:
 * 
 * @author mwa
 * @version $Id: PopularPageController.java,v 1.18 2010-11-17 10:55:34 nosebrain Exp $
 */
public class PopularPageController extends MultiResourceListController implements MinimalisticController<MultiResourceViewCommand>{
	private static final Log log = LogFactory.getLog(PopularPageController.class);
	
	private static final int MAX_TAGS = 50;
	
	
	protected Integer entriesPerPage;
	
	/**
	 * @return the entriesPerPage
	 */
	public Integer getEntriesPerPage() {
		return this.entriesPerPage;
	}

	/**
	 * @param entriesPerPage the entriesPerPage to set
	 */
	public void setEntriesPerPage(final Integer entriesPerPage) {
		this.entriesPerPage = entriesPerPage;
	}

	@Override
	public View workOn(final MultiResourceViewCommand command) {
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);
				
		// set the grouping entity and the order
		final GroupingEntity groupingEntity = GroupingEntity.ALL;
		final Order order = Order.POPULAR;
		
		// the start parameter for OFFSET
		int begin = 0; 
		
		// the value of the field 'popular_days' in the database
		int days = 0;

		do {
			for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
				// build day systemtag
				final List<String> tags = Collections.singletonList(SystemTagsUtil.buildSystemTagString(DaysSystemTag.NAME, begin));
				// determine the value of popular days, e.g. the last 10 days
				days = this.logic.getPostStatistics(resourceType, groupingEntity, null, tags, null, order, null, 0, this.getEntriesPerPage(), null, null);
				
				// only retrieve and set the requested resource lists if days > 0
				// because otherwise the lists will be empty
				if (days > 0) {
					// retrieve and set the requested resource lists
					this.addList(command, resourceType, groupingEntity, null, tags, null, order, null, null, this.getEntriesPerPage());
					// FIXME: do this only once outside the "days"-loop
					this.postProcessAndSortList(command, resourceType);
					this.addDescription(command, resourceType, days + "");
				}
			}
			
			begin++;
		} while (days > 0 && begin < 10); // show max 10 entries

		// only html format, exports are not possible atm 
		this.setTags(command, Resource.class, groupingEntity, null, null, null, null, MAX_TAGS, null);
		command.setPageTitle("popular"); // TODO: i18n
		
		this.endTiming();
		return Views.POPULAR;
	}
	
	@Override
	protected int getFixedTagMax(final int tagMax) {
		return MAX_TAGS;
	}
	
	@Override
	public MultiResourceViewCommand instantiateCommand() {
		return new MultiResourceViewCommand();
	}

}
