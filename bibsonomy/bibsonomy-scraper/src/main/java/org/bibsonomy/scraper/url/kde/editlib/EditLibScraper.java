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

package org.bibsonomy.scraper.url.kde.editlib;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * @author wbi
 * @version $Id: EditLibScraper.java,v 1.15 2011-04-29 07:24:48 bibsonomy Exp $
 */
public class EditLibScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Ed/ITLib";
	private static final String EDITLIB_HOST_NAME  = "http://www.editlib.org";
	private static final String SITE_URL = EDITLIB_HOST_NAME+"/";

	private static final String info = "This Scraper parses a publication from " + href(SITE_URL, SITE_NAME)+".";

	private static final String EDITLIB_HOST  = "editlib.org";
	private static final String EDITLIB_PATH  = "/index.cfm";
	private static final String EDITLIB_ABSTRACT_PATH = "/index.cfm?fuseaction=Reader.ViewAbstract&paper_id=";
	private static final String EDITLIB_BIBTEX_PATH = "/index.cfm?fuseaction=Reader.ChooseCitationFormat&paper_id=";
	private static final String EDITLIB_BIBTEX_DOWNLOAD_PATH = "/index.cfm/files/citation_{id}.bib?fuseaction=Reader.ExportAbstract&citationformat=BibTex&paper_id=";

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + EDITLIB_HOST), Pattern.compile(EDITLIB_PATH + ".*")));
	
	public String getInfo() {
		return info;
	}

	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		String url = sc.getUrl().toString();

		sc.setScraper(this);

		String id = null;

		if(url.startsWith(EDITLIB_HOST_NAME + EDITLIB_ABSTRACT_PATH)) {
			id = url.substring(url.indexOf(EDITLIB_ABSTRACT_PATH) + EDITLIB_ABSTRACT_PATH.length());
		}

		if(url.startsWith(EDITLIB_HOST_NAME + EDITLIB_BIBTEX_PATH)) {
			id = url.substring(url.indexOf(EDITLIB_BIBTEX_PATH) + EDITLIB_BIBTEX_PATH.length());
		}

		String bibResult = null;

		try {
			bibResult = WebUtils.getContentAsString(new URL(EDITLIB_HOST_NAME + EDITLIB_BIBTEX_DOWNLOAD_PATH.replace("{id}", id) + id));
		} catch (IOException ex) {
			throw new InternalFailureException(ex);
		}

		if(bibResult != null) {
			sc.setBibtexResult(bibResult);
			return true;
		}else
			throw new ScrapingFailureException("getting bibtex failed");

	}

	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns;
	}

	public String getSupportedSiteName() {
		// TODO Auto-generated method stub
		return SITE_NAME;
	}

	public String getSupportedSiteURL() {
		// TODO Auto-generated method stub
		return SITE_URL;
	}

}
