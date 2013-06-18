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

import java.util.Date;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.rest.strategy.users.PutPostStrategy;

/**
 * @author dzo
 * @version $Id: PutStandardPostStrategy.java,v 1.2 2010-04-06 12:04:36 nosebrain Exp $
 */
public class PutStandardPostStrategy extends PutPostStrategy {

	/**
	 * sets the context
	 * @param context
	 * @param username 
	 * @param resourceHash 
	 */
	public PutStandardPostStrategy(final Context context, final String username, final String resourceHash) {
		super(context, username, resourceHash);
	}
	
	@Override
	protected Post<? extends Resource> getPost() {
		final Post<? extends Resource> post = this.getRenderer().parseStandardPost(this.doc);
		/*
		 * set postingdate to current time
		 */
		post.setDate(new Date());				
		/*
		 * set the (old) intrahash of the resource as specified in the URL
		 */
		post.getResource().setIntraHash(this.resourceHash);
		return post;
	}
}
