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

package org.bibsonomy.database.managers.chain.tag;

import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.managers.chain.tag.get.GetAllTags;
import org.bibsonomy.database.managers.chain.tag.get.GetPopularTags;
import org.bibsonomy.database.managers.chain.tag.get.GetRelatedTags;
import org.bibsonomy.database.managers.chain.tag.get.GetRelatedTagsForGroup;
import org.bibsonomy.database.managers.chain.tag.get.GetSimilarTags;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByBibtexkey;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByExpression;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByFriendOfUser;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByGroup;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByHash;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByHashForUser;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByResourceSearch;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByTaggedUserRelation;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsByUser;
import org.bibsonomy.database.managers.chain.tag.get.GetTagsViewable;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.model.Tag;

/**
 * @author Dominik Benz
 * @author Miranda Grahl
 * @version $Id: TagChain.java,v 1.24 2011-07-25 12:16:33 folke Exp $
 */
public class TagChain implements FirstListChainElement<Tag, TagParam> {

	private final ListChainElement<Tag, TagParam> getTagsByUser;
	private final ListChainElement<Tag, TagParam> getTagsByGroup;
	private final ListChainElement<Tag, TagParam> getTagsViewable;
	private final ListChainElement<Tag, TagParam> getTagsByRegularExpression;
	private final ListChainElement<Tag, TagParam> getAllTags;
	private final ListChainElement<Tag, TagParam> getTagsByResourceSearch;	
	private final ListChainElement<Tag, TagParam> getRelatedTagsForGroup;
	private final ListChainElement<Tag, TagParam> getRelatedTags;
	private final ListChainElement<Tag, TagParam> getSimilarTags;
	private final ListChainElement<Tag, TagParam> getTagsByHash;
	private final ListChainElement<Tag, TagParam> getTagsByHashForUser;
	private final ListChainElement<Tag, TagParam> getPopularTags;
	private final ListChainElement<Tag, TagParam> getTagsByFriendOfUser;
	private final ListChainElement<Tag, TagParam> getTagsByTaggedUserRelation;
	private final ListChainElement<Tag, TagParam> getTagsByBibtexkey;
	
	/**
	 * Constructs the chain
	 */
	public TagChain() {
		this.getTagsByUser = new GetTagsByUser();
		this.getTagsByGroup = new GetTagsByGroup();
		this.getTagsViewable = new GetTagsViewable();
		this.getAllTags = new GetAllTags();
		this.getTagsByRegularExpression = new GetTagsByExpression();
		this.getTagsByResourceSearch = new GetTagsByResourceSearch();
		this.getRelatedTagsForGroup = new GetRelatedTagsForGroup();
		this.getRelatedTags = new GetRelatedTags();
		this.getTagsByHash = new GetTagsByHash();
		this.getTagsByHashForUser = new GetTagsByHashForUser();
		this.getSimilarTags = new GetSimilarTags();
		this.getPopularTags = new GetPopularTags();
		this.getTagsByFriendOfUser = new GetTagsByFriendOfUser();
		this.getTagsByTaggedUserRelation = new GetTagsByTaggedUserRelation();
		this.getTagsByBibtexkey = new GetTagsByBibtexkey();
		
		this.getTagsByUser.setNext(this.getTagsByGroup);
		this.getTagsByGroup.setNext(this.getTagsByFriendOfUser);
		this.getTagsByFriendOfUser.setNext(this.getAllTags);
		this.getAllTags.setNext(this.getSimilarTags);
		this.getSimilarTags.setNext(this.getRelatedTags);
		this.getRelatedTags.setNext(this.getTagsByResourceSearch);
		this.getTagsByResourceSearch.setNext(this.getTagsViewable);
		this.getTagsViewable.setNext(this.getTagsByRegularExpression);
		this.getTagsByRegularExpression.setNext(this.getRelatedTagsForGroup);
		this.getRelatedTagsForGroup.setNext(this.getTagsByHash);
		this.getTagsByHash.setNext(this.getTagsByHashForUser);
		this.getTagsByHashForUser.setNext(this.getPopularTags);
		this.getPopularTags.setNext(this.getTagsByBibtexkey);
		this.getTagsByBibtexkey.setNext(this.getTagsByTaggedUserRelation);
		
	}
	
	@Override
	public ListChainElement<Tag, TagParam> getFirstElement() {
		return this.getTagsByUser;
	}
}