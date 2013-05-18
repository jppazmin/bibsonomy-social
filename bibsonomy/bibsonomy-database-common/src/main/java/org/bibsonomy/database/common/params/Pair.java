/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.params;

/**
 * TODO: replace the 2 code copies in recommender and community
 * 
 * @author ?
 * @version $Id: Pair.java,v 1.2 2010-07-02 09:43:56 nosebrain Exp $
 * 
 * @param <T>
 * @param <U>
 */
public class Pair <T, U> {
	private T first;
	private U second;

	/**
	 * default constructor
	 */
	public Pair() {
	}

	/**
	 * build constructor
	 * 
	 * @param first
	 * @param second
	 */
	public Pair( final T first, final U second ) {
		this.first = first;
		this.second = second;
	}

	/**
	 * @return the first
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(final T first) {
		this.first = first;
	}

	/**
	 * @return the second
	 */
	public U getSecond() {
		return second;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecond(final U second) {
		this.second = second;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pair<?,?>)) {
			return false;
		}
		final Pair<?,?> other = (Pair<?,?>) obj;
		if (first == null) {
			if (other.first != null) {
				return false;
			}
		} else if (!first.equals(other.first)) {
			return false;
		}
		if (second == null) {
			if (other.second != null) {
				return false;
			}
		} else if (!second.equals(other.second)) {
			return false;
		}
		return true;
	}


} 