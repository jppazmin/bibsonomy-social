(function(a){a.widget("ui.stars",{options:{inputType:"radio",split:0,disabled:false,cancelTitle:"Cancel Rating",cancelValue:0,cancelShow:true,disableValue:true,oneVoteOnly:false,showTitles:false,captionEl:null,callback:null,starWidth:16,cancelClass:"ui-stars-cancel",starClass:"ui-stars-star",starOnClass:"ui-stars-star-on",starHoverClass:"ui-stars-star-hover",starDisabledClass:"ui-stars-star-disabled",cancelHoverClass:"ui-stars-cancel-hover",cancelDisabledClass:"ui-stars-cancel-disabled"},_create:function(){var c=this,f=this.options,b=0;
this.element.data("former.stars",this.element.html());
f.isSelect=f.inputType=="select";
this.$form=a(this.element).closest("form");
this.$selec=f.isSelect?a("select",this.element):null;
this.$rboxs=f.isSelect?a("option",this.$selec):a(":radio",this.element);
this.$stars=this.$rboxs.map(function(j){var k={value:this.value,title:(f.isSelect?this.text:this.title)||this.value,isDefault:(f.isSelect&&this.defaultSelected)||this.defaultChecked};
if(j==0){f.split=typeof f.split!="number"?0:f.split;
f.val2id=[];
f.id2val=[];
f.id2title=[];
f.name=f.isSelect?c.$selec.get(0).name:this.name;
f.disabled=f.disabled||(f.isSelect?a(c.$selec).attr("disabled"):a(this).attr("disabled"))
}if(k.value==f.cancelValue){f.cancelTitle=k.title;
return null
}f.val2id[k.value]=b;
f.id2val[b]=k.value;
f.id2title[b]=k.title;
if(k.isDefault){f.checked=b;
f.value=f.defaultValue=k.value;
f.title=k.title
}var h=a("<div/>").addClass(f.starClass);
var l=a("<a/>").attr("title",f.showTitles?k.title:"").text(k.value);
if(f.split){var g=(b%f.split);
var m=Math.floor(f.starWidth/f.split);
h.width(m);
l.css("margin-left","-"+(g*m)+"px")
}b++;
return h.append(l).get(0)
});
f.items=b;
f.isSelect?this.$selec.remove():this.$rboxs.remove();
this.$cancel=a("<div/>").addClass(f.cancelClass).append(a("<a/>").attr("title",f.showTitles?f.cancelTitle:"").text(f.cancelValue));
f.cancelShow&=!f.disabled&&!f.oneVoteOnly;
f.cancelShow&&this.element.append(this.$cancel);
this.element.append(this.$stars);
if(f.checked===undefined){f.checked=-1;
f.value=f.defaultValue=f.cancelValue;
f.title=""
}this.$value=a("<input type='hidden' name='"+f.name+"' value='"+f.value+"' />");
this.element.append(this.$value);
this.$stars.bind("click.stars",function(h){if(!f.forceSelect&&f.disabled){return false
}var g=c.$stars.index(this);
f.checked=g;
f.value=f.id2val[g];
f.title=f.id2title[g];
c.$value.attr({disabled:f.disabled?"disabled":"",value:f.value});
d(g,false);
c._disableCancel();
!f.forceSelect&&c.callback(h,"star")
}).bind("mouseover.stars",function(){if(f.disabled){return false
}var g=c.$stars.index(this);
d(g,true)
}).bind("mouseout.stars",function(){if(f.disabled){return false
}d(c.options.checked,false)
});
this.$cancel.bind("click.stars",function(g){if(!f.forceSelect&&(f.disabled||f.value==f.cancelValue)){return false
}f.checked=-1;
f.value=f.cancelValue;
f.title="";
c.$value.val(f.value);
f.disableValue&&c.$value.attr({disabled:"disabled"});
e();
c._disableCancel();
!f.forceSelect&&c.callback(g,"cancel")
}).bind("mouseover.stars",function(){if(c._disableCancel()){return false
}c.$cancel.addClass(f.cancelHoverClass);
e();
c._showCap(f.cancelTitle)
}).bind("mouseout.stars",function(){if(c._disableCancel()){return false
}c.$cancel.removeClass(f.cancelHoverClass);
c.$stars.triggerHandler("mouseout.stars")
});
this.$form.bind("reset.stars",function(){!f.disabled&&c.select(f.defaultValue)
});
a(window).unload(function(){c.$cancel.unbind(".stars");
c.$stars.unbind(".stars");
c.$form.unbind(".stars");
c.$selec=c.$rboxs=c.$stars=c.$value=c.$cancel=c.$form=null
});
function d(g,i){if(g!=-1){var j=i?f.starHoverClass:f.starOnClass;
var h=i?f.starOnClass:f.starHoverClass;
c.$stars.eq(g).prevAll("."+f.starClass).andSelf().removeClass(h).addClass(j);
c.$stars.eq(g).nextAll("."+f.starClass).removeClass(f.starHoverClass+" "+f.starOnClass);
c._showCap(f.id2title[g])
}else{e()
}}function e(){c.$stars.removeClass(f.starOnClass+" "+f.starHoverClass);
c._showCap("")
}this.select(f.value);
f.disabled&&this.disable()
},_disableCancel:function(){var c=this.options,b=c.disabled||c.oneVoteOnly||(c.value==c.cancelValue);
if(b){this.$cancel.removeClass(c.cancelHoverClass).addClass(c.cancelDisabledClass)
}else{this.$cancel.removeClass(c.cancelDisabledClass)
}this.$cancel.css("opacity",b?0.5:1);
return b
},_disableAll:function(){var b=this.options;
this._disableCancel();
if(b.disabled){this.$stars.filter("div").addClass(b.starDisabledClass)
}else{this.$stars.filter("div").removeClass(b.starDisabledClass)
}},_showCap:function(b){var c=this.options;
if(c.captionEl){c.captionEl.text(b)
}},value:function(){return this.options.value
},select:function(d){var c=this.options,b=(d==c.cancelValue)?this.$cancel:this.$stars.eq(c.val2id[d]);
c.forceSelect=true;
b.triggerHandler("click.stars");
c.forceSelect=false
},selectID:function(d){var c=this.options,b=(d==-1)?this.$cancel:this.$stars.eq(d);
c.forceSelect=true;
b.triggerHandler("click.stars");
c.forceSelect=false
},enable:function(){this.options.disabled=false;
this._disableAll()
},disable:function(){this.options.disabled=true;
this._disableAll()
},destroy:function(){this.$form.unbind(".stars");
this.$cancel.unbind(".stars").remove();
this.$stars.unbind(".stars").remove();
this.$value.remove();
this.element.unbind(".stars").html(this.element.data("former.stars")).removeData("stars");
return this
},callback:function(c,b){var d=this.options;
d.callback&&d.callback(this,b,d.value,c);
d.oneVoteOnly&&!d.disabled&&this.disable()
}});
a.extend(a.ui.stars,{version:"3.0.1"})
})(jQuery);
if(!document.createElement("canvas").getContext){(function(){var Y=Math;
var q=Y.round;
var o=Y.sin;
var B=Y.cos;
var H=Y.abs;
var N=Y.sqrt;
var d=10;
var f=d/2;
function A(){return this.context_||(this.context_=new D(this))
}var v=Array.prototype.slice;
function g(j,m,p){var i=v.call(arguments,2);
return function(){return j.apply(m,i.concat(v.call(arguments)))
}
}function ad(i){return String(i).replace(/&/g,"&amp;").replace(/"/g,"&quot;")
}function R(j){if(!j.namespaces["g_vml_"]){j.namespaces.add("g_vml_","urn:schemas-microsoft-com:vml","#default#VML")
}if(!j.namespaces["g_o_"]){j.namespaces.add("g_o_","urn:schemas-microsoft-com:office:office","#default#VML")
}if(!j.styleSheets["ex_canvas_"]){var i=j.createStyleSheet();
i.owningElement.id="ex_canvas_";
i.cssText="canvas{display:inline-block;overflow:hidden;"+"text-align:left;width:300px;height:150px}"
}}R(document);
var e={init:function(i){if(/MSIE/.test(navigator.userAgent)&&!window.opera){var j=i||document;
j.createElement("canvas");
j.attachEvent("onreadystatechange",g(this.init_,this,j))
}},init_:function(p){var m=p.getElementsByTagName("canvas");
for(var j=0;
j<m.length;
j++){this.initElement(m[j])
}},initElement:function(j){if(!j.getContext){j.getContext=A;
R(j.ownerDocument);
j.innerHTML="";
j.attachEvent("onpropertychange",z);
j.attachEvent("onresize",V);
var i=j.attributes;
if(i.width&&i.width.specified){j.style.width=i.width.nodeValue+"px"
}else{j.width=j.clientWidth
}if(i.height&&i.height.specified){j.style.height=i.height.nodeValue+"px"
}else{j.height=j.clientHeight
}}return j
}};
function z(j){var i=j.srcElement;
switch(j.propertyName){case"width":i.getContext().clearRect();
i.style.width=i.attributes.width.nodeValue+"px";
i.firstChild.style.width=i.clientWidth+"px";
break;
case"height":i.getContext().clearRect();
i.style.height=i.attributes.height.nodeValue+"px";
i.firstChild.style.height=i.clientHeight+"px";
break
}}function V(j){var i=j.srcElement;
if(i.firstChild){i.firstChild.style.width=i.clientWidth+"px";
i.firstChild.style.height=i.clientHeight+"px"
}}e.init();
var n=[];
for(var ac=0;
ac<16;
ac++){for(var ab=0;
ab<16;
ab++){n[ac*16+ab]=ac.toString(16)+ab.toString(16)
}}function C(){return[[1,0,0],[0,1,0],[0,0,1]]
}function J(p,m){var j=C();
for(var i=0;
i<3;
i++){for(var af=0;
af<3;
af++){var Z=0;
for(var ae=0;
ae<3;
ae++){Z+=p[i][ae]*m[ae][af]
}j[i][af]=Z
}}return j
}function x(j,i){i.fillStyle=j.fillStyle;
i.lineCap=j.lineCap;
i.lineJoin=j.lineJoin;
i.lineWidth=j.lineWidth;
i.miterLimit=j.miterLimit;
i.shadowBlur=j.shadowBlur;
i.shadowColor=j.shadowColor;
i.shadowOffsetX=j.shadowOffsetX;
i.shadowOffsetY=j.shadowOffsetY;
i.strokeStyle=j.strokeStyle;
i.globalAlpha=j.globalAlpha;
i.font=j.font;
i.textAlign=j.textAlign;
i.textBaseline=j.textBaseline;
i.arcScaleX_=j.arcScaleX_;
i.arcScaleY_=j.arcScaleY_;
i.lineScale_=j.lineScale_
}var b={aliceblue:"#F0F8FF",antiquewhite:"#FAEBD7",aquamarine:"#7FFFD4",azure:"#F0FFFF",beige:"#F5F5DC",bisque:"#FFE4C4",black:"#000000",blanchedalmond:"#FFEBCD",blueviolet:"#8A2BE2",brown:"#A52A2A",burlywood:"#DEB887",cadetblue:"#5F9EA0",chartreuse:"#7FFF00",chocolate:"#D2691E",coral:"#FF7F50",cornflowerblue:"#6495ED",cornsilk:"#FFF8DC",crimson:"#DC143C",cyan:"#00FFFF",darkblue:"#00008B",darkcyan:"#008B8B",darkgoldenrod:"#B8860B",darkgray:"#A9A9A9",darkgreen:"#006400",darkgrey:"#A9A9A9",darkkhaki:"#BDB76B",darkmagenta:"#8B008B",darkolivegreen:"#556B2F",darkorange:"#FF8C00",darkorchid:"#9932CC",darkred:"#8B0000",darksalmon:"#E9967A",darkseagreen:"#8FBC8F",darkslateblue:"#483D8B",darkslategray:"#2F4F4F",darkslategrey:"#2F4F4F",darkturquoise:"#00CED1",darkviolet:"#9400D3",deeppink:"#FF1493",deepskyblue:"#00BFFF",dimgray:"#696969",dimgrey:"#696969",dodgerblue:"#1E90FF",firebrick:"#B22222",floralwhite:"#FFFAF0",forestgreen:"#228B22",gainsboro:"#DCDCDC",ghostwhite:"#F8F8FF",gold:"#FFD700",goldenrod:"#DAA520",grey:"#808080",greenyellow:"#ADFF2F",honeydew:"#F0FFF0",hotpink:"#FF69B4",indianred:"#CD5C5C",indigo:"#4B0082",ivory:"#FFFFF0",khaki:"#F0E68C",lavender:"#E6E6FA",lavenderblush:"#FFF0F5",lawngreen:"#7CFC00",lemonchiffon:"#FFFACD",lightblue:"#ADD8E6",lightcoral:"#F08080",lightcyan:"#E0FFFF",lightgoldenrodyellow:"#FAFAD2",lightgreen:"#90EE90",lightgrey:"#D3D3D3",lightpink:"#FFB6C1",lightsalmon:"#FFA07A",lightseagreen:"#20B2AA",lightskyblue:"#87CEFA",lightslategray:"#778899",lightslategrey:"#778899",lightsteelblue:"#B0C4DE",lightyellow:"#FFFFE0",limegreen:"#32CD32",linen:"#FAF0E6",magenta:"#FF00FF",mediumaquamarine:"#66CDAA",mediumblue:"#0000CD",mediumorchid:"#BA55D3",mediumpurple:"#9370DB",mediumseagreen:"#3CB371",mediumslateblue:"#7B68EE",mediumspringgreen:"#00FA9A",mediumturquoise:"#48D1CC",mediumvioletred:"#C71585",midnightblue:"#191970",mintcream:"#F5FFFA",mistyrose:"#FFE4E1",moccasin:"#FFE4B5",navajowhite:"#FFDEAD",oldlace:"#FDF5E6",olivedrab:"#6B8E23",orange:"#FFA500",orangered:"#FF4500",orchid:"#DA70D6",palegoldenrod:"#EEE8AA",palegreen:"#98FB98",paleturquoise:"#AFEEEE",palevioletred:"#DB7093",papayawhip:"#FFEFD5",peachpuff:"#FFDAB9",peru:"#CD853F",pink:"#FFC0CB",plum:"#DDA0DD",powderblue:"#B0E0E6",rosybrown:"#BC8F8F",royalblue:"#4169E1",saddlebrown:"#8B4513",salmon:"#FA8072",sandybrown:"#F4A460",seagreen:"#2E8B57",seashell:"#FFF5EE",sienna:"#A0522D",skyblue:"#87CEEB",slateblue:"#6A5ACD",slategray:"#708090",slategrey:"#708090",snow:"#FFFAFA",springgreen:"#00FF7F",steelblue:"#4682B4",tan:"#D2B48C",thistle:"#D8BFD8",tomato:"#FF6347",turquoise:"#40E0D0",violet:"#EE82EE",wheat:"#F5DEB3",whitesmoke:"#F5F5F5",yellowgreen:"#9ACD32"};
function M(j){var p=j.indexOf("(",3);
var i=j.indexOf(")",p+1);
var m=j.substring(p+1,i).split(",");
if(m.length==4&&j.substr(3,1)=="a"){alpha=Number(m[3])
}else{m[3]=1
}return m
}function c(i){return parseFloat(i)/100
}function u(j,m,i){return Math.min(i,Math.max(m,j))
}function I(af){var m,j,i;
h=parseFloat(af[0])/360%360;
if(h<0){h++
}s=u(c(af[1]),0,1);
l=u(c(af[2]),0,1);
if(s==0){m=j=i=l
}else{var Z=l<0.5?l*(1+s):l+s-l*s;
var ae=2*l-Z;
m=a(ae,Z,h+1/3);
j=a(ae,Z,h);
i=a(ae,Z,h-1/3)
}return"#"+n[Math.floor(m*255)]+n[Math.floor(j*255)]+n[Math.floor(i*255)]
}function a(j,i,m){if(m<0){m++
}if(m>1){m--
}if(6*m<1){return j+(i-j)*6*m
}else{if(2*m<1){return i
}else{if(3*m<2){return j+(i-j)*(2/3-m)*6
}else{return j
}}}}function F(j){var ae,Z=1;
j=String(j);
if(j.charAt(0)=="#"){ae=j
}else{if(/^rgb/.test(j)){var p=M(j);
var ae="#",af;
for(var m=0;
m<3;
m++){if(p[m].indexOf("%")!=-1){af=Math.floor(c(p[m])*255)
}else{af=Number(p[m])
}ae+=n[u(af,0,255)]
}Z=p[3]
}else{if(/^hsl/.test(j)){var p=M(j);
ae=I(p);
Z=p[3]
}else{ae=b[j]||j
}}}return{color:ae,alpha:Z}
}var r={style:"normal",variant:"normal",weight:"normal",size:10,family:"sans-serif"};
var L={};
function E(i){if(L[i]){return L[i]
}var p=document.createElement("div");
var m=p.style;
try{m.font=i
}catch(j){}return L[i]={style:m.fontStyle||r.style,variant:m.fontVariant||r.variant,weight:m.fontWeight||r.weight,size:m.fontSize||r.size,family:m.fontFamily||r.family}
}function w(m,j){var i={};
for(var af in m){i[af]=m[af]
}var ae=parseFloat(j.currentStyle.fontSize),Z=parseFloat(m.size);
if(typeof m.size=="number"){i.size=m.size
}else{if(m.size.indexOf("px")!=-1){i.size=Z
}else{if(m.size.indexOf("em")!=-1){i.size=ae*Z
}else{if(m.size.indexOf("%")!=-1){i.size=(ae/100)*Z
}else{if(m.size.indexOf("pt")!=-1){i.size=Z/0.75
}else{i.size=ae
}}}}}i.size*=0.981;
return i
}function aa(i){return i.style+" "+i.variant+" "+i.weight+" "+i.size+"px "+i.family
}function S(i){switch(i){case"butt":return"flat";
case"round":return"round";
case"square":default:return"square"
}}function D(j){this.m_=C();
this.mStack_=[];
this.aStack_=[];
this.currentPath_=[];
this.strokeStyle="#000";
this.fillStyle="#000";
this.lineWidth=1;
this.lineJoin="miter";
this.lineCap="butt";
this.miterLimit=d*1;
this.globalAlpha=1;
this.font="10px sans-serif";
this.textAlign="left";
this.textBaseline="alphabetic";
this.canvas=j;
var i=j.ownerDocument.createElement("div");
i.style.width=j.clientWidth+"px";
i.style.height=j.clientHeight+"px";
i.style.overflow="hidden";
i.style.position="absolute";
j.appendChild(i);
this.element_=i;
this.arcScaleX_=1;
this.arcScaleY_=1;
this.lineScale_=1
}var t=D.prototype;
t.clearRect=function(){if(this.textMeasureEl_){this.textMeasureEl_.removeNode(true);
this.textMeasureEl_=null
}this.element_.innerHTML=""
};
t.beginPath=function(){this.currentPath_=[]
};
t.moveTo=function(j,i){var m=this.getCoords_(j,i);
this.currentPath_.push({type:"moveTo",x:m.x,y:m.y});
this.currentX_=m.x;
this.currentY_=m.y
};
t.lineTo=function(j,i){var m=this.getCoords_(j,i);
this.currentPath_.push({type:"lineTo",x:m.x,y:m.y});
this.currentX_=m.x;
this.currentY_=m.y
};
t.bezierCurveTo=function(m,j,ai,ah,ag,ae){var i=this.getCoords_(ag,ae);
var af=this.getCoords_(m,j);
var Z=this.getCoords_(ai,ah);
K(this,af,Z,i)
};
function K(i,Z,m,j){i.currentPath_.push({type:"bezierCurveTo",cp1x:Z.x,cp1y:Z.y,cp2x:m.x,cp2y:m.y,x:j.x,y:j.y});
i.currentX_=j.x;
i.currentY_=j.y
}t.quadraticCurveTo=function(ag,m,j,i){var af=this.getCoords_(ag,m);
var ae=this.getCoords_(j,i);
var ah={x:this.currentX_+2/3*(af.x-this.currentX_),y:this.currentY_+2/3*(af.y-this.currentY_)};
var Z={x:ah.x+(ae.x-this.currentX_)/3,y:ah.y+(ae.y-this.currentY_)/3};
K(this,ah,Z,ae)
};
t.arc=function(aj,ah,ai,ae,j,m){ai*=d;
var an=m?"at":"wa";
var ak=aj+B(ae)*ai-f;
var am=ah+o(ae)*ai-f;
var i=aj+B(j)*ai-f;
var al=ah+o(j)*ai-f;
if(ak==i&&!m){ak+=0.125
}var Z=this.getCoords_(aj,ah);
var ag=this.getCoords_(ak,am);
var af=this.getCoords_(i,al);
this.currentPath_.push({type:an,x:Z.x,y:Z.y,radius:ai,xStart:ag.x,yStart:ag.y,xEnd:af.x,yEnd:af.y})
};
t.rect=function(m,j,i,p){this.moveTo(m,j);
this.lineTo(m+i,j);
this.lineTo(m+i,j+p);
this.lineTo(m,j+p);
this.closePath()
};
t.strokeRect=function(m,j,i,p){var Z=this.currentPath_;
this.beginPath();
this.moveTo(m,j);
this.lineTo(m+i,j);
this.lineTo(m+i,j+p);
this.lineTo(m,j+p);
this.closePath();
this.stroke();
this.currentPath_=Z
};
t.fillRect=function(m,j,i,p){var Z=this.currentPath_;
this.beginPath();
this.moveTo(m,j);
this.lineTo(m+i,j);
this.lineTo(m+i,j+p);
this.lineTo(m,j+p);
this.closePath();
this.fill();
this.currentPath_=Z
};
t.createLinearGradient=function(j,p,i,m){var Z=new U("gradient");
Z.x0_=j;
Z.y0_=p;
Z.x1_=i;
Z.y1_=m;
return Z
};
t.createRadialGradient=function(p,ae,m,j,Z,i){var af=new U("gradientradial");
af.x0_=p;
af.y0_=ae;
af.r0_=m;
af.x1_=j;
af.y1_=Z;
af.r1_=i;
return af
};
t.drawImage=function(ao,m){var ah,af,aj,aw,am,ak,aq,ay;
var ai=ao.runtimeStyle.width;
var an=ao.runtimeStyle.height;
ao.runtimeStyle.width="auto";
ao.runtimeStyle.height="auto";
var ag=ao.width;
var au=ao.height;
ao.runtimeStyle.width=ai;
ao.runtimeStyle.height=an;
if(arguments.length==3){ah=arguments[1];
af=arguments[2];
am=ak=0;
aq=aj=ag;
ay=aw=au
}else{if(arguments.length==5){ah=arguments[1];
af=arguments[2];
aj=arguments[3];
aw=arguments[4];
am=ak=0;
aq=ag;
ay=au
}else{if(arguments.length==9){am=arguments[1];
ak=arguments[2];
aq=arguments[3];
ay=arguments[4];
ah=arguments[5];
af=arguments[6];
aj=arguments[7];
aw=arguments[8]
}else{throw Error("Invalid number of arguments")
}}}var ax=this.getCoords_(ah,af);
var p=aq/2;
var j=ay/2;
var av=[];
var i=10;
var ae=10;
av.push(" <g_vml_:group",' coordsize="',d*i,",",d*ae,'"',' coordorigin="0,0"',' style="width:',i,"px;height:",ae,"px;position:absolute;");
if(this.m_[0][0]!=1||this.m_[0][1]||this.m_[1][1]!=1||this.m_[1][0]){var Z=[];
Z.push("M11=",this.m_[0][0],",","M12=",this.m_[1][0],",","M21=",this.m_[0][1],",","M22=",this.m_[1][1],",","Dx=",q(ax.x/d),",","Dy=",q(ax.y/d),"");
var at=ax;
var ar=this.getCoords_(ah+aj,af);
var ap=this.getCoords_(ah,af+aw);
var al=this.getCoords_(ah+aj,af+aw);
at.x=Y.max(at.x,ar.x,ap.x,al.x);
at.y=Y.max(at.y,ar.y,ap.y,al.y);
av.push("padding:0 ",q(at.x/d),"px ",q(at.y/d),"px 0;filter:progid:DXImageTransform.Microsoft.Matrix(",Z.join(""),", sizingmethod='clip');")
}else{av.push("top:",q(ax.y/d),"px;left:",q(ax.x/d),"px;")
}av.push(' ">','<g_vml_:image src="',ao.src,'"',' style="width:',d*aj,"px;"," height:",d*aw,'px"',' cropleft="',am/ag,'"',' croptop="',ak/au,'"',' cropright="',(ag-am-aq)/ag,'"',' cropbottom="',(au-ak-ay)/au,'"'," />","</g_vml_:group>");
this.element_.insertAdjacentHTML("BeforeEnd",av.join(""))
};
t.stroke=function(am){var Z=10;
var an=10;
var ae=5000;
var ag={x:null,y:null};
var al={x:null,y:null};
for(var ah=0;
ah<this.currentPath_.length;
ah+=ae){var ak=[];
var af=false;
ak.push("<g_vml_:shape",' filled="',!!am,'"',' style="position:absolute;width:',Z,"px;height:",an,'px;"',' coordorigin="0,0"',' coordsize="',d*Z,",",d*an,'"',' stroked="',!am,'"',' path="');
var ao=false;
for(var ai=ah;
ai<Math.min(ah+ae,this.currentPath_.length);
ai++){if(ai%ae==0&&ai>0){ak.push(" m ",q(this.currentPath_[ai-1].x),",",q(this.currentPath_[ai-1].y))
}var m=this.currentPath_[ai];
var aj;
switch(m.type){case"moveTo":aj=m;
ak.push(" m ",q(m.x),",",q(m.y));
break;
case"lineTo":ak.push(" l ",q(m.x),",",q(m.y));
break;
case"close":ak.push(" x ");
m=null;
break;
case"bezierCurveTo":ak.push(" c ",q(m.cp1x),",",q(m.cp1y),",",q(m.cp2x),",",q(m.cp2y),",",q(m.x),",",q(m.y));
break;
case"at":case"wa":ak.push(" ",m.type," ",q(m.x-this.arcScaleX_*m.radius),",",q(m.y-this.arcScaleY_*m.radius)," ",q(m.x+this.arcScaleX_*m.radius),",",q(m.y+this.arcScaleY_*m.radius)," ",q(m.xStart),",",q(m.yStart)," ",q(m.xEnd),",",q(m.yEnd));
break
}if(m){if(ag.x==null||m.x<ag.x){ag.x=m.x
}if(al.x==null||m.x>al.x){al.x=m.x
}if(ag.y==null||m.y<ag.y){ag.y=m.y
}if(al.y==null||m.y>al.y){al.y=m.y
}}}ak.push(' ">');
if(!am){y(this,ak)
}else{G(this,ak,ag,al)
}ak.push("</g_vml_:shape>");
this.element_.insertAdjacentHTML("beforeEnd",ak.join(""))
}};
function y(m,ae){var j=F(m.strokeStyle);
var p=j.color;
var Z=j.alpha*m.globalAlpha;
var i=m.lineScale_*m.lineWidth;
if(i<1){Z*=i
}ae.push("<g_vml_:stroke",' opacity="',Z,'"',' joinstyle="',m.lineJoin,'"',' miterlimit="',m.miterLimit,'"',' endcap="',S(m.lineCap),'"',' weight="',i,'px"',' color="',p,'" />')
}function G(ao,ag,aI,ap){var ah=ao.fillStyle;
var az=ao.arcScaleX_;
var ay=ao.arcScaleY_;
var j=ap.x-aI.x;
var p=ap.y-aI.y;
if(ah instanceof U){var al=0;
var aD={x:0,y:0};
var av=0;
var ak=1;
if(ah.type_=="gradient"){var aj=ah.x0_/az;
var m=ah.y0_/ay;
var ai=ah.x1_/az;
var aK=ah.y1_/ay;
var aH=ao.getCoords_(aj,m);
var aG=ao.getCoords_(ai,aK);
var ae=aG.x-aH.x;
var Z=aG.y-aH.y;
al=Math.atan2(ae,Z)*180/Math.PI;
if(al<0){al+=360
}if(al<0.000001){al=0
}}else{var aH=ao.getCoords_(ah.x0_,ah.y0_);
aD={x:(aH.x-aI.x)/j,y:(aH.y-aI.y)/p};
j/=az*d;
p/=ay*d;
var aB=Y.max(j,p);
av=2*ah.r0_/aB;
ak=2*ah.r1_/aB-av
}var at=ah.colors_;
at.sort(function(aL,i){return aL.offset-i.offset
});
var an=at.length;
var ar=at[0].color;
var aq=at[an-1].color;
var ax=at[0].alpha*ao.globalAlpha;
var aw=at[an-1].alpha*ao.globalAlpha;
var aC=[];
for(var aF=0;
aF<an;
aF++){var am=at[aF];
aC.push(am.offset*ak+av+" "+am.color)
}ag.push('<g_vml_:fill type="',ah.type_,'"',' method="none" focus="100%"',' color="',ar,'"',' color2="',aq,'"',' colors="',aC.join(","),'"',' opacity="',aw,'"',' g_o_:opacity2="',ax,'"',' angle="',al,'"',' focusposition="',aD.x,",",aD.y,'" />')
}else{if(ah instanceof T){if(j&&p){var af=-aI.x;
var aA=-aI.y;
ag.push("<g_vml_:fill",' position="',af/j*az*az,",",aA/p*ay*ay,'"',' type="tile"',' src="',ah.src_,'" />')
}}else{var aJ=F(ao.fillStyle);
var au=aJ.color;
var aE=aJ.alpha*ao.globalAlpha;
ag.push('<g_vml_:fill color="',au,'" opacity="',aE,'" />')
}}}t.fill=function(){this.stroke(true)
};
t.closePath=function(){this.currentPath_.push({type:"close"})
};
t.getCoords_=function(p,j){var i=this.m_;
return{x:d*(p*i[0][0]+j*i[1][0]+i[2][0])-f,y:d*(p*i[0][1]+j*i[1][1]+i[2][1])-f}
};
t.save=function(){var i={};
x(this,i);
this.aStack_.push(i);
this.mStack_.push(this.m_);
this.m_=J(C(),this.m_)
};
t.restore=function(){if(this.aStack_.length){x(this.aStack_.pop(),this);
this.m_=this.mStack_.pop()
}};
function k(i){return isFinite(i[0][0])&&isFinite(i[0][1])&&isFinite(i[1][0])&&isFinite(i[1][1])&&isFinite(i[2][0])&&isFinite(i[2][1])
}function X(j,i,p){if(!k(i)){return
}j.m_=i;
if(p){var Z=i[0][0]*i[1][1]-i[0][1]*i[1][0];
j.lineScale_=N(H(Z))
}}t.translate=function(m,j){var i=[[1,0,0],[0,1,0],[m,j,1]];
X(this,J(i,this.m_),false)
};
t.rotate=function(j){var p=B(j);
var m=o(j);
var i=[[p,m,0],[-m,p,0],[0,0,1]];
X(this,J(i,this.m_),false)
};
t.scale=function(m,j){this.arcScaleX_*=m;
this.arcScaleY_*=j;
var i=[[m,0,0],[0,j,0],[0,0,1]];
X(this,J(i,this.m_),true)
};
t.transform=function(Z,p,af,ae,j,i){var m=[[Z,p,0],[af,ae,0],[j,i,1]];
X(this,J(m,this.m_),true)
};
t.setTransform=function(ae,Z,ag,af,p,j){var i=[[ae,Z,0],[ag,af,0],[p,j,1]];
X(this,i,true)
};
t.drawText_=function(ak,ai,ah,an,ag){var am=this.m_,aq=1000,j=0,ap=aq,af={x:0,y:0},ae=[];
var i=w(E(this.font),this.element_);
var p=aa(i);
var ar=this.element_.currentStyle;
var Z=this.textAlign.toLowerCase();
switch(Z){case"left":case"center":case"right":break;
case"end":Z=ar.direction=="ltr"?"right":"left";
break;
case"start":Z=ar.direction=="rtl"?"right":"left";
break;
default:Z="left"
}switch(this.textBaseline){case"hanging":case"top":af.y=i.size/1.75;
break;
case"middle":break;
default:case null:case"alphabetic":case"ideographic":case"bottom":af.y=-i.size/2.25;
break
}switch(Z){case"right":j=aq;
ap=0.05;
break;
case"center":j=ap=aq/2;
break
}var ao=this.getCoords_(ai+af.x,ah+af.y);
ae.push('<g_vml_:line from="',-j,' 0" to="',ap,' 0.05" ',' coordsize="100 100" coordorigin="0 0"',' filled="',!ag,'" stroked="',!!ag,'" style="position:absolute;width:1px;height:1px;">');
if(ag){y(this,ae)
}else{G(this,ae,{x:-j,y:0},{x:ap,y:i.size})
}var al=am[0][0].toFixed(3)+","+am[1][0].toFixed(3)+","+am[0][1].toFixed(3)+","+am[1][1].toFixed(3)+",0,0";
var aj=q(ao.x/d)+","+q(ao.y/d);
ae.push('<g_vml_:skew on="t" matrix="',al,'" ',' offset="',aj,'" origin="',j,' 0" />','<g_vml_:path textpathok="true" />','<g_vml_:textpath on="true" string="',ad(ak),'" style="v-text-align:',Z,";font:",ad(p),'" /></g_vml_:line>');
this.element_.insertAdjacentHTML("beforeEnd",ae.join(""))
};
t.fillText=function(m,i,p,j){this.drawText_(m,i,p,j,false)
};
t.strokeText=function(m,i,p,j){this.drawText_(m,i,p,j,true)
};
t.measureText=function(m){if(!this.textMeasureEl_){var i='<span style="position:absolute;'+"top:-20000px;left:0;padding:0;margin:0;border:none;"+'white-space:pre;"></span>';
this.element_.insertAdjacentHTML("beforeEnd",i);
this.textMeasureEl_=this.element_.lastChild
}var j=this.element_.ownerDocument;
this.textMeasureEl_.innerHTML="";
this.textMeasureEl_.style.font=this.font;
this.textMeasureEl_.appendChild(j.createTextNode(m));
return{width:this.textMeasureEl_.offsetWidth}
};
t.clip=function(){};
t.arcTo=function(){};
t.createPattern=function(j,i){return new T(j,i)
};
function U(i){this.type_=i;
this.x0_=0;
this.y0_=0;
this.r0_=0;
this.x1_=0;
this.y1_=0;
this.r1_=0;
this.colors_=[]
}U.prototype.addColorStop=function(j,i){i=F(i);
this.colors_.push({offset:j,color:i.color,alpha:i.alpha})
};
function T(j,i){Q(j);
switch(i){case"repeat":case null:case"":this.repetition_="repeat";
break;
case"repeat-x":case"repeat-y":case"no-repeat":this.repetition_=i;
break;
default:O("SYNTAX_ERR")
}this.src_=j.src;
this.width_=j.width;
this.height_=j.height
}function O(i){throw new P(i)
}function Q(i){if(!i||i.nodeType!=1||i.tagName!="IMG"){O("TYPE_MISMATCH_ERR")
}if(i.readyState!="complete"){O("INVALID_STATE_ERR")
}}function P(i){this.code=this[i];
this.message=i+": DOM Exception "+this.code
}var W=P.prototype=new Error;
W.INDEX_SIZE_ERR=1;
W.DOMSTRING_SIZE_ERR=2;
W.HIERARCHY_REQUEST_ERR=3;
W.WRONG_DOCUMENT_ERR=4;
W.INVALID_CHARACTER_ERR=5;
W.NO_DATA_ALLOWED_ERR=6;
W.NO_MODIFICATION_ALLOWED_ERR=7;
W.NOT_FOUND_ERR=8;
W.NOT_SUPPORTED_ERR=9;
W.INUSE_ATTRIBUTE_ERR=10;
W.INVALID_STATE_ERR=11;
W.SYNTAX_ERR=12;
W.INVALID_MODIFICATION_ERR=13;
W.NAMESPACE_ERR=14;
W.INVALID_ACCESS_ERR=15;
W.VALIDATION_ERR=16;
W.TYPE_MISMATCH_ERR=17;
G_vmlCanvasManager=e;
CanvasRenderingContext2D=D;
CanvasGradient=U;
CanvasPattern=T;
DOMException=P
})()
};
/* Javascript plotting library for jQuery, v. 0.7.
 *
 * Released under the MIT license by IOLA, December 2007.
 *
 */
(function(b){b.color={};
b.color.make=function(d,e,g,f){var c={};
c.r=d||0;
c.g=e||0;
c.b=g||0;
c.a=f!=null?f:1;
c.add=function(h,j){for(var k=0;
k<h.length;
++k){c[h.charAt(k)]+=j
}return c.normalize()
};
c.scale=function(h,j){for(var k=0;
k<h.length;
++k){c[h.charAt(k)]*=j
}return c.normalize()
};
c.toString=function(){if(c.a>=1){return"rgb("+[c.r,c.g,c.b].join(",")+")"
}else{return"rgba("+[c.r,c.g,c.b,c.a].join(",")+")"
}};
c.normalize=function(){function h(k,j,l){return j<k?k:(j>l?l:j)
}c.r=h(0,parseInt(c.r),255);
c.g=h(0,parseInt(c.g),255);
c.b=h(0,parseInt(c.b),255);
c.a=h(0,c.a,1);
return c
};
c.clone=function(){return b.color.make(c.r,c.b,c.g,c.a)
};
return c.normalize()
};
b.color.extract=function(d,e){var c;
do{c=d.css(e).toLowerCase();
if(c!=""&&c!="transparent"){break
}d=d.parent()
}while(!b.nodeName(d.get(0),"body"));
if(c=="rgba(0, 0, 0, 0)"){c="transparent"
}return b.color.parse(c)
};
b.color.parse=function(c){var d,f=b.color.make;
if(d=/rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/.exec(c)){return f(parseInt(d[1],10),parseInt(d[2],10),parseInt(d[3],10))
}if(d=/rgba\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]+(?:\.[0-9]+)?)\s*\)/.exec(c)){return f(parseInt(d[1],10),parseInt(d[2],10),parseInt(d[3],10),parseFloat(d[4]))
}if(d=/rgb\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*\)/.exec(c)){return f(parseFloat(d[1])*2.55,parseFloat(d[2])*2.55,parseFloat(d[3])*2.55)
}if(d=/rgba\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\s*\)/.exec(c)){return f(parseFloat(d[1])*2.55,parseFloat(d[2])*2.55,parseFloat(d[3])*2.55,parseFloat(d[4]))
}if(d=/#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/.exec(c)){return f(parseInt(d[1],16),parseInt(d[2],16),parseInt(d[3],16))
}if(d=/#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/.exec(c)){return f(parseInt(d[1]+d[1],16),parseInt(d[2]+d[2],16),parseInt(d[3]+d[3],16))
}var e=b.trim(c).toLowerCase();
if(e=="transparent"){return f(255,255,255,0)
}else{d=a[e]||[0,0,0];
return f(d[0],d[1],d[2])
}};
var a={aqua:[0,255,255],azure:[240,255,255],beige:[245,245,220],black:[0,0,0],blue:[0,0,255],brown:[165,42,42],cyan:[0,255,255],darkblue:[0,0,139],darkcyan:[0,139,139],darkgrey:[169,169,169],darkgreen:[0,100,0],darkkhaki:[189,183,107],darkmagenta:[139,0,139],darkolivegreen:[85,107,47],darkorange:[255,140,0],darkorchid:[153,50,204],darkred:[139,0,0],darksalmon:[233,150,122],darkviolet:[148,0,211],fuchsia:[255,0,255],gold:[255,215,0],green:[0,128,0],indigo:[75,0,130],khaki:[240,230,140],lightblue:[173,216,230],lightcyan:[224,255,255],lightgreen:[144,238,144],lightgrey:[211,211,211],lightpink:[255,182,193],lightyellow:[255,255,224],lime:[0,255,0],magenta:[255,0,255],maroon:[128,0,0],navy:[0,0,128],olive:[128,128,0],orange:[255,165,0],pink:[255,192,203],purple:[128,0,128],violet:[128,0,128],red:[255,0,0],silver:[192,192,192],white:[255,255,255],yellow:[255,255,0]}
})(jQuery);
(function(c){function b(av,ai,J,af){var Q=[],O={colors:["#edc240","#afd8f8","#cb4b4b","#4da74d","#9440ed"],legend:{show:true,noColumns:1,labelFormatter:null,labelBoxBorderColor:"#ccc",container:null,position:"ne",margin:5,backgroundColor:null,backgroundOpacity:0.85},xaxis:{show:null,position:"bottom",mode:null,color:null,tickColor:null,transform:null,inverseTransform:null,min:null,max:null,autoscaleMargin:null,ticks:null,tickFormatter:null,labelWidth:null,labelHeight:null,reserveSpace:null,tickLength:null,alignTicksWithAxis:null,tickDecimals:null,tickSize:null,minTickSize:null,monthNames:null,timeformat:null,twelveHourClock:false},yaxis:{autoscaleMargin:0.02,position:"left"},xaxes:[],yaxes:[],series:{points:{show:false,radius:3,lineWidth:2,fill:true,fillColor:"#ffffff",symbol:"circle"},lines:{lineWidth:2,fill:false,fillColor:null,steps:false},bars:{show:false,lineWidth:2,barWidth:1,fill:true,fillColor:null,align:"left",horizontal:false},shadowSize:3},grid:{show:true,aboveData:false,color:"#545454",backgroundColor:null,borderColor:null,tickColor:null,labelMargin:5,axisMargin:8,borderWidth:2,minBorderMargin:null,markings:null,markingsColor:"#f4f4f4",markingsLineWidth:2,clickable:false,hoverable:false,autoHighlight:true,mouseActiveRadius:10},hooks:{}},az=null,ad=null,y=null,H=null,A=null,p=[],aw=[],q={left:0,right:0,top:0,bottom:0},G=0,I=0,h=0,w=0,ak={processOptions:[],processRawData:[],processDatapoints:[],drawSeries:[],draw:[],bindEvents:[],drawOverlay:[],shutdown:[]},aq=this;
aq.setData=aj;
aq.setupGrid=t;
aq.draw=W;
aq.getPlaceholder=function(){return av
};
aq.getCanvas=function(){return az
};
aq.getPlotOffset=function(){return q
};
aq.width=function(){return h
};
aq.height=function(){return w
};
aq.offset=function(){var aB=y.offset();
aB.left+=q.left;
aB.top+=q.top;
return aB
};
aq.getData=function(){return Q
};
aq.getAxes=function(){var aC={},aB;
c.each(p.concat(aw),function(aD,aE){if(aE){aC[aE.direction+(aE.n!=1?aE.n:"")+"axis"]=aE
}});
return aC
};
aq.getXAxes=function(){return p
};
aq.getYAxes=function(){return aw
};
aq.c2p=C;
aq.p2c=ar;
aq.getOptions=function(){return O
};
aq.highlight=x;
aq.unhighlight=T;
aq.triggerRedrawOverlay=f;
aq.pointOffset=function(aB){return{left:parseInt(p[aA(aB,"x")-1].p2c(+aB.x)+q.left),top:parseInt(aw[aA(aB,"y")-1].p2c(+aB.y)+q.top)}
};
aq.shutdown=ag;
aq.resize=function(){B();
g(az);
g(ad)
};
aq.hooks=ak;
F(aq);
Z(J);
X();
aj(ai);
t();
W();
ah();
function an(aD,aB){aB=[aq].concat(aB);
for(var aC=0;
aC<aD.length;
++aC){aD[aC].apply(this,aB)
}}function F(){for(var aB=0;
aB<af.length;
++aB){var aC=af[aB];
aC.init(aq);
if(aC.options){c.extend(true,O,aC.options)
}}}function Z(aC){var aB;
c.extend(true,O,aC);
if(O.xaxis.color==null){O.xaxis.color=O.grid.color
}if(O.yaxis.color==null){O.yaxis.color=O.grid.color
}if(O.xaxis.tickColor==null){O.xaxis.tickColor=O.grid.tickColor
}if(O.yaxis.tickColor==null){O.yaxis.tickColor=O.grid.tickColor
}if(O.grid.borderColor==null){O.grid.borderColor=O.grid.color
}if(O.grid.tickColor==null){O.grid.tickColor=c.color.parse(O.grid.color).scale("a",0.22).toString()
}for(aB=0;
aB<Math.max(1,O.xaxes.length);
++aB){O.xaxes[aB]=c.extend(true,{},O.xaxis,O.xaxes[aB])
}for(aB=0;
aB<Math.max(1,O.yaxes.length);
++aB){O.yaxes[aB]=c.extend(true,{},O.yaxis,O.yaxes[aB])
}if(O.xaxis.noTicks&&O.xaxis.ticks==null){O.xaxis.ticks=O.xaxis.noTicks
}if(O.yaxis.noTicks&&O.yaxis.ticks==null){O.yaxis.ticks=O.yaxis.noTicks
}if(O.x2axis){O.xaxes[1]=c.extend(true,{},O.xaxis,O.x2axis);
O.xaxes[1].position="top"
}if(O.y2axis){O.yaxes[1]=c.extend(true,{},O.yaxis,O.y2axis);
O.yaxes[1].position="right"
}if(O.grid.coloredAreas){O.grid.markings=O.grid.coloredAreas
}if(O.grid.coloredAreasColor){O.grid.markingsColor=O.grid.coloredAreasColor
}if(O.lines){c.extend(true,O.series.lines,O.lines)
}if(O.points){c.extend(true,O.series.points,O.points)
}if(O.bars){c.extend(true,O.series.bars,O.bars)
}if(O.shadowSize!=null){O.series.shadowSize=O.shadowSize
}for(aB=0;
aB<O.xaxes.length;
++aB){V(p,aB+1).options=O.xaxes[aB]
}for(aB=0;
aB<O.yaxes.length;
++aB){V(aw,aB+1).options=O.yaxes[aB]
}for(var aD in ak){if(O.hooks[aD]&&O.hooks[aD].length){ak[aD]=ak[aD].concat(O.hooks[aD])
}}an(ak.processOptions,[O])
}function aj(aB){Q=Y(aB);
ax();
z()
}function Y(aE){var aC=[];
for(var aB=0;
aB<aE.length;
++aB){var aD=c.extend(true,{},O.series);
if(aE[aB].data!=null){aD.data=aE[aB].data;
delete aE[aB].data;
c.extend(true,aD,aE[aB]);
aE[aB].data=aD.data
}else{aD.data=aE[aB]
}aC.push(aD)
}return aC
}function aA(aC,aD){var aB=aC[aD+"axis"];
if(typeof aB=="object"){aB=aB.n
}if(typeof aB!="number"){aB=1
}return aB
}function m(){return c.grep(p.concat(aw),function(aB){return aB
})
}function C(aE){var aC={},aB,aD;
for(aB=0;
aB<p.length;
++aB){aD=p[aB];
if(aD&&aD.used){aC["x"+aD.n]=aD.c2p(aE.left)
}}for(aB=0;
aB<aw.length;
++aB){aD=aw[aB];
if(aD&&aD.used){aC["y"+aD.n]=aD.c2p(aE.top)
}}if(aC.x1!==undefined){aC.x=aC.x1
}if(aC.y1!==undefined){aC.y=aC.y1
}return aC
}function ar(aF){var aD={},aC,aE,aB;
for(aC=0;
aC<p.length;
++aC){aE=p[aC];
if(aE&&aE.used){aB="x"+aE.n;
if(aF[aB]==null&&aE.n==1){aB="x"
}if(aF[aB]!=null){aD.left=aE.p2c(aF[aB]);
break
}}}for(aC=0;
aC<aw.length;
++aC){aE=aw[aC];
if(aE&&aE.used){aB="y"+aE.n;
if(aF[aB]==null&&aE.n==1){aB="y"
}if(aF[aB]!=null){aD.top=aE.p2c(aF[aB]);
break
}}}return aD
}function V(aC,aB){if(!aC[aB-1]){aC[aB-1]={n:aB,direction:aC==p?"x":"y",options:c.extend(true,{},aC==p?O.xaxis:O.yaxis)}
}return aC[aB-1]
}function ax(){var aG;
var aM=Q.length,aB=[],aE=[];
for(aG=0;
aG<Q.length;
++aG){var aJ=Q[aG].color;
if(aJ!=null){--aM;
if(typeof aJ=="number"){aE.push(aJ)
}else{aB.push(c.color.parse(Q[aG].color))
}}}for(aG=0;
aG<aE.length;
++aG){aM=Math.max(aM,aE[aG]+1)
}var aC=[],aF=0;
aG=0;
while(aC.length<aM){var aI;
if(O.colors.length==aG){aI=c.color.make(100,100,100)
}else{aI=c.color.parse(O.colors[aG])
}var aD=aF%2==1?-1:1;
aI.scale("rgb",1+aD*Math.ceil(aF/2)*0.2);
aC.push(aI);
++aG;
if(aG>=O.colors.length){aG=0;
++aF
}}var aH=0,aN;
for(aG=0;
aG<Q.length;
++aG){aN=Q[aG];
if(aN.color==null){aN.color=aC[aH].toString();
++aH
}else{if(typeof aN.color=="number"){aN.color=aC[aN.color].toString()
}}if(aN.lines.show==null){var aL,aK=true;
for(aL in aN){if(aN[aL]&&aN[aL].show){aK=false;
break
}}if(aK){aN.lines.show=true
}}aN.xaxis=V(p,aA(aN,"x"));
aN.yaxis=V(aw,aA(aN,"y"))
}}function z(){var aO=Number.POSITIVE_INFINITY,aI=Number.NEGATIVE_INFINITY,aB=Number.MAX_VALUE,aU,aS,aR,aN,aD,aJ,aT,aP,aH,aG,aC,a0,aX,aL;
function aF(a3,a2,a1){if(a2<a3.datamin&&a2!=-aB){a3.datamin=a2
}if(a1>a3.datamax&&a1!=aB){a3.datamax=a1
}}c.each(m(),function(a1,a2){a2.datamin=aO;
a2.datamax=aI;
a2.used=false
});
for(aU=0;
aU<Q.length;
++aU){aJ=Q[aU];
aJ.datapoints={points:[]};
an(ak.processRawData,[aJ,aJ.data,aJ.datapoints])
}for(aU=0;
aU<Q.length;
++aU){aJ=Q[aU];
var aZ=aJ.data,aW=aJ.datapoints.format;
if(!aW){aW=[];
aW.push({x:true,number:true,required:true});
aW.push({y:true,number:true,required:true});
if(aJ.bars.show||(aJ.lines.show&&aJ.lines.fill)){aW.push({y:true,number:true,required:false,defaultValue:0});
if(aJ.bars.horizontal){delete aW[aW.length-1].y;
aW[aW.length-1].x=true
}}aJ.datapoints.format=aW
}if(aJ.datapoints.pointsize!=null){continue
}aJ.datapoints.pointsize=aW.length;
aP=aJ.datapoints.pointsize;
aT=aJ.datapoints.points;
insertSteps=aJ.lines.show&&aJ.lines.steps;
aJ.xaxis.used=aJ.yaxis.used=true;
for(aS=aR=0;
aS<aZ.length;
++aS,aR+=aP){aL=aZ[aS];
var aE=aL==null;
if(!aE){for(aN=0;
aN<aP;
++aN){a0=aL[aN];
aX=aW[aN];
if(aX){if(aX.number&&a0!=null){a0=+a0;
if(isNaN(a0)){a0=null
}else{if(a0==Infinity){a0=aB
}else{if(a0==-Infinity){a0=-aB
}}}}if(a0==null){if(aX.required){aE=true
}if(aX.defaultValue!=null){a0=aX.defaultValue
}}}aT[aR+aN]=a0
}}if(aE){for(aN=0;
aN<aP;
++aN){a0=aT[aR+aN];
if(a0!=null){aX=aW[aN];
if(aX.x){aF(aJ.xaxis,a0,a0)
}if(aX.y){aF(aJ.yaxis,a0,a0)
}}aT[aR+aN]=null
}}else{if(insertSteps&&aR>0&&aT[aR-aP]!=null&&aT[aR-aP]!=aT[aR]&&aT[aR-aP+1]!=aT[aR+1]){for(aN=0;
aN<aP;
++aN){aT[aR+aP+aN]=aT[aR+aN]
}aT[aR+1]=aT[aR-aP+1];
aR+=aP
}}}}for(aU=0;
aU<Q.length;
++aU){aJ=Q[aU];
an(ak.processDatapoints,[aJ,aJ.datapoints])
}for(aU=0;
aU<Q.length;
++aU){aJ=Q[aU];
aT=aJ.datapoints.points,aP=aJ.datapoints.pointsize;
var aK=aO,aQ=aO,aM=aI,aV=aI;
for(aS=0;
aS<aT.length;
aS+=aP){if(aT[aS]==null){continue
}for(aN=0;
aN<aP;
++aN){a0=aT[aS+aN];
aX=aW[aN];
if(!aX||a0==aB||a0==-aB){continue
}if(aX.x){if(a0<aK){aK=a0
}if(a0>aM){aM=a0
}}if(aX.y){if(a0<aQ){aQ=a0
}if(a0>aV){aV=a0
}}}}if(aJ.bars.show){var aY=aJ.bars.align=="left"?0:-aJ.bars.barWidth/2;
if(aJ.bars.horizontal){aQ+=aY;
aV+=aY+aJ.bars.barWidth
}else{aK+=aY;
aM+=aY+aJ.bars.barWidth
}}aF(aJ.xaxis,aK,aM);
aF(aJ.yaxis,aQ,aV)
}c.each(m(),function(a1,a2){if(a2.datamin==aO){a2.datamin=null
}if(a2.datamax==aI){a2.datamax=null
}})
}function j(aB,aC){var aD=document.createElement("canvas");
aD.className=aC;
aD.width=G;
aD.height=I;
if(!aB){c(aD).css({position:"absolute",left:0,top:0})
}c(aD).appendTo(av);
if(!aD.getContext){aD=window.G_vmlCanvasManager.initElement(aD)
}aD.getContext("2d").save();
return aD
}function B(){G=av.width();
I=av.height();
if(G<=0||I<=0){throw"Invalid dimensions for plot, width = "+G+", height = "+I
}}function g(aC){if(aC.width!=G){aC.width=G
}if(aC.height!=I){aC.height=I
}var aB=aC.getContext("2d");
aB.restore();
aB.save()
}function X(){var aC,aB=av.children("canvas.base"),aD=av.children("canvas.overlay");
if(aB.length==0||aD==0){av.html("");
av.css({padding:0});
if(av.css("position")=="static"){av.css("position","relative")
}B();
az=j(true,"base");
ad=j(false,"overlay");
aC=false
}else{az=aB.get(0);
ad=aD.get(0);
aC=true
}H=az.getContext("2d");
A=ad.getContext("2d");
y=c([ad,az]);
if(aC){av.data("plot").shutdown();
aq.resize();
A.clearRect(0,0,G,I);
y.unbind();
av.children().not([az,ad]).remove()
}av.data("plot",aq)
}function ah(){if(O.grid.hoverable){y.mousemove(aa);
y.mouseleave(l)
}if(O.grid.clickable){y.click(R)
}an(ak.bindEvents,[y])
}function ag(){if(M){clearTimeout(M)
}y.unbind("mousemove",aa);
y.unbind("mouseleave",l);
y.unbind("click",R);
an(ak.shutdown,[y])
}function r(aG){function aC(aH){return aH
}var aF,aB,aD=aG.options.transform||aC,aE=aG.options.inverseTransform;
if(aG.direction=="x"){aF=aG.scale=h/Math.abs(aD(aG.max)-aD(aG.min));
aB=Math.min(aD(aG.max),aD(aG.min))
}else{aF=aG.scale=w/Math.abs(aD(aG.max)-aD(aG.min));
aF=-aF;
aB=Math.max(aD(aG.max),aD(aG.min))
}if(aD==aC){aG.p2c=function(aH){return(aH-aB)*aF
}
}else{aG.p2c=function(aH){return(aD(aH)-aB)*aF
}
}if(!aE){aG.c2p=function(aH){return aB+aH/aF
}
}else{aG.c2p=function(aH){return aE(aB+aH/aF)
}
}}function L(aD){var aB=aD.options,aF,aJ=aD.ticks||[],aI=[],aE,aK=aB.labelWidth,aG=aB.labelHeight,aC;
function aH(aM,aL){return c('<div style="position:absolute;top:-10000px;'+aL+'font-size:smaller">'+'<div class="'+aD.direction+"Axis "+aD.direction+aD.n+'Axis">'+aM.join("")+"</div></div>").appendTo(av)
}if(aD.direction=="x"){if(aK==null){aK=Math.floor(G/(aJ.length>0?aJ.length:1))
}if(aG==null){aI=[];
for(aF=0;
aF<aJ.length;
++aF){aE=aJ[aF].label;
if(aE){aI.push('<div class="tickLabel" style="float:left;width:'+aK+'px">'+aE+"</div>")
}}if(aI.length>0){aI.push('<div style="clear:left"></div>');
aC=aH(aI,"width:10000px;");
aG=aC.height();
aC.remove()
}}}else{if(aK==null||aG==null){for(aF=0;
aF<aJ.length;
++aF){aE=aJ[aF].label;
if(aE){aI.push('<div class="tickLabel">'+aE+"</div>")
}}if(aI.length>0){aC=aH(aI,"");
if(aK==null){aK=aC.children().width()
}if(aG==null){aG=aC.find("div.tickLabel").height()
}aC.remove()
}}}if(aK==null){aK=0
}if(aG==null){aG=0
}aD.labelWidth=aK;
aD.labelHeight=aG
}function au(aD){var aC=aD.labelWidth,aL=aD.labelHeight,aH=aD.options.position,aF=aD.options.tickLength,aG=O.grid.axisMargin,aJ=O.grid.labelMargin,aK=aD.direction=="x"?p:aw,aE;
var aB=c.grep(aK,function(aN){return aN&&aN.options.position==aH&&aN.reserveSpace
});
if(c.inArray(aD,aB)==aB.length-1){aG=0
}if(aF==null){aF="full"
}var aI=c.grep(aK,function(aN){return aN&&aN.reserveSpace
});
var aM=c.inArray(aD,aI)==0;
if(!aM&&aF=="full"){aF=5
}if(!isNaN(+aF)){aJ+=+aF
}if(aD.direction=="x"){aL+=aJ;
if(aH=="bottom"){q.bottom+=aL+aG;
aD.box={top:I-q.bottom,height:aL}
}else{aD.box={top:q.top+aG,height:aL};
q.top+=aL+aG
}}else{aC+=aJ;
if(aH=="left"){aD.box={left:q.left+aG,width:aC};
q.left+=aC+aG
}else{q.right+=aC+aG;
aD.box={left:G-q.right,width:aC}
}}aD.position=aH;
aD.tickLength=aF;
aD.box.padding=aJ;
aD.innermost=aM
}function U(aB){if(aB.direction=="x"){aB.box.left=q.left;
aB.box.width=h
}else{aB.box.top=q.top;
aB.box.height=w
}}function t(){var aC,aE=m();
c.each(aE,function(aF,aG){aG.show=aG.options.show;
if(aG.show==null){aG.show=aG.used
}aG.reserveSpace=aG.show||aG.options.reserveSpace;
n(aG)
});
allocatedAxes=c.grep(aE,function(aF){return aF.reserveSpace
});
q.left=q.right=q.top=q.bottom=0;
if(O.grid.show){c.each(allocatedAxes,function(aF,aG){S(aG);
P(aG);
ap(aG,aG.ticks);
L(aG)
});
for(aC=allocatedAxes.length-1;
aC>=0;
--aC){au(allocatedAxes[aC])
}var aD=O.grid.minBorderMargin;
if(aD==null){aD=0;
for(aC=0;
aC<Q.length;
++aC){aD=Math.max(aD,Q[aC].points.radius+Q[aC].points.lineWidth/2)
}}for(var aB in q){q[aB]+=O.grid.borderWidth;
q[aB]=Math.max(aD,q[aB])
}}h=G-q.left-q.right;
w=I-q.bottom-q.top;
c.each(aE,function(aF,aG){r(aG)
});
if(O.grid.show){c.each(allocatedAxes,function(aF,aG){U(aG)
});
k()
}o()
}function n(aE){var aF=aE.options,aD=+(aF.min!=null?aF.min:aE.datamin),aB=+(aF.max!=null?aF.max:aE.datamax),aH=aB-aD;
if(aH==0){var aC=aB==0?1:0.01;
if(aF.min==null){aD-=aC
}if(aF.max==null||aF.min!=null){aB+=aC
}}else{var aG=aF.autoscaleMargin;
if(aG!=null){if(aF.min==null){aD-=aH*aG;
if(aD<0&&aE.datamin!=null&&aE.datamin>=0){aD=0
}}if(aF.max==null){aB+=aH*aG;
if(aB>0&&aE.datamax!=null&&aE.datamax<=0){aB=0
}}}}aE.min=aD;
aE.max=aB
}function S(aG){var aM=aG.options;
var aH;
if(typeof aM.ticks=="number"&&aM.ticks>0){aH=aM.ticks
}else{aH=0.3*Math.sqrt(aG.direction=="x"?G:I)
}var aT=(aG.max-aG.min)/aH,aO,aB,aN,aR,aS,aQ,aI;
if(aM.mode=="time"){var aJ={"second":1000,"minute":60*1000,"hour":60*60*1000,"day":24*60*60*1000,"month":30*24*60*60*1000,"year":365.2425*24*60*60*1000};
var aK=[[1,"second"],[2,"second"],[5,"second"],[10,"second"],[30,"second"],[1,"minute"],[2,"minute"],[5,"minute"],[10,"minute"],[30,"minute"],[1,"hour"],[2,"hour"],[4,"hour"],[8,"hour"],[12,"hour"],[1,"day"],[2,"day"],[3,"day"],[0.25,"month"],[0.5,"month"],[1,"month"],[2,"month"],[3,"month"],[6,"month"],[1,"year"]];
var aC=0;
if(aM.minTickSize!=null){if(typeof aM.tickSize=="number"){aC=aM.tickSize
}else{aC=aM.minTickSize[0]*aJ[aM.minTickSize[1]]
}}for(var aS=0;
aS<aK.length-1;
++aS){if(aT<(aK[aS][0]*aJ[aK[aS][1]]+aK[aS+1][0]*aJ[aK[aS+1][1]])/2&&aK[aS][0]*aJ[aK[aS][1]]>=aC){break
}}aO=aK[aS][0];
aN=aK[aS][1];
if(aN=="year"){aQ=Math.pow(10,Math.floor(Math.log(aT/aJ.year)/Math.LN10));
aI=(aT/aJ.year)/aQ;
if(aI<1.5){aO=1
}else{if(aI<3){aO=2
}else{if(aI<7.5){aO=5
}else{aO=10
}}}aO*=aQ
}aG.tickSize=aM.tickSize||[aO,aN];
aB=function(aX){var a2=[],a0=aX.tickSize[0],a3=aX.tickSize[1],a1=new Date(aX.min);
var aW=a0*aJ[a3];
if(a3=="second"){a1.setUTCSeconds(a(a1.getUTCSeconds(),a0))
}if(a3=="minute"){a1.setUTCMinutes(a(a1.getUTCMinutes(),a0))
}if(a3=="hour"){a1.setUTCHours(a(a1.getUTCHours(),a0))
}if(a3=="month"){a1.setUTCMonth(a(a1.getUTCMonth(),a0))
}if(a3=="year"){a1.setUTCFullYear(a(a1.getUTCFullYear(),a0))
}a1.setUTCMilliseconds(0);
if(aW>=aJ.minute){a1.setUTCSeconds(0)
}if(aW>=aJ.hour){a1.setUTCMinutes(0)
}if(aW>=aJ.day){a1.setUTCHours(0)
}if(aW>=aJ.day*4){a1.setUTCDate(1)
}if(aW>=aJ.year){a1.setUTCMonth(0)
}var a5=0,a4=Number.NaN,aY;
do{aY=a4;
a4=a1.getTime();
a2.push(a4);
if(a3=="month"){if(a0<1){a1.setUTCDate(1);
var aV=a1.getTime();
a1.setUTCMonth(a1.getUTCMonth()+1);
var aZ=a1.getTime();
a1.setTime(a4+a5*aJ.hour+(aZ-aV)*a0);
a5=a1.getUTCHours();
a1.setUTCHours(0)
}else{a1.setUTCMonth(a1.getUTCMonth()+a0)
}}else{if(a3=="year"){a1.setUTCFullYear(a1.getUTCFullYear()+a0)
}else{a1.setTime(a4+aW)
}}}while(a4<aX.max&&a4!=aY);
return a2
};
aR=function(aV,aY){var a0=new Date(aV);
if(aM.timeformat!=null){return c.plot.formatDate(a0,aM.timeformat,aM.monthNames)
}var aW=aY.tickSize[0]*aJ[aY.tickSize[1]];
var aX=aY.max-aY.min;
var aZ=(aM.twelveHourClock)?" %p":"";
if(aW<aJ.minute){fmt="%h:%M:%S"+aZ
}else{if(aW<aJ.day){if(aX<2*aJ.day){fmt="%h:%M"+aZ
}else{fmt="%b %d %h:%M"+aZ
}}else{if(aW<aJ.month){fmt="%b %d"
}else{if(aW<aJ.year){if(aX<aJ.year){fmt="%b"
}else{fmt="%b %y"
}}else{fmt="%y"
}}}}return c.plot.formatDate(a0,fmt,aM.monthNames)
}
}else{var aU=aM.tickDecimals;
var aP=-Math.floor(Math.log(aT)/Math.LN10);
if(aU!=null&&aP>aU){aP=aU
}aQ=Math.pow(10,-aP);
aI=aT/aQ;
if(aI<1.5){aO=1
}else{if(aI<3){aO=2;
if(aI>2.25&&(aU==null||aP+1<=aU)){aO=2.5;
++aP
}}else{if(aI<7.5){aO=5
}else{aO=10
}}}aO*=aQ;
if(aM.minTickSize!=null&&aO<aM.minTickSize){aO=aM.minTickSize
}aG.tickDecimals=Math.max(0,aU!=null?aU:aP);
aG.tickSize=aM.tickSize||aO;
aB=function(aX){var aZ=[];
var a0=a(aX.min,aX.tickSize),aW=0,aV=Number.NaN,aY;
do{aY=aV;
aV=a0+aW*aX.tickSize;
aZ.push(aV);
++aW
}while(aV<aX.max&&aV!=aY);
return aZ
};
aR=function(aV,aW){return aV.toFixed(aW.tickDecimals)
}
}if(aM.alignTicksWithAxis!=null){var aF=(aG.direction=="x"?p:aw)[aM.alignTicksWithAxis-1];
if(aF&&aF.used&&aF!=aG){var aL=aB(aG);
if(aL.length>0){if(aM.min==null){aG.min=Math.min(aG.min,aL[0])
}if(aM.max==null&&aL.length>1){aG.max=Math.max(aG.max,aL[aL.length-1])
}}aB=function(aX){var aY=[],aV,aW;
for(aW=0;
aW<aF.ticks.length;
++aW){aV=(aF.ticks[aW].v-aF.min)/(aF.max-aF.min);
aV=aX.min+aV*(aX.max-aX.min);
aY.push(aV)
}return aY
};
if(aG.mode!="time"&&aM.tickDecimals==null){var aE=Math.max(0,-Math.floor(Math.log(aT)/Math.LN10)+1),aD=aB(aG);
if(!(aD.length>1&&/\..*0$/.test((aD[1]-aD[0]).toFixed(aE)))){aG.tickDecimals=aE
}}}}aG.tickGenerator=aB;
if(c.isFunction(aM.tickFormatter)){aG.tickFormatter=function(aV,aW){return""+aM.tickFormatter(aV,aW)
}
}else{aG.tickFormatter=aR
}}function P(aF){var aH=aF.options.ticks,aG=[];
if(aH==null||(typeof aH=="number"&&aH>0)){aG=aF.tickGenerator(aF)
}else{if(aH){if(c.isFunction(aH)){aG=aH({min:aF.min,max:aF.max})
}else{aG=aH
}}}var aE,aB;
aF.ticks=[];
for(aE=0;
aE<aG.length;
++aE){var aC=null;
var aD=aG[aE];
if(typeof aD=="object"){aB=+aD[0];
if(aD.length>1){aC=aD[1]
}}else{aB=+aD
}if(aC==null){aC=aF.tickFormatter(aB,aF)
}if(!isNaN(aB)){aF.ticks.push({v:aB,label:aC})
}}}function ap(aB,aC){if(aB.options.autoscaleMargin&&aC.length>0){if(aB.options.min==null){aB.min=Math.min(aB.min,aC[0].v)
}if(aB.options.max==null&&aC.length>1){aB.max=Math.max(aB.max,aC[aC.length-1].v)
}}}function W(){H.clearRect(0,0,G,I);
var aC=O.grid;
if(aC.show&&aC.backgroundColor){N()
}if(aC.show&&!aC.aboveData){ac()
}for(var aB=0;
aB<Q.length;
++aB){an(ak.drawSeries,[H,Q[aB]]);
d(Q[aB])
}an(ak.draw,[H]);
if(aC.show&&aC.aboveData){ac()
}}function D(aB,aI){var aE,aH,aG,aD,aF=m();
for(i=0;
i<aF.length;
++i){aE=aF[i];
if(aE.direction==aI){aD=aI+aE.n+"axis";
if(!aB[aD]&&aE.n==1){aD=aI+"axis"
}if(aB[aD]){aH=aB[aD].from;
aG=aB[aD].to;
break
}}}if(!aB[aD]){aE=aI=="x"?p[0]:aw[0];
aH=aB[aI+"1"];
aG=aB[aI+"2"]
}if(aH!=null&&aG!=null&&aH>aG){var aC=aH;
aH=aG;
aG=aC
}return{from:aH,to:aG,axis:aE}
}function N(){H.save();
H.translate(q.left,q.top);
H.fillStyle=am(O.grid.backgroundColor,w,0,"rgba(255, 255, 255, 0)");
H.fillRect(0,0,h,w);
H.restore()
}function ac(){var aF;
H.save();
H.translate(q.left,q.top);
var aH=O.grid.markings;
if(aH){if(c.isFunction(aH)){var aK=aq.getAxes();
aK.xmin=aK.xaxis.min;
aK.xmax=aK.xaxis.max;
aK.ymin=aK.yaxis.min;
aK.ymax=aK.yaxis.max;
aH=aH(aK)
}for(aF=0;
aF<aH.length;
++aF){var aD=aH[aF],aC=D(aD,"x"),aI=D(aD,"y");
if(aC.from==null){aC.from=aC.axis.min
}if(aC.to==null){aC.to=aC.axis.max
}if(aI.from==null){aI.from=aI.axis.min
}if(aI.to==null){aI.to=aI.axis.max
}if(aC.to<aC.axis.min||aC.from>aC.axis.max||aI.to<aI.axis.min||aI.from>aI.axis.max){continue
}aC.from=Math.max(aC.from,aC.axis.min);
aC.to=Math.min(aC.to,aC.axis.max);
aI.from=Math.max(aI.from,aI.axis.min);
aI.to=Math.min(aI.to,aI.axis.max);
if(aC.from==aC.to&&aI.from==aI.to){continue
}aC.from=aC.axis.p2c(aC.from);
aC.to=aC.axis.p2c(aC.to);
aI.from=aI.axis.p2c(aI.from);
aI.to=aI.axis.p2c(aI.to);
if(aC.from==aC.to||aI.from==aI.to){H.beginPath();
H.strokeStyle=aD.color||O.grid.markingsColor;
H.lineWidth=aD.lineWidth||O.grid.markingsLineWidth;
H.moveTo(aC.from,aI.from);
H.lineTo(aC.to,aI.to);
H.stroke()
}else{H.fillStyle=aD.color||O.grid.markingsColor;
H.fillRect(aC.from,aI.to,aC.to-aC.from,aI.from-aI.to)
}}}var aK=m(),aM=O.grid.borderWidth;
for(var aE=0;
aE<aK.length;
++aE){var aB=aK[aE],aG=aB.box,aQ=aB.tickLength,aN,aL,aP,aJ;
if(!aB.show||aB.ticks.length==0){continue
}H.strokeStyle=aB.options.tickColor||c.color.parse(aB.options.color).scale("a",0.22).toString();
H.lineWidth=1;
if(aB.direction=="x"){aN=0;
if(aQ=="full"){aL=(aB.position=="top"?0:w)
}else{aL=aG.top-q.top+(aB.position=="top"?aG.height:0)
}}else{aL=0;
if(aQ=="full"){aN=(aB.position=="left"?0:h)
}else{aN=aG.left-q.left+(aB.position=="left"?aG.width:0)
}}if(!aB.innermost){H.beginPath();
aP=aJ=0;
if(aB.direction=="x"){aP=h
}else{aJ=w
}if(H.lineWidth==1){aN=Math.floor(aN)+0.5;
aL=Math.floor(aL)+0.5
}H.moveTo(aN,aL);
H.lineTo(aN+aP,aL+aJ);
H.stroke()
}H.beginPath();
for(aF=0;
aF<aB.ticks.length;
++aF){var aO=aB.ticks[aF].v;
aP=aJ=0;
if(aO<aB.min||aO>aB.max||(aQ=="full"&&aM>0&&(aO==aB.min||aO==aB.max))){continue
}if(aB.direction=="x"){aN=aB.p2c(aO);
aJ=aQ=="full"?-w:aQ;
if(aB.position=="top"){aJ=-aJ
}}else{aL=aB.p2c(aO);
aP=aQ=="full"?-h:aQ;
if(aB.position=="left"){aP=-aP
}}if(H.lineWidth==1){if(aB.direction=="x"){aN=Math.floor(aN)+0.5
}else{aL=Math.floor(aL)+0.5
}}H.moveTo(aN,aL);
H.lineTo(aN+aP,aL+aJ)
}H.stroke()
}if(aM){H.lineWidth=aM;
H.strokeStyle=O.grid.borderColor;
H.strokeRect(-aM/2,-aM/2,h+aM,w+aM)
}H.restore()
}function k(){av.find(".tickLabels").remove();
var aG=['<div class="tickLabels" style="font-size:smaller">'];
var aJ=m();
for(var aD=0;
aD<aJ.length;
++aD){var aC=aJ[aD],aF=aC.box;
if(!aC.show){continue
}aG.push('<div class="'+aC.direction+"Axis "+aC.direction+aC.n+'Axis" style="color:'+aC.options.color+'">');
for(var aE=0;
aE<aC.ticks.length;
++aE){var aH=aC.ticks[aE];
if(!aH.label||aH.v<aC.min||aH.v>aC.max){continue
}var aK={},aI;
if(aC.direction=="x"){aI="center";
aK.left=Math.round(q.left+aC.p2c(aH.v)-aC.labelWidth/2);
if(aC.position=="bottom"){aK.top=aF.top+aF.padding
}else{aK.bottom=I-(aF.top+aF.height-aF.padding)
}}else{aK.top=Math.round(q.top+aC.p2c(aH.v)-aC.labelHeight/2);
if(aC.position=="left"){aK.right=G-(aF.left+aF.width-aF.padding);
aI="right"
}else{aK.left=aF.left+aF.padding;
aI="left"
}}aK.width=aC.labelWidth;
var aB=["position:absolute","text-align:"+aI];
for(var aL in aK){aB.push(aL+":"+aK[aL]+"px")
}aG.push('<div class="tickLabel" style="'+aB.join(";")+'">'+aH.label+"</div>")
}aG.push("</div>")
}aG.push("</div>");
av.append(aG.join(""))
}function d(aB){if(aB.lines.show){at(aB)
}if(aB.bars.show){e(aB)
}if(aB.points.show){ao(aB)
}}function at(aE){function aD(aP,aQ,aI,aU,aT){var aV=aP.points,aJ=aP.pointsize,aN=null,aM=null;
H.beginPath();
for(var aO=aJ;
aO<aV.length;
aO+=aJ){var aL=aV[aO-aJ],aS=aV[aO-aJ+1],aK=aV[aO],aR=aV[aO+1];
if(aL==null||aK==null){continue
}if(aS<=aR&&aS<aT.min){if(aR<aT.min){continue
}aL=(aT.min-aS)/(aR-aS)*(aK-aL)+aL;
aS=aT.min
}else{if(aR<=aS&&aR<aT.min){if(aS<aT.min){continue
}aK=(aT.min-aS)/(aR-aS)*(aK-aL)+aL;
aR=aT.min
}}if(aS>=aR&&aS>aT.max){if(aR>aT.max){continue
}aL=(aT.max-aS)/(aR-aS)*(aK-aL)+aL;
aS=aT.max
}else{if(aR>=aS&&aR>aT.max){if(aS>aT.max){continue
}aK=(aT.max-aS)/(aR-aS)*(aK-aL)+aL;
aR=aT.max
}}if(aL<=aK&&aL<aU.min){if(aK<aU.min){continue
}aS=(aU.min-aL)/(aK-aL)*(aR-aS)+aS;
aL=aU.min
}else{if(aK<=aL&&aK<aU.min){if(aL<aU.min){continue
}aR=(aU.min-aL)/(aK-aL)*(aR-aS)+aS;
aK=aU.min
}}if(aL>=aK&&aL>aU.max){if(aK>aU.max){continue
}aS=(aU.max-aL)/(aK-aL)*(aR-aS)+aS;
aL=aU.max
}else{if(aK>=aL&&aK>aU.max){if(aL>aU.max){continue
}aR=(aU.max-aL)/(aK-aL)*(aR-aS)+aS;
aK=aU.max
}}if(aL!=aN||aS!=aM){H.moveTo(aU.p2c(aL)+aQ,aT.p2c(aS)+aI)
}aN=aK;
aM=aR;
H.lineTo(aU.p2c(aK)+aQ,aT.p2c(aR)+aI)
}H.stroke()
}function aF(aI,aQ,aP){var aW=aI.points,aV=aI.pointsize,aN=Math.min(Math.max(0,aP.min),aP.max),aX=0,aU,aT=false,aM=1,aL=0,aR=0;
while(true){if(aV>0&&aX>aW.length+aV){break
}aX+=aV;
var aZ=aW[aX-aV],aK=aW[aX-aV+aM],aY=aW[aX],aJ=aW[aX+aM];
if(aT){if(aV>0&&aZ!=null&&aY==null){aR=aX;
aV=-aV;
aM=2;
continue
}if(aV<0&&aX==aL+aV){H.fill();
aT=false;
aV=-aV;
aM=1;
aX=aL=aR+aV;
continue
}}if(aZ==null||aY==null){continue
}if(aZ<=aY&&aZ<aQ.min){if(aY<aQ.min){continue
}aK=(aQ.min-aZ)/(aY-aZ)*(aJ-aK)+aK;
aZ=aQ.min
}else{if(aY<=aZ&&aY<aQ.min){if(aZ<aQ.min){continue
}aJ=(aQ.min-aZ)/(aY-aZ)*(aJ-aK)+aK;
aY=aQ.min
}}if(aZ>=aY&&aZ>aQ.max){if(aY>aQ.max){continue
}aK=(aQ.max-aZ)/(aY-aZ)*(aJ-aK)+aK;
aZ=aQ.max
}else{if(aY>=aZ&&aY>aQ.max){if(aZ>aQ.max){continue
}aJ=(aQ.max-aZ)/(aY-aZ)*(aJ-aK)+aK;
aY=aQ.max
}}if(!aT){H.beginPath();
H.moveTo(aQ.p2c(aZ),aP.p2c(aN));
aT=true
}if(aK>=aP.max&&aJ>=aP.max){H.lineTo(aQ.p2c(aZ),aP.p2c(aP.max));
H.lineTo(aQ.p2c(aY),aP.p2c(aP.max));
continue
}else{if(aK<=aP.min&&aJ<=aP.min){H.lineTo(aQ.p2c(aZ),aP.p2c(aP.min));
H.lineTo(aQ.p2c(aY),aP.p2c(aP.min));
continue
}}var aO=aZ,aS=aY;
if(aK<=aJ&&aK<aP.min&&aJ>=aP.min){aZ=(aP.min-aK)/(aJ-aK)*(aY-aZ)+aZ;
aK=aP.min
}else{if(aJ<=aK&&aJ<aP.min&&aK>=aP.min){aY=(aP.min-aK)/(aJ-aK)*(aY-aZ)+aZ;
aJ=aP.min
}}if(aK>=aJ&&aK>aP.max&&aJ<=aP.max){aZ=(aP.max-aK)/(aJ-aK)*(aY-aZ)+aZ;
aK=aP.max
}else{if(aJ>=aK&&aJ>aP.max&&aK<=aP.max){aY=(aP.max-aK)/(aJ-aK)*(aY-aZ)+aZ;
aJ=aP.max
}}if(aZ!=aO){H.lineTo(aQ.p2c(aO),aP.p2c(aK))
}H.lineTo(aQ.p2c(aZ),aP.p2c(aK));
H.lineTo(aQ.p2c(aY),aP.p2c(aJ));
if(aY!=aS){H.lineTo(aQ.p2c(aY),aP.p2c(aJ));
H.lineTo(aQ.p2c(aS),aP.p2c(aJ))
}}}H.save();
H.translate(q.left,q.top);
H.lineJoin="round";
var aG=aE.lines.lineWidth,aB=aE.shadowSize;
if(aG>0&&aB>0){H.lineWidth=aB;
H.strokeStyle="rgba(0,0,0,0.1)";
var aH=Math.PI/18;
aD(aE.datapoints,Math.sin(aH)*(aG/2+aB/2),Math.cos(aH)*(aG/2+aB/2),aE.xaxis,aE.yaxis);
H.lineWidth=aB/2;
aD(aE.datapoints,Math.sin(aH)*(aG/2+aB/4),Math.cos(aH)*(aG/2+aB/4),aE.xaxis,aE.yaxis)
}H.lineWidth=aG;
H.strokeStyle=aE.color;
var aC=ae(aE.lines,aE.color,0,w);
if(aC){H.fillStyle=aC;
aF(aE.datapoints,aE.xaxis,aE.yaxis)
}if(aG>0){aD(aE.datapoints,0,0,aE.xaxis,aE.yaxis)
}H.restore()
}function ao(aE){function aH(aN,aM,aU,aK,aS,aT,aQ,aJ){var aR=aN.points,aI=aN.pointsize;
for(var aL=0;
aL<aR.length;
aL+=aI){var aP=aR[aL],aO=aR[aL+1];
if(aP==null||aP<aT.min||aP>aT.max||aO<aQ.min||aO>aQ.max){continue
}H.beginPath();
aP=aT.p2c(aP);
aO=aQ.p2c(aO)+aK;
if(aJ=="circle"){H.arc(aP,aO,aM,0,aS?Math.PI:Math.PI*2,false)
}else{aJ(H,aP,aO,aM,aS)
}H.closePath();
if(aU){H.fillStyle=aU;
H.fill()
}H.stroke()
}}H.save();
H.translate(q.left,q.top);
var aG=aE.points.lineWidth,aC=aE.shadowSize,aB=aE.points.radius,aF=aE.points.symbol;
if(aG>0&&aC>0){var aD=aC/2;
H.lineWidth=aD;
H.strokeStyle="rgba(0,0,0,0.1)";
aH(aE.datapoints,aB,null,aD+aD/2,true,aE.xaxis,aE.yaxis,aF);
H.strokeStyle="rgba(0,0,0,0.2)";
aH(aE.datapoints,aB,null,aD/2,true,aE.xaxis,aE.yaxis,aF)
}H.lineWidth=aG;
H.strokeStyle=aE.color;
aH(aE.datapoints,aB,ae(aE.points,aE.color),0,false,aE.xaxis,aE.yaxis,aF);
H.restore()
}function E(aN,aM,aV,aI,aQ,aF,aD,aL,aK,aU,aR,aC){var aE,aT,aJ,aP,aG,aB,aO,aH,aS;
if(aR){aH=aB=aO=true;
aG=false;
aE=aV;
aT=aN;
aP=aM+aI;
aJ=aM+aQ;
if(aT<aE){aS=aT;
aT=aE;
aE=aS;
aG=true;
aB=false
}}else{aG=aB=aO=true;
aH=false;
aE=aN+aI;
aT=aN+aQ;
aJ=aV;
aP=aM;
if(aP<aJ){aS=aP;
aP=aJ;
aJ=aS;
aH=true;
aO=false
}}if(aT<aL.min||aE>aL.max||aP<aK.min||aJ>aK.max){return
}if(aE<aL.min){aE=aL.min;
aG=false
}if(aT>aL.max){aT=aL.max;
aB=false
}if(aJ<aK.min){aJ=aK.min;
aH=false
}if(aP>aK.max){aP=aK.max;
aO=false
}aE=aL.p2c(aE);
aJ=aK.p2c(aJ);
aT=aL.p2c(aT);
aP=aK.p2c(aP);
if(aD){aU.beginPath();
aU.moveTo(aE,aJ);
aU.lineTo(aE,aP);
aU.lineTo(aT,aP);
aU.lineTo(aT,aJ);
aU.fillStyle=aD(aJ,aP);
aU.fill()
}if(aC>0&&(aG||aB||aO||aH)){aU.beginPath();
aU.moveTo(aE,aJ+aF);
if(aG){aU.lineTo(aE,aP+aF)
}else{aU.moveTo(aE,aP+aF)
}if(aO){aU.lineTo(aT,aP+aF)
}else{aU.moveTo(aT,aP+aF)
}if(aB){aU.lineTo(aT,aJ+aF)
}else{aU.moveTo(aT,aJ+aF)
}if(aH){aU.lineTo(aE,aJ+aF)
}else{aU.moveTo(aE,aJ+aF)
}aU.stroke()
}}function e(aD){function aC(aJ,aI,aL,aG,aK,aN,aM){var aO=aJ.points,aF=aJ.pointsize;
for(var aH=0;
aH<aO.length;
aH+=aF){if(aO[aH]==null){continue
}E(aO[aH],aO[aH+1],aO[aH+2],aI,aL,aG,aK,aN,aM,H,aD.bars.horizontal,aD.bars.lineWidth)
}}H.save();
H.translate(q.left,q.top);
H.lineWidth=aD.bars.lineWidth;
H.strokeStyle=aD.color;
var aB=aD.bars.align=="left"?0:-aD.bars.barWidth/2;
var aE=aD.bars.fill?function(aF,aG){return ae(aD.bars,aD.color,aF,aG)
}:null;
aC(aD.datapoints,aB,aB+aD.bars.barWidth,0,aE,aD.xaxis,aD.yaxis);
H.restore()
}function ae(aD,aB,aC,aF){var aE=aD.fill;
if(!aE){return null
}if(aD.fillColor){return am(aD.fillColor,aC,aF,aB)
}var aG=c.color.parse(aB);
aG.a=typeof aE=="number"?aE:0.4;
aG.normalize();
return aG.toString()
}function o(){av.find(".legend").remove();
if(!O.legend.show){return
}var aH=[],aF=false,aN=O.legend.labelFormatter,aM,aJ;
for(var aE=0;
aE<Q.length;
++aE){aM=Q[aE];
aJ=aM.label;
if(!aJ){continue
}if(aE%O.legend.noColumns==0){if(aF){aH.push("</tr>")
}aH.push("<tr>");
aF=true
}if(aN){aJ=aN(aJ,aM)
}aH.push('<td class="legendColorBox"><div style="border:1px solid '+O.legend.labelBoxBorderColor+';padding:1px"><div style="width:4px;height:0;border:5px solid '+aM.color+';overflow:hidden"></div></div></td>'+'<td class="legendLabel">'+aJ+"</td>")
}if(aF){aH.push("</tr>")
}if(aH.length==0){return
}var aL='<table style="font-size:smaller;color:'+O.grid.color+'">'+aH.join("")+"</table>";
if(O.legend.container!=null){c(O.legend.container).html(aL)
}else{var aI="",aC=O.legend.position,aD=O.legend.margin;
if(aD[0]==null){aD=[aD,aD]
}if(aC.charAt(0)=="n"){aI+="top:"+(aD[1]+q.top)+"px;"
}else{if(aC.charAt(0)=="s"){aI+="bottom:"+(aD[1]+q.bottom)+"px;"
}}if(aC.charAt(1)=="e"){aI+="right:"+(aD[0]+q.right)+"px;"
}else{if(aC.charAt(1)=="w"){aI+="left:"+(aD[0]+q.left)+"px;"
}}var aK=c('<div class="legend">'+aL.replace('style="','style="position:absolute;'+aI+";")+"</div>").appendTo(av);
if(O.legend.backgroundOpacity!=0){var aG=O.legend.backgroundColor;
if(aG==null){aG=O.grid.backgroundColor;
if(aG&&typeof aG=="string"){aG=c.color.parse(aG)
}else{aG=c.color.extract(aK,"background-color")
}aG.a=1;
aG=aG.toString()
}var aB=aK.children();
c('<div style="position:absolute;width:'+aB.width()+"px;height:"+aB.height()+"px;"+aI+"background-color:"+aG+';"> </div>').prependTo(aK).css("opacity",O.legend.backgroundOpacity)
}}}var ab=[],M=null;
function K(aI,aG,aD){var aO=O.grid.mouseActiveRadius,a0=aO*aO+1,aY=null,aR=false,aW,aU;
for(aW=Q.length-1;
aW>=0;
--aW){if(!aD(Q[aW])){continue
}var aP=Q[aW],aH=aP.xaxis,aF=aP.yaxis,aV=aP.datapoints.points,aT=aP.datapoints.pointsize,aQ=aH.c2p(aI),aN=aF.c2p(aG),aC=aO/aH.scale,aB=aO/aF.scale;
if(aH.options.inverseTransform){aC=Number.MAX_VALUE
}if(aF.options.inverseTransform){aB=Number.MAX_VALUE
}if(aP.lines.show||aP.points.show){for(aU=0;
aU<aV.length;
aU+=aT){var aK=aV[aU],aJ=aV[aU+1];
if(aK==null){continue
}if(aK-aQ>aC||aK-aQ<-aC||aJ-aN>aB||aJ-aN<-aB){continue
}var aM=Math.abs(aH.p2c(aK)-aI),aL=Math.abs(aF.p2c(aJ)-aG),aS=aM*aM+aL*aL;
if(aS<a0){a0=aS;
aY=[aW,aU/aT]
}}}if(aP.bars.show&&!aY){var aE=aP.bars.align=="left"?0:-aP.bars.barWidth/2,aX=aE+aP.bars.barWidth;
for(aU=0;
aU<aV.length;
aU+=aT){var aK=aV[aU],aJ=aV[aU+1],aZ=aV[aU+2];
if(aK==null){continue
}if(Q[aW].bars.horizontal?(aQ<=Math.max(aZ,aK)&&aQ>=Math.min(aZ,aK)&&aN>=aJ+aE&&aN<=aJ+aX):(aQ>=aK+aE&&aQ<=aK+aX&&aN>=Math.min(aZ,aJ)&&aN<=Math.max(aZ,aJ))){aY=[aW,aU/aT]
}}}}if(aY){aW=aY[0];
aU=aY[1];
aT=Q[aW].datapoints.pointsize;
return{datapoint:Q[aW].datapoints.points.slice(aU*aT,(aU+1)*aT),dataIndex:aU,series:Q[aW],seriesIndex:aW}
}return null
}function aa(aB){if(O.grid.hoverable){u("plothover",aB,function(aC){return aC["hoverable"]!=false
})
}}function l(aB){if(O.grid.hoverable){u("plothover",aB,function(aC){return false
})
}}function R(aB){u("plotclick",aB,function(aC){return aC["clickable"]!=false
})
}function u(aC,aB,aD){var aE=y.offset(),aH=aB.pageX-aE.left-q.left,aF=aB.pageY-aE.top-q.top,aJ=C({left:aH,top:aF});
aJ.pageX=aB.pageX;
aJ.pageY=aB.pageY;
var aK=K(aH,aF,aD);
if(aK){aK.pageX=parseInt(aK.series.xaxis.p2c(aK.datapoint[0])+aE.left+q.left);
aK.pageY=parseInt(aK.series.yaxis.p2c(aK.datapoint[1])+aE.top+q.top)
}if(O.grid.autoHighlight){for(var aG=0;
aG<ab.length;
++aG){var aI=ab[aG];
if(aI.auto==aC&&!(aK&&aI.series==aK.series&&aI.point[0]==aK.datapoint[0]&&aI.point[1]==aK.datapoint[1])){T(aI.series,aI.point)
}}if(aK){x(aK.series,aK.datapoint,aC)
}}av.trigger(aC,[aJ,aK])
}function f(){if(!M){M=setTimeout(s,30)
}}function s(){M=null;
A.save();
A.clearRect(0,0,G,I);
A.translate(q.left,q.top);
var aC,aB;
for(aC=0;
aC<ab.length;
++aC){aB=ab[aC];
if(aB.series.bars.show){v(aB.series,aB.point)
}else{ay(aB.series,aB.point)
}}A.restore();
an(ak.drawOverlay,[A])
}function x(aD,aB,aF){if(typeof aD=="number"){aD=Q[aD]
}if(typeof aB=="number"){var aE=aD.datapoints.pointsize;
aB=aD.datapoints.points.slice(aE*aB,aE*(aB+1))
}var aC=al(aD,aB);
if(aC==-1){ab.push({series:aD,point:aB,auto:aF});
f()
}else{if(!aF){ab[aC].auto=false
}}}function T(aD,aB){if(aD==null&&aB==null){ab=[];
f()
}if(typeof aD=="number"){aD=Q[aD]
}if(typeof aB=="number"){aB=aD.data[aB]
}var aC=al(aD,aB);
if(aC!=-1){ab.splice(aC,1);
f()
}}function al(aD,aE){for(var aB=0;
aB<ab.length;
++aB){var aC=ab[aB];
if(aC.series==aD&&aC.point[0]==aE[0]&&aC.point[1]==aE[1]){return aB
}}return -1
}function ay(aE,aD){var aC=aD[0],aI=aD[1],aH=aE.xaxis,aG=aE.yaxis;
if(aC<aH.min||aC>aH.max||aI<aG.min||aI>aG.max){return
}var aF=aE.points.radius+aE.points.lineWidth/2;
A.lineWidth=aF;
A.strokeStyle=c.color.parse(aE.color).scale("a",0.5).toString();
var aB=1.5*aF,aC=aH.p2c(aC),aI=aG.p2c(aI);
A.beginPath();
if(aE.points.symbol=="circle"){A.arc(aC,aI,aB,0,2*Math.PI,false)
}else{aE.points.symbol(A,aC,aI,aB,false)
}A.closePath();
A.stroke()
}function v(aE,aB){A.lineWidth=aE.bars.lineWidth;
A.strokeStyle=c.color.parse(aE.color).scale("a",0.5).toString();
var aD=c.color.parse(aE.color).scale("a",0.5).toString();
var aC=aE.bars.align=="left"?0:-aE.bars.barWidth/2;
E(aB[0],aB[1],aB[2]||0,aC,aC+aE.bars.barWidth,0,function(){return aD
},aE.xaxis,aE.yaxis,A,aE.bars.horizontal,aE.bars.lineWidth)
}function am(aJ,aB,aH,aC){if(typeof aJ=="string"){return aJ
}else{var aI=H.createLinearGradient(0,aH,0,aB);
for(var aE=0,aD=aJ.colors.length;
aE<aD;
++aE){var aF=aJ.colors[aE];
if(typeof aF!="string"){var aG=c.color.parse(aC);
if(aF.brightness!=null){aG=aG.scale("rgb",aF.brightness)
}if(aF.opacity!=null){aG.a*=aF.opacity
}aF=aG.toString()
}aI.addColorStop(aE/(aD-1),aF)
}return aI
}}}c.plot=function(g,e,d){var f=new b(c(g),e,d,c.plot.plugins);
return f
};
c.plot.version="0.7";
c.plot.plugins=[];
c.plot.formatDate=function(l,f,h){var o=function(d){d=""+d;
return d.length==1?"0"+d:d
};
var e=[];
var p=false,j=false;
var n=l.getUTCHours();
var k=n<12;
if(h==null){h=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]
}if(f.search(/%p|%P/)!=-1){if(n>12){n=n-12
}else{if(n==0){n=12
}}}for(var g=0;
g<f.length;
++g){var m=f.charAt(g);
if(p){switch(m){case"h":m=""+n;
break;
case"H":m=o(n);
break;
case"M":m=o(l.getUTCMinutes());
break;
case"S":m=o(l.getUTCSeconds());
break;
case"d":m=""+l.getUTCDate();
break;
case"m":m=""+(l.getUTCMonth()+1);
break;
case"y":m=""+l.getUTCFullYear();
break;
case"b":m=""+h[l.getUTCMonth()];
break;
case"p":m=(k)?(""+"am"):(""+"pm");
break;
case"P":m=(k)?(""+"AM"):(""+"PM");
break;
case"0":m="";
j=true;
break
}if(m&&j){m=o(m);
j=false
}e.push(m);
if(!j){p=false
}}else{if(m=="%"){p=true
}else{e.push(m)
}}}return e.join("")
};
function a(e,d){return d*Math.floor(e/d)
}})(jQuery);
(function(b){var a={series:{stack:null}};
function c(f){function d(k,j){var h=null;
for(var g=0;
g<j.length;
++g){if(k==j[g]){break
}if(j[g].stack==k.stack){h=j[g]
}}return h
}function e(C,v,g){if(v.stack==null){return
}var p=d(v,C.getData());
if(!p){return
}var z=g.pointsize,F=g.points,h=p.datapoints.pointsize,y=p.datapoints.points,t=[],x,w,k,J,I,r,u=v.lines.show,G=v.bars.horizontal,o=z>2&&(G?g.format[2].x:g.format[2].y),n=u&&v.lines.steps,E=true,q=G?1:0,H=G?0:1,D=0,B=0,A;
while(true){if(D>=F.length){break
}A=t.length;
if(F[D]==null){for(m=0;
m<z;
++m){t.push(F[D+m])
}D+=z
}else{if(B>=y.length){if(!u){for(m=0;
m<z;
++m){t.push(F[D+m])
}}D+=z
}else{if(y[B]==null){for(m=0;
m<z;
++m){t.push(null)
}E=true;
B+=h
}else{x=F[D+q];
w=F[D+H];
J=y[B+q];
I=y[B+H];
r=0;
if(x==J){for(m=0;
m<z;
++m){t.push(F[D+m])
}t[A+H]+=I;
r=I;
D+=z;
B+=h
}else{if(x>J){if(u&&D>0&&F[D-z]!=null){k=w+(F[D-z+H]-w)*(J-x)/(F[D-z+q]-x);
t.push(J);
t.push(k+I);
for(m=2;
m<z;
++m){t.push(F[D+m])
}r=I
}B+=h
}else{if(E&&u){D+=z;
continue
}for(m=0;
m<z;
++m){t.push(F[D+m])
}if(u&&B>0&&y[B-h]!=null){r=I+(y[B-h+H]-I)*(x-J)/(y[B-h+q]-J)
}t[A+H]+=r;
D+=z
}}E=false;
if(A!=t.length&&o){t[A+2]+=r
}}}}if(n&&A!=t.length&&A>0&&t[A]!=null&&t[A]!=t[A-z]&&t[A+1]!=t[A-z+1]){for(m=0;
m<z;
++m){t[A+z+m]=t[A+m]
}t[A+1]=t[A-z+1]
}}g.points=t
}f.hooks.processDatapoints.push(e)
}b.plot.plugins.push({init:c,options:a,name:"stack",version:"1.2"})
})(jQuery);
var ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR='input[name="abstractGrouping"]';
var OTHER_GROUPING_CLASS_SELECTOR=".otherGroupsBox";
var DISCUSSION_MENU_SELECTOR="#discussionMainMenu";
var DISCUSSION_SELECTOR="#discussion";
var REVIEW_INFO_SELECTOR="#review_info_rating";
var DISCUSSION_TOGGLE_LINK_SELECTOR="#toggleDiscussion";
var REVIEW_OWN_ID="ownReview";
var REVIEW_OWN_SELECTOR="#"+REVIEW_OWN_ID;
var REVIEW_UPDATE_FORM_SELECTOR="form.editreview";
var REPLY_FORM_ID="replyForm";
var REPLY_FORM_SELECTOR="#"+REPLY_FORM_ID;
var EDIT_COMMENT_FORM_ID="editcomment";
var EDIT_FORM_SELECTOR="#"+EDIT_COMMENT_FORM_ID;
var CREATE_REVIEW_LINKS_SELECTOR="a.createReview";
var REVIEW_CREATE_FORM_SELECTOR="form.createreview";
var ANONYMOUS_CLASS="anonymous";
var ANONYMOUS_SELECTOR="input[name=discussionItem\\.anonymous]";
var BOOKMARK_LIST_SELECTOR="#bookmarkList";
var PUBLICATION_LIST_SELECTOR="#bibtexList";
var GROUPS_CLASS="groups";
var PUBLIC_GROUPING="public";
var PRIVATE_GROUPING="private";
var OTHER_GROUPING="other";
var FRIENDS_GROUP_NAME="friends";
var DISCUSSIONITEM_DATA_KEY="discussionItemHash";
$(function(){if($(REVIEW_OWN_SELECTOR).length>0){removeReviewActions()
}$(DISCUSSION_TOGGLE_LINK_SELECTOR).click(function(){$(REVIEW_INFO_SELECTOR).toggle("slow");
$(DISCUSSION_SELECTOR).toggle("slow",updateDiscussionToggleLink);
return false
});
$(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
$.each($(".abstractGroupingGroup"),function(a,b){toggleGroupBox(b)
})
});
function updateDiscussionToggleLink(){var b=$(DISCUSSION_SELECTOR).is(":visible");
var a=getString("post.resource.discussion.actions.show");
if(b){a=getString("post.resource.discussion.actions.hide")
}$(DISCUSSION_TOGGLE_LINK_SELECTOR).text(a)
}function showDiscussion(){$(DISCUSSION_SELECTOR).show();
$(REVIEW_INFO_SELECTOR).show();
updateDiscussionToggleLink()
}function removeAllOtherDiscussionForms(){$(EDIT_FORM_SELECTOR).remove();
$(REVIEW_UPDATE_FORM_SELECTOR).hide();
$(REVIEW_CREATE_FORM_SELECTOR).parent().hide();
$(REPLY_FORM_SELECTOR).remove()
}function showReviewForm(){$(REVIEW_CREATE_FORM_SELECTOR).parent().show()
}function removeReviewActions(){$(CREATE_REVIEW_LINKS_SELECTOR).parent().hide();
$(REVIEW_CREATE_FORM_SELECTOR).parent().remove()
}function addReviewActions(){$(CREATE_REVIEW_LINKS_SELECTOR).parent().show()
}function onAbstractGroupingClick(){toggleGroupBox($(this).parent())
}function toggleGroupBox(c){var a=$(c).children("input:checked");
var b=$(c).siblings(OTHER_GROUPING_CLASS_SELECTOR);
if(!a.hasClass("otherGroups")){b.attr("disabled","disabled")
}else{b.removeAttr("disabled")
}}function populateFormWithGroups(b,c,a){b.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).removeAttr("checked");
b.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+'[value="'+c+'"]').attr("checked","checked");
var d=b.find(OTHER_GROUPING_CLASS_SELECTOR);
if(a.length>0){d.removeAttr("disabled");
d.find("input").removeAttr("selected");
$.each(a,function(e,f){d.find('[value="'+f+'"]').attr("selected","selected")
})
}else{d.attr("disabled","disabled")
}}function getGroups(d){var e=d.find(".groups:first");
var b=e.text();
var a=new Array();
if(b!=""){var c=e.find("a");
if((c.length==0)&&(b.indexOf(getString("post.groups.private"))!=-1)){return a
}if(b.indexOf(getString("post.groups.friends"))!=-1){a.push(FRIENDS_GROUP_NAME)
}$.each(c,function(f,g){a.push($(g).text())
});
return a
}return a
}function getAbstractGrouping(c){var d=c.find(".groups:first");
var a=d.text();
if(a!=""){var b=d.find("a");
if(b.length==0){if(a.indexOf(getString("post.groups.private"))!=-1){return PRIVATE_GROUPING
}return OTHER_GROUPING
}return OTHER_GROUPING
}return PUBLIC_GROUPING
}function buildGroupView(c,a){if(c!=="public"){var b=$("<span></span>").addClass(GROUPS_CLASS);
b.append(getString("post.resource.comment.groups")+" ");
if(c=="private"){b.append(getString("post.groups.private"))
}else{var d=a.length;
$.each(a,function(f,g){if(g==FRIENDS_GROUP_NAME){b.append(getString("post.groups.friends"));
return
}var e=$("<a></a>").attr("href","/group/"+g);
e.html(g);
b.append(e);
if(d!=1&&(d-1)==f){b.append(", ")
}})
}return b
}return""
}function deleteDiscussionItemView(a,c){if(a.find("ul.subdiscussionItems:first li").length>0){a.removeAttr("id");
a.removeClass();
a.addClass("discussionitem");
a.find("img:first").remove();
a.find(".details:first").remove();
a.find(".createReview:first").parent().remove();
a.find(".deleteInfo:first").parent().remove();
a.find("a.editLink:first").parent().remove();
a.find("a.reply:first").parent().remove();
var b=$('<div class="deletedInfo"></div').text(getString("post.resource.discussion.info"));
a.prepend(b);
highlight(b);
if(c!=undefined){c()
}}else{a.fadeOut(1000,function(){$(this).remove();
if(c!=undefined){c()
}})
}}function updateHash(a,b){$(a).find("div.info:first").data(DISCUSSIONITEM_DATA_KEY,b);
$(a).find('input[name="discussionItem\\.hash"]:first').attr("value",b)
}function getInterHash(){return $(DISCUSSION_SELECTOR).data("interHash")
}function getHash(a){return $(a).parent().parent().siblings(".details").find(".info").data(DISCUSSIONITEM_DATA_KEY)
}function highlight(a){$(a).css("background-color","#fff735").animate({backgroundColor:"#ffffff"},1000)
}function scrollTo(b){var a=$("#"+b);
if(a.length){$("html,body").animate({scrollTop:a.offset().top-100},"slow")
}};
var REVIEWS_URL="/ajax/reviews";
var STAR_WIDTH=16;
var RATING_STEPS=11;
var STEP_RATING=2;
var RATING_AVG_DIV_SELECTOR="#ratingAvg";
var RATING_AVG_SELECTOR=RATING_AVG_DIV_SELECTOR+" span[property=v\\:average]";
var REVIEW_EDIT_LINK_SELECTOR="a.reviewEditLink";
var REVIEW_DELETE_LINK_SELECTOR="a.reviewDeleteLink";
var REVIEW_TEXTAREA_SELECTOR='textarea[name="discussionItem\\.text"]';
var REVIEW_ANONYMOUS_SELECTOR='input[name="discussionItem\\.anonymous"]';
var REVIEW_RATING_SELECTOR=".reviewrating";
$(function(){plotRatingDistribution();
initStars();
$(REVIEW_UPDATE_FORM_SELECTOR).hide();
if($("#noReviewInfo").length>0){$(RATING_AVG_DIV_SELECTOR).hide();
$("#ratingDistribution").hide()
}$(CREATE_REVIEW_LINKS_SELECTOR).click(showReviewForm);
$(REVIEW_CREATE_FORM_SELECTOR).submit(createReview);
$(REVIEW_UPDATE_FORM_SELECTOR).submit(updateReview);
$(REVIEW_OWN_SELECTOR).find(REVIEW_DELETE_LINK_SELECTOR).click(deleteReview);
$(REVIEW_OWN_SELECTOR).find(REVIEW_EDIT_LINK_SELECTOR).click(showUpdateReviewForm)
});
function showUpdateReviewForm(){removeAllOtherDiscussionForms();
$(REVIEW_UPDATE_FORM_SELECTOR).toggle("slow")
}function initStars(){$(".reviewrating").stars({split:STEP_RATING})
}function getReviewCount(){return parseInt($("#review_info_rating span[property=v\\:count]").text())
}function getAvg(){return Number($(RATING_AVG_SELECTOR).text().replace(",","."))
}function setAvg(b){b=b.toFixed(2);
$(RATING_AVG_SELECTOR).text(b);
var a=getStarsWidth(b);
$("#review_info_rating .stars-on-1").css("width",a);
$(PUBLICATION_LIST_SELECTOR+" .stars-on-0.75").css("width",a);
$(BOOKMARK_LIST_SELECTOR+" .stars-on-0.75").css("width",a)
}function showReviewForm(){removeAllOtherDiscussionForms();
showDiscussion();
$(REVIEW_CREATE_FORM_SELECTOR).parent().show();
return true
}function setReviewCount(a){var b=getString("post.resource.review.review");
if(a>1){b=getString("post.resource.review.reviews")
}$("#review_info_rating span[property=v\\:count]").text(a);
$("#review_info_rating span[property=v\\:count]").next("span").text(b)
}function getOwnReviewRating(){return Number($("#ownReview .rating").data("rating"))
}function validateRating(a){var b=getRating(a);
if(b==0){if(!confirm(getString("post.resource.review.rating0"))){return false
}}return true
}function plotRatingDistribution(){if($("#ratingDistributionGraph").length==0){return
}var c=[];
var g=[];
var f=[];
$(".subdiscussionItems li").not("#newReview").find(".rating").each(function(){var h=$(this).data("rating");
if(c[h]){c[h]+=1
}else{c[h]=1
}});
for(var b=0;
b<RATING_STEPS;
b++){var a=b/STEP_RATING;
var e=0;
if(c[a]){e=c[a]
}var d=getReviewCount();
if(e>0){e=e/d*100
}else{e=Number.NaN
}f.push([a,e]);
g.push(a)
}$.plot($("#ratingDistributionGraph"),[f],{bars:{show:true,align:"center",barWidth:0.2,fill:0.7,},xaxis:{ticks:g,tickDecimals:1,tickColor:"transparent",autoscaleMargin:0.02,},yaxis:{show:false,min:0,max:110,},grid:{markings:[{xaxis:{from:getAvg(),to:getAvg()},yaxis:{from:0,to:110},color:"#bb0000"}]}})
}function createReview(){var f=$(this);
f.unbind("submit");
var a=f.find(REVIEW_RATING_SELECTOR);
if(!validateRating(a)){f.submit(createReview);
return false
}var i=f.find(".spinner");
i.show("slow");
var d=f.find(REVIEW_TEXTAREA_SELECTOR).val();
var g=f.find(REVIEW_ANONYMOUS_SELECTOR).is(":checked");
var e=getRating(a);
var c=f.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var b=f.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(b==null){b=new Array()
}else{b=new Array(b)
}var h=f.serialize();
$.ajax({url:REVIEWS_URL,type:"POST",dataType:"json",data:h,success:function(n){var p=$("#newReview").remove();
p.attr("id","ownReview");
updateReviewView(p,d,e,c,b);
$(DISCUSSION_SELECTOR+" .subdiscussionItems:first").prepend(p);
updateHash(p,n.hash);
highlight(p);
var m=$(REVIEW_UPDATE_FORM_SELECTOR);
m.find(REVIEW_TEXTAREA_SELECTOR).text(d);
m.find(REVIEW_RATING_SELECTOR).stars({split:STEP_RATING,});
m.find(REVIEW_RATING_SELECTOR).stars("select",e.toFixed(1));
if(g){p.addClass(ANONYMOUS_CLASS);
m.find(REVIEW_ANONYMOUS_SELECTOR).attr("checked","checked")
}m.submit(updateReview);
populateFormWithGroups(m,c,b);
m.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
removeReviewActions();
p.find(REVIEW_EDIT_LINK_SELECTOR).click(showUpdateReviewForm);
p.find(REVIEW_DELETE_LINK_SELECTOR).click(deleteReview);
p.find(REPLY_SELECTOR).click(reply);
var l=getReviewCount();
var j=getAvg();
var o=l+1;
var k=(j*l+e)/o;
setReviewCount(o);
setAvg(k);
plotRatingDistribution();
$("#noReviewInfo").hide();
$("#ratingAvg").show("slow");
$("#ratingDistribution").show("slow");
scrollTo(REVIEW_OWN_ID)
},error:function(j,k,l){handleAjaxErrors(f,jQuery.parseJSON(j.responseText));
f.submit(updateReview)
},});
return false
}function updateReview(){var f=$(this);
f.unbind("submit");
var a=f.find(REVIEW_RATING_SELECTOR);
if(!validateRating(a)){f.submit(updateReview);
return false
}var c=f.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var b=f.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(b==null){b=new Array()
}else{b=new Array(b)
}var j=f.find(".spinner");
j.show("slow");
var d=f.find(REVIEW_TEXTAREA_SELECTOR).val();
var g=f.find(REVIEW_ANONYMOUS_SELECTOR).is(":checked");
var e=getRating(a);
var i=getOwnReviewRating();
var h=f.serialize();
$.ajax({url:REVIEWS_URL,type:"POST",dataType:"json",data:h,success:function(m){f.hide("slow");
j.hide("slow");
var o=$(REVIEW_OWN_SELECTOR);
updateHash(o,m.hash);
if(g){o.addClass(ANONYMOUS_CLASS)
}else{o.removeClass(ANONYMOUS_CLASS)
}updateReviewView(o,d,e,c,b);
highlight(o);
var n=getReviewCount();
var k=getAvg();
var l=(k*n-i+e)/n;
setAvg(l);
plotRatingDistribution();
f.submit(updateReview)
},error:function(k,l,m){handleAjaxErrors(f,jQuery.parseJSON(k.responseText));
f.submit(updateReview)
},});
return false
}function deleteReview(){var d=$(this);
$(this).siblings(".deleteInfo").show();
var f=getInterHash();
var a=$(REVIEW_OWN_SELECTOR);
var e=a.find(".info").data(DISCUSSIONITEM_DATA_KEY);
var c=getOwnReviewRating();
d.remove();
var b=REVIEWS_URL+"?hash="+f+"&ckey="+ckey+"&discussionItem.hash="+e;
$.ajax({url:b,type:"DELETE",success:function(g){deleteDiscussionItemView(a,function(){var j=getReviewCount();
var h=getAvg();
var i=0;
var k=j-1;
if(k>0){i=(h*j-c)/k
}setReviewCount(j-1);
setAvg(i);
plotRatingDistribution();
addReviewActions()
})
},});
return false
}function updateReviewView(h,g,c,f,a){var e=getStarsWidth(c);
var d=h.find(".rating");
d.data("rating",c);
h.find(".review.text").text(g);
d.find(".stars-on-1").css("width",e);
h.find("."+GROUPS_CLASS).remove();
var b=buildGroupView(f,a);
h.find(".info").append(b)
}function getRating(b){var a=$(b).data("stars");
return parseFloat(a.options.value)
}function getStarsWidth(a){return STAR_WIDTH*a
};
var COMMENTS_URL="/ajax/comments";
var REPLY_SELECTOR="a.reply";
var EDIT_COMMENT_LINKS_SELECTOR="a.commentEditLink";
var DELETE_COMMENT_LINKS_SELECTOR="a.commentDeleteLink";
var TOGGLE_REPLY_SELECTOR="a.toggleReplies";
$(function(){$(REPLY_SELECTOR).click(reply);
$(TOGGLE_REPLY_SELECTOR).click(function(){var a=$(this).parent().parent().siblings("ul.subdiscussionItems");
var d=a.is(":visible");
a.toggle("slow");
var c=getString("post.resource.discussion.replies.show");
var b=getString("post.resource.discussion.replies.show.title");
if(!d){c=getString("post.resource.discussion.replies.hide");
b=getString("post.resource.discussion.replies.hide.title")
}$(this).text(c).attr("title",b);
return false
});
$(EDIT_COMMENT_LINKS_SELECTOR).click(showEditCommentForm);
$(DELETE_COMMENT_LINKS_SELECTOR).click(deleteComment)
});
function reply(){showDiscussion();
var b=$(this).parent().parent().parent();
removeAllOtherDiscussionForms();
var a=getHash($(this));
var d=$("#createComment").clone();
d.attr("id",REPLY_FORM_ID);
var c=d.find("form");
c.append($("<input />").attr("name","discussionItem.parentHash").attr("type","hidden").attr("value",a));
c.submit(createComment);
c.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
if(a!=undefined){b.append(d)
}else{$(DISCUSSION_SELECTOR).prepend(d)
}d.show();
c.find("textarea").TextAreaResizer();
scrollTo(REPLY_FORM_ID);
return false
}function showEditCommentForm(){var h=$(this).parent().parent().parent();
removeAllOtherDiscussionForms();
var g=$("#createComment").clone();
g.attr("id",EDIT_COMMENT_FORM_ID);
var d=g.find("form");
populateFormWithGroups(d,getAbstractGrouping(h),getGroups(h));
var c=h.find(".text:first").text();
d.find("textarea").attr("value",c);
if(h.hasClass(ANONYMOUS_CLASS)){d.find(ANONYMOUS_SELECTOR).attr("checked","checked")
}var b=getHash($(this));
d.append($("<input />").attr("name","discussionItem.hash").attr("type","hidden").attr("value",b));
d.append($("<input />").attr("name","_method").attr("type","hidden").attr("value","PUT"));
var a=getString("post.resource.comment.actions.edit");
g.find("h4").text(a);
d.find('input[type="submit"]').attr("value",a);
var f=d.find(".spinner");
var e=f.find("img");
f.empty().append(e).append(getString("post.resource.comment.action.update"));
d.find("textarea").TextAreaResizer();
d.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
d.submit(updateComment);
h.append(g);
g.show("slow");
scrollTo(EDIT_COMMENT_FORM_ID);
return false
}function createComment(){var c=$(this);
c.unbind("submit");
var a=c.parent("#"+REPLY_FORM_ID);
var g=c.find(".spinner");
g.show();
var d=c.serialize();
var b=c.find("textarea");
var h=b.val();
var f=c.find(ANONYMOUS_SELECTOR).is(":checked");
var i=c.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var e=c.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(e==null){e=new Array()
}else{e=new Array(e)
}$.ajax({url:COMMENTS_URL,type:"POST",data:d,dataType:"json",success:function(k){var l=$("#commentTemplate").clone();
l.removeAttr("id");
updateCommentView(l,k.hash,h,f,i,e);
var j=$('<li class="comment"></li>');
j.append(l);
l.find("a.reply").click(reply);
l.find("a.editLink").click(showEditCommentForm);
if($(DISCUSSION_SELECTOR).children(REPLY_FORM_SELECTOR).length>0){$(DISCUSSION_SELECTOR+" ul.subdiscussionItems:first").prepend(j)
}else{a.parent().children(".subdiscussionItems").append(j)
}l.show();
a.remove();
g.hide();
c.submit(createComment);
showReviewForm();
highlight(j)
},error:function(j,k,l){handleAjaxErrors(c,jQuery.parseJSON(j.responseText));
c.submit(createComment)
}});
return false
}function updateCommentView(g,b,c,d,a,f){g.find(".text:first").text(c);
updateHash(g,b);
g.find("."+GROUPS_CLASS).remove();
var e=buildGroupView(a,f);
g.find(".info:first").append(e);
if(d){g.addClass(ANONYMOUS_CLASS)
}else{g.removeClass(ANONYMOUS_CLASS)
}}function updateComment(){var b=$(this);
var e=b.parent().parent();
b.unbind("submit");
var g=b.find(".spinner");
g.show();
var c=b.serialize();
var a=b.find("textarea");
var h=a.val();
var f=b.find(ANONYMOUS_SELECTOR).is(":checked");
var i=b.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var d=b.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(d==null){d=new Array()
}else{d=new Array(d)
}$.ajax({url:COMMENTS_URL,type:"POST",dataType:"json",data:c,success:function(j){updateCommentView(e,j.hash,h,f,i,d);
b.parent().remove();
highlight(e);
showReviewForm()
},error:function(j,k,l){handleAjaxErrors(b,jQuery.parseJSON(j.responseText));
b.submit(updateComment)
},});
return false
}function deleteComment(){var b=$(this);
b.siblings(".deleteInfo").show();
var d=getHash($(this));
var a=getInterHash();
var c=COMMENTS_URL+"?ckey="+ckey+"&hash="+a+"&discussionItem.hash="+d;
var e=b.parent().parent().parent();
b.remove();
$.ajax({url:c,type:"DELETE",success:function(){deleteDiscussionItemView(e)
},})
};
