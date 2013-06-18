/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest.strategy.posts.standard.references;

import java.io.Reader;
import java.util.Set;

import org.bibsonomy.rest.strategy.AbstractDeleteStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author dzo
 * @version $Id: DeleteReferencesStrategy.java,v 1.2 2010-11-10 11:59:49 nosebrain Exp $
 */
public class DeleteReferencesStrategy extends AbstractDeleteStrategy {
	
	private final String hash;
	private final Reader doc;

	/**
	 * @param context
	 * @param hash
	 */
	public DeleteReferencesStrategy(final Context context, final String hash) {
		super(context);
		
		this.hash = hash;
		this.doc = context.getDocument();
	}

	@Override
	protected boolean delete() {
		final Set<String> references = this.getRenderer().parseReferences(this.doc);
		this.getLogic().deleteReferences(this.hash, references);
		
		// no exception => delete successful
		return true;
	}

}
