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

package org.bibsonomy.lucene.database;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.lucene.database.params.ResourcesParam;
import org.bibsonomy.model.GoldStandardPublication;

/**
 * @author dzo
 * @version $Id: LuceneGoldStandardPublicationLogic.java,v 1.2 2010-10-13 11:31:53 nosebrain Exp $
 */
public class LuceneGoldStandardPublicationLogic extends LuceneDBLogic<GoldStandardPublication> {

	@Override
	protected String getResourceName() {
		return GoldStandardPublication.class.getSimpleName();
	}

	@Override
	protected ResourcesParam<GoldStandardPublication> getResourcesParam() {
		return new ResourcesParam<GoldStandardPublication>();
	}
	
	@Override
	public Integer getLastTasId() {
    		final DBSession session = this.openSession();
    		try {
    		    return this.queryForObject("getLastContentId", Integer.class, session);
    		} finally {
    		    session.close();
    		}
	}
} 