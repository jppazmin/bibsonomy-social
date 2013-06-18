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

package org.bibsonomy.webapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.webapp.command.GroupingCommand;

/**
 * utils class for grouping command
 * 
 * @author dzo
 * @version $Id: GroupingCommandUtils.java,v 1.3 2011-05-28 14:34:22 nosebrain Exp $
 */
public abstract class GroupingCommandUtils {
	private static final Log log = LogFactory.getLog(GroupingCommandUtils.class);
	
	private static final Group PUBLIC_GROUP = GroupUtils.getPublicGroup();
	private static final Group PRIVATE_GROUP = GroupUtils.getPrivateGroup();
	
	/**
	 * TODO
	 */
	public static final String OTHER_ABSTRACT_GROUPING = "other";
	
	/**
	 * inits the command
	 * @param command the command to init
	 */
	public static void initGroupingCommand(final GroupingCommand command) {
		command.setAbstractGrouping(PUBLIC_GROUP.getName());
		command.setGroups(new ArrayList<String>());
	}
	
	/**
	 * Copy the groups from the command into provided groups set (make proper groups from
	 * them)
	 * 
	 * @param command -
	 *            contains the groups as represented by the form fields.
	 * @param groupsToInit -
	 *            the groups should be populated from the command.
	 */
	public static void initGroups(final GroupingCommand command, final Set<Group> groupsToInit) {
		log.debug("initializing groups from command");
		/*
		 * we can avoid some checks here, because they're done in the validator
		 * ...
		 */
		final String abstractGrouping = command.getAbstractGrouping();
		if (OTHER_ABSTRACT_GROUPING.equals(abstractGrouping)) {
			log.debug("found 'other' grouping");
			/*
			 * copy groups into post
			 */
			final List<String> groups = command.getGroups();
			log.debug("groups in command: " + groups);
			for (final String groupname : groups) {
				groupsToInit.add(new Group(groupname));
			}
			log.debug("groups: " + groupsToInit);
		} else {
			log.debug("public or private post");
			/*
			 * if the post is private or public --> remove all groups and add
			 * one (private or public)
			 */
			groupsToInit.clear();
			groupsToInit.add(new Group(abstractGrouping));
		}
	}

	/**
	 * Populates the helper attributes of the command with the groups from the
	 * post.
	 * 
	 * @param command -
	 *            the command whose groups should be populated 
	 * @param groups -
	 *            the groups.
	 * @see #initGroups(GroupingCommand, Set)
	 */
	public static void initCommandGroups(final GroupingCommand command, final Set<Group> groups) {
		log.debug("given groups: " + groups);
		final List<String> commandGroups = command.getGroups();
		commandGroups.clear();
		if (groups.contains(PRIVATE_GROUP)) {
			/*
			 * only private
			 */
			command.setAbstractGrouping(PRIVATE_GROUP.getName());
		} else if (groups.contains(PUBLIC_GROUP)) {
			/*
			 * only public
			 */
			command.setAbstractGrouping(PUBLIC_GROUP.getName());
		} else {
			/*
			 * other
			 */
			command.setAbstractGrouping(OTHER_ABSTRACT_GROUPING);
			/*
			 * copy groups into command
			 */
			for (final Group group : groups) {
				commandGroups.add(group.getName());
			}
		}
		log.debug("abstractGrouping: " + command.getAbstractGrouping());
		log.debug("commandGroups: " + command.getGroups());
	}
}
