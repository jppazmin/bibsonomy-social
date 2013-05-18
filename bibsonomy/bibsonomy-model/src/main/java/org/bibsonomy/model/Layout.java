/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author:  rja
 * @version: $Id: Layout.java,v 1.9 2011-04-29 06:45:05 bibsonomy Exp $ 
 */
public abstract class Layout {

	/**
	 * Public layouts are announced on the layout overview pages (/export/). 
	 * NOTE: Making a layout not public does <strong>not</strong> mean that
	 * it can't be publicly used! Everybody who knows the URL prefix for the
	 * layout can access it!
	 * 
	 */
	protected boolean publicLayout = true;
	/**
	 * The name of the layout (used as identifier in the URL).
	 */
	protected final String name;
	/**
	 * The name shown to the user.
	 */
	protected String displayName;
	/**
	 * A short textual description.
	 */
	protected Map<String,String> description;
	/**
	 * The mime type of the rendered file.
	 */
	protected String mimeType;
	/**
	 * The extension of the rendered file.
	 */
	protected String extension;

	/** 
	 * @param name the name of the layout
	 */
	public Layout(final String name) {
		this.name = name;
		this.description = new HashMap<String, String>();
	}
	
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the description
	 */
	public Map<String, String> getDescription(){
		return this.description;
	}

	/**
	 * TODO: improve documentation
	 * 
	 * @param lang
	 * @param description
	 */
	public void setDescription(String lang, String description) {
		this.description.put(lang, description);
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return this.extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Public layouts are announced on the layout overview pages (/export/). 
	 * NOTE: Making a layout not public does <strong>not</strong> mean that
	 * it can't be publicly used! Everybody who knows the URL prefix for the
	 * layout can access it!
	 * @return <true> iff the layout is public
	 */
	public boolean isPublicLayout() {
		return publicLayout;
	}

	/**
	 * Public layouts are announced on the layout overview pages (/export/). 
	 * NOTE: Making a layout not public does <strong>not</strong> mean that
	 * it can't be publicly used! Everybody who knows the URL prefix for the
	 * layout can access it!
	 * @param publicLayout
	 */
	public void setPublicLayout(boolean publicLayout) {
		this.publicLayout = publicLayout;
	}
	
	@Override
	public String toString() {
		return name + "(" + displayName + ", '" + mimeType + ", " + extension + ")";
	}
}
