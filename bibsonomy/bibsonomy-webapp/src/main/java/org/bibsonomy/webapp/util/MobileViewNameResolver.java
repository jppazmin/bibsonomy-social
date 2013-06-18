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

package org.bibsonomy.webapp.util;

import java.util.HashMap;
import java.util.Map;

import org.bibsonomy.webapp.view.MobileViews;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Waldemar Biller <biller@cs.uni-kassel.de>
 * @version $Id: MobileViewNameResolver.java,v 1.4 2011-05-17 09:06:42 sven Exp $
 */
public abstract class MobileViewNameResolver {

	/**
	 * currently supported pages by mobile site
	 */
	private static View[][] viewsArray = new View[][] {
		{Views.HOMEPAGE, MobileViews.HOMEPAGE},
		{Views.PUMAHOMEPAGE, MobileViews.PUMAHOMEPAGE},
		{Views.USERPAGE, MobileViews.USERPAGE},
		{Views.TAGPAGE, MobileViews.TAGPAGE},
		{Views.SEARCHPAGE, MobileViews.SEARCHPAGE},
		{Views.POST_BOOKMARK, MobileViews.POST_BOOKMARK},
		{Views.POST_PUBLICATION, MobileViews.POST_PUBLICATION},
		{Views.BIBTEXDETAILS, MobileViews.BIBTEXDETAILS},
		{Views.GROUPPAGE, MobileViews.GROUPPAGE},
		{Views.LOGIN, MobileViews.LOGIN},
		{Views.USERTAGPAGE, MobileViews.USERTAGPAGE},
		{Views.EDIT_BOOKMARK, MobileViews.EDIT_BOOKMARK},
		{Views.EDIT_PUBLICATION, MobileViews.EDIT_PUBLICATION},
		{Views.AUTHORPAGE, MobileViews.AUTHORPAGE},
		{Views.URLPAGE, MobileViews.URLPAGE},
		{Views.POPULAR_TAGS, MobileViews.POPULAR_TAGS}
	};
	
	private static final Map<String, String> views;
	
	static {
		
		// convert array to map
		views = new HashMap<String, String>();
		
		for(View[] view : viewsArray) {
			
			views.put(view[0].getName(), view[1].getName());
		}
	}
	
	/**
	 * Resolve the view name from desktop to mobile
	 * @param viewName
	 * @return the mobile view name
	 */
	public static String resolveView(String viewName) {
		
		if(views.containsKey(viewName))
			return views.get(viewName);
		
		return viewName;
	}
}