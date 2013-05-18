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

package org.bibsonomy.database.managers.chain.bookmark;

import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.managers.chain.bookmark.get.GetBookmarksByResourceSearch;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByConceptByTag;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByConceptForGroup;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByConceptForUser;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByFollowedUsers;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByFriends;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByHash;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByHashForUser;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByTagNames;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByTagNamesAndUser;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByTaggedUserRelation;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesForGroup;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesForGroupAndTag;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesForHomepage;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesForUser;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesFromInbox;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesOfFriendsByTags;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesOfFriendsByUser;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesPopular;
import org.bibsonomy.database.managers.chain.resource.get.GetResourcesViewable;
import org.bibsonomy.database.params.BookmarkParam;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;

/**
 * @author Miranda Grahl
 * @version $Id: BookmarkChain.java,v 1.26 2011-05-28 20:49:20 folke Exp $
 */
public class BookmarkChain implements FirstListChainElement<Post<Bookmark>, BookmarkParam> {

	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksForUser;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByHash;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByHashForUser;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByTagNames;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByTagNamesAndUser;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksForGroup;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksForGroupAndTag;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksForHomePage;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksForPopular;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksViewable;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByConcept;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByUserFriends;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByUserAndTagsFriends;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByFriends;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByResourceSearch;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByConceptByTag;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByConceptForGroup;	
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByFollowedUsers;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksFromInbox;
	private final ListChainElement<Post<Bookmark>, BookmarkParam> getBookmarksByTaggedUserRelation;
	
	/**
	 * Constructs the chain
	 */
	public BookmarkChain() {
		this.getBookmarksForUser = new GetResourcesForUser<Bookmark, BookmarkParam>();
		this.getBookmarksByHash = new GetResourcesByHash<Bookmark, BookmarkParam>();
		this.getBookmarksByHashForUser = new GetResourcesByHashForUser<Bookmark, BookmarkParam>();
		this.getBookmarksByTagNames = new GetResourcesByTagNames<Bookmark, BookmarkParam>();
		this.getBookmarksByTagNamesAndUser = new GetResourcesByTagNamesAndUser<Bookmark, BookmarkParam>();
		this.getBookmarksForGroup = new GetResourcesForGroup<Bookmark, BookmarkParam>();
		this.getBookmarksForGroupAndTag = new GetResourcesForGroupAndTag<Bookmark, BookmarkParam>();
		this.getBookmarksForHomePage = new GetResourcesForHomepage<Bookmark, BookmarkParam>();
		this.getBookmarksForPopular = new GetResourcesPopular<Bookmark, BookmarkParam>();
		this.getBookmarksViewable = new GetResourcesViewable<Bookmark, BookmarkParam>();
		this.getBookmarksByConcept = new GetResourcesByConceptForUser<Bookmark, BookmarkParam>();
		this.getBookmarksByUserFriends = new GetResourcesOfFriendsByUser<Bookmark, BookmarkParam>();
		this.getBookmarksByUserAndTagsFriends = new GetResourcesOfFriendsByTags<Bookmark, BookmarkParam>();
		this.getBookmarksByFriends = new GetResourcesByFriends<Bookmark, BookmarkParam>();
		this.getBookmarksByFollowedUsers = new GetResourcesByFollowedUsers<Bookmark, BookmarkParam>();
		this.getBookmarksByResourceSearch = new GetBookmarksByResourceSearch();
		this.getBookmarksByConceptByTag = new GetResourcesByConceptByTag<Bookmark, BookmarkParam>();
		this.getBookmarksByConceptForGroup = new GetResourcesByConceptForGroup<Bookmark, BookmarkParam>();
		this.getBookmarksFromInbox = new GetResourcesFromInbox<Bookmark, BookmarkParam>();
		this.getBookmarksByTaggedUserRelation = new GetResourcesByTaggedUserRelation<Bookmark, BookmarkParam>();
		
		this.getBookmarksForHomePage.setNext(this.getBookmarksForPopular);
		this.getBookmarksForPopular.setNext(this.getBookmarksForUser);
		this.getBookmarksForUser.setNext(this.getBookmarksByTagNames);
		this.getBookmarksByTagNames.setNext(this.getBookmarksByHashForUser);
		this.getBookmarksByHashForUser.setNext(this.getBookmarksByHash);
		this.getBookmarksByHash.setNext(this.getBookmarksByTagNamesAndUser);
		this.getBookmarksByTagNamesAndUser.setNext(this.getBookmarksForGroup);
		this.getBookmarksForGroup.setNext(this.getBookmarksForGroupAndTag);
		this.getBookmarksForGroupAndTag.setNext(this.getBookmarksViewable);
		this.getBookmarksViewable.setNext(this.getBookmarksByConcept);
		this.getBookmarksByConcept.setNext(this.getBookmarksByUserFriends);
		this.getBookmarksByUserFriends.setNext(this.getBookmarksByUserAndTagsFriends);
		this.getBookmarksByUserAndTagsFriends.setNext(this.getBookmarksByFriends);
		this.getBookmarksByFriends.setNext(this.getBookmarksByFollowedUsers);
		this.getBookmarksByFollowedUsers.setNext(this.getBookmarksByResourceSearch);
		this.getBookmarksByResourceSearch.setNext(this.getBookmarksByConceptByTag);
		this.getBookmarksByConceptByTag.setNext(this.getBookmarksByConceptForGroup);
		this.getBookmarksByConceptForGroup.setNext(this.getBookmarksFromInbox);
		this.getBookmarksFromInbox.setNext(this.getBookmarksByTaggedUserRelation);
	}

	@Override
	public ListChainElement<Post<Bookmark>, BookmarkParam> getFirstElement() {
		return this.getBookmarksForHomePage;
	}
}