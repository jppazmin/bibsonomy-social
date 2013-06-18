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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.recommender.tags.multiplexer.MultiplexingTagRecommender;
import org.bibsonomy.rest.renderer.Renderer;
import org.bibsonomy.rest.renderer.RendererFactory;
import org.bibsonomy.rest.renderer.RenderingFormat;
import org.bibsonomy.rest.renderer.UrlRenderer;
import org.bibsonomy.services.recommender.TagRecommender;
import org.bibsonomy.webapp.command.ajax.AjaxRecommenderCommand;
import org.bibsonomy.webapp.util.GroupingCommandUtils;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Some common operations for recommendation tasks.
 * 
 * TODO: This is a candidate for refactoring/performance optimization:
 *       As in the post*controller, the post-command has to be filled -
 *       at least with grouping information, as private posts shouldn't
 *       be sent to remotely installed recommender
 * TODO: controller could use the JSONRenderer to return recommandations as
 * 		JSON and not XML
 *       
 * @param <R>
 *  
 * @author fei
 * @version $Id: RecommendationsAjaxController.java,v 1.15 2011-06-09 13:00:40 rja Exp $
 */
public abstract class RecommendationsAjaxController<R extends Resource> extends AjaxController implements MinimalisticController<AjaxRecommenderCommand<R>> {
	private static final Log log = LogFactory.getLog(RecommendationsAjaxController.class);

	/** this identifies spammer, which are flagged by an admin */
	private static final String USERSPAMALGORITHM = "admin";
	
	
	/** default recommender for serving spammers */
	private TagRecommender spamTagRecommender;

	/** 
	 * To sort out spam posts, we need access to informations 
	 * from the spam detection framework 
	 */
	private LogicInterface adminLogic;
	
	/**
	 * Provides tag recommendations to the user.
	 */
	private MultiplexingTagRecommender tagRecommender;
	
	@Override
	public View workOn(AjaxRecommenderCommand<R> command) {
		final RequestWrapperContext context = command.getContext();
		
		/*
		 * only users which are logged in might post bookmarks -> send them to
		 * login page
		 */
		if (!context.isUserLoggedIn()) {
			command.setResponseString("");
			return Views.AJAX_XML;
		}
		
		final User loginUser = context.getLoginUser();
		
		//------------------------------------------------------------------------
		// THIS IS AN ISSUE WE STILL HAVE TO DISCUSS:
		// During the ECML/PKDD recommender challenge, many recommender systems
		// couldn't deal with the high load, so we filter out those users, which
		// are flagged as spammer either by an admin, or by the framework for sure 
		// TODO: we could probably also filter out those users, which are 
		//       flagged as 'spammer unsure' 
		//------------------------------------------------------------------------
		final User dbUser = adminLogic.getUserDetails(loginUser.getName());

		/*
		 * set the user of the post to the loginUser (the recommender might need
		 * the user name)
		 */
		command.getPost().setUser(loginUser);

		/*
		 * initialize groups
		 */
		GroupingCommandUtils.initGroups(command, command.getPost().getGroups());

		// set postID for recommender
		command.getPost().setContentId(command.getPostID());

		if ((dbUser.isSpammer()) && ((dbUser.getPrediction() == null && dbUser.getAlgorithm() == null) ||
					(dbUser.getPrediction().equals(1) || dbUser.getAlgorithm().equals(USERSPAMALGORITHM)))  ) {
			// the user is a spammer
			log.debug("Filtering out recommendation request from spammer");
			if (this.getSpamTagRecommender() != null)	{
				SortedSet<RecommendedTag> result = this.getSpamTagRecommender().getRecommendedTags(command.getPost());
				this.processRecommendedTags(command, result);
			} else {
				command.setResponseString("");
			}
		} else {				
			// the user doesn't seem to be a spammer
			/*
			 * get the recommended tags for the post from the command
			 */
			if (this.getTagRecommender() != null)	{
				SortedSet<RecommendedTag> result = this.getTagRecommender().getRecommendedTags(command.getPost(), command.getPostID());
				this.processRecommendedTags(command, result);
			} else {
				command.setResponseString("");
			}
		}
		
		return Views.AJAX_XML;
	}
	
	@Override
	public AjaxRecommenderCommand<R> instantiateCommand() {
		final AjaxRecommenderCommand<R> command = new AjaxRecommenderCommand<R>();
		/*
		 * initialize lists
		 * FIXME: is it really neccessary to initialize ALL those lists? Which are really needed?
		 */
		command.setRelevantGroups(new ArrayList<String>());
		command.setRelevantTagSets(new HashMap<String, Map<String, List<String>>>());
		command.setRecommendedTags(new TreeSet<RecommendedTag>());
		command.setCopytags(new ArrayList<Tag>());
		/*
		 * initialize post & resource
		 */
		command.setPost(new Post<R>());
		command.getPost().setResource(this.initResource());
		
		GroupingCommandUtils.initGroupingCommand(command);
		
		return command;
	}

	protected abstract R initResource();
	
	//------------------------------------------------------------------------
	// private helper functions
	//------------------------------------------------------------------------
	private void processRecommendedTags(AjaxRecommenderCommand<R> command, SortedSet<RecommendedTag> tags) {
		command.setRecommendedTags(tags);
		final Renderer renderer = new RendererFactory(new UrlRenderer("/api/")).getRenderer(RenderingFormat.XML);
		final StringWriter sw = new StringWriter(100);
		renderer.serializeRecommendedTags(sw, command.getRecommendedTags());
		command.setResponseString(sw.toString());
	}

	/**
	 * @return the tagRecommender
	 */
	public MultiplexingTagRecommender getTagRecommender() {
		return this.tagRecommender;
	}

	/**
	 * @param tagRecommender the tagRecommender to set
	 */
	public void setTagRecommender(MultiplexingTagRecommender tagRecommender) {
		this.tagRecommender = tagRecommender;
	}

	/**
	 * @return the adminLogic
	 */
	public LogicInterface getAdminLogic() {
		return this.adminLogic;
	}

	/**
	 * @param adminLogic the adminLogic to set
	 */
	public void setAdminLogic(LogicInterface adminLogic) {
		this.adminLogic = adminLogic;
	}

	/**
	 * @return the spamTagRecommender
	 */
	public TagRecommender getSpamTagRecommender() {
		return this.spamTagRecommender;
	}

	/**
	 * @param spamTagRecommender the spamTagRecommender to set
	 */
	public void setSpamTagRecommender(TagRecommender spamTagRecommender) {
		this.spamTagRecommender = spamTagRecommender;
	}

}
