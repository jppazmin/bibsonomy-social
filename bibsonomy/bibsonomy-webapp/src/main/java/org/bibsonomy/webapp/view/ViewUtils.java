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

import java.util.Map;

import org.springframework.validation.BindingResult;


/**
 * @author dzo
 * @version $Id: ViewUtils.java,v 1.1 2010-11-11 13:52:38 nosebrain Exp $
 */
public abstract class ViewUtils {

	/**
	 * Gets the BindingResult (containing errors) from the model.
	 * @param model
	 * @return the binding result
	 */
	public static BindingResult getBindingResult(final Map<String, Object> model){
		for (final Object key : model.keySet() ){
			if (((String)key).startsWith(BindingResult.MODEL_KEY_PREFIX)) {
				return (BindingResult) model.get(key);
			}
		}
		
		return null;
	}

}
