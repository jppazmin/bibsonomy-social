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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.model.util.SimHash;

/**
 * A publication with references to other publications
 * 
 * @author dzo
 * @version $Id: GoldStandardPublication.java,v 1.9 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class GoldStandardPublication extends BibTex implements GoldStandard<BibTex> {
	private static final long serialVersionUID = 128893745902925210L;
	
	private Set<BibTex> references;
	private Set<BibTex> referencedBy;
	
	private void lacyLoadReferences() {
		if (this.references == null) {
			this.references = new HashSet<BibTex>();
		}
	}
	
	private void lacyLoadReferencedBy() {
		if (this.referencedBy == null) {
			this.referencedBy = new HashSet<BibTex>();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#getReferences()
	 */
	@Override
	public Set<BibTex> getReferences() {
		this.lacyLoadReferences();
		return Collections.unmodifiableSet(this.references);
	}
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#addToReferences(org.bibsonomy.model.GoldStandardPublication)
	 */
	@Override
	public boolean addToReferences(final BibTex publication) {
		this.lacyLoadReferences();
		return this.references.add(publication);
	}
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#addAllToReferences(java.util.Set)
	 */
	@Override
	public boolean addAllToReferences(final Set<? extends BibTex> publications) {
		this.lacyLoadReferences();
		if (publications != null) {
			return this.references.addAll(publications);
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#removeFromReferences(org.bibsonomy.model.GoldStandard)
	 */
	@Override
	public boolean removeFromReferences(final BibTex publication) {
		return this.references == null ? false : this.references.remove(publication);
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#removeAllFromReferences(java.util.Set)
	 */
	@Override
	public boolean removeAllFromReferences(final Set<? extends BibTex> publications) {
		return this.references == null ? false : this.references.removeAll(publications);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#addAllToReferencedBy(java.util.Set)
	 */
	@Override
	public boolean addAllToReferencedBy(Set<? extends BibTex> resources) {
		this.lacyLoadReferencedBy();
		if (resources != null) {
			return this.referencedBy.addAll(resources);
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#addToReferencedBy(org.bibsonomy.model.Resource)
	 */
	@Override
	public boolean addToReferencedBy(BibTex resource) {
		this.lacyLoadReferencedBy();
		return this.referencedBy.add(resource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#getReferencedBy()
	 */
	@Override
	public Set<BibTex> getReferencedBy() {
		this.lacyLoadReferencedBy();
		return Collections.unmodifiableSet(this.referencedBy);
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#removeAllFromReferencedBy(java.util.Set)
	 */
	@Override
	public boolean removeAllFromReferencedBy(Set<? extends BibTex> resources) {
		return this.referencedBy == null ? false : this.referencedBy.removeAll(resources);
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.GoldStandard#removeFromReferencedBy(org.bibsonomy.model.Resource)
	 */
	@Override
	public boolean removeFromReferencedBy(BibTex resource) {
		return this.referencedBy == null ? false : this.referencedBy.remove(resource);
	}
	
	@Override
	public void recalculateHashes() {
		final String simHash = SimHash.getSimHash(this, HashID.INTER_HASH);
		this.setIntraHash(simHash);
		this.setInterHash(simHash);
	}
}