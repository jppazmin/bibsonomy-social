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
 * Failure because of unsupported actions from user. This failure can be solved 
 * with a little trick by the user (trick is explained in the exception message).
 * @author tst
 */
public class UsageFailureException extends ScrapingException {

	private static final long serialVersionUID = -4269129145897321143L;

	/**
	 * set message
	 * @param message
	 */
	public UsageFailureException(String message) {
		super(message);
	}

	/**
	 * set exception
	 * @param exception
	 */
	public UsageFailureException(Exception exception) {
		super(exception);
	}

}
