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

package org.bibsonomy.lucene.index;

import static org.bibsonomy.lucene.util.LuceneBase.CFG_INDEX_ID_DELIMITER;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_LUCENE_INDEX_PREFIX;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_CONTENT_ID;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_LAST_LOG_DATE;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_LAST_TAS_ID;
import static org.bibsonomy.lucene.util.LuceneBase.FLD_USER_NAME;
import static org.bibsonomy.lucene.util.LuceneBase.getIndexBasePath;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.StaleReaderException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.bibsonomy.lucene.param.LuceneIndexStatistics;
import org.bibsonomy.lucene.param.comparator.DocumentCacheComparator;
import org.bibsonomy.lucene.util.LuceneBase;
import org.bibsonomy.model.Resource;

/**
 * abstract base class for managing lucene resource indices
 * 
 * @author fei
 * @version $Id: LuceneResourceIndex.java,v 1.40 2011-05-28 12:27:35 nosebrain Exp $
 *
 * @param <R> the resource of the index
 */
public abstract class LuceneResourceIndex<R extends Resource> {
	protected static final Log log = LogFactory.getLog(LuceneResourceIndex.class);

	/** coding whether index is opened for writing or reading */
	public static enum AccessMode {
		/**
		 * none
		 */
		None,
		
		/**
		 * read only
		 */
		ReadOnly,
		
		/**
		 * write only
		 */
		WriteOnly;
	}
	
	/** indicating whether index is opened for writing or reading */
	private AccessMode accessMode;

	/** gives read only access to the lucene index */
	protected IndexReader indexReader;

	/** gives write access to the lucene index */
	protected IndexWriter indexWriter;
	
	/** path to the lucene index */
	private String luceneIndexPath;

	/** directory of the lucene index */
	private Directory indexDirectory;

	/** default field tokenizer */
	private Analyzer analyzer;
	
	/** list containing content ids of cached delete operations */
	private final List<Integer> contentIdsToDelete;

	/** list posts to insert into index */
	protected Set<Document> postsToInsert;
	
	/** 
	 * set of usernames which where flagged as spammers since last update
	 * which should be removed from index during next update (blocking new posts
	 * to be inserted for given users) 
	 */
	private final Set<String> usersToFlag;

	/** flag indicating whether the index should be optimized during next update */
	private boolean optimizeIndex;
	
	/** flag indicating whether the index was cleanly initialized */
	private boolean isReady = false;
	
	/** id for identifying redundant resource indeces */
	private int indexId;
	
	/** keeps track of the newest log_date during last index update */
	private Long lastLogDate;
	
	/** keeps track of the newest tas_id during last index update */
	private Integer lastTasId;
	
	/**
	 * constructor disabled
	 */
	protected LuceneResourceIndex(final int indexId) {
		// init data structures
		this.contentIdsToDelete = new LinkedList<Integer>();
		this.postsToInsert = new TreeSet<Document>(new DocumentCacheComparator());
		this.usersToFlag = new TreeSet<String>();
		this.optimizeIndex = false;
		
		this.indexId = indexId;
		

		try {
			init();
		} catch (final Exception e) {
			disableIndex();
		}
	}
	
	/**
	 * Get Statistics for this index
	 * @return LuceneIndexStatistics for this index
	 */
	public LuceneIndexStatistics getStatistics() {
            final LuceneIndexStatistics statistics = new LuceneIndexStatistics();
	    synchronized(this) {
    	    	this.ensureReadAccess();
                
                statistics.setNumDocs(this.indexReader.numDocs());
                statistics.setNumDeletedDocs(this.indexReader.numDeletedDocs());
                statistics.setCurrentVersion(indexReader.getVersion());
                try {
    		    statistics.setCurrent(indexReader.isCurrent());
                    statistics.setLastModified(IndexReader.lastModified(indexReader.directory()));
		} catch (final IOException e1) {
		    log.error(e1);
		}
	    }
	    
	    statistics.setNewestRecordDate(this.getLastLogDate());
	    
            return statistics;
	}

	/** 
	 * Close index-writer and index-reader and disable this index.
	 * @throws CorruptIndexException 
	 * @throws IOException 
	 */
	public void close() throws CorruptIndexException, IOException{
	    if (indexWriter != null) {
	    	this.indexWriter.close();
	    }
	    if (indexReader != null) {
	    	this.indexReader.close();
	    }
	    
	    disableIndex();
	}
	
	/**
	 * initialize internal data structures
	 * @throws IOException 
	 */
	private void init() throws IOException {
		LuceneBase.initRuntimeConfiguration();
		this.luceneIndexPath = getIndexBasePath()+CFG_LUCENE_INDEX_PREFIX+getResourceName()+CFG_INDEX_ID_DELIMITER+getIndexId();

		indexDirectory = FSDirectory.open(new File(luceneIndexPath));
		try {
			if (IndexWriter.isLocked(indexDirectory)) {
				log.error("WARNING: Index "+luceneIndexPath+" is locked - forcibly unlock the index.");
				IndexWriter.unlock(indexDirectory);
				log.error("OK. Index unlocked.");
			}
		} catch (final IOException e) {
			log.fatal("Failed to unlock the index - dying.");
			throw e; 
		}
		
		try {
			openIndexReader();
		} catch (final IOException e) {
			log.error("Error opening IndexReader (" + e.getMessage() + ") - This is ok while creating a new index.");
			throw e;
		}
		
		// everything went fine - enable the index
		enableIndex();
	}
	
	
	/**
	 * @return the latest log_date[ms] from index 
	 */
	public long getLastLogDate() {
		// FIXME: this synchronisation is very inefficient 
		synchronized(this) {
			if (!isIndexEnabled()) {
				return Long.MAX_VALUE;
			} else if (this.lastLogDate != null) {
				return this.lastLogDate;
			}
			
			//----------------------------------------------------------------
			// search over all elements sort them reverse by date 
			// and return 1 top document (newest one)
			//----------------------------------------------------------------
			// get all documents
			final Query matchAll = new MatchAllDocsQuery();
			// sort by last_log_date of type LONG in reversed order 
			final Sort sort = new Sort(new SortField(FLD_LAST_LOG_DATE, SortField.LONG, true));
			
			final Document doc = searchIndex(matchAll, 1, sort);
			if (doc != null) {
				try {
					// parse date
					return Long.parseLong(doc.get(FLD_LAST_LOG_DATE));
				} catch (final NumberFormatException e) {
					log.error("Error parsing last_log_date " + doc.get(FLD_LAST_LOG_DATE));
				}
			}

			return Long.MAX_VALUE;
		}
	}
	
	/**
	 * set newest log_date[ms] 
	 * @param lastLogDate the lastLogDate to set
	 */
	public void setLastLogDate(final Long lastLogDate) {
		this.lastLogDate = lastLogDate;
	}
	
	/** 
	 * @return the newest tas_id from index
	 */
	public Integer getLastTasId() {
		synchronized(this) {
			if (!isIndexEnabled()) {
				return Integer.MAX_VALUE;
			} else if (this.lastTasId != null) {
				return this.lastTasId;
			}
			
			//----------------------------------------------------------------
			// search over all elements sort them reverse by last_tas_id
			// and return 1 top document (newest one)
			//----------------------------------------------------------------
			// get all documents
			final Query matchAll = new MatchAllDocsQuery();
			// order by last_tas_id of type INT in reversed order
			final Sort sort = new Sort(new SortField(FLD_LAST_TAS_ID, SortField.INT, true));
	
			Integer lastTasId = null;
			final Document doc = searchIndex(matchAll, 1, sort);
			if (doc != null) {
				try {
					lastTasId = Integer.parseInt(doc.get(FLD_LAST_TAS_ID));
				} catch (final NumberFormatException e) {
					log.error("Error parsing last_tas_id " + doc.get(FLD_LAST_TAS_ID));
				}
			}
			
			return lastTasId != null ? lastTasId : Integer.MAX_VALUE;
		}
	}
	
	/** 
	 * @param lastTasId the lastTasId to set
	 */
	public void setLastTasId(final Integer lastTasId) {
		this.lastTasId = lastTasId;
	}
	
	/**
	 * triggers index optimization during next update
	 */
	public void optimizeIndex() {
		synchronized(this) {
			this.optimizeIndex = true;
		}
	}

	/**
	 * flag given user as spammer - preventing further posts to be inserted and
	 * mark user's posts for deletion from index
	 * 
	 * @param username
	 */
	public void flagUser(final String username) {
		synchronized(this) {
			this.usersToFlag.add(username);
		}
	}
	
	/**
	 * unflag given user as spammer - enabling further posts to be inserted 
	 * 
	 * @param userName
	 */
	public void unFlagUser(final String userName) {
		synchronized(this) {
			this.usersToFlag.remove(userName);
		}
	}
	
	/**
	 * cache given post for deletion
	 * 
	 * @param contentId post's content id 
	 */
	public void deleteDocumentForContentId(final Integer contentId) {
		synchronized(this) {
			this.contentIdsToDelete.add(contentId);
		}
	}

	/**
	 * cache given posts for deletion
	 * 
	 * @param contentIdsToDelete list of content ids which should be removed from the index
	 */
	public void deleteDocumentsInIndex(final List<Integer> contentIdsToDelete) {
		synchronized(this) {
			this.contentIdsToDelete.addAll(contentIdsToDelete);
		}
	}
	
	/**
	 * cache given post for insertion
	 * 
	 * @param doc post document to insert into the index
	 */
	public void insertDocument(final Document doc) {
		synchronized(this) {
			this.postsToInsert.add(doc);
		}
	}

	/**
	 * cache given post for insertion
	 * 
	 * @param docs post documents to insert into the index
	 */
	public void insertDocuments(final List<Document> docs) {
		synchronized(this) {
			this.postsToInsert.addAll(docs);
		}
	}
	
	/**
	 * perform all cached operations to index
	 */
	public void flush() {
		synchronized(this) {
			if (!isIndexEnabled()) {
				return;
			}
			
			boolean readUpdate  = false;
			boolean writeUpdate = false;
			//----------------------------------------------------------------
			// remove cached posts from index
			//----------------------------------------------------------------
			log.debug("Performing " + contentIdsToDelete.size() + " delete operations");
			if ((contentIdsToDelete.size() > 0) || (usersToFlag.size() > 0) ) {
				this.ensureReadAccess();
				
				// remove each cached post from index
				for (final Integer contentId : this.contentIdsToDelete) {
					try {
						this.purgeDocumentForContentId(contentId);
						log.debug("deleted post " + contentId);
					} catch (final IOException e) {
						log.error("Error deleting post " + contentId + " from index", e);
					}
				}
				
				// remove spam posts form index
				for (final String userName : this.usersToFlag) {
					try {
						final int cnt = purgeDocumentsForUser(userName);
						log.debug("Purged " + cnt + " posts for user " + userName);
					} catch (final IOException e) {
						log.error("Error deleting spam posts for user " + userName + " from index", e);
					}
				}
				
				readUpdate = true;
			}

			//----------------------------------------------------------------
			// add cached posts to index
			//----------------------------------------------------------------
			log.debug("Performing " + postsToInsert.size() + " insert operations");
			if (this.postsToInsert.size() > 0) {
				this.ensureWriteAccess();
				try {
					this.insertRecordsIntoIndex(postsToInsert);
				} catch (final IOException e) {
					log.error("Error adding posts to index.", e);
				}
				writeUpdate = true;
			}
			
			//----------------------------------------------------------------
			// clear all cached data
			//----------------------------------------------------------------
			this.postsToInsert.clear();
			this.contentIdsToDelete.clear();
			this.usersToFlag.clear();
			
			//----------------------------------------------------------------
			// commit reader-changes 
			//----------------------------------------------------------------
			// FIXME: this is a bit ugly...
			if (readUpdate && !writeUpdate) {
				try {
					closeIndexReader();
					openIndexReader();
				} catch (final IOException e) {
					log.error("Error commiting index update.", e);
				}
			} else {
				ensureReadAccess();
			}
		}
	}

	
	/**
	 * closes all writer and reader and reopens the index reader
	 */
	public void reset() {
		synchronized(this) {
			if (!isIndexEnabled()) {
				try {
					init();
				} catch (final Exception e) {
					return;
				}
			}
			switch (this.accessMode) {
			case ReadOnly:
				accessMode = AccessMode.None;
				try {
					closeIndexReader();
				} catch (final IOException e) {
					log.error("IOException while closing index reader", e);
				}
				try {
					openIndexReader();
				} catch (final IOException e) {
					log.error("Error opening index reader", e);
				}
				break;
			case WriteOnly:
				accessMode = AccessMode.None;
				try {
					closeIndexWriter();
				} catch (final IOException e) {
					log.error("IOException while closing index reader", e);
				}
				try {
					openIndexWriter();
				} catch (final IOException e) {
					log.error("Error opening index reader", e);
				}
				break;
			default:
				// nothing to do
			}

			// delete the lists
			this.postsToInsert.clear();
			this.contentIdsToDelete.clear();
			this.usersToFlag.clear();
			
			// reset the cached query parameters
			this.lastLogDate = null;
			this.lastTasId = null;
		}
	}	
	
	//------------------------------------------------------------------------
	// private index access interface
	//------------------------------------------------------------------------
	/**
	 * write given post into the index
	 * 
	 * @param post
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private void insertRecordIntoIndex(final Document post) throws CorruptIndexException, IOException {
		if( !this.usersToFlag.contains(post.get(FLD_USER_NAME)) ) { 
			// skip users which where flagged as spammers
			indexWriter.addDocument(post);
		}
	}	

	/**
	 * write given post into the index
	 * 
	 * @param post
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private void insertRecordsIntoIndex(final Collection<Document> posts) throws CorruptIndexException, IOException {
		for( final Document post : posts ) {
			this.insertRecordIntoIndex(post);
		}
	}
	
	/**
	 * query the index
	 * 
	 * @param searchQuery the search query
	 * @param hitsPerPage maximal number of result items to retrieve
	 * @param ordering sort ordering
	 * @return
	 */
	private Document searchIndex(final Query searchQuery, final int hitsPerPage, final Sort ordering) {
		// prepare the index searcher
		this.ensureReadAccess();
		final IndexSearcher searcher = new IndexSearcher(indexReader);

		// query the index
		try {
			final TopDocs topDocs = searcher.search(searchQuery, null, hitsPerPage, ordering);
			if (topDocs.totalHits > 0) {
				return searcher.doc(topDocs.scoreDocs[0].doc);
			}
		} catch (final Exception e) {
			log.error("Error performing index search in file " + this.luceneIndexPath, e);
		} finally {
			try {
				searcher.close();
			} catch (final IOException e) {
				log.error("Error closing index "+this.luceneIndexPath+" for searching", e);
			}
		}
		
		return null;
	}	
	
	/**
	 * removes given post from index
	 * 
	 * @param contentId post's content id 
	 * @return number of posts deleted from index
	 * 
	 * @throws StaleReaderException
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	private int purgeDocumentForContentId(final Integer contentId) throws StaleReaderException, CorruptIndexException, LockObtainFailedException, IOException {
		final Term term = new Term(FLD_CONTENT_ID, contentId.toString());
		return purgeDocuments(term);
	}
	
	/**
	 * delete all documents of a given user from index
	 * 
	 * @param username
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private int purgeDocumentsForUser(final String username) throws CorruptIndexException, IOException {
		// delete each post owned by given user
		final Term term = new Term(FLD_USER_NAME, username);
		return purgeDocuments(term);
	}

	/**
	 * remove posts matching to given search term from index
	 * 
	 * @param searchTerm
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private int purgeDocuments(final Term searchTerm) throws CorruptIndexException, IOException {
		return this.indexReader.deleteDocuments(searchTerm);
	}
	
	/**
	 * sets access mode to read-only
	 */
	protected void ensureReadAccess() {
		//--------------------------------------------------------------------
		// open index for reading
		//--------------------------------------------------------------------
		// close IndexWriter
		if (accessMode != AccessMode.ReadOnly) {
			try {
				closeIndexWriter();
			} catch (final IOException e) {
				log.error("IOException while closing indexwriter", e);
			}
			accessMode = AccessMode.None;
			try {
				openIndexReader();
			} catch (final IOException e) {
				log.error("Error opening index reader", e);
			}
		}
	}

	protected void openIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
		log.debug("Opening index " + luceneIndexPath + " for writing");
		indexWriter = new IndexWriter(indexDirectory, getAnalyzer(), false, IndexWriter.MaxFieldLength.UNLIMITED);
		accessMode = AccessMode.WriteOnly;
	}

	protected void closeIndexWriter() throws CorruptIndexException, IOException {
		if (this.indexWriter == null) {
			return;
		}
		
		log.debug("Closing index " + luceneIndexPath + " for writing");
		indexWriter.commit();
		// optimize index if requested
		if( this.optimizeIndex ) {
			log.debug("optimizing index " + luceneIndexPath);
			indexWriter.optimize();
			log.debug("optimizing index " + luceneIndexPath + " DONE");
			this.optimizeIndex = false;
		}
		// close index for writing
		indexWriter.close();
	}

	protected void openIndexReader() throws CorruptIndexException, IOException {
		log.debug("Opening index " + luceneIndexPath + " for reading");
		this.indexReader = IndexReader.open(indexDirectory, false);
		this.accessMode = AccessMode.ReadOnly;
	}

	protected void closeIndexReader() throws IOException {
		log.debug("Closing index "+luceneIndexPath+" for reading");
		indexReader.close();
	}

	/**
	 * sets access mode to write-only
	 */
	protected void ensureWriteAccess() {
		//--------------------------------------------------------------------
		// open index for reading
		//--------------------------------------------------------------------
		// close IndexWriter
		if (this.accessMode != AccessMode.WriteOnly) {
			try {
				closeIndexReader();
			} catch (final IOException e) {
				log.error("IOException while closing index reader", e);
			}
			this.accessMode = AccessMode.None;
			try {
				openIndexWriter();
			} catch (final IOException e) {
				log.error("Error opening index writer", e);
			}
		}
	}
	
	/**
	 * disable this index when open fails
	 */
	public void disableIndex() {
		this.isReady = false;
	}
	
	/**
	 * enable this index
	 */
	public void enableIndex() {
		this.isReady = true;
	}

	/**
	 * checks, whether the index is readily initialized
	 * @return true, if index is ready - false, otherwise
	 */
	public boolean isIndexEnabled() {
		return this.isReady;
	}
	
	/**
	 * get managed resource type
	 */
	protected abstract Class<? extends Resource> getResourceType();
	
	/**
	 * @return the managed resource name
	 */
	public String getResourceName() {
		String name = getResourceType().getCanonicalName();
		if (name.lastIndexOf('.') > 0) {
	        name = name.substring(name.lastIndexOf('.')+1);
	    }
		
		return name;
	}
	
	/**
	 * @return the postsToInsert
	 */
	public Set<Document> getPostsToInsert() {
		return this.postsToInsert;
	}

	/**
	 * @return the usersToFlag
	 */
	public Set<String> getUsersToFlag() {
		return usersToFlag;
	}

	/**
	 * @return the analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer the analyzer to set
	 */
	public void setAnalyzer(final Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @return the indexId
	 */
	public int getIndexId() {
		return indexId;
	}

	/**
	 * @param indexId the indexId to set
	 */
	public void setIndexId(final int indexId) {
		this.indexId = indexId;
	}
}
