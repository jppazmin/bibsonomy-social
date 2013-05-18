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

import org.bibsonomy.common.enums.ProfilePrivlevel;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

/**
 * @author dzo
 * @version $Id: ProfilePrivlevelTypeHandlerCallback.java,v 1.1 2010-05-28 13:49:23 nosebrain Exp $
 */
public class ProfilePrivlevelTypeHandlerCallback extends AbstractTypeHandlerCallback {
	private static final ProfilePrivlevel DEFAULT_PROFILE_PRIVLEVEL = ProfilePrivlevel.PRIVATE;

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setInt(DEFAULT_PROFILE_PRIVLEVEL.getProfilePrivlevel());
		} else {
			final ProfilePrivlevel profilePrivlevel = (ProfilePrivlevel) parameter;
			setter.setInt(profilePrivlevel.getProfilePrivlevel());
		}
	}

	@Override
	public Object valueOf(final String str) {
		try {
			return ProfilePrivlevel.getProfilePrivlevel(Integer.parseInt(str));
		} catch (NumberFormatException ex) {
			return DEFAULT_PROFILE_PRIVLEVEL;
		}
	}

}
