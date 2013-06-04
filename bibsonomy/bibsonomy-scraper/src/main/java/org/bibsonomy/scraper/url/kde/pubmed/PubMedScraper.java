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

package org.bibsonomy.scraper.url.kde.pubmed;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;

/**
 * 
 * @author Christian Kramer
 * @version $Id: PubMedScraper.java,v 1.21 2011-04-29 07:24:42 bibsonomy Exp $
 * 
 */
public class PubMedScraper extends AbstractUrlScraper {
	private static final String SITE_NAME = "PubMed";
	private static final String SITE_URL = "http://www.ncbi.nlm.nih.gov/";
	private static final String info = "This scraper parses a publication page of citations from "
			+ href(SITE_URL, SITE_NAME)+".";

	private static final String HOST = "ncbi.nlm.nih.gov";
	private static final String PUBMED_EUTIL_HOST = "eutils.ncbi.nlm.nih.gov";
	private static final String UK_PUBMED_CENTRAL_HOST = "ukpmc.ac.uk";

	private static final List<Tuple<Pattern, Pattern>> patterns = new LinkedList<Tuple<Pattern, Pattern>>();
	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + HOST), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + PUBMED_EUTIL_HOST), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + UK_PUBMED_CENTRAL_HOST), AbstractUrlScraper.EMPTY_PATTERN));
	}

	protected boolean scrapeInternal(ScrapingContext sc)
			throws ScrapingException {
		String bibtexresult = null;
		sc.setScraper(this);

		Pattern pa = Pattern.compile("meta name=\"citation_pmid\" content=\"(\\d+)\"");
		Matcher ma = pa.matcher(sc.getPageContent());
	
		// save the original URL
		String _origUrl = sc.getUrl().toString();

		try {
			if (_origUrl.matches("(?im)^.+db=PubMed.+$")) {
				// try to get the PMID out of the parameters
				pa = Pattern.compile("\\d+");
				ma = pa.matcher(sc.getUrl().getQuery());

				// if the PMID is existent then get the bibtex from hubmed
				if (ma.find()) {
					String newUrl = "http://www.hubmed.org/export/bibtex.cgi?uids="
							+ ma.group();
					bibtexresult = WebUtils.getContentAsString(new URL(newUrl));
				}

				// try to scrape with new URL-Pattern
				// avoid crashes
			} else if (sc.getPageContent().matches("(?ims)^.+PMID: (\\d*) .+$")) {
				// try to get the PMID out of the parameters
				pa = Pattern.compile("(?ms)^.+PMID: (\\d*) .+$");
				ma = pa.matcher(sc.getPageContent());

				// if the PMID is existent then get the bibtex from hubmed
				if (ma.find()) {
					String newUrl = "http://www.hubmed.org/export/bibtex.cgi?uids="
							+ ma.group(1);
					bibtexresult = WebUtils.getContentAsString(new URL(newUrl));
				}
			} else if (ma.find()) {
				String newUrl = "http://www.hubmed.org/export/bibtex.cgi?uids=" + ma.group(1);
				bibtexresult = WebUtils.getContentAsString(new URL(newUrl));
			}
			

			// replace the humbed url through the original URL
			pa = Pattern.compile("url = \".*\"");
			ma = pa.matcher(bibtexresult);

			if (ma.find()) {
				// escape dollar signs 
				bibtexresult = ma.replaceFirst("url = \"" + _origUrl.replace("$", "\\$") + "\"");
			}

			// -- bibtex string may not be empty
			if (bibtexresult != null && !"".equals(bibtexresult)) {
				sc.setBibtexResult(bibtexresult);
				return true;
			} else
				throw new ScrapingFailureException("getting bibtex failed");

		} catch (IOException e) {
			throw new InternalFailureException(e);
		}
	}

	public String getInfo() {
		return info;
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