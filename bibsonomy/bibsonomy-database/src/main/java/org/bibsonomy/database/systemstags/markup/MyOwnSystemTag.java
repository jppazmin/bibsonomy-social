/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.systemstags.markup;

import org.bibsonomy.database.systemstags.AbstractSystemTagImpl;

/**
 * @author sdo
 * @version $Id: MyOwnSystemTag.java,v 1.4 2011-07-21 18:08:03 doerfel Exp $
 */
public class MyOwnSystemTag extends AbstractSystemTagImpl implements MarkUpSystemTag {

	public static final String NAME = "myown";
	private static boolean toHide = false;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isToHide() {
		return toHide;
	}

	@Override
	public MyOwnSystemTag newInstance() {
		return new MyOwnSystemTag();
	}

	@Override
	public boolean isInstance(final String tagName) {
		return NAME.equals(tagName.toLowerCase());
	}

}
