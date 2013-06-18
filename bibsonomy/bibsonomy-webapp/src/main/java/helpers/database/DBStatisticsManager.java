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

package helpers.database;


import helpers.constants;

import java.sql.SQLException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Deprecated
public class DBStatisticsManager extends DBManager {

	private static final String DBLP = "dblp";
	//private static final String userDBLP   = " user_name = '" + DBLP + "' ";
	private static final String nUserDBLP  = " user_name != '" + DBLP + "' ";

	private static final String date24    = " DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date ";
	private static final String dateM     = " DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date ";

	private static final String interHash = " simhash" + constants.INTER_HASH + " ";
	private static final String contentTypeBookmark    = "content_type = " + constants.BOOKMARK_CONTENT_TYPE;
	private static final String contentTypePublication = "content_type = " + constants.BIBTEX_CONTENT_TYPE;

	private static final Log log = LogFactory.getLog(DBStatisticsManager.class);





	/* ******************************************************************************************
	 * Misc
	 */
	public static int getTagTagBatches(String showSpammer) {     
		return getCtr("SELECT count(*) AS ctr FROM tagtag_batch");
	}
	public static int getTags(String showSpammer) {              
		return getCtr("SELECT count(*) AS ctr FROM tags WHERE tag_ctr_public > 0");
	}
	public static int getPostsInBaskets(String showSpammer) {    
		return getCtr("SELECT count(*) AS ctr FROM collector");
	}
	public static int getTagTagRelations(String showSpammer) {   
		return getCtr("SELECT count(*) AS ctr FROM tagtagrelations");
	}
	public static int getUploadedDocuments(String showSpammer) { 
		return getCtr("SELECT count(*) AS ctr FROM document WHERE content_id > 0");
	}
	public static int getUsersWithOwnLayout(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM (SELECT user_name FROM document WHERE content_id = 0 GROUP BY user_name) AS layouts");
	}

	/* ******************************************************************************************
	 * users
	 */
	public static int getUsers(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM user");
	}
	public static int getSpammers(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM user where spammer=1");
	}
	public static int getActiveUsers(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM (SELECT count(*) FROM tas " + getSpammer(showSpammer) + " GROUP BY user_name) AS users");
	}
	public static int getActiveUsersLastMonth(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM (SELECT count(*) FROM tas " + getSpammer(showSpammer) + " AND " + dateM  + " GROUP BY user_name) AS users");
	}
	public static int getActiveUsersLast24(String showSpammer) {   
		return getCtr("SELECT count(*) AS ctr FROM (SELECT count(*) FROM tas " + getSpammer(showSpammer) + " AND " + date24 + " GROUP BY user_name) AS users");
	}

	/* ******************************************************************************************
	 * user histograms
	 */
	public static SortedMap<Integer, Integer> getBookmarkUserHisto(String showSpammer) {
		return getUserHistogram("SELECT count(ctr) AS uCtr, ctr FROM (SELECT COUNT(user_name) AS ctr FROM bibtex   " + getSpammer(showSpammer) + " AND " + nUserDBLP + " GROUP BY user_name) AS foo GROUP BY ctr ORDER BY ctr");
	}
	public static SortedMap<Integer, Integer> getPublicationUserHisto(String showSpammer) {
		return getUserHistogram("SELECT count(ctr) AS uCtr, ctr FROM (SELECT COUNT(user_name) AS ctr FROM bookmark " + getSpammer(showSpammer) + " AND " + nUserDBLP + " GROUP BY user_name) AS foo GROUP BY ctr ORDER BY ctr"); 
	}


	/* ******************************************************************************************
	 * Logged items
	 */
	public static int getLoggedBookmarkPosts(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_bookmark " + getSpammer(showSpammer));
	}

	public static int getLoggedPublicationPosts(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_bibtex   " + getSpammer(showSpammer));
	}

	public static int getLoggedBookmarkPostsDBLP(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_bookmark " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}

	public static int getLoggedPublicationPostsDBLP(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_bibtex   " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}

	public static int getLoggedPostsFromBaskets(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_collector");
	}

	public static int getLoggedFriends(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_friends");
	}

	public static int getLoggedGroups(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_groups");
	}

	public static int getLoggedTagTagRelations(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM log_tagtagrelations");
	}

	/* ******************************************************************************************
	 * TAS
	 */
	public static int getTasBookmarks(String showSpammer) {       
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + contentTypeBookmark);
	}
	public static int getTasPublications(String showSpammer) {    
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + contentTypePublication);
	}
	public static int getTas(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer));
	}

	public static int getTasBookmarksDBLP(String showSpammer) {   
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + contentTypeBookmark    + " AND " + nUserDBLP);
	}
	public static int getTasPublicationsDBLP(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + contentTypePublication + " AND " + nUserDBLP);
	}
	public static int getTasDBLP(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}

	public static int getTasBookmarks24(String showSpammer) {     
		return getCtr("SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + contentTypeBookmark    + " AND " + nUserDBLP + " AND " + date24);
	}
	public static int getTasPublications24(String showSpammer) {  
		return getCtr( "SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + contentTypePublication + " AND " + nUserDBLP + " AND " + date24);
	}
	public static int getTas24(String showSpammer) {
		return getCtr( "SELECT count(*) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + nUserDBLP + " AND " + date24);
	}

	/* ******************************************************************************************
	 * Posts
	 */
	public static int getPostsBookmarks(String showSpammer) {       
		return getCtr("SELECT count(*) AS ctr FROM bookmark " + getSpammer(showSpammer));
	}
	public static int getPostsPublications(String showSpammer) {    
		return getCtr("SELECT count(*) AS ctr FROM bibtex "   + getSpammer(showSpammer));
	}
	public static int getPosts(String showSpammer) {
		return getCtr("SELECT COUNT(DISTINCT content_id) AS ctr FROM tas " + getSpammer(showSpammer));
	}

	public static int getPostsBookmarksDBLP(String showSpammer) {   
		return getCtr("SELECT count(*) AS ctr FROM bookmark " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}
	public static int getPostsPublicationsDBLP(String showSpammer) {
		return getCtr("SELECT count(*) AS ctr FROM bibtex   " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}
	public static int getPostsDBLP(String showSpammer) {
		return getCtr("SELECT COUNT(DISTINCT content_id) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}

	public static int getPostsBookmarks24(String showSpammer) {     
		return getCtr("SELECT count(*) AS ctr FROM bookmark " + getSpammer(showSpammer) + " AND " + nUserDBLP + " AND " + date24);
	}
	public static int getPostsPublications24(String showSpammer) {  
		return getCtr("SELECT count(*) AS ctr FROM bibtex   " + getSpammer(showSpammer) + " AND " + nUserDBLP + " AND " + date24);
	}
	public static int getPosts24(String showSpammer) {
		return getCtr("SELECT COUNT(DISTINCT content_id) AS ctr FROM tas " + getSpammer(showSpammer) + " AND " + nUserDBLP + " AND " + date24);
	}


	/* ******************************************************************************************
	 * Resources
	 */
	public static int getBookmarks(String showSpammer) {       
		return getCtr("SELECT count(DISTINCT book_url_hash)     AS ctr FROM bookmark " + getSpammer(showSpammer));
	}
	public static int getPublications(String showSpammer) {    
		return getCtr("SELECT count(DISTINCT " + interHash + ") AS ctr FROM bibtex" + getSpammer(showSpammer));
	}
	public static int getResources(String showSpammer) {
		// TODO: this is stupid!
		return getBookmarks(getSpammer(showSpammer)) + getPublications(getSpammer(showSpammer));
	}

	public static int getBookmarksDBLP(String showSpammer) {   
		return getCtr("SELECT count(DISTINCT book_url_hash)     AS ctr FROM bookmark " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}
	public static int getPublicationsDBLP(String showSpammer) {
		return getCtr("SELECT count(DISTINCT " + interHash + ") AS ctr FROM bibtex   " + getSpammer(showSpammer) + " AND " + nUserDBLP);
	}
	public static int getResourcesDBLP(String showSpammer) {
		// TODO: this is stupid!
		return getBookmarksDBLP(getSpammer(showSpammer)) + getPublicationsDBLP(getSpammer(showSpammer));
	}

	public static int getBookmarks24(String showSpammer) {     
		return getCtr("SELECT count(DISTINCT book_url_hash)     AS ctr FROM bookmark " + getSpammer(showSpammer) + " AND " + nUserDBLP + " AND " + date24);
	}
	public static int getPublications24(String showSpammer) {  
		return getCtr("SELECT count(DISTINCT " + interHash + ") AS ctr FROM bibtex   " + getSpammer(showSpammer) + " AND " + nUserDBLP + " AND " + date24);
	}
	public static int getResources24(String showSpammer) {
		// TODO: this is stupid!
		return getBookmarks24(getSpammer(showSpammer)) + getPublications24(getSpammer(showSpammer));
	}



	/* ******************************************************************************************
	 * Helper methods
	 */


	/** Depening on the value of showSpammer, the SQL query is modified.
	 * By default, only non spammers are shown.
	 * Possible values for showSpammer: "only" - show only spammers, "yes" - 
	 * return spammers together with normal users, "no" - show only normal users.
	 * 
	 * @param showSpammer
	 * @return
	 */
	private static String getSpammer(String showSpammer) {
		String spammer = " WHERE `group` >= 0 ";
		if ("only".equals(showSpammer)) {
			spammer = " WHERE `group` < 0 ";
		} else if ("yes".equals(showSpammer)) {
			spammer = " WHERE `group` != NULL ";
		}
		return spammer;
	}

	private static int getCtr (String query) {
		DBContext c = new DBContext();
		try {
			if (c.initSlave()) { // initialize slave database
				c.stmt = c.conn.prepareStatement(query);
				c.rst = c.stmt.executeQuery();
				if (c.rst.next()) {
					return c.rst.getInt("ctr");
				}
			}
		} catch (SQLException e) {
			log.fatal("could not get statistics: " + e);
		} finally {
			c.close(); // close database connection
		}
		return 0;
	}

	private static SortedMap<Integer, Integer> getUserHistogram(String query) {
		SortedMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		DBContext c = new DBContext();
		try {
			if (c.initSlave()) { // initialize slave database
				c.stmt = c.conn.prepareStatement(query);
				c.rst = c.stmt.executeQuery();
				while (c.rst.next()) {
					map.put(c.rst.getInt("ctr"), c.rst.getInt("uCtr"));
				}
			}
		} catch (SQLException e) {
			log.fatal("could not get statistics: " + e);
		} finally {
			c.close(); // close database connection
		}
		return map;
	}
}