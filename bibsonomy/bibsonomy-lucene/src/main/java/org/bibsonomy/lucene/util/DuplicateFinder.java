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

package org.bibsonomy.lucene.util;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import static org.bibsonomy.lucene.util.LuceneBase.*;

/**
 * class for finding duplicate entries in a lucene index (with respect to a given field)
 * 
 * credits go to http://lucene.472066.n3.nabble.com/Index-Dedupe-td549923.html
 * 
 * @author fei
 */
public class DuplicateFinder {
	
	/** list of fields to display for duplicates */
	private static final String[] fieldList = {
		FLD_CONTENT_ID,
		FLD_INTERHASH,
		FLD_TITLE
	};
	
	/** lucene index reader */
	private static IndexReader reader;

	/**
	 * search for duplicate entries 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if( args.length != 2 ) {
			System.out.println("Usage: \n\t DuplicateFinder <path to index directory> <field name>");
			return;
		}
		
		try {
			findDuplicates(args[0], args[1], false);
		} catch (Exception e) {
			System.out.println("Error processing index at '"+args[0]+"':");
			e.printStackTrace();
		}
	}

	/**
	 * Use termDocs() to iterate all the terms in a given unique field.  
	 * Duplicate entries are written to stdout and deleted if desired
	 * 
	 * @param indexPath path to the index
	 * @param fieldName name of the unique field
	 * @param doRemove if true, duplicate entries will be removed
	 * 
	 * @throws Exception
	 */
	public static void findDuplicates(String indexPath, String fieldName, boolean doRemove) throws Exception {
		Directory indexDirectory = FSDirectory.open(new File(indexPath));

		reader = IndexReader.open(indexDirectory);

		TermEnum theTerms = reader.terms(new Term(fieldName));

		Term term = null;

		do {
			term = theTerms.term();

			if ((term == null) || !term.field().equalsIgnoreCase(fieldName) ) { 
				break;
			}

			if (theTerms.docFreq() > 1) {
				printDupsForTerm(term, theTerms.docFreq());
				if (doRemove) {
					removeDupsForTerm(term);
				}
			}
		} while (theTerms.next());
	}

	/**
	 * write out duplicate entries for given term
	 * 
	 * @param term
	 * @param docFreq number of duplicates
	 */
	private static void printDupsForTerm(Term term, int docFreq) throws Exception {
		System.out.print(docFreq+" duplicate entries for \n\t");
		
		TermDocs td = reader.termDocs(term);

		for ( int idx = 0; td.next(); ++idx) {
			System.out.print(td.doc()+"\t");
			Document document = reader.document(td.doc());
			
			for (String fieldName : fieldList) {
				System.out.print(document.get(fieldName)+"\t");
			}
			System.out.print("\n\t");
		}
		
		System.out.println();
	}

	/**
	 * skip the first doc for each term
	 * 
	 * @param term
	 * @throws Exception
	 */
	private static void removeDupsForTerm(Term term) throws Exception {
		TermDocs td = reader.termDocs(term);
		for (int idx = 0; td.next(); ++idx) {
			if (idx > 0) {
				reader.deleteDocument(td.doc());
			}
		}
	}
	
}
