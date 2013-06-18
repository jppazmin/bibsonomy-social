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

package org.bibsonomy.webapp.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.LayoutRenderingException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Layout;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.services.renderer.LayoutRenderer;
import org.bibsonomy.webapp.command.SimpleResourceViewCommand;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.JstlView;

import tags.Functions;

/**
 * View which uses an {@link LayoutRenderer} to render the output.
 * 
 * @author rja
 * @version $Id: LayoutView.java,v 1.17 2010-11-11 13:52:38 nosebrain Exp $
 * @param <LAYOUT> 
 */
@SuppressWarnings("deprecation")
public class LayoutView<LAYOUT extends Layout> extends AbstractView {
	private static final Log log = LogFactory.getLog(LayoutView.class);

	private LayoutRenderer<LAYOUT> layoutRenderer;
	
	@Override
	protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		/*
		 * get the data
		 */
		final Object object = model.get(BaseCommandController.DEFAULT_COMMAND_NAME);
		if (object instanceof SimpleResourceViewCommand) {
			/*
			 * we can only handle SimpleResourceViewCommands ...
			 */
			final SimpleResourceViewCommand command = (SimpleResourceViewCommand) object;
			final String loginUserName = command.getContext().getLoginUser().getName();

			/*
			 * get requested layout
			 */
			final String layout = command.getLayout();
			final boolean formatEmbedded = command.getformatEmbedded(); 
			/*
			 * get the requested path
			 * we need it to generate the file names for inline content-disposition
			 * FIXME: The path is written into the request by the UrlRewriteFilter 
			 * ... probably this is not a good idea
			 */
			final String requPath = (String) request.getAttribute("requPath");

			log.info("rendering layout " + layout + " for user " + loginUserName + " with path " + requPath);

			/*
			 * 
			 */
			final List<Post<BibTex>> publicationPosts = command.getBibtex().getList();

			try {
				/*
				 * our basic (and only) renderer is the jabref layout renderer, which supports 
				 * only publications
				 */
				if (layoutRenderer.supportsResourceType(BibTex.class)) {
					/*
					 * render publication posts
					 */
					renderResponse(layout, requPath, publicationPosts, loginUserName, response, formatEmbedded);
				} else {
					/*
					 * we could not find a suitable renderer - this should never happen!
					 */
					throw new RuntimeException("No layout for publications renderer available.");
				}
			} catch (final LayoutRenderingException e) {
				log.error("Could not render layout " + layout + ": " + e.getMessage());
				/*
				 * layout could not be found or contains errors -> set HTTP status to 400
				 */
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				/*
				 * get the errors object and add the error message
				 */
				final BindingResult errors = ViewUtils.getBindingResult(model);
				errors.reject("error.layout.rendering", new Object[]{e.getMessage()}, "Could not render layout: " + e.getMessage());
				/*
				 * do the rendering ... a bit tricky: we need to get an appropriate JSTL view and give it the 
				 * application context
				 */
				final JstlView view = new JstlView("/WEB-INF/jsp/error.jspx");
				view.setApplicationContext(getApplicationContext());
				view.render(model, request, response);
				
			}
		} else {
			/*
			 * FIXME: what todo here?
			 */
		}
	}

	/**
	 * Renders the layout and prepares the response.
	 * 
	 * @param <T>
	 * @param layoutName
	 * @param requPath
	 * @param posts
	 * @param loginUserName
	 * @param response
	 * 
	 * @throws LayoutRenderingException
	 * @throws IOException
	 */
	private <T extends Resource> void renderResponse(final String layoutName, final String requPath, final List<Post<T>> posts, final String loginUserName, final HttpServletResponse response, final boolean formatEmbedded) throws LayoutRenderingException, IOException {
		final LAYOUT layout = layoutRenderer.getLayout(layoutName, loginUserName);

		log.info("got layout " + layout);
		/*
		 * First: do the real rendering, such that when an exception is thrown, we can forward to a JSP,
		 * since response.getOutputStream() hasn't been called yet,
		 */
		final StringBuffer buf = layoutRenderer.renderLayout(layout, posts, formatEmbedded);
		/*
		 * set the content type headers
		 */				
		response.setContentType(layout.getMimeType());
		response.setCharacterEncoding("UTF-8");
		final String extension = layout.getExtension();
		/*
		 * If an extension is given, which is differrent from ".html", this suggests to the 
		 * browser to show a file dialog.
		 */
		if (extension != null && !extension.trim().equals("") && !extension.equalsIgnoreCase(".html")) {
			response.setHeader("Content-Disposition", "attachement; filename=" + Functions.makeCleanFileName(requPath) + extension);
		}
		/*
		 * write the buffer to the response
		 */
		response.getOutputStream().write(buf.toString().getBytes("UTF-8"));
	}
	
	/**
	 * @return The layout renderer for this view.
	 */
	public LayoutRenderer<LAYOUT> getLayoutRenderer() {
		return this.layoutRenderer;
	}

	/** Set the layout renderer to be used by this view.
	 * 
	 * @param layoutRenderer
	 */
	@Required
	public void setLayoutRenderer(final LayoutRenderer<LAYOUT> layoutRenderer) {
		this.layoutRenderer = layoutRenderer;
	}

}
