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

import org.bibsonomy.database.params.GenericParam;
import org.bibsonomy.model.Resource;

/**
 * @author sdo
 * @version $Id: DaysSystemTag.java,v 1.4 2011-06-16 14:29:58 doerfel Exp $
 */
public class DaysSystemTag extends AbstractSearchSystemTagImpl implements SearchSystemTag {

	public static final String NAME = "days";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public DaysSystemTag newInstance() {
		return new DaysSystemTag();
	}

	@Override
	public void handleParam(GenericParam param) {
		/*
		 * FIXME: What do we clear the TagIndex for, What is the TagIndes
		 */
		param.getTagIndex().clear();
		/*
		 * FIXME: How do we handle NumberFormatExceptions from parseInt
		 */
		param.setDays(Integer.parseInt(this.getArgument()));
		log.debug("set days to " + this.getArgument() + " after matching for days system tag");
	}

	@Override
	public <T extends Resource> boolean allowsResource(Class<T> resourceType) {
		return true;
	}


}
