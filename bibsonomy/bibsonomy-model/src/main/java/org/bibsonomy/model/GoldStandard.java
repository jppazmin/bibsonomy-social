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

package org.bibsonomy.model;

import java.util.Set;

/**
 * @author dzo
 * @version $Id: GoldStandard.java,v 1.5 2011-04-29 06:45:05 bibsonomy Exp $
 * @param <R> 
 */
public interface GoldStandard<R extends Resource> {

	/**
	 * @return the references of the resource (unmodifiable)
	 */
	public Set<R> getReferences();

	/**
	 * adds a resource to the references
	 * @param resource
	 * @return <tt>true</tt> iff references did not already contain the specified resource
	 */
	public boolean addToReferences(final R resource);

	/**
	 * adds a set of resources to the references
	 * @param resources
	 * @return <code>true</code> iff the references were changed as a result of the call
	 */
	public boolean addAllToReferences(final Set<? extends R> resources);

	/**
	 * removes a resource from the reference list
	 * @param resource
	 * @return <tt>true</tt> iff reference contained the specified resource
	 */
	public boolean removeFromReferences(final R resource);
	
	/**
	 * removes all references
	 * @param resources the resources to remove from the references
	 * @return <code>true</code> iff the references were changed as a result of the call
	 */
	public boolean removeAllFromReferences(final Set<? extends R> resources);
	
	/**
	 * @return the publications which reference the goldstandard
	 */
	public Set<R> getReferencedBy();
	
	/**
	 * adds a resoure to the referenced by set
	 * @param resource the resource to add to referenced by
	 * @return <code>true</code> iff referenced by did not already contained the specified resource
	 */
	public boolean addToReferencedBy(final R resource);
	
	/**
	 * adds a set of resources to the referenced by set
	 * @param resources
	 * @return <code>true</code> iff the referenced by set was changed as a result of the call
	 */
	public boolean addAllToReferencedBy(final Set<? extends R> resources);

	/**
	 * removes a resource from the referenced by list
	 * @param resource
	 * @return <tt>true</tt> iff referenced by contained the specified resource
	 */
	public boolean removeFromReferencedBy(final R resource);
	
	/**
	 * removes all referenced by publications
	 * @param resources the resources to remove from the references
	 * @return <code>true</code> iff the referenced by set was changed as a result of the call
	 */
	public boolean removeAllFromReferencedBy(final Set<? extends R> resources);

}