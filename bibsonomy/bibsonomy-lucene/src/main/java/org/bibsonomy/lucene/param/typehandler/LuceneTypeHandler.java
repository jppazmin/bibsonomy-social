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

/**
 * interface for converting objects to corresponding string representations 
 * for storing into the lucene index
 * 
 * @author fei
 * @version $Id: LuceneTypeHandler.java,v 1.3 2010-05-28 10:22:50 nosebrain Exp $
 * @param <T> the type to handle
 */
public interface LuceneTypeHandler<T> {
	
	/**
	 * get given object's string representation
	 * @param obj 
	 * @return the string representation of obj
	 */
	public String getValue(T obj);

	/**
	 * 
	 * @param str 
	 * @return the object to the str value
	 */
	public T setValue(String str);
}
