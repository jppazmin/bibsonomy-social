package org.bibsonomy.pingback;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class HttpClientHolder {

	private static HttpClientHolder instance = null;
	private final HttpClient httpClient;

	public static HttpClientHolder getInstance() {
		if (instance == null) {
			instance = new HttpClientHolder();
		}
		return instance;
	}

	private HttpClientHolder() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		ThreadSafeClientConnManager conman = new ThreadSafeClientConnManager(
				schemeRegistry);

		this.httpClient = new DefaultHttpClient(conman);
	}

	public HttpClient getHttpClient() {
		return this.httpClient;
	}
}