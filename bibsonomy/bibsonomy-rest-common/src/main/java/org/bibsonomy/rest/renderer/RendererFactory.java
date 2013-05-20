/**
 *
 *  BibSonomy-Rest-Common - Common things for the REST-client and server.
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

package org.bibsonomy.rest.renderer;

import java.util.HashMap;
import java.util.Map;

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.rest.renderer.impl.JSONRenderer;
import org.bibsonomy.rest.renderer.impl.XMLRenderer;

/**
 * A factory to get implementations of the
 * {@link org.bibsonomy.rest.renderer.Renderer}-interface.
 * 
 * @author Christian Schenk
 * @version $Id: RendererFactory.java,v 1.19 2011-06-09 11:58:45 rja Exp $
 */
public class RendererFactory {
	
	/**
	 * Holds the available renderers. New renderers can be added using 
	 */
	private final Map<RenderingFormat,Renderer> renderers = new HashMap<RenderingFormat, Renderer>();
	
	public RendererFactory(final UrlRenderer urlRenderer) {
		renderers.put(RenderingFormat.JSON, new JSONRenderer(urlRenderer));
		renderers.put(RenderingFormat.XML, new XMLRenderer(urlRenderer));
	}
	
	/**
	 * Registers the provided renderer with the given renderingFormat. 
	 * 
	 * @param renderingFormat
	 * @param renderer
	 */
	public void addRenderer(final RenderingFormat renderingFormat, final Renderer renderer) {
		renderers.put(renderingFormat, renderer);
	}

	/**
	 * Returns the renderer for the given format; it defaults to the XML renderer.
	 * @param renderingFormat 
	 * @return the renderer
	 */
	public Renderer getRenderer(final RenderingFormat renderingFormat) {
		if (renderingFormat == null) throw new InternServerException("RenderingFormat is null");
		
		if (renderers.containsKey(renderingFormat)) {
			return renderers.get(renderingFormat);
		}
		
		// the default is the XML renderer
		return renderers.get(RenderingFormat.XML);
	}
}