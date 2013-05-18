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
import org.bibsonomy.database.managers.InboxDatabaseManager;
import org.bibsonomy.database.managers.chain.statistic.StatisticChainElement;
import org.bibsonomy.database.params.StatisticsParam;

/**
 * Gets count of resources in the inbox of a user
 *  
 */
public class GetResourcesForUserInboxCount extends StatisticChainElement {

	private InboxDatabaseManager inboxDb;
	
	@Override
	protected Integer handle(StatisticsParam param, DBSession session) {
		this.inboxDb = InboxDatabaseManager.getInstance();
		return this.inboxDb.getNumInboxMessages(param.getRequestedUserName(), param.getContentTypeConstant(), session);
	}

	@Override
	protected boolean canHandle(StatisticsParam param) {
		return 	(param.getGrouping() == GroupingEntity.INBOX) && 
				present(param.getRequestedUserName());
	}
}