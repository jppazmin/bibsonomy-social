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

package org.bibsonomy.bibtex.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.bibtex.util.StandardBibTeXFields;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.util.PersonNameUtils;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexMacroReference;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;
import bibtex.dom.BibtexToplevelComment;
import bibtex.expansions.CrossReferenceExpander;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.expansions.PersonListExpander;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;


/**
 * Provides parsing of BibTeX entries represented by {@link String}s into {@link BibTex} objects.
 * 
 * @author rja
 * @version $Id: SimpleBibTeXParser.java,v 1.25 2011-04-29 06:55:59 bibsonomy Exp $
 */
public class SimpleBibTeXParser {

	/**
	 * Determines, if the parser will stop after the first parsing error 
	 * or if it tries to parse all and store all warnings. 
	 */
	private boolean tryParseAll;

	/**
	 * If tryParseAll is true, it holds all exceptions caught during the last parse process.
	 */
	private ParseException[] caughtExceptions;
	
	/**
	 * inits the warnings list
	 */
	public SimpleBibTeXParser() {
		this.warnings = new LinkedList<String>();
	}
	
	/**
	 * @return the tryParseAll
	 */
	public boolean isTryParseAll() {
		return this.tryParseAll;
	}

	/**
	 * @param tryParseAll the tryParseAll to set
	 */
	public void setTryParseAll(boolean tryParseAll) {
		this.tryParseAll = tryParseAll;
	}

	/**
	 * @return the caughtExceptions
	 */
	public ParseException[] getCaughtExceptions() {
		return this.caughtExceptions;
	}

	/**
	 * @param caughtExceptions the caughtExceptions to set
	 */
	public void setCaughtExceptions(ParseException[] caughtExceptions) {
		this.caughtExceptions = caughtExceptions;
	}

	/**
	 * Stores warnings occuring during parsing.
	 */
	private final List<String> warnings;

	/**
	 * @return The warnings created during parsing.
	 */
	public List<String> getWarnings() {
		return this.warnings;
	}

	/**
	 * Clears the warnings.
	 */
	public void clearWarnings() {
		this.warnings.clear();
	}

	/**
	 * Parses one BibTeX entry into a {@link BibTex} object.
	 * 
	 * @param bibtex - the BibTeX entry as string.
	 * @return The filled {@link BibTex} object.
	 * 
	 * @throws ParseException If a serious error during parsing occured. 
	 * 
	 * @throws IOException
	 */
	public BibTex parseBibTeX (final String bibtex) throws ParseException, IOException {
		final List<BibTex> list = this.parseInternal(bibtex, true);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * TODO: improve documentation
	 * @param bibtex
	 * @return TODO
	 * @throws ParseException
	 * @throws IOException
	 */
	public List<BibTex> parseBibTeXs (final String bibtex) throws ParseException, IOException { 
		return this.parseInternal(bibtex, false);
	}

	private List<BibTex> parseInternal (final String bibtex, final boolean firstEntryOnly) throws ParseException, IOException {
		final List<BibTex> result = new LinkedList<BibTex>();

		final BibtexParser parser = new BibtexParser(!tryParseAll);
		/*
		 * configure the parser
		 */
		/*
		 * To allow several "keywords" fields (as done by Connotea), we set the policy
		 * to keep all fields, such that we can access all keywords.
		 * 
		 * Default was KEEP_FIRST, changed by rja on 2008-08-26.
		 */
		//		parser.setMultipleFieldValuesPolicy(BibtexMultipleFieldValuesPolicy.KEEP_ALL);
		final BibtexFile bibtexFile = new BibtexFile();

		/*
		 * parse the string
		 */
		parser.parse(bibtexFile, new BufferedReader(new StringReader(bibtex)));


		// boolean topComment = false;
		// String topLevelComment;//stores comment or snippet, depending on bibtex entries

		// boolean standard = true;

		/* 
		 * expand all macros, crossrefs and author/editor field
		 */
		expandMacrosCrossRefsPersonLists(bibtexFile);



		/* ****************************************************************
		 * iterate over all entries and put them in BibTex objects
		 * ****************************************************************/
		for (Object potentialEntry:bibtexFile.getEntries()) {

			if (!(potentialEntry instanceof BibtexEntry)) {
				/*
				 * Process top level comment, but drop macros, because
				 * they are already expanded!
				 */
				if (potentialEntry instanceof BibtexToplevelComment) {
					/*
					 * Retrieve and process Toplevel Comment if
					 * needed??? BibtexToplevelComment comment =
					 * (BibtexToplevelComment) potentialEntry; String
					 * topLevelComment = comment.getContent();
					 */
					continue;
				}
				
				continue;
			}
			/*
			 * add entry to result list
			 */
			result.add(fillBibtexFromEntry((BibtexEntry) potentialEntry));
			/*
			 * skip remaining entries
			 */
			if (firstEntryOnly) {
				return result;
			}
		}
		
		this.setCaughtExceptions(parser.getExceptions());
		return result;
	}

	/** Expands all macros, crossrefs and person lists. Any exceptions occuring are put into 
	 * the {@link #warnings}.
	 * 
	 * @param bibtexFile
	 */
	private void expandMacrosCrossRefsPersonLists(final BibtexFile bibtexFile) {
		try {
			/*
			 * rja, 2009-10-15; changed second parameter to "false" because 
			 * otherwise we can't store months as "jun", since the parser
			 * always expands them to "June".
			 */
			new MacroReferenceExpander(true, false, false, false).expand(bibtexFile);
		} catch (ExpansionException ee) {
			warnings.add(ee.getMessage());
		}

		try {
			new CrossReferenceExpander(true).expand(bibtexFile);
		} catch (ExpansionException ee) {
			warnings.add(ee.getMessage());
		}

		try {
			new PersonListExpander(true, true, false).expand(bibtexFile);
		} catch (ExpansionException ee) {
			warnings.add(ee.getMessage());
		}
	}
	
	/**
	 * This method does the main BibTeX work - after parsing it gets all field 
	 * values from the parsed entry and fills the BibTex object.
	 * 
	 * @param entry
	 * @return
	 */
	protected BibTex fillBibtexFromEntry(final BibtexEntry entry) {
		final BibTex bibtex = this.createPublication();
		
		/* ************************************************
		 * process non standard bibtex fields 
		 * ************************************************/
		/*
		 * get set of all current fieldnames - like address, author etc. 
		 */
		@SuppressWarnings("unchecked")
		final List<String> nonStandardFieldNames = new ArrayList<String>(entry.getFields().keySet());
		/*
		 * remove standard fields from list to retrieve nonstandard ones
		 * 
		 * FIXME: this needs to be adopted according to where we use the parser!
		 * in BibSonomy this must be the standardBibSonomyFields!
		 */
		nonStandardFieldNames.removeAll(StandardBibTeXFields.getStandardBibTeXFields());

		// iter over arraylist to retrieve nonstandard field values
		for (final String key:nonStandardFieldNames) {
			bibtex.addMiscField(key, getValue(entry.getFieldValue(key)));
		}
		bibtex.serializeMiscFields();

		/* ************************************************
		 * process standard bibtex fields 
		 * ************************************************/


		/*
		 * add mandatory fields
		 */
		// retrieve entry/bibtex key
		bibtex.setBibtexKey(entry.getEntryKey());
		// retrieve entry type - should not be null or ""
		bibtex.setEntrytype(entry.getEntryType());

		String field = null;
		field = getValue(entry.getFieldValue("title")); if (field != null) bibtex.setTitle(field);
		field = getValue(entry.getFieldValue("year"));  if (field != null) bibtex.setYear(field); 

		/*
		 * add optional fields
		 */
		field = getValue(entry.getFieldValue("crossref"));     if (field != null) bibtex.setCrossref(field);     
		field = getValue(entry.getFieldValue("address"));      if (field != null) bibtex.setAddress(field);      
		field = getValue(entry.getFieldValue("annote"));       if (field != null) bibtex.setAnnote(field);       
		field = getValue(entry.getFieldValue("booktitle"));    if (field != null) bibtex.setBooktitle(field);    
		field = getValue(entry.getFieldValue("chapter"));      if (field != null) bibtex.setChapter(field);      
		field = getValue(entry.getFieldValue("day"));          if (field != null) bibtex.setDay(field);
		field = getValue(entry.getFieldValue("edition"));      if (field != null) bibtex.setEdition(field);      
		field = getValue(entry.getFieldValue("howpublished")); if (field != null) bibtex.setHowpublished(field); 
		field = getValue(entry.getFieldValue("institution"));  if (field != null) bibtex.setInstitution(field);  
		field = getValue(entry.getFieldValue("journal"));      if (field != null) bibtex.setJournal(field);      
		field = getValue(entry.getFieldValue("key"));	       if (field != null) bibtex.setKey(field);
		field = getValue(entry.getFieldValue("note"));         if (field != null) bibtex.setNote(field);         
		field = getValue(entry.getFieldValue("number"));       if (field != null) bibtex.setNumber(field);       
		field = getValue(entry.getFieldValue("organization")); if (field != null) bibtex.setOrganization(field); 
		field = getValue(entry.getFieldValue("pages"));        if (field != null) bibtex.setPages(field);        
		field = getValue(entry.getFieldValue("publisher"));    if (field != null) bibtex.setPublisher(field);    
		field = getValue(entry.getFieldValue("school"));       if (field != null) bibtex.setSchool(field);       
		field = getValue(entry.getFieldValue("series"));       if (field != null) bibtex.setSeries(field);       
		field = getValue(entry.getFieldValue("url"));          if (field != null) bibtex.setUrl(field);           
		field = getValue(entry.getFieldValue("volume"));	   if (field != null) bibtex.setVolume(field);        
		field = getValue(entry.getFieldValue("abstract"));	   if (field != null) bibtex.setAbstract(field);
		field = getValue(entry.getFieldValue("type"));  	   if (field != null) bibtex.setType(field);          

		/*
		 * special handling for month - it can be a macro!
		 * FIXME: a month (or any other field!) can even be a 
		 * BibtexConcatenatedValue - we don't care about this!  
		 */
		final BibtexAbstractValue month = entry.getFieldValue("month");
		if (month instanceof BibtexMacroReference) {
			bibtex.setMonth(((BibtexMacroReference) month).getKey());
		} else if (month instanceof BibtexString) {
			field = getValue(month); if (field != null) bibtex.setMonth(field);        
		}

		/*
		 * parse person names for author + editor
		 */
		bibtex.setAuthor(createPersonString(entry.getFieldValue("author")));
		bibtex.setEditor(createPersonString(entry.getFieldValue("editor")));

		/*
		 * rja, 2009-06-30 (added this to BibTeXHandler and copied it here - but deactivated it)
		 * CiteULike uses the "comment" field to export (private) notes in the form
		 * 
		 * comment = {(private-note)This is a test note!}, 
		 * 
		 * Thus, we here extract the field and remove the "(private-note)" part
		 * 
		 * FIXME: add a test for this!
		 */
		field = getValue(entry.getFieldValue("comment"));
		if (field != null) bibtex.setPrivnote(field.replace("(private-note)", ""));
		/*
		 * we export our private notes as "privnote" - add it here
		 */
		field = getValue(entry.getFieldValue("privnote"));
		if (field != null) bibtex.setPrivnote(field);
		
		return bibtex;
	}

	/**
	 * Extracts a string from the given fieldValue. Depending on the type
	 * of the value, it might contain macros!
	 * 
	 * @param fieldValue
	 * @return
	 */
	private String getValue(final BibtexAbstractValue fieldValue) {
		if (fieldValue == null) return null;
		final String value;
		if (fieldValue instanceof BibtexString) {
			value = ((BibtexString) fieldValue).getContent();
		} else {
			/*
			 * It's probably a BibtexConcatenatedValue - 
			 * don't touch it but instead just add the plain string 
			 */
			final StringWriter sw = new StringWriter();
			fieldValue.printBibtex(new PrintWriter(sw));
			value = sw.getBuffer().toString();
		}
		return value;
	}	

	protected BibTex createPublication() {
		return new BibTex();
	}

	/** Extracts all persons from the given field value and concatenates their names
	 * with {@value #AND}.
	 * 
	 * @param fieldValue
	 * @return The persons names concatenated with " and ".
	 */
	private String createPersonString (final BibtexAbstractValue fieldValue) {
		if (fieldValue != null && fieldValue instanceof BibtexPersonList) {
			/*
			 * cast into a person list and extract the persons
			 */
			@SuppressWarnings("unchecked") // getList specified to return a list of BibtexPersons
			final List<BibtexPerson> personList = ((BibtexPersonList) fieldValue).getList();
			/*
			 * result buffer
			 */
			final StringBuilder personBuffer = new StringBuilder();
			/*
			 * build person names
			 */
			for (final BibtexPerson person:personList) {
				/*
				 * build one person
				 * 
				 * FIXME: what is done here breaks author names whose last name
				 * consists of several parts, e.g.,
				 * Vander Wal, Thomas 
				 * If written as
				 * Thomas Vander Wal,
				 * "Vander" is interpreted as second name and the name is 
				 * treated in the wrong way at several occasions.
				 * Thus, we must ensure to store all author names as
				 * lastname, firstname
				 * and only change the order in the JSPs.
				 *  
				 */
				final StringBuilder personName = new StringBuilder();
				/*
				 * first name
				 */
				final String first = person.getFirst();
				if (first != null) personName.append(first);
				/*
				 * between first and last name
				 */
				final String preLast = person.getPreLast();
				if (preLast != null) personName.append(" " + preLast);
				/*
				 * last name
				 */
				final String last = person.getLast();
				if (last != null) personName.append(" " + last);
				/*
				 * "others" has a special meaning in BibTeX (it's converted to "et al."),
				 * so we must not ignore it! 
				 */
				if (person.isOthers()) personName.append("others");
				/*
				 * next name
				 */
				personBuffer.append(personName.toString().trim() + PersonNameUtils.PERSON_NAME_DELIMITER);
			}
			/* 
			 * remove last " and " 
			 */
			if (personBuffer.length() > PersonNameUtils.PERSON_NAME_DELIMITER.length()) {
				return personBuffer.substring(0, personBuffer.length() - PersonNameUtils.PERSON_NAME_DELIMITER.length());
			} 
		}
		return null;
	}
}
