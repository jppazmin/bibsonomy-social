/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags.simple.termprocessing;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.bibsonomy.util.TagStringUtils;

/**
 * Extracts terms from given list of words, using a stopword remover and the
 * cleanTags function from the Discovery Challenge (e.g., remove everything
 * but letters and numbers). 
 * 
 * 
 * @author jil
 * @version $Id: TermProcessingIterator.java,v 1.4 2010-07-14 11:38:29 nosebrain Exp $
 */
public class TermProcessingIterator implements Iterator<String> {
	private final Iterator<String> words;
	private String next;
	private final StopWordRemover stopwordRemover = StopWordRemover.getInstance();

	/**
	 * @param words
	 */
	public TermProcessingIterator(Iterator<String> words) {
		this.words = words;
		fetchNext();
	}
	
	private void fetchNext() {
		/*
		 * skip empty tags
		 */
		while ((next == null || next.trim().equals("")) && words.hasNext()) {
			/*
			 * clean tag according to challenge rules
			 */
			next = TagStringUtils.cleanTag(words.next());
			/*
			 * ignore stop words and tags to be ignored according to the challenge rules
			 */
			if (stopwordRemover.process(next) == null || TagStringUtils.isIgnoreTag(next)) {
				next = null;
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		return (next != null);
	}

	@Override
	public String next() {
		final String rVal;
		if (next != null) {
			rVal = next;
			next = null;
			fetchNext();
		} else {
			throw new NoSuchElementException();
		}
		return rVal;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
