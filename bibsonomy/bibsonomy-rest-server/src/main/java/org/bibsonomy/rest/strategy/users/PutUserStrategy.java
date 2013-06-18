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

import java.io.Reader;
import java.io.Writer;

import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.strategy.AbstractUpdateStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * strategy for updating an user
 * 		- users/USERNAME (HTTP-Method: PUT)
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: PutUserStrategy.java,v 1.18 2010-11-10 11:59:49 nosebrain Exp $
 */
public class PutUserStrategy extends AbstractUpdateStrategy {
	private final Reader doc;
	private final String userName;

	/**
	 * @param context
	 * @param userName
	 */
	public PutUserStrategy(final Context context, final String userName) {
		super(context);
		this.userName = userName;
		this.doc = context.getDocument();
	}

	@Override
	protected void render(final Writer writer, final String userID) {
		this.getRenderer().serializeUserId(writer, userID);
	}

	@Override
	protected String update() throws InternServerException {
		final User user = this.getRenderer().parseUser(this.doc);
		// ensure to use the right user name
		user.setName(this.userName);
		return this.getLogic().updateUser(user, UserUpdateOperation.UPDATE_ALL);
	}
}