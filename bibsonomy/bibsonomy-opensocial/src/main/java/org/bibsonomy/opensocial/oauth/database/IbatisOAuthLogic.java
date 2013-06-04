package org.bibsonomy.opensocial.oauth.database;

import java.io.Reader;
import java.sql.SQLException;
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
import org.bibsonomy.opensocial.oauth.database.beans.OAuthTokenIndex;
import org.bibsonomy.opensocial.oauth.database.beans.OAuthTokenInfo;
import org.bibsonomy.util.ValidationUtils;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class IbatisOAuthLogic implements IOAuthLogic {
	private static final Log log = LogFactory.getLog(IbatisOAuthLogic.class);
	private final SqlMapClient sqlMap;
	private String defaultCallbackUrl;
	private static IOAuthLogic instance = null;

	private IbatisOAuthLogic() {
		try {
			String resource = "SqlMapConfig_OpenSocial.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			this.sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			log.info("OpenSocial database connection initialized.");
		} catch (Exception e) {
			throw new RuntimeException(
					"Error initializing DBAccess class. Cause: " + e);
		}
	}

	public static IOAuthLogic getInstance() {
		if (instance == null)
			instance = new IbatisOAuthLogic();
		return instance;
	}

	public void createAuthentication(String gadgetUrl, String server,
			String consumerKey, String consumerSecret,
			BasicOAuthStoreConsumerKeyAndSecret.KeyType keyType) {
		throw new RuntimeException("METHOD NOT IMPLEMENTED");
	}

	public OAuthStore.ConsumerInfo readAuthentication(
			SecurityToken securityToken, String serviceName,
			OAuthServiceProvider provider) {
		OAuthConsumerInfo consumerParam = makeConsumerInfo(securityToken,
				serviceName);
		OAuthConsumerInfo consumerInfo = null;
		try {
			consumerInfo = (OAuthConsumerInfo) this.sqlMap.queryForObject(
					"getAuthentication", consumerParam);
		} catch (SQLException e) {
			log.error("No consumer information found for '"
					+ securityToken.getActiveUrl() + "' on '" + serviceName
					+ "'");
		}
		if (!ValidationUtils.present(consumerInfo)) {
			throw new RuntimeException("No key for gadget "
					+ securityToken.getAppUrl() + " and service " + serviceName);
		}

		BasicOAuthStoreConsumerKeyAndSecret cks = new BasicOAuthStoreConsumerKeyAndSecret(
				consumerInfo.getConsumerKey(),
				consumerInfo.getConsumerSecret(), consumerInfo.getKeyType(),
				consumerInfo.getKeyName(), consumerInfo.getCallbackUrl());

		OAuthConsumer consumer = null;
		if (cks.getKeyType() == BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE) {
			consumer = new OAuthConsumer(null, cks.getConsumerKey(), null,
					provider);

			consumer.setProperty("oauth_signature_method", "RSA-SHA1");
			consumer.setProperty("RSA-SHA1.PrivateKey", cks.getConsumerSecret());
		} else {
			consumer = new OAuthConsumer(null, cks.getConsumerKey(),
					cks.getConsumerSecret(), provider);
			consumer.setProperty("oauth_signature_method", "HMAC-SHA1");
		}
		String callback = cks.getCallbackUrl() != null ? cks.getCallbackUrl()
				: getDefaultCallbackUrl();
		return new OAuthStore.ConsumerInfo(consumer, cks.getKeyName(), callback);
	}

	public OAuthStore.TokenInfo createToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName, OAuthStore.TokenInfo tokenInfo) {
		OAuthTokenIndex tokenIndex = makeTokenIndex(securityToken, serviceName);
		tokenIndex.setTokenSecret(tokenInfo.getTokenSecret());
		tokenIndex.setTokenExpireMillis(tokenInfo.getTokenExpireMillis());
		tokenIndex.setAccessToken(tokenInfo.getAccessToken());
		tokenIndex.setSessionHandle(tokenInfo.getSessionHandle());
		try {
			this.sqlMap.insert("setToken", tokenIndex);
		} catch (SQLException e) {
			log.error("Error setting token for viewer '"
					+ tokenIndex.getUserId() + "' on gadget '"
					+ tokenIndex.getGadgetUri() + "'");
		}

		return tokenInfo;
	}

	public void deleteToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName) {
		OAuthTokenIndex tokenIndex = makeTokenIndex(securityToken, serviceName);
		try {
			this.sqlMap.delete("removeToken", tokenIndex);
		} catch (SQLException e) {
			log.error("Error removing token for viewer '"
					+ tokenIndex.getUserId() + "' on gadget '"
					+ tokenIndex.getGadgetUri() + "'");
		}
	}

	public OAuthStore.TokenInfo readToken(SecurityToken securityToken,
			OAuthStore.ConsumerInfo consumerInfo, String serviceName,
			String tokenName) {
		OAuthTokenIndex tokenIndex = makeTokenIndex(securityToken, serviceName);

		OAuthTokenInfo tokenInfo = null;
		try {
			tokenInfo = (OAuthTokenInfo) this.sqlMap.queryForObject("getToken",
					tokenIndex);
		} catch (SQLException e) {
			log.error(
					"Error fetching token for viewer '"
							+ tokenIndex.getUserId() + "' on gadget '"
							+ tokenIndex.getGadgetUri() + "'", e);
		}

		OAuthStore.TokenInfo retVal = null;
		if (ValidationUtils.present(tokenInfo)) {
			retVal = new OAuthStore.TokenInfo(tokenInfo.getAccessToken(),
					tokenInfo.getTokenSecret(), tokenInfo.getSessionHandle(),
					tokenInfo.getTokenExpireMillis());
		}
		return retVal;
	}

	private OAuthTokenIndex makeTokenIndex(SecurityToken securityToken,
			String serviceName) {
		OAuthTokenIndex tokenIndex = new OAuthTokenIndex();
		tokenIndex.setGadgetUri(securityToken.getAppUrl());
		tokenIndex.setServiceName(serviceName);
		tokenIndex.setModuleId(securityToken.getModuleId());
		tokenIndex.setUserId(securityToken.getViewerId());
		return tokenIndex;
	}

	public void createProviderToken(OAuthEntry entry) {
		try {
			this.sqlMap.insert("setProviderToken", entry);
		} catch (SQLException e) {
			log.error("Error creating provider token for '" + entry.getAppId()
					+ "'", e);
		}
	}

	public void createConsumer(OAuthConsumerInfo consumerInfo) {
		try {
			if (ValidationUtils.present(consumerInfo.getKeyName())) {
				consumerInfo
						.setKeyType(BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE);
			}

			if ((!ValidationUtils.present(consumerInfo.getKeyName()))
					&& (BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE
							.equals(consumerInfo.getKeyType()))) {
				consumerInfo.setKeyName("RSA-SHA1.PublicKey");
			}
			this.sqlMap.insert("setConsumerInfo", consumerInfo);
		} catch (SQLException e) {
			throw new RuntimeException("Error creating consumer info", e);
		}
	}

	public OAuthConsumerInfo readConsumer(String consumerKey) {
		OAuthConsumerInfo consumerInfo = null;
		try {
			consumerInfo = (OAuthConsumerInfo) this.sqlMap.queryForObject(
					"getConsumerInfo", consumerKey);
		} catch (SQLException e) {
			log.error("Error fetching consumer info for consumer key '"
					+ consumerKey + "'", e);
		}

		return consumerInfo;
	}

	public void deleteConsumer(String consumerKey) {
		try {
			this.sqlMap.delete("removeConsumerInfo", consumerKey);
		} catch (SQLException e) {
			log.error("Error removing consumerInfo for consumerKey '"
					+ consumerKey + "'", e);
		}
	}

	public List<OAuthConsumerInfo> listConsumers() {
		List consumerInfo = null;
		try {
			consumerInfo = this.sqlMap.queryForList("listConsumerInfo");
		} catch (SQLException e) {
			log.error("Error listing consumer info", e);
		}

		return consumerInfo;
	}

	public OAuthEntry readProviderToken(String oauthToken) {
		OAuthEntry entry = null;
		try {
			entry = (OAuthEntry) this.sqlMap.queryForObject("getProviderToken",
					oauthToken);
		} catch (SQLException e) {
			log.error("Error retrieving token details for token '" + oauthToken
					+ "'", e);
		}
		return entry;
	}

	public void updateProviderToken(OAuthEntry entry) {
		try {
			this.sqlMap.insert("updateProviderToken", entry);
		} catch (SQLException e) {
			log.error("Error updating provider token for '" + entry.getAppId()
					+ "'", e);
		}
	}

	public void deleteProviderToken(String token) {
		try {
			this.sqlMap.delete("removeProviderToken", token);
		} catch (SQLException e) {
			log.error("Error removing token '" + token + "'", e);
		}
	}

	private OAuthConsumerInfo makeConsumerInfo(SecurityToken securityToken,
			String serviceName) {
		OAuthConsumerInfo consumerInfo = new OAuthConsumerInfo();
		consumerInfo.setGadgetUrl(securityToken.getAppUrl());
		consumerInfo.setServiceName(serviceName);
		return consumerInfo;
	}

	public void setDefaultCallbackUrl(String defaultCallbackUrl) {
		this.defaultCallbackUrl = defaultCallbackUrl;
	}

	public String getDefaultCallbackUrl() {
		return this.defaultCallbackUrl;
	}
}