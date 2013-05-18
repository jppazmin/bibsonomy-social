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

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.managers.GeneralDatabaseManager;
import org.bibsonomy.database.params.discussion.DiscussionItemParam;
import org.bibsonomy.database.plugin.DatabasePluginRegistry;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.util.DiscussionItemUtils;
import org.bibsonomy.util.ReflectionUtils;

/**
 * the following statements should exist for the discussion item:
 *  - get<DISCUSSIONITEM>sByHashForResource
 *  - insert<DISCUSSIONITEM>
 * 
 * @author dzo
 * @version $Id: DiscussionItemDatabaseManager.java,v 1.3 2011-06-27 15:21:02 nosebrain Exp $
 * @param <D> 
 */
public abstract class DiscussionItemDatabaseManager<D extends DiscussionItem> extends AbstractDatabaseManager {
	private static final Log log = LogFactory.getLog(DiscussionItemDatabaseManager.class);	
	
	private final DatabasePluginRegistry plugins;
	private final GeneralDatabaseManager generalDb;
	
	private final String discussionItemName;

	protected DiscussionItemDatabaseManager() {
		this.plugins = DatabasePluginRegistry.getInstance();
		this.generalDb = GeneralDatabaseManager.getInstance();
		
		this.discussionItemName = ReflectionUtils.getActualClassArguments(this.getClass()).get(0).getSimpleName();
	}
	
	protected D getDiscussionItemByHashForResource(final String interHash, final String username, final String hash, final DBSession session) {
		final DiscussionItemParam<D> param = this.createDiscussionItemParam(interHash, username);
		param.setHash(hash);
		
		final List<D> discussionItems = this.getDiscussionItemsByHashForResource(param, session);
		
		if (present(discussionItems)) {
			if (discussionItems.size() > 1) {
				log.warn("found multiple discussion items for resource '" + interHash + "' with same hash '" + hash + "'");
			}
			
			return discussionItems.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected List<D> getDiscussionItemsByHashForResource(final DiscussionItemParam<D> param, final DBSession session) {
		return this.queryForList("get" + this.discussionItemName + "sByHashForResource", param, session);
	}
	
	/**
	 * creates a new discussion item 
	 * @param interHash
	 * @param discussionItem
	 * @param session
	 * @return <code>true</code> iff the discussion item was created successfully
	 */
	public boolean createDiscussionItemForResource(final String interHash, final D discussionItem, final DBSession session) {
		/*
		 * check if interHash is present
		 */
		if (!present(interHash)) {
			throw new ValidationException("please provide an interHash for the discussion item");
		}
		
		session.beginTransaction();
		try {
			this.checkDiscussionItemOnCreate(interHash, discussionItem, session);
			
			/*
			 * get a new discussion id from db (used for group visibility)
			 */
			final int discussionId = this.generalDb.getNewId(ConstantID.IDS_DISCUSSION_ITEM_ID, session);
			discussionItem.setId(discussionId);
			
			/*
			 * populate with date and recalculate hash
			 */
			discussionItem.setDate(new Date());
			discussionItem.setHash(DiscussionItemUtils.recalculateHash(discussionItem));
			
			/*
			 * create the discussion item
			 * for each group an own entry! for performance!
			 */
			for (final Group group : discussionItem.getGroups()) {
				final DiscussionItemParam<D> param = this.createDiscussionItemParam(interHash, discussionItem.getUser().getName());
				param.setDiscussionItem(discussionItem);
				param.setGroupId(group.getGroupId());
				this.insert("insert" + this.discussionItemName, param, session);
			}
			
			this.discussionItemCreated(interHash, discussionItem, session);
			
			session.commitTransaction();
			return true;
		} finally {
			session.endTransaction();
		}
	}

	protected void checkDiscussionItemOnCreate(final String interHash, final D discussionItem, final DBSession session) {
		final String parentHash = discussionItem.getParentHash();
		/*
		 * check if parent is in database
		 */
		if (present(parentHash)) {
			final DiscussionItem parentComment = this.getDiscussionItemForHash(interHash, parentHash, session);
			if (!present(parentComment)) {
				throw new ValidationException("parent discussion item not found"); // TODO: error message?!
			}
		}
		
		this.checkDiscussionItem(discussionItem, session);
	}

	protected void discussionItemCreated(final String interHash, final D discussionItem, final DBSession session) {
		// noop
	}

	protected DiscussionItemParam<D> createDiscussionItemParam(final String interHash, final String username) {
		final DiscussionItemParam<D> param = this.createDiscussionItemParam();
		this.fillDiscussionItemParam(param, interHash, username);
		return param;
	}
	
	protected abstract DiscussionItemParam<D> createDiscussionItemParam();
	
	protected abstract void checkDiscussionItem(final D discussionItem, final DBSession session);

	protected void fillDiscussionItemParam(final DiscussionItemParam<?> param, final String interHash, final String username) {
		param.setInterHash(interHash);
		param.setUserName(username);
	}
	
	/**
	 * updates a discussion item
	 * 
	 * @param interHash
	 * @param oldHash
	 * @param discussionItem
	 * @param session
	 * @return <code>true</code> iff the discussion item was updated successfully
	 */
	public boolean updateDiscussionItemForResource(final String interHash, final String oldHash, final D discussionItem, final DBSession session) {
		if (!present(interHash)) {
			throw new ValidationException("please provide an interHash");
		}
		
		log.debug("updating discussionItem with hash " + discussionItem.getHash() + " for resource " + interHash);
		session.beginTransaction();
		try {
			/*
			 * first check if old discussion item is in database
			 */
			final String username = discussionItem.getUser().getName();
			final D oldDiscussionItem = this.getDiscussionItemByHashForResource(interHash, username, oldHash, session);
			final Date changeDate = new Date(); // only one change date
			
			if (!present(oldDiscussionItem)) {
				return false; // TODO error message?
			}
			
			/*
			 * set dates and recalculate hash
			 */
			discussionItem.setDate(oldDiscussionItem.getDate()); // be sure
			discussionItem.setChangeDate(changeDate);
			discussionItem.setHash(DiscussionItemUtils.recalculateHash(discussionItem));
			discussionItem.setId(oldDiscussionItem.getId());
			// parent hash shouldn't change (don't update thread structure)
			discussionItem.setParentHash(oldDiscussionItem.getParentHash());
			
			/*
			 * first check discussion item to update
			 */
			this.checkDiscussionItem(discussionItem, session);
			
			/*
			 * inform the plugins
			 */
			this.plugins.onDiscussionUpdate(interHash, discussionItem, oldDiscussionItem, session);
			
			
			/*
			 * delete all old entries
			 */
			final DiscussionItemParam<D> param = this.createDiscussionItemParam(interHash, username);
			param.setHash(oldHash);
			this.delete("deleteDiscussionItem", param, session);
			
			/*
			 * create new entries
			 */
			param.setDiscussionItem(discussionItem);
			
			/*
			 * insert the item again for each group
			 */
			for (final Group group : discussionItem.getGroups()) {
				param.setGroupId(group.getGroupId());
				this.insert("insert" + this.discussionItemName, param, session);
			}
			
			this.discussionItemUpdated(interHash, discussionItem, oldDiscussionItem, session);
			
			session.commitTransaction();
			return true;
		} finally {
			session.endTransaction();
		}
	}
	
	protected void discussionItemUpdated(final String interHash, final D discussionItem, final D oldDiscussionItem, final DBSession session) {
		// noop
	}

	protected DiscussionItem getDiscussionItemForHash(final String interHash, final String hash, final DBSession session) {
		final DiscussionItemParam<DiscussionItem> param = new DiscussionItemParam<DiscussionItem>();
		param.setHash(hash);
		param.setInterHash(interHash);
		
		return this.queryForObject("getDiscussionItemForResourceByHash", param, DiscussionItem.class, session);
	}

	/**
	 * deletes the specified hash from the query
	 * 
	 * @param interHash
	 * @param user
	 * @param hash
	 * @param session
	 * @return <code>true</code> iff the discussion item was deleted
	 */
	public boolean deleteDiscussionItemForResource(final String interHash, final User user, final String hash, final DBSession session) {
		session.beginTransaction();
		try {
			final String username = user.getName();
			final D oldItem = this.getDiscussionItemByHashForResource(interHash, username, hash, session);
			
			if (!present(oldItem)) {
				return false;
			}
			
			final DiscussionItemParam<D> param = this.createDiscussionItemParam(interHash, username);
			param.setHash(hash);
			
			/*
			 * inform the plugins (logging, …)
			 */
			this.plugins.onDiscussionItemDelete(interHash, oldItem, session);
			
			this.handleDiscussionItemDelete(interHash, user, oldItem, session);
			
			final boolean hasChildren = this.queryForObject("hasSubDiscussionItems", param, Boolean.class, session);
			
			if (hasChildren) {
				/*
				 * to keep thread structure we don't delete the item we only
				 * clean it and don't delete group assignments
				 * otherwise no result set
				 */
				this.update("cleanDiscussionItem", param, session);
			} else {
				// delete discussion item
				this.delete("deleteDiscussionItem", param, session);
			}		
			
			session.commitTransaction();
			return true;
		} finally {
			session.endTransaction();
		}
	}

	protected void handleDiscussionItemDelete(final String interHash, final User user, final D oldDiscussionItem, final DBSession session) {
		// noop
	}
}
