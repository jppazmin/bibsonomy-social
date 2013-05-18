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

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.statistic.StatisticChainElement;
import org.bibsonomy.database.params.StatisticsParam;

/**
 * @author DaiLL
 * @version $Id: DefaultCatchAllCount.java,v 1.3 2010-09-28 11:19:42 nosebrain Exp $
 * 
 * Catches all possibilities of requesting statistics in case of no match without throwing an error.
 */
public class DefaultCatchAllCount extends StatisticChainElement {

	@Override
	protected Integer handle(StatisticsParam param, DBSession session) {
		return null;
	}
	
	@Override
	protected boolean canHandle(StatisticsParam param) {
		return true;
	}

}
