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
<h1><a href="/" rel="Start">${projectName}</a> :: ISWC+ASWC 2007
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

<%@include file="/boxes/navi.jsp" %>    <%-------------------------- Navigatopm -----------------------%>



  <div id="full">

    <div class="topic">ISWC+ASWC 2007. As Tagcloud.</div>
    
    <div id="logos">
      <a href="http://iswc2007.semanticweb.org/"><img src="/resources/image/iswc_flash.jpg"></a>
    </div>
  
    

    <div class="explanation">
    
      This service is provided by <a href="http://www.bibsonomy.org/">BibSonomy</a>, a social bookmark and
      publication sharing system. It contains all accepted papers of the <a href="http://iswc2007.semanticweb.org/">ISWC+ASWC 2007 conference</a> and its
      workshops, together with the keywords (tags) that authors have
      associated with their papers or that show up in the paper titles.

      <b>Want to organize your conference visit? Just <a href="/register">get a BibSonomy account</a> and
      start copying and annoting the papers you are interested in!</b>

    </div>

    <div class="linkbox">
        <a href="/help/basic">What is ${projectName}?</a>
        <a href="/help/tutorials">Tutorial</a>
        <a href="/register">Get a ${projectName} account!</a>
    </div>


    <div class="explanation">
      The color of each tag indicates the track (as shown at the bottom of
      this page) to which most abstracts annotated with that tag belong to.
      Clicking on a tag (keyword) will retrieve from BibSonomy the abstracts
      that have been tagged with it. 

      The publication metadata are <a href="http://www.bibsonomy.org/export/user/iswc2007">available in many formats</a>, including BibTeX and RDF.
    </div>


    <div id="tagcloudy">
      <%@include file="tagcloud.jsp" %>
    </div>

    <div id="topics">
      <%@include file="topics.jsp" %>
    </div>



    <div class="linkbox linkbox2">
      <span id="cool">
        <a href="/events/iswc2007/cool.jsp">See what your collegues find cool</a> or
      </span>
    
      <span style="font-weight: bold; font-size: 120%;padding-left:15px;display:inline;">Search for an author:</span>
      <form id="specialsearch" method="get" action="/redirect" style="display:inline;">
        <input type="hidden" name="scope" value="author">
        <input type="hidden" name="requUser" value="iswc2007">
        <input type="text" id="inpf" name="search" size="25"/>
      </form>
    </div>

  </div>

<%@ include file="footer.jsp" %>
<%@ include file="/footer.jsp" %>