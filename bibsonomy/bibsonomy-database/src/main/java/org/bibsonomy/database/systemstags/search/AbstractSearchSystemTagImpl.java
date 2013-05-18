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

package org.bibsonomy.database.systemstags.search;

import org.bibsonomy.database.systemstags.AbstractSystemTagImpl;
import org.bibsonomy.database.systemstags.SystemTagsUtil;

/**
 * @author sdo
 * @version $Id: AbstractSearchSystemTagImpl.java,v 1.2 2011-06-16 14:29:58 doerfel Exp $
 */
public abstract class AbstractSearchSystemTagImpl extends AbstractSystemTagImpl implements SearchSystemTag{

	@Override
	public boolean isToHide() {
		return false;
	}

	@Override
	public boolean isInstance(String tagName) {
		return SystemTagsUtil.hasPrefixTypeAndArgument(tagName) && SystemTagsUtil.extractType(tagName).equals(this.getName());
	}

}
