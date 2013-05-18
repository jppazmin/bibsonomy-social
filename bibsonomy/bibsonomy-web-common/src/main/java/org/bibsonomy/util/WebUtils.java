/**
 *
 *  BibSonomy-Web-Common - A blue social bookmark and publication sharing system.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rja
 * @version $Id: WebUtils.java,v 1.2 2011-01-29 07:34:45 rja Exp $
 */
public class WebUtils {
		private static final Log log = LogFactory.getLog(WebUtils.class);

	/**
	 * maximal number of redirects to follow in {@link #getRedirectUrl(URL)}
	 */
	private static final int MAX_REDIRECT_COUNT = 10;

	/**
	 * The user agent used for all requests with {@link HttpURLConnection}.
	 */
	private static final String USER_AGENT_PROPERTY_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)";

	private static final String CHARSET			= "charset=";
	private static final String DEFAULT_CHARSET	= "UTF-8";
	private static final String LOCATION		= "Location";
	private static final String EQUAL_SIGN		= "=";
	private static final String AMP_SIGN		= "&";
	private static final String NEWLINE			= "\n";
	private static final String SEMICOLON		= ";";
	private static final String USER_AGENT_HEADER_NAME   = "User-Agent";
	private static final String COOKIE_HEADER_NAME  	 = "Cookie";
	private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

	/*
	 * The maximum number of characters (~bytes) to read from a HTTP connection.
	 * We fixed this to 1 MB to avoid that radio streams or huge files
	 * mess up our heap. If this is not enough, please increase the size
	 * carefully.
	 */
	private static final int MAX_CONTENT_LENGTH = 1 * 1024 * 1024;

	/*
	 * according to http://hc.apache.org/httpclient-3.x/threading.html
	 * HttpClient is thread safe and we can use one instance for several requests.
	 */
  	private static final MultiThreadedHttpConnectionManager connectionManager =	new MultiThreadedHttpConnectionManager();
  	private static final HttpClient client = new HttpClient(connectionManager);
  	static {
  		client.getParams().setParameter(HttpMethodParams.USER_AGENT, USER_AGENT_PROPERTY_VALUE);
  	}

	/**
	 * Do a POST request to the given URL with the given content. Assume the charset of the result to be charset.
	 * 
	 * @param url
	 * @param postContent
	 * @param charset - the assumed charset of the result. If <code>null</code>, the charset from the response header is used.
	 * @param cookie - the Cookie to be attached to the request. If <code>null</code>, the Cookie header is not set.
	 * @return The content of the result page.
	 * 
	 * @throws IOException
	 * 
	 * @Deprecated
	 */
	public static String getPostContentAsString(final URL url, final String postContent, final String charset, final String cookie) throws IOException {
		final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setAllowUserInteraction(false);
		urlConn.setDoInput(true);
		urlConn.setDoOutput(true);
		urlConn.setUseCaches(false);
		urlConn.setRequestMethod("POST");
		urlConn.setRequestProperty(CONTENT_TYPE_HEADER_NAME, "application/x-www-form-urlencoded");


		/*
		 * set user agent (see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) since some 
		 * pages require it to download content.
		 */
		urlConn.setRequestProperty(USER_AGENT_HEADER_NAME, USER_AGENT_PROPERTY_VALUE);

		if (cookie != null) {
			urlConn.setRequestProperty(COOKIE_HEADER_NAME, cookie);
		}

		
		writeStringToStream(postContent, urlConn.getOutputStream());

		// connect
		urlConn.connect();

		/*
		 * extract character encoding from header
		 */
		final String activeCharset;
		if (charset == null) {
			activeCharset = getCharset(urlConn);
		} else {
			activeCharset = charset;
		}

		/*
		 * FIXME: check content type header to ensure that we only read textual 
		 * content (and not a PDF, radio stream or DVD image ...)
		 */
		
		// write into string writer
		final StringBuilder out = inputStreamToStringBuilder(urlConn.getInputStream(), activeCharset);

		// disconnect
		urlConn.disconnect();

		return out.toString();
	}

	
	/**
	 * Do a POST request to the given URL with the given content. Assume the charset of the result to be charset.
	 * 
	 * @param url
	 * @param postContent
	 * @param charset - the assumed charset of the result. If <code>null</code>, the charset from the response header is used.
	 * @return The content of the result page.
	 * 
	 * @throws IOException
	 * 
	 * @Deprecated
	 */
	public static String getPostContentAsString(final URL url, final String postContent, final String charset) throws IOException {
		return getPostContentAsString(url, postContent, charset, null);
	}
	
	/**
	 * Do a POST request to the given URL with the given content and cookie. Assume the charset of the result to be charset.
	 * 
	 * @param url
	 * @param postContent
	 * @param cookie
	 * @return The content of the result page.
	 * 
	 * @throws IOException
	 * 
	 * @Deprecated
	 */
	public static String getPostContentAsString(final String cookie, final URL url, final String postContent) throws IOException {
		return getPostContentAsString(url, postContent, null, cookie);
	}

	/**
	 * Do a POST request to the given URL with the given content.
	 * 
	 * @param url
	 * @param postContent
	 * @return The content of the result page.
	 * 
	 * @throws IOException
	 * 
	 * @Deprecated
	 */
	public static String getPostContentAsString(final URL url, final String postContent) throws IOException {
		return getPostContentAsString(url, postContent, null, null);
	}

	/**
	 * Reads from a URL and writes the content into a string.
	 * 
	 * @param inputURL the URL of the content.
	 * @param cookie a cookie which should be included in the header of the request send to the server
	 * @return String which holds the page content.
	 * @throws IOException 
	 * 
	 * @Deprecated
	 */
	public static String getContentAsString(final URL inputURL, final String cookie) throws IOException {
		try {
			final HttpURLConnection urlConn = (HttpURLConnection) inputURL.openConnection();
			urlConn.setAllowUserInteraction(false);
			urlConn.setDoInput(true);
			urlConn.setDoOutput(false);
			urlConn.setUseCaches(false);

			/*
			 * set user agent (see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) since some 
			 * pages require it to download content.
			 */
			urlConn.setRequestProperty(USER_AGENT_HEADER_NAME, USER_AGENT_PROPERTY_VALUE);
			if (cookie != null) {
				urlConn.setRequestProperty(COOKIE_HEADER_NAME, cookie);
			}
			urlConn.connect();

			/*
			 * extract character encoding from header
			 */
			final String charSet = getCharset(urlConn);

			/*
			 * FIXME: check content type header to ensure that we only read textual 
			 * content (and not a PDF, radio stream or DVD image ...)
			 */
			
			/*
			 * write content into string buffer
			 */
			final StringBuilder out = inputStreamToStringBuilder(urlConn.getInputStream(), charSet);

			urlConn.disconnect();

			return out.toString();
		} catch (final ConnectException cex) {
			log.debug("Could not get content for URL " + inputURL.toString() + " : " + cex.getMessage());
			throw new IOException(cex);
		} catch (final IOException ioe) {
			log.debug("Could not get content for URL " + inputURL.toString() + " : " + ioe.getMessage());
			throw ioe;
		}
	}
	
	/**
	 * Reads from a URL and writes the content into a string.
	 * 
	 * @param inputURL the URL of the content.
	 * @return String which holds the page content.
	 * @throws IOException
	 * 
	 * @Deprecated
	 */
	public static String getContentAsString(final URL inputURL) throws IOException {
		return getContentAsString(inputURL, null);
	}
	
	
	/**
	 * Reads from a URL and writes the content into a string.
	 * 
	 * @param url
	 * @param cookie
	 * @param postData 
	 * @param visitBefore
	 * 
	 * @return String which holds the page content.
	 * 
	 * @throws IOException
	 */
	public static String getContentAsString(final String url, final String cookie, final String postData, final String visitBefore) throws IOException {
		if (present(visitBefore)) {
			/*
			 * visit URL to get cookies if needed
			 */
			client.executeMethod(new GetMethod(visitBefore));
		}
		
		final HttpMethod method;
		if (present(postData)) {
			/*
			 * do a POST request
			 */
			final List<NameValuePair> data = new ArrayList<NameValuePair>();
			
			for (final String s : postData.split(AMP_SIGN)) {
				final String[] p = s.split(EQUAL_SIGN);
				
				if (p.length != 2) {
					continue;
				} 
				
				data.add(new NameValuePair(p[0], p[1]));
			}
			
			method = new PostMethod(url);
			((PostMethod)method).setRequestBody(data.toArray(new NameValuePair[data.size()]));
		} else {
			/*
			 * do a GET request
			 */
			method = new GetMethod(url);
			method.setFollowRedirects(true);
		}
		
		/*
		 * set cookie
		 */
		if (present(cookie)) {
			method.setRequestHeader(COOKIE_HEADER_NAME, cookie);
		}
		
		/*
		 * do request
		 */
		final int status = client.executeMethod(method);
		if (status != HttpStatus.SC_OK) {
			throw new IOException(url + " returns: " + status);
		}

		/*
		 * FIXME: check content type header to ensure that we only read textual 
		 * content (and not a PDF, radio stream or DVD image ...)
		 */

		
		/*
		 * collect response
		 */
		final String charset = extractCharset(method.getResponseHeader(CONTENT_TYPE_HEADER_NAME).getValue()); 
		final StringBuilder content = inputStreamToStringBuilder(method.getResponseBodyAsStream(), charset);
		method.releaseConnection();
		
		final String string = content.toString();
		if (string.length() > 0) {
			return string;
		}
		
		return null;

	}
	
	/**
	 * Reads from a URL and writes the content into a string.
	 * 
	 * @param url
	 * @return String which holds the page content.
	 * @throws IOException
	 */
	public static String getContentAsString(final String url) throws IOException {
		return getContentAsString(url, null, null, null);
	}
	
	/**
	 * Reads from a URL and writes the content into a string.
	 * 
	 * @param url
	 * @param cookie 
	 * @return String which holds the page content.
	 * @throws IOException
	 */
	public static String getContentAsString(final String url, final String cookie) throws IOException {
		return getContentAsString(url, cookie, null, null);
	}


	/**
	 * Sends a request to the given URL and checks, if it contains a redirect.
	 * If it does, returns the redirect URL. Otherwise, returns null.
	 * This is done up to {@value #MAX_REDIRECT_COUNT}-times until the final page is reached. 
	 *  
	 * 
	 * 
	 * @param url
	 * @return - The redirect URL.
	 */
	public static URL getRedirectUrl(final URL url) {
		try {
			URL internalUrl = url;
			URL previousUrl = null;
			int redirectCtr = 0;
			while (internalUrl != null && redirectCtr < MAX_REDIRECT_COUNT) {
				redirectCtr++;

				final HttpURLConnection urlConn = (HttpURLConnection) internalUrl.openConnection();

				urlConn.setAllowUserInteraction(false);
				urlConn.setDoInput(true);
				urlConn.setDoOutput(false);
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(false);


				/*
				 * set user agent (see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html) since some 
				 * pages require it to download content.
				 */
				urlConn.setRequestProperty(USER_AGENT_HEADER_NAME , USER_AGENT_PROPERTY_VALUE);

				urlConn.connect();

				// get URL to redirected resource
				previousUrl = internalUrl;
				try {
					internalUrl = new URL(urlConn.getHeaderField(LOCATION));
				} catch (final MalformedURLException e) {
					internalUrl = null;
				}

				urlConn.disconnect();

			}

			return previousUrl;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the cookies returned by the server on accessing the URL. 
	 * The format of the returned cookies is as
	 * 
	 * 
	 * @param url
	 * @return The cookies as string, build by {@link #buildCookieString(List)}.
	 * @throws IOException
	 */
	public static String getCookies(final URL url) throws IOException {
		final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

		urlConn.setAllowUserInteraction(false);
		urlConn.setDoInput(true);
		urlConn.setDoOutput(false);
		urlConn.setUseCaches(false);

		urlConn.setRequestProperty(USER_AGENT_HEADER_NAME, USER_AGENT_PROPERTY_VALUE);

		urlConn.connect();

		final List<String> cookies = urlConn.getHeaderFields().get("Set-Cookie");
		urlConn.disconnect();
		
		return buildCookieString(cookies);
	}
	
	/**
	 * Builds a cookie string as used in the HTTP header.
	 * 
	 * @param cookies - a list of key/value pairs
	 * @return The cookies folded into a string.
	 */
	public static String buildCookieString(final List<String> cookies) {
		final StringBuffer result = new StringBuffer();

		if (cookies != null) {
			for (final String cookie : cookies) {
				if (result.length() != 0)
					result.append(";");
				result.append(cookie);
			}
		}
		return result.toString();
	}


	/** Extracts the charset ID of a web page as returned by the server.
	 * 
	 * @param urlConn
	 * @return
	 */
	private static String getCharset(final HttpURLConnection urlConn) {
		return extractCharset(urlConn.getContentType());
	}

	/**
	 * Extracts the charset from the given string. The string should resemble
	 * the content type header of an HTTP request. Valid examples are:
	 * <ul>
	 * <li>text/html; charset=utf-8; qs=1</li>
	 * <li>
	 * </ul>
	 *
	 * @param contentType
	 * @return - The charset.
	 */
	public static String extractCharset(final String contentType) {
		/*
		 * this typically looks like that:
		 * text/html; charset=utf-8; qs=1
		 */
		if (present(contentType)) {
			final int charsetPosition = contentType.indexOf(CHARSET);
			if (charsetPosition > -1) {
				/*
				 * cut this:
				 *                    |<--   -->|             
				 * text/html; charset=utf-8; qs=1
				 */
				String charSet = contentType.substring(charsetPosition + CHARSET.length());

				// get only charset
				final int charsetEnding = charSet.indexOf(SEMICOLON);
				if (charsetEnding > -1) {
					/*
					 * cut this:
					 * |<->|             
					 * utf-8; qs=1
					 */
					charSet = charSet.substring(0, charsetEnding);
				}
				return charSet.trim().toUpperCase();
			} 
		} 
		/*
		 * default charset
		 */
		return DEFAULT_CHARSET;
	}

	/** Copies the stream into the string builder.
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static StringBuilder inputStreamToStringBuilder(final InputStream inputStream, final String charset) throws IOException {
		final InputStreamReader in;
		/*
		 * set charset
		 */
		if (charset == null || charset.trim().equals(""))
			in = new InputStreamReader(inputStream);
		else 
			in = new InputStreamReader(inputStream, charset);
		/*
		 * use buffered reader (we always assume to have text)
		 */
		final BufferedReader buf = new BufferedReader(in);
		final StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = buf.readLine()) != null && sb.length() + line.length() < MAX_CONTENT_LENGTH) {
			sb.append(line).append(NEWLINE);
		}
		buf.close();
		
		return sb;
	}

	/** Writes the given string to the stream.
	 * 
	 * @param s
	 * @param outputStream
	 * @throws IOException
	 */
	private static void writeStringToStream(final String s, final OutputStream outputStream) throws IOException {
		final StringReader reader = new StringReader(s);
		int b;
		while ((b = reader.read()) >= 0) {
			outputStream.write(b);
		}
		outputStream.flush();
	}
}
