var server=null;
var path=null;
var port=0;
var mostInnerLi=true;
var logUsername="";
var serverurl="/logging";
function log_init(){log_register_events()
}function log_setUsername(a){logUsername=a
}function log_getUsername(){return logUsername
}function log_register_events(){if(document.addEventListener){try{document.addEventListener("click",log_sendRequest,false)
}catch(a){}}else{if(document.attachEvent){try{document.attachEvent("onclick",log_sendRequest)
}catch(a){}}else{}}}function log_sendRequest(g){if(document.addEventListener){element=g.target
}else{if(document.attachEvent){element=event.srcElement
}else{}}welement=element;
dom_path="";
dom_path2="";
dom_acontent="";
dom_ahref="";
a_node_present=false;
numberofposts="";
function i(){if(window.innerWidth){return window.innerWidth
}else{if(document.body&&document.body.offsetWidth){return document.body.offsetWidth
}else{return 0
}}}function j(){if(window.innerHeight){return window.innerHeight
}else{if(document.body&&document.body.offsetHeight){return document.body.offsetHeight
}else{return 0
}}}function c(l){var n=(typeof document.compatMode!="undefined"&&document.compatMode!="BackCompat")?"documentElement":"body";
var m=l?l.pageX:window.event.x;
var k=l?l.pageY:window.event.y;
if(document.all&&!document.captureEvents){m+=document[n].scrollLeft;
k+=document[n].scrollTop
}return m+" "+k
}function h(l){var m=l?l.clientX:window.event.clientX;
var k=l?l.clientY:window.event.clientY;
return m+" "+k
}mostInnerLi=true;
do{welementattrs=welement.attributes;
welementattrs_id="";
welementattrs_class="";
welementattrs_ahref="";
welementattrs_title="";
try{welementattrs_id=welementattrs.getNamedItem("id").value
}catch(f){}try{welementattrs_class=welementattrs.getNamedItem("class").value
}catch(f){}try{welementattrs_ahref=welementattrs.getNamedItem("href").value
}catch(f){}try{welementattrs_title=welementattrs.getNamedItem("title").value
}catch(f){}if(welement.nodeName=="A"){childNodesA=[];
childNodesA=welement.childNodes;
dom_acontent="";
for(var d=0;
d<childNodesA.length;
d++){var b=childNodesA[d];
if(b.nodeName=="#text"){dom_acontent+=" "+b.nodeValue
}else{if(b.nodeName=="IMG"){var a=["id","title","alt"];
for(element in a){if((b.getAttribute(a[element])!=null)&&(b.getAttribute(a[element])!="")){dom_acontent+=" "+b.getAttribute(a[element]);
break
}}}}}if(dom_acontent){dom_acontent=dom_acontent.replace(/^\s+/,"").replace(/\s+$/,"")
}dom_ahref=welementattrs_ahref;
numberofposts=welementattrs_title.split(" ")[0];
a_node_present=true;
if(welementattrs_id.substr(0,5)=="spam_"){welement.setAttribute("id","no"+welement.getAttribute("id"));
welement.setAttribute("title",getString("post.meta.unflag_as_spam.title"));
welement.firstChild.nodeValue=getString("post.meta.unflag_as_spam")
}else{if(welementattrs_id.substr(0,7)=="nospam_"){welement.setAttribute("id",welementattrs_id.substr(2));
welement.setAttribute("title",getString("post.meta.flag_as_spam.title"));
welement.firstChild.nodeValue=getString("post.meta.flag_as_spam")
}}}dom_path+=welement.nodeName;
if(welementattrs_id!=""){dom_path+="#"+welementattrs_id
}dom_path+="/";
dom_path2+=welement.nodeName;
if(welementattrs_id!=""){dom_path2+="#"+welementattrs_id
}if(welementattrs_class!=""){dom_path2+="."+welementattrs_class
}dom_path2+="/";
sibling_count=0;
if(welement.nodeName=="LI"&&mostInnerLi){mostInnerLi=false;
siblingnode=welement;
sibling_count=1;
while(siblingnode.previousSibling){siblingnode=siblingnode.previousSibling;
if(siblingnode.nodeName=="LI"){sibling_count+=1
}}}welement=welement.parentNode
}while(welement.parentNode);
http_request=false;
if(a_node_present){if(window.XMLHttpRequest){http_request=new XMLHttpRequest();
if(http_request.overrideMimeType){http_request.overrideMimeType("text/xml")
}}else{if(window.ActiveXObject){try{http_request=new ActiveXObject("Msxml2.XMLHTTP")
}catch(g){try{http_request=new ActiveXObject("Microsoft.XMLHTTP")
}catch(g){}}}}if(!http_request){return false
}senddata="dompath="+dom_path+"&dompath2="+dom_path2+"&pageurl="+document.location.href+"&numberofposts="+numberofposts+"&acontent="+dom_acontent+"&ahref="+dom_ahref+"&windowsize="+i()+" "+j()+"&mousedocumentpos="+c(g)+"&mouseclientpos="+h(g)+"&listpos="+sibling_count+"&referer="+document.referrer+"&username="+log_getUsername();
http_request.open("POST",serverurl,true);
http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
http_request.send(senddata)
}}log_init();