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

package org.bibsonomy.webapp.command;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.sync.SyncService;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author Steffen Kress
 * @version $Id: SettingsViewCommand.java,v 1.39 2011-07-27 12:44:02 rja Exp $
 */
public class SettingsViewCommand extends TabsCommand<Object> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1211293063812357398L;
	
	/** Indexes of definded tabs */
	public final static int MY_PROFILE_IDX = 0;
	public final static int SETTINGS_IDX = 1;
	public final static int IMPORTS_IDX = 2;
	public final static int GROUP_IDX = 3;
	public final static int SYNC_IDX = 4;
	
	private static final String TAB_URL = "/settings";
	
	/**
	 * action can be logging, api or layoutTagPost
	 * this three types determine the different possible actions which will be handled 
	 * by this controller for the settings.settings site
	 */
	private String action;
	
	/**
	 * current user
	 */
	private User user;
	
	private String wikiText;
	
	private boolean hasOwnGroup;
	
	/**
	 * number of the new maxCount/minFreq
	 */
	private int changeTo;
	
	/**
	 * users which added the current login user in their friend list 
	 */
	private List<User> friendsOfUser;
	
	/**
	 * list with friends of the current login user
	 */
	private List<User> userFriends;
	
	/**
	 * the privacy level of this user's profile {friends, private or public}
	 */
	private String profilePrivlevel;
	
	private String importType;
	
	private boolean overwrite;
	private boolean overwriteV1;
	private boolean overwriteV2;
	
	private CommonsMultipartFile file;
	
	/*
	 * settings for groups
	 */
	private int privlevel;
	// TODO: why not boolean?
	private int sharedDocuments;
	private Group group;
	
	/**
	 * current password of user
	 */
	private String oldPassword = null;
	
	private String newPassword = null;
	
	private String newPasswordRetype = null;

	private Map<String, String> newBookmarks = null;

	private Map<String, String> updatedBookmarks = null;

	private List<String> nonCreatedBookmarks = null;
	
	/**
	 * name of the begin layout file
	 */
	private String beginName = null;
	
	/**
	 * hash of the begin layout file
	 */
	private String beginHash = null;
	
	/**
	 * name of the item layout file
	 */
	private String itemName = null;
	
	/**
	 * hash of the begin layout file
	 */
	private String itemHash = null;
	
	/**
	 * name of the end layout file
	 */
	private String endName = null;
	
	/**
	 * hash of the end layout file
	 */
	private String endHash = null;
	
	/**
	 * delete the account yes or no
	 */
	private String delete = null;

	private String importData;
	
	private List<SyncService> syncServer;
	private SyncService newSyncServer;
	
	private List<URI> availableSyncClients;
	private List<URI> availableSyncServers;
	
	/** for delicious v2 import only, import bookmarks or bundles? **/
	private String importDataV2 = "posts";
	
	/** for delicious v1 import only, import bookmarks or bundles? **/
	private String importDataV1 = "posts";
	
	/** in case of an import from a remote service 
	 *  userName and passWord are required **/
	private String userName;
	private String passWord;
	/**
	 * Constructor.
	 */
	public SettingsViewCommand() {
		this.addTab(MY_PROFILE_IDX, "navi.myprofile");
		this.addTab(SETTINGS_IDX, "navi.settings");
		this.addTab(IMPORTS_IDX, "navi.imports");	
		this.setSelTab(MY_PROFILE_IDX);
		this.setTabURL(TAB_URL);
	}
	
	/**
	 * shows the group tab on the settings.settings site if the user is a group
	 * @param show
	 */
	public void showGroupTab(boolean show) {
		if (show) {
			this.addTab(GROUP_IDX, "navi.groups");
		}
	}
	
	/**
	 * shows the sync tab for admins
	 * @param show
	 */
	public void showSyncTab(boolean show) {
		if(show) {
			this.addTab(SYNC_IDX, "navi.sync");
		}
	}
	
	/**
	 * @return the privacy level of this user's profile
	 */
	public String getProfilePrivlevel() {
		return this.profilePrivlevel;
	}

	/**
	 * @param profilePrivlevel - the privacy level of this user's profile
	 */
	public void setProfilePrivlevel(final String profilePrivlevel) {
		this.profilePrivlevel = profilePrivlevel;
	}

	/**
	 * @return importType
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
	 * @return overwrite
	 */
	public boolean isOverwrite() {
		return this.overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	/**
	 * @return overwrite
	 */
	public boolean isOverwriteV1() {
		return this.overwriteV1;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwriteV1(boolean overwrite) {
		this.overwriteV1 = overwrite;
	}
	
	/**
	 * @return overwrite
	 */
	public boolean isOverwriteV2() {
		return this.overwriteV2;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwriteV2(boolean overwrite) {
		this.overwriteV2 = overwrite;
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
	 * @return newBookmarks
	 */
	public Map<String, String> getNewBookmarks() {
		return this.newBookmarks;
	}

	/**
	 * @return updatedBookmarks
	 */
	public Map<String, String> getUpdatedBookmark() {
		return this.updatedBookmarks;
	}

	/**
	 * @return nonCreatedBookmarks
	 */
	public List<String> getNonCreatedBookmark() {
		return this.nonCreatedBookmarks;
	}

	/**
	 * @param newBookmarks
	 */
	public void setNewBookmarks(Map<String, String> newBookmarks) {
		this.newBookmarks = newBookmarks;
	}

	/**
	 * @param updatedBookmarks
	 */
	public void setUpdatedBookmarks(Map<String, String> updatedBookmarks) {
		this.updatedBookmarks = updatedBookmarks;
	}

	/**
	 * @param nonCreatedBookmarks
	 */
	public void setNonCreatedBookmarks(List<String> nonCreatedBookmarks) {
		this.nonCreatedBookmarks = nonCreatedBookmarks;
	}

	/**
	 * @param privlevel
	 */
	public void setPrivlevel(int privlevel) {
		this.privlevel = privlevel;
	}

	public int getPrivlevel() {
		return privlevel;
	}

	/**
	 * @param sharedDocuments
	 */
	public void setSharedDocuments(int sharedDocuments) {
		this.sharedDocuments = sharedDocuments;
	}

	public int getSharedDocuments() {
		return sharedDocuments;
	}

	public String getBeginName() {
		return this.beginName;
	}

	public String getBeginHash() {
		return this.beginHash;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getItemHash() {
		return this.itemHash;
	}

	public String getEndName() {
		return this.endName;
	}

	public String getEndHash() {
		return this.endHash;
	}

	/**
	 * @param beginName
	 */
	public void setBeginName(String beginName) {
		this.beginName = beginName;
	}

	/**
	 * @param beginHash
	 */
	public void setBeginHash(String beginHash) {
		this.beginHash = beginHash;
	}

	/**
	 * @param itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @param itemHash
	 */
	public void setItemHash(String itemHash) {
		this.itemHash = itemHash;
	}

	/**
	 * @param endName
	 */
	public void setEndName(String endName) {
		this.endName = endName;
	}

	/**
	 * @param endHash
	 */
	public void setEndHash(String endHash) {
		this.endHash = endHash;
	}
	
	/**
	 * @param hasOwnGroup
	 */
	public void setHasOwnGroup(boolean hasOwnGroup) {
		this.hasOwnGroup = hasOwnGroup;
	}

	public boolean getHasOwnGroup() {
		return hasOwnGroup;
	}
	
	/**
	 * @return the friendsOfUser
	 */
	public List<User> getFriendsOfUser() {
		return this.friendsOfUser;
	}

	/**
	 * @param friendsOfUser the friendsOfUser to set
	 */
	public void setFriendsOfUser(List<User> friendsOfUser) {
		this.friendsOfUser = friendsOfUser;
	}

	/**
	 * @return the userFriends
	 */
	public List<User> getUserFriends() {
		return this.userFriends;
	}

	/**
	 * @param userFriends the userFriends to set
	 */
	public void setUserFriends(List<User> userFriends) {
		this.userFriends = userFriends;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return this.oldPassword;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return this.newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the newPasswordRetype
	 */
	public String getNewPasswordRetype() {
		return this.newPasswordRetype;
	}

	/**
	 * @param newPasswordRetype the newPasswordRetype to set
	 */
	public void setNewPasswordRetype(String newPasswordRetype) {
		this.newPasswordRetype = newPasswordRetype;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the delete
	 */
	public String getDelete() {
		return this.delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(String delete) {
		this.delete = delete;
	}
	
	/**
	 * @return the changeTo
	 */
	public int getChangeTo() {
		return this.changeTo;
	}

	/**
	 * @param changeTo the changeTo to set
	 */
	public void setChangeTo(int changeTo) {
		this.changeTo = changeTo;
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

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return this.group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @param wikiText the wikiText to set
	 */
	public void setWikiText(String wikiText) {
		this.wikiText = wikiText;
	}

	/**
	 * @return the wikiText
	 */
	public String getWikiText() {
		return wikiText;
	}

	/**
	 * @param syncServer the syncServer to set
	 */
	public void setSyncServer(List<SyncService> syncServer) {
		this.syncServer = syncServer;
	}

	/**
	 * @return the syncServer
	 */
	public List<SyncService> getSyncServer() {
		return syncServer;
	}

	/**
	 * @param availableSyncServers the avlSyncServer to set
	 */
	public void setAvailableSyncServers(List<URI> availableSyncServers) {
		this.availableSyncServers = availableSyncServers;
	}

	/**
	 * @return the avlSyncServer
	 */
	public List<URI> getAvailableSyncServers() {
		return availableSyncServers;
	}
	
	/**
	 * @return the importData
	 */
	public String getImportDataV2() {
		return this.importDataV2;
	}

	/**
	 * @param importData the importData to set
	 */
	public void setImportDataV2(String importData) {
		this.importDataV2 = importData;
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
	 * @param importDataV1
	 */
	public void setImportDataV1(String importDataV1) {
		this.importDataV1 = importDataV1;
	}

	/**
	 * @return importData for v1 form
	 */
	public String getImportDataV1() {
		return importDataV1;
	}

	/**
	 * @return The available synchronization clients.
	 */
	public List<URI> getAvailableSyncClients() {
		return this.availableSyncClients;
	}

	/**
	 * @param availableSyncClients
	 */
	public void setAvailableSyncClients(List<URI> availableSyncClients) {
		this.availableSyncClients = availableSyncClients;
	}

	public SyncService getNewSyncServer() {
		return this.newSyncServer;
	}

	public void setNewSyncServer(SyncService newSyncServer) {
		this.newSyncServer = newSyncServer;
	}
}