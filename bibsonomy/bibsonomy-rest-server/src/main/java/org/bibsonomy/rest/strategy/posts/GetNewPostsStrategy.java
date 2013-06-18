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

package org.bibsonomy.rest.strategy.posts;

import java.util.List;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetNewPostsStrategy.java,v 1.21 2011-06-09 12:11:22 rja Exp $
 */
public class GetNewPostsStrategy extends AbstractListOfPostsStrategy {
	private final String nextLinkPrefix;
	
	/**
	 * @param context
	 */
	public GetNewPostsStrategy(final Context context) {
		super(context);
		this.nextLinkPrefix = this.getUrlRenderer().getApiUrl() + RestProperties.getInstance().getPostsUrl() + "/" + RestProperties.getInstance().getAddedPostsUrl();
	}

	@Override
	protected StringBuilder getLinkPrefix() {
		return new StringBuilder(this.nextLinkPrefix);
	}

	@Override
	protected List<? extends Post<? extends Resource>> getList() {
		return this.getLogic().getPosts(resourceType, grouping, groupingValue, this.tags, null, Order.ADDED, null, this.getView().getStartValue(), this.getView().getEndValue(), search);
	}
}