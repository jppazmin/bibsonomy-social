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

<%-- spammer tag suggestions --%>
<c:if test="${param.tag != null and param.type == 1}">
	<sql:query var="tag" dataSource="jdbc/bibsonomy">
		SELECT DISTINCT tag_name 
		FROM tas t
		LEFT JOIN spammer_tags s USING (tag_name)
		WHERE t.group = -2147483648 AND 
		ISNULL(s.tag_name) AND
		t.tag_name LIKE ? LIMIT 10
		<sql:param value="${param.tag}%"/>
	</sql:query>
	<ul>
	<c:forEach items="${tag.rows}" var="n">
		<li style="text-align:left;">${n.tag_name}</li>
	</c:forEach>
	</ul>
</c:if>

<%-- spammer-tag list entries --%>
<c:if test="${param.tag != null and param.type == 2}">
	<sql:query var="tag" dataSource="jdbc/bibsonomy">
		SELECT DISTINCT tag_name 
		FROM spammer_tags 
		WHERE spammer = 1 AND
		tag_name LIKE ? LIMIT 10
		<sql:param value="${param.tag}%"/>
	</sql:query>
	<ul>
	<c:forEach items="${tag.rows}" var="n">
		<li style="text-align:left;">${n.tag_name}</li>
	</c:forEach>
</ul>
</c:if>

<%-- related tags to chosen spammertag --%>
<c:if test="${param.tag != null and param.type == 3}">
	<sql:query var="pubreltags" dataSource="jdbc/bibsonomy">
		SELECT t2 AS tag, tt.ctr_public AS ctr
		FROM tagtag tt
		LEFT JOIN spammer_tags s ON (s.tag_name = tt.t2)
		WHERE t1 = ?
		AND ctr_public > 0
		AND ISNULL(s.tag_name)
		ORDER BY tt.ctr_public DESC LIMIT 25
		<sql:param value="${param.tag}"/>					
	</sql:query>	
	
	<sql:query var="privreltags" dataSource="jdbc/bibsonomy">
		SELECT t2 AS tag, tt.ctr AS ctr
		FROM tagtag tt
		LEFT JOIN spammer_tags s ON (s.tag_name = tt.t2)
		WHERE t1 = ?
		AND tt.ctr > 0
		AND ISNULL(s.tag_name)
		ORDER BY tt.ctr DESC LIMIT 25
		<sql:param value="${param.tag}"/>					
	</sql:query>	
	
	<div style="width:100%; font-size:0.7em; text-align:right;"><a href="javascript:ajax_hideTooltip()">close</a></div>
	
	<c:choose>	
		<c:when test="${pubreltags.rowCount eq 0 and privreltags.rowCount eq 0}">		
		- no related tags found for <c:out value="${param.tag}"/> - 		
		</c:when>
		
		<c:otherwise>	
		<table>
		<tr>
		<td style="border-right: 1px solid #CCC; padding-right:10px">
			<table class="taglist">
			<tr>
				<th colspan="3">public</th>							
			</tr>
			<tr>
				<th>tag</th><th>count</th><th>action</th>				
			</tr>
			<tbody id="pubrelatedlist">
			<c:forEach var="row" items="${pubreltags.rows}" varStatus="status">
				<tr id="rtpub<c:out value='${status.count}'/>">
					<td><a href="/tag/<mtl:encode value='${row.tag}'/>"><c:out value="${row.tag}"/></a></td>
					<td align="center"><c:out value="${row.ctr}"/></td>					
					<td>
						<a href="javascript:addSpammertag('<c:out value="${row.tag}"/>','pubrelatedlist','rtpub<c:out value='${status.count}'/>')" title="mark as spammertag"><img src="/resources/image/plus.png"/></a>
						<a href="javascript:cleanTag('<c:out value="${row.tag}"/>','pubrelatedlist','rtpub<c:out value='${status.count}'/>')" title="remove tag from suggestion list"><img src="/resources/image/minus.png"/></a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table> 	
		</td>
		<td style="padding-left:10px">	
			<table class="taglist">
			<tr>				
				<th colspan="3">private</th>				
			</tr>
			<tr>
				<th>tag</th><th>count</th><th>action</th>				
			</tr>
			<tbody id="privrelatedlist">
			<c:forEach var="row" items="${privreltags.rows}" varStatus="status">
				<tr id="rtpriv<c:out value='${status.count}'/>">
					<td><a href="/tag/<mtl:encode value='${row.tag}'/>"><c:out value="${row.tag}"/></a></td>
					<td align="center"><c:out value="${row.ctr}"/></td>					
					<td>
						<a href="javascript:addSpammertag('<c:out value="${row.tag}"/>','privrelatedlist','rtpriv<c:out value='${status.count}'/>')" title="mark as spammertag"><img src="/resources/image/plus.png"/></a>
						<a href="javascript:cleanTag('<c:out value="${row.tag}"/>','privrelatedlist','rtpriv<c:out value='${status.count}'/>')" title="remove tag from suggestion list"><img src="/resources/image/minus.png"/></a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table> 	
		</td>					
		</c:otherwise>		
	</c:choose>	
</c:if>

<!-- handle request on spammer and spammertag page -->
<c:if test="${param.type == 0}">
	<jsp:useBean id="adminBean" class="beans.AdminBean">
		<jsp:setProperty name="adminBean" property="*"/>
        <jsp:setProperty name="adminBean" property="currUser" value="${user.name}"/>
	</jsp:useBean>

	<% adminBean.queryDB(); %>
	<c:forEach var="info" items="${adminBean.infos}">
		<li><c:out value="${info}"/></li>
	</c:forEach>
	<c:forEach var="error" items="${adminBean.errors}">
		<li style="color: red;"><c:out value="${error}"/></li>
	</c:forEach>
</c:if>