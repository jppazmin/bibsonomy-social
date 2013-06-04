package org.bibsonomy.opensocial.oauth.database.beans;

import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;

public class OAuthConsumerInfo {
	private String consumerKey;
	private String consumerSecret;
	private BasicOAuthStoreConsumerKeyAndSecret.KeyType keyType;
	private String keyName;
	private String callbackUrl;
	private String gadgetUrl;
	private long moduleId;
	private String title;
	private String summary;
	private String description;
	private String thumbnail;
	private String icon;
	private String serviceName;

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerKey() {
		return this.consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getConsumerSecret() {
		return this.consumerSecret;
	}

	public void setKeyType(BasicOAuthStoreConsumerKeyAndSecret.KeyType keyType) {
		this.keyType = keyType;
	}

	public BasicOAuthStoreConsumerKeyAndSecret.KeyType getKeyType() {
		return this.keyType;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setGadgetUrl(String gadgetUrl) {
		this.gadgetUrl = gadgetUrl;
	}

	public String getGadgetUrl() {
		return this.gadgetUrl;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	public long getModuleId() {
		return this.moduleId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return this.icon;
	}
}