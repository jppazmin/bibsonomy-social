package org.bibsonomy.pingback;

import com.malethan.pingback.Link;

public class TrackbackLink extends Link {

	public TrackbackLink(String title, String url, String pingbackUrl,
			boolean success) {
		super(title, url, pingbackUrl, success);
	}

	public boolean isPingbackEnabled() {
		return false;
	}

	public boolean isTrackbackEnabled() {
		return super.isPingbackEnabled();
	}
}