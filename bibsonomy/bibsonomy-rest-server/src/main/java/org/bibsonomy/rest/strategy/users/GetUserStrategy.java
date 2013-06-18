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


import java.io.ByteArrayOutputStream;

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.rest.strategy.Strategy;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetUserStrategy.java,v 1.14 2011-04-06 12:10:04 nosebrain Exp $
 */
public class GetUserStrategy extends Strategy {

	private final String userName;

	/**
	 * @param context
	 * @param userName
	 */
	public GetUserStrategy(final Context context, final String userName) {
		super(context);
		this.userName = userName;
	}

	@Override
	public void perform(final ByteArrayOutputStream outStream) throws InternServerException, NoSuchResourceException {
		final User user = this.getLogic().getUserDetails(userName);
		// user cannot be null - if user is not found, an empty user object is given back by getUserDetails.
		// -> check for user name being null
		if (user.getName() == null) {
			throw new NoSuchResourceException("The requested user '" + userName + "' does not exist.");
		}
		//
		// delegate to the renderer
		this.getRenderer().serializeUser(writer, user, new ViewModel());
	}

	@Override
	public String getContentType() {
		return "user";
	}
}