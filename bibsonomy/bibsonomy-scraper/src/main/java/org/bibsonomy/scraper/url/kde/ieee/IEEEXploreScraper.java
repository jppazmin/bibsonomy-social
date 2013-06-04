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

package org.bibsonomy.scraper.url.kde.ieee;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.UrlCompositeScraper;


/** General scraper for IEEE Explore
 * @author rja
 *
 */
public class IEEEXploreScraper extends UrlCompositeScraper {
	private static final String SITE_URL = "http://ieeexplore.ieee.org/";
	private static final String SITE_NAME = "IEEEXplore";
	private static final String info = "This scraper creates a BibTeX entry for the media at " + AbstractUrlScraper.href(SITE_URL, SITE_NAME) + ".";

	/**
	 * 
	 */
	public IEEEXploreScraper() {
		addScraper(new IEEEXploreJournalProceedingsScraper());
		addScraper(new IEEEXploreBookScraper());
		addScraper(new IEEEXploreStandardsScraper());
	}

	public String getInfo() {
		return info;
	}

	@Override
	public String getSupportedSiteName() {
		return SITE_NAME;
	}
	
	@Override
	public String getSupportedSiteURL() {
		return SITE_URL;
	}

}