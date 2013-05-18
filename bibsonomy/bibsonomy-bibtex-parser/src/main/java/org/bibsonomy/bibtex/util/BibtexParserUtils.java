/**
 *
 *  BibSonomy-BibTeX-Parser - BibTeX Parser from
 * 		http://www-plan.cs.colorado.edu/henkel/stuff/javabib/
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

package org.bibsonomy.bibtex.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.model.util.PersonNameUtils;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.PersonListExpander;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;

/**
 * Mainly a wrapper class for bibsonomy-bibtex-parser 
 * 
 * @author dbenz
 * @version $Id: BibtexParserUtils.java,v 1.13 2011-07-14 09:48:46 rja Exp $
 */
@Deprecated
public class BibtexParserUtils {
	private static final Log log = LogFactory.getLog(BibtexParserUtils.class);

	private static final String BIBTEX_IS_INVALID_MSG = "The validation of the BibTeX entry failed: ";

	/**
	 * Person fields
	 */
	private static enum personField {
		AUTHORS("author"),
		EDITORS("editor");

		private final String label;

		private personField(final String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	// the parsed bibtex
	private final BibtexFile bibfile;

	// internal pointer to current entry
	private Integer currentEntryId;

	// current entry
	private BibtexEntry currentEntry;	

	/**
	 * create a new BibTex Utils Object; the given 
	 * bibtexString is parsed, and the internal pointer is set to the first entry
	 * 
	 * @param bibtexString a bibtex string
	 */
	public BibtexParserUtils(final String bibtexString) {
		this.bibfile = this.parse(bibtexString);
		this.currentEntryId = -1;
		this.nextEntry();
	}

	/**
	 * move the internal pointer to the next entry
	 */
	public void nextEntry() {
		if (!this.hasNextEntry()) {
			throw new ValidationException("No more BibTeX entries.");
		}
		this.currentEntryId++;
		final Object entry = this.bibfile.getEntries().get(this.currentEntryId);
		if (entry instanceof BibtexEntry) {
			this.currentEntry = (BibtexEntry) entry;
		} else {
			this.nextEntry();
		}
	}

	/**
	 * Check if there is another bibtex entry besides the current one
	 * 
	 * @return Boolean
	 */
	public boolean hasNextEntry() {
		for (int pos = this.currentEntryId + 1; 
		pos <= this.bibfile.getEntries().size() - 1; 
		pos++ ) {
			final Object entry = this.bibfile.getEntries().get( pos );
			if (entry instanceof BibtexEntry) {
				return true;	
			}
		}		
		return false;
	}

	/**
	 * Parse a given bibtex string
	 * 
	 * @param bibtexString - a bibtex string
	 * @return true if parsing was successful, otherwise exceptions are thrown
	 */
	private final BibtexFile parse(final String bibtexString) {
		final BibtexParser parser = new BibtexParser(true);
		final BibtexFile bibtexFile = new BibtexFile();

		try {
			// parse file, exceptions are catched below
			parser.parse(bibtexFile, new StringReader(bibtexString));

			// expand person Lists			
			final PersonListExpander pListExpander = new PersonListExpander(true, true, false);
			pListExpander.expand(bibtexFile);

			// return bibtexFile
			return bibtexFile;

		} catch (final ParseException ex) {
			log.error(ex.getMessage());
			throw new ValidationException(BIBTEX_IS_INVALID_MSG + "Error while parsing BibTeX.");
		} catch (final IOException ex) {
			log.error(ex.getMessage());
			throw new ValidationException(BIBTEX_IS_INVALID_MSG + "I/O Error while parsing BibTeX.");
		} catch (final ExpansionException ex) {			
			log.error(ex.getMessage());
			throw new ValidationException(BIBTEX_IS_INVALID_MSG + "Error when trying to normalize authors.");
		}							
	}

	/**
	 * Return the author field of the current entry formatted according to
	 * FIRSTNAME LASTNAME and FIRSTNAME LASTNAME and ...
	 * 
	 * @return the formatted Author String
	 */
	public String getFormattedAuthorString() {
		return getFormattedPersonString(this.currentEntry, personField.AUTHORS);
	}

	/**
	 * Return the author field of the current entry formatted according to
	 * FIRSTNAME LASTNAME and FIRSTNAME LASTNAME and ...
	 * 
	 * @return the formatted editor String
	 */
	public String getFormattedEditorString() {
		return getFormattedPersonString(this.currentEntry, personField.EDITORS);
	}				

	/**
	 * format a person field (author or editor) of a given BibtexEntry
	 * according to
	 *   FIRSTNAME LASTNAME and FIRSTNAME LASTNAME and ...
	 * and do some consistency checks 
	 * 
	 * @param entry a BibTexEntry
	 * @param field a field name (author or editor)
	 * @return
	 */
	private static String getFormattedPersonString(final BibtexEntry entry, final personField field) {
		final BibtexAbstractValue fieldValue = entry.getFieldValue(field.getLabel());
		log.debug("fieldValue: " + fieldValue);
		if (fieldValue instanceof BibtexPersonList) {
			final BibtexPersonList personsString = (BibtexPersonList) fieldValue;
			final StringBuilder personBuffer = new StringBuilder();
			log.debug("personsString: " + personsString);
			if (personsString != null) {
				@SuppressWarnings("unchecked") // BibtexPersonList.getList specified to return a list of BibtexPersons
				final List<BibtexPerson> personList = personsString.getList();
				log.debug("personList: " + personList);

				for (final BibtexPerson person:personList) {

					// build one person					
					final StringBuilder personString = new StringBuilder();
					final String first = person.getFirst();
					if (present(first)) {
						personString.append(first);
					}

					final String preLast = person.getPreLast();
					if (present(preLast)) {
						personString.append(" ").append(preLast);
					}

					final String last = person.getLast();
					if (present(last)) {
						personString.append(" ").append(last);
					}
					
					if (person.isOthers()) {
						personString.append("others");
					}

					personBuffer.append(personString).append(PersonNameUtils.PERSON_NAME_DELIMITER);
					log.debug("personString: " + personString);
				}
				/* remove last " and " */
				if (!personList.isEmpty()) {
					return (personBuffer.substring(0, personBuffer.lastIndexOf(PersonNameUtils.PERSON_NAME_DELIMITER)));
				}
				
				// this means there was an error when trying to format this person
				log.error(BIBTEX_IS_INVALID_MSG + "Error while trying to format person list: " + personsString);
				throw new ValidationException(BIBTEX_IS_INVALID_MSG + "Error while trying to format person list: " + personsString);
			}
		} else if (fieldValue instanceof BibtexString) {
			log.error(BIBTEX_IS_INVALID_MSG + "Error while trying to format person list: " + fieldValue);
			throw new ValidationException(BIBTEX_IS_INVALID_MSG + "Error while trying to format person list: " + fieldValue);
		}
		// this means no author was given
		return null;
	}		
}
