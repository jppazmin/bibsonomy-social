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

package org.bibsonomy.recommender.tags.database.params;

/**
 * 
 * @author fei
 * @version $Id: Pair.java,v 1.3 2010-07-14 11:27:39 nosebrain Exp $
 *
 * @param <T>
 * @param <U>
 */
@Deprecated // TODO: remove copy
public class Pair <T extends Comparable<T>, U extends Comparable<U>> implements Comparable<Pair<T,U>>
{
	private T first;
	private U second;
	private transient final int hash;

	public Pair() {
		this.first = null;
		this.second = null;
		this.hash = 0;
	}
	
	public Pair( T f, U s )
	{
		this.first = f;
		this.second = s;
		hash = (getFirst() == null? 0 : getFirst().hashCode() * 31)
		+(second == null? 0 : second.hashCode());
	}

	public T getFirst()
	{
		return first;
	}
	public U getSecond()
	{
		return second;
	}
	public void setFirst(T first)
	{
		this.first = first;
	}
	public void setSecond(U second)
	{
		this.second = second;
	}


	@Override
	public int hashCode()
	{
		return hash;
	}

	@Override
	public boolean equals( Object oth )
	{
		if ( this == oth )
		{
			return true;
		}
		if ( oth == null || !(getClass().isInstance( oth )) )
		{
			return false;
		}
		Pair<?, ?> other = (Pair)oth;
		return (getFirst() == null? other.getFirst() == null : getFirst().equals( other.getFirst() ))
		&& (second == null? other.second == null : second.equals( other.second ));
	}

	/**
	 * implements lexicographic ordering
	 */
	@Override
	public int compareTo(Pair<T, U> o) {
		int left = getFirst().compareTo(o.getFirst());
		int right= getSecond().compareTo(o.getSecond());
		if( left!=0 )
			return left;
		
		return right;
	}

} 