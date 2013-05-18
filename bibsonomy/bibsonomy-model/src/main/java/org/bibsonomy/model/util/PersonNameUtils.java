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

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.bibsonomy.model.PersonName;
import org.bibsonomy.util.StringUtils;

/**
 * Nice place for static util methods regarding names of persons.
 *
 * @author  Jens Illig
 * @version $Id: PersonNameUtils.java,v 1.13 2011-07-11 11:34:04 rja Exp $
 */
public class PersonNameUtils {

	/**
	 * the delimiter used for separating person names
	 */
	public static final String PERSON_NAME_DELIMITER = " and ";
	/**
	 * By default, all author and editor names are in "Last, First" order
	 * FIXME: change to "true" in September 2011! 
	 */
	public static final boolean DEFAULT_LAST_FIRST_NAMES = false;


	/**
	 * Analyses a string of names of the form "J. T. Kirk and M. Scott"
	 * 
	 * @param authorField the source string 
	 * @return the result
	 */
	public static List<PersonName> extractList(final String authorField) {
		final List<PersonName> authors = new LinkedList<PersonName>();
		if (present(authorField)) {
			final Scanner t = new Scanner(authorField);
			t.useDelimiter(PERSON_NAME_DELIMITER);
			while (t.hasNext()) {
				final PersonName a = new PersonName();
				a.setName(t.next());
				authors.add(a);
			}
		}
		return authors;
	}

	/**
	 * Converts a name in the format "Last, First" into the "First Last" format
	 * by splitting it at the first comma.
	 * If the name is already in that format (=no comma found), the name is returned as is.
	 * 
	 * @param name
	 * @return The name in format "First Last"
	 */
	public static String lastFirstToFirstLast(final String name) {
		if (present(name)) {
			final int indexOf = name.indexOf(PersonName.LAST_FIRST_DELIMITER);
			if (indexOf >= 0) {
				return name.substring(indexOf + 1).trim() + " " + name.substring(0, indexOf).trim();
			}
		}
		return name;
	}
	
	/**
	 * Given a list of person names separated by {@link #PERSON_NAME_DELIMITER}, we check
	 * if one of them is in "Last, First" format and use {@link #lastFirstToFirstLast(String)}
	 * to transform them to "First Last.
	 * 
	 * The string is only changed if it contains at least one comma.
	 * 
	 * @param names
	 * @return a list of person names, where each name is in the "First Last" format. 
	 */
	public static String lastFirstToFirstLastMany(final String names) {
		if (present(names)) {
			if (names.contains(PersonName.LAST_FIRST_DELIMITER)) {
				final StringBuilder namesNew = new StringBuilder();
				
				final String[] split = names.split(PERSON_NAME_DELIMITER);
				for (int i = 0; i < split.length; i++) {
					namesNew.append(lastFirstToFirstLast(split[i]));
					if (i < split.length - 1) namesNew.append(PERSON_NAME_DELIMITER);
				}
				return namesNew.toString();
			}
		}
		return names;
	}

	/**
	 * Tries to detect the firstname and lastname of each author or editor.
	 * 
	 * @param name 
	 * @param personName 
	 */
	public static void discoverFirstAndLastName(final String name, final PersonName personName) {
		if (present(name)) {
			/*
			 * DBLP author names sometimes contain numbers (when there are
			 * several authors with the same name. Here we remove those numbers
			 */
			final String cleanedName = StringUtils.removeSingleNumbers(name).trim();
			/*
			 * Names can be in several formats:
			 * 
			 * 1) First (preLast) Last
			 * 2) (preLast) Last, First
			 * 3) {Long name of a Company}
			 * 
			 * If the name starts with a brace and ends with a brace, we assume case 3).
			 */
			final int indexOfLbr = cleanedName.indexOf("{");
			final int indexOfRbr = cleanedName.lastIndexOf("}");
			if (indexOfLbr == 0 && indexOfRbr == cleanedName.length() - 1) {
				/*
				 * 3) {Long name of Company}
				 * 
				 * We do not remove the braces and use the complete "name" as last name. 
				 */
				personName.setLastName(cleanedName);
				return;
			}
			/*
			 * If the name contains a comma, we assume case 2).
			 */
			final int indexOfComma = cleanedName.indexOf(PersonName.LAST_FIRST_DELIMITER);
			if (indexOfComma >= 0) {
				/*
				 * 2) We assume (preLast) Last, First.
				 * Since our PersonName does not have an extra "preLast" attribute,
				 * we store it together with "Last".
				 */
				personName.setFirstName(cleanedName.substring(indexOfComma + 1).trim());
				personName.setLastName(cleanedName.substring(0, indexOfComma).trim());
				return;
			}
			/*
			 * 1) First Last ... its not so obvious, which part is what. 
			 * 
			 * We assume that a name has either only one (abbreviated or not) 
			 * first name, or several - while all except the first must be 
			 * abbreviated. The last name then begins at the first word that 
			 * does not contain a ".".
			 * Or, the last name begins at the first word with a lower case 
			 * letter.
			 * 
			 */

			/*
			 * first: split at whitespace
			 */
			final String[] nameList = cleanedName.split("\\s+");
			/*
			 * detect first name and last name
			 */
			final StringBuilder firstNameBuilder = new StringBuilder();
			int i = 0;
			while (i < nameList.length - 1) { // iterate up to the last but one part
				final String part = nameList[i++];
				firstNameBuilder.append(part + " ");
				/*
				 * stop, if this is the last abbreviated first name
				 * or 
				 * the next part begins with a lowercase letter
				 */
				final String nextPart = nameList[i];
				if ((part.contains(".") && !nextPart.endsWith(".")) || nextPart.matches("^[a-z].*")) {
					break;
				}
			}

			final StringBuilder lastNameBuilder = new StringBuilder();
			while (i < nameList.length) {
				lastNameBuilder.append(nameList[i++] + " ");
			}

			personName.setFirstName(firstNameBuilder.toString().trim());
			personName.setLastName(lastNameBuilder.toString().trim());
		}
	}

	/**
	 * Tries to extract the last name of the first person. 
	 * 
	 * @param person some string representation of a list of persons with their first- and lastnames  
	 * @return the last name of the first person
	 */
	public static String getFirstPersonsLastName(final String person) {
		if (present(person)) {
			final String firstauthor;
			/*
			 * check, if there is more than one author
			 */
			final int firstand = person.indexOf(PERSON_NAME_DELIMITER);
			if (firstand < 0) {
				firstauthor = person;
			} else {
				firstauthor = person.substring(0, firstand);				
			}
			/*
			 * first author extracted, get its last name
			 */
			final PersonName personName = new PersonName();
			discoverFirstAndLastName(firstauthor, personName);
			return personName.getLastName();
		}
		return null;
	}
	
	/**
	 * Returns a normalized representation of the given personNames using the 
	 * default value of {@link #DEFAULT_LAST_FIRST_NAMES}.
	 * 
	 * @param names
	 * @return The normalized person names.
	 */
	public static String normalizePersonNames(final String names) {
		return normalizePersonNames(names, DEFAULT_LAST_FIRST_NAMES);
	}
	
	/**
	 * Returns a normalized representation of the given personNames - either 
	 * in "Last, First" order (of lastFirstNames is <code>true</code>) or in
	 * "First Last" order.
	 * 
	 * @param names
	 * @param lastFirstNames
	 * @return The normalized person names.
	 */
	public static String normalizePersonNames(final String names, final boolean lastFirstNames) {
		if (!present(names)) return names;
		final List<PersonName> personNames = new LinkedList<PersonName>();
		for (final String name : names.split(PERSON_NAME_DELIMITER)) {
			personNames.add(new PersonName(name));
		}
		return serializePersonNames(personNames, lastFirstNames);
	}
	
	/**
	 * Joins the names of the persons in "Last, First" form (if lastFirstNames is
	 * <code>true</code>) or "First Last" form (if lastFirstNames is
	 * <code>false</code>) using the {@link #PERSON_NAME_DELIMITER}.
	 * 
	 * @param personNames
	 * @param lastFirstNames
	 * @return The joined names or <code>null</code> if the list is empty.
	 */
	public static String serializePersonNames(final List<PersonName> personNames, final boolean lastFirstNames) {
		if (!present(personNames)) return null;
		final StringBuilder sb = new StringBuilder();
		int i = personNames.size();
		for (final PersonName personName : personNames) {
			i--;
			sb.append(serializePersonName(personName, lastFirstNames));
			if (i > 0) {
				sb.append(PERSON_NAME_DELIMITER);
			}
		}
		return sb.toString();
	}

	/**
	 * Returns the name of the person in "Last, First" form (if lastFirstNames is
	 * <code>true</code>) or "First Last" form (if lastFirstNames is
	 * <code>false</code>)
	 * 
	 * @param personName
	 * @param lastFirstName
	 * @return The name or <code>null</code> if the name is empty.
	 */
	public static String serializePersonName(final PersonName personName, final boolean lastFirstName) {
		if (!present(personName)) return null;
		final String first;
		final String last;
		final String delim;
		if (lastFirstName) {
			 first = personName.getLastName();
			 last = personName.getFirstName();
			 delim = PersonName.LAST_FIRST_DELIMITER + " ";
		} else {
			 first = personName.getFirstName();
			 last = personName.getLastName();
			 delim = " ";
		}
		if (present(first)) {
			if (present(last)) {
				return first + delim + last;
			}
			return first;
		} 
		if (present(last)) {
			return last;
		}
		return null;
	}

}