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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.lucene.index.manager.LuceneResourceManager;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.admin.AdminLuceneViewCommand;
import org.bibsonomy.webapp.command.admin.LuceneIndexSettingsCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * Controller for lucene admin page
 * 
 * @author Sven Stefani
 * @version $Id: AdminLuceneController.java,v 1.19 2011-07-14 15:19:56 nosebrain Exp $
 */
public class AdminLuceneController implements MinimalisticController<AdminLuceneViewCommand> {
	private static final Log log = LogFactory.getLog(AdminLuceneController.class);
	
	private static final String GENERATE_INDEX = "generateIndex";
	
	
	private List<LuceneResourceManager<? extends Resource>> luceneResourceManagers;
	
	@Override
	public View workOn(final AdminLuceneViewCommand command) {
		log.debug(this.getClass().getSimpleName());

		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		/* Check user role
		 * If user is not logged in or not an admin: show error message */
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in as admin");
		}	
		
		if (GENERATE_INDEX.equals(command.getAction())) {
			final LuceneResourceManager<? extends Resource> mng = getManagerByResourceName(command.getResource());
			if (mng != null) {
				if (!mng.isGeneratingIndex()) {
					mng.generateIndex();
				} else {
					command.setAdminResponse("Already building lucene-index for resource \"" + command.getResource() + "\".");
				}
			} else {
				command.setAdminResponse("Cannot build new index because there exists no manager for resource \"" + command.getResource() + "\".");
			}
		}
		// Infos über die einzelnen Indexe
		// Anzahl Einträge, letztes Update, ...
		final List<LuceneIndexSettingsCommand> indices = command.getIndices();
		
		for (final LuceneResourceManager<? extends Resource> manager: luceneResourceManagers) {
			final boolean isIndexEnabled = manager.isIndexEnabled();
			final LuceneIndexSettingsCommand indexCmd         = new LuceneIndexSettingsCommand();
			final LuceneIndexSettingsCommand indexCmdInactive = new LuceneIndexSettingsCommand();
			
			/*
			// If a new Index was generated, the index and searcher have to be reset
			if(generatedIndex && !manager.isIndexEnabled()) {
				manager.resetIndexReader();
				manager.resetIndexSearcher();
				isIndexEnabled = manager.isIndexEnabled();
			}*/
			
			indexCmd.setEnabled(isIndexEnabled);
			indexCmd.setResourceName(manager.getResourceName());
			indexCmd.setName(manager.getResourceName() + " index");
			indexCmd.setInactiveIndex(indexCmdInactive);
				
			//TODO: show index-ids
			//indexCmd.setId(...);
			if (manager.isGeneratingIndex()) {
				indexCmd.setGeneratingIndex(true);
				indexCmd.setIndexGenerationProgress(manager.getGenerator().getProgressPercentage());
			}
			if (isIndexEnabled) {
				indexCmd.setIndexStatistics(manager.getStatistics());
				indexCmdInactive.setIndexStatistics(manager.getInactiveIndexStatistics());
			}
			
			indices.add(indexCmd);
		}
		
		
		return Views.ADMIN_LUCENE;
	}
	
	private LuceneResourceManager<? extends Resource> getManagerByResourceName(final String resource) {
		for(final LuceneResourceManager<? extends Resource> mng: luceneResourceManagers) {
			if(mng.getResourceName().equals(resource)) {
				return mng;
			}
		}
		return null;
	}
	

	@Override
	public AdminLuceneViewCommand instantiateCommand() {
		return new AdminLuceneViewCommand();
	}
	
	/**
	 * @param luceneResourceManagers
	 */
	public void setLuceneResourceManagers(final List<LuceneResourceManager<? extends Resource>> luceneResourceManagers) {
		this.luceneResourceManagers = luceneResourceManagers;
	}
}