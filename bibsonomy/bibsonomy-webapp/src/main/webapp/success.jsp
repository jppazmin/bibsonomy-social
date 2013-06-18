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

<%@include file="include_jsp_head.jsp" %>

<jsp:include page="html_header.jsp">
  <jsp:param name="title" value="success" />
</jsp:include>

<%-------------------------- Heading -----------------------%>
<h1 id="path"><a href="/">${projectName}</a> :: <a href="#" rel="path_menu"><img src="/resources/image/box_arrow.png">&nbsp;success</a></h1> 

<%-------------------------- Path Navigation -----------------------%>
<%@include file="/boxes/path_navi.jsp" %>

<%-------------------------- Navigation -----------------------%>
<%@include file="/boxes/navi.jsp" %> 

<div id="general">

<h2>${success}</h2>

</div>
<%@ include file="footer.jsp" %>