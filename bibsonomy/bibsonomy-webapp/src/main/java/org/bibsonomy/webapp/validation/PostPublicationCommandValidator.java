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

package org.bibsonomy.webapp.validation;

import java.util.List;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.actions.PostPublicationCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.validation.Errors;

/**
 * @author ema
 * @version $Id: PostPublicationCommandValidator.java,v 1.4 2011-05-28 14:23:33 nosebrain Exp $
 */
public class PostPublicationCommandValidator implements Validator<PostPublicationCommand> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		return PostPublicationCommand.class.equals(clazz);
	}

	/**
	 * this method names the errors of its target items "<resource.getInterHash()>".resource.<XYZ>
	 */
	@Override
	public void validate(final Object target, final Errors errors) {
		final PostPublicationCommand command = (PostPublicationCommand) target;
		errors.pushNestedPath("bibtex");
		
		final ListCommand<Post<BibTex>> listCommand = command.getBibtex();
		
		//validate resource
		final List<Post<BibTex>> list = listCommand.getList();
		final PostValidator<BibTex> validator = new PostValidator<BibTex>();

		for (int i = 0; i < list.size(); i++) {
			errors.pushNestedPath("list[" + i + "]");
			
			validator.validateResource(errors, list.get(i).getResource());
			validator.validateGroups(errors, command);
			
			errors.popNestedPath();
		}

		errors.popNestedPath();
	}
}

