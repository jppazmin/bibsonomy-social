var Prototype={Version:"1.5.0_rc2",BrowserFeatures:{XPath:!!document.evaluate},ScriptFragment:"(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)",emptyFunction:function(){},K:function(a){return a
}};
var Class={create:function(){return function(){this.initialize.apply(this,arguments)
}
}};
var Abstract=new Object();
Object.extend=function(a,c){for(var b in c){a[b]=c[b]
}return a
};
Object.extend(Object,{inspect:function(a){try{if(a===undefined){return"undefined"
}if(a===null){return"null"
}return a.inspect?a.inspect():a.toString()
}catch(b){if(b instanceof RangeError){return"..."
}throw b
}},keys:function(a){var b=[];
for(var c in a){b.push(c)
}return b
},values:function(b){var a=[];
for(var c in b){a.push(b[c])
}return a
},clone:function(a){return Object.extend({},a)
}});
Function.prototype.bind=function(){var a=this,c=$A(arguments),b=c.shift();
return function(){return a.apply(b,c.concat($A(arguments)))
}
};
Function.prototype.bindAsEventListener=function(c){var a=this,b=$A(arguments),c=b.shift();
return function(d){return a.apply(c,[(d||window.event)].concat(b).concat($A(arguments)))
}
};
Object.extend(Number.prototype,{toColorPart:function(){var a=this.toString(16);
if(this<16){return"0"+a
}return a
},succ:function(){return this+1
},times:function(a){$R(0,this,true).each(a);
return this
}});
var Try={these:function(){var c;
for(var b=0,d=arguments.length;
b<d;
b++){var a=arguments[b];
try{c=a();
break
}catch(f){}}return c
}};
var PeriodicalExecuter=Class.create();
PeriodicalExecuter.prototype={initialize:function(b,a){this.callback=b;
this.frequency=a;
this.currentlyExecuting=false;
this.registerCallback()
},registerCallback:function(){this.timer=setInterval(this.onTimerEvent.bind(this),this.frequency*1000)
},stop:function(){if(!this.timer){return
}clearInterval(this.timer);
this.timer=null
},onTimerEvent:function(){if(!this.currentlyExecuting){try{this.currentlyExecuting=true;
this.callback(this)
}finally{this.currentlyExecuting=false
}}}};
String.interpret=function(a){return a==null?"":String(a)
};
Object.extend(String.prototype,{gsub:function(e,c){var a="",d=this,b;
c=arguments.callee.prepareReplacement(c);
while(d.length>0){if(b=d.match(e)){a+=d.slice(0,b.index);
a+=String.interpret(c(b));
d=d.slice(b.index+b[0].length)
}else{a+=d,d=""
}}return a
},sub:function(c,a,b){a=this.gsub.prepareReplacement(a);
b=b===undefined?1:b;
return this.gsub(c,function(d){if(--b<0){return d[0]
}return a(d)
})
},scan:function(b,a){this.gsub(b,a);
return this
},truncate:function(b,a){b=b||30;
a=a===undefined?"...":a;
return this.length>b?this.slice(0,b-a.length)+a:this
},strip:function(){return this.replace(/^\s+/,"").replace(/\s+$/,"")
},stripTags:function(){return this.replace(/<\/?[^>]+>/gi,"")
},stripScripts:function(){return this.replace(new RegExp(Prototype.ScriptFragment,"img"),"")
},extractScripts:function(){var b=new RegExp(Prototype.ScriptFragment,"img");
var a=new RegExp(Prototype.ScriptFragment,"im");
return(this.match(b)||[]).map(function(c){return(c.match(a)||["",""])[1]
})
},evalScripts:function(){return this.extractScripts().map(function(script){return eval(script)
})
},escapeHTML:function(){var b=document.createElement("div");
var a=document.createTextNode(this);
b.appendChild(a);
return b.innerHTML
},unescapeHTML:function(){var a=document.createElement("div");
a.innerHTML=this.stripTags();
return a.childNodes[0]?(a.childNodes.length>1?$A(a.childNodes).inject("",function(b,c){return b+c.nodeValue
}):a.childNodes[0].nodeValue):""
},toQueryParams:function(b){var a=this.strip().match(/([^?#]*)(#.*)?$/);
if(!a){return{}
}return a[1].split(b||"&").inject({},function(e,f){if((f=f.split("="))[0]){var c=decodeURIComponent(f[0]);
var d=f[1]?decodeURIComponent(f[1]):undefined;
if(e[c]!==undefined){if(e[c].constructor!=Array){e[c]=[e[c]]
}if(d){e[c].push(d)
}}else{e[c]=d
}}return e
})
},toArray:function(){return this.split("")
},succ:function(){return this.slice(0,this.length-1)+String.fromCharCode(this.charCodeAt(this.length-1)+1)
},camelize:function(){var d=this.split("-"),a=d.length;
if(a==1){return d[0]
}var c=this.charAt(0)=="-"?d[0].charAt(0).toUpperCase()+d[0].substring(1):d[0];
for(var b=1;
b<a;
b++){c+=d[b].charAt(0).toUpperCase()+d[b].substring(1)
}return c
},capitalize:function(){return this.charAt(0).toUpperCase()+this.substring(1).toLowerCase()
},underscore:function(){return this.gsub(/::/,"/").gsub(/([A-Z]+)([A-Z][a-z])/,"#{1}_#{2}").gsub(/([a-z\d])([A-Z])/,"#{1}_#{2}").gsub(/-/,"_").toLowerCase()
},dasherize:function(){return this.gsub(/_/,"-")
},inspect:function(b){var a=this.replace(/\\/g,"\\\\");
if(b){return'"'+a.replace(/"/g,'\\"')+'"'
}else{return"'"+a.replace(/'/g,"\\'")+"'"
}}});
String.prototype.gsub.prepareReplacement=function(b){if(typeof b=="function"){return b
}var a=new Template(b);
return function(c){return a.evaluate(c)
}
};
String.prototype.parseQuery=String.prototype.toQueryParams;
var Template=Class.create();
Template.Pattern=/(^|.|\r|\n)(#\{(.*?)\})/;
Template.prototype={initialize:function(a,b){this.template=a.toString();
this.pattern=b||Template.Pattern
},evaluate:function(a){return this.template.gsub(this.pattern,function(b){var c=b[1];
if(c=="\\"){return b[2]
}return c+String.interpret(a[b[3]])
})
}};
var $break=new Object();
var $continue=new Object();
var Enumerable={each:function(b){var a=0;
try{this._each(function(d){try{b(d,a++)
}catch(f){if(f!=$continue){throw f
}}})
}catch(c){if(c!=$break){throw c
}}return this
},eachSlice:function(c,b){var a=-c,d=[],e=this.toArray();
while((a+=c)<e.length){d.push(e.slice(a,a+c))
}return d.map(b)
},all:function(b){var a=true;
this.each(function(d,c){a=a&&!!(b||Prototype.K)(d,c);
if(!a){throw $break
}});
return a
},any:function(b){var a=false;
this.each(function(d,c){if(a=!!(b||Prototype.K)(d,c)){throw $break
}});
return a
},collect:function(b){var a=[];
this.each(function(d,c){a.push((b||Prototype.K)(d,c))
});
return a
},detect:function(b){var a;
this.each(function(d,c){if(b(d,c)){a=d;
throw $break
}});
return a
},findAll:function(b){var a=[];
this.each(function(d,c){if(b(d,c)){a.push(d)
}});
return a
},grep:function(c,b){var a=[];
this.each(function(f,e){var d=f.toString();
if(d.match(c)){a.push((b||Prototype.K)(f,e))
}});
return a
},include:function(a){var b=false;
this.each(function(c){if(c==a){b=true;
throw $break
}});
return b
},inGroupsOf:function(b,a){a=a===undefined?null:a;
return this.eachSlice(b,function(c){while(c.length<b){c.push(a)
}return c
})
},inject:function(a,b){this.each(function(d,c){a=b(a,d,c)
});
return a
},invoke:function(b){var a=$A(arguments).slice(1);
return this.map(function(c){return c[b].apply(c,a)
})
},max:function(b){var a;
this.each(function(d,c){d=(b||Prototype.K)(d,c);
if(a==undefined||d>=a){a=d
}});
return a
},min:function(b){var a;
this.each(function(d,c){d=(b||Prototype.K)(d,c);
if(a==undefined||d<a){a=d
}});
return a
},partition:function(c){var b=[],a=[];
this.each(function(e,d){((c||Prototype.K)(e,d)?b:a).push(e)
});
return[b,a]
},pluck:function(b){var a=[];
this.each(function(d,c){a.push(d[b])
});
return a
},reject:function(b){var a=[];
this.each(function(d,c){if(!b(d,c)){a.push(d)
}});
return a
},sortBy:function(a){return this.map(function(c,b){return{value:c,criteria:a(c,b)}
}).sort(function(f,e){var d=f.criteria,c=e.criteria;
return d<c?-1:d>c?1:0
}).pluck("value")
},toArray:function(){return this.map()
},zip:function(){var b=Prototype.K,a=$A(arguments);
if(typeof a.last()=="function"){b=a.pop()
}var c=[this].concat(a).map($A);
return this.map(function(e,d){return b(c.pluck(d))
})
},size:function(){return this.toArray().length
},inspect:function(){return"#<Enumerable:"+this.toArray().inspect()+">"
}};
Object.extend(Enumerable,{map:Enumerable.collect,find:Enumerable.detect,select:Enumerable.findAll,member:Enumerable.include,entries:Enumerable.toArray});
var $A=Array.from=function(d){if(!d){return[]
}if(d.toArray){return d.toArray()
}else{var b=[];
for(var a=0,c=d.length;
a<c;
a++){b.push(d[a])
}return b
}};
Object.extend(Array.prototype,Enumerable);
if(!Array.prototype._reverse){Array.prototype._reverse=Array.prototype.reverse
}Object.extend(Array.prototype,{_each:function(b){for(var a=0,c=this.length;
a<c;
a++){b(this[a])
}},clear:function(){this.length=0;
return this
},first:function(){return this[0]
},last:function(){return this[this.length-1]
},compact:function(){return this.select(function(a){return a!=null
})
},flatten:function(){return this.inject([],function(b,a){return b.concat(a&&a.constructor==Array?a.flatten():[a])
})
},without:function(){var a=$A(arguments);
return this.select(function(b){return !a.include(b)
})
},indexOf:function(a){for(var b=0,c=this.length;
b<c;
b++){if(this[b]==a){return b
}}return -1
},reverse:function(a){return(a!==false?this:this.toArray())._reverse()
},reduce:function(){return this.length>1?this:this[0]
},uniq:function(){return this.inject([],function(b,a){return b.include(a)?b:b.concat([a])
})
},clone:function(){return[].concat(this)
},size:function(){return this.length
},inspect:function(){return"["+this.map(Object.inspect).join(", ")+"]"
}});
Array.prototype.toArray=Array.prototype.clone;
function $w(a){a=a.strip();
return a?a.split(/\s+/):[]
}if(window.opera){Array.prototype.concat=function(){var e=[];
for(var b=0,c=this.length;
b<c;
b++){e.push(this[b])
}for(var b=0,c=arguments.length;
b<c;
b++){if(arguments[b].constructor==Array){for(var a=0,d=arguments[b].length;
a<d;
a++){e.push(arguments[b][a])
}}else{e.push(arguments[b])
}}return e
}
}var Hash={_each:function(b){for(var a in this){var c=this[a];
if(typeof c=="function"){continue
}var d=[a,c];
d.key=a;
d.value=c;
b(d)
}},keys:function(){return this.pluck("key")
},values:function(){return this.pluck("value")
},merge:function(a){return $H(a).inject(this,function(b,c){b[c.key]=c.value;
return b
})
},toQueryString:function(){return this.map(function(b){if(!b.key){return null
}if(b.value&&b.value.constructor==Array){b.value=b.value.compact();
if(b.value.length<2){b.value=b.value.reduce()
}else{var a=encodeURIComponent(b.key);
return b.value.map(function(c){return a+"="+encodeURIComponent(c)
}).join("&")
}}if(b.value==undefined){b[1]=""
}return b.map(encodeURIComponent).join("=")
}).join("&")
},inspect:function(){return"#<Hash:{"+this.map(function(a){return a.map(Object.inspect).join(": ")
}).join(", ")+"}>"
}};
function $H(a){var b=Object.extend({},a||{});
Object.extend(b,Enumerable);
Object.extend(b,Hash);
return b
}ObjectRange=Class.create();
Object.extend(ObjectRange.prototype,Enumerable);
Object.extend(ObjectRange.prototype,{initialize:function(c,a,b){this.start=c;
this.end=a;
this.exclusive=b
},_each:function(a){var b=this.start;
while(this.include(b)){a(b);
b=b.succ()
}},include:function(a){if(a<this.start){return false
}if(this.exclusive){return a<this.end
}return a<=this.end
}});
var $R=function(c,a,b){return new ObjectRange(c,a,b)
};
var Ajax={getTransport:function(){return Try.these(function(){return new XMLHttpRequest()
},function(){return new ActiveXObject("Msxml2.XMLHTTP")
},function(){return new ActiveXObject("Microsoft.XMLHTTP")
})||false
},activeRequestCount:0};
Ajax.Responders={responders:[],_each:function(a){this.responders._each(a)
},register:function(a){if(!this.include(a)){this.responders.push(a)
}},unregister:function(a){this.responders=this.responders.without(a)
},dispatch:function(d,b,c,a){this.each(function(f){if(typeof f[d]=="function"){try{f[d].apply(f,[b,c,a])
}catch(g){}}})
}};
Object.extend(Ajax.Responders,Enumerable);
Ajax.Responders.register({onCreate:function(){Ajax.activeRequestCount++
},onComplete:function(){Ajax.activeRequestCount--
}});
Ajax.Base=function(){};
Ajax.Base.prototype={setOptions:function(a){this.options={method:"post",asynchronous:true,contentType:"application/x-www-form-urlencoded",encoding:"UTF-8",parameters:""};
Object.extend(this.options,a||{});
this.options.method=this.options.method.toLowerCase();
this.options.parameters=$H(typeof this.options.parameters=="string"?this.options.parameters.toQueryParams():this.options.parameters)
}};
Ajax.Request=Class.create();
Ajax.Request.Events=["Uninitialized","Loading","Loaded","Interactive","Complete"];
Ajax.Request.prototype=Object.extend(new Ajax.Base(),{_complete:false,initialize:function(b,a){this.transport=Ajax.getTransport();
this.setOptions(a);
this.request(b)
},request:function(b){var d=this.options.parameters;
if(d.any()){d["_"]=""
}if(!["get","post"].include(this.options.method)){d["_method"]=this.options.method;
this.options.method="post"
}this.url=b;
if(this.options.method=="get"&&d.any()){this.url+=(this.url.indexOf("?")>=0?"&":"?")+d.toQueryString()
}try{Ajax.Responders.dispatch("onCreate",this,this.transport);
this.transport.open(this.options.method.toUpperCase(),this.url,this.options.asynchronous);
if(this.options.asynchronous){setTimeout(function(){this.respondToReadyState(1)
}.bind(this),10)
}this.transport.onreadystatechange=this.onStateChange.bind(this);
this.setRequestHeaders();
var a=this.options.method=="post"?(this.options.postBody||d.toQueryString()):null;
this.transport.send(a);
if(!this.options.asynchronous&&this.transport.overrideMimeType){this.onStateChange()
}}catch(c){this.dispatchException(c)
}},onStateChange:function(){var a=this.transport.readyState;
if(a>1&&!((a==4)&&this._complete)){this.respondToReadyState(this.transport.readyState)
}},setRequestHeaders:function(){var e={"X-Requested-With":"XMLHttpRequest","X-Prototype-Version":Prototype.Version,"Accept":"text/javascript, text/html, application/xml, text/xml, */*"};
if(this.options.method=="post"){e["Content-type"]=this.options.contentType+(this.options.encoding?"; charset="+this.options.encoding:"");
if(this.transport.overrideMimeType&&(navigator.userAgent.match(/Gecko\/(\d{4})/)||[0,2005])[1]<2005){e["Connection"]="close"
}}if(typeof this.options.requestHeaders=="object"){var c=this.options.requestHeaders;
if(typeof c.push=="function"){for(var b=0,d=c.length;
b<d;
b+=2){e[c[b]]=c[b+1]
}}else{$H(c).each(function(f){e[f.key]=f.value
})
}}for(var a in e){this.transport.setRequestHeader(a,e[a])
}},success:function(){return !this.transport.status||(this.transport.status>=200&&this.transport.status<300)
},respondToReadyState:function(a){var c=Ajax.Request.Events[a];
var f=this.transport,b=this.evalJSON();
if(c=="Complete"){try{this._complete=true;
(this.options["on"+this.transport.status]||this.options["on"+(this.success()?"Success":"Failure")]||Prototype.emptyFunction)(f,b)
}catch(d){this.dispatchException(d)
}}try{(this.options["on"+c]||Prototype.emptyFunction)(f,b);
Ajax.Responders.dispatch("on"+c,this,f,b)
}catch(d){this.dispatchException(d)
}if(c=="Complete"){if((this.getHeader("Content-type")||"").strip().match(/^(text|application)\/(x-)?(java|ecma)script(;.*)?$/i)){this.evalResponse()
}this.transport.onreadystatechange=Prototype.emptyFunction
}},getHeader:function(a){try{return this.transport.getResponseHeader(a)
}catch(b){return null
}},evalJSON:function(){try{var json=this.getHeader("X-JSON");
return json?eval("("+json+")"):null
}catch(e){return null
}},evalResponse:function(){try{return eval(this.transport.responseText)
}catch(e){this.dispatchException(e)
}},dispatchException:function(a){(this.options.onException||Prototype.emptyFunction)(this,a);
Ajax.Responders.dispatch("onException",this,a)
}});
Ajax.Updater=Class.create();
Object.extend(Object.extend(Ajax.Updater.prototype,Ajax.Request.prototype),{initialize:function(a,c,b){this.container={success:(a.success||a),failure:(a.failure||(a.success?null:a))};
this.transport=Ajax.getTransport();
this.setOptions(b);
var d=this.options.onComplete||Prototype.emptyFunction;
this.options.onComplete=(function(f,e){this.updateContent();
d(f,e)
}).bind(this);
this.request(c)
},updateContent:function(){var b=this.container[this.success()?"success":"failure"];
var a=this.transport.responseText;
if(!this.options.evalScripts){a=a.stripScripts()
}if(b=$(b)){if(this.options.insertion){new this.options.insertion(b,a)
}else{b.update(a)
}}if(this.success()){if(this.onComplete){setTimeout(this.onComplete.bind(this),10)
}}}});
Ajax.PeriodicalUpdater=Class.create();
Ajax.PeriodicalUpdater.prototype=Object.extend(new Ajax.Base(),{initialize:function(a,c,b){this.setOptions(b);
this.onComplete=this.options.onComplete;
this.frequency=(this.options.frequency||2);
this.decay=(this.options.decay||1);
this.updater={};
this.container=a;
this.url=c;
this.start()
},start:function(){this.options.onComplete=this.updateComplete.bind(this);
this.onTimerEvent()
},stop:function(){this.updater.options.onComplete=undefined;
clearTimeout(this.timer);
(this.onComplete||Prototype.emptyFunction).apply(this,arguments)
},updateComplete:function(a){if(this.options.decay){this.decay=(a.responseText==this.lastText?this.decay*this.options.decay:1);
this.lastText=a.responseText
}this.timer=setTimeout(this.onTimerEvent.bind(this),this.decay*this.frequency*1000)
},onTimerEvent:function(){this.updater=new Ajax.Updater(this.container,this.url,this.options)
}});
function $(b){if(arguments.length>1){for(var a=0,d=[],c=arguments.length;
a<c;
a++){d.push($(arguments[a]))
}return d
}if(typeof b=="string"){b=document.getElementById(b)
}return Element.extend(b)
}if(Prototype.BrowserFeatures.XPath){document._getElementsByXPath=function(f,a){var c=[];
var e=document.evaluate(f,$(a)||document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
for(var b=0,d=e.snapshotLength;
b<d;
b++){c.push(e.snapshotItem(b))
}return c
}
}document.getElementsByClassName=function(d,a){if(Prototype.BrowserFeatures.XPath){var g=".//*[contains(concat(' ', @class, ' '), ' "+d+" ')]";
return document._getElementsByXPath(g,a)
}else{var c=($(a)||document.body).getElementsByTagName("*");
var f=[],h;
for(var b=0,e=c.length;
b<e;
b++){h=c[b];
if(Element.hasClassName(h,d)){f.push(Element.extend(h))
}}return f
}};
if(!window.Element){var Element=new Object()
}Element.extend=function(c){if(!c||_nativeExtensions||c.nodeType==3){return c
}if(!c._extended&&c.tagName&&c!=window){var b=Object.clone(Element.Methods),a=Element.extend.cache;
if(c.tagName=="FORM"){Object.extend(b,Form.Methods)
}if(["INPUT","TEXTAREA","SELECT"].include(c.tagName)){Object.extend(b,Form.Element.Methods)
}Object.extend(b,Element.Methods.Simulated);
for(var e in b){var d=b[e];
if(typeof d=="function"&&!(e in c)){c[e]=a.findOrStore(d)
}}}c._extended=true;
return c
};
Element.extend.cache={findOrStore:function(a){return this[a]=this[a]||function(){return a.apply(null,[this].concat($A(arguments)))
}
}};
Element.Methods={visible:function(a){return $(a).style.display!="none"
},toggle:function(a){a=$(a);
Element[Element.visible(a)?"hide":"show"](a);
return a
},hide:function(a){$(a).style.display="none";
return a
},show:function(a){$(a).style.display="";
return a
},remove:function(a){a=$(a);
a.parentNode.removeChild(a);
return a
},update:function(b,a){a=typeof a=="undefined"?"":a.toString();
$(b).innerHTML=a.stripScripts();
setTimeout(function(){a.evalScripts()
},10);
return b
},replace:function(c,b){c=$(c);
if(c.outerHTML){c.outerHTML=b.stripScripts()
}else{var a=c.ownerDocument.createRange();
a.selectNodeContents(c);
c.parentNode.replaceChild(a.createContextualFragment(b.stripScripts()),c)
}setTimeout(function(){b.evalScripts()
},10);
return c
},inspect:function(b){b=$(b);
var a="<"+b.tagName.toLowerCase();
$H({"id":"id","className":"class"}).each(function(f){var e=f.first(),c=f.last();
var d=(b[e]||"").toString();
if(d){a+=" "+c+"="+d.inspect(true)
}});
return a+">"
},recursivelyCollect:function(a,c){a=$(a);
var b=[];
while(a=a[c]){if(a.nodeType==1){b.push(Element.extend(a))
}}return b
},ancestors:function(a){return $(a).recursivelyCollect("parentNode")
},descendants:function(a){return $A($(a).getElementsByTagName("*"))
},immediateDescendants:function(a){if(!(a=$(a).firstChild)){return[]
}while(a&&a.nodeType!=1){a=a.nextSibling
}if(a){return[a].concat($(a).nextSiblings())
}return[]
},previousSiblings:function(a){return $(a).recursivelyCollect("previousSibling")
},nextSiblings:function(a){return $(a).recursivelyCollect("nextSibling")
},siblings:function(a){a=$(a);
return a.previousSiblings().reverse().concat(a.nextSiblings())
},match:function(b,a){if(typeof a=="string"){a=new Selector(a)
}return a.match($(b))
},up:function(b,c,a){return Selector.findElement($(b).ancestors(),c,a)
},down:function(b,c,a){return Selector.findElement($(b).descendants(),c,a)
},previous:function(b,c,a){return Selector.findElement($(b).previousSiblings(),c,a)
},next:function(b,c,a){return Selector.findElement($(b).nextSiblings(),c,a)
},getElementsBySelector:function(){var a=$A(arguments),b=$(a.shift());
return Selector.findChildElements(b,a)
},getElementsByClassName:function(a,b){return document.getElementsByClassName(b,a)
},readAttribute:function(b,a){return $(b).getAttribute(a)
},getHeight:function(a){return $(a).offsetHeight
},classNames:function(a){return new Element.ClassNames(a)
},hasClassName:function(a,b){if(!(a=$(a))){return
}var c=a.className;
if(c.length==0){return false
}if(c==b||c.match(new RegExp("(^|\\s)"+b+"(\\s|$)"))){return true
}return false
},addClassName:function(a,b){if(!(a=$(a))){return
}Element.classNames(a).add(b);
return a
},removeClassName:function(a,b){if(!(a=$(a))){return
}Element.classNames(a).remove(b);
return a
},toggleClassName:function(a,b){if(!(a=$(a))){return
}Element.classNames(a)[a.hasClassName(b)?"remove":"add"](b);
return a
},observe:function(){Event.observe.apply(Event,arguments);
return $A(arguments).first()
},stopObserving:function(){Event.stopObserving.apply(Event,arguments);
return $A(arguments).first()
},cleanWhitespace:function(b){b=$(b);
var c=b.firstChild;
while(c){var a=c.nextSibling;
if(c.nodeType==3&&!/\S/.test(c.nodeValue)){b.removeChild(c)
}c=a
}return b
},empty:function(a){return $(a).innerHTML.match(/^\s*$/)
},childOf:function(b,a){b=$(b),a=$(a);
while(b=b.parentNode){if(b==a){return true
}}return false
},scrollTo:function(a){a=$(a);
var b=Position.cumulativeOffset(a);
window.scrollTo(b[0],b[1]);
return a
},getStyle:function(b,c){b=$(b);
var e=(c=="float"?(typeof b.style.styleFloat!="undefined"?"styleFloat":"cssFloat"):c).camelize();
var d=b.style[e];
if(!d){if(document.defaultView&&document.defaultView.getComputedStyle){var a=document.defaultView.getComputedStyle(b,null);
d=a?a[e]:null
}else{if(b.currentStyle){d=b.currentStyle[e]
}}}if((d=="auto")&&["width","height"].include(c)&&(b.getStyle("display")!="none")){d=b["offset"+c.capitalize()]+"px"
}if(window.opera&&["left","top","right","bottom"].include(c)){if(Element.getStyle(b,"position")=="static"){d="auto"
}}if(c=="opacity"){if(d){return parseFloat(d)
}if(d=(b.getStyle("filter")||"").match(/alpha\(opacity=(.*)\)/)){if(d[1]){return parseFloat(d[1])/100
}}return 1
}return d=="auto"?null:d
},setStyle:function(b,c){b=$(b);
for(var a in c){var d=c[a];
if(a=="opacity"){if(d==1){d=(/Gecko/.test(navigator.userAgent)&&!/Konqueror|Safari|KHTML/.test(navigator.userAgent))?0.999999:1;
if(/MSIE/.test(navigator.userAgent)&&!window.opera){b.style.filter=b.getStyle("filter").replace(/alpha\([^\)]*\)/gi,"")
}}else{if(d<0.00001){d=0
}if(/MSIE/.test(navigator.userAgent)&&!window.opera){b.style.filter=b.getStyle("filter").replace(/alpha\([^\)]*\)/gi,"")+"alpha(opacity="+d*100+")"
}}}else{if(a=="float"){a=(typeof b.style.styleFloat!="undefined")?"styleFloat":"cssFloat"
}}b.style[a.camelize()]=d
}return b
},getDimensions:function(b){b=$(b);
if(Element.getStyle(b,"display")!="none"){return{width:b.offsetWidth,height:b.offsetHeight}
}var a=b.style;
var e=a.visibility;
var c=a.position;
a.visibility="hidden";
a.position="absolute";
a.display="";
var f=b.clientWidth;
var d=b.clientHeight;
a.display="none";
a.position=c;
a.visibility=e;
return{width:f,height:d}
},makePositioned:function(a){a=$(a);
var b=Element.getStyle(a,"position");
if(b=="static"||!b){a._madePositioned=true;
a.style.position="relative";
if(window.opera){a.style.top=0;
a.style.left=0
}}return a
},undoPositioned:function(a){a=$(a);
if(a._madePositioned){a._madePositioned=undefined;
a.style.position=a.style.top=a.style.left=a.style.bottom=a.style.right=""
}return a
},makeClipping:function(a){a=$(a);
if(a._overflow){return a
}a._overflow=a.style.overflow||"auto";
if((Element.getStyle(a,"overflow")||"visible")!="hidden"){a.style.overflow="hidden"
}return a
},undoClipping:function(a){a=$(a);
if(!a._overflow){return a
}a.style.overflow=a._overflow=="auto"?"":a._overflow;
a._overflow=null;
return a
}};
Element.Methods.Simulated={hasAttribute:function(a,b){return $(a).getAttributeNode(b).specified
}};
if(document.all){Element.Methods.update=function(c,b){c=$(c);
b=typeof b=="undefined"?"":b.toString();
var a=c.tagName.toUpperCase();
if(["THEAD","TBODY","TR","TD"].include(a)){var d=document.createElement("div");
switch(a){case"THEAD":case"TBODY":d.innerHTML="<table><tbody>"+b.stripScripts()+"</tbody></table>";
depth=2;
break;
case"TR":d.innerHTML="<table><tbody><tr>"+b.stripScripts()+"</tr></tbody></table>";
depth=3;
break;
case"TD":d.innerHTML="<table><tbody><tr><td>"+b.stripScripts()+"</td></tr></tbody></table>";
depth=4
}$A(c.childNodes).each(function(e){c.removeChild(e)
});
depth.times(function(){d=d.firstChild
});
$A(d.childNodes).each(function(e){c.appendChild(e)
})
}else{c.innerHTML=b.stripScripts()
}setTimeout(function(){b.evalScripts()
},10);
return c
}
}Object.extend(Element,Element.Methods);
var _nativeExtensions=false;
if(/Konqueror|Safari|KHTML/.test(navigator.userAgent)){["","Form","Input","TextArea","Select"].each(function(b){var c="HTML"+b+"Element";
if(window[c]){return
}var a=window[c]={};
a.prototype=document.createElement(b?b.toLowerCase():"div").__proto__
})
}Element.addMethods=function(a){Object.extend(Element.Methods,a||{});
function b(f,d,c){c=c||false;
var e=Element.extend.cache;
for(var h in f){var g=f[h];
if(!c||!(h in d)){d[h]=e.findOrStore(g)
}}}if(typeof HTMLElement!="undefined"){b(Element.Methods,HTMLElement.prototype);
b(Element.Methods.Simulated,HTMLElement.prototype,true);
b(Form.Methods,HTMLFormElement.prototype);
[HTMLInputElement,HTMLTextAreaElement,HTMLSelectElement].each(function(c){b(Form.Element.Methods,c.prototype)
});
_nativeExtensions=true
}};
var Toggle=new Object();
Toggle.display=Element.toggle;
Abstract.Insertion=function(a){this.adjacency=a
};
Abstract.Insertion.prototype={initialize:function(b,c){this.element=$(b);
this.content=c.stripScripts();
if(this.adjacency&&this.element.insertAdjacentHTML){try{this.element.insertAdjacentHTML(this.adjacency,this.content)
}catch(d){var a=this.element.tagName.toUpperCase();
if(["TBODY","TR"].include(a)){this.insertContent(this.contentFromAnonymousTable())
}else{throw d
}}}else{this.range=this.element.ownerDocument.createRange();
if(this.initializeRange){this.initializeRange()
}this.insertContent([this.range.createContextualFragment(this.content)])
}setTimeout(function(){c.evalScripts()
},10)
},contentFromAnonymousTable:function(){var a=document.createElement("div");
a.innerHTML="<table><tbody>"+this.content+"</tbody></table>";
return $A(a.childNodes[0].childNodes[0].childNodes)
}};
var Insertion=new Object();
Insertion.Before=Class.create();
Insertion.Before.prototype=Object.extend(new Abstract.Insertion("beforeBegin"),{initializeRange:function(){this.range.setStartBefore(this.element)
},insertContent:function(a){a.each((function(b){this.element.parentNode.insertBefore(b,this.element)
}).bind(this))
}});
Insertion.Top=Class.create();
Insertion.Top.prototype=Object.extend(new Abstract.Insertion("afterBegin"),{initializeRange:function(){this.range.selectNodeContents(this.element);
this.range.collapse(true)
},insertContent:function(a){a.reverse(false).each((function(b){this.element.insertBefore(b,this.element.firstChild)
}).bind(this))
}});
Insertion.Bottom=Class.create();
Insertion.Bottom.prototype=Object.extend(new Abstract.Insertion("beforeEnd"),{initializeRange:function(){this.range.selectNodeContents(this.element);
this.range.collapse(this.element)
},insertContent:function(a){a.each((function(b){this.element.appendChild(b)
}).bind(this))
}});
Insertion.After=Class.create();
Insertion.After.prototype=Object.extend(new Abstract.Insertion("afterEnd"),{initializeRange:function(){this.range.setStartAfter(this.element)
},insertContent:function(a){a.each((function(b){this.element.parentNode.insertBefore(b,this.element.nextSibling)
}).bind(this))
}});
Element.ClassNames=Class.create();
Element.ClassNames.prototype={initialize:function(a){this.element=$(a)
},_each:function(a){this.element.className.split(/\s+/).select(function(b){return b.length>0
})._each(a)
},set:function(a){this.element.className=a
},add:function(a){if(this.include(a)){return
}this.set($A(this).concat(a).join(" "))
},remove:function(a){if(!this.include(a)){return
}this.set($A(this).without(a).join(" "))
},toString:function(){return $A(this).join(" ")
}};
Object.extend(Element.ClassNames.prototype,Enumerable);
var Selector=Class.create();
Selector.prototype={initialize:function(a){this.params={classNames:[]};
this.expression=a.toString().strip();
this.parseExpression();
this.compileMatcher()
},parseExpression:function(){function g(h){throw"Parse error in selector: "+h
}if(this.expression==""){g("empty expression")
}var f=this.params,e=this.expression,b,a,d,c;
while(b=e.match(/^(.*)\[([a-z0-9_:-]+?)(?:([~\|!]?=)(?:"([^"]*)"|([^\]\s]*)))?\]$/i)){f.attributes=f.attributes||[];
f.attributes.push({name:b[2],operator:b[3],value:b[4]||b[5]||""});
e=b[1]
}if(e=="*"){return this.params.wildcard=true
}while(b=e.match(/^([^a-z0-9_-])?([a-z0-9_-]+)(.*)/i)){a=b[1],d=b[2],c=b[3];
switch(a){case"#":f.id=d;
break;
case".":f.classNames.push(d);
break;
case"":case undefined:f.tagName=d.toUpperCase();
break;
default:g(e.inspect())
}e=c
}if(e.length>0){g(e.inspect())
}},buildMatchExpression:function(){var e=this.params,d=[],c;
if(e.wildcard){d.push("true")
}if(c=e.id){d.push("element.id == "+c.inspect())
}if(c=e.tagName){d.push("element.tagName.toUpperCase() == "+c.inspect())
}if((c=e.classNames).length>0){for(var a=0,b=c.length;
a<b;
a++){d.push("Element.hasClassName(element, "+c[a].inspect()+")")
}}if(c=e.attributes){c.each(function(g){var h="element.getAttribute("+g.name.inspect()+")";
var f=function(i){return h+" && "+h+".split("+i.inspect()+")"
};
switch(g.operator){case"=":d.push(h+" == "+g.value.inspect());
break;
case"~=":d.push(f(" ")+".include("+g.value.inspect()+")");
break;
case"|=":d.push(f("-")+".first().toUpperCase() == "+g.value.toUpperCase().inspect());
break;
case"!=":d.push(h+" != "+g.value.inspect());
break;
case"":case undefined:d.push(h+" != null");
break;
default:throw"Unknown operator "+g.operator+" in selector"
}})
}return d.join(" && ")
},compileMatcher:function(){this.match=new Function("element","if (!element.tagName) return false;       return "+this.buildMatchExpression())
},findElements:function(d){var c;
if(c=$(this.params.id)){if(this.match(c)){if(!d||Element.childOf(c,d)){return[c]
}}}d=(d||document).getElementsByTagName(this.params.tagName||"*");
var b=[];
for(var a=0,e=d.length;
a<e;
a++){if(this.match(c=d[a])){b.push(Element.extend(c))
}}return b
},toString:function(){return this.expression
}};
Object.extend(Selector,{matchElements:function(b,c){var a=new Selector(c);
return b.select(a.match.bind(a)).map(Element.extend)
},findElement:function(b,c,a){if(typeof c=="number"){a=c,c=false
}return Selector.matchElements(b,c||"*")[a||0]
},findChildElements:function(a,b){return b.map(function(c){return c.strip().split(/\s+/).inject([null],function(e,f){var d=new Selector(f);
return e.inject([],function(h,g){return h.concat(d.findElements(g||a))
})
})
}).flatten()
}});
function $$(){return Selector.findChildElements(document,$A(arguments))
}var Form={reset:function(a){$(a).reset();
return a
},serializeElements:function(a){return a.inject([],function(d,c){var b=Form.Element.serialize(c);
if(b){d.push(b)
}return d
}).join("&")
}};
Form.Methods={serialize:function(a){return Form.serializeElements($(a).getElements())
},getElements:function(a){return $A($(a).getElementsByTagName("*")).inject([],function(b,c){if(Form.Element.Serializers[c.tagName.toLowerCase()]){b.push(Element.extend(c))
}return b
})
},getInputs:function(g,c,d){g=$(g);
var a=g.getElementsByTagName("input"),h=[];
if(!c&&!d){return $A(a).map(Element.extend)
}for(var e=0,f=a.length;
e<f;
e++){var b=a[e];
if((c&&b.type!=c)||(d&&b.name!=d)){continue
}h.push(Element.extend(b))
}return h
},disable:function(a){a=$(a);
a.getElements().each(function(b){b.blur();
b.disabled="true"
});
return a
},enable:function(a){a=$(a);
a.getElements().each(function(b){b.disabled=""
});
return a
},findFirstElement:function(a){return $(a).getElements().find(function(b){return b.type!="hidden"&&!b.disabled&&["input","select","textarea"].include(b.tagName.toLowerCase())
})
},focusFirstElement:function(a){a=$(a);
a.findFirstElement().activate();
return a
}};
Object.extend(Form,Form.Methods);
Form.Element={focus:function(a){$(a).focus();
return a
},select:function(a){$(a).select();
return a
}};
Form.Element.Methods={serialize:function(b){b=$(b);
if(b.disabled){return""
}var d=b.tagName.toLowerCase();
var c=Form.Element.Serializers[d](b);
if(c){var a=encodeURIComponent(c[0]);
if(a.length==0){return
}if(c[1].constructor!=Array){c[1]=[c[1]]
}return c[1].map(function(e){return a+"="+encodeURIComponent(e)
}).join("&")
}},getValue:function(a){a=$(a);
var c=a.tagName.toLowerCase();
var b=Form.Element.Serializers[c](a);
if(b){return b[1]
}},clear:function(a){$(a).value="";
return a
},present:function(a){return $(a).value!=""
},activate:function(a){a=$(a);
a.focus();
if(a.select&&(a.tagName.toLowerCase()!="input"||!["button","reset","submit"].include(a.type))){a.select()
}return a
},disable:function(a){a=$(a);
a.disabled=true;
return a
},enable:function(a){a=$(a);
a.blur();
a.disabled=false;
return a
}};
Object.extend(Form.Element,Form.Element.Methods);
var Field=Form.Element;
Form.Element.Serializers={input:function(a){switch(a.type.toLowerCase()){case"checkbox":case"radio":return Form.Element.Serializers.inputSelector(a);
default:return Form.Element.Serializers.textarea(a)
}return false
},inputSelector:function(a){if(a.checked){return[a.name,a.value]
}},textarea:function(a){return[a.name,a.value]
},select:function(a){return Form.Element.Serializers[a.type=="select-one"?"selectOne":"selectMany"](a)
},selectOne:function(c){var d="",b,a=c.selectedIndex;
if(a>=0){b=Element.extend(c.options[a]);
d=b.hasAttribute("value")?b.value:b.text
}return[c.name,d]
},selectMany:function(c){var e=[];
for(var b=0,d=c.length;
b<d;
b++){var a=Element.extend(c.options[b]);
if(a.selected){e.push(a.hasAttribute("value")?a.value:a.text)
}}return[c.name,e]
}};
var $F=Form.Element.getValue;
Abstract.TimedObserver=function(){};
Abstract.TimedObserver.prototype={initialize:function(a,b,c){this.frequency=b;
this.element=$(a);
this.callback=c;
this.lastValue=this.getValue();
this.registerCallback()
},registerCallback:function(){setInterval(this.onTimerEvent.bind(this),this.frequency*1000)
},onTimerEvent:function(){var a=this.getValue();
var b=("string"==typeof this.lastValue&&"string"==typeof a?this.lastValue!=a:String(this.lastValue)!=String(a));
if(b){this.callback(this.element,a);
this.lastValue=a
}}};
Form.Element.Observer=Class.create();
Form.Element.Observer.prototype=Object.extend(new Abstract.TimedObserver(),{getValue:function(){return Form.Element.getValue(this.element)
}});
Form.Observer=Class.create();
Form.Observer.prototype=Object.extend(new Abstract.TimedObserver(),{getValue:function(){return Form.serialize(this.element)
}});
Abstract.EventObserver=function(){};
Abstract.EventObserver.prototype={initialize:function(a,b){this.element=$(a);
this.callback=b;
this.lastValue=this.getValue();
if(this.element.tagName.toLowerCase()=="form"){this.registerFormCallbacks()
}else{this.registerCallback(this.element)
}},onElementEvent:function(){var a=this.getValue();
if(this.lastValue!=a){this.callback(this.element,a);
this.lastValue=a
}},registerFormCallbacks:function(){Form.getElements(this.element).each(this.registerCallback.bind(this))
},registerCallback:function(a){if(a.type){switch(a.type.toLowerCase()){case"checkbox":case"radio":Event.observe(a,"click",this.onElementEvent.bind(this));
break;
default:Event.observe(a,"change",this.onElementEvent.bind(this));
break
}}}};
Form.Element.EventObserver=Class.create();
Form.Element.EventObserver.prototype=Object.extend(new Abstract.EventObserver(),{getValue:function(){return Form.Element.getValue(this.element)
}});
Form.EventObserver=Class.create();
Form.EventObserver.prototype=Object.extend(new Abstract.EventObserver(),{getValue:function(){return Form.serialize(this.element)
}});
if(!window.Event){var Event=new Object()
}Object.extend(Event,{KEY_BACKSPACE:8,KEY_TAB:9,KEY_RETURN:13,KEY_ESC:27,KEY_LEFT:37,KEY_UP:38,KEY_RIGHT:39,KEY_DOWN:40,KEY_DELETE:46,KEY_HOME:36,KEY_END:35,KEY_PAGEUP:33,KEY_PAGEDOWN:34,element:function(a){return a.target||a.srcElement
},isLeftClick:function(a){return(((a.which)&&(a.which==1))||((a.button)&&(a.button==1)))
},pointerX:function(a){return a.pageX||(a.clientX+(document.documentElement.scrollLeft||document.body.scrollLeft))
},pointerY:function(a){return a.pageY||(a.clientY+(document.documentElement.scrollTop||document.body.scrollTop))
},stop:function(a){if(a.preventDefault){a.preventDefault();
a.stopPropagation()
}else{a.returnValue=false;
a.cancelBubble=true
}},findElement:function(c,b){var a=Event.element(c);
while(a.parentNode&&(!a.tagName||(a.tagName.toUpperCase()!=b.toUpperCase()))){a=a.parentNode
}return a
},observers:false,_observeAndCache:function(d,c,b,a){if(!this.observers){this.observers=[]
}if(d.addEventListener){this.observers.push([d,c,b,a]);
d.addEventListener(c,b,a)
}else{if(d.attachEvent){this.observers.push([d,c,b,a]);
d.attachEvent("on"+c,b)
}}},unloadCache:function(){if(!Event.observers){return
}for(var a=0,b=Event.observers.length;
a<b;
a++){Event.stopObserving.apply(this,Event.observers[a]);
Event.observers[a][0]=null
}Event.observers=false
},observe:function(d,c,b,a){d=$(d);
a=a||false;
if(c=="keypress"&&(navigator.appVersion.match(/Konqueror|Safari|KHTML/)||d.attachEvent)){c="keydown"
}Event._observeAndCache(d,c,b,a)
},stopObserving:function(d,c,b,a){d=$(d);
a=a||false;
if(c=="keypress"&&(navigator.appVersion.match(/Konqueror|Safari|KHTML/)||d.detachEvent)){c="keydown"
}if(d.removeEventListener){d.removeEventListener(c,b,a)
}else{if(d.detachEvent){try{d.detachEvent("on"+c,b)
}catch(f){}}}}});
if(navigator.appVersion.match(/\bMSIE\b/)){Event.observe(window,"unload",Event.unloadCache,false)
}var Position={includeScrollOffsets:false,prepare:function(){this.deltaX=window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0;
this.deltaY=window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0
},realOffset:function(b){var a=0,c=0;
do{a+=b.scrollTop||0;
c+=b.scrollLeft||0;
b=b.parentNode
}while(b);
return[c,a]
},cumulativeOffset:function(b){var a=0,c=0;
do{a+=b.offsetTop||0;
c+=b.offsetLeft||0;
b=b.offsetParent
}while(b);
return[c,a]
},positionedOffset:function(b){var a=0,d=0;
do{a+=b.offsetTop||0;
d+=b.offsetLeft||0;
b=b.offsetParent;
if(b){if(b.tagName=="BODY"){break
}var c=Element.getStyle(b,"position");
if(c=="relative"||c=="absolute"){break
}}}while(b);
return[d,a]
},offsetParent:function(a){if(a.offsetParent){return a.offsetParent
}if(a==document.body){return a
}while((a=a.parentNode)&&a!=document.body){if(Element.getStyle(a,"position")!="static"){return a
}}return document.body
},within:function(b,a,c){if(this.includeScrollOffsets){return this.withinIncludingScrolloffsets(b,a,c)
}this.xcomp=a;
this.ycomp=c;
this.offset=this.cumulativeOffset(b);
return(c>=this.offset[1]&&c<this.offset[1]+b.offsetHeight&&a>=this.offset[0]&&a<this.offset[0]+b.offsetWidth)
},withinIncludingScrolloffsets:function(b,a,d){var c=this.realOffset(b);
this.xcomp=a+c[0]-this.deltaX;
this.ycomp=d+c[1]-this.deltaY;
this.offset=this.cumulativeOffset(b);
return(this.ycomp>=this.offset[1]&&this.ycomp<this.offset[1]+b.offsetHeight&&this.xcomp>=this.offset[0]&&this.xcomp<this.offset[0]+b.offsetWidth)
},overlap:function(b,a){if(!b){return 0
}if(b=="vertical"){return((this.offset[1]+a.offsetHeight)-this.ycomp)/a.offsetHeight
}if(b=="horizontal"){return((this.offset[0]+a.offsetWidth)-this.xcomp)/a.offsetWidth
}},page:function(d){var a=0,c=0;
var b=d;
do{a+=b.offsetTop||0;
c+=b.offsetLeft||0;
if(b.offsetParent==document.body){if(Element.getStyle(b,"position")=="absolute"){break
}}}while(b=b.offsetParent);
b=d;
do{if(!window.opera||b.tagName=="BODY"){a-=b.scrollTop||0;
c-=b.scrollLeft||0
}}while(b=b.parentNode);
return[c,a]
},clone:function(c,e){var a=Object.extend({setLeft:true,setTop:true,setWidth:true,setHeight:true,offsetTop:0,offsetLeft:0},arguments[2]||{});
c=$(c);
var d=Position.page(c);
e=$(e);
var f=[0,0];
var b=null;
if(Element.getStyle(e,"position")=="absolute"){b=Position.offsetParent(e);
f=Position.page(b)
}if(b==document.body){f[0]-=document.body.offsetLeft;
f[1]-=document.body.offsetTop
}if(a.setLeft){e.style.left=(d[0]-f[0]+a.offsetLeft)+"px"
}if(a.setTop){e.style.top=(d[1]-f[1]+a.offsetTop)+"px"
}if(a.setWidth){e.style.width=c.offsetWidth+"px"
}if(a.setHeight){e.style.height=c.offsetHeight+"px"
}},absolutize:function(b){b=$(b);
if(b.style.position=="absolute"){return
}Position.prepare();
var d=Position.positionedOffset(b);
var f=d[1];
var e=d[0];
var c=b.clientWidth;
var a=b.clientHeight;
b._originalLeft=e-parseFloat(b.style.left||0);
b._originalTop=f-parseFloat(b.style.top||0);
b._originalWidth=b.style.width;
b._originalHeight=b.style.height;
b.style.position="absolute";
b.style.top=f+"px";
b.style.left=e+"px";
b.style.width=c+"px";
b.style.height=a+"px"
},relativize:function(a){a=$(a);
if(a.style.position=="relative"){return
}Position.prepare();
a.style.position="relative";
var c=parseFloat(a.style.top||0)-(a._originalTop||0);
var b=parseFloat(a.style.left||0)-(a._originalLeft||0);
a.style.top=c+"px";
a.style.left=b+"px";
a.style.height=a._originalHeight;
a.style.width=a._originalWidth
}};
if(/Konqueror|Safari|KHTML/.test(navigator.userAgent)){Position.cumulativeOffset=function(b){var a=0,c=0;
do{a+=b.offsetTop||0;
c+=b.offsetLeft||0;
if(b.offsetParent==document.body){if(Element.getStyle(b,"position")=="absolute"){break
}}b=b.offsetParent
}while(b);
return[c,a]
}
}Element.addMethods();