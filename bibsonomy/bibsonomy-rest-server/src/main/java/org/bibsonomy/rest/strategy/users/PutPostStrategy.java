/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
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

package org.bibsonomy.rest.strategy.users;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.Writer;
import java.util.Collections;
import java.util.Date;

import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.errors.DuplicatePostErrorMessage;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.errors.IdenticalHashErrorMessage;
import org.bibsonomy.common.errors.MissingFieldErrorMessage;
import org.bibsonomy.common.errors.UnspecifiedErrorMessage;
import org.bibsonomy.common.errors.UpdatePostErrorMessage;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.common.exceptions.InvalidModelException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.strategy.AbstractUpdateStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: PutPostStrategy.java,v 1.31 2011-04-06 12:10:04 nosebrain Exp $
 */
public class PutPostStrategy extends AbstractUpdateStrategy {
	private final String userName;
	protected final String resourceHash;

	/**
	 * Create new PutPostStrategy
	 * 
	 * @param context
	 * 			- the context of the request
	 * @param userName
	 * 			- user name of the user who submitted the request
	 * @param resourceHash
	 * 			- intraHash of the resource to be updated
	 */
	public PutPostStrategy(final Context context, final String userName, final String resourceHash) {
		super(context);
		this.userName = userName;
		this.resourceHash = resourceHash;
	}

	@Override
	public void canAccess() {
		if (!this.userName.equals(this.getLogic().getAuthenticatedUser().getName())) throw new AccessDeniedException();
	}

	@Override
	public String getContentType() {
		return "resourcehash";
	}

	@Override
	protected void render(final Writer writer, final String newResourceHash) {
		this.getRenderer().serializeResourceHash(writer, newResourceHash);		
	}

	@Override
	protected String update() throws InternServerException, BadRequestOrResponseException {
		final Post<?> post = this.getPost();
		/*
		 * XXX: neither the client nor the REST API will calculate the new
		 * hash - this will be done by the logic behind the LogicInterface!
		 */ 		
		try {
			return this.getLogic().updatePosts(Collections.<Post<?>>singletonList(post), PostUpdateOperation.UPDATE_ALL).get(0);
		} catch (final DatabaseException de) {
			for (final String hash: de.getErrorMessages().keySet()) {
				for (final ErrorMessage em: de.getErrorMessages(hash)) {
					if (em instanceof DuplicatePostErrorMessage) {
						// duplicate post detected => handle this
						// before this would have been an IllegalArgumentException
						throw new BadRequestOrResponseException(em.toString());
					}
					if (em instanceof UpdatePostErrorMessage) {
						// a non-existing post was tried to be updated
						// this used to cause an ResourceNotFoundException
						throw new NoSuchResourceException(em.toString());
					}
					if (em instanceof IdenticalHashErrorMessage) {
						// the new post would have the same hash as an old one
						// this used to cause an IllegalArgumentException
						throw new BadRequestOrResponseException(em.toString());
					}
					if (em instanceof MissingFieldErrorMessage) {
						// some compulsory field of the post was missing
						// this used to cause an InvalidModelException
						throw new BadRequestOrResponseException(em.toString());
					}
					if (em instanceof UnspecifiedErrorMessage) {
						final Exception ex = ((UnspecifiedErrorMessage)em).getException();
						if (present(ex)) {
							if (ex instanceof InvalidModelException) {
								throw new BadRequestOrResponseException(ex.getMessage());
							}
							if (ex instanceof ResourceNotFoundException) {
								throw new NoSuchResourceException(ex.getMessage());
							}
						}
					}
				}
			}
			// if none of the errors handled above occurred we throw the original exception
			throw de;
		}
	}

	/**
	 * @return the post to update
	 */
	protected Post<? extends Resource> getPost() {
		final Post<? extends Resource> post = this.getRenderer().parsePost(this.doc);
		/*
		 * set postingdate to current time
		 */
		post.setDate(new Date());				
		/*
		 * set the (old) intrahash of the resource as specified in the URL
		 */
		post.getResource().setIntraHash(this.resourceHash);
		return post;
	}
}