package org.bibsonomy.opensocial.security;

import java.util.List;

import org.apache.shindig.auth.AnonymousAuthenticationHandler;
import org.apache.shindig.auth.AuthenticationHandler;
import org.apache.shindig.auth.UrlParameterAuthenticationHandler;
import org.apache.shindig.social.core.oauth.OAuthAuthenticationHandler;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BibSonomyAuthenticationHandlerProvider implements
		Provider<List<AuthenticationHandler>> {
	protected List<AuthenticationHandler> handlers;

	@Inject
	public BibSonomyAuthenticationHandlerProvider(
			SpringSecurityAuthenticationHandler springSecurityHandler,
			UrlParameterAuthenticationHandler urlParam,
			OAuthAuthenticationHandler threeLeggedOAuth,
			AnonymousAuthenticationHandler anonymous) {
		this.handlers = Lists.newArrayList(new AuthenticationHandler[] {
				springSecurityHandler, urlParam, threeLeggedOAuth, anonymous });
	}

	public List<AuthenticationHandler> get() {
		return this.handlers;
	}
}