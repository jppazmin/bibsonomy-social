/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bibsonomy.model.Comment;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.Review;
import org.bibsonomy.util.StringUtils;

/**
 * @author dzo
 * @version $Id: DiscussionItemUtils.java,v 1.2 2011-07-26 08:31:04 bibsonomy Exp $
 */
public class DiscussionItemUtils {
	
	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	/**
	 * Calculates a hash for a discussionItem
	 * 
	 * @param discussionItem
	 * @return the new hash
	 */
	public static String recalculateHash(final DiscussionItem discussionItem) {
		if (discussionItem instanceof Comment) {
			return recalculateHash((Comment) discussionItem);
		}
		
		if (discussionItem instanceof Review) {
			return recalculateHash((Review) discussionItem);
		}
		
		throw new IllegalArgumentException("discussion item class not supported");
	}
	
	private static String getUserAndDate(final DiscussionItem item) {
		return item.getUser().getName() + FORMAT.format(item.getDate());
	}
	
	private static String recalculateHash(final Review review) {
		final String text = review.getText() == null ? "" : review.getText();
		final String toHash = getUserAndDate(review) + text;
		return StringUtils.getMD5Hash(toHash);
	}

	private static String recalculateHash(final Comment comment) {
		final String text = comment.getText();
		final String toHash = getUserAndDate(comment) + text;
		return StringUtils.getMD5Hash(toHash);
	}
}
