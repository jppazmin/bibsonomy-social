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
 * Bibtex is not generated as expected (output is  something like "" or null).
 * @author tst
 */
public class ScrapingFailureException extends ScrapingException {

	private static final long serialVersionUID = -5622350446172682574L;

	/**
	 * set message
	 * @param message
	 */
	public ScrapingFailureException(String message) {
		super(message);
	}

	/**
	 * set exception
	 * @param exception
	 */
	public ScrapingFailureException(Exception exception) {
		super(exception);
	}

}
