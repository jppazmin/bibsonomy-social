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

package org.bibsonomy.model.enums;

/**
 * Defines some ordering criteria for lists of entities
 * 
 * FIXME: the orderings "FREQUENCY" AND "POPULAR" probably mean the
 * same. Check their usage and if possible, remove one of them.
 * 
 * @author Jens Illig
 * @version $Id: Order.java,v 1.11 2011-04-29 06:45:07 bibsonomy Exp $
 */
public enum Order {
	/** for ordering by adding time (desc) */
	ADDED,
	/** for ordering by popularity (desc) */
	POPULAR,
	/** for ordering by folkrank (desc) */
	FOLKRANK,	
	/** for ordering tags by frequency (desc) */
	FREQUENCY,	
	/**
	 * Some items can be ordered alphabetically ...
	 * (in particular groups)
	 */
	ALPH;

	/**
	 * Retrieve Order by name
	 * 
	 * @param name
	 *            the requested order (e.g. "folkrank")
	 * @return the corresponding Order enum
	 */
	public static Order getOrderByName(String name) {
		try {
			return Order.valueOf(name.toUpperCase());
		} catch (NullPointerException np) {
			throw new IllegalArgumentException("No order specified!");
		} catch (IllegalArgumentException ia) {
			throw new IllegalArgumentException("Requested order not supported. Possible values are 'added', 'popular', 'alph', 'frequency' or 'folkrank'");
		}
	}
}