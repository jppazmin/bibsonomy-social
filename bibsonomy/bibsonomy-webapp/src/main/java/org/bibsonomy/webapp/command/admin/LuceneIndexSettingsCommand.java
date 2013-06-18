/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.command.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bibsonomy.lucene.param.LuceneIndexStatistics;

/**
 * Bean for classifier settings
 * 
 * @author Sven Stefani
 * @version $Id: LuceneIndexSettingsCommand.java,v 1.6 2010-12-03 00:18:52 bsc Exp $
 */
public class LuceneIndexSettingsCommand {

	private String instance;
	private String name;
	private String resourceName;
	private int numDocs;
	private int numDeletedDocs;
	private Date newestDate;
	private Date lastModified;
	private long currentVersion;
	private String currentVersionString;
	private boolean isCurrent;
	private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private LuceneIndexSettingsCommand inactiveIndex;
	private int id;
	private boolean isEnabled;
	private boolean isGeneratingIndex;
	private int indexGenerationProgress;

	/**
	 * @param indexStatistics
	 */
	public void setIndexStatistics (LuceneIndexStatistics indexStatistics){
		if(indexStatistics != null) {
			setNumDocs(indexStatistics.getNumDocs());
			setNumDeletedDocs(indexStatistics.getNumDeletedDocs());
			setNewestDate(new Date(indexStatistics.getNewestRecordDate()));
			setLastModified(new Date(indexStatistics.getLastModified()));
			setCurrentVersion(indexStatistics.getCurrentVersion());
			setCurrent(indexStatistics.isCurrent());
		}
	}
	
	/**
	 * @return the instance
	 */
	public String getInstance() {
		return this.instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}

	/**
	 * @return the newestDate
	 */
	public Date getNewestDate() {
		return this.newestDate;
	}

	/**
	 * @param newestDate the newestDate to set
	 */
	public void setNewestDate(Date newestDate) {
		this.newestDate = newestDate;
	}
	
	/**
	 * @param newestDate the newestDate to set
	 */
	public String getNewestDateString() {
		if(newestDate == null) {
			return "";
		}
		return dateformat.format(this.newestDate);
	}

	/**
	 * @return the numDocs
	 */
	public int getNumDocs() {
		return this.numDocs;
	}

	/**
	 * @param numDocs the numDocs to set
	 */
	public void setNumDocs(int numDocs) {
		this.numDocs = numDocs;
	}

	/**
	 * @return the numDeletedDocs
	 */
	public int getNumDeletedDocs() {
		return this.numDeletedDocs;
	}

	/**
	 * @param numDeletedDocs the numDeletedDocs to set
	 */
	public void setNumDeletedDocs(int numDeletedDocs) {
		this.numDeletedDocs = numDeletedDocs;
	}

	/**
	 * @return the lastModified
	 */
	public Date getLastModified() {
		return this.lastModified;
	}
	
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return lastModified as a String
	 */
	public String getLastModifiedString() {
		if(lastModified == null)
			return "";
		return dateformat.format(this.lastModified);
	}

	/**
	 * @return the currentVersion
	 */
	public long getCurrentVersion() {
		return this.currentVersion;
	}

	/**
	 * @param currentVersion the currentVersion to set
	 */
	public void setCurrentVersion(long currentVersion) {
		this.currentVersion = currentVersion;
	}

	/**
	 * @return the isCurrent
	 */
	public boolean isCurrent() {
		return this.isCurrent;
	}

	/**
	 * @param isCurrent the isCurrent to set
	 */
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	/**
	 * @return the currentVersionString
	 */
	public String getCurrentVersionString() {
		return this.currentVersionString;
	}

	/**
	 * @param currentVersionString the currentVersionString to set
	 */
	private void setCurrentVersionString(String currentVersionString) {
		this.currentVersionString = currentVersionString;
	}

	/**
	 * @param inactiveIndex
	 */
	public void setInactiveIndex(LuceneIndexSettingsCommand inactiveIndex) {
		this.inactiveIndex = inactiveIndex;
		inactiveIndex.setName("inactive");
	}

	/**
	 * @return the inactive index
	 */
	public LuceneIndexSettingsCommand getInactiveIndex() {
		return inactiveIndex;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the indexname
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return the isEnabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * @param resourceName the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}
	
    /**
     * @param b isGeneratingIndex
     */
	public void setGeneratingIndex(boolean b) {
		isGeneratingIndex = b;
	}
	
	/**
	 * @return isGeneratingIndex
	 */
	public boolean isGeneratingIndex() {
		return isGeneratingIndex;
	}

	/**
	 * @param indexGenerationProgress the progress of the index-generation
	 */
	public void setIndexGenerationProgress(int indexGenerationProgress) {
		this.indexGenerationProgress = indexGenerationProgress;
	}
	
    /**
     * @return the progress of the index-generation
     */
	public int getIndexGenerationProgress() {
		return indexGenerationProgress;
	}
}