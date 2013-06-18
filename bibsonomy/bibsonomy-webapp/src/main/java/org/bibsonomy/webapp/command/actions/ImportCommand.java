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

package org.bibsonomy.webapp.command.actions;

import java.util.List;
import java.util.Map;

import org.bibsonomy.webapp.command.BaseCommand;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author mwa
 * @version $Id: ImportCommand.java,v 1.11 2011-03-25 17:56:27 hks Exp $
 */
public class ImportCommand extends BaseCommand {
	
	/** when true, duplicate entries will be overwritten **/
	private boolean overwrite;
	
	/** the import-type describes which kind of import will be used 
	 *  e.g. FireFox import, Delicious import etc.. **/
	private String importType;
	
	/**
	 * Appended to the Delicious (Yahoo! accounts) import callback url.
	 */
	private String oauth_token;
	private String oauth_verifier;
	
	/** in case of an import from a remote service 
	 *  userName and passWord are required **/
	private String userName;
	private String passWord;
	
	/** the group: private or public **/
	private String group;
	
	private int totalCount;
	
	/** the file to import **/
	private CommonsMultipartFile file;
	
	private Map<String, String> newBookmarks;

	private Map<String, String> updatedBookmarks;

	private Map<String, String> nonCreatedBookmarks;
	
	private List<String> storedConcepts;
	
	/** for delicious import only, import bookmarks or bundles? **/
	private String importData;
	
	/**
	 * @return true if duplicate entries shall be overwritten
	 */
	public boolean isOverwrite() {
		return this.overwrite;
	}
	
	/**
	 * @param overwrite
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	/**
	 * @return the actual import-type
	 */
	public String getImportType() {
		return this.importType;
	}
	
	/**
	 * @param importType
	 */
	public void setImportType(String importType) {
		this.importType = importType;
	}
	
	/**
	 * @param oauth_token
	 */
	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	/**
	 * @return oAuth token for Delicious (Yahoo!) import
	 */
	public String getOauth_token() {
		return oauth_token;
	}

	/**
	 * @param oauth_verifier
	 */
	public void setOauth_verifier(String oauth_verifier) {
		this.oauth_verifier = oauth_verifier;
	}

	/**
	 * @return oAuth verifier for Delicious (Yahoo!) import
	 */
	public String getOauth_verifier() {
		return oauth_verifier;
	}

	/**
	 * @return the userName, required for importing resources form a remote service
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * @return the user's password
	 */
	public String getPassWord() {
		return this.passWord;
	}
	
	/**
	 * @param passWord
	 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	/**
	 * 
	 * @return a Map containing the URLs of all created bookmarks
	 */
	public Map<String, String> getNewBookmarks() {
		return this.newBookmarks;
	}
	
	/**
	 * 
	 * @param newBookmarks
	 */
	public void setNewBookmarks(Map<String, String> newBookmarks) {
		this.newBookmarks = newBookmarks;
	}
	
	/**
	 * 
	 * @return a Map containing the URLs of all updated bookmarks
	 */
	public Map<String, String> getUpdatedBookmarks() {
		return this.updatedBookmarks;
	}
	
	/**
	 * 
	 * @param updatedBookmarks
	 */
	public void setUpdatedBookmarks(Map<String, String> updatedBookmarks) {
		this.updatedBookmarks = updatedBookmarks;
	}
	
	/**
	 * 
	 * @return a Map containing the URLs of all non created bookmarks
	 */
	public Map<String, String> getNonCreatedBookmarks() {
		return this.nonCreatedBookmarks;
	}
	
	/**
	 * 
	 * @param nonCreatedBookmarkEntries
	 */
	public void setNonCreatedBookmarks(Map<String, String> nonCreatedBookmarkEntries) {
		this.nonCreatedBookmarks = nonCreatedBookmarkEntries;
	}
	
	/**
	 * @return the group
	 */
	public String getGroup() {
		return this.group;
	}
	
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * @return the file
	 */
	public CommonsMultipartFile getFile() {
		return this.file;
	}
	
	/**
	 * @param file the file to set
	 */
	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
	
	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * @return the storedConcepts
	 */
	public List<String> getStoredConcepts() {
		return this.storedConcepts;
	}

	/**
	 * @param storedConcepts the storedConcepts to set
	 */
	public void setStoredConcepts(List<String> storedConcepts) {
		this.storedConcepts = storedConcepts;
	}

	/**
	 * @return the importData
	 */
	public String getImportData() {
		return this.importData;
	}

	/**
	 * @param importData the importData to set
	 */
	public void setImportData(String importData) {
		this.importData = importData;
	}
		
}