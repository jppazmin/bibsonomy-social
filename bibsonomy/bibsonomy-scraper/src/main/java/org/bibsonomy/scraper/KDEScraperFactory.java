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

package org.bibsonomy.scraper;

import org.bibsonomy.scraper.InformationExtraction.IEScraper;
import org.bibsonomy.scraper.generic.BibtexScraper;
import org.bibsonomy.scraper.generic.CoinsScraper;
import org.bibsonomy.scraper.generic.HighwireScraper;
import org.bibsonomy.scraper.generic.UnAPIScraper;
import org.bibsonomy.scraper.id.kde.doi.DOIScraper;
import org.bibsonomy.scraper.id.kde.isbn.ISBNScraper;
import org.bibsonomy.scraper.snippet.SnippetScraper;

/**  
 * Configures the scrapers used by BibSonomy.
 * 
 * @author rja
 *
 */
public class KDEScraperFactory {

	/**
	 * @return The scrapers produced by this factory.
	 */
	public CompositeScraper<Scraper> getScraper () {
		final CompositeScraper<Scraper> scraper = getScraperWithoutIE();

		/*
		 * If nothing works: do information extraction using MALLET.
		 */
		scraper.addScraper(new IEScraper());
		return scraper;
	}

	/**
	 *  @return All scrapers produced by this factory without the {@link IEScraper}. 
	 *  
	 */
	public CompositeScraper<Scraper> getScraperWithoutIE() {
		final CompositeScraper<Scraper> scraper = new CompositeScraper<Scraper>();

		/*
		 * first scraper: the DOIScraper, because it replaces dx.doi.org URLs 
		 * by the corresponding "real" URLs (i.e., the URLs, where the dx.doi.org
		 * URL points to using HTTP redirect)
		 */
		scraper.addScraper(new DOIScraper());
		
		scraper.addScraper(new KDEUrlCompositeScraper());
		
		// this scraper always crawls the content and thus accepts ALL URLs!
		scraper.addScraper(new UnAPIScraper());
		
		//temporary solution to avoid manifold content download 
		scraper.addScraper(new HighwireScraper());
		
		scraper.addScraper(new SnippetScraper());

		scraper.addScraper(new CoinsScraper());
		
		// TODO: ISBNScraper can be used as a snippet scraper 
		scraper.addScraper(new ISBNScraper());
		
		// TODO: Scraper for searching bibtex in HTML-Sourcecode 
		scraper.addScraper(new BibtexScraper());
		return scraper;
	}

}
