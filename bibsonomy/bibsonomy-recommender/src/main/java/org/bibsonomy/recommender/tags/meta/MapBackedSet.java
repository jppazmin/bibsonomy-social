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

package org.bibsonomy.recommender.tags.meta;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Problem Having a set, one might want to access objects of the set, given 
 * only a portion of their identity. One could use a map for this, by mapping
 * the objects using this identity. But then one could not so easily use the
 * map for methods which rely on sets. This set internally does exactly this 
 * - you must give it a {@link KeyExtractor} which defines an objects identity
 * under which you can retrieve it from the set using {@link #get(Object)}.
 *   
 * 
 * 
 * @author rja
 * @version $Id: MapBackedSet.java,v 1.2 2010-07-14 12:04:33 nosebrain Exp $
 * @param <K> The type of the keys.
 * @param <V> The type of the values. 
 */
public class MapBackedSet<K, V> extends AbstractSet<V> {

	private final Map<K, V> map;
	private final KeyExtractor<K, V> keyExtractor;
	
	/**
	 * @param keyExtractor
	 */
	public MapBackedSet(final KeyExtractor<K, V> keyExtractor) {
		this.map = new HashMap<K, V>();
		this.keyExtractor = keyExtractor;
	}
	
	private K getKey(final V e) {
		return keyExtractor.getKey(e);
	}
	
	@Override
	public boolean add(final V e) {
		return null != this.map.put(getKey(e), e);
	}

	@Override
	public void clear() {
		this.map.clear();
	}
	
	/**
	 * @param value
	 * @return The value in this set which has the same key as the given value. 
	 */
	public V get(final V value) {
		return this.map.get(getKey(value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		return map.containsKey(getKey((V) o));
	}

	@Override
	public Iterator<V> iterator() {
		return map.values().iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		return null != map.remove(getKey((V) o));
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("retainAll not implemented");
	}

	@Override
	public int size() {
		return this.map.size();
	}

	/**
	 * Interface for {@link MapBackedSet} to generate keys for values.
	 * 
	 * @author rja
	 * @version $Id: MapBackedSet.java,v 1.2 2010-07-14 12:04:33 nosebrain Exp $
	 * @param <K> type of the key values should be associated with 
	 * @param <V> type of the values 
	 */
	interface KeyExtractor<K, V> {
		
		/**
		 * Returns the key the value is associated with.
		 * 
		 * @param value
		 * @return The key representing the given value.
		 */
		public K getKey(V value);

	}
}
