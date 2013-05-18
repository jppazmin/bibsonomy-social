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

package org.bibsonomy.database.managers.chain.resource;

import java.util.HashMap;
import java.util.Map;

import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.database.managers.BibTexDatabaseManager;
import org.bibsonomy.database.managers.BookmarkDatabaseManager;
import org.bibsonomy.database.managers.PostDatabaseManager;
import org.bibsonomy.database.managers.chain.ListChainElement;
import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.database.params.BookmarkParam;
import org.bibsonomy.database.params.ResourceParam;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * 
 * @param <R> the resource of the chain element
 * @param <P> the param of the chain element
 * 
 * @author dzo
 * @version $Id: ResourceChainElement.java,v 1.3 2010-09-28 11:19:44 nosebrain Exp $
 */
public abstract class ResourceChainElement<R extends Resource, P extends ResourceParam<R>> extends ListChainElement<Post<R>, P> {

	// TODO: extract to an external class?!
	private static final Map<Class<?>, PostDatabaseManager<?, ?>> dbs;
	
	static {
		dbs = new HashMap<Class<?>, PostDatabaseManager<?, ?>>();
		
		dbs.put(BookmarkParam.class, BookmarkDatabaseManager.getInstance());
		dbs.put(BibTexParam.class, BibTexDatabaseManager.getInstance());
	}
	
	@SuppressWarnings("unchecked")
	protected PostDatabaseManager<R, P> getDatabaseManagerForType(final Class<?> clazz) {
		if (dbs.containsKey(clazz)) {
			return (PostDatabaseManager<R, P>) dbs.get(clazz);
		}
		
		throw new UnsupportedResourceTypeException("param " + clazz.getName() + " not supported");
	}
}
