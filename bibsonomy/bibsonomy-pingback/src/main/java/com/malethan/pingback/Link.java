package com.malethan.pingback;

public class Link {
	private String title;
	private String url;
	private String pingbackUrl;
	private boolean success;

	public Link(String title, String url, String pingbackUrl, boolean success) {
		this.title = title;
		this.url = url;
		this.pingbackUrl = pingbackUrl;
		this.success = success;
	}

	public String getTitle() {
		return this.title;
	}

	public String getUrl() {
		return this.url;
	}

	public String getPingbackUrl() {
		return this.pingbackUrl;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public boolean isPingbackEnabled() {
		return (this.pingbackUrl != null) && (this.pingbackUrl.length() > 0);
	}
}