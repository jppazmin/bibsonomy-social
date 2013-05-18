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

package org.bibsonomy.lucene.param.comparator;

import static org.bibsonomy.lucene.util.LuceneBase.FLD_CONTENT_ID;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_LAST_LOG_DATE;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.lucene.document.Document;

/**
 * comparator for avoiding duplicates in index-update-cache
 * 
 * FIXME: we have to consider the case, that a post is updated between
 *        two update sessions, after the user was unflagged as spammer
 *        THIS UPDATE WILL BE LOST!!!
 * 
 * @author fei
 * @version $Id: DocumentCacheComparator.java,v 1.6 2010-07-16 12:12:01 nosebrain Exp $
 */
public class DocumentCacheComparator implements Comparator<Document>, Serializable {
	private static final long serialVersionUID = -5135628928597791434L;

	@Override
	public int compare(Document o1, Document o2) {
		
		// most important: treat documents as equal, if their content_ids conincide
		String id1 = o1.get(FLD_CONTENT_ID);
		String id2 = o2.get(FLD_CONTENT_ID);
		if( (id1!=null) && (id2!=null) && (id1.equals(id2)) )
			return 0;
		
		// otherwise order according to the date field
		try {
			Long date1  = Long.parseLong(o1.get(FLD_LAST_LOG_DATE));
			Long date2  = Long.parseLong(o2.get(FLD_LAST_LOG_DATE));
			if( (date1!=null) && (date2!=null) ) {
				int cmp = date1.compareTo(date2);
				if( cmp==0 )
					return -1;
				
				return cmp;
			}
		} catch(NumberFormatException e) {
			return -1;
		}
		
		// fallback
		return -1;
	}
}
