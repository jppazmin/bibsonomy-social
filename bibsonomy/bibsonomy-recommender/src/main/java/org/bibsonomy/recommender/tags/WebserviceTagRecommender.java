/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.recommender.tags;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.comparators.RecommendedTagComparator;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.recommender.tags.database.IdleClosingConnectionManager;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.renderer.Renderer;
import org.bibsonomy.rest.renderer.RendererFactory;
import org.bibsonomy.rest.renderer.RenderingFormat;
import org.bibsonomy.rest.renderer.UrlRenderer;

/**
 * Class for encapsulating webservice queries to recommenders
 * @author fei
 * @version $Id: WebserviceTagRecommender.java,v 1.24 2011-06-09 12:31:46 rja Exp $
 */
public class WebserviceTagRecommender implements TagRecommenderConnector {
	final Log log = LogFactory.getLog(WebserviceTagRecommender.class);
	
	/** url map for the getRecommendation method */
	private static final String METHOD_GETRECOMMENDEDTAGS = "getRecommendedTags";
	/** url map for the setFeedback method */
	private static final String METHOD_SETFEEDBACK = "setFeedback";
	
	private static final int SOCKET_TIMEOUT_MS = 10000;
	private static final int HTTP_CONNECTION_TIMEOUT_MS = 1000;
	private static final long IDLE_TIMEOUT_MS = 3000;
	
	private final HttpClient client;
	// service's address
	private URI address;
	// serializes post
	private final Renderer renderer;
	
	// FIXME: These values are also used in TagRecommenderServlet and should
	//        be defined in a class commonly accessible
	/** post parameter for the feedback (xml-)post model */
	public final String ID_FEEDBACK = "feedback";
	/** post parameter for the recommendation (xml-)post model */
	public final String ID_RECQUERY = "data";
	/** post parameter for the post id */
	public final String ID_POSTID   = "postID";

	

	// MultiThreadedHttpConnectionManager 
	private final IdleClosingConnectionManager connectionManager;
	private final IdleConnectionTimeoutThread idleConnectionHandler;
	
	/**
	 * inits the recommender
	 */
	public WebserviceTagRecommender() {
		// Create an instance of HttpClient.
		connectionManager = new IdleClosingConnectionManager();// MultiThreadedHttpConnectionManager();
      	client = new HttpClient(connectionManager);
      	
      	// set default timeouts
      	final HttpConnectionManagerParams connectionParams = connectionManager.getParams();
      	connectionParams.setSoTimeout(SOCKET_TIMEOUT_MS);
      	connectionParams.setConnectionTimeout(HTTP_CONNECTION_TIMEOUT_MS);
      	connectionManager.setParams(connectionParams);
      	log.debug("MAXCONNECTIONS: "+connectionParams.getMaxTotalConnections());
      	log.debug("MAXCONNECTIONSPERHOST: "+connectionParams.getDefaultMaxConnectionsPerHost());
      	
      	// handle idle connections
      	connectionManager.closeIdleConnections(IDLE_TIMEOUT_MS);
      	idleConnectionHandler = new IdleConnectionTimeoutThread();
      	idleConnectionHandler.addConnectionManager(connectionManager);
      	idleConnectionHandler.start();
      	
		this.renderer = new RendererFactory(new UrlRenderer("/api/")).getRenderer(RenderingFormat.XML);
	}
	
	/**
	 * Constructor
	 * @param address 
	 */
	public WebserviceTagRecommender(final URI address) {
		this();
		this.setAddress(address);
	}
	
	/**
	 * @return the address
	 */
	public URI getAddress() {
		return this.address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(URI address) {
		this.address = address;
	}

	//------------------------------------------------------------------------
	// TagRecommender interface
	//------------------------------------------------------------------------
	@Override
	public void addRecommendedTags(final Collection<RecommendedTag> recommendedTags, final Post<? extends Resource> post) {
		// render post
		// FIXME: choose buffer size
		final StringWriter sw = new StringWriter(100);
		renderPost(post, sw);
		
		// Create a method instance.
		final NameValuePair[] data = {
				new NameValuePair(ID_RECQUERY, sw.toString()),
				new NameValuePair(ID_POSTID, "" + post.getContentId())
		};
		// Create a method instance.
		// send request
		// FIXME: THIS IS JUST FOR DOWNWARD COMPATIBILITY DURING THE DC09 RECOMMENDER CHALLENGE
		//        Replace the following three lines of code with:
		//        InputStreamReader input = sendRequest(data, "/"+METHOD_GETRECOMMENDEDTAGS);
		PostMethod cnct = new PostMethod(getAddress().toString());
		cnct.setRequestBody(data);
		InputStreamReader input = sendRequest(cnct);
		if( input==null ) {
			cnct.releaseConnection();
			cnct = new PostMethod(getAddress().toString()+"/"+METHOD_GETRECOMMENDEDTAGS);
			cnct.setRequestBody(data);
			input = sendRequest(cnct); 
		}
		
		// Deal with the response.
		SortedSet<RecommendedTag> result = null;
		if( input!=null ) {
			try {
				result = renderer.parseRecommendedTagList(input);
			} catch( final Exception e ) {
				log.error("Error parsing recommender response ("+getAddress().toString()+").", e);
				result = null;
			}
		}
		if( result!=null )
			recommendedTags.addAll(result);
		
		cnct.releaseConnection();
	}

	@Override
	public SortedSet<RecommendedTag> getRecommendedTags(
			final Post<? extends Resource> post) {
		final SortedSet<RecommendedTag> retVal = 
			new TreeSet<RecommendedTag>(new RecommendedTagComparator());
		addRecommendedTags(retVal, post);
		return retVal;
	}


	@Override
	public void setFeedback(final Post<? extends Resource> post) {
		// render post
		// FIXME: choose buffer size
		final StringWriter sw = new StringWriter(100);
		renderPost(post, sw);
		
		// Create a method instance.
		final NameValuePair[] data = {
				new NameValuePair(ID_FEEDBACK, sw.toString()),
				new NameValuePair(ID_RECQUERY, sw.toString()), // for downward compatibility
				new NameValuePair(ID_POSTID, "" + post.getContentId())
		};

		// send request
		final PostMethod cnct = new PostMethod(getAddress().toString()+"/"+METHOD_SETFEEDBACK);
		cnct.setRequestBody(data);
		final InputStreamReader input = sendRequest(cnct);

		// Deal with the response.
		if( input!=null ) {
			final String status = renderer.parseStat(input);
			log.info("Feedback status: " + status);
		}
		
		cnct.releaseConnection();
	}

	@Override
	public byte[] getMeta() {
		return getAddress().toString().getBytes();
	}

	@Override
	public String getInfo() {
		return "Webservice";
	}

	@Override
	public String getId() {
		return getAddress().toString();
	}

	//------------------------------------------------------------------------
	// TagRecommenderConnector interface
	//------------------------------------------------------------------------
	@Override
	public boolean connect() throws Exception {
		return false;
	}

	@Override
	public boolean disconnect() throws Exception {
		return false;
	}

	@Override
	public boolean initialize(final Properties props) throws Exception {
		return false;
	}
	
	
	//------------------------------------------------------------------------
	// private helpers
	//------------------------------------------------------------------------

	private void renderPost(final Post<? extends Resource> post, final StringWriter sw) {
		final ViewModel vm = new ViewModel();
		// we use rest-api's xml rederer which perfoms some validation tests which our
		// post model has to pass:
		// 1) set hashes
		// 2) append 'empty' tag
		// 3) set empty title
		if( post.getResource().getInterHash()==null || post.getResource().getInterHash().length()==0 )
			post.getResource().setInterHash("abc");
		if( post.getResource().getIntraHash()==null || post.getResource().getIntraHash().length()==0 )
			post.getResource().setIntraHash("abc");
		if( (post.getTags()==null) || (post.getTags().size()==0) ) {
			final Set<Tag> tags = new HashSet<Tag>();
			tags.add(TagUtils.getEmptyTag());
			post.setTags(tags);
		}
		if( post.getResource().getTitle()==null ) {
			post.getResource().setTitle("");
		}
		renderer.serializePost(sw, post, vm);		
	}
	
	private InputStreamReader sendRequest(final PostMethod cnct) {
		InputStreamReader input = null;
		// byte[] responseBody = null;
		
		try {
			// Execute the method.
			final int statusCode = client.executeMethod(cnct);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method at " + getAddress().toString() + " failed: " + cnct.getStatusLine());
			} else {
				// Read the response body.
				// responseBody = cnct.getResponseBody();
				input        = new InputStreamReader(cnct.getResponseBodyAsStream(), "UTF-8");
			}

		} catch (final HttpException e) {
			log.fatal("Fatal protocol violation("+getAddress()+"): " + e.getMessage(), e);
		} catch (final UnsupportedEncodingException ex) {
			// returns InputStream with default encoding if a exception
			// is thrown with utf-8 support
			log.fatal("Encoding error("+getAddress()+"): " + ex.getMessage(), ex);
		} catch (final IOException e) {
			log.fatal("Fatal transport error("+getAddress()+"): " + e.getMessage(), e);
		} catch (final Exception e) {
			log.fatal("Unknown error ("+getAddress()+")", e);
		} finally {
			// Release the connection.
			// cnct.releaseConnection();
		}  	
		
		// all done.
		// log.debug("Got response: " + new String(responseBody));
		return input;
	}
}
