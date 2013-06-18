/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.command;


import java.util.Collection;

import org.bibsonomy.scraper.Scraper;

/**
 * @author ema
 * @version $Id: ScraperInfoCommand.java,v 1.2 2010-04-09 11:46:20 nosebrain Exp $
 */
public class ScraperInfoCommand extends ResourceViewCommand {
	private Collection<Scraper> scraperList;

	/**
	 * @return the scraperList
	 */
	public Collection<Scraper> getScraperList() {
		return this.scraperList;
	}

	/**
	 * @param scraperList the scraperList to set
	 */
	public void setScraperList(final Collection<Scraper> scraperList) {
		this.scraperList = scraperList;
	}

}
