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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.bibtex.parser.PostBibTeXParser;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.errors.DuplicatePostErrorMessage;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.errors.SystemTagErrorMessage;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.common.exceptions.UnsupportedFileTypeException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.scraper.converter.EndnoteToBibtexConverter;
import org.bibsonomy.scraper.converter.RisToBibtexConverter;
import org.bibsonomy.scraper.exceptions.ConversionException;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.util.upload.FileUploadInterface;
import org.bibsonomy.util.upload.impl.FileUploadFactory;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.actions.PostPublicationCommand;
import org.bibsonomy.webapp.util.GroupingCommandUtils;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.bibsonomy.webapp.validation.PostPublicationCommandValidator;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import bibtex.parser.ParseException;

/**
 * 
 * @author ema
 * @version $Id: PostPublicationController.java,v 1.80 2011-07-14 14:06:30 nosebrain Exp $
 */
public class PostPublicationController extends AbstractEditPublicationController<PostPublicationCommand> {
	/**
	 * the log...
	 */
	private static final Log log = LogFactory.getLog(PostPublicationController.class);

	/**
	 * if the user tries to import more than MAXCOUNT_ERRORHANDLING posts AND an error exists
	 * in one or more of the posts, the correct posts will be saved no matter what.
	 */
	private static final Integer MAXCOUNT_ERRORHANDLING = 1000;
	/**
	 * The session dictionary name for temporarily stored publications. 
	 * Will be used when PostPublicationCommand.editBeforeImport is true. 
	 */
	public static final String TEMPORARILY_IMPORTED_PUBLICATIONS = "TEMPORARILY_IMPORTED_PUBLICATIONS";

	/**
	 * Extracts the line number from the parser error messages.
	 */
	private static final Pattern lineNumberPattern = Pattern.compile("([0-9]+)");

	/**
	 * the factory used to get an instance of a FileUploadHandler.
	 */
	private FileUploadFactory uploadFactory;

	/**
	 * TODO: we could inject this object using Spring.
	 */
	private final EndnoteToBibtexConverter e2bConverter = new EndnoteToBibtexConverter();


	@Override
	public PostPublicationCommand instantiateCommand() {
		/**
		 * initialize post & resource
		 */
		final PostPublicationCommand command = new PostPublicationCommand();
		command.setGroups(new ArrayList<String>());

		command.setPost(new Post<BibTex>());
		command.setAbstractGrouping(GroupUtils.getPublicGroup().getName());
		command.getPost().setResource(new BibTex());

		return command;
	}

	@Override
	public View workOn(final PostPublicationCommand command) {
		log.debug("workOn started");
		final RequestWrapperContext context = command.getContext();

		/*
		 * only users which are logged in might post -> send them to
		 * login page
		 */
		final BibTex publication = command.getPost().getResource();
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedNoticeException("please log in", LOGIN_NOTICE + publication.getClass().getSimpleName().toLowerCase());
		}

		/* 
		 * If the user entered the post data manually, the EditPublicationController
		 * will handle the remaining work.
		 * 
		 * To find out, if the data was entered manually, a good heuristic is to 
		 * check if an entrytype is given, because that field can't be empty. 
		 */
		if (present(publication.getEntrytype())) {
			log.debug("user has manually entered post data -> forwarding to edit post controller");
			return super.workOn(command);
		}

		/*
		 * This variable will hold the information contained in the bibtex/endnote-file or selection field
		 */
		String snippet = null;

		/*
		 * This handles the cases
		 * 1) the user just started the postPublication process
		 * 2) the user entered a snippet (might be empty)
		 * 3) the user selected a file to upload posts (might be empty)
		 * DOI/ISBN or manual input are handled in EditPostController
		 */
		final String selection = command.getSelection();
		if (present(selection)) {
			/*
			 * The user has entered text into the snippet selection - we use that 
			 */
			log.debug("user has filled selection");
			snippet = this.handleSelection(command, selection);
		} else if(present(command.getFile())) {
			/*
			 * The user uploads a BibTeX or EndNote file
			 */
			log.debug("user uploads a file");
			// get the (never empty) content or add corresponding errors 
			snippet = handleFileUpload(command);
		} else {
			/*
			 * nothing given -> 
			 * user just opened the postPublication Dialogue OR
			 * user send empty snippet or "nonexisting" file
			 * FIXME: that second case should result in some error and hint for the user
			 */
			return Views.POST_PUBLICATION;
		}

		/*
		 * Either a file or a snippet was given,
		 * it's content is now stored in snippet
		 * -> check if valid
		 */

		if (errors.hasErrors()) {
			log.debug("errors found, returning to view");
			if (log.isDebugEnabled()) {
				log.debug(errors);
			}
			return Views.POST_PUBLICATION;
		}

		/*
		 * Extract posts from snippet ...
		 */

		/*
		 * configure the parser
		 */
		final PostBibTeXParser parser = new PostBibTeXParser();
		parser.setDelimiter(command.getDelimiter());
		parser.setWhitespace(command.getWhitespace());
		parser.setTryParseAll(true);

		/*
		 * FIXME: why aren't commas, etc. removed?
		 */

		List<Post<BibTex>> posts = null;

		try {
			/*
			 * Parse the BibTeX snippet	
			 */
			posts = parser.parseBibTeXPosts(snippet);
		} catch (final ParseException ex) {
			errors.reject("error.upload.failed.parse", ex.getMessage());
		} catch (final IOException ex) {
			errors.reject("error.upload.failed.parse", ex.getMessage());
		}

		/*
		 * The errors we have collected until now should be fixed before we proceed.
		 * 
		 * (We did not collect errors due to individual broken BibTeX lines, yet!)
		 */
		if (errors.hasErrors()) {
			return Views.POST_PUBLICATION;
		}

		/*
		 * turn parse exceptions into error messages ...
		 */
		handleParseExceptions(parser.getCaughtExceptions());

		if (!errors.hasErrors() && !present(posts)) {
			/*
			 * no errors ... but also no posts ... Ooops!
			 * the parser was not able to produce posts but did not add errors nor throw exceptions
			 */
			errors.reject("error.upload.failed.parse", "Upload failed because of parser errors.");
			return Views.POST_PUBLICATION;
		}

		/*
		 * If exactly one post has been extracted, and there were no parse exceptions, 
		 * the edit post controller can handle the remaining work.
		 */
		if (posts.size() == 1 && !errors.hasErrors()) {
			final Post<BibTex> post = posts.get(0);
			if (present(post)) {
				/*
				 * Delete the selection, otherwise the AbstractEditPublicationControllers 
				 * workOnCommand() method would try to scrape it.
				 */
				command.setSelection(null);
				command.setPost(post);
				/*
				 * When exactly one post is imported, its tags are not put into 
				 * the tag field. Instead, we show them here as "tags of copied post".
				 */
				command.setCopytags(new LinkedList<Tag>(post.getTags()));
				return super.workOn(command);
			}
		}

		/*
		 * Complete the posts with missing information:
		 * 
		 * add additional information from the form to the 
		 * post (description, groups)... present in both upload tabs
		 */
		for (final Post<BibTex> post: posts) {
			post.setUser(context.getLoginUser());
			post.setDescription(command.getDescription());
			if (!present(post.getTags())) {
				post.setTags(Collections.singleton(TagUtils.getImportedTag()));
			}
			/*
			 * set visibility of this post for the groups, the user specified 
			 */
			GroupingCommandUtils.initGroups(command, post.getGroups());
			/*
			 * hashes have to be set, in order to call the validator
			 */
			post.getResource().recalculateHashes();
		}

		/*
		 * add list of posts to command for showing them to the user 
		 * (such that he can edit them)
		 */
		final ListCommand<Post<BibTex>> postListCommand = new ListCommand<Post<BibTex>>(command);
		postListCommand.setList(posts);
		/*
		 * FIXME: rename the "bibtex" attribute of the command (hint: we try
		 * to avoid the name "bibtex" wherever possible)
		 * (hint: errors.pushNestedPath("bibtex"); in the PostPublicationCommandValidator 
		 * then has to be adapted, too. As does the code in the JSPs, of course.) 
		 */
		command.setBibtex(postListCommand);

		/*
		 * validate the posts
		 */
		ValidationUtils.invokeValidator(new PostPublicationCommandValidator(), command, errors);

		/*
		 * We try to store only posts that have no validation errors.
		 */
		final Map<Post<BibTex>, Integer> postsToStore = getPostsWithNoValidationErrors(posts);
		log.debug("will try to store " + postsToStore.size() + " of " + posts.size() + " posts in database");

		/*
		 * finally store the posts
		 */
		if (command.isEditBeforeImport()) {
			/*
			 * user wants to edit the posts before storing them 
			 * -> put them into the session
			 */
			setSessionAttribute(TEMPORARILY_IMPORTED_PUBLICATIONS, posts);
		} else {
			/*
			 * the publications are saved in the database
			 */
			storePosts(postsToStore, command.getOverwrite());
		}

		/*
		 * If there were any errors, some posts were not stored in the database. We
		 * need to get them from the session later on, thus we store them there.
		 */
		if (errors.hasErrors())	{
			/*
			 * Trigger the correct setting of the "delete/save" check boxes on
			 * the batch edit page.
			 */
			command.setDeleteCheckedPosts(false);
			/*
			 * save posts in session
			 */
			setSessionAttribute(TEMPORARILY_IMPORTED_PUBLICATIONS, posts);
		}

		/*
		 * If the user wants to store the posts permanently AND (his posts have
		 * no errors OR he ignores the errors OR the number of bibtexes is
		 * greater than the treshold, we will forward him to the appropriate
		 * site, where he can delete posts (they were saved)
		 */
		if (!command.isEditBeforeImport() && (!errors.hasErrors() || posts.size() > MAXCOUNT_ERRORHANDLING)) {
			command.setDeleteCheckedPosts(true); //posts will have to get saved, because the user decided to
		} else {
			command.setDeleteCheckedPosts(false);
		}

		/*
		 * If there are errors now or not - we return to the post
		 * publication view to let the user edit his posts. 
		 */
		return Views.POST_PUBLICATION;
	}

	/**
	 * Convertes a String into a BibTeX String
	 * if selection is BibTeX nothing happens
	 * if selection is EndNote is will be converted to BibTex
	 * @param selection
	 * @return
	 */
	private String handleSelection(final PostPublicationCommand command, final String selection) {
		// FIXME: at this point we must first convert to bibtex!
		if (EndnoteToBibtexConverter.canHandle(selection)) {
			return this.e2bConverter.endnoteToBibtex(selection);
		}
		if (RisToBibtexConverter.canHandle(selection)) {
			return new RisToBibtexConverter().RisToBibtex(selection);
		}
		/*
		 * should be BibTeX
		 */
		return selection;
	}

	/**
	 * Checks each post for validation errors and returns only those posts, 
	 * that don't have any errors. The posts are returned in a hashmap, where
	 * each post points to its position in the original list such that we can
	 * later add errors (from the database) at the correct position.  
	 *  
	 * @param posts
	 * @return
	 */
	private Map<Post<BibTex>, Integer> getPostsWithNoValidationErrors(final List<Post<BibTex>> posts) {
		final Map<Post<BibTex>, Integer> storageList = new LinkedHashMap<Post<BibTex>, Integer>(); 

		/*
		 * iterate over all posts
		 */
		for (int i = 0; i < posts.size(); i++) {
			/*
			 * check, if this post has field errors
			 */
			if (!present(errors.getFieldErrors("bibtex.list[" + i + "]*"))) {
				log.debug("post no. " + i + " has no field errors");
				/*
				 * post has no field errors --> try to store it in database
				 * 
				 * We also remember the original position of the post to 
				 * add error messages later.
				 */
				storageList.put(posts.get(i), i);
			}
		}
		return storageList;
	}

	/**
	 * Extracts the parse exceptions and adds the line numbers with errors
	 * to the errors object. 
	 * 
	 * @param parseExceptions
	 */
	private void handleParseExceptions(final ParseException[] parseExceptions) {
		final StringBuilder buf = new StringBuilder();
		boolean lineFound = false;
		for (final ParseException parseException : parseExceptions) {
			final Matcher m = lineNumberPattern.matcher(parseException.getMessage());
			if (m.find()) {
				/*
				 * if we have already found a broken line, append ", "
				 */
				if (lineFound) {
					buf.append(", ");
				}
				/*
				 * we have found a line number -> add it
				 */
				buf.append(m.group(1));
				lineFound = true;
			}
		}
		if (lineFound) {
			errors.reject("import.error.erroneous_line_numbers", new Object[]{buf}, "Your submitted publications contain errors at lines {0}.");
		}
	}

	/**
	 * Handles an uploaded file and returns its contents - if necessary 
	 * after converting EndNote to BibTeX;
	 * 
	 * @param command
	 * @return
	 */
	private String handleFileUpload(final PostPublicationCommand command) {
		/*
		 * get temp file
		 */
		File file = null;
		try {

			final CommonsMultipartFile uploadedFile = command.getFile();
			final FileUploadInterface uploadFileHandler = this.uploadFactory.getFileUploadHandler(Collections.singletonList(uploadedFile.getFileItem()), FileUploadInterface.bibtexEndnoteExt);
			/*
			 * FIXME: the upload file handler throws an exception, 
			 * if the file type does not match - this exception also comes, when
			 * no file is given at all. We should check for empty file names and
			 * give a specific error message then.
			 */
			final Document uploadedDocument = uploadFileHandler.writeUploadedFile();
			file = uploadedDocument.getFile();

			final String fileName = uploadedDocument.getFileName();

			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), command.getEncoding()));

			String fileContent = null;
			if (StringUtils.matchExtension(fileName, FileUploadInterface.bibtexEndnoteExt[1])) {
				/*
				 * In case the uploaded file is in EndNote format, we convert it to BibTeX.				
				 */
				log.debug("the file is in EndNote format");
				fileContent = e2bConverter.endnoteToBibtexString(reader);
			} else {
				/*
				 * or just use it as it is ...
				 */
				log.debug("the file is in BibTeX format");
				fileContent = StringUtils.getStringFromReader(reader);
			}
			if (present(fileContent)) {
				return fileContent;
			}
			errors.reject("error.upload.failed.emptyFile", "The specified file is empty.");
			return null;

		} catch (final ConversionException e) {
			errors.reject("error.upload.failed.conversion", "An error occurred during converting your EndNote file to BibTeX.");
		} catch (final UnsupportedFileTypeException e) {
			/*
			 * FIXME: the key is missing! We need to get the supported file types from the exception
			 */
			errors.reject("error.upload.failed.filetype", FileUploadInterface.bibtexEndnoteExt, e.getMessage());
		} catch (final Exception ex1) {
			errors.reject("error.upload.failed.fileAccess", "An error occurred while accessing your file.");
		} finally {
			/*
			 * clear temporary file
			 * FIXME: is this method always called?
			 */
			log.debug("deleting uploaded temp file");
			if (file != null) {
				file.delete();
			}
		}
		return null;
	}


	/**
	 * Tries to save the posts in the database.
	 * 
	 * If posts already exist in the database and <code>overwrite</code> is <code>true</code>,
	 * those posts are overwritten (otherwise they produce an error). 
	 * Posts that have errors will be rejected in any case.
	 * 
	 * FIXME: the error handling here is almost identical to that
	 * in {@link BatchEditController#storePosts}
	 * 
	 * @param postsToStore
	 * @param overwrite - posts which already exist are overwritten, if <code>true</code>
	 */
	private void storePosts(final Map<Post<BibTex>, Integer> postsToStore, final boolean overwrite) {
		try {
			/*
			 * Try to save all posts in one transaction. 
			 * (Hint: it's not a transaction in the database sense, but basically we try to 
			 * save all posts and collect errors for posts we can't save.)
			 * 
			 */
			logic.createPosts(new LinkedList<Post<?>>(postsToStore.keySet()));
		} catch (final DatabaseException e) {
			/*
			 * get error messages 
			 */
			final Map<String, List<ErrorMessage>> errorMessages = e.getErrorMessages(); 
			log.debug("caught database exception, found " +  errorMessages.size() + " errors");
			/*
			 * these posts will be updated
			 */
			final LinkedList<Post<?>> postsForUpdate = new LinkedList<Post<?>>();
			/*
			 * check for all posts what kind of errors they have 
			 */			
			for (final Entry<Post<BibTex>, Integer> entry : postsToStore.entrySet()) {
				/*
				 * get post and its position in the original list of posts 
				 */
				final Post<BibTex> post = entry.getKey();
				final Integer i = entry.getValue();
				log.debug("found errors in post no. " + i);
				/*
				 * get all error messages for this post
				 */
				final List<ErrorMessage> postErrorMessages = errorMessages.get(post.getResource().getIntraHash());
				if (present(postErrorMessages))	{
					boolean hasErrors = false;
					boolean hasDuplicate = false;
					/*
					 * go over all error messages 
					 */
					for (final ErrorMessage errorMessage : postErrorMessages) {
						log.debug("found error " + errorMessage);
						final String errorItem;
						if (errorMessage instanceof DuplicatePostErrorMessage) {
							hasDuplicate = true;
							if (overwrite) {
								/*
								 * if we shall overwrite posts, duplicates are no errors
								 */
								continue;
							} 
							errorItem = "resource";
						} else if (errorMessage instanceof SystemTagErrorMessage) {
							errorItem = "tags";
						} else {
							errorItem = "resource";
						}
						/*
						 * add error to error list
						 */
						hasErrors = true;
						errors.rejectValue("bibtex.list[" + i + "]." + errorItem, errorMessage.getErrorCode(), errorMessage.getParameters(), errorMessage.getDefaultMessage());
					}
					/*
					 * If the post has no errors, but is a duplicate, we add it to
					 * the list of posts which should be updated. 
					 */
					if (!hasErrors && hasDuplicate) {
						postsForUpdate.add(post);
					}
				}
			}

			/*
			 * If we got ONLY duplicate "errors", we save the non-duplicate ones
			 * and update the others, if isOverwrite is true. Same is true, if
			 * the number of publications is greater than the threshold.
			 */
			try {
				if (overwrite) {
					log.debug("trying to update " + postsForUpdate.size() + " posts");
					logic.updatePosts(postsForUpdate, PostUpdateOperation.UPDATE_ALL);
				}
			} catch (final DatabaseException ex) {
				final Map<String, List<ErrorMessage>> allErrorMessages = ex.getErrorMessages();
				log.debug("caught database exception, found " + allErrorMessages.size() + " errors");
				/*
				 * checking each post for errors
				 */
				for (final Post<?> post : postsForUpdate) {
					/*
					 * get intra hash and original position of post
					 */
					final String intraHash = post.getResource().getIntraHash();
					/*
					 * The i-th position in the list at hand is not the
					 * same as the i-th position in the original list! ->use mapping
					 */
					final int i = postsToStore.get(post);
					log.debug("checking post no. " + i + " with intra hash " + intraHash);
					final List<ErrorMessage> postErrorMessages = allErrorMessages.get(intraHash);
					if (present(postErrorMessages)) {
						log.debug("found " + postErrorMessages.size() + "error(s) on post no. " + i);
						for (final ErrorMessage msg : postErrorMessages) {
							/*
							 * handle system tag error messages
							 */
							if (msg instanceof SystemTagErrorMessage) {
								log.debug("found system tag error");
								errors.rejectValue("bibtex.list[" + i + "].tags", msg.getErrorCode(), msg.getParameters(), msg.getDefaultMessage());
							}
						}
					}
				}
				log.debug("all field errors: " + errors.getFieldError("bibtex.*"));
			}
		}
	}

	@Override
	protected PostPublicationCommand instantiateEditPostCommand() {
		return new PostPublicationCommand();
	}

	/**
	 * @return the uploadFactory
	 */
	public FileUploadFactory getUploadFactory() {
		return this.uploadFactory;
	}

	/**
	 * @param uploadFactory the uploadFactory to set
	 */
	public void setUploadFactory(final FileUploadFactory uploadFactory) {
		this.uploadFactory = uploadFactory;
	}
}
