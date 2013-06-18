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
import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.util.ResourceUtils;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.strategy.AbstractGetListStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetUserPostsStrategy.java,v 1.21 2011-06-09 12:11:22 rja Exp $
 */
public class GetUserPostsStrategy extends AbstractGetListStrategy<List<? extends Post<? extends Resource>>> {

	private final String userName;
	private final List<String> tags;
	private final String tagString;
	private final String search;
	private final Class<? extends Resource> resourceType;
	
	/**
	 * @param context
	 * @param userName
	 */
	public GetUserPostsStrategy(final Context context, final String userName) {
		super(context);
		this.userName = userName;
		this.tags = context.getTags("tags");
		this.tagString = context.getStringAttribute("tags", null);
		this.search = context.getStringAttribute("search", null);
		this.resourceType = ResourceUtils.getResource(context.getStringAttribute("resourcetype", "all"));
	}

	@Override
	protected void appendLinkPostFix(final StringBuilder sb) {
		if (this.tagString != null) {
			sb.append("&tags=").append(this.tagString);
		}
		if (this.resourceType != Resource.class) {
			sb.append("&resourcetype=").append(ResourceUtils.toString(this.resourceType).toLowerCase());
		}
	}

	@Override
	protected StringBuilder getLinkPrefix() {
		final StringBuilder sb = new StringBuilder(this.getUrlRenderer().getApiUrl() );
		sb.append( RestProperties.getInstance().getUsersUrl() ).append("/").append(this.userName).append("/").append(RestProperties.getInstance().getPostsUrl()); 
		return sb;
	}

	@Override
	protected List<? extends Post<? extends Resource>> getList() {
		return this.getLogic().getPosts(resourceType, GroupingEntity.USER, userName, this.tags, null, null, null, this.getView().getStartValue(), this.getView().getEndValue(), search);
	}

	@Override
	protected void render(Writer writer, List<? extends Post<? extends Resource>> resultList) {
		this.getRenderer().serializePosts(writer, resultList, this.getView());
	}

	@Override
	public String getContentType() {
		return "posts";
	}
}