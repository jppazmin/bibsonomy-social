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

package org.bibsonomy.scraper.url.kde.acm;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.util.WebUtils;
import org.bibsonomy.util.XmlUtils;
import org.bibsonomy.util.id.DOIUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Scrapes the ACM digital library
 * @author rja
 *
 */
public class ACMBasicScraper extends AbstractUrlScraper {

	private final Log log = LogFactory.getLog(ACMBasicScraper.class);

	private static final String SITE_NAME = "ACM Digital Library";
	private static final String SITE_URL  = "http://portal.acm.org/";
	private static final String info      = "This scraper parses a publication page from the " + href(SITE_URL, SITE_NAME);

	private static final List<Tuple<Pattern, Pattern>> patterns = Collections.singletonList(new Tuple<Pattern, Pattern>(
			Pattern.compile(".*" + "portal.acm.org"), 
			Pattern.compile("(/beta)?/citation.cfm.*")
	));

	private static final String BROKEN_END = new String("},\n}");
	private static final Pattern URL_PARAM_ID_PATTERN = Pattern.compile("id=([0-9\\.]+)");
	private static final Pattern ABSTRACT_PATTERN = Pattern.compile("<div style=\"display:inline\">(\\s*<p>\\s*)?(.+?)(\\s*<\\/p>\\s*)?<\\/div>", Pattern.MULTILINE);

	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {
		sc.setScraper(this);

		try {
			/*
			 * extract the id from the URL
			 */
			final Matcher matcher = URL_PARAM_ID_PATTERN.matcher(sc.getUrl().getQuery());
			if (matcher.find()) {
				String id = matcher.group(1);
				/*
				 * If the id contains a dot ".", we use the part after the dot.
				 * TODO: Can we do this nicer? 
				 */
				final int indexOfDot = id.indexOf(".");
				if (indexOfDot > -1) {
					id = id.substring(indexOfDot + 1);
				}
				/*
				 * Scrape entries from popup BibTeX site. BibTeX entry on these
				 * pages looks like this: <PRE id="155273">@article{155273,
				 * author = {The Author}, title = {This is the title}...}</pre>
				 */
				final StringBuffer bibtexEntries = extractBibtexEntries(SITE_URL, "exportformats.cfm?expformat=bibtex&id=" + id);

				/*
				 * download the abstract
				 * FIXME: 
				 * 
				 * IDs with a "dot" (e.g., 1082036.1082037 seem to have no abstract:
				 * 
				 * http://portal.acm.org/tab_abstract.cfm?id=1082036.1082037&usebody=tabbody
				 * 
				 * We must use only the part after the dot to get it:
				 * 
				 * http://portal.acm.org/tab_abstract.cfm?id=1082037&usebody=tabbody
				 * 
				 * This must be done!
				 * 
				 */
				final String abstrct = WebUtils.getContentAsString(SITE_URL + "/tab_abstract.cfm?usebody=tabbody&id=" + id);
				if (present(abstrct)) {
					/*
					 * extract abstract from HTML
					 */
					final Matcher matcher2 = ABSTRACT_PATTERN.matcher(abstrct);
					if (matcher2.find()) {
						/*
						 * FIXME: we don't get here for URL 
						 * 
						 * http://doi.acm.org/10.1145/1105664.1105676
						 *  
						 * Thus, the pattern fails for that abstract at
						 * 
						 * http://portal.acm.org/tab_abstract.cfm?id=1105676&usebody=tabbody
						 */
						final String extractedAbstract = matcher2.group(2);
						BibTexUtils.addFieldIfNotContained(bibtexEntries, "abstract", extractedAbstract);

					} else {
						// log if abstract is not available
						log.info("ACMBasicScraper: Abstract not available");
					}
				} else {
					// log if abstract is not available
					log.info("ACMBasicScraper: Abstract not available");
				}

				/*
				 * Some entries (e.g., http://portal.acm.org/citation.cfm?id=500737.500755) seem
				 * to have broken BibTeX entries with a "," too much at the end. We remove this
				 * here.
				 *
				 * Some entries have the following end: "},\n} \n" instead of the BROKEN_END String.
				 * So we have to adjust the starting index by the additional 2 symbols.
				 */
				final int indexOf = bibtexEntries.indexOf(BROKEN_END, bibtexEntries.length() - BROKEN_END.length() - 2);
				if (indexOf > 0) {
					bibtexEntries.replace(indexOf, bibtexEntries.length(), "}\n}");
				}

				final String result = DOIUtils.cleanDOI(bibtexEntries.toString().trim());
				//final String result = bibtexEntries.toString().trim();

				if (!"".equals(result)) {
					sc.setBibtexResult(result);
					return true;
				} else
					throw new ScrapingFailureException("getting bibtex failed");
			}
			return false;

		} catch (Exception e) {
			throw new InternalFailureException(e);
		}
	}

	/**
	 * This method walks through the dom of the given url
	 * and tries to extract the bibtex entries.
	 * 
	 * Structure is:
	 * 
	 * ...
	 * <PRE>
	 * 	Bibtex Entry
	 * </PRE>
	 * ...
	 * 
	 * 
	 * @param siteUrl
	 * @param pathsToScrape
	 * @return extracted bibtex entries
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private StringBuffer extractBibtexEntries(final String siteUrl, final String path) throws MalformedURLException, IOException{
		final StringBuffer bibtexEntries = new StringBuffer(); 

		// create a DOM with each
		final Document doc = XmlUtils.getDOM(new URL(siteUrl + path));

		// fetch the nodelist
		final NodeList pres = doc.getElementsByTagName("pre");

		// and extract the bibtex entry
		for (int i = 0; i < pres.getLength(); i++) {
			final Node currNode = pres.item(i);
			bibtexEntries.append(XmlUtils.getText(currNode));
		}

		return bibtexEntries;
	}

	public String getInfo() {
		return info;
	}

	@Override
	public List<Tuple<Pattern, Pattern>> getUrlPatterns() {
		return patterns; 
	}


	public String getSupportedSiteName() {
		return SITE_NAME;
	}


	public String getSupportedSiteURL() {
		return SITE_URL;
	}

	public static void main(String[] args) throws MalformedURLException, ScrapingException {
		final String[] urls = new String[] {
				"http://portal.acm.org/citation.cfm?id=1015428&amp;coll=Portal&amp;dl=ACM&amp;CFID=22531872&amp;CFTOKEN=18437036",
				"http://portal.acm.org/citation.cfm?id=333115.333119&amp;coll=GUIDE&amp;dl=GUIDE&amp;CFID=11052258&amp;CFTOKEN=84161555",
				"http://portal.acm.org/citation.cfm?id=1105676",
				"http://portal.acm.org/citation.cfm?id=553876",
				"http://portal.acm.org/beta/citation.cfm?id=359859",
				"http://portal.acm.org/citation.cfm?id=1082036.1082037&amp;coll=Portal&amp;dl=GUIDE&amp;CFID=88775871&amp;CFTOKEN=40392553#",
				"http://doi.acm.org/10.1145/1105664.1105676",
				"http://portal.acm.org/citation.cfm?id=500737.500755"
		};
		for (String url : urls) {
			System.out.println("trying url " + url);
			final ScrapingContext sc = new ScrapingContext(new URL(url));

			final ACMBasicScraper scraper = new ACMBasicScraper();
			scraper.scrape(sc);
			System.out.println("\n----------------------------------------\n");
			System.out.println(sc.getBibtexResult());
			System.out.println("----------------------------------------\n");
			
		}

	}

}

