package com.malethan.pingback.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.malethan.pingback.Link;
import com.malethan.pingback.LinkLoader;

public class TextileLinkLoader implements LinkLoader {

	private static final Log log = LogFactory.getLog(TextileLinkLoader.class);
	public static final String URL_REGEX = "((\":)|href=\")((http(s?)\\:\\/\\/|~/|/)?((\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))|localhost)(:[\\d]{1,5})?(((/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|/)+|\\?|#)?((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)(&([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?)";
	private String title;
	private String pingbackUrl;
	private boolean success;

	public Link loadLink(String linkUrl) {
		reset();
		try {
			URL url = new URL(linkUrl);
			URLConnection con = url.openConnection();
			this.pingbackUrl = con.getHeaderField("X-Pingback");
			if (con.getContentType().indexOf("text/html") > -1) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String headContent = readHeadSectionOfPage(reader);
				processHtmlForPingbackUrlAndTitle(headContent);
				this.success = true;
			}
		} catch (IOException e) {
			log.error("Had a problem with url: '" + linkUrl + "'", e);
		}

		return new Link(this.title, linkUrl, this.pingbackUrl, this.success);
	}

	private void reset() {
		this.title = null;
		this.pingbackUrl = null;
		this.success = false;
	}

	public List<String> findLinkAddresses(String textileText) {
		Pattern linkPattern = Pattern
				.compile(
						"((\":)|href=\")((http(s?)\\:\\/\\/|~/|/)?((\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))|localhost)(:[\\d]{1,5})?(((/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|/)+|\\?|#)?((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)(&([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?)",
						2);
		List<String> linkUrls = new ArrayList<String>();
		Matcher linkMatcher = linkPattern.matcher(textileText);
		while (linkMatcher.find()) {
			linkUrls.add(linkMatcher.group(3));
		}
		return linkUrls;
	}

	public boolean containsLink(String htmlText, String link) {
		for (String linkInText : findLinkAddresses(htmlText)) {
			if (linkInText.equalsIgnoreCase(link)) {
				return true;
			}
		}
		return false;
	}

	public String loadPageContents(String linkUrl) {
		try {
			URL url = new URL(linkUrl);
			URLConnection con = url.openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			return readPage(reader);
		} catch (IOException e) {
			log.error("Had a problem with url: '" + linkUrl + "'", e);
		}
		return null;
	}

	protected String readHeadSectionOfPage(BufferedReader reader)
			throws IOException {
		String content = "";
		String line = reader.readLine();
		while ((line != null) && (!reachedEndOfHeadSection(line))) {
			content = content + line;
			line = reader.readLine();
		}
		return content;
	}

	protected String readPage(BufferedReader reader) throws IOException {
		String content = "";
		String line = reader.readLine();
		while (line != null) {
			content = content + line;
			line = reader.readLine();
		}
		return content;
	}

	protected boolean reachedEndOfHeadSection(String line) {
		return line.matches("<body[^>]+>|</head>");
	}

	protected void processHtmlForPingbackUrlAndTitle(String content) {
		if (this.pingbackUrl == null) {
			this.pingbackUrl = getPingbackUrlFromHtml(content);
		}
		this.title = getTitleFromHtml(content);
	}

	protected String getTitleFromHtml(String html) {
		Pattern titlePattern = Pattern.compile("<title>([^<>]+)</title>");
		Matcher matcher = titlePattern.matcher(html);
		if (matcher.find()) {
			return matcher.group(1).replaceAll("&[^;]+;", "")
					.replaceAll("\\s+", " ");
		}
		return null;
	}

	protected String getPingbackUrlFromHtml(String html) {
		Pattern pingbackUrlPattern = Pattern.compile(
				"<link rel=\"pingback\" href=\"([^\"]+)\" ?/?>", 2);
		Matcher matcher = pingbackUrlPattern.matcher(html);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
}