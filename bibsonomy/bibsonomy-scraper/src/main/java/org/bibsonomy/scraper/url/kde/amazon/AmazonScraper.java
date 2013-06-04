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

package org.bibsonomy.scraper.url.kde.amazon;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.AbstractUrlScraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.Tuple;
import org.bibsonomy.scraper.exceptions.InternalFailureException;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.scraper.exceptions.ScrapingFailureException;
import org.bibsonomy.scraper.util.ConfigUtil;
import org.bibsonomy.scraper.util.ScraperConfigurator;
import org.bibsonomy.scraper.util.SignedRequestsHelper;
import org.bibsonomy.util.id.ISBNUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Scraper for the amazon onlineshop
 * @author tst
 */
public class AmazonScraper extends AbstractUrlScraper {

	private static final String SITE_NAME = "Amazon";
	private static final String SITE_URL  = "http://www.amazon.com/";
	private static final String INFO			 = "Extracts publications from the " 
		+ href(SITE_URL, SITE_NAME) + " onlineshop.";
	private static final String ASIN_URL		 = "ASIN/";
	private static final String PRODUCT_URL		 = "product/";
	private static final String DOUBLE_SLASH_URL = "//";
	private static final String DP_URL			 = "dp/";
	private static final String SLASH			 = "/";
	private static final String QUESTION_MARK	 = "?"; 
	
	/** 
	 * access key for the amazon web service (aws)
	 */
	private static String AMAZON_ACCESS_KEY = null;
	/** 
	 * access key for the amazon web service (aws)
	 */
	private static String AMAZON_SECRET_KEY = null;
	
	private static final String DATE_PATTERN = ".*([0-9]{4}).*";
	
	/**
	 * get access key from environment
	 */
	static {
		AMAZON_ACCESS_KEY = ConfigUtil.getEnvironmentVariable("AmazonAccessKey");
		AMAZON_SECRET_KEY = ConfigUtil.getEnvironmentVariable("AmazonSecretKey");
	}

	/**
	 * Supported AMAZON Hosts
	 */
	private static final String AMAZON_HOST_COM		= "amazon.com";
	private static final String AMAZON_HOST_DE		= "amazon.de";
	private static final String AMAZON_HOST_CA		= "amazon.ca";
	private static final String AMAZON_HOST_FR		= "amazon.fr";
	private static final String AMAZON_HOST_JP		= "amazon.jp";
	private static final String AMAZON_HOST_CO_JP	= "amazon.co.jp";
	private static final String AMAZON_HOST_CO_UK	= "amazon.co.uk";
	private static final String AMAZON_HOST_PREFIX	= "http://www.";

	private static final List<Tuple<Pattern,Pattern>> patterns = new LinkedList<Tuple<Pattern,Pattern>>();

	static {
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_CA), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_JP), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_CO_JP), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_CO_UK), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_COM), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_DE), AbstractUrlScraper.EMPTY_PATTERN));
		patterns.add(new Tuple<Pattern, Pattern>(Pattern.compile(".*" + AMAZON_HOST_FR), AbstractUrlScraper.EMPTY_PATTERN));
	}
	
	private static final List<String> hosts = new ArrayList<String>();
	
	static {
		hosts.add(AMAZON_HOST_COM);
		hosts.add(AMAZON_HOST_DE);
		hosts.add(AMAZON_HOST_FR);
		hosts.add(AMAZON_HOST_CO_UK);
		hosts.add(AMAZON_HOST_CA); 
		hosts.add(AMAZON_HOST_CO_JP);
		hosts.add(AMAZON_HOST_JP);
	}
	
	/**
	 * Until we use Spring for configuration, we statically configure this class
	 * using the {@link ScraperConfigurator}.
	 * @param amazonAccessKey
	 */
	public static void setAmazonAccessKey(final String amazonAccessKey) {
		AMAZON_ACCESS_KEY = amazonAccessKey;
	}
	/**
	 * Until we use Spring for configuration, we statically configure this class
	 * using the {@link ScraperConfigurator}.
	 * @param amazonSecretKey
	 */
	public static void setAmazonSecretKey(final String amazonSecretKey) {
		AMAZON_SECRET_KEY = amazonSecretKey;
	}	

	public static String getAmazonSecretKey() {
		return AMAZON_SECRET_KEY;
	}
	public static String getAmazonAccessKey() {
		return AMAZON_SECRET_KEY;
	}
	
	/**
	 * INFO field of this scraper
	 */
	public String getInfo() {
		return INFO;
	}

	/**
	 * Scrapes a product from amazon
	 */
	@Override
	protected boolean scrapeInternal(ScrapingContext sc) throws ScrapingException {

		sc.setScraper(this);
		String url = sc.getUrl().toString();
		URL scrapingUrl = null;
		
		try {
			url = URLDecoder.decode(sc.getUrl().toString(), "UTF-8");
			scrapingUrl = new URL(url);
		} catch (Exception e) {
			throw new InternalFailureException(e);
		} 
		
		// extract product key from url
		String amazonProduktKey = null;

		if(url.contains(DP_URL)){
			amazonProduktKey = url.substring(url.indexOf(DP_URL) + 3);
			if (amazonProduktKey.indexOf(SLASH) > -1) {
				amazonProduktKey = amazonProduktKey.substring(0, amazonProduktKey.indexOf(SLASH));
			} else if (amazonProduktKey.indexOf(QUESTION_MARK) > -1) {
				amazonProduktKey = amazonProduktKey.substring(0, amazonProduktKey.indexOf(QUESTION_MARK));
			}

		// substring(7) beacuse of the http:// at beginning, that should be ignored
		} else if (url.substring(7).contains(DOUBLE_SLASH_URL)){
			amazonProduktKey = url.substring(url.lastIndexOf(DOUBLE_SLASH_URL) + 2);
			if(amazonProduktKey.indexOf(SLASH) > -1)
				amazonProduktKey = amazonProduktKey.substring(0, amazonProduktKey.indexOf(SLASH));

		} else if (url.contains(ASIN_URL)){
			amazonProduktKey = url.substring(url.indexOf(ASIN_URL) + 5);
			if (amazonProduktKey.indexOf(SLASH) > -1)
				amazonProduktKey = amazonProduktKey.substring(0, amazonProduktKey.indexOf(SLASH));
			
		} else if (url.contains(PRODUCT_URL)) {
			amazonProduktKey = url.substring(url.indexOf(PRODUCT_URL) + 8);
			if (amazonProduktKey.indexOf(SLASH) > -1)
				amazonProduktKey = amazonProduktKey.substring(0, amazonProduktKey.indexOf(SLASH));
		}
		
		if(amazonProduktKey != null){

			/*
			 * init aws client with same locale as the shop from url
			 */
			try {
				final String requestUrl = getRequestUrl(scrapingUrl, amazonProduktKey);
		        
		        /*
				 * get BibTeX
				 */
		        final String bibtex = fetchBibTeX(requestUrl, url);
				
				/*
				 * return result
				 */
		        if (present(bibtex)) {
		        	sc.setBibtexResult(bibtex);
					return true;
		        } else {
		        	throw new ScrapingFailureException("no aws item available");
		        }
		        
			} catch (final Exception e) {
				throw new InternalFailureException(e);
			}
			
		}
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param scrapingUrl
	 * @param amazonProduktKey
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws ScrapingException
	 */
	private static String getRequestUrl(URL scrapingUrl, String amazonProduktKey) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException, ScrapingException {
		
		String requestUrl;
		final SignedRequestsHelper helper = SignedRequestsHelper.getInstance(scrapingUrl, AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2009-03-31");
		params.put("Operation", "ItemLookup");
		params.put("ItemId", amazonProduktKey);
		params.put("ResponseGroup", "ItemAttributes");
		
		/*
		 * isbn13 numbers don't work on co.jp sites
		 */
		if (!scrapingUrl.toString().contains(AMAZON_HOST_CO_JP)) {
			params.put("SearchIndex", "Books");
			params.put("IdType", "ISBN");
		}
		
		requestUrl = helper.sign(params);
		
		return requestUrl;
	}
	
	 /**
     * Utility function to fetch the response from the service and extract the
     * BibTeX from the XML.
     * 
     * @param requestUrl
	 * @param sc 
     * @return
	 * @throws ScrapingException 
     */
    private static String fetchBibTeX(final String requestUrl, final String url) throws ScrapingException {
		final BibTex bibtex = new BibTex();
		
        String authorEntry	= "";
        String editorEntry	= "";
        String dateEntry	= "";
        
        Document doc		= null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        
        NodeList _nl = null;
        Node _n = null;
		
        try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(requestUrl);
		} catch (Exception e) {
			throw new ScrapingException("can't parse DOM");
		}
        
        // build asin
        _n = doc.getElementsByTagName("ASIN").item(0);
        if (_n != null) {
    		bibtex.setEntrytype("book");
    		bibtex.addMiscField("asin", _n.getTextContent());
        } else {
        	return null;
        }
        
        // build authors
        _nl = doc.getElementsByTagName("Author");
        for (int i = 0; i < _nl.getLength(); i++) {
        	if (authorEntry.equals("")) {
        		authorEntry = _nl.item(i).getTextContent();
        	} else {
        		authorEntry = authorEntry + " and " + _nl.item(i).getTextContent();
        	}
        }
        if (!authorEntry.equals("")) {
        	bibtex.setAuthor(authorEntry);
        }
        
        // build editors
        List<String> editors = new LinkedList<String>();
        _nl = doc.getElementsByTagName("Creator");
        for (int i = 0; i < _nl.getLength(); i++) {
        	String role = _nl.item(i).getAttributes().getNamedItem("Role").getTextContent();
			if(role.equals("Herausgeber"))
				editors.add(_nl.item(i).getTextContent());
		}
        if (editors.size() > 0) {
			editorEntry = "";
			for (String editor: editors) {
				if (editorEntry.equals("")) {
					editorEntry = editor;
				}
				else {
					editorEntry = editorEntry + " and " + editor;
				}
			}
			if (!editorEntry.equals(""))
				bibtex.setEditor(editorEntry);
		}
        
        // build title
        _n = doc.getElementsByTagName("Title").item(0);
        if (_n != null) {
        	bibtex.setTitle(_n.getTextContent());
        }

		// build publisher
		_n = doc.getElementsByTagName("Publisher").item(0);
		if (_n != null) {
			bibtex.setPublisher(_n.getTextContent());
		} else {
			_n = doc.getElementsByTagName("Department").item(0);
			if (_n != null) {
				bibtex.setPublisher(_n.getTextContent());
			}
		}
		
		// build edition
		_n = doc.getElementsByTagName("Edition").item(0);
		if (_n != null) {
			bibtex.setEdition(_n.getTextContent());
		}
		
		// build publication date
		_n = doc.getElementsByTagName("PublicationDate").item(0);
		if(_n == null) {
			_n = doc.getElementsByTagName("ReleaseDate").item(0);
		}
		if (_n != null) {
			dateEntry = _n.getTextContent();
			
			// try to find the year only if not just copy all info.
			Pattern datePattern = Pattern.compile(DATE_PATTERN);
			Matcher dateMatcher = datePattern.matcher(dateEntry);
			if (dateMatcher.find()) {
				bibtex.setYear(dateMatcher.group(1));
			} else {
				bibtex.setYear(dateEntry);
			}
		}
		
		// add URL to product page
		_n = doc.getElementsByTagName("DetailPageURL").item(0);
		if (_n != null) {
			bibtex.setUrl(url);
		}

		// build address
		_n = doc.getElementsByTagName("Country").item(0);
		if (_n != null) {
			bibtex.setAddress(_n.getTextContent());
		}

		// build dewey
		_n = doc.getElementsByTagName("DeweyDecimalNumber").item(0);
		if (_n != null) {
			bibtex.addMiscField("dewey", _n.getTextContent());
		}

		// build ean
		_n = doc.getElementsByTagName("EAN").item(0);
		if (_n != null) {
			bibtex.addMiscField("ean", _n.getTextContent());
		}

		// build isbn
		_n = doc.getElementsByTagName("ISBN").item(0);
		if (_n != null && ISBNUtils.extractISBN(_n.getTextContent()) != null) {
			bibtex.addMiscField("isbn", _n.getTextContent());
		}
		
		// build issn
		_n = doc.getElementsByTagName("ISSN").item(0);
		if (_n != null && ISBNUtils.extractISBN(_n.getTextContent()) != null) {
			bibtex.addMiscField("isbn", _n.getTextContent());
		}
		
		bibtex.setBibtexKey(BibTexUtils.generateBibtexKey(bibtex));

		return BibTexUtils.toBibtexString(bibtex);
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
	
	
	/**
	 * Required for ISBN Scraper
	 * 
	 * @param isbn
	 * @return bibtex String
	 * @throws ScrapingException 
	 */
	public static String getBibtexByISBN(final String isbn) throws ScrapingException {
		try {
			for (String s : hosts) {
				String requestUrl = getRequestUrl(new URL(AMAZON_HOST_PREFIX + s), isbn);
		        
		        final String bibtex = fetchBibTeX(requestUrl, null);
				
		        if (bibtex != null && !bibtex.equals("")) {
		        	return bibtex;
		        } 
			}
			
		} catch (Exception e) {
			throw new ScrapingException(e);
		}
		
		return null;
	}
}
