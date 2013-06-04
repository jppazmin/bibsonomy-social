package org.bibsonomy.opensocial.security;

import java.util.Map;

import org.apache.shindig.auth.BibSonomyBlobCrypterSecurityTokenCodec;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.auth.SecurityTokenException;
import org.apache.shindig.config.ContainerConfig;

import com.google.inject.Inject;

public class BibSonomySecurityTokenCodec implements SecurityTokenCodec {
	private final SecurityTokenCodec codec;

	@Inject
	public BibSonomySecurityTokenCodec(ContainerConfig config) {
		this.codec = new BibSonomyBlobCrypterSecurityTokenCodec(config);
	}

	public SecurityToken createToken(Map<String, String> tokenParameters)
			throws SecurityTokenException {
		return this.codec.createToken(tokenParameters);
	}

	public String encodeToken(SecurityToken token)
			throws SecurityTokenException {
		if (token == null) {
			return null;
		}
		return this.codec.encodeToken(token);
	}
}