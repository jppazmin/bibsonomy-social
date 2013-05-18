/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
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

package org.bibsonomy.layout.jabref;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.KeyCollisionException;
import net.sf.jabref.export.layout.Layout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.LayoutRenderingException;
import org.bibsonomy.layout.util.JabRefModelConverter;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.services.URLGenerator;
import org.bibsonomy.services.renderer.LayoutRenderer;
import org.springframework.beans.factory.annotation.Required;

/**
 * This renderer handles jabref layouts.
 * 
 * @author: rja
 * @version: $Id: JabrefLayoutRenderer.java,v 1.38 2011-06-09 09:21:48 nosebrain
 *           Exp $ $Author: nosebrain $
 * 
 */
public class JabrefLayoutRenderer implements LayoutRenderer<JabrefLayout> {
	private static final Log log = LogFactory
			.getLog(JabrefLayoutRenderer.class);

	private URLGenerator urlGenerator;

	/**
	 * saves all loaded layouts (html, bibtexml, tablerefs, hash(user.username),
	 * ...)
	 */
	private final JabrefLayouts layouts = new JabrefLayouts();

	/**
	 * constructs a new jabref layout renderer
	 */
	public JabrefLayoutRenderer() {
		this.init();
	}

	/**
	 * Initializes the bean by loading default layouts.
	 */
	private void init() {
		/*
		 * initialize JabRef preferences. This is neccessary ... because they
		 * use global preferences and if we don't initialize them, we get
		 * NullPointerExceptions later
		 */
		Globals.prefs = JabRefPreferences.getInstance();

		// load default filters
		try {
			layouts.init();
		} catch (final IOException e) {
			log.fatal("Could not load default layout filters.", e);
		}
	}

	/**
	 * Returns the requested layout.
	 * 
	 * @see org.bibsonomy.services.renderer.LayoutRenderer#getLayout(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public JabrefLayout getLayout(final String layout,
			final String loginUserName) throws LayoutRenderingException,
			IOException {
		final JabrefLayout jabrefLayout;
		if ("custom".equals(layout)) {
			/*
			 * get custom user layout from map
			 */
			jabrefLayout = layouts.getUserLayout(loginUserName);
		} else {
			/*
			 * get standard layout
			 */
			jabrefLayout = layouts.getLayout(layout);
		}
		/*
		 * no layout found -> LayoutRenderingException
		 */
		if (jabrefLayout == null) {
			throw new LayoutRenderingException("Could not find layout '"
					+ layout + "' for user '" + loginUserName + "'");
		}
		return jabrefLayout;

	}

	/**
	 * Renders the posts with the given layout.
	 * 
	 * @see org.bibsonomy.services.renderer.LayoutRenderer#renderLayout(org.bibsonomy.model.Layout,
	 *      java.util.List, boolean)
	 */
	@Override
	public StringBuffer renderLayout(final JabrefLayout layout,
			final List<? extends Post<? extends Resource>> posts,
			final boolean embeddedLayout) throws LayoutRenderingException,
			IOException {
		log.debug("rendering " + posts.size() + " posts with "
				+ layout.getName() + " layout");
		/*
		 * XXX: different handling of "duplicates = no" in new code: old code:
		 * duplicate removal and sorting by year, only for layouts, done in
		 * layout renderer new code: duplicate removal in controller, no sorting
		 * by year - must be enforced by another parameter
		 */
		final BibtexDatabase database = bibtex2JabrefDB(posts);
		/*
		 * render the database
		 */
		return renderDatabase(database,
				JabRefModelConverter.convertPosts(posts, urlGenerator), layout,
				embeddedLayout);
	}

	/**
	 * This is the export method for BibTeX entries to any available format.
	 * 
	 * @param postList
	 *            Entries to export.
	 * @param userName
	 *            User to whom the passed entries belong
	 * @param layout
	 *            - the layout to be rendered. If "custom", export with user
	 *            specific layout filter
	 * @param embeddedLayout
	 *            - if <code>true</code> the corresponding embedded begin/end
	 *            parts (see {@link LayoutPart}) are used (only if available).
	 * @return output The formatted BibTeX entries as a string.
	 * @throws LayoutRenderingException
	 *             - if a layout could not be found
	 */
	private StringBuffer renderDatabase(final BibtexDatabase database,
			final List<BibtexEntry> sorted, final JabrefLayout layout,
			final boolean embeddedLayout) throws LayoutRenderingException {
		final StringBuffer output = new StringBuffer();

		/*
		 * *************** rendering the header *****************
		 */
		Layout beginLayout = null;
		/*
		 * first: try embedded begin layout, if requested.
		 */
		if (embeddedLayout) {
			beginLayout = layout.getSubLayout(LayoutPart.EMBEDDEDBEGIN);
		}
		/*
		 * second: if not available, take normal begin layout
		 */
		if (beginLayout == null) {
			beginLayout = layout.getSubLayout(LayoutPart.BEGIN);
		}
		/*
		 * third: render, if layout found
		 */
		if (beginLayout != null) {
			output.append(beginLayout.doLayout(database, "UTF-8"));
		}

		/*
		 * *************** rendering the entries *****************
		 */
		if (layout.isUserLayout()) {
			/*
			 * render custom user layout
			 */
			final Layout itemLayout = layout.getSubLayout(LayoutPart.ITEM);
			if (itemLayout == null) {
				/*
				 * no layout for user found -> throw an exception
				 */
				throw new LayoutRenderingException("no custom layout found");
			}
			for (final BibtexEntry entry : sorted) {
				output.append(itemLayout.doLayout(entry, database));
			}

		} else {
			// try to retrieve type-specific layouts and process output
			for (final BibtexEntry entry : sorted) {

				// We try to get a type-specific layout for this entry
				// FIXME: adding the dot "." here isn't so nice ...
				Layout itemLayout = layout.getSubLayout("."
						+ entry.getType().getName().toLowerCase());
				if (itemLayout == null) {
					/*
					 * try to get a generic layout
					 */
					itemLayout = layout.getSubLayout("");
					if (itemLayout == null) {
						/*
						 * no layout found -> throw an exception
						 */
						throw new LayoutRenderingException(
								"layout file(s) for '" + layout.getName()
										+ "' could not be found");
					}
				}
				output.append(itemLayout.doLayout(entry, database));
			}

		}

		/*
		 * *************** rendering the footer *****************
		 */
		Layout endLayout = null;
		/*
		 * first: try embedded end layout, if requested.
		 */
		if (embeddedLayout) {
			endLayout = layout.getSubLayout(LayoutPart.EMBEDDEDEND);
		}
		/*
		 * second: if not available, take normal begin layout
		 */
		if (endLayout == null) {
			endLayout = layout.getSubLayout(LayoutPart.END);
		}
		/*
		 * third: render, if layout found
		 */
		if (endLayout != null) {
			output.append(endLayout.doLayout(database, "UTF-8"));
		}

		return output;
	}

	/**
	 * This method converts BibSonomy BibTeX entries to JabRef entries and
	 * stores them into a JabRef specific BibtexDatabase!
	 * 
	 * @param bibtexList
	 *            List of BibSonomy BibTeX objects
	 * @return BibtexDatabase
	 * @throws IOException
	 * @throws KeyCollisionException
	 *             If two entries have exactly the same BibTeX key
	 */
	private BibtexDatabase bibtex2JabrefDB(
			final List<? extends Post<? extends Resource>> bibtexList) {
		final BibtexDatabase db = new BibtexDatabase();
		for (final Post<? extends Resource> post : bibtexList) {
			db.insertEntry(JabRefModelConverter.convertPost(post, urlGenerator));
		}
		return db;
	}

	/**
	 * Prints the loaded layouts.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return layouts.toString();
	}

	/**
	 * @param urlGen
	 *            the urlGen to set
	 */
	public void setUrlGenerator(final URLGenerator urlGen) {
		this.urlGenerator = urlGen;
	}

	/**
	 * The path where the user layout files are.
	 * 
	 * @param userLayoutFilePath
	 */
	@Required
	public void setUserLayoutFilePath(final String userLayoutFilePath) {
		layouts.setUserLayoutFilePath(userLayoutFilePath);
	}

	/**
	 * The path where the default layout files are. Defaults to
	 * <code>layouts</code>. Must be accessible by the classloader.
	 * 
	 * @param defaultLayoutFilePath
	 */
	public void setDefaultLayoutFilePath(final String defaultLayoutFilePath) {
		layouts.setDefaultLayoutFilePath(defaultLayoutFilePath);
	}

	/**
	 * This renderer only supports {@link BibTex}.
	 * 
	 * @see org.bibsonomy.services.renderer.LayoutRenderer#supportsResourceType(java.lang.Class)
	 */
	@Override
	public boolean supportsResourceType(final Class<? extends Resource> clazz) {
		return BibTex.class.equals(clazz);
	}

	/**
	 * Unloads the custom layout of the user. Note that all parts of the layout
	 * are unloaded!
	 * 
	 * @param userName
	 */
	public void unloadUserLayout(final String userName) {
		layouts.unloadCustomFilter(userName);
	}

	/**
	 * Use this method to get all layouts
	 * 
	 * @return all layouts
	 */
	public Map<String, JabrefLayout> getJabrefLayouts() {
		return this.layouts.getLayoutMap();
	}

}
