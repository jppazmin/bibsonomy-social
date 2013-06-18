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

import java.io.Writer;
import java.util.Set;

import org.bibsonomy.rest.strategy.AbstractCreateStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * adds references to a gold standard post
 * 
 * @author dzo
 * @version $Id: PostReferencesStrategy.java,v 1.3 2011-04-06 12:10:05 nosebrain Exp $
 */
public class PostReferencesStrategy extends AbstractCreateStrategy {

	private final String hash;
	
	/**
	 * @param context
	 * @param hash 
	 */
	public PostReferencesStrategy(final Context context, final String hash) {
		super(context);
		
		this.hash = hash;
	}

	@Override
	protected String create() {
		final Set<String> references = this.getRenderer().parseReferences(this.doc);
		this.getLogic().createReferences(this.hash, references);
		
		return this.hash;
	}

	@Override
	protected void render(final Writer writer, final String resourceID) {
		this.getRenderer().serializeResourceHash(this.writer, resourceID);
	}

}
