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
import org.bibsonomy.model.Tag;

/**
 * Returns a list of tags for a given user.
 * 
 * @author Dominik Benz
 * @author Miranda Grahl
 * @version $Id: GetTagsByUser.java,v 1.27 2010-06-02 11:31:58 nosebrain Exp $
 */
public class GetTagsByUser extends TagChainElement {

	@Override
	protected List<Tag> handle(final TagParam param, final DBSession session) {
		if (param.getTagIndex() != null && param.getTagIndex().size() > 0) {
			// retrieve related tags
			return this.db.getRelatedTagsForUser(param.getUserName(),
							param.getRequestedUserName(), 
							param.getTagIndex(),
							param.getGroups(),
							param.getLimit(),
							param.getOffset(),
							session);
		}
		// retrieve all tags from user
		return this.db.getTagsByUser(param, session);
	}

	@Override
	protected boolean canHandle(final TagParam param) {
		return (param.getGrouping() == GroupingEntity.USER && 
				present(param.getRequestedUserName()) &&
				!present(param.getRegex()) &&
				!present(param.getBibtexKey()) &&
				!present(param.getHash()) &&
				!present(param.getSearch()) &&
				!present(param.getTitle()) &&
				!present(param.getAuthor()) 
				);
	}
}