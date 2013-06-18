var activeField=null;
var sidebar=null;
var tagbox=null;
var tags_toggle=0;
var tags_filter=null;
var ckey=null;
var currUser=null;
var requUser=null;
var projectName=null;
var pwd_id_postfix="_form_copy";
function init(b,d,c,g,f,a,e){add_hints();
$(".descriptiveLabel").each(function(){$(this).descrInputLabel({})
});
sidebar=document.getElementById("sidebar");
tagbox=document.getElementById("tagbox");
ckey=a;
currUser=f;
if(g!=""){requUser=g
}projectName=e;
if(tagbox){init_tagbox(b,d,c,g)
}if(!location.pathname.startsWith("/postPublication")&&!location.pathname.startsWith("/postBookmark")){if(sidebar){add_filter();
init_sidebar()
}}add_tags_toggle()
}function stopEvt(){return false
}function add_filter(){var b=document.createElement("form");
b.onsubmit=stopEvt;
b.title="filter sidebar";
b.style.padding="5px";
b.style.display="inline";
b.appendChild(document.createTextNode(getString("filter.label")+"\u00A0"));
tags_filter=document.createElement("input");
tags_filter.autocomplete="off";
tags_filter.type="text";
tags_filter.style.size="10ex";
tags_filter.onkeyup=filter_tags;
b.appendChild(tags_filter);
var a=document.createElement("li");
a.style.paddingBottom=".5em";
a.style.fontSize="80%";
a.appendChild(b);
if(document.getElementById("sidebarfilter")){sidebar.replaceChild(a,document.getElementById("sidebarfilter"))
}else{sidebar.insertBefore(a,sidebar.childNodes[0])
}b.parentNode.style.marginBottom="5px"
}function init_sidebar(){var e=sidebar.childNodes;
for(var c=0;
c<e.length;
c++){var d=e[c];
if(d.nodeName.toUpperCase()=="LI"){for(var b=0;
b<d.childNodes.length;
b++){var a=d.childNodes[b];
if(a.className=="sidebar_h"){a.insertBefore(getToggler(),a.firstChild)
}}}}}function getToggler(){var a=document.createElement("span");
var b=document.createElement("img");
b.src="/resources/image/icon_collapse.png";
b.border=0;
a.appendChild(b);
a.className="toggler";
a.onclick=hideNextList;
return a
}function hideNextList(){var b=this.parentNode;
var a=this.firstChild;
while(b&&b.nodeName.toUpperCase()!="UL"){b=b.nextSibling
}if(b){if(b.style.display!="none"){b.style.display="none";
a.src="/resources/image/icon_expand.png"
}else{b.style.display="block";
a.src="/resources/image/icon_collapse.png"
}}}function confirmDelete(d){var c=xget_event(d);
var a=getParent(c,"bm");
a.style.background="#fdd";
var b=confirm(getString("post.meta.delete.confirm"));
a.style.background="transparent";
return b
}function getParent(b,a){if(b.className==a){return b
}return getParent(b.parentNode,a)
}function maximizeById(a){if(window.innerWidth<1200){document.getElementById(a).style.width="95%"
}}function getFormTextCopy(a){return $("#"+a.id+pwd_id_postfix).css("color","#aaa").width($(a).width()).click(function(){hideFormTextCopy({elementCopy:"#"+a.id+pwd_id_postfix,element:a})
})[0]
}function hideFormTextCopy(a){$(a.elementCopy).hide();
$(a.element).removeClass("hiddenElement").focus()
}function add_hints(){var b=document.getElementById("se");
if(validElement(b)&&(b.value==""||b.value==getString("navi.search.hint"))){b.value=getString("navi.search.hint");
b.className="descriptiveLabel "+b.className
}b=document.getElementById("un");
if(validElement(b,"input")&&b.name=="username"){if(b.value==""||b.value==getString("navi.username")){b.value=getString("navi.username");
b.className="descriptiveLabel "+b.className
}b.onblur=function(){hideFormTextCopy({elementCopy:"#pw"+pwd_id_postfix,element:"#pw"})
}
}b=document.getElementById("pw");
if(validElement(b,"input")&&b.name=="password"){b=getFormTextCopy(b);
b.value=getString("navi.password")
}b=document.getElementById("unldap");
if(validElement(b,"input")&&b.name=="username"){if(b.value==""||b.value==getString("navi.username.ldap")){b.value=getString("navi.username.ldap");
b.className="descriptiveLabel "+b.className
}b.onblur=function(){hideFormTextCopy({elementCopy:"#pwldap"+pwd_id_postfix,element:"#pwldap"})
}
}b=document.getElementById("pwldap");
if(validElement(b,"input")&&b.name=="password"&&(b.value==""||b.value==getString("navi.password.ldap"))){b=getFormTextCopy(b);
b.value=getString("navi.password.ldap")
}b=document.getElementById("openID");
if(validElement(b,"input")&&b.name=="openID"&&(b.value==""||b.value==getString("navi.openid"))){b.value=getString("navi.openid");
b.className="descriptiveLabel "+b.className
}b=document.getElementById("inpf");
if(validElement(b,"input")&&(b.name=="tag"||b.name=="tags")&&(b.value==""||b.value==getString("navi.tag.hint"))){b.value=getString("navi.tag.hint");
b.className="descriptiveLabel "+b.className
}var a=null;
if(validElement(b,"input")&&b.name=="search"&&validElement((a=document.getElementById("scope")),"select")){$(a).bind("change",function(){setSearchInputLabel(this)
}).trigger("change")
}}function validElement(b,a){return(b!=null&&(!a||b.tagName.toUpperCase()==a.toUpperCase()))
}function clear_tags(){var a=document.getElementById("inpf");
if(a.value==getString("navi.tag.hint")){a.value=""
}}function focus(b){var a=document.getElementById(b);
if(a){a.focus()
}}function toggle_required_author_editor(){if(document.post_bibtex.author.value.search(/^\s*$/)==-1){document.post_bibtex.editor.style.backgroundColor="#ffffff";
document.post_bibtex.author.style.backgroundColor=document.post_bibtex.title.style.backgroundColor
}else{if(document.post_bibtex.editor.value.search(/^\s*$/)==-1){document.post_bibtex.author.style.backgroundColor="#ffffff";
document.post_bibtex.editor.style.backgroundColor=document.post_bibtex.title.style.backgroundColor
}else{document.post_bibtex.author.style.backgroundColor=document.post_bibtex.title.style.backgroundColor;
document.post_bibtex.editor.style.backgroundColor=document.post_bibtex.title.style.backgroundColor
}}}function filter_tags(h){var d;
if(!h){h=window.event
}if(h.which){d=h.which
}else{d=h.keyCode
}var c=tags_filter.value.toLowerCase();
returnPressed=d==13;
var g=0;
var f=sidebar.getElementsByTagName("ul");
for(i=0;
i<f.length;
i++){if(f[i]!=style_list){var a=f[i].getElementsByTagName("li");
for(x=0;
x<a.length;
x++){var j=a[x].getElementsByTagName("a");
for(xx=0;
xx<j.length;
xx++){var b=j[xx].childNodes[0].nodeValue;
if(j[xx].className!="bmaction"){if(b.toLowerCase().search(c)==-1){a[x].style.display="none"
}else{a[x].style.display="";
if(a[x].getElementsByTagName("a")[0].getAttribute("href")){if(returnPressed&&g==0){g=a[x].getElementsByTagName("a")[0].getAttribute("href")
}}else{if(returnPressed&&g==0){g=a[x].getElementsByTagName("a")[0].getAttribute("text")
}}}}}}}}if(g!=0){window.location.href=g
}}function setActiveInputField(b){activeField=b;
var a=document.getElementById("suggested");
if(a){while(a.hasChildNodes()){a.removeChild(a.firstChild)
}}}function toggle(b){clear_tags();
var c=xget_event(b);
var a=c.childNodes[0].nodeValue;
if(activeField){var d=document.getElementById(activeField)
}else{var d=document.getElementById("inpf")
}toggleTag(d,a)
}function add_toggle(){tags_toggle=1
}function add_tags_toggle(){if(tags_toggle==1){var b=tagbox.getElementsByTagName("li");
for(x=0;
x<b.length;
x++){var a=b[x].getElementsByTagName("a")[0];
a.onclick=toggle;
a.setAttribute("text",a.getAttribute("href"));
a.removeAttribute("href");
a.style.cursor="pointer"
}var c=document.getElementById("copytag");
if(c!=null){var b=c.getElementsByTagName("li");
for(x=0;
x<b.length;
x++){var a=b[x];
a.onclick=toggle
}}}}function add_toggle_relations(){var f=document.getElementById("relations");
var h=f.childNodes;
var a=0;
for(x=0;
x<h.length;
x++){var c=h[x];
if(c.nodeName=="LI"){a++;
var b=c.getElementsByTagName("a")[0];
b.onclick=add_supertag_to_input;
b.removeAttribute("href");
b.style.cursor="pointer";
b.setAttribute("title","add as supertag");
var g=h[x].getElementsByTagName("ul")[0];
var d=g.getElementsByTagName("li");
for(y=0;
y<d.length;
y++){var e=d[y].getElementsByTagName("a")[0];
e.onclick=add_subtag_to_input;
e.removeAttribute("href");
e.style.cursor="pointer";
e.setAttribute("title","add as subtag")
}}}}function add_supertag_to_input(b){var a=xget_event(b);
var c=a.childNodes[0].nodeValue;
document.getElementById("delete_up").value=c;
document.getElementById("insert_up").value=c
}function add_subtag_to_input(a){var c=xget_event(a);
var h=c.childNodes[0].nodeValue;
var f=document.getElementById("delete_lo");
h=h.replace(/ /,"");
var j=f.value.split(" ");
if(j[0]==""){j.splice(0,1)
}var e=0;
var b=new Array();
for(var g=0;
g<j.length;
g++){eintag=j[g];
if(eintag==h){e=1
}else{b.push(eintag)
}}if(!e){b.push(h)
}var d=b.join(" ");
f.value=d;
f.focus()
}function set_cookie(c,d){var b=new Date();
var a=new Date(b.getTime()+(1000*60*60*24*365));
document.cookie=c+"="+d+"; expires="+a.toGMTString()+";"
}function xget_event(a){if(!a){a=window.event
}if(a.srcElement){return a.srcElement
}else{if(a.target){return a.target
}}}function checkBrowser(){var a="";
if(navigator.appName.indexOf("Opera")!=-1){a="opera"
}else{if(navigator.appName.indexOf("Explorer")!=-1){a="ie"
}else{if(navigator.appName.indexOf("Netscape")!=-1){a="ns"
}else{a="undefined"
}}}return a
}var maxTagFreq=0;
var list=new Array();
var listElements=new Array();
var nodeList=new Array();
var copyListElements=new Array();
var copyList=new Array();
var savTag="";
var activeTag="";
var sortedCollection;
var collect;
var copyCollect;
var pos=0;
function enableHandler(){document.onkeydown=document.onkeypress=document.onkeyup=handler
}function disableHandler(){if(checkBrowser()=="ie"||checkBrowser()=="opera"){document.onkeydown=document.onkeypress=document.onkeyup
}else{document.onkeydown=document.onkeypress=document.onkeyup=disHandler
}}function Suggestion(b,a){this.tagname=b;
this.wighting=a
}function disHandler(a){}function handler(b){var a=activeField?document.getElementById(activeField).value:document.getElementById("inpf").value;
var c=(b||window.event||b.shiftkey);
if(c.type=="keyup"){switch(c.keyCode){case 8:if(sortedCollection){delete sortedCollection;
sortedCollection=new Array()
}if(a==""){savTag="";
activeTag=""
}else{getActiveTag(true);
clearSuggestion();
if(activeTag!=""){suggest()
}}break;
case 9:if(a!=""&&activeTag){clearSuggestion();
completeTag();
activeTag=""
}if(sortedCollection){delete sortedCollection;
sortedCollection=new Array()
}break;
case 38:case 40:break;
case 35:case 36:case 37:case 39:case 32:if(sortedCollection){delete sortedCollection;
sortedCollection=new Array()
}clearSuggestion();
break;
case 13:getActiveTag(false);
if(activeTag!=""){clearSuggestion();
completeTag()
}break;
default:getActiveTag(false);
clearSuggestion();
if(activeTag!=""){suggest()
}break
}}else{if(c.type=="keypress"){switch(c.keyCode){case 9:if(a!=""&&activeTag&&c.preventDefault()){c.preventDefault()
}break;
case 8:clearSuggestion();
savTag=getTags(a);
break;
case 38:case 40:if(c.preventDefault&&c.originalTarget){c.preventDefault()
}break;
default:savTag=getTags(a);
break
}}else{if(c.type=="keydown"){switch(c.keyCode){case 8:clearSuggestion();
savTag=getTags(a);
break;
case 38:if(a!=""){if(pos<sortedCollection.length-1&&pos<2){pos++
}else{pos=0
}clearSuggestion();
addToggleChild(sortedCollection)
}break;
case 40:if(a!=""){if(pos>0){pos--
}else{if(sortedCollection.length<3){pos=sortedCollection.length-1
}else{pos=2
}}clearSuggestion();
addToggleChild(sortedCollection)
}break;
default:savTag=getTags(a);
break
}}}}}function switchField(a,b){if(activeTag!=""){document.getElementById(a).focus()
}else{if(activeTag==""){document.getElementById(b).focus()
}}}function deleteCache(){clearSuggestion()
}function populateSuggestionsFromJSON(c){if(parseInt(c.items.length)==0){return
}var f=c.items;
var g=[];
for(b in f){g.push(f[b])
}g.sort(tagCompare);
for(var b=0;
b<g.length;
b++){var a=g[b].label;
var e=g[b].count;
if(e>maxTagFreq){maxTagFreq=e
}list.push(a);
var d=new Object;
d.title=e+" ";
d.count=e;
nodeList[a]=d
}return g
}function populateSuggestionsFromRecommendations(d){for(var b=0;
b<d.length;
b++){var a=d[b].label.replace(/^\s+|\s+$/g,"").replace(/ /g,"_");
list.push(a);
var c=d[b];
c.title=Math.ceil(c.score*(maxTagFreq/2)+(maxTagFreq/2))+" ";
nodeList[a]=d[b]
}list.sort(stringCompare)
}function tagCompare(d,c){return stringCompare(d.label,c.label)
}function stringCompare(d,c){if(d.toLowerCase()<c.toLowerCase()){return -1
}else{if(d.toLowerCase()==c.toLowerCase()){return 0
}else{return 1
}}}function setOps(){var d=document.getElementById("tagbox");
var g=new Array();
g=d.getElementsByTagName("li");
for(var c=0;
c<g.length;
++c){if(g[c].getElementsByTagName("a").length<2){var b=g[c].getElementsByTagName("a")[0];
var f=b.firstChild.data.trim();
listElements[f]=c;
nodeList[f]=b;
list.push(f)
}else{var b=g[c].getElementsByTagName("a")[2];
var f=b.firstChild.data.trim();
listElements[f.trim()]=c;
nodeList[f]=b;
list.push(f)
}}if(document.getElementById("recommendtag")){var a=document.getElementById("recommendtag");
var e=a.getElementsByTagName("li");
for(var c=0;
c<e.length;
++c){var b=e[c].getElementsByTagName("a")[0];
var f=b.firstChild.data;
listElements[f]=g.length+c;
nodeList[f]=b;
list.push(f.trim())
}}if(document.getElementById("copytag")){copyTag=document.getElementById("copytag");
copyRows=copyTag.getElementsByTagName("li");
for(var c=0;
c<copyRows.length;
++c){copyListElements[copyRows[c].firstChild.data]=c;
copyList[copyRows[c].firstChild.data]=copyRows[c]
}}list.sort(unicodeCollation)
}function getTags(b){var d=b.split(" ");
var a=new Array();
if(b.match(/->/)||b.match(/<-/)){for(i in d){if(d[i].match(/->/)){var c=d[i].split("->");
a.push(c[0]);
a.push(c[1])
}else{if(d[i].match(/<-/)){var c=d[i].split("<-");
a.push(c[0]);
a.push(c[1])
}else{a.push(d[i])
}}}}else{a=d
}return a
}function getActiveTag(b){var d=activeField?document.getElementById(activeField).value.toLowerCase().split(" "):document.getElementById("inpf").value.toLowerCase().split(" ");
var a=activeField?document.getElementById(activeField).value:document.getElementById("inpf").value;
var c=new Array();
c=getTags(a);
for(var e in c){if(typeof savTag!="undefined"){if(c[e]>savTag[e]&&!b){activeTag=c[e];
break
}else{if(c[e]<savTag[e]&&b&&c[e]>""){activeTag=c[e];
break
}else{activeTag=""
}}}}delete c
}function suggest(){delete collect;
if(sortedCollection){delete sortedCollection
}delete copyCollect;
collect=new Array();
sortedCollection=new Array();
copyCollect=new Array();
var n=activeTag.toLowerCase();
var d=n.length;
var m=false;
var a=0;
var h=0;
var g=list.length-1;
var b=0;
var o=activeField?document.getElementById(activeField).value.toLowerCase().split(" "):document.getElementById("inpf").value.toLowerCase().split(" ");
pos=0;
while(!m&&h<=g){b=Math.floor((h+g)/2);
if(list[b].substring(0,d).toLowerCase()==n){var e=b-1;
var c=b+1;
while(e>=0&&list[e].substring(0,d).toLowerCase()==n){collect.push(new Suggestion(list[e],nodeList[list[e]].title.split(" ")[0]));
e--
}while(c<=list.length-1&&list[c].substring(0,d).toLowerCase()==n){collect.push(new Suggestion(list[c],nodeList[list[c]].title));
c++
}collect.push(new Suggestion(list[b],nodeList[list[b]].title.split(" ")[0]));
m==true;
break
}else{if(list[b].substring(0,d).toLowerCase()>n){g=b-1
}else{h=b+1
}}}collect.sort(byWight);
if(document.getElementById("copytags")!=null){copyTag=document.getElementById("copytags");
copyRows=copyTag.getElementsByTagName("a");
for(var e=0;
e<copyRows.length;
++e){copyListElements[copyRows[e].firstChild.data.replace(/^\s+/,"").replace(/\s+$/,"")]=e;
copyList[copyRows[e].firstChild.data.replace(/^\s+/,"").replace(/\s+$/,"")]=copyRows[e]
}}for(copyTag in copyListElements){var f=false;
var l="";
valid=true;
if(d<=copyTag.length){l=copyTag.substring(0,d).toLowerCase()
}else{valid=false
}if(l.match(activeTag)&&valid){for(var k in o){f=false;
if(copyTag.toLowerCase()==o[k].toLowerCase()){f=true;
break
}}if(!f){copyCollect.push(copyTag)
}}e++
}for(var e in copyCollect){sortedCollection.push(copyCollect[e])
}for(var e in collect){for(var k in o){f=false;
if(collect[e].tagname.toLowerCase()==o[k].toLowerCase()){f=true;
break
}}var c=0;
while(c<sortedCollection.length&&!f){if(collect[e].tagname.toLowerCase()==sortedCollection[c].toLowerCase()){f=true;
break
}c++
}if(!f){sortedCollection.push(collect[e].tagname)
}}addToggleChild(sortedCollection)
}function byWight(d,c){if(c.wighting==d.wighting){if(c.tagname<d.tagname){return -1
}else{if(c.tagname>d.tagname){return 1
}else{return 0
}}}else{return c.wighting-d.wighting
}}function addToggleChild(d){var c=document.getElementById("suggested");
for(var a in d){var b=document.createElement("a");
c.appendChild(document.createTextNode(" "));
b.className="tagone";
b.appendChild(document.createTextNode(d[a]));
b.removeAttribute("href");
b.style.cursor="pointer";
b.setAttribute("href",'javascript:completeTag("'+d[a].replace(/"/g,'\\"')+'")');
if(a==pos){b.style.color="white";
b.style.backgroundColor="#006699"
}if(a==3){break
}c.appendChild(b)
}}function clearSuggestion(){var b=document.getElementById("suggested");
if(document.getElementById("copytag")){for(var a=0;
a<copyRows.length;
++a){copyRows[a].style.color="";
copyRows[a].style.backgroundColor=""
}}while(b.hasChildNodes()){b.removeChild(b.firstChild)
}}function getRelations(a){var b=new Array();
for(i in a){if(a[i].match(/->/)){b.push(1);
b.push(1)
}else{if(a[i].match(/<-/)){b.push(2);
b.push(2)
}else{b.push(0)
}}}return b
}function completeTag(m){var c=activeField?document.getElementById(activeField):document.getElementById("inpf");
var l=getTags(c.value);
var e=getTags(c.value.toLowerCase());
var f=getRelations(c.value.split(" "));
var k="";
var d=new Array();
var a=0;
var h=false;
var b=false;
for(var g in e){if(e[g]==activeTag.toLowerCase()){if(m){h=true;
l[g]=m+" ";
break
}else{if(sortedCollection){if(sortedCollection[pos]!=""){h=true;
var m=sortedCollection[pos];
l[g]=m+" ";
var j=lookupRecommendedTag(m);
if(j!=null){simulateClick(j)
}}if(!sortedCollection[pos]){h=false;
break
}}}}}if(h){for(var g=0;
g<l.length;
g++){b=false;
if(f[g]==1){k=l[g]+"->"+l[g+1];
g++
}else{if(f[g]==2){k=l[g]+"<-"+l[g+1];
g++
}else{k=l[g]
}}d.push(k)
}c.value=d.join(" ");
delete d
}activeTag="";
clearSuggestion();
c.focus();
if(window.opera){c.select()
}c.value=c.value
}function lookupRecommendedTag(b){var a=document.getElementById("tagField");
var c=a.getElementsByTagName("a");
var g=b.replace(/^\s+|\s+$/g,"");
var e=null;
for(var d=0;
d<c.length;
d++){var f=c[d].firstChild.nodeValue.replace(/^\s+|\s+$/g,"");
if(g==f){e=c[d];
break
}}return e
}function simulateClick(c){var a;
var b=c;
if(document.createEvent){a=document.createEvent("MouseEvents");
if(a.initMouseEvent){a.initMouseEvent("click",true,true,window,0,0,0,0,0,false,false,false,false,0,null)
}else{a=false
}}(a)?b.dispatchEvent(a):(b.click&&b.click())
}function setButton(){var c=document.getElementById("privnote");
if(c.firstChild){var d=document.getElementById("note");
var a=document.getElementById("makeP");
d.removeChild(a);
var b=document.createElement("input");
b.setAttribute("id","makeP");
b.setAttribute("type","button");
b.setAttribute("value","update");
b.setAttribute("onClick","makeParagraph()");
d.appendChild(b)
}}function makeParagraph(){var a=document.getElementById("makeP");
var c=document.getElementById("privnote");
var f=document.getElementById("note");
var d=document.createElement("p");
var b="";
if(c.firstChild){b=c.firstChild.data
}var e=document.createTextNode(b);
d.setAttribute("id","pText");
d.appendChild(e);
if(b!=""){c.style.display="none";
f.appendChild(d);
a.setAttribute("onClick","makeText()");
a.setAttribute("value","edit");
f.appendChild(a)
}}function makeText(){var a=document.getElementById("makeP");
var d=document.getElementById("pText");
var e=document.getElementById("note");
var c=document.getElementById("privnote");
c.style.display="inline";
var b=document.createElement("input");
b.setAttribute("type","submit");
b.setAttribute("value","update");
e.removeChild(d);
e.removeChild(a);
e.appendChild(b)
}function unicodeCollation(b,c){var a;
if(isNaN(b)==false&&isNaN(c)==false){a=b-c
}else{if(isNaN(b)==false&&isNaN(c)==true){a=-1
}else{if(isNaN(c)==false&&isNaN(b)==true){a=1
}else{if(b.toLowerCase()<c.toLowerCase()){a=-1
}else{if(c.toLowerCase()<b.toLowerCase()){a=1
}else{if(c.toLowerCase()==b.toLowerCase()){if(b<c){a=-1
}else{if(c<b){a=1
}else{a=0
}}}else{a=0
}}}}}}return a
}function ajaxInit(){var a;
try{if(window.XMLHttpRequest){a=new XMLHttpRequest()
}else{if(window.ActiveXObject){a=new ActiveXObject("Microsoft.XMLHTTP")
}}if(a.overrideMimeType){a.overrideMimeType("text/xml")
}}catch(b){return false
}return a
}function showOrHideConcept(a,d){var c=xget_event(a);
var b=c.parentNode.getElementsByTagName("a")[2].firstChild.nodeValue;
updateRelations(a,d,b)
}function hideConcept(a){var c=xget_event(a);
var b=c.parentNode.getElementsByTagName("a")[1].firstChild.nodeValue;
updateRelations(a,"hide",b)
}function updateRelations(a,f,e){var d=ajaxInit();
if(d){var b="/ajax/pickUnpickConcept?action="+f+"&tag="+encodeURIComponent(e)+"&ckey="+ckey;
d.open("GET",b,true);
d.setRequestHeader("Content-Type","text/xml");
d.setRequestHeader("If-Modified-Since","Sat, 1 Jan 2000 00:00:00 GMT");
var c=ajax_updateRelations(d);
d.onreadystatechange=c;
d.send(null);
if(a.stopPropagation){a.stopPropagation();
a.preventDefault()
}else{if(window.event){window.event.cancelBubble=true;
window.event.returnValue=false
}}}}function ajax_updateRelations(a){return function(){if(4==a.readyState){if(200==a.status){var C=document.getElementById("relations");
var m=new Array();
for(x=0;
x<C.childNodes.length;
x++){if(C.childNodes[x].nodeName=="LI"){m.push(C.childNodes[x])
}}for(x=0;
x<m.length;
x++){C.removeChild(m[x])
}var h=a.responseXML.documentElement;
var G=new Array();
if(h){var p=h.getAttribute("user");
var D=h.getElementsByTagName("relation");
for(x=0;
x<D.length;
x++){var e=D[x];
var B=e.getElementsByTagName("upper")[0].firstChild.nodeValue;
var f=document.createElement("li");
f.className="box_upperconcept";
G.push(B);
var b=document.createElement("a");
var A=document.createAttribute("href");
A.nodeValue="/ajax/pickUnpickConcept?action=hide&tag="+B+"&ckey="+ckey;
b.setAttributeNode(A);
var r=document.createTextNode(String.fromCharCode(8595));
b.appendChild(r);
f.appendChild(b);
f.appendChild(document.createTextNode(" "));
if(b.attachEvent){b.attachEvent("onclick",hideConcept)
}else{if(b.addEventListener){b.addEventListener("click",hideConcept,true)
}else{b.onclick=hideConcept
}}var u=document.createElement("a");
var s=document.createAttribute("href");
s.nodeValue="/concept/user/"+encodeURIComponent(p)+"/"+encodeURIComponent(B);
u.setAttributeNode(s);
var n=document.createTextNode(B);
u.appendChild(n);
f.appendChild(u);
f.appendChild(document.createTextNode(" "+String.fromCharCode(8592)+" "));
var w=e.getElementsByTagName("lower");
var j=document.createElement("ul");
j.className="box_lowerconcept_elements";
var v=document.createAttribute("id");
v.nodeValue=B;
j.setAttributeNode(v);
for(y=0;
y<w.length;
y++){var F=w[y].firstChild.nodeValue;
var g=document.createElement("li");
g.className="box_lowerconcept";
var k=document.createElement("a");
var t=document.createAttribute("href");
t.nodeValue="/user/"+encodeURIComponent(p)+"/"+encodeURIComponent(F);
k.setAttributeNode(t);
var o=document.createTextNode(F+" ");
k.appendChild(o);
g.appendChild(k);
j.appendChild(g)
}f.appendChild(j);
C.appendChild(f)
}}var l=document.getElementById("tagbox");
var q=l.getElementsByTagName("li");
for(x=0;
x<q.length;
x++){var d=q[x].getElementsByTagName("a");
if(d.length==3){var E=d[2].firstChild.nodeValue;
var c=true;
for(y=0;
y<G.length;
y++){if(E==G[y]){c=false
}}if(c){d[0].style.display="none";
d[1].style.display="inline"
}else{d[0].style.display="inline";
d[1].style.display="none"
}if(x==0){alert("done")
}}}delete G
}}}
}function pickAll(a){pickUnpickAll(a,"pickAll")
}function unpickAll(a){pickUnpickAll(a,"unpickAll")
}function pickUnpickAll(b,h){var a=document.getElementById("bibtex");
var c=a.getElementsByTagName("li");
var g="";
for(x=0;
x<c.length;
x++){var f=c[x].getElementsByTagName("div");
for(y=0;
y<f.length;
y++){if(f[y].className=="bmtitle"){var e=f[y].getElementsByTagName("a");
for(z=0;
z<e.length;
z++){if(e[z].getAttribute("href").match(/^.*\/documents\/.*/)==null){var d=e[z].getAttribute("href").replace(/^.*bibtex./,"");
g+=d+" "
}}}}}updateCollector("action="+h+"&hash="+encodeURIComponent(g));
breakEvent(b)
}function breakEvent(a){if(a.stopPropagation){a.stopPropagation();
a.preventDefault()
}else{if(window.event){window.event.cancelBubble=true;
window.event.returnValue=false
}}}function pickUnpickPublication(b){var d=xget_event(b).getAttribute("href").replace(/^.*?\?/,"");
updateCollector(d);
if(location.pathname.startsWith("/basket")){var a=b.currentTarget.parentNode.parentNode.parentNode;
var c=a.parentNode;
c.removeChild(a);
document.getElementById("ttlctr").childNodes[0].nodeValue="("+document.getElementById("pickctr").childNodes[0].nodeValue+")"
}breakEvent(b)
}function updateCollector(c){var b=ajaxInit();
if(b){var a="/ajax/pickUnpickPost?ckey="+ckey;
b.open("POST",a,true);
b.setRequestHeader("Content-type","application/x-www-form-urlencoded");
b.setRequestHeader("Content-length",c.length);
b.setRequestHeader("Connection","close");
b.onreadystatechange=ajax_updateCollector(b);
b.send(c)
}}function ajax_updateCollector(a){return function(){if(4==a.readyState){if(200==a.status){if(location.pathname.startsWith("/basket")){window.location.reload()
}else{document.getElementById("pickctr").childNodes[0].nodeValue=a.responseText
}}}}
}function sendEditTags(f,c,b,d){var a=f.childNodes[0].value;
var e=f.childNodes[0].name;
var b=f.childNodes[1].value;
var g=0;
$.ajax({type:"POST",url:"/batchEdit?newTags['"+e+"']="+encodeURIComponent(a.trim())+"&ckey="+b+"&deleteCheckedPosts=true"+"&resourcetype="+c+"&format=ajax",dataType:"html",global:"false",success:function(m){var k=f.parentNode;
var j=getString("post.meta.edit");
$(f).parent().children(".help").remove();
if(m.trim().length>0){if(c=="bibtex"){$(f).parent().children(":first").css({"float":"left"})
}$(f).before(m);
return false
}if(c=="bibtex"){$(f).parent().children(":first").css({"float":""});
g=2;
k.removeChild(k.childNodes[g]);
k.removeChild(k.childNodes[g])
}else{k.removeChild(k.firstChild);
k.removeChild(k.firstChild)
}var l=document.createElement("a");
l.setAttribute("onclick","editTags(this, '"+b+"'); return false;");
l.setAttribute("tags",a.trim());
l.setAttribute("href",d);
l.setAttribute("name",e);
l.appendChild(document.createTextNode(j));
k.insertBefore(l,k.childNodes[g]);
k=k.parentNode.previousSibling.childNodes[1];
while(k.hasChildNodes()){k.removeChild(k.firstChild)
}var n=a.split(" ");
for(i in n){var h=document.createElement("a");
h.setAttribute("href","/user/"+encodeURIComponent(currUser)+"/"+encodeURIComponent(n[i]));
h.appendChild(document.createTextNode(n[i]+" "));
k.appendChild(h)
}}});
return false
}function editTags(e,b){var n=e.getAttribute("tags");
var j=e.getAttribute("href");
var f=e.getAttribute("name");
var l=0;
var m=e.parentNode;
var h="bookmark";
if(j.search(/^\/editPublication/)!=-1){h="bibtex";
l=2;
m.removeChild(m.childNodes[l])
}m.removeChild(m.childNodes[l]);
var c=document.createElement("form");
c.className="tagtextfield";
c.setAttribute("onsubmit","sendEditTags(this, '"+h+"',  '"+b+"', "+"'"+j+"'); return false;");
var k=document.createElement("input");
k.setAttribute("name",f);
k.setAttribute("size","30");
k.value=n;
var g=document.createElement("input");
g.type="hidden";
g.setAttribute("name","ckey");
g.value=b;
var a=document.createElement("a");
a.setAttribute("href",j);
if(h=="bibtex"){a.appendChild(document.createTextNode(getString("bibtex.actions.details")));
a.title=getString("bibtex.actions.details.title")
}else{a.appendChild(document.createTextNode(getString("bookmark.actions.details")));
a.title=getString("bookmark.actions.details.title")
}c.appendChild(k);
c.appendChild(g);
if(h=="bibtex"){var d=document.createTextNode(" | ");
m.insertBefore(d,m.childNodes[l]);
m.insertBefore(a,m.childNodes[l]);
m.insertBefore(c,m.childNodes[l])
}else{m.insertBefore(a,m.firstChild);
m.insertBefore(c,m.firstChild)
}}String.prototype.startsWith=function(a){return this.indexOf(a)==0
};
function getString(a){if(typeof LocalizedStrings=="undefined"){return"???"+a+"???"
}var b=LocalizedStrings[a];
if(!b){return"???"+a+"???"
}return b
}function toggleTag(f,g){clear_tags();
var j=g.replace(/^\s+|\s+$/g,"").replace(/ /g,"_");
var b=f;
var h=b.value.split(" ");
if(h[0]==""){h.splice(0,1)
}var d=0;
var a=new Array();
for(var e=0;
e<h.length;
e++){eintag=h[e];
if(eintag==j){d=1
}else{a.push(eintag)
}}if(!d){a.push(j)
}var c=a.join(" ");
activeTag="";
if(sortedCollection){sortedCollection[0]="";
clearSuggestion()
}b.focus();
b.value=c
}function copytag(c,a){var b=document.getElementById(c);
if(b){toggleTag(b,a)
}}function showTagSets(a){for(var b=0;
b<a.options.length;
b++){var d=a.options[b];
var c=document.getElementById("field_"+d.value);
if(c!=null){if(d.selected){c.style.display=""
}else{c.style.display="none"
}}}}function addSystemTags(){var a=0;
clear_tags();
var b=document.getElementById("inpf").value;
while(document.getElementById("relgroup"+a)!=null){if(document.getElementById("relgroup"+a).selected==true){var c=document.getElementById("relgroup"+a).value;
if(b.match(":"+c)==null){if(systemtags==null){systemtags="sys:relevantFor:"+c
}else{systemtags+=" "+"sys:relevantFor:"+c
}}}a++
}if(systemtags!=null){copytag("inpf",systemtags)
}}function getNodeValueByEvent(a){node=xget_event(a);
return node.getAttributeNode("value").value
}function copyOptionTags(c,a){var b=getNodeValueByEvent(a);
copytag(c,b)
}String.prototype.trim=function(){return this.replace(/^\s+/g,"").replace(/\s+$/g,"")
};
function switchLogin(){var b="db,openid";
var e=null;
var c="login";
switch(arguments.length){case 3:if(arguments[2]!=""){c=arguments[2]
}case 2:e=arguments[1];
case 1:if(arguments[0]!=""){b=arguments[0]
}}methods=b.split(",");
if(!e){e=methods[0]
}var d=document.getElementById(c.concat("MethodSelect"));
if(d){for(i=0;
i<d.length;
i++){if(d.options[i].value==e){d.options[i].selected=true
}else{d.options[i].selected=false
}}}for(i=0;
i<methods.length;
i++){elementId=c.concat(methods[i]);
if(e==methods[i]){document.getElementById(elementId).style.display="block";
var a=document.getElementById(c.concat("Method").concat(e));
if(a){a.value=e
}}else{document.getElementById(elementId).style.display="none"
}}}function prepareErrorBoxes(b){var a="."+b;
$(a).each(function(){if(parseInt($(this).html().length)==0){return true
}$(this).mouseover(function(){$(this).fadeOut("slow")
});
if(typeof $(this).children(":first")!=undefined&&$(this).children(":first").attr("id")!=undefined&&$(this).children(":first").attr("id").length>0){var e=("#"+($(this).children(":first").attr("id")).substr(0,($(this).children(":first").attr("id")).length-".errors".length)).replace(/\./g,"\\.");
var d=$(this);
var c=function(){d.fadeOut("slow")
};
$(e).keyup(c).change(c)
}if(!$(this).hasClass("initiallyHidden")){$(this).fadeIn("slow")
}});
$("#inpf").keyup(function(){$("#tags\\.errors").parent().fadeOut("slow")
})
}function cmpClass(a,b){for(i=0,partials=a.className.split(" ");
parseInt(partials.length)>i;
i++){if(b==partials[i]){return true
}}return false
}function toggleFieldsetVisibility(c){var d=null;
var a=null;
var b=null;
if((d=getNextByClass(c.parentNode,""))==null){return
}if(cmpClass(c.parentNode.parentNode,"fsHidden")){$(d).hide();
$(c.parentNode.parentNode).removeClass("fsHidden").addClass("fsVisible");
a="/resources/image/icon_collapse.png"
}else{a="/resources/image/icon_expand.png";
b="fsHidden"
}$(d).css("visibility","hidden").slideToggle(200,function(){c.src=a;
if(b){$(c.parentNode.parentNode).removeClass("fsVisible").addClass(b)
}$(this).css("visibility","visible")
})
}function getNextByClass(b,a){while(b!=null){if(b.tagName=="DIV"){if(cmpClass(b,a)){return b
}}b=b.nextSibling
}return null
}(function(a){a.fn.descrInputLabel=function(b){a(this).each(function(){var e=this;
var d=getParentForm(e);
var c=((typeof b.valueCallback=="function")?b.valueCallback:e.value);
a(e).bind("focus",function(){if(a(e).hasClass("descriptiveLabel")){e.value="";
a(e).removeClass("descriptiveLabel")
}});
a(d).submit(function(){if(a(e).hasClass("descriptiveLabel")||e.value==""){a(e).val("").removeClass("descriptiveLabel").trigger("focus");
return false
}})
})
}
})(jQuery);
function overwriteLabel(a){value=a.val();
if((!value.length||value==getString("navi.author.hint")||value==getString("navi.tag.hint")||value==getString("navi.user.hint")||value==getString("navi.group.hint")||value==getString("navi.concept.hint")||value==getString("navi.bibtexkey.hint"))||(a!=null&&value==getString("navi.search.hint"))){return true
}return false
}function setSearchInputLabel(b){var a=$("input[name=search]");
if(!overwriteLabel(a)){return
}if(b.value=="tag"){a.val(getString("navi.tag.hint"))
}else{if(b.value=="user"){a.val(getString("navi.user.hint"))
}else{if(b.value=="group"){a.val(getString("navi.group.hint"))
}else{if(b.value=="author"){a.val(getString("navi.author.hint"))
}else{if(b.value=="concept/tag"){a.val(getString("navi.concept.hint"))
}else{if(b.value=="bibtexkey"){a.val(getString("navi.bibtexkey.hint"))
}else{if(b.value.indexOf("user")!=-1||b.value=="search"){a.val(getString("navi.search.hint"))
}}}}}}}if(!a.hasClass("descriptiveLabel")){a.addClass("descriptiveLabel")
}return a
}function getParentForm(a){a=($(a).parent())[0];
return((validElement(a,"form"))?a:getParentForm(a))
}function appendToToolbar(){var a=function(c,b){return $("<a></a>").attr("id",c).html(b)
};
$("#toolbar").append($("<div></div>").attr("id","post-toggle").append((a("post-method-isbn",getString("post_bibtex.doi_isbn.isbn"))).addClass("active")).append(a("post-method-manual",getString("post_bibtex.manual.title"))).append($("<div></div>").css({"clear":"both","height":"0"}).html("&nbsp;")))
}function concatArray(b,e,d){var c="";
var a;
if(d==null){d="\n"
}for(a in b){c+=b[a]+((a<b.length-1)?d:"")
}return((e!=null)&&(c.length>e))?c.substr(0,e)+"...":c
}function createParameters(b){if(b[b.length-1]==" "){b=b.substr(0,b.length-1)
}var a=b.split(" ");
b="";
for(i=0;
i<parseInt(a.length);
i++){b+="sys:title:"+encodeURIComponent(a[i])+((i+1<parseInt(a.length))?"+":"*")
}return b
};