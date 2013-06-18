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

<%@include file="/include_jsp_head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs/myajaxtaglib.tld" prefix="ajax" %>

<%@include file="/boxes/admin/login.jsp"%>

<script type="text/javascript" src="/ajax/scriptaculous/lib/prototype.js"></script>
<script type="text/javascript" src="/ajax/scriptaculous/src/scriptaculous.js"></script>    


<%-- include HTML header --%>
<jsp:include page="html_header.jsp">
  <jsp:param name="title" value="statistics" />
</jsp:include>

<%-------------------------- Heading -----------------------%>
<h1 id="path"><a href="/">${projectName}</a> :: <a rel="path_menu" href="/admin_statistics.jsp"><img src="/resources/image/box_arrow.png">&nbsp;statistics</a></h1>

<%-------------------------- Path Navigation -----------------------%>
<%@include file="/boxes/path_navi.jsp" %>

<%-------------------------- Navigation -----------------------%>
<%@include file="/boxes/navi.jsp"%>

<jsp:useBean id="stats" class="beans.StatisticsBean" scope="request">
  <jsp:setProperty name="stats" property="*"/>
</jsp:useBean>

<div style="margin: 5px 10px 2% 10px;">
<%@include file="/boxes/admin/navi.jsp"%>

<h2>Statistics for ${stats.today} (showSpammer:<c:out value='${param.showSpammer}'/>)</h2>

<p style="font-weight: bold; 
          background: #eee; 
          border-bottom: 1px solid #ccc;
          border-top: 1px solid #ccc;
          margin: 0px;
          padding: 0px 10px 0px 10px;">
show statistics: <a href="?showSpammer=no">without spammers</a>, <a href="?showSpammer=yes">with spammers</a>, <a href="?showSpammer=only">only for spammers</a></p>

<%-- ################################################### resources, posts, tas ########################################## --%>

<h3>resource, post and TAS counts</h3>

<table id="table1" class="thetable">
  <tr><th>         </th><th colspan="3">overall counts</th>
                        <th colspan="3">overall counts (without DBLP)</th>
                        <th colspan="3">last 24 hours (without DBLP)</th></tr>
  <tr><th>         </th><th>sum                                  </th><th>bookmarks                                 </th><th>publications                                 </th>
                        <th>sum                                  </th><th>bookmarks                                 </th><th>publications                                 </th>
                        <th>sum                                  </th><th>bookmarks                                 </th><th>publications                                 </th></tr>
  <tr><th>resources</th><td><ajax:get var="stats.resources"/>    </td><td><ajax:get var="stats.bookmarks"/>         </td><td><ajax:get var="stats.publications"/>         </td>
                        <td><ajax:get var="stats.resourcesDBLP"/></td><td><ajax:get var="stats.bookmarksDBLP"/>     </td><td><ajax:get var="stats.publicationsDBLP"/>     </td>
                        <td><ajax:get var="stats.resources24"/>  </td><td><ajax:get var="stats.bookmarks24"/>       </td><td><ajax:get var="stats.publications24"/>       </td></tr>
  <tr><th>posts    </th><td><ajax:get var="stats.posts"/>        </td><td><ajax:get var="stats.postsBookmarks"/>    </td><td><ajax:get var="stats.postsPublications"/>    </td>
                        <td><ajax:get var="stats.postsDBLP"/>    </td><td><ajax:get var="stats.postsBookmarksDBLP"/></td><td><ajax:get var="stats.postsPublicationsDBLP"/></td>
                        <td><ajax:get var="stats.posts24"/>      </td><td><ajax:get var="stats.postsBookmarks24"/>  </td><td><ajax:get var="stats.postsPublications24"/>  </td></tr>
  <tr><th>TAS      </th><td><ajax:get var="stats.tas"/>          </td><td><ajax:get var="stats.tasBookmarks"/>      </td><td><ajax:get var="stats.tasPublications"/>      </td>
                        <td><ajax:get var="stats.tasDBLP"/>      </td><td><ajax:get var="stats.tasBookmarksDBLP"/>  </td><td><ajax:get var="stats.tasPublicationsDBLP"/>  </td>
                        <td><ajax:get var="stats.tas24"/>        </td><td><ajax:get var="stats.tasBookmarks24"/>    </td><td><ajax:get var="stats.tasPublications24"/>    </td></tr>

<%--
  <tr><th>TAS/posts</th><td><ajax:get var="stats.tas                                           / stats.posts"/>      </th>
                        <td><ajax:get var="stats.tasBookmarks                                  / stats.postsBookmarks"/>  </td>
                        <td><ajax:get var="stats.tasPublications                               / stats.postsPublications"/>  </td>
                        <td><ajax:get var="(stats.tas - stats.tasDBLP)                         / (stats.posts - stats.postsDBLP)"/>      </td>
                        <td><ajax:get var="(stats.tasBookmarks - stats.tasBookmarksDBLP)       / (stats.postsBookmarks - stats.postsBookmarksDBLP)"/>  </td>
                        <td><ajax:get var="(stats.tasPublications - stats.tasPublicationsDBLP) / (stats.postsPublications - stats.postsPublicationsDBLP)"/>  </td>
                        <td><ajax:get var="stats.tas24                                         / stats.posts24"/>      </td>
                        <td><ajax:get var="stats.tasBookmarks24                                / stats.postsBookmarks24"/>  </td>
                        <td><ajax:get var="stats.tasPublications24                             / stats.postsPublications24"/>  </td></tr>
    --%>                    
</table>

<%-- ################################################### logging tables ########################################## --%>

<h3>logging tables</h3>
<table id="table4" class="thetable">
  <tr><th>                  </th><th>overal counts                                 </th><th>overall counts (without DBLP)                     </th></tr>
  <tr><th>bookmark posts    </th><td><ajax:get var="stats.loggedBookmarkPosts"/>   </td><td><ajax:get var="stats.loggedBookmarkPostsDBLP"/>   </td></tr>
  <tr><th>publication posts </th><td><ajax:get var="stats.loggedPublicationPosts"/></td><td><ajax:get var="stats.loggedPublicationPostsDBLP"/></td></tr>
  <tr><th>posts from baskets</th><td><ajax:get var="stats.loggedPostsFromBaskets"/></td></tr>
  <tr><th>friendships       </th><td><ajax:get var="stats.loggedFriends"/>         </td></tr>
  <tr><th>group memberships </th><td><ajax:get var="stats.loggedGroups"/>          </td></tr>
  <tr><th>tag-tag relations </th><td><ajax:get var="stats.loggedTagTagRelations"/> </td></tr>
</table>


<%-- ################################################### users ########################################## --%>

<h3>users</h3>
<table id="table5" class="thetable">
  <tr><th>user                      </th><td><ajax:get var="stats.users"/>               </td></tr>
  <tr><th>spammer                   </th><td><ajax:get var="stats.spammers"/>            </td></tr>
  <tr><th>active users              </th><td><ajax:get var="stats.activeUsers"/>         </td></tr>
  <tr><th>active users last month   </th><td><ajax:get var="stats.activeUsersLastMonth"/></td></tr>
  <tr><th>active users last 24 hours</th><td><ajax:get var="stats.activeUsersLast24"/>   </td></tr>
</table>
  

<%-- ################################################### misc ########################################## --%>

<h3>misc</h3>
<table id="table6" class="thetable">
  <tr><th>tags              </th><td><ajax:get var="stats.tags"/>              </td></tr>
  <tr><th>tag-tag relations </th><td><ajax:get var="stats.tagTagRelations"/>   </td></tr>
  <tr><th>tagtag batches    </th><td><ajax:get var="stats.tagTagBatches"/>     </td></tr>
  <tr><th>posts in baskets  </th><td><ajax:get var="stats.postsInBaskets"/>    </td></tr>
  <tr><th>user layouts      </th><td><ajax:get var="stats.usersWithOwnLayout"/></td></tr>
  <tr><th>uploaded documents</th><td><ajax:get var="stats.uploadedDocuments"/> </td></tr>
</table>

</div>




<%-- ################################################### table coloring ########################################## --%>



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
alternate('table4');
alternate('table5');
alternate('table6');

var ajaxServlet = "/AdminHandler";

var autofill = document.getElementsByTagName("span");

for (var i=0; i < autofill.length; i++) {
  if (autofill[i].className == "autofill") {
    fillSpan(autofill[i].id);
  }
}

function getRequest(){
  var req;
  try{
    if(window.XMLHttpRequest){
      req = new XMLHttpRequest();
    }else if(window.ActiveXObject){
      req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    if( req.overrideMimeType ) {
      req.overrideMimeType("text/xml");
    }         
  } catch(e){
    return false;
  }
  return req;
}

function fillSpan(id) {
  var request = getRequest();
  if(request) {
    var url = ajaxServlet + "?action=ajax&var=" + id + "&spammer=${param.showSpammer}";
    request.open('GET', url, true);
    request.setRequestHeader("Content-Type", "text/xml");
    request.setRequestHeader('If-Modified-Since', 'Sat, 1 Jan 2000 00:00:00 GMT');
    // attach function which handles the request
    request.onreadystatechange = fillSpanHandler(request,id);
    request.send(null);
  } 
}

function fillSpanHandler(request,id) {
  return function() {
    if (4 == request.readyState) {
      if (200 == request.status) {
        // put result into element
        document.getElementById(id).appendChild(document.createTextNode(request.responseText));
      }
    }
  }
}    


</script>



<%@ include file="footer.jsp"%>