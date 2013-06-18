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
  HTML starts here
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="content-Type" content="text/html; charset=UTF-8" >
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css" >
    <link rel="stylesheet" type="text/css" href="/resources/css/statphys23.css" >
    <!-- link rel="stylesheet" type="text/css" href="http://pil.phys.uniroma1.it/~ciro/bibsonomy_statphy23.css"-->
    <link rel="icon" href="/resources/image/favicon.png" type="image/png">
    <script type="text/javascript" src="/resources/javascript/functions.js"></script>
    <script type="text/javascript" src="/resources/javascript/tooltip.js"></script>
    <script type="text/javascript" src="/resources/javascript/style.js"></script>
    <meta name="author" content="Knowledge and Data Engineering Group, University of Kassel, Germany" />
    <meta name="copyright" content="Knowledge and Data Engineering Group, University of Kassel, Germany" />
    <meta name="email" content="webmaster@bibsonomy.org" />
    <meta name="keywords" lang="de" content="BibTeX, Bookmarks, Folksonomy, Tagging, Wissensverarbeitung, Wissensmanagement, Data Mining, Informatik, Universität Kassel" />
    <meta name="keywords" lang="en" content="BibTeX, bookmarks, knowledge discovery, folksonomy, tagging, knowledge management, data mining, computer science, University of Kassel" /> 
    <meta name="description" lang="de" content="Webapplikation des Fachgebiets Wissensverarbeitung, Universität Kassel" />
    <meta name="description" lang="en" content="Webapplication of the Knowledge and Data Engineering Group, University of Kassel, Germany" />
    <c:if test="${isResourceSite eq 'yes'}">
      <link rel="alternate" type="application/rss+xml" title="Bookmark RSS feed for <c:out value='/${requPath}' />" href="${projectHome}rss/<c:out value='${requPath}'/>" />
      <link rel="alternate" type="application/rss+xml" title="Publication RSS feed for <c:out value='/${requPath}' />" href="${projectHome}publrss/<c:out value='${requPath}'/>" />
      <link rel="alternate" type="application/rss+xml" title="BuRST RSS feed for <c:out value='/${requPath}' />" href="${projectHome}burst/<c:out value='${requPath}'/>" />
    </c:if>
    <link rel="alternate" type="application/atom+xml" title="BibSonomy Blog - Atom" href="http://bibsonomy.blogspot.com/feeds/posts/default" />
    <link rel="alternate" type="application/rss+xml"  title="BibSonomy Blog - RSS"  href="http://bibsonomy.blogspot.com/feeds/posts/default?alt=rss" />
    <title><c:out value="${projectName}" />::<c:out value="${param.title}" /></title>
  </head>
