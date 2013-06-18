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

package org.bibsonomy.webapp.command;

import org.bibsonomy.util.EnumUtils;
import org.bibsonomy.webapp.util.RankingUtil.RankingMethod;

/**
 * Command to hold information about ranking
 * 
 * @author dbenz
 * @version $Id: RankingCommand.java,v 1.2 2010-05-11 12:47:59 nosebrain Exp $
 */
public class RankingCommand {
	
	/** 
	 * the ranking period; starting from 1:
	 *   - 1: the most recent 1000 posts
	 *   - 2: most recent 1001 to 2000
	 *   - 3: most recent 2001 to 3000... 
	*/	
	private Integer period = 0;
	/**
     * Start-/End values for ranking periods
    */ 	
	private Integer periodStart;
	private Integer periodEnd;
	/**
	 * the ranking method used
	 */
	private RankingMethod method = RankingMethod.TFIDF;
	/**
	 * whether to normalize the ranking or not
	 */
	private boolean normalize = false;
	
	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return this.period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}

	/**
	 * @return the periodStart
	 */
	public Integer getPeriodStart() {
		return this.periodStart;
	}

	/**
	 * @param periodStart the periodStart to set
	 */
	public void setPeriodStart(Integer periodStart) {
		this.periodStart = periodStart;
	}

	/**
	 * @return the periodEnd
	 */
	public Integer getPeriodEnd() {
		return this.periodEnd;
	}

	/**
	 * @param periodEnd the periodEnd to set
	 */
	public void setPeriodEnd(Integer periodEnd) {
		this.periodEnd = periodEnd;
	}

	/**
	 * @return the name of the {@link #method} (lower case)
	 */
	public String getMethod() {
		return this.method.name().toLowerCase();
	}
	
	/**
	 * @return the {@link #method}
	 */
	public RankingMethod getMethodObj() {
		return this.method;
	}
	
	/**
	 * @param method the name of the method to set
	 */
	public void setMethod(String method) {
		if (method != null) {
			RankingMethod newMethod = EnumUtils.searchEnumByName(RankingMethod.values(), method);
			if (newMethod != null) {
				this.method = newMethod;
			}
		}
	}
	
	/**
	 * @return the normalize
	 */
	public boolean getNormalize() {
		return this.normalize;
	}
	
	/**
	 * @param normalize the normalize to set
	 */
	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	/**
	 * @return the next period
	 */
	public Integer getNextPeriod() {
		if (this.period == null) {
			return 1;
		}
		return this.period + 1;
	}
	
	/**
	 * @return the previous period
	 */
	public Integer getPrevPeriod() {
		if (this.period == null || this.period == 0) {
			return 0;
		}
		return this.period - 1;
	}
}
