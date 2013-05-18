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

package org.bibsonomy.database.systemstags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sdo
 * @version $Id: AbstractSystemTagImpl.java,v 1.7 2011-06-16 14:30:00 doerfel Exp $
 */
public abstract class AbstractSystemTagImpl implements SystemTag {
	protected static final Log log = LogFactory.getLog(SystemTag.class);

	private String argument;

	/**
	 * @param argument the argument to set
	 */
	@Override
	public void setArgument(String argument) {
		this.argument = argument;
	}

	@Override
	public String getArgument() {
		return this.argument;
	}
	
}
