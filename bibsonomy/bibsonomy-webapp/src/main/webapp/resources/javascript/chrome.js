var cssdropdown={disappeardelay:250,dropdownindicator:"",enableswipe:1,enableiframeshim:1,dropmenuobj:null,asscmenuitem:null,domsupport:document.all||document.getElementById,standardbody:null,iframeshimadded:false,swipetimer:undefined,bottomclip:0,getposOffset:function(d,c){var b=(c=="left")?d.offsetLeft:d.offsetTop-2;
var a=d.offsetParent;
while(a!=null){b=(c=="left")?b+a.offsetLeft:b+a.offsetTop;
a=a.offsetParent
}return b
},swipeeffect:function(){if(this.bottomclip<parseInt(this.dropmenuobj.offsetHeight)){this.bottomclip+=10+(this.bottomclip/10);
this.dropmenuobj.style.clip="rect(0 auto "+this.bottomclip+"px 0)"
}else{return
}this.swipetimer=setTimeout("cssdropdown.swipeeffect()",10)
},css:function(b,a,c){var d=new RegExp("(^|\\s+)"+a+"($|\\s+)","ig");
if(c=="check"){return d.test(b.className)
}else{if(c=="remove"){b.className=b.className.replace(d,"")
}else{if(c=="add"&&!d.test(b.className)){b.className+=" "+a
}}}},showhide:function(b,a){this.dropmenuobj.style.left=this.dropmenuobj.style.top="-500px";
if(this.enableswipe==1){if(typeof this.swipetimer!="undefined"){clearTimeout(this.swipetimer)
}b.clip="rect(0 auto 0 0)";
this.bottomclip=0;
this.swipeeffect()
}b.visibility="visible";
this.css(this.asscmenuitem,"selected","add")
},clearbrowseredge:function(e,c){var b=0;
if(c=="rightedge"){var d=document.all&&!window.opera?this.standardbody.scrollLeft+this.standardbody.clientWidth-15:window.pageXOffset+window.innerWidth-15;
this.dropmenuobj.contentmeasure=this.dropmenuobj.offsetWidth;
if(d-this.dropmenuobj.x<this.dropmenuobj.contentmeasure){b=this.dropmenuobj.contentmeasure-e.offsetWidth
}}else{var a=document.all&&!window.opera?this.standardbody.scrollTop:window.pageYOffset;
var d=document.all&&!window.opera?this.standardbody.scrollTop+this.standardbody.clientHeight-15:window.pageYOffset+window.innerHeight-18;
this.dropmenuobj.contentmeasure=this.dropmenuobj.offsetHeight;
if(d-this.dropmenuobj.y<this.dropmenuobj.contentmeasure){b=this.dropmenuobj.contentmeasure+e.offsetHeight;
if((this.dropmenuobj.y-a)<this.dropmenuobj.contentmeasure){b=this.dropmenuobj.y+e.offsetHeight-a
}}}return b
},dropit:function(c,b,a){if(this.dropmenuobj!=null){this.hidemenu()
}this.clearhidemenu();
this.dropmenuobj=document.getElementById(a);
this.asscmenuitem=c;
this.showhide(this.dropmenuobj.style,b);
this.dropmenuobj.x=this.getposOffset(c,"left");
this.dropmenuobj.y=this.getposOffset(c,"top");
this.dropmenuobj.style.left=this.dropmenuobj.x-this.clearbrowseredge(c,"rightedge")+"px";
this.dropmenuobj.style.top=this.dropmenuobj.y-this.clearbrowseredge(c,"bottomedge")+c.offsetHeight+1+"px";
this.positionshim()
},positionshim:function(){if(this.enableiframeshim&&typeof this.shimobject!="undefined"){if(this.dropmenuobj.style.visibility=="visible"){this.shimobject.style.width=this.dropmenuobj.offsetWidth+"px";
this.shimobject.style.height=this.dropmenuobj.offsetHeight+"px";
this.shimobject.style.left=this.dropmenuobj.style.left;
this.shimobject.style.top=this.dropmenuobj.style.top
}this.shimobject.style.display=(this.dropmenuobj.style.visibility=="visible")?"block":"none"
}},hideshim:function(){if(this.enableiframeshim&&typeof this.shimobject!="undefined"){this.shimobject.style.display="none"
}},isContained:function(a,b){var b=window.event||b;
var d=b.relatedTarget||((b.type=="mouseover")?b.fromElement:b.toElement);
while(d&&d!=a){try{d=d.parentNode
}catch(b){d=a
}}if(d==a){return true
}else{return false
}},dynamichide:function(a,b){if(!this.isContained(a,b)){this.delayhidemenu()
}},delayhidemenu:function(){this.delayhide=setTimeout("cssdropdown.hidemenu()",this.disappeardelay)
},hidemenu:function(){this.css(this.asscmenuitem,"selected","remove");
this.dropmenuobj.style.visibility="hidden";
this.dropmenuobj.style.left=this.dropmenuobj.style.top=0;
this.hideshim()
},clearhidemenu:function(){if(this.delayhide!="undefined"){clearTimeout(this.delayhide)
}},addEvent:function(b,c,a){if(b!=null){if(b.addEventListener){b.addEventListener(a,c,false)
}else{if(b.attachEvent){b.attachEvent("on"+a,function(){return c.call(b,window.event)
})
}}}},startchrome:function(){if(!this.domsupport){return
}this.standardbody=(document.compatMode=="CSS1Compat")?document.documentElement:document.body;
for(var d=0;
d<arguments.length;
d++){var e=document.getElementById(arguments[d]).getElementsByTagName("a");
for(var b=0;
b<e.length;
b++){if(e[b].getAttribute("rel")){var a=e[b].getAttribute("rel");
var c=document.getElementById(a);
this.addEvent(c,function(){cssdropdown.clearhidemenu()
},"mouseover");
this.addEvent(c,function(f){cssdropdown.dynamichide(this,f)
},"mouseout");
this.addEvent(c,function(){cssdropdown.delayhidemenu()
},"click");
this.addEvent(e[b],function(g){if(!cssdropdown.isContained(this,g)){var f=window.event||g;
cssdropdown.dropit(this,f,this.getAttribute("rel"))
}},"mouseover");
this.addEvent(e[b],function(f){cssdropdown.dynamichide(this,f)
},"mouseout");
this.addEvent(e[b],function(){cssdropdown.delayhidemenu()
},"click")
}}}if(window.createPopup&&!window.XmlHttpRequest&&!this.iframeshimadded){document.write('<IFRAME id="iframeshim"  src="" style="display: none; left: 0; top: 0; z-index: 90; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)" frameBorder="0" scrolling="no"></IFRAME>');
this.shimobject=document.getElementById("iframeshim");
this.iframeshimadded=true
}}};