/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.util.StringUtils;

/**
 * Comparator used to eliminate duplicates (when used in conjuction with a TreeSet)
 * 
 * @author Dominik Benz
 * @version $Id: BibTexPostInterhashComparator.java,v 1.8 2011-04-29 06:45:03 bibsonomy Exp $
 */
public class BibTexPostInterhashComparator implements Comparator<Post<BibTex>>, Serializable {

	private static final long serialVersionUID = -8523955200241922144L;

	@Override
	public int compare(final Post<BibTex> post1, final Post<BibTex> post2) {
		return StringUtils.secureCompareTo(post1.getResource().getInterHash(), post2.getResource().getInterHash());
	}
}