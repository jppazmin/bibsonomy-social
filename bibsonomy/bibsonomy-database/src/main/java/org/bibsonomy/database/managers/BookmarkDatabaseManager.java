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

package org.bibsonomy.database.managers;

import java.util.List;

import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.managers.chain.bookmark.BookmarkChain;
import org.bibsonomy.database.params.BookmarkParam;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;


/**
 * Used to CRUD bookmarks from the database.
 * 
 * @author Miranda Grahl
 * @author Jens Illig
 * @author Christian Schenk
 * @author Daniel Zoller
 * @version $Id: BookmarkDatabaseManager.java,v 1.115 2011-07-14 12:58:36 nosebrain Exp $
 */
public class BookmarkDatabaseManager extends PostDatabaseManager<Bookmark, BookmarkParam> {
	private static final BookmarkDatabaseManager singleton = new BookmarkDatabaseManager();
	
	private static final BookmarkChain chain = new BookmarkChain();
	private static final HashID[] hashRange = { HashID.SIM_HASH0 };
	
	/**
	 * @return BookmarkDatabaseManager
	 */
	public static BookmarkDatabaseManager getInstance() {
		return singleton;
	}

	private BookmarkDatabaseManager() {
	}


	@Override
	protected List<Post<Bookmark>> getPostsForHomepage(final BookmarkParam param, final DBSession session) {
		final FilterEntity filter = param.getFilter();
		
		if (FilterEntity.UNFILTERED.equals(filter)) {
			return this.postList("getBookmarkForHomepageUnfiltered", param, session);
		}
		
		return super.getPostsForHomepage(param, session);
	}
	
	@Override
	public List<Post<Bookmark>> getPostsFromBasketForUser(final String loginUser, final int limit, final int offset, final DBSession session) {
		throw new UnsupportedOperationException("not available for bookmarks");
	}
	
	@Override
	protected void checkPost(final Post<Bookmark> post, final DBSession session) {
		// nop
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.PostDatabaseManager#onPostDelete(java.lang.Integer, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	protected void onPostDelete(final Integer contentId, final DBSession session) {
		this.plugins.onBookmarkDelete(contentId, session);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.PostDatabaseManager#onPostUpdate(java.lang.Integer, java.lang.Integer, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	protected void onPostUpdate(final Integer oldContentId, final Integer newContentId, final DBSession session) {
		this.plugins.onBookmarkUpdate(oldContentId, newContentId, session);
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.PostDatabaseManager#getChain()
	 */
	@Override
	protected FirstListChainElement<Post<Bookmark>, BookmarkParam> getChain() {
		return chain;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.PostDatabaseManager#getHashRange()
	 */
	@Override
	protected HashID[] getHashRange() {
		return hashRange;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.PostDatabaseManager#getInsertParam(org.bibsonomy.model.Post, org.bibsonomy.database.util.DBSession)
	 */
	@Override
	protected BookmarkParam getInsertParam(final Post<? extends Bookmark> post, final DBSession session) {
		final BookmarkParam insert = this.getNewParam();
		
		insert.setResource(post.getResource());
		insert.setDate(post.getDate());
		insert.setChangeDate(post.getChangeDate());
		insert.setRequestedContentId(post.getContentId());
		insert.setHash(post.getResource().getIntraHash());
		insert.setDescription(post.getDescription());
		insert.setUserName(post.getUser().getName());
		insert.setUrl(post.getResource().getUrl());	

		// in field group in table bookmark, insert the id for PUBLIC, PRIVATE or the id of the FIRST group in list
		final int groupId = post.getGroups().iterator().next().getGroupId();
		insert.setGroupId(groupId);
		
		return insert;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.managers.PostDatabaseManager#getNewParam()
	 */
	@Override
	protected BookmarkParam getNewParam() {
		return new BookmarkParam();
	}
}