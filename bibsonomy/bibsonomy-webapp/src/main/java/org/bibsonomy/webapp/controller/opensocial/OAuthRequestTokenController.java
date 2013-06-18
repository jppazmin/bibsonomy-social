/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.controller.opensocial;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuth.Parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.opensocial.OAuthCommand;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * This controller implements the OAuth endpoints described in RFC 5849, section 2:
 * 
 * Temporary Credential Request ("requestToken")
 *        The endpoint used by the client to obtain a set of temporary
 *        credentials as described in Section 2.1.
 *
 * 
 * @author fei
 * @version $Id: OAuthRequestTokenController.java,v 1.2 2011-05-11 13:45:01 folke Exp $
 */
public class OAuthRequestTokenController extends OAuthProtocolController {
	private static final Log log = LogFactory.getLog(OAuthRequestTokenController.class);
	
	//------------------------------------------------------------------------
	// OAuthProtocolController interface
	//------------------------------------------------------------------------
	@Override
	protected View doWorkOn(OAuthCommand command, User loginUser) throws IOException, OAuthException, URISyntaxException {
		return createRequestToken(command, loginUser);
	}

	//------------------------------------------------------------------------
	// OAuth protocol end point implementation
	//------------------------------------------------------------------------
	/**
	 * The request token (called "temporary credentials" in RFC 5849) is used for identifying
	 * a continuing authorization process and is transformed to a token credential after a successful
	 * authorization by the resource owner
	 *  
	 * @param command
	 * @param loginUser
	 * 
	 * @return temporary credentials for obtaining token credentials after authorization
	 * 
	 * @throws IOException
	 * @throws OAuthException
	 * @throws URISyntaxException
	 * @throws OAuthException 
	 */
	private View createRequestToken(OAuthCommand command, User loginUser) throws IOException, URISyntaxException, OAuthException {
		// extract the OAuth parameters from the request
		OAuthMessage requestMessage = this.requestLogic.getOAuthMessage(null);

		// get the mandatory consumer key which identifies the requesting client 
		String consumerKey = requestMessage.getConsumerKey();

		if (consumerKey == null) {
			OAuthProblemException e = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
			e.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_CONSUMER_KEY);
			throw e;
		}
		
		// check and retrieve the shared secret for the requesting client
		OAuthConsumer consumer = this.getDataStore().getConsumer(consumerKey);

		if (!present(consumer)) {
			throw new OAuthProblemException(OAuth.Problems.CONSUMER_KEY_UNKNOWN);
		}
		
		// validate the OAuth request (i.e. verify the request's signature)
		OAuthAccessor accessor = new OAuthAccessor(consumer);
		VALIDATOR.validateMessage(requestMessage, accessor);

		// Get the client's callback URL (RFC 5849, Section 2.1)
		// If the client is unable to receive callbacks or a
        // callback URI has been established via other means,
        // the parameter value MUST be set to "oob"
		String callback = requestMessage.getParameter(OAuth.OAUTH_CALLBACK);
		if (!present(callback)) {
			// see if the consumer has a callback
			callback = consumer.callbackURL;
		}
		if (!present(callback)) {
			callback = OUT_OF_BAND;
		}

		// generate request_token and secret
		OAuthEntry entry = getDataStore().generateRequestToken(consumerKey, requestMessage.getParameter(OAuth.OAUTH_VERSION), callback);
		
		List<Parameter> responseParams = OAuth.newList(OAuth.OAUTH_TOKEN, entry.getToken(), OAuth.OAUTH_TOKEN_SECRET, entry.getTokenSecret());
		if (present(callback)) {
			responseParams.add(new Parameter(OAuth.OAUTH_CALLBACK_CONFIRMED, "true"));
		}

		// return the temporary request token
		command.setResponseString(OAuth.formEncode(responseParams));
		return Views.OAUTH_RESPONSE;
	}

	@Override
	protected String getRequestAction() {
		return OAuthAction.requestToken.name();
	}

}
