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
 * Defines sorting criteria (aka sort keys) for displayed lists.
 * 
 * The difference between this enum and the one defined in 
 *   org.bibsonomy.model.enums.Order
 * is that the latter defines sorting criteria which are applied when
 * retrieving items from the database (i.e. arguments to ORDER BY..). This
 * class here defines sorting options which are applied only for the currently 
 * displayed entries, e.g. the first 10 ones.
 * 
 * The sort order (asc / desc) is defined in org.bibsonomy.common.enums.SortOrder 
 * 
 * @author Dominik Benz
 * @see org.bibsonomy.model.enums.Order
 * @see org.bibsonomy.common.enums.SortOrder
 * @version $Id: SortKey.java,v 1.10 2011-05-16 15:00:28 dbe Exp $
 */
public enum SortKey {
	/** no re-sorting, keep order as it comes from DB */
	NONE,
	/** sort by year */
	YEAR,
	/** sort by month */
	MONTH,
	/** sort by day */
	DAY,	
	/** sort by author */
	AUTHOR,
	/** sort by editor */
	EDITOR,
	/** by entrytype */
	ENTRYTYPE,
	/** by title */
	TITLE,
	/** by booktitle */
	BOOKTITLE,
	/** by journal */
	JOURNAL,
	/** by school */
	SCHOOL,
	/** by note */
	NOTE,	
	/** by posting date*/
	DATE,
	/** by a (somehow computed) ranking */
	RANKING;
}