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

package org.bibsonomy.lucene.param;

/**
 * bean for configuring lucene via JNDI
 * 
 * @author fei
 * @version $Id: LuceneConfig.java,v 1.7 2010-05-28 10:22:50 nosebrain Exp $
 */
public class LuceneConfig {
	/** configure search mode (lucene/database) */
	private String searchMode;
	/** base path to the indices */
	private String indexPath;
	/** enable/disable index updater */
	private Boolean enableUpdater = false;
	/** indicate whether the index should be loaded into the ram */
	private Boolean loadIndexIntoRam = false;
	/** determing maximal field length for lucene fields */
	private String maximumFieldLength;
	/** db driver name - FIXME: only needed for offline index creation */
	private String dbDriverName;
	/** nr. of redundant indeces */
	private String redundantCnt = "2";
	/** enable/disable tag clouds on search pages */
	private Boolean enableTagClouds = false;
	/** number of posts to consider for building the tag cloud */
	private String tagCloudLimit = "1000";
	
	/**
	 * @return the indexPath
	 */
	public String getIndexPath() {
		return indexPath;
	}
	
	/**
	 * @param indexPath the indexPath to set
	 */
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	
	/**
	 * @param enableUpdater the string representation of enableUpdater
	 */
	public void setEnableUpdater(String enableUpdater) {
		this.enableUpdater = Boolean.valueOf(enableUpdater);
	}
	
	/**
	 * @return the string representation of enableUpdater
	 */
	public String getEnableUpdater() {
		return enableUpdater.toString();
	}
	
	/**
	 * @return the enableTagClouds
	 */
	public Boolean getEnableTagClouds() {
		return enableTagClouds;
	}

	/**
	 * @param enableTagClouds the enableTagClouds to set
	 */
	public void setEnableTagClouds(Boolean enableTagClouds) {
		this.enableTagClouds = enableTagClouds;
	}

	/**
	 * @return the searchMode
	 */
	public String getSearchMode() {
		return searchMode;
	}

	/**
	 * @param searchMode the searchMode to set
	 */
	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	/**
	 * @param loadIndexIntoRam the string representation of loadINtexIntoRam
	 */
	public void setLoadIndexIntoRam(String loadIndexIntoRam) {
		this.loadIndexIntoRam = Boolean.valueOf(loadIndexIntoRam);
	}
	
	/**
	 * @return the string representation of loadIndexIntoRam
	 */
	public String getLoadIndexIntoRam() {
		return loadIndexIntoRam.toString();
	}
	
	/**
	 * @return the maximumFieldLength
	 */
	public String getMaximumFieldLength() {
		return maximumFieldLength;
	}

	/**
	 * @param maximumFieldLength the maximumFieldLength to set
	 */
	public void setMaximumFieldLength(String maximumFieldLength) {
		this.maximumFieldLength = maximumFieldLength;
	}
	
	/**
	 * @return the dbDriverName
	 */
	public String getDbDriverName() {
		return dbDriverName;
	}

	/**
	 * @param dbDriverName the dbDriverName to set
	 */
	public void setDbDriverName(String dbDriverName) {
		this.dbDriverName = dbDriverName;
	}
	
	/**
	 * @return the redundantCnt
	 */
	public String getRedundantCnt() {
		return redundantCnt;
	}

	/**
	 * @param redundantCnt the redundantCnt to set
	 */
	public void setRedundantCnt(String redundantCnt) {
		this.redundantCnt = redundantCnt;
	}

	/**
	 * @return the tagCloudLimit
	 */
	public String getTagCloudLimit() {
		return tagCloudLimit;
	}

	/**
	 * @param tagCloudLimit the tagCloudLimit to set
	 */
	public void setTagCloudLimit(String tagCloudLimit) {
		this.tagCloudLimit = tagCloudLimit;
	}
}
