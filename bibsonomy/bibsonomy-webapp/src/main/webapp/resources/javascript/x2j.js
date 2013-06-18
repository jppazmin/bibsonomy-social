(function(d){var c=Object.prototype.toString,f={isIE:function(){return(+"\0"===0)
},isStr:function(k){return c.call(k)==="[object String]"
},isFn:function(k){return c.call(k)==="[object Function]"
},isDef:function(k){return(typeof(k)!=="undefined")
},isArr:function(k){return c.call(k)==="[object Array]"
},isNum:function(l){var k=false;
if(f.isStr(l)){var m=/^((-)?([0-9]*)((\.{0,1})([0-9]+))?$)/;
k=m.test(l)
}return k
},isXNode:function(k){return(typeof(k)==="object"&&this.isDef(k.nodeName))
},isNodeSet:function(k){return k instanceof a
},trim:function(l){var k=l;
if(this.isStr(l)){k=l.replace(/^\s\s*/,"").replace(/\s\s*$/,"")
}return k
},formatName:function(l){var k=this.trim(String(l));
return k
},inherit:function(k,l){k.prototype=new l();
k.prototype.constructor=k
},method:function(){var k=arguments.length,o,n,l,m;
switch(k){case 2:o=arguments[0];
m=arguments[1];
for(n in m){l=m[n];
o[n]=l
}break;
case 3:o=arguments[0];
m=arguments[1];
l=arguments[2];
if(f.isFn(l)&&f.isStr(n)){o[n]=l
}break
}}};
var j={newDocument:function(l,k){if(!l){l=""
}if(!k){k=""
}if(document.implementation&&document.implementation.createDocument){return document.implementation.createDocument(k,l,null)
}else{var q=new ActiveXObject("MSXML2.DOMDocument");
if(l){var n="";
var m=l;
var o=l.indexOf(":");
if(o!==-1){n=l.substring(0,o);
m=l.substring(o+1)
}if(k){if(!n){n="a0"
}}else{n=""
}var r="<"+(n?(n+":"):"")+m+(k?(" xmlns:"+n+'="'+k+'"'):"")+"/>";
q.loadXML(r)
}return q
}},objToNode:function(p,q){var o,l,t;
if(f.isDef(p)&&f.isDef(q)){for(o in q){if(q.hasOwnProperty&&q.hasOwnProperty(o)){l=q[o];
if(o.indexOf("@")!==-1){p.setAttribute(o.replace("@",""),l)
}else{if(o==="$comments"){if(!!l.length){var r=0,s=l.length-1,m;
do{m=l[r];
p.appendChild(this.createComment(m))
}while(r++<s)
}}else{if(f.isNodeSet(l)){var k=0,u=l.length-1,v;
do{v=l[k];
if(!!v.ns){if(f.isDef(this.createElementNS)){t=p.appendChild(this.createElementNS(v.ns,o))
}else{t=p.appendChild(this.createNode(1,o,v.ns))
}}else{t=p.appendChild(this.createElement(o))
}if(!!v.Text){t.appendChild(f.isDef(v.hasCDATA)?this.createCDATASection(v.Text):this.createTextNode(v.Text))
}j.objToNode.call(this,t,v)
}while(k++<u)
}}}}}}}};
var e=function(k){this.parent=k||null
};
var i=function(k){this.nodeName=k||"";
this.ns=""
};
var b=function(){var l=null,k=null,m=null;
switch(arguments.length){case 1:k=arguments[0];
break;
case 2:l=arguments[0];
k=arguments[1];
break;
case 3:l=arguments[0];
k=arguments[1];
m=arguments[2];
break;
default:k="noname"
}i.apply(this,[k]);
e.apply(this,[l]);
this.Text=m||""
};
function a(){}f.inherit(a,Array);
f.method(a.prototype,{getNodesByAttribute:function(k,p){var l=[];
if(!!this.length&&f.isStr(k)&&f.isDef(p)){var q=this.length,o,m;
while(q--){o=this[q];
m=o[k];
if(f.isDef(m)&&m===p){l.unshift(o)
}}}return(!l.length)?null:l
},getNodesByValue:function(m){var k=[];
if(!!this.length&&f.isDef(m)){var o=this.length,l;
while(o--){l=this[o];
if(l.Text===m){k.unshift(l)
}}}return(!k.length)?null:k
},contains:function(k,p){var l=false;
if(!!this.length&&f.isStr(k)&&f.isDef(p)){var q=this.length,o,m;
while(q--){o=this[q];
m=o[k];
if(f.isDef(m)&&m===p){l=true;
break
}}}return l
},indexOf:function(){var l=-1,k,o;
if(!!this.length){var q=this.length,m,p;
switch(arguments.length){case 1:o=arguments[0];
while(q--){m=this[q];
p=m.val();
if(f.isDef(p)&&p===o){l=q;
break
}}break;
case 2:k=arguments[0];
o=arguments[1];
while(q--){m=this[q];
p=m[k];
if(f.isDef(p)&&p===o){l=q;
break
}}break
}}return l
},sortByAttribute:function(k,l){if(!!this.length&&f.isStr(k)){this.sort(function(n,m){var q=(n.attr(k)),o=(m.attr(k));
q=f.isNum(q)?parseFloat(q):q;
o=f.isNum(o)?parseFloat(o):o;
var p=(q<o)?-1:(o<q)?1:0;
p=(f.isDef(l)&&l.toLowerCase()==="desc")?(0-p):p;
return p
})
}},sortByValue:function(k){if(!!this.length){this.sort(function(m,l){var p=(m.Text),n=(l.Text);
p=f.isNum(p)?parseFloat(p):p;
n=f.isNum(n)?parseFloat(n):n;
var o=(p<n)?-1:(n<p)?1:0;
o=(f.isDef(k)&&k.toLowerCase()==="desc")?(0-o):o;
return o
})
}},sortByChildNode:function(l,k){if(!!this.length&&f.isStr(l)){this.sort(function(n,m){var q=n[l],o=m[l];
q=(f.isDef(q)&&!!q.length)?q[0].Text:null;
o=(f.isDef(o)&&!!o.length)?o[0].Text:null;
q=f.isNum(q)?parseFloat(q):q;
o=f.isNum(o)?parseFloat(o):o;
var p=(q<o)?-1:(o<q)?1:0;
p=(f.isDef(k)&&k.toLowerCase()==="desc")?(0-p):p;
return p
})
}},first:function(){var k=null;
if(!!this.length){k=this[0]
}return k
},last:function(){var k=null;
if(!!this.length){k=this[this.length-1]
}return k
}});
var h=(function(){var k={makeNodeSet:function(){var l=new a();
return l
},makeNode:function(m,o){var l=o.localName||o.baseName;
l=f.formatName(l);
var n=new b(m,l);
n.ns=o.prefix||"";
this.setAttributes(n,o);
return n
},setAttributes:function(o,l){if(f.isDef(l)&&!!l.attributes.length){var m=l.attributes.length-1,p=null,n=null;
do{p="@"+l.attributes[m].name;
o.attr(p,l.attributes[m].value)
}while(m--)
}},run:function(q,m){var l,p;
if(f.isDef(q)&&f.isDef(m)){if(m.hasChildNodes()){var o=m.childNodes.length-1,s=0;
do{l=m.childNodes[s];
switch(l.nodeType){case 1:p=k.makeNode(q,l);
if(f.isFn(this.decorator)){var r=this.decorator.call(p);
if(r===false){continue
}}if(!!p){q.appendChild(p);
if(l.hasChildNodes()){k.run.apply(this,[p,l])
}}break;
case 3:if(!q.Text){q.val(f.trim(l.nodeValue))
}else{q.Text+=f.trim(l.nodeValue)
}break;
case 4:q.hasCDATA=true;
q.val(f.isDef(l.text)?f.trim(l.text):f.trim(l.nodeValue));
break;
case 8:if(!f.isDef(this.noComments)||!this.noComments){if(!f.isDef(q.$comments)){q.$comments=[]
}q.$comments.push(f.trim(l.nodeValue))
}break
}}while(s++<o)
}}},init:function(l,n){n=n||{noComments:true};
if(f.isStr(l)){l=this.textToXML(l)
}else{if(f.isXNode(l)){l=l
}else{l=null
}}if(!l){return null
}var o=(l.nodeType===9)?l.documentElement:(l.nodeType===11)?l.firstChild:l;
var m=new i(o.nodeName);
if(f.isFn(n.decorator)){n.decorator.call(m)
}if(l.nodeType===3||l.nodeType===4){return l.nodeValue
}this.run.apply(n,[m,o]);
this.setAttributes(m,o);
return m
},textToXML:function(p){var m=null;
try{m=(f.isIE())?new ActiveXObject("MSXML2.DOMDocument"):new DOMParser();
m.async=false
}catch(n){throw new Error("XML Parser could not be instantiated")
}var l=null,o=true;
if(f.isIE()){o=m.loadXML(p);
l=(o)?m:false
}else{l=m.parseFromString(p,"text/xml");
o=(l.documentElement.tagName!=="parsererror")
}if(!o){throw new Error("Error parsing XML string")
}return l
}};
return k
})();
i.prototype={typeOf:"xmlObjectifier",attr:function(){var l,k,m;
if(!!arguments.length){switch(arguments.length){case 1:k=f.formatName(arguments[0]);
m=this["@"+k]||this[k];
if(f.isDef(m)&&!f.isArr(m)){l=m
}break;
case 2:k=f.formatName(arguments[0]);
m=arguments[1];
if(f.isStr(k)){this[(/^@/.test(k))?k:"@"+k]=m;
l=this
}break
}}return l
},find:function(m){var r=null,y,o,n;
if(f.isStr(m)){var B,s;
var u=/\[(\d+|@\w+(=\w+)?)\]/,z,q,w,v;
var k=/(?=\.)?([A-Za-z\-]+(\[(\d+|@\w+(=\w+)?)\])?)/g;
var l=/^@\w+/,x;
if(m.match(l)){x=m.match(l)[0];
return this.attr(x)
}y=m.match(k);
if(!!y.length){var A=0,p=y.length-1;
do{o=y[A];
n=o.match(/[A-Za-z\-]+/)[0];
B=!!B?(f.isArr(B)?B[0]:B)[n]:this[n];
if(!B){break
}z=!!o.match(u)&&o.match(u)[0].replace("[","").replace("]","");
if(z&&!!z.length){if(z.indexOf("=")!==-1){q=z.split("=");
w=f.trim(q[0]);
v=f.trim(q[1]);
if(w.indexOf("@")!==-1){x=w;
B=B.getNodesByAttribute(x,v)
}else{s=f.trim(B[w]);
if(s){B=s.getNodesByValue(v)
}}}else{if(f.isNum(z)){B=B[~~+z]
}}}}while(A++<p);
r=B
}}return r
},addComment:function(k){if(f.isStr(k)){if(!f.isDef(this.$comments)){this.$comments=[]
}this.$comments.push(k)
}return this
},val:function(k){var l=this;
if(f.isDef(k)){this.Text=k
}else{l=this.Text
}return l
},toXML:function(){var k=j.newDocument(this.nodeName,this.ns);
j.objToNode.call(k,k.documentElement,this);
return k
},toString:function(){var k=this.toXML();
var l="";
if(f.isDef(k.xml)){l=k.xml
}else{if(f.isDef(j)){var m=new window.XMLSerializer();
l=m.serializeToString(k)
}}return l
},appendChild:function(k){if(f.isDef(k)&&k instanceof b){k.parent=this;
if(!f.isDef(this[k.nodeName])){this[k.nodeName]=h.makeNodeSet()
}this[k.nodeName].push(k)
}}};
b.prototype={attr:i.prototype.attr,val:i.prototype.val,find:i.prototype.find,addComment:i.prototype.addComment,appendChild:i.prototype.appendChild};
var g={xmlToJSON:function(k,l){return h.init(k,l)
},textToXML:h.textToXML,xmlObjectifier:{RootClass:i,NodeClass:b,NodeSetClass:a}};
if(f.isDef(d)){d.extend(g)
}else{window.XMLObjectifier=g
}})((typeof(jQuery)!=="undefined")&&jQuery||undefined);