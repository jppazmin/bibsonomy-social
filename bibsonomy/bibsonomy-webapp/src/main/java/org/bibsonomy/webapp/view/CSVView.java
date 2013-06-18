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

package org.bibsonomy.webapp.view;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.services.renderer.LayoutRenderer;
import org.bibsonomy.webapp.command.SimpleResourceViewCommand;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.JstlView;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * View which uses an {@link LayoutRenderer} to render the output.
 * 
 * @author rja
 * @version $Id: CSVView.java,v 1.8 2010-11-11 13:52:38 nosebrain Exp $
 */
@SuppressWarnings("deprecation")
public class CSVView extends AbstractView {
	private static final Log log = LogFactory.getLog(CSVView.class);
	
	@Override
	protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		/*
		 * get the data
		 */
		final Object object = model.get(BaseCommandController.DEFAULT_COMMAND_NAME);
		if (object instanceof SimpleResourceViewCommand) {
			/*
			 * we can only handle SimpleResourceViewCommands ...
			 */
			final SimpleResourceViewCommand command = (SimpleResourceViewCommand) object;

			/*
			 * set the content type headers
			 */				
			response.setContentType("text/csv");
			response.setCharacterEncoding("UTF-8");

			/*
			 * get the requested path
			 * we need it to generate the file names for inline content-disposition
			 * FIXME: The path is written into the request by the UrlRewriteFilter 
			 * ... probably this is not a good idea
			 */
//			final String requPath = (String) request.getAttribute("requPath");
//			response.setHeader("Content-Disposition", "attachement; filename=" + Functions.makeCleanFileName(requPath) + extension);

			try {
				/*
				 * write the buffer to the response
				 */
				final ServletOutputStream outputStream = response.getOutputStream();
				final CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, "UTF-8"));
				/*
				 * write header
				 */
				csvWriter.writeNext(new String[]{
						"intrahash",
						"user",
						"date",
						"tags",
						"groups",
						"description",
						
						"title",
												
						// publication-only fields
						"bibtexKey",
						// from here: sorted by alphabet
						"address",
						"annote",
						"author",
						"entrytype",
						"booktitle",
						"chapter",
						"crossref",
						"day",
						"edition",
						"editor",
						"howpublished",
						"institution",
						"journal",
						"key",
						"month",
						"note",
						"number",
						"organization",
						"pages",
						"publisher",
						"school",
						"series",
						"type",
						"volume",
						"year",

						// remaining "special" fields
						"private note",
						"misc",
						"abstract"
						
				});
				
				/*
				 * write publications
				 */
				final List<Post<BibTex>> publicationList = command.getBibtex().getList();
				if (publicationList != null) {
					for (final Post<BibTex> post : publicationList) {
						final BibTex resource = post.getResource();
						csvWriter.writeNext(getArray(post, resource.getAuthor(), resource.getEditor(), resource.getBibtexKey(), 
								resource.getAnnote(), resource.getBooktitle(), resource.getCrossref(), resource.getAddress(), 
								resource.getEntrytype(), resource.getChapter(), resource.getEdition(), resource.getDay(), 
								resource.getHowpublished(), resource.getInstitution(), resource.getJournal(), resource.getMonth(), 
								resource.getKey(), resource.getNumber(), resource.getOrganization(), resource.getNote(), 
								resource.getPages(), resource.getPublisher(), resource.getSchool(), resource.getSeries(),
								resource.getType(), resource.getVolume(), resource.getYear(), resource.getPrivnote(), 
								resource.getMisc(), resource.getAbstract()));
					}
				}

				/*
				 * write bookmarks
				 */
				final List<Post<Bookmark>> bookmarkList = command.getBookmark().getList();
				if (bookmarkList != null) {
					for (final Post<Bookmark> post : bookmarkList) {
						csvWriter.writeNext(getArray(post, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
					}
				}

				csvWriter.close();

			} catch (final IOException e) {
				log.error("Could not render CSV view.", e);
				/*
				 * layout could not be found or contains errors -> set HTTP status to 400
				 */
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				/*
				 * get the errors object and add the error message
				 */
				final BindingResult errors = ViewUtils.getBindingResult(model);
				errors.reject("error.layout.rendering", new Object[]{e.getMessage()}, "Could not render layout: " + e.getMessage());
				/*
				 * do the rendering ... a bit tricky: we need to get an appropriate JSTL view and give it the 
				 * application context
				 */
				final JstlView view = new JstlView("/WEB-INF/jsp/error.jspx");
				view.setApplicationContext(getApplicationContext());
				view.render(model, request, response);
			}
		} else {
			/*
			 * FIXME: what todo here?
			 */
		}
	}

	private String[] getArray(final Post<? extends Resource> post, final String author, final String editor, final String bibtexKey, final String annote, final String booktitle, final String crossref, final String address, final String entrytype, final String chapter, final String edition, final String day, final String howpublished, final String institution, final String journal, final String month, final String key, final String number, final String organization, final String note, final String pages, final String publisher, final String school, final String series, final String type, final String volume, final String year, final String privnote, final String misc, final String bibtexAbstract) {
		final Resource resource = post.getResource();
		return new String[] {
				// common fields of post
				post.getResource().getIntraHash(),
				post.getUser().getName(),
				post.getDate().toString(),
				TagUtils.toTagString(post.getTags(), " "),
				groupsToString(post.getGroups()),
				post.getDescription(),
				
				resource.getTitle(),
				
				// publication-only fields
				bibtexKey,
				// from here: sorted by alphabet
				address,
				annote,
				author,
				entrytype,
				booktitle,
				chapter,
				crossref,
				day,
				edition,
				editor,
				howpublished,
				institution,
				journal,
				key,
				month,
				note,
				number,
				organization,
				pages,
				publisher,
				school,
				series,
				type,
				volume,
				year,

				// remaining "special" fields
				privnote,
				misc,
				bibtexAbstract
		};
	}

	/**
	 * Creates the group string for CSV output
	 * @param groups
	 * @return
	 */
	private String groupsToString (final Set<Group> groups) {
		final StringBuilder buf = new StringBuilder();
		if (groups.isEmpty()) {
			buf.append(GroupUtils.getPublicGroup().getName());
		} else {
			for (final Group group: groups) {
				buf.append(group.getName() + " ");
			}
		}
		return buf.toString().trim();
	}
}
