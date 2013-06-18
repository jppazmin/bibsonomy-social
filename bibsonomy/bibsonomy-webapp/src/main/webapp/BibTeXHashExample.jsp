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

<%-- include HTML header --%>
<jsp:include page="html_header.jsp">
  <jsp:param name="title" value="export" />
</jsp:include>

<%-------------------------- Heading -----------------------%>
<h1 id="path"><a href="/">${projectName}</a> :: <a href="/BibTeXHashExample.jsp">BibTeXHashExample.jsp</a></h1> 

<%-------------------------- Path Navigation -----------------------%>
<%@include file="/boxes/path_navi.jsp" %>

<%-------------------------- Navigation -----------------------%>
<%@include file="/boxes/navi.jsp" %> 

<div id="general"> 

<h2>Calculate ${projectName} BibTeX hashes</h2>

<p>Here you can enter some facts about a publication and ${projectName} 
calculates the resulting hashes for you.</p>


<jsp:useBean id="bibtex" class="org.bibsonomy.model.BibTex" scope="request">
  <jsp:setProperty name="bibtex" property="*"/>
</jsp:useBean>



<form>
<table>
  <tr><th colspan="2">used in interhash and intrahash</th></tr>
  <tr><td>title:  </td><td><input type="text" size="100" name="title" value="${f:escapeXml(param.title)}"/></td></tr>
  <tr><td>author: </td><td><input type="text" size="100" name="author" value="${f:escapeXml(param.author)}"/></td></tr>
  <tr><td>editor: </td><td><input type="text" size="100" name="editor" value="${f:escapeXml(param.editor)}"/></td></tr>
  <tr><td>year:   </td><td><input type="text" size="100" name="year" value="${f:escapeXml(param.year)}"/></td></tr>
  <tr><th colspan="2">used only in intrahash</th></tr>
  <tr><td>entrytype:  </td><td><input type="text" size="100" name="entrytype" value="${f:escapeXml(param.entrytype)}"/></td></tr>
  <tr><td>journal:    </td><td><input type="text" size="100" name="journal" value="${f:escapeXml(param.journal)}"/></td></tr>
  <tr><td>booktitle:  </td><td><input type="text" size="100" name="booktitle" value="${f:escapeXml(param.booktitle)}"/></td></tr>
  <tr><td>volume:     </td><td><input type="text" size="100" name="volume" value="${f:escapeXml(param.volume)}"/></td></tr>
  <tr><td>number:     </td><td><input type="text" size="100" name="number" value="${f:escapeXml(param.number)}"/></td></tr>
  
  <% bibtex.recalculateHashes(); %>
  
  <tr><th colspan="2">resulting hashes</th></tr>
  <tr><td>interhash:</td><td><tt>${bibtex.interHash}</tt> &rarr; <tt><a href="/bibtex/<%=org.bibsonomy.common.enums.HashID.INTER_HASH.getId()%>${bibtex.interHash}">/bibtex/<b><%=org.bibsonomy.common.enums.HashID.INTER_HASH.getId()%></b>${bibtex.interHash}</a></tt></td></tr>
  <tr><td>intrahash:</td><td><tt>${bibtex.intraHash}</tt> &rarr; <tt><a href="/bibtex/<%=org.bibsonomy.common.enums.HashID.INTRA_HASH.getId()%>${bibtex.intraHash}">/bibtex/<b><%=org.bibsonomy.common.enums.HashID.INTRA_HASH.getId()%></b>${bibtex.intraHash}</a></tt></td></tr>
</table>
<input type="submit"/>
</form>





</div>

<%@ include file="footer.jsp" %>