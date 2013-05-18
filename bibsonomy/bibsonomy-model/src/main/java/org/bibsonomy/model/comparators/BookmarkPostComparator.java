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

package org.bibsonomy.model.comparators;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.bibsonomy.common.enums.SortKey;
import org.bibsonomy.common.enums.SortOrder;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;


/**
 * Comparator used to sort bibtex posts
 * 
 * @author Dominik Benz
 * @version $Id: BookmarkPostComparator.java,v 1.8 2011-06-14 08:50:23 dbe Exp $
 */
public class BookmarkPostComparator extends PostComparator implements Comparator<Post<Bookmark>>, Serializable {
	private static final long serialVersionUID = -2993829588313719046L;

	/**
	 * Constructor
	 * 
	 * @param sortKeys
	 * @param sortOrders
	 */
	public BookmarkPostComparator(final List<SortKey> sortKeys, final List<SortOrder> sortOrders) {
		super(sortKeys, sortOrders);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * 
	 * main comparison method
	 */
	@Override
	public int compare(final Post<Bookmark> post1, final Post<Bookmark> post2) {
		for (final SortCriterium crit : this.sortCriteria) {
			try {				
				// posting date
				if (SortKey.DATE.equals(crit.sortKey)) {
					return this.compare(post1.getDate(), post2.getDate(), crit.sortOrder);
				}
				// title 
				else if (SortKey.TITLE.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(post1.getResource().getTitle(), post2.getResource().getTitle(), crit.sortOrder);
				}				
				// ranking
				else if (SortKey.RANKING.equals(crit.sortKey)) {
					return this.compare(post1.getRanking(), post2.getRanking(), crit.sortOrder);
				}
				else {
					return 0;
				}
			}
			catch (SortKeyIsEqualException ignore) {
				// the for-loop will jump to the next sort criterium in this case
			}
		}
		return 0;
	}

}