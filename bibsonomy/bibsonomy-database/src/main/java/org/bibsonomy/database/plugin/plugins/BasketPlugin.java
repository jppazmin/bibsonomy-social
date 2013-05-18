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
import org.bibsonomy.database.managers.BasketDatabaseManager;
import org.bibsonomy.database.plugin.AbstractDatabasePlugin;

/**
 * Updates/Deletes items from the basket.
 * 
 * XXX: we can't have a static/singleton {@link BasketDatabaseManager} instance,
 * because we have a circular dependency (the manager contains the plugins ...)
 * 
 * @author daill
 * @version $Id: BasketPlugin.java,v 1.4 2011-04-11 11:02:58 nosebrain Exp $
 */
public class BasketPlugin extends AbstractDatabasePlugin {
	
	@Override
	public Runnable onPublicationDelete(final int contentId, final DBSession session) {
		return new Runnable() {
			
			@Override
			public void run() {
				BasketDatabaseManager.getInstance().deleteItems(contentId, session);
			}
			
		};
	}
	
	@Override
	public Runnable onBookmarkDelete(final int contentId, final DBSession session) {
		return new Runnable() {
			
			@Override
			public void run() {
				BasketDatabaseManager.getInstance().deleteItems(contentId, session);
			}
			
		};
	}
	
	@Override
	public Runnable onPublicationUpdate(final int newContentId, final int contentId, final DBSession session) {
		return new Runnable() {
			
			@Override
			public void run() {
				BasketDatabaseManager.getInstance().updateItems(newContentId, contentId, session);
			}
			
		};
	}
	
	@Override
	public Runnable onBookmarkUpdate(final int newContentId, final int contentId, final DBSession session){
		return new Runnable() {
			
			@Override
			public void run() {
				BasketDatabaseManager.getInstance().updateItems(newContentId, contentId, session);
			}
			
		};
	}
}
