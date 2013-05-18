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

package org.bibsonomy.database.plugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.BasketParam;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.database.plugin.plugins.BasketPlugin;
import org.bibsonomy.database.plugin.plugins.BibTexExtraPlugin;
import org.bibsonomy.database.plugin.plugins.GoldStandardPublicationReferencePlugin;
import org.bibsonomy.database.plugin.plugins.Logging;
import org.bibsonomy.database.plugin.plugins.DiscussionPlugin;
import org.bibsonomy.model.DiscussionItem;

/**
 * All database plugins are registered here.
 * 
 * @author Christian Schenk
 * @version $Id: DatabasePluginRegistry.java,v 1.27 2011-06-16 13:55:01 nosebrain Exp $
 */
public class DatabasePluginRegistry implements DatabasePlugin {
	
	private static final List<DatabasePlugin> DEFAULT_PLUGINS;
	
	static {
		// TODO: config via spring
		DEFAULT_PLUGINS = new LinkedList<DatabasePlugin>();
		
		// order matters!
		DEFAULT_PLUGINS.add(new Logging());
		DEFAULT_PLUGINS.add(new BibTexExtraPlugin());
		DEFAULT_PLUGINS.add(new BasketPlugin());
		DEFAULT_PLUGINS.add(new GoldStandardPublicationReferencePlugin());
		DEFAULT_PLUGINS.add(new DiscussionPlugin());
	}
	
	/**
	 * @return the default plugins
	 */
	public static List<DatabasePlugin> getDefaultPlugins() {
		return Collections.unmodifiableList(DEFAULT_PLUGINS);
	}

	private static final DatabasePluginRegistry singleton = new DatabasePluginRegistry();
	
	/** Holds all plugins; order matters! */
	private final LinkedHashMap<String, DatabasePlugin> plugins;
	
	private DatabasePluginRegistry() {
		this.plugins = new LinkedHashMap<String, DatabasePlugin>();
		
		for (final DatabasePlugin plugin : DatabasePluginRegistry.DEFAULT_PLUGINS) {
			this.add(plugin);
		}
	}

	/**
	 * @return {@link DatabasePluginRegistry} instance
	 */
	public static DatabasePluginRegistry getInstance() {
		return singleton;
	}

	/**
	 * Plugins can be added with this method.
	 * FIXME: will be removed with the introduction of a DI-framework
	 * @param plugin 
	 */
	public void add(final DatabasePlugin plugin) {
		final String key = plugin.getClass().getName();
		if (this.plugins.containsKey(key)) {
			throw new RuntimeException("Plugin already present " + key);
		}
		this.plugins.put(key, plugin);
	}

	/**
	 * FIXME: will be removed with the introduction of a DI-framework
	 */
	public void clearPlugins() {
		this.plugins.clear();
	}

	private void executeRunnable(final Runnable runnable) {
		// If the runnable is null -> do nothing
		if (runnable == null) {
			return;
		}
		runnable.run();
	}
	
	@Override
	public Runnable onPublicationInsert(final int contentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onPublicationInsert(contentId, session));
		}
		
		return null;
	}

	@Override
	public Runnable onPublicationDelete(final int contentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onPublicationDelete(contentId, session));
		}
		
		return null;
	}

	@Override
	public Runnable onPublicationUpdate(final int oldContentId, final int newContentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onPublicationUpdate(newContentId, oldContentId, session)); // new and old contentId are not swapped!
		}
		
		return null;
	}
	
	@Override
	public Runnable onGoldStandardPublicationCreate(final String interhash, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onGoldStandardPublicationCreate(interhash, session));
		}
		
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationDelete(final String interhash, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onGoldStandardPublicationDelete(interhash, session));
		}
		
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationUpdate(final String newInterhash, final String interhash, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onGoldStandardPublicationUpdate(newInterhash, interhash, session));
		}
		
		return null;
	}
	
	@Override
	public Runnable onGoldStandardPublicationReferenceCreate(final String userName, final String interHashPublication, final String interHashReference) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onGoldStandardPublicationReferenceCreate(userName, interHashPublication, interHashReference));
		}
		
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationReferenceDelete(final String userName, final String interHashPublication, final String interHashReference, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onGoldStandardPublicationReferenceDelete(userName, interHashPublication, interHashReference, session));
		}
		
		return null;
	}

	@Override
	public Runnable onBookmarkInsert(final int contentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onBookmarkInsert(contentId, session));
		}

		return null;
	}

	@Override
	public Runnable onBookmarkDelete(final int contentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onBookmarkDelete(contentId, session));
		}

		return null;
	}

	@Override
	public Runnable onBookmarkUpdate(final int oldContentId, final int newContentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onBookmarkUpdate(newContentId, oldContentId, session));
		}
		
		return null;
	}

	@Override
	public Runnable onTagRelationDelete(final String upperTagName, final String lowerTagName, final String userName, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onTagRelationDelete(upperTagName, lowerTagName, userName, session));
		}

		return null;
	}
	
	@Override
	public Runnable onConceptDelete(final String conceptName, final String userName, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onConceptDelete(conceptName, userName, session));
		}

		return null;
	}

	@Override
	public Runnable onTagDelete(final int contentId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onTagDelete(contentId, session));
		}

		return null;
	}

	@Override
	public Runnable onRemoveUserFromGroup(final String username, final int groupId, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onRemoveUserFromGroup(username, groupId, session));
		}

		return null;
	}
	
	@Override
	public Runnable onUserDelete(final String userName, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onUserDelete(userName, session));
		}

		return null;
	}

	@Override
	public Runnable onUserInsert(final String userName, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onUserInsert(userName, session));
		}

		return null;
	}

	@Override
	public Runnable onUserUpdate(final String userName, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onUserUpdate(userName, session));
		}
		
		return null;
	}
	
	@Override
	public Runnable onDeleteFellowship(final UserParam param, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onDeleteFellowship(param, session));
		}
		
		return null;
	}
	
	@Override
	public Runnable onDeleteFriendship(final UserParam param,final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onDeleteFriendship(param, session));
		}
		
		return null;
	}
	
	@Override
	public Runnable onDeleteBasketItem(final BasketParam param, final DBSession session){
		for (final DatabasePlugin plugin : this.plugins.values()) {
			this.executeRunnable(plugin.onDeleteBasketItem(param, session));
		}

		return null;
	}
	
	@Override
	public Runnable onDeleteAllBasketItems(final String userName, final DBSession session){
		for (final DatabasePlugin plugin : this.plugins.values()){
			this.executeRunnable(plugin.onDeleteAllBasketItems(userName, session));
		}

		return null;
	}

	@Override
	public Runnable onDiscussionUpdate(final String interHash, final DiscussionItem discussionItem, final DiscussionItem oldDiscussionItem, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()){
			this.executeRunnable(plugin.onDiscussionUpdate(interHash, discussionItem, oldDiscussionItem, session));
		}

		return null;
	}
	
	@Override
	public Runnable onDiscussionItemDelete(final String interHash, final DiscussionItem deletedDiscussionItem, final DBSession session) {
		for (final DatabasePlugin plugin : this.plugins.values()){
			this.executeRunnable(plugin.onDiscussionItemDelete(interHash, deletedDiscussionItem, session));
		}

		return null;
	}
}