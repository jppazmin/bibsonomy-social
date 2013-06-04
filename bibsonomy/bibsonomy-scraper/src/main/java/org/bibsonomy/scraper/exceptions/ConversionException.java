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

import org.bibsonomy.scraper.converter.EndnoteToBibtexConverter;


/**
 * 
 * A converter (e.g., {@link EndnoteToBibtexConverter} failed to convert.
 * 
 * 
 * @author tst
 * @version $Id: ConversionException.java,v 1.3 2011-04-29 07:24:30 bibsonomy Exp $
 */
public class ConversionException extends ScrapingException {

	private static final long serialVersionUID = -5622350446172682574L;

	/**
	 * set message
	 * @param message
	 */
	public ConversionException(String message) {
		super(message);
	}

	/**
	 * set exception
	 * @param exception
	 */
	public ConversionException(Exception exception) {
		super(exception);
	}

}
