package org.bibsonomy.opensocial.oauth.database;

import java.util.Date;
import java.util.UUID;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.crypto.Crypto;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.opensocial.oauth.database.beans.OAuthConsumerInfo;
import org.bibsonomy.util.ValidationUtils;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class BibSonomyOAuthDataStore implements OAuthDataStore {
	private static final int CALLBACK_TOKEN_LENGTH = 6;
	private static final String OAUTH_CONTAINER_NAME = "default";
	private static final String OAUTH_DOMAIN_NAME = "samplecontainer.com";
	private static final String OAUTH_BASEURL = "/oauth/";
	IOAuthLogic authLogic = IbatisOAuthLogic.getInstance();
	private OAuthServiceProvider serviceProvider;
	private static BibSonomyOAuthDataStore instance;

	@Inject
	public BibSonomyOAuthDataStore(
			@Named("shindig.oauth.base-url") String baseUrl) {
		this.serviceProvider = new OAuthServiceProvider(baseUrl
				+ "requestToken", baseUrl + "authorize", baseUrl
				+ "accessToken");
	}

	public BibSonomyOAuthDataStore() {
		this.serviceProvider = new OAuthServiceProvider("/oauth/requestToken",
				"/oauth/authorize", "/oauth/accessToken");
	}

	public static BibSonomyOAuthDataStore getInstance() {
		if (instance == null) {
			instance = new BibSonomyOAuthDataStore();
		}

		return instance;
	}

	public void authorizeToken(OAuthEntry entry, String userId)
			throws OAuthProblemException {
		if ((ValidationUtils.present(entry))
				&& (ValidationUtils.present(userId))) {
			entry.setAuthorized(true);
			entry.setUserId(userId);
			if (entry.isCallbackUrlSigned()) {
				entry.setCallbackToken(Crypto.getRandomDigits(6));
			}
			this.authLogic.updateProviderToken(entry);
		} else {
			throw new RuntimeException("Error updating token '"
					+ entry.getToken() + "' for '" + entry.getUserId() + "'");
		}
	}

	public OAuthEntry convertToAccessToken(OAuthEntry entry)
			throws OAuthProblemException {
		if (!ValidationUtils.present(entry)) {
			throw new IllegalArgumentException("no OAuth entry given");
		}

		if (!OAuthEntry.Type.REQUEST.equals(entry.getType())) {
			throw new OAuthProblemException("Token must be a request token");
		}

		OAuthEntry accessEntry = new OAuthEntry(entry);

		accessEntry.setUserId(entry.getUserId());

		accessEntry.setToken(UUID.randomUUID().toString());
		accessEntry.setTokenSecret(UUID.randomUUID().toString());

		accessEntry.setType(OAuthEntry.Type.ACCESS);
		accessEntry.setIssueTime(new Date());

		this.authLogic.deleteProviderToken(entry.getToken());

		this.authLogic.createProviderToken(accessEntry);

		return accessEntry;
	}

	public void disableToken(OAuthEntry entry) {
		if (!ValidationUtils.present(entry)) {
			throw new IllegalArgumentException("no OAuth entry given");
		}

		entry.setType(OAuthEntry.Type.DISABLED);
		this.authLogic.updateProviderToken(entry);
	}

	public OAuthEntry generateRequestToken(String consumerKey,
			String oauthVersion, String signedCallbackUrl)
			throws OAuthProblemException {
		OAuthEntry entry = new OAuthEntry();
		entry.setAppId(consumerKey);
		entry.setConsumerKey(consumerKey);

		entry.setDomain("samplecontainer.com");

		entry.setContainer("default");

		entry.setToken(UUID.randomUUID().toString());
		entry.setTokenSecret(UUID.randomUUID().toString());

		entry.setType(OAuthEntry.Type.REQUEST);
		entry.setIssueTime(new Date());
		entry.setOauthVersion(oauthVersion);
		if (signedCallbackUrl != null) {
			entry.setCallbackUrlSigned(true);
			entry.setCallbackUrl(signedCallbackUrl);
		}

		this.authLogic.createProviderToken(entry);
		return entry;
	}

	public OAuthConsumer getConsumer(String consumerKey)
			throws OAuthProblemException {
		OAuthConsumerInfo consumerInfo = this.authLogic
				.readConsumer(consumerKey);
		if (!ValidationUtils.present(consumerInfo)) {
			return null;
		}

		OAuthConsumer consumer = new OAuthConsumer(null, consumerKey,
				consumerInfo.getConsumerSecret(), this.serviceProvider);

		if (ValidationUtils.present(consumerInfo.getKeyName())) {
			consumer.setProperty(consumerInfo.getKeyName(),
					consumerInfo.getConsumerSecret());
		}

		consumer.setProperty("title", consumerInfo.getTitle());
		consumer.setProperty("icon", consumerInfo.getIcon());
		consumer.setProperty("thumbnail", consumerInfo.getThumbnail());
		consumer.setProperty("summary", consumerInfo.getSummary());
		consumer.setProperty("description", consumerInfo.getDescription());

		return consumer;
	}

	public OAuthEntry getEntry(String oauthToken) {
		return this.authLogic.readProviderToken(oauthToken);
	}

	public SecurityToken getSecurityTokenForConsumerRequest(String consumerKey,
			String userId) throws OAuthProblemException {
		return null;
	}

	public void removeToken(OAuthEntry entry) {
	}
}