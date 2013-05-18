/**
 *
 *  BibSonomy-Logging - Logs clicks from users of the BibSonomy webapp.
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

/**
 * 
 */
package org.bibsonomy.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author sst
 *
 */

public class LogMe extends HttpServlet {
	

        private static final long serialVersionUID = 7035035642527692979L;
        
	private static final Log log = LogFactory.getLog(LogMe.class);

	public static List<String> getMatches(Pattern pattern, String text, int splitAtSpace) {
		List<String> matches = new ArrayList<String>();
		Matcher m = pattern.matcher(text);
		while(m.find()) {
			
			if (m.group(1).contains(" ") && splitAtSpace==1 ) {
				String[] tempMatchesM = m.group(1).split(" ");
				for (String s : tempMatchesM) {
					matches.add(s);
				}
			}
			else {
				matches.add(m.group(1));
			}
			 
		}
		return matches;
	} 

	public static List<String> getMatches(Pattern pattern, String text) {
		return getMatches(pattern, text, 0);
	} 

	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {


		// Antwort am Anfang senden, damit Browser nicht warten muss
		response.setContentType("text/html");
		PrintWriter pw = new PrintWriter (response.getOutputStream());
		
/*
		pw.println("");
		pw.println("<head><title>Sven TEST</title></head>");
		pw.println("");
		pw.println("<body>");
		pw.println("");
		pw.println("Hallo!");
		pw.println("");
		pw.println("</body>");
		pw.println("</html>");
*/

//		pw.println("");
	    pw.close();

	    
		
		
		int dompath_length = 0;
		
		try {
			dompath_length = req.getParameter("dompath").length();
		} catch (NullPointerException e) {
			// kein dompath angegeben 
		}
		
		log.debug("dompath_lenght = " + dompath_length);

		// if dompath is empty 
		if (dompath_length>0)
		{

	//		String userAgent =  req.getHeader("user-agent");
	//		System.out.println("Hallo " + userAgent);
	
			// schreibe Cookies in Cookie-Array cookie
			Cookie[] cookies = req.getCookies();
	//		System.out.println (req.getParameter("dompath"));
	//		System.out.println (req.getHeader("remote_addr"));
			
			@SuppressWarnings("unchecked")
			Enumeration<String> headerNames = req.getHeaderNames();
			String cookieUsername = "";
	    	String cookieSessionId = "";
	    	String completeHeader ="";
	    	String logType ="";
	    	int separatorIndex = 0;
	        
			while (headerNames.hasMoreElements()) {
			  String element = headerNames.nextElement();
	          completeHeader += (element + ": " + req.getHeader(element) + "\n");
	        }
	
			if (cookies != null) {
		        for (int i = 0; i < cookies.length; i++) {
	//	        	System.out.println ("Cookie " + i + ": " + cookies[i].getName() + " = " + cookies[i].getValue());
		        	if (cookies[i].getName().equals("_currUser")){
		        		separatorIndex = cookies[i].getValue().indexOf("%20");
		        		cookieUsername = cookies[i].getValue().substring(0, separatorIndex);
		        	}
		        
		        	if (cookies[i].getName().equals("JSESSIONID")){
		        		cookieSessionId = cookies[i].getValue();
		        	}
		        }
			}
			
			
			// build an array for used ids with all char strings, beginning with # and ending with / or . 
			// (ending with non a-z, A-Z, 0-9 or -)
			// regular expression: /#([A-Za-z0-9\-]+)/
			
			 Pattern p = Pattern.compile("#([a-zA-Z0-9-_]+)");
			 String text = req.getParameter("dompath2");
			 @SuppressWarnings("unused")
			List<String> idArray = getMatches(p, text);
			
//			 System.out.println (idArray.toString());
			 
			
			
			// then build another array for used classes with all char strings, beginning with . and ending with / or .
			// if class contains spaces, split it to multiple classes

			// regular expression: /\.[A-Za-z0-9\- ]+/
			
			 p = Pattern.compile("\\.([a-zA-Z0-9- _]+)");
			 text = req.getParameter("dompath2");
			 List<String> classArray = getMatches(p, text,1);

//			 System.out.println (classArray.toString());
			
	
			// logType is the type of logging information
			// where in page has user clicked? Bookmark area,...  
			logType = req.getParameter("dompath").replaceFirst("^[^#]+#", "");
			logType = logType.replaceFirst("/.*$", "");
			
			
			// if class tagcloud exists, add to type with blank inbetween
			// if class bmown set bmown-value to 1 otherwise to 0

			String abmown = "0";
			// if classArray contains class bmown, then link is users own bookmark
			if (classArray.contains("bmown")) {
				abmown = "1";
			}
			else {
				abmown = "0";
			}
			
			
			org.bibsonomy.logging.Log LogData = new org.bibsonomy.logging.Log();
			
			LogData.setAhref(req.getParameter("ahref"));
			LogData.setAcontent(req.getParameter("acontent"));
			LogData.setAnumberofposts(req.getParameter("numberofposts"));
			LogData.setDompath(req.getParameter("dompath"));
			LogData.setDompath2(req.getParameter("dompath2"));
			LogData.setType(logType);
			LogData.setPageurl(req.getParameter("pageurl"));
			LogData.setUseragent(req.getHeader("user-agent"));
			
			if (req.getParameter("username").isEmpty()) {
				LogData.setUsername(cookieUsername);
			}
			else
			{
				LogData.setUsername(req.getParameter("username"));
			}
			
			LogData.setSessionid(cookieSessionId);
			LogData.setHost(req.getHeader("host"));
			LogData.setCompleteheader(completeHeader);
			LogData.setXforwardedfor(req.getHeader("X-Forwarded-For"));
			LogData.setListpos(req.getParameter("listpos"));
			LogData.setWindowsize(req.getParameter("windowsize"));
			LogData.setMouseclientpos(req.getParameter("mouseclientpos"));
			LogData.setMousedocumentpos(req.getParameter("mousedocumentpos"));
			LogData.setAbmown(abmown);
			LogData.setReferer(req.getParameter("referer"));
			
			log.debug("LogData to insert:\n" + LogData.toString());
			log.info("Clicked at anchor with shown text: " + LogData.getAcontent());
			
			try {
				QueryDB.getInstance().insertLogdata(LogData);
				log.info("Database access: insertLogdata ok");
				
			} catch (SQLException e) {
				log.error("Database error: insertLogdata", e);
			}
			
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse response)
	throws ServletException, IOException {
		log.debug("POST-Request");
		doGet (req, response);
	}




}
