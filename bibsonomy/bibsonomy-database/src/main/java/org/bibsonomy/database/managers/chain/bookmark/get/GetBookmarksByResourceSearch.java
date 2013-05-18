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

package org.bibsonomy.database.managers.chain.bookmark.get;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByResourceSearch;
import org.bibsonomy.database.params.BookmarkParam;
import org.bibsonomy.model.Bookmark;

/**
 * Returns a list of BibTex's for a given search.
 * 
 * @author claus
 * @version $Id: GetBookmarksByResourceSearch.java,v 1.7 2010-09-28 12:12:39 nosebrain Exp $
 */
public class GetBookmarksByResourceSearch extends GetResourcesByResourceSearch<Bookmark, BookmarkParam> {

	@Override
	protected boolean canHandle(final BookmarkParam param) {
		return (present(param.getSearch()) || present(param.getTitle())); 
	}
}