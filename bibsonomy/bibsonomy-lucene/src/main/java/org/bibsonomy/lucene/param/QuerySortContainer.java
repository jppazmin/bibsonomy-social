/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.param;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.bibsonomy.lucene.search.collector.TagCountCollector;
import org.bibsonomy.model.enums.Order;

/**
 * Container for a query string and a filter string for lucene 
 * 
 * if we can use more than one filter, we should use an arrayList or something
 * like this for filter. with methods addFilter, setFilter, getFilter
 * 
 * @version $Id: QuerySortContainer.java,v 1.6 2010-05-28 10:22:50 nosebrain Exp $
 *
 */
public class QuerySortContainer {
	private static final long serialVersionUID = -5889003240930421319L;
	
	
	/**
	 * query
	 */
	private Query query;

	/**
	 * filter
	 */
	private Sort sort;
	
	/**
	 * ordering
	 */
	private Order order;
	
	/**
	 * determine, how to cut off tag cloud
	 */
	private Order limitType;
	
	/**
	 * limit tag cloud 
	 */
	private int limit;
	
	/**
	 * tagcount collector
	 */
	private TagCountCollector tagCountCollector;

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return this.query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(Query query) {
		this.query = query;
		
	}
	
	/**
	 * @return the sort
	 */
	public Sort getSort() {
		return this.sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
		
	}	

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the limitType
	 */
	public Order getLimitType() {
		return limitType;
	}

	/**
	 * @param limitType the limitType to set
	 */
	public void setLimitType(Order limitType) {
		this.limitType = limitType;
	}

	/**
	 * @param tagCountCollector the tagCountCollector to set
	 */
	public void setTagCountCollector(TagCountCollector tagCountCollector) {
		this.tagCountCollector = tagCountCollector;
	}

	/**
	 * @return the tagCountCollector
	 */
	public TagCountCollector getTagCountCollector() {
		return tagCountCollector;
	}	
}