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


/**
 * Bean for a single tab in a multiple tab context
 * 
 * @author Stefan Stützer
 * @version $Id: TabCommand.java,v 1.1 2008-03-26 19:26:07 ss05sstuetzer Exp $
 */
public class TabCommand {
	
	/** The id of the tab */
	private Integer id;
	
	/** The title of the tab */
	private String title;		
	
	/**
	 * Constructor
	 * @param id ID
	 * @param title Title of tab
	 */
	public TabCommand(Integer id, String title) {
		this.id = id;
		this.title = title;
	}
	
	/**
	 * @return tab id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets tab id
	 * @param id tab id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return tab title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * sets the title of the tab
	 * @param title tab title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}