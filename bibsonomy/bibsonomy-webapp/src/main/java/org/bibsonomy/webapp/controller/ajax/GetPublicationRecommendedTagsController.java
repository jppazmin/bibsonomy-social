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

package org.bibsonomy.webapp.controller.ajax;

import org.bibsonomy.model.BibTex;


/**
 * Class handling ajax requests for given post's recommendations.
 *    
 * @author fei
 * @version $Id: GetPublicationRecommendedTagsController.java,v 1.7 2010-05-10 12:57:56 nosebrain Exp $
 */
public class GetPublicationRecommendedTagsController extends RecommendationsAjaxController<BibTex>  {

	@Override
	protected BibTex initResource() {
		final BibTex publication = new BibTex();
		/*
		 * set default values
		 */
		publication.setUrl("http://");
		return publication;
	}
}
