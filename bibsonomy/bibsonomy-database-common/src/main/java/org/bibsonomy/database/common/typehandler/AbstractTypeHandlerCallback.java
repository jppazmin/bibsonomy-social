/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.typehandler;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * Implements the {@link TypeHandlerCallback#getResult(ResultGetter)} method of
 * {@link TypeHandlerCallback} by just returning null if getter was null or
 * otherwise the result of the {@link TypeHandlerCallback#valueOf(String)}
 * method using the value provided by {@link ResultGetter#getString()}
 * 
 * @author dzo
 * @version $Id: AbstractTypeHandlerCallback.java,v 1.1 2010-05-28 13:49:23 nosebrain Exp $
 */
public abstract class AbstractTypeHandlerCallback implements TypeHandlerCallback {

	/*
	 * (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#getResult(com.ibatis.sqlmap.client.extensions.ResultGetter)
	 */
	@Override
	public Object getResult(final ResultGetter getter) throws SQLException {
		final String value = getter.getString();
		
		if (getter.wasNull()) {
			return null;
		}
		
		return this.valueOf(value);
	}

}