package org.bibsonomy.opensocial.oauth;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.crypto.BasicBlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.crypto.Crypto;
import org.apache.shindig.common.util.ResourceLoader;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.oauth.BasicOAuthStore;
import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;
import org.apache.shindig.gadgets.oauth.OAuthFetcherConfig;
import org.apache.shindig.gadgets.oauth.OAuthRequest;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.bibsonomy.opensocial.oauth.database.BibSonomyOAuthStore;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class OAuthModule extends AbstractModule {
	private static final Logger LOG = Logger.getLogger(OAuthModule.class
			.getName());
	private static final String OAUTH_CONFIG = "oauthconfig.json";
	private static final String OAUTH_SIGNING_KEY_FILE = "shindig.signing.key-file";
	private static final String OAUTH_SIGNING_KEY_NAME = "shindig.signing.key-name";
	private static final String OAUTH_CALLBACK_URL = "shindig.signing.global-callback-url";

	protected void configure() {
		bind(BlobCrypter.class).annotatedWith(
				Names.named("shindig.oauth.state-crypter")).toProvider(
				OAuthCrypterProvider.class);

		bind(OAuthStore.class).toProvider(OAuthStoreProvider.class);
		bind(OAuthRequest.class).toProvider(OAuthRequestProvider.class);
	}

	@Singleton
	public static class OAuthStoreProvider implements Provider<OAuthStore> {
		private final BibSonomyOAuthStore store;

		@Inject
		public OAuthStoreProvider(
				@Named("shindig.signing.key-file") String signingKeyFile,
				@Named("shindig.signing.key-name") String signingKeyName,
				@Named("shindig.signing.global-callback-url") String defaultCallbackUrl) {
			this.store = new BibSonomyOAuthStore();

			loadDefaultKey(signingKeyFile, signingKeyName);
			this.store.setDefaultCallbackUrl(defaultCallbackUrl);
			loadConsumers();
		}

		private void loadDefaultKey(String signingKeyFile, String signingKeyName) {
			BasicOAuthStoreConsumerKeyAndSecret key = null;
			if (!StringUtils.isBlank(signingKeyFile)) {
				try {
					OAuthModule.LOG.info("Loading OAuth signing key from "
							+ signingKeyFile);
					String privateKey = IOUtils.toString(
							ResourceLoader.open(signingKeyFile), "UTF-8");
					privateKey = BasicOAuthStore.convertFromOpenSsl(privateKey);
					key = new BasicOAuthStoreConsumerKeyAndSecret(
							null,
							privateKey,
							BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE,
							signingKeyName, null);
				} catch (Throwable t) {
					OAuthModule.LOG.log(Level.WARNING,
							"Couldn't load key file " + signingKeyFile, t);
				}
			}
			if (key != null)
				this.store.setDefaultKey(key);
			else
				OAuthModule.LOG
						.log(Level.WARNING,
								"Couldn't load OAuth signing key.  To create a key, run:\n  openssl req -newkey rsa:1024 -days 365 -nodes -x509 -keyout testkey.pem \\\n     -out testkey.pem -subj '/CN=mytestkey'\n  openssl pkcs8 -in testkey.pem -out oauthkey.pem -topk8 -nocrypt -outform PEM\n\nThen edit shindig.properties and add these lines:\nshindig.signing.key-file=<path-to-oauthkey.pem>\nshindig.signing.key-name=mykey\n");
		}

		private void loadConsumers() {
			try {
				String oauthConfigString = ResourceLoader
						.getContent("oauthconfig.json");
			} catch (Throwable t) {
				OAuthModule.LOG
						.log(Level.WARNING,
								"Failed to initialize OAuth consumers from oauthconfig.json",
								t);
			}
		}

		public OAuthStore get() {
			return this.store;
		}
	}

	public static class OAuthRequestProvider implements Provider<OAuthRequest> {
		private final HttpFetcher fetcher;
		private final OAuthFetcherConfig config;

		@Inject
		public OAuthRequestProvider(HttpFetcher fetcher,
				OAuthFetcherConfig config) {
			this.fetcher = fetcher;
			this.config = config;
		}

		public OAuthRequest get() {
			return new OAuthRequest(this.config, this.fetcher);
		}
	}

	@Singleton
	public static class OAuthCrypterProvider implements Provider<BlobCrypter> {
		private final BlobCrypter crypter;

		@Inject
		public OAuthCrypterProvider(
				@Named("shindig.signing.state-key") String stateCrypterPath)
				throws IOException {
			if (StringUtils.isBlank(stateCrypterPath)) {
				OAuthModule.LOG
						.info("Using random key for OAuth client-side state encryption");
				this.crypter = new BasicBlobCrypter(Crypto.getRandomBytes(16));
			} else {
				OAuthModule.LOG.info("Using file " + stateCrypterPath
						+ " for OAuth client-side state encryption");
				this.crypter = new BasicBlobCrypter(new File(stateCrypterPath));
			}
		}

		public BlobCrypter get() {
			return this.crypter;
		}
	}
}