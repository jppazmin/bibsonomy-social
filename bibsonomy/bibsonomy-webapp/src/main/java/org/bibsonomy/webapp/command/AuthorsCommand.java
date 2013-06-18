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

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.common.enums.TagCloudSort;
import org.bibsonomy.common.enums.TagCloudStyle;
import org.bibsonomy.model.Author;

/**
 * @author Christian Claus
 * @version $Id: AuthorsCommand.java,v 1.3 2010-04-28 15:33:23 nosebrain Exp $
 */
public class AuthorsCommand extends BaseCommand {	
	private static final int DEFAULT_MAX_FREQ = 100;
	
	
	private List<Author> authorList = new ArrayList<Author>();
	private TagCloudStyle style = TagCloudStyle.CLOUD;
	private TagCloudSort sort = TagCloudSort.ALPHA;
	private int minFreq	 = 0;
	private int maxFreq	 = DEFAULT_MAX_FREQ;
	private int maxCount = 0;
	
	
	/**
	 * @return the authorList
	 */
	public List<Author> getAuthorList() {
		return this.authorList;
	}
	
	private void calculateMaxAuthorCount() {
		for (final Author a : authorList) {
			if (a.getCtr() > maxCount) {
				maxCount = a.getCtr();
			}
		}
	}
	
	/**
	 * @param authorList the authorList to set
	 */
	public void setAuthorList(final List<Author> authorList) {
		this.authorList = authorList;
		this.calculateMaxAuthorCount();
	}

	/**
	 * @return the style
	 */
	public TagCloudStyle getStyle() {
		return this.style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(final TagCloudStyle style) {
		this.style = style;
	}

	/**
	 * @return the sort
	 */
	public TagCloudSort getSort() {
		return this.sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(final TagCloudSort sort) {
		this.sort = sort;
	}

	/**
	 * @return the minFreq
	 */
	public int getMinFreq() {
		return this.minFreq;
	}

	/**
	 * @param minFreq the minFreq to set
	 */
	public void setMinFreq(final int minFreq) {
		this.minFreq = minFreq;
	}

	/**
	 * @return the maxFreq
	 */
	public int getMaxFreq() {
		return this.maxFreq;
	}

	/**
	 * @param maxFreq the maxFreq to set
	 */
	public void setMaxFreq(final int maxFreq) {
		this.maxFreq = maxFreq;
	}

	/**
	 * @return the maxCount
	 */
	public int getMaxCount() {
		return this.maxCount;
	}

	/**
	 * @param maxCount the maxCount to set
	 */
	public void setMaxCount(final int maxCount) {
		this.maxCount = maxCount;
	}
}
