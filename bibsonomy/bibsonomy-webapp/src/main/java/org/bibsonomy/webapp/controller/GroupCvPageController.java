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

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.systemstags.markup.MyOwnSystemTag;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.GroupResourceViewCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
/**
 * Controller for the "group cv page":
 * - /cv/group/GROUPNAME
 * 
 * @author wla
 * @version $Id: GroupCvPageController.java,v 1.7 2011-08-03 15:10:40 doerfel Exp $
 */
public class GroupCvPageController extends ResourceListController implements MinimalisticController<GroupResourceViewCommand>{
	
	/**
	 * the count of publications and bookmarks to show
	 */
	private int entries;
	
	@Override
	public View workOn(final GroupResourceViewCommand command) {
		final String requestedGroup = command.getRequestedGroup();
		final Group group = this.logic.getGroupDetails(requestedGroup);
		if (!present(group)) {
			return Views.ERROR404;
		}
		final GroupingEntity groupingEntity = GroupingEntity.GROUP;
		command.setDuplicates("no");
		
		command.setPageTitle("Curriculum vitae"); // TODO: i18n
		
		final List<User> groupUsers = this.logic.getUsers(null, groupingEntity, requestedGroup, null, null, null, null, null, 0, 1000);
		group.setUsers(groupUsers);
		command.setGroup(group);
		
		/*
		 * create tag list
		 */
		this.setTags(command, Resource.class, groupingEntity, requestedGroup, null, null, null, Integer.MAX_VALUE, null);
		
		/*
		 *  retrieve and set the requested resource lists
		 */
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(command.getFormat(), command.getResourcetype())) {
			this.setList(command, resourceType, groupingEntity, requestedGroup, Collections.singletonList(MyOwnSystemTag.NAME), null, null, null, null, entries);
		}
		
		/*
		 * remove duplicated posts 
		 */
		postProcessAndSortList(command, command.getBibtex().getList());
		
		return Views.GROUPCVPAGE;
	}
	
	@Override
	public GroupResourceViewCommand instantiateCommand() {
		return new GroupResourceViewCommand();
	}
	
	/**
	 * @return the entries
	 */
	public int getEntries() {
		return this.entries;
	}

	/**
	 * @param entries the count of publications and bookmarks to show
	 */
	public void setEntries(final int entries) {
		this.entries = entries;
	}

}
