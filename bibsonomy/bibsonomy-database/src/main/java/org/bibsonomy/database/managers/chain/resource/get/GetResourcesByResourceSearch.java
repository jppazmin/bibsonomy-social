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

package org.bibsonomy.database.managers.chain.resource.get;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.resource.ResourceChainElement;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.database.systemstags.search.YearSystemTag;
import org.bibsonomy.database.util.DatabaseUtils;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * @author claus
 * @version $Id: GetResourcesByResourceSearch.java,v 1.4 2010-09-28 12:12:39 nosebrain Exp $
 * @param <R> the resource
 * @param <P> the param
 */
public abstract class GetResourcesByResourceSearch<R extends Resource, P extends ResourceParam<R>> extends ResourceChainElement<R, P> {

	@Override
	protected List<Post<R>> handle(P param, DBSession session) {
		// convert tag index to tag list
		List<String> tagIndex = null;
		if (present(param.getTagIndex())) {
			tagIndex = DatabaseUtils.extractTagNames(param.getTagIndex());
		}
		
		/*
		 * extract first-, last- and year from the system tag if present
		 */
		String year = null;
		String firstYear = null;
		String lastYear = null;
		
		final YearSystemTag yearTag = (YearSystemTag) param.getSystemTags().get(YearSystemTag.NAME);
		if (present(yearTag)) {
			year = yearTag.getYear();
			firstYear = yearTag.getFirstYear();
			lastYear = yearTag.getLastYear();
		}
		
		// query the resource searcher
		return this.getDatabaseManagerForType(param.getClass()).getPostsByResourceSearch(
				param.getUserName(), param.getRequestedUserName(), param.getRequestedGroupName(), 
				param.getGroupNames(), param.getRawSearch(), param.getTitle(), param.getAuthor(), tagIndex, 
				year, firstYear, lastYear, 
				param.getLimit(), param.getOffset());
	}
}
