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

<%@include file="/include_jsp_head.jsp" %>

<%-- include HTML header --%>
<jsp:include page="/html_header.jsp">
  <jsp:param name="title" value="cool" />
</jsp:include>

<%-------------------------- Heading -----------------------%>
<h1 id="path"><a href="/">${projectName}</a> :: <a rel="path_menu" href="/register"><img src="/resources/image/box_arrow.png">&nbsp;cool</a></h1> 

<%-------------------------- Path Navigation -----------------------%>
<%@include file="/boxes/path_navi.jsp" %>

<%-------------------------- Navigation -----------------------%>
<%@include file="/boxes/navi.jsp" %> 
<div id="general">


<c:set var="requGroup" value="ht08"/>


  <center>
    <h2>all tags of all users in the group ${requGroup}</h2>
    Here you can see a live view of all tags used by the users of the 
    <a href="/group/<c:out value='${requGroup}'/>">Hypertext 2008 group</a>. 
    Feel free to <a href="/join_group?group=ht08">join</a> the group, 
    such that your tags become a part of it. 
  </center>




    <sql:query var="rst" dataSource="${dataSource}">
    SELECT tag_name, tag_anzahl, round(log(if(tag_anzahl>100, 100, tag_anzahl)))*30+100 AS tag_size FROM (
      SELECT tag_name, count(tag_name) AS tag_anzahl
        FROM tas t, groups g, groupids gi
        WHERE g.group = gi.group
          AND gi.group_name = ?
          AND g.user_name = t.user_name
          AND t.user_name != ?
          AND DATE(t.date) > "2008-06-16"
        GROUP BY t.tag_name 
        ORDER BY tag_name COLLATE utf8_unicode_ci) AS innen
      <sql:param value="${requGroup}"/>
      <sql:param value="${requGroup}"/>
    </sql:query>
    
    
    <ul class="tagcloud">
    <c:forEach var="row" items="${rst.rows}">
      <li>
        <a style="font-size: ${row.tag_size}%;" title="${row.tag_anzahl} posts" href="/group/<mtl:encode value='${requGroup}' />/<mtl:encode value='${row.tag_name}' />"><c:out value="${row.tag_name}" /></a>
      </li>
    </c:forEach>
    </ul>



</div>

<%@include file="/footer.jsp" %>
