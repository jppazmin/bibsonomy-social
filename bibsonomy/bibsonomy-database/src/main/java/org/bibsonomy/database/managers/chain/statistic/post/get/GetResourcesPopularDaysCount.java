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

package org.bibsonomy.database.managers.chain.statistic.post.get;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.managers.chain.statistic.StatisticChainElement;
import org.bibsonomy.database.params.StatisticsParam;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.enums.Order;

/**
 * @author mwa
 * @version $Id: GetResourcesPopularDaysCount.java,v 1.4 2011-06-07 13:33:41 rja Exp $
 */
public class GetResourcesPopularDaysCount extends StatisticChainElement {

	@Override
	protected Integer handle(StatisticsParam param, DBSession session) {
		if (param.getContentType() == ConstantID.BOOKMARK_CONTENT_TYPE.getId()) {
			return this.db.getPopularDays(Bookmark.class, param.getDays(), session);
		}
		
		if (param.getContentType() == ConstantID.BIBTEX_CONTENT_TYPE.getId()) {
			return this.db.getPopularDays(BibTex.class, param.getDays(), session);
		}
		
		return Integer.valueOf(0);
	}
	
	@Override
	protected boolean canHandle(StatisticsParam param) {
		return 	param.getOrder() == Order.POPULAR &&
				param.getDays() >= 0 &&
				param.getGrouping() == GroupingEntity.ALL &&
				!present(param.getHash()) &&
				!present(param.getSearch());
				
	}

}
