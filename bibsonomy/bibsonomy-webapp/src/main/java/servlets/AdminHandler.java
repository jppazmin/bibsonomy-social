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

package servlets;
import helpers.database.DBStatisticsManager;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.User;
import org.bibsonomy.util.spring.security.AuthenticationUtils;



/**
 * Gathers statistics for the admin_statistics.jsp page
 *
 */
@Deprecated
public class AdminHandler extends HttpServlet {
	
	private static final long serialVersionUID = 3691036578076309554L;
	private static final Log log = LogFactory.getLog(AdminHandler.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}	
	
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * check user name (only admins are allowed)
		 */
		final User user = AuthenticationUtils.getUser();

		if (!Role.ADMIN.equals(user.getRole())) {
			response.sendRedirect("/login");
			return;
		}

		
		if ("ajax".equals(request.getParameter("action"))) {
			/*
			 * extract method name
			 */
			final String var = request.getParameter("var");
			final String stats = "stats.";
			final int length = stats.length();

			if (var.startsWith(stats)) {
				/*
				 * construct method name
				 */
				final String op = "get" + var.substring(length, length + 1).toUpperCase() + var.substring(length + 1);
				try {
					/*
					 * get and invoke method
					 */
					final Method meth = DBStatisticsManager.class.getMethod(op, new Class[]{String.class});
					final Integer result = (Integer)meth.invoke(DBStatisticsManager.class, request.getParameter("spammer"));
					/*
					 * write result
					 */
					final OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
					writer.write(result.toString());
					writer.close();
				} catch (Exception e) {
					log.fatal(e);
				}
			}
		}
	}

}