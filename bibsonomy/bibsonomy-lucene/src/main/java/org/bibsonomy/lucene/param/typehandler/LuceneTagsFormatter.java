/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.param.typehandler;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.bibsonomy.model.Tag;

/**
 * convert date objects to a standardized string representation
 * 
 * @author fei
 * @version $Id: LuceneTagsFormatter.java,v 1.2 2010-05-28 10:22:50 nosebrain Exp $
 */
public class LuceneTagsFormatter extends LuceneCollectionFormatter<Tag> {

	@Override
	protected Collection<Tag> createCollection() {
		return new LinkedHashSet<Tag>();
	}

	@Override
	protected Tag createItem(String token) {
		return new Tag(token);
	}

	@Override
	protected String convertItem(Tag item) {
		return item.getName();
	}
}