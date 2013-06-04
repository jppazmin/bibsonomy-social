package org.apache.shindig.auth;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.crypto.BasicBlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypterException;
import org.apache.shindig.config.ContainerConfig;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;

@Singleton
public class BibSonomyBlobCrypterSecurityTokenCodec implements
		SecurityTokenCodec {
	public static final String SECURITY_TOKEN_KEY_STRING = "gadgets.securityTokenKeyString";
	public static final String SIGNED_FETCH_DOMAIN = "gadgets.signedFetchDomain";
	protected final Map<String, BlobCrypter> crypters = Maps.newHashMap();

	protected final Map<String, String> domains = Maps.newHashMap();

	public BibSonomyBlobCrypterSecurityTokenCodec(ContainerConfig config) {
		for (String container : config.getContainers()) {
			String keyString = config.getString(container,
					"gadgets.securityTokenKeyString");
			if (keyString != null) {
				BlobCrypter crypter = new BasicBlobCrypter(keyString.getBytes());
				this.crypters.put(container, crypter);
			}
			String domain = config.getString(container,
					"gadgets.signedFetchDomain");
			this.domains.put(container, domain);
		}
	}

	public SecurityToken createToken(Map<String, String> tokenParameters)
			throws SecurityTokenException {
		String token = (String) tokenParameters.get("token");
		if (StringUtils.isBlank(token)) {
			return new AnonymousSecurityToken();
		}
		String[] fields = StringUtils.split(token, ':');
		if (fields.length != 2) {
			throw new SecurityTokenException("Invalid security token " + token);
		}
		String container = fields[0];
		BlobCrypter crypter = (BlobCrypter) this.crypters.get(container);
		if (crypter == null) {
			throw new SecurityTokenException("Unknown container " + token);
		}
		String domain = (String) this.domains.get(container);
		String activeUrl = (String) tokenParameters.get("activeUrl");
		String crypted = fields[1];
		try {
			return BlobCrypterSecurityToken.decrypt(crypter, container, domain,
					crypted, activeUrl);
		} catch (BlobCrypterException e) {
			throw new SecurityTokenException(e);
		}
	}

	public String encodeToken(SecurityToken token)
			throws SecurityTokenException {
		if (!(token instanceof BlobCrypterSecurityToken)) {
			throw new SecurityTokenException(
					"Can only encode BlogCrypterSecurityTokens");
		}

		BlobCrypterSecurityToken t = (BlobCrypterSecurityToken) token;
		try {
			return t.encrypt();
		} catch (BlobCrypterException e) {
			throw new SecurityTokenException(e);
		}
	}
}