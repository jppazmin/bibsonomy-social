/**
 *
 *  BibSonomy-Scraper - Web page scrapers returning BibTeX for BibSonomy.
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

package org.bibsonomy.scraper;

/**
 * A Tuple stores two objects of type P and Q. 
 * 
 * @author rja
 * @version $Id: Tuple.java,v 1.5 2011-04-29 07:24:31 bibsonomy Exp $
 * @param <P> type of first object.
 * @param <Q> type of second object.
 */
public class Tuple<P,Q> {
	
	private P first;
	private Q second;
	
	/**
	 * @param first
	 * @param second
	 */
	public Tuple(P first, Q second) {
		super();
		this.first = first;
		this.second = second;
	}
	/**
	 * @return The first object.
	 */
	public P getFirst() {
		return this.first;
	}
	/**
	 * @param first
	 */
	public void setFirst(P first) {
		this.first = first;
	}
	/**
	 * @return The second object.
	 */
	public Q getSecond() {
		return this.second;
	}
	/**
	 * @param second
	 */
	public void setSecond(Q second) {
		this.second = second;
	}
	
	
	

}
