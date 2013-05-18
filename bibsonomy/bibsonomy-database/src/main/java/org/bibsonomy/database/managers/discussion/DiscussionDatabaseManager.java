/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.database.managers.discussion;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.discussion.DiscussionChain;
import org.bibsonomy.database.params.discussion.DiscussionItemParam;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.User;
import org.bibsonomy.model.util.UserUtils;

/**
 * @author dzo
 * @version $Id: DiscussionDatabaseManager.java,v 1.2 2011-07-06 16:43:37 nosebrain Exp $
 */
public class DiscussionDatabaseManager extends AbstractDatabaseManager {
	private static final DiscussionDatabaseManager INSTANCE = new DiscussionDatabaseManager();


	private static final DiscussionChain CHAIN = new DiscussionChain();
	
	/**
	 * @return the @{link:DiscussionManager} instance
	 */
	public static DiscussionDatabaseManager getInstance() {
		return INSTANCE;
	}

	private DiscussionDatabaseManager() {
		// noop
	}
	
	/**
	 * @param loginUser the login user (to get groups of the user)
	 * @param interHash 
	 * @param session
	 * @return a list of discussion items
	 */
	public List<DiscussionItem> getDiscussionSpace(final User loginUser, final String interHash, final DBSession session) {
		final DiscussionItemParam<?> param = new DiscussionItemParam<DiscussionItem>();
		param.setInterHash(interHash);
		param.setUserName(loginUser.getName());
		param.addGroupsAndGroupnames(UserUtils.getListOfGroups(loginUser));
		
		/*
		 * get the list of discussion items
		 */
		return CHAIN.getFirstElement().perform(param, session);
	}
	
	/**
	 * 
	 * @param interHash
	 * @param loginUser 
	 * @param visibleGroupIDs 
	 * @param session
	 * @return all resources for the specific resource
	 */
	public List<DiscussionItem> getDiscussionSpaceForResource(final String interHash, final String loginUser, final List<Integer> visibleGroupIDs, final DBSession session) {
		final DiscussionItemParam<DiscussionItem> param = this.createDiscussionParam(interHash, loginUser);
		param.setGroups(visibleGroupIDs);
		param.setInterHash(interHash);
		
		// TODO: maybe we should query here for a map (item.hash => item)
		return this.buildThreadStructure(this.queryForList("getDiscussionSpaceForResource", param, DiscussionItem.class, session));
	}
	
	private DiscussionItemParam<DiscussionItem> createDiscussionParam(final String interHash, final String userName) {
		final DiscussionItemParam<DiscussionItem> param = new DiscussionItemParam<DiscussionItem>();
		param.setUserName(userName);
		param.setInterHash(interHash);
		return param;
	}

	protected List<DiscussionItem> buildThreadStructure(final List<DiscussionItem> discussionItems) {
		/*
		 * build thread structure for discussion items
		 * 
		 * 1.) build a map discussionItem.hash => discussionItem
		 */
		final Map<String, DiscussionItem> dicussionItemsMap = new HashMap<String, DiscussionItem>();
		for (final DiscussionItem discussionItem : discussionItems) {
			dicussionItemsMap.put(discussionItem.getHash(), discussionItem);
		}
		
		/*
		 * 2.) loop through all discussion items and find roots (no parentHash)
		 * and add all sub items to its parent
		 */
		final List<DiscussionItem> rootItems = new LinkedList<DiscussionItem>();
		for (final DiscussionItem discussionItem : discussionItems) {
			final String parentHash = discussionItem.getParentHash();
			if (!present(parentHash)) {
				/*
				 *  no parentHash => a root discussion item
				 */
				rootItems.add(discussionItem);
			} else {
				final DiscussionItem parentItem;
				
				/*
				 * no parent => maybe deleted or invisible for the user
				 */
				if (!dicussionItemsMap.containsKey(parentHash)) {
					// we don't know which item it is
					parentItem = new DiscussionItem();
					parentItem.setHash(parentHash);
					dicussionItemsMap.put(parentHash, parentItem);
				} else {
					 parentItem = dicussionItemsMap.get(parentHash);
				}
				
				parentItem.addToDiscussionItems(discussionItem);
			}
		}
		
		/*
		 * we want that root items are sorted by date descending but sub items
		 * sorted ascending
		 * root items are currently sorted ascending (by sql query) so we only
		 * need to reverse them
		 */
		Collections.reverse(rootItems);
		return rootItems;
	}
}
