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

import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.managers.chain.statistic.StatisticChainElement;
import org.bibsonomy.database.params.StatisticsParam;
import org.bibsonomy.model.BibTex;

/**
 * @author Stefan Stützer
 * @version $Id: GetResourcesDuplicateCount.java,v 1.5 2011-06-07 13:33:41 rja Exp $
 */
public class GetResourcesDuplicateCount extends StatisticChainElement {

	@Override
	protected Integer handle(StatisticsParam param, DBSession session) {
		if (param.getContentType() == ConstantID.BIBTEX_CONTENT_TYPE.getId()) {
			return this.db.getNumberOfDuplicates(BibTex.class, param.getRequestedUserName(), session);
		}
		
		throw new UnsupportedResourceTypeException("Resource type not supported for this query.");
	}

	@Override
	protected boolean canHandle(StatisticsParam param) {
		return 	param.getGrouping() == GroupingEntity.USER &&
				param.getFilter() == FilterEntity.DUPLICATES;
	}
}