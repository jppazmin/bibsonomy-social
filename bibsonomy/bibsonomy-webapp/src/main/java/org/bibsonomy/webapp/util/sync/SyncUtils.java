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

package org.bibsonomy.webapp.util.sync;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.bibsonomy.model.Resource;
import org.bibsonomy.model.sync.SynchronizationAction;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.webapp.util.RequestLogic;
import org.springframework.context.MessageSource;

/**
 * @author wla
 * @version $Id: SyncUtils.java,v 1.3 2011-08-08 12:32:01 rja Exp $
 */
public class SyncUtils {
	/**
	 * prefix of key to access the sync plan stored in the session
	 */
	private static final String SESSION_KEY = "SYNC_PLAN_";
	
	/**
	 * 
	 * @param serviceName
	 * @param requestLogic
	 * @return The sync plan for the given user or <code>null</code> if no such plan could be found.
	 */
	@SuppressWarnings("unchecked")
	public static Map<Class<? extends Resource>, List<SynchronizationPost>> getSyncPlan(final URI serviceName, final RequestLogic requestLogic) {
		final Object sessionAttribute = requestLogic.getSessionAttribute(SESSION_KEY + serviceName);
		if (!present(sessionAttribute) || !(sessionAttribute instanceof Map<?,?>)) {
			return null;
		}
		return (Map<Class<? extends Resource>, List<SynchronizationPost>>) sessionAttribute;
	}
	
	/**
	 * Stores the sync plan in the session.
	 * 
	 * @param serviceName
	 * @param syncPlan
	 * @param requestLogic 
	 */
	public static void setSyncPlan(final URI serviceName, final Map<Class<? extends Resource>, List<SynchronizationPost>> syncPlan, final RequestLogic requestLogic) {
		requestLogic.setSessionAttribute(SyncUtils.SESSION_KEY + serviceName, syncPlan);
	}
	
	/**
	 * 
	 * @param syncPlan
	 * @param serverName
	 * @param locale 
	 * @param messageSource 
	 * @param projectHome 
	 * @return plan summary as map of strings, readable for human
	 */
	public static Map<Class<? extends Resource>, Map<String, String>> getPlanSummary(Map<Class<? extends Resource>, List<SynchronizationPost>> syncPlan, String serverName, final Locale locale, final MessageSource messageSource, final String projectHome) {
		final Map<Class<? extends Resource>, Map<String, String>> result = new LinkedHashMap<Class<? extends Resource>, Map<String,String>>();
		for (final Entry<Class<? extends Resource>, List<SynchronizationPost>> entry : syncPlan.entrySet()) {
			int createClient = 0;
			int updateClient = 0;
			int deleteClient = 0;
			int createServer = 0;
			int updateServer = 0;
			int deleteServer = 0;
			int ok = 0;
			final Class<? extends Resource> resourceType = entry.getKey();
			for (final SynchronizationPost synchronizationPost : entry.getValue()) {
				final SynchronizationAction action = synchronizationPost.getAction();
				switch (action) {
				case CREATE_CLIENT:
					createClient++;
					break;
				case UPDATE_CLIENT:
					updateClient++;
					break;
				case DELETE_CLIENT:
					deleteClient++;
					break;
				case CREATE_SERVER:
					createServer++;
					break;
				case UPDATE_SERVER:
					updateServer++;
					break;
				case DELETE_SERVER:
					deleteServer++;
					break;
				case OK:
					ok++;
					break;
				default:
					break;
				}
			}
			final Map<String, String> messages = new LinkedHashMap<String, String>();
			messages.put("CLIENT", messageSource.getMessage("synchronization.syncPlan.message", new Object[]{projectHome, createClient, updateClient, deleteClient}, locale));
			messages.put("SERVER", messageSource.getMessage("synchronization.syncPlan.message", new Object[]{serverName, createServer, updateServer, deleteServer}, locale));
			messages.put("OTHER", messageSource.getMessage("synchronization.syncPlan.message.other", new Object[]{ok}, locale));
			result.put(resourceType, messages);
		}
		return result;
	}
}
