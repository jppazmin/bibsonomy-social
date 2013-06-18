(function(a){a.fn.relatedSelects=function(c){c=a.extend({},a.fn.relatedSelects.defaults,c);
return this.each(function(){new b(this,c)
})
};
var b=function(f,c){var l=a(f),m=[];
if(a.isArray(c.selects)){d()
}a.each(c.selects,function(){m.push(key)
});
j();
a.each(c.selects,function(s,t){var q=l.find("select[name='"+s+"']"),p=i(s),r=q.val();
t=a.extend({defaultOptionText:c.defaultOptionText||q.data("defaultOption")},c,t);
q.data("defaultOption",t.defaultOptionText);
q.change(function(){t.onChange.call(q);
e(q,p,s,t)
});
if(r&&r.length>0&&h(p)){return
}e(q,p,s,t)
});
function j(){var q,r;
for(var p=1,o=m.length;
p<o;
p++){q=l.find("select[name='"+m[p]+"']");
r=q.find("option[value='']").text();
q.data("defaultOption",r)
}}function e(q,p,r,t){if(!p.length){return
}var s=a.trim(q.val());
if(s.length>0&&s!==t.loadingMessage&&p){g(r);
k(q,p,t)
}else{if(p){g(r)
}}}function k(t,r,v){var s=[],u;
for(var q=0,p=m.length;
q<p;
q++){s.push('select[name="'+m[q]+'"]')
}u=a(s.join(","),l).serialize();
r.attr("disabled","disabled").html('<option value="">'+v.loadingMessage+"</option>");
a.ajax({beforeSend:function(){v.onLoadingStart.call(r)
},complete:function(){v.onLoadingEnd.call(r)
},dataType:v.dataType,data:u,url:v.onChangeLoad,success:function(w){var o=[],x=r.data("defaultOption");
if(x.length>0){o.push('<option value="" selected="selected">'+x+"</option>")
}if(v.dataType==="json"&&typeof w==="object"&&w){a.each(w,function(y,z){o.push('<option value="'+y+'">'+z+"</option>")
});
r.html(o.join("")).removeAttr("disabled")
}else{if(v.dataType==="html"&&a.trim(w).length>0){o.push(a.trim(w));
r.html(o.join("")).removeAttr("disabled")
}else{r.html(o.join(""));
if(!v.disableIfEmpty){r.removeAttr("disabled")
}v.onEmptyResult.call(t)
}}}})
}function h(p){var o=p.find("option");
return(o.length===0||(o.length===1&&o.filter(":first").attr("value").length===0))?false:true
}function g(r){var q=n(r);
for(var p=q+1,o=m.length;
p<o;
p++){a("select[name='"+m[p]+"']",l).attr("disabled","disabled").find("option:first").attr("selected","selected")
}}function i(o){return l.find("select[name='"+m[n(o)+1]+"']")
}function n(q){for(var p=0,o=m.length;
p<o;
p++){if(m[p]===q){return p
}}}function d(){var q=c.selects;
c.selects={};
for(var p=0,o=q.length;
p<o;
p++){c.selects[q[p]]={}
}}};
a.fn.relatedSelects.defaults={selects:{},loadingMessage:"Loading, please wait...",disableIfEmpty:false,dataType:"json",onChangeLoad:"",onLoadingStart:function(){},onLoadingEnd:function(){},onChange:function(){},onEmptyResult:function(){}}
})(jQuery);