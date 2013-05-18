/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.comparators;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bibsonomy.common.enums.SortKey;
import org.bibsonomy.common.enums.SortOrder;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.util.DateUtils;
import org.bibsonomy.util.StringUtils;

/**
 * Base class to compare posts
 * 
 * @author dbenz
 * @version $Id: PostComparator.java,v 1.5 2011-04-29 06:45:03 bibsonomy Exp $
 */
public abstract class PostComparator {

	private static final long serialVersionUID = 1L;
	protected List<SortCriterium> sortCriteria = new ArrayList<SortCriterium>();

	/** Helper structure to bind a sort key to a sort order */
	protected class SortCriterium {
		/** sort key */
		public final SortKey sortKey;
		/** sort order */
		public final SortOrder sortOrder;
		/**
		 * Constructor
		 * @param key 
		 * @param order
		 */
		public SortCriterium(final SortKey key, final SortOrder order) {
			this.sortKey = key;
			this.sortOrder = order;
		}
	}	

	/** helper exception */
	protected class SortKeyIsEqualException extends Exception {
		private static final long serialVersionUID = 1L;		
	}

	/**
	 * instantiate comparator
	 * 
	 * @param sortKeys
	 * @param sortOrders
	 */
	public PostComparator(final List<SortKey> sortKeys, final List<SortOrder> sortOrders) {
		for (int i = 0; i <= sortKeys.size() - 1; i++) {
			try {
				this.sortCriteria.add(new SortCriterium(sortKeys.get(i), sortOrders.get(i)));
			} catch (IndexOutOfBoundsException ignore) {
				// fill up with default ascending order
				this.sortCriteria.add(new SortCriterium(sortKeys.get(i), SortOrder.ASC));
			}
		}
	}
	
	/**
	 * Compare two strings following a specified order
	 * 
	 * @param s1 first string
	 * @param s2 second string
	 * @param order sort order
	 * @return an int comparison value
	 * @throws SortKeyIsEqualException 
	 */
	protected int nomalizeAndCompare(String s1, String s2, final SortOrder order) throws SortKeyIsEqualException {
		// normalization
		if (present(s1)) s1 = BibTexUtils.cleanBibTex(s1).trim();
		if (present(s2)) s2 = BibTexUtils.cleanBibTex(s2).trim();
		// comparison
		int comp = 0;
		if (SortOrder.ASC.equals(order)) {
			comp = StringUtils.secureCompareTo(s1, s2);
		} else {
			comp = StringUtils.secureCompareTo(s2, s1);
		}
		if (comp == 0) throw new SortKeyIsEqualException();
		return comp;
	}

	/**
	 * Compare two integers following a specified order
	 * 
	 * @param i1 first integer
	 * @param i2 second integer
	 * @param order sort order
	 * @return an int comparison value
	 * @throws SortKeyIsEqualException 
	 */
	protected int compare (final int i1, final int i2, final SortOrder order) throws SortKeyIsEqualException {
		int comp = 0;
		if (order.equals(SortOrder.ASC)) {
			comp = i1 - i2;
		} else {
			comp = i2 - i1;
		}
		if (comp == 0) throw new SortKeyIsEqualException();
		return comp;
	}
	
	/**
	 * Compare two doubles following a specified order
	 * 
	 * @param d1 first double
	 * @param d2 second double
	 * @param order sort order
	 * @return an int comparison value
	 * @throws SortKeyIsEqualException 
	 */
	protected int compare (final double i1, final double i2, final SortOrder order) throws SortKeyIsEqualException {
		double comp = 0;
		if (order.equals(SortOrder.ASC)) {
			comp = i1 - i2;
		} else {
			comp = i2 - i1;
		}
		if (comp == 0) throw new SortKeyIsEqualException();
		return (comp > 0 ? 1 : 0);
	}		
	
	/**
	 * Compare two dates following a specified order
	 * 
	 * @param d1 first date
	 * @param d2 second date
	 * @param order sort order
	 * @return an int comparison value
	 * @throws SortKeyIsEqualException
	 */
	protected int compare (final Date d1, final Date d2, final SortOrder order) throws SortKeyIsEqualException {
		int comp = 0;
		if (order.equals(SortOrder.ASC)) {
			comp = DateUtils.secureCompareTo(d1, d2);
		}
		else {
			comp = DateUtils.secureCompareTo(d2, d1);
		}
		if (comp == 0) throw new SortKeyIsEqualException();
		return comp;
	}	
	
}
