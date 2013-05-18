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
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.managers.chain.tag.TagChainElement;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.model.Tag;

/**
 * Retrieve tags by Hash
 * 
 * @author Dominik Benz
 * @version $Id: GetTagsByHash.java,v 1.3 2011-06-07 13:33:50 rja Exp $
 */
public class GetTagsByHash extends TagChainElement {

	@Override
	protected List<Tag> handle(final TagParam param, final DBSession session) {		
		final int contentType = param.getContentType();
		if (contentType == ConstantID.BIBTEX_CONTENT_TYPE.getId()) {
			return this.db.getTagsByBibtexHash(param.getUserName(), 
											   param.getHash(), 
											   HashID.getSimHash(param.getSimHash()), 
											   param.getGroups(), 
											   param.getLimit(), 
											   param.getOffset(), 
											   session);
		}
		
		if (contentType == ConstantID.BOOKMARK_CONTENT_TYPE.getId()) {
			return this.db.getTagsByBookmarkHash(param.getUserName(), 
												 param.getHash(), 
												 param.getGroups(), 
												 param.getLimit(), 
												 param.getOffset(), 
												 session);
		}
		
		throw new UnsupportedResourceTypeException();
	}

	@Override
	protected boolean canHandle(final TagParam param) {
		return (present(param.getGrouping()) &&
				param.getGrouping() == GroupingEntity.ALL &&
				present(param.getHash()) &&
				!present(param.getBibtexKey()) &&
				!present(param.getRequestedUserName()));
	}
}