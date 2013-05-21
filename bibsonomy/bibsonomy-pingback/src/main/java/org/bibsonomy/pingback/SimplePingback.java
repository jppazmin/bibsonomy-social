package org.bibsonomy.pingback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.services.Pingback;
import org.bibsonomy.services.URLGenerator;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Required;

import com.malethan.pingback.Link;
import com.malethan.pingback.LinkLoader;
import com.malethan.pingback.PingbackClient;
import com.malethan.pingback.PingbackException;

public class SimplePingback implements Pingback {
	private static final Log log = LogFactory.getLog(Pingback.class);
	private URLGenerator urlGenerator;
	private PingbackClient pingbackClient;
	private PingbackClient trackbackClient;
	private LinkLoader linkLoader;

	public String sendPingback(Post<? extends Resource> post) {
		if (GroupUtils.isPublicGroup(post.getGroups())) {
			String linkAddress = getLinkAddress(post);
			if (ValidationUtils.present(linkAddress)) {
				Link link = this.linkLoader.loadLink(linkAddress);
				if (link.isSuccess()) {
					log.debug("found pingback link");
					if (link.isPingbackEnabled())
						return "pingback: " + sendPingback(post, link);
					if ((link instanceof TrackbackLink)) {
						log.debug("found trackback link");
						TrackbackLink trackbackLink = (TrackbackLink) link;
						if (trackbackLink.isTrackbackEnabled()) {
							return "trackback: "
									+ sendTrackback(post, trackbackLink);
						}
					}
				}
			}
		}
		return null;
	}

	private String getLinkAddress(Post<? extends Resource> post) {
		Resource resource = post.getResource();
		if ((resource instanceof Bookmark))
			return ((Bookmark) resource).getUrl();
		if ((resource instanceof BibTex)) {
			BibTex bibtex = (BibTex) resource;

			String url = bibtex.getUrl();
			if (ValidationUtils.present(url))
				return UrlUtils.cleanBibTeXUrl(url);
			bibtex.serializeMiscFields();

			String ee = bibtex.getMiscField("ee");
			if (ValidationUtils.present(ee))
				return UrlUtils.cleanBibTeXUrl(ee);
		}
		return null;
	}

	private String sendPingback(Post<? extends Resource> post, Link link) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("sending pingback for " + link.getUrl() + " to "
						+ link.getPingbackUrl());
			}
			post.getResource().recalculateHashes();
			String permaLink = this.urlGenerator.getPostUrl(post);
			return this.pingbackClient.sendPingback(permaLink, link);
		} catch (PingbackException e) {
			log.debug("Pingback to '" + link.getUrl() + "' failed", e);
			if (48 == e.getFaultCode()) {
				log.debug("Pingback to '" + link.getUrl()
						+ "' already registered");
			}
			return "error (" + e.getMessage() + ")";
		}
	}

	private String sendTrackback(Post<? extends Resource> post,
			TrackbackLink link) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("sending trackback for " + link.getUrl() + " to "
						+ link.getPingbackUrl());
			}
			post.getResource().recalculateHashes();
			String permaLink = this.urlGenerator.getPostUrl(post);
			return this.trackbackClient.sendPingback(permaLink, link);
		} catch (PingbackException e) {
			log.debug("Trackback to '" + link.getUrl() + "' failed", e);
			return "error (" + e.getMessage() + ")";
		}
	}

	@Required
	public void setUrlGenerator(URLGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}

	@Required
	public void setPingbackClient(PingbackClient pingbackClient) {
		this.pingbackClient = pingbackClient;
	}

	@Required
	public void setLinkLoader(LinkLoader linkLoader) {
		this.linkLoader = linkLoader;
	}

	@Required
	public void setTrackbackClient(PingbackClient trackbackClient) {
		this.trackbackClient = trackbackClient;
	}
}