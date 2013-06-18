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

package org.bibsonomy.rest.strategy;

import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.util.List;

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * @author Jens Illig
 * @version $Id: AbstractGetListStrategy.java,v 1.9 2011-06-09 12:03:28 rja Exp $
 * @param <L> 
 */
public abstract class AbstractGetListStrategy<L extends List<?>> extends Strategy {
	private final ViewModel view;
	
	/**
	 * @param context
	 */
	public AbstractGetListStrategy(final Context context) {
		super(context);
		this.view = new ViewModel();
		this.view.setStartValue(context.getIntAttribute("start", 0));
		this.view.setEndValue(context.getIntAttribute("end", 20));
		this.view.setOrder(context.getStringAttribute("order", null));
		if (view.getStartValue() > view.getEndValue()) {
			throw new BadRequestOrResponseException("start must be less than or equal end");
		}
	}

	@Override
	public final void perform(final ByteArrayOutputStream outStream) throws InternServerException {
		final L resultList = getList();
		
		if (resultList.size() != (getView().getEndValue() - getView().getStartValue())) {
			this.view.setEndValue( resultList.size() + this.view.getStartValue());
		} else {
			this.view.setUrlToNextResources( buildNextLink() );
		}
		render(writer, resultList);
	}

	protected abstract void render(Writer writer, L resultList);

	protected abstract L getList();

	private final String buildNextLink() {
		final StringBuilder sb = getLinkPrefix();
		sb.append("?start=").append(getView().getEndValue()).append("&end=").append(getView().getEndValue() + getView().getEndValue() - getView().getStartValue());
		appendLinkPostFix(sb);
		return sb.toString();
	}

	protected abstract StringBuilder getLinkPrefix();

	protected abstract void appendLinkPostFix(StringBuilder sb);

	protected ViewModel getView() {
		return this.view;
	}
}