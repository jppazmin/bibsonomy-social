package org.bibsonomy.opensocial.spi;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthProblemException;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;

public class MockOAuthDataStore implements OAuthDataStore {
	public MockOAuthDataStore() {
		int i = 0;
	}

	public void authorizeToken(OAuthEntry entry, String userId)
			throws OAuthProblemException {
		throw new OAuthProblemException("Not implemented");
	}

	public OAuthEntry convertToAccessToken(OAuthEntry entry)
			throws OAuthProblemException {
		throw new OAuthProblemException("Not implemented");
	}

	public OAuthEntry generateRequestToken(String consumerKey,
			String oauthVersion, String signedCallbackUrl)
			throws OAuthProblemException {
		throw new OAuthProblemException("Not implemented");
	}

	public OAuthConsumer getConsumer(String consumerKey)
			throws OAuthProblemException {
		throw new OAuthProblemException("Not implemented");
	}

	public OAuthEntry getEntry(String oauthToken) {
		return null;
	}

	public SecurityToken getSecurityTokenForConsumerRequest(String consumerKey,
			String userId) throws OAuthProblemException {
		throw new OAuthProblemException("Not implemented");
	}

	public void disableToken(OAuthEntry entry) {
	}

	public void removeToken(OAuthEntry entry) {
	}
}