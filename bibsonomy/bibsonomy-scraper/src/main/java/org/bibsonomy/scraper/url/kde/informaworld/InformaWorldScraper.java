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

package org.bibsonomy.scraper.url.kde.informaworld;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.converter.EndnoteToBibtexConverter;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * @author wbi
 * @version $Id: InformaWorldScraper.java,v 1.17 2011-04-29 07:24:47 bibsonomy Exp $
 */
public class InformaWorldScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Informaworld";

	private static final String SITE_URL = "http://www.informaworld.com/";

	private static final String info = "This scraper parses a publication from " + href(SITE_URL, SITE_NAME)+".";

	private static final String INFORMAWORLD_HOST_NAME  = "informaworld.com";
	private static final String INFORMAWORLD_BIBTEX_DOWNLOAD_PATH = "/smpp/content?file.txt&tab=citation&popup=&group=&expanded=&mode=&maction=&backurl=&citstyle=endnote&showabs=false&format=file&toemail=&subject=&fromname=&fromemail=&content={id}&selecteditems={sid}";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + INFORMAWORLD_HOST_NAME), AbstractUrlScraper.EMPTY_PATTERN));

	private static final Pattern pattern = Pattern.compile("content=([^~]*)");

	private static final EndnoteToBibtexConverter converter = new EndnoteToBibtexConverter();


	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		try {
			final String cookie = WebUtils.getCookies(sc.getUrl());

			if (cookie != null) {
				String id = null;

				final Matcher matcher = pattern.matcher(sc.getUrl().getPath());
				if(matcher.find())
					id = matcher.group(1);

				sc.setUrl(new URL(("http://www." + INFORMAWORLD_HOST_NAME + (INFORMAWORLD_BIBTEX_DOWNLOAD_PATH.replace("{id}", id)).replace("{sid}", id.substring(1)))));

				final String bibResult = converter.processEntry(WebUtils.getContentAsString(sc.getUrl(), cookie));

				if (bibResult != null) {
					sc.setBibtexResult(bibResult);
					return true;
				} else
					throw new ScrapingFailureException("getting BibTeX failed");
			}else
				throw new ScrapingFailureException("cookie is missing");
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}

	}


	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		return SITE_URL;
	}

}
