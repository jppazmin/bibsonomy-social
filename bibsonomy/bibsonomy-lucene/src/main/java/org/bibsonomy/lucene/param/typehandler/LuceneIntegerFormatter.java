/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.param.typehandler;


/**
 * convert date objects to a standardized string representation
 * 
 * @author fei
 * @version $Id: LuceneIntegerFormatter.java,v 1.2 2010-05-28 10:22:50 nosebrain Exp $
 */
public class LuceneIntegerFormatter extends AbstractTypeHandler<Integer> {

	@Override
	public String getValue(Integer obj) {
		return obj.toString();
	}

	@Override
	public Integer setValue(String str) {
		try {
			return Integer.parseInt(str);
		} catch( Exception e ) {
			log.error("Error parsing number " + str, e);
		}
				
		return null;
	}
}