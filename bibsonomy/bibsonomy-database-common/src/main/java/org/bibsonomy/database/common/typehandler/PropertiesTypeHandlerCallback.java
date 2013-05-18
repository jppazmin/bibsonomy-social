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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

/**
 * An iBATIS type handler callback for {@link Properties} that are mapped to
 * Strings in the database.
 * 
 * @author Robert Jaeschke
 * @author wla
 */
public class PropertiesTypeHandlerCallback extends AbstractTypeHandlerCallback {

	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setNull(Types.CHAR);
		} else {
			final Properties properties = (Properties) parameter;
			final StringWriter sw = new StringWriter();
			try {
			    properties.store(sw, null);
			} catch (IOException e) {
			    /* TODO Auto-generated catch block
			    e.printStackTrace();*/
			}
			setter.setString(sw.getBuffer().toString());
		}
	}

	@Override
	public Object valueOf(final String str) {
		StringReader reader = new StringReader(str);
		final Properties properties = new Properties();
		try {
		    properties.load(reader);
		} catch (IOException e) {
		    return new Properties();
		}
		return properties;
	}
}