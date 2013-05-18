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

package org.bibsonomy.database.managers.chain.tag.get;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.tag.TagChainElement;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.enums.Order;

/**
 * @author Stefan Stützer
 * @version $Id: GetPopularTags.java,v 1.4 2010-06-02 11:31:58 nosebrain Exp $
 */
public class GetPopularTags extends TagChainElement {
	
	@Override
	protected List<Tag> handle(TagParam param, DBSession session) {
		return this.db.getPopularTags(param, session);
	}
	
	@Override
	protected boolean canHandle(TagParam param) {
		return (GroupingEntity.ALL.equals(param.getGrouping()) && 
				Order.POPULAR.equals(param.getOrder()) &&
				!present(param.getRegex()) &&
				!present(param.getSearch()) &&
				!present(param.getTitle()) &&
				!present(param.getAuthor()) &&
				!present(param.getBibtexKey()) &&
				!present(param.getHash()));
	}

}
