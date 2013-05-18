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

package org.bibsonomy.database.systemstags.executable;

import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.errors.UnspecifiedErrorMessage;
import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.GeneralDatabaseManager;
import org.bibsonomy.database.managers.GroupDatabaseManager;
import org.bibsonomy.database.managers.InboxDatabaseManager;
import org.bibsonomy.database.managers.TagDatabaseManager;
import org.bibsonomy.database.systemstags.AbstractSystemTagImpl;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.markup.SentSystemTag;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;

/**
 * This system tag creates a link to its post in the inbox of a specified user (the receiver)
 * The link to the post is its content_id
 * The link also receives all tags of the post including this one (deactivated and renamed to from:senderName)
 * The tag is deactivated (renamed to sent:receiverName) instead of removed
 * @author sdo
 * @version $Id: ForFriendTag.java,v 1.30 2011-07-21 18:08:04 doerfel Exp $
 */
public class ForFriendTag extends AbstractSystemTagImpl implements ExecutableSystemTag {

	private static final String NAME = "send";
	private static boolean toHide = true;

	private Tag tag; // the original (regular) tag that this systemTag was created from


	@Override
	public ForFriendTag newInstance() {
		return new ForFriendTag();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isToHide() {
		return toHide;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	@Override
	public <T extends Resource> void performBeforeCreate(final Post<T> post, final DBSession session) {
		// nothing is performed
	}

	@Override
	public <T extends Resource> void performBeforeUpdate(Post<T> newPost, final Post<T> oldPost, PostUpdateOperation operation, DBSession session) {
		// nothing is performed
	}

	@Override
	public <T extends Resource> void performAfterUpdate(Post<T> newPost, final Post<T> oldPost, PostUpdateOperation operation, DBSession session) {
		// do exactly the same as in a Creation of a post (i. e. ignore which operation)
		this.performAfterCreate(newPost, session);
	}


	@Override
	public <T extends Resource> void performAfterCreate(final Post<T> post, final DBSession session) {
		log.debug("performing after access");
		String receiver = this.getArgument().toLowerCase();
		String sender = post.getUser().getName();
		String intraHash = post.getResource().getIntraHash();
		/*
		 * Check permissions
		 */
		if (!this.hasPermissions(sender, receiver, intraHash, session)) {
			// sender is not allowed to use this tag, errorMessages were added
			return;
		}
		log.debug("permissions granted");
		/*
		 * Rename forFriendTag from send:userName to sent:userName
		 * We deactivate the systemTag to avoid sending the Message again and again each time the sender updates his post
		 */
		final TagDatabaseManager tagDb = TagDatabaseManager.getInstance();
		// 1. delete all tags from the database (will be replaced by new ones)
		tagDb.deleteTags(post, session);
		// 2. rename this tag for the receiver (store senderName)
		this.tag.setName("from:" + sender);	
		try {
			// 3. store the inboxMessage with tag from:senderName 
			InboxDatabaseManager.getInstance().createInboxMessage(sender, receiver, post, session);
			log.debug("message was created");
			// 4. rename this tag for the sender (store receiverName)
			this.tag.setName(SentSystemTag.NAME + SystemTagsUtil.DELIM + receiver);	
		} catch (UnsupportedResourceTypeException urte) {
			session.addError(intraHash, new UnspecifiedErrorMessage(urte));
			log.warn("Added UnspecifiedErrorMessage (unsupported ResourceType) for post " + intraHash);
		}
		// 5. store the tags for the sender with the confirmation tag: sent:userName
		tagDb.insertTags(post, session);		
	}


	/**
	 * Checks the preconditions to this tags usage, adds errorMessages
	 * using the tag is allowed, 
	 * - if the sender is in the friends list of the receiver or 
	 * - if a group exists that both sender and receiver are a member of
	 * @param intraHash
	 * @param session
	 * @param sender
	 * @param receiver
	 * @return true iff sender is allowed to use the tag
	 */
	private boolean hasPermissions(final String sender, final String receiver, final String intraHash, final DBSession session) {
		final GroupDatabaseManager groupDb = GroupDatabaseManager.getInstance();
		final GeneralDatabaseManager generalDb = GeneralDatabaseManager.getInstance();

		/*
		 *  We decided to ignore errors in systemTags. Thus the user is free use any tag.
		 *  The drawback: If it is the user's intention to use a systemTag, he will never know if there was a typo! 
		 */
		if ( !( generalDb.isFriendOf(sender, receiver, session) || groupDb.getCommonGroups(sender, receiver, session).size()>0 ) ) {
			return false;
		}
		if (sender.equals(receiver)) {
			return false;
		}
		return true;
	}

	/*
	 * We overwrite this method because we want to interpret also the send tag 
	 * without prefix (sys/system) as systemTag and we need an argument
	 * @see org.bibsonomy.database.systemstags.AbstractSystemTagImpl#isInstance(java.lang.String)
	 */
	@Override
	public boolean isInstance(String tagName) {
		// the send tag must have an argument, the prefix is not required
		return SystemTagsUtil.hasTypeAndArgument(tagName) && NAME.equals(SystemTagsUtil.extractType(tagName));
	}

}
