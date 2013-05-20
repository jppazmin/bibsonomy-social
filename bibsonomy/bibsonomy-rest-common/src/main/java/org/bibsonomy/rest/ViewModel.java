/**
 *
 *  BibSonomy-Rest-Common - Common things for the REST-client and server.
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

package org.bibsonomy.rest;

import org.bibsonomy.model.enums.Order;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ViewModel.java,v 1.8 2011-04-29 06:53:11 bibsonomy Exp $
 */
public class ViewModel {

	/**
	 * a url to the next part of the resource list
	 */
	private String urlToNextResources;

	/**
	 * start value for the list of resources
	 */
	private int startValue;

	/**
	 * end value for the list of resources
	 */
	private int endValue;

	/**
	 * Order attribute for Tags
	 */
	private Order order;

	/**
	 * @return Returns the urlToNextResources.
	 */
	public String getUrlToNextResources() {
		return urlToNextResources;
	}

	/**
	 * @param urlToNextResources
	 *            The urlToNextResources to set.
	 */
	public void setUrlToNextResources(String urlToNextResources) {
		this.urlToNextResources = urlToNextResources;
	}

	/**
	 * @return Returns the endValue.
	 */
	public int getEndValue() {
		return endValue;
	}

	/**
	 * @param endValue
	 *            The endValue to set.
	 */
	public void setEndValue(int endValue) {
		this.endValue = endValue;
	}

	/**
	 * @return Returns the startValue.
	 */
	public int getStartValue() {
		return startValue;
	}

	/**
	 * @param startValue
	 *            The startValue to set.
	 */
	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}

	/**
	 * @param newOrderName
	 *            The Name of the new Order to set
	 */
	public void setOrder(String newOrderName) {
		if (newOrderName != null) this.order = Order.getOrderByName(newOrderName);
	}

	/**
	 * @return Returns the order
	 */
	public Order getOrder() {
		return this.order;
	}
}