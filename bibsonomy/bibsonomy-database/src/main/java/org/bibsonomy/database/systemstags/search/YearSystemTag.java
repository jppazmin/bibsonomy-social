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

package org.bibsonomy.database.systemstags.search;

import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.database.params.GenericParam;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;

/**
 * @author dzo
 * @version $Id: YearSystemTag.java,v 1.5 2011-06-16 14:29:58 doerfel Exp $
 */
public class YearSystemTag extends AbstractSearchSystemTagImpl implements SearchSystemTag{

	public static final String NAME = "year";

	/**
	 * It is necessary to distinguish between the 4 types of legal arguments for the YearSystemTag.
	 * Some examples illustrate those types
	 * 1. 2007 => all publications of 2007
	 * 2. 2005-2007 => all publications of 2005, 2006, and 2007
	 * 3. -2007 => all publications before and in 2007
	 * 4. 2004- => all publication in and after 2004
	 */
	private String year; 		// for cases 1, 3, 4
	private String firstYear;	// for case 2
	private String lastYear;	// for case 2

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * @return the firstYear
	 */
	public String getFirstYear() {
		return this.firstYear;
	}

	/**
	 * @param firstYear the firstYear to set
	 */
	public void setFirstYear(String firstYear) {
		this.firstYear = firstYear;
	}

	/**
	 * @return the lastYear
	 */
	public String getLastYear() {
		return this.lastYear;
	}

	/**
	 * @param lastYear the lastYear to set
	 */
	public void setLastYear(String lastYear) {
		this.lastYear = lastYear;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return this.year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	public YearSystemTag newInstance() {
		return new YearSystemTag();
	}

	@Override
	public void handleParam(final GenericParam param) {
		if (param instanceof BibTexParam ) {
			param.addToSystemTags(this);

			/*
			 * extract first-, last- and year from the argument
			 */
			// 1st case: year explicitly given (eg. 2006)
			if (this.getArgument().matches("[12]{1}[0-9]{3}")) {
				this.year = this.getArgument();
				log.debug("Set year to " + this.getArgument() + " after matching year system tag");
			} 
			// 2nd case: range (e.g. 2001-2006)
			else if (this.getArgument().matches("[12]{1}[0-9]{3}-[12]{1}[0-9]{3}")) {
				String[] years = this.getArgument().split("-");
				this.firstYear = years[0];
				this.lastYear = years[1];
				log.debug("Set firstyear/lastyear to " + years[0] + "/" + years[1] + "after matching year system tag");
			}
			// 3rd case: upper bound (e.g -2005) means all years before 2005 
			else if(this.getArgument().matches("-[12]{1}[0-9]{3}")) {
				// cut off the "-" at the beginning
				this.lastYear = this.getArgument().substring(1);
				log.debug("Set lastyear to " + this.getArgument() + "after matching year system tag");
			}
			// 4th case: lower bound (e.g 1998-) means all years since 1998 
			else if(this.getArgument().matches("[12]{1}[0-9]{3}-")) {
				// cut off the "-" at the end
				this.firstYear = this.getArgument().substring(0, this.getArgument().length()-1);
				log.debug("Set firstyear to " + this.getArgument() + "after matching year system tag");
			}
		} // for Bookmarks do nothing
	}

	@Override
	public <T extends Resource> boolean allowsResource(Class<T> resourceType) {
		if (resourceType == Bookmark.class) {
			return false;
		}
		return true;
	}

}
