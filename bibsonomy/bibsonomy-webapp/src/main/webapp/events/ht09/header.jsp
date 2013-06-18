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
    <link rel="stylesheet" type="text/css" href="/resources/css/ht09.css">
    <link rel="icon" href="/resources/image/favicon.png" type="image/png">
    <script type="text/javascript" src="/resources/javascript/functions.js"></script>
    <script type="text/javascript" src="/resources/javascript/tooltip.js"></script>
    <script type="text/javascript" src="/resources/javascript/style.js"></script>
    <script type="text/javascript" src="/resources/javascript/chrome.js"></script>
    <script src="/resources/javascript/localized_strings_en.js" type="text/javascript">&nbsp;</script>
    <meta name="author" content="Knowledge and Data Engineering Group, University of Kassel, Germany">
    <meta name="copyright" content="Knowledge and Data Engineering Group, University of Kassel, Germany">
    <meta name="email" content="webmaster@bibsonomy.org">
    <meta name="keywords" lang="en" content="ht09, hypertext, conference, proceedings, 2009, bibsonomy"> 
    <meta name="description" lang="en" content="Hypertext 2009 conference proceedings on the BibSonomy bookmark/BibTeX tagging and sharing system">
    <link rel="alternate" type="application/atom+xml" title="BibSonomy Blog - Atom" href="http://bibsonomy.blogspot.com/feeds/posts/default">
    <link rel="alternate" type="application/rss+xml"  title="BibSonomy Blog - RSS"  href="http://bibsonomy.blogspot.com/feeds/posts/default?alt=rss">
    <title><c:out value="${projectName}" /> :: Hypertext 2009 </title>
  </head>
  
  <body>
  
  <div id="topNavBar">
  <c:choose>
    <c:when test="${!empty user.name}">
      logged in as <a href="/user/<c:out value="${user.name}" />"><c:out value="${user.name}" /></a> &middot;
          <a href="/settings">settings</a> &middot;
          <a href="/logout">logout</a> &middot;
    </c:when>
    <c:otherwise>
      <a href="/login">login</a> &middot;
      <a href="/register">register</a>
    </c:otherwise>
  </c:choose>
  
  <a href="/help" rel="Help">help</a> &middot;
  <a href="http://bibsonomy.blogspot.com/">blog</a> &middot;
  <a href="/help/contact">about</a> &middot;
  <a href="?lang=en"><img alt="en" src="/resources/image/lang_en.png"/></a>
  </div>

  <div id="main">
  <div id="main_header">
  
    <!-- Navigation -->
    <div id="heading" style="float:left">
  