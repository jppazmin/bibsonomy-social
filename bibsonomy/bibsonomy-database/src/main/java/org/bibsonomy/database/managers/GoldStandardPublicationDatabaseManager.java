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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.FirstListChainElement;
import org.bibsonomy.database.managers.chain.goldstandard.publication.GoldStandardPublicationChain;
import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.GoldStandardPublication;
import org.bibsonomy.model.Post;

/**
 * Used to create, read, update and delete gold standard publications from the database.
 * 
 * @author dzo
 * @version $Id: GoldStandardPublicationDatabaseManager.java,v 1.5 2010-10-12 11:42:28 nosebrain Exp $
 */
public final class GoldStandardPublicationDatabaseManager extends GoldStandardDatabaseManager<BibTex, GoldStandardPublication, BibTexParam> {
	private static final GoldStandardPublicationDatabaseManager INSTANCE = new GoldStandardPublicationDatabaseManager();
	
	
	private static final GoldStandardPublicationChain chain = new GoldStandardPublicationChain();
	
	/**
	 * @return the @{link:GoldStandardPublicationDatabaseManager} instance
	 */
	public static GoldStandardPublicationDatabaseManager getInstance() {
		return INSTANCE;
	}
	
	private GoldStandardPublicationDatabaseManager() {}

	
	@Override
	protected BibTexParam getInsertParam(final Post<GoldStandardPublication> post) {
		final BibTexParam insert = new BibTexParam();
		insert.setResource(post.getResource());
		insert.setDescription(post.getDescription());
		insert.setDate(post.getDate());
		insert.setRequestedContentId(post.getContentId());
		insert.setUserName((present(post.getUser()) ? post.getUser().getName() : ""));
		insert.setGroupId(GroupID.PUBLIC); // gold standards are public
		
		return insert;
	}
	
	@Override
	protected void onGoldStandardCreate(String resourceHash, DBSession session) {
		this.plugins.onGoldStandardPublicationCreate(resourceHash, session);
	}
	
	@Override
	protected void onGoldStandardUpdate(String oldHash, String newResourceHash, DBSession session) {
		this.plugins.onGoldStandardPublicationUpdate(newResourceHash, oldHash, session);		
	}
	
	@Override
	protected void onGoldStandardDelete(String resourceHash, DBSession session) {
		this.plugins.onGoldStandardPublicationDelete(resourceHash, session);
	}
	
	@Override
	protected void onGoldStandardReferenceDelete(String userName, String interHash, String interHashRef, DBSession session) {
		this.plugins.onGoldStandardPublicationReferenceDelete(userName, interHash, interHashRef, session);		
	}

	@Override
	protected FirstListChainElement<Post<GoldStandardPublication>, BibTexParam> getChain() {
	    return chain;
	}
}