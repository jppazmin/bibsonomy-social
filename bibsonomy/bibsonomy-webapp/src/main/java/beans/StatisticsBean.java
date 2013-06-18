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

package beans;

import helpers.database.DBStatisticsManager;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedMap;

public class StatisticsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9199232665123872255L;

	private String showSpammer = null;
	
	public String getShowSpammer() {
		return showSpammer;
	}
	public void setShowSpammer(String showSpammer) {
		this.showSpammer = showSpammer;
	}

	private Date today = new Date();
	
	private int tagTagBatches = -1;
	private int tags = -1;
	private int postsInBaskets = -1; // average!
	private int tagTagRelations = -1; // average!
	private int usersWithOwnLayout = -1;
	private int uploadedDocuments = -1;

	/*
	 * user
	 */
	private int users = -1;
	private int spammers = -1;
	private int activeUsers = -1; // have least one post and are NOT spammers
	private int activeUsersLastMonth = -1; // -"- in the last month
	private int activeUsersLast24 = -1; // -"- in the last 24 hours
	// histograms
	private SortedMap<Integer,Integer> bookmarkUserHisto = null;
	private SortedMap<Integer,Integer> publicationUserHisto = null;
	
	/*
	 * resourcen
	 */
	private int resources = -1;
	private int bookmarks = -1; // number of URLs
	private int publications = -1;
	
	private int resourcesDBLP = -1;
	private int bookmarksDBLP = -1;
	private int publicationsDBLP = -1;

	private int resources24 = -1;
	private int bookmarks24 = -1;
	private int publications24 = -1;
	
	/*
	 * posts
	 */
	private int posts = -1;
	private int postsBookmarks = -1; // number of URLs
	private int postsPublications = -1;
	
	private int postsDBLP = -1;
	private int postsBookmarksDBLP = -1;
	private int postsPublicationsDBLP = -1;

	private int posts24 = -1;
	private int postsBookmarks24 = -1;
	private int postsPublications24 = -1;
	

	/*
	 * tas
	 */
	private int tas = -1;
	private int tasBookmarks = -1;
	private int tasPublications = -1;
	
	private int tasDBLP = -1;
	private int tasBookmarksDBLP = -1;
	private int tasPublicationsDBLP = -1;
	
	private int tas24 = -1;
	private int tasBookmarks24 = -1;
	private int tasPublications24 = -1;
	
	/*
	 * logging
	 */
	private int loggedBookmarkPosts = -1;
	private int loggedPublicationPosts = -1;
	private int loggedPostsFromBaskets = -1;
	private int loggedFriends = -1;
	private int loggedGroups = -1;
	private int loggedTagTagRelations = -1;

	private int loggedBookmarkPostsDBLP = -1;
	private int loggedPublicationPostsDBLP = -1;

	
	public int getBookmarks() {
		if (bookmarks == -1) bookmarks = DBStatisticsManager.getBookmarks(showSpammer);
		return bookmarks;
	}
	public int getBookmarks24() {
		if (bookmarks24 == -1) bookmarks24 = DBStatisticsManager.getBookmarks24(showSpammer);
		return bookmarks24;
	}
	public int getBookmarksDBLP() {
		if (bookmarksDBLP == -1) bookmarksDBLP = DBStatisticsManager.getBookmarksDBLP(showSpammer);
		return bookmarksDBLP;
	}
	public int getLoggedBookmarkPosts() {
		if (loggedBookmarkPosts == -1) loggedBookmarkPosts = DBStatisticsManager.getLoggedBookmarkPosts(showSpammer);
		return loggedBookmarkPosts;
	}
	public int getLoggedPostsFromBaskets() {
		if (loggedPostsFromBaskets == -1) loggedPostsFromBaskets = DBStatisticsManager.getLoggedPostsFromBaskets(showSpammer);
		return loggedPostsFromBaskets;
	}
	public int getLoggedPublicationPosts() {
		if (loggedPublicationPosts == -1) loggedPublicationPosts = DBStatisticsManager.getLoggedPublicationPosts(showSpammer);
		return loggedPublicationPosts;
	}
	public int getPosts() {
		if (posts == -1) posts = DBStatisticsManager.getPosts(showSpammer);
		return posts;
	}
	public int getPosts24() {
		if (posts24 == -1) posts24 = DBStatisticsManager.getPosts24(showSpammer);
		return posts24;
	}
	public int getPostsDBLP() {
		if (postsDBLP == -1) postsDBLP = DBStatisticsManager.getPostsDBLP(showSpammer);
		return postsDBLP;
	}
	public int getPublications24() {
		if (publications24 == -1) publications24 = DBStatisticsManager.getPublications24(showSpammer);
		return publications24;
	}
	public int getPublicationsDBLP() {
		if (publicationsDBLP == -1) publicationsDBLP = DBStatisticsManager.getPublicationsDBLP(showSpammer);
		return publicationsDBLP;
	}
	public int getPublications() {
		if (publications == -1) publications = DBStatisticsManager.getPublications(showSpammer);
		return publications;
	}
	public int getSpammers() {
		if (spammers == -1) spammers = DBStatisticsManager.getSpammers(showSpammer);
		return spammers;
	}
	public int getTags() {
		if (tags == -1) tags = DBStatisticsManager.getTags(showSpammer);
		return tags;
	}
	public int getTagTagBatches() {
		if (tagTagBatches == -1) tagTagBatches = DBStatisticsManager.getTagTagBatches(showSpammer);
		return tagTagBatches;
	}

	public int getTas() {
		if (tas == -1) tas = DBStatisticsManager.getTas(showSpammer);
		return tas;
	}
	public int getTas24() {
		if (tas24 == -1) tas24 = DBStatisticsManager.getTas24(showSpammer);
		return tas24;
	}
	public int getTasDBLP() {
		if (tasDBLP == -1) tasDBLP = DBStatisticsManager.getTasDBLP(showSpammer);
		return tasDBLP;
	}
	public int getTasBookmarks() {
		if (tasBookmarks == -1) tasBookmarks = DBStatisticsManager.getTasBookmarks(showSpammer);
		return tasBookmarks;
	}
	public int getTasBookmarks24() {
		if (tasBookmarks24 == -1) tasBookmarks24 = DBStatisticsManager.getTasBookmarks24(showSpammer);
		return tasBookmarks24;
	}
	public int getTasBookmarksDBLP() {
		if (tasBookmarksDBLP == -1) tasBookmarksDBLP = DBStatisticsManager.getTasBookmarksDBLP(showSpammer);
		return tasBookmarksDBLP;
	}
	public int getTasPublications() {
		if (tasPublications == -1) tasPublications = DBStatisticsManager.getTasPublications(showSpammer);
		return tasPublications;
	}
	public int getTasPublications24() {
		if (tasPublications24 == -1) tasPublications24 = DBStatisticsManager.getTasPublications24(showSpammer);
		return tasPublications24;
	}
	public int getTasPublicationsDBLP() {
		if (tasPublicationsDBLP == -1) tasPublicationsDBLP = DBStatisticsManager.getTasPublicationsDBLP(showSpammer);
		return tasPublicationsDBLP;
	}

	public int getUsers() {
		if (users == -1) users = DBStatisticsManager.getUsers(showSpammer);
		return users;
	}

	public int getPostsBookmarks() {
		if (postsBookmarks == -1) postsBookmarks = DBStatisticsManager.getPostsBookmarks(showSpammer);
		return postsBookmarks;
	}
	public int getPostsBookmarks24() {
		if (postsBookmarks24 == -1) postsBookmarks24 = DBStatisticsManager.getPostsBookmarks24(showSpammer);
		return postsBookmarks24;
	}
	public int getPostsBookmarksDBLP() {
		if (postsBookmarksDBLP == -1) postsBookmarksDBLP = DBStatisticsManager.getPostsBookmarksDBLP(showSpammer);
		return postsBookmarksDBLP;
	}
	public int getPostsPublications() {
		if (postsPublications == -1) postsPublications = DBStatisticsManager.getPostsPublications(showSpammer);
		return postsPublications;
	}
	public int getPostsPublications24() {
		if (postsPublications24 == -1) postsPublications24 = DBStatisticsManager.getPostsPublications24(showSpammer);
		return postsPublications24;
	}
	public int getPostsPublicationsDBLP() {
		if (postsPublicationsDBLP == -1) postsPublicationsDBLP = DBStatisticsManager.getPostsPublicationsDBLP(showSpammer);
		return postsPublicationsDBLP;
	}
	
	
	
	public int getResources() {
		if (resources == -1) resources = DBStatisticsManager.getResources(showSpammer);
		return resources;
	}
	public int getResources24() {
		if (resources24 == -1) resources24 = DBStatisticsManager.getResources24(showSpammer);
		return resources24;
	}
	public int getResourcesDBLP() {
		if (resourcesDBLP == -1) resourcesDBLP = DBStatisticsManager.getResourcesDBLP(showSpammer);
		return resourcesDBLP;
	}
	
	
	
	public int getLoggedFriends() {
		if (loggedFriends == -1) loggedFriends = DBStatisticsManager.getLoggedFriends(showSpammer);
		return loggedFriends;
	}
	public int getLoggedGroups() {
		if (loggedGroups == -1) loggedGroups = DBStatisticsManager.getLoggedGroups(showSpammer);
		return loggedGroups;
	}
	public int getLoggedTagTagRelations() {
		if (loggedTagTagRelations == -1) loggedTagTagRelations = DBStatisticsManager.getLoggedTagTagRelations(showSpammer);
		return loggedTagTagRelations;
	}
	
	
	public int getTagTagRelations() {
		if (tagTagRelations == -1) tagTagRelations = DBStatisticsManager.getTagTagRelations(showSpammer);
		return tagTagRelations;
	}
	public int getUsersWithOwnLayout() {
		if (usersWithOwnLayout == -1) usersWithOwnLayout = DBStatisticsManager.getUsersWithOwnLayout(showSpammer);
		return usersWithOwnLayout;
	}
	public int getUploadedDocuments() {
		if (uploadedDocuments == -1) uploadedDocuments = DBStatisticsManager.getUploadedDocuments(showSpammer);
		return uploadedDocuments;
	}
	public int getActiveUsers() {
		if (activeUsers == -1) activeUsers = DBStatisticsManager.getActiveUsers(showSpammer);
		return activeUsers;
	}
	public int getActiveUsersLastMonth() {
		if (activeUsersLastMonth == -1) activeUsersLastMonth = DBStatisticsManager.getActiveUsersLastMonth(showSpammer);
		return activeUsersLastMonth;
	}
	public int getActiveUsersLast24() {
		if (activeUsersLast24 == -1) activeUsersLast24 = DBStatisticsManager.getActiveUsersLast24(showSpammer);
		return activeUsersLast24;
	}
	public Date getToday() {
		return today;
	}
	
	
	public int getLoggedPublicationPostsDBLP() {
		if (loggedPublicationPostsDBLP == -1) loggedPublicationPostsDBLP = DBStatisticsManager.getLoggedPublicationPostsDBLP(showSpammer);
		return loggedPublicationPostsDBLP;
	}
	public int getLoggedBookmarkPostsDBLP() {
		if (loggedBookmarkPostsDBLP == -1) loggedBookmarkPostsDBLP = DBStatisticsManager.getLoggedBookmarkPostsDBLP(showSpammer);
		return loggedBookmarkPostsDBLP;
	}
	
	
	public SortedMap<Integer, Integer> getBookmarkUserHisto() {
		if (bookmarkUserHisto == null) bookmarkUserHisto = DBStatisticsManager.getBookmarkUserHisto(showSpammer);
		return bookmarkUserHisto;
	}
	public SortedMap<Integer, Integer> getPublicationUserHisto() {
		if (publicationUserHisto == null) publicationUserHisto = DBStatisticsManager.getPublicationUserHisto(showSpammer);
		return publicationUserHisto;
	}
	public int getPostsInBaskets() {
		if (postsInBaskets == -1) postsInBaskets = DBStatisticsManager.getPostsInBaskets(showSpammer);
		return postsInBaskets;
	}
	
}
