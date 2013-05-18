/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
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

package org.bibsonomy.layout.csl.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Models an entry. See the file 'csl-variables' at
 *   https://bitbucket.org/bdarcus/csl-schema/
 * for a list of supported variables.
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: Record.java,v 1.2 2011-07-26 13:44:49 bibsonomy Exp $
 */
public class Record {

    /**
     * Constructor
     */
    public Record() {
	super();
    }    
    
    // id of the entry
    private String id;
    
    //*************************************************
    // persons / names
    // FIXME: more available, see http://citationstyles.org/downloads/specification.html#name-variables
    //*************************************************
    
    // author(s)
    private List<Person> author = new ArrayList<Person>();
    // editor(s)
    private List<Person> editor = new ArrayList<Person>();

    //*************************************************
    // dates
    // FIXME: there are some more dates available, see http://citationstyles.org/downloads/specification.html#date-variables
    //*************************************************    
    
    // date
    private Date issued;
    
    //*************************************************
    // variables
    // see http://citationstyles.org/downloads/specification.html#standard-variables
    //*************************************************    
    
    // abstract
    private String abstractt;

    // notes made by a reader about the content of the resource
    private String annote;

    // the name of the archive
    private String archive;

    // the location within an archival collection (for example, box and folder)
    private String archive_location;

    // the place of the archive
    private String archive_place;

    // issuing authority (for patents) or judicial authority (such as court
    // for legal cases)
    private String authority;

    // ?
    private String call_number;

    // ?
    private String chapter_number;

    // the number used for the in_text citation mark in numeric styles
    private String citation_number;

    // the label used for the in_text citation mark in label styles
    private String citation_label;

    // collection number; for example, series number
    private String collection_number;

    // the tertiary title for the cited item; for example, a series title
    private String collection_title;

    // the secondary title for the cited item (book title for book chapters,
    // journal title for articles, etc.).
    private String container_title;

    // doi identifier
    private String DOI;

    // an edition description
    private String edition;

    // the name or title of a related event such as a conference or hearing
    private String event;

    // the location or place for the related event
    private String event_place;

    // The number of a preceding note containing the first reference to this
    // item. Relevant only for note_based styles, and null for first references.
    private String first_reference_note_number;

    //
    private String genre;

    //
    private String ISBN;

    // the issue number for the container publication
    private String issue;

    // For legislation and patents; scope of geographic relevance for a
    // document.
    private String jurisdiction;

    // keyword(s)
    private String keyword;

    // a description to locate an item within some larger container or
    // collection; a volume or issue number is a kind of locator, for example.
    private String locator;

    // medium description (DVD, CD, etc.)
    private String medium;

    // a short inline note, often used to refer to additional details of the
    // resource
    private String note;

    // a document number; useful for reports and such
    private String number;

    // refers to the number of pages in a book or other document
    private String number_of_pages;

    // refers to the number of items in multi_volume books and such
    private String number_of_volumes;

    // the name of the original publisher
    private String original_publisher;

    // the place of the original publisher
    private String original_publisher_place;

    // title of a related original version; often useful in cases of translation
    private String original_title;

    // the range of pages an item covers in a containing item
    private String page;

    // the first page of an item within a containing item
    private String page_first;

    // the name of the publisher
    private String publisher;

    // the place of the publisher
    private String publisher_place;

    // for related referenced resources; used for legal case histories, but
    // may be relevant for other contexts.
    private String references;

    // a section description (for newspapers, etc.)
    private String section;

    // the (typically publication) status of an item; for example ;forthcoming;
    private String status;

    // the primary title for the cited item
    private String title;

    // url
    private String URL;

    // version
    private String version;

    // volume number for the container periodical
    private String volume;

    // The year suffix for author_date styles; e.g. the 'a' in '1999a'.
    private String year_suffix;

    // Type
    private String type;
    
    
    //*************************************************
    // getter / setter
    //*************************************************    
    
    public String getAbstractt() {
        return abstractt;
    }

    public void setAbstractt(String abstractt) {
        this.abstractt = abstractt;
    }

    public String getAnnote() {
        return annote;
    }

    public void setAnnote(String annote) {
        this.annote = annote;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getArchive_location() {
        return archive_location;
    }

    public void setArchive_location(String archive_location) {
        this.archive_location = archive_location;
    }

    public String getArchive_place() {
        return archive_place;
    }

    public void setArchive_place(String archive_place) {
        this.archive_place = archive_place;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getCall_number() {
        return call_number;
    }

    public void setCall_number(String call_number) {
        this.call_number = call_number;
    }

    public String getChapter_number() {
        return chapter_number;
    }

    public void setChapter_number(String chapter_number) {
        this.chapter_number = chapter_number;
    }

    public String getCitation_number() {
        return citation_number;
    }

    public void setCitation_number(String citation_number) {
        this.citation_number = citation_number;
    }

    public String getCitation_label() {
        return citation_label;
    }

    public void setCitation_label(String citation_label) {
        this.citation_label = citation_label;
    }

    public String getCollection_number() {
        return collection_number;
    }

    public void setCollection_number(String collection_number) {
        this.collection_number = collection_number;
    }

    public String getCollection_title() {
        return collection_title;
    }

    public void setCollection_title(String collection_title) {
        this.collection_title = collection_title;
    }

    public String getContainer_title() {
        return container_title;
    }

    public void setContainer_title(String container_title) {
        this.container_title = container_title;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(String dOI) {
        DOI = dOI;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent_place() {
        return event_place;
    }

    public void setEvent_place(String event_place) {
        this.event_place = event_place;
    }

    public String getFirst_reference_note_number() {
        return first_reference_note_number;
    }

    public void setFirst_reference_note_number(String first_reference_note_number) {
        this.first_reference_note_number = first_reference_note_number;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber_of_pages() {
        return number_of_pages;
    }

    public void setNumber_of_pages(String number_of_pages) {
        this.number_of_pages = number_of_pages;
    }

    public String getNumber_of_volumes() {
        return number_of_volumes;
    }

    public void setNumber_of_volumes(String number_of_volumes) {
        this.number_of_volumes = number_of_volumes;
    }

    public String getOriginal_publisher() {
        return original_publisher;
    }

    public void setOriginal_publisher(String original_publisher) {
        this.original_publisher = original_publisher;
    }

    public String getOriginal_publisher_place() {
        return original_publisher_place;
    }

    public void setOriginal_publisher_place(String original_publisher_place) {
        this.original_publisher_place = original_publisher_place;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_first() {
        return page_first;
    }

    public void setPage_first(String page_first) {
        this.page_first = page_first;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher_place() {
        return publisher_place;
    }

    public void setPublisher_place(String publisher_place) {
        this.publisher_place = publisher_place;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getYear_suffix() {
        return year_suffix;
    }

    public void setYear_suffix(String year_suffix) {
        this.year_suffix = year_suffix;
    }

    public void setAuthor(List<Person> authors) {
	this.author = authors;
    }

    public List<Person> getAuthor() {
	return author;
    }

    public void setIssued(Date issued) {
	this.issued = issued;
    }

    public Date getIssued() {
	return issued;
    }

    public void setEditor(List<Person> editor) {
	this.editor = editor;
    }

    public List<Person> getEditor() {
	return editor;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    
    
}
