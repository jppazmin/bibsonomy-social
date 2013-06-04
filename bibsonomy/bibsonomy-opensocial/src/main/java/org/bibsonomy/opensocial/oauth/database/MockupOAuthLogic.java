package org.bibsonomy.opensocial.oauth.database;

import java.util.List;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.opensocial.oauth.database.beans.OAuthConsumerInfo;

public class MockupOAuthLogic implements IOAuthLogic {
	private static Log log = LogFactory.getLog(MockupOAuthLogic.class);
	OAuthStore.TokenInfo lastToken;

	public void createAuthentication(String gadgetUrl, String server,
			String consumerKey, String consumerSecret,
			BasicOAuthStoreConsumerKeyAndSecret.KeyType keyType) {
	}

	public OAuthStore.ConsumerInfo readAuthentication(
			SecurityToken securityToken, String serviceName,
			OAuthServiceProvider provider) {
		log.info("Retrieving authentication for '" + securityToken.getAppUrl()
				+ "' and server '" + serviceName + "'" + provider.toString());

		BasicOAuthStoreConsumerKeyAndSecret cks = new BasicOAuthStoreConsumerKeyAndSecret(
				serviceName, "mockupKey",
				BasicOAuthStoreConsumerKeyAndSecret.KeyType.HMAC_SYMMETRIC,
				"mockupName", "mockupCallback");
		OAuthConsumer consumer = null;
		consumer = new OAuthConsumer(null, cks.getConsumerKey(),
				cks.getConsumerSecret(), provider);
		consumer.setProperty("oauth_signature_method", "HMAC-SHA1");
		return new OAuthStore.ConsumerInfo(consumer, cks.getKeyName(),
				"mockupCallback");
	}

	public OAuthStore.TokenInfo createToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName, OAuthStore.TokenInfo tokenInfo) {
		log.info("Creating token for " + securityToken.getViewerId());
		this.lastToken = tokenInfo;
		return tokenInfo;
	}

	public void deleteToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName) {
		log.info("Deleting token for " + securityToken.getViewerId());
	}

	public OAuthStore.TokenInfo readToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName) {
		log.info("Reading token for " + securityToken.getViewerId());
		return this.lastToken;
	}

	public void createProviderToken(OAuthEntry entry) {
	}

	public void deleteProviderToken(String token) {
	}

	public OAuthConsumerInfo readConsumer(String consumerKey) {
		return null;
	}

	public OAuthEntry readProviderToken(String oauthToken) {
		return null;
	}

	public void updateProviderToken(OAuthEntry entry) {
	}

	public List<OAuthConsumerInfo> listConsumers() {
		return null;
	}

	public void createConsumer(OAuthConsumerInfo consumerInfo) {
	}

	public void deleteConsumer(String consumerKey) {
	}
}