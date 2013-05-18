/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.params.beans;

/**
 * This class holds the tagname and the corresponding index and join-index.
 * While the name of the class might be misleading, it can be used for tags as
 * well for concepts.
 * 
 * @author Christian Schenk
 * @version $Id: TagIndex.java,v 1.1 2010-06-02 11:10:17 nosebrain Exp $
 */
public class TagIndex {

	/** This name can be both a name of a tag or concept. */
	private final String tagName;
	/** A index to produce a self-join, like t1...=t2..., t2...=t3..., etc. */
	private final int index;

	/**
	 * Creates a new instance with the given namen an start index.
	 * 
	 * @param tagName
	 * @param index
	 */
	public TagIndex(final String tagName, final int index) {
		this.tagName = tagName;
		this.index = index;
	}

	/**
	 * @return the tag's name
	 */
	public String getTagName() {
		return this.tagName;
	}

	/**
	 * @return current index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Retrieves the join-index which is always the current index plus one.
	 * 
	 * Hint: a call to this function isn't idempotent, i.e. it changes the value
	 * of the index.
	 * 
	 * @return current index plus one
	 */
	public int getIndex2() {
		return (this.index + 1);
	}
}