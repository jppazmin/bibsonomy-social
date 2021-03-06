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

package org.bibsonomy.lucene.util.generator;

import org.bibsonomy.lucene.util.JNDITestDatabaseBinder;
import org.bibsonomy.lucene.util.LuceneSpringContextWrapper;
import org.bibsonomy.model.Bookmark;

/**
 * @author fei
 * @version $Id: LuceneGenerateBookmarkIndex.java,v 1.4 2011-05-28 12:27:35 nosebrain Exp $
 */
public class LuceneGenerateBookmarkIndex extends LuceneGenerateResourceIndex<Bookmark> {
	/** singleton pattern */
	private static LuceneGenerateBookmarkIndex instance;
	
	/**
	 * @return the {@link LuceneGenerateBookmarkIndex} instance (configured)
	 */
	public static LuceneGenerateBookmarkIndex getInstance() {
		if (instance==null) {
			instance = new LuceneGenerateBookmarkIndex();
			LuceneSpringContextWrapper.init(); // inits the fields of the singleton
		}
		
		return instance;
	}
	
	/**
	 * main method - generate index from database as configured in property file
	 * 
	 * @param args 
	 * @throws Exception  
	 */
	public static void main(final String[] args) throws Exception {
		// configure jndi context
		JNDITestDatabaseBinder.bind();
		
		// create index
	    final LuceneGenerateResourceIndex<Bookmark> indexer = LuceneGenerateBookmarkIndex.getInstance();
	    indexer.generateIndex();
	    indexer.shutdown();
	}
	
	private LuceneGenerateBookmarkIndex() {
	}
	
	@Override
	protected String getResourceName() {
		return Bookmark.class.getSimpleName();
	}
}
