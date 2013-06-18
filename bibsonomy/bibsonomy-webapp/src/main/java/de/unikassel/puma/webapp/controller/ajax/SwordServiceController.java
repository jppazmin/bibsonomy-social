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

package de.unikassel.puma.webapp.controller.ajax;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.common.exceptions.SwordException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Repository;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.controller.ajax.AjaxController;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.context.MessageSource;

import de.unikassel.puma.openaccess.sword.PumaData;
import de.unikassel.puma.openaccess.sword.SwordService;
import de.unikassel.puma.webapp.command.SwordServiceCommand;

/**
 * @author philipp
 * @version $Id: SwordServiceController.java,v 1.11 2011-05-18 15:23:46 sven Exp $
 */
public class SwordServiceController extends AjaxController implements MinimalisticController<SwordServiceCommand> {
	private static final Log log = LogFactory.getLog(SwordServiceController.class);

	private SwordService swordService;
	private MessageSource messageSource;

	@Override
	public SwordServiceCommand instantiateCommand() {
		return new SwordServiceCommand();
	}

	@Override
	public View workOn(SwordServiceCommand command) {
		if(!command.getContext().isUserLoggedIn()) {
			throw new AccessDeniedException("error.method_not_allowed");
		}
		
		String message = "error.sword.sentsuccessful";
		int statuscode = 1; // statuscode=1: ok, =0: error 

		final User user = command.getContext().getLoginUser();
		
		final Post<?> post = getPostToHash(command.getResourceHash(), user.getName());
		
		if(!present(post)) {
			
		}
		
		// add some metadata to post
		final PumaData<BibTex> pumaData = new PumaData<BibTex>();

		pumaData.setPost((Post<BibTex>) post);

		
		
		
		// get additional metadata
		Map<String, List<String>> metadataMap = logic.getExtendedFields(BibTex.class, command.getContext().getLoginUser().getName(), post.getResource().getIntraHash(), null);
		// TODO is use of PublicationClassificatorSingleton classification here possible?
//		Set<String> availableClassifications = classificator.getInstance().getAvailableClassifications(); 
		
		for (final Entry<String, List<String>> item : metadataMap.entrySet()) {
			final String firstValue = item.getValue().get(0);
			final String key = item.getKey();
			if (SwordService.AF_INSTITUTION.equals(key)) pumaData.setExaminstitution(firstValue);
			else if (SwordService.AF_PHDREFEREE.equals(key)) pumaData.addExamreferee(firstValue);
			else if (SwordService.AF_PHDREFEREE2.equals(key)) pumaData.addExamreferee(firstValue);
			else if (SwordService.AF_PHDORALEXAM.equals(key)) pumaData.setPhdoralexam(firstValue);
			else if (SwordService.AF_SPONSOR.equals(key)) pumaData.addSponsor(firstValue);
			else if (SwordService.AF_ADDITIONALTITLE.equals(key)) pumaData.addAdditionaltitle(firstValue);
			else pumaData.addClassification(key, item.getValue());

//			if (availableClassifications.contains(item.getKey())) {
//				pumaData.addClassification(item.getKey(), item.getValue());
//			}	
		}		

		try {
			// TODO: do not throw an exception if transfer was ok
			swordService.submitDocument(pumaData, user);
		} catch (final SwordException ex) {
			
			// send message of exception to webpage via ajax to give feedback of submission result
			message = ex.getMessage();
			
			// errcode 2xx is ok / 200, 201, 202
			if (message.substring(0, 20).equals("error.sword.errcode2")){
				// transmission complete and successful
				statuscode = 1;
				message = "error.sword.sentsuccessful";
			} else {
				// Error
				statuscode = 0;
			}
		}

		// log successful store to repository 
		if (statuscode == 1) {
			//final Post<?> createdPost = logic.getPostDetails(command.getResourceHash(), user.getName());
			List<Repository> repositorys = new ArrayList<Repository>();
			Repository repo = new Repository();
			repo.setId("REPOSITORY");  // TODO: set ID to current repository - it should be possible in fututre to send a post to multiple/different repositories 
			repositorys.add(repo);
			post.setRepositorys(repositorys);
	
			List<String> updatedPosts = logic.updatePosts(Collections.<Post<?>>singletonList(post), PostUpdateOperation.UPDATE_REPOSITORY);
		}
	
		final JSONObject json = new JSONObject();
		final JSONObject jsonResponse = new JSONObject();

		final Locale locale = requestLogic.getLocale();
		
		jsonResponse.put("statuscode", statuscode);
		jsonResponse.put("message", message);
		// TODO: get from somewhere localized messages to transmit via ajax
		// localizedMessage = puma.repository.response.$message
		jsonResponse.put("localizedMessage", messageSource.getMessage(message, null, locale));
		json.put("response", jsonResponse);
		
		/*
		 * write the output, it will show the JSON-object as a plaintext string
		 */
		command.setResponseString(json.toString());
		
		return Views.AJAX_JSON;
	}
	
	private Post<?> getPostToHash(String intraHash, String userName) {
		Post<?> post = null;
		
		try {
			post = logic.getPostDetails(intraHash, userName);
		} catch (ResourceNotFoundException ex) {
			post = null;
		} catch (ResourceMovedException ex) {
			post = getPostToHash(ex.getNewIntraHash(), userName);
		}
		
		return post;
	}
	
	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param swordService
	 */
	public void setSwordService(SwordService swordService) {
		this.swordService = swordService;
	}

}
