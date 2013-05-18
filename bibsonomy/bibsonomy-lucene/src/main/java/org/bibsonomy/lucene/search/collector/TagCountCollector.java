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

package org.bibsonomy.lucene.search.collector;

import static org.bibsonomy.lucene.util.LuceneBase.FLD_TAS;
import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.MapFieldSelector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.bibsonomy.model.Tag;

/**
 * experimental hits collector for calculating author tag cloud
 * 
 * FIXME: springify this
 * 
 * @author fei
 * @version $Id: TagCountCollector.java,v 1.13 2010-07-16 12:12:01 nosebrain Exp $
 */
public class TagCountCollector extends Collector {
	private static final Log log = LogFactory.getLog(TagCountCollector.class);
	
	private static final String CFG_LIST_DELIMITER = " ";
	
	private Map<Integer,IndexReader> docToReaderMap;
	private IndexReader lastReader = null;
	
	@SuppressWarnings("unused")
	// TODO: REMOVE ME?
	private int lastDocBase = 0;

	/**
	 * TODO: check constructor filter nDocs and sort aren't need. IOException, too
	 * constructor
	 * @param filter 
	 * @param nDocs 
	 * @param sort 
	 * @throws IOException 
	 */
	public TagCountCollector(Filter filter, int nDocs, Sort sort) throws IOException {
		// instantiate collector
		this.docToReaderMap = new HashMap<Integer, IndexReader>();
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collect(int doc) throws IOException {
		docToReaderMap.put(doc, lastReader);
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase) throws IOException {
		this.lastDocBase = docBase;
		this.lastReader  = reader;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
	}
	
	/**
	 * fetches tags and their corresponding counts from collected documents
	 * 
	 * @param searcher index searcher for accessing documents
	 * @return the tags and their corresponding counts from collected documents
	 */
	public List<Tag> getTags(Searcher searcher) {
		Map<String,Integer> tagCounter = new HashMap<String,Integer>();
		
		log.debug("Start extracting tags from index...");
		List<Tag> tags = new LinkedList<Tag>();
		for (Integer docId : docToReaderMap.keySet()) {
			try {
				FieldSelector tasSelector = new MapFieldSelector(FLD_TAS); 
				Document doc = docToReaderMap.get(docId).document(docId, tasSelector);
				String tagsString = doc.get(FLD_TAS);
				if (present(tagsString)) {
					for (String tag : tagsString.split(CFG_LIST_DELIMITER)) {
						Integer oldCnt = tagCounter.get(tag);
						if (!present(oldCnt) ) {
							oldCnt = 1;
						} else {
							oldCnt += 1;
						}
						tagCounter.put(tag, oldCnt);
					}
				}
			} catch (IOException e) {
				log.error("Error fetching document " + docId + " from index.", e);
			}

		}
		log.debug("Done extracting tags from index...");
		
		// extract all tags
		for (Map.Entry<String,Integer> entry : tagCounter.entrySet()) {
			final Tag transientTag = new Tag();
			transientTag.setName(entry.getKey());
			transientTag.setUsercount(entry.getValue());
			transientTag.setGlobalcount(entry.getValue()); // FIXME: we set user==global count
			tags.add(transientTag);
		}
		log.debug("Done converting tag list");
		
		return tags;
	}
}
