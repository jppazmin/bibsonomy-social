package org.bibsonomy.opensocial.oauth.database;

import java.util.List;

import net.oauth.OAuthServiceProvider;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.opensocial.oauth.database.beans.OAuthConsumerInfo;

public interface IOAuthLogic {
	public void createAuthentication(String paramString1, String paramString2,
			String paramString3, String paramString4,
			BasicOAuthStoreConsumerKeyAndSecret.KeyType paramKeyType);

	public OAuthStore.ConsumerInfo readAuthentication(
			SecurityToken paramSecurityToken, String paramString,
			OAuthServiceProvider paramOAuthServiceProvider);

	public OAuthStore.TokenInfo createToken(SecurityToken paramSecurityToken,
			OAuthStore.ConsumerInfo paramConsumerInfo, String paramString1,
			String paramString2, OAuthStore.TokenInfo paramTokenInfo);

	public OAuthStore.TokenInfo readToken(SecurityToken paramSecurityToken,
			OAuthStore.ConsumerInfo paramConsumerInfo, String paramString1,
			String paramString2);

	public void deleteToken(SecurityToken paramSecurityToken,
			OAuthStore.ConsumerInfo paramConsumerInfo, String paramString1,
			String paramString2);

	public void createConsumer(OAuthConsumerInfo paramOAuthConsumerInfo);

	public OAuthConsumerInfo readConsumer(String paramString);

	public void deleteConsumer(String paramString);

	public List<OAuthConsumerInfo> listConsumers();

	public void createProviderToken(OAuthEntry paramOAuthEntry);

	public OAuthEntry readProviderToken(String paramString);

	public void updateProviderToken(OAuthEntry paramOAuthEntry);

	public void deleteProviderToken(String paramString);
}