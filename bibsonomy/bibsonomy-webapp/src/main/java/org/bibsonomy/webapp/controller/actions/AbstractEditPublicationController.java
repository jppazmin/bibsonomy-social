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

package org.bibsonomy.webapp.controller.actions;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.bibtex.parser.PostBibTeXParser;
import org.bibsonomy.bibtex.parser.SimpleBibTeXParser;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.ScraperMetadata;
import org.bibsonomy.model.User;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.scraper.Scraper;
import org.bibsonomy.scraper.ScrapingContext;
import org.bibsonomy.scraper.exceptions.ScrapingException;
import org.bibsonomy.util.upload.DocumentUtils;
import org.bibsonomy.webapp.command.actions.EditPublicationCommand;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.validation.PostValidator;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

import bibtex.parser.ParseException;

/**
 * Posting/editing one (!) publication posts.
 * 
 * TODO:
 * <ul>
 * <li>{@link SimpleBibTeXParser} stores warnings in a list - maybe we should
 * show those to the user?</li>
 * </ul>
 * 
 * @author rja
 * @author dzo
 * @version $Id: AbstractEditPublicationController.java,v 1.17 2011-05-04 15:57:56 nosebrain Exp $
 * 
 * @param <COMMAND> 
 */
public abstract class AbstractEditPublicationController<COMMAND extends EditPublicationCommand> extends EditPostController<BibTex, COMMAND> {

	private static final String SESSION_ATTRIBUTE_SCRAPER_METADATA = "scraperMetaData";

	private static final Log log = LogFactory.getLog(AbstractEditPublicationController.class);

	private Scraper scraper;
	
	private String docPath;
	private String tempPath;

	@Override
	protected View getPostView() {
		return Views.EDIT_PUBLICATION; // TODO: this could be configured using Spring!
	}

	/**
	 * If the command has set a url or selection, the scrapers are called to fill
	 * the command's post with the scraped data. 
	 * 
	 * @see org.bibsonomy.webapp.controller.actions.EditPostController#workOnCommand(org.bibsonomy.webapp.command.actions.EditPostCommand, org.bibsonomy.model.User)
	 */
	@Override
	protected void workOnCommand(final COMMAND command, final User loginUser) {
		/*
		 * Check if the controller was called by a bookmarklet which just 
		 * delivers URL + selection which should be passed to the scrapers.
		 */
		final String url = command.getUrl();
		final String selection = command.getSelection();

		if ((present(url) || present(selection))) {
			handleScraper(command, loginUser, url, selection);
		}
		
	}

	private void handleScraper(final COMMAND command, final User loginUser, final String url, String selection) {
		/*
		 * We have a URL set which means we shall scrape!
		 * 
		 * set selected text
		 */
		if (selection == null) {
			selection = "";
		}

		/*
		 * --> scrape the website and parse bibtex
		 */
		try {
			/*
			 * scrape bibtex
			 * NOTE: if the given URL is null, we probably have to scrape a 
			 * selection (e.g., ISBN) - therefore, we insert a null URL
			 */
			final ScrapingContext scrapingContext = new ScrapingContext(url == null ? null : new URL(url), selection);
			final boolean isSuccess = scraper.scrape(scrapingContext);
			final String scrapedBibtex = scrapingContext.getBibtexResult();
			if (isSuccess && present(scrapedBibtex)) {
				/*
				 * When the parser is thread-safe (it currently is not!), we can 
				 * use the same instance for each invocation.
				 * TODO: why don't we use the PostBibTeXParser? (There must be 
				 * reasons for not using it! E.g., probably because otherwise
				 * tags are taken from the post and then no edit form appears.)
				 */
				try {
					final SimpleBibTeXParser parser = new SimpleBibTeXParser();
					final BibTex parsedBibTex = parser.parseBibTeX(scrapedBibtex);
					log.debug(parser.getWarnings());

					/*
					 * check if a bibtex was scraped
					 */
					if (present(parsedBibTex)) {						
						/*
						 * save result
						 */
						command.getPost().setResource(parsedBibTex);
						/*
						 * store scraping context and scraping metadata
						 */
						handleScraperMetadata(command, scrapingContext);						
					} else {
						/*
						 * the parser did not return any result ...
						 */
						this.getErrors().reject("error.scrape.nothing", new Object[]{scrapedBibtex, url}, "The BibTeX\n\n{0}\n\nwe scraped from {1} could not be parsed.");
					}
				} catch (final IOException ex) {
					/*
					 * exception while parsing bibtex
					 */ 
					this.getErrors().reject("error.parse.bibtex.failed", new Object[]{scrapedBibtex, ex.getMessage()}, "Error parsing BibTeX:\n\n{0}\n\nMessage was: {1}");
				} catch (final ParseException ex) {
					/*
					 * exception while parsing bibtex; inform user and show him the scraped bibtex
					 */
					this.getErrors().reject("error.parse.bibtex.failed", new Object[]{scrapedBibtex, ex.getMessage()}, "Error parsing BibTeX:\n\n{0}\n\nMessage was: {1}");
				}
			} // if (isSuccess && present(scrapedBibtex))
			else {
				/*
				 * We could not scrape the given URL, i.e., the URL is either not
				 * supported (no scraper) or the scrape did not manage to extract
				 * something.
				 * FIXME: in this case we should probably show the 
				 * boxes/import_publication_hints.jsp 
				 */
				this.getErrors().reject("error.scrape.nothing", new Object[]{url}, "The URL {0} is not supported by one of our scrapers.");
			}
		} catch (final ScrapingException ex) {
			/*
			 * scraping failed no bibtex scraped
			 */
			this.getErrors().reject("error.scrape.failed", new Object[]{url, ex.getMessage()}, "Could not scrape the URL {0}.\nMessage was: {1}");
		} catch (final MalformedURLException ex) {
			/*
			 * wrong url format
			 */
			this.getErrors().reject("error.scrape.failed", new Object[]{url, ex.getMessage()}, "Could not scrape the URL {0}.\nMessage was: {1}");
		}
	}

	private void handleScraperMetadata(final COMMAND command, final ScrapingContext scrapingContext) {
		/*
		 * store scraping context to show user meta information
		 */
		command.setScrapingContext(scrapingContext);
		/*
		 * clean old scraper metadata
		 */
		setSessionAttribute(SESSION_ATTRIBUTE_SCRAPER_METADATA, null);
		/*
		 * store scraper metadata in session (to later store it 
		 * together with the post)
		 */
		if (present(scrapingContext.getMetaResult())) {
			final ScraperMetadata scraperMetadata = new ScraperMetadata();
			scraperMetadata.setScraperClass(scrapingContext.getScraper().getClass().getName());
			scraperMetadata.setMetaData(scrapingContext.getMetaResult());
			scraperMetadata.setUrl(scrapingContext.getUrl());
			setSessionAttribute(SESSION_ATTRIBUTE_SCRAPER_METADATA, scraperMetadata);
		}
	}

	@Override
	protected void preparePostForView(final Post<BibTex> post) {
		/*
		 * replace all " and "s by a new line in author and
		 * editor field of the bibtex to separate multiple authors and editors
		 */
		BibTexUtils.prepareEditorAndAuthorFieldForView(post.getResource());
	}

	@Override
	protected void createOrUpdateSuccess(final COMMAND command, final User loginUser, final Post<BibTex> post) {
		super.createOrUpdateSuccess(command, loginUser, post);
		handleAddFiles(command, loginUser.getName());
	}
	
	/**
	 * The temporary files will be stored on the file system and in the database 
	 */
	private void handleAddFiles(final EditPublicationCommand command, final String userName) {
		//TODO check length of fileHash list and fileName list
		final List<String> fileNames = command.getFileName();
		if (!present(fileNames)) {
			return;
		}
		for (final String compoundFileName: fileNames) {
			/*
			 * copy temporary file to documents directory
			 */
			final Document document = DocumentUtils.getPersistentDocument(this.tempPath, this.docPath, userName, compoundFileName);
			/*
			 * add document to the database
			 */
			logic.createDocument(document, command.getIntraHashToUpdate());
		}
	}

	/** 
	 * This controller exchanges the resource by a parsed version of it and 
	 * additionally adds scraper metadata from the session (if available).
	 * 
	 * @see org.bibsonomy.webapp.controller.actions.EditPostController#cleanPost(org.bibsonomy.model.Post)
	 */
	@Override
	protected void cleanPost(final Post<BibTex> post) {
		super.cleanPost(post);
		/*
		 * exchange post with a parsed version
		 */
		try {
			new PostBibTeXParser(this.instantiateResource().getClass()).updateWithParsedBibTeX(post);
		} catch (final ParseException ex) {
			/*
			 * we silently ignore parsing errors - they have been handled by the
			 * validator
			 */
		} catch (final IOException ex) {
			/*
			 * we silently ignore parsing errors - they have been handled by the
			 * validator
			 */
		}
		/*
		 * store scraper metadata
		 */
		final Object scraperMetadata = getSessionAttribute(SESSION_ATTRIBUTE_SCRAPER_METADATA);
		if (present(scraperMetadata)) {
			post.getResource().setScraperMetadata((ScraperMetadata) scraperMetadata);
		}
	}

	@Override
	protected void preparePostAfterView(final Post<BibTex> post) {
		/*
		 * replace all new lines with an " and " to undo the preparePostForView action
		 */
		BibTexUtils.prepareEditorAndAuthorFieldForDatabase(post.getResource());
	}

	@Override
	protected BibTex instantiateResource() {
		return new BibTex();
	}

	@Override
	protected PostValidator<BibTex> getValidator() {
		return new PostValidator<BibTex>();
	}

	@Override
	protected void setDuplicateErrorMessage(final Post<BibTex> post, final Errors errors) {
		errors.rejectValue("post.resource.title", "error.field.valid.url.alreadyStoredPublication", "You already have this publication in your collection. ");
	}

	/**
	 * @param scraper the scraper to set
	 */
	public void setScraper(Scraper scraper) {
		this.scraper = scraper;
	}
	/**
	 * @param docPath the docPath to set
	 */
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	/**
	 * @param tempPath the tempPath to set
	 */
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

}
