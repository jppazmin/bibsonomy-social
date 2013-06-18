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

import java.util.List;

/**
 * @author rja
 * @version $Id: TabsCommandInterface.java,v 1.1 2009-11-27 13:34:41 rja Exp $
 * @param <T> 
 */
public interface TabsCommandInterface<T> {

	/**
	 * @return content of tab
	 */
	public abstract List<T> getContent();

	/**
	 * Sets the content of the current selected tab
	 * @param content
	 */
	public abstract void setContent(List<T> content);

	/**
	 * @return ID of current selected tab
	 */
	public abstract Integer getSelTab();

	/**
	 * Sets the id of the current selected tab
	 * @param selectedTab The tab ID
	 */
	public abstract void setSelTab(Integer selectedTab);

	/**
	 * @return List of defined tabs 
	 */
	public abstract List<TabCommand> getTabs();

	/**
	 * @param tabs Sets the tabs 
	 */
	public abstract void setTabs(List<TabCommand> tabs);


}