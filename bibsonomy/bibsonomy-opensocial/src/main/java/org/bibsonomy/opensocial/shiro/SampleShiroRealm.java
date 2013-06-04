package org.bibsonomy.opensocial.shiro;

import java.util.Set;

import org.apache.shindig.social.sample.spi.JsonDbOpensocialService;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

public class SampleShiroRealm extends AuthorizingRealm {

	@Inject
	private static JsonDbOpensocialService jsonDbService;

	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		if (username == null) {
			throw new AccountException(
					"Null usernames are not allowed by this realm.");
		}
		String password = jsonDbService.getPassword(username);

		return new SimpleAuthenticationInfo(username, password, getName());
	}

	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException(
					"PrincipalCollection method argument cannot be null.");
		}

		String username = (String) principals.fromRealm(getName()).iterator()
				.next();
		Set roleNames;
		if (username == null)
			roleNames = ImmutableSet.of();
		else {
			roleNames = ImmutableSet.of("foo", "goo");
		}

		return new SimpleAuthorizationInfo(roleNames);
	}
}