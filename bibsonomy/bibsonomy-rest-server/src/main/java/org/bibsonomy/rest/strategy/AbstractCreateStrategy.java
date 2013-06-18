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

package org.bibsonomy.rest.strategy;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.Writer;

import org.bibsonomy.common.exceptions.InternServerException;

/**
 * @author Dominik Benz
 * @version $Id: AbstractCreateStrategy.java,v 1.6 2011-04-06 12:10:05 nosebrain Exp $
 */
public abstract class AbstractCreateStrategy extends Strategy {

	protected final Reader doc;
	
	/**
	 * @param context
	 */
	public AbstractCreateStrategy(final Context context) {
		super(context);
		this.doc = context.getDocument();		
	}

	@Override
	public final void perform(final ByteArrayOutputStream outStream) throws InternServerException {
		final String resourceID = this.create();	
		render(this.writer, resourceID);
	}

	protected abstract void render(Writer writer, String resourceID);

	protected abstract String create();
}