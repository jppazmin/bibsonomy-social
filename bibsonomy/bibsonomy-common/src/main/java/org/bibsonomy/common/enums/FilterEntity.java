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

package org.bibsonomy.common.enums;

/**
 * Defines possible filter entities
 * 
 * @author Stefan Stützer
 * @version $Id: FilterEntity.java,v 1.13 2011-05-17 09:09:29 econ11 Exp $
 */
public enum FilterEntity {

	/**
	 * Use this when you ONLY want to retrieve resources with a PDF
	 * file attached
	 */
	JUST_PDF,
	
	/**
	 * Only retrieve resources which are stored at least two times
	 */
	DUPLICATES,
	
	/**
	 * Filter to retrieve posts for spammers
	 * This can be only used by admins
	 */
	ADMIN_SPAM_POSTS,
	
	/**
	 * Use this when you ONLY want to retrieve resources which are 
	 * viewable for your groups
	 */
	MY_GROUP_POSTS,
	
	/**
	 * Some pages apply filtering, e.g., the homepage does not show posts
	 * which contain a blacklisted tag. Setting the filter to this entity
	 * should turn off such filtering. 
	 */
	UNFILTERED,
	
	/**
	 * use this if user is allowed to access documents
	 */
	POSTS_WITH_DOCUMENTS,
	
	/**
	 * use this if user isn't allowed to access documents
	 */
	JUST_POSTS,
	
	/**
	 * Return only posts which have been send to a repository (PUMA specific)
	 */
	POSTS_WITH_REPOSITORY;
	
}