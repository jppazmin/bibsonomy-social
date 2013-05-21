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

import java.util.Collection;

import org.bibsonomy.model.RecommendedTag;

/**
 * Tag modifiers arbitrarily change a recommended tag's content.
 * 
 * @author fei
 * @version $Id: RecommendedTagModifier.java,v 1.2 2010-04-09 07:57:35 nosebrain Exp $
 */
public interface RecommendedTagModifier {

	/**
	 * Tag modifiers arbitrarily change a recommended tag's content.
	 * 
	 * @param tags collection of recommended tags to filter
	 */
	public void alterTags(Collection<RecommendedTag> tags);

}