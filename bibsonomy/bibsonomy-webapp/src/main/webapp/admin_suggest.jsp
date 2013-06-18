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

<%@ include file="include_jsp_head.jsp"%>

<%@include file="/boxes/admin/login.jsp"%>

<c:if test="${not empty param.q and param.type eq 0}">
	<sql:query var="name" dataSource="jdbc/bibsonomy">
		SELECT user_name FROM user
		WHERE user_name LIKE ?
		AND spammer = 0
		LIMIT 10
		<sql:param value="${param.q}%"/>
	</sql:query>
</c:if>

<c:if test="${not empty param.q and param.type eq 1}">
	<sql:query var="name" dataSource="jdbc/bibsonomy">
		SELECT user_name FROM user
		WHERE user_name LIKE ?
		AND spammer = 1
		LIMIT 10
		<sql:param value="${param.q}%"/>
	</sql:query>
</c:if>

<c:if test="${not empty param.q and param.type eq 2}">
	<sql:query var="name" dataSource="jdbc/bibsonomy">
		SELECT user_name FROM user
		WHERE user_name LIKE ?
		LIMIT 10
		<sql:param value="${param.q}%"/>
	</sql:query>
</c:if>

<c:forEach items="${name.rows}" var="n">
	${n.user_name}<c:out value="
"/>
</c:forEach>

