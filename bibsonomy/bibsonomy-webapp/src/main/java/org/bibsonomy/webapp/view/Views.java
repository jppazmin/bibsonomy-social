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

/*
 * Created on 08.10.2007
 */
package org.bibsonomy.webapp.view;

import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.webapp.util.View;

/**
 * some symbols for views in the application, along with information 
 * which views are capable to display publication / bookmark only
 * 
 * @author Jens Illig
 * @author rja
 * @version $Id: Views.java,v 1.140 2011-07-27 13:36:13 rja Exp $
 */
public enum Views implements View {
		
	/* *****************************************************
	 * query views
	 * *****************************************************/
		
	/**
	 * the first page you see when entering the application
	 */
	HOMEPAGE("home"),
	
	/**
	 * the page where a user can change his personal settings
	 */
	SETTINGSPAGE("settings"),
	
	/**
	 * synchronization page
	 */
	SYNC("actions/sync"),
	
	/**
	 * user page displaying the resources of a single user
	 */
	USERPAGE("user"),
	
	/**
	 * the user specific friend-of-a-friend (FOAF) project output
	 */
	FOAF("foaf"),
	
	/**
	 * the user specific curriculum vitae page
	 */
	CVPAGE("cvpage"),
	
	/**
	 * the group specific curriculum vitae page
	 */
	GROUPCVPAGE("groupcvpage"),
	
	/**
	 * user-user page to highlight the relevant posts / tags of user 2 to user 1 
	 */
	USERUSERPAGE("useruser"),
	
	/**
	 * user page displaying the resources of a single user tagged with a given list of tags
	 */
	USERTAGPAGE("usertag"),	
	
	/**
	 * groups page showing all groups available
	 */
	GROUPSPAGE("groups"),

	/**
	 * group page showing all resources of a specified group
	 */
	GROUPPAGE("group"),
	
	/**
	 * group page showing all resources of a specified group and a given tag or list of tags
	 */	
	GROUPTAGPAGE("grouptag"),
	
	/**
	 * tag page show all resources with a given tag or a list of tags
	 */
	TAGPAGE("tag"),
	
	/**
	 * cluster page for browsing resources based on clusterings
	 */
	CLUSTERPAGE("browsing/cluster"),

	/**
	 * page for listing all relation tagged users 
	 */
	SPHERELIST("spheres/sphereList"),
	
	/**
	 * tagged friend page shows all users of the requested group and their posts
	 */
	SPHEREDETAILS("spheres/sphere"),
	
	/**
	 * topics page for browsing resources based on topics
	 */
	TOPICSPAGE("browsing/topics"),
	
	/**
	 * inbox page
	 */
	INBOX("inbox"),
	
	/**
	 * authors overview page
	 */
	AUTHORSPAGE("authors"),
		
	/**
	 * concept page shows all suptags of an requested tag
	 */
	CONCEPTPAGE("concept"),
	
	/**
	 * friends page show all tags whose are viewable for friends by a friend of you
	 */
	FRIENDSPAGE("friends"),
	
	/**
	 * friend page shows all posts which are set viewable for friends of the requested user
	 */
	FRIENDPAGE("friend"),
	
	/**
	 * bibtex page shows all publications with the given inter-/intrahash
	 */
	BIBTEXPAGE("bibtex"),
	
	/**
	 * all the posts the user has picked in his basket 
	 */
	BASKETPAGE("basket"),
	
	/**
	 * relations page shows all the relations of an user
	 */
	USERRELATED("userRelations"),
	
	/**
	 * details of a publication 
	 */
	BIBTEXDETAILS("bibtexdetails"),
	
	/**
	 * bibtexkey page does something with the bibtexkey, perhaps shows the details for a given bibtexkex  
	 */
	BIBTEXKEYPAGE("bibtexkey"),

	/**
	 * url page, displays all bookmarks for a given url hash  
	 */
	URLPAGE("url"),
	
	/**
	 * popular page
	 */
	POPULAR("popular"),
	
	/**
	 * popular tags page
	 */
	POPULAR_TAGS("popularTags"),
	
	/**
	 * userpage only for publications with documents attached
	 */
	USERDOCUMENTPAGE("userDocument"),
	
	/**
	 * userpage only for publications with documents attached
	 */
	GROUPDOCUMENTPAGE("groupDocument"),
	
	/**
	 * viewable page
	 */
	VIEWABLEPAGE("viewable"),
	
	/**
	 * viewable page showing all resources of a specified group and a given tag or list of tags
	 */	
	VIEWABLETAGPAGE("viewabletag"),
	
	/**
	 * author page
	 */
	AUTHORPAGE("author"),
	
	/**
	 * the author tag showing all resources of a specified author and a given tag or list of tags
	 */
	AUTHORTAGPAGE("authortag"),
	
	/**
	 * search page
	 */
	SEARCHPAGE("search"),
	
	/**
	 * additional posts 
	 */
	MY_GROUP_POSTS_PAGE("myGroupPosts"), 

	/**
	 * relevant-for page
	 */
	RELEVANTFORPAGE("relevantfor"),

	/** scraper info list **/
	SCRAPER_INFO("scraperInfo"),

	/**
	 * reporting
	 */
	REPORTING("reporting"),
	
	/* *****************************************************
	 * OpenSocial Views
	 * *****************************************************/
	/**
	 * sample gadget container page
	 */
	GADGETCONTAINER("opensocial/container"),
	
	/* *****************************************************
	 * AJAX views
	 * *****************************************************/

	/**
	 * command.responseString is written withOUT (!) c:out 
	 */
	AJAX_JSON("ajax/snippetJson"),
	/**
	 * command.responseString is written with c:out
	 */
	AJAX_TEXT("ajax/snippetPlain"),
	/**
	 * response page snippet for xml ajax requests
	 */
	AJAX_XML("ajax/snippetXML"),	
	
	/**
	 * used by editBookmark to get the details for a given Url
	 */
	AJAX_GET_TITLE_FOR_URL("ajax/jsonURLDetails"),

	/**
	 * get bibtex keys for a given user
	 */
	AJAX_GET_BIBTEXKEYS_FOR_USER("ajax/bibtexkeys"),
	
	/**
	 * posts 
	 */
	AJAX_POSTS("ajax/posts"),
	
	/**
	 * spammer predictions 
	 */
	AJAX_PREDICTIONS("ajax/predictions"),

	/**
	 * a tag cloud (to reload it using AJAX)
	 */
	AJAX_TAGCLOUD("ajax/tagcloud"),
	
	/**
	 * response for quick AJAX tag editing
	 */
	AJAX_EDITTAGS("ajax/edittags"),	
	
	/**
	 * errors in JSON
	 */
	AJAX_ERRORS("ajax/errors"),

	/* *****************************************************
	 * ADMIN views
	 * *****************************************************/	
	/**
	 * spam admin page
	 */
	ADMIN_SPAM("actions/admin/spam"),
	/**
	 * lucene admin page
	 */
	ADMIN_LUCENE("actions/admin/lucene"),
	/**
	 * recommender admin page
	 */
	ADMIN_RECOMMENDER("actions/admin/recommender"),
	/**
	 * Show the page for administrating groups
	 */
	ADMIN_GROUP("actions/admin/group"),
	/**
	 * manage OAuth consumer keys
	 */
	ADMIN_OAUTH("actions/admin/oauth"),
	/**
	 * manage synchronization settings
	 */
	ADMIN_SYNC("actions/admin/sync"),
	/**
	 * general admin page
	 */
	ADMIN("actions/admin/index"),

	/* *****************************************************
	 * action views
	 * *****************************************************/
	
	/**
	 * where users can register
     * TODO: we will probably move those action parts
     * into a separate Views class!
	 */
	REGISTER_USER("actions/register/user"),

	/**
	 * After a user has successfully registered that must still be activated.
	 */
	REGISTER_USER_TO_BE_ACTIVATED("actions/register/user_to_be_activated"),

	/**
	 * After a user has successfully registered, he will see this view.
	 */
	REGISTER_USER_SUCCESS("actions/register/user_success"),
	
	/**
	 * After a user has been successfully activated, he will see this view.
	 */
	ACTIVATE_USER_SUCCESS("actions/register/user_activated"),
	
	/**
	 * When admins successfully register a user, this page shows them
	 * the details.
	 */
	REGISTER_USER_SUCCESS_ADMIN("actions/register/user_success_admin"),
	
	/**
	 * where user can register using her openid url
	 */
	REGISTER_USER_OPENID("actions/register/openid/user"),
	
	/**
	 * OpenID register form prefilled with information from 
	 * the OpenID provider
	 */
	REGISTER_USER_OPENID_PROVIDER_FORM("actions/register/openid/provider_form"),
	
	/**
	 * LDAP register form prefilled with information from LDAP server 
	 * 
	 */
	REGISTER_USER_LDAP_FORM("actions/register/ldap/profile_form"),
		
	/**
	 * Log into the system. 
	 */
	LOGIN("actions/login"),
		
	/**
	 * The dialog to EDIT a bookmark (big dialog).
	 */
	EDIT_BOOKMARK("actions/post/editBookmark"),
	
	/**
	 * The dialog to EDIT a publication (big dialog).
	 */
	EDIT_PUBLICATION("actions/post/editPublication"),
	
	/**
	 * The dialog to EDIT a gold standard publication (big dialog)
	 */
	EDIT_GOLD_STANDARD_PUBLICATION("actions/post/editGoldStandardPublication"), 
	
	/**
	 * The dialog to enter a URL for posting (small dialog).
	 */
	POST_BOOKMARK("actions/post/postBookmark"), 	
	
	/**
	 * The dialog to post one or more publications (tabbed view)
	 */
	POST_PUBLICATION("actions/post/postPublication"),
	
	/**
	 * join group view
	 */
	JOIN_GROUP("actions/join_group"),
	
	/**
	 * join group view
	 */
	SUCCESS("actions/success"),
	
	/** 
	 * import view 
	 */
	IMPORT("actions/post/import"),
	
	/**
     * Show a form to request a password reminder.
     */ 
	PASSWORD_REMINDER("actions/user/passwordReminder"), 
	
	/**
	 * Show the form after reminding a password to change it
	 */
	PASSWORD_CHANGE_ON_REMIND("actions/user/passwordChangeOnRemind"),
	
	/**
	 * view for streaming the desired document to the outputResponse
	 */
	DOWNLOAD_FILE("downloadFile"),
	
	/**
	 * to edit the tags of publications
	 */
	BATCHEDITBIB("actions/edit/batcheditbib"),

	/**
	 * to edit the tags of bookmarks 
	 */
	BATCHEDITURL("actions/edit/batchediturl"),
	
	/**
	 * edit tags
	 */
	EDIT_TAGS("actions/edit/edittags"),
	
	/* *****************************************************
	 * OpenSocial views
	 * *****************************************************/
	/**
	 * a plain text OAuth response message (e.g. "/oauth/requestToken")
	 */
	OAUTH_RESPONSE("opensocial/oauthResponse"),
	
	/**
	 * display an OAuth authorization request 
	 */
	OAUTH_AUTHORIZE("opensocial/authorize"),

	/**
	 * display an OAuth authorization denial
	 */
	OAUTH_DENY("opensocial/deny"),
	
	/**
	 * display an OAuth authorization success message
	 */
	OAUTH_AUTHORIZATION_SUCCESS("opensocial/authorizationSuccess"),
	
	/* *****************************************************
	 * query independent views to show bookmark or 
	 * publication lists
	 * *****************************************************/
	
	/**
	 * bibtex output
	 */
	BIBTEX("export/bibtex/bibtex"),
	
	/**
	 * EndNote (RIS) output 
	 */
	ENDNOTE("export/bibtex/endnote"),
	
	/**
	 * burst output for publications
	 */
	BURST("export/bibtex/burst"),
	
	/**
	 * rss bookmark outout for bookmarks
	 */
	RSS("export/bookmark/rssfeed"),
	
	/**
	 * rss output for publications
	 */
	PUBLRSS("export/bibtex/rssfeed"),
	
	/**
	 * RSS output for publications with modification for NEPOMUK project  
	 */
	PUBLRSSNEPOMUK("export/bibtex/rssfeedNepomuk"),
	
	/**
	 * swrc output for publications
	 */
	SWRC("export/bibtex/swrc"),
	
	/**
	 * html output for publications
	 */
	PUBL("export/bibtex/htmlOutput"),
	
	/**
	 * aparss output for publications
	 */
	APARSS("export/bibtex/aparssfeed"),
	
	/**
	 * xml output for bookmarks
	 */
	XML("export/bookmark/xmlOutput"),
	
	/**
	 * JSON for both bookmarks and publications
	 */
	JSON("export/json"),
	
	/**
	 * html output for bookmarks
	 */
	BOOKPUBL("export/bookmark/bookpubl"),
	
	/**
	 * BibTeX output for bookmarks 
	 */
	BOOKBIB("export/bookmark/bibtex"),
	
	/**
	 * An XML file printing all output formats supported by UnAPI.
	 * Basically, a list of some of our export formats in XML.
	 */
	UNAPI_SUPPORTED_FORMATS("export/bibtex/unapi"),	
	
	/**
	 * /layout/* pages which are rendered by JabRef 
	 */
	LAYOUT("layout"),	
	/**
	 * csl-compatible JSON output
	 */
	CSL("csl"),
	/**
	 * /csv/ pages rendered by the CSVView
	 */
	CSV("csv"),
	
	/**
	 * show the export page
	 */
	EXPORT("export"),

	/**
	 * export layouts in a JSON object
	 */
	EXPORTLAYOUTS("exportLayouts"),	
	
	/* *************************************************************************
	 * TODO: The following views have been added by not so careful developers 
	 * who forgot to put them at the correct place above this comment. Please
	 * have a look and see, into which section your view fits best!  
	 *  
	 * *************************************************************************/
	/**
	 * show the advanced_search page
	 */
	MYSEARCH("mySearch"),
	
	/**
	 * show button-page view
	 */
	BUTTONS("buttons"),
	
	/**
	 * show relations
	 */
	RELATIONS("relations"), 
	
	/**
	 * show followers
	 */
	FOLLOWERS("followers"),
	
	/*
	 * Error pages
	 */
	/**
	 * error page
	 */
	ERROR("error"),
	
	/**
	 * 403
	 */
	ERROR403("errors/403"),

	/**
	 * 404
	 */
	ERROR404("errors/404"),

	/**
	 * 405
	 */
	ERROR405("errors/405"),

	/**
	 * 500
	 */
	ERROR500("errors/500"),

	/**
	 * 503
	 */
	ERROR503("errors/503"),
	
	/**
	 * PUMA the first page you see when entering the application
	 */
	PUMAHOMEPAGE("pumahome"),
	
	/**
	 * PUMA, author agreement page, form filled in with publication data for printing 
	 */
	AUTHORAGREEMENTPAGE("authoragreement");
	

	/*
	 * both bookmarks and publications
	 */
	public static final String FORMAT_STRING_CSV = "csv";
	public static final String FORMAT_STRING_JSON = "json";
	/*
	 * publications
	 */
	/**
	 * BibTeX can be requested by using the URL path "/bib/" or by using the
	 * query parameter "format=bibtex". Hence, we have two versions here:
	 * "bibtex" is matched against the query parameter ...  
	 */
	public static final String FORMAT_STRING_BIBTEX = "bibtex";
	/**
	 * and "bib" against the URL path. Decide carefully, which one you use!
	 * (I would call this a FIXME, but we can neither change the path nor the
	 * parameter, since they are well-known.)
	 */
	public static final String FORMAT_STRING_BIB = "bib";
	public static final String FORMAT_STRING_CSL = "csl";
	public static final String FORMAT_STRING_LAYOUT = "layout";
	public static final String FORMAT_STRING_BURST = "burst";
	public static final String FORMAT_STRING_APARSS = "aparss";
	public static final String FORMAT_STRING_ENDNOTE = "endnote";
	public static final String FORMAT_STRING_PUBL = "publ";
	public static final String FORMAT_STRING_SWRC = "swrc";
	public static final String FORMAT_STRING_PUBLRSS = "publrss";
	public static final String FORMAT_STRING_PUBLRSSN = "publrssN";
	/*
	 * bookmarks
	 */
	public static final String FORMAT_STRING_XML = "xml";
	public static final String FORMAT_STRING_RSS = "rss";
	public static final String FORMAT_STRING_BOOKPUBL = "bookpubl";
	public static final String FORMAT_STRING_BOOKBIB = "bookbib";
	
	private final String name;
	
	private Views(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.bibsonomy.webapp.util.View#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Helper method to identify those formats whose corresponding view 
	 * displays ONLY bibtex posts
	 * 
	 * @param format the name of the format
	 * @return true if the corresponding view displays only bibtex posts, false otherwise
	 */
	public static boolean isPublicationOnlyFormat(final String format) {
		return 
			FORMAT_STRING_BIBTEX.equals(format) || 
			FORMAT_STRING_PUBLRSS.equals(format) ||
			FORMAT_STRING_PUBL.equals(format) ||			
			FORMAT_STRING_APARSS.equals(format) ||
			FORMAT_STRING_BURST.equals(format) ||
			FORMAT_STRING_LAYOUT.equals(format) ||
			FORMAT_STRING_CSL.equals(format) ||
			"batcheditbib".equals(format) ||
			FORMAT_STRING_SWRC.equals(format);
	}
	
	/**
	 * Helper method to identify those formats whose corresponding view 
	 * displays ONLY bookmark posts
	 * 
	 * @param format the name of the format
	 * @return true if the corresponding view displays only bookmark posts, false otherwise
	 */
	public static boolean isBookmarkOnlyFormat(final String format) {
		return FORMAT_STRING_XML.equals(format) || 
			FORMAT_STRING_RSS.equals(format) ||
			FORMAT_STRING_BOOKBIB.equals(format) ||
			"batchediturl".equals(format) ||
			FORMAT_STRING_BOOKPUBL.equals(format);
	}
	
	/**
	 * Helper method to retrieve a view by a format string (passed to the
	 * application via e.g. ?format=rss)
	 * 
	 * @param format the name of the format
	 * @return the corresponding view for a given format
	 */
	public static Views getViewByFormat(final String format) {
		if (FORMAT_STRING_BIB.equals(format) || FORMAT_STRING_BIBTEX.equals(format))
			return BIBTEX;
		if (FORMAT_STRING_JSON.equals(format)) 
			return JSON;
		if (FORMAT_STRING_BURST.equals(format))
			return BURST;
		if (FORMAT_STRING_RSS.equals(format))
			return RSS;
		if (FORMAT_STRING_PUBLRSS.equals(format))
			return PUBLRSS;
		if (FORMAT_STRING_SWRC.equals(format))
			return SWRC;
		if (FORMAT_STRING_PUBL.equals(format))
			return PUBL;
		if (FORMAT_STRING_ENDNOTE.equals(format))
			return ENDNOTE;
		if (FORMAT_STRING_APARSS.equals(format))
			return APARSS;
		if (FORMAT_STRING_XML.equals(format))
			return XML;
		if (FORMAT_STRING_BOOKPUBL.equals(format))
			return BOOKPUBL;
		if (FORMAT_STRING_BOOKBIB.equals(format))
			return BOOKBIB;
		if (FORMAT_STRING_PUBLRSSN.equals(format))
			return PUBLRSSNEPOMUK;
		if (FORMAT_STRING_LAYOUT.equals(format))
			return LAYOUT;
		if ("batcheditbib".equals(format)) 
			return BATCHEDITBIB;
		if ("batchediturl".equals(format)) 
			return BATCHEDITURL;
		if ("tagcloud".equals(format))
			return AJAX_TAGCLOUD;
		if (FORMAT_STRING_CSV.equals(format))
			return CSV;
		if (FORMAT_STRING_CSL.equals(format))
			return CSL;
		
		throw new BadRequestOrResponseException("Invalid format specification.");
	}
}
