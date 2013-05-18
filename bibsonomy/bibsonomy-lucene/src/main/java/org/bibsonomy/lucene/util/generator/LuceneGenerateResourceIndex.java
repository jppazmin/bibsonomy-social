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

import static org.bibsonomy.lucene.util.LuceneBase.CFG_INDEX_ID_DELIMITER;
import static org.bibsonomy.lucene.util.LuceneBase.CFG_LUCENE_INDEX_PREFIX;
import static org.bibsonomy.lucene.util.LuceneBase.SQL_BLOCKSIZE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NoSuchDirectoryException;
import org.bibsonomy.lucene.database.LuceneDBInterface;
import org.bibsonomy.lucene.param.LucenePost;
import org.bibsonomy.lucene.util.LuceneBase;
import org.bibsonomy.lucene.util.LuceneResourceConverter;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * reads bibsonomy data from database and builds lucene index for all resource
 * entries
 * 
 * @author sst
 * @author fei
 * @version $Id: LuceneGenerateResourceIndex.java,v 1.10 2011-05-28 12:27:35 nosebrain Exp $
 * 
 * @param <R> the resource of the index to generate
 */
public abstract class LuceneGenerateResourceIndex<R extends Resource> implements Runnable {
	protected static final Log log = LogFactory.getLog(LuceneGenerateResourceIndex.class);

	/** database logic */
	protected LuceneDBInterface<R> dbLogic;

	/** path to the bookmark index */
	private String luceneResourceIndexPath;
	
	/** maximum length of fields in the lucene index */
	private IndexWriter.MaxFieldLength mfl;
	
	/** writes the bookmark index */
	protected IndexWriter indexWriter;

	/** default analyzer */
	private Analyzer analyzer = null;
	
	/** converts post model objects to lucene documents */
	private LuceneResourceConverter<R> resourceConverter;
	
	private GenerateIndexCallback callback = null;
	
	/** the progress-percentage if index-generation is running */
	private int progressPercentage;
	
	/** set to true if the generator is currently generating an index */
	private boolean isRunning;
	
	/**
	 * constructor 
	 */
	public LuceneGenerateResourceIndex() {
		// load configuration
		init();
	}

	/** 
	 * reads in parameters from the properties file and stores them in 
	 * the corresponding attributes 
	 */
	private void init() {
		// initialize run time configuration
		LuceneBase.initRuntimeConfiguration();
		
		// load the db drivers
		try {
			Class.forName(LuceneBase.getDbDriverName());
		} catch( final Exception e ) {
			log.error("Error loading the mysql driver. Please check, that the mysql connector library is available. ["+e.getMessage()+"]");
		}
		
		// 1) index files
		this.luceneResourceIndexPath = LuceneBase.getIndexBasePath() + CFG_LUCENE_INDEX_PREFIX + getResourceName();//props.getProperty(LUCENE_INDEX_PATH_PREFIX + getResourceName());
		
		// 2) maximal field width in the index
		this.mfl = LuceneBase.getMaximumFieldLength();
	}

	/**
	 * frees allocated resources and closes all files
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 */
	public void shutdown() throws CorruptIndexException, IOException {
		indexWriter.close();
		
		if(callback != null) {
		    callback.done();
			callback = null;
		}
	}
	
	/**
	 * Read in data from database and build index. 
	 * 
	 * Database as well as index files are configured in the lucene.properties file.
	 * 
	 * @param copyRedundantIndices if set to true, the newly generated index will also be copied to the redunant indices
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void generateIndex(final boolean copyRedundantIndices) throws CorruptIndexException, IOException, ClassNotFoundException, SQLException {
		// Allow only one index-generation at a time.
		if(isRunning) return;
		isRunning = true;
		
		// Delete the old index, if exists
    	deleteIndex(0);
    	
		// open index
		createEmptyIndex();

		// generate index
		createIndexFromDatabase();
		
		// create redundant indeces
		if(copyRedundantIndices) {
			log.info("Creating "+ LuceneBase.getRedundantCnt() + " redundant indeces.");
			this.copyRedundantIndeces();
		}
		
		isRunning = false;
	}
	
	/**
	 * Generate Index including redundant indices
	 * @throws CorruptIndexException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public void generateIndex() throws CorruptIndexException, IOException, ClassNotFoundException, SQLException {
		generateIndex(true);
	}
	
	/**
	 * Create empty index. Attributes must already be configured (via init()).
	 *  
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public void createEmptyIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
		// create index, possibly overwriting existing index files
		log.info("Creating empty lucene index...");
		final Directory indexDirectory = FSDirectory.open(new File(this.luceneResourceIndexPath + CFG_INDEX_ID_DELIMITER + "0"));
		indexWriter  = new IndexWriter(indexDirectory, getAnalyzer(), true, mfl); 
	}
	
	/**
	 * creates index of bookmark entries
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public void createIndexFromDatabase() throws CorruptIndexException, IOException {
		log.info("Filling index with database post entries.");

		// set up resource specific data structures
		setUp();
		
		// number of post entries to calculate progress
		//FIXME: the number of posts is wrong
		final int numberOfPosts = this.dbLogic.getNumberOfPosts();
		progressPercentage = 0;
		log.info("Number of post entries: "+  this.dbLogic.getNumberOfPosts());
		
		
		// initialize variables
		final Integer lastTasId = this.dbLogic.getLastTasId();
		Date lastLogDate  = this.dbLogic.getLastLogDate();
		
		if (lastLogDate == null) {
		    lastLogDate = new Date(System.currentTimeMillis() - 1000);
		}
		
		// get all relevant bookmarks from bookmark table
		int i    = 0;		// number of evaluated entries 
		int is   = 0;		// number of spam entries 

		log.info("Start writing data to lucene index");
		
		// read block wise all posts
		List<LucenePost<R>> postList = null;
		int skip = 0;
		do {
			postList = this.dbLogic.getPostEntries(skip, SQL_BLOCKSIZE);
			skip += postList.size(); // TODO: += SQL_BLOCKSIZE?!
			log.info("Read " + skip + " entries.");

			// cycle through all posts of currently read block
			for (final LucenePost<R> postEntry : postList) {
				// update management fields
				postEntry.setLastLogDate(lastLogDate);
				postEntry.setLastTasId(lastTasId);
				
				// create index document from post model
				final Document post = this.resourceConverter.readPost(postEntry);

				// add (non-spam) document to index
				// FIXME: is this check necessary?
				if (isNotSpammer(postEntry)) {
					indexWriter.addDocument(post);
					i++;
				} else {
					is++;
				}			
			}
			progressPercentage = (int) Math.round(100 * ((double)skip/numberOfPosts));
			log.info(progressPercentage + "% of index-generation done!");
			
		} while (postList.size() == SQL_BLOCKSIZE);

		// optimize index
		log.info("optimizing index " + luceneResourceIndexPath);
		indexWriter.optimize();
		
		// close bookmark-indexWriter
		log.info("closing index " + luceneResourceIndexPath);
		indexWriter.close();

		// all done
		log.info("(" + i + " indexed entries, " + is + " not indexed spam entries)");
	}
	
	/**
	 * Get the progress-percentage
	 * @return the progressPercentage
	 */
	public int getProgressPercentage() {
		return progressPercentage;
	}
	
	
	/** Run the index-generation in a thread. */
	@Override
	public void run() {
        try {
			generateIndex(false);
		} catch (final Exception e) {
			log.error("Failed to generate " + getResourceName() + "-index!", e);
		} finally {
			try {
				shutdown();
			} catch (final Exception e) {
				log.error("Failed to close index-writer!", e);
			}
		}
	}
	

	/**
	 * test if given post is a spam post
	 * 
	 * @param post
	 * @return <code>true</code> iff the post user is a spammer
	 */
	protected boolean isNotSpammer(final Post<? extends Resource> post) {
		for (final Group group : post.getGroups()) {
			if (group.getGroupId() < 0) {
				/*
				 * spammer group found => user is spammer
				 */
				return false;
			}
		}
		return true;
	}

	//------------------------------------------------------------------------
	// private helper
	//------------------------------------------------------------------------
	/**
	 * copy created index to redundant indeces
	 */
	public void copyRedundantIndeces() {
		final File inputFile = new File(this.luceneResourceIndexPath + CFG_INDEX_ID_DELIMITER + "0");
		for(int i = 1; i < LuceneBase.getRedundantCnt(); i++ ) {
			try {
				deleteIndex(i);
				final File outputFile = new File(this.luceneResourceIndexPath + CFG_INDEX_ID_DELIMITER + i);
				log.info("Copying index "+i);
				copyDirectory(inputFile, outputFile);
				log.info("Done.");
			} catch( final Exception e) {
				log.error("Error copying index to index file " + i, e);
			}
		}
	}
	
	/** 
	 * Delete one index identified by its id
	 * @param id the index-id
	 * */
	public void deleteIndex(final int id) {
		try {
			final File directory = new File(this.luceneResourceIndexPath + CFG_INDEX_ID_DELIMITER + id);
			final Directory indexDirectory = FSDirectory.open(directory);
			
			log.info("Deleting index " + directory.getAbsolutePath() + "...");
			for(final String filename: indexDirectory.listAll()) {
			    indexDirectory.deleteFile(filename);
			    log.debug("Deleted " + filename);
			}
			log.info("Success.");
		} catch (final NoSuchDirectoryException e) {
			log.warn("Tried to delete the lucene-index-directory but it could not be found.", e);
		}
		catch (final IOException e) {
			log.error("Could not delete directory-content before index-generation or index-copy.", e);
		}
	}
	
	/**
	 * Copies all files under srcDir to dstDir.
 	 * If dstDir does not exist, it will be created.
	 * @param srcDir
	 * @param dstDir
	 * @throws IOException
	 */
    public void copyDirectory(final File srcDir, final File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
    
            final String[] children = srcDir.list();
            for (final String child : children) {
        	copyDirectory(new File(srcDir, child), new File(dstDir, child));
	    }
        } else {
            copyFile(srcDir, dstDir);
        }
    }
	
	/**
	 * Fast & simple file copy.
	 * 
	 * @param source 
	 * @param dest 
	 * @throws IOException 
	 */
	public static void copyFile(final File source, final File dest) throws IOException {
	     FileChannel in = null, out = null;
	     try {          
	          in = new FileInputStream(source).getChannel();
	          out = new FileOutputStream(dest).getChannel();
	 
	          final long size = in.size();
	          final MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
	 
	          out.write(buf);
	 
	     } finally {
	          if (in != null) in.close();
	          if (out != null) out.close();
	     }
	}
	
	/**
	 * @return get managed resource name
	 */
	protected abstract String getResourceName();
	
	/** set up resource specific data structures */
	protected void setUp() {
		// noop
	}

	/**
	 * @return the dbLogic
	 */
	public LuceneDBInterface<R> getLogic() {
		return dbLogic;
	}

	/**
	 * @param dbLogic the dbLogic to set
	 */
	public void setLogic(final LuceneDBInterface<R> dbLogic) {
		this.dbLogic = dbLogic;
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
	 * @return the resourceConverter
	 */
	public LuceneResourceConverter<R> getResourceConverter() {
		return resourceConverter;
	}

	/**
	 * @param resourceConverter the resourceConverter to set
	 */
	public void setResourceConverter(final LuceneResourceConverter<R> resourceConverter) {
		this.resourceConverter = resourceConverter;
	}

	/**
	 * @param callback the callback to set
	 *  */
	public void registerCallback(final GenerateIndexCallback callback) {
		this.callback = callback;
	}
}
