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

package org.bibsonomy.rest.strategy.posts.standard;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.rest.strategy.users.PostPostStrategy;

/**
 * strategy for creating standard posts
 * 
 * @author dzo
 * @version $Id: PostStandardPostStrategy.java,v 1.2 2010-04-06 12:04:36 nosebrain Exp $
 */
public class PostStandardPostStrategy extends PostPostStrategy {

	/**
	 * sets the context
	 * @param context
	 * @param username 
	 */
	public PostStandardPostStrategy(final Context context, final String username) {
		super(context, username);
	}
	
	@Override
	protected Post<? extends Resource> parsePost() {
		return this.getRenderer().parseStandardPost(this.doc);
	}
}
