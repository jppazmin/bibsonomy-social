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

package org.bibsonomy.webapp.controller.ajax;


import static org.bibsonomy.util.ValidationUtils.present;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.SynchronizationRunningException;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.model.sync.SynchronizationData;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.model.sync.SynchronizationStatus;
import org.bibsonomy.model.util.ResourceUtils;
import org.bibsonomy.sync.TwoStepSynchronizationClient;
import org.bibsonomy.webapp.command.ajax.AjaxSynchronizationCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.sync.SyncUtils;
import org.bibsonomy.webapp.view.Views;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import com.ibm.icu.text.DateFormat;

/**
 * @author wla
 * @version $Id: SynchronizationController.java,v 1.22 2011-08-05 13:05:29 rja Exp $
 */
public class SynchronizationController extends AjaxController implements MinimalisticController<AjaxSynchronizationCommand>, ErrorAware {
	private Errors errors;
	private TwoStepSynchronizationClient client;
	private MessageSource messageSource;
	private String projectHome;
	
	@Override
	public View workOn(AjaxSynchronizationCommand command) {
		final RequestWrapperContext context = command.getContext();

		/*
		 * some security checks
		 */
		if (!context.isUserLoggedIn()) {
			throw new org.springframework.security.access.AccessDeniedException("please log in");
		}
		final User loginUser = context.getLoginUser();
		if (loginUser.isSpammer()) {
			throw new AccessDeniedException("error.method_not_allowed");
		}
		if (!context.isValidCkey()) {
			this.errors.reject("error.field.valid.ckey");
		}


		/*
		 * check server URI from service name
		 */
		final URI serviceName = command.getServiceName();
		if (!present(serviceName)) {
			errors.rejectValue("serviceName", "error.field.required");
		}

		if (errors.hasErrors()) {
			return Views.AJAX_ERRORS;
		}

		/*
		 * get details for sync server
		 */
		final SyncService server = client.getServerByURI(logic, serviceName);
		
		/*
		 * work on each HTTP method
		 */
		final JSONObject json = new JSONObject();
		switch (requestLogic.getHttpMethod()) {
		case GET:
			/*
			 * get new sync plan
			 */
			final Map<Class<? extends Resource>, List<SynchronizationPost>> syncPlan;
			try {
				syncPlan = client.getSyncPlan(logic, server);
			} catch (final SynchronizationRunningException e) {
				errors.reject("error.synchronization.running");
				return Views.AJAX_ERRORS;
			}
			/*
			 * store it in session for later use
			 */
			SyncUtils.setSyncPlan(serviceName, syncPlan, requestLogic);
			/*
			 * serialize it to show user
			 */
			json.put("syncPlan", serializeSyncPlan(syncPlan, serviceName));
			
			/*
			 * get sync data of this plan
			 */
			final Map<Class<? extends Resource>, SynchronizationData> syncData = new LinkedHashMap<Class<? extends Resource>, SynchronizationData>();
			for (Class<? extends Resource> resourceType : syncPlan.keySet()) {
				final SynchronizationData data = client.getLastSyncData(server, resourceType);
				if (SynchronizationStatus.PLANNED.equals(data.getStatus())) {
					syncData.put(resourceType, data);
				}
			}
			json.put("syncData", serializeSyncData(syncData));

			break;
		case POST:
			/*
			 * get sync plan from session
			 */
			final Map<Class<? extends Resource>, List<SynchronizationPost>> syncPlan2 = SyncUtils.getSyncPlan(serviceName, this.requestLogic);
			if (!present(syncPlan2)) {
				errors.reject("error.synchronization.no_sync_plan_found");
				return Views.AJAX_ERRORS;
			}
			/*
			 * run sync plan
			 */
			final Map<Class<? extends Resource>, SynchronizationData> syncResult;
			try {
				syncResult = client.synchronize(logic, server, syncPlan2);
			} catch (final SynchronizationRunningException e) {
				errors.reject("error.synchronization.running");
				return Views.AJAX_ERRORS;
			}
			/*
			 * remove sync plan from session
			 * FIXME: do this before synchronize()?
			 */
			SyncUtils.setSyncPlan(serviceName, null, requestLogic);
			/*
			 * serialize result
			 */
			json.put("syncData", serializeSyncData(syncResult));
			break;
		case DELETE:
			/*
			 * delete synchronization data
			 * 
			 * if no sync date is given -> all syncData for all resourceTypes is removed
			 */
			final JSONObject second = new JSONObject();
			for (final Class<? extends Resource> resourceType : ResourceUtils.getResourceTypesByClass(server.getResourceType())) {
				client.deleteSyncData(server, resourceType, command.getSyncDate());
				/*
				 * add message for empty result
				 */
				second.put(resourceType.getSimpleName(), messageSource.getMessage("synchronization.noresult", null, requestLogic.getLocale()));
			}
			json.put("syncData", second);
			break;
		default:
			/*
			 * FIXME: what to do here?
			 */
			break;
		}
		
		command.setResponseString(json.toString());

		return Views.AJAX_JSON;
	}
	
	private JSONObject serializeSyncData(final Map<Class<? extends Resource>, SynchronizationData> data) {
		final JSONObject json = new JSONObject();
		final Locale locale = requestLogic.getLocale();
		final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);

		for (final Entry<Class<? extends Resource>, SynchronizationData> entry : data.entrySet()) {
			final StringBuilder buf = new StringBuilder();
			final SynchronizationData value = entry.getValue();
			buf.append(dateFormat.format(value.getLastSyncDate()) + " ");
			buf.append(messageSource.getMessage("synchronization.result", null, locale));
			buf.append(" " + messageSource.getMessage("synchronization.result." + value.getStatus().toString().toLowerCase(), null, locale));
			if (present(value.getInfo())) {
				buf.append(" <em>(" + value.getInfo() + ")</em>");
			}
			json.put(entry.getKey().getSimpleName(), buf.toString());
		}
		return json;
	}

	/**
	 * Currently, the method counts all operations and then creates human-readable
	 * messages that describe what will happen.
	 * 
	 * @param syncPlan
	 * @return
	 */
	private JSONObject serializeSyncPlan(final Map<Class<? extends Resource>, List<SynchronizationPost>> syncPlan, final URI serverName) {
		final JSONObject json = new JSONObject();
		final Locale locale = requestLogic.getLocale();
		
		final Map<Class<? extends Resource>, Map<String, String>> planSummary = SyncUtils.getPlanSummary(syncPlan, serverName.toString(), locale, messageSource, projectHome);
		
		for (final Entry<Class<? extends Resource>, Map<String, String>> planEntry : planSummary.entrySet()) {
			json.put(planEntry.getKey().getSimpleName(), planEntry.getValue());
		}
		return json;
	}
	
	@Override
	public AjaxSynchronizationCommand instantiateCommand() {
		return new AjaxSynchronizationCommand();
	}
	
	@Override
	public Errors getErrors() {
		return errors;
	}

	@Override
	public void setErrors(Errors errors) {
		this.errors = errors;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(final TwoStepSynchronizationClient client) {
		this.client = client;
	}

	/** 
	 * The message source is necessary to render a human-readable synchronization plan. 
	 * @param messageSource
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * To render a human-readable message for the sync plan
	 * 
	 * @param projectHome
	 */
	public void setProjectHome(String projectHome) {
		this.projectHome = projectHome;
	}
}
