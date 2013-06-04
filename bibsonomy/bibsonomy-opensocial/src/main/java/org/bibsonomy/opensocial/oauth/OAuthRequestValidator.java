package org.bibsonomy.opensocial.oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.CharsetUtil;
import org.apache.shindig.social.core.oauth.OAuthSecurityToken;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.util.ValidationUtils;

public class OAuthRequestValidator {

	private static final Log log = LogFactory
			.getLog(OAuthRequestValidator.class);
	public static final String REQUESTOR_ID_PARAM = "xoauth_requestor_id";
	public static final String STASHED_BODY = "STASHED_BODY";
	private static OAuthRequestValidator INSTANCE = null;
	private OAuthDataStore store;

	private OAuthRequestValidator() {
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OAuthRequestValidator();
		}
	}

	public static OAuthRequestValidator getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	public SecurityToken getSecurityTokenFromRequest(HttpServletRequest request) {
		OAuthMessage message = OAuthServlet.getMessage(request, null);
		if (!ValidationUtils.present(getParameter(message, "oauth_signature"))) {
			return null;
		}

		String bodyHash = getParameter(message, "oauth_body_hash");
		if (ValidationUtils.present(bodyHash))
			;
		try {
			return verifyMessage(message);
		} catch (OAuthProblemException oauthException) {
			throw new RuntimeException("OAuth Authentication Failure",
					oauthException);
		}
	}

	protected SecurityToken verifyMessage(OAuthMessage message)
			throws OAuthProblemException {
		OAuthEntry entry = getOAuthEntry(message);
		OAuthConsumer authConsumer = getConsumer(message);

		OAuthAccessor accessor = new OAuthAccessor(authConsumer);

		if (ValidationUtils.present(entry)) {
			accessor.tokenSecret = entry.getTokenSecret();
			accessor.accessToken = entry.getToken();
		}
		try {
			OAuthValidator validator = new SimpleOAuthValidator();
			validator.validateMessage(message, accessor);
		} catch (OAuthProblemException e) {
			throw e;
		} catch (OAuthException e) {
			throw createOAuthProblemException(e);
		} catch (IOException e) {
			throw createOAuthProblemException(e);
		} catch (URISyntaxException e) {
			throw createOAuthProblemException(e);
		}
		return getTokenFromVerifiedRequest(message, entry, authConsumer);
	}

	private OAuthProblemException createOAuthProblemException(Exception e) {
		OAuthProblemException ope = new OAuthProblemException(
				"signature_invalid");
		ope.setParameter("oauth_problem_advice", e.getMessage());
		return ope;
	}

	public static void verifyBodyHash(HttpServletRequest request,
			String oauthBodyHash) throws AccessDeniedException {
		if ((request.getContentType() != null)
				&& (request.getContentType()
						.contains("application/x-www-form-urlencoded"))) {
			throw new AccessDeniedException(
					"Cannot use oauth_body_hash with a Content-Type of application/x-www-form-urlencoded");
		}
		try {
			byte[] rawBody = readBody(request);
			byte[] received = Base64.decodeBase64(CharsetUtil
					.getUtf8Bytes(oauthBodyHash));
			byte[] expected = DigestUtils.sha(rawBody);
			if (!Arrays.equals(received, expected))
				throw new AccessDeniedException(
						"oauth_body_hash failed verification");
		} catch (IOException ioe) {
			throw new AccessDeniedException(
					"Unable to read content body for oauth_body_hash verification");
		}
	}

	protected SecurityToken getTokenFromVerifiedRequest(OAuthMessage message,
			OAuthEntry entry, OAuthConsumer authConsumer)
			throws OAuthProblemException {
		if (entry != null) {
			return new OAuthSecurityToken(entry.getUserId(),
					entry.getCallbackUrl(), entry.getAppId(),
					entry.getDomain(), entry.getContainer(), Long.valueOf(entry
							.expiresAt().getTime()));
		}

		String userId = getParameter(message, "xoauth_requestor_id");
		return this.store.getSecurityTokenForConsumerRequest(
				authConsumer.consumerKey, userId);
	}

	protected OAuthEntry getOAuthEntry(OAuthMessage message)
			throws OAuthProblemException {
		OAuthEntry entry = null;
		String token = getParameter(message, "oauth_token");
		if (ValidationUtils.present(token)) {
			entry = this.store.getEntry(token);
			if (entry == null) {
				OAuthProblemException e = new OAuthProblemException(
						"token_rejected");
				e.setParameter("oauth_problem_advice", "cannot find token");
				throw e;
			}
			if (entry.getType() != OAuthEntry.Type.ACCESS) {
				OAuthProblemException e = new OAuthProblemException(
						"token_rejected");
				e.setParameter("oauth_problem_advice",
						"token is not an access token");
				throw e;
			}
			if (entry.isExpired()) {
				throw new OAuthProblemException("token_expired");
			}
		}
		return entry;
	}

	protected OAuthConsumer getConsumer(OAuthMessage message)
			throws OAuthProblemException {
		String consumerKey = getParameter(message, "oauth_consumer_key");
		OAuthConsumer authConsumer = this.store.getConsumer(consumerKey);
		if (!ValidationUtils.present(authConsumer)) {
			throw new OAuthProblemException("consumer_key_unknown");
		}
		return authConsumer;
	}

	public static String getParameter(OAuthMessage requestMessage, String key) {
		try {
			String str = requestMessage.getParameter(key);
			return str == null ? null : str.trim();
		} catch (IOException e) {
		}
		return null;
	}

	public static byte[] readBody(HttpServletRequest request)
			throws IOException {
		if (ValidationUtils.present(request.getAttribute("STASHED_BODY"))) {
			return (byte[]) request.getAttribute("STASHED_BODY");
		}
		byte[] rawBody = IOUtils.toByteArray(request.getInputStream());
		request.setAttribute("STASHED_BODY", rawBody);
		return rawBody;
	}

	public byte[] getUtf8Bytes(String s) {
		byte[] bb = ArrayUtils.EMPTY_BYTE_ARRAY;
		if (ValidationUtils.present(s)) {
			try {
				bb = s.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("Unsupported encoding", e);
			}
		}

		return bb;
	}

	public void setStore(OAuthDataStore store) {
		this.store = store;
	}

	public final class OAuthConstants {
		public static final String OAUTH_SESSION_HANDLE = "oauth_session_handle";
		public static final String OAUTH_EXPIRES_IN = "oauth_expires_in";
		public static final String OAUTH_BODY_HASH = "oauth_body_hash";
		public static final String PROBLEM_ACCESS_TOKEN_EXPIRED = "access_token_expired";
		public static final String PROBLEM_PARAMETER_MISSING = "parameter_missing";
		public static final String PROBLEM_TOKEN_INVALID = "token_invalid";
		public static final String PROBLEM_BAD_VERIFIER = "bad_verifier";

		private OAuthConstants() {
		}
	}
}