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

package org.bibsonomy.webapp.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.MySearchCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;

/**
 * This controller retrieve all bibtex informations of a currently logged in
 * user and builds relation tables between several bibtex information fields
 * like author, title and tags.
 * 
 * @author Christian Voigtmann
 * @version $Id: MySearchController.java,v 1.8 2010-11-17 10:55:34 nosebrain Exp $
 */
public class MySearchController extends SingleResourceListControllerWithTags implements MinimalisticController<MySearchCommand> {
	private static final Log log = LogFactory.getLog(MySearchController.class);

	@Override
	public View workOn(final MySearchCommand command) {
		/*
		 * FIXME: implement this for a group!
		 */
		log.debug(this.getClass().getSimpleName());
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		/*
		 * only users which are logged in might post bookmarks -> send them to
		 * login page
		 */
		if (!command.getContext().isUserLoggedIn()) {
			/*
			 * FIXME: We need to add the ?referer= parameter such that the user
			 * is send back to this controller after login. This is not so
			 * simple, because we cannot access the query path and for POST
			 * requests we would need to build the parameters by ourselves.
			 */
			return new ExtendedRedirectView("/login");
		}

		final User user = command.getContext().getLoginUser();

		// set grouping entity, grouping name, tags
		// final GroupingEntity groupingEntity = GroupingEntity.USER;
		// final String groupingName = user.getName();

		String groupingName = command.getRequGroup();
		GroupingEntity groupingEntity = GroupingEntity.GROUP;

		if (groupingName == null) {
			groupingName = user.getName();
			groupingEntity = GroupingEntity.USER;
		}

		// retrieve and set the requested resource lists, along with total
		// counts
		for (final Class<? extends Resource> resourceType : this.getListsToInitialize(format, command.getResourcetype())) {
			// FIXME: we should deliver items dynamically via ajax,
			// displaying a 'wheel of fortune' until all items are loaded
			this.setList(command, resourceType, groupingEntity, groupingName, null, null, null, null, null, Integer.MAX_VALUE);
			this.postProcessAndSortList(command, resourceType);
		}

		/**
		 * retrieve all bibtex from current user
		 */
		final ListCommand<Post<BibTex>> bibtex = command.getBibtex();

		final SortedSet<String> titles = new TreeSet<String>();
		final SortedSet<String> authors = new TreeSet<String>();
		final SortedSet<String> tags = new TreeSet<String>();

		/**
		 * read title, author and tag information form bibtex
		 */
		for (Post<BibTex> bibtexEntry : bibtex.getList()) {

			String title = bibtexEntry.getResource().getTitle().replaceAll("\\n|\\r", "");
			titles.add(title);

			String author = buildAuthorsAndEditors(bibtexEntry.getResource().getAuthor(), bibtexEntry.getResource().getEditor());

			List<String> authorsLastNames = extractAuthorsLastNames(author);
			for (String lastName : authorsLastNames) {
				authors.add(lastName);
			}

			for (Tag tag : bibtexEntry.getTags()) {
				tags.add(tag.getName());
			}
		}

		command.setTitles(new LinkedList<String>(titles));
		command.setAuthors(new LinkedList<String>(authors));
		command.setTags(new LinkedList<String>(tags));

		buildRelationTables(bibtex, command);

		// set page title
		command.setPageTitle("user :: " + groupingName); // TODO: i18n

		this.endTiming();

		/**
		 * return view to show the mySearch.jspx side
		 */
		return Views.MYSEARCH;
	}

	private void buildRelationTables(ListCommand<Post<BibTex>> bibtex, MySearchCommand command) {

		/**
		 * containers for relation tables
		 */
		final LinkedList<String> titleList = command.getTitles();
		final LinkedList<String> tagList = command.getTags();
		final LinkedList<String> authorList = command.getAuthors();

		/**
		 * sorted lists for several relations
		 */
		SortedSet<Integer>[] tagTitle = new TreeSet[tagList.size()];
		SortedSet<Integer>[] authorTitle = new TreeSet[authorList.size()];
		SortedSet<Integer>[] tagAuthor = new TreeSet[tagList.size()];
		SortedSet<Integer>[] titleAuthor = new TreeSet[titleList.size()];

		/**
		 * string arrays for hash and url informations for the several bibtex
		 */
		String[] bibtexHashs = new String[titleList.size()];
		String[] bibtexUrls = new String[titleList.size()];

		/**
		 * build the relations from the bibtex informations
		 */
		for (Post<BibTex> bibtexEntry : bibtex.getList()) {
			// read values from resultset
			String title = bibtexEntry.getResource().getTitle().replaceAll("\\n|\\r", "");
			Set<Tag> tags = bibtexEntry.getTags();
			String hash = bibtexEntry.getResource().getSimHash2();
			String url = bibtexEntry.getResource().getUrl();
			String author = buildAuthorsAndEditors(bibtexEntry.getResource().getAuthor(), bibtexEntry.getResource().getEditor());

			// tag --> title relation
			for (Tag tag : tags) {
				if (tagTitle[tagList.indexOf(tag.getName())] == null) {
					SortedSet<Integer> v = new TreeSet<Integer>();
					v.add(titleList.indexOf(title));
					tagTitle[tagList.indexOf(tag.getName())] = v;
				} else {
					tagTitle[tagList.indexOf(tag.getName())].add(titleList.indexOf(title));
				}
			}

			// author --> title relation
			List<String> authorsLastNames = extractAuthorsLastNames(author);
			for (String name : authorsLastNames) {
				if (authorTitle[authorList.indexOf(name)] == null) {
					SortedSet<Integer> v = new TreeSet<Integer>();
					v.add(titleList.indexOf(title));
					authorTitle[authorList.indexOf(name)] = v;
				} else {
					authorTitle[authorList.indexOf(name)].add(titleList.indexOf(title));
				}
			}

			// tag --> author relation
			for (Tag tag : tags) {
				if (tagAuthor[tagList.indexOf(tag.getName())] == null) {
					SortedSet<Integer> v = new TreeSet<Integer>();
					for (String name : authorsLastNames) {
						v.add(authorList.indexOf(name));
					}
					tagAuthor[tagList.indexOf(tag.getName())] = v;
				} else {
					for (String name : authorsLastNames) {
						tagAuthor[tagList.indexOf(tag.getName())].add(authorList.indexOf(name));
					}
				}
			}

			// title --> author relation
			if (titleAuthor[titleList.indexOf(title)] == null) {
				SortedSet<Integer> v = new TreeSet<Integer>();
				for (String name : authorsLastNames) {
					v.add(authorList.indexOf(name));
				}
				titleAuthor[titleList.indexOf(title)] = v;
			} else {
				for (String name : authorsLastNames) {
					titleAuthor[titleList.indexOf(title)].add(authorList.indexOf(name));
				}
			}

			// BibTeX-Hashtable
			bibtexHashs[titleList.indexOf(title)] = hash;

			// Urls
			bibtexUrls[titleList.indexOf(title)] = url;

		}

		/**
		 * store relation tables in the command object
		 */
		command.setTagTitle(tagTitle);
		command.setAuthorTitle(authorTitle);
		command.setTagAuthor(tagAuthor);
		command.setTitleAuthor(titleAuthor);
		command.setBibtexHash(bibtexHashs);
		command.setBibtexUrls(bibtexUrls);

		/**
		 * simhash is needed by the javascript code in the mySearch.jspx side to
		 * complete the bibtex hash string
		 */
		command.setSimHash(HashID.getSimHash(2).getId());
	}

	@Override
	public MySearchCommand instantiateCommand() {
		return new MySearchCommand();
	}

	/**
	 * 
	 * @param author
	 * @param editor
	 * @return a string containing all authors and editors
	 */
	private String buildAuthorsAndEditors(String author, String editor) {
		final StringBuilder authors = new StringBuilder();

		if (author != null) authors.append(author);

		if (editor != null) {
			if (author != null) authors.append(" and ");
			authors.append(editor);
		}

		return authors.toString();
	}

	/**
	 * extract the last names of the authors
	 * 
	 * @param authors
	 * @return string list of the last names of the authors
	 */
	private List<String> extractAuthorsLastNames(String authors) {
		List<String> authorsList = new LinkedList<String>();
		List<String> names = new LinkedList<String>();
		Pattern pattern = Pattern.compile("[0-9]+"); // only numbers

		Scanner s = new Scanner(authors);
		s.useDelimiter(" and ");
		while (s.hasNext())
			names.add(s.next());

		for (String person : names) {
			/*
			 * extract all parts of a name
			 */
			List<String> nameList = new LinkedList<String>();
			StringTokenizer token = new StringTokenizer(person);
			while (token.hasMoreTokens()) {
				/*
				 * ignore numbers (from DBLP author names)
				 */
				final String part = token.nextToken();
				if (!pattern.matcher(part).matches()) {
					nameList.add(part);
				}
			}

			/*
			 * detect lastname
			 */
			int i = 0;
			while (i < nameList.size() - 1) { // iterate up to the last but one
				// part
				final String part = nameList.get(i++);
				/*
				 * stop, if this is the last abbreviated forename
				 */
				if (part.contains(".") && !nameList.get(i).contains(".")) {
					break;
				}
			}

			StringBuilder lastName = new StringBuilder();
			while (i < nameList.size()) {
				lastName.append(nameList.get(i++) + " ");
			}

			// add name to list
			authorsList.add(lastName.toString().trim());
		}
		return authorsList;
	}

}
