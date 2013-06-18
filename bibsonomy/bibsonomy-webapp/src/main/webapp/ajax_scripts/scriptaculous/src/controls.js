if(typeof Effect=="undefined"){throw ("controls.js requires including script.aculo.us' effects.js library")
}var Autocompleter={};
Autocompleter.Base=function(){};
Autocompleter.Base.prototype={baseInitialize:function(b,c,a){this.element=$(b);
this.update=$(c);
this.hasFocus=false;
this.changed=false;
this.active=false;
this.index=0;
this.entryCount=0;
if(this.setOptions){this.setOptions(a)
}else{this.options=a||{}
}this.options.paramName=this.options.paramName||this.element.name;
this.options.tokens=this.options.tokens||[];
this.options.frequency=this.options.frequency||0.4;
this.options.minChars=this.options.minChars||1;
this.options.onShow=this.options.onShow||function(d,e){if(!e.style.position||e.style.position=="absolute"){e.style.position="absolute";
Position.clone(d,e,{setHeight:false,offsetTop:d.offsetHeight})
}Effect.Appear(e,{duration:0.15})
};
this.options.onHide=this.options.onHide||function(d,e){new Effect.Fade(e,{duration:0.15})
};
if(typeof(this.options.tokens)=="string"){this.options.tokens=new Array(this.options.tokens)
}this.observer=null;
this.element.setAttribute("autocomplete","off");
Element.hide(this.update);
Event.observe(this.element,"blur",this.onBlur.bindAsEventListener(this));
Event.observe(this.element,"keypress",this.onKeyPress.bindAsEventListener(this))
},show:function(){if(Element.getStyle(this.update,"display")=="none"){this.options.onShow(this.element,this.update)
}if(!this.iefix&&(navigator.appVersion.indexOf("MSIE")>0)&&(navigator.userAgent.indexOf("Opera")<0)&&(Element.getStyle(this.update,"position")=="absolute")){new Insertion.After(this.update,'<iframe id="'+this.update.id+'_iefix" '+'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" '+'src="javascript:false;" frameborder="0" scrolling="no"></iframe>');
this.iefix=$(this.update.id+"_iefix")
}if(this.iefix){setTimeout(this.fixIEOverlapping.bind(this),50)
}},fixIEOverlapping:function(){Position.clone(this.update,this.iefix,{setTop:(!this.update.style.height)});
this.iefix.style.zIndex=1;
this.update.style.zIndex=2;
Element.show(this.iefix)
},hide:function(){this.stopIndicator();
if(Element.getStyle(this.update,"display")!="none"){this.options.onHide(this.element,this.update)
}if(this.iefix){Element.hide(this.iefix)
}},startIndicator:function(){if(this.options.indicator){Element.show(this.options.indicator)
}},stopIndicator:function(){if(this.options.indicator){Element.hide(this.options.indicator)
}},onKeyPress:function(a){if(this.active){switch(a.keyCode){case Event.KEY_TAB:case Event.KEY_RETURN:this.selectEntry();
Event.stop(a);
case Event.KEY_ESC:this.hide();
this.active=false;
Event.stop(a);
return;
case Event.KEY_LEFT:case Event.KEY_RIGHT:return;
case Event.KEY_UP:this.markPrevious();
this.render();
if(navigator.appVersion.indexOf("AppleWebKit")>0){Event.stop(a)
}return;
case Event.KEY_DOWN:this.markNext();
this.render();
if(navigator.appVersion.indexOf("AppleWebKit")>0){Event.stop(a)
}return
}}else{if(a.keyCode==Event.KEY_TAB||a.keyCode==Event.KEY_RETURN||(navigator.appVersion.indexOf("AppleWebKit")>0&&a.keyCode==0)){return
}}this.changed=true;
this.hasFocus=true;
if(this.observer){clearTimeout(this.observer)
}this.observer=setTimeout(this.onObserverEvent.bind(this),this.options.frequency*1000)
},activate:function(){this.changed=false;
this.hasFocus=true;
this.getUpdatedChoices()
},onHover:function(b){var a=Event.findElement(b,"LI");
if(this.index!=a.autocompleteIndex){this.index=a.autocompleteIndex;
this.render()
}Event.stop(b)
},onClick:function(b){var a=Event.findElement(b,"LI");
this.index=a.autocompleteIndex;
this.selectEntry();
this.hide()
},onBlur:function(a){setTimeout(this.hide.bind(this),250);
this.hasFocus=false;
this.active=false
},render:function(){if(this.entryCount>0){for(var a=0;
a<this.entryCount;
a++){this.index==a?Element.addClassName(this.getEntry(a),"selected"):Element.removeClassName(this.getEntry(a),"selected")
}if(this.hasFocus){this.show();
this.active=true
}}else{this.active=false;
this.hide()
}},markPrevious:function(){if(this.index>0){this.index--
}else{this.index=this.entryCount-1
}this.getEntry(this.index).scrollIntoView(true)
},markNext:function(){if(this.index<this.entryCount-1){this.index++
}else{this.index=0
}this.getEntry(this.index).scrollIntoView(false)
},getEntry:function(a){return this.update.firstChild.childNodes[a]
},getCurrentEntry:function(){return this.getEntry(this.index)
},selectEntry:function(){this.active=false;
this.updateElement(this.getCurrentEntry())
},updateElement:function(f){if(this.options.updateElement){this.options.updateElement(f);
return
}var c="";
if(this.options.select){var a=document.getElementsByClassName(this.options.select,f)||[];
if(a.length>0){c=Element.collectTextNodes(a[0],this.options.select)
}}else{c=Element.collectTextNodesIgnoreClass(f,"informal")
}var e=this.findLastToken();
if(e!=-1){var d=this.element.value.substr(0,e+1);
var b=this.element.value.substr(e+1).match(/^\s+/);
if(b){d+=b[0]
}this.element.value=d+c
}else{this.element.value=c
}this.element.focus();
if(this.options.afterUpdateElement){this.options.afterUpdateElement(this.element,f)
}},updateChoices:function(c){if(!this.changed&&this.hasFocus){this.update.innerHTML=c;
Element.cleanWhitespace(this.update);
Element.cleanWhitespace(this.update.down());
if(this.update.firstChild&&this.update.down().childNodes){this.entryCount=this.update.down().childNodes.length;
for(var a=0;
a<this.entryCount;
a++){var b=this.getEntry(a);
b.autocompleteIndex=a;
this.addObservers(b)
}}else{this.entryCount=0
}this.stopIndicator();
this.index=0;
if(this.entryCount==1&&this.options.autoSelect){this.selectEntry();
this.hide()
}else{this.render()
}}},addObservers:function(a){Event.observe(a,"mouseover",this.onHover.bindAsEventListener(this));
Event.observe(a,"click",this.onClick.bindAsEventListener(this))
},onObserverEvent:function(){this.changed=false;
if(this.getToken().length>=this.options.minChars){this.startIndicator();
this.getUpdatedChoices()
}else{this.active=false;
this.hide()
}},getToken:function(){var b=this.findLastToken();
if(b!=-1){var a=this.element.value.substr(b+1).replace(/^\s+/,"").replace(/\s+$/,"")
}else{var a=this.element.value
}return/\n/.test(a)?"":a
},findLastToken:function(){var c=-1;
for(var b=0;
b<this.options.tokens.length;
b++){var a=this.element.value.lastIndexOf(this.options.tokens[b]);
if(a>c){c=a
}}return c
}};
Ajax.Autocompleter=Class.create();
Object.extend(Object.extend(Ajax.Autocompleter.prototype,Autocompleter.Base.prototype),{initialize:function(c,d,b,a){this.baseInitialize(c,d,a);
this.options.asynchronous=true;
this.options.onComplete=this.onComplete.bind(this);
this.options.defaultParams=this.options.parameters||null;
this.url=b
},getUpdatedChoices:function(){entry=encodeURIComponent(this.options.paramName)+"="+encodeURIComponent(this.getToken());
this.options.parameters=this.options.callback?this.options.callback(this.element,entry):entry;
if(this.options.defaultParams){this.options.parameters+="&"+this.options.defaultParams
}new Ajax.Request(this.url,this.options)
},onComplete:function(a){this.updateChoices(a.responseText)
}});
Autocompleter.Local=Class.create();
Autocompleter.Local.prototype=Object.extend(new Autocompleter.Base(),{initialize:function(b,d,c,a){this.baseInitialize(b,d,a);
this.options.array=c
},getUpdatedChoices:function(){this.updateChoices(this.options.selector(this))
},setOptions:function(a){this.options=Object.extend({choices:10,partialSearch:true,partialChars:2,ignoreCase:true,fullSearch:false,selector:function(b){var d=[];
var c=[];
var h=b.getToken();
var g=0;
for(var e=0;
e<b.options.array.length&&d.length<b.options.choices;
e++){var f=b.options.array[e];
var j=b.options.ignoreCase?f.toLowerCase().indexOf(h.toLowerCase()):f.indexOf(h);
while(j!=-1){if(j==0&&f.length!=h.length){d.push("<li><strong>"+f.substr(0,h.length)+"</strong>"+f.substr(h.length)+"</li>");
break
}else{if(h.length>=b.options.partialChars&&b.options.partialSearch&&j!=-1){if(b.options.fullSearch||/\s/.test(f.substr(j-1,1))){c.push("<li>"+f.substr(0,j)+"<strong>"+f.substr(j,h.length)+"</strong>"+f.substr(j+h.length)+"</li>");
break
}}}j=b.options.ignoreCase?f.toLowerCase().indexOf(h.toLowerCase(),j+1):f.indexOf(h,j+1)
}}if(c.length){d=d.concat(c.slice(0,b.options.choices-d.length))
}return"<ul>"+d.join("")+"</ul>"
}},a||{})
}});
Field.scrollFreeActivate=function(a){setTimeout(function(){Field.activate(a)
},1)
};
Ajax.InPlaceEditor=Class.create();
Ajax.InPlaceEditor.defaultHighlightColor="#FFFF99";
Ajax.InPlaceEditor.prototype={initialize:function(c,b,a){this.url=b;
this.element=$(c);
this.options=Object.extend({paramName:"value",okButton:true,okText:"ok",cancelLink:true,cancelText:"cancel",savingText:"Saving...",clickToEditText:"Click to edit",okText:"ok",rows:1,onComplete:function(e,d){new Effect.Highlight(d,{startcolor:this.options.highlightcolor})
},onFailure:function(d){alert("Error communicating with the server: "+d.responseText.stripTags())
},callback:function(d){return Form.serialize(d)
},handleLineBreaks:true,loadingText:"Loading...",savingClassName:"inplaceeditor-saving",loadingClassName:"inplaceeditor-loading",formClassName:"inplaceeditor-form",highlightcolor:Ajax.InPlaceEditor.defaultHighlightColor,highlightendcolor:"#FFFFFF",externalControl:null,submitOnBlur:false,ajaxOptions:{},evalScripts:false},a||{});
if(!this.options.formId&&this.element.id){this.options.formId=this.element.id+"-inplaceeditor";
if($(this.options.formId)){this.options.formId=null
}}if(this.options.externalControl){this.options.externalControl=$(this.options.externalControl)
}this.originalBackground=Element.getStyle(this.element,"background-color");
if(!this.originalBackground){this.originalBackground="transparent"
}this.element.title=this.options.clickToEditText;
this.onclickListener=this.enterEditMode.bindAsEventListener(this);
this.mouseoverListener=this.enterHover.bindAsEventListener(this);
this.mouseoutListener=this.leaveHover.bindAsEventListener(this);
Event.observe(this.element,"click",this.onclickListener);
Event.observe(this.element,"mouseover",this.mouseoverListener);
Event.observe(this.element,"mouseout",this.mouseoutListener);
if(this.options.externalControl){Event.observe(this.options.externalControl,"click",this.onclickListener);
Event.observe(this.options.externalControl,"mouseover",this.mouseoverListener);
Event.observe(this.options.externalControl,"mouseout",this.mouseoutListener)
}},enterEditMode:function(a){if(this.saving){return
}if(this.editing){return
}this.editing=true;
this.onEnterEditMode();
if(this.options.externalControl){Element.hide(this.options.externalControl)
}Element.hide(this.element);
this.createForm();
this.element.parentNode.insertBefore(this.form,this.element);
if(!this.options.loadTextURL){Field.scrollFreeActivate(this.editField)
}if(a){Event.stop(a)
}return false
},createForm:function(){this.form=document.createElement("form");
this.form.id=this.options.formId;
Element.addClassName(this.form,this.options.formClassName);
this.form.onsubmit=this.onSubmit.bind(this);
this.createEditField();
if(this.options.textarea){var a=document.createElement("br");
this.form.appendChild(a)
}if(this.options.okButton){okButton=document.createElement("input");
okButton.type="submit";
okButton.value=this.options.okText;
okButton.className="editor_ok_button";
this.form.appendChild(okButton)
}if(this.options.cancelLink){cancelLink=document.createElement("a");
cancelLink.href="#";
cancelLink.appendChild(document.createTextNode(this.options.cancelText));
cancelLink.onclick=this.onclickCancel.bind(this);
cancelLink.className="editor_cancel";
this.form.appendChild(cancelLink)
}},hasHTMLLineBreaks:function(a){if(!this.options.handleLineBreaks){return false
}return a.match(/<br/i)||a.match(/<p>/i)
},convertHTMLLineBreaks:function(a){return a.replace(/<br>/gi,"\n").replace(/<br\/>/gi,"\n").replace(/<\/p>/gi,"\n").replace(/<p>/gi,"")
},createEditField:function(){var e;
if(this.options.loadTextURL){e=this.options.loadingText
}else{e=this.getText()
}var c=this;
if(this.options.rows==1&&!this.hasHTMLLineBreaks(e)){this.options.textarea=false;
var a=document.createElement("input");
a.obj=this;
a.type="text";
a.name=this.options.paramName;
a.value=e;
a.style.backgroundColor=this.options.highlightcolor;
a.className="editor_field";
var b=this.options.size||this.options.cols||0;
if(b!=0){a.size=b
}if(this.options.submitOnBlur){a.onblur=this.onSubmit.bind(this)
}this.editField=a
}else{this.options.textarea=true;
var d=document.createElement("textarea");
d.obj=this;
d.name=this.options.paramName;
d.value=this.convertHTMLLineBreaks(e);
d.rows=this.options.rows;
d.cols=this.options.cols||40;
d.className="editor_field";
if(this.options.submitOnBlur){d.onblur=this.onSubmit.bind(this)
}this.editField=d
}if(this.options.loadTextURL){this.loadExternalText()
}this.form.appendChild(this.editField)
},getText:function(){return this.element.innerHTML
},loadExternalText:function(){Element.addClassName(this.form,this.options.loadingClassName);
this.editField.disabled=true;
new Ajax.Request(this.options.loadTextURL,Object.extend({asynchronous:true,onComplete:this.onLoadedExternalText.bind(this)},this.options.ajaxOptions))
},onLoadedExternalText:function(a){Element.removeClassName(this.form,this.options.loadingClassName);
this.editField.disabled=false;
this.editField.value=a.responseText.stripTags();
Field.scrollFreeActivate(this.editField)
},onclickCancel:function(){this.onComplete();
this.leaveEditMode();
return false
},onFailure:function(a){this.options.onFailure(a);
if(this.oldInnerHTML){this.element.innerHTML=this.oldInnerHTML;
this.oldInnerHTML=null
}return false
},onSubmit:function(){var a=this.form;
var b=this.editField.value;
this.onLoading();
if(this.options.evalScripts){new Ajax.Request(this.url,Object.extend({parameters:this.options.callback(a,b),onComplete:this.onComplete.bind(this),onFailure:this.onFailure.bind(this),asynchronous:true,evalScripts:true},this.options.ajaxOptions))
}else{new Ajax.Updater({success:this.element,failure:null},this.url,Object.extend({parameters:this.options.callback(a,b),onComplete:this.onComplete.bind(this),onFailure:this.onFailure.bind(this)},this.options.ajaxOptions))
}if(arguments.length>1){Event.stop(arguments[0])
}return false
},onLoading:function(){this.saving=true;
this.removeForm();
this.leaveHover();
this.showSaving()
},showSaving:function(){this.oldInnerHTML=this.element.innerHTML;
this.element.innerHTML=this.options.savingText;
Element.addClassName(this.element,this.options.savingClassName);
this.element.style.backgroundColor=this.originalBackground;
Element.show(this.element)
},removeForm:function(){if(this.form){if(this.form.parentNode){Element.remove(this.form)
}this.form=null
}},enterHover:function(){if(this.saving){return
}this.element.style.backgroundColor=this.options.highlightcolor;
if(this.effect){this.effect.cancel()
}Element.addClassName(this.element,this.options.hoverClassName)
},leaveHover:function(){if(this.options.backgroundColor){this.element.style.backgroundColor=this.oldBackground
}Element.removeClassName(this.element,this.options.hoverClassName);
if(this.saving){return
}this.effect=new Effect.Highlight(this.element,{startcolor:this.options.highlightcolor,endcolor:this.options.highlightendcolor,restorecolor:this.originalBackground})
},leaveEditMode:function(){Element.removeClassName(this.element,this.options.savingClassName);
this.removeForm();
this.leaveHover();
this.element.style.backgroundColor=this.originalBackground;
Element.show(this.element);
if(this.options.externalControl){Element.show(this.options.externalControl)
}this.editing=false;
this.saving=false;
this.oldInnerHTML=null;
this.onLeaveEditMode()
},onComplete:function(a){this.leaveEditMode();
this.options.onComplete.bind(this)(a,this.element)
},onEnterEditMode:function(){},onLeaveEditMode:function(){},dispose:function(){if(this.oldInnerHTML){this.element.innerHTML=this.oldInnerHTML
}this.leaveEditMode();
Event.stopObserving(this.element,"click",this.onclickListener);
Event.stopObserving(this.element,"mouseover",this.mouseoverListener);
Event.stopObserving(this.element,"mouseout",this.mouseoutListener);
if(this.options.externalControl){Event.stopObserving(this.options.externalControl,"click",this.onclickListener);
Event.stopObserving(this.options.externalControl,"mouseover",this.mouseoverListener);
Event.stopObserving(this.options.externalControl,"mouseout",this.mouseoutListener)
}}};
Ajax.InPlaceCollectionEditor=Class.create();
Object.extend(Ajax.InPlaceCollectionEditor.prototype,Ajax.InPlaceEditor.prototype);
Object.extend(Ajax.InPlaceCollectionEditor.prototype,{createEditField:function(){if(!this.cached_selectTag){var a=document.createElement("select");
var c=this.options.collection||[];
var b;
c.each(function(f,d){b=document.createElement("option");
b.value=(f instanceof Array)?f[0]:f;
if((typeof this.options.value=="undefined")&&((f instanceof Array)?this.element.innerHTML==f[1]:f==b.value)){b.selected=true
}if(this.options.value==b.value){b.selected=true
}b.appendChild(document.createTextNode((f instanceof Array)?f[1]:f));
a.appendChild(b)
}.bind(this));
this.cached_selectTag=a
}this.editField=this.cached_selectTag;
if(this.options.loadTextURL){this.loadExternalText()
}this.form.appendChild(this.editField);
this.options.callback=function(d,e){return"value="+encodeURIComponent(e)
}
}});
Form.Element.DelayedObserver=Class.create();
Form.Element.DelayedObserver.prototype={initialize:function(b,a,c){this.delay=a||0.5;
this.element=$(b);
this.callback=c;
this.timer=null;
this.lastValue=$F(this.element);
Event.observe(this.element,"keyup",this.delayedListener.bindAsEventListener(this))
},delayedListener:function(a){if(this.lastValue==$F(this.element)){return
}if(this.timer){clearTimeout(this.timer)
}this.timer=setTimeout(this.onTimerEvent.bind(this),this.delay*1000);
this.lastValue=$F(this.element)
},onTimerEvent:function(){this.timer=null;
this.callback(this.element,$F(this.element))
}};