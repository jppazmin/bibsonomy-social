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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.TagCloudCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for popular tags page /tags
 * 
 * @author Stefan Stützer
 * @version $Id: PopularTagsPageController.java,v 1.4 2010-11-17 10:55:35 nosebrain Exp $
 */
public class PopularTagsPageController implements MinimalisticController<TagCloudCommand> {
	private static final Log log = LogFactory.getLog(PopularTagsPageController.class);

	private LogicInterface logic;
	
	@Override
	public View workOn(TagCloudCommand command) {
		log.debug(this.getClass().getSimpleName());
		
		/* set title */
		command.setPageTitle("Tags"); // TODO: i18n
		
		/* fill command with tags */
		command.setTags(this.logic.getTags(Resource.class, GroupingEntity.ALL, null, null, null, null, Order.POPULAR, 0, 100, null, null));
		
		return Views.POPULAR_TAGS;
	}

	@Override
	public TagCloudCommand instantiateCommand() {
		return new TagCloudCommand();
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(LogicInterface logic) {
		this.logic = logic;
	}
}
