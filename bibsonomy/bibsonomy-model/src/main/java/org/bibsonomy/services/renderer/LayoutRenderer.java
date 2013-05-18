/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.services.renderer;

import java.io.IOException;
import java.util.List;

import org.bibsonomy.common.exceptions.LayoutRenderingException;
import org.bibsonomy.model.Layout;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * Interface for basic layout rendering. 
 * 
 * @author:  rja
 * @version: $Id: LayoutRenderer.java,v 1.9 2011-06-07 14:31:19 rja Exp $
 * $Author: rja $
 * @param <LAYOUT> - the type of layout this renderer renders
 * 
 */
public interface LayoutRenderer<LAYOUT extends Layout> {

	/** Returns the requested layout. A layout may be user-specific, thus the name 
	 * of the login user must be given. 
	 *  
	 * @param layoutName
	 * @param loginUserName
	 * @return The selected layout. 
	 * @throws IOException - if the layout could not be loaded because of internal errors 
	 * (i.e., not the layout itself is the problem) 
	 * @throws LayoutRenderingException - if the layout could not be found or contains errors
	 */
	public LAYOUT getLayout(final String layoutName, final String loginUserName) throws LayoutRenderingException, IOException;

	/** Renders the given layout to the outputStream.
	 * 
	 * @param layout
	 * @param posts
	 * @param embeddedLayout - if possible, the rendering result should be embeddable into
	 * a page (i.e., for HTML based layouts: don't render &lt;html&gt; tags) 
	 * @return The buffer the layout has been rendered to.
	 * 
	 * @throws IOException - if there was an internal problem rendering the layout
	 * @throws LayoutRenderingException - if the layout contains errors
	 */
	public StringBuffer renderLayout(final LAYOUT layout, final  List<? extends Post<? extends Resource>> posts, final boolean embeddedLayout) throws LayoutRenderingException, IOException;

	/** Checks, if the renderer supports the given resource type.
	 * 
	 * XXX: this could also be layout-dependent, i.e., we should not ask the
	 * renderer, but the layout ...
	 * 
	 * @param clazz
	 * @return <code>true</code>, if this renderer supports the given resource type.
	 */
	public boolean supportsResourceType(final Class<? extends Resource> clazz);	
}

