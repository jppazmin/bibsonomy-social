/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.util;

import java.util.Collections;
import java.util.List;

import org.bibsonomy.common.enums.SortKey;
import org.bibsonomy.common.enums.SortOrder;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.comparators.BookmarkPostComparator;

/**
 * Some Bookmark utility functions
 * 
 * @author Dominik Benz
 * @version $Id: BookmarkUtils.java,v 1.5 2011-04-29 06:45:01 bibsonomy Exp $
 */
public class BookmarkUtils {

	/**
	 * Sort a list of publication posts (and eventually remove duplicates).
	 * 
	 * @param bookmarkList
	 * @param sortKeys
	 * @param sortOrders
	 */
	public static void sortBookmarkList(final List<Post<Bookmark>> bookmarkList, final List<SortKey> sortKeys, final List<SortOrder> sortOrders) {
		Collections.sort(bookmarkList, new BookmarkPostComparator(sortKeys, sortOrders));
	}
	
}