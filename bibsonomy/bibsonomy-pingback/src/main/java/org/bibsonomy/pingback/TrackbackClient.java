package org.bibsonomy.pingback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.bibsonomy.util.ValidationUtils;

import com.malethan.pingback.Link;
import com.malethan.pingback.PingbackClient;
import com.malethan.pingback.PingbackException;

public class TrackbackClient implements PingbackClient {

	private static final Log log = LogFactory.getLog(TrackbackClient.class);
	private static final int MAX_HTTP_BODY_CHARS = 500000;
	private static final Pattern XML_ERROR = Pattern
			.compile("<error>\\s*([01])\\s*</error>");
	private static final Pattern XML_ERROR_MESSAGE = Pattern
			.compile("<message>\\s*(.+?)\\s*</message>");
	private final HttpClient httpClient;

	public TrackbackClient() {
		this.httpClient = HttpClientHolder.getInstance().getHttpClient();
	}

	public String sendPingback(String articleUrl, Link link) {
		if ((link instanceof TrackbackLink)) {
			TrackbackLink trackbackLink = (TrackbackLink) link;
			String trackbackUrl = trackbackLink.getPingbackUrl();
			try {
				HttpPost httpPost = new HttpPost(trackbackUrl);

				List formparams = new ArrayList();
				formparams.add(new BasicNameValuePair("url", articleUrl));

				UrlEncodedFormEntity entity2 = new UrlEncodedFormEntity(
						formparams, "UTF-8");
				entity2.setContentType("application/x-www-form-urlencoded; charset=utf-8");
				httpPost.setEntity(entity2);
				try {
					log.debug(new StringBuilder()
							.append("sending trackback request to ")
							.append(trackbackUrl).toString());
					HttpResponse response = this.httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();
					if (ValidationUtils.present(entity)) {
						Header contentType = entity.getContentType();
						if ((ValidationUtils.present(contentType))
								&& (contentType.getValue()
										.contains("application/x-www-form-urlencoded"))) {
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(entity.getContent()));
							try {
								String content = readContent(reader);

								Matcher matcher = XML_ERROR.matcher(content);
								if (matcher.find()) {
									String responseCode = matcher.group(1);
									if ("0".equals(responseCode)) {
										return "success";
									}
									if ("1".equals(responseCode)) {
										Matcher matcher2 = XML_ERROR_MESSAGE
												.matcher(content);
										String message;
										if (matcher2.find())
											message = matcher2.group(1);
										else {
											message = "the server did not return an error message";
										}
										throw new PingbackException(message, 0,
												trackbackUrl,
												trackbackLink.getUrl());
									}
								}
							} finally {
							}
						}
					} else {
						throw new PingbackException(
								"got no response from server", 33,
								trackbackUrl, trackbackLink.getUrl());
					}
				} finally {
					httpPost.abort();
				}
			} catch (IOException e) {
				log.debug("got exception: ", e);
				throw new PingbackException(new StringBuilder()
						.append("request error: ").append(e).toString(), 50,
						trackbackUrl, trackbackLink.getUrl());
			}
			throw new PingbackException("unknown error", 0, trackbackUrl,
					trackbackLink.getUrl());
		}
		throw new IllegalArgumentException(new StringBuilder()
				.append("Only instances of ")
				.append(TrackbackLink.class.getSimpleName())
				.append(" are supported as 'link' argument.").toString());
	}

	protected String readContent(BufferedReader reader) throws IOException {
		StringBuilder content = new StringBuilder();
		String line;
		while (((line = reader.readLine()) != null)
				&& (content.length() < 500000)) {
			content.append(line);
		}
		return content.toString();
	}
}