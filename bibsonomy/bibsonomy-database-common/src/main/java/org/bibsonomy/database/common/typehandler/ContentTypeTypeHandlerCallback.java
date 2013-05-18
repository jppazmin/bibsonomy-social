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

import static org.bibsonomy.util.ValidationUtils.present;

import java.sql.SQLException;

import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.model.Resource;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

/**
 * 
 * @version $Id: ContentTypeTypeHandlerCallback.java,v 1.2 2011-07-18 14:58:35 rja Exp $
 */
public class ContentTypeTypeHandlerCallback extends AbstractTypeHandlerCallback {

	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
		if (present(parameter) && parameter instanceof Class<?>) {
			setter.setInt(ConstantID.getContentTypeByClass((Class<? extends Resource>)parameter).getId());
		} else {
			throw new UnsupportedResourceTypeException();
		}
	}

	@Override
	public Object valueOf(final String str) {
		try {
			return ConstantID.getClassByContentType(Integer.parseInt(str));
		} catch (final Exception e) {
			throw new UnsupportedResourceTypeException();
		}
	}
}