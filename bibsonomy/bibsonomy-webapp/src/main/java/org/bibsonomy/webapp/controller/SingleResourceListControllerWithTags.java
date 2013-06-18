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

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.TagSimilarity;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.webapp.command.RelatedTagCommand;
import org.bibsonomy.webapp.command.TagResourceViewCommand;

/**
 * Convenience class to provide the functionality of setting related tags
 * to all controllers handling tags (e.g. userPageController, GroupPageController, ...)
 * 
 * @author Dominik Benz
 * @version $Id: SingleResourceListControllerWithTags.java,v 1.6 2010-11-17 10:55:35 nosebrain Exp $
 */
public class SingleResourceListControllerWithTags extends SingleResourceListController {
	
	/**
     * Retrieve a set of related tags to a list of given tags 
     * from the database logic and add them to the command object
     * 
	 * @param cmd the command
	 * @param resourceType the resource type
	 * @param groupingEntity the grouping entity
	 * @param groupingName the grouping name
	 * @param regex regular expression for tag filtering
	 * @param tags list of tags
	 * @param start start parameter
	 * @param end end parameter
	 **/
	protected void setRelatedTags(final TagResourceViewCommand cmd, Class<? extends Resource> resourceType, GroupingEntity groupingEntity, String groupingName, String regex, List<String> tags, Order order, int start, int end, String search) {
		final RelatedTagCommand relatedTagCommand = cmd.getRelatedTagCommand();
		relatedTagCommand.setRelatedTags(this.logic.getTags(resourceType, groupingEntity, groupingName, regex, tags, null, order, start, end, search, null));		
	}
	
	/**
	 * Retrieve a set of similar tags
	 * 
	 * @param cmd
	 * @param resourceType
	 * @param groupingEntity
	 * @param groupingName
	 * @param regex
	 * @param tags
	 * @param order
	 * @param start
	 * @param end
	 * @param search
	 */
	protected void setSimilarTags(final TagResourceViewCommand cmd, Class<? extends Resource> resourceType, GroupingEntity groupingEntity, String groupingName, String regex, List<String> tags, Order order, int start, int end, String search) {
		final RelatedTagCommand similarTags = cmd.getSimilarTags();
		similarTags.setRelatedTags(this.logic.getTags(resourceType, groupingEntity, groupingName, regex, tags, null, order, start, end, search, TagSimilarity.COSINE));		
	}

}
