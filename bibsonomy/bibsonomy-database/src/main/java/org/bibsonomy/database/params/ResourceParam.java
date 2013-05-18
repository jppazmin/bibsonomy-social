/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.params;

import org.bibsonomy.common.enums.RatingAverage;
import org.bibsonomy.model.Resource;

/** 
 * Super class for parameter objects that are about resources.
 * 
 * @param <T> resource (e.g. Bookmark, Publication, etc.)
 * 
 * @author Jens Illig
 * @version $Id: ResourceParam.java,v 1.4 2011-05-11 13:18:12 nosebrain Exp $
 */
public class ResourceParam<T extends Resource> extends GenericParam {
	
	private RatingAverage ratingAverage = RatingAverage.ARITHMETIC_MEAN;

	protected T resource;

	/**
	 * @param resource the resource to set
	 */
	public void setResource(final T resource) {
		this.resource = resource;
	}

	/**
	 * @return the resource
	 */
	public T getResource() {
		return resource;
	}
	
	/**
	 * @return the ratingAverage
	 */
	public RatingAverage getRatingAverage() {
		return this.ratingAverage;
	}

	/**
	 * @param ratingAverage the ratingAverage to set
	 */
	public void setRatingAverage(RatingAverage ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

}