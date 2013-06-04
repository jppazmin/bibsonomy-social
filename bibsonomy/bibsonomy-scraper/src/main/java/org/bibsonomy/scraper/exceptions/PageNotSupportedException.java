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

package org.bibsonomy.scraper.exceptions;


/**
 * Special failure case: Scraper not support the given Page
 * @author tst
 */
public class PageNotSupportedException extends ScrapingException {

	private static final long serialVersionUID = 9030796354343329688L;
	
	/**
	 * default error message
	 */
	public static final String DEFAULT_ERROR_MESSAGE = "The posted page is not supported by scraper ";

	/**
	 * set message
	 * @param message
	 */
	public PageNotSupportedException(String message){
		super(message);
	}
	
	/**
	 * set exception
	 * @param exception
	 */
	public PageNotSupportedException(Exception exception){
		super(exception);
	}
	
}
