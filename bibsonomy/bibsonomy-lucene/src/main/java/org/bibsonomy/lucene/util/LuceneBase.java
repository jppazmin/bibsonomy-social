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

import static org.bibsonomy.util.ValidationUtils.present;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;
import org.bibsonomy.lucene.param.LuceneConfig;

/**
 * this class is a temporary hack for collecting all constants which should be consistent 
 * throughout the module
 * 
 *  FIXME: this should be consistent with the spring configuration
 *  
 * @author fei
 * @version $Id: LuceneBase.java,v 1.15 2010-10-13 11:31:53 nosebrain Exp $
 */
public class LuceneBase {
	private static final Log log = LogFactory.getLog(LuceneBase.class);
	
	/** TODO: improve documentation */
	public static final String PARAM_RELEVANCE = "relevance";

	/** TODO: improve documentation */
	public static final String CFG_LUCENENAME = "luceneName";
	/** TODO: improve documentation */
	public static final String CFG_ANALYZER = "fieldAnalyzer";
	/** TODO: improve documentation */
	public static final String CFG_TYPEHANDLER = "typeHandler";
	/** TODO: improve documentation */
	public static final String CFG_ITEMPROPERTY = "itemProperty";
	/** TODO: improve documentation */
	public static final String CFG_LIST_DELIMITER = " ";
	/** TODO: improve documentation */
	public static final String CFG_FLDINDEX = "luceneIndex";
	/** TODO: improve documentation */
	public static final String CFG_FLDSTORE = "luceneStore";
	/** TODO: improve documentation */
	public static final String CFG_FULLTEXT_FLAG = "fulltextSearch";
	/** TODO: improve documentation */
	public static final String CFG_PRIVATE_FLAG = "privateSearch";
	/** TODO: improve documentation */
	public static final String CFG_INDEX_ID_DELIMITER = "-";
	
	/** delimiter to specify which field to search for */
	public static final String CFG_LUCENE_FIELD_SPECIFIER = ":";
	
	/** directory prefix for different resource indeces */
	public static final String CFG_LUCENE_INDEX_PREFIX = "lucene_";
	
	/** max. number of posts to consider for building the author tag cloud */
	public static final Integer CFG_TAG_CLOUD_LIMIT = Integer.MAX_VALUE;
	
	/** the naming context for lucene classes */
	public static final String CONTEXT_ENV_NAME    = "java:/comp/env";
	
	/** context variable containing lucene's configuration */
	public static final String CONTEXT_CONFIG_BEAN = "luceneConfig";

	// FIXME: configure these fieldnames via spring
	public static final String FLD_MERGEDFIELDS  = "mergedfields";
	public static final String FLD_PRIVATEFIELDS = "privatefields";
	public static final String FLD_INTRAHASH     = "intrahash";
	public static final String FLD_INTERHASH     = "interhash";
	public static final String FLD_GROUP         = "group";
	public static final String FLD_AUTHOR        = "author";
	public static final String FLD_USER          = "user_name";
	public static final String FLD_DATE          = "date";
	public static final String FLD_YEAR          = "year";
	public static final String FLD_TAS           = "tas";	
	public static final String FLD_ADDRESS       = "address";
	public static final String FLD_TITLE         = "title";	
	public static final String FLD_LAST_TAS_ID   = "last_tas_id";
	public static final String FLD_LAST_LOG_DATE = "last_log_date";
	public static final String FLD_USER_NAME     = "user_name";
	public static final String FLD_CONTENT_ID    = "content_id";
	
	/** keyword identifying unlimited field length in the lucene index */
	public static final String KEY_UNLIMITED     = "UNLIMITED";
	/** keyword identifying limited field length in the lucene index */
	public static final Object KEY_LIMITED       = "LIMITED";

	/** TODO: improve documentation */
	public static final int SQL_BLOCKSIZE = 25000;
	
	//------------------------------------------------------------------------
	// runtime configuration
	//------------------------------------------------------------------------
	private static String indexBasePath = "";
	private static String searchMode = "database";
	private static Boolean enableUpdater = false;
	private static Boolean loadIndexIntoRam = false;
	private static String dbDriverName = "com.mysql.jdbc.Driver";
	private static IndexWriter.MaxFieldLength maximumFieldLength = new IndexWriter.MaxFieldLength(5000);
	private static Integer redundantCnt = 2;
	private static Boolean enableTagClouds = false;
	private static Integer tagCloudLimit = 1000;
	
	/**
	 * get runtime configuration from context
	 */
	public static void initRuntimeConfiguration() {
		try {
			final Context initContext = new InitialContext();
			final Context envContext  = (Context) initContext.lookup(CONTEXT_ENV_NAME);
			final LuceneConfig config = (LuceneConfig)envContext.lookup(CONTEXT_CONFIG_BEAN);
			
			// index base path
			setIndexBasePath(config.getIndexPath());
			// search mode
			if (present(config.getSearchMode())) {
			    searchMode = config.getSearchMode();
			}
			
			// db driver name
			if (present(config.getDbDriverName())) {
			    dbDriverName = config.getDbDriverName();
			}
			
			// maximum field length in the lucene index
			if (present(config.getMaximumFieldLength())) {
				String mflIn = config.getMaximumFieldLength();
				if (KEY_UNLIMITED.equals(mflIn)) {
					maximumFieldLength = IndexWriter.MaxFieldLength.UNLIMITED;
				} else if (KEY_LIMITED.equals(mflIn)) {
					maximumFieldLength = IndexWriter.MaxFieldLength.LIMITED;
				} else {
					Integer value;
					try {
						value = Integer.parseInt(mflIn);
					} catch (NumberFormatException e) {
						value = IndexWriter.DEFAULT_MAX_FIELD_LENGTH;
					}
					maximumFieldLength = new IndexWriter.MaxFieldLength(value);
				}				
			}
			
			// nr. of redundant indeces
			if (present(config.getRedundantCnt())) {
				Integer value;
				try {
					value = Integer.parseInt(config.getRedundantCnt());
				} catch (NumberFormatException e) {
					value = IndexWriter.DEFAULT_MAX_FIELD_LENGTH;
				}
				redundantCnt = value;
			}
			
			// enable/disable tag cloud on search pages
			setEnableTagClouds(Boolean.valueOf(config.getEnableTagClouds()));
			
			// limit number of posts to consider for building the tag cloud
			setTagCloudLimit(Integer.valueOf(config.getTagCloudLimit()));
			
			setEnableUpdater(Boolean.valueOf(config.getEnableUpdater()));
			loadIndexIntoRam = Boolean.valueOf(config.getLoadIndexIntoRam());
		} catch (Exception e) {
			log.error("Error requesting JNDI environment variables", e);
		}
		
		// done - print out debug information
		log.debug("\t indexBasePath    : " + getIndexBasePath());
		log.debug("\t searchMode       : " + searchMode);
		log.debug("\t enableUpdater    : " + getEnableUpdater());
		log.debug("\t loadIndexIntoRam : " + loadIndexIntoRam);
	}

	/**
	 * @return the indexBasePath
	 */
	public static String getIndexBasePath() {
		return indexBasePath;
	}

	/**
	 * @param indexBasePath the indexBasePath to set
	 */
	public static void setIndexBasePath(String indexBasePath) {
		LuceneBase.indexBasePath = indexBasePath;
	}

	/**
	 * @return the maximumFieldLength
	 */
	public static IndexWriter.MaxFieldLength getMaximumFieldLength() {
		return maximumFieldLength;
	}

	/**
	 * @param maximumFieldLength the maximumFieldLength to set
	 */
	public static void setMaximumFieldLength(IndexWriter.MaxFieldLength maximumFieldLength) {
		LuceneBase.maximumFieldLength = maximumFieldLength;
	}

	/**
	 * @return the dbDriverName
	 */
	public static String getDbDriverName() {
		return dbDriverName;
	}

	/**
	 * @param dbDriverName the dbDriverName to set
	 */
	public static void setDbDriverName(String dbDriverName) {
		LuceneBase.dbDriverName = dbDriverName;
	}

	/**
	 * @param enableUpdater the enableUpdater to set
	 */
	public static void setEnableUpdater(Boolean enableUpdater) {
		LuceneBase.enableUpdater = enableUpdater;
	}

	/**
	 * @return the enableUpdater
	 */
	public static Boolean getEnableUpdater() {
		return enableUpdater;
	}

	/**
	 * @param redundantCnt the redundantCnt to set
	 */
	public static void setRedundantCnt(Integer redundantCnt) {
		LuceneBase.redundantCnt = redundantCnt;
	}

	/**
	 * @return the redundantCnt
	 */
	public static Integer getRedundantCnt() {
		return redundantCnt;
	}

	/**
	 * @return the enableTagClouds
	 */
	public static Boolean getEnableTagClouds() {
		return enableTagClouds;
	}

	/**
	 * @param enableTagClouds the enableTagClouds to set
	 */
	public static void setEnableTagClouds(Boolean enableTagClouds) {
		LuceneBase.enableTagClouds = enableTagClouds;
	}

	/**
	 * @return the tagCloudLimit
	 */
	public static Integer getTagCloudLimit() {
		return tagCloudLimit;
	}

	/**
	 * @param tagCloudLimit the tagCloudLimit to set
	 */
	public static void setTagCloudLimit(Integer tagCloudLimit) {
		LuceneBase.tagCloudLimit = tagCloudLimit;
	}
}
