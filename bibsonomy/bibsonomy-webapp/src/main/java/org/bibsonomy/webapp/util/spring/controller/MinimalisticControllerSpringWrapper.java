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

package org.bibsonomy.webapp.util.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.services.URLGenerator;
import org.bibsonomy.webapp.command.ContextCommand;
import org.bibsonomy.webapp.controller.ajax.AjaxController;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ResponseLogic;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.ServiceUnavailableException;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.BaseCommandController;

/**
 * Instances of this class wrap MinimalisticController and adapt them
 * to the spring Controller interface. It also registers a custom
 * databinder to also bind attributes to command-properties and not only
 * parameters.
 * 
 * @param <T> type of the command object used in the MinimalisticController
 * 
 * @author Jens Illig
 * @version $Id: MinimalisticControllerSpringWrapper.java,v 1.41 2011-06-08 11:25:25 nosebrain Exp $
 */
@SuppressWarnings("deprecation")
public class MinimalisticControllerSpringWrapper<T extends ContextCommand> extends BaseCommandController {
	private static final Log log = LogFactory.getLog(MinimalisticControllerSpringWrapper.class);
	
	private static final String CONTROLLER_ATTR_NAME = "minctrlatrr";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private String controllerBeanName;
	
	private String[] allowedFields;
	private String[] disallowedFields;
	
	private URLGenerator urlGenerator;
	
	private ConversionService conversionService;

	/**
	 * @param conversionService the conversionService to set
	 */
	@Required
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/** 
	 * Sets the fields which Spring is allowed to bind to command objects.
	 * <br/>
	 * Note that in the current implementation, these fields are the same for ALL 
	 * controllers.
	 * 
	 * @param allowedFields
	 */
	@Required
	public void setAllowedFields(final String[] allowedFields) {
		this.allowedFields = allowedFields;
	}
	
	/** Sets the fields which Spring must not bind to command objects. 
	 * <br/>
	 * This list overrides the allowedFields, such that a field listed
	 * here will <em>not</em> be bound, even it appears in the allowed
	 * fields! 
	 * 
	 * @param disallowedFields
	 */
	public void setDisallowedFields(final String[] disallowedFields) {
		this.disallowedFields = disallowedFields;
	}

	/**
	 * @param controllerBeanName the name of the controller bean in the context
	 *                           of the renderer
	 */
	@Required
	public void setControllerBeanName(final String controllerBeanName) {
		this.controllerBeanName = controllerBeanName;
	}
	
	/**
	 * @param urlGenerator the urlGenerator to set
	 */
	@Required
	public void setUrlGenerator(URLGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean suppressValidation(final HttpServletRequest request, final Object command) {
		final MinimalisticController<T> controller = (MinimalisticController<T>) request.getAttribute(CONTROLLER_ATTR_NAME);
		if (controller instanceof ValidationAwareController<?>) {
			return !((ValidationAwareController<T>) controller).isValidationRequired((T)command);
		}
		
		return false;
	}

	/**
	 * instantiates, initializes and runs the MinimalisticController
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		((RequestLogic) getApplicationContext().getBean("requestLogic")).setRequest(request); // hack but thats springs fault
		((ResponseLogic) getApplicationContext().getBean("responseLogic")).setResponse(response); // hack but thats springs fault
		final MinimalisticController<T> controller = (MinimalisticController<T>) getApplicationContext().getBean(controllerBeanName);
		/**
		 * Controller is put into request.
		 * 
		 * FIXME: is this still neccessary?
		 * 
		 * SuppressValidation retrieves controller from request again!
		 */
		request.setAttribute(CONTROLLER_ATTR_NAME, controller);
		
		/*
		 * DEBUG: log request attributes
		 */
		if (log.isDebugEnabled()) {
			final Enumeration<?> e = request.getAttributeNames();
			while (e.hasMoreElements()) {
				log.debug(e.nextElement().toString());			
			}
		}
		
		final T command = controller.instantiateCommand();

		/*
		 * put context into command
		 * 
		 * TODO: in the future this is hopefully no longer needed, since the wrapper
		 * only exists to transfer request attributes into the command.
		 */
		command.setContext((RequestWrapperContext) request.getAttribute(RequestWrapperContext.class.getName()));

		/*
		 * set validator for this instance
		 */
		if (controller instanceof ValidationAwareController<?>) {
			this.setValidator(((ValidationAwareController<T>) controller).getValidator());
		}
		
		/*
		 * bind request attributes to command
		 */
		final ServletRequestDataBinder binder = bindAndValidate(request, command);
		final BindException errors = new BindException(binder.getBindingResult());
		if (controller instanceof ErrorAware) {
			((ErrorAware)controller).setErrors(errors);
		}
		
		View view;
		
		/*
		 * define error view
		 */
		if (controller instanceof AjaxController) {
			view = Views.AJAX_ERRORS;
		} else {
			view = Views.ERROR;
		}
		
		try {
			view = controller.workOn(command);
		} catch (final MalformedURLSchemeException malformed) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			errors.reject("error.http.notFound", malformed.getMessage());
			log.warn("Could not complete controller (invalid URL scheme) : " + malformed.getMessage());
		} catch (final AccessDeniedException ad) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			errors.reject(ad.getMessage());
			log.warn("Could not complete controller (AccessDeniedException), occured in: " + ad.getStackTrace()[0] + ", msg is: " + ad.getMessage());
		} catch (final ServiceUnavailableException e) {
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			response.setHeader("Retry-After", Long.toString(e.getRetryAfter()));
			errors.reject(e.getMessage(), new Object[]{e.getRetryAfter()}, "Service unavailable");
			/*
			 *  this exception is only thrown in UserLoginController
			 *  if desired, add some logging there. Otherwise, our error logs get
			 *  cluttered.(dbe)
			 */
			// log.warn("Could not complete controller (Service unavailable): " + e.getMessage());
		} catch (final ResourceMovedException e) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("Location", urlGenerator.getPostUrl(e.getResourceType(), e.getNewIntraHash(), e.getUserName()));
		} catch (final ResourceNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			errors.reject("error.post.notfound", e.getMessage()); // FIXME: it would be better, to show the specific 404 view
		} catch (final org.springframework.security.access.AccessDeniedException ex) {
			/*
			 * we rethrow the exception here in order that Spring Security can
			 * handle the exception (saving request and redirecting to the login
			 * page (if user is not logged in) or to the access denied page)
			 */
			throw ex;
		} catch (final Exception ex) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errors.reject("error.internal", new Object[]{ex}, "Internal Server Error: " + ex.getMessage());
			log.error("Could not complete controller (general exception) for request /" + request.getRequestURI() + "?" + request.getQueryString() + " with referer " + request.getHeader("Referer"), ex);
		}
		
		log.debug("Exception catching block passed, putting comand+errors into model.");
		
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put(getCommandName(), command);
		
		/*
		 * put errors into model 
		 */
		model.putAll(errors.getModel());
		
		log.debug("Returning model and view.");
		
		/**
		 * If the view is already a Spring view, use it directly.
		 * The primal reason for the this workaround is, that Spring's RedirctView
		 * automatically appends the model parameters to each redirected URL. This
		 * can only be avoided by calling setExposeModelAttributes(false) on the 
		 * RedirectView. Hence, we must directly create a redirect view instead of 
		 * using a "redirect:..." URL.  
		 */
		if (org.springframework.web.servlet.View.class.isAssignableFrom(view.getClass())) {
			return new ModelAndView((org.springframework.web.servlet.View) view, model);
		}
		
		return new ModelAndView(view.getName(), model);			
	}

	@Override
	protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);

		/*
		 * set convertion service (string => enum, string => class)
		 */
		binder.setConversionService(this.conversionService);
		
		/*
		 * Register a custom date editor to support binding of date fields.
		 * 
		 * FIXME: This is a HACK to allow the DBLP update to set the date of 
		 * (bookmark) posts. The problem is, that the date format is now fixed 
		 * for ALL our controllers, since we can't override this initBinder
		 * method (since we're using this MinimalisticController ... wrapper)
		 *  
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DATE_FORMAT, true));
		
		/*
		 * setting the dis/allowed fields for the binder
		 */
		binder.setAllowedFields(allowedFields);
		binder.setDisallowedFields(disallowedFields);
	}	
}
