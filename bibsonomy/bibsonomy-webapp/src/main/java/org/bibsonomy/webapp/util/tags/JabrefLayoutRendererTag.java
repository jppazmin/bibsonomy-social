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

package org.bibsonomy.webapp.util.tags;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.LayoutRenderingException;
import org.bibsonomy.layout.jabref.JabrefLayout;
import org.bibsonomy.layout.jabref.JabrefLayoutRenderer;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * a jsp tag that prints the rendered layout of the configured
 * JabRefLayoutRenderer in bibsonomy2-servlet.xml
 * 
 * @author dzo
 * @version $Id: JabrefLayoutRendererTag.java,v 1.2 2011-06-21 13:56:22 nosebrain Exp $
 */
public class JabrefLayoutRendererTag extends TagSupport {
	private static final String SUPPORTED_EXTENSION = ".html";

	private static final long serialVersionUID = 8006189027834637063L;
	
	private static final Log log = LogFactory.getLog(JabrefLayoutRendererTag.class);
	
	// TODO: move
	private static final String SERVLET_CONTEXT_PATH = "org.springframework.web.servlet.FrameworkServlet.CONTEXT.bibsonomy2";

	/**
	 * the posts to render
	 */
	private List<Post<? extends Resource>> posts;
	
	private String layout;

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(this.renderPosts());		
		} catch (final IOException ex) {
			throw new JspException("Error: IOException while writing to client" + ex.getMessage());
		}
		
		return super.doStartTag();
	}
	
	private String renderPosts() {
		if (present(this.posts)) {
			final JabrefLayoutRenderer renderer = this.getJabRefLayoutRenderer();
			try {
				final JabrefLayout layout = renderer.getLayout(this.layout, "");
				if (!SUPPORTED_EXTENSION.equals(layout.getExtension())) {
					return "The requested layout is not valid; only HTML layouts are allowed. Requested extension is: " + layout.getExtension();
				}
				return renderer.renderLayout(layout, posts, true).toString();
			} catch (final LayoutRenderingException ex) {
				log.error(ex.getMessage());
				return ex.getMessage();			
			} catch (final UnsupportedEncodingException ex) {
				log.error(ex.getMessage());
				return "An Encoding error occured while trying to convert to layout '" + layout  + "'.";
			} catch (final IOException ex) {
				log.error(ex.getMessage());
				return "An I/O error occured while trying to convert to layout '" + layout  + "'."; 
			} catch (final Exception ex) {
				log.error(ex.getMessage());
				return "A unknown error occured while processing the layout " + layout + ".";
			}
		}
		return "";
	}
	
	/**
	 * @return the configured jabref layout renderer in bibsonomy2-servlet.xml
	 * this requires the {@link ContextLoader} configured in web.xml
	 */
	private JabrefLayoutRenderer getJabRefLayoutRenderer() {
		final ServletContext servletContext = this.pageContext.getServletContext();
        final WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext, SERVLET_CONTEXT_PATH);
        final Map<String, JabrefLayoutRenderer> renderer = ctx.getBeansOfType(JabrefLayoutRenderer.class);
        
        return renderer.values().iterator().next();
	}
	
	/**
	 * @param posts the posts to set
	 */
	public void setPosts(final List<Post<? extends Resource>> posts) {
		this.posts = posts;
	}
	
	/**
	 * @param post the post to set
	 */
	public void setPost(final Post<? extends Resource> post) {
		this.posts = Collections.<Post<? extends Resource>>singletonList(post);
	}
	
	/**
	 * @param layout the layout to set
	 */
	public void setLayout(final String layout) {
		this.layout = layout;
	}
}
