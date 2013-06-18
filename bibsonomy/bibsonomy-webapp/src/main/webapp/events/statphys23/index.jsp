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
  
<body>


<center>
  <a href="http://www.statphys23.org/"><img border="0" src="http://pil.phys.uniroma1.it/~ciro/logo_statphys23.png" alt="Statphys23 logo"></a>
  <div class="topic">Statphys23. In Keywords.</div>
</center>

<div class="explanation">
  This page is a front-end to <a href="http://www.bibsonomy.org/">BibSonomy</a>, a social bookmark and publication sharing system. All contributions submitted to the <a href="http://www.statphys23.org/">Statphys23</a> conference have been loaded into the system, together with the keywords (tags) that authors have associated with their abstracts. Tags can be used to navigate through the conference abstracts.
</div>

<div class="linkbox">
    <a href="/help/basic">What is BibSonomy?</a>
    <a href="/help/tutorial">Tutorials</a>
    <a href="/register">Get a BibSonomy account!</a>
</div>


<div class="explanation">
  Below, the tags (keywords) most frequently used by Statphys23 authors are shown. The size of each tag (keyword) is proportional to the logarithm of the number of abstracts that have been tagged with it. The number of abstracts associated with a tag (keyword) is displayed when hovering over it. Colors encode topics (tracks) of the Statphys23 conference, as shown at the bottom of this page. The color of each tag indicates the topic to which most abstracts annotated with that tag belong to. Clicking on a tag (keyword) will retrieve from BibSonomy a list of the abstracts that have been tagged with it. The full abstract of each contribution is available in BibTeX format.
</div>


<%@include file="tagcloud.jsp" %>


<div class="linkbox linkbox2">

  <span id="cool">
    <a href="/events/statphys23/cool.jsp">See what your collegues find cool</a>

    or

  </span>
    
    <span style="font-weight: bold; font-size: 120%;padding-left:15px;display:inline;">Search for an author:</span>
      <form id="specialsearch" method="get" action="/redirect" style="display:inline;">
        <input type="hidden" name="scope" value="author">
        <input type="hidden" name="requUser" value="statphys23">
        <input type="text" id="inpf" name="search" size="25"/>
      </form>
</div>

<%@include file="footer.jsp" %>