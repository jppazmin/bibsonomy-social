String.prototype.parseColor=function(){var a="#";
if(this.slice(0,4)=="rgb("){var c=this.slice(4,this.length-1).split(",");
var b=0;
do{a+=parseInt(c[b]).toColorPart()
}while(++b<3)
}else{if(this.slice(0,1)=="#"){if(this.length==4){for(var b=1;
b<4;
b++){a+=(this.charAt(b)+this.charAt(b)).toLowerCase()
}}if(this.length==7){a=this.toLowerCase()
}}}return(a.length==7?a:(arguments[0]||this))
};
Element.collectTextNodes=function(a){return $A($(a).childNodes).collect(function(b){return(b.nodeType==3?b.nodeValue:(b.hasChildNodes()?Element.collectTextNodes(b):""))
}).flatten().join("")
};
Element.collectTextNodesIgnoreClass=function(a,b){return $A($(a).childNodes).collect(function(c){return(c.nodeType==3?c.nodeValue:((c.hasChildNodes()&&!Element.hasClassName(c,b))?Element.collectTextNodesIgnoreClass(c,b):""))
}).flatten().join("")
};
Element.setContentZoom=function(a,b){a=$(a);
a.setStyle({fontSize:(b/100)+"em"});
if(navigator.appVersion.indexOf("AppleWebKit")>0){window.scrollBy(0,0)
}return a
};
Element.getOpacity=function(a){return $(a).getStyle("opacity")
};
Element.setOpacity=function(a,b){return $(a).setStyle({opacity:b})
};
Element.getInlineOpacity=function(a){return $(a).style.opacity||""
};
Element.forceRerendering=function(a){try{a=$(a);
var c=document.createTextNode(" ");
a.appendChild(c);
a.removeChild(c)
}catch(b){}};
Array.prototype.call=function(){var a=arguments;
this.each(function(b){b.apply(this,a)
})
};
var Effect={_elementDoesNotExistError:{name:"ElementDoesNotExistError",message:"The specified DOM element does not exist, but is required for this effect to operate"},tagifyText:function(a){if(typeof Builder=="undefined"){throw ("Effect.tagifyText requires including script.aculo.us' builder.js library")
}var b="position:relative";
if(/MSIE/.test(navigator.userAgent)&&!window.opera){b+=";zoom:1"
}a=$(a);
$A(a.childNodes).each(function(c){if(c.nodeType==3){c.nodeValue.toArray().each(function(d){a.insertBefore(Builder.node("span",{style:b},d==" "?String.fromCharCode(160):d),c)
});
Element.remove(c)
}})
},multiple:function(b,c){var e;
if(((typeof b=="object")||(typeof b=="function"))&&(b.length)){e=b
}else{e=$(b).childNodes
}var a=Object.extend({speed:0.1,delay:0},arguments[2]||{});
var d=a.delay;
$A(e).each(function(g,f){new c(g,Object.extend(a,{delay:f*a.speed+d}))
})
},PAIRS:{"slide":["SlideDown","SlideUp"],"blind":["BlindDown","BlindUp"],"appear":["Appear","Fade"]},toggle:function(b,c){b=$(b);
c=(c||"appear").toLowerCase();
var a=Object.extend({queue:{position:"end",scope:(b.id||"global"),limit:1}},arguments[2]||{});
Effect[b.visible()?Effect.PAIRS[c][1]:Effect.PAIRS[c][0]](b,a)
}};
var Effect2=Effect;
Effect.Transitions={linear:Prototype.K,sinoidal:function(a){return(-Math.cos(a*Math.PI)/2)+0.5
},reverse:function(a){return 1-a
},flicker:function(a){return((-Math.cos(a*Math.PI)/4)+0.75)+Math.random()/4
},wobble:function(a){return(-Math.cos(a*Math.PI*(9*a))/2)+0.5
},pulse:function(b,a){a=a||5;
return(Math.round((b%(1/a))*a)==0?((b*a*2)-Math.floor(b*a*2)):1-((b*a*2)-Math.floor(b*a*2)))
},none:function(a){return 0
},full:function(a){return 1
}};
Effect.ScopedQueue=Class.create();
Object.extend(Object.extend(Effect.ScopedQueue.prototype,Enumerable),{initialize:function(){this.effects=[];
this.interval=null
},_each:function(a){this.effects._each(a)
},add:function(b){var c=new Date().getTime();
var a=(typeof b.options.queue=="string")?b.options.queue:b.options.queue.position;
switch(a){case"front":this.effects.findAll(function(d){return d.state=="idle"
}).each(function(d){d.startOn+=b.finishOn;
d.finishOn+=b.finishOn
});
break;
case"with-last":c=this.effects.pluck("startOn").max()||c;
break;
case"end":c=this.effects.pluck("finishOn").max()||c;
break
}b.startOn+=c;
b.finishOn+=c;
if(!b.options.queue.limit||(this.effects.length<b.options.queue.limit)){this.effects.push(b)
}if(!this.interval){this.interval=setInterval(this.loop.bind(this),15)
}},remove:function(a){this.effects=this.effects.reject(function(b){return b==a
});
if(this.effects.length==0){clearInterval(this.interval);
this.interval=null
}},loop:function(){var a=new Date().getTime();
this.effects.invoke("loop",a)
}});
Effect.Queues={instances:$H(),get:function(a){if(typeof a!="string"){return a
}if(!this.instances[a]){this.instances[a]=new Effect.ScopedQueue()
}return this.instances[a]
}};
Effect.Queue=Effect.Queues.get("global");
Effect.DefaultOptions={transition:Effect.Transitions.sinoidal,duration:1,fps:60,sync:false,from:0,to:1,delay:0,queue:"parallel"};
Effect.Base=function(){};
Effect.Base.prototype={position:null,start:function(a){this.options=Object.extend(Object.extend({},Effect.DefaultOptions),a||{});
this.currentFrame=0;
this.state="idle";
this.startOn=this.options.delay*1000;
this.finishOn=this.startOn+(this.options.duration*1000);
this.event("beforeStart");
if(!this.options.sync){Effect.Queues.get(typeof this.options.queue=="string"?"global":this.options.queue.scope).add(this)
}},loop:function(c){if(c>=this.startOn){if(c>=this.finishOn){this.render(1);
this.cancel();
this.event("beforeFinish");
if(this.finish){this.finish()
}this.event("afterFinish");
return
}var b=(c-this.startOn)/(this.finishOn-this.startOn);
var a=Math.round(b*this.options.fps*this.options.duration);
if(a>this.currentFrame){this.render(b);
this.currentFrame=a
}}},render:function(a){if(this.state=="idle"){this.state="running";
this.event("beforeSetup");
if(this.setup){this.setup()
}this.event("afterSetup")
}if(this.state=="running"){if(this.options.transition){a=this.options.transition(a)
}a*=(this.options.to-this.options.from);
a+=this.options.from;
this.position=a;
this.event("beforeUpdate");
if(this.update){this.update(a)
}this.event("afterUpdate")
}},cancel:function(){if(!this.options.sync){Effect.Queues.get(typeof this.options.queue=="string"?"global":this.options.queue.scope).remove(this)
}this.state="finished"
},event:function(a){if(this.options[a+"Internal"]){this.options[a+"Internal"](this)
}if(this.options[a]){this.options[a](this)
}},inspect:function(){return"#<Effect:"+$H(this).inspect()+",options:"+$H(this.options).inspect()+">"
}};
Effect.Parallel=Class.create();
Object.extend(Object.extend(Effect.Parallel.prototype,Effect.Base.prototype),{initialize:function(a){this.effects=a||[];
this.start(arguments[1])
},update:function(a){this.effects.invoke("render",a)
},finish:function(a){this.effects.each(function(b){b.render(1);
b.cancel();
b.event("beforeFinish");
if(b.finish){b.finish(a)
}b.event("afterFinish")
})
}});
Effect.Event=Class.create();
Object.extend(Object.extend(Effect.Event.prototype,Effect.Base.prototype),{initialize:function(){var a=Object.extend({duration:0},arguments[0]||{});
this.start(a)
},update:Prototype.emptyFunction});
Effect.Opacity=Class.create();
Object.extend(Object.extend(Effect.Opacity.prototype,Effect.Base.prototype),{initialize:function(b){this.element=$(b);
if(!this.element){throw (Effect._elementDoesNotExistError)
}if(/MSIE/.test(navigator.userAgent)&&!window.opera&&(!this.element.currentStyle.hasLayout)){this.element.setStyle({zoom:1})
}var a=Object.extend({from:this.element.getOpacity()||0,to:1},arguments[1]||{});
this.start(a)
},update:function(a){this.element.setOpacity(a)
}});
Effect.Move=Class.create();
Object.extend(Object.extend(Effect.Move.prototype,Effect.Base.prototype),{initialize:function(b){this.element=$(b);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var a=Object.extend({x:0,y:0,mode:"relative"},arguments[1]||{});
this.start(a)
},setup:function(){this.element.makePositioned();
this.originalLeft=parseFloat(this.element.getStyle("left")||"0");
this.originalTop=parseFloat(this.element.getStyle("top")||"0");
if(this.options.mode=="absolute"){this.options.x=this.options.x-this.originalLeft;
this.options.y=this.options.y-this.originalTop
}},update:function(a){this.element.setStyle({left:Math.round(this.options.x*a+this.originalLeft)+"px",top:Math.round(this.options.y*a+this.originalTop)+"px"})
}});
Effect.MoveBy=function(b,a,c){return new Effect.Move(b,Object.extend({x:c,y:a},arguments[3]||{}))
};
Effect.Scale=Class.create();
Object.extend(Object.extend(Effect.Scale.prototype,Effect.Base.prototype),{initialize:function(b,c){this.element=$(b);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var a=Object.extend({scaleX:true,scaleY:true,scaleContent:true,scaleFromCenter:false,scaleMode:"box",scaleFrom:100,scaleTo:c},arguments[2]||{});
this.start(a)
},setup:function(){this.restoreAfterFinish=this.options.restoreAfterFinish||false;
this.elementPositioning=this.element.getStyle("position");
this.originalStyle={};
["top","left","width","height","fontSize"].each(function(b){this.originalStyle[b]=this.element.style[b]
}.bind(this));
this.originalTop=this.element.offsetTop;
this.originalLeft=this.element.offsetLeft;
var a=this.element.getStyle("font-size")||"100%";
["em","px","%","pt"].each(function(b){if(a.indexOf(b)>0){this.fontSize=parseFloat(a);
this.fontSizeType=b
}}.bind(this));
this.factor=(this.options.scaleTo-this.options.scaleFrom)/100;
this.dims=null;
if(this.options.scaleMode=="box"){this.dims=[this.element.offsetHeight,this.element.offsetWidth]
}if(/^content/.test(this.options.scaleMode)){this.dims=[this.element.scrollHeight,this.element.scrollWidth]
}if(!this.dims){this.dims=[this.options.scaleMode.originalHeight,this.options.scaleMode.originalWidth]
}},update:function(a){var b=(this.options.scaleFrom/100)+(this.factor*a);
if(this.options.scaleContent&&this.fontSize){this.element.setStyle({fontSize:this.fontSize*b+this.fontSizeType})
}this.setDimensions(this.dims[0]*b,this.dims[1]*b)
},finish:function(a){if(this.restoreAfterFinish){this.element.setStyle(this.originalStyle)
}},setDimensions:function(a,e){var f={};
if(this.options.scaleX){f.width=Math.round(e)+"px"
}if(this.options.scaleY){f.height=Math.round(a)+"px"
}if(this.options.scaleFromCenter){var c=(a-this.dims[0])/2;
var b=(e-this.dims[1])/2;
if(this.elementPositioning=="absolute"){if(this.options.scaleY){f.top=this.originalTop-c+"px"
}if(this.options.scaleX){f.left=this.originalLeft-b+"px"
}}else{if(this.options.scaleY){f.top=-c+"px"
}if(this.options.scaleX){f.left=-b+"px"
}}}this.element.setStyle(f)
}});
Effect.Highlight=Class.create();
Object.extend(Object.extend(Effect.Highlight.prototype,Effect.Base.prototype),{initialize:function(b){this.element=$(b);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var a=Object.extend({startcolor:"#ffff99"},arguments[1]||{});
this.start(a)
},setup:function(){if(this.element.getStyle("display")=="none"){this.cancel();
return
}this.oldStyle={backgroundImage:this.element.getStyle("background-image")};
this.element.setStyle({backgroundImage:"none"});
if(!this.options.endcolor){this.options.endcolor=this.element.getStyle("background-color").parseColor("#ffffff")
}if(!this.options.restorecolor){this.options.restorecolor=this.element.getStyle("background-color")
}this._base=$R(0,2).map(function(a){return parseInt(this.options.startcolor.slice(a*2+1,a*2+3),16)
}.bind(this));
this._delta=$R(0,2).map(function(a){return parseInt(this.options.endcolor.slice(a*2+1,a*2+3),16)-this._base[a]
}.bind(this))
},update:function(a){this.element.setStyle({backgroundColor:$R(0,2).inject("#",function(b,c,d){return b+(Math.round(this._base[d]+(this._delta[d]*a)).toColorPart())
}.bind(this))})
},finish:function(){this.element.setStyle(Object.extend(this.oldStyle,{backgroundColor:this.options.restorecolor}))
}});
Effect.ScrollTo=Class.create();
Object.extend(Object.extend(Effect.ScrollTo.prototype,Effect.Base.prototype),{initialize:function(a){this.element=$(a);
this.start(arguments[1]||{})
},setup:function(){Position.prepare();
var b=Position.cumulativeOffset(this.element);
if(this.options.offset){b[1]+=this.options.offset
}var a=window.innerHeight?window.height-window.innerHeight:document.body.scrollHeight-(document.documentElement.clientHeight?document.documentElement.clientHeight:document.body.clientHeight);
this.scrollStart=Position.deltaY;
this.delta=(b[1]>a?a:b[1])-this.scrollStart
},update:function(a){Position.prepare();
window.scrollTo(Position.deltaX,this.scrollStart+(a*this.delta))
}});
Effect.Fade=function(c){c=$(c);
var a=c.getInlineOpacity();
var b=Object.extend({from:c.getOpacity()||1,to:0,afterFinishInternal:function(d){if(d.options.to!=0){return
}d.element.hide().setStyle({opacity:a})
}},arguments[1]||{});
return new Effect.Opacity(c,b)
};
Effect.Appear=function(b){b=$(b);
var a=Object.extend({from:(b.getStyle("display")=="none"?0:b.getOpacity()||0),to:1,afterFinishInternal:function(c){c.element.forceRerendering()
},beforeSetup:function(c){c.element.setOpacity(c.options.from).show()
}},arguments[1]||{});
return new Effect.Opacity(b,a)
};
Effect.Puff=function(b){b=$(b);
var a={opacity:b.getInlineOpacity(),position:b.getStyle("position"),top:b.style.top,left:b.style.left,width:b.style.width,height:b.style.height};
return new Effect.Parallel([new Effect.Scale(b,200,{sync:true,scaleFromCenter:true,scaleContent:true,restoreAfterFinish:true}),new Effect.Opacity(b,{sync:true,to:0})],Object.extend({duration:1,beforeSetupInternal:function(c){Position.absolutize(c.effects[0].element)
},afterFinishInternal:function(c){c.effects[0].element.hide().setStyle(a)
}},arguments[1]||{}))
};
Effect.BlindUp=function(a){a=$(a);
a.makeClipping();
return new Effect.Scale(a,0,Object.extend({scaleContent:false,scaleX:false,restoreAfterFinish:true,afterFinishInternal:function(b){b.element.hide().undoClipping()
}},arguments[1]||{}))
};
Effect.BlindDown=function(b){b=$(b);
var a=b.getDimensions();
return new Effect.Scale(b,100,Object.extend({scaleContent:false,scaleX:false,scaleFrom:0,scaleMode:{originalHeight:a.height,originalWidth:a.width},restoreAfterFinish:true,afterSetup:function(c){c.element.makeClipping().setStyle({height:"0px"}).show()
},afterFinishInternal:function(c){c.element.undoClipping()
}},arguments[1]||{}))
};
Effect.SwitchOff=function(b){b=$(b);
var a=b.getInlineOpacity();
return new Effect.Appear(b,Object.extend({duration:0.4,from:0,transition:Effect.Transitions.flicker,afterFinishInternal:function(c){new Effect.Scale(c.element,1,{duration:0.3,scaleFromCenter:true,scaleX:false,scaleContent:false,restoreAfterFinish:true,beforeSetup:function(d){d.element.makePositioned().makeClipping()
},afterFinishInternal:function(d){d.element.hide().undoClipping().undoPositioned().setStyle({opacity:a})
}})
}},arguments[1]||{}))
};
Effect.DropOut=function(b){b=$(b);
var a={top:b.getStyle("top"),left:b.getStyle("left"),opacity:b.getInlineOpacity()};
return new Effect.Parallel([new Effect.Move(b,{x:0,y:100,sync:true}),new Effect.Opacity(b,{sync:true,to:0})],Object.extend({duration:0.5,beforeSetup:function(c){c.effects[0].element.makePositioned()
},afterFinishInternal:function(c){c.effects[0].element.hide().undoPositioned().setStyle(a)
}},arguments[1]||{}))
};
Effect.Shake=function(b){b=$(b);
var a={top:b.getStyle("top"),left:b.getStyle("left")};
return new Effect.Move(b,{x:20,y:0,duration:0.05,afterFinishInternal:function(c){new Effect.Move(c.element,{x:-40,y:0,duration:0.1,afterFinishInternal:function(d){new Effect.Move(d.element,{x:40,y:0,duration:0.1,afterFinishInternal:function(e){new Effect.Move(e.element,{x:-40,y:0,duration:0.1,afterFinishInternal:function(f){new Effect.Move(f.element,{x:40,y:0,duration:0.1,afterFinishInternal:function(g){new Effect.Move(g.element,{x:-20,y:0,duration:0.05,afterFinishInternal:function(h){h.element.undoPositioned().setStyle(a)
}})
}})
}})
}})
}})
}})
};
Effect.SlideDown=function(c){c=$(c).cleanWhitespace();
var a=c.down().getStyle("bottom");
var b=c.getDimensions();
return new Effect.Scale(c,100,Object.extend({scaleContent:false,scaleX:false,scaleFrom:window.opera?0:1,scaleMode:{originalHeight:b.height,originalWidth:b.width},restoreAfterFinish:true,afterSetup:function(d){d.element.makePositioned();
d.element.down().makePositioned();
if(window.opera){d.element.setStyle({top:""})
}d.element.makeClipping().setStyle({height:"0px"}).show()
},afterUpdateInternal:function(d){d.element.down().setStyle({bottom:(d.dims[0]-d.element.clientHeight)+"px"})
},afterFinishInternal:function(d){d.element.undoClipping().undoPositioned();
d.element.down().undoPositioned().setStyle({bottom:a})
}},arguments[1]||{}))
};
Effect.SlideUp=function(b){b=$(b).cleanWhitespace();
var a=b.down().getStyle("bottom");
return new Effect.Scale(b,window.opera?0:1,Object.extend({scaleContent:false,scaleX:false,scaleMode:"box",scaleFrom:100,restoreAfterFinish:true,beforeStartInternal:function(c){c.element.makePositioned();
c.element.down().makePositioned();
if(window.opera){c.element.setStyle({top:""})
}c.element.makeClipping().show()
},afterUpdateInternal:function(c){c.element.down().setStyle({bottom:(c.dims[0]-c.element.clientHeight)+"px"})
},afterFinishInternal:function(c){c.element.hide().undoClipping().undoPositioned().setStyle({bottom:a});
c.element.down().undoPositioned()
}},arguments[1]||{}))
};
Effect.Squish=function(a){return new Effect.Scale(a,window.opera?1:0,{restoreAfterFinish:true,beforeSetup:function(b){b.element.makeClipping()
},afterFinishInternal:function(b){b.element.hide().undoClipping()
}})
};
Effect.Grow=function(c){c=$(c);
var b=Object.extend({direction:"center",moveTransition:Effect.Transitions.sinoidal,scaleTransition:Effect.Transitions.sinoidal,opacityTransition:Effect.Transitions.full},arguments[1]||{});
var a={top:c.style.top,left:c.style.left,height:c.style.height,width:c.style.width,opacity:c.getInlineOpacity()};
var g=c.getDimensions();
var h,f;
var e,d;
switch(b.direction){case"top-left":h=f=e=d=0;
break;
case"top-right":h=g.width;
f=d=0;
e=-g.width;
break;
case"bottom-left":h=e=0;
f=g.height;
d=-g.height;
break;
case"bottom-right":h=g.width;
f=g.height;
e=-g.width;
d=-g.height;
break;
case"center":h=g.width/2;
f=g.height/2;
e=-g.width/2;
d=-g.height/2;
break
}return new Effect.Move(c,{x:h,y:f,duration:0.01,beforeSetup:function(i){i.element.hide().makeClipping().makePositioned()
},afterFinishInternal:function(i){new Effect.Parallel([new Effect.Opacity(i.element,{sync:true,to:1,from:0,transition:b.opacityTransition}),new Effect.Move(i.element,{x:e,y:d,sync:true,transition:b.moveTransition}),new Effect.Scale(i.element,100,{scaleMode:{originalHeight:g.height,originalWidth:g.width},sync:true,scaleFrom:window.opera?1:0,transition:b.scaleTransition,restoreAfterFinish:true})],Object.extend({beforeSetup:function(j){j.effects[0].element.setStyle({height:"0px"}).show()
},afterFinishInternal:function(j){j.effects[0].element.undoClipping().undoPositioned().setStyle(a)
}},b))
}})
};
Effect.Shrink=function(c){c=$(c);
var b=Object.extend({direction:"center",moveTransition:Effect.Transitions.sinoidal,scaleTransition:Effect.Transitions.sinoidal,opacityTransition:Effect.Transitions.none},arguments[1]||{});
var a={top:c.style.top,left:c.style.left,height:c.style.height,width:c.style.width,opacity:c.getInlineOpacity()};
var f=c.getDimensions();
var e,d;
switch(b.direction){case"top-left":e=d=0;
break;
case"top-right":e=f.width;
d=0;
break;
case"bottom-left":e=0;
d=f.height;
break;
case"bottom-right":e=f.width;
d=f.height;
break;
case"center":e=f.width/2;
d=f.height/2;
break
}return new Effect.Parallel([new Effect.Opacity(c,{sync:true,to:0,from:1,transition:b.opacityTransition}),new Effect.Scale(c,window.opera?1:0,{sync:true,transition:b.scaleTransition,restoreAfterFinish:true}),new Effect.Move(c,{x:e,y:d,sync:true,transition:b.moveTransition})],Object.extend({beforeStartInternal:function(g){g.effects[0].element.makePositioned().makeClipping()
},afterFinishInternal:function(g){g.effects[0].element.hide().undoClipping().undoPositioned().setStyle(a)
}},b))
};
Effect.Pulsate=function(c){c=$(c);
var b=arguments[1]||{};
var a=c.getInlineOpacity();
var e=b.transition||Effect.Transitions.sinoidal;
var d=function(f){return e(1-Effect.Transitions.pulse(f,b.pulses))
};
d.bind(e);
return new Effect.Opacity(c,Object.extend(Object.extend({duration:2,from:0,afterFinishInternal:function(f){f.element.setStyle({opacity:a})
}},b),{transition:d}))
};
Effect.Fold=function(b){b=$(b);
var a={top:b.style.top,left:b.style.left,width:b.style.width,height:b.style.height};
b.makeClipping();
return new Effect.Scale(b,5,Object.extend({scaleContent:false,scaleX:false,afterFinishInternal:function(c){new Effect.Scale(b,1,{scaleContent:false,scaleY:false,afterFinishInternal:function(d){d.element.hide().undoClipping().setStyle(a)
}})
}},arguments[1]||{}))
};
Effect.Morph=Class.create();
Object.extend(Object.extend(Effect.Morph.prototype,Effect.Base.prototype),{initialize:function(c){this.element=$(c);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var b=Object.extend({style:{}},arguments[1]||{});
if(typeof b.style=="string"){if(b.style.indexOf(":")==-1){var d="",a="."+b.style;
$A(document.styleSheets).reverse().each(function(e){if(e.cssRules){cssRules=e.cssRules
}else{if(e.rules){cssRules=e.rules
}}$A(cssRules).reverse().each(function(f){if(a==f.selectorText){d=f.style.cssText;
throw $break
}});
if(d){throw $break
}});
this.style=d.parseStyle();
b.afterFinishInternal=function(e){e.element.addClassName(e.options.style);
e.transforms.each(function(f){if(f.style!="opacity"){e.element.style[f.style.camelize()]=""
}})
}
}else{this.style=b.style.parseStyle()
}}else{this.style=$H(b.style)
}this.start(b)
},setup:function(){function a(b){if(!b||["rgba(0, 0, 0, 0)","transparent"].include(b)){b="#ffffff"
}b=b.parseColor();
return $R(0,2).map(function(c){return parseInt(b.slice(c*2+1,c*2+3),16)
})
}this.transforms=this.style.map(function(g){var f=g[0].underscore().dasherize(),e=g[1],d=null;
if(e.parseColor("#zzzzzz")!="#zzzzzz"){e=e.parseColor();
d="color"
}else{if(f=="opacity"){e=parseFloat(e);
if(/MSIE/.test(navigator.userAgent)&&!window.opera&&(!this.element.currentStyle.hasLayout)){this.element.setStyle({zoom:1})
}}else{if(Element.CSS_LENGTH.test(e)){var c=e.match(/^([\+\-]?[0-9\.]+)(.*)$/),e=parseFloat(c[1]),d=(c.length==3)?c[2]:null
}}}var b=this.element.getStyle(f);
return $H({style:f,originalValue:d=="color"?a(b):parseFloat(b||0),targetValue:d=="color"?a(e):e,unit:d})
}.bind(this)).reject(function(b){return((b.originalValue==b.targetValue)||(b.unit!="color"&&(isNaN(b.originalValue)||isNaN(b.targetValue))))
})
},update:function(a){var b=$H(),c=null;
this.transforms.each(function(d){c=d.unit=="color"?$R(0,2).inject("#",function(e,f,g){return e+(Math.round(d.originalValue[g]+(d.targetValue[g]-d.originalValue[g])*a)).toColorPart()
}):d.originalValue+Math.round(((d.targetValue-d.originalValue)*a)*1000)/1000+d.unit;
b[d.style]=c
});
this.element.setStyle(b)
}});
Effect.Transform=Class.create();
Object.extend(Effect.Transform.prototype,{initialize:function(a){this.tracks=[];
this.options=arguments[1]||{};
this.addTracks(a)
},addTracks:function(a){a.each(function(b){var c=$H(b).values().first();
this.tracks.push($H({ids:$H(b).keys().first(),effect:Effect.Morph,options:{style:c}}))
}.bind(this));
return this
},play:function(){return new Effect.Parallel(this.tracks.map(function(a){var b=[$(a.ids)||$$(a.ids)].flatten();
return b.map(function(c){return new a.effect(c,Object.extend({sync:true},a.options))
})
}).flatten(),this.options)
}});
Element.CSS_PROPERTIES=["azimuth","backgroundAttachment","backgroundColor","backgroundImage","backgroundPosition","backgroundRepeat","borderBottomColor","borderBottomStyle","borderBottomWidth","borderCollapse","borderLeftColor","borderLeftStyle","borderLeftWidth","borderRightColor","borderRightStyle","borderRightWidth","borderSpacing","borderTopColor","borderTopStyle","borderTopWidth","bottom","captionSide","clear","clip","color","content","counterIncrement","counterReset","cssFloat","cueAfter","cueBefore","cursor","direction","display","elevation","emptyCells","fontFamily","fontSize","fontSizeAdjust","fontStretch","fontStyle","fontVariant","fontWeight","height","left","letterSpacing","lineHeight","listStyleImage","listStylePosition","listStyleType","marginBottom","marginLeft","marginRight","marginTop","markerOffset","marks","maxHeight","maxWidth","minHeight","minWidth","opacity","orphans","outlineColor","outlineOffset","outlineStyle","outlineWidth","overflowX","overflowY","paddingBottom","paddingLeft","paddingRight","paddingTop","page","pageBreakAfter","pageBreakBefore","pageBreakInside","pauseAfter","pauseBefore","pitch","pitchRange","position","quotes","richness","right","size","speakHeader","speakNumeral","speakPunctuation","speechRate","stress","tableLayout","textAlign","textDecoration","textIndent","textShadow","textTransform","top","unicodeBidi","verticalAlign","visibility","voiceFamily","volume","whiteSpace","widows","width","wordSpacing","zIndex"];
Element.CSS_LENGTH=/^(([\+\-]?[0-9\.]+)(em|ex|px|in|cm|mm|pt|pc|\%))|0$/;
String.prototype.parseStyle=function(){var b=Element.extend(document.createElement("div"));
b.innerHTML='<div style="'+this+'"></div>';
var c=b.down().style,a=$H();
Element.CSS_PROPERTIES.each(function(d){if(c[d]){a[d]=c[d]
}});
if(/MSIE/.test(navigator.userAgent)&&!window.opera&&this.indexOf("opacity")>-1){a.opacity=this.match(/opacity:\s*((?:0|1)?(?:\.\d*)?)/)[1]
}return a
};
Element.morph=function(a,b){new Effect.Morph(a,Object.extend({style:b},arguments[2]||{}));
return a
};
["setOpacity","getOpacity","getInlineOpacity","forceRerendering","setContentZoom","collectTextNodes","collectTextNodesIgnoreClass","morph"].each(function(a){Element.Methods[a]=Element[a]
});
Element.Methods.visualEffect=function(b,c,a){s=c.gsub(/_/,"-").camelize();
effect_class=s.charAt(0).toUpperCase()+s.substring(1);
new Effect[effect_class](b,a);
return $(b)
};
Element.addMethods();