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

<c:if test="${empty user.name || not(user.name == 'jaeschke' || user.name == 'hotho' || user.name == 'schmitz' || user.name == 'stumme' || user.name == 'grahl' || user.name == 'beate')}">
   <jsp:forward page="/login"/>
</c:if>

<%-- include HTML header --%>
<jsp:include page="html_header.jsp">
  <jsp:param name="title" value="statistics" />
</jsp:include>

<%-------------------------- Heading -----------------------%>
<h1 id="path"><a href="/">${projectName}</a> :: <a rel="path_menu" href="/statistics"><img src="/resources/image/box_arrow.png">&nbsp;statistics</a></h1> 

<%-------------------------- Path Navigation -----------------------%>
<%@include file="/boxes/path_navi.jsp" %>

<%-------------------------- Navigation -----------------------%>
<%@include file="/boxes/navi.jsp" %> 
<%-- count rows --%>
<%-- batch jobs  --%><sql:query var="tagtag_batch" dataSource="${dataSource}">SELECT count(*) AS ctr FROM tagtag_batch</sql:query>
<%-- users       --%><sql:query var="users"        dataSource="${dataSource}">SELECT count(*) AS ctr FROM user        </sql:query>
<%-- spammer       --%><sql:query var="spammer"    dataSource="${dataSource}">SELECT count(*) AS ctr FROM user where spammer=1   </sql:query>
<%-- tags        --%><sql:query var="tags"         dataSource="${dataSource}">SELECT count(*) AS ctr FROM tags        </sql:query>
<%-- ubookmarks --%><sql:query var="ubookmarks"    dataSource="${dataSource}">SELECT count(distinct book_url_hash) AS ctr FROM bookmark    </sql:query>
<%-- ibibtex    --%><sql:query var="ubibtex"       dataSource="${dataSource}">SELECT count(distinct simhash1) AS ctr FROM bibtex where user_name != 'dblp'   </sql:query>
<%-- log_bookmark--%><sql:query var="lbook"        dataSource="${dataSource}">SELECT count(*) AS ctr FROM log_bookmark</sql:query>
<%-- log_bibtex  --%><sql:query var="lbib"         dataSource="${dataSource}">SELECT count(*) AS ctr FROM log_bibtex</sql:query>
<%-- log_collector--%><sql:query var="lpick"       dataSource="${dataSource}">SELECT count(*) AS ctr FROM log_collector</sql:query>
<%-- collector   --%><sql:query var="pick"         dataSource="${dataSource}">SELECT count(*) AS ctr FROM collector</sql:query>

<%-- posts       --%><sql:query var="posts"        dataSource="${dataSource}">SELECT count(DISTINCT content_id) AS ctr FROM tas</sql:query>
<%-- posts-dblp  --%><sql:query var="postsd"       dataSource="${dataSource}">SELECT count(DISTINCT content_id) AS ctr FROM tas WHERE user_name != 'dblp'</sql:query>
<%-- bibtex      --%><sql:query var="bibtex"       dataSource="${dataSource}">SELECT count(*) AS ctr FROM bibtex      </sql:query>
<%-- bibtex-dblp --%><sql:query var="bibtexd"      dataSource="${dataSource}">SELECT count(*) AS ctr FROM bibtex WHERE user_name != 'dblp'</sql:query>
<%-- bookmarks   --%><sql:query var="bookmarks"    dataSource="${dataSource}">SELECT count(*) AS ctr FROM bookmark    </sql:query>

<%-- posts       --%><sql:query var="posts24"      dataSource="${dataSource}">SELECT count(DISTINCT content_id) AS ctr FROM tas WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date</sql:query>
<%-- posts-dblp  --%><sql:query var="postsd24"     dataSource="${dataSource}">SELECT count(DISTINCT content_id) AS ctr FROM tas WHERE user_name != 'dblp' AND DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date</sql:query>
<%-- bibtex      --%><sql:query var="bibtex24"     dataSource="${dataSource}">SELECT count(*) AS ctr FROM bibtex WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date</sql:query>
<%-- bibtex-dblp --%><sql:query var="bibtexd24"    dataSource="${dataSource}">SELECT count(*) AS ctr FROM bibtex WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date AND user_name != 'dblp'</sql:query>
<%-- bookmarks   --%><sql:query var="bookmarks24"  dataSource="${dataSource}">SELECT count(*) AS ctr FROM bookmark WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date</sql:query>

<%-- tas         --%><sql:query var="tas"          dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas         </sql:query>
<%-- tas-dblp    --%><sql:query var="tasd"         dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE user_name != 'dblp'</sql:query>
<%-- tasbib      --%><sql:query var="tasbib"       dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE content_type = 2  </sql:query>
<%-- tasbib-dblp --%><sql:query var="tasbibd"      dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE content_type = 2 AND user_name != 'dblp'</sql:query>
<%-- tasbook     --%><sql:query var="tasbook"      dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE content_type = 1  </sql:query>

<%-- tas         --%><sql:query var="tas24"        dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date</sql:query>
<%-- tas-dblp    --%><sql:query var="tasd24"       dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date AND user_name != 'dblp'</sql:query>
<%-- tasbib      --%><sql:query var="tasbib24"     dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date AND content_type = 2  </sql:query>
<%-- tasbib-dblp --%><sql:query var="tasbibd24"    dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date AND content_type = 2 AND user_name != 'dblp'</sql:query>
<%-- tasbook     --%><sql:query var="tasbook24"    dataSource="${dataSource}">SELECT count(*) AS ctr FROM tas WHERE DATE_SUB(CURDATE(), INTERVAL 24 HOUR) <= date AND content_type = 1  </sql:query>

<c:forEach var="row" items="${posts.rows}">      <c:set var="vposts" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${postsd.rows}">     <c:set var="vpostsd" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${bibtex.rows}">     <c:set var="vbibtex" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${bibtexd.rows}">    <c:set var="vbibtexd" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${bookmarks.rows}">  <c:set var="vbookmarks" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${posts24.rows}">    <c:set var="vposts24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${postsd24.rows}">   <c:set var="vpostsd24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${bibtex24.rows}">   <c:set var="vbibtex24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${bibtexd24.rows}">  <c:set var="vbibtexd24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${bookmarks24.rows}"><c:set var="vbookmarks24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tas.rows}">        <c:set var="vtas" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasd.rows}">       <c:set var="vtasd" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasbib.rows}">     <c:set var="vtasbib" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasbibd.rows}">    <c:set var="vtasbibd" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasbook.rows}">    <c:set var="vtasbook" value="${row.ctr}"/></c:forEach>    
<c:forEach var="row" items="${tas24.rows}">      <c:set var="vtas24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasd24.rows}">     <c:set var="vtasd24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasbib24.rows}">   <c:set var="vtasbib24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasbibd24.rows}">  <c:set var="vtasbibd24" value="${row.ctr}"/></c:forEach>
<c:forEach var="row" items="${tasbook24.rows}">  <c:set var="vtasbook24" value="${row.ctr}"/></c:forEach>


<div id="general">

<table id="table1" class="thetable">
  <tr>
    <th>tagtag batches</th>
    <td><c:forEach var="row" items="${tagtag_batch.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>users</th>
    <td><c:forEach var="row" items="${users.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>spammer</th>
    <td><c:forEach var="row" items="${spammer.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>tags</th>
    <td><c:forEach var="row" items="${tags.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>unique bookmarks</th>
    <td><c:forEach var="row" items="${ubookmarks.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>unique bibtex</th>
    <td><c:forEach var="row" items="${ubibtex.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>picked items</th>
    <td><c:forEach var="row" items="${pick.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>logged picked items</th>
    <td><c:forEach var="row" items="${lpick.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>logged bookmarks</th>
    <td><c:forEach var="row" items="${lbook.rows}">${row.ctr}</c:forEach></td>
  </tr>
  <tr>
    <th>logged bibtex</th>
    <td><c:forEach var="row" items="${lbib.rows}">${row.ctr}</c:forEach></td>
  </tr>
</table>

<table id="table2" class="thetable">
  <tr><th>           </th><th>sum        </th><th>sum - dblp  </th><th>bibtex      </th><th>bibtex - dblp</th><th>bookmark       </th></tr>
  <tr><th>posts      </th><td>${vposts}  </td><td>${vpostsd}  </td><td>${vbibtex}  </td><td>${vbibtexd}  </td><td>${vbookmarks}  </td></tr>
  <tr><th>posts (24h)</th><td>${vposts24}</td><td>${vpostsd24}</td><td>${vbibtex24}</td><td>${vbibtexd24}</td><td>${vbookmarks24}</td></tr>
  <tr><th>tas        </th><td>${vtas}    </td><td>${vtasd}    </td><td>${vtasbib}  </td><td>${vtasbibd}  </td><td>${vtasbook}    </td></tr>
  <tr><th>tas (24h)  </th><td>${vtas24}  </td><td>${vtasd24}  </td><td>${vtasbib24}</td><td>${vtasbibd24}</td><td>${vtasbook24}  </td></tr>
  <tr>
    <th>tas/posts     </th>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtas/vposts}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasd/vpostsd}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasbib/vbibtex}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasbibd/vbibtexd}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasbook/vbookmarks}"/></td>
  </tr>
  <tr>
    <th>tas/posts (24h)</th>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtas24/vposts24}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasd24/vpostsd24}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasbib24/vbibtex24}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasbibd24/vbibtexd24}"/></td>
    <td><fmt:formatNumber maxFractionDigits="3" value="${vtasbook24/vbookmarks24}"/></td>
  </tr>

</table>

</div>


<style type="text/css">
 .odd{background-color: white;}
 .even{background-color: #eeeeee;}
 .thetable {
   margin: 1em;
 }
 tr, td, th {
   padding: 4px;
 }
 td {
   text-align: right;
 }
 th {
   text-align: left;
 }
</style>


<script type="text/javascript">
function alternate(id){
 if(document.getElementsByTagName){  
   var table = document.getElementById(id);  
   var rows = table.getElementsByTagName("tr");  
   for(i = 0; i < rows.length; i++){          
 //manipulate rows
     if(i % 2 == 0){
       rows[i].className = "even";
     }else{
       rows[i].className = "odd";
     }      
   }
 }
}
alternate('table1');
alternate('table2');
</script>


<%-- ------------------------ right box -------------------------- --%>
<div id="rightbox">
</div>        

<%@ include file="footer.jsp" %>