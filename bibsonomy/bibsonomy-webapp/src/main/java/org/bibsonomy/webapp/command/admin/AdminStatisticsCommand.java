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

package org.bibsonomy.webapp.command.admin;

import java.util.HashMap;

/**
 * Presents status information of spam framework, for example, how many 
 * spammers have been flagged recently
 * @author sts
 * @author bkr
 * @version $Id: AdminStatisticsCommand.java,v 1.1 2009-11-18 14:29:15 beatekr Exp $
 */
public class AdminStatisticsCommand {

	private final HashMap<Long, Integer> numAdminSpammer = new HashMap<Long, Integer>();
	
	private final HashMap<Long, Integer> numAdminNoSpammer = new HashMap<Long, Integer>();
	
	private final HashMap<Long, Integer> numClassifierSpammer = new HashMap<Long, Integer>();
	
	private final HashMap<Long, Integer> numClassifierSpammerUnsure = new HashMap<Long, Integer>();
	
	private final HashMap<Long, Integer> numClassifierNoSpammer = new HashMap<Long, Integer>();
	
	private final HashMap<Long, Integer> numClassifierNoSpammerUnsure = new HashMap<Long, Integer>();
	
	public HashMap<Long, Integer> getNumAdminSpammer() {
		return this.numAdminSpammer;
	}

	public void setNumAdminSpammer(Long interval, int counts) {
		this.numAdminSpammer.put(interval, counts);
	}

	public HashMap<Long, Integer> getNumAdminNoSpammer() {
		return this.numAdminNoSpammer;
	}

	public void setNumAdminNoSpammer(Long interval, int counts) {
		this.numAdminNoSpammer.put(interval, counts);
	}

	public HashMap<Long, Integer> getNumClassifierSpammer() {
		return this.numClassifierSpammer;
	}

	public void setNumClassifierSpammer(Long interval, int counts) {
		this.numClassifierSpammer.put(interval, counts);
	}

	public HashMap<Long, Integer> getNumClassifierSpammerUnsure() {
		return this.numClassifierSpammerUnsure;
	}

	public void setNumClassifierSpammerUnsure(Long interval, int counts) {
		this.numClassifierSpammerUnsure.put(interval, counts);
	}

	public HashMap<Long, Integer> getNumClassifierNoSpammer() {
		return this.numClassifierNoSpammer;
	}

	public void setNumClassifierNoSpammer(Long interval, int counts) {
		this.numClassifierNoSpammer.put(interval, counts);
	}

	public HashMap<Long, Integer> getNumClassifierNoSpammerUnsure() {
		return this.numClassifierNoSpammerUnsure;
	}

	public void setNumClassifierNoSpammerUnsure(Long interval, int counts) {
		this.numClassifierNoSpammerUnsure.put(interval, counts);
	}	
}