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

package org.bibsonomy.webapp.util.spring.factorybeans;

import org.springframework.beans.factory.FactoryBean;

/**
 * To provide NULL beans for functionality one theme has (e.g., PUMA) and the
 * other doesn't (e.g., BibSonomy).
 * 
 * @author rja
 * @version $Id: NullFactoryBean.java,v 1.2 2011-03-16 13:53:39 nosebrain Exp $
 */
public class NullFactoryBean implements FactoryBean<Void> {

	@Override
    public Void getObject() throws Exception {
        return null;
    }

	@Override
    public Class<? extends Void> getObjectType() {
        return null;
    }

	@Override
    public boolean isSingleton() {
        return true;
    }
}

