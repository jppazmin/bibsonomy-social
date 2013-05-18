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

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jabref.export.layout.Layout;
import net.sf.jabref.export.layout.LayoutHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.util.file.FileUtil;
import org.springframework.beans.factory.annotation.Required;

/**
 * Holds and manages the available jabref layouts.
 * 
 * @author:  rja
 * @version: $Id: JabrefLayouts.java,v 1.21 2011-04-29 07:34:00 bibsonomy Exp $
 * $Author: bibsonomy $
 * 
 */
public class JabrefLayouts {

	/*
	 * copied from JabRefs Globals
	 */
	private static final String GLOBALS_FORMATTER_PACKAGE = "net.sf.jabref.export.layout.format.";

	private static final Log log = LogFactory.getLog(JabrefLayouts.class);

	/**
	 * One layout my consist of several files - e.g., sublayouts. These are 
	 * the possible (typical) sublayouts. They're used as part of the file 
	 * name.
	 */
	private static final String[] subLayouts = new String[] {
		"", 											     /* the default layout - should always exist; renders one entry */
		"." + LayoutPart.BEGIN.name().toLowerCase(), 	     /* the beginning - is added to the beginning of the rendered entries */
		"." + LayoutPart.EMBEDDEDBEGIN.name().toLowerCase(), /* the beginning - for embedded layouts */
		"." + LayoutPart.END.name().toLowerCase(),			 /* the end - is added to the end of the rendered entries */
		"." + LayoutPart.EMBEDDEDEND.name().toLowerCase(),	 /* the end - for embedded layouts */
		".article",								             /* ****************************************************** */ 
		".inbook",								             /* the remaining sublayouts are for different entry types */
		".book",
		".booklet",
		".incollection",
		".conference",
		".inproceedings",
		".proceedings",
		".manual",
		".mastersthesis",
		".phdthesis",
		".techreport",
		".unpublished",
		".patent",
		".periodical",
		".presentation",
		".preamble",
		".standard",
		".electronic",
		".periodical",
		".misc",
		".other"
	};

	/**
	 * Configured by the setter: the path where the user layout files are.
	 */
	private String userLayoutFilePath;
	/**
	 * Can be configured by the setter: the path where the default layout files are.
	 */
	private String defaultLayoutFilePath = "org/bibsonomy/layout/jabref";
	/**
	 * saves all loaded layouts (html, bibtexml, tablerefs, hash(user.username), ...)
	 */
	private Map<String,JabrefLayout> layouts;

	/** Initialize the layouts by loading them into a map.
	 * 
	 * @throws IOException
	 */
	protected void init() throws IOException {
		loadDefaultLayouts();
	}

	/**
	 * Loads default filters (xxx.xxx.layout and xxx.layout) from the default layout directory into a map.
	 * 
	 * @throws IOException 
	 */
	private void loadDefaultLayouts() throws IOException {
		/*
		 * create a new hashmap to store the layouts
		 */
		layouts = new LinkedHashMap<String, JabrefLayout>();
		/*
		 * load layout definition from XML file
		 */
		final List<JabrefLayout> jabrefLayouts = new XMLJabrefLayoutReader(new BufferedReader(new InputStreamReader(JabrefLayoutUtils.getResourceAsStream(defaultLayoutFilePath + "/" + "JabrefLayouts.xml"), "UTF-8"))).getJabrefLayoutsDefinitions();
		log.info("found " + jabrefLayouts.size() + " layout definitions");
		/*
		 * iterate over all layout definitions
		 */
		for (final JabrefLayout jabrefLayout : jabrefLayouts) {
			log.debug("loading layout " + jabrefLayout.getName());
			final String filePath = defaultLayoutFilePath + "/" + getDirectory(jabrefLayout.getDirectory());
			/*
			 * iterate over all subLayouts
			 */
			for (final String subLayout: subLayouts) {
				final String fileName = filePath + jabrefLayout.getBaseFileName() + subLayout + JabrefLayoutUtils.layoutFileExtension;
				log.debug("trying to load sublayout " + fileName + "...");
				final Layout layout = loadLayout(fileName);
				if (layout != null) {
					log.debug("... success!");
					jabrefLayout.addSubLayout(subLayout, layout);
				}
			}
			layouts.put(jabrefLayout.getName(), jabrefLayout);
		}
		log.info("loaded " + layouts.size() + " layouts");
	}

	/** Loads a layout from the given location. 
	 * 
	 * @param fileLocation - the location of the file, such that it can be found by the used class loader.
	 * @return The loaded layout, or <code>null</code> if it could not be found.
	 * @throws IOException
	 */
	private Layout loadLayout(final String fileLocation) throws IOException {
		final InputStream resourceAsStream = JabrefLayoutUtils.getResourceAsStream(fileLocation);
		if (resourceAsStream != null) {
			/*
			 * give file to layout helper
			 */
			final LayoutHelper layoutHelper = new LayoutHelper(new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8")));
			/*
			 * load layout
			 */
			try {
				return layoutHelper.getLayoutFromText(GLOBALS_FORMATTER_PACKAGE);
			} catch (Exception e) {
				log.error("Error while trying to load layout " + fileLocation + " : " + e.getMessage());
				throw new IOException(e);
			}
		} 
		return null;
	}

	/** Create string for directories. If no given, the string is empty.
	 * @param directory
	 * @return
	 */
	private String getDirectory(final String directory) {
		if (directory == null) return "";
		return directory + "/";
	}

	/** Returns the requested layout. This is for layouts which don't have item parts for specific publication types. 
	 * 
	 * @param layout
	 * @return
	 */
	protected JabrefLayout getLayout(final String layout) {
		return layouts.get(layout);
	}

	/** Removes all filters from the cache and loads the default filters.
	 * @throws IOException
	 */
	protected void resetFilters() throws IOException {
		loadDefaultLayouts();
	}

	/**
	 * Loads user filter from file into a map.
	 * 
	 * @param userName The user who requested a filter
	 * 
	 * @throws IOException 
	 */
	private void loadUserLayout(final String userName) throws IOException {
		/*
		 * initialize a new user layout
		 */
		final JabrefLayout jabrefLayout = new JabrefLayout(JabrefLayoutUtils.userLayoutName(userName));
		jabrefLayout.setDescription("en", "Custom layout of user " + userName);
		jabrefLayout.setDisplayName("custom");
		jabrefLayout.setMimeType("text/html"); // FIXME: this should be adaptable by the user ...
		jabrefLayout.setUserLayout(true);
		jabrefLayout.setPublicLayout(false);

		/*
		 * iterate over layout parts (.begin, .item, .end)
		 */
		for (final LayoutPart layoutPart : LayoutPart.layoutParts) {
			final String hashedName = JabrefLayoutUtils.userLayoutHash(userName, layoutPart);
			final File file = new File(FileUtil.getFileDir(userLayoutFilePath, hashedName) + hashedName);

			log.debug("trying to load custom user layout (part " + layoutPart + ") for user " + userName + " from file " + file);

			if (file.exists()) {
				log.debug("custom layout (part '" + layoutPart + "') found!");
				final LayoutHelper layoutHelper = new LayoutHelper(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")));
				try {
					jabrefLayout.addSubLayout(layoutPart, layoutHelper.getLayoutFromText(GLOBALS_FORMATTER_PACKAGE));
				} catch (final Exception e) {
					/*
					 * unfortunately, layoutHelper.getLayoutFromText throws a generic Exception, 
					 * so we catch it here
					 */
					throw new IOException ("Could not load layout: " + e.getMessage());
				}
			}
		}
		/*
		 * we add the layout only to the map, if it is complete, i.e., it contains an item layout
		 */
		if (jabrefLayout.getSubLayout(LayoutPart.ITEM) != null) {
			/*
			 * add user layout to map
			 */
			log.debug("user layout contains 'item' part - loading it");
			synchronized(layouts) {
				layouts.put(jabrefLayout.getName(), jabrefLayout);
			}
		}
	}

	@Override
	public String toString() {
		return layouts.toString();
	}

	/** Returns the layout for the given user. If no layout could be found, <code>null</code>
	 * is returned instead of throwing an exception. This allows for missing parts (i.e., 
	 * no begin.layout).
	 * 
	 * @param userName
	 * @return
	 */
	protected JabrefLayout getUserLayout(final String userName) {
		/*
		 * check if custom filter exists
		 */
		final String userLayoutName = JabrefLayoutUtils.userLayoutName(userName);
		if (present(userName) && !layouts.containsKey(userLayoutName)) {
			/*
			 * custom filter of current user is not loaded yet -> check if a filter exists at all
			 */
			try {
				loadUserLayout(userName);
			} catch (final IOException e) {
				log.info("Error loading custom filter for user " + userName, e);
			}
		}
		return layouts.get(userLayoutName);
	}

	/**
	 * Unloads the custom layout of the given user. Note that all parts
	 * of the layout are unloaded! 
	 * 
	 * @param userName - the name of the user 
	 */
	protected void unloadCustomFilter(final String userName) {
		synchronized(layouts) {
			layouts.remove(JabrefLayoutUtils.userLayoutName(userName));
		}
	}

	/** The path where the user layout files are.
	 * 
	 * @param userLayoutFilePath
	 */
	@Required
	protected void setUserLayoutFilePath(String userLayoutFilePath) {
		this.userLayoutFilePath = userLayoutFilePath;
	}

	/**
	 * The path where the default layout files are. Defaults to <code>layouts</code>.
	 * Must be accessible by the classloader.
	 * 
	 * @param defaultLayoutFilePath
	 */
	protected void setDefaultLayoutFilePath(String defaultLayoutFilePath) {
		this.defaultLayoutFilePath = defaultLayoutFilePath;
	}
	
	/**
	 * Returns a map with all layouts
	 * 
	 * @return Map with all Layouts
	 */
	protected Map<String, JabrefLayout> getLayoutMap(){
		return this.layouts;
	}

}

