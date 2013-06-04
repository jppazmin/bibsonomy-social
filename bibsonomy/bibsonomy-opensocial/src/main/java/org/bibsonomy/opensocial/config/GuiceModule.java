package org.bibsonomy.opensocial.config;

import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.AlbumService;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.MediaItemService;
import org.apache.shindig.social.opensocial.spi.MessageService;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.sample.spi.JsonDbOpensocialService;
import org.bibsonomy.opensocial.oauth.database.BibSonomyOAuthDataStore;
import org.bibsonomy.opensocial.security.BibSonomySecurityTokenCodec;
import org.springframework.context.ApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class GuiceModule extends AbstractModule {
	private static final String PERSON_SPI_BEAN_NAME = "personSpi";
	private static final String OAUTH_DATA_STORE_BEAN_NAME = "oAuthDataStore";

	protected void configure() {
		ApplicationContext applicationContext = ApplicationContextFactory
				.getApplicationContext();

		bind(String.class).annotatedWith(
				Names.named("shindig.canonical.json.db")).toInstance(
				"testdb.json");

		bind(ActivityService.class).to(JsonDbOpensocialService.class);
		bind(AlbumService.class).to(JsonDbOpensocialService.class);
		bind(MediaItemService.class).to(JsonDbOpensocialService.class);
		bind(AppDataService.class).to(JsonDbOpensocialService.class);

		bind(PersonService.class).toInstance(
				(PersonService) applicationContext.getBean("personSpi"));

		bind(MessageService.class).to(JsonDbOpensocialService.class);

		bind(OAuthDataStore.class).to(BibSonomyOAuthDataStore.class);

		bind(SecurityTokenCodec.class).to(BibSonomySecurityTokenCodec.class);
	}
}