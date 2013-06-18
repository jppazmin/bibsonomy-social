(function(a0,I){var am=a0.document;
var b=(function(){var bo=function(bI,bJ){return new bo.fn.init(bI,bJ,bm)
},bD=a0.jQuery,bq=a0.$,bm,bH=/^(?:[^<]*(<[\w\W]+>)[^>]*$|#([\w\-]+)$)/,bw=/\S/,bs=/^\s+/,bn=/\s+$/,br=/\d/,bk=/^<(\w+)\s*\/?>(?:<\/\1>)?$/,bx=/^[\],:{}\s]*$/,bF=/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,bz=/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,bt=/(?:^|:|,)(?:\s*\[)+/g,bi=/(webkit)[ \/]([\w.]+)/,bB=/(opera)(?:.*version)?[ \/]([\w.]+)/,bA=/(msie) ([\w.]+)/,bC=/(mozilla)(?:.*? rv:([\w.]+))?/,bG=navigator.userAgent,bE,bl,e,bv=Object.prototype.toString,bp=Object.prototype.hasOwnProperty,bj=Array.prototype.push,bu=Array.prototype.slice,by=String.prototype.trim,bf=Array.prototype.indexOf,bh={};
bo.fn=bo.prototype={constructor:bo,init:function(bI,bM,bL){var bK,bN,bJ,bO;
if(!bI){return this
}if(bI.nodeType){this.context=this[0]=bI;
this.length=1;
return this
}if(bI==="body"&&!bM&&am.body){this.context=am;
this[0]=am.body;
this.selector="body";
this.length=1;
return this
}if(typeof bI==="string"){bK=bH.exec(bI);
if(bK&&(bK[1]||!bM)){if(bK[1]){bM=bM instanceof bo?bM[0]:bM;
bO=(bM?bM.ownerDocument||bM:am);
bJ=bk.exec(bI);
if(bJ){if(bo.isPlainObject(bM)){bI=[am.createElement(bJ[1])];
bo.fn.attr.call(bI,bM,true)
}else{bI=[bO.createElement(bJ[1])]
}}else{bJ=bo.buildFragment([bK[1]],[bO]);
bI=(bJ.cacheable?bo.clone(bJ.fragment):bJ.fragment).childNodes
}return bo.merge(this,bI)
}else{bN=am.getElementById(bK[2]);
if(bN&&bN.parentNode){if(bN.id!==bK[2]){return bL.find(bI)
}this.length=1;
this[0]=bN
}this.context=am;
this.selector=bI;
return this
}}else{if(!bM||bM.jquery){return(bM||bL).find(bI)
}else{return this.constructor(bM).find(bI)
}}}else{if(bo.isFunction(bI)){return bL.ready(bI)
}}if(bI.selector!==I){this.selector=bI.selector;
this.context=bI.context
}return bo.makeArray(bI,this)
},selector:"",jquery:"1.5.2",length:0,size:function(){return this.length
},toArray:function(){return bu.call(this,0)
},get:function(bI){return bI==null?this.toArray():(bI<0?this[this.length+bI]:this[bI])
},pushStack:function(bJ,bL,bI){var bK=this.constructor();
if(bo.isArray(bJ)){bj.apply(bK,bJ)
}else{bo.merge(bK,bJ)
}bK.prevObject=this;
bK.context=this.context;
if(bL==="find"){bK.selector=this.selector+(this.selector?" ":"")+bI
}else{if(bL){bK.selector=this.selector+"."+bL+"("+bI+")"
}}return bK
},each:function(bJ,bI){return bo.each(this,bJ,bI)
},ready:function(bI){bo.bindReady();
bl.done(bI);
return this
},eq:function(bI){return bI===-1?this.slice(bI):this.slice(bI,+bI+1)
},first:function(){return this.eq(0)
},last:function(){return this.eq(-1)
},slice:function(){return this.pushStack(bu.apply(this,arguments),"slice",bu.call(arguments).join(","))
},map:function(bI){return this.pushStack(bo.map(this,function(bK,bJ){return bI.call(bK,bJ,bK)
}))
},end:function(){return this.prevObject||this.constructor(null)
},push:bj,sort:[].sort,splice:[].splice};
bo.fn.init.prototype=bo.fn;
bo.extend=bo.fn.extend=function(){var bR,bK,bI,bJ,bO,bP,bN=arguments[0]||{},bM=1,bL=arguments.length,bQ=false;
if(typeof bN==="boolean"){bQ=bN;
bN=arguments[1]||{};
bM=2
}if(typeof bN!=="object"&&!bo.isFunction(bN)){bN={}
}if(bL===bM){bN=this;
--bM
}for(;
bM<bL;
bM++){if((bR=arguments[bM])!=null){for(bK in bR){bI=bN[bK];
bJ=bR[bK];
if(bN===bJ){continue
}if(bQ&&bJ&&(bo.isPlainObject(bJ)||(bO=bo.isArray(bJ)))){if(bO){bO=false;
bP=bI&&bo.isArray(bI)?bI:[]
}else{bP=bI&&bo.isPlainObject(bI)?bI:{}
}bN[bK]=bo.extend(bQ,bP,bJ)
}else{if(bJ!==I){bN[bK]=bJ
}}}}}return bN
};
bo.extend({noConflict:function(bI){a0.$=bq;
if(bI){a0.jQuery=bD
}return bo
},isReady:false,readyWait:1,ready:function(bI){if(bI===true){bo.readyWait--
}if(!bo.readyWait||(bI!==true&&!bo.isReady)){if(!am.body){return setTimeout(bo.ready,1)
}bo.isReady=true;
if(bI!==true&&--bo.readyWait>0){return
}bl.resolveWith(am,[bo]);
if(bo.fn.trigger){bo(am).trigger("ready").unbind("ready")
}}},bindReady:function(){if(bl){return
}bl=bo._Deferred();
if(am.readyState==="complete"){return setTimeout(bo.ready,1)
}if(am.addEventListener){am.addEventListener("DOMContentLoaded",e,false);
a0.addEventListener("load",bo.ready,false)
}else{if(am.attachEvent){am.attachEvent("onreadystatechange",e);
a0.attachEvent("onload",bo.ready);
var bI=false;
try{bI=a0.frameElement==null
}catch(bJ){}if(am.documentElement.doScroll&&bI){bg()
}}}},isFunction:function(bI){return bo.type(bI)==="function"
},isArray:Array.isArray||function(bI){return bo.type(bI)==="array"
},isWindow:function(bI){return bI&&typeof bI==="object"&&"setInterval" in bI
},isNaN:function(bI){return bI==null||!br.test(bI)||isNaN(bI)
},type:function(bI){return bI==null?String(bI):bh[bv.call(bI)]||"object"
},isPlainObject:function(bJ){if(!bJ||bo.type(bJ)!=="object"||bJ.nodeType||bo.isWindow(bJ)){return false
}if(bJ.constructor&&!bp.call(bJ,"constructor")&&!bp.call(bJ.constructor.prototype,"isPrototypeOf")){return false
}var bI;
for(bI in bJ){}return bI===I||bp.call(bJ,bI)
},isEmptyObject:function(bJ){for(var bI in bJ){return false
}return true
},error:function(bI){throw bI
},parseJSON:function(bI){if(typeof bI!=="string"||!bI){return null
}bI=bo.trim(bI);
if(bx.test(bI.replace(bF,"@").replace(bz,"]").replace(bt,""))){return a0.JSON&&a0.JSON.parse?a0.JSON.parse(bI):(new Function("return "+bI))()
}else{bo.error("Invalid JSON: "+bI)
}},parseXML:function(bK,bI,bJ){if(a0.DOMParser){bJ=new DOMParser();
bI=bJ.parseFromString(bK,"text/xml")
}else{bI=new ActiveXObject("Microsoft.XMLDOM");
bI.async="false";
bI.loadXML(bK)
}bJ=bI.documentElement;
if(!bJ||!bJ.nodeName||bJ.nodeName==="parsererror"){bo.error("Invalid XML: "+bK)
}return bI
},noop:function(){},globalEval:function(bK){if(bK&&bw.test(bK)){var bJ=am.head||am.getElementsByTagName("head")[0]||am.documentElement,bI=am.createElement("script");
if(bo.support.scriptEval()){bI.appendChild(am.createTextNode(bK))
}else{bI.text=bK
}bJ.insertBefore(bI,bJ.firstChild);
bJ.removeChild(bI)
}},nodeName:function(bJ,bI){return bJ.nodeName&&bJ.nodeName.toUpperCase()===bI.toUpperCase()
},each:function(bL,bP,bK){var bJ,bM=0,bN=bL.length,bI=bN===I||bo.isFunction(bL);
if(bK){if(bI){for(bJ in bL){if(bP.apply(bL[bJ],bK)===false){break
}}}else{for(;
bM<bN;
){if(bP.apply(bL[bM++],bK)===false){break
}}}}else{if(bI){for(bJ in bL){if(bP.call(bL[bJ],bJ,bL[bJ])===false){break
}}}else{for(var bO=bL[0];
bM<bN&&bP.call(bO,bM,bO)!==false;
bO=bL[++bM]){}}}return bL
},trim:by?function(bI){return bI==null?"":by.call(bI)
}:function(bI){return bI==null?"":bI.toString().replace(bs,"").replace(bn,"")
},makeArray:function(bL,bJ){var bI=bJ||[];
if(bL!=null){var bK=bo.type(bL);
if(bL.length==null||bK==="string"||bK==="function"||bK==="regexp"||bo.isWindow(bL)){bj.call(bI,bL)
}else{bo.merge(bI,bL)
}}return bI
},inArray:function(bK,bL){if(bL.indexOf){return bL.indexOf(bK)
}for(var bI=0,bJ=bL.length;
bI<bJ;
bI++){if(bL[bI]===bK){return bI
}}return -1
},merge:function(bM,bK){var bL=bM.length,bJ=0;
if(typeof bK.length==="number"){for(var bI=bK.length;
bJ<bI;
bJ++){bM[bL++]=bK[bJ]
}}else{while(bK[bJ]!==I){bM[bL++]=bK[bJ++]
}}bM.length=bL;
return bM
},grep:function(bJ,bO,bI){var bK=[],bN;
bI=!!bI;
for(var bL=0,bM=bJ.length;
bL<bM;
bL++){bN=!!bO(bJ[bL],bL);
if(bI!==bN){bK.push(bJ[bL])
}}return bK
},map:function(bJ,bO,bI){var bK=[],bN;
for(var bL=0,bM=bJ.length;
bL<bM;
bL++){bN=bO(bJ[bL],bL,bI);
if(bN!=null){bK[bK.length]=bN
}}return bK.concat.apply([],bK)
},guid:1,proxy:function(bK,bJ,bI){if(arguments.length===2){if(typeof bJ==="string"){bI=bK;
bK=bI[bJ];
bJ=I
}else{if(bJ&&!bo.isFunction(bJ)){bI=bJ;
bJ=I
}}}if(!bJ&&bK){bJ=function(){return bK.apply(bI||this,arguments)
}
}if(bK){bJ.guid=bK.guid=bK.guid||bJ.guid||bo.guid++
}return bJ
},access:function(bI,bQ,bO,bK,bN,bP){var bJ=bI.length;
if(typeof bQ==="object"){for(var bL in bQ){bo.access(bI,bL,bQ[bL],bK,bN,bO)
}return bI
}if(bO!==I){bK=!bP&&bK&&bo.isFunction(bO);
for(var bM=0;
bM<bJ;
bM++){bN(bI[bM],bQ,bK?bO.call(bI[bM],bM,bN(bI[bM],bQ)):bO,bP)
}return bI
}return bJ?bN(bI[0],bQ):I
},now:function(){return(new Date()).getTime()
},uaMatch:function(bJ){bJ=bJ.toLowerCase();
var bI=bi.exec(bJ)||bB.exec(bJ)||bA.exec(bJ)||bJ.indexOf("compatible")<0&&bC.exec(bJ)||[];
return{browser:bI[1]||"",version:bI[2]||"0"}
},sub:function(){function bJ(bL,bM){return new bJ.fn.init(bL,bM)
}bo.extend(true,bJ,this);
bJ.superclass=this;
bJ.fn=bJ.prototype=this();
bJ.fn.constructor=bJ;
bJ.subclass=this.subclass;
bJ.fn.init=function bK(bL,bM){if(bM&&bM instanceof bo&&!(bM instanceof bJ)){bM=bJ(bM)
}return bo.fn.init.call(this,bL,bM,bI)
};
bJ.fn.init.prototype=bJ.fn;
var bI=bJ(am);
return bJ
},browser:{}});
bo.each("Boolean Number String Function Array Date RegExp Object".split(" "),function(bJ,bI){bh["[object "+bI+"]"]=bI.toLowerCase()
});
bE=bo.uaMatch(bG);
if(bE.browser){bo.browser[bE.browser]=true;
bo.browser.version=bE.version
}if(bo.browser.webkit){bo.browser.safari=true
}if(bf){bo.inArray=function(bI,bJ){return bf.call(bJ,bI)
}
}if(bw.test("\xA0")){bs=/^[\s\xA0]+/;
bn=/[\s\xA0]+$/
}bm=bo(am);
if(am.addEventListener){e=function(){am.removeEventListener("DOMContentLoaded",e,false);
bo.ready()
}
}else{if(am.attachEvent){e=function(){if(am.readyState==="complete"){am.detachEvent("onreadystatechange",e);
bo.ready()
}}
}}function bg(){if(bo.isReady){return
}try{am.documentElement.doScroll("left")
}catch(bI){setTimeout(bg,1);
return
}bo.ready()
}return bo
})();
var a="then done fail isResolved isRejected promise".split(" "),aA=[].slice;
b.extend({_Deferred:function(){var bh=[],bi,bf,bg,e={done:function(){if(!bg){var bk=arguments,bl,bo,bn,bm,bj;
if(bi){bj=bi;
bi=0
}for(bl=0,bo=bk.length;
bl<bo;
bl++){bn=bk[bl];
bm=b.type(bn);
if(bm==="array"){e.done.apply(e,bn)
}else{if(bm==="function"){bh.push(bn)
}}}if(bj){e.resolveWith(bj[0],bj[1])
}}return this
},resolveWith:function(bk,bj){if(!bg&&!bi&&!bf){bj=bj||[];
bf=1;
try{while(bh[0]){bh.shift().apply(bk,bj)
}}finally{bi=[bk,bj];
bf=0
}}return this
},resolve:function(){e.resolveWith(this,arguments);
return this
},isResolved:function(){return !!(bf||bi)
},cancel:function(){bg=1;
bh=[];
return this
}};
return e
},Deferred:function(bf){var e=b._Deferred(),bh=b._Deferred(),bg;
b.extend(e,{then:function(bj,bi){e.done(bj).fail(bi);
return this
},fail:bh.done,rejectWith:bh.resolveWith,reject:bh.resolve,isRejected:bh.isResolved,promise:function(bj){if(bj==null){if(bg){return bg
}bg=bj={}
}var bi=a.length;
while(bi--){bj[a[bi]]=e[a[bi]]
}return bj
}});
e.done(bh.cancel).fail(e.cancel);
delete e.cancel;
if(bf){bf.call(e,e)
}return e
},when:function(bk){var bf=arguments,bg=0,bj=bf.length,bi=bj,e=bj<=1&&bk&&b.isFunction(bk.promise)?bk:b.Deferred();
function bh(bl){return function(bm){bf[bl]=arguments.length>1?aA.call(arguments,0):bm;
if(!(--bi)){e.resolveWith(e,aA.call(bf,0))
}}
}if(bj>1){for(;
bg<bj;
bg++){if(bf[bg]&&b.isFunction(bf[bg].promise)){bf[bg].promise().then(bh(bg),e.reject)
}else{--bi
}}if(!bi){e.resolveWith(e,bf)
}}else{if(e!==bk){e.resolveWith(e,bj?[bk]:[])
}}return e.promise()
}});
(function(){b.support={};
var bf=am.createElement("div");
bf.style.display="none";
bf.innerHTML="   <link/><table></table><a href='/a' style='color:red;float:left;opacity:.55;'>a</a><input type='checkbox'/>";
var bo=bf.getElementsByTagName("*"),bm=bf.getElementsByTagName("a")[0],bn=am.createElement("select"),bg=bn.appendChild(am.createElement("option")),bl=bf.getElementsByTagName("input")[0];
if(!bo||!bo.length||!bm){return
}b.support={leadingWhitespace:bf.firstChild.nodeType===3,tbody:!bf.getElementsByTagName("tbody").length,htmlSerialize:!!bf.getElementsByTagName("link").length,style:/red/.test(bm.getAttribute("style")),hrefNormalized:bm.getAttribute("href")==="/a",opacity:/^0.55$/.test(bm.style.opacity),cssFloat:!!bm.style.cssFloat,checkOn:bl.value==="on",optSelected:bg.selected,deleteExpando:true,optDisabled:false,checkClone:false,noCloneEvent:true,noCloneChecked:true,boxModel:null,inlineBlockNeedsLayout:false,shrinkWrapBlocks:false,reliableHiddenOffsets:true,reliableMarginRight:true};
bl.checked=true;
b.support.noCloneChecked=bl.cloneNode(true).checked;
bn.disabled=true;
b.support.optDisabled=!bg.disabled;
var bh=null;
b.support.scriptEval=function(){if(bh===null){var bq=am.documentElement,br=am.createElement("script"),bt="script"+b.now();
try{br.appendChild(am.createTextNode("window."+bt+"=1;"))
}catch(bs){}bq.insertBefore(br,bq.firstChild);
if(a0[bt]){bh=true;
delete a0[bt]
}else{bh=false
}bq.removeChild(br)
}return bh
};
try{delete bf.test
}catch(bj){b.support.deleteExpando=false
}if(!bf.addEventListener&&bf.attachEvent&&bf.fireEvent){bf.attachEvent("onclick",function bp(){b.support.noCloneEvent=false;
bf.detachEvent("onclick",bp)
});
bf.cloneNode(true).fireEvent("onclick")
}bf=am.createElement("div");
bf.innerHTML="<input type='radio' name='radiotest' checked='checked'/>";
var bi=am.createDocumentFragment();
bi.appendChild(bf.firstChild);
b.support.checkClone=bi.cloneNode(true).cloneNode(true).lastChild.checked;
b(function(){var br=am.createElement("div"),e=am.getElementsByTagName("body")[0];
if(!e){return
}br.style.width=br.style.paddingLeft="1px";
e.appendChild(br);
b.boxModel=b.support.boxModel=br.offsetWidth===2;
if("zoom" in br.style){br.style.display="inline";
br.style.zoom=1;
b.support.inlineBlockNeedsLayout=br.offsetWidth===2;
br.style.display="";
br.innerHTML="<div style='width:4px;'></div>";
b.support.shrinkWrapBlocks=br.offsetWidth!==2
}br.innerHTML="<table><tr><td style='padding:0;border:0;display:none'></td><td>t</td></tr></table>";
var bq=br.getElementsByTagName("td");
b.support.reliableHiddenOffsets=bq[0].offsetHeight===0;
bq[0].style.display="";
bq[1].style.display="none";
b.support.reliableHiddenOffsets=b.support.reliableHiddenOffsets&&bq[0].offsetHeight===0;
br.innerHTML="";
if(am.defaultView&&am.defaultView.getComputedStyle){br.style.width="1px";
br.style.marginRight="0";
b.support.reliableMarginRight=(parseInt(am.defaultView.getComputedStyle(br,null).marginRight,10)||0)===0
}e.removeChild(br).style.display="none";
br=bq=null
});
var bk=function(e){var br=am.createElement("div");
e="on"+e;
if(!br.attachEvent){return true
}var bq=(e in br);
if(!bq){br.setAttribute(e,"return;");
bq=typeof br[e]==="function"
}return bq
};
b.support.submitBubbles=bk("submit");
b.support.changeBubbles=bk("change");
bf=bo=bm=null
})();
var aG=/^(?:\{.*\}|\[.*\])$/;
b.extend({cache:{},uuid:0,expando:"jQuery"+(b.fn.jquery+Math.random()).replace(/\D/g,""),noData:{"embed":true,"object":"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000","applet":true},hasData:function(e){e=e.nodeType?b.cache[e[b.expando]]:e[b.expando];
return !!e&&!Q(e)
},data:function(bh,bf,bj,bi){if(!b.acceptData(bh)){return
}var bm=b.expando,bl=typeof bf==="string",bk,bn=bh.nodeType,e=bn?b.cache:bh,bg=bn?bh[b.expando]:bh[b.expando]&&b.expando;
if((!bg||(bi&&bg&&!e[bg][bm]))&&bl&&bj===I){return
}if(!bg){if(bn){bh[b.expando]=bg=++b.uuid
}else{bg=b.expando
}}if(!e[bg]){e[bg]={};
if(!bn){e[bg].toJSON=b.noop
}}if(typeof bf==="object"||typeof bf==="function"){if(bi){e[bg][bm]=b.extend(e[bg][bm],bf)
}else{e[bg]=b.extend(e[bg],bf)
}}bk=e[bg];
if(bi){if(!bk[bm]){bk[bm]={}
}bk=bk[bm]
}if(bj!==I){bk[bf]=bj
}if(bf==="events"&&!bk[bf]){return bk[bm]&&bk[bm].events
}return bl?bk[bf]:bk
},removeData:function(bi,bg,bj){if(!b.acceptData(bi)){return
}var bl=b.expando,bm=bi.nodeType,bf=bm?b.cache:bi,bh=bm?bi[b.expando]:b.expando;
if(!bf[bh]){return
}if(bg){var bk=bj?bf[bh][bl]:bf[bh];
if(bk){delete bk[bg];
if(!Q(bk)){return
}}}if(bj){delete bf[bh][bl];
if(!Q(bf[bh])){return
}}var e=bf[bh][bl];
if(b.support.deleteExpando||bf!=a0){delete bf[bh]
}else{bf[bh]=null
}if(e){bf[bh]={};
if(!bm){bf[bh].toJSON=b.noop
}bf[bh][bl]=e
}else{if(bm){if(b.support.deleteExpando){delete bi[b.expando]
}else{if(bi.removeAttribute){bi.removeAttribute(b.expando)
}else{bi[b.expando]=null
}}}}},_data:function(bf,e,bg){return b.data(bf,e,bg,true)
},acceptData:function(bf){if(bf.nodeName){var e=b.noData[bf.nodeName.toLowerCase()];
if(e){return !(e===true||bf.getAttribute("classid")!==e)
}}return true
}});
b.fn.extend({data:function(bi,bk){var bj=null;
if(typeof bi==="undefined"){if(this.length){bj=b.data(this[0]);
if(this[0].nodeType===1){var e=this[0].attributes,bg;
for(var bh=0,bf=e.length;
bh<bf;
bh++){bg=e[bh].name;
if(bg.indexOf("data-")===0){bg=bg.substr(5);
aV(this[0],bg,bj[bg])
}}}}return bj
}else{if(typeof bi==="object"){return this.each(function(){b.data(this,bi)
})
}}var bl=bi.split(".");
bl[1]=bl[1]?"."+bl[1]:"";
if(bk===I){bj=this.triggerHandler("getData"+bl[1]+"!",[bl[0]]);
if(bj===I&&this.length){bj=b.data(this[0],bi);
bj=aV(this[0],bi,bj)
}return bj===I&&bl[1]?this.data(bl[0]):bj
}else{return this.each(function(){var bn=b(this),bm=[bl[0],bk];
bn.triggerHandler("setData"+bl[1]+"!",bm);
b.data(this,bi,bk);
bn.triggerHandler("changeData"+bl[1]+"!",bm)
})
}},removeData:function(e){return this.each(function(){b.removeData(this,e)
})
}});
function aV(bg,bf,bh){if(bh===I&&bg.nodeType===1){bh=bg.getAttribute("data-"+bf);
if(typeof bh==="string"){try{bh=bh==="true"?true:bh==="false"?false:bh==="null"?null:!b.isNaN(bh)?parseFloat(bh):aG.test(bh)?b.parseJSON(bh):bh
}catch(bi){}b.data(bg,bf,bh)
}else{bh=I
}}return bh
}function Q(bf){for(var e in bf){if(e!=="toJSON"){return false
}}return true
}b.extend({queue:function(bf,e,bh){if(!bf){return
}e=(e||"fx")+"queue";
var bg=b._data(bf,e);
if(!bh){return bg||[]
}if(!bg||b.isArray(bh)){bg=b._data(bf,e,b.makeArray(bh))
}else{bg.push(bh)
}return bg
},dequeue:function(bh,bg){bg=bg||"fx";
var e=b.queue(bh,bg),bf=e.shift();
if(bf==="inprogress"){bf=e.shift()
}if(bf){if(bg==="fx"){e.unshift("inprogress")
}bf.call(bh,function(){b.dequeue(bh,bg)
})
}if(!e.length){b.removeData(bh,bg+"queue",true)
}}});
b.fn.extend({queue:function(e,bf){if(typeof e!=="string"){bf=e;
e="fx"
}if(bf===I){return b.queue(this[0],e)
}return this.each(function(bh){var bg=b.queue(this,e,bf);
if(e==="fx"&&bg[0]!=="inprogress"){b.dequeue(this,e)
}})
},dequeue:function(e){return this.each(function(){b.dequeue(this,e)
})
},delay:function(bf,e){bf=b.fx?b.fx.speeds[bf]||bf:bf;
e=e||"fx";
return this.queue(e,function(){var bg=this;
setTimeout(function(){b.dequeue(bg,e)
},bf)
})
},clearQueue:function(e){return this.queue(e||"fx",[])
}});
var aE=/[\n\t\r]/g,a5=/\s+/,aI=/\r/g,a4=/^(?:href|src|style)$/,g=/^(?:button|input)$/i,D=/^(?:button|input|object|select|textarea)$/i,l=/^a(?:rea)?$/i,R=/^(?:radio|checkbox)$/i;
b.props={"for":"htmlFor","class":"className",readonly:"readOnly",maxlength:"maxLength",cellspacing:"cellSpacing",rowspan:"rowSpan",colspan:"colSpan",tabindex:"tabIndex",usemap:"useMap",frameborder:"frameBorder"};
b.fn.extend({attr:function(e,bf){return b.access(this,e,bf,true,b.attr)
},removeAttr:function(e,bf){return this.each(function(){b.attr(this,e,"");
if(this.nodeType===1){this.removeAttribute(e)
}})
},addClass:function(bl){if(b.isFunction(bl)){return this.each(function(bo){var bn=b(this);
bn.addClass(bl.call(this,bo,bn.attr("class")))
})
}if(bl&&typeof bl==="string"){var e=(bl||"").split(a5);
for(var bh=0,bg=this.length;
bh<bg;
bh++){var bf=this[bh];
if(bf.nodeType===1){if(!bf.className){bf.className=bl
}else{var bi=" "+bf.className+" ",bk=bf.className;
for(var bj=0,bm=e.length;
bj<bm;
bj++){if(bi.indexOf(" "+e[bj]+" ")<0){bk+=" "+e[bj]
}}bf.className=b.trim(bk)
}}}}return this
},removeClass:function(bj){if(b.isFunction(bj)){return this.each(function(bn){var bm=b(this);
bm.removeClass(bj.call(this,bn,bm.attr("class")))
})
}if((bj&&typeof bj==="string")||bj===I){var bk=(bj||"").split(a5);
for(var bg=0,bf=this.length;
bg<bf;
bg++){var bi=this[bg];
if(bi.nodeType===1&&bi.className){if(bj){var bh=(" "+bi.className+" ").replace(aE," ");
for(var bl=0,e=bk.length;
bl<e;
bl++){bh=bh.replace(" "+bk[bl]+" "," ")
}bi.className=b.trim(bh)
}else{bi.className=""
}}}}return this
},toggleClass:function(bh,bf){var bg=typeof bh,e=typeof bf==="boolean";
if(b.isFunction(bh)){return this.each(function(bj){var bi=b(this);
bi.toggleClass(bh.call(this,bj,bi.attr("class"),bf),bf)
})
}return this.each(function(){if(bg==="string"){var bk,bj=0,bi=b(this),bl=bf,bm=bh.split(a5);
while((bk=bm[bj++])){bl=e?bl:!bi.hasClass(bk);
bi[bl?"addClass":"removeClass"](bk)
}}else{if(bg==="undefined"||bg==="boolean"){if(this.className){b._data(this,"__className__",this.className)
}this.className=this.className||bh===false?"":b._data(this,"__className__")||""
}}})
},hasClass:function(e){var bh=" "+e+" ";
for(var bg=0,bf=this.length;
bg<bf;
bg++){if((" "+this[bg].className+" ").replace(aE," ").indexOf(bh)>-1){return true
}}return false
},val:function(bm){if(!arguments.length){var bg=this[0];
if(bg){if(b.nodeName(bg,"option")){var bf=bg.attributes.value;
return !bf||bf.specified?bg.value:bg.text
}if(b.nodeName(bg,"select")){var bk=bg.selectedIndex,bn=[],bo=bg.options,bj=bg.type==="select-one";
if(bk<0){return null
}for(var bh=bj?bk:0,bl=bj?bk+1:bo.length;
bh<bl;
bh++){var bi=bo[bh];
if(bi.selected&&(b.support.optDisabled?!bi.disabled:bi.getAttribute("disabled")===null)&&(!bi.parentNode.disabled||!b.nodeName(bi.parentNode,"optgroup"))){bm=b(bi).val();
if(bj){return bm
}bn.push(bm)
}}if(bj&&!bn.length&&bo.length){return b(bo[bk]).val()
}return bn
}if(R.test(bg.type)&&!b.support.checkOn){return bg.getAttribute("value")===null?"on":bg.value
}return(bg.value||"").replace(aI,"")
}return I
}var e=b.isFunction(bm);
return this.each(function(br){var bq=b(this),bs=bm;
if(this.nodeType!==1){return
}if(e){bs=bm.call(this,br,bq.val())
}if(bs==null){bs=""
}else{if(typeof bs==="number"){bs+=""
}else{if(b.isArray(bs)){bs=b.map(bs,function(bt){return bt==null?"":bt+""
})
}}}if(b.isArray(bs)&&R.test(this.type)){this.checked=b.inArray(bq.val(),bs)>=0
}else{if(b.nodeName(this,"select")){var bp=b.makeArray(bs);
b("option",this).each(function(){this.selected=b.inArray(b(this).val(),bp)>=0
});
if(!bp.length){this.selectedIndex=-1
}}else{this.value=bs
}}})
}});
b.extend({attrFn:{val:true,css:true,html:true,text:true,data:true,width:true,height:true,offset:true},attr:function(bf,e,bk,bn){if(!bf||bf.nodeType===3||bf.nodeType===8||bf.nodeType===2){return I
}if(bn&&e in b.attrFn){return b(bf)[e](bk)
}var bg=bf.nodeType!==1||!b.isXMLDoc(bf),bj=bk!==I;
e=bg&&b.props[e]||e;
if(bf.nodeType===1){var bi=a4.test(e);
if(e==="selected"&&!b.support.optSelected){var bl=bf.parentNode;
if(bl){bl.selectedIndex;
if(bl.parentNode){bl.parentNode.selectedIndex
}}}if((e in bf||bf[e]!==I)&&bg&&!bi){if(bj){if(e==="type"&&g.test(bf.nodeName)&&bf.parentNode){b.error("type property can't be changed")
}if(bk===null){if(bf.nodeType===1){bf.removeAttribute(e)
}}else{bf[e]=bk
}}if(b.nodeName(bf,"form")&&bf.getAttributeNode(e)){return bf.getAttributeNode(e).nodeValue
}if(e==="tabIndex"){var bm=bf.getAttributeNode("tabIndex");
return bm&&bm.specified?bm.value:D.test(bf.nodeName)||l.test(bf.nodeName)&&bf.href?0:I
}return bf[e]
}if(!b.support.style&&bg&&e==="style"){if(bj){bf.style.cssText=""+bk
}return bf.style.cssText
}if(bj){bf.setAttribute(e,""+bk)
}if(!bf.attributes[e]&&(bf.hasAttribute&&!bf.hasAttribute(e))){return I
}var bh=!b.support.hrefNormalized&&bg&&bi?bf.getAttribute(e,2):bf.getAttribute(e);
return bh===null?I:bh
}if(bj){bf[e]=bk
}return bf[e]
}});
var aR=/\.(.*)$/,a2=/^(?:textarea|input|select)$/i,L=/\./g,ab=/ /g,ax=/[^\w\s.|`]/g,F=function(e){return e.replace(ax,"\\$&")
};
b.event={add:function(bi,bm,bt,bk){if(bi.nodeType===3||bi.nodeType===8){return
}try{if(b.isWindow(bi)&&(bi!==a0&&!bi.frameElement)){bi=a0
}}catch(bn){}if(bt===false){bt=a7
}else{if(!bt){return
}}var bg,br;
if(bt.handler){bg=bt;
bt=bg.handler
}if(!bt.guid){bt.guid=b.guid++
}var bo=b._data(bi);
if(!bo){return
}var bs=bo.events,bl=bo.handle;
if(!bs){bo.events=bs={}
}if(!bl){bo.handle=bl=function(bu){return typeof b!=="undefined"&&b.event.triggered!==bu.type?b.event.handle.apply(bl.elem,arguments):I
}
}bl.elem=bi;
bm=bm.split(" ");
var bq,bj=0,bf;
while((bq=bm[bj++])){br=bg?b.extend({},bg):{handler:bt,data:bk};
if(bq.indexOf(".")>-1){bf=bq.split(".");
bq=bf.shift();
br.namespace=bf.slice(0).sort().join(".")
}else{bf=[];
br.namespace=""
}br.type=bq;
if(!br.guid){br.guid=bt.guid
}var bh=bs[bq],bp=b.event.special[bq]||{};
if(!bh){bh=bs[bq]=[];
if(!bp.setup||bp.setup.call(bi,bk,bf,bl)===false){if(bi.addEventListener){bi.addEventListener(bq,bl,false)
}else{if(bi.attachEvent){bi.attachEvent("on"+bq,bl)
}}}}if(bp.add){bp.add.call(bi,br);
if(!br.handler.guid){br.handler.guid=bt.guid
}}bh.push(br);
b.event.global[bq]=true
}bi=null
},global:{},remove:function(bt,bo,bg,bk){if(bt.nodeType===3||bt.nodeType===8){return
}if(bg===false){bg=a7
}var bw,bj,bl,bq,br=0,bh,bm,bp,bi,bn,e,bv,bs=b.hasData(bt)&&b._data(bt),bf=bs&&bs.events;
if(!bs||!bf){return
}if(bo&&bo.type){bg=bo.handler;
bo=bo.type
}if(!bo||typeof bo==="string"&&bo.charAt(0)==="."){bo=bo||"";
for(bj in bf){b.event.remove(bt,bj+bo)
}return
}bo=bo.split(" ");
while((bj=bo[br++])){bv=bj;
e=null;
bh=bj.indexOf(".")<0;
bm=[];
if(!bh){bm=bj.split(".");
bj=bm.shift();
bp=new RegExp("(^|\\.)"+b.map(bm.slice(0).sort(),F).join("\\.(?:.*\\.)?")+"(\\.|$)")
}bn=bf[bj];
if(!bn){continue
}if(!bg){for(bq=0;
bq<bn.length;
bq++){e=bn[bq];
if(bh||bp.test(e.namespace)){b.event.remove(bt,bv,e.handler,bq);
bn.splice(bq--,1)
}}continue
}bi=b.event.special[bj]||{};
for(bq=bk||0;
bq<bn.length;
bq++){e=bn[bq];
if(bg.guid===e.guid){if(bh||bp.test(e.namespace)){if(bk==null){bn.splice(bq--,1)
}if(bi.remove){bi.remove.call(bt,e)
}}if(bk!=null){break
}}}if(bn.length===0||bk!=null&&bn.length===1){if(!bi.teardown||bi.teardown.call(bt,bm)===false){b.removeEvent(bt,bj,bs.handle)
}bw=null;
delete bf[bj]
}}if(b.isEmptyObject(bf)){var bu=bs.handle;
if(bu){bu.elem=null
}delete bs.events;
delete bs.handle;
if(b.isEmptyObject(bs)){b.removeData(bt,I,true)
}}},trigger:function(bf,bk,bh){var bo=bf.type||bf,bj=arguments[3];
if(!bj){bf=typeof bf==="object"?bf[b.expando]?bf:b.extend(b.Event(bo),bf):b.Event(bo);
if(bo.indexOf("!")>=0){bf.type=bo=bo.slice(0,-1);
bf.exclusive=true
}if(!bh){bf.stopPropagation();
if(b.event.global[bo]){b.each(b.cache,function(){var bt=b.expando,bs=this[bt];
if(bs&&bs.events&&bs.events[bo]){b.event.trigger(bf,bk,bs.handle.elem)
}})
}}if(!bh||bh.nodeType===3||bh.nodeType===8){return I
}bf.result=I;
bf.target=bh;
bk=b.makeArray(bk);
bk.unshift(bf)
}bf.currentTarget=bh;
var bl=b._data(bh,"handle");
if(bl){bl.apply(bh,bk)
}var bq=bh.parentNode||bh.ownerDocument;
try{if(!(bh&&bh.nodeName&&b.noData[bh.nodeName.toLowerCase()])){if(bh["on"+bo]&&bh["on"+bo].apply(bh,bk)===false){bf.result=false;
bf.preventDefault()
}}}catch(bp){}if(!bf.isPropagationStopped()&&bq){b.event.trigger(bf,bk,bq,true)
}else{if(!bf.isDefaultPrevented()){var bg,bm=bf.target,e=bo.replace(aR,""),br=b.nodeName(bm,"a")&&e==="click",bn=b.event.special[e]||{};
if((!bn._default||bn._default.call(bh,bf)===false)&&!br&&!(bm&&bm.nodeName&&b.noData[bm.nodeName.toLowerCase()])){try{if(bm[e]){bg=bm["on"+e];
if(bg){bm["on"+e]=null
}b.event.triggered=bf.type;
bm[e]()
}}catch(bi){}if(bg){bm["on"+e]=bg
}b.event.triggered=I
}}}},handle:function(e){var bn,bg,bf,bp,bo,bj=[],bl=b.makeArray(arguments);
e=bl[0]=b.event.fix(e||a0.event);
e.currentTarget=this;
bn=e.type.indexOf(".")<0&&!e.exclusive;
if(!bn){bf=e.type.split(".");
e.type=bf.shift();
bj=bf.slice(0).sort();
bp=new RegExp("(^|\\.)"+bj.join("\\.(?:.*\\.)?")+"(\\.|$)")
}e.namespace=e.namespace||bj.join(".");
bo=b._data(this,"events");
bg=(bo||{})[e.type];
if(bo&&bg){bg=bg.slice(0);
for(var bi=0,bh=bg.length;
bi<bh;
bi++){var bm=bg[bi];
if(bn||bp.test(bm.namespace)){e.handler=bm.handler;
e.data=bm.data;
e.handleObj=bm;
var bk=bm.handler.apply(this,bl);
if(bk!==I){e.result=bk;
if(bk===false){e.preventDefault();
e.stopPropagation()
}}if(e.isImmediatePropagationStopped()){break
}}}}return e.result
},props:"altKey attrChange attrName bubbles button cancelable charCode clientX clientY ctrlKey currentTarget data detail eventPhase fromElement handler keyCode layerX layerY metaKey newValue offsetX offsetY pageX pageY prevValue relatedNode relatedTarget screenX screenY shiftKey srcElement target toElement view wheelDelta which".split(" "),fix:function(bh){if(bh[b.expando]){return bh
}var bf=bh;
bh=b.Event(bf);
for(var bg=this.props.length,bj;
bg;
){bj=this.props[--bg];
bh[bj]=bf[bj]
}if(!bh.target){bh.target=bh.srcElement||am
}if(bh.target.nodeType===3){bh.target=bh.target.parentNode
}if(!bh.relatedTarget&&bh.fromElement){bh.relatedTarget=bh.fromElement===bh.target?bh.toElement:bh.fromElement
}if(bh.pageX==null&&bh.clientX!=null){var bi=am.documentElement,e=am.body;
bh.pageX=bh.clientX+(bi&&bi.scrollLeft||e&&e.scrollLeft||0)-(bi&&bi.clientLeft||e&&e.clientLeft||0);
bh.pageY=bh.clientY+(bi&&bi.scrollTop||e&&e.scrollTop||0)-(bi&&bi.clientTop||e&&e.clientTop||0)
}if(bh.which==null&&(bh.charCode!=null||bh.keyCode!=null)){bh.which=bh.charCode!=null?bh.charCode:bh.keyCode
}if(!bh.metaKey&&bh.ctrlKey){bh.metaKey=bh.ctrlKey
}if(!bh.which&&bh.button!==I){bh.which=(bh.button&1?1:(bh.button&2?3:(bh.button&4?2:0)))
}return bh
},guid:100000000,proxy:b.proxy,special:{ready:{setup:b.bindReady,teardown:b.noop},live:{add:function(e){b.event.add(this,o(e.origType,e.selector),b.extend({},e,{handler:ag,guid:e.handler.guid}))
},remove:function(e){b.event.remove(this,o(e.origType,e.selector),e)
}},beforeunload:{setup:function(bg,bf,e){if(b.isWindow(this)){this.onbeforeunload=e
}},teardown:function(bf,e){if(this.onbeforeunload===e){this.onbeforeunload=null
}}}}};
b.removeEvent=am.removeEventListener?function(bf,e,bg){if(bf.removeEventListener){bf.removeEventListener(e,bg,false)
}}:function(bf,e,bg){if(bf.detachEvent){bf.detachEvent("on"+e,bg)
}};
b.Event=function(e){if(!this.preventDefault){return new b.Event(e)
}if(e&&e.type){this.originalEvent=e;
this.type=e.type;
this.isDefaultPrevented=(e.defaultPrevented||e.returnValue===false||e.getPreventDefault&&e.getPreventDefault())?i:a7
}else{this.type=e
}this.timeStamp=b.now();
this[b.expando]=true
};
function a7(){return false
}function i(){return true
}b.Event.prototype={preventDefault:function(){this.isDefaultPrevented=i;
var bf=this.originalEvent;
if(!bf){return
}if(bf.preventDefault){bf.preventDefault()
}else{bf.returnValue=false
}},stopPropagation:function(){this.isPropagationStopped=i;
var bf=this.originalEvent;
if(!bf){return
}if(bf.stopPropagation){bf.stopPropagation()
}bf.cancelBubble=true
},stopImmediatePropagation:function(){this.isImmediatePropagationStopped=i;
this.stopPropagation()
},isDefaultPrevented:a7,isPropagationStopped:a7,isImmediatePropagationStopped:a7};
var aa=function(bg){var bf=bg.relatedTarget;
try{if(bf&&bf!==am&&!bf.parentNode){return
}while(bf&&bf!==this){bf=bf.parentNode
}if(bf!==this){bg.type=bg.data;
b.event.handle.apply(this,arguments)
}}catch(bh){}},aM=function(e){e.type=e.data;
b.event.handle.apply(this,arguments)
};
b.each({mouseenter:"mouseover",mouseleave:"mouseout"},function(bf,e){b.event.special[bf]={setup:function(bg){b.event.add(this,e,bg&&bg.selector?aM:aa,bf)
},teardown:function(bg){b.event.remove(this,e,bg&&bg.selector?aM:aa)
}}
});
if(!b.support.submitBubbles){b.event.special.submit={setup:function(bf,e){if(this.nodeName&&this.nodeName.toLowerCase()!=="form"){b.event.add(this,"click.specialSubmit",function(bi){var bh=bi.target,bg=bh.type;
if((bg==="submit"||bg==="image")&&b(bh).closest("form").length){aP("submit",this,arguments)
}});
b.event.add(this,"keypress.specialSubmit",function(bi){var bh=bi.target,bg=bh.type;
if((bg==="text"||bg==="password")&&b(bh).closest("form").length&&bi.keyCode===13){aP("submit",this,arguments)
}})
}else{return false
}},teardown:function(e){b.event.remove(this,".specialSubmit")
}}
}if(!b.support.changeBubbles){var a8,k=function(bf){var e=bf.type,bg=bf.value;
if(e==="radio"||e==="checkbox"){bg=bf.checked
}else{if(e==="select-multiple"){bg=bf.selectedIndex>-1?b.map(bf.options,function(bh){return bh.selected
}).join("-"):""
}else{if(bf.nodeName.toLowerCase()==="select"){bg=bf.selectedIndex
}}}return bg
},Y=function Y(bh){var bf=bh.target,bg,bi;
if(!a2.test(bf.nodeName)||bf.readOnly){return
}bg=b._data(bf,"_change_data");
bi=k(bf);
if(bh.type!=="focusout"||bf.type!=="radio"){b._data(bf,"_change_data",bi)
}if(bg===I||bi===bg){return
}if(bg!=null||bi){bh.type="change";
bh.liveFired=I;
b.event.trigger(bh,arguments[1],bf)
}};
b.event.special.change={filters:{focusout:Y,beforedeactivate:Y,click:function(bh){var bg=bh.target,bf=bg.type;
if(bf==="radio"||bf==="checkbox"||bg.nodeName.toLowerCase()==="select"){Y.call(this,bh)
}},keydown:function(bh){var bg=bh.target,bf=bg.type;
if((bh.keyCode===13&&bg.nodeName.toLowerCase()!=="textarea")||(bh.keyCode===32&&(bf==="checkbox"||bf==="radio"))||bf==="select-multiple"){Y.call(this,bh)
}},beforeactivate:function(bg){var bf=bg.target;
b._data(bf,"_change_data",k(bf))
}},setup:function(bg,bf){if(this.type==="file"){return false
}for(var e in a8){b.event.add(this,e+".specialChange",a8[e])
}return a2.test(this.nodeName)
},teardown:function(e){b.event.remove(this,".specialChange");
return a2.test(this.nodeName)
}};
a8=b.event.special.change.filters;
a8.focus=a8.beforeactivate
}function aP(bf,bh,e){var bg=b.extend({},e[0]);
bg.type=bf;
bg.originalEvent={};
bg.liveFired=I;
b.event.handle.call(bh,bg);
if(bg.isDefaultPrevented()){e[0].preventDefault()
}}if(am.addEventListener){b.each({focus:"focusin",blur:"focusout"},function(bh,e){var bf=0;
b.event.special[e]={setup:function(){if(bf++===0){am.addEventListener(bh,bg,true)
}},teardown:function(){if(--bf===0){am.removeEventListener(bh,bg,true)
}}};
function bg(bi){var bj=b.event.fix(bi);
bj.type=e;
bj.originalEvent={};
b.event.trigger(bj,null,bj.target);
if(bj.isDefaultPrevented()){bi.preventDefault()
}}})
}b.each(["bind","one"],function(bf,e){b.fn[e]=function(bl,bm,bk){if(typeof bl==="object"){for(var bi in bl){this[e](bi,bm,bl[bi],bk)
}return this
}if(b.isFunction(bm)||bm===false){bk=bm;
bm=I
}var bj=e==="one"?b.proxy(bk,function(bn){b(this).unbind(bn,bj);
return bk.apply(this,arguments)
}):bk;
if(bl==="unload"&&e!=="one"){this.one(bl,bm,bk)
}else{for(var bh=0,bg=this.length;
bh<bg;
bh++){b.event.add(this[bh],bl,bj,bm)
}}return this
}
});
b.fn.extend({unbind:function(bi,bh){if(typeof bi==="object"&&!bi.preventDefault){for(var bg in bi){this.unbind(bg,bi[bg])
}}else{for(var bf=0,e=this.length;
bf<e;
bf++){b.event.remove(this[bf],bi,bh)
}}return this
},delegate:function(e,bf,bh,bg){return this.live(bf,bh,bg,e)
},undelegate:function(e,bf,bg){if(arguments.length===0){return this.unbind("live")
}else{return this.die(bf,null,bg,e)
}},trigger:function(e,bf){return this.each(function(){b.event.trigger(e,bf,this)
})
},triggerHandler:function(e,bg){if(this[0]){var bf=b.Event(e);
bf.preventDefault();
bf.stopPropagation();
b.event.trigger(bf,bg,this[0]);
return bf.result
}},toggle:function(bg){var e=arguments,bf=1;
while(bf<e.length){b.proxy(bg,e[bf++])
}return this.click(b.proxy(bg,function(bh){var bi=(b._data(this,"lastToggle"+bg.guid)||0)%bf;
b._data(this,"lastToggle"+bg.guid,bi+1);
bh.preventDefault();
return e[bi].apply(this,arguments)||false
}))
},hover:function(e,bf){return this.mouseenter(e).mouseleave(bf||e)
}});
var aJ={focus:"focusin",blur:"focusout",mouseenter:"mouseover",mouseleave:"mouseout"};
b.each(["live","die"],function(bf,e){b.fn[e]=function(bp,bm,br,bi){var bq,bn=0,bo,bh,bt,bk=bi||this.selector,bg=bi?this:b(this.context);
if(typeof bp==="object"&&!bp.preventDefault){for(var bs in bp){bg[e](bs,bm,bp[bs],bk)
}return this
}if(b.isFunction(bm)){br=bm;
bm=I
}bp=(bp||"").split(" ");
while((bq=bp[bn++])!=null){bo=aR.exec(bq);
bh="";
if(bo){bh=bo[0];
bq=bq.replace(aR,"")
}if(bq==="hover"){bp.push("mouseenter"+bh,"mouseleave"+bh);
continue
}bt=bq;
if(bq==="focus"||bq==="blur"){bp.push(aJ[bq]+bh);
bq=bq+bh
}else{bq=(aJ[bq]||bq)+bh
}if(e==="live"){for(var bl=0,bj=bg.length;
bl<bj;
bl++){b.event.add(bg[bl],"live."+o(bq,bk),{data:bm,selector:bk,handler:br,origType:bq,origHandler:br,preType:bt})
}}else{bg.unbind("live."+o(bq,bk),br)
}}return this
}
});
function ag(bp){var bm,bh,bv,bj,e,br,bo,bq,bn,bu,bl,bk,bt,bs=[],bi=[],bf=b._data(this,"events");
if(bp.liveFired===this||!bf||!bf.live||bp.target.disabled||bp.button&&bp.type==="click"){return
}if(bp.namespace){bk=new RegExp("(^|\\.)"+bp.namespace.split(".").join("\\.(?:.*\\.)?")+"(\\.|$)")
}bp.liveFired=this;
var bg=bf.live.slice(0);
for(bo=0;
bo<bg.length;
bo++){e=bg[bo];
if(e.origType.replace(aR,"")===bp.type){bi.push(e.selector)
}else{bg.splice(bo--,1)
}}bj=b(bp.target).closest(bi,bp.currentTarget);
for(bq=0,bn=bj.length;
bq<bn;
bq++){bl=bj[bq];
for(bo=0;
bo<bg.length;
bo++){e=bg[bo];
if(bl.selector===e.selector&&(!bk||bk.test(e.namespace))&&!bl.elem.disabled){br=bl.elem;
bv=null;
if(e.preType==="mouseenter"||e.preType==="mouseleave"){bp.type=e.preType;
bv=b(bp.relatedTarget).closest(e.selector)[0]
}if(!bv||bv!==br){bs.push({elem:br,handleObj:e,level:bl.level})
}}}}for(bq=0,bn=bs.length;
bq<bn;
bq++){bj=bs[bq];
if(bh&&bj.level>bh){break
}bp.currentTarget=bj.elem;
bp.data=bj.handleObj.data;
bp.handleObj=bj.handleObj;
bt=bj.handleObj.origHandler.apply(bj.elem,arguments);
if(bt===false||bp.isPropagationStopped()){bh=bj.level;
if(bt===false){bm=false
}if(bp.isImmediatePropagationStopped()){break
}}}return bm
}function o(bf,e){return(bf&&bf!=="*"?bf+".":"")+e.replace(L,"`").replace(ab,"&")
}b.each(("blur focus focusin focusout load resize scroll unload click dblclick "+"mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave "+"change select submit keydown keypress keyup error").split(" "),function(bf,e){b.fn[e]=function(bh,bg){if(bg==null){bg=bh;
bh=null
}return arguments.length>0?this.bind(e,bh,bg):this.trigger(e)
};
if(b.attrFn){b.attrFn[e]=true
}});
/*
 * Sizzle CSS Selector Engine
 *  Copyright 2011, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
(function(){var bp=/((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,bq=0,bt=Object.prototype.toString,bk=false,bj=true,br=/\\/g,bx=/\W/;
[0,0].sort(function(){bj=false;
return 0
});
var bh=function(bC,e,bF,bG){bF=bF||[];
e=e||am;
var bI=e;
if(e.nodeType!==1&&e.nodeType!==9){return[]
}if(!bC||typeof bC!=="string"){return bF
}var bz,bK,bN,by,bJ,bM,bL,bE,bB=true,bA=bh.isXML(e),bD=[],bH=bC;
do{bp.exec("");
bz=bp.exec(bH);
if(bz){bH=bz[3];
bD.push(bz[1]);
if(bz[2]){by=bz[3];
break
}}}while(bz);
if(bD.length>1&&bl.exec(bC)){if(bD.length===2&&bm.relative[bD[0]]){bK=bu(bD[0]+bD[1],e)
}else{bK=bm.relative[bD[0]]?[e]:bh(bD.shift(),e);
while(bD.length){bC=bD.shift();
if(bm.relative[bC]){bC+=bD.shift()
}bK=bu(bC,bK)
}}}else{if(!bG&&bD.length>1&&e.nodeType===9&&!bA&&bm.match.ID.test(bD[0])&&!bm.match.ID.test(bD[bD.length-1])){bJ=bh.find(bD.shift(),e,bA);
e=bJ.expr?bh.filter(bJ.expr,bJ.set)[0]:bJ.set[0]
}if(e){bJ=bG?{expr:bD.pop(),set:bn(bG)}:bh.find(bD.pop(),bD.length===1&&(bD[0]==="~"||bD[0]==="+")&&e.parentNode?e.parentNode:e,bA);
bK=bJ.expr?bh.filter(bJ.expr,bJ.set):bJ.set;
if(bD.length>0){bN=bn(bK)
}else{bB=false
}while(bD.length){bM=bD.pop();
bL=bM;
if(!bm.relative[bM]){bM=""
}else{bL=bD.pop()
}if(bL==null){bL=e
}bm.relative[bM](bN,bL,bA)
}}else{bN=bD=[]
}}if(!bN){bN=bK
}if(!bN){bh.error(bM||bC)
}if(bt.call(bN)==="[object Array]"){if(!bB){bF.push.apply(bF,bN)
}else{if(e&&e.nodeType===1){for(bE=0;
bN[bE]!=null;
bE++){if(bN[bE]&&(bN[bE]===true||bN[bE].nodeType===1&&bh.contains(e,bN[bE]))){bF.push(bK[bE])
}}}else{for(bE=0;
bN[bE]!=null;
bE++){if(bN[bE]&&bN[bE].nodeType===1){bF.push(bK[bE])
}}}}}else{bn(bN,bF)
}if(by){bh(by,bI,bF,bG);
bh.uniqueSort(bF)
}return bF
};
bh.uniqueSort=function(by){if(bs){bk=bj;
by.sort(bs);
if(bk){for(var e=1;
e<by.length;
e++){if(by[e]===by[e-1]){by.splice(e--,1)
}}}}return by
};
bh.matches=function(e,by){return bh(e,null,null,by)
};
bh.matchesSelector=function(e,by){return bh(by,null,null,[e]).length>0
};
bh.find=function(bE,e,bF){var bD;
if(!bE){return[]
}for(var bA=0,bz=bm.order.length;
bA<bz;
bA++){var bB,bC=bm.order[bA];
if((bB=bm.leftMatch[bC].exec(bE))){var by=bB[1];
bB.splice(1,1);
if(by.substr(by.length-1)!=="\\"){bB[1]=(bB[1]||"").replace(br,"");
bD=bm.find[bC](bB,e,bF);
if(bD!=null){bE=bE.replace(bm.match[bC],"");
break
}}}}if(!bD){bD=typeof e.getElementsByTagName!=="undefined"?e.getElementsByTagName("*"):[]
}return{set:bD,expr:bE}
};
bh.filter=function(bI,bH,bL,bB){var bD,e,bz=bI,bN=[],bF=bH,bE=bH&&bH[0]&&bh.isXML(bH[0]);
while(bI&&bH.length){for(var bG in bm.filter){if((bD=bm.leftMatch[bG].exec(bI))!=null&&bD[2]){var bM,bK,by=bm.filter[bG],bA=bD[1];
e=false;
bD.splice(1,1);
if(bA.substr(bA.length-1)==="\\"){continue
}if(bF===bN){bN=[]
}if(bm.preFilter[bG]){bD=bm.preFilter[bG](bD,bF,bL,bN,bB,bE);
if(!bD){e=bM=true
}else{if(bD===true){continue
}}}if(bD){for(var bC=0;
(bK=bF[bC])!=null;
bC++){if(bK){bM=by(bK,bD,bC,bF);
var bJ=bB^!!bM;
if(bL&&bM!=null){if(bJ){e=true
}else{bF[bC]=false
}}else{if(bJ){bN.push(bK);
e=true
}}}}}if(bM!==I){if(!bL){bF=bN
}bI=bI.replace(bm.match[bG],"");
if(!e){return[]
}break
}}}if(bI===bz){if(e==null){bh.error(bI)
}else{break
}}bz=bI
}return bF
};
bh.error=function(e){throw"Syntax error, unrecognized expression: "+e
};
var bm=bh.selectors={order:["ID","NAME","TAG"],match:{ID:/#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,CLASS:/\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,NAME:/\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,ATTR:/\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,TAG:/^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,CHILD:/:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,POS:/:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,PSEUDO:/:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/},leftMatch:{},attrMap:{"class":"className","for":"htmlFor"},attrHandle:{href:function(e){return e.getAttribute("href")
},type:function(e){return e.getAttribute("type")
}},relative:{"+":function(bD,by){var bA=typeof by==="string",bC=bA&&!bx.test(by),bE=bA&&!bC;
if(bC){by=by.toLowerCase()
}for(var bz=0,e=bD.length,bB;
bz<e;
bz++){if((bB=bD[bz])){while((bB=bB.previousSibling)&&bB.nodeType!==1){}bD[bz]=bE||bB&&bB.nodeName.toLowerCase()===by?bB||false:bB===by
}}if(bE){bh.filter(by,bD,true)
}},">":function(bD,by){var bC,bB=typeof by==="string",bz=0,e=bD.length;
if(bB&&!bx.test(by)){by=by.toLowerCase();
for(;
bz<e;
bz++){bC=bD[bz];
if(bC){var bA=bC.parentNode;
bD[bz]=bA.nodeName.toLowerCase()===by?bA:false
}}}else{for(;
bz<e;
bz++){bC=bD[bz];
if(bC){bD[bz]=bB?bC.parentNode:bC.parentNode===by
}}if(bB){bh.filter(by,bD,true)
}}},"":function(bA,by,bC){var bB,bz=bq++,e=bv;
if(typeof by==="string"&&!bx.test(by)){by=by.toLowerCase();
bB=by;
e=bf
}e("parentNode",by,bz,bA,bB,bC)
},"~":function(bA,by,bC){var bB,bz=bq++,e=bv;
if(typeof by==="string"&&!bx.test(by)){by=by.toLowerCase();
bB=by;
e=bf
}e("previousSibling",by,bz,bA,bB,bC)
}},find:{ID:function(by,bz,bA){if(typeof bz.getElementById!=="undefined"&&!bA){var e=bz.getElementById(by[1]);
return e&&e.parentNode?[e]:[]
}},NAME:function(bz,bC){if(typeof bC.getElementsByName!=="undefined"){var by=[],bB=bC.getElementsByName(bz[1]);
for(var bA=0,e=bB.length;
bA<e;
bA++){if(bB[bA].getAttribute("name")===bz[1]){by.push(bB[bA])
}}return by.length===0?null:by
}},TAG:function(e,by){if(typeof by.getElementsByTagName!=="undefined"){return by.getElementsByTagName(e[1])
}}},preFilter:{CLASS:function(bA,by,bz,e,bD,bE){bA=" "+bA[1].replace(br,"")+" ";
if(bE){return bA
}for(var bB=0,bC;
(bC=by[bB])!=null;
bB++){if(bC){if(bD^(bC.className&&(" "+bC.className+" ").replace(/[\t\n\r]/g," ").indexOf(bA)>=0)){if(!bz){e.push(bC)
}}else{if(bz){by[bB]=false
}}}}return false
},ID:function(e){return e[1].replace(br,"")
},TAG:function(by,e){return by[1].replace(br,"").toLowerCase()
},CHILD:function(e){if(e[1]==="nth"){if(!e[2]){bh.error(e[0])
}e[2]=e[2].replace(/^\+|\s*/g,"");
var by=/(-?)(\d*)(?:n([+\-]?\d*))?/.exec(e[2]==="even"&&"2n"||e[2]==="odd"&&"2n+1"||!/\D/.test(e[2])&&"0n+"+e[2]||e[2]);
e[2]=(by[1]+(by[2]||1))-0;
e[3]=by[3]-0
}else{if(e[2]){bh.error(e[0])
}}e[0]=bq++;
return e
},ATTR:function(bB,by,bz,e,bC,bD){var bA=bB[1]=bB[1].replace(br,"");
if(!bD&&bm.attrMap[bA]){bB[1]=bm.attrMap[bA]
}bB[4]=(bB[4]||bB[5]||"").replace(br,"");
if(bB[2]==="~="){bB[4]=" "+bB[4]+" "
}return bB
},PSEUDO:function(bB,by,bz,e,bC){if(bB[1]==="not"){if((bp.exec(bB[3])||"").length>1||/^\w/.test(bB[3])){bB[3]=bh(bB[3],null,null,by)
}else{var bA=bh.filter(bB[3],by,bz,true^bC);
if(!bz){e.push.apply(e,bA)
}return false
}}else{if(bm.match.POS.test(bB[0])||bm.match.CHILD.test(bB[0])){return true
}}return bB
},POS:function(e){e.unshift(true);
return e
}},filters:{enabled:function(e){return e.disabled===false&&e.type!=="hidden"
},disabled:function(e){return e.disabled===true
},checked:function(e){return e.checked===true
},selected:function(e){if(e.parentNode){e.parentNode.selectedIndex
}return e.selected===true
},parent:function(e){return !!e.firstChild
},empty:function(e){return !e.firstChild
},has:function(bz,by,e){return !!bh(e[3],bz).length
},header:function(e){return(/h\d/i).test(e.nodeName)
},text:function(bz){var e=bz.getAttribute("type"),by=bz.type;
return"text"===by&&(e===by||e===null)
},radio:function(e){return"radio"===e.type
},checkbox:function(e){return"checkbox"===e.type
},file:function(e){return"file"===e.type
},password:function(e){return"password"===e.type
},submit:function(e){return"submit"===e.type
},image:function(e){return"image"===e.type
},reset:function(e){return"reset"===e.type
},button:function(e){return"button"===e.type||e.nodeName.toLowerCase()==="button"
},input:function(e){return(/input|select|textarea|button/i).test(e.nodeName)
}},setFilters:{first:function(by,e){return e===0
},last:function(bz,by,e,bA){return by===bA.length-1
},even:function(by,e){return e%2===0
},odd:function(by,e){return e%2===1
},lt:function(bz,by,e){return by<e[3]-0
},gt:function(bz,by,e){return by>e[3]-0
},nth:function(bz,by,e){return e[3]-0===by
},eq:function(bz,by,e){return e[3]-0===by
}},filter:{PSEUDO:function(bz,bE,bD,bF){var e=bE[1],by=bm.filters[e];
if(by){return by(bz,bD,bE,bF)
}else{if(e==="contains"){return(bz.textContent||bz.innerText||bh.getText([bz])||"").indexOf(bE[3])>=0
}else{if(e==="not"){var bA=bE[3];
for(var bC=0,bB=bA.length;
bC<bB;
bC++){if(bA[bC]===bz){return false
}}return true
}else{bh.error(e)
}}}},CHILD:function(e,bA){var bD=bA[1],by=e;
switch(bD){case"only":case"first":while((by=by.previousSibling)){if(by.nodeType===1){return false
}}if(bD==="first"){return true
}by=e;
case"last":while((by=by.nextSibling)){if(by.nodeType===1){return false
}}return true;
case"nth":var bz=bA[2],bG=bA[3];
if(bz===1&&bG===0){return true
}var bC=bA[0],bF=e.parentNode;
if(bF&&(bF.sizcache!==bC||!e.nodeIndex)){var bB=0;
for(by=bF.firstChild;
by;
by=by.nextSibling){if(by.nodeType===1){by.nodeIndex=++bB
}}bF.sizcache=bC
}var bE=e.nodeIndex-bG;
if(bz===0){return bE===0
}else{return(bE%bz===0&&bE/bz>=0)
}}},ID:function(by,e){return by.nodeType===1&&by.getAttribute("id")===e
},TAG:function(by,e){return(e==="*"&&by.nodeType===1)||by.nodeName.toLowerCase()===e
},CLASS:function(by,e){return(" "+(by.className||by.getAttribute("class"))+" ").indexOf(e)>-1
},ATTR:function(bC,bA){var bz=bA[1],e=bm.attrHandle[bz]?bm.attrHandle[bz](bC):bC[bz]!=null?bC[bz]:bC.getAttribute(bz),bD=e+"",bB=bA[2],by=bA[4];
return e==null?bB==="!=":bB==="="?bD===by:bB==="*="?bD.indexOf(by)>=0:bB==="~="?(" "+bD+" ").indexOf(by)>=0:!by?bD&&e!==false:bB==="!="?bD!==by:bB==="^="?bD.indexOf(by)===0:bB==="$="?bD.substr(bD.length-by.length)===by:bB==="|="?bD===by||bD.substr(0,by.length+1)===by+"-":false
},POS:function(bB,by,bz,bC){var e=by[2],bA=bm.setFilters[e];
if(bA){return bA(bB,bz,by,bC)
}}}};
var bl=bm.match.POS,bg=function(by,e){return"\\"+(e-0+1)
};
for(var bi in bm.match){bm.match[bi]=new RegExp(bm.match[bi].source+(/(?![^\[]*\])(?![^\(]*\))/.source));
bm.leftMatch[bi]=new RegExp(/(^(?:.|\r|\n)*?)/.source+bm.match[bi].source.replace(/\\(\d+)/g,bg))
}var bn=function(by,e){by=Array.prototype.slice.call(by,0);
if(e){e.push.apply(e,by);
return e
}return by
};
try{Array.prototype.slice.call(am.documentElement.childNodes,0)[0].nodeType
}catch(bw){bn=function(bB,bA){var bz=0,by=bA||[];
if(bt.call(bB)==="[object Array]"){Array.prototype.push.apply(by,bB)
}else{if(typeof bB.length==="number"){for(var e=bB.length;
bz<e;
bz++){by.push(bB[bz])
}}else{for(;
bB[bz];
bz++){by.push(bB[bz])
}}}return by
}
}var bs,bo;
if(am.documentElement.compareDocumentPosition){bs=function(by,e){if(by===e){bk=true;
return 0
}if(!by.compareDocumentPosition||!e.compareDocumentPosition){return by.compareDocumentPosition?-1:1
}return by.compareDocumentPosition(e)&4?-1:1
}
}else{bs=function(bF,bE){var bC,by,bz=[],e=[],bB=bF.parentNode,bD=bE.parentNode,bG=bB;
if(bF===bE){bk=true;
return 0
}else{if(bB===bD){return bo(bF,bE)
}else{if(!bB){return -1
}else{if(!bD){return 1
}}}}while(bG){bz.unshift(bG);
bG=bG.parentNode
}bG=bD;
while(bG){e.unshift(bG);
bG=bG.parentNode
}bC=bz.length;
by=e.length;
for(var bA=0;
bA<bC&&bA<by;
bA++){if(bz[bA]!==e[bA]){return bo(bz[bA],e[bA])
}}return bA===bC?bo(bF,e[bA],-1):bo(bz[bA],bE,1)
};
bo=function(by,e,bz){if(by===e){return bz
}var bA=by.nextSibling;
while(bA){if(bA===e){return -1
}bA=bA.nextSibling
}return 1
}
}bh.getText=function(e){var by="",bA;
for(var bz=0;
e[bz];
bz++){bA=e[bz];
if(bA.nodeType===3||bA.nodeType===4){by+=bA.nodeValue
}else{if(bA.nodeType!==8){by+=bh.getText(bA.childNodes)
}}}return by
};
(function(){var by=am.createElement("div"),bz="script"+(new Date()).getTime(),e=am.documentElement;
by.innerHTML="<a name='"+bz+"'/>";
e.insertBefore(by,e.firstChild);
if(am.getElementById(bz)){bm.find.ID=function(bB,bC,bD){if(typeof bC.getElementById!=="undefined"&&!bD){var bA=bC.getElementById(bB[1]);
return bA?bA.id===bB[1]||typeof bA.getAttributeNode!=="undefined"&&bA.getAttributeNode("id").nodeValue===bB[1]?[bA]:I:[]
}};
bm.filter.ID=function(bC,bA){var bB=typeof bC.getAttributeNode!=="undefined"&&bC.getAttributeNode("id");
return bC.nodeType===1&&bB&&bB.nodeValue===bA
}
}e.removeChild(by);
e=by=null
})();
(function(){var e=am.createElement("div");
e.appendChild(am.createComment(""));
if(e.getElementsByTagName("*").length>0){bm.find.TAG=function(by,bC){var bB=bC.getElementsByTagName(by[1]);
if(by[1]==="*"){var bA=[];
for(var bz=0;
bB[bz];
bz++){if(bB[bz].nodeType===1){bA.push(bB[bz])
}}bB=bA
}return bB
}
}e.innerHTML="<a href='#'></a>";
if(e.firstChild&&typeof e.firstChild.getAttribute!=="undefined"&&e.firstChild.getAttribute("href")!=="#"){bm.attrHandle.href=function(by){return by.getAttribute("href",2)
}
}e=null
})();
if(am.querySelectorAll){(function(){var e=bh,bA=am.createElement("div"),bz="__sizzle__";
bA.innerHTML="<p class='TEST'></p>";
if(bA.querySelectorAll&&bA.querySelectorAll(".TEST").length===0){return
}bh=function(bL,bC,bG,bK){bC=bC||am;
if(!bK&&!bh.isXML(bC)){var bJ=/^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec(bL);
if(bJ&&(bC.nodeType===1||bC.nodeType===9)){if(bJ[1]){return bn(bC.getElementsByTagName(bL),bG)
}else{if(bJ[2]&&bm.find.CLASS&&bC.getElementsByClassName){return bn(bC.getElementsByClassName(bJ[2]),bG)
}}}if(bC.nodeType===9){if(bL==="body"&&bC.body){return bn([bC.body],bG)
}else{if(bJ&&bJ[3]){var bF=bC.getElementById(bJ[3]);
if(bF&&bF.parentNode){if(bF.id===bJ[3]){return bn([bF],bG)
}}else{return bn([],bG)
}}}try{return bn(bC.querySelectorAll(bL),bG)
}catch(bH){}}else{if(bC.nodeType===1&&bC.nodeName.toLowerCase()!=="object"){var bD=bC,bE=bC.getAttribute("id"),bB=bE||bz,bN=bC.parentNode,bM=/^\s*[+~]/.test(bL);
if(!bE){bC.setAttribute("id",bB)
}else{bB=bB.replace(/'/g,"\\$&")
}if(bM&&bN){bC=bC.parentNode
}try{if(!bM||bN){return bn(bC.querySelectorAll("[id='"+bB+"'] "+bL),bG)
}}catch(bI){}finally{if(!bE){bD.removeAttribute("id")
}}}}}return e(bL,bC,bG,bK)
};
for(var by in e){bh[by]=e[by]
}bA=null
})()
}(function(){var e=am.documentElement,bz=e.matchesSelector||e.mozMatchesSelector||e.webkitMatchesSelector||e.msMatchesSelector;
if(bz){var bB=!bz.call(am.createElement("div"),"div"),by=false;
try{bz.call(am.documentElement,"[test!='']:sizzle")
}catch(bA){by=true
}bh.matchesSelector=function(bD,bF){bF=bF.replace(/\=\s*([^'"\]]*)\s*\]/g,"='$1']");
if(!bh.isXML(bD)){try{if(by||!bm.match.PSEUDO.test(bF)&&!/!=/.test(bF)){var bC=bz.call(bD,bF);
if(bC||!bB||bD.document&&bD.document.nodeType!==11){return bC
}}}catch(bE){}}return bh(bF,null,null,[bD]).length>0
}
}})();
(function(){var e=am.createElement("div");
e.innerHTML="<div class='test e'></div><div class='test'></div>";
if(!e.getElementsByClassName||e.getElementsByClassName("e").length===0){return
}e.lastChild.className="e";
if(e.getElementsByClassName("e").length===1){return
}bm.order.splice(1,0,"CLASS");
bm.find.CLASS=function(by,bz,bA){if(typeof bz.getElementsByClassName!=="undefined"&&!bA){return bz.getElementsByClassName(by[1])
}};
e=null
})();
function bf(by,bD,bC,bG,bE,bF){for(var bA=0,bz=bG.length;
bA<bz;
bA++){var e=bG[bA];
if(e){var bB=false;
e=e[by];
while(e){if(e.sizcache===bC){bB=bG[e.sizset];
break
}if(e.nodeType===1&&!bF){e.sizcache=bC;
e.sizset=bA
}if(e.nodeName.toLowerCase()===bD){bB=e;
break
}e=e[by]
}bG[bA]=bB
}}}function bv(by,bD,bC,bG,bE,bF){for(var bA=0,bz=bG.length;
bA<bz;
bA++){var e=bG[bA];
if(e){var bB=false;
e=e[by];
while(e){if(e.sizcache===bC){bB=bG[e.sizset];
break
}if(e.nodeType===1){if(!bF){e.sizcache=bC;
e.sizset=bA
}if(typeof bD!=="string"){if(e===bD){bB=true;
break
}}else{if(bh.filter(bD,[e]).length>0){bB=e;
break
}}}e=e[by]
}bG[bA]=bB
}}}if(am.documentElement.contains){bh.contains=function(by,e){return by!==e&&(by.contains?by.contains(e):true)
}
}else{if(am.documentElement.compareDocumentPosition){bh.contains=function(by,e){return !!(by.compareDocumentPosition(e)&16)
}
}else{bh.contains=function(){return false
}
}}bh.isXML=function(e){var by=(e?e.ownerDocument||e:0).documentElement;
return by?by.nodeName!=="HTML":false
};
var bu=function(e,bE){var bC,bA=[],bB="",bz=bE.nodeType?[bE]:bE;
while((bC=bm.match.PSEUDO.exec(e))){bB+=bC[0];
e=e.replace(bm.match.PSEUDO,"")
}e=bm.relative[e]?e+"*":e;
for(var bD=0,by=bz.length;
bD<by;
bD++){bh(e,bz[bD],bA)
}return bh.filter(bB,bA)
};
b.find=bh;
b.expr=bh.selectors;
b.expr[":"]=b.expr.filters;
b.unique=bh.uniqueSort;
b.text=bh.getText;
b.isXMLDoc=bh.isXML;
b.contains=bh.contains
})();
var X=/Until$/,aj=/^(?:parents|prevUntil|prevAll)/,aY=/,/,bb=/^.[^:#\[\.,]*$/,N=Array.prototype.slice,G=b.expr.match.POS,ap={children:true,contents:true,next:true,prev:true};
b.fn.extend({find:function(e){var bg=this.pushStack("","find",e),bj=0;
for(var bh=0,bf=this.length;
bh<bf;
bh++){bj=bg.length;
b.find(e,this[bh],bg);
if(bh>0){for(var bk=bj;
bk<bg.length;
bk++){for(var bi=0;
bi<bj;
bi++){if(bg[bi]===bg[bk]){bg.splice(bk--,1);
break
}}}}}return bg
},has:function(bf){var e=b(bf);
return this.filter(function(){for(var bh=0,bg=e.length;
bh<bg;
bh++){if(b.contains(this,e[bh])){return true
}}})
},not:function(e){return this.pushStack(aw(this,e,false),"not",e)
},filter:function(e){return this.pushStack(aw(this,e,true),"filter",e)
},is:function(e){return !!e&&b.filter(e,this).length>0
},closest:function(bo,bf){var bl=[],bi,bg,bn=this[0];
if(b.isArray(bo)){var bk,bh,bj={},e=1;
if(bn&&bo.length){for(bi=0,bg=bo.length;
bi<bg;
bi++){bh=bo[bi];
if(!bj[bh]){bj[bh]=b.expr.match.POS.test(bh)?b(bh,bf||this.context):bh
}}while(bn&&bn.ownerDocument&&bn!==bf){for(bh in bj){bk=bj[bh];
if(bk.jquery?bk.index(bn)>-1:b(bn).is(bk)){bl.push({selector:bh,elem:bn,level:e})
}}bn=bn.parentNode;
e++
}}return bl
}var bm=G.test(bo)?b(bo,bf||this.context):null;
for(bi=0,bg=this.length;
bi<bg;
bi++){bn=this[bi];
while(bn){if(bm?bm.index(bn)>-1:b.find.matchesSelector(bn,bo)){bl.push(bn);
break
}else{bn=bn.parentNode;
if(!bn||!bn.ownerDocument||bn===bf){break
}}}}bl=bl.length>1?b.unique(bl):bl;
return this.pushStack(bl,"closest",bo)
},index:function(e){if(!e||typeof e==="string"){return b.inArray(this[0],e?b(e):this.parent().children())
}return b.inArray(e.jquery?e[0]:e,this)
},add:function(e,bf){var bh=typeof e==="string"?b(e,bf):b.makeArray(e),bg=b.merge(this.get(),bh);
return this.pushStack(C(bh[0])||C(bg[0])?bg:b.unique(bg))
},andSelf:function(){return this.add(this.prevObject)
}});
function C(e){return !e||!e.parentNode||e.parentNode.nodeType===11
}b.each({parent:function(bf){var e=bf.parentNode;
return e&&e.nodeType!==11?e:null
},parents:function(e){return b.dir(e,"parentNode")
},parentsUntil:function(bf,e,bg){return b.dir(bf,"parentNode",bg)
},next:function(e){return b.nth(e,2,"nextSibling")
},prev:function(e){return b.nth(e,2,"previousSibling")
},nextAll:function(e){return b.dir(e,"nextSibling")
},prevAll:function(e){return b.dir(e,"previousSibling")
},nextUntil:function(bf,e,bg){return b.dir(bf,"nextSibling",bg)
},prevUntil:function(bf,e,bg){return b.dir(bf,"previousSibling",bg)
},siblings:function(e){return b.sibling(e.parentNode.firstChild,e)
},children:function(e){return b.sibling(e.firstChild)
},contents:function(e){return b.nodeName(e,"iframe")?e.contentDocument||e.contentWindow.document:b.makeArray(e.childNodes)
}},function(e,bf){b.fn[e]=function(bj,bg){var bi=b.map(this,bf,bj),bh=N.call(arguments);
if(!X.test(e)){bg=bj
}if(bg&&typeof bg==="string"){bi=b.filter(bg,bi)
}bi=this.length>1&&!ap[e]?b.unique(bi):bi;
if((this.length>1||aY.test(bg))&&aj.test(e)){bi=bi.reverse()
}return this.pushStack(bi,e,bh.join(","))
}
});
b.extend({filter:function(bg,e,bf){if(bf){bg=":not("+bg+")"
}return e.length===1?b.find.matchesSelector(e[0],bg)?[e[0]]:[]:b.find.matches(bg,e)
},dir:function(bg,bf,bi){var e=[],bh=bg[bf];
while(bh&&bh.nodeType!==9&&(bi===I||bh.nodeType!==1||!b(bh).is(bi))){if(bh.nodeType===1){e.push(bh)
}bh=bh[bf]
}return e
},nth:function(bi,e,bg,bh){e=e||1;
var bf=0;
for(;
bi;
bi=bi[bg]){if(bi.nodeType===1&&++bf===e){break
}}return bi
},sibling:function(bg,bf){var e=[];
for(;
bg;
bg=bg.nextSibling){if(bg.nodeType===1&&bg!==bf){e.push(bg)
}}return e
}});
function aw(bh,bg,e){if(b.isFunction(bg)){return b.grep(bh,function(bj,bi){var bk=!!bg.call(bj,bi,bj);
return bk===e
})
}else{if(bg.nodeType){return b.grep(bh,function(bj,bi){return(bj===bg)===e
})
}else{if(typeof bg==="string"){var bf=b.grep(bh,function(bi){return bi.nodeType===1
});
if(bb.test(bg)){return b.filter(bg,bf,!e)
}else{bg=b.filter(bg,bf)
}}}}return b.grep(bh,function(bj,bi){return(b.inArray(bj,bg)>=0)===e
})
}var ac=/ jQuery\d+="(?:\d+|null)"/g,ak=/^\s+/,P=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig,d=/<([\w:]+)/,w=/<tbody/i,U=/<|&#?\w+;/,M=/<(?:script|object|embed|option|style)/i,n=/checked\s*(?:[^=]|=\s*.checked.)/i,ao={option:[1,"<select multiple='multiple'>","</select>"],legend:[1,"<fieldset>","</fieldset>"],thead:[1,"<table>","</table>"],tr:[2,"<table><tbody>","</tbody></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],col:[2,"<table><tbody></tbody><colgroup>","</colgroup></table>"],area:[1,"<map>","</map>"],_default:[0,"",""]};
ao.optgroup=ao.option;
ao.tbody=ao.tfoot=ao.colgroup=ao.caption=ao.thead;
ao.th=ao.td;
if(!b.support.htmlSerialize){ao._default=[1,"div<div>","</div>"]
}b.fn.extend({text:function(e){if(b.isFunction(e)){return this.each(function(bg){var bf=b(this);
bf.text(e.call(this,bg,bf.text()))
})
}if(typeof e!=="object"&&e!==I){return this.empty().append((this[0]&&this[0].ownerDocument||am).createTextNode(e))
}return b.text(this)
},wrapAll:function(e){if(b.isFunction(e)){return this.each(function(bg){b(this).wrapAll(e.call(this,bg))
})
}if(this[0]){var bf=b(e,this[0].ownerDocument).eq(0).clone(true);
if(this[0].parentNode){bf.insertBefore(this[0])
}bf.map(function(){var bg=this;
while(bg.firstChild&&bg.firstChild.nodeType===1){bg=bg.firstChild
}return bg
}).append(this)
}return this
},wrapInner:function(e){if(b.isFunction(e)){return this.each(function(bf){b(this).wrapInner(e.call(this,bf))
})
}return this.each(function(){var bf=b(this),bg=bf.contents();
if(bg.length){bg.wrapAll(e)
}else{bf.append(e)
}})
},wrap:function(e){return this.each(function(){b(this).wrapAll(e)
})
},unwrap:function(){return this.parent().each(function(){if(!b.nodeName(this,"body")){b(this).replaceWith(this.childNodes)
}}).end()
},append:function(){return this.domManip(arguments,true,function(e){if(this.nodeType===1){this.appendChild(e)
}})
},prepend:function(){return this.domManip(arguments,true,function(e){if(this.nodeType===1){this.insertBefore(e,this.firstChild)
}})
},before:function(){if(this[0]&&this[0].parentNode){return this.domManip(arguments,false,function(bf){this.parentNode.insertBefore(bf,this)
})
}else{if(arguments.length){var e=b(arguments[0]);
e.push.apply(e,this.toArray());
return this.pushStack(e,"before",arguments)
}}},after:function(){if(this[0]&&this[0].parentNode){return this.domManip(arguments,false,function(bf){this.parentNode.insertBefore(bf,this.nextSibling)
})
}else{if(arguments.length){var e=this.pushStack(this,"after",arguments);
e.push.apply(e,b(arguments[0]).toArray());
return e
}}},remove:function(e,bh){for(var bf=0,bg;
(bg=this[bf])!=null;
bf++){if(!e||b.filter(e,[bg]).length){if(!bh&&bg.nodeType===1){b.cleanData(bg.getElementsByTagName("*"));
b.cleanData([bg])
}if(bg.parentNode){bg.parentNode.removeChild(bg)
}}}return this
},empty:function(){for(var e=0,bf;
(bf=this[e])!=null;
e++){if(bf.nodeType===1){b.cleanData(bf.getElementsByTagName("*"))
}while(bf.firstChild){bf.removeChild(bf.firstChild)
}}return this
},clone:function(bf,e){bf=bf==null?false:bf;
e=e==null?bf:e;
return this.map(function(){return b.clone(this,bf,e)
})
},html:function(bh){if(bh===I){return this[0]&&this[0].nodeType===1?this[0].innerHTML.replace(ac,""):null
}else{if(typeof bh==="string"&&!M.test(bh)&&(b.support.leadingWhitespace||!ak.test(bh))&&!ao[(d.exec(bh)||["",""])[1].toLowerCase()]){bh=bh.replace(P,"<$1></$2>");
try{for(var bg=0,bf=this.length;
bg<bf;
bg++){if(this[bg].nodeType===1){b.cleanData(this[bg].getElementsByTagName("*"));
this[bg].innerHTML=bh
}}}catch(bi){this.empty().append(bh)
}}else{if(b.isFunction(bh)){this.each(function(bj){var e=b(this);
e.html(bh.call(this,bj,e.html()))
})
}else{this.empty().append(bh)
}}}return this
},replaceWith:function(e){if(this[0]&&this[0].parentNode){if(b.isFunction(e)){return this.each(function(bh){var bg=b(this),bf=bg.html();
bg.replaceWith(e.call(this,bh,bf))
})
}if(typeof e!=="string"){e=b(e).detach()
}return this.each(function(){var bg=this.nextSibling,bf=this.parentNode;
b(this).remove();
if(bg){b(bg).before(e)
}else{b(bf).append(e)
}})
}else{return this.length?this.pushStack(b(b.isFunction(e)?e():e),"replaceWith",e):this
}},detach:function(e){return this.remove(e,true)
},domManip:function(bl,bp,bo){var bh,bi,bk,bn,bm=bl[0],bf=[];
if(!b.support.checkClone&&arguments.length===3&&typeof bm==="string"&&n.test(bm)){return this.each(function(){b(this).domManip(bl,bp,bo,true)
})
}if(b.isFunction(bm)){return this.each(function(br){var bq=b(this);
bl[0]=bm.call(this,br,bp?bq.html():I);
bq.domManip(bl,bp,bo)
})
}if(this[0]){bn=bm&&bm.parentNode;
if(b.support.parentNode&&bn&&bn.nodeType===11&&bn.childNodes.length===this.length){bh={fragment:bn}
}else{bh=b.buildFragment(bl,this,bf)
}bk=bh.fragment;
if(bk.childNodes.length===1){bi=bk=bk.firstChild
}else{bi=bk.firstChild
}if(bi){bp=bp&&b.nodeName(bi,"tr");
for(var bg=0,e=this.length,bj=e-1;
bg<e;
bg++){bo.call(bp?aZ(this[bg],bi):this[bg],bh.cacheable||(e>1&&bg<bj)?b.clone(bk,true,true):bk)
}}if(bf.length){b.each(bf,ba)
}}return this
}});
function aZ(e,bf){return b.nodeName(e,"table")?(e.getElementsByTagName("tbody")[0]||e.appendChild(e.ownerDocument.createElement("tbody"))):e
}function t(e,bl){if(bl.nodeType!==1||!b.hasData(e)){return
}var bk=b.expando,bh=b.data(e),bi=b.data(bl,bh);
if((bh=bh[bk])){var bm=bh.events;
bi=bi[bk]=b.extend({},bh);
if(bm){delete bi.handle;
bi.events={};
for(var bj in bm){for(var bg=0,bf=bm[bj].length;
bg<bf;
bg++){b.event.add(bl,bj+(bm[bj][bg].namespace?".":"")+bm[bj][bg].namespace,bm[bj][bg],bm[bj][bg].data)
}}}}}function ad(bf,e){if(e.nodeType!==1){return
}var bg=e.nodeName.toLowerCase();
e.clearAttributes();
e.mergeAttributes(bf);
if(bg==="object"){e.outerHTML=bf.outerHTML
}else{if(bg==="input"&&(bf.type==="checkbox"||bf.type==="radio")){if(bf.checked){e.defaultChecked=e.checked=bf.checked
}if(e.value!==bf.value){e.value=bf.value
}}else{if(bg==="option"){e.selected=bf.defaultSelected
}else{if(bg==="input"||bg==="textarea"){e.defaultValue=bf.defaultValue
}}}}e.removeAttribute(b.expando)
}b.buildFragment=function(bj,bh,bf){var bi,e,bg,bk=(bh&&bh[0]?bh[0].ownerDocument||bh[0]:am);
if(bj.length===1&&typeof bj[0]==="string"&&bj[0].length<512&&bk===am&&bj[0].charAt(0)==="<"&&!M.test(bj[0])&&(b.support.checkClone||!n.test(bj[0]))){e=true;
bg=b.fragments[bj[0]];
if(bg){if(bg!==1){bi=bg
}}}if(!bi){bi=bk.createDocumentFragment();
b.clean(bj,bk,bi,bf)
}if(e){b.fragments[bj[0]]=bg?bi:1
}return{fragment:bi,cacheable:e}
};
b.fragments={};
b.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(e,bf){b.fn[e]=function(bg){var bj=[],bm=b(bg),bl=this.length===1&&this[0].parentNode;
if(bl&&bl.nodeType===11&&bl.childNodes.length===1&&bm.length===1){bm[bf](this[0]);
return this
}else{for(var bk=0,bh=bm.length;
bk<bh;
bk++){var bi=(bk>0?this.clone(true):this).get();
b(bm[bk])[bf](bi);
bj=bj.concat(bi)
}return this.pushStack(bj,e,bm.selector)
}}
});
function a3(e){if("getElementsByTagName" in e){return e.getElementsByTagName("*")
}else{if("querySelectorAll" in e){return e.querySelectorAll("*")
}else{return[]
}}}b.extend({clone:function(bi,bk,bg){var bj=bi.cloneNode(true),e,bf,bh;
if((!b.support.noCloneEvent||!b.support.noCloneChecked)&&(bi.nodeType===1||bi.nodeType===11)&&!b.isXMLDoc(bi)){ad(bi,bj);
e=a3(bi);
bf=a3(bj);
for(bh=0;
e[bh];
++bh){ad(e[bh],bf[bh])
}}if(bk){t(bi,bj);
if(bg){e=a3(bi);
bf=a3(bj);
for(bh=0;
e[bh];
++bh){t(e[bh],bf[bh])
}}}return bj
},clean:function(bg,bi,bp,bk){bi=bi||am;
if(typeof bi.createElement==="undefined"){bi=bi.ownerDocument||bi[0]&&bi[0].ownerDocument||am
}var bq=[];
for(var bo=0,bj;
(bj=bg[bo])!=null;
bo++){if(typeof bj==="number"){bj+=""
}if(!bj){continue
}if(typeof bj==="string"&&!U.test(bj)){bj=bi.createTextNode(bj)
}else{if(typeof bj==="string"){bj=bj.replace(P,"<$1></$2>");
var br=(d.exec(bj)||["",""])[1].toLowerCase(),bh=ao[br]||ao._default,bn=bh[0],bf=bi.createElement("div");
bf.innerHTML=bh[1]+bj+bh[2];
while(bn--){bf=bf.lastChild
}if(!b.support.tbody){var e=w.test(bj),bm=br==="table"&&!e?bf.firstChild&&bf.firstChild.childNodes:bh[1]==="<table>"&&!e?bf.childNodes:[];
for(var bl=bm.length-1;
bl>=0;
--bl){if(b.nodeName(bm[bl],"tbody")&&!bm[bl].childNodes.length){bm[bl].parentNode.removeChild(bm[bl])
}}}if(!b.support.leadingWhitespace&&ak.test(bj)){bf.insertBefore(bi.createTextNode(ak.exec(bj)[0]),bf.firstChild)
}bj=bf.childNodes
}}if(bj.nodeType){bq.push(bj)
}else{bq=b.merge(bq,bj)
}}if(bp){for(bo=0;
bq[bo];
bo++){if(bk&&b.nodeName(bq[bo],"script")&&(!bq[bo].type||bq[bo].type.toLowerCase()==="text/javascript")){bk.push(bq[bo].parentNode?bq[bo].parentNode.removeChild(bq[bo]):bq[bo])
}else{if(bq[bo].nodeType===1){bq.splice.apply(bq,[bo+1,0].concat(b.makeArray(bq[bo].getElementsByTagName("script"))))
}bp.appendChild(bq[bo])
}}}return bq
},cleanData:function(bf){var bi,bg,e=b.cache,bn=b.expando,bl=b.event.special,bk=b.support.deleteExpando;
for(var bj=0,bh;
(bh=bf[bj])!=null;
bj++){if(bh.nodeName&&b.noData[bh.nodeName.toLowerCase()]){continue
}bg=bh[b.expando];
if(bg){bi=e[bg]&&e[bg][bn];
if(bi&&bi.events){for(var bm in bi.events){if(bl[bm]){b.event.remove(bh,bm)
}else{b.removeEvent(bh,bm,bi.handle)
}}if(bi.handle){bi.handle.elem=null
}}if(bk){delete bh[b.expando]
}else{if(bh.removeAttribute){bh.removeAttribute(b.expando)
}}delete e[bg]
}}}});
function ba(e,bf){if(bf.src){b.ajax({url:bf.src,async:false,dataType:"script"})
}else{b.globalEval(bf.text||bf.textContent||bf.innerHTML||"")
}if(bf.parentNode){bf.parentNode.removeChild(bf)
}}var af=/alpha\([^)]*\)/i,al=/opacity=([^)]*)/,aO=/-([a-z])/ig,z=/([A-Z]|^ms)/g,a1=/^-?\d+(?:px)?$/i,a9=/^-?\d/,aX={position:"absolute",visibility:"hidden",display:"block"},ah=["Left","Right"],aT=["Top","Bottom"],V,az,aN,m=function(e,bf){return bf.toUpperCase()
};
b.fn.css=function(e,bf){if(arguments.length===2&&bf===I){return this
}return b.access(this,e,bf,true,function(bh,bg,bi){return bi!==I?b.style(bh,bg,bi):b.css(bh,bg)
})
};
b.extend({cssHooks:{opacity:{get:function(bg,bf){if(bf){var e=V(bg,"opacity","opacity");
return e===""?"1":e
}else{return bg.style.opacity
}}}},cssNumber:{"zIndex":true,"fontWeight":true,"opacity":true,"zoom":true,"lineHeight":true},cssProps:{"float":b.support.cssFloat?"cssFloat":"styleFloat"},style:function(bh,bg,bm,bi){if(!bh||bh.nodeType===3||bh.nodeType===8||!bh.style){return
}var bl,bj=b.camelCase(bg),bf=bh.style,bn=b.cssHooks[bj];
bg=b.cssProps[bj]||bj;
if(bm!==I){if(typeof bm==="number"&&isNaN(bm)||bm==null){return
}if(typeof bm==="number"&&!b.cssNumber[bj]){bm+="px"
}if(!bn||!("set" in bn)||(bm=bn.set(bh,bm))!==I){try{bf[bg]=bm
}catch(bk){}}}else{if(bn&&"get" in bn&&(bl=bn.get(bh,false,bi))!==I){return bl
}return bf[bg]
}},css:function(bj,bi,bf){var bh,bg=b.camelCase(bi),e=b.cssHooks[bg];
bi=b.cssProps[bg]||bg;
if(e&&"get" in e&&(bh=e.get(bj,true,bf))!==I){return bh
}else{if(V){return V(bj,bi,bg)
}}},swap:function(bh,bg,bi){var e={};
for(var bf in bg){e[bf]=bh.style[bf];
bh.style[bf]=bg[bf]
}bi.call(bh);
for(bf in bg){bh.style[bf]=e[bf]
}},camelCase:function(e){return e.replace(aO,m)
}});
b.curCSS=b.css;
b.each(["height","width"],function(bf,e){b.cssHooks[e]={get:function(bi,bh,bg){var bj;
if(bh){if(bi.offsetWidth!==0){bj=p(bi,e,bg)
}else{b.swap(bi,aX,function(){bj=p(bi,e,bg)
})
}if(bj<=0){bj=V(bi,e,e);
if(bj==="0px"&&aN){bj=aN(bi,e,e)
}if(bj!=null){return bj===""||bj==="auto"?"0px":bj
}}if(bj<0||bj==null){bj=bi.style[e];
return bj===""||bj==="auto"?"0px":bj
}return typeof bj==="string"?bj:bj+"px"
}},set:function(bg,bh){if(a1.test(bh)){bh=parseFloat(bh);
if(bh>=0){return bh+"px"
}}else{return bh
}}}
});
if(!b.support.opacity){b.cssHooks.opacity={get:function(bf,e){return al.test((e&&bf.currentStyle?bf.currentStyle.filter:bf.style.filter)||"")?(parseFloat(RegExp.$1)/100)+"":e?"1":""
},set:function(bh,bi){var bg=bh.style;
bg.zoom=1;
var e=b.isNaN(bi)?"":"alpha(opacity="+bi*100+")",bf=bg.filter||"";
bg.filter=af.test(bf)?bf.replace(af,e):bg.filter+" "+e
}}
}b(function(){if(!b.support.reliableMarginRight){b.cssHooks.marginRight={get:function(bg,bf){var e;
b.swap(bg,{"display":"inline-block"},function(){if(bf){e=V(bg,"margin-right","marginRight")
}else{e=bg.style.marginRight
}});
return e
}}
}});
if(am.defaultView&&am.defaultView.getComputedStyle){az=function(bj,e,bh){var bg,bi,bf;
bh=bh.replace(z,"-$1").toLowerCase();
if(!(bi=bj.ownerDocument.defaultView)){return I
}if((bf=bi.getComputedStyle(bj,null))){bg=bf.getPropertyValue(bh);
if(bg===""&&!b.contains(bj.ownerDocument.documentElement,bj)){bg=b.style(bj,bh)
}}return bg
}
}if(am.documentElement.currentStyle){aN=function(bi,bg){var bj,bf=bi.currentStyle&&bi.currentStyle[bg],e=bi.runtimeStyle&&bi.runtimeStyle[bg],bh=bi.style;
if(!a1.test(bf)&&a9.test(bf)){bj=bh.left;
if(e){bi.runtimeStyle.left=bi.currentStyle.left
}bh.left=bg==="fontSize"?"1em":(bf||0);
bf=bh.pixelLeft+"px";
bh.left=bj;
if(e){bi.runtimeStyle.left=e
}}return bf===""?"auto":bf
}
}V=az||aN;
function p(bg,bf,e){var bi=bf==="width"?ah:aT,bh=bf==="width"?bg.offsetWidth:bg.offsetHeight;
if(e==="border"){return bh
}b.each(bi,function(){if(!e){bh-=parseFloat(b.css(bg,"padding"+this))||0
}if(e==="margin"){bh+=parseFloat(b.css(bg,"margin"+this))||0
}else{bh-=parseFloat(b.css(bg,"border"+this+"Width"))||0
}});
return bh
}if(b.expr&&b.expr.filters){b.expr.filters.hidden=function(bg){var bf=bg.offsetWidth,e=bg.offsetHeight;
return(bf===0&&e===0)||(!b.support.reliableHiddenOffsets&&(bg.style.display||b.css(bg,"display"))==="none")
};
b.expr.filters.visible=function(e){return !b.expr.filters.hidden(e)
}
}var j=/%20/g,ai=/\[\]$/,be=/\r?\n/g,bc=/#.*$/,at=/^(.*?):[ \t]*([^\r\n]*)\r?$/mg,aQ=/^(?:color|date|datetime|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,aD=/^(?:about|app|app\-storage|.+\-extension|file|widget):$/,aF=/^(?:GET|HEAD)$/,c=/^\/\//,J=/\?/,aW=/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,q=/^(?:select|textarea)/i,h=/\s+/,bd=/([?&])_=[^&]*/,S=/(^|\-)([a-z])/g,aL=function(bf,e,bg){return e+bg.toUpperCase()
},H=/^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/,A=b.fn.load,W={},r={},av,s;
try{av=am.location.href
}catch(an){av=am.createElement("a");
av.href="";
av=av.href
}s=H.exec(av.toLowerCase())||[];
function f(e){return function(bi,bk){if(typeof bi!=="string"){bk=bi;
bi="*"
}if(b.isFunction(bk)){var bh=bi.toLowerCase().split(h),bg=0,bj=bh.length,bf,bl,bm;
for(;
bg<bj;
bg++){bf=bh[bg];
bm=/^\+/.test(bf);
if(bm){bf=bf.substr(1)||"*"
}bl=e[bf]=e[bf]||[];
bl[bm?"unshift":"push"](bk)
}}}
}function aK(bf,bo,bj,bn,bl,bh){bl=bl||bo.dataTypes[0];
bh=bh||{};
bh[bl]=true;
var bk=bf[bl],bg=0,e=bk?bk.length:0,bi=(bf===W),bm;
for(;
bg<e&&(bi||!bm);
bg++){bm=bk[bg](bo,bj,bn);
if(typeof bm==="string"){if(!bi||bh[bm]){bm=I
}else{bo.dataTypes.unshift(bm);
bm=aK(bf,bo,bj,bn,bm,bh)
}}}if((bi||!bm)&&!bh["*"]){bm=aK(bf,bo,bj,bn,"*",bh)
}return bm
}b.fn.extend({load:function(bg,bj,bk){if(typeof bg!=="string"&&A){return A.apply(this,arguments)
}else{if(!this.length){return this
}}var bi=bg.indexOf(" ");
if(bi>=0){var e=bg.slice(bi,bg.length);
bg=bg.slice(0,bi)
}var bh="GET";
if(bj){if(b.isFunction(bj)){bk=bj;
bj=I
}else{if(typeof bj==="object"){bj=b.param(bj,b.ajaxSettings.traditional);
bh="POST"
}}}var bf=this;
b.ajax({url:bg,type:bh,dataType:"html",data:bj,complete:function(bm,bl,bn){bn=bm.responseText;
if(bm.isResolved()){bm.done(function(bo){bn=bo
});
bf.html(e?b("<div>").append(bn.replace(aW,"")).find(e):bn)
}if(bk){bf.each(bk,[bn,bl,bm])
}}});
return this
},serialize:function(){return b.param(this.serializeArray())
},serializeArray:function(){return this.map(function(){return this.elements?b.makeArray(this.elements):this
}).filter(function(){return this.name&&!this.disabled&&(this.checked||q.test(this.nodeName)||aQ.test(this.type))
}).map(function(e,bf){var bg=b(this).val();
return bg==null?null:b.isArray(bg)?b.map(bg,function(bi,bh){return{name:bf.name,value:bi.replace(be,"\r\n")}
}):{name:bf.name,value:bg.replace(be,"\r\n")}
}).get()
}});
b.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "),function(e,bf){b.fn[bf]=function(bg){return this.bind(bf,bg)
}
});
b.each(["get","post"],function(e,bf){b[bf]=function(bg,bi,bj,bh){if(b.isFunction(bi)){bh=bh||bj;
bj=bi;
bi=I
}return b.ajax({type:bf,url:bg,data:bi,success:bj,dataType:bh})
}
});
b.extend({getScript:function(e,bf){return b.get(e,I,bf,"script")
},getJSON:function(e,bf,bg){return b.get(e,bf,bg,"json")
},ajaxSetup:function(bg,e){if(!e){e=bg;
bg=b.extend(true,b.ajaxSettings,e)
}else{b.extend(true,bg,b.ajaxSettings,e)
}for(var bf in {context:1,url:1}){if(bf in e){bg[bf]=e[bf]
}else{if(bf in b.ajaxSettings){bg[bf]=b.ajaxSettings[bf]
}}}return bg
},ajaxSettings:{url:av,isLocal:aD.test(s[1]),global:true,type:"GET",contentType:"application/x-www-form-urlencoded",processData:true,async:true,accepts:{xml:"application/xml, text/xml",html:"text/html",text:"text/plain",json:"application/json, text/javascript","*":"*/*"},contents:{xml:/xml/,html:/html/,json:/json/},responseFields:{xml:"responseXML",text:"responseText"},converters:{"* text":a0.String,"text html":true,"text json":b.parseJSON,"text xml":b.parseXML}},ajaxPrefilter:f(W),ajaxTransport:f(r),ajax:function(bj,bh){if(typeof bj==="object"){bh=bj;
bj=I
}bh=bh||{};
var bn=b.ajaxSetup({},bh),bB=bn.context||bn,bq=bB!==bn&&(bB.nodeType||bB instanceof b)?b(bB):b.event,bA=b.Deferred(),bx=b._Deferred(),bl=bn.statusCode||{},bm,br={},bz,bi,bv,bo,bs,bk=0,bg,bu,bt={readyState:0,setRequestHeader:function(e,bC){if(!bk){br[e.toLowerCase().replace(S,aL)]=bC
}return this
},getAllResponseHeaders:function(){return bk===2?bz:null
},getResponseHeader:function(bC){var e;
if(bk===2){if(!bi){bi={};
while((e=at.exec(bz))){bi[e[1].toLowerCase()]=e[2]
}}e=bi[bC.toLowerCase()]
}return e===I?null:e
},overrideMimeType:function(e){if(!bk){bn.mimeType=e
}return this
},abort:function(e){e=e||"abort";
if(bv){bv.abort(e)
}bp(0,e);
return this
}};
function bp(bH,bF,bI,bE){if(bk===2){return
}bk=2;
if(bo){clearTimeout(bo)
}bv=I;
bz=bE||"";
bt.readyState=bH?4:0;
var bC,bM,bL,bG=bI?a6(bn,bt,bI):I,bD,bK;
if(bH>=200&&bH<300||bH===304){if(bn.ifModified){if((bD=bt.getResponseHeader("Last-Modified"))){b.lastModified[bm]=bD
}if((bK=bt.getResponseHeader("Etag"))){b.etag[bm]=bK
}}if(bH===304){bF="notmodified";
bC=true
}else{try{bM=E(bn,bG);
bF="success";
bC=true
}catch(bJ){bF="parsererror";
bL=bJ
}}}else{bL=bF;
if(!bF||bH){bF="error";
if(bH<0){bH=0
}}}bt.status=bH;
bt.statusText=bF;
if(bC){bA.resolveWith(bB,[bM,bF,bt])
}else{bA.rejectWith(bB,[bt,bF,bL])
}bt.statusCode(bl);
bl=I;
if(bg){bq.trigger("ajax"+(bC?"Success":"Error"),[bt,bn,bC?bM:bL])
}bx.resolveWith(bB,[bt,bF]);
if(bg){bq.trigger("ajaxComplete",[bt,bn]);
if(!(--b.active)){b.event.trigger("ajaxStop")
}}}bA.promise(bt);
bt.success=bt.done;
bt.error=bt.fail;
bt.complete=bx.done;
bt.statusCode=function(bC){if(bC){var e;
if(bk<2){for(e in bC){bl[e]=[bl[e],bC[e]]
}}else{e=bC[bt.status];
bt.then(e,e)
}}return this
};
bn.url=((bj||bn.url)+"").replace(bc,"").replace(c,s[1]+"//");
bn.dataTypes=b.trim(bn.dataType||"*").toLowerCase().split(h);
if(bn.crossDomain==null){bs=H.exec(bn.url.toLowerCase());
bn.crossDomain=!!(bs&&(bs[1]!=s[1]||bs[2]!=s[2]||(bs[3]||(bs[1]==="http:"?80:443))!=(s[3]||(s[1]==="http:"?80:443))))
}if(bn.data&&bn.processData&&typeof bn.data!=="string"){bn.data=b.param(bn.data,bn.traditional)
}aK(W,bn,bh,bt);
if(bk===2){return false
}bg=bn.global;
bn.type=bn.type.toUpperCase();
bn.hasContent=!aF.test(bn.type);
if(bg&&b.active++===0){b.event.trigger("ajaxStart")
}if(!bn.hasContent){if(bn.data){bn.url+=(J.test(bn.url)?"&":"?")+bn.data
}bm=bn.url;
if(bn.cache===false){var bf=b.now(),by=bn.url.replace(bd,"$1_="+bf);
bn.url=by+((by===bn.url)?(J.test(bn.url)?"&":"?")+"_="+bf:"")
}}if(bn.data&&bn.hasContent&&bn.contentType!==false||bh.contentType){br["Content-Type"]=bn.contentType
}if(bn.ifModified){bm=bm||bn.url;
if(b.lastModified[bm]){br["If-Modified-Since"]=b.lastModified[bm]
}if(b.etag[bm]){br["If-None-Match"]=b.etag[bm]
}}br.Accept=bn.dataTypes[0]&&bn.accepts[bn.dataTypes[0]]?bn.accepts[bn.dataTypes[0]]+(bn.dataTypes[0]!=="*"?", */*; q=0.01":""):bn.accepts["*"];
for(bu in bn.headers){bt.setRequestHeader(bu,bn.headers[bu])
}if(bn.beforeSend&&(bn.beforeSend.call(bB,bt,bn)===false||bk===2)){bt.abort();
return false
}for(bu in {success:1,error:1,complete:1}){bt[bu](bn[bu])
}bv=aK(r,bn,bh,bt);
if(!bv){bp(-1,"No Transport")
}else{bt.readyState=1;
if(bg){bq.trigger("ajaxSend",[bt,bn])
}if(bn.async&&bn.timeout>0){bo=setTimeout(function(){bt.abort("timeout")
},bn.timeout)
}try{bk=1;
bv.send(br,bp)
}catch(bw){if(status<2){bp(-1,bw)
}else{b.error(bw)
}}}return bt
},param:function(e,bg){var bf=[],bi=function(bj,bk){bk=b.isFunction(bk)?bk():bk;
bf[bf.length]=encodeURIComponent(bj)+"="+encodeURIComponent(bk)
};
if(bg===I){bg=b.ajaxSettings.traditional
}if(b.isArray(e)||(e.jquery&&!b.isPlainObject(e))){b.each(e,function(){bi(this.name,this.value)
})
}else{for(var bh in e){v(bh,e[bh],bg,bi)
}}return bf.join("&").replace(j,"+")
}});
function v(bg,bi,bf,bh){if(b.isArray(bi)&&bi.length){b.each(bi,function(bk,bj){if(bf||ai.test(bg)){bh(bg,bj)
}else{v(bg+"["+(typeof bj==="object"||b.isArray(bj)?bk:"")+"]",bj,bf,bh)
}})
}else{if(!bf&&bi!=null&&typeof bi==="object"){if(b.isArray(bi)||b.isEmptyObject(bi)){bh(bg,"")
}else{for(var e in bi){v(bg+"["+e+"]",bi[e],bf,bh)
}}}else{bh(bg,bi)
}}}b.extend({active:0,lastModified:{},etag:{}});
function a6(bn,bm,bj){var bf=bn.contents,bl=bn.dataTypes,bg=bn.responseFields,bi,bk,bh,e;
for(bk in bg){if(bk in bj){bm[bg[bk]]=bj[bk]
}}while(bl[0]==="*"){bl.shift();
if(bi===I){bi=bn.mimeType||bm.getResponseHeader("content-type")
}}if(bi){for(bk in bf){if(bf[bk]&&bf[bk].test(bi)){bl.unshift(bk);
break
}}}if(bl[0] in bj){bh=bl[0]
}else{for(bk in bj){if(!bl[0]||bn.converters[bk+" "+bl[0]]){bh=bk;
break
}if(!e){e=bk
}}bh=bh||e
}if(bh){if(bh!==bl[0]){bl.unshift(bh)
}return bj[bh]
}}function E(br,bj){if(br.dataFilter){bj=br.dataFilter(bj,br.dataType)
}var bn=br.dataTypes,bq={},bk,bo,bg=bn.length,bl,bm=bn[0],bh,bi,bp,bf,e;
for(bk=1;
bk<bg;
bk++){if(bk===1){for(bo in br.converters){if(typeof bo==="string"){bq[bo.toLowerCase()]=br.converters[bo]
}}}bh=bm;
bm=bn[bk];
if(bm==="*"){bm=bh
}else{if(bh!=="*"&&bh!==bm){bi=bh+" "+bm;
bp=bq[bi]||bq["* "+bm];
if(!bp){e=I;
for(bf in bq){bl=bf.split(" ");
if(bl[0]===bh||bl[0]==="*"){e=bq[bl[1]+" "+bm];
if(e){bf=bq[bf];
if(bf===true){bp=e
}else{if(e===true){bp=bf
}}break
}}}}if(!(bp||e)){b.error("No conversion from "+bi.replace(" "," to "))
}if(bp!==true){bj=bp?bp(bj):e(bf(bj))
}}}}return bj
}var ar=b.now(),u=/(\=)\?(&|$)|\?\?/i;
b.ajaxSetup({jsonp:"callback",jsonpCallback:function(){return b.expando+"_"+(ar++)
}});
b.ajaxPrefilter("json jsonp",function(bo,bk,bn){var bm=(typeof bo.data==="string");
if(bo.dataTypes[0]==="jsonp"||bk.jsonpCallback||bk.jsonp!=null||bo.jsonp!==false&&(u.test(bo.url)||bm&&u.test(bo.data))){var bl,bg=bo.jsonpCallback=b.isFunction(bo.jsonpCallback)?bo.jsonpCallback():bo.jsonpCallback,bj=a0[bg],e=bo.url,bi=bo.data,bf="$1"+bg+"$2",bh=function(){a0[bg]=bj;
if(bl&&b.isFunction(bj)){a0[bg](bl[0])
}};
if(bo.jsonp!==false){e=e.replace(u,bf);
if(bo.url===e){if(bm){bi=bi.replace(u,bf)
}if(bo.data===bi){e+=(/\?/.test(e)?"&":"?")+bo.jsonp+"="+bg
}}}bo.url=e;
bo.data=bi;
a0[bg]=function(bp){bl=[bp]
};
bn.then(bh,bh);
bo.converters["script json"]=function(){if(!bl){b.error(bg+" was not called")
}return bl[0]
};
bo.dataTypes[0]="json";
return"script"
}});
b.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/javascript|ecmascript/},converters:{"text script":function(e){b.globalEval(e);
return e
}}});
b.ajaxPrefilter("script",function(e){if(e.cache===I){e.cache=false
}if(e.crossDomain){e.type="GET";
e.global=false
}});
b.ajaxTransport("script",function(bg){if(bg.crossDomain){var e,bf=am.head||am.getElementsByTagName("head")[0]||am.documentElement;
return{send:function(bh,bi){e=am.createElement("script");
e.async="async";
if(bg.scriptCharset){e.charset=bg.scriptCharset
}e.src=bg.url;
e.onload=e.onreadystatechange=function(bk,bj){if(!e.readyState||/loaded|complete/.test(e.readyState)){e.onload=e.onreadystatechange=null;
if(bf&&e.parentNode){bf.removeChild(e)
}e=I;
if(!bj){bi(200,"success")
}}};
bf.insertBefore(e,bf.firstChild)
},abort:function(){if(e){e.onload(0,1)
}}}
}});
var y=b.now(),K,au;
function B(){b(a0).unload(function(){for(var e in K){K[e](0,1)
}})
}function aC(){try{return new a0.XMLHttpRequest()
}catch(bf){}}function ae(){try{return new a0.ActiveXObject("Microsoft.XMLHTTP")
}catch(bf){}}b.ajaxSettings.xhr=a0.ActiveXObject?function(){return !this.isLocal&&aC()||ae()
}:aC;
au=b.ajaxSettings.xhr();
b.support.ajax=!!au;
b.support.cors=au&&("withCredentials" in au);
au=I;
if(b.support.ajax){b.ajaxTransport(function(e){if(!e.crossDomain||b.support.cors){var bf;
return{send:function(bl,bg){var bk=e.xhr(),bj,bi;
if(e.username){bk.open(e.type,e.url,e.async,e.username,e.password)
}else{bk.open(e.type,e.url,e.async)
}if(e.xhrFields){for(bi in e.xhrFields){bk[bi]=e.xhrFields[bi]
}}if(e.mimeType&&bk.overrideMimeType){bk.overrideMimeType(e.mimeType)
}if(!e.crossDomain&&!bl["X-Requested-With"]){bl["X-Requested-With"]="XMLHttpRequest"
}try{for(bi in bl){bk.setRequestHeader(bi,bl[bi])
}}catch(bh){}bk.send((e.hasContent&&e.data)||null);
bf=function(bu,bo){var bp,bn,bm,bs,br;
try{if(bf&&(bo||bk.readyState===4)){bf=I;
if(bj){bk.onreadystatechange=b.noop;
delete K[bj]
}if(bo){if(bk.readyState!==4){bk.abort()
}}else{bp=bk.status;
bm=bk.getAllResponseHeaders();
bs={};
br=bk.responseXML;
if(br&&br.documentElement){bs.xml=br
}bs.text=bk.responseText;
try{bn=bk.statusText
}catch(bt){bn=""
}if(!bp&&e.isLocal&&!e.crossDomain){bp=bs.text?200:404
}else{if(bp===1223){bp=204
}}}}}catch(bq){if(!bo){bg(-1,bq)
}}if(bs){bg(bp,bn,bs,bm)
}};
if(!e.async||bk.readyState===4){bf()
}else{if(!K){K={};
B()
}bj=y++;
bk.onreadystatechange=K[bj]=bf
}},abort:function(){if(bf){bf(0,1)
}}}
}})
}var O={},aq=/^(?:toggle|show|hide)$/,aH=/^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i,aU,ay=[["height","marginTop","marginBottom","paddingTop","paddingBottom"],["width","marginLeft","marginRight","paddingLeft","paddingRight"],["opacity"]];
b.fn.extend({show:function(bh,bk,bj){var bg,bi;
if(bh||bh===0){return this.animate(aS("show",3),bh,bk,bj)
}else{for(var bf=0,e=this.length;
bf<e;
bf++){bg=this[bf];
bi=bg.style.display;
if(!b._data(bg,"olddisplay")&&bi==="none"){bi=bg.style.display=""
}if(bi===""&&b.css(bg,"display")==="none"){b._data(bg,"olddisplay",x(bg.nodeName))
}}for(bf=0;
bf<e;
bf++){bg=this[bf];
bi=bg.style.display;
if(bi===""||bi==="none"){bg.style.display=b._data(bg,"olddisplay")||""
}}return this
}},hide:function(bg,bj,bi){if(bg||bg===0){return this.animate(aS("hide",3),bg,bj,bi)
}else{for(var bf=0,e=this.length;
bf<e;
bf++){var bh=b.css(this[bf],"display");
if(bh!=="none"&&!b._data(this[bf],"olddisplay")){b._data(this[bf],"olddisplay",bh)
}}for(bf=0;
bf<e;
bf++){this[bf].style.display="none"
}return this
}},_toggle:b.fn.toggle,toggle:function(bg,bf,bh){var e=typeof bg==="boolean";
if(b.isFunction(bg)&&b.isFunction(bf)){this._toggle.apply(this,arguments)
}else{if(bg==null||e){this.each(function(){var bi=e?bg:b(this).is(":hidden");
b(this)[bi?"show":"hide"]()
})
}else{this.animate(aS("toggle",3),bg,bf,bh)
}}return this
},fadeTo:function(e,bh,bg,bf){return this.filter(":hidden").css("opacity",0).show().end().animate({opacity:bh},e,bg,bf)
},animate:function(bi,bf,bh,bg){var e=b.speed(bf,bh,bg);
if(b.isEmptyObject(bi)){return this.each(e.complete)
}return this[e.queue===false?"each":"queue"](function(){var bl=b.extend({},e),bp,bm=this.nodeType===1,bn=bm&&b(this).is(":hidden"),bj=this;
for(bp in bi){var bk=b.camelCase(bp);
if(bp!==bk){bi[bk]=bi[bp];
delete bi[bp];
bp=bk
}if(bi[bp]==="hide"&&bn||bi[bp]==="show"&&!bn){return bl.complete.call(this)
}if(bm&&(bp==="height"||bp==="width")){bl.overflow=[this.style.overflow,this.style.overflowX,this.style.overflowY];
if(b.css(this,"display")==="inline"&&b.css(this,"float")==="none"){if(!b.support.inlineBlockNeedsLayout){this.style.display="inline-block"
}else{var bo=x(this.nodeName);
if(bo==="inline"){this.style.display="inline-block"
}else{this.style.display="inline";
this.style.zoom=1
}}}}if(b.isArray(bi[bp])){(bl.specialEasing=bl.specialEasing||{})[bp]=bi[bp][1];
bi[bp]=bi[bp][0]
}}if(bl.overflow!=null){this.style.overflow="hidden"
}bl.curAnim=b.extend({},bi);
b.each(bi,function(br,bv){var bu=new b.fx(bj,bl,br);
if(aq.test(bv)){bu[bv==="toggle"?bn?"show":"hide":bv](bi)
}else{var bt=aH.exec(bv),bw=bu.cur();
if(bt){var bq=parseFloat(bt[2]),bs=bt[3]||(b.cssNumber[br]?"":"px");
if(bs!=="px"){b.style(bj,br,(bq||1)+bs);
bw=((bq||1)/bu.cur())*bw;
b.style(bj,br,bw+bs)
}if(bt[1]){bq=((bt[1]==="-="?-1:1)*bq)+bw
}bu.custom(bw,bq,bs)
}else{bu.custom(bw,bv,"")
}}});
return true
})
},stop:function(bf,e){var bg=b.timers;
if(bf){this.queue([])
}this.each(function(){for(var bh=bg.length-1;
bh>=0;
bh--){if(bg[bh].elem===this){if(e){bg[bh](true)
}bg.splice(bh,1)
}}});
if(!e){this.dequeue()
}return this
}});
function aS(bf,e){var bg={};
b.each(ay.concat.apply([],ay.slice(0,e)),function(){bg[this]=bf
});
return bg
}b.each({slideDown:aS("show",1),slideUp:aS("hide",1),slideToggle:aS("toggle",1),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(e,bf){b.fn[e]=function(bg,bi,bh){return this.animate(bf,bg,bi,bh)
}
});
b.extend({speed:function(bg,bh,bf){var e=bg&&typeof bg==="object"?b.extend({},bg):{complete:bf||!bf&&bh||b.isFunction(bg)&&bg,duration:bg,easing:bf&&bh||bh&&!b.isFunction(bh)&&bh};
e.duration=b.fx.off?0:typeof e.duration==="number"?e.duration:e.duration in b.fx.speeds?b.fx.speeds[e.duration]:b.fx.speeds._default;
e.old=e.complete;
e.complete=function(){if(e.queue!==false){b(this).dequeue()
}if(b.isFunction(e.old)){e.old.call(this)
}};
return e
},easing:{linear:function(bg,bh,e,bf){return e+bf*bg
},swing:function(bg,bh,e,bf){return((-Math.cos(bg*Math.PI)/2)+0.5)*bf+e
}},timers:[],fx:function(bf,e,bg){this.options=e;
this.elem=bf;
this.prop=bg;
if(!e.orig){e.orig={}
}}});
b.fx.prototype={update:function(){if(this.options.step){this.options.step.call(this.elem,this.now,this)
}(b.fx.step[this.prop]||b.fx.step._default)(this)
},cur:function(){if(this.elem[this.prop]!=null&&(!this.elem.style||this.elem.style[this.prop]==null)){return this.elem[this.prop]
}var e,bf=b.css(this.elem,this.prop);
return isNaN(e=parseFloat(bf))?!bf||bf==="auto"?0:bf:e
},custom:function(bj,bi,bh){var e=this,bg=b.fx;
this.startTime=b.now();
this.start=bj;
this.end=bi;
this.unit=bh||this.unit||(b.cssNumber[this.prop]?"":"px");
this.now=this.start;
this.pos=this.state=0;
function bf(bk){return e.step(bk)
}bf.elem=this.elem;
if(bf()&&b.timers.push(bf)&&!aU){aU=setInterval(bg.tick,bg.interval)
}},show:function(){this.options.orig[this.prop]=b.style(this.elem,this.prop);
this.options.show=true;
this.custom(this.prop==="width"||this.prop==="height"?1:0,this.cur());
b(this.elem).show()
},hide:function(){this.options.orig[this.prop]=b.style(this.elem,this.prop);
this.options.hide=true;
this.custom(this.cur(),0)
},step:function(bh){var bm=b.now(),bi=true;
if(bh||bm>=this.options.duration+this.startTime){this.now=this.end;
this.pos=this.state=1;
this.update();
this.options.curAnim[this.prop]=true;
for(var bj in this.options.curAnim){if(this.options.curAnim[bj]!==true){bi=false
}}if(bi){if(this.options.overflow!=null&&!b.support.shrinkWrapBlocks){var bg=this.elem,bn=this.options;
b.each(["","X","Y"],function(bo,bp){bg.style["overflow"+bp]=bn.overflow[bo]
})
}if(this.options.hide){b(this.elem).hide()
}if(this.options.hide||this.options.show){for(var e in this.options.curAnim){b.style(this.elem,e,this.options.orig[e])
}}this.options.complete.call(this.elem)
}return false
}else{var bf=bm-this.startTime;
this.state=bf/this.options.duration;
var bk=this.options.specialEasing&&this.options.specialEasing[this.prop];
var bl=this.options.easing||(b.easing.swing?"swing":"linear");
this.pos=b.easing[bk||bl](this.state,bf,0,1,this.options.duration);
this.now=this.start+((this.end-this.start)*this.pos);
this.update()
}return true
}};
b.extend(b.fx,{tick:function(){var bf=b.timers;
for(var e=0;
e<bf.length;
e++){if(!bf[e]()){bf.splice(e--,1)
}}if(!bf.length){b.fx.stop()
}},interval:13,stop:function(){clearInterval(aU);
aU=null
},speeds:{slow:600,fast:200,_default:400},step:{opacity:function(e){b.style(e.elem,"opacity",e.now)
},_default:function(e){if(e.elem.style&&e.elem.style[e.prop]!=null){e.elem.style[e.prop]=(e.prop==="width"||e.prop==="height"?Math.max(0,e.now):e.now)+e.unit
}else{e.elem[e.prop]=e.now
}}}});
if(b.expr&&b.expr.filters){b.expr.filters.animated=function(e){return b.grep(b.timers,function(bf){return e===bf.elem
}).length
}
}function x(bg){if(!O[bg]){var e=b("<"+bg+">").appendTo("body"),bf=e.css("display");
e.remove();
if(bf==="none"||bf===""){bf="block"
}O[bg]=bf
}return O[bg]
}var T=/^t(?:able|d|h)$/i,Z=/^(?:body|html)$/i;
if("getBoundingClientRect" in am.documentElement){b.fn.offset=function(bs){var bi=this[0],bl;
if(bs){return this.each(function(e){b.offset.setOffset(this,bs,e)
})
}if(!bi||!bi.ownerDocument){return null
}if(bi===bi.ownerDocument.body){return b.offset.bodyOffset(bi)
}try{bl=bi.getBoundingClientRect()
}catch(bp){}var br=bi.ownerDocument,bg=br.documentElement;
if(!bl||!b.contains(bg,bi)){return bl?{top:bl.top,left:bl.left}:{top:0,left:0}
}var bm=br.body,bn=aB(br),bk=bg.clientTop||bm.clientTop||0,bo=bg.clientLeft||bm.clientLeft||0,bf=bn.pageYOffset||b.support.boxModel&&bg.scrollTop||bm.scrollTop,bj=bn.pageXOffset||b.support.boxModel&&bg.scrollLeft||bm.scrollLeft,bq=bl.top+bf-bk,bh=bl.left+bj-bo;
return{top:bq,left:bh}
}
}else{b.fn.offset=function(bp){var bj=this[0];
if(bp){return this.each(function(bq){b.offset.setOffset(this,bp,bq)
})
}if(!bj||!bj.ownerDocument){return null
}if(bj===bj.ownerDocument.body){return b.offset.bodyOffset(bj)
}b.offset.initialize();
var bm,bg=bj.offsetParent,bf=bj,bo=bj.ownerDocument,bh=bo.documentElement,bk=bo.body,bl=bo.defaultView,e=bl?bl.getComputedStyle(bj,null):bj.currentStyle,bn=bj.offsetTop,bi=bj.offsetLeft;
while((bj=bj.parentNode)&&bj!==bk&&bj!==bh){if(b.offset.supportsFixedPosition&&e.position==="fixed"){break
}bm=bl?bl.getComputedStyle(bj,null):bj.currentStyle;
bn-=bj.scrollTop;
bi-=bj.scrollLeft;
if(bj===bg){bn+=bj.offsetTop;
bi+=bj.offsetLeft;
if(b.offset.doesNotAddBorder&&!(b.offset.doesAddBorderForTableAndCells&&T.test(bj.nodeName))){bn+=parseFloat(bm.borderTopWidth)||0;
bi+=parseFloat(bm.borderLeftWidth)||0
}bf=bg;
bg=bj.offsetParent
}if(b.offset.subtractsBorderForOverflowNotVisible&&bm.overflow!=="visible"){bn+=parseFloat(bm.borderTopWidth)||0;
bi+=parseFloat(bm.borderLeftWidth)||0
}e=bm
}if(e.position==="relative"||e.position==="static"){bn+=bk.offsetTop;
bi+=bk.offsetLeft
}if(b.offset.supportsFixedPosition&&e.position==="fixed"){bn+=Math.max(bh.scrollTop,bk.scrollTop);
bi+=Math.max(bh.scrollLeft,bk.scrollLeft)
}return{top:bn,left:bi}
}
}b.offset={initialize:function(){var e=am.body,bf=am.createElement("div"),bi,bk,bj,bl,bg=parseFloat(b.css(e,"marginTop"))||0,bh="<div style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;'><div></div></div><table style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>";
b.extend(bf.style,{position:"absolute",top:0,left:0,margin:0,border:0,width:"1px",height:"1px",visibility:"hidden"});
bf.innerHTML=bh;
e.insertBefore(bf,e.firstChild);
bi=bf.firstChild;
bk=bi.firstChild;
bl=bi.nextSibling.firstChild.firstChild;
this.doesNotAddBorder=(bk.offsetTop!==5);
this.doesAddBorderForTableAndCells=(bl.offsetTop===5);
bk.style.position="fixed";
bk.style.top="20px";
this.supportsFixedPosition=(bk.offsetTop===20||bk.offsetTop===15);
bk.style.position=bk.style.top="";
bi.style.overflow="hidden";
bi.style.position="relative";
this.subtractsBorderForOverflowNotVisible=(bk.offsetTop===-5);
this.doesNotIncludeMarginInBodyOffset=(e.offsetTop!==bg);
e.removeChild(bf);
b.offset.initialize=b.noop
},bodyOffset:function(e){var bg=e.offsetTop,bf=e.offsetLeft;
b.offset.initialize();
if(b.offset.doesNotIncludeMarginInBodyOffset){bg+=parseFloat(b.css(e,"marginTop"))||0;
bf+=parseFloat(b.css(e,"marginLeft"))||0
}return{top:bg,left:bf}
},setOffset:function(bh,bq,bk){var bl=b.css(bh,"position");
if(bl==="static"){bh.style.position="relative"
}var bj=b(bh),bf=bj.offset(),e=b.css(bh,"top"),bo=b.css(bh,"left"),bp=(bl==="absolute"||bl==="fixed")&&b.inArray("auto",[e,bo])>-1,bn={},bm={},bg,bi;
if(bp){bm=bj.position()
}bg=bp?bm.top:parseInt(e,10)||0;
bi=bp?bm.left:parseInt(bo,10)||0;
if(b.isFunction(bq)){bq=bq.call(bh,bk,bf)
}if(bq.top!=null){bn.top=(bq.top-bf.top)+bg
}if(bq.left!=null){bn.left=(bq.left-bf.left)+bi
}if("using" in bq){bq.using.call(bh,bn)
}else{bj.css(bn)
}}};
b.fn.extend({position:function(){if(!this[0]){return null
}var bg=this[0],bf=this.offsetParent(),bh=this.offset(),e=Z.test(bf[0].nodeName)?{top:0,left:0}:bf.offset();
bh.top-=parseFloat(b.css(bg,"marginTop"))||0;
bh.left-=parseFloat(b.css(bg,"marginLeft"))||0;
e.top+=parseFloat(b.css(bf[0],"borderTopWidth"))||0;
e.left+=parseFloat(b.css(bf[0],"borderLeftWidth"))||0;
return{top:bh.top-e.top,left:bh.left-e.left}
},offsetParent:function(){return this.map(function(){var e=this.offsetParent||am.body;
while(e&&(!Z.test(e.nodeName)&&b.css(e,"position")==="static")){e=e.offsetParent
}return e
})
}});
b.each(["Left","Top"],function(bf,e){var bg="scroll"+e;
b.fn[bg]=function(bj){var bh=this[0],bi;
if(!bh){return null
}if(bj!==I){return this.each(function(){bi=aB(this);
if(bi){bi.scrollTo(!bf?bj:b(bi).scrollLeft(),bf?bj:b(bi).scrollTop())
}else{this[bg]=bj
}})
}else{bi=aB(bh);
return bi?("pageXOffset" in bi)?bi[bf?"pageYOffset":"pageXOffset"]:b.support.boxModel&&bi.document.documentElement[bg]||bi.document.body[bg]:bh[bg]
}}
});
function aB(e){return b.isWindow(e)?e:e.nodeType===9?e.defaultView||e.parentWindow:false
}b.each(["Height","Width"],function(bf,e){var bg=e.toLowerCase();
b.fn["inner"+e]=function(){return this[0]?parseFloat(b.css(this[0],bg,"padding")):null
};
b.fn["outer"+e]=function(bh){return this[0]?parseFloat(b.css(this[0],bg,bh?"margin":"border")):null
};
b.fn[bg]=function(bi){var bj=this[0];
if(!bj){return bi==null?null:this
}if(b.isFunction(bi)){return this.each(function(bn){var bm=b(this);
bm[bg](bi.call(this,bn,bm[bg]()))
})
}if(b.isWindow(bj)){var bk=bj.document.documentElement["client"+e];
return bj.document.compatMode==="CSS1Compat"&&bk||bj.document.body["client"+e]||bk
}else{if(bj.nodeType===9){return Math.max(bj.documentElement["client"+e],bj.body["scroll"+e],bj.documentElement["scroll"+e],bj.body["offset"+e],bj.documentElement["offset"+e])
}else{if(bi===I){var bl=b.css(bj,bg),bh=parseFloat(bl);
return b.isNaN(bh)?bl:bh
}else{return this.css(bg,typeof bi==="string"?bi:bi+"px")
}}}}
});
a0.jQuery=a0.$=b
})(window);
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
(function(c){var h,i;
var d=0;
var a=32;
var e;
c.fn.TextAreaResizer=function(){return this.each(function(){if(!c(this).hasClass("textarea-resizer-processed")){c(this).addClass("textarea-resizer-processed");
c(this).wrap('<div class="resizable-textarea"><span></span></div>').parent().append(c('<div class="grippie"></div>'))
}h=c(this);
i=null;
var k=c("div.grippie",c(this).parent())[0];
k.style.marginRight=(k.offsetWidth-c(this)[0].offsetWidth)+"px";
c(k).bind("mousedown",{el:this},b)
})
};
function b(k){h=c(k.data.el);
h.blur();
d=j(k).y;
i=h.height()-d;
h.css("opacity",0.25);
c(document).mousemove(g).mouseup(f);
return false
}function g(m){var k=j(m).y;
var l=i+k;
if(d>=(k)){l-=5
}d=k;
l=Math.max(a,l);
h.height(l+"px");
if(l<a){f(m)
}return false
}function f(k){c(document).unbind("mousemove",g).unbind("mouseup",f);
h.css("opacity",1);
h.focus();
h=null;
i=null;
d=0
}function j(k){return{x:k.clientX+document.documentElement.scrollLeft,y:k.clientY+document.documentElement.scrollTop}
}})(jQuery);
/*
 * jQuery corner plugin: simple corner rounding
 * Examples and documentation at: http://jquery.malsup.com/corner/
 * version 2.11 (15-JUN-2010)
 * Requires jQuery v1.3.2 or later
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * Authors: Dave Methvin and Mike Alsup
 */
(function(c){var a=document.createElement("div").style,h=a["MozBorderRadius"]!==undefined,j=a["WebkitBorderRadius"]!==undefined,e=a["borderRadius"]!==undefined||a["BorderRadius"]!==undefined,d=document.documentMode||0,l=c.browser.msie&&((c.browser.version<8&&!d)||d<8),i=c.browser.msie&&(function(){var n=document.createElement("div");
try{n.style.setExpression("width","0+0");
n.style.removeExpression("width")
}catch(m){return false
}return true
})();
c.support=c.support||{};
c.support.borderRadius=h||j||e;
function g(m,n){return parseInt(c.css(m,n))||0
}function k(m){var m=parseInt(m).toString(16);
return(m.length<2)?"0"+m:m
}function b(o){while(o){var m=c.css(o,"backgroundColor"),n;
if(m&&m!="transparent"&&m!="rgba(0, 0, 0, 0)"){if(m.indexOf("rgb")>=0){n=m.match(/\d+/g);
return"#"+k(n[0])+k(n[1])+k(n[2])
}return m
}if(o.nodeName.toLowerCase()=="html"){break
}o=o.parentNode
}return"#ffffff"
}function f(o,m,n){switch(o){case"round":return Math.round(n*(1-Math.cos(Math.asin(m/n))));
case"cool":return Math.round(n*(1+Math.cos(Math.asin(m/n))));
case"sharp":return Math.round(n*(1-Math.cos(Math.acos(m/n))));
case"bite":return Math.round(n*(Math.cos(Math.asin((n-m-1)/n))));
case"slide":return Math.round(n*(Math.atan2(m,n/m)));
case"jut":return Math.round(n*(Math.atan2(n,(n-m-1))));
case"curl":return Math.round(n*(Math.atan(m)));
case"tear":return Math.round(n*(Math.cos(m)));
case"wicked":return Math.round(n*(Math.tan(m)));
case"long":return Math.round(n*(Math.sqrt(m)));
case"sculpt":return Math.round(n*(Math.log((n-m-1),n)));
case"dogfold":case"dog":return(m&1)?(m+1):n;
case"dog2":return(m&2)?(m+1):n;
case"dog3":return(m&3)?(m+1):n;
case"fray":return(m%2)*n;
case"notch":return n;
case"bevelfold":case"bevel":return m+1
}}c.fn.corner=function(m){if(this.length==0){if(!c.isReady&&this.selector){var n=this.selector,o=this.context;
c(function(){c(n,o).corner(m)
})
}return this
}return this.each(function(v){var u=c(this),D=[u.attr(c.fn.corner.defaults.metaAttr)||"",m||""].join(" ").toLowerCase(),K=/keep/.test(D),C=((D.match(/cc:(#[0-9a-f]+)/)||[])[1]),p=((D.match(/sc:(#[0-9a-f]+)/)||[])[1]),G=parseInt((D.match(/(\d+)px/)||[])[1])||10,E=/round|bevelfold|bevel|notch|bite|cool|sharp|slide|jut|curl|tear|fray|wicked|sculpt|long|dog3|dog2|dogfold|dog/,r=((D.match(E)||["round"])[0]),s=/dogfold|bevelfold/.test(D),q={T:0,B:1},z={TL:/top|tl|left/.test(D),TR:/top|tr|right/.test(D),BL:/bottom|bl|left/.test(D),BR:/bottom|br|right/.test(D)},H,N,F,I,y,O,B,L,J,x,M,P,A,t;
if(!z.TL&&!z.TR&&!z.BL&&!z.BR){z={TL:1,TR:1,BL:1,BR:1}
}if(c.fn.corner.defaults.useNative&&r=="round"&&(e||h||j)&&!C&&!p){if(z.TL){u.css(e?"border-top-left-radius":h?"-moz-border-radius-topleft":"-webkit-border-top-left-radius",G+"px")
}if(z.TR){u.css(e?"border-top-right-radius":h?"-moz-border-radius-topright":"-webkit-border-top-right-radius",G+"px")
}if(z.BL){u.css(e?"border-bottom-left-radius":h?"-moz-border-radius-bottomleft":"-webkit-border-bottom-left-radius",G+"px")
}if(z.BR){u.css(e?"border-bottom-right-radius":h?"-moz-border-radius-bottomright":"-webkit-border-bottom-right-radius",G+"px")
}return
}H=document.createElement("div");
c(H).css({overflow:"hidden",height:"1px",minHeight:"1px",fontSize:"1px",backgroundColor:p||"transparent",borderStyle:"solid"});
N={T:parseInt(c.css(this,"paddingTop"))||0,R:parseInt(c.css(this,"paddingRight"))||0,B:parseInt(c.css(this,"paddingBottom"))||0,L:parseInt(c.css(this,"paddingLeft"))||0};
if(typeof this.style.zoom!=undefined){this.style.zoom=1
}if(!K){this.style.border="none"
}H.style.borderColor=C||b(this.parentNode);
F=c(this).outerHeight();
for(I in q){y=q[I];
if((y&&(z.BL||z.BR))||(!y&&(z.TL||z.TR))){H.style.borderStyle="none "+(z[I+"R"]?"solid":"none")+" none "+(z[I+"L"]?"solid":"none");
O=document.createElement("div");
c(O).addClass("jquery-corner");
B=O.style;
y?this.appendChild(O):this.insertBefore(O,this.firstChild);
if(y&&F!="auto"){if(c.css(this,"position")=="static"){this.style.position="relative"
}B.position="absolute";
B.bottom=B.left=B.padding=B.margin="0";
if(i){B.setExpression("width","this.parentNode.offsetWidth")
}else{B.width="100%"
}}else{if(!y&&c.browser.msie){if(c.css(this,"position")=="static"){this.style.position="relative"
}B.position="absolute";
B.top=B.left=B.right=B.padding=B.margin="0";
if(i){L=g(this,"borderLeftWidth")+g(this,"borderRightWidth");
B.setExpression("width","this.parentNode.offsetWidth - "+L+'+ "px"')
}else{B.width="100%"
}}else{B.position="relative";
B.margin=!y?"-"+N.T+"px -"+N.R+"px "+(N.T-G)+"px -"+N.L+"px":(N.B-G)+"px -"+N.R+"px -"+N.B+"px -"+N.L+"px"
}}for(J=0;
J<G;
J++){x=Math.max(0,f(r,J,G));
M=H.cloneNode(false);
M.style.borderWidth="0 "+(z[I+"R"]?x:0)+"px 0 "+(z[I+"L"]?x:0)+"px";
y?O.appendChild(M):O.insertBefore(M,O.firstChild)
}if(s&&c.support.boxModel){if(y&&l){continue
}for(P in z){if(!z[P]){continue
}if(y&&(P=="TL"||P=="TR")){continue
}if(!y&&(P=="BL"||P=="BR")){continue
}A={position:"absolute",border:"none",margin:0,padding:0,overflow:"hidden",backgroundColor:H.style.borderColor};
t=c("<div/>").css(A).css({width:G+"px",height:"1px"});
switch(P){case"TL":t.css({bottom:0,left:0});
break;
case"TR":t.css({bottom:0,right:0});
break;
case"BL":t.css({top:0,left:0});
break;
case"BR":t.css({top:0,right:0});
break
}O.appendChild(t[0]);
var Q=c("<div/>").css(A).css({top:0,bottom:0,width:"1px",height:G+"px"});
switch(P){case"TL":Q.css({left:G});
break;
case"TR":Q.css({right:G});
break;
case"BL":Q.css({left:G});
break;
case"BR":Q.css({right:G});
break
}O.appendChild(Q[0])
}}}}})
};
c.fn.uncorner=function(){if(e||h||j){this.css(e?"border-radius":h?"-moz-border-radius":"-webkit-border-radius",0)
}c("div.jquery-corner",this).remove();
return this
};
c.fn.corner.defaults={useNative:true,metaAttr:"data-corner"}
})(jQuery);
(function(d){var a=null;
var k=null;
var e=0;
var b=null;
var i=(navigator.appVersion.indexOf("X11")!=-1)?"ew-resize":"e-resize";
d.fn.SideBarResizer=function(l){if(l!=null){a=d(this);
d(a).parent().css("overflow","hidden");
d(l).bind("mousedown",c)
}return this
};
function c(l){if(b==null){b=parseInt(d(a).width())
}a.css("opacity",0.7).parent().css("cursor",i);
d(window).bind("resize",f);
e=j(l).x;
d(document).mousemove(h).mouseup(g);
return false
}function h(m){var l=j(m).x;
var n=l-e;
e=l;
if((n>0&&parseInt(a.width())-n>20)||(n<0&&((parseInt(a.width())+Math.abs(n))<parseInt(b)))){a.width((parseInt(a.width())-n))
}return false
}function f(l){d(window).unbind("resize",f);
d(a).width("");
b=null
}function g(l){d(document).unbind("mousemove",h).unbind("mouseup",g);
e=0;
if(a!=null){a.css("opacity",1).parent().css("cursor","default")
}return false
}function j(l){return{x:l.clientX+document.documentElement.scrollLeft,y:l.clientY+document.documentElement.scrollTop}
}})(jQuery);
var ERROR_CLASS="dissError";
var FIELD_DATA_KEY="field";
function handleAjaxErrors(a,b){a.find(".spinner").hide();
if(b.globalErrors){$.each(b.globalErrors,function(d,c){alert(decodeHTML(c.message))
})
}if(b.fieldErrors){$.each(b.fieldErrors,function(e,d){var g=d.message;
var h=d.field;
var c="[name="+escapeSelector(h)+"]:first";
var f=a.find(c);
if(f.length!=0){var i=$("<div></div>").addClass(ERROR_CLASS).css("display","block");
i.html(g);
i.data(FIELD_DATA_KEY,h);
f.after(i)
}else{}});
prepareAjaxErrorBoxes(ERROR_CLASS)
}}function escapeSelector(a){return a.replace(/\./g,"\\.")
}function decodeHTML(a){return $("<div/>").html(a).text()
}function prepareAjaxErrorBoxes(a){$("."+a).each(function(){var b=$(this);
if(parseInt(b.html().length)==0){return true
}b.mouseover(function(){$(this).fadeOut("slow")
});
var c=b.data(FIELD_DATA_KEY);
b.siblings("[name="+escapeSelector(c)+"]").bind("change keyup",function(){b.fadeOut("slow")
})
})
};
