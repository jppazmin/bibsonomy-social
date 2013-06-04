package org.bibsonomy.opensocial.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.auth.AuthenticationHandler;
import org.apache.shindig.auth.AuthenticationMode;
import org.apache.shindig.auth.BasicSecurityToken;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.auth.UrlParameterAuthenticationHandler;
import org.bibsonomy.model.User;
import org.bibsonomy.util.spring.security.AuthenticationUtils;

import com.google.inject.Inject;

public class SpringSecurityAuthenticationHandler extends
		UrlParameterAuthenticationHandler {
	private static final Log log = LogFactory
			.getLog(SpringSecurityAuthenticationHandler.class);

	@Inject
	public SpringSecurityAuthenticationHandler(
			SecurityTokenCodec securityTokenCodec) {
		super(securityTokenCodec);
	}

	public String getName() {
		return AuthenticationMode.COOKIE.name();
	}

	public SecurityToken getSecurityTokenFromRequest(HttpServletRequest request)
			throws AuthenticationHandler.InvalidAuthenticationException {
		Map parameters = getMappedParameters(request);
		try {
			if (parameters.get("token") == null) {
				return null;
			}

			User loginUser = AuthenticationUtils.getUser();
			String viewer = loginUser.getName();

			SecurityToken st = getSecurityTokenCodec().createToken(parameters);
			return new BasicSecurityToken(st.getOwnerId(), viewer,
					st.getAppId(), st.getDomain(), st.getAppUrl(),
					Long.toString(st.getModuleId()), st.getContainer(),
					st.getActiveUrl(), st.getExpiresAt());
		} catch (Exception e) {
			throw new AuthenticationHandler.InvalidAuthenticationException(
					"Malformed security token "
							+ (String) parameters.get("token"), e);
		}
	}
}