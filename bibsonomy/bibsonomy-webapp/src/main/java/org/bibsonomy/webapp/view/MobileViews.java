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

package org.bibsonomy.webapp.view;

import org.bibsonomy.webapp.util.View;

/**
 * @author Waldemar Biller <biller@cs.uni-kassel.de>
 * @version $Id: MobileViews.java,v 1.5 2011-05-17 09:06:42 sven Exp $
 */
public enum MobileViews implements View {
	
	/**
	 * the first page you see when entering the application
	 */
	HOMEPAGE("mobile/home"),
	
	/**
	 * the first page you see when entering the application
	 */
	PUMAHOMEPAGE("mobile/home"),

	/**
	 * user page displaying the resources of a single user
	 */
	USERPAGE("mobile/user"),
	
	/**
	 * tag page show all resources with a given tag or a list of tags
	 */
	TAGPAGE("mobile/tag"),
	
	/**
	 * search page
	 */
	SEARCHPAGE("mobile/search"),
	
	/**
	 * The dialog to enter a URL for posting (small dialog).
	 */
	POST_BOOKMARK("mobile/post_bookmark"),
	
	/**
	 * The dialog to post one or more publications (tabbed view)
	 */
	POST_PUBLICATION("mobile/post_publication"),
	
	/**
	 * details of a publication 
	 */
	BIBTEXDETAILS("mobile/bibtex_details"),
	
	/**
	 * group page showing all resources of a specified group
	 */
	GROUPPAGE("mobile/group"),
	
	/**
	 * user page displaying the resources of a single user tagged with a given list of tags
	 */
	USERTAGPAGE("mobile/tag"),	
	
	/**
	 * The dialog to EDIT a bookmark.
	 */
	EDIT_BOOKMARK("mobile/edit_bookmark"),
	
	/**
	 * he dialog to EDIT a publication.
	 */
	EDIT_PUBLICATION("mobile/edit_publication"),
	
	/**
	 * authors overview page
	 */
	AUTHORPAGE("mobile/author"),
	
	/**
	 * url page, displays all bookmarks for a given url hash  
	 */
	URLPAGE("mobile/url"),
	
	/**
	 * popular tags page
	 */
	POPULAR_TAGS("mobile/popular_tags"),
	
	/**
	 * 
	 */
	LOGIN("mobile/login");
		
	private String name;
	
	private MobileViews(String name) {
		
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}