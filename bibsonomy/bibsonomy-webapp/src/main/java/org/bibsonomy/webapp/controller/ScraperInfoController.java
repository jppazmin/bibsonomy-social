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

package org.bibsonomy.webapp.controller;

import java.util.Collection;

import org.bibsonomy.scraper.KDEScraperFactory;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.webapp.command.ScraperInfoCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * controller for the scraper info page
 * - /scraperinfo
 * 
 * @author ema
 * @version $Id: ScraperInfoController.java,v 1.5 2010-09-08 11:39:35 nosebrain Exp $
 */
public class ScraperInfoController implements MinimalisticController<ScraperInfoCommand>{

	/*
	 * TODO: inject the scraper list using Spring
	 */
	private static final Collection<Scraper> scraperList = new KDEScraperFactory().getScraper().getScraper();
	
	@Override
	public View workOn(final ScraperInfoCommand command) {
		command.setScraperList(scraperList);
		return Views.SCRAPER_INFO;			
	}
	
	@Override
	public ScraperInfoCommand instantiateCommand() {
		return new ScraperInfoCommand();
	}

}
