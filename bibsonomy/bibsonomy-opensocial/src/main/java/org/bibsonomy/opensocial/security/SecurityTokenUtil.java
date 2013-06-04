package org.bibsonomy.opensocial.security;

import org.apache.shindig.auth.BlobCrypterSecurityToken;
import org.apache.shindig.common.crypto.BasicBlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypterException;
import org.apache.shindig.common.util.Utf8UrlCoder;
import org.bibsonomy.model.User;

public class SecurityTokenUtil {
	public static final String SECURITY_TOKEN_KEY_STRING = "gadgets.securityTokenKeyString";
	private static final String INSECURE_KEY = "Th!s Iz a kEy which is long EnOUgh";
	private static final String container = "default";
	private static final String domain = null;

	private static final BlobCrypter crypter = new BasicBlobCrypter(
			"Th!s Iz a kEy which is long EnOUgh".getBytes());

	public static String getSecurityToken(User loginUser, String gadgetUrl)
			throws BlobCrypterException {
		BlobCrypterSecurityToken st = new BlobCrypterSecurityToken(crypter,
				"default", domain);
		st.setViewerId(loginUser.getName());
		st.setOwnerId(loginUser.getName());
		st.setAppUrl(gadgetUrl);
		return Utf8UrlCoder.encode(st.encrypt());
	}
}