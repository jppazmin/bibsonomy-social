package org.bibsonomy.pingback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.bibsonomy.util.ValidationUtils;

import com.malethan.pingback.Link;
import com.malethan.pingback.LinkLoader;

public class HttpClientLinkLoader implements LinkLoader {
	private static final Log log = LogFactory
			.getLog(HttpClientLinkLoader.class);
	private static final String HTTP_HEADER_XPINGBACK = "X-Pingback";
	private static final Pattern PINGBACK_URL_PATTERN = Pattern.compile(
			"<link rel=\"pingback\" href=\"([^\"]+)\" ?/?>", 2);
	private static final Pattern TRACKBACK_RDF_PATTERN = Pattern.compile(
			"(<rdf:RDF.*?</rdf:RDF>)", 32);
	private static final Pattern TRACKBACK_LINK_URL_PATTERN = Pattern
			.compile("dc:identifier=\"([^\"]+)\"");
	private static final Pattern TRACKBACK_PING_URL_PATTERN = Pattern
			.compile("trackback:ping=\"([^\"]+)\"");
	private static final Pattern END_OF_HTML_HEAD_PATTERN = Pattern.compile(
			"<body[^>]+>|</head>", 2);
	private static final int MAX_HTML_BODY_CHARS = 500000;
	private final HttpClient httpClient;

	public HttpClientLinkLoader() {
		this.httpClient = HttpClientHolder.getInstance().getHttpClient();
	}

	public Link loadLink(String linkUrl) {
		log.debug(new StringBuilder().append("loading link ").append(linkUrl)
				.toString());
		try {
			HttpGet httpGet = new HttpGet(linkUrl);
			try {
				HttpResponse response = this.httpClient.execute(httpGet);

				Header header = response.getFirstHeader("X-Pingback");
				if (ValidationUtils.present(header)) {
					log.debug("found pingback header");
					return new Link(null, linkUrl, header.getValue(), true);
				}

				HttpEntity entity = response.getEntity();
				if (ValidationUtils.present(entity)) {
					Header contentType = entity.getContentType();
					if ((ValidationUtils.present(contentType))
							&& (contentType.getValue().contains("html"))) {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(entity.getContent()));
						try {
							String headContent = readHeadSectionOfPage(reader);

							String pingbackUrl = getPingbackUrlFromHtml(headContent);
							if (ValidationUtils.present(pingbackUrl)) {
								log.debug("found pingback meta tag");
								return new Link(null, linkUrl, pingbackUrl,
										true);
							}

							String trackbackUrl = getTrackbackUrl(linkUrl,
									headContent, reader);
							if (ValidationUtils.present(trackbackUrl)) {
								log.debug("found trackback URL");
								return new TrackbackLink(null, linkUrl,
										trackbackUrl, true);
							}
						} finally {
						}
					}
				}
			} finally {
				httpGet.abort();
			}
		} catch (Exception e) {
			log.debug("got exception: ", e);
		}

		log.debug("no link found");
		return new Link(null, linkUrl, null, false);
	}

	protected String getTrackbackUrl(String linkUrl, String headContent,
			BufferedReader reader) throws IOException {
		String trackbackUrl = getTrackbackUrlFromHtml(linkUrl, headContent);
		if (ValidationUtils.present(trackbackUrl)) {
			return trackbackUrl;
		}

		return getTrackbackUrlFromHtml(linkUrl, readPortionOfPage(reader));
	}

	public String loadPageContents(String linkUrl) {
		throw new UnsupportedOperationException();
	}

	public boolean containsLink(String htmlText, String link) {
		throw new UnsupportedOperationException();
	}

	public List<String> findLinkAddresses(String textileText) {
		throw new UnsupportedOperationException();
	}

	protected String readPortionOfPage(BufferedReader reader)
			throws IOException {
		StringBuilder content = new StringBuilder();
		String line;
		while (((line = reader.readLine()) != null)
				&& (content.length() < 500000)) {
			content.append(line);
		}
		return content.toString();
	}

	protected String readHeadSectionOfPage(BufferedReader reader)
			throws IOException {
		StringBuilder content = new StringBuilder();
		String line = reader.readLine();
		while ((line != null) && (!reachedEndOfHeadSection(line))) {
			content.append(line);
			line = reader.readLine();
		}
		return content.toString();
	}

	protected boolean reachedEndOfHeadSection(String line) {
		return END_OF_HTML_HEAD_PATTERN.matcher(line).matches();
	}

	protected String getPingbackUrlFromHtml(String html) {
		Matcher matcher = PINGBACK_URL_PATTERN.matcher(html);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	protected String getTrackbackUrlFromHtml(String linkUrl, String html) {
		Matcher matcher = TRACKBACK_RDF_PATTERN.matcher(html);
		while (matcher.find()) {
			String match = matcher.group(1);
			Matcher matcher2 = TRACKBACK_LINK_URL_PATTERN.matcher(match);
			if (matcher2.find()) {
				String linkUrl2 = matcher2.group(1);

				if ((linkUrl.equals(linkUrl2))
						|| (linkUrl.replaceAll("\\#.*$", "").equals(linkUrl2))) {
					Matcher matcher3 = TRACKBACK_PING_URL_PATTERN.matcher(html);
					if (matcher3.find()) {
						return matcher3.group(1);
					}
				}

			}

		}

		return null;
	}
}