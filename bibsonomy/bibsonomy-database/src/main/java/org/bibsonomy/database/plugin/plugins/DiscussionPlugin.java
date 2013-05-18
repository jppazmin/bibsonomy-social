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
import org.bibsonomy.database.params.LoggingParam;
import org.bibsonomy.database.plugin.AbstractDatabasePlugin;
import org.bibsonomy.model.DiscussionItem;

/**
 * @author dzo
 * @version $Id: DiscussionPlugin.java,v 1.2 2011-06-21 13:42:20 nosebrain Exp $
 */
public class DiscussionPlugin extends AbstractDatabasePlugin {
	
	/* (non-Javadoc)
	 * @see org.bibsonomy.database.plugin.AbstractDatabasePlugin#onCommentUpdate(java.lang.String, org.bibsonomy.model.Comment, org.bibsonomy.model.Comment, org.bibsonomy.database.common.DBSession)
	 */
	@Override
	public Runnable onDiscussionUpdate(final String interHash, final DiscussionItem discussionItem, final DiscussionItem oldDiscussionItem, final DBSession session) {
		return new Runnable() {
			
			@Override
			public void run() {
				final LoggingParam<String> param = new LoggingParam<String>();
				param.setNewId(discussionItem.getHash());
				param.setOldId(oldDiscussionItem.getHash());
				update("updateParentHash", param, session);
			}
		};
	}
}
