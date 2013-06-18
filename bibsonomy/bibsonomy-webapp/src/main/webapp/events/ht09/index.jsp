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

<%@include file="header.jsp" %>


<%-------------------------- Heading -----------------------%>
<h1><a href="/" rel="Start">${projectName}</a> :: Hypertext 2009
<form id="specialsearch" method="get" action="/redirect">
  <select name="scope" size="1" id="scope">
    <option value="tag">tag</option>
    <option value="user">user</option>
    <option value="group">group</option>
    <option value="author">author</option> 
    <option value="concept/tag">concept</option> 
    <option value="search" selected="selected">search:all</option> 
    <c:if test="${not empty user.name}">
      <option value="user:<c:out value='${user.name}'/>">search:<c:out value="${user.name}"/></option> 
    </c:if>       
  </select>  ::
  <input type="text" id="inpf" name="search" size="30"/>  
</form>
</h1>
<%-------------------------- Path Navigation -----------------------%>
<%@include file="/boxes/path_navi.jsp" %>
<%-------------------------- Navigatopm -----------------------%>
<%@include file="/boxes/navi.jsp" %>    

<style type="text/css">
#full {
 font-size: 80%;
}

</style>


  <div id="full">

    <div class="topic">Hypertext 2009. As Tagcloud.</div>
    
    <div id="logos">
      <a href="http://www.ht2009.org/"><img src="/resources/image/ht09_logo.jpg"></a>
    </div>
  
    

    <div class="explanation">
    
      This service is provided by <a href="${projectHome}">${projectName}</a>, a social bookmark and
      publication sharing system. It contains all accepted papers of the <a href="http://www.ht2009.org/">Hypertext 2009 conference</a> and its
      workshops, together with the keywords (tags) that authors have
      associated with their papers or that show up in the paper titles.

      <b>Want to organize your conference visit? Just <a href="/register">get a ${projectName} account</a> and
      start copying and annotating the papers you are interested in!</b>

    </div>

    <div class="linkbox">
        <a href="/help/basic">What is ${projectName}?</a>
        <a href="/help/tutorials">Tutorial</a>
        <a href="/register">Get a ${projectName} account!</a>
    </div>


    <div class="explanation">
      The color of each tag indicates the session (as shown at the bottom of
      this page) to which most abstracts annotated with that tag belong to.
      Clicking on a tag (keyword) will retrieve from ${projectName} the abstracts
      that have been tagged with it. 

      The publication metadata are <a href="/export/user/ht09">available in many formats</a>, including  <a href="/bib/user/ht09">BibTeX</a> and  <a href="/burst/user/ht09">RDF</a>.
    </div>


    <div id="tagcloudy">
      <%@include file="tagcloud.jsp" %>
    </div>

    <div id="topics">
      <%@include file="topics.jsp" %>
    </div>



    <div class="linkbox linkbox2">
<!--      <span id="cool">-->
<!--        <a href="/events/eswc2008/cool.jsp">See what your collegues find cool</a> or-->
<!--      </span>-->
    
      <span style="font-weight: bold; font-size: 120%;padding-left:15px;display:inline;">Search for an author:</span>
      <form id="specialsearch" method="get" action="/author" style="display:inline;">
        <input type="text" id="inpf" name="requestedAuthor" size="25"/>
        <input type="hidden" name="requestedTags" value="sys:user:ht09">
      </form>
    </div>

  </div>

<%@ include file="footer.jsp" %>
<%@ include file="/footer.jsp" %>