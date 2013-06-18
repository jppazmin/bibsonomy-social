<%--


     BibSonomy-Webapp - The webapplication for Bibsonomy.

     Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
                               University of Kassel, Germany
                               http://www.kde.cs.uni-kassel.de/

     This program is free software; you can redistribute it and/or
     modify it under the terms of the GNU General Public License
     as published by the Free Software Foundation; either version 2
     of the License, or (at your option) any later version.

     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.

     You should have received a copy of the GNU General Public License
     along with this program; if not, write to the Free Software
     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

--%>

<%@ page language="java" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="servlets.listeners.*" %>
<%@ page import="helpers.*" %>
<%@ page import="resources.*" %>
<%@ page contentType="text/html;charset=UTF-8" %> 
<%@ page pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="/WEB-INF/taglibs/mytaglib.tld" prefix="mtl" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<sql:setDataSource dataSource="jdbc/bibsonomy" var="dataSource"/> 

<%
    response.setHeader("Pragma","no-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",-1);
    response.setDateHeader("Last-Modified",0);
%>



<%--
 INTRODUCTION:
  This file is included at the top of every page that is displayed to the user.
  If a new page is created that is displayed to the user this file should be
  included with the <jsp:include ...> method.
  
  INPUT PARAMETER: title 
  
  Calling example:
  
  <jsp:include page="html_header.jsp">
    <jsp:param name="title" value="<%=requUser%>" />
  </jsp:include>
  
--%>

<%-- 
  This selects the correct values for basePath and stuff like that,
  depending on the Hostname (e.g. localhost vs. other)
--%>

<%@ page import="servlets.listeners.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs/mytaglib.tld" prefix="mtl" %>

<%--
  HTML starts here
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/eswc2008.css">
    <link rel="icon" href="/resources/image/favicon.png" type="image/png">
    <script type="text/javascript" src="/resources/javascript/functions.js"></script>
    <script type="text/javascript" src="/resources/javascript/tooltip.js"></script>
    <script type="text/javascript" src="/resources/javascript/style.js"></script>
    <script type="text/javascript" src="/resources/javascript/chrome.js"></script>
    <meta name="author" content="Knowledge and Data Engineering Group, University of Kassel, Germany">
    <meta name="copyright" content="Knowledge and Data Engineering Group, University of Kassel, Germany">
    <meta name="email" content="webmaster@bibsonomy.org">
    <meta name="keywords" lang="en" content="eswc2008, conference, proceedings, semantic web, international semantic web conference, 2007, bibsonomy"> 
    <meta name="description" lang="en" content="ESWC 2008 conference proceedings on the BibSonomy bookmark/BibTeX tagging and sharing system">
    <link rel="alternate" type="application/atom+xml" title="BibSonomy Blog - Atom" href="http://bibsonomy.blogspot.com/feeds/posts/default">
    <link rel="alternate" type="application/rss+xml"  title="BibSonomy Blog - RSS"  href="http://bibsonomy.blogspot.com/feeds/posts/default?alt=rss">
    <title><c:out value="${projectName}" /> :: ESWC 2008 </title>
  </head>
  
  <body>