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
import org.bibsonomy.model.GoldStandardPublication;

/**
 * @author dzo
 * @version $Id: LuceneGenerateGoldStandardPublicationIndex.java,v 1.6 2011-05-28 12:27:34 nosebrain Exp $
 */
public class LuceneGenerateGoldStandardPublicationIndex extends LuceneGenerateResourceIndex<GoldStandardPublication> {

	private static LuceneGenerateGoldStandardPublicationIndex INSTANCE;

	/**
	 * @return the @{link:LuceneGenerateGoldstandardPublicationIndex} instance
	 */
	public static LuceneGenerateGoldStandardPublicationIndex getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LuceneGenerateGoldStandardPublicationIndex();
			LuceneSpringContextWrapper.init();
		}
		
		return INSTANCE;
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
        final LuceneGenerateResourceIndex<GoldStandardPublication> indexer = LuceneGenerateGoldStandardPublicationIndex.getInstance();
        indexer.generateIndex();
        indexer.shutdown();
	}
	
	private LuceneGenerateGoldStandardPublicationIndex() {
	}
	
	@Override
	protected String getResourceName() {
		return GoldStandardPublication.class.getSimpleName();
	}
}