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

import java.io.Writer;

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.strategy.AbstractCreateStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: PostUserStrategy.java,v 1.13 2011-02-14 12:24:19 rja Exp $
 */
public class PostUserStrategy extends AbstractCreateStrategy {
	
	/**
	 * @param context
	 */
	public PostUserStrategy(final Context context) {
		super(context);
	}

	@Override
	protected String create() throws InternServerException, BadRequestOrResponseException {
		final User user = this.getRenderer().parseUser(this.doc);
		// (old comment) check this here, because its not checked in the renderer
		/*
		 * FIXME: our renderer does not render the password - thus we can't 
		 * expect one here. Furthermore, probably more checks are necessary. 
		 */
		// if (user.getPassword() == null || user.getPassword().length() == 0) throw new BadRequestOrResponseException("missing password");
		return this.getLogic().createUser(user);
	}

	@Override
	protected void render(Writer writer, String userID) {
		this.getRenderer().serializeUserId(writer, userID);		
	}
}