package org.bibsonomy.opensocial.oauth.database.beans;

import org.apache.shindig.gadgets.oauth.BasicOAuthStoreTokenIndex;

public class OAuthTokenIndex extends BasicOAuthStoreTokenIndex {
	private String accessToken;
	private String tokenSecret;
	private String sessionHandle;
	private long tokenExpireMillis;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getTokenSecret() {
		return this.tokenSecret;
	}

	public void setSessionHandle(String sessionHandle) {
		this.sessionHandle = sessionHandle;
	}

	public String getSessionHandle() {
		return this.sessionHandle;
	}

	public void setTokenExpireMillis(long tokenExpireMillis) {
		this.tokenExpireMillis = tokenExpireMillis;
	}

	public long getTokenExpireMillis() {
		return this.tokenExpireMillis;
	}
}