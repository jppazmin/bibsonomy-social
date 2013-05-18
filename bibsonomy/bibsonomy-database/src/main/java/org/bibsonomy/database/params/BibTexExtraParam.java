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

package org.bibsonomy.database.params;

import org.bibsonomy.model.extra.BibTexExtra;

/**
 * @author Christian Schenk
 * @version $Id: BibTexExtraParam.java,v 1.3 2008-04-20 17:55:34 cschenk Exp $
 */
public class BibTexExtraParam extends BibTexParam {

	private BibTexExtra bibtexExtra;

	/**
	 * @return BibTexExtra
	 */
	public BibTexExtra getBibtexExtra() {
		if (this.bibtexExtra == null) this.bibtexExtra = new BibTexExtra();
		return this.bibtexExtra;
	}

	/**
	 * @param extra
	 */
	public void setBibtexExtra(BibTexExtra extra) {
		this.bibtexExtra = extra;
	}
}