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

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.ConceptUpdateOperation;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.Tag;
import org.bibsonomy.webapp.command.ajax.ConceptAjaxCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This controller is used to pick and unpick one or all concepts of the logged in user.
 * 
 * 
 * @author Christian Kramer
 * @version $Id: ConceptController.java,v 1.7 2010-11-05 13:40:27 nosebrain Exp $
 */
public class ConceptController extends AjaxController implements MinimalisticController<ConceptAjaxCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(ConceptController.class);
	
	private Errors errors;
	
	@Override
	public View workOn(ConceptAjaxCommand command) {
		log.debug(this.getClass().getSimpleName());
		
		final RequestWrapperContext context = command.getContext();
		
		if (!context.getUserLoggedIn()){
			log.debug("someone tried to access this ajax controller manually and isn't logged in");
			return new ExtendedRedirectView("/");
		}
		
		//check if ckey is valid
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
			return Views.ERROR;
		}
		
		final String loginUserName = context.getLoginUser().getName();

		
		/*
		 * decide which action will be done
		 */
		final String action = command.getAction();
		final String tag = command.getTag();
		if ("show".equals(action)){
			logic.updateConcept(new Tag(tag), GroupingEntity.USER, loginUserName, ConceptUpdateOperation.PICK);
		} else if ("hide".equals(action)){
			logic.updateConcept(new Tag(tag), GroupingEntity.USER, loginUserName, ConceptUpdateOperation.UNPICK);
		} else if ("all".equals(action)){
			if ("show".equals(tag)){
				logic.updateConcept(null, GroupingEntity.USER, loginUserName, ConceptUpdateOperation.PICK_ALL);
			} else if ("hide".equals(tag)){
				logic.updateConcept(null, GroupingEntity.USER, loginUserName, ConceptUpdateOperation.UNPICK_ALL);
			}
		} 
		
		// if forward is available redirect to referer (in case of javascript disabled)
		if (present(command.getForward())) {
			return new ExtendedRedirectView(requestLogic.getReferer());
		}
		
		/*
		 * get the picked concepts from the DB
		 */
		final List<Tag> pickedConcepts = this.logic.getConcepts(null, GroupingEntity.USER, loginUserName, null, null, ConceptStatus.PICKED, 0, Integer.MAX_VALUE);

		/*
		 * create the response string XML
		 */
		command.setResponseString(prepareResponseString(loginUserName, pickedConcepts));
		
		return Views.AJAX_XML;
	}
	
	/*
	 * This private method gets the list of picked concepts and
	 * transform them into XML which will serialized as a string and returned.
	 * 
	 * @param groupingname
	 * @return String
	 */
	protected String prepareResponseString(final String loginUserName, final List<Tag> pickedConcepts){
		final StringWriter response = new StringWriter();  

		try {
			final Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
			
			// append root node
			final Element relations = doc.createElement("relations");
			relations.setAttribute("user", loginUserName);
			doc.appendChild(relations);
			
			
			// append all other informations
			for (final Tag tag : pickedConcepts){
				final Element relation = doc.createElement("relation");
				relations.appendChild(relation);
				
				final Element upper = doc.createElement("upper");
				upper.setTextContent(tag.getName());
				relation.appendChild(upper);
				
				final Element lowers = doc.createElement("lowers");
				lowers.setAttribute("id", tag.getName());
				relation.appendChild(lowers);

				for (final Tag subTag : tag.getSubTags()){
					final Element lower = doc.createElement("lower");
					lower.setTextContent(subTag.getName());
					lowers.appendChild(lower);	
				}
			}
			
			new XMLSerializer (response, new OutputFormat (doc)).serialize(doc);
		
			// return it as string
            return response.toString();
			
		} catch (ParserConfigurationException ex) {
			log.error("Could not parse XML ", ex);
		} catch (IOException ex) {
			log.error("Could not serialize XML ", ex);
		}
		
		return null;
	}

	@Override
	public ConceptAjaxCommand instantiateCommand() {
		return new ConceptAjaxCommand();
	}
	
	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(Errors errors) {
		this.errors = errors;
	}	
}
