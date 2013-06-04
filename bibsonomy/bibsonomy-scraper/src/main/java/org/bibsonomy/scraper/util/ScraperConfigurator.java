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

package org.bibsonomy.scraper.util;

import org.bibsonomy.scraper.url.kde.amazon.AmazonScraper;

/**
 * Configures (static) attributes of scrapers
 * 
 * Initially written to replace configuration of Amazon scraper to allow Spring
 * to configure the scraper instead of the scraper getting its configuration.
 * 
 *  TODO: When we use Spring to configure our scrapers this class will become
 *  obsolete. We can then just plug the scrapers together and configure them in 
 *  the deployment descriptor (using non-static attributes).
 *  
 * 
 * @author rja
 * @version $Id: ScraperConfigurator.java,v 1.4 2011-04-29 07:24:26 bibsonomy Exp $
 */
public class ScraperConfigurator {

	public String getAmazonAccessKey() {
		return AmazonScraper.getAmazonAccessKey();
	}
	public void setAmazonAccessKey(String amazonAccessKey) {
		AmazonScraper.setAmazonAccessKey(amazonAccessKey);
	}
	public String getAmazonSecretKey() {
		return AmazonScraper.getAmazonSecretKey();
	}
	public void setAmazonSecretKey(String amazonSecretKey) {
		AmazonScraper.setAmazonSecretKey(amazonSecretKey);
	}
	
}
