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

package org.bibsonomy.webapp.controller.admin;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.User;
import org.bibsonomy.recommender.tags.database.DBAccess;
import org.bibsonomy.recommender.tags.database.DBLogic;
import org.bibsonomy.recommender.tags.database.params.RecAdminOverview;
import org.bibsonomy.recommender.tags.multiplexer.MultiplexingTagRecommender;
import org.bibsonomy.recommender.tags.multiplexer.util.RecommenderUtil;
import org.bibsonomy.services.recommender.TagRecommender;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.webapp.command.admin.AdminRecommenderViewCommand;
import org.bibsonomy.webapp.command.admin.AdminRecommenderViewCommand.Tab;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author bsc
 * @version $Id: AdminRecommenderController.java,v 1.16 2011-07-14 15:19:56 nosebrain Exp $
 */
public class AdminRecommenderController implements MinimalisticController<AdminRecommenderViewCommand> {
	private static final Log log = LogFactory.getLog(AdminRecommenderController.class);
	
	private static final String CMD_EDITRECOMMENDER = "editRecommender";
	private static final String CMD_UPDATE_RECOMMENDERSTATUS = "updateRecommenderstatus";
	private static final String CMD_REMOVERECOMMENDER = "removerecommender";
	private static final String CMD_ADDRECOMMENDER = "addrecommender";
	
	// TODO: config via spring
	private final DBLogic db = DBAccess.getInstance();
	
	private MultiplexingTagRecommender mp;
	
	/**
	 * Initialize Controller and multiplexer
	 **/
	public void init() {
		List<Long> recs = null;
		try {
			recs = db.getActiveRecommenderSettingIds();
			for (final Long sid : recs)
				mp.enableRecommender(sid);
		} catch (final SQLException e) {
			log.debug("Couldn't initialize multiplexer! ", e);
		}
	}

	@Override
	public View workOn(final AdminRecommenderViewCommand command) {
		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		final Tab tab = Tab.values()[command.getTab()];

		log.info("ACTIVE TAB: " + tab + " -> " + command.getTabDescription());

		/*
		 * Check user role If user is not logged in or not an admin: show error
		 * message
		 */
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in as admin");
		}

		/* ---------------------- Actions ---------------------------- */

		if (!present(command.getAction())) {
			// Do nothing
		} else if (CMD_ADDRECOMMENDER.equals(command.getAction())) {
			handleAddRecommender(command);
		} else if (CMD_REMOVERECOMMENDER.equals(command.getAction())) {
			handleRemoveRecommender(command);
		} else if (CMD_UPDATE_RECOMMENDERSTATUS.equals(command.getAction())) {
			handleUpdateRecommenderStatus(command);
		} else if (CMD_EDITRECOMMENDER.equals(command.getAction())) {
			handleEditRecommender(command);
		}

		command.setAction(null);

		/* ---------------------- Tabs ---------------------------- */

		if (command.getTab() == Tab.STATUS.ordinal()) {
			showStatusTab(command);
		} else if (command.getTab() == Tab.ACTIVATE.ordinal()) {
			showActivationTab(command);
		} else if (command.getTab() == Tab.ADD.ordinal()) {
			showAddTab(command);
		}

		return Views.ADMIN_RECOMMENDER;
	}

	/**
	 * Remove/add recommender page. 
	 */
	private void showAddTab(final AdminRecommenderViewCommand command) {
		try {
			final List<Long> recs = db.getDistantRecommenderSettingIds();
			final Map<Long, String> recMap = db.getRecommenderIdsForSettingIds(recs);

			command.setActiveRecommenders(recMap);
		} catch (final SQLException e) {
			log.debug(e);
		}
	}

	/**
	 * Recommender activation/deactivation page; get Settingids of
	 * active/disabled recommenders from database; store in
	 * command.activeRecs/command.disabledRecs
	 */
	private void showActivationTab(final AdminRecommenderViewCommand command) {
		try {
			final Map<Long, String> activeRecs = db.getRecommenderIdsForSettingIds(db.getActiveRecommenderSettingIds());
			final Map<Long, String> disabledRecs = db.getRecommenderIdsForSettingIds(db.getDisabledRecommenderSettingIds());

			command.setActiveRecommenders(activeRecs);
			command.setDisabledRecommenders(disabledRecs);

		} catch (final SQLException e) {
			log.debug(e);
		}
	}

	/**
	 * Recommender Statuspage; get active recommenders from multiplexer and
	 * fetch setting_id, rec_id, average latency from database; store in
	 * command.recOverview
	 */
	private void showStatusTab(final AdminRecommenderViewCommand command) {
		final List<TagRecommender> recommenderList = new ArrayList<TagRecommender>();
		final List<RecAdminOverview> recommenderInfoList = new ArrayList<RecAdminOverview>();
		recommenderList.addAll(mp.getLocalRecommenders());
		recommenderList.addAll(mp.getDistRecommenders());

		for (final TagRecommender p : recommenderList) {
			try {
				final RecAdminOverview current = db.getRecommenderAdminOverview(RecommenderUtil.getRecommenderId(p));
				current.setLatency(db.getAverageLatencyForRecommender(current.getSettingID(), command.getQueriesPerLatency()));
				recommenderInfoList.add(current);
			} catch (final SQLException e) {
				log.debug(e.toString());
			}
		}
		/* Store info */
		command.setRecOverview(recommenderInfoList);
	}

	private void handleEditRecommender(final AdminRecommenderViewCommand command) {
		try {
			// TODO: add a validator?
			if (!UrlUtils.isValid(command.getNewrecurl())) throw new MalformedURLException();
			final URL newRecurl = new URL(command.getNewrecurl());

			final long sid = command.getEditSid();
			final boolean recommenderEnabled = mp.disableRecommender(sid);
			db.updateRecommenderUrl(command.getEditSid(), newRecurl);
			if (recommenderEnabled) mp.enableRecommender(sid);

			command.setAdminResponse("Changed url of recommender #" + command.getEditSid() + " to " + command.getNewrecurl() + ".");
		} catch (final MalformedURLException ex) {
			command.setAdminResponse("Could not edit recommender. Please check if '" + command.getNewrecurl() + "' is a valid url.");
		} catch (final SQLException e) {
			log.warn("SQLException while editing recommender", e);
			command.setAdminResponse(e.getMessage());
		}
		command.setNewrecurl(null);
		command.setTab(Tab.ADD);
	}

	private void handleUpdateRecommenderStatus(final AdminRecommenderViewCommand command) {
		if (command.getActiveRecs() != null) {
			for (final Long sid : command.getActiveRecs()) {
				mp.enableRecommender(sid);
			}
		}
		if (command.getDisabledRecs() != null) {
			for (final Long sid : command.getDisabledRecs()) {
				mp.disableRecommender(sid);
			}
		}
		command.setTab(Tab.ACTIVATE);
		command.setAdminResponse("Successfully Updated Recommenderstatus!");
	}

	private void handleRemoveRecommender(final AdminRecommenderViewCommand command) {
		try {
			int failures = 0;
			
			if(command.getDeleteRecIds() == null || command.getDeleteRecIds().isEmpty()) {
				command.setAdminResponse("Please select a recommender first!");
			} else {
				for(final String urlString : command.getDeleteRecIds()) {
					final URL url = new URL(urlString);
					final boolean success = mp.removeRecommender(url);
					if(!success) failures++;
				}
				if (failures == 0) {
					command.setAdminResponse("Successfully removed all selected recommenders.");
				} else {
					command.setAdminResponse(failures + " recommender(s) could not be removed.");
				}
			}
		} catch (final MalformedURLException ex) {
			log.warn("Invalid url in removeRecommender ", ex);
		}

		command.setTab(Tab.ADD);
	}

	private void handleAddRecommender(final AdminRecommenderViewCommand command) {
		try {
			if (!UrlUtils.isValid(command.getNewrecurl())) {
				throw new MalformedURLException();
			}

			mp.addRecommender(new URL(command.getNewrecurl()));
			command.setAdminResponse("Successfully added and activated new recommender!");

		} catch (final MalformedURLException e) {
			command.setAdminResponse("Could not add new recommender. Please check if '" + command.getNewrecurl() + "' is a valid url.");
		} catch (final Exception e) {
			log.error("Error testing 'set recommender'", e);
			command.setAdminResponse("Failed to add new recommender");
		}

		command.setTab(Tab.ADD);
	}

	@Override
	public AdminRecommenderViewCommand instantiateCommand() {
		return new AdminRecommenderViewCommand();
	}

	/** @param mp */
	public void setMultiplexingTagRecommender(final MultiplexingTagRecommender mp) {
		this.mp = mp;
	}

}