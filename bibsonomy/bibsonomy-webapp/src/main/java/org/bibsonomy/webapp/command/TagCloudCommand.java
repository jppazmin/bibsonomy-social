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

/*
 * Created on 14.10.2007
 */
package org.bibsonomy.webapp.command;

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.common.enums.TagCloudSort;
import org.bibsonomy.common.enums.TagCloudStyle;
import org.bibsonomy.model.Tag;

/**
 * bean for displaying a tag cloud
 * 
 * @author Dominik Benz
 */
public class TagCloudCommand extends BaseCommand {
	private List<Tag> tags = new ArrayList<Tag>();
	private int minFreq = 0; // threshold which tags to display
	private int maxFreq = 100; // maximum occurrence frequency of all tags
	private int maxCount = 0; // used for set the value via URL
	private TagCloudStyle style = TagCloudStyle.CLOUD;
	private TagCloudSort sort = TagCloudSort.ALPHA;
	private int maxTagCount;
	private int maxUserTagCount;
	
	/**
	 * @return the maxUserTagCount
	 */
	public int getMaxUserTagCount() {
		return this.maxUserTagCount;
	}

	/**
	 * find the max Tag Count
	 */
	private void calculateMaxTagCount() {
		maxTagCount = Integer.MIN_VALUE;
		maxUserTagCount = Integer.MIN_VALUE;
		for (final Tag tag : tags) {
			if (tag.getGlobalcount() > maxTagCount) {
				maxTagCount = tag.getGlobalcount();
			}
			if (tag.getUsercount() > maxUserTagCount) {
				maxUserTagCount = tag.getUsercount();
			}
		}
	}

	/**
	 * @return the maxTagCount
	 */
	public int getMaxTagCount() {
		return this.maxTagCount;
	}

	/**
	 * @return the list of contained tags
	 */
	public List<Tag> getTags() {
		return this.tags;
	}

	/**
	 * @param tags a list of tags
	 */
	public void setTags(final List<Tag> tags) {
		this.tags = tags;
		calculateMaxTagCount();
	}

	/**
	 * @return minimum occurrence frequency
	 */
	public int getMinFreq() {
		return this.minFreq;
	}

	/**
	 * @param minFreq minimum occurrence frequency
	 */
	public void setMinFreq(final int minFreq) {
		this.minFreq = minFreq;
	}

	/**
	 * @return maximum occurrence frequency
	 */
	public int getMaxFreq() {
		return this.maxFreq;
	}

	/**
	 * @param maxFreq the maximum occurrence frequency
	 */
	public void setMaxFreq(final int maxFreq) {
		this.maxFreq = maxFreq;
	}

	/**
	 * @return the display mode
	 */
	public TagCloudStyle getStyle() {
		return this.style;
	}

	/**
	 * @param mode the display mode
	 */
	public void setStyle(final TagCloudStyle mode) {
		this.style = mode;
	}

	/**
	 * @return the sorting mode
	 */
	public TagCloudSort getSort() {
		return this.sort;
	}

	/**
	 * @param sort the sorting mode
	 */
	public void setSort(final TagCloudSort sort) {
		this.sort = sort;
	}

	/**
	 * @param maxCount the maxCount to set
	 */
	public void setMaxCount(final int maxCount) {
		this.maxCount = maxCount;
	}

	/**
	 * @return the tagboxMaxCount
	 */
	public int getMaxCount() {
		return maxCount;
	}
		
}
