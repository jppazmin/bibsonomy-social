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

package org.bibsonomy.database.plugin.plugins;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.GoldStandardReferenceParam;
import org.bibsonomy.database.params.LoggingParam;
import org.bibsonomy.database.plugin.AbstractDatabasePlugin;

/**
 * @author dzo
 * @version $Id: GoldStandardPublicationReferencePlugin.java,v 1.4 2011-07-01 12:56:04 nosebrain Exp $
 */
public class GoldStandardPublicationReferencePlugin extends AbstractDatabasePlugin {
	
	@Override
	public Runnable onGoldStandardPublicationDelete(final String interhash, final DBSession session) {
		// delete all references of the post
		return new Runnable() {
			
			@Override
			public void run() {
				final GoldStandardReferenceParam param = new GoldStandardReferenceParam();
				param.setHash(interhash);
				param.setRefHash(interhash);
				
				delete("deleteReferencesGoldStandardPublication", param, session);				
				delete("deleteGoldStandardPublicationReferences", param, session);
			}
		};
	}
	
	@Override
	public Runnable onGoldStandardPublicationUpdate(final String newInterhash, final String interhash, final DBSession session) {
		// update all references of the post
		return new Runnable() {
			@Override
			public void run() {
				final LoggingParam<String> param = new LoggingParam<String>();
				param.setNewId(newInterhash);
				param.setOldId(interhash);
				
				update("updateGoldStandardPublicationReference", param, session);
				update("updateReferenceGoldStandardPublication", param, session);
				update("updateDiscussion", param, session);
				update("updateReviewRatingCache", param, session);
			}
		};
	}
}