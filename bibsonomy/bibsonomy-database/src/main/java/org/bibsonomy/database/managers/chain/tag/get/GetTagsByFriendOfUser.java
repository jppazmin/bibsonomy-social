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

package org.bibsonomy.database.managers.chain.tag.get;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.tag.TagChainElement;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.database.systemstags.search.NetworkRelationSystemTag;
import org.bibsonomy.model.Tag;

/**
 * Returns a list of tags for a given friend of a given user.
 * 
 * @author Steffen Kress
 * @version $Id: GetTagsByFriendOfUser.java,v 1.7 2011-07-25 12:16:33 folke Exp $
 */
public class GetTagsByFriendOfUser extends TagChainElement {
	@Override
	protected List<Tag> handle(final TagParam param, final DBSession session) {
		if (present(param.getTagIndex())) {
			// retrieve related tags
			return this.db.getRelatedTagsForUser(param.getUserName(), 
												 param.getRequestedUserName(), 
												 param.getTagIndex(), 
												 param.getGroups(),
												 param.getLimit(),
												 param.getOffset(),
												 session);
		}
		// retrieve all tags from friend that are visible to his friends
		return this.db.getTagsByFriendOfUser(param, session);
	}

	@Override
	protected boolean canHandle(final TagParam param) {
		return (param.getGrouping() == GroupingEntity.FRIEND && 
				present(param.getRequestedUserName()) &&
				// discriminate from the tagged user relation queries
				( !present(param.getRelationTags()) || 
					param.getRelationTags().size()==1 && (NetworkRelationSystemTag.BibSonomyFriendSystemTag.equals(param.getRelationTags().get(0)))
				) &&
				!present(param.getRegex()) &&
				!present(param.getBibtexKey()) &&
				!present(param.getSearch()) &&
				!present(param.getTitle()) &&
				!present(param.getAuthor()) &&
				!present(param.getHash()));
	}
}