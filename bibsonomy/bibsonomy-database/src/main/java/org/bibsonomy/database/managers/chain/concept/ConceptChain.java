/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.managers.chain.concept;

import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.managers.chain.concept.get.GetAllConcepts;
import org.bibsonomy.database.managers.chain.concept.get.GetAllConceptsForUser;
import org.bibsonomy.database.managers.chain.concept.get.GetPickedConceptsForUser;
import org.bibsonomy.database.params.TagRelationParam;
import org.bibsonomy.model.Tag;

/**
 * Chain for concept queries
 * 
 * @author Stefan Stützer
 * @version $Id: ConceptChain.java,v 1.4 2010-09-28 11:19:44 nosebrain Exp $
 */
public class ConceptChain implements FirstListChainElement<Tag, TagRelationParam> {

	private final ListChainElement<Tag, TagRelationParam> getAllConcepts;
	private final ListChainElement<Tag, TagRelationParam> getAllConceptsForUser;
	private final ListChainElement<Tag, TagRelationParam> getPickedConceptsForUser;

	/**
	 * Constructs the chain
	 */
	public ConceptChain() {
		// intialize chain elements
		this.getAllConcepts = new GetAllConcepts();
		this.getAllConceptsForUser = new GetAllConceptsForUser();
		this.getPickedConceptsForUser = new GetPickedConceptsForUser();

		// set order of chain elements
		this.getAllConcepts.setNext(this.getAllConceptsForUser);
		this.getAllConceptsForUser.setNext(this.getPickedConceptsForUser);
	}

	@Override
	public ListChainElement<Tag, TagRelationParam> getFirstElement() {
		return this.getAllConcepts;
	}
}