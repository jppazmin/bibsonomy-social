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

import java.util.Arrays;
import java.util.List;

import org.bibsonomy.lucene.util.JNDITestDatabaseBinder;
import org.bibsonomy.model.Resource;

/**
 * Generates empty index files for lucene. The path of the index files is configured 
 * in 'lucene.properties'
 *  
 * @author sst
 * @author fei
 * @version $Id: GenerateEmptyLuceneIndex.java,v 1.3 2010-07-16 12:12:01 nosebrain Exp $
 */
public class GenerateEmptyLuceneIndex {
    
    static {
	JNDITestDatabaseBinder.bind();
    }
    
    private final static List<LuceneGenerateResourceIndex<? extends Resource>> GENERATORS = Arrays.<LuceneGenerateResourceIndex<?>>asList(LuceneGenerateBibTexIndex.getInstance(), LuceneGenerateGoldStandardPublicationIndex.getInstance(), LuceneGenerateBookmarkIndex.getInstance());

    /**
     * generates a empty index foreach known index
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {	
	for (final LuceneGenerateResourceIndex<? extends Resource> generator : GENERATORS) {
	    generator.createEmptyIndex();
	    generator.shutdown();
	}
    }
	
}
