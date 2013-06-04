package org.bibsonomy.opensocial.shiro;

import com.google.inject.AbstractModule;

public class ShiroGuiceModule extends AbstractModule {
	protected void configure() {
		requestStaticInjection(new Class[] { SampleShiroRealm.class });
	}
}