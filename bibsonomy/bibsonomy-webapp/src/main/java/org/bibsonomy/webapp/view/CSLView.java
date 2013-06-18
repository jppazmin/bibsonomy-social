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

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.PropertyNameProcessor;
import net.sf.json.util.PropertyFilter;

import org.bibsonomy.layout.csl.CslModelConverter;
import org.bibsonomy.layout.csl.model.Person;
import org.bibsonomy.layout.csl.model.Date;
import org.bibsonomy.layout.csl.model.Record;
import org.bibsonomy.layout.csl.model.RecordList;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.webapp.command.SimpleResourceViewCommand;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.web.servlet.view.AbstractView;

/**
 * View to export data in CSL-compatible JSON-Format
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: CSLView.java,v 1.1 2011-04-19 16:53:54 dbe Exp $
 */
public class CSLView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
			response.setContentType("text/plain"); // FIXME: this is just for
													// testing purposes; should
													// be changed to
													// "application/json" in
													// real setting
			response.setCharacterEncoding("UTF-8");

			/*
			 * output stream
			 */
			final ServletOutputStream outputStream = response.getOutputStream();
			final OutputStreamWriter writer = new OutputStreamWriter(outputStream);

			/*
			 * loop over records, convert to CSL model and then to JSON
			 */
			final List<Post<BibTex>> publicationList = command.getBibtex().getList();
			if (publicationList != null) {
				RecordList recList = new RecordList();
				for (final Post<BibTex> post : publicationList) {
					final Record rec = CslModelConverter.convertPost(post);
					recList.add(rec);
				}
				writer.write(JSONSerializer.toJSON(recList, getJsonConfig()).toString());
				writer.close();
			}
		}
	}

	private JsonConfig getJsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		// output only not-null fields
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				if (value == null) {
					return true;
				}
				return false;
			}
		});
		// transform underscores into "-"
		jsonConfig.registerJsonPropertyNameProcessor(Person.class, new PropertyNameProcessor() {

			@Override
			public String processPropertyName(Class arg0, String arg1) {
				return arg1.replace("_", "-");
			}
		});
		jsonConfig.registerJsonPropertyNameProcessor(Record.class, new PropertyNameProcessor() {

			@Override
			public String processPropertyName(Class arg0, String arg1) {
				// special handling for abstract field
				if ("abstractt".equals(arg1)) {
					return "abstract";
				}
				return arg1.replace("_", "-");
			}
		});
		jsonConfig.registerJsonPropertyNameProcessor(Date.class, new PropertyNameProcessor() {

			@Override
			public String processPropertyName(Class arg0, String arg1) {
				return arg1.replace("_", "-");
			}
		});
		return jsonConfig;
	}

}
