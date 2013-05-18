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
import org.bibsonomy.database.systemstags.SystemTagsUtil;
/**
 * @author sdo
 * @version $Id: HiddenTagSystemTag.java,v 1.1 2011-07-21 18:08:42 doerfel Exp $
 * This SystemTag is used to create own tags that are hidden from other users
 * The tag is of the form sys:hidden:<MyHiddenArgument> where the sys|system prefix is optional
 * The argument cannot be empty
 */
public class HiddenTagSystemTag extends AbstractSystemTagImpl implements MarkUpSystemTag {

	public static final String NAME = "hidden";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isToHide() {
		return true;
	}


	@Override
	public HiddenTagSystemTag newInstance() {
		return new HiddenTagSystemTag();
	}

	@Override
	public boolean isInstance(final String tagName) {
		return SystemTagsUtil.hasTypeAndArgument(tagName) && NAME.equals(SystemTagsUtil.extractType(tagName));
	}

}