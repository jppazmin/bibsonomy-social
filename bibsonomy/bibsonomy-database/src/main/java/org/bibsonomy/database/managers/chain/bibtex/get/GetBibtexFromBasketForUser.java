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

package org.bibsonomy.database.managers.chain.bibtex.get;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.chain.bibtex.BibTexChainElement;
import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;

/**
 * Returns a list of BibTexs contained in a certain user's basket
 * 
 * @author Dominik Benz
 * @version $Id: GetBibtexFromBasketForUser.java,v 1.4 2010-06-02 11:31:51 nosebrain Exp $
 */
public class GetBibtexFromBasketForUser extends BibTexChainElement {
	
	@Override
	protected List<Post<BibTex>> handle(final BibTexParam param, final DBSession session) {
		return this.db.getPostsFromBasketForUser(param.getUserName(), param.getLimit(), param.getOffset(), session);
	}

	@Override
	protected boolean canHandle(final BibTexParam param) {
		return (param.getGrouping() == GroupingEntity.BASKET &&
				present(param.getUserName()) && 
				!present(param.getBibtexKey()) &&
				!present(param.getSearch()) &&
				!present(param.getHash()) &&
				!present(param.getTagIndex())
		);
	}
}