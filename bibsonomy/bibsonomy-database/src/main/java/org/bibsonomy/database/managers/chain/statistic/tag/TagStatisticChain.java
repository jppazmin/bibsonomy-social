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

package org.bibsonomy.database.managers.chain.statistic.tag;

import org.bibsonomy.database.managers.chain.ChainElement;
import org.bibsonomy.database.managers.chain.FirstChainElement;
import org.bibsonomy.database.managers.chain.statistic.tag.get.GetRelationCountByUser;
import org.bibsonomy.database.params.StatisticsParam;

/**
 * Chain of Responsibility for counts regarding tags
 * 
 * @author Stefan Stützer
 * @version $Id: TagStatisticChain.java,v 1.5 2010-07-09 11:28:22 nosebrain Exp $
 */
public class TagStatisticChain implements FirstChainElement<Integer, StatisticsParam> {

	private final GetRelationCountByUser getRelationCountByUser;
	
	/**
	 * 
	 */
	public TagStatisticChain() {
		getRelationCountByUser = new GetRelationCountByUser();
	}
	
	@Override
	public ChainElement<Integer, StatisticsParam> getFirstElement() {
		return this.getRelationCountByUser;
	}
}