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

import java.util.Map;

import org.bibsonomy.layout.jabref.JabrefLayout;

/**
 * @author daill
 * @version $Id: ExportPageCommand.java,v 1.4 2010-02-05 11:21:27 rja Exp $
 */
public class ExportPageCommand extends ResourceViewCommand{
	
	private Map<String, JabrefLayout> layoutMap;
	private String lang;

	/**
	 * @return language code
	 */
	public String getLang() {
		return this.lang;
	}

	/**
	 * @param lang
	 */
	public void setLang(final String lang) {
		this.lang = lang;
	}

	/**
	 * @return jabref layout map
	 */
	public Map<String, JabrefLayout> getLayoutMap() {
		return this.layoutMap;
	}

	/**
	 * @param layoutMap
	 */
	public void setLayoutMap(final Map<String, JabrefLayout> layoutMap) {
		this.layoutMap = layoutMap;
	}

	
}
