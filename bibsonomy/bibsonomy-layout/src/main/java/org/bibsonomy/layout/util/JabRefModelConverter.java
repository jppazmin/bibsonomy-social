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

/**
 * 
 */
package org.bibsonomy.layout.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.BibtexEntryType;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.services.URLGenerator;

/**
 * Converts between BibSonomy's and JabRef's BibTeX model.
 * 
 * @author Waldemar Biller <wbi@cs.uni-kassel.de>
 * @version $Id: JabRefModelConverter.java,v 1.17 2011-06-07 14:30:44 rja Exp $
 * 
 */
public class JabRefModelConverter {

	private static final Log log = LogFactory
			.getLog(JabRefModelConverter.class);

	private static final Set<String> EXCLUDE_FIELDS = new HashSet<String>(
			Arrays.asList(new String[] { "abstract", // added separately
					"bibtexAbstract", // added separately
					"bibtexkey", "entrytype", // added at beginning of entry
					"misc", // contains several fields; handled separately
					"month", // handled separately
					"openURL", // not added
					"simHash0", // not added
					"simHash1", // not added
					"simHash2", // not added
					"simHash3", // not added
					"description", "keywords", "comment", "id" }));

	/**
	 * date's in JabRef are stored as strings, in BibSonomy as Date objects
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	/**
	 * separates tags
	 */
	private static final String jabRefKeywordSeparator = JabRefPreferences
			.getInstance().get("groupKeywordSeparator", ", ");

	/**
	 * Converts a list of posts in BibSonomy's format into JabRef's format.
	 * 
	 * @param posts
	 *            - a list of posts in BibSonomy's data model
	 * @param urlGen
	 *            - the URL generator to create the biburl-field
	 * @return A list of posts in JabRef's data model.
	 */
	public static List<BibtexEntry> convertPosts(
			final List<? extends Post<? extends Resource>> posts,
			URLGenerator urlGen) {
		final List<BibtexEntry> entries = new ArrayList<BibtexEntry>();
		for (final Post<? extends Resource> post : posts) {
			entries.add(convertPost(post, urlGen));
		}
		return entries;
	}

	/**
	 * Converts a BibSonomy post into a JabRef BibtexEntry
	 * 
	 * @param post
	 * @param urlGen
	 *            - the URLGenerator to create the biburl-field
	 * @return
	 */
	public static BibtexEntry convertPost(final Post<? extends Resource> post,
			URLGenerator urlGen) {

		try {
			/*
			 * what we have
			 */
			final BibTex bibtex = (BibTex) post.getResource();
			/*
			 * what we want
			 */
			final BibtexEntry entry = new BibtexEntry();
			/*
			 * each entry needs an ID (otherwise we get a NPE) ... let JabRef
			 * generate it
			 */
			/*
			 * we use introspection to get all fields ...
			 */
			final BeanInfo info = Introspector.getBeanInfo(bibtex.getClass());
			final PropertyDescriptor[] descriptors = info
					.getPropertyDescriptors();

			/*
			 * iterate over all properties
			 */
			for (final PropertyDescriptor pd : descriptors) {

				final Method getter = pd.getReadMethod();

				// loop over all String attributes
				final Object o = getter.invoke(bibtex, (Object[]) null);

				if (String.class.equals(pd.getPropertyType())
						&& (o != null)
						&& !JabRefModelConverter.EXCLUDE_FIELDS.contains(pd
								.getName())) {
					final String value = ((String) o);
					if (present(value))
						entry.setField(pd.getName().toLowerCase(), value);
				}
			}

			/*
			 * convert entry type (Is never null but getType() returns null for
			 * unknown types and JabRef knows less types than we.)
			 * 
			 * FIXME: a nicer solution would be to implement the corresponding
			 * classes for the missing entrytypes.
			 */
			final BibtexEntryType entryType = BibtexEntryType.getType(bibtex
					.getEntrytype());
			entry.setType(entryType == null ? BibtexEntryType.OTHER : entryType);

			if (present(bibtex.getMisc()) || present(bibtex.getMiscFields())) {

				// parse the misc fields and loop over them
				bibtex.parseMiscField();

				if (bibtex.getMiscFields() != null)
					for (final String key : bibtex.getMiscFields().keySet()) {
						if ("id".equals(key)) {
							// id is used by jabref
							entry.setField("misc_id", bibtex.getMiscField(key));
							continue;
						}

						if (key.startsWith("__")) // ignore fields starting with
							// __ - jabref uses them for
							// control
							continue;

						entry.setField(key, bibtex.getMiscField(key));
					}

			}

			final String month = bibtex.getMonth();
			if (present(month)) {
				/*
				 * try to convert the month abbrev like JabRef does it
				 */
				final String longMonth = Globals.MONTH_STRINGS.get(month);
				if (present(longMonth)) {
					entry.setField("month", longMonth);
				} else {
					entry.setField("month", month);
				}
			}

			final String bibAbstract = bibtex.getAbstract();
			if (present(bibAbstract))
				entry.setField("abstract", bibAbstract);

			/*
			 * concatenate tags using the JabRef keyword separator
			 */
			final Set<Tag> tags = post.getTags();
			final StringBuffer tagsBuffer = new StringBuffer();
			for (final Tag tag : tags) {
				tagsBuffer.append(tag.getName() + jabRefKeywordSeparator);
			}
			/*
			 * remove last separator
			 */
			if (!tags.isEmpty()) {
				tagsBuffer.delete(
						tagsBuffer.lastIndexOf(jabRefKeywordSeparator),
						tagsBuffer.length());
			}
			final String tagsBufferString = tagsBuffer.toString();
			if (present(tagsBufferString))
				entry.setField(BibTexUtils.ADDITIONAL_MISC_FIELD_KEYWORDS,
						tagsBufferString);

			// set groups - will be used in jabref when exporting to bibsonomy
			if (present(post.getGroups())) {
				final Set<Group> groups = post.getGroups();
				final StringBuffer groupsBuffer = new StringBuffer();
				for (final Group group : groups)
					groupsBuffer.append(group.getName() + " ");

				final String groupsBufferString = groupsBuffer.toString()
						.trim();
				if (present(groupsBufferString))
					entry.setField("groups", groupsBufferString);
			}

			// set comment + description
			final String description = post.getDescription();
			if (present(description)) {
				entry.setField(BibTexUtils.ADDITIONAL_MISC_FIELD_DESCRIPTION,
						post.getDescription());
				entry.setField("comment", post.getDescription());
			}

			if (present(post.getDate())) {
				entry.setField("added-at", sdf.format(post.getDate()));
			}

			if (present(post.getChangeDate())) {
				entry.setField("timestamp", sdf.format(post.getChangeDate()));
			}

			if (present(post.getUser()))
				entry.setField("username", post.getUser().getName());

			// set URL to bibtex version of this entry (bibrecord = ...)
			entry.setField(BibTexUtils.ADDITIONAL_MISC_FIELD_BIBURL, urlGen
					.getPublicationUrl(bibtex, post.getUser()).toString());

			return entry;

		} catch (final Exception e) {
			log.error(
					"Could not convert BibSonomy post into a JabRef BibTeX entry.",
					e);
		}

		return null;
	}

	/**
	 * Convert a JabRef BibtexEntry into a BibSonomy post
	 * 
	 * @param entry
	 * @return
	 */
	public static Post<? extends Resource> convertEntry(final BibtexEntry entry) {

		try {
			final Post<BibTex> post = new Post<BibTex>();
			final BibTex bibtex = new BibTex();
			post.setResource(bibtex);

			final List<String> knownFields = new ArrayList<String>();

			final BeanInfo info = Introspector.getBeanInfo(bibtex.getClass());
			final PropertyDescriptor[] descriptors = info
					.getPropertyDescriptors();

			bibtex.setMisc("");

			// set all known properties of the BibTex
			for (final PropertyDescriptor pd : descriptors)
				if (present(entry.getField((pd.getName().toLowerCase())))
						&& !JabRefModelConverter.EXCLUDE_FIELDS.contains(pd
								.getName().toLowerCase())) {
					pd.getWriteMethod().invoke(bibtex,
							entry.getField(pd.getName().toLowerCase()));
					knownFields.add(pd.getName());
				}

			// Add not known Properties to misc
			for (final String field : entry.getAllFields())
				if (!knownFields.contains(field)
						&& !JabRefModelConverter.EXCLUDE_FIELDS.contains(field))
					bibtex.addMiscField(field, entry.getField(field));

			bibtex.serializeMiscFields();

			// set the key
			bibtex.setBibtexKey(entry.getCiteKey());
			bibtex.setEntrytype(entry.getType().getName().toLowerCase());

			// set the date of the post
			final String timestamp = entry.getField("timestamp");
			if (present(timestamp))
				post.setDate(sdf.parse(timestamp));

			final String abstractt = entry.getField("abstract");
			if (present(abstractt))
				bibtex.setAbstract(abstractt);

			final String keywords = entry.getField("keywords");
			if (present(keywords))
				post.setTags(TagUtils.parse(keywords.replaceAll(
						jabRefKeywordSeparator, " ")));

			// Set the groups
			if (present(entry.getField("groups"))) {

				final String[] groupsArray = entry.getField("groups")
						.split(" ");
				final Set<Group> groups = new HashSet<Group>();

				for (final String group : groupsArray)
					groups.add(new Group(group));

				post.setGroups(groups);
			}

			final String description = entry.getField("description");
			if (present(description))
				post.setDescription(description);

			final String comment = entry.getField("comment");
			if (present(comment))
				post.setDescription(comment);

			final String month = entry.getField("month");
			if (present(month))
				bibtex.setMonth(month);

			return post;

		} catch (final Exception e) {
			System.out.println(e.getStackTrace());
			log.debug("Could not convert JabRef entry into BibSonomy post.", e);
		}

		return null;
	}

	/**
	 * Convert a list of JabRef's BibtexEntries into a list of BibSonomy's posts
	 * 
	 * @param entries
	 * @return
	 */
	public static List<Post<? extends Resource>> convertEntries(
			final List<BibtexEntry> entries) {

		final List<Post<? extends Resource>> posts = new ArrayList<Post<? extends Resource>>();
		for (final BibtexEntry entry : entries)
			posts.add(JabRefModelConverter.convertEntry(entry));

		return posts;
	}
}
