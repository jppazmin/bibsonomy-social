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

package org.bibsonomy.database.managers.chain.bibtex;

import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.managers.chain.bibtex.get.GetBibtexByKey;
import org.bibsonomy.database.managers.chain.bibtex.get.GetBibtexByResourceSearch;
import org.bibsonomy.database.managers.chain.bibtex.get.GetBibtexFromBasketForUser;
import org.bibsonomy.database.managers.chain.bibtex.get.GetBibtexWithRepository;
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
import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;

/**
 * @author Miranda Grahl
 * @version $Id: BibTexChain.java,v 1.32 2011-05-28 20:49:20 folke Exp $
 */
public class BibTexChain implements FirstListChainElement<Post<BibTex>, BibTexParam> {

	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByHash;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByHashForUser;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByKey;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByTagNames;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByTagNamesAndUser;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsForGroup;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsForGroupAndTag;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsForHomepage;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsForPopular;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsViewable;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsForUser;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByConceptForUser;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByUserFriends;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByUserAndTagsFriends;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByFriends;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByResourceSearch;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByConceptByTag;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByConceptForGroup;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsFromBasketForUser;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByFollowedUsers;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsFromInbox;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsWithRepository;
	private final ListChainElement<Post<BibTex>, BibTexParam> getPublicationsByTaggedUserRelation;

	/**
	 * Constructs the chain
	 */
	public BibTexChain() {
		this.getPublicationsByHash = new GetResourcesByHash<BibTex, BibTexParam>();
		this.getPublicationsByHashForUser = new GetResourcesByHashForUser<BibTex, BibTexParam>();
		this.getPublicationsByTagNames = new GetResourcesByTagNames<BibTex, BibTexParam>();
		this.getPublicationsByTagNamesAndUser = new GetResourcesByTagNamesAndUser<BibTex, BibTexParam>();
		this.getPublicationsForGroup = new GetResourcesForGroup<BibTex, BibTexParam>();
		this.getPublicationsForGroupAndTag = new GetResourcesForGroupAndTag<BibTex, BibTexParam>();
		this.getPublicationsForHomepage = new GetResourcesForHomepage<BibTex, BibTexParam>();
		this.getPublicationsForPopular = new GetResourcesPopular<BibTex, BibTexParam>();
		this.getPublicationsViewable = new GetResourcesViewable<BibTex, BibTexParam>();
		this.getPublicationsForUser = new GetResourcesForUser<BibTex, BibTexParam>();
		this.getPublicationsByConceptForUser = new GetResourcesByConceptForUser<BibTex, BibTexParam>();
		this.getPublicationsByUserFriends = new GetResourcesOfFriendsByUser<BibTex, BibTexParam>();
		this.getPublicationsByUserAndTagsFriends = new GetResourcesOfFriendsByTags<BibTex, BibTexParam>();
		this.getPublicationsByFriends = new GetResourcesByFriends<BibTex, BibTexParam>();	
		this.getPublicationsByResourceSearch = new GetBibtexByResourceSearch();
		this.getPublicationsByConceptByTag = new GetResourcesByConceptByTag<BibTex, BibTexParam>();
		this.getPublicationsByConceptForGroup = new GetResourcesByConceptForGroup<BibTex, BibTexParam>();
		this.getPublicationsByKey = new GetBibtexByKey();
		this.getPublicationsFromBasketForUser = new GetBibtexFromBasketForUser();
		this.getPublicationsByFollowedUsers = new GetResourcesByFollowedUsers<BibTex, BibTexParam>();
		this.getPublicationsFromInbox = new GetResourcesFromInbox<BibTex, BibTexParam>();
		this.getPublicationsWithRepository = new GetBibtexWithRepository();
		this.getPublicationsByTaggedUserRelation = new GetResourcesByTaggedUserRelation<BibTex, BibTexParam>();

		this.getPublicationsWithRepository.setNext(this.getPublicationsByKey);
		this.getPublicationsByKey.setNext(this.getPublicationsForHomepage);
		this.getPublicationsForHomepage.setNext(this.getPublicationsForPopular);
		this.getPublicationsForPopular.setNext(this.getPublicationsForUser);
		this.getPublicationsForUser.setNext(this.getPublicationsByTagNames);
		this.getPublicationsByTagNames.setNext(this.getPublicationsByHashForUser);
		this.getPublicationsByHashForUser.setNext(this.getPublicationsByHash);
		this.getPublicationsByHash.setNext(this.getPublicationsByTagNamesAndUser);
		this.getPublicationsByTagNamesAndUser.setNext(this.getPublicationsForGroup);
		this.getPublicationsForGroup.setNext(this.getPublicationsForGroupAndTag);
		this.getPublicationsForGroupAndTag.setNext(this.getPublicationsViewable);
		this.getPublicationsViewable.setNext(this.getPublicationsByConceptForUser);
		this.getPublicationsByConceptForUser.setNext(this.getPublicationsByUserFriends);
		this.getPublicationsByUserFriends.setNext(this.getPublicationsByUserAndTagsFriends);
		this.getPublicationsByUserAndTagsFriends.setNext(this.getPublicationsByFriends);
		this.getPublicationsByFriends.setNext(this.getPublicationsByFollowedUsers);
		this.getPublicationsByFollowedUsers.setNext(this.getPublicationsByResourceSearch);
		this.getPublicationsByResourceSearch.setNext(this.getPublicationsByConceptByTag);
		this.getPublicationsByConceptByTag.setNext(this.getPublicationsByConceptForGroup);
		this.getPublicationsByConceptForGroup.setNext(this.getPublicationsFromBasketForUser);
		this.getPublicationsFromBasketForUser.setNext(this.getPublicationsFromInbox);
		this.getPublicationsFromInbox.setNext(this.getPublicationsByTaggedUserRelation);
	}

	@Override
	public ListChainElement<Post<BibTex>, BibTexParam> getFirstElement() {
		return this.getPublicationsWithRepository;
	}
}