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

import java.util.Date;


/**
 * convert date object's milliseconds to a long value (as string)
 * 
 * @author fei
 * @version $Id: LuceneDateMSFormatter.java,v 1.4 2010-07-16 12:12:00 nosebrain Exp $
 */
public class LuceneDateMSFormatter extends AbstractTypeHandler<Date> {

	@Override
	public String getValue(Date obj) {
		return String.valueOf(obj.getTime());
	}

	@Override
	public Date setValue(String str) {
		try {
			long ms = Long.parseLong(str);
			return new Date(ms);
		} catch (final Exception e) {
			log.error("Error parsing date " + str, e);
		}
				
		return new Date(0);
	}
}
