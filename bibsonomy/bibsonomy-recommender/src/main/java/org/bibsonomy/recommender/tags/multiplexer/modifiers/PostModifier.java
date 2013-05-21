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

package org.bibsonomy.recommender.tags.multiplexer.modifiers;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * Post modifiers arbitrarily change a post's content.
 * 
 * @author fei
 * @version $Id: PostModifier.java,v 1.1 2009-07-27 09:08:21 folke Exp $
 */
public interface PostModifier {

	/**
	 * post modifiers arbitrarily change a post's content
	 * 
	 * @param post the post to filter
	 */
	public void alterPost(Post<? extends Resource> post);

}