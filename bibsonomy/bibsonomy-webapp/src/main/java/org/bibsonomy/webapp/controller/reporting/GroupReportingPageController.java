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

package org.bibsonomy.webapp.controller.reporting;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.util.SortUtils;
import org.bibsonomy.webapp.command.reporting.GroupReportingCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for group reporting pages.
 * 
 * FIXME: This is just a starting point.
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: GroupReportingPageController.java,v 1.12 2010-06-10 18:24:24 nosebrain Exp $
 */
public class GroupReportingPageController implements MinimalisticController<GroupReportingCommand> {
	
	/** logic interface */
	private LogicInterface logic;
	
	@Override
	public View workOn(GroupReportingCommand command) {
		// allow only logged-in users FIXME: check errormsg
		if (command.getContext().getLoginUser().getName() == null) {
			throw new MalformedURLSchemeException("Not logged in!");
		}
		
		// if no group given -> error FIXME: check errormsg
		if (command.getRequestedGroup() == null) {
			throw new MalformedURLSchemeException("error.group_page_without_groupname");
		}
		
		// if no tags given return FIXME: check errormsg
		if (!present(command.getRequestedTags())) {
			throw new MalformedURLSchemeException("error.tag_page_without_tag");
		}		
		
		/*
		 * assemble taglist
		 */
		List<String> tags = new ArrayList<String>();
		tags.add(command.getRequestedTags());
		
		/*
		 * fetch all bibtex & remove duplicates
		 */
		List<Post<BibTex>> groupBibtexEntries = logic.getPosts(BibTex.class, GroupingEntity.GROUP, command.getRequestedGroup(), tags, null, null, null, 0, 10000, null);
		BibTexUtils.removeDuplicates(groupBibtexEntries);
		
		
		/*
		 * sort entries in descending order by year
		 */
		BibTexUtils.sortBibTexList(groupBibtexEntries, SortUtils.parseSortKeys("year"), SortUtils.parseSortOrders("desc"));
		
		
		/*
		 * init entrytypes 
		 */
		for (String type : BibTexUtils.ENTRYTYPES) { command.getPublicationCounts().getColumnHeaders().add(type); }
		/*
		 * loop over entries, accumulate and fill command
		 */
		BibTex bib;
		int lastYear = Integer.MIN_VALUE;
		HashMap<String,Integer> row = null;
		for (Post<BibTex> post : groupBibtexEntries) {
			bib = post.getResource();
			try {
				int curYear = Integer.valueOf(bib.getYear());
				if ( curYear != lastYear) {
					if (lastYear != Integer.MIN_VALUE) {
						// write last row into command, if there is one
						command.getPublicationCounts().getValues().put(lastYear, row);
						command.getPublicationCounts().getRowHeaders().add(lastYear);
					}
					// init a new row with zero values
					row = new HashMap<String,Integer>();
					for (String type : BibTexUtils.ENTRYTYPES) {
						row.put(type, 0);
					}
				}
				// increment counter of type TYPE in current year
				this.increment(row, bib.getEntrytype());
				lastYear = curYear;
				
			} catch (NumberFormatException ex) {
				// ignore silently
			}
			
		}
		
		// add the last year
		if (lastYear != Integer.MIN_VALUE) {
			command.getPublicationCounts().getValues().put(lastYear, row);
			command.getPublicationCounts().getRowHeaders().add(lastYear);
		}
		
		/*
		 *  create some dummy data for testing 
		 *  FIXME: replace this by fetching the data from the logic instead!
		 */		
//		final int[] dummyYears = {2009,2008,2007,2006,2005};
//		final String[] dummyTypes = Bibtex.entrytypes;
//		int dummyValue = 23;
//		
//		// initialize column + row headings
//		for (String type : dummyTypes) { command.getPublicationCounts().getColumnHeaders().add(type); }
//		for (Integer year : dummyYears) { command.getPublicationCounts().getRowHeaders().add(year); }		
//		
//		// init values
//		HashMap<String,Integer> row2;
//		for (int year : dummyYears) {
//			row2 = new HashMap<String,Integer>();			
//			// write row values
//			for (String type : dummyTypes) {
//				row2.put(type, dummyValue++);
//			}			
//			// store row in command
//			command.getPublicationCounts().getValues().put(year, row);
//		}
						
		
		// TODO: add totals: sum up values for each years 
		
		return Views.REPORTING;
	}
	
	/**
	 * @return the logic
	 */
	public LogicInterface getLogic() {
		return this.logic;
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(LogicInterface logic) {
		this.logic = logic;
	}

	@Override
	public GroupReportingCommand instantiateCommand() {
		return new GroupReportingCommand();
	}
	
	/*
	 * increment map at position 'key'
	 */
	private void increment(Map<String, Integer> map, String key) {
		if (key == null || !map.containsKey(key.toLowerCase())) {
			return;
		}
		int lastVal = map.get(key);
		map.put(key, lastVal + 1);
	}

}
