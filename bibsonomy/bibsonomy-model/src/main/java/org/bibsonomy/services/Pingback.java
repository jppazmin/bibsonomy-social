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

package org.bibsonomy.services;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * 
 * 
 * @author rja
 * @version $Id: Pingback.java,v 1.3 2011-07-26 08:31:04 bibsonomy Exp $
 */
public interface Pingback {

	/**
	 * Sends a pingback for the provided post. Implementations must ensure that
	 * <ul>
	 * <li>pingbacks are send only for publicly visible posts,</li>
	 * <li>the method does not block, i.e., the pingback is sent asynchronously</li>
	 * </ul>
	 * 
	 * @param post
	 */
	public String sendPingback(final Post<? extends Resource> post);
}
