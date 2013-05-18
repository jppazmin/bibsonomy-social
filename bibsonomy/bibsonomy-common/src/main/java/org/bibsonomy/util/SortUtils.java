/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.util;

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.common.enums.SortKey;
import org.bibsonomy.common.enums.SortOrder;

/**
 * Convenience methods for sorting lists
 *
 * @author Dominik Benz
 * @version $Id: SortUtils.java,v 1.7 2011-04-29 06:36:50 bibsonomy Exp $
 */
public class SortUtils {

	private static final String SORT_KEY_DELIMITER       = "|";
	private static final String SORT_ORDER_DELIMITER       = "|";
	
		
	/**
	 * parse a list of sort keys (delimited by SORT_KEY_DELIMITER)
	 * 
	 * @param sortKeys
	 * @return a list of sort keys
	 */
	public static List<SortKey> parseSortKeys(final String sortKeys) {
		final List<SortKey> parsedSortKeys = new ArrayList<SortKey>();
		if (sortKeys == null) {
			return parsedSortKeys;
		}
		for (String sortKey : sortKeys.split("\\" + SORT_KEY_DELIMITER)) {
			parsedSortKeys.add(EnumUtils.searchEnumByName(SortKey.values(), sortKey));
		}
		return parsedSortKeys;
	}
	
	/**
	 * parse a list of sort oders (delimited by SORT_ORDER_DELIMITER) 
	 * 
	 * @param sortOrders
	 * @return a list of sort orders
	 */
	public static List<SortOrder> parseSortOrders(final String sortOrders) {
		final List<SortOrder> parsedSortOrders = new ArrayList<SortOrder>();
		if (sortOrders == null) {
			return parsedSortOrders;
		}
		for (String sortOrder : sortOrders.split("\\" + SORT_ORDER_DELIMITER)) {
			parsedSortOrders.add(EnumUtils.searchEnumByName(SortOrder.values(), sortOrder));
		}
		return parsedSortOrders;
	}	
}