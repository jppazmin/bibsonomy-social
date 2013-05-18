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

import org.bibsonomy.model.Bookmark;

/**
 * class for bookmark search
 * 
 * @author fei
 * @version $Id: LuceneSearchBookmarks.java,v 1.16 2010-07-16 12:11:59 nosebrain Exp $
 */
public class LuceneSearchBookmarks extends LuceneResourceSearch<Bookmark> {	
	private final static LuceneSearchBookmarks singleton = new LuceneSearchBookmarks();

	/**
	 * @return LuceneSearchBookmarks
	 */
	public static LuceneSearchBookmarks getInstance() {
		return singleton;
	}
	
	/**
	 * constructor
	 */
	private LuceneSearchBookmarks() {
		reloadIndex(0);
	}
	
	@Override
	protected String getResourceName() {
		return Bookmark.class.getSimpleName();
	}

}