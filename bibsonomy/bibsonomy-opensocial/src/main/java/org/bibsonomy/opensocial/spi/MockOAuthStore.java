package org.bibsonomy.opensocial.spi;

import net.oauth.OAuthServiceProvider;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.oauth.OAuthStore;

public class MockOAuthStore implements OAuthStore {
	public OAuthStore.ConsumerInfo getConsumerKeyAndSecret(
			SecurityToken securityToken, String serviceName,
			OAuthServiceProvider provider) throws GadgetException {
		return null;
	}

	public OAuthStore.TokenInfo getTokenInfo(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName) throws GadgetException {
		return null;
	}

	public void removeToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName) throws GadgetException {
	}

	public void setTokenInfo(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName, OAuthStore.TokenInfo tokenInfo)
			throws GadgetException {
	}
}