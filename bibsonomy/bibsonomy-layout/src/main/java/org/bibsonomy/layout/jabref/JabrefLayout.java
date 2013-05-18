/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
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

package org.bibsonomy.layout.jabref;

import java.util.HashMap;
import java.util.Map;

import net.sf.jabref.export.layout.Layout;


/**
 * Represents an entry of a jabref layout definition XML file according to
 * JabrefLayoutDefinition.xsd.  
 * 
 * @author:  rja
 * @version: $Id: JabrefLayout.java,v 1.8 2011-04-29 07:34:00 bibsonomy Exp $
 * $Author: bibsonomy $
 * 
 */
public class JabrefLayout extends org.bibsonomy.model.Layout {

	/**
	 * If the layout files are in a subdirectory of the layout directory, the name of the directory.
	 */
	private String directory;
	/**
	 * The base file name, most often equal to {@link #name}.
	 */
	private String baseFileName;
	
	/**
	 * <code>true</code>, if this is a custom user layout
	 */
	private boolean userLayout;
	
	/**
	 * The associated layouts filters. 
	 */
	private Map<String, Layout> subLayouts = new HashMap<String, Layout>();
	
	public JabrefLayout(final String name) {
		super(name);
	}

	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	public String getBaseFileName() {
		return baseFileName;
	}
	
	public void setBaseFileName(String baseFileName) {
		this.baseFileName = baseFileName;
	}

	@Override
	public String toString() {
		return super.toString() + "/" + directory + "/" + baseFileName + "(" + subLayouts.size() + ")";
	}

	public Layout getSubLayout(final String subLayoutName) {
		return subLayouts.get(subLayoutName);
	}

	public void addSubLayout(final String subLayoutName, final Layout layout) {
		subLayouts.put(subLayoutName, layout);
	}
	
	public Layout getSubLayout(final LayoutPart layoutPart) {
		return getSubLayout("." + layoutPart);
	}
	
	public void addSubLayout(final LayoutPart layoutPart, final Layout layout) {
		addSubLayout("." + layoutPart, layout);
	}

	public boolean isUserLayout() {
		return userLayout;
	}

	public void setUserLayout(boolean userLayout) {
		this.userLayout = userLayout;
	}

}

