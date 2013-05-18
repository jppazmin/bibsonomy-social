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

package org.bibsonomy.model;

import java.io.Serializable;

/**
 * A basket that holds some posts for a user.
 * 
 * TODO: implement full basket functionality
 * 
 * @author Dominik Benz
 * @version $Id: Basket.java,v 1.9 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class Basket implements Serializable {
	private static final long serialVersionUID = -3051707370037449963L;

	private int numPosts;

	/**
	 * @return numPosts
	 */
	public int getNumPosts() {
		return this.numPosts;
	}

	/**
	 * @param numPosts
	 */
	public void setNumPosts(int numPosts) {
		this.numPosts = numPosts;
	}
}