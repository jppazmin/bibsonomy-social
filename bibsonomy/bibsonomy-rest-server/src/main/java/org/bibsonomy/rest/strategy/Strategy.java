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

package org.bibsonomy.rest.strategy;

import java.io.ByteArrayOutputStream;
import java.io.Writer;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.rest.RESTUtils;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.RestServlet;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.renderer.Renderer;
import org.bibsonomy.rest.renderer.RenderingFormat;
import org.bibsonomy.rest.renderer.UrlRenderer;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: Strategy.java,v 1.18 2011-06-09 12:10:53 rja Exp $
 */
public abstract class Strategy {
	private final Context context;
	
	protected Writer writer;

	/**
	 * @param context
	 */
	public Strategy(final Context context) {
		this.context = context;
	}

	/**
	 * @see Context#canAccess()
	 */
	public void canAccess() {
		// noop
	}
	
	/**
	 * @param outputStream the output stream
	 */
	public void initWriter(final ByteArrayOutputStream outputStream) {
		this.writer = RESTUtils.getOutputWriterForStream(outputStream, RestServlet.RESPONSE_ENCODING);
	}

	/**
	 * @param outStream 
	 * @throws InternServerException
	 * @throws NoSuchResourceException
	 * @throws ResourceNotFoundException 
	 * @throws ResourceMovedException 
	 */
	public abstract void perform(final ByteArrayOutputStream outStream) throws InternServerException, NoSuchResourceException, ResourceMovedException, ResourceNotFoundException;

	/**
	 * Get Content type to be set for response, depending on the specified user agent.
	 * 
	 * @param userAgent - 
	 * @return the contentType of the answer document
	 */
	public final String getContentType(final String userAgent) {
		final String contentType = this.getContentType();
		if (contentType != null && this.context.apiIsUserAgent(userAgent)) {
			// Use special content type if request comes from BibSonomy REST client
			// (like bibsonomy/post+XML )
			// TODO: check if the client has ever used this content type
			return RestProperties.getInstance().getSystemName().toLowerCase() + "/" + contentType + "+" + getRenderingFormat().toString();
		}
		
		return this.getRenderingFormat().getMimeType();
	}

	protected RenderingFormat getRenderingFormat() {
		return this.context.getRenderingFormat();
	}

	@Deprecated
	protected String getContentType() {
		return null;
	}

	/**
	 * Chooses a GroupingEntity based on the parameterMap in the {@link Context}.
	 * 
	 * @return The GroupingEntity; it defaults to ALL.
	 */
	protected GroupingEntity chooseGroupingEntity() {
		if (this.context.getStringAttribute("user", null) != null) return GroupingEntity.USER;
		if (this.context.getStringAttribute("group", null) != null) return GroupingEntity.GROUP;
		if (this.context.getStringAttribute("viewable", null) != null) return GroupingEntity.VIEWABLE;
		if (this.context.getStringAttribute("friend", null) != null) return GroupingEntity.FRIEND;
		return GroupingEntity.ALL;
	}

	protected LogicInterface getLogic() {
		return this.context.getLogic();
	}
	
	protected UrlRenderer getUrlRenderer() {
		return this.context.getUrlRenderer();
	}

	protected Renderer getRenderer() {
		return this.context.getRenderer();
	}
}