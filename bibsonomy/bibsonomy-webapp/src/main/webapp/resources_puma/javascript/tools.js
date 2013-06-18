function replaceElementsTextnode(b,c,a){e=document.getElementById(b);
if(e){e.firstChild.nodeValue=c;
if(a&&(a.length==7||a.length==4)){e.setAttribute("style","color:"+a+";")
}else{e.removeAttribute("style")
}}}function pumainit(){if($("#sidebarroundcorner").attr("id")==null){$("#outer").after('<div id="sidebarroundcorner" style="display: none;"></div>');
$("#outer").attr("oldstyle",$("#outer").attr("style"))
}$("#sidebarroundcorner").prepend('<div class="sidebarBoxOuter" id="helpbox"><div class="sidebarBoxInner"><div id="helpboxcontent" class="boxcontent"><div id="togglehelp" class="togglehelp closeX">x</div><div class="bc-head">Hilfe</div>Weitere Informationen erhalten sie auf den <a href="http://www.ub.uni-kassel.de/puma.html">Projektseiten</a>, in der Hilfe und den beiden Blogs <a href="http://puma-projekt.blogspot.com/">PUMA-Projekt</a> und <a href="http://blog.bibsonomy.org/">BibSonomy</a>.</div></div></div>');
$("img#sidebarGrip").remove();
$("div#helpbox").before('<img id="sidebarGrip" border="0" src="/resources/image/grip.png" />');
$("#sidebarroundcorner").SideBarResizer("img#sidebarGrip");
$(".sidebarBoxInner").corner("round 8px").parent().css("padding","3px").corner("round 8px");
$("#helpbox").toggle();
$("#nice_tnav").prepend('<div id="navitogglehelp" class="togglehelp">?</div>');
$(".togglehelp").click(function(){if(($("#outer").attr("oldstyle")!=null)||($("#sidebarroundcorner").css("display")=="none")){if(($("#outer").attr("style")!=null)||($("#sidebarroundcorner").css("display")=="none")){$("#outer").removeAttr("style");
$("#sidebarroundcorner").css("display","block");
$("#helpbox").show()
}else{$("#helpbox").hide();
$("#sidebarroundcorner").css("display","none");
$("#outer").attr("style",$("#outer").attr("oldstyle"))
}}else{$("#helpbox").toggle()
}})
}$(document).ready(pumainit);