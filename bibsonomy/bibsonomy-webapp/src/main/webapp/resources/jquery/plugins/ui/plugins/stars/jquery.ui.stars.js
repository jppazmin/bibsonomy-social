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