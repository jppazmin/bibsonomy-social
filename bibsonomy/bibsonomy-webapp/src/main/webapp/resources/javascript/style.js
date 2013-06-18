var request=null;
var style_list=null;
var style_sort=new Array("alph","freq");
var style_show=new Array("cloud","list");
var userMinFreq=1;
function init_tagbox(a,c,b,e){style_list=document.createElement("ul");
style_list.className="floatul";
style_list.appendChild(document.createElement("li"));
style_list.appendChild(document.createElement("li"));
style_list.appendChild(document.createElement("li"));
style_list.replaceChild(getStyleItem(style_sort[c],style_sort),style_list.childNodes[0]);
style_list.replaceChild(getStyleItem(style_show[a],style_show),style_list.childNodes[1]);
if(typeof tagbox_minfreq_style!="undefined"){if(tagbox_minfreq_style=="user"){showUserMinfreq(b,e)
}else{if(tagbox_minfreq_style=="default"){showMinfreq()
}else{if(tagbox_minfreq_style=="none"){}}}}var d=document.createElement("span");
d.appendChild(style_list);
changeTagBox(style_show[a]);
changeTagBox(style_sort[c]);
tagbox.parentNode.insertBefore(d,tagbox)
}function attachChangeTagBox(a){return(function(){changeTagBox(a)
})
}function changeTagBox(b){var a=ajaxInit();
if(b=="list"||b=="cloud"){tagbox.className="tag"+b;
style_list.replaceChild(getStyleItem(b,style_show),style_list.childNodes[1])
}else{if(b=="alph"||b=="freq"){style_list.replaceChild(getStyleItem(b,style_sort),style_list.childNodes[0]);
b=="alph"?setTagBoxAlph():setTagBoxFreq()
}}}function getStyleItem(c,b){var a=document.createElement("li");
var d=document.createElement("a");
d.style.cursor="pointer";
a.appendChild(document.createTextNode(" ("));
if(c==b[0]){a.appendChild(document.createTextNode(getString("tagbox."+b[0])+" | "));
d.onclick=attachChangeTagBox(b[1]);
d.appendChild(document.createTextNode(getString("tagbox."+b[1])));
a.appendChild(d)
}else{d.onclick=attachChangeTagBox(b[0]);
d.appendChild(document.createTextNode(getString("tagbox."+b[0])));
a.appendChild(d);
a.appendChild(document.createTextNode(" | "+getString("tagbox."+b[1])))
}a.appendChild(document.createTextNode(") "));
return a
}function attachMinUsertags(a){return(function(){minUsertags(a)
})
}function getMinUsertagsLink(b){var a=document.createElement("a");
a.onclick=attachMinUsertags(b);
a.appendChild(document.createTextNode(b));
a.style.cursor="pointer";
return a
}function getMinTagsLink(b){var a=document.createElement("a");
if(userMinFreq!=b){a.onclick=attachMinUsertags(b);
a.style.cursor="pointer"
}a.appendChild(document.createTextNode(b));
return a
}function showUserMinfreq(a,c){var b=document.createElement("li");
b.appendChild(document.createTextNode(" ("+getString("tagbox.minfreq")+"  "));
if(a==1){b.appendChild(document.createTextNode("1 | "));
b.appendChild(getMinUsertagsLink(2));
b.appendChild(document.createTextNode(" | "));
b.appendChild(getMinUsertagsLink(5))
}else{if(a==2){b.appendChild(getMinUsertagsLink(1));
b.appendChild(document.createTextNode(" | 2 | "));
b.appendChild(getMinUsertagsLink(5))
}else{if(a==5){b.appendChild(getMinUsertagsLink(1));
b.appendChild(document.createTextNode(" | "));
b.appendChild(getMinUsertagsLink(2));
b.appendChild(document.createTextNode(" | 5"))
}else{b.appendChild(getMinUsertagsLink(1));
b.appendChild(document.createTextNode(" | "));
b.appendChild(getMinUsertagsLink(2));
b.appendChild(document.createTextNode(" | "));
b.appendChild(getMinUsertagsLink(5))
}}}b.appendChild(document.createTextNode(") "));
style_list.replaceChild(b,style_list.childNodes[2])
}function showMinfreq(){var a=document.createElement("li");
a.appendChild(document.createTextNode(" ("+getString("tagbox.minfreq")+"  "));
a.appendChild(getMinTagsLink(1));
a.appendChild(document.createTextNode(" | "));
a.appendChild(getMinTagsLink(2));
a.appendChild(document.createTextNode(" | "));
a.appendChild(getMinTagsLink(5));
a.appendChild(document.createTextNode(") "));
style_list.replaceChild(a,style_list.childNodes[2])
}function setTagBoxAlph(){collection_tagname=new Array();
collection_li=new Object();
var d=document.getElementById("tagbox");
var h=d.getElementsByTagName("li");
for(x=0;
x<h.length;
x++){var i=h[x].getElementsByTagName("a");
if(i.length==1){var g=i[0].firstChild.nodeValue;
collection_tagname.push(g);
collection_li[g]=h[x].cloneNode(true)
}else{if(i.length>=2){var g=i[2].firstChild.nodeValue;
collection_tagname.push(g);
collection_li[g]=h[x].cloneNode(true)
}}}var e=h.length;
for(x=0;
x<h.length;
x++){var b=h[x].getElementsByTagName("a");
var c=b.length;
for(y=0;
y<c;
y++){h[x].removeChild(b[0])
}}for(x=0;
x<e;
x++){d.removeChild(h[0])
}var a=document.createTextNode(" ");
collection_tagname.sort(unicodeCollation);
for(x=0;
x<collection_tagname.length;
x++){var g=collection_tagname[x];
var f=collection_li[g];
f.appendChild(a.cloneNode(true));
d.appendChild(f)
}delete collection_tagname;
delete collection_li
}function setTagBoxFreq(){collection_tagname=new Array();
collection_li=new Object();
collection_tagposts=new Object();
collection_numberofposts=new Array();
var g=document.getElementById("tagbox");
var l=g.getElementsByTagName("li");
for(x=0;
x<l.length;
x++){var m=l[x].getElementsByTagName("a");
if(m.length==1){var k=m[0].firstChild.nodeValue;
collection_tagname.push(k);
collection_li[k]=l[x].cloneNode(true);
var i=m[0].getAttribute("title");
var b=i.split(" ");
var f=parseInt(b[0]);
collection_tagposts[k]=f;
var c=true;
for(y=0;
y<collection_numberofposts.length;
y++){if(collection_numberofposts[y]==f){c=false
}}if(c){collection_numberofposts.push(f)
}}else{if(m.length>=2){var k=m[2].firstChild.nodeValue;
collection_tagname.push(k);
collection_li[k]=l[x].cloneNode(true);
var i=m[2].getAttribute("title");
var b=i.split(" ");
var f=parseInt(b[0]);
collection_tagposts[k]=f;
var c=true;
for(y=0;
y<collection_numberofposts.length;
y++){if(collection_numberofposts[y]==f){c=false
}}if(c){collection_numberofposts.push(f)
}}}}var h=l.length;
for(x=0;
x<l.length;
x++){var d=l[x].getElementsByTagName("a");
var e=d.length;
for(y=0;
y<e;
y++){l[x].removeChild(d[0])
}}for(x=0;
x<h;
x++){g.removeChild(l[0])
}collection_numberofposts.sort(unicodeCollation);
collection_numberofposts.reverse();
for(x=0;
x<collection_numberofposts.length;
x++){var m=new Array();
for(y=0;
y<collection_tagname.length;
y++){var k=collection_tagname[y];
if(collection_tagposts[k]==collection_numberofposts[x]){m.push(k)
}}m.sort(unicodeCollation);
var a=document.createTextNode(" ");
for(y=0;
y<m.length;
y++){var k=m[y];
var j=collection_li[k];
j.appendChild(a.cloneNode(true));
g.appendChild(j)
}delete m
}delete collection_tagname;
delete collection_li;
delete collection_tagposts;
delete collection_numberofposts
}function sendMinfreqRequ(a,c){var b=ajaxInit();
if(b){if(a==null){a=1
}b.open("GET","?tagcloud.minFreq="+a+"&ckey="+ckey+"&tagstype=default&format=tagcloud",true);
userMinFreq=a;
b.onreadystatechange=handleMinfreqResponse(b);
b.send(null)
}}function handleMinfreqResponse(a){return function(){if(a.readyState==4){if(window.navigator.userAgent.indexOf("MSIE ")>-1){window.location.reload()
}else{replaceTags(a)
}}}
}function replaceTags(e){var d='<li class="tag';
var b="</ul>";
var f=e.responseText;
var h=f.indexOf(d);
var a=f.indexOf(b,h);
tagbox.innerHTML=f.slice(h,a);
var c="<span>";
var g="</span>";
h=f.indexOf(c)+(g.length-1);
a=f.indexOf(g);
if(f.slice(h,a)=="ALPHA"){setTagBoxAlph()
}else{setTagBoxFreq()
}}function minUsertags(a){sendMinfreqRequ(a);
showMinfreq(a)
}var gOptions=new Array();
function naviSwitchSpecial(h){var f=null;
if(requUser!=null){f=requUser
}else{if(currUser!=null){f=currUser
}}var g=document.getElementsByTagName("body")[0];
var l=document.getElementById("heading").parentNode;
if(l==null){l=document.getElementById("path")
}var o=document.createElement("h1");
o.setAttribute("id","path");
var m=document.createElement("a");
m.setAttribute("href","/");
m.setAttribute("rel","Start_js");
m.appendChild(document.createTextNode(projectName));
o.appendChild(m);
o.appendChild(document.createTextNode(" :: "));
var b=document.createElement("form");
b.setAttribute("id","specialsearch");
b.setAttribute("method","get");
b.setAttribute("action","/redirect");
var k=document.createElement("select");
k.setAttribute("name","scope");
k.setAttribute("size","1");
k.setAttribute("id","scope");
var q=new Array("tag","user","group","author","concept/tag","bibtexkey","search","explicit_user","explicit_group");
var d="";
for(var e=0;
e<q.length;
e++){if(q[e]=="search"){var p=document.createElement("option");
p.setAttribute("value",q[e]);
p.appendChild(document.createTextNode(getString("navi.search")+":"+getString("navi.all")));
if(q[e]==h){p.setAttribute("selected","");
d=getString("navi.search.hint")
}k.appendChild(p)
}else{if(q[e]=="concept/tag"){var p=document.createElement("option");
p.setAttribute("value",q[e]);
p.appendChild(document.createTextNode(getString("navi.concept")));
if(q[e]==h){p.setAttribute("selected","");
d=getString("navi.concept.hint")
}k.appendChild(p)
}else{if(q[e]=="explicit_user"){if(f!=""&&f!=null){var p=document.createElement("option");
p.setAttribute("value","user:"+f);
p.appendChild(document.createTextNode(getString("navi.search")+":"+f));
if(q[e]==h){p.setAttribute("selected","");
d=getString("navi.search.hint")
}k.appendChild(p)
}}else{if(q[e]=="explicit_group"){for(var c=0;
c<gOptions.length;
++c){var p=document.createElement("option");
p.setAttribute("value","group:"+gOptions[c]);
p.appendChild(document.createTextNode(getString("navi.search")+":"+gOptions[c]));
if(gOptions[c]==h){p.setAttribute("selected","");
d=getString("navi.search.hint")
}k.appendChild(p)
}}else{var p=document.createElement("option");
p.setAttribute("value",q[e]);
p.appendChild(document.createTextNode(getString("navi."+q[e])));
if(q[e]==h){p.setAttribute("selected","");
d=getString("navi."+q[e]+".hint")
}k.appendChild(p)
}}}}}b.appendChild(k);
b.appendChild(document.createTextNode(" :: "));
var a=document.createElement("input");
a.setAttribute("type","text");
a.setAttribute("id","inpf");
a.setAttribute("name","search");
a.setAttribute("size","30");
if(document.getElementById("inpf")!=null){var n=document.getElementById("inpf").value;
a.value=n
}b.appendChild(a);
o.appendChild(b);
o.appendChild(document.createTextNode(" "));
curr_navi=l.cloneNode(true);
l.replaceChild(o,document.getElementById("heading"));
o.id="heading";
$(a).descrInputLabel({});
$(k).bind("change",function(){setSearchInputLabel(this)
}).trigger("change");
a.focus();
if(window.opera){a.select()
}a.value=a.value
};