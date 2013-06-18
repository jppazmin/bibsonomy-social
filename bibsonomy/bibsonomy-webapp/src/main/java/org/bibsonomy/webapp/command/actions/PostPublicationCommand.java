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

package org.bibsonomy.webapp.command.actions;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.TabCommand;
import org.bibsonomy.webapp.command.TabsCommandInterface;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * This command takes a the information for displaying the import publication views. 
 * The publications will be entered as a file or string containing the information.
 * 
 * @author ema
 * @version $Id: PostPublicationCommand.java,v 1.20 2010-07-14 15:38:38 nosebrain Exp $
 */
public class PostPublicationCommand extends EditPublicationCommand implements TabsCommandInterface<Object> {
	
	/**
	 * The URL which the tab header links to.
	 */
	private static final String TAB_URL = "/postPublication"; 
	
	/* ***************************
	 * FOR THE TAB FUNCTIONALITY
	 * ***************************/
	
	/**
	 * TAB HEADER LOCALIZED
	 */
	private final static String[] tabTitles = {
		"post_bibtex.manual.title", 
		"post_bibtex.pub_snippet.title", 
		"post_bibtex.bibtex_endnote.title", 
		"post_bibtex.doi_isbn.title"
	};
	
	/**
	 * stores if the user wants to overwrite existing posts 
	 */
	private boolean overwrite;
	
	/**
	 *  id of currently selected tab 
	 */
	protected Integer selTab = null;
	
	/**
	 * holds the tabcommands, containing tuples of the number of a tab and the message key
	 * representing the clickable textheader of the corresponding tab. 
	 */
	private List<TabCommand> tabs;
	
	/**
	 * the description of the snippet/upload file
	 */
	private String description;
	
	/**
	 * constructor
	 * inits the tabs and sets their titles
	 */
	public PostPublicationCommand() {
		tabs = new ArrayList<TabCommand>();
		// Preparation for all tabs
		//=== make the tabtitle available
		this.addTabs(tabTitles);

		//=== change default tab to the manual tab
		
		if (!present(selTab))
			selTab = 0;
		
		this.setTabURL(TAB_URL);
		
		/*
		 * defaults:
		 */
		this.whitespace = "_";
	}
	
	/**
	 * @return The index of the currently selected tab.
	 */
	@Override
	public Integer getSelTab() {
		return selTab;
	}
	
	/**
	 * @param selectedTab 
	 */
	@Override
	public void setSelTab(final Integer selectedTab) {
		this.selTab = selectedTab;
	}
	
	/**
	 * @return the tabcommands (tabs)
	 */
	@Override
	public List<TabCommand> getTabs() {
		return tabs;
	}

	/**
	 * *not used in general*
	 * @param tabs the tabcommands (tabs) 
	 */
	@Override
	public void setTabs(final List<TabCommand> tabs) {
		this.tabs = tabs;
	}
	
	/**
	 * @param id the index of the new tab to add
	 * @param title the message key of the tab to add (clickable text header)
	 */
	private void addTab(final Integer id, final String title) {
		tabs.add(new TabCommand(id, title));		
	}

	/**
	 * @param titles the message keys of the tabs to add (clickable text header)
	 */
	private void addTabs(final String[] titles) {
		for (int i = 0; i < titles.length; i++) {
			addTab(i, titles[i]);
		}
	}

	/**
	 * URL of the tabheader-anchor-links 
	 * (needed, because postPublication calls this site first, but tabs-hrefs have to be...
	 * ... import/publications)
	 */
	private String tabURL;
	
	/**
	 * @return the url of the tabbed site
	 */
	public String getTabURL() {
		return this.tabURL;
	}

	/**
	 * @param tabURL the url of the tabbed site
	 */
	public void setTabURL(final String tabURL) {
		this.tabURL = tabURL;
	}
	

	/****************************
	 * FOR ALL IMPORTS
	 ****************************/
	

	/**
	 * this flag determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	private boolean deleteCheckedPosts;
	
	/**
	 * @return the flag that determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	public boolean getDeleteCheckedPosts() {
		return this.deleteCheckedPosts;
	}

	/**
	 * @return the flag that determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	public boolean isDeleteCheckedPosts() {
		return this.deleteCheckedPosts;
	}
	
	/**
	 * @param deleteCheckedPosts the flag that determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	public void setDeleteCheckedPosts(final boolean deleteCheckedPosts) {
		this.deleteCheckedPosts = deleteCheckedPosts;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/****************************
	 * SPECIAL FOR FILE UPLOAD
	 ****************************/

	/**
	 * the BibTeX/Endnote file
	 */
	private CommonsMultipartFile file;
	
	/**
	 * @return the file
	 */
	public CommonsMultipartFile getFile() {
		return this.file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}

	/**
	 * The whitespace substitute
	 */
	private String whitespace;
	
	/**
	 * @return the whitespace
	 */
	public String getWhitespace() {
		return this.whitespace;
	}

	/**
	 * @param whitespace the whitespace to set
	 */
	public void setWhitespace(String whitespace) {
		this.whitespace = whitespace;
	}

	/**
	 * encoding of the file
	 */
	private String encoding;
	
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return this.encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * the delimiter
	 */
	private String delimiter;
	
	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return this.delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Determines, if the bookmarks will be saved before being edited or afterwards
	 */
	private boolean editBeforeImport;
	
	/**
	 * @param editBeforeImport the editBeforeImport to set
	 */
	public void setEditBeforeImport(boolean editBeforeImport) {
		this.editBeforeImport = editBeforeImport;
	}

	/**
	 * @return the editBeforeImport
	 */
	public boolean isEditBeforeImport() {
		return this.editBeforeImport;
	}
	
	/**
	 * @return @see {@link #isEditBeforeImport()}
	 */
	public boolean getEditBeforeImport() {
		return this.editBeforeImport;
	}

	/**
	 * The posts, that were updated during import.
	 */
	private Map<String,String> updatedPosts;	

	/**
	 * @return the updatedPosts
	 */
	public Map<String, String> getUpdatedPosts() {
		return this.updatedPosts;
	}

	/**
	 * @param updatedPosts the updatedPosts to set
	 */
	public void setUpdatedPosts(Map<String, String> updatedPosts) {
		this.updatedPosts = updatedPosts;
	}

	/**
	 * For multiple posts
	 */
	private ListCommand<Post<BibTex>> posts = new ListCommand<Post<BibTex>>(this);

	/**
	 * @return The list of publication posts.
	 */
	public ListCommand<Post<BibTex>> getBibtex() {
		return this.posts;
	}

	/**
	 * @param bibtex
	 */
	public void setBibtex(final ListCommand<Post<BibTex>> bibtex) {
		this.posts = bibtex;
	}
	
	/**
	 * @return The list of publication posts.
	 */
	public ListCommand<Post<BibTex>> getPosts() {
		return this.posts;
	}

	/**
	 * @param bibtex
	 */
	public void setPosts(final ListCommand<Post<BibTex>> bibtex) {
		this.posts = bibtex;
	}
	
	@Override
	public List<Object> getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(final List<Object> content) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return this.overwrite;
	}
	
	/**
	 * @return @see {@link #isOverwrite()}
	 */
	public boolean getOverwrite() {
		return this.overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
}
