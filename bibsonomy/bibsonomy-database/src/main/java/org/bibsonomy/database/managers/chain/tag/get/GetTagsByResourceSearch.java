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

import java.util.Collection;
import java.util.List;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.tag.TagChainElement;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.database.util.DatabaseUtils;
import org.bibsonomy.model.Tag;

/**
 * Returns a list of tags for a given author.
 * 
 * @author Dominik Benz
 * @author Miranda Grahl
 * @version $Id: GetTagsByResourceSearch.java,v 1.6 2011-06-16 13:55:02 nosebrain Exp $
 */
public class GetTagsByResourceSearch extends TagChainElement {
	
	@Override
	protected List<Tag> handle(final TagParam param, final DBSession session) {
		Collection<String> tags = null;
		if (present(param.getTagIndex())) {
			tags = DatabaseUtils.extractTagNames(param.getTagIndex());
		}
		return this.db.getTagsByResourceSearch(param.getUserName(), param.getRequestedUserName(), param.getRequestedGroupName(), param.getGroupNames(), param.getSearch(), param.getTitle(), param.getAuthor(), tags, null, null, null, param.getLimit(), param.getOffset());
	}

	@Override
	protected boolean canHandle(final TagParam param) {
		return ( !present(param.getBibtexKey()) &&
				 !present(param.getRegex()) &&
				 !present(param.getHash()) &&
				 !present(param.getTagRelationType()) &&
				(present(param.getSearch()) || present(param.getTitle()) || present(param.getAuthor())) );
	}	
}