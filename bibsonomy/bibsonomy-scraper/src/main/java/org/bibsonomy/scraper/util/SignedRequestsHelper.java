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

import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bibsonomy.scraper.exceptions.ScrapingException;


/**
 * This class contains all the logic for signing requests
 * to the Amazon Product Advertising API.
 */
public class SignedRequestsHelper {
    /**
     * All strings are handled as UTF-8
     */
    private static final String UTF8_CHARSET = "UTF-8";
    
    /**
     * The HMAC algorithm required by Amazon
     */
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    /**
     * This is the URI for the service, don't change unless you really know
     * what you're doing.
     */
    private static final String REQUEST_URI = "/onca/xml";
    
    /**
     * The sample uses HTTP GET to fetch the response. If you changed the sample
     * to use HTTP POST instead, change the value below to POST. 
     */
    private static final String REQUEST_METHOD = "GET";
    
    /**
	 * Supported AMAZON Hosts
	 */
    private static final String AMAZON_HOST_COM		= "amazon.com";
	private static final String AMAZON_HOST_DE		= "amazon.de";
	private static final String AMAZON_HOST_CA		= "amazon.ca";
	private static final String AMAZON_HOST_FR		= "amazon.fr";
	private static final String AMAZON_HOST_CO_JP	= "amazon.co.jp";
	private static final String AMAZON_HOST_JP		= "amazon.jp";
	private static final String AMAZON_HOST_CO_UK	= "amazon.co.uk";
	
	private static final String ECS_AMAZON_HOST_COM		= "ecs.amazonaws.com";
	private static final String ECS_AMAZON_HOST_DE		= "ecs.amazonaws.de";
	private static final String ECS_AMAZON_HOST_CA		= "ecs.amazonaws.ca";
	private static final String ECS_AMAZON_HOST_FR		= "ecs.amazonaws.fr";
	private static final String ECS_AMAZON_HOST_JP		= "ecs.amazonaws.jp";
	private static final String ECS_AMAZON_HOST_CO_UK	= "ecs.amazonaws.co.uk";

    private String endpoint = null;
    private String awsAccessKeyId = null;
    private String awsSecretKey = null;

    private SecretKeySpec secretKeySpec = null;
    private Mac mac = null;

    /**
     * You must provide the three values below to initialize the helper.
     *  
     * @param endpoint          Destination for the requests.
     * @param awsAccessKeyId    Your AWS Access Key ID
     * @param awsSecretKey      Your AWS Secret Key
     * @throws ScrapingException 
     */
    public static SignedRequestsHelper getInstance(
            URL url, 
            String awsAccessKeyId, 
            String awsSecretKey
    ) throws IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ScrapingException
    {
        if (null == url)
            { throw new IllegalArgumentException("endpoint is null"); }
        if (null == awsAccessKeyId || awsAccessKeyId.length() == 0) 
            { throw new IllegalArgumentException("awsAccessKeyId is null or empty"); }
        if (null == awsSecretKey || awsSecretKey.length() == 0)   
            { throw new IllegalArgumentException("awsSecretKey is null or empty"); }
        
        SignedRequestsHelper instance = new SignedRequestsHelper();
        instance.endpoint = getEndpointForLocale(url);
        instance.awsAccessKeyId = awsAccessKeyId;
        instance.awsSecretKey = awsSecretKey;

        byte[] secretyKeyBytes = instance.awsSecretKey.getBytes(UTF8_CHARSET);
        instance.secretKeySpec = new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
        instance.mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        instance.mac.init(instance.secretKeySpec);

        return instance;
    }
    
    /**
     * returns ecs endpoint for specific locales
     * 
     * @param url
     * @return
     * @throws ScrapingException
     */
    private static String getEndpointForLocale(URL url) throws ScrapingException {
    	final String host = url.getHost();
		
		if(host.contains(AMAZON_HOST_COM))
			return ECS_AMAZON_HOST_COM;
		else if(host.contains(AMAZON_HOST_DE))
			return ECS_AMAZON_HOST_DE;
		else if(host.contains(AMAZON_HOST_CA))
			return ECS_AMAZON_HOST_CA;
		else if(host.contains(AMAZON_HOST_FR))
			return ECS_AMAZON_HOST_FR;
		else if(host.contains(AMAZON_HOST_CO_JP) || host.contains(AMAZON_HOST_JP))
			return ECS_AMAZON_HOST_JP;
		else if(host.contains(AMAZON_HOST_CO_UK))
			return ECS_AMAZON_HOST_CO_UK;
		else {
			throw new ScrapingException("The Amazon service for " + host + " is currently not supported.");
		}
	}

	/**
     * The construct is private since we'd rather use getInstance()
     */
    private SignedRequestsHelper() {}

    /**
     * This method signs requests in hashmap form. It returns a URL that should
     * be used to fetch the response. The URL returned should not be modified in
     * any way, doing so will invalidate the signature and Amazon will reject
     * the request.
     */
    public String sign(Map<String, String> params) {
        // Let's add the AWSAccessKeyId and Timestamp parameters to the request.
        params.put("AWSAccessKeyId", this.awsAccessKeyId);
        params.put("Timestamp", this.timestamp());

        // The parameters need to be processed in lexicographical order, so we'll
        // use a TreeMap implementation for that.
        SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
        
        // get the canonical form the query string
        String canonicalQS = this.canonicalize(sortedParamMap);
        
        // create the string upon which the signature is calculated 
        String toSign = 
            REQUEST_METHOD + "\n" 
            + this.endpoint + "\n"
            + REQUEST_URI + "\n"
            + canonicalQS;

        // get the signature
        String hmac = this.hmac(toSign);
        String sig = this.percentEncodeRfc3986(hmac);

        // construct the URL
        String url = "http://" + this.endpoint + REQUEST_URI + "?" + canonicalQS + "&Signature=" + sig;

        return url;
    }

    /**
     * This method signs requests in query-string form. It returns a URL that
     * should be used to fetch the response. The URL returned should not be
     * modified in any way, doing so will invalidate the signature and Amazon
     * will reject the request.
     */
    public String sign(String queryString) {
        // let's break the query string into it's constituent name-value pairs
        Map<String, String> params = this.createParameterMap(queryString);
        
        // then we can sign the request as before
        return this.sign(params);
    }

    /**
     * Compute the HMAC.
     *  
     * @param stringToSign  String to compute the HMAC over.
     * @return              base64-encoded hmac value.
     */
    private String hmac(String stringToSign) {
        String signature = null;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes(UTF8_CHARSET);
            rawHmac = mac.doFinal(data);
            Base64 encoder = new Base64();
            signature = new String(encoder.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
        }
        return signature;
    }

    /**
     * Generate a ISO-8601 format timestamp as required by Amazon.
     *  
     * @return  ISO-8601 format timestamp.
     */
    private String timestamp() {
        String timestamp = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());
        return timestamp;
    }

    /**
     * Canonicalize the query string as required by Amazon.
     * 
     * @param sortedParamMap    Parameter name-value pairs in lexicographical order.
     * @return                  Canonical form of query string.
     */
    private String canonicalize(SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            buffer.append(percentEncodeRfc3986(kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        String cannoical = buffer.toString();
        return cannoical;
    }

    /**
     * Percent-encode values according the RFC 3986. The built-in Java
     * URLEncoder does not encode according to the RFC, so we make the
     * extra replacements.
     * 
     * @param s decoded string
     * @return  encoded string per RFC 3986
     */
    private String percentEncodeRfc3986(String s) {
        String out;
        try {
        	/*
        	 * Somehow the encode() Method appends a carriage return and new line char to the string.
        	 * Therefore this signature does not match the Amazon signature.
        	 * 
			 * This is not the most elegant way to fix, but replacing the "chars" is working for now.
        	 */
            out = URLEncoder.encode(s, UTF8_CHARSET)
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~")
           		.replace("%0D%0A", "");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }

    /**
     * Takes a query string, separates the constituent name-value pairs
     * and stores them in a hashmap.
     * 
     * @param queryString
     * @return
     */
    private Map<String, String> createParameterMap(String queryString) {
        Map<String, String> map = new HashMap<String, String>();
        String[] pairs = queryString.split("&");

        for (String pair: pairs) {
            if (pair.length() < 1) {
                continue;
            }

            String[] tokens = pair.split("=",2);
            for(int j=0; j<tokens.length; j++)
            {
                try {
                    tokens[j] = URLDecoder.decode(tokens[j], UTF8_CHARSET);
                } catch (UnsupportedEncodingException e) {
                }
            }
            switch (tokens.length) {
                case 1: {
                    if (pair.charAt(0) == '=') {
                        map.put("", tokens[0]);
                    } else {
                        map.put(tokens[0], "");
                    }
                    break;
                }
                case 2: {
                    map.put(tokens[0], tokens[1]);
                    break;
                }
            }
        }
        return map;
    }
}
