package org.bibsonomy.opensocial.container;

import java.util.Map;
import java.util.concurrent.Future;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;
import org.apache.shindig.social.sample.spi.JsonDbOpensocialService;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;

@Service(name = "samplecontainer", path = "/{type}/{doevil}")
public class SampleContainerHandler {
	private final JsonDbOpensocialService service;
	private final HttpFetcher fetcher;

	@Inject
	public SampleContainerHandler(JsonDbOpensocialService dbService,
			HttpFetcher fetcher) {
		this.service = dbService;
		this.fetcher = fetcher;
	}

	@Operation(httpMethods = { "PUT" })
	public Future<?> update(RequestItem request) throws ProtocolException {
		return create(request);
	}

	@Operation(httpMethods = { "POST" }, bodyParam = "data")
	public Future<?> create(RequestItem request) throws ProtocolException {
		String type = request.getParameter("type");
		if ("setstate".equals(type)) {
			try {
				Map bodyparams = (Map) request.getTypedParameter("data",
						Map.class);
				String stateFile = (String) bodyparams.get("fileurl");
				this.service
						.setDb(new JSONObject(fetchStateDocument(stateFile)));
			} catch (JSONException e) {
				throw new ProtocolException(400,
						"The json state file was not valid json", e);
			}
		} else if ("setevilness".equals(type)) {
			throw new ProtocolException(501,
					"evil data has not been implemented yet");
		}

		return ImmediateFuture.newInstance(null);
	}

	@Operation(httpMethods = { "GET" })
	public Future<?> get(RequestItem request) {
		return ImmediateFuture.newInstance(this.service.getDb());
	}

	private String fetchStateDocument(String stateFileLocation) {
		String errorMessage = "The json state file " + stateFileLocation
				+ " could not be fetched and parsed.";
		try {
			HttpResponse response = this.fetcher.fetch(new HttpRequest(Uri
					.parse(stateFileLocation)));
			if (response.getHttpStatusCode() != 200) {
				throw new RuntimeException(errorMessage);
			}
			return response.getResponseAsString();
		} catch (GadgetException e) {
			throw new RuntimeException(errorMessage, e);
		}
	}
}