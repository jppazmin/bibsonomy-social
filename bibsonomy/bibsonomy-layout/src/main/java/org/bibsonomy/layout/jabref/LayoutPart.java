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


/**
 * The parts a layout consists of: begin, item, end.
 * <ul>
 * <li>begin: prepended to the result</li>
 * <li>end: appended to the result</li>
 * <li>item: used to format one item</li> 
 * 
 * @author:  rja
 * @version: $Id: LayoutPart.java,v 1.11 2011-04-29 07:34:00 bibsonomy Exp $
 * $Author: bibsonomy $
 * 
 */
public enum LayoutPart {
	/**
	 * 
	 */
	BEGIN("begin"), 
	/**
	 * 
	 */
	EMBEDDEDBEGIN("embeddedbegin"),
	/**
	 * 
	 */
	END("end"),
	/**
	 * 
	 */
	EMBEDDEDEND("embeddedend"),
	/**
	 * 
	 */
	ITEM("item");

	public static LayoutPart[] layoutParts = new LayoutPart[]{ BEGIN, END, ITEM, EMBEDDEDBEGIN, EMBEDDEDEND};
	
	private static String[] allTypes = new String[] { BEGIN.name, END.name, ITEM.name, EMBEDDEDBEGIN.name, EMBEDDEDEND.name};

	/**
	 * The name of a part.
	 */
	private String name;
	
	private LayoutPart(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static LayoutPart getLayoutType (final String typeString) {
		for (final LayoutPart part: layoutParts) {
			if (part.getName().equals(typeString)) return part;
		}
		return ITEM;
	}

	@Override
	public String toString() {
		return name;
	}

	public static String[] getLayoutTypes () {
		return allTypes;
	}

}

