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


<%-- ######### myBibSonomy * post bookmark * post bibtex ######### --%>
<div id="nice_tnav">
<ul>
	<li>
		<a id="nav_home" href="/">Home</a>
	</li>
	<c:choose>
		<c:when test="${not empty user.name}">
			<li>
		        <a id="nav_my" href="/user/<mtl:encode value='${user.name}'/>" rel="my_menu"><img src="/resources/image/nice_box_arrow.png" alt="" />my${projectName}</a>
 
			      <!-- dropdown menu for myBibSonomy -->  
				  <div id="my_menu" class="nicedropmenudiv">
				    <a href="/friends">myFriends</a>
					<a href="/relations/<mtl:encode value='${user.name}'/>">myRelations</a>
					<a href="/mySearch">mySearch</a>
			        <a href="/user/<mtl:encode value='${user.name}'/>?filter=myPDF">myPDF</a>
			        <a href="/user/<mtl:encode value='${user.name}'/>?filter=myDuplicates">myDuplicates</a>
			        <a href="/bib/user/<mtl:encode value='${user.name}'/>?items=1000">myBibTeX</a>
				  </div>
			</li>
			<li>
				<a id="nav_postBook" href="/postBookmark">post bookmark</a>
			</li>
			<li>
				<a id="nav_postPub" href="/postPublication">post publication</a>
			</li>
		</c:when>
	</c:choose>
     <%-- ######### tags * groups * relations * popular ######### --%>
	<li>
	  <a id="nav_tags" href="/tags">tags</a>
	</li>		
	<li>
		<a id="nav_authors" href="/authors">authors</a>
	</li>
	<li>
	  <a id="nav_relations" href="/relations">relations</a>
	</li>
	<li>
     <a id="nav_urls" href="/groups" rel="grp_menu"><c:if test="${not empty user.groups}"><img src="/resources/image/nice_box_arrow.png" alt="" /> </c:if>groups</a>
	</li>
	<li>
	  <%-- dropdown menu for groups --%>		
  	  <div id="grp_menu" class="nicedropmenudiv">
  	  	<c:if test="${not empty user.groups}">
	    	<c:forEach var="grp" items="${user.groups}">
		  	<a href="/group/<mtl:encode value='${grp.name}'/>"><c:out value='${grp.name}'/></a>
			</c:forEach>
		</c:if>  	
	  </div>										
	</li>
	<li>
 	  <a id="nav_popular" href="/popular">popular</a>
	</li>
</ul>
</div>

<%-- 2008/12/18, fei: commented out conditional as otherwise no tab is brought
                      to front when user isn't logged in 
--%>
<%-- <c:if test="${not empty user.name}">  --%>
	<script type="text/javascript">
  		cssdropdown.startchrome("nice_tnav");
		if ("${selectedNaviTab}" != "" ) {
			document.getElementById("${selectedNaviTab}").className = "checked";
		} else {
			document.getElementById("nav_my").className = "checked";
		}
	</script>  
<%-- </c:if>  --%> 
	