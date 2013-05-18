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

import org.bibsonomy.database.managers.chain.resource.get.GetResourcesByResourceSearch;
import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.model.BibTex;

/**
 * Returns a list of BibTex's for a given search.
 * 
 * @author fei
 * @version $Id: GetBibtexByResourceSearch.java,v 1.8 2010-09-28 12:12:39 nosebrain Exp $
 */
public class GetBibtexByResourceSearch extends GetResourcesByResourceSearch<BibTex, BibTexParam> {

	@Override
	protected boolean canHandle(final BibTexParam param) {
		return (!present(param.getBibtexKey()) &&
				(present(param.getSearch()) || present(param.getAuthor()) || present(param.getTitle()))
				); 
	}	
}