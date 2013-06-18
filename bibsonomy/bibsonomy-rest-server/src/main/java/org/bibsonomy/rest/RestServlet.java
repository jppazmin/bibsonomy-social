/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.auth.SecurityToken;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.database.DBLogicNoAuthInterfaceFactory;
import org.bibsonomy.database.ShindigDBLogicUserInterfaceFactory;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.database.util.IbatisDBSessionFactory;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.logic.LogicInterfaceFactory;
import org.bibsonomy.opensocial.oauth.OAuthRequestValidator;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.AuthenticationException;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.renderer.Renderer;
import org.bibsonomy.rest.renderer.RendererFactory;
import org.bibsonomy.rest.renderer.RenderingFormat;
import org.bibsonomy.rest.renderer.UrlRenderer;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.rest.util.MultiPartRequestParser;
import org.bibsonomy.rest.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @author Christian Kramer
 * @version $Id: RestServlet.java,v 1.69 2011-06-16 10:55:26 nosebrain Exp $
 */
public final class RestServlet extends HttpServlet {
	private static final long serialVersionUID = -1737804091652029470L;
	private static final Log log = LogFactory.getLog(RestServlet.class);

	private static final String NO_AUTH_ERROR = "Please authenticate yourself.";
	/**
	 * Used in {@link #validateAuthorization(String)} to identify HTTP basic
	 * authentication.
	 */
	private static final String HTTP_AUTH_BASIC_IDENTIFIER = "Basic ";

	/**
	 * the key for the documents path
	 */
	public static final String DOCUMENTS_PATH_KEY = "docPath";

	/**
	 * the key for the project home
	 */
	public static final String PROJECT_HOME_KEY = "projectHome";

	/**
	 * the response encoding used to encode HTTP responses.
	 */
	public static final String RESPONSE_ENCODING = "UTF-8";

	/**
	 * the request default encoding
	 */
	public static final String REQUEST_ENCODING = "UTF-8";

	private LogicInterfaceFactory logicFactory;

	private UrlRenderer urlRenderer;
	private RendererFactory rendererFactory;

	// store some infos about the specific request or the webservice (i.e.
	// document path)
	private final Map<String, String> additionalInfos = new HashMap<String, String>();

	/** handles OAuth requests */
	private OAuthRequestValidator oauthValidator;

	/** logic interface factory for handling oauth requests */
	private ShindigDBLogicUserInterfaceFactory oauthLogicFactory;

	/**
	 * Sets the base URL of the project. Typically "project.home" in the file
	 * <tt>project.properties</tt>.
	 * 
	 * @param projectHome
	 */
	@Required
	public void setProjectHome(final String projectHome) {
		additionalInfos.put(PROJECT_HOME_KEY, projectHome);
	}

	/**
	 * Renders the URLs returned by the servlet, e.g., in the XML.
	 * 
	 * @param urlRenderer
	 */
	@Required
	public void setUrlRenderer(final UrlRenderer urlRenderer) {
		this.urlRenderer = urlRenderer;
		this.rendererFactory = new RendererFactory(urlRenderer);
	}

	/**
	 * Sets the base path to the documents.
	 * 
	 * @param documentPath
	 */
	@Required
	public void setDocumentPath(final String documentPath) {
		additionalInfos.put(DOCUMENTS_PATH_KEY, documentPath);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		// initialize oauth database layer
		try {
			// TODO: configure via spring
			final DBSessionFactory dbSessionFactory = new IbatisDBSessionFactory();
			this.oauthValidator = OAuthRequestValidator.getInstance();
			this.oauthLogicFactory = new ShindigDBLogicUserInterfaceFactory();
			this.oauthLogicFactory.setDbSessionFactory(dbSessionFactory);

			final DBLogicNoAuthInterfaceFactory noAuthFactory = new DBLogicNoAuthInterfaceFactory();
			noAuthFactory.setDbSessionFactory(dbSessionFactory);
			// this.oauthLogicFactory.setNoAuthLogicFactory(noAuthFactory);

			log.debug("Sucessfully enabled oauth database layer");
		} catch (final Error e) {
			// FIXME: IbatisDBSessionFactory doesn't have a JNDI datasource
			// during tests
			// we have to springify the rest server to cleanly handle this case
			log.error("Error initializing the oauth database layer (disabling oauth for the rest api)");
		}
	}

	/**
	 * Configure the logic interface factory to be used to aquire instances of
	 * the logic interface.
	 * 
	 * @param logicInterfaceFactory
	 */
	@Required
	public void setLogicInterfaceFactory(
			final LogicInterfaceFactory logicInterfaceFactory) {
		this.logicFactory = logicInterfaceFactory;
	}

	/**
	 * Respond to a GET request for the content produced by this servlet.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param response
	 *            The servlet response we are producing
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			ServletException {
		handle(request, response, HttpMethod.GET);
	}

	@Override
	public void doPut(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		handle(request, response, HttpMethod.PUT);
	}

	@Override
	public void doDelete(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		handle(request, response, HttpMethod.DELETE);
	}

	@Override
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		handle(request, response, HttpMethod.POST);
	}

	@Override
	public void doHead(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		validateAuthorization(request);
	}

	/**
	 * @param request
	 *            the servletrequest
	 * @param response
	 *            the servletresponse
	 * @param method
	 *            httpMethod to use, see {@link HttpMethod}
	 * @throws IOException
	 */
	private void handle(final HttpServletRequest request,
			final HttpServletResponse response, final HttpMethod method)
			throws IOException {
		log.debug("Incoming Request: " + method.name() + " "
				+ request.getRequestURL() + " from IP "
				+ request.getHeader("x-forwarded-for"));
		final long start = System.currentTimeMillis();

		try {
			// validate the requesting user's authorization
			final LogicInterface logic = validateAuthorization(request);

			// parse the request object to retrieve a list with all items of the
			// http request
			final MultiPartRequestParser parser = new MultiPartRequestParser(
					request);

			// choose rendering format (defaults to xml)
			final RenderingFormat renderingFormat = RESTUtils
					.getRenderingFormatForRequest(request.getParameterMap(),
							request.getHeader(HeaderUtils.HEADER_ACCEPT),
							request.getContentType());

			// create Context
			final Reader reader = RESTUtils.getInputReaderForStream(
					request.getInputStream(), REQUEST_ENCODING);
			final Context context = new Context(method, getPathInfo(request),
					renderingFormat, this.urlRenderer, reader,
					parser.getList(), logic, request.getParameterMap(),
					additionalInfos);

			// validate request
			context.canAccess();

			// set some response headers
			final String userAgent = request
					.getHeader(HeaderUtils.HEADER_USER_AGENT);
			log.debug("[USER-AGENT] " + userAgent);
			response.setContentType(context.getContentType(userAgent));
			response.setCharacterEncoding(RESPONSE_ENCODING);

			// send answer
			if (method.equals(HttpMethod.POST)) {
				// if a POST request completes successfully this means that a
				// resource has been created
				response.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
			}

			// just define an ByteArrayOutputStream to store all outgoing data
			final ByteArrayOutputStream cachingStream = new ByteArrayOutputStream();
			context.perform(cachingStream);

			/*
			 * XXX: note: cachingStream.size() !=
			 * cachingStream.toString().length() !! the correct value is the
			 * first one!
			 */
			response.setContentLength(cachingStream.size());

			// some more logging
			log.debug("Size of output sent:" + cachingStream.size());
			final long elapsed = System.currentTimeMillis() - start;
			log.debug("Processing time: " + elapsed + " ms");

			cachingStream.writeTo(response.getOutputStream());
		} catch (final AuthenticationException e) {
			log.warn(e.getMessage());
			response.setHeader("WWW-Authenticate", "Basic realm=\""
					+ RestProperties.getInstance().getBasicRealm() + "\"");
			sendError(request, response, HttpURLConnection.HTTP_UNAUTHORIZED,
					e.getMessage());
		} catch (final InternServerException e) {
			log.error(e.getMessage());
			sendError(request, response,
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		} catch (final NoSuchResourceException e) {
			log.error(e.getMessage());
			sendError(request, response, HttpServletResponse.SC_NOT_FOUND,
					e.getMessage());
		} catch (final BadRequestOrResponseException e) {
			log.error(e.getMessage());
			sendError(request, response, HttpServletResponse.SC_BAD_REQUEST,
					e.getMessage());
		} catch (final AccessDeniedException e) {
			log.error(e.getMessage());
			sendError(request, response, HttpServletResponse.SC_FORBIDDEN,
					e.getMessage());
		} catch (final ResourceMovedException e) {
			log.error(e.getMessage());
			/*
			 * sending new location TODO: add date using
			 */
			response.setHeader(
					"Location",
					urlRenderer.createHrefForResource(e.getUserName(),
							e.getNewIntraHash()));
			sendError(request, response,
					HttpServletResponse.SC_MOVED_PERMANENTLY, e.getMessage());
		} catch (final DatabaseException e) {
			final StringBuilder returnMessage = new StringBuilder("");
			for (final String hash : e.getErrorMessages().keySet()) {
				for (final ErrorMessage em : e.getErrorMessages(hash)) {
					log.error(em.toString());
					returnMessage.append(em.toString() + "\n ");
				}
			}
			sendError(request, response, HttpServletResponse.SC_BAD_REQUEST,
					returnMessage.toString());

		} catch (final Exception e) {
			log.error(e, e);
			// well, lets fetch each and every error...
			sendError(request, response,
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
	}

	/**
	 * Strips the API URL part from the beginning of the complete URL.
	 * 
	 * @param request
	 * @return
	 */
	private String getPathInfo(final HttpServletRequest request) {
		final String pathInfo = request.getPathInfo();
		if (present(pathInfo))
			return pathInfo;
		final StringBuffer url = request.getRequestURL();
		return url.substring(urlRenderer.getApiUrl().length()).toString();
	}

	/**
	 * Sends an error to the client.
	 * 
	 * @param request
	 *            the current {@link HttpServletRequest} object.
	 * @param response
	 *            the current {@link HttpServletResponse} object.
	 * @param code
	 *            the error code to send.
	 * @param message
	 *            the message to send.
	 * @throws IOException
	 */
	private void sendError(final HttpServletRequest request,
			final HttpServletResponse response, final int code,
			final String message) throws IOException {
		// get renderer
		final RenderingFormat mediaType = RESTUtils
				.getRenderingFormatForRequest(request.getParameterMap(),
						request.getHeader(HeaderUtils.HEADER_ACCEPT),
						request.getContentType());
		final Renderer renderer = rendererFactory.getRenderer(mediaType);

		// send error
		response.setStatus(code);
		response.setContentType(mediaType.getMimeType());
		final ByteArrayOutputStream cachingStream = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(cachingStream,
				Charset.forName(RESPONSE_ENCODING));
		renderer.serializeError(writer, message);
		response.setContentLength(cachingStream.size());
		response.getOutputStream().print(
				cachingStream.toString(RESPONSE_ENCODING));
	}

	/**
	 * @param authentication
	 *            Authentication-value of the header's request
	 * @throws IOException
	 */
	protected LogicInterface validateAuthorization(
			final HttpServletRequest request) throws AuthenticationException {
		final String authenticationHeader = request
				.getHeader(HeaderUtils.HEADER_AUTHORIZATION);
		if (HeaderUtils.isHttpBasicAuthorization(authenticationHeader)) {
			// try http basic authorization
			return validateHttpBasicAuthorization(authenticationHeader);
		} else if (present(this.oauthValidator)
				&& present(this.oauthLogicFactory)) {
			// try oauth authorization
			return validateOAuthAuthorization(request);
		}
		throw new AuthenticationException(NO_AUTH_ERROR);
	}

	/**
	 * Authorize OAuth requests
	 * 
	 * @param request
	 * @return
	 */
	private LogicInterface validateOAuthAuthorization(
			final HttpServletRequest request) {
		// try oauth authorization - if configured
		final SecurityToken securityToken = this.oauthValidator
				.getSecurityTokenFromRequest(request);
		if (!present(securityToken) || securityToken.isAnonymous()) {
			throw new AuthenticationException(NO_AUTH_ERROR);
		}
		return this.oauthLogicFactory.getLogicAccess(securityToken);
	}

	/**
	 * Authorize Http basic requests
	 * 
	 * @param authentication
	 * @return logic interface for the requesting user on success
	 */
	protected LogicInterface validateHttpBasicAuthorization(
			final String authentication) {
		if (!HeaderUtils.isHttpBasicAuthorization(authentication)) {
			throw new AuthenticationException(NO_AUTH_ERROR);
		}

		final String basicCookie;
		try {
			basicCookie = new String(
					Base64.decodeBase64(authentication.substring(
							HTTP_AUTH_BASIC_IDENTIFIER.length()).getBytes()),
					RESPONSE_ENCODING);
		} catch (final IOException e) {
			throw new BadRequestOrResponseException(
					"error decoding authorization header: " + e.toString());
		}

		final int i = basicCookie.indexOf(':');
		if (i < 0) {
			throw new BadRequestOrResponseException(
					"error decoding authorization header: syntax error");
		}

		// check username and password
		final String username = basicCookie.substring(0, i);
		final String apiKey = basicCookie.substring(i + 1);
		log.debug("Username/API-key: " + username + " / " + apiKey);
		try {
			return logicFactory.getLogicAccess(username, apiKey);
		} catch (final AccessDeniedException e) {
			throw new AuthenticationException("Authentication failure: "
					+ e.getMessage());
		}
	}
}