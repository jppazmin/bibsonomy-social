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

package org.bibsonomy.model.comparators;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.common.enums.SortKey;
import org.bibsonomy.common.enums.SortOrder;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.model.util.PersonNameUtils;

/**
 * Comparator used to sort bibtex posts
 * 
 * @author Dominik Benz
 * @version $Id: BibTexPostComparator.java,v 1.20 2011-05-25 10:24:13 folke Exp $
 */
public class BibTexPostComparator extends PostComparator implements Comparator<Post<BibTex>>, Serializable {
	private static final long serialVersionUID = 8550700973763853912L;

	/**
	 * regex to extract the day part from expressions like 23.22.2011, 1st,
	 * 22-23, etc.
	 */
	private static Pattern dayPattern = Pattern.compile("^(\\d+)[^\\d]?.*");

	/**
	 * Constructor
	 * 
	 * @param sortKeys
	 * @param sortOrders
	 */
	public BibTexPostComparator(final List<SortKey> sortKeys, final List<SortOrder> sortOrders) {
		super(sortKeys, sortOrders);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * 
	 * main comparison method
	 */
	@Override
	public int compare(final Post<BibTex> post1, final Post<BibTex> post2) {
		for (final SortCriterium crit : this.sortCriteria) {
			try {
				// author
				if (SortKey.AUTHOR.equals(crit.sortKey)) {
					// if author not present, take editor
					final String personList1 = (present(post1.getResource().getAuthor()) ? post1.getResource().getAuthor() : post1.getResource().getEditor());
					final String personList2 = (present(post2.getResource().getAuthor()) ? post2.getResource().getAuthor() : post2.getResource().getEditor());
					return this.nomalizeAndCompare(PersonNameUtils.getFirstPersonsLastName(personList1), PersonNameUtils.getFirstPersonsLastName(personList2), crit.sortOrder);
				}
				// year
				else if (SortKey.YEAR.equals(crit.sortKey)) {
					return this.compare(BibTexUtils.getYear(post1.getResource().getYear()), BibTexUtils.getYear(post2.getResource().getYear()), crit.sortOrder);
				}
				// month
				else if (SortKey.MONTH.equals(crit.sortKey)) {
					return this.compareMonth(post1.getResource().getMonth(), post2.getResource().getMonth(), crit.sortOrder);
				} 
				// day
				else if (SortKey.DAY.equals(crit.sortKey)) {
					return this.compareDay(post1.getResource().getDay(), post2.getResource().getDay(), crit.sortOrder);
				}
				// editor
				else if (SortKey.EDITOR.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(PersonNameUtils.getFirstPersonsLastName(post1.getResource().getEditor()), PersonNameUtils.getFirstPersonsLastName(post2.getResource().getEditor()), crit.sortOrder);
				}
				// entrytype
				else if (SortKey.ENTRYTYPE.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(post1.getResource().getEntrytype(), post2.getResource().getEntrytype(), crit.sortOrder);
				}
				// title
				else if (SortKey.TITLE.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(post1.getResource().getTitle(), post2.getResource().getTitle(), crit.sortOrder);
				}
				// booktitle
				else if (SortKey.BOOKTITLE.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(post1.getResource().getBooktitle(), post2.getResource().getBooktitle(), crit.sortOrder);
				}
				// school
				else if (SortKey.SCHOOL.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(post1.getResource().getSchool(), post2.getResource().getSchool(), crit.sortOrder);
				}
				// posting date
				else if (SortKey.DATE.equals(crit.sortKey)) {
					return this.compare(post1.getDate(), post2.getDate(), crit.sortOrder);
				}
				// note
				else if (SortKey.NOTE.equals(crit.sortKey)) {
					return this.nomalizeAndCompare(post1.getResource().getNote(), post2.getResource().getNote(), crit.sortOrder);
				}
				// ranking
				else if (SortKey.RANKING.equals(crit.sortKey)) {
					return this.compare(post1.getRanking(), post2.getRanking(), crit.sortOrder);
				} else {
					return 0;
				}
			} catch (SortKeyIsEqualException ignore) {
				// the for-loop will jump to the next sort criterium in this
				// case
			}
		}
		return 0;
	}

	/**
	 * Compare two day parts of a bibtex entry.
	 * 
	 * @param day1
	 *            - the first day
	 * @param day2
	 *            - the second ay
	 * @param order
	 *            - the desired sorting order
	 * @return an integer representing the sorted order between both day
	 *         arguments.
	 * @throws SortKeyIsEqualException
	 */
	private int compareDay(final String day1, final String day2, SortOrder order) throws SortKeyIsEqualException {
		/*
		 * first try: successful if both arguments are integers (e.g. 21 and 13)
		 */
		try {
			return this.compare(Integer.valueOf(day1), Integer.valueOf(day2), order);
		}
		/*
		 * second try: exctract day part from arguments (e.g. "1st" and "2nd")
		 */
		catch (NumberFormatException nfe1) {
			try {
				Matcher m1 = dayPattern.matcher(day1);
				Matcher m2 = dayPattern.matcher(day2);
				if (m1.matches() && m2.matches()) {
					return this.compare(Integer.valueOf(m1.group(1)), Integer.valueOf(m2.group(1)), order);
				}
			}
			catch (Exception e) {
				// nop
			}
			/*
			 * fallback: perform string comparison
			 */
			return this.nomalizeAndCompare(day1, day2, order);
		}
	}

	/**
	 * compare two month parts of a bibtex entry
	 * 
	 * @param month1
	 *            - first month
	 * @param month2
	 *            - second month
	 * @param order
	 *            - sort order
	 * @return an integer representing the sorted order between both day
	 *         arguments.
	 * @throws SortKeyIsEqualException
	 */
	private int compareMonth(final String month1, final String month2, SortOrder order) throws SortKeyIsEqualException {
		final String month1number = BibTexUtils.getMonthAsNumber(month1);
		final String month2number = BibTexUtils.getMonthAsNumber(month2);
		try {
			// if both monthes are Integers, compare them
			// numerically
			return this.compare(Integer.parseInt(month1number), Integer.parseInt(month2number), order);
		} catch (NumberFormatException ex) {
			// otherwise compare them lexicographically
			return this.nomalizeAndCompare(month1number, month2number, order);
		}
	}

}