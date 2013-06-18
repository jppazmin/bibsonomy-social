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

package org.bibsonomy.rest.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.bibtex.util.BibtexParserUtils;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.util.BibTexUtils;

/**
 * Validates the given model.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ServersideModelValidator.java,v 1.7 2011-07-14 09:49:49 rja Exp $
 */
public class ServersideModelValidator implements ModelValidator {
	private static final Log log = LogFactory.getLog(ServersideModelValidator.class);

	private static final boolean LAST_FIRST_NAMES = true;
	private static final String BIBTEX_IS_INVALID_MSG = "The validation of the BibTeX entry failed: ";
	
	private static ServersideModelValidator modelValidator;

	/** Get an instance of this model validator
	 * 
	 * @return An instance of the model validator.
	 */
	public static ServersideModelValidator getInstance() {
		if (ServersideModelValidator.modelValidator == null) {
			ServersideModelValidator.modelValidator = new ServersideModelValidator();
		}
		return ServersideModelValidator.modelValidator;
	}
	
	private ServersideModelValidator() {}
	
	/**
	 * Parses the given publication using the BibTeX parser.
	 * 
	 * Additionally, exchanges author and editor names with normalized versions.
	 * 
	 * @see org.bibsonomy.rest.validation.ModelValidator#checkPublication(org.bibsonomy.model.BibTex)
	 * 
	 * FIXME: oh shit, see what this method does:
	 * 
  ServerSideModelValidator.checkPublication(final BibTex publication)

  pn = PersonNameUtils.extractList(b.getAuthor())

  s = PersonNameUtils.serializePersonNames(pn)


  b = SimpleBibTeXParser().parseBibTeX(s);


  pn = PersonNameUtils.extractList(b.getAuthor())

  PersonNameUtils.serializePersonNames(pn)

	 * 
	 */
//	@Override
//	public void checkPublication(final BibTex publication) {
//		/*
//		 * parse BibTeX so see whether the entry is valid
//		 */
//		final BibTex parsedBibTeX;
//		try {
//			parsedBibTeX = new SimpleBibTeXParser().parseBibTeX(BibTexUtils.toBibtexString(publication));
//		} catch (ParseException ex) {
//			log.error(ex.getMessage());
//			throw new ValidationException(BIBTEX_IS_INVALID_MSG + "Error while parsing BibTeX.");
//		} catch (final IOException ex) {
//			log.error(ex.getMessage());
//			throw new ValidationException(BIBTEX_IS_INVALID_MSG + "I/O Error while parsing BibTeX.");
//		}
//		/*
//		 * FIXME: validator is modifying the publication
//		 */
//		publication.setAuthor(PersonNameUtils.serializePersonNames(parsedBibTeX.getAuthorList(), LAST_FIRST_NAMES));
//		publication.setEditor(PersonNameUtils.serializePersonNames(parsedBibTeX.getEditorList(), LAST_FIRST_NAMES));
//	}
	
	@Override
	public void checkPublication(final BibTex publication) {
		// parse Bibtex so see whether the entry is valid
		final BibtexParserUtils bibutil = new BibtexParserUtils( BibTexUtils.toBibtexString(publication) );	
		
		/*
		 * FIXME: validator is modifying the publication
		 */
		publication.setAuthor( bibutil.getFormattedAuthorString() );
		publication.setEditor( bibutil.getFormattedEditorString() );
	}
}