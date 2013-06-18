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

		<%--
			layer for subnavigation inside the path elements (headline)
			@author: ccl
		 --%>
		
		<%-- dropdown menu for BibSonomy Path --%>  
		<div id="path_menu" class="dropmenudiv">
			<a onclick="naviSwitchSpecial('tag')" style="cursor:pointer">tag</a>
			<a onclick="naviSwitchSpecial('user')" style="cursor:pointer">user</a>
			<a onclick="naviSwitchSpecial('group')" style="cursor:pointer">group</a>
			<a onclick="naviSwitchSpecial('author')" style="cursor:pointer">author</a>
			<a onclick="naviSwitchSpecial('concept/tag')" style="cursor:pointer">concept</a>
			<a onclick="naviSwitchSpecial('bibtexkey')" style="cursor:pointer">BibTeX key</a>
			<a onclick="naviSwitchSpecial('search')" style="cursor:pointer">search:all</a>
			<c:if test="${not empty param.requUser}">
				<a onclick="naviSwitchSpecial('explicit_user')" style="cursor:pointer">search:<c:out value='${param.requUser}'/></a>
				<c:set var="mode" value="requUser" />
			</c:if>
			<c:if test="${not empty user.name and empty param.requUser}">
				<a onclick="naviSwitchSpecial('explicit_user')" style="cursor:pointer">search:<c:out value='${user.name}'/></a>
				<c:set var="mode" value="requUser" />
			</c:if>
		</div>
		<script type="text/javascript">
		  cssdropdown.startchrome("path");
		</script>
	</div>	
	
	<div id="mainHeaderRightBox">
 		<c:choose>
			<c:when test="${!empty user.name}">
				<span id="pickctr">${user.basket.numPosts}</span> picked in <a href="/basket">basket</a> &middot;
				<a href="/edit_tags">edit tags</a> 
			</c:when>
			<c:otherwise>
				<span style="display: block; margin-top: 7px;">
					<div id="userAndPw">
						<c:set var="login">login </c:set>
						<c:url var="loginUrl" value="/login" context="${projectContext}" />
						<form method="POST" action="${loginUrl}" style="display: inline">
							<label for="un">username:&nbsp;</label>
							<input type="text" size="10" name="username" id="un" />&nbsp; 
							<label for="pw">password:&nbsp;</label> 
							<input type="password" size="10" name="password" id="pw" /> 
							<input type="image" src="${projectContext}resources/image/grey.png" alt="${login}" /> 
							<a href="javascript:switchLogin();">
								<img style="width: 16px; height: 16px;" src="${resdir}/image/login_logo_OPENID.png" />
							</a>
						</form>
					</div>

					<div id="openID" style="display: none">
						<form action="/login" method="POST">
							<label path="openID">OpenID:</label>
							<input type="text" id="openID" class="openid" size="30" value="" name="openID" /> <a href="javascript:switchLogin();">
							<img style="width: 16px; height: 16px;" src="${resdir}/image/login_logo_INTERNAL.png" /></a>
						</form>
					</div>
				</span>

				<%-- little script to switch between default login and OpenID login forms --%>
				<script type="text/javascript">
							function switchLogin() {
								var defaultLoginDiv = document.getElementById('userAndPw');
								var openIDLoginDiv = document.getElementById('openID');
							
								if (defaultLoginDiv.style.display == 'none') {
									defaultLoginDiv.style.display = 'block';
									openIDLoginDiv.style.display = 'none';
								} else {
									defaultLoginDiv.style.display = 'none';
									openIDLoginDiv.style.display = 'block';
								}
							}
				</script>
			</c:otherwise>
		</c:choose>
		
	</div>

	<div id="welcomeTop">A blue social bookmark and publication sharing system.</div>
</div>