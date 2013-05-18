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

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.bibsonomy.common.enums.SerializeBibtexMode;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.factories.ResourceFactory;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.util.TagStringUtils;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexString;
import bibtex.parser.ParseException;

/**
 * Provides parsing of BibTeX entries represented by {@link String}s into
 * {@link Post} objects.
 *
 * This class is not thread-safe!
 * 
 * 
 * @author rja
 * @version $Id: PostBibTeXParser.java,v 1.25 2011-04-29 06:55:59 bibsonomy Exp $
 */
public class PostBibTeXParser extends SimpleBibTeXParser {

	/**
	 * Specifies the delimiter for keywords and tags
	 */
	private String delimiter;
	/**
	 * Specifies the whitespace substitute for keywords and tags
	 */
	private String whitespace;

	/**
	 * To parse the date in the "date" field of BibTeX entries into the
	 * date attribute of posts, we support the following date format.
	 * (needed for DBLP import) 
	 */
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Class<? extends BibTex> pubInstanceToCreate = BibTex.class;
	private final ResourceFactory resourceFactory;

	/**
	 * inits the resource factory
	 */
	public PostBibTeXParser() {
		this.resourceFactory = new ResourceFactory();
	}

	/**
	 * sets the publication type to create
	 * @param pubInstanceToCreate
	 */
	public PostBibTeXParser(final Class<? extends BibTex> pubInstanceToCreate) {
		this();
		this.pubInstanceToCreate = pubInstanceToCreate;
	}

	/**
	 * Parses the given BibTeX entry and puts fields which are not part of the
	 * {@link BibTex} class into the Post. See {@link #fillPost(BibTex)} for 
	 * details.
	 * 
	 * @param bibtex -
	 *            the string which contains one (!) BibTeX-Entry.
	 * 
	 * @return The post which contains all data of the BibTeX-Entry.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public Post<BibTex> parseBibTeXPost(final String bibtex) throws ParseException, IOException {
		/*
		 * call parseBibTeX
		 */
		final BibTex parsedBibTeX = this.parseBibTeX(bibtex);
		/*
		 * create post and put resource into post
		 */
		return this.fillPost(parsedBibTeX);
	}

	/**
	 * Puts the resource into a post. Does the following additional steps:
	 * 
	 *  <ul>
	 *  <li>Sets description from misc field-</li>
	 *  <li>Parses and sets tags from misc field-</li>
	 *  <li>Removes additional misc fields intrahash, interhash, and {@link BibTexUtils#ADDITIONAL_MISC_FIELDS}.</li>
	 *  <li>Re-Serializes misc fields.
	 *  </ul>
	 * 
	 * @param bibtex
	 * @return
	 */
	private Post<BibTex> fillPost(final BibTex bibtex) {
		final Post<BibTex> post = new Post<BibTex>();
		post.setResource(bibtex);
		/*
		 * get misc fields for next steps
		 */
		bibtex.parseMiscField();
		/*
		 * if a post does not have misc fields, we don't have to do anything
		 */
		if (present(bibtex.getMiscFields())) {
			/*
			 * put description from misc fields into post
			 */
			post.setDescription(bibtex.removeMiscField(BibTexUtils.ADDITIONAL_MISC_FIELD_DESCRIPTION));
			/*
			 * parse tags
			 */
			final String keywords = bibtex.removeMiscField(BibTexUtils.ADDITIONAL_MISC_FIELD_KEYWORDS);
			try {
				/*
				 * we check whitespace only against NULL to allow empty strings (to produce
				 * CamelCase tags)
				 */
				if (present(delimiter) && whitespace != null) {
					post.setTags(TagUtils.parse(TagStringUtils.cleanTags(keywords, true,  delimiter, whitespace)));
				} else {
					post.setTags(TagUtils.parse(keywords));
				}
			} catch (final RecognitionException ex) {
				/*
				 * silently ignore tag parsing errors ....
				 */
			}
			/*
			 * The DBLP updater sets the date of posts using the "date" field. Therefore,
			 * we must parse it here and fill the post's date attribute. 
			 */
			setDate(bibtex, post);

			/*
			 * remove other misc fields which should not be stored as misc field 
			 * (but rather as regular field/column).
			 * 
			 * Please note: keys for misc fields are automatically turned into lowercase
			 * by our bibtex parser!
			 */
			bibtex.removeMiscField("intrahash");
			bibtex.removeMiscField("interhash");
			for (final String additionalMiscField: BibTexUtils.ADDITIONAL_MISC_FIELDS) {
				bibtex.removeMiscField(additionalMiscField);	
			}
		}
		/*
		 * re-write misc field to fix above changes
		 */
		bibtex.serializeMiscFields();
		return post;
	}

	/**
	 * If a date is given in the "date" field, it is parsed and set in
	 * the post's "date" attribute.
	 * 
	 * @param bibtex
	 * @param post
	 */
	private void setDate(final BibTex bibtex, final Post<BibTex> post) {
		final String dateField = bibtex.removeMiscField(BibTexUtils.ADDITIONAL_MISC_FIELD_DATE);
		if (present(dateField)) {
			try {
				post.setDate(dateFormat.parse(dateField));
			} catch (java.text.ParseException ex) {
				// ignore parse errors
			}
		}
	}

	/**
	 * Like {@link #parseBibTeXPost(String)} but for multiple entries.
	 * 
	 * @param bibtex - a BibTeX string containing one or more BibTeX entries.
	 * @return A list of posts containing the parsed BibTeX entries as resources.
	 * @throws ParseException
	 * @throws IOException
	 */
	public List<Post<BibTex>> parseBibTeXPosts(final String bibtex) throws ParseException, IOException {
		/*
		 * parse entries
		 */
		final List<BibTex> parsedBibTeXs = this.parseBibTeXs(bibtex);
		/*
		 * contains resulting posts
		 */
		final List<Post<BibTex>> result = new LinkedList<Post<BibTex>>();
		/*
		 * create the posts
		 */
		for (final BibTex publication : parsedBibTeXs) {
			result.add(this.fillPost(publication));
		}

		return result;
	}

	/** 
	 * In addition to org.bibsonomy.bibtex.parser.SimpleBibTeXParser#fillBibtexFromEntry(bibtex.dom.BibtexEntry)
	 * this method handles description, keywords, etc. which are not part of 
	 * {@link BibTex} but of {@link Post}.
	 * 
	 * All additional fields are added as "misc" field to the resulting bibtex.
	 * 
	 * 
	 * @see org.bibsonomy.bibtex.parser.SimpleBibTeXParser#fillBibtexFromEntry(bibtex.dom.BibtexEntry)
	 */
	@Override
	protected BibTex fillBibtexFromEntry(final BibtexEntry entry) {
		final BibTex bibtex = super.fillBibtexFromEntry(entry);

		for (final String additionalField: BibTexUtils.ADDITIONAL_MISC_FIELDS) {
			final BibtexString field = (BibtexString) entry.getFieldValue(additionalField); 
			if (field != null) bibtex.addMiscField(additionalField, field.getContent());

		}

		return bibtex;
	}

	/**
	 * Builds a BibTeX-String from the BibTex contained in the post and parses
	 * this string into a Post. Then, all fields in the new post which were
	 * contained in the string are copied back into the new post.
	 * 
	 * Purpose: To ensure that we show only valid and normalized BibTeX entries
	 * (e.g., on /bib/ pages; currently, we also need this for /layout/, since
	 * there all posts are parsed through the JabRef parser) we send all posts
	 * through the parser and thereby normalize them before we store them in the
	 * DB.
	 * 
	 * @see BibTexUtils#toBibtexString(Post) - all fields added there have to be
	 * copied here, to!
	 * @param post
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void updateWithParsedBibTeX(final Post<BibTex> post) throws ParseException, IOException {
		/*
		 * parse the bibtex in the post
		 */
		final Post<BibTex> copyPost = this.getParsedCopy(post);
		/*
		 * exchange the bibtex in the post by the parsed version
		 */
		post.setResource(copyPost.getResource());
		/*
		 * We don't need to copy those fields back, because they're not touched/
		 * normalized by the parser.
		 */
		//		post.setTags(copyPost.getTags());
		//		post.setDescription(copyPost.getDescription());
	}

	/**
	 * Parses the given post and returns a copy where all fields which are put
	 * into a BibTeX string in {@link BibTexUtils#toBibtexString(Post)} are put
	 * into the copy. Please note that ONLY THOSE fields are put into the copy
	 * post! I.e., fields like "group" or "user", which never occur in a BibTeX 
	 * string are not copied into the new post!
	 * 
	 * @param post
	 * @return TODO: improve documentation
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public Post<BibTex> getParsedCopy(final Post<BibTex> post) throws ParseException, IOException {
		/*
		 * parseBibTeXPost must ensure to add all fields which 
		 * BibTexUtils.toBibtexString(post) puts into the string. 
		 */
		return this.parseBibTeXPost(BibTexUtils.toBibtexString(post, SerializeBibtexMode.PLAIN_MISCFIELDS));
	}

	@Override
	protected BibTex createPublication() {
		return this.resourceFactory.createPublication(this.pubInstanceToCreate);
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return this.delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the whitespace
	 */
	public String getWhitespace() {
		return this.whitespace;
	}

	/**
	 * @param whitespace the whitespace to set
	 */
	public void setWhitespace(String whitespace) {
		this.whitespace = whitespace;
	}
}
