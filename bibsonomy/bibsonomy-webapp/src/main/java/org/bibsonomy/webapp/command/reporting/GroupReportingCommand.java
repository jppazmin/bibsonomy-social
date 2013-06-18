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

package org.bibsonomy.webapp.command.reporting;

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * Reporting command for a group.
 * 
 * FIXME: This is just a starting point.
 * 
 * @author dbenz
 * @version $Id: GroupReportingCommand.java,v 1.5 2010-05-28 13:58:32 nosebrain Exp $
 */
public class GroupReportingCommand extends BaseCommand {

	/** holds the table with the publication counts per type */ 
	private final ReportingTableCommand<Integer, String, Integer> publicationCounts = new ReportingTableCommand<Integer, String, Integer>();
	
	/** the name of the requested group */
	private String requestedGroup = "";

	/** transposes displayed matrix if is set to 1 */
	private String transposeMatrix = "";

	private String requestedTags;

	/**
	 * @return the requestedGroup
	 */
	public String getRequestedGroup() {
		return this.requestedGroup;
	}

	/**
	 * @param requestedGroup the requestedGroup to set
	 */
	public void setRequestedGroup(String requestedGroup) {
		this.requestedGroup = requestedGroup;
	}

	/**
	 * @return the transposeMatrix
	 */
	public String getTransposeMatrix() {
		return this.transposeMatrix;
	}

	/**
	 * @param transposeMatrix the transposeMatrix to set
	 */
	public void setTransposeMatrix(String transposeMatrix) {
		this.transposeMatrix = transposeMatrix;
	}

	/**
	 * @return the requestedTags
	 */
	public String getRequestedTags() {
		return this.requestedTags;
	}

	/**
	 * @param requestedTags the requestedTags to set
	 */
	public void setRequestedTags(String requestedTags) {
		this.requestedTags = requestedTags;
	}

	/**
	 * @return the publicationCounts
	 */
	public ReportingTableCommand<Integer, String, Integer> getPublicationCounts() {
		return this.publicationCounts;
	}
	
}
