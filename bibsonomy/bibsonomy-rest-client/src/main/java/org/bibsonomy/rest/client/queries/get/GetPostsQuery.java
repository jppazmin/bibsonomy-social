/**
 *
 *  BibSonomy-Rest-Client - The REST-client.
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

package org.bibsonomy.rest.client.queries.get;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.util.ResourceUtils;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;


/**
 * Use this Class to receive an ordered list of all posts.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetPostsQuery.java,v 1.25 2011-06-09 12:00:42 rja Exp $
 */
public final class GetPostsQuery extends AbstractQuery<List<Post<? extends Resource>>> {

	private final int start;
	private final int end;
	private Order order;
	private String search;
	private Class<? extends Resource> resourceType;
	private List<String> tags;
	private GroupingEntity grouping = GroupingEntity.ALL;
	private String groupingValue;
	private String resourceHash;
	private static final Log log = LogFactory.getLog(GetPostsQuery.class);
	/**
	 * Gets bibsonomy's posts list.
	 */
	public GetPostsQuery() {
		this(0, 19);
	}

	/**
	 * Gets bibsonomy's posts list.
	 * 
	 * @param start
	 *            start of the list
	 * @param end
	 *            end of the list
	 */
	public GetPostsQuery(int start, int end) {
		if (start < 0) start = 0;
		if (end < start) end = start;

		this.start = start;
		this.end = end;
	}

	/**
	 * Set the grouping used for this query. If {@link GroupingEntity#ALL} is
	 * chosen, the groupingValue isn't evaluated (-> it can be null or empty).
	 * 
	 * @param grouping
	 *            the grouping to use
	 * @param groupingValue
	 *            the value for the chosen grouping; for example the username if
	 *            grouping is {@link GroupingEntity#USER}
	 * @throws IllegalArgumentException
	 *             if grouping is != {@link GroupingEntity#ALL} and
	 *             groupingValue is null or empty
	 */
	public void setGrouping(final GroupingEntity grouping, final String groupingValue) throws IllegalArgumentException {
		if (grouping == GroupingEntity.ALL) {
			this.grouping = grouping;
			return;
		}
		if (groupingValue == null || groupingValue.length() == 0) throw new IllegalArgumentException("no grouping value given");

		this.grouping = grouping;
		this.groupingValue = groupingValue;
	}

	/**
	 * set the resource type of the resources of the posts.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setResourceType(final Class<? extends Resource> type) {
		this.resourceType = type;
	}

	/**
	 * @param resourceHash
	 *            The resourceHash to set.
	 */
	public void setResourceHash(final String resourceHash) {
		this.resourceHash = resourceHash;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @param order
	 * 				the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @param search
	 * 				the search string to set
	 */
	public void setSearch(String search) {
		this.search = search.replace(" ", "+");
	}


	@Override
	public List<Post<? extends Resource>> getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.downloadedDocument == null) throw new IllegalStateException("Execute the query first.");
		try {
			return getRendererFactory().getRenderer(getRenderingFormat()).parsePostList(this.downloadedDocument);
		} catch (final InternServerException ex) {
			throw new BadRequestOrResponseException(ex);
		}
	}

	@Override
	protected List<Post<? extends Resource>> doExecute() throws ErrorPerformingRequestException {
		String url = URL_POSTS + "?start=" + this.start + "&end=" + this.end;

		if (this.resourceType != Resource.class) {
			url += "&resourcetype=" + ResourceUtils.toString(this.resourceType).toLowerCase();
		}

		switch (this.grouping) {
		case USER:
			url += "&user=" + this.groupingValue;
			break;
		case GROUP:
			url += "&group=" + this.groupingValue;
			break;
		case VIEWABLE:
			url += "&viewable=" + this.groupingValue;
			break;
		case ALL:
			break;
		default:
			throw new UnsupportedOperationException("The grouping " + this.grouping + " is currently not supported by this query.");
		}

		if (this.tags != null && this.tags.size() > 0) {
			boolean first = true;
			for (final String tag : tags) {
				if (first) {
					url += "&tags=" + tag;
					first = false;
				} else {
					url += "+" + tag;
				}
			}
		}

		if (this.resourceHash != null && this.resourceHash.length() > 0) {
			url += "&resource=" + this.resourceHash;
		}

		if(this.order != null){
			url += "&order=" +this.order;
		}

		if(this.search != null && this.search.length() > 0){
			url += "&search=" +this.search;
		}
		log.debug("GetPostsQuery doExecute() called - URL: "+url);
		this.downloadedDocument = performGetRequest(url);
		return null;
	}
}