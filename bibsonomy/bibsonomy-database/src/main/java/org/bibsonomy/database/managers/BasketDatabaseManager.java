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

package org.bibsonomy.database.managers;

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.BasketParam;
import org.bibsonomy.database.plugin.DatabasePluginRegistry;

/**
 * Manages Basket functionalities
 * 
 * TODO: implement full basket functionality
 * 
 * @author Dominik Benz
 * @author Christian Kramer
 * @version $Id: BasketDatabaseManager.java,v 1.12 2011-05-09 12:08:44 nosebrain Exp $
 */
public class BasketDatabaseManager extends AbstractDatabaseManager {
	private final static BasketDatabaseManager singleton = new BasketDatabaseManager();
	
	/**
	 * @return a singleon instance of this BasketDatabaseManager
	 */
	public static BasketDatabaseManager getInstance() {
		return singleton;
	}
	
	private final DatabasePluginRegistry plugins;

	private BasketDatabaseManager() {
		this.plugins = DatabasePluginRegistry.getInstance();
	}

	/**
	 * Retrieve the number of entries currently present in the basket of the
	 * given user.
	 * 
	 * @param username
	 *            the username
	 * @param session
	 *            the database session
	 * @return the number of entries currently stored in the basket
	 */
	public int getNumBasketEntries(String username, final DBSession session) {
		return queryForObject("getNumBasketEntries", username, Integer.class, session);
	}
	
	/**
	 * creates basket items
	 * @param userName - name of the user from whose basket we want to delete the item
	 * @param contentId 
	 * @param session 
	 */
	public void createItem(final String userName, final int contentId, final DBSession session){
		final BasketParam param = new BasketParam();			
		param.setUserName(userName);
		param.setContentId(contentId);
		this.insert("createBasketItem", param, session);
	}
	
	/**
	 * deletes basket items
	 * @param userName - name of the user from whose basket we want to delete the item
	 * @param contentId 
	 * @param session 
	 */
	public void deleteItem(final String userName, final int contentId, final DBSession session){
		final BasketParam param = new BasketParam();			
		param.setUserName(userName);
		param.setContentId(contentId);
		this.plugins.onDeleteBasketItem(param, session);
		this.delete("deleteBasketItem", param, session);
	}
	
	/**
	 * Deletes all items with the given content_id from the basket.
	 * 
	 * @param contentId
	 * @param session
	 */
	public void deleteItems(final int contentId, final DBSession session){
		final BasketParam param = new BasketParam();			
		param.setContentId(contentId);
		this.plugins.onDeleteBasketItem(param, session);
		this.delete("deleteBasketItems", param, session);
	}
	
	/**
	 * updates basket items
	 * @param newContentId 
	 * @param contentId 
	 * @param session 
	 */
	public void updateItems(final int newContentId, final int contentId, final DBSession session){
		final BasketParam param = new BasketParam();
		param.setContentId(contentId);
		param.setNewContentId(newContentId);
		this.update("updateBasketItems", param, session);
	}
	
	/**
	 * drops all basket items related to this user name
	 * 
	 * @param userName
	 * @param session
	 */
	public void deleteAllItems(final String userName, final DBSession session){
		this.plugins.onDeleteAllBasketItems(userName, session);
		this.delete("deleteAllItems", userName, session);
	}
		
}