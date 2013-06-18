/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.command;

import java.util.List;

import org.bibsonomy.model.Tag;

/**
 * @author Christian Kramer
 * @version $Id: RelationsCommand.java,v 1.1 2009-05-31 21:17:31 daill Exp $
 */
public class RelationsCommand extends ResourceViewCommand{
	
	private List<Tag> tagRelations;

	/**
	 * @return List<Tag>
	 */
	public List<Tag> getTagRelations() {
		return this.tagRelations;
	}

	/**
	 * @param tagRelations
	 */
	public void setTagRelations(final List<Tag> tagRelations) {
		this.tagRelations = tagRelations;
	}
	
	

}
