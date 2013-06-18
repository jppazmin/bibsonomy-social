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

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.rest.strategy.AbstractDeleteStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: DeleteUserStrategy.java,v 1.9 2010-11-10 11:59:49 nosebrain Exp $
 */
public class DeleteUserStrategy extends AbstractDeleteStrategy {

	private final String userName;
	
	/**
	 * @param context
	 * @param userName
	 */
	public DeleteUserStrategy(final Context context, final String userName) {
		super(context);
		this.userName = userName;
	}

	@Override
	protected boolean delete() throws InternServerException {
		this.getLogic().deleteUser(this.userName);
		// no exception -> assume success
		return true;
	}
}