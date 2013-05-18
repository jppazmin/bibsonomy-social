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

package org.bibsonomy.database.managers.chain.statistic.post;

import org.bibsonomy.database.managers.chain.ChainElement;
import org.bibsonomy.database.managers.chain.FirstChainElement;
import org.bibsonomy.database.managers.chain.statistic.post.get.DefaultCatchAllCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesByTagNamesAndUserCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesByTagNamesCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesDuplicateCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesForGroupCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesForHashAndUserCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesForHashCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesForUserCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesForUserInboxCount;
import org.bibsonomy.database.managers.chain.statistic.post.get.GetResourcesPopularDaysCount;
import org.bibsonomy.database.params.StatisticsParam;

/**
 * Chain of Responsibility for counts regarding posts
 * 
 * @author Stefan Stützer
 * @version $Id: PostStatisticChain.java,v 1.10 2010-09-28 11:19:49 nosebrain Exp $
 */
public class PostStatisticChain implements FirstChainElement<Integer, StatisticsParam> {

	private final GetResourcesForGroupCount getResourcesForGroupCount;
	private final GetResourcesForUserCount getResourcesForUserCount;
	private final GetResourcesByTagNamesAndUserCount getResourcesByTagNamesAndUserCount;
	private final GetResourcesByTagNamesCount getResourcesByTagNamesCount;
	private final GetResourcesDuplicateCount getResourcesDuplicateCount;
	private final GetResourcesPopularDaysCount getResourcesPopularDays;
	private final GetResourcesForHashCount getResourcesForHashCount;
	private final GetResourcesForHashAndUserCount getResourcesForHashAndUserCount;
	private final GetResourcesForUserInboxCount getResourcesForUserInboxCount;
	private final DefaultCatchAllCount defaultCatchAllCount;
	
	/**
	 * Default Constructor
	 */
	public PostStatisticChain() {
		getResourcesForGroupCount 	= new GetResourcesForGroupCount();
		getResourcesForUserCount	= new GetResourcesForUserCount();
		getResourcesByTagNamesAndUserCount = new GetResourcesByTagNamesAndUserCount();
		getResourcesByTagNamesCount = new GetResourcesByTagNamesCount();
		getResourcesDuplicateCount 	= new GetResourcesDuplicateCount();
		getResourcesPopularDays = new GetResourcesPopularDaysCount();
		getResourcesForHashCount = new GetResourcesForHashCount();
		getResourcesForHashAndUserCount = new GetResourcesForHashAndUserCount();
		getResourcesForUserInboxCount = new GetResourcesForUserInboxCount();
		defaultCatchAllCount = new DefaultCatchAllCount();
		
		getResourcesForGroupCount.setNext(getResourcesForUserCount);
		getResourcesForUserCount.setNext(getResourcesByTagNamesAndUserCount);
		getResourcesByTagNamesAndUserCount.setNext(getResourcesByTagNamesCount);
		getResourcesByTagNamesCount.setNext(getResourcesDuplicateCount);
		getResourcesDuplicateCount.setNext(getResourcesPopularDays);
		getResourcesPopularDays.setNext(getResourcesForHashCount);
		getResourcesForHashCount.setNext(getResourcesForHashAndUserCount);
		getResourcesForHashAndUserCount.setNext(getResourcesForUserInboxCount);
		getResourcesForUserInboxCount.setNext(defaultCatchAllCount);
	}

	@Override
	public ChainElement<Integer, StatisticsParam> getFirstElement() {
	    return this.getResourcesForGroupCount;
	}
}