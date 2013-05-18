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

import static org.bibsonomy.lucene.util.LuceneBase.CFG_LIST_DELIMITER;

import java.util.Collection;

/**
 * convert date objects to a standardized string representation
 * 
 * @author fei
 * @version $Id: LuceneCollectionFormatter.java,v 1.2 2010-05-28 10:22:50 nosebrain Exp $
 * @param <T> 
 */
public abstract class LuceneCollectionFormatter<T> extends AbstractTypeHandler<Collection<T>> {
	
	@Override
	public String getValue(Collection<T> collection) {		
		StringBuilder retVal = new StringBuilder("");
		for (T item : collection) {
			retVal.append(CFG_LIST_DELIMITER).append(convertItem(item));
		}
			
		return retVal.toString().trim();
	}
	
	@Override
	public Collection<T> setValue(String str) {
		Collection<T> retVal = this.createCollection();
		
		String[] tokens = str.split(CFG_LIST_DELIMITER);
		
		for( String token : tokens )
			retVal.add(createItem(token));
		
		return retVal;
	}

	protected abstract Collection<T> createCollection();
	protected abstract T createItem(String token);
	protected abstract String convertItem(T item);
}