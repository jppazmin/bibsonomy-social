/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest.strategy.users;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.rest.strategy.AbstractDeleteStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Stefan Stützer
 * @version $Id: DeleteUserConceptStrategy.java,v 1.3 2010-11-10 11:59:49 nosebrain Exp $
 */
public class DeleteUserConceptStrategy extends AbstractDeleteStrategy {

	private final String conceptName;
	private final String userName;
	private final String lowerTag;
	
	/**
	 * sets fields
	 * @param context
	 * @param conceptName
	 * @param userName
	 */
	public DeleteUserConceptStrategy(Context context, String conceptName, String userName) {
		super(context);
		this.conceptName = conceptName;
		this.userName = userName;
		
		this.lowerTag = context.getStringAttribute("subtag", null);		
	}

	@Override
	protected boolean delete() {
		// delete whole concept
		if (this.lowerTag == null) {
			this.getLogic().deleteConcept(this.conceptName, GroupingEntity.USER, this.userName);		
		} else {
			// delete relation only
			this.getLogic().deleteRelation(this.conceptName, this.lowerTag, GroupingEntity.USER, this.userName);
		}
		return true;
	}
}