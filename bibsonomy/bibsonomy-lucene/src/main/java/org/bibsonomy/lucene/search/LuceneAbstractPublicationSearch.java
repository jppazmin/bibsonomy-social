/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.search;

import static org.bibsonomy.lucene.util.LuceneBase.FLD_AUTHOR;
import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collection;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.bibsonomy.model.BibTex;

/**
 * @author dzo
 * @version 
 *
 * @param <P>
 */
public abstract class LuceneAbstractPublicationSearch<P extends BibTex> extends LuceneResourceSearch<P> {

    @Override
    protected BooleanQuery buildSearchQuery(String userName, String searchTerms, String titleSearchTerms, String authorSearchTerms, Collection<String> tagIndex) {
	final BooleanQuery searchQuery = super.buildSearchQuery(userName, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex);
		
	// search author
	if (present(authorSearchTerms)) {
		final Query authorQuery = this.parseSearchQuery(FLD_AUTHOR, authorSearchTerms);
		searchQuery.add(authorQuery, Occur.MUST);
	}
	
	return searchQuery;
    }
}
