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

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.params.GenericParam;
import org.bibsonomy.model.Resource;

/**
 * @author sdo
 * @version $Id: GroupSystemTag.java,v 1.5 2011-06-16 14:29:58 doerfel Exp $
 */
public class GroupSystemTag extends AbstractSearchSystemTagImpl implements SearchSystemTag {

	public static final String NAME = "group";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public GroupSystemTag newInstance() {
		return new GroupSystemTag();
	}

	@Override
	public void handleParam(GenericParam param) {
		param.setGrouping(GroupingEntity.GROUP);
		param.setRequestedGroupName(this.getArgument());
		log.debug("set grouping to 'group' and requestedGroupName to " + this.getArgument() + " after matching for group system tag");
	}

	@Override
	public <T extends Resource> boolean allowsResource(Class<T> resourceType) {
		return true;
	}


}
